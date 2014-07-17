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

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:template name="string-replace-all">
        <xsl:param name="text" />
        <xsl:param name="replace" />
        <xsl:param name="by" />
        <xsl:choose>
            <xsl:when test="contains($text, $replace)">
                <xsl:value-of select="substring-before($text,$replace)" />
                <xsl:value-of select="$by" />
                <xsl:call-template name="string-replace-all">
                    <xsl:with-param name="text" select="substring-after($text,$replace)" />
                    <xsl:with-param name="replace" select="$replace" />
                    <xsl:with-param name="by" select="$by" />
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$text" />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:output method="html"/>
    <xsl:template name="view_dependants">
        <xsl:variable name="result"  select="//context/query/results"/>
        <xsl:variable name="tag" select=" 'ViewDependencies' "/>
        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                       
                      
        <xsl:if test=" $result != ''  ">
            <xsl:variable name="URI_Reference_Path" select="//context/URI_Reference_Path"/>
                           
            <a href="#" onclick="javascript:toggleVisibility(document.getElementById('dependants'));">
                <xsl:value-of select="$translated"/>
            </a>
                          
            <br/>
            <br/>
            <script language="JavaScript" src="formating/javascript/utils/sortable.js"></script>        
            <xsl:variable name="output" select="//context//query/outputs/path"/>
            <div id="dependants" style="display:none;">
                <table id="results" class="sortable" border="0" align="center" cellspacing="1">
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
                        <td>
                        </td>
                    </tr>
                    <xsl:for-each select="//result">
                        <tr id="resultRow" align="center" valign="middle" class="resultRow"  >
                            <td>
                                <strong>
                                    <xsl:value-of select="position()"/>
                                </strong>
                            </td>
                            <td>                                                
                                <xsl:value-of select="./name/text()"/>
                            </td>
                            <td title="{concat($URI_Reference_Path,./uri_id/text())}">                                                
                                <xsl:value-of select="./uri_id/text()"/>
                            </td>
                            <xsl:variable name="type" select="./type/text()"/>                               
                            <xsl:variable name="id" select="./id/text()"/>                               
                            <xsl:variable name="tag" select=" 'Proboli' "/>                               
                            <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                            <xsl:variable name="ServletView" select="//context//actions//menu[@id='View']/actionPerType[@id=$type]/userRights[./text()=$UserRights]/@id"/>
                      
                            <xsl:variable name="ServletViewNoLang">
                                <xsl:call-template name="string-replace-all">
                                    <xsl:with-param name="text" select="$ServletView" />
                                    <xsl:with-param name="replace" select="'&amp;lang='" />
                                    <xsl:with-param name="by" select="''" />
                                </xsl:call-template>
                            </xsl:variable>
                     
                            <td>
                                <a class="action" href="javascript:void(0)" onClick="previewPopUp('{concat($ServletViewNoLang,$id,'&amp;lang=',$lang)}', '{$type}')">
                                    <img border="0" src="{ concat($systemRoot, '/formating/images/view.png') }"/>
                                </a>
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
            </div>
            <br/>
        </xsl:if>
    </xsl:template>
</xsl:stylesheet>