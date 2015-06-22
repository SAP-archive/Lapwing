package net.sf.okapi.applications.lapwing.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sf.okapi.applications.lapwing.core.Input;
import net.sf.okapi.applications.lapwing.core.Output;
import net.sf.okapi.applications.lapwing.core.Pipeline;
import net.sf.okapi.applications.lapwing.exceptions.PipelineException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class PipelineTest {

	private static final String OKP_LQI_POS_ATTR = "okp:lqiPos";
	private static final String LOC_COMMENT = "locQualityIssueComment";
	private static final String LOC_TYPE = "locQualityIssueType";
	private static final String OKP_LQI_SEG_ID = "okp:lqiSegId";
	private static final String OKP_LQI_TYPE = "okp:lqiType";

	private static final List<String> ISSUE_COMPARISON_ATTRIBUTES = Arrays
			.asList(new String[] { LOC_COMMENT, OKP_LQI_POS_ATTR,
					OKP_LQI_SEG_ID, OKP_LQI_TYPE, LOC_TYPE });

	private static final String TAG_LOC_QUAL_ISSUES = "its:locQualityIssues";
	private static final String TAG_LOC_QUAL_ISSUE = "its:locQualityIssue";
	private InputStream inputFile;
	private InputStream outputFile;

	@Before
	public void setUp() {
		InputStream sourceXlf = getClass().getResourceAsStream("/input.xlf");
		Assert.assertNotNull("Source file not found", sourceXlf);
		InputStream resultXlf = getClass().getResourceAsStream(
				"/sample_output.xlf");
		Assert.assertNotNull("Output file not found", resultXlf);
		this.inputFile = sourceXlf;
		this.outputFile = resultXlf;
	}

	@After
	public void tearDown() throws IOException {
		this.inputFile.close();
	}

	@Test(expected = net.sf.okapi.applications.lapwing.exceptions.PipelineException.class)
	public void testEmptyEncoding() throws Exception {
		Pipeline lapwingPipeline = new Pipeline();
		Input lapwingInput = new Input();
		lapwingInput.setData(inputFile);
		lapwingInput.setEncoding("");
		lapwingInput.setSrcLocale("en");
		lapwingInput.setTgtLocale("de");
		lapwingPipeline.check(lapwingInput);
	}

	@Test(expected = net.sf.okapi.applications.lapwing.exceptions.PipelineException.class)
	public void testNullEncoding() throws Exception {
		Pipeline lapwingPipeline = new Pipeline();
		Input lapwingInput = new Input();
		lapwingInput.setData(inputFile);
		lapwingInput.setEncoding(null);
		lapwingInput.setSrcLocale("en");
		lapwingInput.setTgtLocale("de");
		lapwingPipeline.check(lapwingInput);
	}

	@Test(expected = net.sf.okapi.applications.lapwing.exceptions.PipelineException.class)
	public void testEmptySrcLang() throws Exception {
		Pipeline lapwingPipeline = new Pipeline();
		Input lapwingInput = new Input();
		lapwingInput.setData(inputFile);
		lapwingInput.setEncoding("UTF-8");
		lapwingInput.setSrcLocale("");
		lapwingInput.setTgtLocale("de");
		lapwingPipeline.check(lapwingInput);
	}

	@Test(expected = net.sf.okapi.applications.lapwing.exceptions.PipelineException.class)
	public void testNullSrcLang() throws Exception {
		Pipeline lapwingPipeline = new Pipeline();
		Input lapwingInput = new Input();
		lapwingInput.setData(inputFile);
		lapwingInput.setEncoding("UTF-8");
		lapwingInput.setSrcLocale(null);
		lapwingInput.setTgtLocale("de");
		lapwingPipeline.check(lapwingInput);
	}

	@Test(expected = net.sf.okapi.applications.lapwing.exceptions.PipelineException.class)
	public void testEmptyTgtLang() throws Exception {
		Pipeline lapwingPipeline = new Pipeline();
		Input lapwingInput = new Input();
		lapwingInput.setData(inputFile);
		lapwingInput.setEncoding("UTF-8");
		lapwingInput.setSrcLocale("en");
		lapwingInput.setTgtLocale("");
		lapwingPipeline.check(lapwingInput);
	}

	@Test(expected = net.sf.okapi.applications.lapwing.exceptions.PipelineException.class)
	public void testNulTgtLang() throws Exception {
		Pipeline lapwingPipeline = new Pipeline();
		Input lapwingInput = new Input();
		lapwingInput.setData(inputFile);
		lapwingInput.setEncoding("UTF-8");
		lapwingInput.setSrcLocale("en");
		lapwingInput.setTgtLocale(null);
		lapwingPipeline.check(lapwingInput);
	}

	@Test
	public void testPipeline() throws PipelineException, IOException,
			SAXException, ParserConfigurationException {
		Pipeline lapwingPipeline = new Pipeline();
		Input lapwingInput = new Input();
		lapwingInput.setData(inputFile);
		lapwingInput.setEncoding("UTF-8");
		lapwingInput.setSrcLocale("en");
		lapwingInput.setTgtLocale("de");
		Output output = lapwingPipeline.check(lapwingInput);
		InputStream actualOutData = output.getData();
		/*
		 * ByteArrayOutputStream baos = new ByteArrayOutputStream();
		 * IOUtils.copy(actualOutData, baos); System.out.println(new
		 * String(baos.toByteArray()));
		 */

		InputSource actualInputSource = new InputSource(actualOutData);
		InputSource expectedInputSource = new InputSource(outputFile);
		DocumentBuilderFactory docBuilderFactInstance = DocumentBuilderFactory
				.newInstance();
		Document actualDom = docBuilderFactInstance.newDocumentBuilder().parse(
				actualInputSource);
		Document expectedDom = docBuilderFactInstance.newDocumentBuilder()
				.parse(expectedInputSource);

		/*
		 * the expected and actual documents cannot be compared character by
		 * character as different IDs are generated. We do a rougher comparison
		 * on the number of occurrences of each tag
		 */
		NodeList actualIssueNodes = actualDom
				.getElementsByTagName(TAG_LOC_QUAL_ISSUE);
		NodeList expectedIssueNodes = expectedDom
				.getElementsByTagName(TAG_LOC_QUAL_ISSUE);
		Assert.assertEquals(expectedIssueNodes.getLength(),
				actualIssueNodes.getLength());
		Map<String, Node> actualNodesMap = new HashMap<String, Node>();
		Map<String, Node> expectedNodesMap = new HashMap<String, Node>();
		String[] actualNodesKeys = null;
		String[] expectedNodesKeys = null;
		// sorting of nodes according to issue position. This is done to prevent
		// test failures caused by different order of elements
		for (int i = 0; i < actualIssueNodes.getLength(); i++) {
			Node currActNode = actualIssueNodes.item(i);
			Node currExpNode = expectedIssueNodes.item(i);

			actualNodesMap.put(
					currActNode.getAttributes().getNamedItem(OKP_LQI_POS_ATTR)
							.getNodeValue(), currActNode);
			expectedNodesMap.put(
					currExpNode.getAttributes().getNamedItem(OKP_LQI_POS_ATTR)
							.getNodeValue(), currExpNode);
			actualNodesKeys = actualNodesMap.keySet().toArray(new String[0]);
			Arrays.sort(actualNodesKeys);
			expectedNodesKeys = expectedNodesMap.keySet()
					.toArray(new String[0]);
			Arrays.sort(expectedNodesKeys);
		}
		// comparison of actual issue nodes
		for (int i = 0; i < actualNodesKeys.length; i++) {
			NamedNodeMap actualAttributes = actualNodesMap.get(
					actualNodesKeys[i]).getAttributes();
			NamedNodeMap expectedAttributes = expectedNodesMap.get(
					expectedNodesKeys[i]).getAttributes();
			assertAttributesEqual(actualAttributes, expectedAttributes,
					ISSUE_COMPARISON_ATTRIBUTES);

		}
		// comparison of issues nodes
		actualIssueNodes = actualDom.getElementsByTagName(TAG_LOC_QUAL_ISSUES);
		expectedIssueNodes = expectedDom
				.getElementsByTagName(TAG_LOC_QUAL_ISSUES);
		Assert.assertEquals(expectedIssueNodes.getLength(),
				actualIssueNodes.getLength());

	}

	private void assertAttributesEqual(NamedNodeMap actualAttributes,
			NamedNodeMap expectedAttributes, List<String> attributeName) {
		for (String currAttr : attributeName) {
			assertAttributeEqual(actualAttributes, expectedAttributes, currAttr);
		}
	}

	private void assertAttributeEqual(NamedNodeMap actualAttributes,
			NamedNodeMap expectedAttributes, String attributeName) {

		Node actualNode = actualAttributes.getNamedItem(attributeName);
		Node expectedNode = expectedAttributes.getNamedItem(attributeName);

		Assert.assertEquals(expectedNode.getNodeValue(),
				actualNode.getNodeValue());

	}

	public static void printDocument(Document doc, OutputStream out)
			throws IOException, TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "4");

		transformer.transform(new DOMSource(doc), new StreamResult(
				new OutputStreamWriter(out, "UTF-8")));
	}
}
