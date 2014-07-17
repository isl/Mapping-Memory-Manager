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
	<xsl:template name="paging">
		<xsl:param name="step"/>
		<xsl:param name="start"/>
		<xsl:param name="end"/>
		<xsl:param name="letter"/>
		<xsl:param name="count"/>
		<xsl:param name="url"/>
		<xsl:variable name="pre_tip">
			<xsl:choose>
				<xsl:when test="$start = $step">
					1 - <xsl:value-of select="$start - 1"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$start - $step"/> - <xsl:value-of select="$start - 1"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="next_tip">
			<xsl:choose>
				<xsl:when test="$end + $step &lt; $count">
					<xsl:value-of select="$end + 1"/> - <xsl:value-of select="$end + $step"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$end + 1"/> - <xsl:value-of select="$count"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<xsl:variable name="pre_start">
			<xsl:choose>
				<xsl:when test="$start - $step > 1">
					<xsl:value-of select="$start - $step - 1"/>
				</xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<xsl:if test="$start > 1">
			<a href="{$url}&amp;start={$pre_start}">
			<xsl:choose>
			<xsl:when test="$lang='ar'">
				<img src="formating/images/next.jpg" title="{$pre_tip}" alt="{$pre_tip}" border="0"/>
			</xsl:when>
			<xsl:otherwise>
				<img src="formating/images/previous.jpg" title="{$pre_tip}" alt="{$pre_tip}" border="0"/>
			</xsl:otherwise>
			</xsl:choose>
			</a>
		</xsl:if>
		<xsl:if test=" $step &lt; $count ">
			&#160;&#160;
			<!--<b><xsl:value-of select="$start"/> - <xsl:value-of select="$end"/> / <xsl:value-of select="$count"/></b>-->
			
			<span class="contentTitleText"><xsl:value-of select="$start"/> - <xsl:value-of select="$end"/> / <xsl:value-of select="$count"/></span>
			&#160;&#160;
		</xsl:if>
		<xsl:if test="$end &lt; $count">
			<a href="{$url}&amp;start={$end}">
			<xsl:choose>
			<xsl:when test="$lang='ar'">
				<img src="formating/images/previous.jpg" title="{$pre_tip}" alt="{$pre_tip}" border="0"/>
			</xsl:when>
			<xsl:otherwise>
				<img src="formating/images/next.jpg" title="{$next_tip}" alt="{$next_tip}" border="0"/>
			</xsl:otherwise>
			</xsl:choose>
				
			</a>
		</xsl:if>
		
	</xsl:template>
</xsl:stylesheet>