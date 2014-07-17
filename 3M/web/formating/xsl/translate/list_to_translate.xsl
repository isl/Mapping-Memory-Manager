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

<xsl:stylesheet xmlns:url="http://whatever/java/java.net.URLEncoder" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xdt="http://www.w3.org/2005/02/xpath-datatypes" xmlns:fn="http://www.w3.org/2005/02/xpath-functions" version="2.0">
    <xsl:output method="html" indent="yes" encoding="UTF-8"/>
    <xsl:include href="../ui/page.xsl"/>
    <xsl:include href="../paging/SearchPaging.xsl"/>
    <xsl:variable name="AdminMode" select="//context/AdminMode"/>
    <xsl:variable name="EntityType" select="//context/EntityType"/>
    <xsl:variable name="FileId" select="//context/FileId"/>
    <xsl:variable name="ListOf" select="//context/ListOf"/>
    <xsl:variable name="URI_Reference_Path" select="//context/URI_Reference_Path"/>
    <xsl:variable name="output" select="//context/query/outputs/path[@selected='yes']"/>
    <xsl:variable name="ServletView" select="//context//actions//menu[@id='View']/actionPerType[@id=$EntityType]/userRights[./text()=$UserRights]/@id"/>
    <xsl:variable name="queryPages" select="//stats/@queryPages"/>
    <xsl:variable name="end" select="//stats/@end"/>
    <xsl:variable name="start" select="//stats/@start"/>
    <xsl:variable name="count" select="//stats/@count"/>
    <xsl:variable name="currentP" select="//stats/@currentP"/>
    <xsl:variable name="pageLoop" select="//pageLoop/lista"/>
    <xsl:variable name="showPages" select="//showPages/show"/>
   <xsl:variable name="ServletName" select="//context/ServletName"/>
    <xsl:variable name="FileId" select="//context/FileId"/>


    
    <xsl:template match="/">
        <xsl:call-template name="page"/>
    </xsl:template>
    <xsl:template name="context">

        <td colSpan="{$columns}" vAlign="top" align="center" class="content">

            <br/>
            <div id="addCriterion" style="float:left;font-size:10pt;font-weight:bold;" onclick="javascript:toggleVisibility(document.getElementById('criterion'));$('#downArrow').toggle();$('#upArrow').toggle();">
                <xsl:variable name="tag" select=" 'Pros8hkhKrithriwn' "/>
                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                <a href="javascript:void(0)">
                <xsl:value-of select="$translated"/>
                 <span id="downArrow">▼</span>
                 <span id="upArrow" style="display:none;">▲</span>
                </a>
            </div>
            <br/>
            <script type="text/javascript">
                 var lang = '<xsl:value-of select="$lang"/>';
                 var servlet = '<xsl:value-of select="$ServletView"/>';
                 var hasLang = (servlet.indexOf("lang=")>-1);
                 if(hasLang){
                    servlet=servlet.replace("lang=","lang="+lang); 
                 }
            </script>        
            <form  method="post" id="criterionForm" action="TranslateServlet?action=totranslate&amp;style=criteria&amp;type={$EntityType}&amp;id={$FileId}">
                <div id="criterion" style="font-size:9pt;float:left;display:none;">
                    <xsl:variable name="tag" select=" 'lang' "/>
                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                    <xsl:value-of select="$translated"/>
                    <xsl:text> </xsl:text>:
                    <xsl:text> </xsl:text>
                    <xsl:variable name="tag" select=" 'Epilogi' "/>
                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                    <select id="lang" name="lang">                             
                        <option value="0">---
                            <xsl:value-of select="$translated"/>---
                        </option>
                        <xsl:for-each select="//context/Langs/Lang">
                            <xsl:if test="./text()!=$lang">                                
                                <xsl:variable name="tag" select="./text()"/>
                                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                <option value="{./text()}">
                                    <xsl:value-of select="$translated"/>
                                </option>  
                            </xsl:if>
                        </xsl:for-each>
                    </select>
                    <xsl:text> </xsl:text>
                    <xsl:text> </xsl:text>
                    <xsl:text> </xsl:text>
                    <xsl:text> </xsl:text>
                    <xsl:variable name="tag" select=" 'id' "/>
                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                    <xsl:value-of select="$translated"/>
                    <xsl:text> </xsl:text>:
                    <xsl:text> </xsl:text>
                    <input type="text" size="13" value="" name="code"/>
                    <xsl:text> </xsl:text>
                    <xsl:text> </xsl:text>
                    <xsl:text> </xsl:text>
                    <xsl:text> </xsl:text>
                    <xsl:variable name="tag" select="//context/SearchName/text() "/>
                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                    <xsl:value-of select="$translated"/>
                    <xsl:text> </xsl:text>:
                    <xsl:text> </xsl:text>
                    <input type="text" size="13" value="" name="name"/>
                    <xsl:text> </xsl:text>
                    <input type="image" src="formating/images/refresh.png"/>
                </div> 
            </form>
            <xsl:if test="count(//result)&gt;0">
                <br/>
                <p align="center" class="contentText">
                    <xsl:variable name="tag" select=" 'EpilogiDeltiouApo' "/>
                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                    <font size="4">
                        <strong>
                            <xsl:value-of select="$translated"/>
                        </strong>
                    </font>
                </p>
                <form method="post" action="TranslateServlet?action=translate&amp;mode={$AdminMode}&amp;type={$EntityType}&amp;id={$FileId}">
                    <table border="0" align="center"  valign="bottom" >
                    <tr>
                        <td>
							
                            <xsl:if test=" $queryPages &gt; 1">
                                <xsl:call-template name="SearchPaging">
                                    <xsl:with-param name="url" select="concat($ServletName, '?type=', $EntityType)"/>
                                    <xsl:with-param name="queryPages" select="$queryPages"/>
                                    <xsl:with-param name="currentP" select="$currentP"/>
                                    <xsl:with-param name="pageLoop" select="$pageLoop"/>
                                    <xsl:with-param name="showPages" select="$showPages"/>
                                    <xsl:with-param name="selected" select="1"/>
                                </xsl:call-template>
                            </xsl:if>
                        </td>
                    </tr>
                </table>				
                <script language="JavaScript" src="formating/javascript/utils/sortable.js"></script>     
                    <table border="0" align="center" class="results" cellspacing="1">
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
                            <td></td>
                            <td></td>
                        </tr>
                        <xsl:for-each select="//result">
                            <xsl:variable name="ID" select="./FileId/text()"/>
                            <tr id="resultRow" align="center" valign="middle" class="resultRow" onMouseOver="highlight(this, true)" onMouseOut="highlight(this, false)">
                                <td>
                                    <strong>
                                    <xsl:value-of select="./@pos"/>
                                </strong>
                                </td>
                                <xsl:for-each select="./*[name() != 'FileId' and name() !=  'info' and name() !=  'type']  ">
                                    <td>
                                        <xsl:for-each select="./*">

                                            <xsl:if test="position() > 1">
                                                <br/>
                                            </xsl:if>
                                          <xsl:choose>
                                            <xsl:when test="name() ='filename'">    
                                                <xsl:variable name="uriId" select=".//text()"/>                                   
                                                    <xsl:value-of select="$uriId"/>
                                            </xsl:when>
                                             <xsl:when test="name() ='ShortDescription'">    
                                                <xsl:variable name="description" select="./ShortDescription/text()"/>                                   
                                                    <xsl:variable name="short_description" select="substring($description,1,15)"/>                                   
                                                    <xsl:value-of select="$short_description"/>
                                                    <xsl:if test="$short_description!=''and $short_description!=$description" >
                                                        <xsl:value-of select="'...'"/>
                                                    </xsl:if>             
                                            </xsl:when>
                                            <xsl:when test="name() = 'ΨηφιακόΑρχείο' or name() = 'File' ">
                                                <xsl:variable name="tag" select=" 'Proboli' "/>
                                                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                                <xsl:variable name="mime" select="'Photos'"/>
                                           
                                                    <a href="FetchBinFile?mime={$mime}&amp;type={$EntityType}&amp;depository=disk&amp;file={url:encode(./text())}" target="blank_">
                                                        <!--Samarita Servlet attempt-->                                                               
                                                        <img src="FetchBinFile?mime={$mime}&amp;size=small&amp;type={$EntityType}&amp;depository=disk&amp;file={url:encode(./text())}" border="1" alt="{$translated}" width="50"></img>
                                                    </a>                                                     
                                            </xsl:when>
                                             <xsl:otherwise>
                                                    <xsl:value-of select="./text()"/>
                                             </xsl:otherwise>
                                           </xsl:choose>

                                        </xsl:for-each>
                                    </td>
                                </xsl:for-each>                          
                                <xsl:variable name="tag" select=" 'Proboli' "/>                               
                                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                <xsl:variable name="id" select="./FileId/text()"/>
                                <xsl:variable name="servletView" select="concat(servlet,$id)"/>
                                <td id="action" class="action" onmouseover="highlight(this, true)" onmouseout="highlight(this, false)" title="{$translated}">
                                    <a class="action" href="javascript:void(0)" onClick="previewPopUp(servlet+'{$id}', '{$EntityType}')">
                                        <img border="0" src="{ concat($systemRoot, '/formating/images/view.png') }"/>
                                    </a>
                                </td>                               
                                <xsl:variable name="tag" select=" 'EpilogiDeltiouApo' "/>
                                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                <td id="action" class="action" onmouseover="highlight(this, true)" onmouseout="highlight(this, false)" title="{$translated}">
                                    <input type="radio" name="fromId" value="{$ID}"></input>
                                </td>
                            </tr>
                        </xsl:for-each>
                    </table>
                    <br/>
                    <xsl:if test=" $queryPages &gt; 1">
                        <xsl:call-template name="SearchPaging">
                        <xsl:with-param name="url" select=" concat($ServletName, '?type=', $EntityType)"/>
                        <xsl:with-param name="queryPages" select="$queryPages"/>
                        <xsl:with-param name="currentP" select="$currentP"/>
                        <xsl:with-param name="pageLoop" select="$pageLoop"/>
                        <xsl:with-param name="showPages" select="$showPages"/>
                        <xsl:with-param name="selected" select="2"/>
                    </xsl:call-template>
                    </xsl:if>
             
                    <xsl:variable name="tag" select=" 'Oloklirwsi' "/>
                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                    <input type="submit" class="button" value="{$translated}" onclick="getObj('proccessing').style.display='block'"></input>
                    <xsl:variable name="tag" select=" 'LighWra' "/>
                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                    <p class="contentText" style="font-size:10pt">
                        <b>(
                            <xsl:value-of select="$translated"/>)
                        </b>
                    </p>
                </form>
                <xsl:choose>
                <xsl:when test="count(//context/hasCriteria)=0">

                    <form id="searchPagingForm" name="searchPagingForm" method="post" action="TranslateServlet?action=totranslate">
		    <input type="hidden" name="type" value="{$EntityType}"/>
                   <input type="hidden" name="id" value="{$FileId}"/>
                    <input type="hidden" name="paging" value="true"/>
                    <input type="hidden" name="pages" value="{$queryPages}"/>
                    <input type="hidden" name="current" value="{$currentP}"/>
                    <input type="hidden" name="newP" value=""/>
                    <input type="hidden" name="move" value=""/>
                </form>
                </xsl:when>
                <xsl:otherwise>
                    <form id="searchPagingForm" name="searchPagingForm" method="post" action="TranslateServlet?action=totranslate&amp;style=criteria&amp;type={$EntityType}&amp;id={$FileId}">
		    <input type="hidden" name="type" value="{$EntityType}"/>
                   <input type="hidden" name="id" value="{$FileId}"/>
                    <input type="hidden" name="paging" value="true"/>
                    <input type="hidden" name="pages" value="{$queryPages}"/>
                    <input type="hidden" name="current" value="{$currentP}"/>
                    <input type="hidden" name="newP" value=""/>
                    <input type="hidden" name="move" value=""/>
                    <input type="hidden" name="lang" value="{//context/hasCriteria/lang/text()}"/>
                    <input type="hidden" name="code" value="{//context/hasCriteria/code/text()}"/>
                    <input type="hidden" name="name" value="{//context/hasCriteria/name/text()}"/>
                </form> 
                </xsl:otherwise>
                </xsl:choose>
                <div id="proccessing" class="contentHeadText" style="font-size:10pt; display:none">
                    <xsl:variable name="tag" select=" 'SeExelixi' "/>
                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                    <xsl:value-of select="$translated"/>
                </div>
            </xsl:if>
            <xsl:if test="count(//result)&lt;1">
                <br/>
                <xsl:variable name="tag" select=" 'DenBrethikanArxeia' "/>
                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                <p align="center" style="font-size:11pt;font-weight:bold;">                           
                    <xsl:value-of select="$translated"/>
                </p>
                <p align="center" style="padding-left:10px; font-size:9pt;">
                    <xsl:variable name="tag" select=" 'Epistrofi' "/>
                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                    <a href="javascript:window.history.go(-1);">
                        <xsl:value-of select="$translated"/>
                    </a>
                </p>
            </xsl:if>
        </td>
    </xsl:template>
</xsl:stylesheet>
