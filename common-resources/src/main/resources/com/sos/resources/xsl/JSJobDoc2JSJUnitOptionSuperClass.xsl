<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:jobdoc="http://www.sos-berlin.com/schema/scheduler_job_documentation_v1.1"
    xmlns:xi="http://www.w3.org/2001/XInclude"
	xmlns:xhtml="http://www.w3.org/1999/xhtml"
	exclude-result-prefixes="jobdoc xhtml">

	<xsl:output method="text" encoding="iso-8859-1" indent="no" />

<xsl:param name="timestamp" required="no" as="xs:string"/> 
<xsl:param name="package_name" required="no" as="xs:string"/> 
<xsl:param name="standalone" required="no" as="xs:string"/> 
<xsl:param name="sourcetype" required="no" as="xs:string"/> 
<xsl:param name="ExtendsClassName"  required="no" as="xs:string"/>
<xsl:param name="ClassName"  required="no" as="xs:string"/>
<xsl:param name="WorkerClassName"  required="no" as="xs:string"/>
<xsl:param name="XSLTFilename"  required="no" as="xs:string"/>
<xsl:param name="XMLDocuFilename"  required="no" as="xs:string"/>
<xsl:param name="keywords" required="no" as="xs:string"/>
<xsl:param name="Category" required="no" as="xs:string"/>
<xsl:param name="default_lang"   required="yes" as="xs:string"/>


	<xsl:variable name="nl" select="'&#xa;'" />

	<xsl:template match="/jobdoc:description">
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="jobdoc:job">
		<xsl:variable name="real_class_name">
			<xsl:value-of select="concat(./@name, 'Options')" />
		</xsl:variable>

		<xsl:variable name="class_title">
			<xsl:value-of select="./@title" />
		</xsl:variable>

package <xsl:value-of select="$package_name" />;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.JSHelper.Listener.JSListenerClass;

<xsl:copy-of select="//jobdoc:description/documentation[language=$default_lang]" />
 
public class <xsl:value-of select="$ClassName" /> extends  <xsl:value-of select="$ExtendsClassName"/> {
	private static final int DEBUG9 = 9;
	
	protected <xsl:value-of select="$WorkerClassName" />Options	objOptions = null;

	private static final String CLASSNAME = "<xsl:value-of select="$ClassName" />"; 
	private static final Logger LOGGER = Logger.getLogger(<xsl:value-of select="$ClassName" />.class);
	private <xsl:value-of select="$WorkerClassName" /> objE = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
        // TODO: Implement Method setUpBeforeClass here
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
        // TODO: Implement Method tearDownAfterClass here
	}

	@Before
	public void setUp() throws Exception {
		objE = new <xsl:value-of select="$WorkerClassName" />();
		objE.registerMessageListener(this);
		objOptions = objE.getOptions();
		objOptions.registerMessageListener(this);
		JSListenerClass.bolLogDebugInformation = true;
		JSListenerClass.intMaxDebugLevel = DEBUG9;
	}

	@After
	public void tearDown() throws Exception {
        // Implement Method here
	}

	<xsl:call-template name="CreateDataElements" />
		       
}  
	</xsl:template>

	<xsl:template match="jobdoc:configuration">
	
<!-- 		<xsl:apply-templates />  -->
	</xsl:template>

	<xsl:template name="CreateDataElements">
        <xsl:for-each select="//xi:include">
            <xsl:sort select="./@href" order="ascending" />
            <xsl:apply-templates select="." mode="resolve" />
        </xsl:for-each>
        
		<xsl:for-each select="//jobdoc:param">
			<xsl:sort select="./@name" order="ascending" />
			<xsl:apply-templates select="." />
		</xsl:for-each>
	</xsl:template>

    <xsl:template name="CreateGetterAndSetter">
        <xsl:for-each select="//xi:include">
            <xsl:sort select="./@href" order="ascending" />
            <xsl:apply-templates select="." mode="CreateGetterAndSetter" />
        </xsl:for-each>
        
        <xsl:for-each select="//jobdoc:param">
            <xsl:sort select="./@name" order="ascending" />
            <xsl:apply-templates select="."  mode="CreateGetterAndSetter" />
        </xsl:for-each>
    </xsl:template>


	<xsl:template match="text()">
		<!-- 	<xsl:value-of select="normalize-space(.)"/> -->
	</xsl:template>
 
<xsl:template match="jobdoc:param"> 
        <xsl:message>
        <xsl:value-of select="concat('Parameter = ', ./@name)"></xsl:value-of>
        </xsl:message>

            <xsl:choose>
                <xsl:when test="@name and not(@name='') and not(@name='*')">
			        <xsl:message>
			        ..... process param ....<xsl:value-of select="concat(@name, ' ', @DataType, ' ', @DefaultValue)"></xsl:value-of>
			        </xsl:message>

                    <xsl:variable name="title">
                        <xsl:value-of
                            select="normalize-space(substring(./jobdoc:note[@language=$default_lang and position()=1],2, 80 ))" />
                    </xsl:variable>
                    <xsl:variable name="descr">
                        <xsl:value-of
                            select="normalize-space(substring(./jobdoc:note[@language=$default_lang],2, 80 ))" />
                    </xsl:variable>

                    <xsl:variable name="datatype">
                        <xsl:choose>
                            <xsl:when test="@data_type and not(@data_type='')">
                                <xsl:value-of select="@data_type" />
                            </xsl:when>
                            <xsl:when test="@DataType and not(@DataType='')">
                                <xsl:value-of select="@DataType" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>SOSOptionString</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>

                    <xsl:variable name="initialvalue">
                        <xsl:choose>
                            <xsl:when test="@default_value and not(@default_value='')">
                                <xsl:value-of select="@default_value" />
                            </xsl:when>
                            <xsl:when test="@DefaultValue and not(@DefaultValue='')">
                                <xsl:value-of select="@DefaultValue" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text> </xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>

                    <xsl:variable name="defaultvalue">
                        <xsl:choose>
                            <xsl:when test="@default_value and not(@default_value='')">
                                <xsl:value-of select="@default_value" />
                            </xsl:when>
                            <xsl:when test="@DefaultValue and not(@DefaultValue='')">
                                <xsl:value-of select="@DefaultValue" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>----</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>

                    <xsl:variable name="mandatory">
                        <xsl:choose>
                            <xsl:when test="@required and not(@required='')">
                                <xsl:value-of select="@required" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>false</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>

                    <xsl:variable name="dataclass">
                        <xsl:choose>
                            <xsl:when test="@DataType and not(@DataType='')">
                                <xsl:value-of select="concat('SOSOption', @DataType)" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:text>SOSOptionString</xsl:text>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>
 
    @Test
    public void test<xsl:value-of select="./@name" />() { 
    <xsl:choose>
    	<xsl:when test="$datatype = 'SOSOptionString'">
    	 objOptions.<xsl:value-of select="./@name" />.setValue("<xsl:value-of select="concat('++', $defaultvalue, '++')" />");
    	 assertEquals ("<xsl:value-of select="$title" />", objOptions.<xsl:value-of select="./@name" />.getValue(),"<xsl:value-of select="concat('++', $defaultvalue, '++')" />");
    	</xsl:when>
    	<xsl:when test="$datatype = 'SOSOptionInteger'">
    	 objOptions.<xsl:value-of select="./@name" />.setValue("12345");
    	 assertEquals ("<xsl:value-of select="$title" />", objOptions.<xsl:value-of select="./@name" />.getValue(),"12345");
    	 assertEquals ("<xsl:value-of select="$title" />", objOptions.<xsl:value-of select="./@name" />.value(),12345);
    	 objOptions.<xsl:value-of select="./@name" />.value(12345);
    	 assertEquals ("<xsl:value-of select="$title" />", objOptions.<xsl:value-of select="./@name" />.getValue(),"12345");
    	 assertEquals ("<xsl:value-of select="$title" />", objOptions.<xsl:value-of select="./@name" />.value(),12345);
    	</xsl:when>
    	<xsl:when test="$datatype = 'SOSOptionFileSize'">
    	 objOptions.<xsl:value-of select="./@name" />.setValue("25KB");
    	 assertEquals ("<xsl:value-of select="$title" />", objOptions.<xsl:value-of select="./@name" />.getValue(),"25KB");
    	 objOptions.<xsl:value-of select="./@name" />.setValue("25MB");
    	 assertEquals ("<xsl:value-of select="$title" />", objOptions.<xsl:value-of select="./@name" />.getValue(),"25MB");
    	 objOptions.<xsl:value-of select="./@name" />.setValue("25GB");
    	 assertEquals ("<xsl:value-of select="$title" />", objOptions.<xsl:value-of select="./@name" />.getValue(),"25GB");
    	</xsl:when>
    	<xsl:when test="$datatype = 'SOSOptionTime'">
    	 objOptions.<xsl:value-of select="./@name" />.setValue("30");
    	 assertEquals ("<xsl:value-of select="$title" />", objOptions.<xsl:value-of select="./@name" />.getValue(),"30");
    	 assertEquals ("<xsl:value-of select="$title" />", objOptions.<xsl:value-of select="./@name" />.getTimeAsSeconds(),30);
    	 objOptions.<xsl:value-of select="./@name" />.setValue("1:30");
    	 assertEquals ("<xsl:value-of select="$title" />", objOptions.<xsl:value-of select="./@name" />.getValue(),"1:30");
    	 assertEquals ("<xsl:value-of select="$title" />", objOptions.<xsl:value-of select="./@name" />.getTimeAsSeconds(),90);
    	 objOptions.<xsl:value-of select="./@name" />.setValue("1:10:30");
    	 assertEquals ("<xsl:value-of select="$title" />", objOptions.<xsl:value-of select="./@name" />.getValue(),"1:10:30");
    	 assertEquals ("<xsl:value-of select="$title" />", objOptions.<xsl:value-of select="./@name" />.getTimeAsSeconds(),30+10*60+60*60);
    	</xsl:when>
    	<xsl:when test="$datatype = 'SOSOptionBoolean'">
    	 objOptions.<xsl:value-of select="./@name" />.setValue("true");
    	 assertTrue ("<xsl:value-of select="$title" />", objOptions.<xsl:value-of select="./@name" />.value());
    	 objOptions.<xsl:value-of select="./@name" />.setValue("false");
    	 assertFalse ("<xsl:value-of select="$title" />", objOptions.<xsl:value-of select="./@name" />.value());
    	</xsl:when>
    	<xsl:otherwise>
    	objOptions.<xsl:value-of select="./@name" />.setValue("<xsl:value-of select="concat('++', $defaultvalue, '++')" />");
    	assertEquals ("<xsl:value-of select="$title" />", objOptions.<xsl:value-of select="./@name" />.getValue(),"<xsl:value-of select="concat('++', $defaultvalue, '++')" />");
    	</xsl:otherwise>
    </xsl:choose>
    }
 
                </xsl:when>
                <xsl:otherwise>
                </xsl:otherwise>
            </xsl:choose>

</xsl:template>

<xsl:template match="xi:include" xmlns:xi="http://www.w3.org/2001/XInclude" mode = "resolve">
	<xsl:for-each select="document(@href)">
    	<xsl:apply-templates select="./jobdoc:param" />
	</xsl:for-each>
</xsl:template>

</xsl:stylesheet>