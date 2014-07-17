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

<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions">
    <xsl:template name="splashB">
        <div id="pleasewaitScreenB" style="position:absolute;z-index:5;top:17%;left:38%;visibility:hidden;">
            <table bgcolor="#000000" border="1" bordercolor="#000000" cellpadding="0" cellspacing="0" height="200" width="300">
                <tr>
                    <td width="100%" height="100%" bgcolor="#FFFFFF" align="center" valign="middle">
                        <br/>                                                
                        <img src="{$annakom_icons_url}/ajax-loader.gif"/>
                        <br/>
                        <br/>
                        <xsl:variable name="tag" select=" 'backupSplashMessage' "/> 
                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                        <font face="Helvetica,verdana,Arial" size="3" color="#000066">
                            <b>
                                <xsl:value-of select="$translated"/>
                            </b>
                        </font>
                        <br/>
                        <br/>
                    </td>
                </tr>
            </table>
        </div>
    </xsl:template>
</xsl:stylesheet>