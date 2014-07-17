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
	<xsl:template name="head_html">
		<xsl:param name="title"/>
		<xsl:param name="javascript"/>
		<xsl:param name="css"/>
		<head>
			<title>
				<xsl:value-of select="//context/systemName/text()"/>
			</title>
			<script type="text/JavaScript" src="{$systemRoot}{$formatingDir}/javascript/storage/storage.js"></script>
			<script language="JavaScript">
				<xsl:attribute name="src"><xsl:value-of select="$javascript"/></xsl:attribute>
			</script>
			<link rel="stylesheet" type="text/css">
				<xsl:attribute name="href"><xsl:value-of select="$css"/></xsl:attribute>
			</link>
			
			<script language="JavaScript">
			<xsl:variable name="tag" select=" 'EpileksteProta' "/>
			var AlertEpileksteProta = '<xsl:value-of select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>' ; 
			<xsl:variable name="tag" select=" 'EgirosAri8mosSelidas' "/>
			var EgirosAri8mosSelidas = '<xsl:value-of select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>' ; 
			<xsl:variable name="tag" select=" 'nofreespace' "/>
			var nofreespace = '<xsl:value-of select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>' ;
                        <xsl:variable name="tag" select=" 'backupMsg' "/>
			var backupMsg = '<xsl:value-of select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>' ;
			
			</script> 

			
			<script language="JavaScript">
				<xsl:attribute name="src"><xsl:value-of select="concat($systemRoot, '/formating/javascript/utils/scripts.js')"/></xsl:attribute>
			</script>
			
		</head>
	</xsl:template>
</xsl:stylesheet>
