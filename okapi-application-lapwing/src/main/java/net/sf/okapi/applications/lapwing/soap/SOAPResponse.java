package net.sf.okapi.applications.lapwing.soap;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlMimeType;

@XmlAccessorType(XmlAccessType.FIELD)
public class SOAPResponse {

	private String encoding;
	@XmlMimeType("application/octet-stream")
	private DataHandler data;

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public DataHandler getData() {
		return data;
	}

	public void setData(DataHandler data) {
		this.data = data;
	}

}
