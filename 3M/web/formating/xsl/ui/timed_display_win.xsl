<?xml version="1.0" encoding="UTF-8"?>
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

This file is part of the 3M webapp of Mapping Memory Manager project.

-->

<xsl:stylesheet xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xdt="http://www.w3.org/2005/02/xpath-datatypes" xmlns:fn="http://www.w3.org/2005/02/xpath-functions" version="2.0">
    <xsl:output method="html" indent="yes" encoding="UTF-8"/>

    <xsl:variable name="systemRoot" select="//page/systemRoot"/>
    <xsl:variable name="locale" select="document(concat($systemRoot, '/formating/multi_lang.xml'))/locale"/>
    <xsl:variable name="lang" select="//page/@language"/>
	
    <xsl:variable name="Display" select="//context/Display"/>
    <xsl:variable name="Time" select="//context/Time"/>
    <xsl:template name="displaycontext" match="/">
        <html>
            <head>
                <xsl:variable name="tag" select=" 'Info' "/>
                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                <title><xsl:value-of select="$translated"/></title>
                <script language="JavaScript" src="formating/javascript/utils/scripts.js"></script>
                <link rel="stylesheet" type="text/css" href="formating/css/page.css"></link>
                
                
            </head>
            <body onLoad="setTimeout('self.close()','{$Time}')">
                <table width="100%">
                    <tr>
                        <td valign="top" class="content">
                            <br/>
                            <p align="center" class="contentText" style="border:1px solid #777799">
                                <xsl:choose>
                                    <xsl:when test=" $Display='DOC_UNLOCKING' ">
                                        <xsl:variable name="tag" select=" $Display "/>
                                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                        <b><xsl:value-of select="$translated"/></b>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <b><xsl:value-of select="$Display"/></b>
                                    </xsl:otherwise>
                                </xsl:choose>
                                <br/><br/>
                            </p>                    
                            <br/><br/><br/><br/><br/>
                        </td>
                    </tr>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>