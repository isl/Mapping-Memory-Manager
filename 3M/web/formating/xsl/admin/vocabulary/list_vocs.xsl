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
    <xsl:include href="../../ui/page.xsl"/>
	
    <xsl:variable name="AdminAction" select="//context/AdminAction"/>
    <xsl:variable name="AdminMode" select="//context/AdminMode"/>
    <xsl:variable name="EntityType" select="//context/EntityType"/>
    <xsl:variable name="ListOf" select="//context/ListOf"/>
    <xsl:variable name="VocsList" select="//context/VocsList/Vocabularies/*"/>
	
    <xsl:template match="/">
        <xsl:call-template name="page"/>
    </xsl:template>
    <xsl:template name="context">
        <script>
            function expandVocsList(ind){
            getObj('expand_'+ind).style.display='none';
            getObj('collapse_'+ind).style.display='block';
            getObj('vocs_'+ind).style.display='block';
            }
			
            function collapseVocsList(ind){
            getObj('expand_'+ind).style.display='block';
            getObj('collapse_'+ind).style.display='none';
            getObj('vocs_'+ind).style.display='none';
            }
        </script>
        <td colSpan="{$columns}" vAlign="top" align="center" class="content">
            <br/>
            <xsl:for-each select="$VocsList"> <!-- gia ka8e omada le3ilogiwn -->
                <xsl:variable name="ind" select="position()"/>
                <p >
                    <table border="0" class="results" cellspacing="0" width="500">
                        <tr  valign="middle" class="resultRow2">
                            <td>
                                <a id="expand_{$ind}" class="action"  href="javascript:expandVocsList('{$ind}')" style="display:block">[+] <xsl:value-of select="./displayname/*[name()=$lang]"/></a>
                                <a id="collapse_{$ind}" class="action"  href="javascript:collapseVocsList('{$ind}')" style="display:none">[-] <xsl:value-of select="./displayname/*[name()=$lang]"/></a>
                            </td>
                        </tr>
                        <tr class="resultRow" >
                            <td>
                                <table id="vocs_{$ind}" class="results" style="display:none" cellspacing="0">
                                    <xsl:for-each select="./*[name() != 'displayname']"> <!-- gia ka8e le3ilogio ths omadas -->
                                        <tr class="resultRow" align="left" valign="middle">
                                            <td style="padding-left:15px" width="500">
                                                <a href="AdminVoc?action={$AdminAction}&amp;mode={$AdminMode}&amp;file={./file}&amp;menuId={$EntityType}">
                                                    <xsl:value-of select="./displayname/*[name()=$lang]"/>
                                                </a>
                                            </td>
                                        </tr>
                                    </xsl:for-each>
                                </table>
                            </td>
                        </tr>
                                                
                    </table>
                </p>
            </xsl:for-each>
            <br/>
        </td>
    </xsl:template>
</xsl:stylesheet>