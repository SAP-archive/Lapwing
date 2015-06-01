#Lapwing
##What is Lapwing?
“Lapwing” implements a “SOAP wrapper” for functionality that uses Okapi and LanguageTool. "Lapwing" features the following:
 
- Exposes basic LanguageTool capabilities as Web Service 
- Supports processing of OASIS XLIFF 1.2 (see https://www.oasis-open.org/committees/xliff/) 
- Uses the Okapi libraries for reading, annotating, and writing (see http://okapi.opentag.com/) 
- Provides annotations related to possible linguistic issues based on the W3C Internationalization Tag Set 2.0 (see http://www.w3.org/TR/its20/#lqissue) 
- Allows centrally managed components that interact based on the Web Services paradigm (no need for local, or special installations of LanguagelTool or Okapi) 
- Uses Message Transmission Optimization Mechanism (MTOM) to speed up content transfers 
 
In a sense, “Lapwing” implements a “Web API” that can be deployed to/and used in a wide range of environments. After “Lapwing” has been deployed to a plain Java Servlet Engine, or a JEE-compliant Java application server, its capabilities are exposed as SOAP endpoint that allows you to process an XLIFF file with LanguageTool. 
 
“Lapwing” is meant to be a starting point for additional, standards-based multilingual processing based on Web Services. Accordingly, “Lapwing” does not come with a lot of bells and whistles. 

##Get it!
In order to get Lapwing to run on an Servlet Container of your choice, you just need to do the following:
- clone the repository and build the sources through Maven
- deploy *lapwing.war* to your server 
- Lapwing's WSDL will be accessible at: http://host:port/lapwing/services/itsprocessor?wsdl

##Libraries Lapwing builds upon
LanguageTool (see www.languagetool.org) is an open source checker related to various linguistic levels (e.g. spelling, terminology, grammar, style) for a wide range of languages (including Chinese and Russian). It derives its power from the use of Natural Language Processing (NLP) technology. It is embedded for example into OpenOffice/LibreOffice and can be used in other fashions (e.g. as Java library or as stand-alone HTTP server). The checking (and correction) that can be performed is customizable. 
 
Okapi (see okapi.opentag.com) is a "cross-platform and free open-source set of components and applications that offer extensive support for localizing and translating documentation and Software" (from the Okapi Web site). 

##Current known limitations
Design decisions that have been made are the following: 
* Web Service based on SOAP protocol/paradigm (as opposed to ReST) 
* No support for extensive parameterization (the only input parameters are source language, target language, encoding, and file)
