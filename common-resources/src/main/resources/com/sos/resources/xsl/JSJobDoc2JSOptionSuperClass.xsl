<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:jobdoc="http://www.sos-berlin.com/schema/scheduler_job_documentation_v1.1"
    xmlns:xi="http://www.w3.org/2001/XInclude"
	xmlns:java="http://xml.apache.org/xslt/java" 
	xmlns:xhtml="http://www.w3.org/1999/xhtml"
	xmlns:functx="http://www.functx.com" 
	exclude-result-prefixes="jobdoc xhtml java">

	<xsl:output method="text" encoding="iso-8859-1" indent="no" />

<xsl:param name="timestamp" required="yes" as="xs:string"/> 
<xsl:param name="package_name" required="yes" as="xs:string"/> 
<xsl:param name="standalone" required="yes" as="xs:string"/> 
<xsl:param name="sourcetype" required="yes" as="xs:string"/> 
<xsl:param name="ExtendsClassName"  required="yes" as="xs:string"/>
<xsl:param name="XSLTFilename"  required="yes" as="xs:string"/>
<xsl:param name="XMLDocuFilename"  required="yes" as="xs:string"/>
<xsl:param name="keywords" required="no" as="xs:string"/>
<xsl:param name="Category" required="no" as="xs:string"/>
<xsl:param name="default_lang" required="yes" as="xs:string"/>



	<xsl:variable name="nl" select="'&#xa;'" />

    <xsl:template match="/">
    <xsl:message>Here we are ....</xsl:message>
        <xsl:apply-templates select="jobdoc:description"/>

 </xsl:template>
    <xsl:template match="//description">
    <xsl:message>//jobdoc:description</xsl:message>
        <xsl:apply-templates />
    </xsl:template>


	<xsl:template match="//jobdoc:description">
    <xsl:message>//jobdoc:description</xsl:message>
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="jobdoc:job">
		<xsl:variable name="real_class_name">
			<xsl:value-of select="concat(./@name, 'Options')" />
		</xsl:variable>


		<xsl:variable name="class_name">
			<xsl:value-of select="concat(./@name, 'OptionsSuperClass')" />
		</xsl:variable>

		<xsl:variable name="class_title">
			<xsl:value-of select="./@title" />
		</xsl:variable>

package <xsl:value-of select="$package_name" />;

import java.util.HashMap;

import com.sos.JSHelper.Options.*;
import com.sos.JSHelper.Annotations.JSOptionClass;
import com.sos.JSHelper.Annotations.JSOptionDefinition;
import com.sos.JSHelper.Exceptions.JSExceptionMandatoryOptionMissing;
import com.sos.JSHelper.Listener.JSListener; 
import org.apache.log4j.Logger;

import com.sos.JSHelper.Options.*;

<<<<<<< HEAD

=======
 
>>>>>>> origin/release/1.9
@JSOptionClass(name = "<xsl:value-of select="$class_name" />", description = "<xsl:value-of select="$class_name" />")
public class <xsl:value-of select="$class_name" /> extends JSOptionsClass {
	private final String					conClassName						= "<xsl:value-of select="$class_name" />";
		@SuppressWarnings("unused")
	private static Logger		logger			= Logger.getLogger(<xsl:value-of select="$class_name" />.class);

		<xsl:call-template name="CreateDataElements" />
<!-- <xsl:call-template name="CreateGetterAndSetter" />  -->        
        
	public <xsl:value-of select="$class_name" />() {
		objParentClass = this.getClass();
	} // public <xsl:value-of select="$class_name" />

	public <xsl:value-of select="$class_name" />(JSListener pobjListener) {
		this();
		this.registerMessageListener(pobjListener);
	} // public <xsl:value-of select="$class_name" />

		//

	public <xsl:value-of select="$class_name" /> (HashMap &lt;String, String&gt; JSSettings) throws Exception {
		this();
		this.setAllOptions(JSSettings);
	} // public <xsl:value-of select="$class_name" /> (HashMap JSSettings)
/**
 * \brief getAllOptionsAsString - liefert die Werte und Beschreibung aller
 * Optionen als String
 *
 * \details
 * 
 * \see toString 
 * \see toOut
 */
	private String getAllOptionsAsString() {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::getAllOptionsAsString";
		String strT = conClassName + "\n";
		final StringBuffer strBuffer = new StringBuffer();
		strT += this.toString(); // fix
	 
		return strT;
	} // private String getAllOptionsAsString ()

 
	public void setAllOptions(HashMap &lt;String, String&gt; pobjJSSettings)   {
		@SuppressWarnings("unused")
		final String conMethodName = conClassName + "::setAllOptions";
		flgSetAllOptions = true;
		objSettings = pobjJSSettings;
		super.Settings(objSettings);
		super.setAllOptions(pobjJSSettings);
		flgSetAllOptions = false;
	} // public void setAllOptions (HashMap &lt;String, String&gt; JSSettings)

 
		@Override
	public void CheckMandatory() throws JSExceptionMandatoryOptionMissing //
		, Exception {
		try {
			super.CheckMandatory();
		}
		catch (Exception e) {
			throw new JSExceptionMandatoryOptionMissing(e.toString());
		}
		} // public void CheckMandatory ()

 
	@Override
	public void CommandLineArgs(String[] pstrArgs)   {
		super.CommandLineArgs(pstrArgs);
		this.setAllOptions(super.objSettings);
	}
} // public class <xsl:value-of select="$class_name" />
	</xsl:template>

	<xsl:template match="jobdoc:configuration">
			<xsl:message>
			jobdoc:configuration reached ...
		</xsl:message>
	
<!-- 		<xsl:apply-templates />  -->
	</xsl:template>

	<xsl:template match="jobdoc:params1--">
		<xsl:for-each select="./jobdoc:param">
			<xsl:sort select="./@name" order="ascending" />
			<xsl:choose>
				<xsl:when test="@name and not(@name='') and not(@name='*')">
					<xsl:variable name="title">
						<xsl:value-of
							select="normalize-space(substring(./jobdoc:note[@language=$default_lang],2, 80 ))" />
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
                                <xsl:value-of select="@data_type" />
                            </xsl:when>
							<xsl:otherwise>
								<xsl:text>String</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>

					<xsl:variable name="initialvalue">
						<xsl:choose>
							<xsl:when test="@default_value and not(@default_value='')">
								<xsl:value-of select="@default_value" />
							</xsl:when>
                            <xsl:when test="@DefaultValue and not(@DefaultValue='')">
                                <xsl:value-of select="@default_value" />
                            </xsl:when>
							<xsl:otherwise>
								<xsl:text> </xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>

					<xsl:value-of
						select="concat(./@name,';',./@name,';',$title,';',$descr,';',./@required,';',$datatype,';',$initialvalue,';',./@default_value, $nl)" />
					<!--
						select="string-join((@name,@name,$title,$descr,@required,$datatype,$initialvalue,@default_value,$nl),';')"
						/>
					-->
				</xsl:when>
				<xsl:otherwise>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
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

<!-- 
	<xsl:function name="functx:capitalize-first" as="xs:string">
		<xsl:param name="arg" as="xs:string" />

  <xsl:sequence select=" 
   concat(upper-case(substring($arg,1,1)),
             substring($arg,2))
 " />

	</xsl:function>
-->

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
                                <xsl:text> </xsl:text>
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

 
    @JSOptionDefinition(name = "<xsl:value-of select="./@name" />", 
    description = "<xsl:value-of select="$title" />", 
    key = "<xsl:value-of select="./@name" />", 
    type = "<xsl:value-of select="$datatype" />", 
    mandatory = <xsl:value-of select="$mandatory" />)
    
    public <xsl:value-of select="concat($datatype,' ')" /> <xsl:value-of select="./@name" /> = new <xsl:value-of select="$datatype" />(this, conClassName + ".<xsl:value-of select="./@name" />", // HashMap-Key
                                                                "<xsl:value-of select="$title" />", // Titel
                                                                "<xsl:value-of select="$initialvalue" />", // InitValue
                                                                "<xsl:value-of select="$defaultvalue" />", // DefaultValue
                                                                <xsl:value-of select="$mandatory" /> // isMandatory
                    );

 
    public <xsl:value-of select="$datatype" /><xsl:text> </xsl:text> get<xsl:value-of select="./@name" />() {
        return <xsl:value-of select="./@name" />;
    }

 
    public void set<xsl:value-of select="concat(./@name, ' (', $datatype, ' p_',./@name, ') {')" /> 
        this.<xsl:value-of select="concat(./@name, ' = p_', ./@name, ';' )" />
    }

                        <xsl:choose>
                            <xsl:when test="@Alias and not(@Alias='')">
    public <xsl:value-of select="concat($datatype,' ')" /> <xsl:value-of select="./@Alias" /> =
    <xsl:value-of select="concat('(',$datatype,') ')" />  
    <xsl:value-of select="concat(./@name, '.SetAlias(conClassName + ')" />".<xsl:value-of select="./@Alias"/>"<xsl:value-of select="');'" />
                            </xsl:when>
                            <xsl:otherwise>
                            </xsl:otherwise>
                        </xsl:choose>

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