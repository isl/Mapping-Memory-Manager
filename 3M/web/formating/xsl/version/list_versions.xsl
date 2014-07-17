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
    <xsl:output method="html" indent="yes" encoding="UTF-8"/>
    <xsl:include href="../ui/page.xsl"/>
    <xsl:include href="../paging/SearchPaging.xsl"/>
    <xsl:variable name="EntityType" select="//context/EntityType"/>
    <xsl:variable name="FileId" select="//context/FileId"/>
    <xsl:variable name="ListOf" select="//context/ListOf"/>
    <xsl:variable name="URI_Reference_Path" select="//context/URI_Reference_Path"/>
    <xsl:variable name="output" select="//context/query/outputs/path"/>
    <xsl:variable name="ServletView" select="//context//actions//menu[@id='View']/actionPerType[@id=$EntityType]/userRights[./text()=$UserRights]/@id"/>




    
    <xsl:template match="/">
        <xsl:call-template name="page"/>
    </xsl:template>
    <xsl:template name="context">
        <td colSpan="{$columns}" vAlign="top" align="center" class="content">
      
            <script type="text/javascript">
                var lang = '<xsl:value-of select="$lang"/>';
                var servlet = '<xsl:value-of select="$ServletView"/>';
                var hasLang = (servlet.indexOf("lang=")>-1);
                if(hasLang){
                servlet=servlet.replace("lang=","lang="+lang); 
                }
            </script>        

            <xsl:if test="count(//result)&gt;0">
                <br/>
                <p align="center" class="contentText">
                    <xsl:variable name="tag" select=" 'EkdoseisGiaTinEggrafi' "/>
                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                    <font size="3">
                       
                            <xsl:value-of select="$translated"/>
                      <xsl:value-of select="//context/nameValue"/>"
                    </font>
                </p>
                   <br/>				
                <script language="JavaScript" src="formating/javascript/utils/sortable.js"></script>     
                <table border="0" align="center" class="results" cellspacing="1">
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
                        <td></td>
                        <td></td>
                    </tr>
                    <xsl:for-each select="//result">
                        <xsl:variable name="ID" select="./FileId/text()"/>
                        <tr id="resultRow" align="center" valign="middle" class="resultRow" onMouseOver="highlight(this, true)" onMouseOut="highlight(this, false)">
                            <td>
                                <strong>
                                    <xsl:value-of select="position()"/>
                                </strong>
                            </td>
                            <xsl:for-each select="./*">
                                <td>
                                    <xsl:choose>
                                        <xsl:when test="name()='versionId'">
                                            <xsl:variable name="tag" select=" 'version' "/>                               
                                            <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/> 
                                            <xsl:value-of select="$translated"/>                                  
                                            <xsl:value-of select="./text()"/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:value-of select="./text()"/>
                                        </xsl:otherwise>
                                    </xsl:choose>                                 
                                </td>
                            </xsl:for-each>
                      
                            <xsl:variable name="tag" select=" 'Proboli' "/>                               
                            <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>                    
                            <td id="action" class="action" onmouseover="highlight(this, true)" onmouseout="highlight(this, false)" title="{$translated}">
                                <a class="action" href="javascript:void(0)" onClick="previewPopUp(servlet+'{$FileId}'+'&amp;versions=yes&amp;collectionID='+'{./versionId/text()}'+'&amp;entityType={$EntityType}'+'&amp;xmlId={$FileId}', '{$EntityType}')">
                                    <img border="0" src="{ concat($systemRoot, '/formating/images/view.png') }"/>
                                </a>
                            </td>                               
                            <xsl:variable name="tag" select=" 'EksagwghXML_RDF' "/>
                            <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                            <td id="action" class="action" onmouseover="highlight(this, true)" onmouseout="highlight(this, false)" title="{$translated}">
                                <a class="action" href="ExportVersions?id={$FileId}&amp;type={$EntityType}&amp;collectionID={./versionId/text()}">
                                    <img border="0" src="{ concat($systemRoot, '/formating/images/export.png') }"/>
                                </a>                            
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
                 
                
            
            </xsl:if>
         
            <p align="center" style="padding-left:20px; padding-right:20px ;font-size:12;">
           <xsl:variable name="tag" select=" 'Epistrofi' "/>
                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                        <a href="javascript:window.history.go(-1);">
                            <xsl:value-of select="$translated"/>
                        </a>
            </p>
        </td>
    </xsl:template>
</xsl:stylesheet>
