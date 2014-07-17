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

<xsl:stylesheet xmlns:url="http://whatever/java/java.net.URLEncoder" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xdt="http://www.w3.org/2005/02/xpath-datatypes" xmlns:fn="http://www.w3.org/2005/02/xpath-functions" version="2.0">
    <xsl:output method="html" indent="yes" encoding="UTF-8"/>
    <xsl:include href="../ui/page.xsl"/>
    <xsl:include href="../paging/SearchPaging.xsl"/>
    
    <xsl:variable name="IsGuestUser" select="//context/IsGuestUser"/>
    <xsl:variable name="DocStatus" select="//context/DocStatus"/>
    <xsl:variable name="EntityType" select="//context/EntityType"/>
    <xsl:variable name="ServletName" select="//context/ServletName"/>
    <xsl:variable name="output" select="//context/query/outputs/path[@selected='yes']"/>
    <xsl:variable name="userOrg" select="//page/@userOrg"/>
    <xsl:variable name="queryPages" select="//stats/@queryPages"/>
    <xsl:variable name="queryPages" select="//stats/@queryPages"/>
    <xsl:variable name="end" select="//stats/@end"/>
    <xsl:variable name="start" select="//stats/@start"/>
    <xsl:variable name="count" select="//stats/@count"/>
    <xsl:variable name="currentP" select="//stats/@currentP"/>
    <xsl:variable name="pageLoop" select="//pageLoop/lista"/>
    <xsl:variable name="showPages" select="//showPages/show"/>
    <xsl:variable name="step">
        <xsl:value-of select="//stats/@step"/>
    </xsl:variable>	
    
    <xsl:template match="/">
        <xsl:call-template name="page"/>
    </xsl:template>
    <xsl:template name="context">
        <xsl:variable name="tag" select=" 'PromptMessage' "/>
        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
        <script type="text/JavaScript">
            var str = '<xsl:value-of select="$translated"/>';
        </script>
		
        <td colSpan="{$columns}" vAlign="top"  class="content">
            <xsl:call-template name="actions"/>
            <xsl:if test="count(//result)&gt;0">
                <table border="0" align="center" cellspacing="1">
                    <tr>
                        <td>
                            <xsl:if test=" $queryPages &gt; 1">        
                                <xsl:call-template name="SearchPaging">
                                    <xsl:with-param name="url" select=" concat($ServletName, '?type=', $EntityType, '&amp;status=', $DocStatus) "/>
                                    <xsl:with-param name="queryPages" select="$queryPages"/>
                                    <xsl:with-param name="currentP" select="$currentP"/>
                                    <xsl:with-param name="pageLoop" select="$pageLoop"/>
                                    <xsl:with-param name="showPages" select="$showPages"/>
                                    <xsl:with-param name="selected" select="1"/>
                                </xsl:call-template>
                            </xsl:if>
                        </td>
                    </tr>
                </table>
                
                <script language="JavaScript" src="formating/javascript/utils/sortable.js"></script>
                
                <table border="0" align="center" class="sortable" cellspacing="1"  id="results">
                    <tr align="center" valign="middle" class="contentHeadText">
                        <td>
                            <xsl:variable name="tag" select=" 'Auxwn' "/>
                            <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                            <strong>
                                <xsl:value-of select="$translated"/>
                            </strong>
                        </td>
                        <xsl:for-each select="$output">
                            <td>
                                <strong>
                                    <xsl:variable name="tag" select=" ./text() "/>
                                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                    <xsl:value-of select="$translated"/>
                                </strong>
                            </td>
                        </xsl:for-each>
                      <td></td>  <!--for lock key,impotred doc -->

                    </tr>
                    <xsl:for-each select="//result">
                        <tr id="resultRow" align="center" valign="middle" class="resultRow">
                            <td class="invisible" >
                                <xsl:value-of select="./hiddenResults/FileId/text()"/>
                            </td> 
                            <td>
                                <strong>
                                    <xsl:value-of select="./@pos"/>
                                </strong>
                            </td>
                           
                            <xsl:for-each select="./*[name() != 'FileId' and name() != 'type' and name()!='hiddenResults']">
                                <xsl:choose>
                                    <xsl:when test="name() = 'ΨηφιακόΑρχείο' ">
                                        <xsl:variable name="tag" select=" 'Proboli' "/>
                                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                        <xsl:variable name="mime" select="../type/type/text()"/>
                                        <xsl:variable name="filename" select="./*[1]/text()"/>
                                        <td>
                                            <xsl:choose>
                                                <!--xsl:when test=" preceding-sibling::*[1]/*[1]/text() = 'Φωτογραφία' or preceding-sibling::*[1]/*[1]/text() = 'Σχέδιο'  "-->
                                                <xsl:when test="following-sibling::*[1]/*[1]/text() = 'Photos'">
                                                    <xsl:choose>
                                                        <xsl:when test=" ./*[1]/text() != '' ">
                                                            <a href="FetchBinFile?mime={$mime}&amp;type={$EntityType}&amp;depository=disk&amp;file={url:encode(./*[1]/text())}"  target="blank_">
                                                                <!--Samarita Servlet attempt-->                                                               
                                                                <img src="FetchBinFile?mime={$mime}&amp;size=small&amp;type={$EntityType}&amp;depository=disk&amp;file={url:encode(./*[1]/text())}" border="1" alt="{$translated}" width="50"></img>
                                                            </a>
                                                        </xsl:when>
                                                        <xsl:otherwise>
                                                            <img src="FetchBinFile?mime={$mime}&amp;type={$EntityType}&amp;depository=disk&amp;file=empty_photo.gif" alt="{$translated}" width="50"></img>
                                                        </xsl:otherwise>
                                                    </xsl:choose>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                    <xsl:if test=" ./*[1]/text() != '' ">
                                                        <a href="FetchBinFile?mime={$mime}&amp;type={$EntityType}&amp;depository=disk&amp;file={url:encode(./*[1]/text())}" alt="{$translated}" target="blank_">
                                                            <xsl:value-of select="$translated"/>
                                                        </a>
                                                        <!--a href="uploads/Archive/{./*[1]/text()}" alt="{$translated}" target="blank_"><xsl:value-of select="$translated"/></a-->
                                                    </xsl:if>
                                                </xsl:otherwise>
                                            </xsl:choose>
                                        </td>
                                    </xsl:when>
                                   
                                    <xsl:when test="name() ='ShortDescription'">    
                                        <xsl:variable name="description" select="./ShortDescription/text()"/>                                   
                                        <td title="{$description}" >
                                            <xsl:variable name="short_description" select="substring($description,1,15)"/>                                   
                                            <xsl:value-of select="$short_description"/>
                                            <xsl:if test="$short_description!=''and $short_description!=$description" >
                                                <xsl:value-of select="'...'"/>
                                            </xsl:if> 
                                        </td>
                                                                   
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <td>
                                            <xsl:for-each select="./*">
                                                <xsl:if test="position() > 1">
                                                    <br/>
                                                </xsl:if>
                                                <xsl:value-of select="./text()"/>
                                            </xsl:for-each>
                                        </td>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:for-each>
                           <td>
                                <xsl:if test="$IsGuestUser = 'false' and $user!='sysadmin'">
                                    <xsl:choose>
                                        <xsl:when test="./hiddenResults/organization/text()!=$userOrg or ./hiddenResults/hasPublicDependants/text()!='false' ">
                                            <!--td id="action" class="action" onmouseover="highlight(this, true)" onmouseout="highlight(this, false)"-->
                                                <img border="0"  src="{ concat($systemRoot, '/formating/images/lock.png') }"/>
                                            <!--/td-->
                                        </xsl:when>                                        
                                    </xsl:choose>                                    
                                </xsl:if>
                                <xsl:if test="$IsGuestUser = 'false' and $user!='sysadmin'">
                                    <xsl:choose>
                                        <xsl:when test="./hiddenResults/isImported/text()!='false' ">
                                            <!--td id="action" class="action" onmouseover="highlight(this, true)" onmouseout="highlight(this, false)"-->
                                                <img border="0"  src="{ concat($systemRoot, '/formating/images/import.png') }"/>
                                            <!--/td-->
                                        </xsl:when>                                        
                                    </xsl:choose>                                    
                                </xsl:if>                           
                    </td> 
                        </tr>
                    </xsl:for-each>
                </table>
                <table border="0" align="center" cellspacing="1">
                    <tr>
                        <td>
                            <xsl:if test=" $queryPages &gt; 1">        
                                <xsl:call-template name="SearchPaging">
                                    <xsl:with-param name="url" select=" concat($ServletName, '?type=', $EntityType, '&amp;status=', $DocStatus) "/>
                                    <xsl:with-param name="queryPages" select="$queryPages"/>
                                    <xsl:with-param name="currentP" select="$currentP"/>
                                    <xsl:with-param name="pageLoop" select="$pageLoop"/>
                                    <xsl:with-param name="showPages" select="$showPages"/>
                                    <xsl:with-param name="selected" select="2"/>
                                </xsl:call-template>
                            </xsl:if>
                        </td>
                    </tr>
                </table>
            </xsl:if>
            <form id="searchPagingForm" name="searchPagingForm" method="post" action="ListEntity">
		
                <input type="hidden" name="type" value="{$EntityType}"/>
                <input type="hidden" name="status" value="{$DocStatus}"/>
                <input type="hidden" name="pages" value="{$queryPages}"/>
                <input type="hidden" name="current" value="{$currentP}"/>
                <input type="hidden" name="newP" value=""/>
                <input type="hidden" name="move" value=""/>
            </form>
        </td>
    </xsl:template>
</xsl:stylesheet>
