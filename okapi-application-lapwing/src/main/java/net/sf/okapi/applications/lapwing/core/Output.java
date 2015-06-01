package net.sf.okapi.applications.lapwing.core;

import java.io.InputStream;

public class Output {

	private InputStream data;
	private String encoding;

	public Output() {

	}

	public InputStream getData() {
		return data;
	}

	public void setData(InputStream data) {
		this.data = data;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

}
