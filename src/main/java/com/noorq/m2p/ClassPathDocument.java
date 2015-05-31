/*
 *      Copyright (C) 2015 Noorq, Inc.
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
package com.noorq.m2p;

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

	private final Map<String, Element> entriesMap = new HashMap<String, Element>();

	public void addFile(File file, String module) throws IOException {

		Document doc = Jsoup.parse(file, "UTF-8");

		Elements entries = doc.select("classpathentry");

		for (Element entry : entries) {

			String kind = entry.attr("kind");

			String path = entry.attr("path");
			if (path == null) {
				continue;
			}

			if ("src".equals(kind)) {

				if (!path.startsWith("src")) {
					continue;
				}

				path = module + File.separator + path;

				entry.attr("path", path);

			}

			entriesMap.put(path, entry);

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
