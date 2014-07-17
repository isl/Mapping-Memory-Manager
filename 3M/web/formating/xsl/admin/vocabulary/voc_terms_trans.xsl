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
    <xsl:include href="../../ui/page.xsl"/>

    <xsl:variable name="AdminAction" select="//context/AdminAction"/>
    <xsl:variable name="VocFile" select="//context/VocFile"/>
    <xsl:variable name="VocFileName" select="//context/VocFileName"/>
    <xsl:variable name="EntityType" select="//context/EntityType"/>	
    <xsl:variable name="AdminMode" select="//context/AdminMode"/>
    <xsl:variable name="EntityXMLFile" select="//context/EntityXMLFile"/>
	
    <xsl:variable name="TransId" select="//context/TransId"/>
    <xsl:variable name="Msg" select="//context/Msg"/>

    <xsl:template match="/">
        <xsl:call-template name="page"/>
    </xsl:template>
    <xsl:template name="context">
        <td colSpan="{$columns}" vAlign="top" class="content">
           <xsl:call-template name="actions"/>
            <br/>
            <script type="text/JavaScript">
                <xsl:variable name="tag" select=" 'NoTerm' "/>
                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                var noTermStr = '<xsl:value-of select="$translated"/>';

                <xsl:variable name="tag" select=" 'TermExists' "/>
                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                var termExistsStr = '<xsl:value-of select="$translated"/>';
                
                <xsl:variable name="tag" select=" 'PromptMessage' "/>
                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                var str = '<xsl:value-of select="$translated"/>';
            </script>
            <div align="center" class="divA" width="30%">
                <xsl:value-of select="$VocFileName"/>
            </div>
             <div  id="fileType" style="display:none;"> <xsl:value-of select="$VocFile"/> </div>
            <form id="newTermFrm" method="post" onsubmit=" alertMsg();" action="AdminVoc?action={$AdminAction}&amp;menuId={$EntityType}">
                <div style="height:300px; overflow: auto;" align="center">
                  <br/>
                   <script language="JavaScript" src="formating/javascript/utils/sortable.js"></script>        
                    <table border="0" id="results" class="results sortable" cellspacing="1">
                        <tbody>
                            <tr align="center" class="contentHeadText">
                                <th>
                                    <xsl:variable name="tag" select=" 'Auxwn' "/>
                                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                    <b>
                                        <xsl:value-of select="$translated"/>
                                    </b>
                                </th>
                                <xsl:for-each select="//TransLang">
                                    <th>
                                        <xsl:variable name="tag" select="./text()"/>
                                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                        <b>
                                            <xsl:value-of select="$translated"/>
                                        </b>
                                    </th>
                                </xsl:for-each>                               
                            </tr>
                            <xsl:for-each select="//translation">
                                <xsl:variable name="ID" select="./@id"/>
                                <tr id="resultRow" align="left" valign="middle" class="resultRow">
                                   <td class="invisible">
                                       <xsl:value-of select="./@id"/>
                                   </td>
                                    <td>
                                        <xsl:value-of select="position()"/>
                                    </td>
                                    <xsl:for-each select="./*">
                                        <td>
                                            <input type="hidden" id="term_{name(.)}" name="term_{name(.)}" value="{./text()}"></input>
                                            <xsl:value-of select="./text()"/>
                                        </td>
                                    </xsl:for-each>                                    
                                </tr>
                            </xsl:for-each>
                        </tbody>
                    </table>
                </div>
                <br/>		
                <input type="hidden" name="EntityXMLFile" value="{$EntityXMLFile}"></input>
                <div id="addNewTerm" align="center" style="visibility:hidden;">
                    <xsl:variable name="tag" select=" 'NeaMetafrasi' "/>
                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                    <div class="divA">
                        <xsl:value-of select="$translated"/>
                    </div>
                    <table border="0">
                        <xsl:for-each select="//VocTerms">
                            <xsl:variable name="TermSelected" select="./TermSelected"/>
                            <tr>
                                <td  class="tdLang">
                                    <xsl:variable name="tag" select=" ./Lang "/>
                                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                    <xsl:value-of select="$translated"/>
                                </td>
                                <td vAlign="top" align="left">
                                    <xsl:variable name="tag" select=" 'Epilogi' "/>
                                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                    <select class="trans" name="trans_{./Lang}">
                                        <option value="0">
                                            <xsl:value-of select="$translated"/>
                                        </option>
                                        <xsl:for-each select="./Όρος">
                                            <option value="{./@id}">
                                                <xsl:if test=" ./@id = $TermSelected ">
                                                    <xsl:attribute name="selected">selected</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="./text()"/>
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                </td>
                            </tr>
                        </xsl:for-each>
                        <tr>
                            <td align="right">
                                <input type="hidden" name="file" value="{$VocFile}"></input>
                                <input type="hidden" id="id" name="id" value="{$TransId}"></input>
                                <xsl:variable name="tag" select=" 'Kataxwrisi' "/>
                                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                <input type="submit" value="{$translated}" class="button"></input>
                            </td>
                        </tr>
                    </table>
                </div>
            </form>
            <script type="text/javascript">
                
                
                function alertMsg(){
                if('<xsl:value-of select="$AdminAction"/>'=="insert_trans"){                            
                            <xsl:variable name="tag" select="'TRANS_ADD_SUCCESS'"/>  
                            <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                            <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                            var msg='<xsl:value-of select="$translated"/>';
                            alert(msg);
                        }else if('<xsl:value-of select="$AdminAction"/>'=="edit_trans"){
                            <xsl:variable name="tag" select="'TRANS_UPDATE_SUCCESS'"/>
                            <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                            <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                            var msg='<xsl:value-of select="$translated"/>';
                           alert(msg);
                        }   
                }
                function checkOption(){
                    var options=$('select.trans');
                      $.each(options, function(index, item) {                                  
                                   if(item.value!=0){
                                         alertMsg();
                                         return;
                                    }
                                   
                                });               
                  
                    }
                if('<xsl:value-of select="$AdminAction"/>'=="edit_trans" || '<xsl:value-of select="$AdminAction"/>'=="insert_trans"){                   
                        var elemID= document.getElementById('addNewTerm');    
                        elemID.style.visibility='visible'; 
                }
                
            </script>          
        </td>
    </xsl:template>
</xsl:stylesheet>
