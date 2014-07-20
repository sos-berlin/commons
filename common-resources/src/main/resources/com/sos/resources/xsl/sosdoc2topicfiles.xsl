<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- $Id: sosdoc2topicfiles.xsl 17903 2012-08-26 07:56:24Z kb $ -->
<!-- 
12.12.2011

2 neue Stylesheets in J:\Data\doku\ant-docu-helper :

    * oldsosdoc2draft.xsl
    * sosdoc2topicfiles.xsl

Hierfür gibt es entsprechende Targets in .\ant-docu-helper\docu-targets.ant:

    * oldsosdoc2draft
    * sosdoc2topicfiles


oldsosdoc2draft.xsl generiert [project.name].sosdoc.draft aus [project.name].xml,
wobei alle p-, title-, subtitle- und glossaryTerm/name-Elemente language-Attribute erhalten und gedoppelt (eins für 'en' und eins für 'de') werden.
Bei den p-Elementen werden nur die toplevel p-Elemente gedoppelt.
In welches der doppelten Elemente der ursprüngliche Text geschrieben wird, hängt von -Dlang im ant-Aufruf ab (default 'en').
Im glossaryTerm-Element wird das id-Attribute hinzugefügt und mit glossaryTerm/name/text() gefüllt.
/documentation/subtitle wird für 'en' und 'de' ggf. neu erzeugt.

sosdoc2topicfiles.xsl generiert für jedes Topic bzw. GlossaryTerm <xi:include>-Elemente und speichert die Topics und GlossaryTerms in
./topics, ./appendices und ./glossary. Verzeichnisse und Extension der erzeugten Dateien ist parametrisierbar (siehe ./ant-docu-helper/docu-targets.ant).
Achtung: GlossaryTerms benötigen ebenfalls ein id-Attribute.

Gruß Olli
 -->

<!DOCTYPE stylesheet [
    <!ENTITY nbsp "&#160;">
]>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xi="http://www.w3.org/2001/XInclude"
                xmlns:xhtml="http://www.w3.org/1999/xhtml"
                version="2.0">
                
        <xsl:output method="xml" encoding="ISO-8859-1" indent="yes" standalone="yes"
                  cdata-section-elements="pre code" use-character-maps="entities"/>
        <xsl:strip-space elements="*"/>
        
        <xsl:character-map name="entities">
            <xsl:output-character character="&#160;" string="&amp;#160;"/>
            <xsl:output-character character="&nbsp;" string="&amp;#160;"/>   
        </xsl:character-map>                  
        
        <xsl:param name="topics.dir" select="'topics'" />
        <xsl:param name="topics.extension" select="'.topic.sosdoc'" />
        <xsl:param name="appendices.dir" select="'appendices'" />
        <xsl:param name="appendices.extension" select="'.topic.sosdoc'" />
        <xsl:param name="glossary.dir" select="'glossary'" />
        <xsl:param name="glossary.extension" select="'.glossary.sosdoc'" />
        <xsl:param name="filename" />
        <xsl:param name="filedir" />
        
        <xsl:template match="/documentation">
            <xsl:message>Parameter topics.dir            = <xsl:value-of select="$topics.dir" /></xsl:message>
            <xsl:message>Parameter topics.extension      = <xsl:value-of select="$topics.extension" /></xsl:message>
            <xsl:message>Parameter appendices.dir        = <xsl:value-of select="$appendices.dir" /></xsl:message>
            <xsl:message>Parameter appendices.extension  = <xsl:value-of select="$appendices.extension" /></xsl:message>
            <xsl:message>Parameter glossary.dir          = <xsl:value-of select="$glossary.dir" /></xsl:message>
            <xsl:message>Parameter glossary.extension    = <xsl:value-of select="$glossary.extension" /></xsl:message>
            <xsl:if test="//topic[not(@id)] or //glossaryTerm[not(@id)]">
                <xsl:message terminate="yes">There are some topic or glossaryTerm elements without an id attribute in <xsl:value-of select="concat($filedir,'/',$filename)" /></xsl:message>
            </xsl:if>
            <xsl:copy>
                <xsl:apply-templates select="*|@*|comment()|text()" />
            </xsl:copy>
        </xsl:template>
        
        <xsl:template match="topic|glossaryTerm">
            <xsl:variable name="file">
                <xsl:call-template name="setfilename"/>
            </xsl:variable>
            <xsl:choose>
                <xsl:when test="$file">
                    <xsl:element name="xi:include">
						            <xsl:choose>
						                <xsl:when test="parent::topic"> 
						                        <xsl:attribute name="href"><xsl:value-of select="concat('../',$file)" /></xsl:attribute>
						                		<xsl:message>Folder selected: <xsl:value-of select="'../'" /></xsl:message>
						                </xsl:when>
						                <xsl:otherwise>
						                        <xsl:attribute name="href"><xsl:value-of select="concat('./',$file)" /></xsl:attribute>
						                		<xsl:message>Folder selected: <xsl:value-of select="'./'" /></xsl:message>
						                </xsl:otherwise>
						            </xsl:choose>
                        
                        <xsl:attribute name="parse">xml</xsl:attribute>
                    </xsl:element>
                    <xsl:result-document href="{$file}" method="xml" encoding="ISO-8859-1" 
                       output-version="1.0" indent="yes" cdata-section-elements="pre code">
                       <xsl:comment>Created by sosdoc2topicfiles</xsl:comment>
                       <xsl:comment>$Id: sosdoc2topicfiles.xsl 17903 2012-08-26 07:56:24Z kb $</xsl:comment>
                        <xsl:copy>
                            <xsl:apply-templates select="*|@*|comment()|text()"/>
                        </xsl:copy>  
                        
                    </xsl:result-document>
                    <xsl:message>File created: <xsl:value-of select="$file" /></xsl:message>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="."/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:template>
        
        
        <xsl:template match="*|@*|comment()|text()">
            <xsl:copy>
                <xsl:apply-templates select="*|@*|comment()|text()"/>
            </xsl:copy>    
        </xsl:template>
        
        <xsl:template name="setfilename">
            <xsl:choose>
                <xsl:when test="ancestor::glossary">
                    <xsl:value-of select="concat($glossary.dir,'/',@id,$glossary.extension)" />
                </xsl:when>
                <xsl:when test="ancestor::topics">
                    <xsl:value-of select="concat($topics.dir,'/',@id,$topics.extension)" />
                </xsl:when>
                <xsl:when test="ancestor::appendices">
                    <xsl:value-of select="concat($appendices.dir,'/',@id,$appendices.extension)" />
                </xsl:when>
            </xsl:choose>
        </xsl:template>
        
</xsl:stylesheet>