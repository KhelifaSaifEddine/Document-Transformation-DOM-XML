package dom_project;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author ryuk
 */
public class Renault {
    public static void RenaultTraitement(File file) throws ParserConfigurationException, IOException, SAXException, TransformerConfigurationException, TransformerException{
        DocumentBuilderFactory factorySrc = DocumentBuilderFactory.newInstance();
        DocumentBuilder parserSrc = factorySrc.newDocumentBuilder();
       
        Document docSrc = parserSrc.parse(file);
        Element liste = docSrc.getDocumentElement();

        NodeList ndlSrc = liste.getElementsByTagName("div");

        DocumentBuilderFactory factoryDestination = DocumentBuilderFactory.newInstance();
        DocumentBuilder parserDestination = factoryDestination.newDocumentBuilder();
        DOMImplementation domImp = parserDestination.getDOMImplementation();
        Document docDestination = domImp.createDocument(null, "Concessionnaires", null);
        docDestination.setXmlStandalone(true);
        Element racDest = docDestination.getDocumentElement();

        int i = 0, indice = 0;
        boolean found = false;
        while (i < ndlSrc.getLength() && !found) {
            if (ndlSrc.item(i).getAttributes() != null) {
                NamedNodeMap atts = ndlSrc.item(i).getAttributes();
                if (atts.getLength() > 0) {
                    if ("class".equals(((Attr) atts.item(0)).getName()) && "post-single".equals(((Attr) atts.item(0)).getValue())) {
                        found = true;
                        indice = i;
                        break;
                    } else {
                        i++;
                    }
                } else {
                    i++;
                }
            } else {
                i++;
            }
        }
        NodeList buffNdl = ndlSrc;
        if (found) {
            buffNdl = ndlSrc.item(indice).getChildNodes();
        }
        ArrayList<String> CorrectData = new ArrayList();
        for (i = 0; i < buffNdl.getLength(); i++) {
            Node nd = buffNdl.item(i);
            if (nd.getNodeName().equals("p")) {
                ArrayList<String> DataStrings = DonneNoeud(nd.getChildNodes());
                int cpt = 0;
                for (String str : DataStrings) {
                    str = str.replaceAll("(\\n|\\t)", "");
                    str = str.replaceAll("(Adresse|TÃ©l|Fax| :|: |:)", "");
                    if (str.startsWith(" ")) {
                        str = str.replaceFirst(" ", "");
                    }
                    if (str.length() >= 3 & !str.equals("") & !str.equals(" ")) {
                        if (cpt < 3) {
                            CorrectData.add(str);
                            cpt++;
                        }
                    }

                }
            }
        }
        int cpt = 0;
        for (String str : CorrectData) {
            Element elem = null;
            switch (cpt) {
                case 0:
                    elem = docDestination.createElement("Nom");
                    cpt++;
                    break;
                case 1:
                    elem = docDestination.createElement("Adresse");
                    cpt++;
                    break;
                case 2:
                    elem = docDestination.createElement("Num_téléphone");
                    cpt = 0;
                    break;
            }
            elem.appendChild(docDestination.createTextNode(str));
            racDest.appendChild(elem);
        }
        
        DOMSource domDestination = new DOMSource(docDestination);
        StreamResult res = new StreamResult(new File("Result/renault.xml"));

        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer trans = transFactory.newTransformer();

        trans.setOutputProperty(OutputKeys.INDENT, "yes");
        trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        trans.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
        trans.transform(domDestination, res);
    }
    
    private static ArrayList<String> DonneNoeud(NodeList ndl) throws UnsupportedEncodingException{
        ArrayList<String> results = new ArrayList();
        if(ndl.getLength()>0){
            for(int i=0;i<ndl.getLength();i++){
                if(Filter(ndl.item(i))){
                    results.addAll(DonneNoeud(ndl.item(i).getChildNodes()));
                }else{
                    if(!"".equals(ndl.item(i).getNodeValue().replaceAll("(\\n)"," ")))
                        results.add(ndl.item(i).getNodeValue().replaceAll("(\\n)"," "));
                }
                
            }
            
        }
        return results;
    }
    
    private static boolean Filter(Node e){
        return (!"#text".equals(e.getNodeName()));
    }
}