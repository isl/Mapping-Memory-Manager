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
    <xsl:template name="if-ruleCell">                
        <xsl:param name="pathSoFar"/>
      
           
        <!-- handle  contents of 'if' tag -->
        <td>
            <!-- first remove  "../"   -->
            <xsl:for-each select=".//if[equals]">
                <xsl:variable name="pos" select="position()"/>
                <xsl:variable name="RangeIfVar">
                    <xsl:call-template name="string-replace-all">
                        <xsl:with-param name="text" select="equals"/>
                        <xsl:with-param name="replace" select="'../'"/>
                        <xsl:with-param name="by" select="''"/>
                    </xsl:call-template>
                </xsl:variable>
                <!-- now remove  "/text()"  -->
                <xsl:variable name="RangeIfVar1">
                    <xsl:call-template name="string-replace-all">
                        <xsl:with-param name="text" select="$RangeIfVar"/>
                        <xsl:with-param name="replace" select="'/text()'"/>
                        <xsl:with-param name="by" select="''"/>
                    </xsl:call-template>
                </xsl:variable>
                <!-- if  path=text() replace with  "  -->
                <xsl:variable name="RangeIfVar2">
                    <xsl:call-template name="string-replace-all">
                        <xsl:with-param name="text" select="$RangeIfVar1"/>
                        <xsl:with-param name="replace" select="'text()'"/>
                        <xsl:with-param name="by" select="(../../source_node|../../source_relation|../../../../source_relation|/../../../source_node)"/>
                    </xsl:call-template>
                </xsl:variable>
               
                <span  data-editable="" data-path="{concat($pathSoFar,'/if/or/if[',$pos,']/equals')}">
                    <xsl:value-of select="$RangeIfVar2"/>
                </span>&#160;
                <xsl:text>=</xsl:text>
               &#160;
               
                <span data-editable="" data-path="{concat($pathSoFar,'/if/or/if[',$pos,']/equals/@value')}">
                   
                    <xsl:value-of select="equals/@value"/>
                </span>
                               &#160;
               
                <xsl:choose>
                    <xsl:when test="count(../if)>1">
                        <a class="inline"  title="Delete if-rule" href="" onclick="action('Mapping?action=delete&amp;xpath={concat($pathSoFar,'/if/or/if[',$pos,']')}&amp;id={//output/id}');return false;">
                            <img src="formating/images/delete16.png"/>
                        </a>
                                       
                    </xsl:when>
                    <xsl:otherwise>
                        <a class="inline"  title="Delete if-rule" href="" onclick="action('Mapping?action=delete&amp;xpath={concat($pathSoFar,'/if')}&amp;id={//output/id}');return false;">
                          <img src="formating/images/delete16.png"/>
                        </a>
                    </xsl:otherwise>
                                   
                </xsl:choose>
              
                
                <br/>
            </xsl:for-each>
            
            <a  class="smallerLetters inline" title="Add if-rule" href="" >
                <xsl:attribute name="onclick">
                    <xsl:choose>
                        <xsl:when test="if">
                            <xsl:text>action('Mapping?action=addAfter___if/or/if&amp;xpath=</xsl:text>
                            <xsl:value-of select="concat($pathSoFar,'/if/or/if[last()]')"/>
                            <xsl:text>&amp;id=</xsl:text>
                            <xsl:value-of select="//output/id"/>
                            <xsl:text>');return false;</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:text>action('Mapping?action=addBefore___if&amp;xpath=</xsl:text>
                            <xsl:value-of select="concat($pathSoFar,'/*[1]')"/>
                            <xsl:text>&amp;id=</xsl:text>
                            <xsl:value-of select="//output/id"/>
                            <xsl:text>');return false;</xsl:text>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:attribute>
                <img  src="formating/images/plus16.png"/>           
                if-rule</a>
        </td>
              
       
    </xsl:template>

</xsl:stylesheet>
