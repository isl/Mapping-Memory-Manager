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

<xsl:stylesheet xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xdt="http://www.w3.org/2005/02/xpath-datatypes" xmlns:fn="http://www.w3.org/2005/02/xpath-functions" version="2.0">
	<xsl:include href="header.xsl"/>
	<xsl:include href="footer.xsl"/>
	<xsl:include href="head_html.xsl"/>
	<xsl:include href="vars.xsl"/>
	<xsl:template name="loginPageTemplate">
		<html>
		<xsl:if test=" $lang='ar'">
		<xsl:attribute name="class">rtl</xsl:attribute>
		</xsl:if>
			<xsl:call-template name="head_html">
				<xsl:with-param name="title" select="$title"/>
				<xsl:with-param name="javascript" select="concat($systemRoot,'/formating/javascript/utils/scripts.js')"/>
				<xsl:with-param name="css" select="concat($systemRoot,'/formating/css/page.css')"/>
			</xsl:call-template>
			<body topmargin="0" leftmargin="0">
			<!--............<xsl:value-of select="$lang"/>-->
				<table border="0" width="1000" cellspacing="0">
					<tbody>
						<xsl:call-template name="header">
							<xsl:with-param name="name" select="$name"/>
							<xsl:with-param name="logo" select="$logo"/>
						</xsl:call-template>					
						<tr>
							<!--xsl:call-template name="leftmenu"/-->
							<xsl:call-template name="context"/>
						</tr>
						<xsl:call-template name="footer">
							<xsl:with-param name="text" select="$text"/>
							<xsl:with-param name="user" select="$user"/>
						</xsl:call-template>
					</tbody>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
