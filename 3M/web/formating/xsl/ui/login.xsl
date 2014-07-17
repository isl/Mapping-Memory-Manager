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
    <xsl:include href="loginPageTemplate.xsl"/>

    <xsl:variable name="ErrorMsg" select="//context/ErrorMsg"/>
    <xsl:variable name="lang" select="//page/@language"/>


    <xsl:template match="/">
        <xsl:call-template name="loginPageTemplate"/>
    </xsl:template>
    <xsl:template name="context">
        <script type="text/javascript" src="formating/javascript/jquery/jquery.min.js"></script>

        <td colSpan="3" vAlign="top" align="center" class="content">
            <br/>
            <br/>
            <form id="loginForm" method="post" action="Login" style="margin-bottom:0px;">
                <!--table width="400" style="border: 4px double #444488"-->
                
                <table style="padding-left:150px;">
            
                    <tr>
                        <xsl:variable name="tag" select=" 'username' "/>
                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                        <!--td style="color: #003366;font-size: 12px;font-weight: bold;font-family:Verdana;" >
                            <xsl:value-of select="$translated"/>
                        </td-->
                        <td>
                            <input type="text" id="username" name="username" placeholder="{$translated}" style="width:210px"></input>
                        </td>
                        <td></td>
                    </tr><!--3a2e1d-->
                    <tr>
                        <xsl:variable name="tag" select=" 'password' "/>
                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                        <!--td style="color: #003366;font-size: 12px;font-weight: bold;font-family:Verdana;" >
                            <xsl:value-of select="$translated"/>
                        </td-->
                        <td>
                            <input type="password" name="password" placeholder="{$translated}" style="width:210px"></input>
                        </td>
                        <td> 
                            <xsl:variable name="tag" select=" 'ForgetPass' "/>
                            <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                            <a style="padding-left:10px; font-size:9px;" href="ForgetPass?lang={$lang}">
                                <xsl:value-of select="$translated"/>
                            </a>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input type="hidden" name="lang" value="{$lang}"/>
                            <xsl:variable name="tag" select=" 'SignIn' "/>
                            <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                            <input style="width:80px;font-size:11px;"  type="submit" class="button" value="{$translated}"></input>
                        <xsl:if test="//context/signUp/text() = 'true'">
                            <a style="padding-left:7px; font-size:10px;"  href="SignUp?lang={$lang}">
                                <xsl:variable name="tag" select=" 'operator_or' "/> 
                                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/> 
                                <xsl:value-of select="$translated"/>
                                <xsl:text> </xsl:text>
                                <xsl:variable name="tag" select=" 'SignUp' "/> 
                                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/> 
                                <xsl:value-of select="$translated"/>
                            </a>
                        </xsl:if>
                        </td>
                    </tr>
                    <tr>                      
                        <td></td>                                            
                    </tr>
                </table>
                <table>
                    <tr>
                        <xsl:choose>
                            <xsl:when test="$ErrorMsg != '' ">
                                <xsl:variable name="tag" select="$ErrorMsg"/>
                                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                <td class="contentText" style="color:#EE0000;">
                                    <b>
                                        <xsl:value-of select="$translated"/>
                                    </b>
                                </td>
                            </xsl:when>
                            <xsl:otherwise>
                                <td style="color:#EE0000;">
                                    <br/>
                                </td>
                            </xsl:otherwise>
                        </xsl:choose>
                    </tr>
                </table>
            </form>
            <br/>
            <script language="javascript">
                document.getElementById('username').focus();
               
            </script>
        </td>
    </xsl:template>
</xsl:stylesheet>