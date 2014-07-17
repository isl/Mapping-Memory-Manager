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
    <xsl:variable name="FileId" select="//context/FileId"/>
    <xsl:variable name="EntityType" select="//context/EntityType"/>
    <xsl:variable name="comment" select="//context/Comment"/>
    
    <xsl:template match="/">
        <xsl:call-template name="page"/>
    </xsl:template>
    <xsl:template name="context">
        <td colSpan="{$columns}" vAlign="top" align="center" class="content">
            <br/>
            <br/>
            <form id="userForm" method="post" action="CreateVersion?type={$EntityType}&amp;action=create&amp;id={$FileId}" style="margin-bottom:0px;">
                <table width="100%" class="contentText">
                    <tr>
                        <xsl:variable name="tag" select=" 'Sxolio' "/>
                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                        <td width="20%" align="right">
                            <xsl:value-of select="$translated"/>
                        </td>
                        <td>
                            <textarea rows="3" id="comment" name="comment" style="width:400px"><xsl:value-of select="$comment"/></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <br/>
                            <input type="hidden" name="lang" value="{$lang}"/>
                            <xsl:variable name="tag" select=" 'Oloklirwsi' "/>
                            <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                            <input type="submit" class="button" value="{$translated}"></input>	
                        </td>
                    </tr>
                </table>               
            </form>
            <br/>
            <script language="javascript">document.getElementById('comment').focus();</script>
        </td>
    </xsl:template>
</xsl:stylesheet>
