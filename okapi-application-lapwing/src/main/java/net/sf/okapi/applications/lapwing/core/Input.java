package net.sf.okapi.applications.lapwing.core;

import java.io.InputStream;

public class Input {

	private InputStream data;
	private String encoding;
	private String srcLocale;
	private String tgtLocale;

	public Input() {
		// TODO Auto-generated constructor stub
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

	public String getSrcLocale() {
		return srcLocale;
	}

	public void setSrcLocale(String srcLocale) {
		this.srcLocale = srcLocale;
	}

	public String getTgtLocale() {
		return tgtLocale;
	}

	public void setTgtLocale(String tgtLocale) {
		this.tgtLocale = tgtLocale;
	}

}
