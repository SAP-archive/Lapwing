package net.sf.okapi.applications.lapwing.core;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;

import net.sf.okapi.applications.lapwing.exceptions.PipelineException;
import net.sf.okapi.common.ExecutionContext;
import net.sf.okapi.common.LocaleId;
import net.sf.okapi.common.filters.FilterConfigurationMapper;
import net.sf.okapi.common.filters.IFilterConfigurationMapper;
import net.sf.okapi.common.filterwriter.XLIFFWriter;
import net.sf.okapi.common.filterwriter.XLIFFWriterParameters;
import net.sf.okapi.common.pipelinedriver.PipelineDriver;
import net.sf.okapi.common.resource.RawDocument;
import net.sf.okapi.steps.common.FilterEventsToRawDocumentStep;
import net.sf.okapi.steps.common.FilterEventsWriterStep;
import net.sf.okapi.steps.common.RawDocumentToFilterEventsStep;
import net.sf.okapi.steps.languagetool.LanguageToolStep;
import net.sf.okapi.steps.languagetool.Parameters;

public class Pipeline {

	private static final String CFG_CLASS = "net.sf.okapi.filters.xliff.XLIFFFilter";
	private static final String OKF_XLIFF_CONFIG_ID = "okf_xliff";
	private static final String TMP_FILE_EXT = ".tmp";
	private static final String TMP_FILE_NAME = "tempFile";
	private IFilterConfigurationMapper fcMapper;

	public Pipeline() {
		fcMapper = new FilterConfigurationMapper();
		fcMapper.addConfigurations(Pipeline.CFG_CLASS);
	}

	private String getOutputEncoding(RawDocument rd) {
		return rd.getEncoding();
	}

	private void validateInput(Input input) throws PipelineException {
		String encoding = input.getEncoding();
		if (encoding == null || encoding.equals("")) {
			throw new PipelineException(
					"Encoding cannot be null or an empty string");
		}

		String srcLocale = input.getSrcLocale();
		if (srcLocale == null || srcLocale.equals("")) {
			throw new PipelineException(
					"Source language cannot be null or an empty string");
		}

		String tgtLocale = input.getTgtLocale();
		if (tgtLocale == null || tgtLocale.equals("")) {
			throw new PipelineException(
					"Target language cannot be null or an empty string");
		}
	}

	public Output check(Input input) throws PipelineException {
		validateInput(input);
		try {
			RawDocument rawDoc = new RawDocument(input.getData(),
					input.getEncoding(), new LocaleId(input.getSrcLocale()),
					new LocaleId(input.getTgtLocale()));
			byte[] annotatedDoc = internalCheck(rawDoc);
			ByteArrayInputStream bais = new ByteArrayInputStream(annotatedDoc);
			Output retVal = new Output();
			retVal.setData(bais);
			retVal.setEncoding(getOutputEncoding(rawDoc));
			return retVal;
		} catch (Exception exc) {
			exc.printStackTrace();
			throw new PipelineException(exc);
		}

	}

	private byte[] internalCheck(RawDocument rd) throws Exception {

		rd.setFilterConfigId(Pipeline.OKF_XLIFF_CONFIG_ID);
		System.out.println(rd.getStream().markSupported());
		// Create the driver
		PipelineDriver driver = new PipelineDriver();
		driver.setFilterConfigurationMapper(fcMapper);
		ExecutionContext context = new ExecutionContext();
		context.setApplicationName("");
		context.setIsNoPrompt(true);
		driver.setExecutionContext(context);

		// Raw document to filter events step
		RawDocumentToFilterEventsStep rd2feStep = new RawDocumentToFilterEventsStep();
		driver.addStep(rd2feStep);

		// languagetool step
		LanguageToolStep ltStep = new LanguageToolStep();
		Parameters parameters = new Parameters();
		parameters.setCheckSource(true);
		parameters.setCheckSpelling(true);
		parameters.setEnableFalseFriends(true);
		driver.addStep(ltStep);

		FilterEventsToRawDocumentStep fetrdStep = new FilterEventsToRawDocumentStep();
		// Filter events to raw document final step (using the XLIFF writer)
		FilterEventsWriterStep fewStep = new FilterEventsWriterStep();

		XLIFFWriter writer = new XLIFFWriter();
		XLIFFWriterParameters paramsXliff = (XLIFFWriterParameters) writer
				.getParameters();
		paramsXliff.setPlaceholderMode(true);
		paramsXliff.setCopySource(false);
		paramsXliff.setIncludeAltTrans(true);
		paramsXliff.setIncludeCodeAttrs(true);
		writer.setOptions(rd.getSourceLocale(), getOutputEncoding(rd));

		fewStep.setFilterWriter(writer);

		File temporary = File.createTempFile(Pipeline.TMP_FILE_NAME,
				Pipeline.TMP_FILE_EXT);
		driver.addStep(fetrdStep);
		driver.addBatchItem(rd, temporary.toURI(), getOutputEncoding(rd));

		// Process
		driver.processBatch();
		byte[] retVal = Files.readAllBytes(temporary.toPath());
		temporary.delete();

		return retVal;
	}

}
