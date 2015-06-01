package net.sf.okapi.applications.lapwing.soap;

import javax.servlet.ServletConfig;
import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.http.DestinationRegistry;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;

public class PublishingServlet extends CXFNonSpringServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2143719123202258646L;

	public PublishingServlet() {

	}

	public PublishingServlet(DestinationRegistry destinationRegistry) {
		super(destinationRegistry);

	}

	public PublishingServlet(DestinationRegistry destinationRegistry,
			boolean loadBus) {
		super(destinationRegistry, loadBus);

	}

	@Override
	public void loadBus(ServletConfig servletConfig) {
		super.loadBus(servletConfig);
		Bus bus = getBus();
		BusFactory.setDefaultBus(bus);
		Endpoint ep = Endpoint.publish("/itsprocessor", new ITSProcessorImpl());
		((SOAPBinding) ep.getBinding()).setMTOMEnabled(true);

	}

}
