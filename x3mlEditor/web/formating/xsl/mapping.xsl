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

This file is part of the x3mlEditor webapp of Mapping Memory Manager project.

-->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:include href="infoTable.xsl"/>
    <xsl:include href="domainRow.xsl"/>
    <xsl:include href="commentsRow.xsl"/>
    <xsl:include href="pathRow.xsl"/>\
    <xsl:include href="rangeRow.xsl"/>
    <xsl:include href="addAdditional.xsl"/>
    <xsl:include href="addIntermediate.xsl"/>
    <xsl:include href="commentsCell.xsl"/>
    <xsl:include href="if-ruleCell.xsl"/>
    <xsl:include href="entity.xsl"/>
    <xsl:include href="utils.xsl"/>




    <!-- updated 2013/11/07 by Dimitris Angelakis to manage Scehma version 2.0-->
    <!-- updated 2013/05/28 by Konstantina Konsolaki  -->
    <!-- next template taken from http://geekswithblogs.net/Erik/archive/2008/04/01/120915.aspx  -->
    <xsl:template name="string-replace-all">
        <xsl:param name="text"/>
        <xsl:param name="replace"/>
        <xsl:param name="by"/>
        <xsl:choose>
            <xsl:when test="contains($text, $replace)">
                <xsl:value-of select="substring-before($text,$replace)"/>
                <xsl:value-of select="$by"/>
                <xsl:call-template name="string-replace-all">
                    <xsl:with-param name="text" select="substring-after($text,$replace)"/>
                    <xsl:with-param name="replace" select="$replace"/>
                    <xsl:with-param name="by" select="$by"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$text"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="/">
        <html>
            <title>
                <xsl:value-of select="//x3ml/info/title"/>
            </title>
            <head>
                <script>
                    var id = <xsl:value-of select="//output/id"/>;                                   
                </script>
                <script src="formating/js/jquery.min.js"/>
                <script src="formating/js/scripts.js"/>
                <link href="formating/css/style.css"  rel="stylesheet"/>
                <link href="formating/css/fineuploader.css" rel="stylesheet"/>

                <xsl:if test="//viewMode=0">
                    <link href="formating/css/edit.css"  rel="stylesheet"/>
                    <script src="formating/js/edit.js"/>

                </xsl:if>
            </head> 
            <body onunload="closeAndUnlock('{//output/id}');">

                
                <xsl:call-template name="infoTable"/>
                <p/>
                <p/>
                <a name="Schema_Matching_Table"/>
               
                <table border="0" cellpadding="3" id="Schema_Matching_Table">
                     
                    <tr >
                        <td colspan="6" class="titleRibbon">Schema Matching Table&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;
  &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;
  &#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;
                            <div class="Value_Binding">
                                <a href="#Legend" style="color:white;">Legend</a>
                            </div>
                        </td>
                    </tr>
                   
                    <tr class="header" >
                        <!--
                        <th align="center" style="width:15px"> </th>
                        <th align="center" style="width:435px">source</th>
                        <th align="center" style="width:250px">target</th>
                        <th align="center" style="width:200px">if-rule</th>
                        <th align="center" style="width:50px">comments</th>
                        <th align="center" style="width:50px">actions</th>
                        -->
                        <th align="center"> </th>
                        <th align="center" >source</th>
                        <th align="center" >target</th>
                        <th align="center" >
                            <xsl:attribute name="style">
                                <xsl:choose>
                                    <xsl:when test="//exists">
                                        <xsl:text>min-width:230px;</xsl:text>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:text>min-width:50px;</xsl:text>
                                    </xsl:otherwise>
                                </xsl:choose> 
                            </xsl:attribute>
                            if-rule</th>
                        <th align="center" style="max-width:100px">comments</th>
                        <xsl:if test="//viewMode=0">
                            <th align="center" style="width:65px">actions</th>
                        </xsl:if>
                    </tr>
                   
                    <xsl:for-each select="//x3ml/mappings/mapping">
                        <xsl:variable name="pos" select="position()"/>
                        <xsl:call-template name="domainRow">
                            <xsl:with-param name="pos" select="$pos"/>
                        </xsl:call-template>
                        <xsl:for-each select="domain/comments/comment">                          
                            <xsl:call-template name="commentsRow"> 
                                <xsl:with-param name="pathSoFar" select="concat('//x3ml/mappings/mapping[',$pos,']','/domain/comments/comment')"/>
                            </xsl:call-template>                            
                        </xsl:for-each>

                        
                        <xsl:for-each select="link">
                            <xsl:variable name="pos2" select="position()"/>
                            <xsl:for-each select="path">
                                <xsl:call-template name="pathRow"> 
                                    <xsl:with-param name="pathSoFar" select="concat('//x3ml/mappings/mapping[',$pos,']','/link[',$pos2,']/path')"/>
                                </xsl:call-template>
                                <xsl:for-each select="comments/comment">                          
                                    <xsl:call-template name="commentsRow"> 
                                        <xsl:with-param name="pathSoFar" select="concat('//x3ml/mappings/mapping[',$pos,']','/link[',$pos2,']/path/comments/comment')"/>
                                    </xsl:call-template>                            
                                </xsl:for-each>
                                
                            </xsl:for-each>
                              
                            <xsl:for-each select="range">
                                <xsl:call-template name="rangeRow"> 
                                    <xsl:with-param name="pathSoFar" select="concat('//x3ml/mappings/mapping[',$pos,']','/link[',$pos2,']/range')"/>
                                </xsl:call-template>
                                <xsl:for-each select="comments/comment">                          
                                    <xsl:call-template name="commentsRow"> 
                                        <xsl:with-param name="pathSoFar" select="concat('//x3ml/mappings/mapping[',$pos,']','/link[',$pos2,']/range/comments/comment')"/>
                                    </xsl:call-template>                            
                                </xsl:for-each>
                            </xsl:for-each>
                            <tr height="3" class="spacerRow">                                  
                            </tr>
                          
                        </xsl:for-each>
                        <tr class="addLinkRow">
                            <td colspan="6" align="middle" >

                                <a class="inline" title="Add Link" href="" onclick="action('Mapping?action=add&amp;xpath={concat('//x3ml/mappings/mapping[',$pos,']','/link')}&amp;id={//output/id}');return false;">
                                    <img  src="formating/images/plus16.png"/>           
                                    New Link</a>
                            </td>
                        </tr>
                        <tr height="10" class="spaceBelowAddLinkRow"/>
                       
                    </xsl:for-each>
                      
                    <tr class="addMappingRow">
                        <td colspan="6" align="middle">
                       
                            <a class="inline" title="Add Mapping" href="" onclick="action('Mapping?action=add&amp;xpath=//x3ml/mappings/mapping&amp;id={//output/id}');return false;">
                                <img  src="formating/images/plus16.png"/>New Mapping</a>
                        </td>
                    </tr>
                </table>
                <p/>
                <a name="Legend"/>Legend 
                <br/>
                <div class="Internal_Node">&#160;Intermediate&#160;</div>&#160;&#160;&#160;&#160;&#160;
                <div class="Constant_Node" style="display:inline;">&#160;Additional&#160;</div>&#160;&#160;&#160;&#160;&#160;D:Domain&#160;&#160;&#160;&#160;&#160;P:Path&#160;&#160;&#160;R:Range
                <br/>
                <br/>
                <a href="#Schema_Matching_Table">Go to start of Schema Matching Table</a>
                <a href="changes.html" class="footnote">What's new</a>
                <br/>
                <script src="formating/js/jquery.jeditable.mini.js"/>
                <script src="formating/js/jquery.fineuploader-3.0.js"/>
                <input id="comboAPI" type="hidden" value="{//output/mode}"/>

            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
