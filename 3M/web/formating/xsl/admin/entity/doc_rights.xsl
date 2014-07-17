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
	<xsl:include href="../../ui/page.xsl"/>

	<xsl:variable name="CodeValue" select="//context/CodeValue"/>
	<xsl:variable name="MainCurrentName" select="//context/MainCurrentName"/>
	
	<xsl:variable name="Users" select="//context/user"/>

	<xsl:variable name="FileId" select="//context/FileId"/>

	<xsl:variable name="EntityType" select="//context/EntityType"/>
	<xsl:variable name="AdminAction" select="//context/AdminAction"/>

    <xsl:template match="/">
        <xsl:call-template name="page"/>
    </xsl:template>
    <xsl:template name="context">
		<td colSpan="{$columns}" vAlign="top" align="center" class="content">
			<xsl:variable name="tag" select=" 'DiaxeirisiDikaiwmatwn' "/>
			<xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
			<br/>
			<p class="contentText" style="font-size:11pt">
				<b><xsl:value-of select="$translated"/></b>
			</p>
			<form id="userForm" method="post" action="AdminEntity?type={$EntityType}&amp;action=setrights&amp;id={$FileId}" style="margin-bottom:0px;">
				<table width="100%" class="contentText">
					<tr>
						<xsl:variable name="tag" select=" 'ΤιμήΚωδικού' "/>
						<xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
						<td width="20%" align="right"><xsl:value-of select="$translated"/></td>
						<td><input type="text" id="codeValue" name="codeValue" style="width:400px" value="{$CodeValue}" disabled="disabled"></input></td>
					</tr>
					<tr>
						<xsl:variable name="tag" select=" concat ('PrimaryInsertField',$EntityType) "/>
						<xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
						<td width="20%" align="right"><xsl:value-of select="$translated"/></td>
						<td><input type="text" id="mainCurrentName" name="mainCurrentName" style="width:400px" value="{$MainCurrentName}" disabled="disabled"></input></td>
					</tr>
					<tr>
						<td colspan="2"><hr size="1"/></td>
					</tr>
					<tr>
						<xsl:variable name="tag" select=" 'Dikaiwma' "/>
						<xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
						<td width="20%" align="right"><xsl:value-of select="$translated"/>:</td>
						<td>
							<xsl:variable name="tag" select=" 'Write' "/>
							<xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
							<b><xsl:value-of select="$translated"/></b>
						</td>
					</tr>
					<tr>
						<td align="right">
							<br/>
							<xsl:variable name="tag" select=" 'Xristes' "/>
							<xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
							<p class="contentText">
								<b><xsl:value-of select="$translated"/></b>
							</p>
						</td>
						<td></td>
					</tr>
					<xsl:for-each select="$Users">
						<tr>
							<td align="right">
								<input type="checkbox" id="username" name="username" value="{./text()}">
									<xsl:if test=" ./@write='true' ">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</input>
							</td>
							<td><xsl:value-of select="./text()"/></td>
						</tr>
					</xsl:for-each>
					<tr>
						<td></td>
						<td>
							<br/>
							<input type="hidden" name="lang" value="{$lang}"/>
							<xsl:variable name="tag" select=" 'Oloklirwsi' "/>
							<xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
							<input type="submit" class="button" value="{$translated}"></input>	
						</td>
					</tr>
				</table>
			</form>
			<br/>
		</td>
	</xsl:template>
</xsl:stylesheet>