package net.sf.okapi.applications.lapwing.soap;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.jws.WebService;
import javax.mail.util.ByteArrayDataSource;

import net.sf.okapi.applications.lapwing.core.Input;
import net.sf.okapi.applications.lapwing.core.Output;
import net.sf.okapi.applications.lapwing.core.Pipeline;
import net.sf.okapi.applications.lapwing.exceptions.ServerException;

import org.apache.cxf.helpers.IOUtils;

@WebService(endpointInterface = "net.sf.okapi.applications.lapwing.soap.IITSProcessor", serviceName = "itsprocessor", targetNamespace = "http://lapwing.applications.okapi.sf.net", portName = "itsprocessor")
public class ITSProcessorImpl implements IITSProcessor {

	private static final String OUTPUT_MIME_TYPE = "application/xml";

	public ITSProcessorImpl() {

	}

	@Override
	public SOAPResponse check(SOAPRequest input) throws ServerException {

		Pipeline pipeline = new Pipeline();
		byte[] annotatedFile;
		try {
			Input ltinput = new Input();
			ltinput.setData(input.getData().getInputStream());
			ltinput.setEncoding(input.getEncoding());
			ltinput.setSrcLocale(input.getSource());
			ltinput.setTgtLocale(input.getTarget());
			Output output = pipeline.check(ltinput);
			SOAPResponse response = new SOAPResponse();
			annotatedFile = IOUtils.readBytesFromStream(output.getData());
			DataSource responseFile = new ByteArrayDataSource(annotatedFile,
					ITSProcessorImpl.OUTPUT_MIME_TYPE);
			DataHandler handler = new DataHandler(responseFile);
			response.setData(handler);
			response.setEncoding(output.getEncoding());
			return response;
		} catch (Exception e) {
			throw new ServerException(e);
		}

	}
}
