package fr.fjdhj.rasmusic.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.fjdhj.rasmusic.module.radio.Radio;

public class XMLUtil {
	private static final String RADIOLIST_PATH = "ressource/radiolist/";
	
	public static HashMap<String,Radio> loadRadioList() {
		HashMap<String,Radio> liste = new HashMap<String,Radio>();		
		File root = new File(RADIOLIST_PATH);
		System.out.println(root.getAbsolutePath());
		for(File file : root.listFiles()) {
			if(!file.isDirectory()) {
				processRadioFile(liste, file);
			}
		}
		return liste;
	}

	private static void processRadioFile(HashMap<String,Radio> liste, File file) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		
		try {
			builder= factory.newDocumentBuilder();
			Document xml = builder.parse(file);
			
			NodeList children = xml.getFirstChild().getChildNodes();
			for(int i=0;i<children.getLength();i++) {
				Node child = children.item(i);
				if(child instanceof Element) {
					//On crée les objets
					System.out.println("name:" + ((Element) child).getAttribute("name") + "		:	" + ((Element) child).getAttribute("URL"));
					String name = ((Element) child).getAttribute("name");
					URL url = null;
					URL iconurl = null;
					try {
						url = new URL(((Element) child).getAttribute("URL"));
						iconurl = new URL(((Element) child).getAttribute("icon"));
					}catch(java.net.MalformedURLException ex) {}
					
					liste.put(name,new Radio(name, url, iconurl));
				}
			}
		
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.out.println(XMLUtil.loadRadioListAsXML());
	}
	
	private static String processRadioFile(File file) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		String data = "";
		try {
			builder= factory.newDocumentBuilder();
			Document xml = builder.parse(file);
			
			NodeList children = xml.getFirstChild().getChildNodes();
			for(int i=0;i<children.getLength();i++) {
				Node child = children.item(i);
				if(child instanceof Element) {
					//On crée les objets
					data += (xmlNodeToString(child));
				}
			}
		
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	private static String xmlNodeToString(Node child) {
		String data = "";
		data += "<" + child.getNodeName()+" ";
		NamedNodeMap attributs = child.getAttributes();
		for(int i=attributs.getLength()-1; i>=0;i--) {
			String nom = attributs.item(i).getNodeName();
			String val = attributs.item(i).getNodeValue();
			data += (nom + "=\"" + val +"\"");
		}
		data+="></"+child.getNodeName()+">";
		return data;
	}

	public static String loadRadioListAsXML() {
		String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		File root = new File(RADIOLIST_PATH);
		System.out.println(root.getAbsolutePath());
		for(File file : root.listFiles()) {
			if(!file.isDirectory()) {
				content += processRadioFile(file)+ "\n";
			}
		}
		return content;
	}
}
