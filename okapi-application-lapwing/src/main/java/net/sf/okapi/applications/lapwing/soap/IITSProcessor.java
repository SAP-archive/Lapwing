	package net.sf.okapi.applications.lapwing.soap;

import javax.jws.WebService;

import net.sf.okapi.applications.lapwing.exceptions.ServerException;

@WebService(name = "ITSProcessor", targetNamespace = "http://lapwing.applications.okapi.sf.net")
public interface IITSProcessor {

	public SOAPResponse check(SOAPRequest input)
			throws ServerException;

}
