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
import java.util.HashMap;
import java.util.Map;

public class ParentClassPath {

	private static final String POMXML_FILENAME = "pom.xml";
	private static final String CLASSPATH_FILENAME = ".classpath";
	private final String projectDir;

	public ParentClassPath(String projectDir) {
		this.projectDir = projectDir;
	}

	public Map<File, String> findInnerClassPaths() {
		Map<File, String> collector = new HashMap<File, String>();
		File path = new File(projectDir);
		findInnerClassPaths(collector, path, path.getAbsolutePath().length());
		return collector;
	}

	private void findInnerClassPaths(Map<File, String> collector, File path, int rootLength) {
		for (File child : path.listFiles()) {
			if (child.isDirectory() && isModuleDir(child)) {
				findInnerClassPaths(collector, child, rootLength);
			} else if (child.isFile() && CLASSPATH_FILENAME.equals(child.getName())) {
				collector.put(child, path.getAbsolutePath().substring(rootLength + 1));
			}
		}
	}

	public boolean isModuleDir(File dir) {
		String name = dir.getName();
		File m2pignore = new File(dir, ".m2pignore");
		if ("bin".equals(name) || "target".equals(name) || m2pignore.exists()) {
			return false;
		}
		return true;
	}

	public boolean hasPomXmlFile() {
		File file = new File(projectDir, POMXML_FILENAME);
		return file.exists();
	}

	public File getClassPathFile() {
		return new File(projectDir, CLASSPATH_FILENAME);
	}

	public boolean hasClassPathFile() {
		return getClassPathFile().exists();
	}

	public void dropClassPathFile() {
		getClassPathFile().delete();
	}

}
