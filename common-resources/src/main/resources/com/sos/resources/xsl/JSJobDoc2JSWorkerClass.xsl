<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:jobdoc="http://www.sos-berlin.com/schema/scheduler_job_documentation_v1.1"
	xmlns:xhtml="http://www.w3.org/1999/xhtml"
	exclude-result-prefixes="jobdoc">

	<xsl:output method="text" encoding="iso-8859-1" indent="no" />

<xsl:param name="Category" required="no" as="xs:string"/>
<xsl:param name="ClassNameExtension" required="yes" as="xs:string"/>
<xsl:param name="ExtendsClassName"  required="yes" as="xs:string"/>
<xsl:param name="WorkerClassName"  required="yes" as="xs:string"/>
<xsl:param name="XMLDocuFilename"  required="yes" as="xs:string"/>
<xsl:param name="XSLTFilename"  required="yes" as="xs:string"/>

<xsl:param name="timestamp" required="yes" as="xs:string"/> 
<xsl:param name="package_name" required="yes" as="xs:string"/> 
<xsl:param name="standalone" required="yes" as="xs:string"/> 
<xsl:param name="sourcetype" required="yes" as="xs:string"/> 
<xsl:param name="keywords" required="no" as="xs:string"/>
<xsl:param name="default_lang" required="yes" as="xs:string"/>


	<xsl:variable name="nl" select="'&#xa;'" />

	<xsl:template match="//jobdoc:description">
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="jobdoc:job">
	<!--  the name of the class is derived from the attribute name of the job-tac -->
		<xsl:variable name="class_name">
			<xsl:value-of select="concat(./@name, $ClassNameExtension)" />
		</xsl:variable>

		<xsl:variable name="class_title">
			<xsl:value-of select="./@title" />
		</xsl:variable>

package <xsl:value-of select="$package_name" />;

import <xsl:value-of select="$package_name" />.<xsl:value-of select="$WorkerClassName" />;
import <xsl:value-of select="$package_name" />.<xsl:value-of select="$WorkerClassName" />Options;
import org.apache.log4j.Logger;
import com.sos.JSHelper.Basics.JSJobUtilitiesClass;
import com.sos.localization.*;
import com.sos.scheduler.messages.JSMessages;
import com.sos.JSHelper.Basics.JSJobUtilities;


public class <xsl:value-of select="$class_name" /> extends JSJobUtilitiesClass &lt;<xsl:value-of select="$class_name" />Options&gt;{  
	private static final String CLASSNAME = "<xsl:value-of select="$class_name" />";  
	private static final Logger LOGGER = Logger.getLogger(<xsl:value-of select="$class_name" />.class);

	protected <xsl:value-of select="$class_name"/>Options objOptions = null;
    private JSJobUtilities  objJSJobUtilities  = this;


	public <xsl:value-of select="$class_name" />() {
		super(new <xsl:value-of select="$class_name" />Options());
	}

 
	public <xsl:value-of select="$class_name" />Options getOptions() {

		final String METHODNAME = CLASSNAME + "::Options";  

		if (objOptions == null) {
			objOptions = new <xsl:value-of select="$class_name" />Options();
		}
		return objOptions;
	}

 
	public <xsl:value-of select="$class_name" />Options getOptions(final <xsl:value-of select="$class_name" />Options pobjOptions) {
		final String METHODNAME = CLASSNAME + "::Options";  

		objOptions = pobjOptions;
		return objOptions;
	}

 
	public <xsl:value-of select="$class_name" /> Execute() throws Exception {
		final String METHODNAME = CLASSNAME + "::Execute";  

        LOGGER.debug(String.format(JSMessages.JSJ_I_110.get(), METHODNAME ));

		try { 
			getOptions().CheckMandatory();
			LOGGER.debug(getOptions().toString());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			LOGGER.error(String.format(JSMessages.JSJ_F_107.get(), METHODNAME ),e);
            throw e;			
		} finally {
            LOGGER.debug(String.format(JSMessages.JSJ_I_111.get(), METHODNAME ));
		}
		
		return this;
	}

	public void init() {
		final String methodName = CLASSNAME + "::init";  
		doInitialize();
	}

	private void doInitialize() {
	    // TODO: Implement Method doInitialize here  
	}  
  

 
    @Override
    public String replaceSchedulerVars(boolean isWindows, String pstrString2Modify) {
        LOGGER.debug("replaceSchedulerVars as Dummy-call executed. No Instance of JobUtilites specified.");
        return pstrString2Modify;
    }

 
    @Override
    public void setJSParam(String pstrKey, String pstrValue) {
        // Implement Method here  
    }

    @Override
    public void setJSParam(String pstrKey, StringBuffer pstrValue) {
        // Implement Method here  
    }

 
    public void setJSJobUtilites(JSJobUtilities pobjJSJobUtilities) {

        if (pobjJSJobUtilities == null) {
            objJSJobUtilities = this;
        }
        else {
            objJSJobUtilities = pobjJSJobUtilities;
        }
        LOGGER.debug("objJSJobUtilities = " + objJSJobUtilities.getClass().getName());
    }



}  

</xsl:template>

	<xsl:template match="text()">
		<!-- 	<xsl:value-of select="normalize-space(.)"/> -->
	</xsl:template>


</xsl:stylesheet>