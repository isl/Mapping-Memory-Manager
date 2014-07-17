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
 
    <xsl:template name="replaceNL">
        <xsl:param name="string"/>
        <xsl:choose>
            <xsl:when test="contains($string,'&#10;')">
                <xsl:value-of select="substring-before($string,'&#10;')"/>
                <br/>
                <xsl:call-template name="replaceNL">
                    <xsl:with-param name="string" select="substring-after($string,'&#10;')"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$string"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="replaceNL_Translate">
        <xsl:param name="string"/>
        <xsl:choose>
            <xsl:when test="contains($string,'&#10;')">
                <xsl:variable name="tag" select="substring-before($string,'&#10;')"/>
                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                <xsl:value-of select="$translated"/>
                <br/>
                <xsl:call-template name="replaceNL_Translate">
                    <xsl:with-param name="string" select="substring-after($string,'&#10;')"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:variable name="tag" select="$string"/>
                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                <xsl:value-of select="$translated"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:include href="empty_page.xsl"/>
      <xsl:include href="../entity/view_dependancies.xsl"/>
    <xsl:variable name="DisplayError" select="//context/DisplayError"/>
    <xsl:variable name="Display" select="//context/Display"/>
    <xsl:variable name="Unlock" select="//context/Unlock"/>
    <xsl:variable name="GoOnImport" select="//context/GoOnImport"/>
    <!--Sam's touch-->
    <xsl:variable name="Owner" select="//context/Owner"/>
    <xsl:template match="/">
        <xsl:call-template name="page"/>
    </xsl:template>
    <xsl:template name="context">
        <td colSpan="{$columns}" vAlign="top" class="content">
            <br/>
            <p align="center" class="contentText">
                <b>
                    <xsl:call-template name="replaceNL_Translate">
                        <xsl:with-param name="string" select="$Display"/>
                    </xsl:call-template>
                </b>
      
                <br/>
                <!--Sam's touch-->
                <xsl:if test="$Display='IS_EDITED_BY_USER'">
                    <xsl:text> </xsl:text>
                    <xsl:value-of select="$Owner"/>
                </xsl:if>
                <br/>                
                <xsl:if test=" $Unlock != 'false'  ">
             
                    <xsl:variable name="tag" select=" 'SinexeiaEpexergasiasMsg' "/>
                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                    <font>
                        <xsl:call-template name="replaceNL">
                            <xsl:with-param name="string" select="$translated"/>
                        </xsl:call-template>
                    </font>
                    <br/>
                    <xsl:variable name="tag" select=" 'SinexeiaEpexergasias' "/>
                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                    <a href="{$Unlock}">
                        <xsl:value-of select="$translated"/>
                    </a>
                </xsl:if>
                <xsl:if test=" $GoOnImport != 'false'  ">
                    <xsl:variable name="tag" select=" 'SinexeiaEisagwgisMsg' "/>
                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                    <font>
                        <xsl:call-template name="replaceNL">
                            <xsl:with-param name="string" select="$translated"/>
                        </xsl:call-template>
                    </font>
                    <br/>
                    <xsl:variable name="tag" select=" 'SinexeiaEisagwgis' "/>
                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                    <a href="{$GoOnImport}">
                        <xsl:value-of select="$translated"/>
                    </a>
                </xsl:if>
            </p>
                <xsl:variable name="result"  select="//context/query/results"/>

            <p align="right" style="padding-left:20px; padding-right:20px ;font-size:12;">
                <!--xsl:variable name="tag" select=" 'Epistrofi' "/>
               <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                <a href="javascript:window.history.go(-1);window.resizeTo(screen.width-150,screen.height-80);window.moveTo(0,0);">
                <xsl:value-of select="$translated"/></a>&#160;&#160;-->
              
                <xsl:choose>
                <xsl:when test=" $result != ''  ">
                    <xsl:text> </xsl:text>
                    <xsl:text> </xsl:text>
                    <xsl:text> </xsl:text>
                      <xsl:call-template name="view_dependants">
                         </xsl:call-template>
                        </xsl:when>
                        <xsl:otherwise>
                              <xsl:variable name="tag" select=" 'Kleisimo' "/>
                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                <a href="javascript:window.close();" >
                    <xsl:value-of select="$translated"/>
                </a>
                        </xsl:otherwise>
                </xsl:choose>
            </p>
            <!--/xsl:otherwise>
            </xsl:choose-->
        </td>
    </xsl:template>
</xsl:stylesheet>