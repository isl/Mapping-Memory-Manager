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
    <xsl:template name="leftmenu">
        <xsl:variable name="EntityType" select="//context/EntityType"/>
        <xsl:variable name="user" select="//page/@UserRights"/>
        <td id="leftmenuTD" align="left" valign="top" class="leftcontent">
            <xsl:for-each select="//leftmenu/menugroup[menu/userRights=$user]">
                <table id="leftmenu" border="0" cellspacing="0">                                            
                    <tbody>
                        <xsl:for-each select="./menu[userRights=$user]">                           
                            <tr>
                                <td width="100" id="menulabel" class="menulabel">
                                    <xsl:attribute name="class">leftmenu</xsl:attribute>
                                    <xsl:attribute name="onmouseover">highlight(this, true)</xsl:attribute>
                                    <xsl:attribute name="onmouseout">highlight(this, false)</xsl:attribute>
                                    <xsl:variable name="tag" select="./label"/>
                                    <xsl:variable name="translated" select="$locale/leftmenu/*[name()=$tag]/*[name()=$lang]"/>
                                    <xsl:choose>
                                        <xsl:when test="count(./submenu)=0">                                          
                                            <xsl:choose>
                                                <xsl:when test="./@id=$EntityType">
                                                    <a class="highlighted_leftmenu" id="leftmenu">
                                                        <xsl:attribute name="href">
                                                            <xsl:value-of select="string(./userRights[text()=$user]/@href)"/>
                                                        </xsl:attribute>                                    
                                                        <xsl:value-of select="$translated"/>
                                                    </a> 
                                                </xsl:when>
                                                <xsl:otherwise>
                                                    <a class="leftmenu" id="leftmenu">
                                                        <xsl:attribute name="href">
                                                            <xsl:value-of select="string(./userRights[text()=$user]/@href)"/>
                                                        </xsl:attribute>                                    
                                                        <xsl:value-of select="$translated"/>
                                                    </a> 
                                                </xsl:otherwise>
                                            </xsl:choose>
                                        </xsl:when>
                                        <xsl:otherwise> 
                                            <div class="leftmenu" id="leftmenu" onmouseover="highlight(this,true)" onmouseout="highlight(this,false)" onclick="toggleVisibility(document.getElementById('morefile{position()}'))" >
                                                <xsl:attribute name="href">
                                                    <xsl:value-of select="./@href"/>
                                                </xsl:attribute>                                    
                                                <xsl:value-of select="$translated"/>
                                                <xsl:text> </xsl:text>&#8595;
                                            </div>
                                            <table cellspacing="0" border="0" id="morefile{position()}" class="menulabelmorefile"  >
                                                <script>
                                                    var pos='<xsl:value-of select="position()"/>';                                                    
                                                </script>
                                                <xsl:for-each select="./submenu[userRights=$user]">  
                                                    <tr>
                                                        <td style="padding-left:25px;"  class="menulabel" id="menulabel" width="100">
                                                            <xsl:variable name="tag" select="./label"/>
                                                            <xsl:variable name="translated" select="$locale/leftmenu/*[name()=$tag]/*[name()=$lang]"/>                                                           
                                                            <xsl:choose>
                                                                <xsl:when test="./@id=$EntityType">                                          
                                                                    <a class="highlighted_leftmenu" id="leftmenu">                                                                      
                                                                        <xsl:attribute name="href">
                                                                            <xsl:value-of select="string(./userRights[text()=$user]/@href)"/>
                                                                        </xsl:attribute>                                    
                                                                        <xsl:value-of select="$translated"/>
                                                                         <script>
                                                                            toggleVisibility(document.getElementById('morefile'+pos));
                                                                        </script>
                                                                    </a> 
                                                                </xsl:when>
                                                                <xsl:otherwise>
                                                                    <a class="leftmenu" id="leftmenu">
                                                                        <xsl:attribute name="href">
                                                                            <xsl:value-of select="string(./userRights[text()=$user]/@href)"/>
                                                                        </xsl:attribute>                                    
                                                                        <xsl:value-of select="$translated"/>
                                                                    </a> 
                                                                </xsl:otherwise>
                                                            </xsl:choose>
                                                        </td>
                                                    </tr>
                                                </xsl:for-each>
                                            </table>
                                        </xsl:otherwise>
                                    </xsl:choose>                          
                                </td>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                </table>
                <hr/>
            </xsl:for-each>
        </td>
    </xsl:template>
</xsl:stylesheet>