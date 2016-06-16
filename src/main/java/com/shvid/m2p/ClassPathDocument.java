/*
 *      Copyright (C) 2015 Alex Shvid
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.shvid.m2p;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ClassPathDocument {

	private final String projectDir;
	private final Map<String, Element> entriesMap = new HashMap<String, Element>();

	public ClassPathDocument(String projectDir) {
		this.projectDir = projectDir;
	}
	
	public void addFile(File file, String module) throws IOException {

		Document doc = Jsoup.parse(file, "UTF-8");

		Elements entries = doc.select("classpathentry");

		for (Element entry : entries) {

			String kind = entry.attr("kind");

			String path = entry.attr("path");
			if (path == null) {
				continue;
			}

			boolean addPath = true;
			
			if ("src".equals(kind)) {

				if (path.startsWith("/")) {
					// could be root file dir or project in eclipse
					
					boolean eclipseModule = path.indexOf("/", 1) == -1;
					
					if (eclipseModule) {
						continue;
					}
					
					if (path.startsWith(projectDir)) {
						path = path.substring(projectDir.length());
						
						if (path.startsWith("/")) {
							path = path.substring(1);
						}
						
					}
					
				}
				else {
					path = module + File.separator + path;
				}

				File pathFile = new File(projectDir + File.separator + path);
				
				if (!pathFile.exists()) {
					addPath = false;
				}
				
				entry.attr("path", path);

			}

			if (addPath) {
				entriesMap.put(path, entry);
			}

		}

	}

	public void write(File out) throws IOException {
		FileWriter writer = new FileWriter(out);

		try {

			writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			writer.write("<classpath>\n");

			for (Element entry : entriesMap.values()) {
				writer.write(entry + "\n");
			}

			writer.write("</classpath>\n");

		} finally {
			writer.close();
		}
	}

}
