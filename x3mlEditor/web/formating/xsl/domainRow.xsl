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

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template name="domainRow" match="/">
        <xsl:param name="pos"/>
        
        <xsl:variable name="pathSoFar" select="concat('//x3ml//mapping[',$pos,']','/domain')"></xsl:variable>
        <tr bgcolor="#708090" class="domainRow">
            <td class="whiteAndBold">                                
                <a title="Domain">D</a>
            </td>
            <td>&#160;
                <span class="whiteAndBold" data-editable="text" data-path="{concat($pathSoFar,'/source_node')}">
                    <xsl:value-of select="domain/source_node"/>
                </span>
                <!--img src="formating/images/idea16.png"/--> 
            </td>
            <td class="whiteAndBold" >
                <xsl:for-each select="domain/target_node/entity">
                    <xsl:call-template name="entity">
                        <xsl:with-param name="pathSoFar" select="concat($pathSoFar,'/target_node/entity')">
                        </xsl:with-param>
                    </xsl:call-template>
                    
                 
                    <xsl:if test="//viewMode=0">
                        
                        <xsl:call-template name="addAdditional">
                            <xsl:with-param name="pathSoFar" select="concat($pathSoFar,'/target_node/entity')"/>
                        </xsl:call-template>
                    </xsl:if>
                    
                    
                    
                    
                </xsl:for-each>
            </td>
            <!--
            <td class="whiteAndBold" data-editable="select" data-path="{concat('//x3ml//mapping[',$pos,']','/domain/entity/@tag')}">
                <xsl:value-of select="domain/entity/@tag"/>
            </td>
            -->
            <xsl:for-each select="domain">
                <xsl:for-each select="target_node">
                    <xsl:call-template name="if-ruleCell">
                        <xsl:with-param name="pathSoFar" select="concat($pathSoFar,'/target_node')"/>
                        <!--xsl:with-param name="entityOrProperty" select="'entity'"/-->
                    </xsl:call-template>
                </xsl:for-each>
                <xsl:call-template name="commentsCell">
                    <xsl:with-param name="pathSoFar" select="$pathSoFar"/>

                </xsl:call-template>
            </xsl:for-each>
                        
           
            <xsl:if test="//viewMode=0">
             
                <td>
                    <xsl:choose>
                        <xsl:when test="count(//x3ml//mapping)>1">
                            <a style="float:right;" title="Delete Mapping" href="" onclick="confirmDialog('Mapping');action('Mapping?action=delete&amp;xpath={concat('//x3ml//mapping[',$pos,']')}&amp;id={//output/id}');return false;">
                                <img src="formating/images/delete16.png"/>
                            </a>
                        </xsl:when>
                        <xsl:otherwise>
                            <img style="float:right;opacity:0.3;"  src="formating/images/delete16.png"/>
                        </xsl:otherwise>
                    
                    </xsl:choose>
                    <xsl:choose>
                        <xsl:when test="preceding-sibling::mapping">
                            <a style="float:right;" title="Move Mapping Up" href="" onclick="action('Mapping?action=moveUp&amp;xpath={concat('//x3ml//mapping[',$pos,']')}&amp;id={//output/id}');return false;">
                                <img src="formating/images/up16.png"/>
                            </a>
                        </xsl:when>
                        <xsl:otherwise>
                            <img style="float:right;opacity:0.3;"  src="formating/images/up16.png"/>

                        </xsl:otherwise>
                    </xsl:choose>
                    <xsl:choose>
                        <xsl:when test="following-sibling::mapping">
                            <a style="float:right;" title="Move Mapping Down" href="" onclick="action('Mapping?action=moveDown&amp;xpath={concat('//x3ml//mapping[',$pos,']')}&amp;id={//output/id}');return false;">
                                <img src="formating/images/down16.png"/>
                            </a>
                        </xsl:when>
                        <xsl:otherwise>
                            <img style="float:right;opacity:0.3;" src="formating/images/down16.png"/>
                        </xsl:otherwise>
                    </xsl:choose>
                    <a style="float:right;" class="Value_Binding inline" title="Clone Mapping" href="" onclick="action('Mapping?action=clone&amp;xpath={concat('//x3ml//mapping[',$pos,']')}&amp;id={//output/id}');return false;">
                        <img style="vertical-align:bottom;"  src="formating/images/plus16.png"/>           
                    </a>
                </td>
                   
            </xsl:if>
            
        </tr>
    </xsl:template>

</xsl:stylesheet>
