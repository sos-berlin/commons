<?xml version="1.0" encoding="utf-8"?>
<!-- $Id: freemind2topicfiles.xsl 19712 2013-03-25 18:32:57Z kb $ -->

<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xi="http://www.w3.org/2001/XInclude"
    xmlns:xhtml="http://www.w3.org/1999/xhtml"
    xmlns:xd="http://www.pnp-software.com/XSLTdoc"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:noNamespaceSchemaLocation="http://www.sos-berlin.com/schema/sos_docu.xsd"

    version="2.0">

    <xsl:output method="xml" encoding="utf-8" indent="yes" standalone="no" doctype-system="../../local_entities.dtd" />
    <xsl:strip-space elements="*" />

    <xsl:param name="topic.depth" select="4" />
    <xsl:param name="topics.dir" select="'topics'" />
    <xsl:param name="topics.temp.dir" select="'mm.topics'" />

    <xsl:param name="topics.extension" select="'.topic.sosdoc'" />
    <xsl:param name="topics.root" select="'.topic.sosdoc'" />
    <xsl:param name="filename" />
    <xsl:param name="filedir" />


    <xsl:template match="/map">
        <xsl:message>
            Parameter topic.depth = <xsl:value-of select="$topic.depth" />
        </xsl:message>
        <xsl:message>
            Parameter topics.dir = <xsl:value-of select="$topics.dir" />
        </xsl:message>
        <xsl:message>
            Parameter topics.extension = <xsl:value-of select="$topics.extension" />
        </xsl:message>
        <xsl:apply-templates select="node" />
    </xsl:template>

    <xsl:template match="/map/node">
        <xsl:result-document  href="{$topics.root}" method="xml" encoding="utf-8" output-version="1.0" indent="yes" standalone="no" doctype-system="../../local_entities.dtd" >

        <documentation     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:noNamespaceSchemaLocation="http://www.sos-berlin.com/schema/sos_docu.xsd"

        >
        <xsl:comment>
        $Id: freemind2topicfiles.xsl 19712 2013-03-25 18:32:57Z kb $
        </xsl:comment>
            <title language="en">
                <xsl:value-of select="attribute[@NAME='title en']/@VALUE" />
            </title>
            <title language="de">
                <xsl:value-of select="attribute[@NAME='title de']/@VALUE" />
            </title>

            <title2 language="en">
                <xsl:value-of select="attribute[@NAME='title en']/@VALUE" />
            </title2>
            <title2 language="de">
                <xsl:value-of select="attribute[@NAME='title de']/@VALUE" />
            </title2>

            <subtitle language="en">
                <xsl:value-of select="attribute[@NAME='subtitle en']/@VALUE" />
            </subtitle>
            <subtitle language="de">
                <xsl:value-of select="attribute[@NAME='subtitle de']/@VALUE" />
            </subtitle>
            <author>
                <name>
                    <xsl:value-of select="attribute[@NAME='author name']/@VALUE" />
                </name>
                <email>
                    <xsl:value-of select="attribute[@NAME='author email']/@VALUE" />
                </email>
            </author>
            <xi:include href="../../globals/company.sosdoc" parse="xml" />
            <topics>
                <xsl:apply-templates select="node">
                    <xsl:sort select="attribute[@NAME='order']/@Value" data-type="number" />
                </xsl:apply-templates>
            </topics>
        </documentation>
        </xsl:result-document>
    </xsl:template>

    <xsl:template match="node">
        <xsl:variable name="topicname">
            <xsl:choose>
                <xsl:when test="attribute[@NAME = 'Filename']/@VALUE != ''">
                    <xsl:value-of select="attribute[@NAME = 'Filename']/@VALUE" />
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="concat(@TEXT,'-', @ID)" />
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <xsl:variable name="file" select="concat($topicname,$topics.extension)" />
        <xsl:variable name="topicFileName" select="concat($topics.temp.dir,'/', $file)" />
        <xsl:variable name="tempFileName" select="concat($topics.temp.dir,'/', $file)" />

        <xsl:message>
            <xsl:value-of select="concat('File created: ', $topicFileName)" />
        </xsl:message>

        <xsl:element name="xi:include">
<!--            <xsl:attribute name="href"><xsl:value-of select="concat('./',$topics.temp.dir,'/',$file)" /></xsl:attribute>
 -->
            <xsl:attribute name="href"><xsl:value-of select="concat('./',$file)" /></xsl:attribute>
            <xsl:attribute name="parse" select="'xml'" />
            <xsl:element name="fallback">
                <xsl:value-of select="concat('./',$file)" />
            </xsl:element>
        </xsl:element>

        <xsl:result-document  href="{$topicFileName}" method="xml" encoding="utf-8" output-version="1.0" indent="yes" standalone="no" doctype-system="../../../local_entities.dtd" >
        <topic      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xsi:noNamespaceSchemaLocation="http://www.sos-berlin.com/schema/sos_docu.xsd"
        >
                <xsl:attribute name="id" select="$topicname" />
                <xsl:comment>
        $Id: freemind2topicfiles.xsl 19712 2013-03-25 18:32:57Z kb $
        </xsl:comment>

                <xsl:call-template name="create_topic" />
                <xsl:apply-templates select="node">
                    <xsl:sort select="attribute[@NAME='order']/@VALUE" data-type="number" />
                </xsl:apply-templates>
                </topic>
        </xsl:result-document>

        <xsl:message>
            <xsl:value-of select="concat('File created: ', $tempFileName)" />
        </xsl:message>
    </xsl:template>

    <xsl:template name="create_topic">

        <title language="en">
            <xsl:choose>
                <xsl:when test="attribute[@NAME='title en']/@VALUE">
                    <xsl:value-of select="attribute[@NAME='title en']/@VALUE" />
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="@TEXT" />
                </xsl:otherwise>
            </xsl:choose>
        </title>
        <title language="de">
            <xsl:choose>
                <xsl:when test="attribute[@NAME='title de']/@VALUE">
                    <xsl:value-of select="attribute[@NAME='title de']/@VALUE" />
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="@TEXT" />
                </xsl:otherwise>
            </xsl:choose>
        </title>
        <description>
            <p language="en">
            ...
            <!--
<p>Far far away, behind the word mountains, far from the countries Vokalia and Consonantia, there live the blind texts. Separated they live in Bookmarksgrove right at the coast of the Semantics, a large language ocean. A small river named Duden flows by their place and supplies it with the necessary regelialia.</p>
<p>It is a paradisematic country, in which roasted parts of sentences fly into your mouth. Even the all-powerful Pointing has no control about the blind texts it is an almost unorthographic life One day however a small line of blind text by the name of Lorem Ipsum decided to leave for the far World of Grammar.</p>
<p>The Big Oxmox advised her not to do so, because there were thousands of bad Commas, wild Question Marks and devious Semikoli, but the Little Blind Text didn’t listen. She packed her seven versalia, put her initial into the belt and made herself on the way.</p>
<p>When she reached the first hills of the Italic Mountains, she had a last view back on the skyline of her hometown Bookmarksgrove, the headline of Alphabet Village and the subline of her own road, the Line Lane. Pityful a rethoric question ran over her cheek, then she continued her way.</p>
<p>On her way she met a copy. The copy warned the Little Blind Text, that where it came from it would have been rewritten a thousand times and everything that was left from its origin would be the word "and" and the Little Blind Text should turn around and return to its own, safe country. But nothing the copy said could convince her and so it didn’t take long until a few insidious Copy Writers ambushed her, made her drunk with Longe and Parole and dragged her into their agency, where they abused her for their</p>
             -->
 </p>
            <p language="de">
            ...
            <!--
<p>Ich bin Blindtext. Von Geburt an. Es hat lange gedauert, bis ich begriffen habe, was es bedeutet, ein blinder Text zu sein: Man macht keinen Sinn. Man wirkt hier und da aus dem Zusammenhang gerissen. Oft wird man gar nicht erst gelesen. Aber bin ich deshalb ein schlechter Text?</p>
<p>Ich weiß, dass ich nie die Chance haben werde im Stern zu erscheinen. Aber bin ich darum weniger wichtig? Ich bin blind! Aber ich bin gerne Text. Und sollten Sie mich jetzt tatsächlich zu Ende lesen, dann habe ich etwas geschafft, was den meisten "normalen" Texten nicht gelingt. Ich bin Blindtext.</p>
<p>Von Geburt an. Es hat lange gedauert, bis ich begriffen habe, was es bedeutet, ein blinder Text zu sein: Man macht keinen Sinn. Man wirkt hier und da aus dem Zusammenhang gerissen. Oft wird man gar nicht erst gelesen. Aber bin ich deshalb ein schlechter Text? Ich weiß, dass ich nie die Chance haben werde im Stern zu erscheinen. Aber bin ich darum weniger wichtig? Ich bin blind! Aber ich bin gerne Text. Und sollten Sie mich jetzt tatsächlich zu Ende lesen, dann habe ich etwas geschafft, was den meisten "normalen" Texten nicht gelingt. Ich bin Blindtext. Von</p>
             -->
            </p>
        </description>
    </xsl:template>

</xsl:stylesheet>
