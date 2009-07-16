/**   
 * License Agreement for Jaeksoft OpenSearchServer
 *
 * Copyright (C) 2008-2009 Emmanuel Keller / Jaeksoft
 * 
 * http://www.open-search-server.com
 * 
 * This file is part of Jaeksoft OpenSearchServer.
 *
 * Jaeksoft OpenSearchServer is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 * Jaeksoft OpenSearchServer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Jaeksoft OpenSearchServer. 
 *  If not, see <http://www.gnu.org/licenses/>.
 **/

package com.jaeksoft.searchlib.crawler.file.database;

import java.util.Date;
import java.util.List;

import com.jaeksoft.searchlib.crawler.common.database.FetchStatus;
import com.jaeksoft.searchlib.crawler.common.database.IndexStatus;
import com.jaeksoft.searchlib.crawler.common.database.ParserStatus;
import com.jaeksoft.searchlib.crawler.common.database.Selector;
import com.jaeksoft.searchlib.index.IndexDocument;
import com.jaeksoft.searchlib.util.GenericMap;

public class PathItem extends GenericMap<String> {

	private static final String NO = "no";
	private static final String YES = "yes";

	public enum Status {
		UNDEFINED("Undefined"), INJECTED("Injected"), ALREADY(
				"Already injected"), ERROR("Unknown Error");

		private String name;

		private Status(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private Status status;
	private String path;
	private Selector<PathItem> fileSelector;
	private boolean withSub;

	public PathItem() {
		status = Status.UNDEFINED;
		path = null;
		fileSelector = null;
		withSub = false;
	}

	public PathItem(String path, boolean withSub) {
		this();
		setPath(path);
		setWithSub(withSub);
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status v) {
		status = v;
	}

	public void setSelected(boolean v) {
		if (v)
			fileSelector.addSelection(this);
		else
			fileSelector.removeSelection(this);
	}

	public boolean isSelected() {
		return fileSelector.isSelected(this);
	}

	public void setPath(String s) {
		path = s;
	}

	public String getPath() {
		return path;
	}

	public void setFileSelector(Selector<PathItem> patternSelector) {
		this.fileSelector = patternSelector;
	}

	public boolean isWithSub() {
		return withSub;
	}

	public String isWithSubToString() {
		return (withSub ? YES : NO);
	}

	public static boolean parse(String text) {
		if (text == null)
			return false;

		if (text.equals(YES))
			return true;

		return false;
	}

	public void setWithSub(boolean withSub) {
		this.withSub = withSub;
	}

	public void load(List<PathItem> items) {
		for (PathItem current : items) {
			add(current.getPath(), current.isWithSubToString());
		}
	}

	public void populate(IndexDocument indexDocument) {
		indexDocument.set("path", getPath());
		indexDocument.set("when", FileItem.getWhenDateFormat().format(
				new Date()));

		indexDocument.set("fetchStatus", FetchStatus.UN_FETCHED.value);
		indexDocument.set("parserStatus", ParserStatus.NOT_PARSED.value);
		indexDocument.set("indexStatus", IndexStatus.NOT_INDEXED.value);
	}

	public IndexDocument getIndexDocument() {
		IndexDocument indexDocument = new IndexDocument();
		populate(indexDocument);
		return indexDocument;
	}

}
