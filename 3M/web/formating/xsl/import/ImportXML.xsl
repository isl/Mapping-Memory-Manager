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
    <xsl:output method="html"/>
    <xsl:include href="../ui/page.xsl"/>
    <xsl:template match="/">
        <xsl:call-template name="page"/>
    </xsl:template>
    <xsl:template name="context">
        <xsl:variable name="EntityType" select="//context/EntityType"/>
        <xsl:variable name="Display" select="//context/Display"/>

        <td colSpan="{$columns}" vAlign="top"  class="content">
            <br/>
            <xsl:choose>
                <xsl:when test="$Display='form'">
                    <p align="left" style="font-size:13;">
                        <xsl:variable name="tag" select=" 'SelectFile' "/>
                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>               
                        <xsl:value-of select="$translated"/>
                    </p>
                    <p>
                        <form name="upload_form" id="upload_form" action="ImportXML?type={$EntityType}" method="post" enctype="multipart/form-data">
                            <input type="file" size="30" id="file" name="file"/>                        
                            <input type="submit" value="OK"/>
                        </form>
                    </p>
                </xsl:when>
                <xsl:otherwise>
                      <br/>
                    <br/>
                    <br/>
                    <br/>
                    <br/>
                    <br/>
                    <p align="center" style="padding-left:20px; padding-right:20px ;font-size:12;">
                        <xsl:variable name="tag" select="$Display"/>
                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>               
                        <xsl:value-of select="$translated"/>
                    </p>
                </xsl:otherwise>
            </xsl:choose>           
            <p align="center" style="padding-left:20px; padding-right:20px ;font-size:12;">
                <xsl:variable name="tag" select=" 'Epistrofi' "/>
                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                <xsl:choose>
                    <xsl:when test="$Display='ACTION_SUCCESS'">
                        <a href="javascript:window.history.go(-2);">
                            <xsl:value-of select="$translated"/>
                        </a>&#160;&#160; 
                    </xsl:when>   
                    <xsl:otherwise>
                        <a href="javascript:window.history.go(-1);">
                            <xsl:value-of select="$translated"/>
                        </a>&#160;&#160; 
                    </xsl:otherwise>
                </xsl:choose>                  
            </p>
        </td>
        <script type="text/javascript">
            
            function getExtension(filename) {
            var parts = filename.split('.');
            return parts[parts.length - 1];
            }

            function isXML(filename) {
            var ext = getExtension(filename);
            ext = ext.toLowerCase(); 
            if(ext=="xml"){
            return true;
            }
            return false;
            }
            
            $(function() {
            $('#upload_form').submit(function() {
            function failValidation(msg) {
            alert(msg); 
            return false;
            }

            var file = $('#file');
            if (!isXML(file.val())) {
            <xsl:variable name="tag" select=" 'NotValidFile' "/>
            <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
            var msg ="<xsl:value-of select="$translated"/>";
            return failValidation(msg);
            }
            return true
            });

            });
        </script>

    </xsl:template>

</xsl:stylesheet>
