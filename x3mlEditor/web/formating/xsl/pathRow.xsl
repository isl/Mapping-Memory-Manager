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
    <xsl:include href="additional.xsl"/>


    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template name="pathRow" match="/">
        <xsl:param name="pathSoFar"/>


        <tr bgcolor="#D3D3D3" class="pathRow" >
            <td >
                <a title="Path">P</a>
            </td>
            <td>&#160;
                <span style="font-style:italic;" data-editable="" data-path="{concat($pathSoFar,'/source_relation')}">
                    <xsl:value-of select="source_relation"/>
                </span><!--img src="formating/images/idea16.png"/--> 
            </td>
            <td>                                                                                                 
                                                
                <span  class="relationship" data-editable="select" data-path="{concat($pathSoFar,'/target_relation/relationship[1]')}">
                    <xsl:call-template name="substring-after-last-and-remove-prefix">
                        <xsl:with-param name="string" select="target_relation/relationship[1]" />
                        <xsl:with-param name="delimiter" select="'/'" />
                    </xsl:call-template>
                </span>
                <br/>
                
                
                <xsl:for-each select="target_relation/relationship[position()>1]">
                    <xsl:variable name="relPos" select="position()+1"/>
                    <xsl:for-each select="..">
                        <xsl:call-template name="intermediate"> 
                            <xsl:with-param name="pathSoFar" select="concat($pathSoFar,'/target_relation')"/>
                            <xsl:with-param name="pos3" select="$relPos"/>
                        </xsl:call-template>  
                    </xsl:for-each>
                </xsl:for-each>
              
                
                <xsl:if test="//viewMode=0">
             
                    <xsl:call-template name="addIntermediate">
                        <xsl:with-param name="pathSoFar" select="concat($pathSoFar,'/target_relation')"/>
                    </xsl:call-template>
                </xsl:if>
                            
                                        
                   
            </td>
            <xsl:for-each select="target_relation">
                <xsl:call-template name="if-ruleCell">
                    <xsl:with-param name="pathSoFar" select="concat($pathSoFar,'/target_relation')"/>
                    <!--xsl:with-param name="entityOrProperty" select="'property'"/-->

                </xsl:call-template>
            </xsl:for-each>
           
            <xsl:call-template name="commentsCell">
                <xsl:with-param name="pathSoFar" select="$pathSoFar"/>

            </xsl:call-template>
          
            <xsl:if test="//viewMode=0">
                <td rowspan="2">
                           
                    <a style="float:right;" title="Delete Link" href="" onclick="confirmDialog();action('Mapping?action=delete&amp;xpath={concat($pathSoFar,'/..')}&amp;id={//output/id}');return false;">
                        <img src="formating/images/delete16.png"/>
                    </a>                        
                    <xsl:choose>
                        <xsl:when test="../preceding-sibling::link">
                            <a style="float:right;" title="Move Link Up" href="" onclick="action('Mapping?action=moveUp&amp;xpath={concat($pathSoFar,'/..')}&amp;id={//output/id}');return false;">
                                <img src="formating/images/up16.png"/>
                            </a>
                        </xsl:when>
                        <xsl:otherwise>
                            <img style="float:right;opacity:0.3;"  src="formating/images/up16.png"/>

                        </xsl:otherwise>
                    </xsl:choose>
                    <xsl:choose>
                        <xsl:when test="../following-sibling::link">
                            <a style="float:right;" title="Move Link Down" href="" onclick="action('Mapping?action=moveDown&amp;xpath={concat($pathSoFar,'/..')}&amp;id={//output/id}');return false;">
                                <img src="formating/images/down16.png"/>
                            </a>
                        </xsl:when>
                        <xsl:otherwise>
                            <img style="float:right;opacity:0.3;" src="formating/images/down16.png"/>
                        </xsl:otherwise>
                    </xsl:choose>
                                            
                    <a style="float:right;" class="Value_Binding inline" title="Clone Link" href="" onclick="action('Mapping?action=clone&amp;xpath={concat($pathSoFar,'/..')}&amp;id={//output/id}');return false;">
                        <img style="vertical-align:bottom;"  src="formating/images/plus16.png"/>           
                    </a>
                </td>
            </xsl:if>
                                
        </tr>
    </xsl:template>

</xsl:stylesheet>
