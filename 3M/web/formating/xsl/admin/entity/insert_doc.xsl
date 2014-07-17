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

    <xsl:variable name="EntityType" select="//context/EntityType"/>
    <xsl:variable name="AdminAction" select="//context/AdminAction"/>
    <xsl:variable name="AdminMode" select="//context/AdminMode"/>
	
    <xsl:variable name="Organization" select="//context/result/Organization/Organization"/>
    <xsl:variable name="OrgId" select="//context/result/Group/group"/> <!-- einai mikra ta teleutaia epeidh etsi einai sto arxei DMSUsers.xml -->

    <xsl:template match="/">
        <xsl:call-template name="page"/>
    </xsl:template>
    <xsl:template name="context">
        <td colSpan="{$columns}" vAlign="top"  class="content">
            <br/>
            <br/>
            <form id="userForm" method="post" action="AdminEntity?type={$EntityType}&amp;action=insert&amp;mode={$AdminMode}" style="margin-bottom:0px;">
                <table width="100%" class="contentText">
                    <!-- uri implemantation. H timi kwdiku einai pleon to uriId.-->
                    <!--tr>
                        <xsl:variable name="tag" select=" 'ΤιμήΚωδικού' "/>
                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                        <td width="20%">
                            <xsl:value-of select="$translated"/>
                        </td>
                        <td>
                            <input type="text" id="codeValue" name="codeValue" style="width:150px"></input>
                        </td>
                    </tr-->
                    <tr>
                        <xsl:variable name="tag" select="concat ('PrimaryInsertField',$EntityType) "/>
                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                        <td width="20%" >
                            <xsl:value-of select="$translated"/>
                        </td>
                        <td>
                            <input type="text" id="mainCurrentName" name="mainCurrentName" style="width:400px"></input>
                        </td>
                    </tr>
                    <tr>
                        <xsl:variable name="tag" select=" 'Επωνυμία' "/>
                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                        <td width="25%" >
                            <xsl:value-of select="$translated"/>
                        </td>
                        <td>
                            <xsl:variable name="tag" select=" 'AdminOrganization' "/>
                            <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                            <select name="orgId" style="width:150px">
                                <option value="0">----------
                                    <xsl:value-of select="$translated"/>----------
                                </option>
                                <xsl:for-each select="//context/groups/group">
                                    <option value="{./id}">
                                        <xsl:if test=" ./id = $OrgId ">
                                            <xsl:attribute name="selected">selected</xsl:attribute>
                                        </xsl:if>
                                        <xsl:value-of select="./name"/>
                                    </option>
                                </xsl:for-each>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <xsl:variable name="tag" select=" 'Deltiolang' "/>
                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                        <td width="20%">
                            <xsl:value-of select="$translated"/>
                        </td>
                        <td>
                            <select id="lang" name="lang">                             
                                <option value="0">---
                                    <xsl:value-of select="$translated"/>---
                                </option>
                                <xsl:for-each select="//context/Langs/Lang">                                        
                                    <xsl:variable name="tag" select="./text()"/>                                  
                                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                    <option value="{./text()}">
                                        <xsl:if test=" $tag = $lang  ">
                                            <xsl:attribute name="selected">true</xsl:attribute>
                                        </xsl:if>
                                        <xsl:value-of select="$translated"/>
                                    </option>  
                                </xsl:for-each>
                            </select>
                        </td>
                    </tr>
					
                    <!--tr>
                            <xsl:variable name="tag" select=" 'Κράτος' "/>
                            <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                            <td width="20%" ><xsl:value-of select="$translated"/></td>
                            <td><input type="text" id="country" name="country" style="width:150px"></input></td>
                    </tr-->
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