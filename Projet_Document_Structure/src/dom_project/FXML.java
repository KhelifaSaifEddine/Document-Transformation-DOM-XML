package dom_project;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
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
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
public class FXML {
	private static File dossierResultat=new File("Resultat");
	public static void FXMLTraitement(File file) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException{
        DocumentBuilderFactory factorySrc = DocumentBuilderFactory.newInstance();
       
        DocumentBuilder parserSrc = factorySrc.newDocumentBuilder();
        parserSrc.setEntityResolver(new EntityResolver() {
			@Override
			public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
				if (systemId.contains(".dtd")) {
					return new InputSource(new StringReader(""));
				} else {
					return null;
				}
			}
		});
        Document docSrc = parserSrc.parse(file);
        Element liste = docSrc.getDocumentElement();
        
        DocumentBuilderFactory factoryDes = DocumentBuilderFactory.newInstance();
        DocumentBuilder parserDes = factoryDes.newDocumentBuilder();
        DOMImplementation domImp = parserDes.getDOMImplementation();
        
        Document documentDestination = domImp.createDocument(null, "Racine", null);
        documentDestination.setXmlStandalone(true);
        Element racDest = documentDestination.getDocumentElement();
        racDest.setAttribute("xmlns:fx", "http://javafx.com/fxml");
        
        NodeList neoudList = liste.getChildNodes();
        
        ArrayList<String> donnee = new ArrayList();
        
        NamedNodeMap attrs = liste.getAttributes();
        
        if(attrs !=null && attrs.getLength()>0){
            for(int i=0;i<attrs.getLength();i++){
                Attr attr = (Attr) attrs.item(i);
                donnee.add(attr.getName());
                donnee.add(attr.getValue());
            }
        }
        
        donnee.addAll(DonneeAttribut(neoudList));
        
        for(int i=0;i<donnee.size();i=i+2){
            Element text = documentDestination.createElement("texte");
            String name = donnee.get(i);
            String value =  donnee.get(i+1);
            text.setAttribute(name, "x");
            text.appendChild(documentDestination.createTextNode(value));
            racDest.appendChild(text);
        }
        
        DOMSource domDest = new DOMSource(documentDestination);
        StreamResult res = new StreamResult(new File("Result/javafx.xml"));
        
        TransformerFactory transFactory  = TransformerFactory.newInstance();
        Transformer trans = transFactory.newTransformer();
        
        trans.setOutputProperty(OutputKeys.INDENT, "yes");
        trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "8");
        trans.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
        
        trans.transform(domDest, res);
        
    }
    
    private static ArrayList<String> DonneeAttribut(NodeList neoudList){
        ArrayList<String> donnee = new ArrayList();
        if(neoudList != null){
            for(int i=0;i<neoudList.getLength();i++){
                Node nd = neoudList.item(i);
                NamedNodeMap attrs = nd.getAttributes();
                if(attrs != null){
                    for(int o=0;o<attrs.getLength();o++){
                        Attr attr = (Attr) attrs.item(o);
                        donnee.add(attr.getName());
                        donnee.add(attr.getValue());
                    }
                }
                donnee.addAll(DonneeAttribut(neoudList.item(i).getChildNodes()));
            }
            return donnee;
        }else{
            return null;
        }
    }
    
}
