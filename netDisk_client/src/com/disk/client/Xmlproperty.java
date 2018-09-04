package com.disk.client;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Xmlproperty {

	public static String IP;
	public static int PORT;

	static {
		SAXReader saxReader = new SAXReader();
		try {
			Document document = new SAXReader().read(new File("property.xml"));
			Element root = document.getRootElement();
			Element e = root.element("property");
			IP = e.elementText("IP");
			PORT = Integer.valueOf(e.elementText("port"));
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
