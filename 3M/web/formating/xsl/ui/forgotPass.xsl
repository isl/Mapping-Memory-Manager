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
    <xsl:variable name="emailMsg" select="$locale/context/NotValidEmail/*[name()=$lang]"/>

    <xsl:template match="/">
        <xsl:call-template name="loginPageTemplate"/>
        
    </xsl:template>    
    <xsl:template name="context">
        <script type="text/javascript" src="formating/javascript/jquery/jquery.min.js"></script>
        <xsl:choose>
            <xsl:when test="//context/Display=''">
                <td colSpan="3" vAlign="top" align="center" class="content">
                    <br/>
                    <br/>
                    <form id="loginForm" method="post" action="ForgetPass" style="margin-bottom:0px;" onsubmit="return validateEmail();">
                        <!--table width="400" style="border: 4px double #444488"-->
                        <table>
                      
                            <tr>
                                <xsl:variable name="tag" select=" 'EmailPrompt' "/>
                                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                <td style="color: #003366;font-size: 12px;font-weight: bold;font-family:Verdana;" >
                                    <xsl:value-of select="$translated"/>
                                </td>
                                <td>
                                    <input type="email" id="email" name="email" style="width:210px"></input>
                                </td>
                            </tr><!--3a2e1d-->
                            <tr>
                                <td></td>
                                <td>
                                    <input type="hidden" name="lang" value="{$lang}"/>
                                    <xsl:variable name="tag" select=" 'Eisodos' "/>
                                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                    <input style="width: 70px; margin-left:139px;"  type="submit" class="button" value="{$translated}"></input>	
                                </td>
                            </tr>               
                        </table>
               
                    </form>
                    <br/>
                    <script language="javascript">
                        document.getElementById('email').focus();
                        function validateEmail(){
                        var email = $("#email").val();
                        var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;  
                        if(!email.match(mailformat))  
                        {
                        var msg = '<xsl:value-of select="$emailMsg"/>';
                        alert(msg);  
                        event.preventDefault();                
                        }
                        return true;
                        }
              
                    </script>
                </td>
            </xsl:when>
            <xsl:otherwise>
               
                <td colSpan="3" vAlign="top" align="center" class="content">
                     <br/>
                    <br/>
                    <table>
                        <xsl:variable name="tag" select="//context/Display"/>
                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                        <td style="color: #003366;font-size: 12px;font-weight: bold;font-family:Verdana;" >
                            <p>                                    
                                <xsl:value-of select="$translated"/>
                            </p>
                        </td>  
                        <tr></tr>  
                        <tr>
                            <xsl:variable name="tag" select="'ReturnLogin'"/>
                            <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                            <td style="color: #003366;font-size: 12px;font-weight: bold;font-family:Verdana;" >
                                <p align="right" style="padding-left:20px; padding-right:20px ;font-size:12;">
                                    <a href="Login">
                                        <xsl:value-of select="$translated"/>
                                    </a> 
                                </p>                           
                            </td>                           
                        </tr>
                    </table>                  
                </td>            
            </xsl:otherwise>
        </xsl:choose>
        
    </xsl:template>
</xsl:stylesheet>