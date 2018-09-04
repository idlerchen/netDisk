package com.disk.server;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Xmlproperty {

	public static String IP;
	public static int PORT;
	public static String PATH;
	public static String LOG_PATH;
	

	static {
		SAXReader saxReader = new SAXReader();
		try {
			Document document = new SAXReader().read(new File("property.xml"));
			Element root = document.getRootElement();
			Element e = root.element("property");
			IP = e.elementText("IP");
			PORT = Integer.valueOf(e.elementText("port"));
			PATH = e.elementText("path");
			LOG_PATH = e.elementText("logpath");
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
