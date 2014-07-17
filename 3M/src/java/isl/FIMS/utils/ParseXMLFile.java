/*
 * Copyright 2014 Institute of Computer Science,
 * Foundation for Research and Technology - Hellas
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved
 * by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations
 * under the Licence.
 *
 * Contact:  POBox 1385, Heraklio Crete, GR-700 13 GREECE
 * Tel:+30-2810-391632
 * Fax: +30-2810-391638
 * E-mail: isl@ics.forth.gr
 * http://www.ics.forth.gr/isl
 *
 * Authors : Georgios Samaritakis, Konstantina Konsolaki.
 *
 * This file is part of the 3M webapp of Mapping Memory Manager project.
 */
package isl.FIMS.utils;

import isl.FIMS.servlet.ApplicationBasicServlet;


import java.io.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

/** This class represents short example how to parse XML file,
 * get XML nodes values and its values.<br><br>
 * It implements method to save XML document to XML file too
 */
public class ParseXMLFile {

	

	/** Creates a new instance of ParseXMLFile */
	public ParseXMLFile(String systemroot, String xmlFileName) {
//		parse XML file -> XML document will be build
		Document doc = parseFile(systemroot+ xmlFileName);
//		get root node of xml tree structure
		Node root = doc.getDocumentElement();
//		write node and its child nodes into System.out
		System.out.println("Statemend of XML document...");
		writeDocumentToOutput(root,0);
		System.out.println("... end of statement");
//		write Document into XML file
		//saveXMLDocument(targetFileName, doc);
	}

	public static String getElementName(String systemroot,String xmlFileName, String nodeValue) {
		String tag = null;
		Document doc = parseFile(systemroot+ xmlFileName);
		Node root = doc.getDocumentElement();
	
//		System.out.println("Statemend of XML document...");
//		writeDocumentToOutput(root,0);
//		System.out.println("... end of statement");
			try {
				tag=writeDocumentToOutput(root,0,nodeValue);
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return tag;
	}
	
	/** Returns element value
	 * @param elem element (it is XML tag)
	 * @return Element value otherwise empty String
	 */
	public final static String getElementValue( Node elem ) {
		Node kid;
		if( elem != null){
			if (elem.hasChildNodes()){
				for( kid = elem.getFirstChild(); kid != null; kid = kid.getNextSibling() ){
					if( kid.getNodeType() == Node.TEXT_NODE  ){
						return kid.getNodeValue();
					}
				}
			}
		}
		return "";
	}
	

	private static String getIndentSpaces(int indent) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < indent; i++) {
			buffer.append(" ");
		}
		return buffer.toString();
	}

	/** Writes node and all child nodes into System.out
	 * @param node XML node from from XML tree wrom which will output statement start
	 * @param indent number of spaces used to indent output
	 */
	public static void writeDocumentToOutput(Node node,int indent) {
//		get element name
		String nodeName = node.getNodeName();
//		get element value
		String nodeValue = getElementValue(node);
//		get attributes of element
		NamedNodeMap attributes = node.getAttributes();
		System.out.println(getIndentSpaces(indent) + "NodeName: " + nodeName + ", NodeValue: " + nodeValue);
		for (int i = 0; i < attributes.getLength(); i++) {
			Node attribute = attributes.item(i);
			System.out.println(getIndentSpaces(indent + 2) + "AttributeName: " + attribute.getNodeName() + ", attributeValue: " + attribute.getNodeValue());
		}
//		write all child nodes recursively
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				writeDocumentToOutput(child,indent + 2);
			}
		}
	}

	public static String writeDocumentToOutput(Node node,int indent, String nodeVal) {

		String tag="";
		String nodeName = node.getNodeName();

		String nodeValue = getElementValue(node);

		NamedNodeMap attributes = node.getAttributes();
		//System.out.println(getIndentSpaces(indent) + "NodeName: " + nodeName + ", NodeValue: " + nodeValue);
		
		if (nodeValue.contains(nodeVal)){
			tag=nodeName;
		}
		else{
		NodeList children = node.getChildNodes();

		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
				String childNodeValue=getElementValue(child);
				String childNodeName =child.getNodeName();
				if (childNodeValue.contains(nodeVal)){
					tag=childNodeName;
				}
				else if (child.getNodeType() == Node.ELEMENT_NODE) {
				writeDocumentToOutput(child,indent + 2);
				}
			}
	   }
		return tag;
	}
	/** Saves XML Document into XML file.
	 * @param fileName XML file name
	 * @param doc XML document to save
	 * @return <B>true</B> if method success <B>false</B> otherwise
	 */    
	public static boolean saveXMLDocument(String fileName, Document doc) {
		System.out.println("Saving XML file... " + fileName);
//		open output stream where XML Document will be saved
		File xmlOutputFile = new File(fileName);
		FileOutputStream fos;
		Transformer transformer;
		try {
			fos = new FileOutputStream(xmlOutputFile);
		}
		catch (FileNotFoundException e) {
                        System.out.println(" Save XML Document");
			System.out.println("Error occured: " + e.getMessage());
			return false;
		}
//		Use a Transformer for output
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		try {
			transformer = transformerFactory.newTransformer();
		}
		catch (TransformerConfigurationException e) {
			System.out.println("Transformer configuration error: " + e.getMessage());
			return false;
		}
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(fos);
//		transform source into result will do save
		try {
			transformer.transform(source, result);
		}
		catch (TransformerException e) {
			System.out.println("Error transform: " + e.getMessage());
		}
		System.out.println("XML file saved.");
		return true;
	}

	/** Parses XML file and returns XML document.
	 * @param fileName XML file to parse
	 * @return XML document or <B>null</B> if error occured
	 */
	public static Document parseFile(String fileName) {
		System.out.println("Parsing XML file... " + fileName);
		DocumentBuilder docBuilder;
		Document doc = null;
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setIgnoringElementContentWhitespace(true);
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
		}
		catch (ParserConfigurationException e) {
			System.out.println("Wrong parser configuration: " + e.getMessage());
			return null;
		}
		//File sourceFile = new File(fileName);
		try {
			//doc = docBuilder.parse(sourceFile);
			doc = docBuilder.parse(fileName);
		}
		catch (SAXException e) {
			System.out.println("Wrong XML file structure: " + e.getMessage());
			return null;
		}
		catch (IOException e) {
			System.out.println("Could not read source file: " + e.getMessage());
		}
		System.out.println("XML file parsed");
		return doc;
	}

//	/** Starts XML parsing example
//	 * @param args the command line arguments
//	 */
//	public static void main(String[] args) {
//		new ParseXMLFile(null);
//	}

} 
