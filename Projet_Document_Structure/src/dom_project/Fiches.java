package dom_project;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Fiches {
	private static DocumentBuilder builder;
	private static Document Sortie;
	private static File dossierResultat=new File("Resultat");
	 public static void fichesTraitement(File file) throws IOException, ParserConfigurationException, TransformerConfigurationException, TransformerException{
	        BufferedReader brH1 = new BufferedReader(new FileReader(file));
	        BufferedReader brH2 = new BufferedReader(new FileReader(file));
	        BufferedReader brD1 = new BufferedReader(new FileReader(file));
	        BufferedReader brD2 = new BufferedReader(new FileReader(file));
	        
	        
	        DocumentBuilderFactory factoryDes = DocumentBuilderFactory.newInstance();
	        DocumentBuilder parserDes = factoryDes.newDocumentBuilder();
	        DOMImplementation domImp = parserDes.getDOMImplementation();
	        DocumentType dtd = domImp.createDocumentType("FICHES", null, "fiches.dtd");
	        
	        Document docDest = domImp.createDocument(null, "FICHES", dtd);
	        docDest.setXmlStandalone(true);
	        
	        Element racDest = docDest.getDocumentElement();
	        Node xsl = docDest.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"fiche.xsl\"");
	        
	        ArrayList<String> headers = fiches1EnTete(brH1);
	        ArrayList<ArrayList<String>> donnee = donneeFiches1(brD1);
	        for(int i=1;i<5;i++){
	            Element fiches = docDest.createElement("FICHE");
	            fiches.setAttribute("id", Integer.toString(i));
	            for(int j=0;j<3;j++){
	                String header = headers.get(3*(i-1)+j);
	                String deb = header.substring(0, 2);
	                if(deb.equals("PN")) deb="BE";
	                Element H = docDest.createElement(deb);
	                H.appendChild(docDest.createTextNode(header));
	                fiches.appendChild(H);
	            }
	            Element langue = docDest.createElement("Langue");
	            langue.setAttribute("id", "AR");
	            ArrayList<String> arb = donnee.get(i-1);
	            String first = arb.get(0);
	            String second = arb.get(1);
	            int cpt=0;
	            for(String data : arb){
	                if(data.equals("FR")){
	                    break;
	                }
	                if(!data.equals("AR")){
	                    String bal = data.substring(0, 2);
	                    Element ele = docDest.createElement(bal);
	                    ele.appendChild(docDest.createTextNode(data));
	                    langue.appendChild(ele);
	                }
	                
	            }
	            fiches.appendChild(langue);
	            langue = docDest.createElement("Langue");
	            String deb1 = first.substring(0,2);
	            String deb2 = second.substring(0,2);
	            Element ele1 = docDest.createElement(deb1);
	            Element ele2 = docDest.createElement(deb2);
	            ele1.appendChild(docDest.createTextNode(first));
	            ele2.appendChild(docDest.createTextNode(second));
	            langue.appendChild(ele1);
	            langue.appendChild(ele2);
	            langue.setAttribute("id", "FR");
	            boolean fr=false;
	            for(String data : arb){
	                if(data.equals("FR")){
	                    fr=true;
	                }
	                if(fr && !data.equals("FR")){
	                    String bal = data.substring(0, 2);
	                    Element ele = docDest.createElement(bal);
	                    ele.appendChild(docDest.createTextNode(data));
	                    langue.appendChild(ele);
	                }
	            }
	            fiches.appendChild(langue);
	            racDest.appendChild(fiches);
	        }
	        DOMSource domDest = new DOMSource(docDest);
	        StreamResult res = new StreamResult(new File("Result/fiches1.xml"));
	        
	        TransformerFactory transFactory  = TransformerFactory.newInstance();
	        Transformer trans = transFactory.newTransformer();

	        docDest.insertBefore(xsl, racDest);
	        trans.setOutputProperty(OutputKeys.INDENT, "yes");
	        trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        trans.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "fiches.dtd");
	        trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	        docDest.insertBefore(xsl, racDest);
	        trans.transform(domDest, res);
	        
	        
	        factoryDes = DocumentBuilderFactory.newInstance();
	        parserDes = factoryDes.newDocumentBuilder();
	        domImp = parserDes.getDOMImplementation();
	        
	        docDest = domImp.createDocument(null, "FICHES", null);
	        docDest.setXmlStandalone(true);
	        
	        racDest = docDest.getDocumentElement();
	        headers = fiches2EnTete(brH2);
	        donnee = donneeFiches2(brD2);
	        int cpt=0;
	        for(int i=0;i<4;i++){
	            Element fiches = docDest.createElement("FICHE");
	            fiches.setAttribute("id", Integer.toString(i+1));
	            for(int j=0;j<5;j++){
	                String header = headers.get(5*i+j);
	                String deb = header.substring(0, 2);
	                if(deb.equals("PN")) deb="BE";
	                Element H = docDest.createElement(deb);
	                H.appendChild(docDest.createTextNode(header));
	                fiches.appendChild(H);
	            }
	            Element langue = docDest.createElement("Langue");
	            langue.setAttribute("id", "AR");
	            ArrayList<String> arb = donnee.get(i);
	            cpt=0;
	            for(String data : arb){
	                if(data.equals("FR")){
	                    break;
	                }
	                if(!data.equals("AR")){
	                    String bal = data.substring(0, 2);
	                    Element ele = docDest.createElement(bal);
	                    ele.appendChild(docDest.createTextNode(data));
	                    langue.appendChild(ele);
	                }
	            }
	            fiches.appendChild(langue);
	            langue = docDest.createElement("Langue");
	            langue.setAttribute("id", "FR");
	            boolean fr=false;
	            for(String data : arb){
	                if(data.equals("FR")){
	                    fr=true;
	                }
	                if(fr && !data.equals("FR")){
	                    String bal = data.substring(0, 2);
	                    Element ele = docDest.createElement(bal);
	                    ele.appendChild(docDest.createTextNode(data));
	                    langue.appendChild(ele);
	                }
	            }
	            fiches.appendChild(langue);
	            racDest.appendChild(fiches);
	        }
	        Node xsl1 = docDest.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"fiche.xsl\"");
	        docDest.insertBefore(xsl1, racDest);
	        domDest = new DOMSource(docDest);
	        res = new StreamResult(new File("Result/fiches2.xml"));
	        
	        transFactory  = TransformerFactory.newInstance();
	        trans = transFactory.newTransformer();
	        
	        trans.setOutputProperty(OutputKeys.INDENT, "yes");
	        trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	        trans.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
	        
	        trans.transform(domDest, res);
	    }
	    
	    private static ArrayList<String> fiches1EnTete(BufferedReader br) throws IOException{
	        ArrayList<String> header = new ArrayList();
	        String line;
	        
	        line=br.readLine();
	        
	        for(int i=0;i<4;i++){
	            String deb = line.substring(line.length()-2,line.length());
	            line=line.substring(0, line.length()-2);
	            if(!deb.equals("BE")){
	                line=deb.concat(" : ".concat(line));
	            }
	            header.add(line);

	            line=br.readLine();
	            deb = line.substring(line.length()-2,line.length());
	            line=line.substring(0, line.length()-2);
	            line=deb.concat(" : ".concat(line));

	            header.add(line);
	            br.readLine();
	            br.readLine();

	            line=br.readLine();
	            deb = line.substring(line.length()-2,line.length());
	            line=line.substring(0, line.length()-2);
	            line=deb.concat(" : ".concat(line));

	            header.add(line);

	            while(!line.equals("")){
	                line=br.readLine();
	            }

	            while(line.equals("")){
	                line=br.readLine();
	            }
	        }
	        
	        return header;
	    }
	    
	    private static ArrayList<String> fiches2EnTete(BufferedReader br) throws IOException{
	        ArrayList<String> header = new ArrayList();
	        String line;
	        
	        line=br.readLine();
	        String deb;
	        
	        for(int i=0;i<4;i++){
	            
	            line=line.substring(0, line.length()-2);

	            header.add(line);

	            line=br.readLine();
	            deb = line.substring(line.length()-2,line.length());
	            line=line.substring(0, line.length()-2);
	            line = deb.concat(" : ".concat(line));

	            header.add(line);

	            line=br.readLine();
	            deb = line.substring(line.length()-2,line.length());
	            line=line.substring(0, line.length()-2);
	            line = deb.concat(" : ".concat(line));

	            header.add(line);

	            line=br.readLine();
	            deb = line.substring(line.length()-2,line.length());
	            line=line.substring(0, line.length()-2);
	            line = deb.concat(" : ".concat(line));

	            header.add(line);

	            line=br.readLine();
	            deb = line.substring(line.length()-2,line.length());
	            line=line.substring(0, line.length()-2);
	            line = deb.concat(" : ".concat(line));

	            header.add(line);


	            while(!line.equals("")){
	                line=br.readLine();
	            }

	            while(line.equals("")){
	                line=br.readLine();
	            }
	        
	        }
	        return header;
	    }
	    
	    private static ArrayList<ArrayList <String>> donneeFiches1(BufferedReader br) throws IOException{
	        ArrayList<String> data = new ArrayList();
	        
	        String line;
	        
	        br.readLine();
	        br.readLine();
	        line = br.readLine();
	        String str = line.substring(line.indexOf("DO"),line.length());
	        line=line.substring(0, line.length()-2);
	        str = str.concat(" : ");
	        line = str.concat(line);
	        data.add(line);
	        
	        line = br.readLine();
	        str = line.substring(line.indexOf("SD"),line.length());
	        line=line.substring(0, line.length()-2);
	        str = str.concat(" : ");
	        line = str.concat(line);
	        data.add(line);
	        
	        while(!br.readLine().equals("AR"));
	        
	        data.add("AR");
	        line = br.readLine();
	        boolean RF = false;
	        String deb;
	        while(!line.equals("FR")){
	            if(line.contains("RF")){
	                RF=true;
	            }
	            if(!RF){
	                if(line.contains("VE")){
	                    deb=line.substring(line.indexOf("VE"),line.length());
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("DF")){
	                    deb=line.substring(line.indexOf("DF"),line.length());
	                    line = line.substring(0,line.indexOf("DF"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }
	            }else{
	                if(line.contains("VE") && line.contains("DF")){
	                    deb = "DF : VE :";
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }
	            }
	            data.add(line);
	            line = br.readLine();
	        }
	        
	        data.add("FR");
	        line = br.readLine();
	        RF=false;
	        while(!line.equals("")){
	            if(line.contains("RF")){
	                RF=true;
	            }
	            if(!RF){
	                if(line.contains("VE")){
	                    deb=line.substring(line.indexOf("VE"),line.length());
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("DF")){
	                    deb=line.substring(line.indexOf("DF"),line.length());
	                    line = line.substring(0,line.indexOf("DF"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }
	            }else{
	                if(line.contains("VE")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("VE"),line.length());
	                    deb = deb.replaceAll("RF :", "");
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("DF")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("DF"),line.length());
	                    line = line.substring(0,line.indexOf("DF"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }
	            }
	            data.add(line);
	            line = br.readLine();
	        }
	        
	        ArrayList<ArrayList<String>> Datas = new ArrayList<>();
	        Datas.add((data));
	        data = new ArrayList<>();
	        line = br.readLine();
	        while(!line.contains("DO")){
	            line = br.readLine();
	        }
	        
	        str = line.substring(line.indexOf("DO"),line.length());
	        line=line.substring(0, line.length()-2);
	        str = str.concat(" : ");
	        line = str.concat(line);
	        data.add(line);
	        
	        line = br.readLine();
	        str = line.substring(line.indexOf("SD"),line.length());
	        line=line.substring(0, line.length()-2);
	        str = str.concat(" : ");
	        line = str.concat(line);
	        data.add(line);
	        //Datas.add(data);
	        
	        while(!br.readLine().contains("AR"));
	        data.add("AR");
	        RF=false;
	        line = br.readLine();
	        while(!line.contains("FR")){
	            if(line.contains("RF")){
	                RF=true;
	            }
	            if(!RF){
	                if(line.contains("VE")){
	                    deb=line.substring(line.indexOf("VE"),line.length());
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("DF")){
	                    deb=line.substring(line.indexOf("DF"),line.length());
	                    line = line.substring(0,line.indexOf("DF"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }
	            }else{
	                if(line.contains("VE") && line.contains("DF")){
	                    deb = "DF : VE :";
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }
	            }
	            data.add(line);
	            line = br.readLine();
	        }
	        RF=false;
	        while(!line.equals("")){
	            if(line.contains("RF")){
	                RF=true;
	            }
	            if(!RF){
	                if(line.contains("VE")){
	                    deb=line.substring(line.indexOf("VE"),line.length());
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("DF")){
	                    deb=line.substring(line.indexOf("DF"),line.length());
	                    line = line.substring(0,line.indexOf("DF"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }
	            }else{
	                if(line.contains("VE")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("VE"),line.length());
	                    deb = deb.replaceAll("RF :", "");
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("DF")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("DF"),line.length());
	                    line = line.substring(0,line.indexOf("DF"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }
	            }
	            data.add(line);
	            line = br.readLine();
	        }
	        
	        Datas.add(data);
	        
	        while(!line.contains("DO")){
	            line = br.readLine();
	        }
	        
	        data = new ArrayList<>();
	        str = line.substring(line.indexOf("DO"),line.length());
	        line=line.substring(0, line.length()-2);
	        str = str.concat(" : ");
	        line = str.concat(line);
	        data.add(line);
	        
	        line = br.readLine();
	        str = line.substring(line.indexOf("SD"),line.length());
	        line=line.substring(0, line.length()-2);
	        str = str.concat(" : ");
	        line = str.concat(line);
	        data.add(line);
	        //Datas.add(data);
	        
	        while(!br.readLine().contains("AR"));
	        data.add("AR");
	        line = br.readLine();
	        RF=false;
	        
	        while(!line.contains("FR")){
	            if(line.contains("RF")){
	                RF=true;
	            }
	            if(!RF){
	                if(line.contains("VE")){
	                    deb=line.substring(line.indexOf("VE"),line.length());
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("DF")){
	                    deb=line.substring(line.indexOf("DF"),line.length());
	                    line = line.substring(0,line.indexOf("DF"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }
	            }else{
	                if(line.contains("VE") && line.contains("DF")){
	                    deb = " NT : PH : DF : VE :";
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }
	            }
	            data.add(line);
	            line = br.readLine();
	        }
	        RF=false;
	        while(!line.equals("")){
	            if(line.contains("RF")){
	                RF=true;
	            }
	            if(!RF){
	                if(line.contains("VE")){
	                    deb=line.substring(line.indexOf("VE"),line.length());
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("DF")){
	                    deb=line.substring(line.indexOf("DF"),line.length());
	                    line = line.substring(0,line.indexOf("DF"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }
	            }else{
	                if(line.contains("VE")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("VE"),line.length());
	                    deb = deb.replaceAll("RF :", "");
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("DF")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("DF"),line.length());
	                    line = line.substring(0,line.indexOf("DF"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }
	            }
	            data.add(line);
	            line = br.readLine();
	        }
	        Datas.add(data);
	        
	        while(!line.contains("DO")){
	            line = br.readLine();
	        }
	        
	        data = new ArrayList<>();
	        str = line.substring(line.indexOf("DO"),line.length());
	        line=line.substring(0, line.length()-2);
	        str = str.concat(" : ");
	        line = str.concat(line);
	        data.add(line);
	        
	        line = br.readLine();
	        str = line.substring(line.indexOf("SD"),line.length());
	        line=line.substring(0, line.length()-2);
	        str = str.concat(" : ");
	        line = str.concat(line);
	        data.add(line);
	        //Datas.add(data);
	        
	        while(!br.readLine().contains("AR"));
	        data.add("AR");
	        line = br.readLine();
	        RF=false;
	        
	        while(!line.contains("FR")){
	            if(line.contains("RF")){
	                RF=true;
	            }
	            if(!RF){
	                if(line.contains("VE")){
	                    deb=line.substring(line.indexOf("VE"),line.length());
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("DF")){
	                    deb=line.substring(line.indexOf("DF"),line.length());
	                    line = line.substring(0,line.indexOf("DF"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }
	            }else{
	                if(line.contains("VE") && line.contains("DF")){
	                    deb = "DF : VE :";
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }
	            }
	            data.add(line);
	            line = br.readLine();
	        }
	        RF=false;
	        while(!line.equals("")){
	            if(line.contains("RF")){
	                RF=true;
	            }
	            if(!RF){
	                if(line.contains("VE")){
	                    deb=line.substring(line.indexOf("VE"),line.length());
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("DF")){
	                    deb=line.substring(line.indexOf("DF"),line.length());
	                    line = line.substring(0,line.indexOf("DF"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }
	            }else{
	                if(line.contains("VE")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("VE"),line.length());
	                    //deb = deb.replaceAll("RF :", "");
	                    deb = "PH : DF : VE :";
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("DF")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("DF"),line.length());
	                    line = line.substring(0,line.indexOf("DF"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }
	            }
	            data.add(line);
	            line = br.readLine();
	        }
	        Datas.add(data);
	        return Datas;
	    }
	    
	    private static ArrayList<ArrayList <String>> donneeFiches2(BufferedReader br) throws IOException{
	        ArrayList<String> data = new ArrayList();
	        String line;
	        
	        while(!br.readLine().equals("AR"));
	        
	        data.add("AR");
	        line = br.readLine();
	        boolean RF = false;
	        String deb;
	        while(!line.equals("FR")){
	            if(line.contains("RF")){
	                RF=true;
	            }
	            if(!RF){
	                if(line.contains("VE")){
	                    deb=line.substring(line.indexOf("VE"),line.length());
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("DF")){
	                    deb=line.substring(line.indexOf("DF"),line.length());
	                    line = line.substring(0,line.indexOf("DF"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }
	            }else{
	                if(line.contains("VE") && line.contains("DF")){
	                    deb = "DF : VE :";
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }
	            }
	            data.add(line);
	            line = br.readLine();
	        }
	        
	        data.add("FR");
	        line = br.readLine();
	        RF=false;
	        while(!line.equals("")){
	            if(line.contains("RF")){
	                RF=true;
	            }
	            if(!RF){
	                if(line.contains("VE")){
	                    deb=line.substring(line.indexOf("VE"),line.length());
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("DF")){
	                    deb=line.substring(line.indexOf("DF"),line.length());
	                    line = line.substring(0,line.indexOf("DF"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }
	            }else{
	                if(line.contains("VE")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("VE"),line.length());
	                    deb = deb.replaceAll("RF :", "");
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("DF")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("DF"),line.length());
	                    line = line.substring(0,line.indexOf("DF"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }
	            }
	            data.add(line);
	            line = br.readLine();
	        }
	        
	        ArrayList<ArrayList<String>> Datas = new ArrayList<>();
	        Datas.add((data));
	        data = new ArrayList<>();
	        line = br.readLine();
	        
	        while(!br.readLine().contains("AR"));
	        data.add("AR");
	        RF=false;
	        line = br.readLine();
	        while(!line.contains("FR")){
	            if(line.contains("RF")){
	                RF=true;
	            }
	            if(!RF){
	                if(line.contains("VE")){
	                    deb=line.substring(line.indexOf("VE"),line.length());
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("DF")){
	                    deb=line.substring(line.indexOf("DF"),line.length());
	                    line = line.substring(0,line.indexOf("DF"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }
	            }else{
	                if(line.contains("VE") && line.contains("DF")){
	                    deb = "DF : VE :";
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }
	            }
	            data.add(line);
	            line = br.readLine();
	        }
	        RF=false;
	        while(!line.equals("")){
	            if(line.contains("RF")){
	                RF=true;
	            }
	            if(!RF){
	                if(line.contains("VE")){
	                    deb=line.substring(line.indexOf("VE"),line.length());
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("DF")){
	                    deb=line.substring(line.indexOf("DF"),line.length());
	                    line = line.substring(0,line.indexOf("DF"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }
	            }else{
	                if(line.contains("VE")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("VE"),line.length());
	                    deb = deb.replaceAll("RF :", "");
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("DF")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("DF"),line.length());
	                    line = line.substring(0,line.indexOf("DF"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }
	            }
	            data.add(line);
	            line = br.readLine();
	        }
	        
	        Datas.add(data);
	        
	        data = new ArrayList<>();
	        while(!br.readLine().contains("AR"));
	        data.add("AR");
	        line = br.readLine();
	        RF=false;
	        
	        while(!line.contains("FR")){
	            if(line.contains("RF")){
	                RF=true;
	            }
	            if(!RF){
	                if(line.contains("VE")){
	                    deb=line.substring(line.indexOf("VE"),line.length());
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("DF")){
	                    deb=line.substring(line.indexOf("DF"),line.length());
	                    line = line.substring(0,line.indexOf("DF"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }
	            }else{
	                if(line.contains("VE") && line.contains("DF")){
	                    deb = " NT : PH : DF : VE :";
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }
	            }
	            data.add(line);
	            line = br.readLine();
	        }
	        RF=false;
	        while(!line.equals("")){
	            if(line.contains("RF")){
	                RF=true;
	            }
	            if(!RF){
	                if(line.contains("VE")){
	                    deb=line.substring(line.indexOf("VE"),line.length());
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("DF")){
	                    deb=line.substring(line.indexOf("DF"),line.length());
	                    line = line.substring(0,line.indexOf("DF"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }
	            }else{
	                if(line.contains("VE")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("VE"),line.length());
	                    deb = deb.replaceAll("RF :", "");
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("DF")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("DF"),line.length());
	                    line = line.substring(0,line.indexOf("DF"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }
	            }
	            data.add(line);
	            line = br.readLine();
	        }
	        Datas.add(data);
	        
	        data = new ArrayList<>();
	        while(!br.readLine().contains("AR"));
	        data.add("AR");
	        line = br.readLine();
	        RF=false;
	        
	        while(!line.contains("FR")){
	            if(line.contains("RF")){
	                RF=true;
	            }
	            if(!RF){
	                if(line.contains("VE")){
	                    deb=line.substring(line.indexOf("VE"),line.length());
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("DF")){
	                    deb=line.substring(line.indexOf("DF"),line.length());
	                    line = line.substring(0,line.indexOf("DF"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }
	            }else{
	                if(line.contains("VE") && line.contains("DF")){
	                    deb = "DF : VE :";
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }
	            }
	            data.add(line);
	            line = br.readLine();
	        }
	        RF=false;
	        while(!line.equals("")){
	            if(line.contains("RF")){
	                RF=true;
	            }
	            if(!RF){
	                if(line.contains("VE")){
	                    deb=line.substring(line.indexOf("VE"),line.length());
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("DF")){
	                    deb=line.substring(line.indexOf("DF"),line.length());
	                    line = line.substring(0,line.indexOf("DF"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = deb.concat(" ");
	                    line = deb.concat(line);
	                }
	            }else{
	                if(line.contains("VE")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("VE"),line.length());
	                    //deb = deb.replaceAll("RF :", "");
	                    deb = "PH : DF : VE :";
	                    line = line.substring(0,line.indexOf("VE"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("DF")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("DF"),line.length());
	                    line = line.substring(0,line.indexOf("DF"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("PH")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("PH"),line.length());
	                    line = line.substring(0,line.indexOf("PH"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }else if(line.contains("NT")){
	                    line = line.replaceAll("\t", "");
	                    deb=line.substring(line.indexOf("NT"),line.length());
	                    line = line.substring(0,line.indexOf("NT"));
	                    deb = "RF | ".concat(deb.concat(" "));
	                    line = deb.concat(line);
	                }
	            }
	            data.add(line);
	            line = br.readLine();
	        }
	        Datas.add(data);
	        return Datas;
	    }
	
}
