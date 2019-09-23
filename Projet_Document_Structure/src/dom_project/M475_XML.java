package dom_project;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
public class M475_XML {
	public static void M457Traitement(File file) throws ParserConfigurationException, FileNotFoundException, UnsupportedEncodingException, SAXException, IOException, TransformerConfigurationException, TransformerException{
        DocumentBuilderFactory factorySrc = DocumentBuilderFactory.newInstance();
        DocumentBuilder parserSrc = factorySrc.newDocumentBuilder();
        Document docSrc = parserSrc.parse(file);
        Element liste = docSrc.getDocumentElement();
        
        NodeList ndlSrc = liste.getElementsByTagName("p");
        
        DocumentBuilderFactory factoryDestination = DocumentBuilderFactory.newInstance();
        DocumentBuilder parserDestination = factoryDestination.newDocumentBuilder();
        DOMImplementation domImp = parserDestination.getDOMImplementation();
        DocumentType dtd = domImp.createDocumentType("Racine", null, "dom.dtd");
        
        Document docDestination = domImp.createDocument(null, "TEI_S", dtd);
        docDestination.setXmlStandalone(true);
        
        Element racDest = docDestination.getDocumentElement();
        
        Element M457_xml = docDestination.createElement("M457.xml");
        racDest.appendChild(M457_xml);
        NodeList buffNdl;
        for(int i=0;i<ndlSrc.getLength();i++){
            buffNdl=ndlSrc.item(i).getChildNodes();
            for(int j=0;j<buffNdl.getLength();j++){
                if("#text".equals(buffNdl.item(j).getNodeName())){
                    Element text = docDestination.createElement("texte");
                    if(!"".equals(buffNdl.item(j).getNodeValue().replaceAll("(\\t|\\n)", ""))){
                        text.appendChild(docDestination.createTextNode(buffNdl.item(j).getNodeValue().replaceAll("(\\t|\\n)", "")));
                        M457_xml.appendChild(text);
                    }
                }
            }
        }
        
        DOMSource domDest = new DOMSource(docDestination);
        StreamResult res = new StreamResult(new File("Result/sortie2.xml"));
        
        TransformerFactory transFactory  = TransformerFactory.newInstance();
        Transformer trans = transFactory.newTransformer();
        
        trans.setOutputProperty(OutputKeys.INDENT, "yes");
        trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        trans.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "dom.dtd");
        
        trans.transform(domDest, res);
    }
}
