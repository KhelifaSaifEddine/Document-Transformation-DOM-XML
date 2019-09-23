package dom_project;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

public class Neruda {
	private static File dossierResultat=new File("Resultat");
    public static void poemeTraitement(File file) throws FileNotFoundException, IOException, ParserConfigurationException, TransformerConfigurationException, TransformerException{
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        
        DocumentBuilderFactory factoryDestination = DocumentBuilderFactory.newInstance();
        DocumentBuilder parserDestination = factoryDestination.newDocumentBuilder();
        DOMImplementation domImp = parserDestination.getDOMImplementation();
        DocumentType dtd = domImp.createDocumentType("Racine", null, "neruda.dtd");
        
        Document docDestination = domImp.createDocument(null, "poema", dtd);
        docDestination.setXmlStandalone(true);
        
        Element racDest = docDestination.getDocumentElement();
        
        Element titre = docDestination.createElement("titulo");
        titre.appendChild(docDestination.createTextNode(br.readLine()));
        racDest.appendChild(titre);
        Element estrofa = null;
        Element verso = null;
        boolean estro = false;
        while((line = br.readLine())!=null){
            if(!line.equals("")){
                if(!estro){
                    estrofa = docDestination.createElement("estrofa");
                    estro=true;
                }
                verso = docDestination.createElement("verso");
                verso.appendChild(docDestination.createTextNode(line));
                estrofa.appendChild(verso);
            }else{
                if(estro){
                    racDest.appendChild(estrofa);
                }
                estro = false;
            }
        }
        
        DOMSource domDestination = new DOMSource(docDestination);
        StreamResult res = new StreamResult(new File("Result/neruda.xml"));
        
        TransformerFactory transFactory  = TransformerFactory.newInstance();
        Transformer trans = transFactory.newTransformer();
        
        trans.setOutputProperty(OutputKeys.INDENT, "yes");
        trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        trans.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "neruda.dtd");
        
        trans.transform(domDestination, res);
    }
}
