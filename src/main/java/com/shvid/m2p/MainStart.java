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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Map;

public class MainStart {

	public static void main(String[] args) throws Exception {

		String currentDir = System.getProperty("current.dir");

		System.out.println("M2P .classpath multi-project generator started at " + currentDir);

		ParentClassPath pcp = new ParentClassPath(currentDir);

		if (!pcp.hasPomXmlFile()) {
			System.out.println("pom.xml file is not found in current directory");
			return;
		}

		if (pcp.hasClassPathFile()) {
			System.out.println(".classpath file is found in current directory");
			System.out.println("Do you want to override .classpath file? (Y,N)");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String answer = reader.readLine();
			if ("Y".equalsIgnoreCase(answer)) {
				pcp.dropClassPathFile();
			} else {
				System.out.println("Delete .classpath file and restart m2p");
				return;
			}
		}

		Map<File, String> classpaths = pcp.findInnerClassPaths();

		System.out.println("Found source .classpath files:");
		for (File f : classpaths.keySet()) {
			System.out.println("	" + f);
		}

		ClassPathDocument doc = new ClassPathDocument();

		for (Map.Entry<File, String> e : classpaths.entrySet()) {
			doc.addFile(e.getKey(), e.getValue());
		}

		doc.write(pcp.getClassPathFile());

		System.out.println(pcp.getClassPathFile() + " successfully generated");

	}
}
