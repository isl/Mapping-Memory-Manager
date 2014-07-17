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
    <xsl:variable name="tableStyle" select="concat('tableStyle',$lang)"/>
    <xsl:variable name="EntityCategory" select="//context/EntityCategory"/>
    <xsl:output method="html" indent="yes" encoding="UTF-8"/>
    <xsl:include href="../ui/page.xsl"/>
    <xsl:template match="/">
        <xsl:call-template name="page"/>
    </xsl:template>
    <xsl:template name="context">
        <link rel="stylesheet" type="text/css" href="formating/css/chosen_plugin/chosen.css"/>
        <script type="text/javascript" src="formating/javascript/chosen_plugin/chosen.jquery.js"></script>
        <script type="text/javascript" src="formating/javascript/chosen_plugin/chosen.jquery.min.js"></script>
      

        <td colSpan="{$columns}" vAlign="top"  class="content">
            <br/>
                  <script type="text/javascript">
        $(document).ready(function(){
        $('.chzn-select').chosen();                 
        var zidx = 100;
        $('.chzn-container').each(function(){
        $(this).css('z-index', zidx);
        zidx-=1;
        });
        });
    </script>
            <form id="searchForm" method="post" action="" style="margin-top:0px;">
                <div id="addCriterion"  onclick="javascript:toggleVisibility(document.getElementById('moreCriteria')); $('#downArrow').toggle();$('#upArrow').toggle();">
                    <xsl:variable name="tag" select=" 'ExtraSearchKrithria' "/>
                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                    <a href="#" style="float:left;font-size:10pt;font-weight:bold;"> 
                        <xsl:value-of select="$translated"/>
                         <span id="downArrow">▼</span>
                        <span id="upArrow" style="display:none;">▲</span>
                    </a>
                </div>
                <br/> 
                <div id="moreCriteria" style="font-size:9pt;display:none;">                   
                    <div style="font-size:10pt">
                        <xsl:if test="$EntityCategory='primary'">
                            <br/>
                            <xsl:variable name="tag" select=" 'AdminStatus' "/>
                            <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                            <xsl:value-of select="$translated"/>
                            <xsl:variable name="tag" select=" 'Epilogi' "/>
                            <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                            <select  data-placeholder="{$translated}" class="chzn-select" multiple="multiple" style="width:350px;" tabindex="4" id="extraStatus" name="extraStatus">                             
                                <option value=""></option> 
                                <xsl:for-each select="//context/statusType/status">
                                    <xsl:variable name="tag" select="./text()"/>
                                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                    <option  value="{./text()}">
                                        <xsl:value-of select="$translated"/>
                                    </option>
                                </xsl:for-each>
                            </select>                       
                        </xsl:if>   
                    </div>
                    <br/>
                    <div style="font-size:10pt">
                        <xsl:variable name="tag" select=" 'AdminUsers' "/>
                        <xsl:variable name="translated" select="$locale/leftmenu/*[name()=$tag]/*[name()=$lang]"/>
                        <xsl:value-of select="$translated"/>
                       
                        <xsl:variable name="tag" select=" 'Epilogi' "/>
                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                        <select data-placeholder="{$translated}" id="user" name="user" style="width:350px;" class="chzn-select" multiple="multiple" tabindex="6">
                            <option value=""></option>
                            <xsl:for-each select="//context/Users/group">
                                <optgroup label="{string(./@name)}">
                                    <xsl:for-each select="./userInGroup">
                                        <option value="{./text()}">
                                            <xsl:value-of select="./text()"/>
                                        </option>    
                                    </xsl:for-each>
                                </optgroup>
                            </xsl:for-each>
                        </select>
                    </div>
                    <br/>
                    <div style="font-size:10pt">
                        <xsl:variable name="tag" select=" 'AdminOrganization' "/>
                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                        <xsl:value-of select="$translated"/>                     
                        <xsl:variable name="tag" select=" 'Epilogi' "/>
                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                        <select data-placeholder="{$translated}" class="chzn-select" multiple="multiple" style="width:350px;" tabindex="4" id="org" name="org">                             
                            <option value=""></option>
                            <xsl:for-each select="//context/Groups/groups">
                                <option value="{string(./@id)}">
                                    <xsl:value-of select="./text()"/>
                                </option>                                   
                            </xsl:for-each>
                        </select>
                    </div>
                    <br/>
                    <div style="font-size:10pt">
                        <xsl:variable name="tag" select=" 'id' "/>
                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                        <xsl:value-of select="$translated"/>
                        <input type="text" size="13" value="" name="code"/>
                    </div>               
                </div> 
                <input type="hidden" name="target" value="{//context/query/targets/path[@selected='yes']/@xpath}"/>
                <table class="contentText2" border="0" width="100%"  cellspacing="0">				
                    <tr>
                        <td>
                            <br/>
                            <br/>
                            <table border="1" class="contentText2" id="criteria">                               
                                <xsl:choose>
                                    <xsl:when test="//context/query/info/operator='or'">                                           
                                        <input type="radio" name="operator" value="and"/>AND 
                                        <xsl:text> </xsl:text>                                           
                                        <input type="radio" name="operator" value="or" checked="checked"/>OR
                                    </xsl:when>
                                    <xsl:otherwise>                                            
                                        <input type="radio" name="operator" value="and" checked="checked"/>AND
                                        <xsl:text> </xsl:text>                                           
                                        <input type="radio" name="operator" value="or"/>OR
                                    </xsl:otherwise>
                                </xsl:choose>
                                								
                                <tbody id="criteriaBody">
                                    <tr id="header" class="contentHeadText" align="center">
                                        <!--
                                        <xsl:variable name="tag" select=" 'Parametros' "/>
                                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                        <td align="center"><xsl:value-of select="$translated"/></td>
                                        -->
                                        <xsl:variable name="tag" select=" 'EpiloghPediouKrithriou' "/>
                                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                        <td >
                                            <xsl:value-of select="$translated"/>
                                        </td>
                            
                                        <xsl:variable name="tag" select=" 'Sinthiki' "/>
                                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                        <td >
                                            <xsl:value-of select="$translated"/>
                                        </td>
                            
                                        <xsl:variable name="tag" select=" 'Timi' "/>
                                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                        <td colspan="2">
                                            <xsl:value-of select="$translated"/>
                                        </td>
                                        
                                        <!--td align="center"></td-->
                                    </tr>
                                    <xsl:for-each select="//context/query/inputs/input">
                                        <tr id="criterion" >
                                            <td style="display: none"> <!-- Proswrina display: none -->
                                                <input type="checkbox" id="inputparameter" name="inputparameter" value="{./@id}">
                                                    <xsl:if test="./@parameter='yes'">
                                                        <xsl:attribute name="checked">
                                                            <xsl:value-of select="checked"/>
                                                        </xsl:attribute>
                                                    </xsl:if>
                                                </input>
                                                <input type="hidden" id="inputid" name="inputid" value="{./@id}"/>
                                            </td>
                                            <td>
                                                <select name="input" onchange="javascript:onChangeField2(this, this.selectedIndex, this.parentNode.childNodes[1])">
                                                    <xsl:for-each select="./path">
                                                        <option value="{./@xpath}">
                                                            <xsl:if test="./@selected='yes'">
                                                                <xsl:attribute name="selected">
                                                                    <xsl:value-of select="selected"/>
                                                                </xsl:attribute>
                                                            </xsl:if>
                                                            <xsl:value-of select="."/>
                                                        </option>
                                                    </xsl:for-each>
                                                </select>
                                                <select id="dataTypes" name="input" disabled="disabled" style="display:none">
                                                    <xsl:for-each select="./path">
                                                        <option value="{./@dataType}">
                                                            <xsl:if test="./@selected='yes'">
                                                                <xsl:attribute name="selected">
                                                                    <xsl:value-of select="selected"/>
                                                                </xsl:attribute>
                                                            </xsl:if>
                                                            <xsl:value-of select="./@dataType"/>
                                                        </option>
                                                    </xsl:for-each>
                                                </select>
                                            </td>
                                            <td >
                                                
                                                <xsl:variable name="operPos" select="position()"/>
                                                
                                                <select id="string_inputoper" name="inputoper" >
                                                    <xsl:choose>

                                                        <xsl:when test="./path[@selected='yes']/@dataType='string'">
                                                            <xsl:attribute name="style">
                                                                <xsl:value-of select="'display:block'"/>
                                                            </xsl:attribute>
                                                        </xsl:when>
                                                        <xsl:when test="./path[@selected='yes']/@dataType='time'">
                                                            <xsl:attribute name="style">
                                                                <xsl:value-of select="'display:none'"/>
                                                            </xsl:attribute>
                                                            <xsl:attribute name="disabled">
                                                                <xsl:value-of select="true"/>
                                                            </xsl:attribute>
                                                        </xsl:when>
                                                        <xsl:otherwise>
                                                            <xsl:attribute name="style">
                                                                <xsl:value-of select="'display:block'"/>
                                                            </xsl:attribute>
                                                        </xsl:otherwise>

                                                    </xsl:choose>
                                                   
 
                                                    <xsl:for-each select="//types/string/operator">
                                                        <option value="{./text()}">
                                                            <!--
                                                            <xsl:if test="./@selected='yes'">
                                                            <xsl:attribute name="selected">
                                                            <xsl:value-of select="selected"/>
                                                            </xsl:attribute>
                                                            </xsl:if>
                                                            -->
                                                            <xsl:if test="./text()=//input[position()=$operPos]/oper/text()">
                                                                <xsl:attribute name="selected">
                                                                    <xsl:value-of select="selected"/>
                                                                </xsl:attribute>
                                                            </xsl:if>
                                                            <xsl:value-of select="@*[name(.)=$lang]"/>
                                                        </option>
                                                    </xsl:for-each>
                                                   
                                                </select>
                                                <select id="time_inputoper" name="inputoper">
                                                    <xsl:choose>
                                                       
                                                        <xsl:when test="./path[@selected='yes']/@dataType='time'">
                                                            <xsl:attribute name="style">
                                                                <xsl:value-of select="'display:block'"/>
                                                            </xsl:attribute>
                                                           
                                                        </xsl:when>
                                                        <xsl:when test="./path[@selected='yes']/@dataType='string'">
                                                            <xsl:attribute name="style">
                                                                <xsl:value-of select="'display:none'"/>
                                                            </xsl:attribute>
                                                            <xsl:attribute name="disabled">
                                                                <xsl:value-of select="true"/>
                                                            </xsl:attribute>
                                                        </xsl:when>
                                                        <xsl:otherwise>
                                                            <xsl:attribute name="style">
                                                                <xsl:value-of select="'display:none'"/>
                                                            </xsl:attribute>
                                                            <xsl:attribute name="disabled">
                                                                <xsl:value-of select="true"/>
                                                            </xsl:attribute>
                                                        </xsl:otherwise>
                                                        
                                                    </xsl:choose>
                                                    <xsl:for-each select="//types/time/operator">
                                                        <option value="{./text()}">
                                                            <!--
                                                            <xsl:if test="./@selected='yes'">
                                                            <xsl:attribute name="selected">
                                                            <xsl:value-of select="selected"/>
                                                            </xsl:attribute>
                                                            </xsl:if>
                                                            -->
                                                            <xsl:if test="./text()=//input[position()=$operPos]/oper/text()">
                                                                <xsl:attribute name="selected">
                                                                    <xsl:value-of select="selected"/>
                                                                </xsl:attribute>
                                                            </xsl:if>
                                                            <xsl:value-of select="@*[name(.)=$lang]"/>
                                                        </option>
                                                    </xsl:for-each>
                                                   
                                                </select>
                                                
                                                <!-- matching operator for the selected field. updated in javascript:onChangeField -->
                                                <!--
                                                <select id="inputoper" name="inputoper" style="display:block;">
                                                <xsl:for-each select="./path">
                                                <option value="{./@oper}">
                                                <xsl:if test="./@selected='yes'">
                                                <xsl:attribute name="selected">
                                                <xsl:value-of select="selected"/>
                                                </xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="./@oper"/>
                                                </option>
                                                </xsl:for-each>
                                                </select>
                                                -->
                                            </td>
                               
                                            <td  nowrap="nowrap">                                             
                                                <input type="text" id="inputvalue1" name="inputvalue" size="15" value="{./value}">
                                                    <xsl:choose>
                                                       
                                                        <xsl:when test="./path[@selected='yes']/@dataType='string'">
                                                            <xsl:attribute name="style">
                                                                <xsl:value-of select="'display:block'"/>
                                                            </xsl:attribute>
                                                        </xsl:when>
                                                        <xsl:when test="./path[@selected='yes']/@dataType='time'">
                                                            <xsl:attribute name="style">
                                                                <xsl:value-of select="'display:none'"/>
                                                            </xsl:attribute>
                                                            <xsl:attribute name="disabled">
                                                                <xsl:value-of select="true"/>
                                                            </xsl:attribute>
                                                        </xsl:when>
                                                        <xsl:otherwise>
                                                            <xsl:attribute name="style">
                                                                <xsl:value-of select="'display:block'"/>
                                                            </xsl:attribute>
                                                        </xsl:otherwise>

                                                    </xsl:choose>
                                                </input>
                                                <div name="timeDiv" id="timeDiv">
                                                    <xsl:choose>
                                                       
                                                        <xsl:when test="./path[@selected='yes']/@dataType='time'">
                                                            <xsl:attribute name="style">
                                                                <xsl:value-of select="'display:block'"/>
                                                            </xsl:attribute>
                                                           
                                                        </xsl:when>
                                                        <xsl:when test="./path[@selected='yes']/@dataType='string'">
                                                            <xsl:attribute name="style">
                                                                <xsl:value-of select="'display:none'"/>
                                                            </xsl:attribute>
                                                        &#160;
                                                        </xsl:when>
                                                        <xsl:otherwise>
                                                            <xsl:attribute name="style">
                                                                <xsl:value-of select="'display:none'"/>
                                                            </xsl:attribute>
                                                       
                                                        </xsl:otherwise>
                                                        
                                                    </xsl:choose>
                                                    <input type="text" id="inputvalue2" name="inputvalue" size="15" onkeydown="timeCheck(this);" onkeyup="timeCheck(this);" value="{./value}">
                                                        <xsl:choose>
                                                            <xsl:when test="./path[@selected='yes']/@dataType='time'">
                                                                <!--xsl:attribute name="disabled">
                                                                    <xsl:value-of select="false"/>
                                                                </xsl:attribute-->
                                                            </xsl:when>
                                                            <xsl:when test="./path[@selected='yes']/@dataType='string'">
                                                                <xsl:attribute name="disabled">
                                                                    <xsl:value-of select="true"/>
                                                                </xsl:attribute>
                                                            </xsl:when>
                                                            <!--xsl:otherwise>
                                                            <xsl:attribute name="disabled">
                                                            <xsl:value-of select="true"/>
                                                            </xsl:attribute>
                                                            </xsl:otherwise-->
                                                        </xsl:choose>
                                                    </input>
                                                    <img src="formating/images/HelpTimeButton.bmp" onclick="javascript:popUp('time_directives/HelpPage_{$lang}.html','helpPage',450,580);"></img>
                                                </div>                                               
                                            </td>
                                            <td nowrap="nowrap">		
                                                <xsl:variable name="tag" select=" 'ProsthikiKritiriou' "/>
                                                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                                <input type="button" class="button" name="more" value="+" onclick="addCriterion('criteriaBody', 'criterion');" />
                                                <xsl:variable name="tag" select=" 'DiagrafiKritiriou' "/>
                                                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                                <input type="button" class="button" value="X" title="{$translated}" onClick="removeRow(this.parentNode.parentNode)" style="font-weight: bold; color: #FF0000"/>
                                            </td>                                    
                                        </tr>
                                    </xsl:for-each>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                   
                    <!--If added by samarita so that when outputs are no more than 11, more outputs component will not be shown -->
                    <!-- MORE OUTPUTS-->
                      
                    <tr>                            
                        <td>
                            <table border="1"  class="contentText2" id="outputs">
                                <tbody id="outputBody">
                                    <br/>
                                    <br/>
                                    <tr id="header" class="contentHeadText">
                                        <xsl:variable name="tag" select=" 'EpiloghPediwnEksodou' "/>
                                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>   
                                        <td align="center">
                                            <xsl:value-of select="$translated"/>
                                        </td>
                                    </tr>                                  
                                    <xsl:choose>
                                        <xsl:when test="//context/query/outputs/path[position() &gt; 1]/@selected='yes' ">
                                            <xsl:for-each select="//context/query/outputs/path[position() &gt; 1 and @selected='yes']">
                                                <xsl:variable name="outXpath" select="./@xpath"/>
                                                <tr id="outputR" >
                                                    <td>
                                                        <select id="outputSel" name="output">
                                                            <xsl:for-each select="//context/query/inputs/input[1]/path[position() &gt; 1]">
                                                                <option value="{./@xpath}">
                                                                    <xsl:if test="$outXpath = ./@xpath">
                                                                        <xsl:attribute name="selected">
                                                                            <xsl:value-of select="selected"/>
                                                                        </xsl:attribute>
                                                                    </xsl:if>
                                                                    <xsl:value-of select="."/>
                                                                </option>
                                                            </xsl:for-each>
                                                        </select>
                                                        <xsl:variable name="tag" select=" 'ProsthikiExodou' "/>
                                                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                                        <input type="button" class="button" name="moreOutputs" value="+" onclick="addOutput('outputBody', 'outputR');" />
                                        
                                                        <xsl:variable name="tag" select=" 'DiagrafiExodou' "/>
                                                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                                        <input type="button" class="button" value="X" title="{$translated}" onClick="removeRow(this.parentNode.parentNode)" style="font-weight: bold; color: #FF0000"/>
                                                    </td>
                                                </tr>
                                            </xsl:for-each>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <tr id="outputR">
                                                <td>
                                                    <select id="outputSel" name="output" >
                                                        <xsl:for-each select="//context/query/inputs/input[1]/path[position() &gt; 1]">
                                                            <option value="{./@xpath}">
                                                                <xsl:value-of select="."/>
                                                            </option>
                                                        </xsl:for-each>
                                                    </select>
                                                    <xsl:variable name="tag" select=" 'ProsthikiExodou' "/>
                                                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                                    <input type="button" class="button" name="moreOutputs" value="+" onclick="addOutput('outputBody', 'outputR');" />
                                                    <xsl:variable name="tag" select=" 'DiagrafiExodou' "/>
                                                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                                    <input type="button" class="button" value="X" title="{$translated}" onClick="removeRow(this.parentNode.parentNode)" style="font-weight: bold; color: #FF0000"/>
                                                </td>
                                            </tr>
                                        </xsl:otherwise>
                                    </xsl:choose>                        
                                </tbody>
                            </table>                            
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <table> 
                                <br/>
                                <tr>                                    
                                    <td>
                                        <xsl:variable name="tag" select=" 'Eperotisi' "/>
                                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                        <input type="submit" class="button" name="submit4search" value="{$translated}" onClick="submitFormTo('searchForm', 'SearchResults')" style="width:180"/> 
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
				
                <!--span align="right" class="contentTitleText">
                <xsl:variable name="tag" select=" 'Kritiria' "/>
                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                <xsl:value-of select="$translated"/>
                </span-->
                <form id="savedQueries" method="post" action="Search" style="margin-bottom:0px;">
                    <!--............'<xsl:value-of select="$tableStyle"/>'.........<xsl:value-of select="$lang"/>-->
                    <table class="contentText2" border="0" width="100%"  cellspacing="0">
                        <tr>
                            <td>
                                <br/>
                                <div class="contentText2">
                                    <select id="qid" name="qid">
                                        <xsl:variable name="tag" select=" 'EpilexteApo8hkeymenhEperwtisi' "/>
                                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                        <option value="0">---------- 
                                            <xsl:value-of select="$translated"/> ----------
                                        </option>
                                        <xsl:variable name="tag" select=" 'Genikes' "/>
                                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                        <option value="0">--------------- 
                                            <xsl:value-of select="$translated"/> ---------------
                                        </option>                                                
                                        <xsl:for-each select="//publicQueries">
                                            <option value="{./@id}">
                                                <xsl:if test="./@selected='yes'">
                                                    <xsl:attribute name="selected">
                                                        <xsl:value-of select="selected"/>
                                                    </xsl:attribute>                                                   
                                                </xsl:if>
                                                <xsl:value-of select="."/>
                                            </option>
                                        </xsl:for-each>
                                        <xsl:variable name="tag" select=" 'Prosopikes' "/>
                                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                        <option value="0">---------------- 
                                            <xsl:value-of select="$translated"/> ----------------
                                        </option>
                                        <xsl:for-each select="//personalQueries">
                                            <option value="{./@id}">
                                                <xsl:if test="./@selected='yes'">
                                                    <xsl:attribute name="selected">
                                                        <xsl:value-of select="selected"/>  
                                                    </xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="."/>
                                            </option>
                                        </xsl:for-each>
                                    </select>&#160;
                                    <xsl:variable name="tag" select=" 'Epilogi' "/>
                                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                    <input  type="submit" class="button" name="submit4select" value="{$translated}"/>
                                </div>
                                <br/>
                                <div class="contentText2">
                                    <xsl:variable name="tag" select=" 'Prosopikes' "/>
                                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                    <input align="center" type="radio" name="type" value="personal" checked="checked"/>                                  
                                    <xsl:value-of select="$translated"/>
                                    <xsl:variable name="tag" select=" 'Genikes' "/>
                                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                    <input  type="radio" name="type" value="public"/>
                                    <xsl:value-of select="$translated"/>
                                    <br/>
                                    <xsl:variable name="tag" select=" 'MnimonikoOnomaEperwthshs' "/>
                                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                    <xsl:value-of select="$translated"/>:
                                    <input type="text" id="mnemonicName" name="mnemonicName" value="{//context/query/info/name/text()}" size="40" width="30"/>
                                    <xsl:variable name="tag" select=" 'Apothikeusi' "/>
                                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                    <input type="submit" class="button" name="submit4save" value="{$translated}" onClick="submitFormTo('searchForm', 'SearchSave')"/>
                                    <xsl:if test="//context/isPersonal='true'">
                                        <xsl:variable name="tag" select=" 'Diagrafi' "/>
                                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                        <xsl:variable name="tag2" select=" 'Erwtimatiko' "/>
                                        <xsl:variable name="translated2" select="$locale/context/*[name()=$tag2]/*[name()=$lang]"/>
                                        <xsl:variable name="tag1" select=" 'DiagrafiPrompt' "/>
                                        <xsl:variable name="translated1" select="$locale/context/*[name()=$tag1]/*[name()=$lang]"/>
                                        <input type="submit" class="button" name="submit4delete" value="{$translated}" onClick="if (confirmAction('{$translated1} \''+getObj('mnemonicName').value+'\'{$translated2}')) submitFormTo('searchForm', 'SearchDelete'); else return false;"/>
                                    </xsl:if>                               
                                </div> 
                            </td>
                        </tr>
                    </table>
                    <input type="hidden" name="category" value="{//context/query/info/category}"/>
                    <input type="hidden" name="status" value="{//context/query/info/status}"/>
                </form>

                <input type="hidden" name="qid" value="{//context/query/@id}"/>
                <input type="hidden" name="category" value="{//context/query/info/category}"/>
                <input type="hidden" name="status" value="{//context/query/info/status}"/>
            </form>
        </td>

    </xsl:template>
 
</xsl:stylesheet>