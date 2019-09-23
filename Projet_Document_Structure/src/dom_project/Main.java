package dom_project;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;



public class Main {
static String s="";
static String se="";
static String fer = "";


public static int MyDevloping = 0;
public static Node nd = null;
	static DocumentBuilder parseur;
	static Document document;
	private static DocumentBuilder builder;
	private static Document Sortie;
	private static File OUTPUT=new File("Resultat");
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, TransformerException {
		
		
	RENAULT("C:\\Users\\Administrator\\Desktop\\hana\\renault.html");
	    
}
	public static void RENAULT(String path) throws ParserConfigurationException, TransformerFactoryConfigurationError, IOException, SAXException, TransformerException {
	
		
		DocumentBuilderFactory INIT_Builder = DocumentBuilderFactory.newInstance();
	    builder = INIT_Builder.newDocumentBuilder();
		builder.setEntityResolver(new EntityResolver() {
	        @Override
	        public InputSource resolveEntity(String publicId, String systemId)
                throws SAXException, IOException {
                return new InputSource(new StringReader(""));
	        }
	    });
		OUTPUT.mkdirs();
		Transformer tr = TransformerFactory.newInstance().newTransformer();
		tr.setOutputProperty(OutputKeys.INDENT,"yes");
	
		Reader MYREADER = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
		BufferedReader fin = new BufferedReader(MYREADER);
		String contents="",s;
		while ((s=fin.readLine())!=null) contents+=s+"\n";
		fin.close();
		contents=contents.replace("&nbsp;","&#160;");
		File f=new File(path);
		f.delete();
		BufferedWriter outs=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF8"));
		outs.write(contents);
		outs.flush();
		outs.close();
		Document Entree = builder.parse(new File(path));
		Sortie = builder.getDOMImplementation().createDocument(null,"Concessionnaires", null);
		Sortie.setXmlStandalone(true);
		RENAULT_GO(Entree.getDocumentElement());
		DOMSource domSource = new DOMSource(Sortie);
		tr.transform(domSource, new StreamResult(new File(OUTPUT + "\\" + "renault.xml")));
		MYREADER = new BufferedReader(new InputStreamReader(new FileInputStream(OUTPUT + "\\" + "renault.xml"), "UTF-8"));
		fin = new BufferedReader(MYREADER);
		contents="";
		while ((s=fin.readLine())!=null) contents+=s+"\n";
		fin.close();
		f=new File(OUTPUT + "\\" + "renault.xml");
		f.delete();
		outs=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OUTPUT + "\\" + "renault.xml"), "UTF8"));
		outs.write((char)+0xfeff+contents.replace("><", ">\n<"));
		outs.flush();
		outs.close();
	}
	private static void RENAULT_GO(Node node) {
		if (node.getNodeName().equals("#text")&&!node.getNodeValue().matches("[\\s:]*")) {
			if(node.getParentNode().getNodeName().equals("strong")) {
				if(MyDevloping==0) {
					nd = Sortie.createElement("Nom");
					Node txt=Sortie.createTextNode(node.getNodeValue());
					nd.appendChild(txt);
					MyDevloping++;
				}
				else if(MyDevloping==1) {
					if(node.getNodeValue().equals("Adresse")) {
						Sortie.getDocumentElement().appendChild(nd);
						Element element = Sortie.createElement("Adresse");
						Sortie.getDocumentElement().appendChild(element);
						nd=element;
						MyDevloping++;
					} else {
						nd = Sortie.createElement("Nom");
						Node txt=Sortie.createTextNode(node.getNodeValue());
						nd.appendChild(txt);
						MyDevloping=1;
					}
				}
				else if(MyDevloping==3) {
					Element element = Sortie.createElement("Num_téléphone");
					Sortie.getDocumentElement().appendChild(element);
					nd=element;
					MyDevloping++;
				}
			} else {
				if(MyDevloping==2) {
					Node txt=Sortie.createTextNode(node.getNodeValue().replace(": ", "").replace("\n", " ").replaceAll("^ ", ""));
					nd.appendChild(txt);
					MyDevloping++;						
				}
				if(MyDevloping==4) {
					Node txt=Sortie.createTextNode(node.getNodeValue().replace(": ", "").replace("\n", " ").replaceAll("^ ", ""));
					nd.appendChild(txt);
					MyDevloping=0;						
				}
			}
		}
		NodeList nl=node.getChildNodes();
		int length=nl.getLength();
		for(int i=0;i<length;i++)
			RENAULT_GO(nl.item(i));
	}
}
	


	
