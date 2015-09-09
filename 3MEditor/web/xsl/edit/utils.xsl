
<!--
Copyright 2014 Institute of Computer Science,
Foundation for Research and Technology - Hellas

Licensed under the EUPL, Version 1.1 or - as soon they will be approved
by the European Commission - subsequent versions of the EUPL (the "Licence");
You may not use this work except in compliance with the Licence.
You may obtain a copy of the Licence at:

http://ec.europa.eu/idabc/eupl

Unless required by applicable law or agreed to in writing, software distributed
under the Licence is distributed on an "AS IS" basis,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the Licence for the specific language governing permissions and limitations
under the Licence.

Contact:  POBox 1385, Heraklio Crete, GR-700 13 GREECE
Tel:+30-2810-391632
Fax: +30-2810-391638
E-mail: isl@ics.forth.gr
http://www.ics.forth.gr/isl

Authors : Georgios Samaritakis, Konstantina Konsolaki.

This file is part of the 3MEditor webapp of Mapping Memory Manager project.

-->
<xsl:stylesheet version="1.0"  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output omit-xml-declaration="yes" indent="yes"/>
    <xsl:strip-space elements="*"/>

    <xsl:variable name="vApos">'</xsl:variable>

    <xsl:template name="getPath">
        <xsl:param name="nodeSet"></xsl:param>
        <xsl:apply-templates select="$nodeSet"/>
    </xsl:template>

    <xsl:template match="*[@* or not(*)] ">
        <xsl:if test="not(*)">
            <xsl:apply-templates select="ancestor-or-self::*" mode="path"/>
            <!--            <xsl:value-of select="concat('=',$vApos,.,$vApos)"/>-->
            <xsl:text>&#xA;</xsl:text>
        </xsl:if>
        <xsl:apply-templates select="@*|*"/>
    </xsl:template>

    <xsl:template match="*" mode="path">
        <xsl:value-of select="concat('/',name())"/>
        <xsl:variable name="vnumPrecSiblings" select=
         "count(preceding-sibling::*[name()=name(current())])"/>
        <xsl:if test="$vnumPrecSiblings">
            <xsl:value-of select="concat('[', $vnumPrecSiblings +1, ']')"/>
        </xsl:if>
    </xsl:template>

    <xsl:template match="@*">
        <xsl:apply-templates select="../ancestor-or-self::*" mode="path"/>
        <!--        <xsl:value-of select="concat('[@',name(), '=',$vApos,.,$vApos,']')"/>-->
        <xsl:value-of select="concat('[@',name(),']')"/>

        <xsl:text>&#xA;</xsl:text>
    </xsl:template>
</xsl:stylesheet>