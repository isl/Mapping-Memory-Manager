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
    <xsl:template match="additional">
        <xsl:param name="pathSoFar"/>
        <xsl:param name="pos3"/>          
       
        <div  class="Constant_Node">
            
            <xsl:attribute name="style">
                <!--
         <xsl:choose>
             <xsl:when test="name(..)='internal_node'">
                -->
                <xsl:text>float:left;width:90%;position:relative;left:10px;display:block;</xsl:text>
                <!--  </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>float:left;width:90%;position:relative;left:10px;display:block;</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>-->
                <xsl:if test="name(../../..)='domain'">
                    <xsl:text>color:black;</xsl:text>
                </xsl:if>
            </xsl:attribute>
           
            <xsl:if test="//viewMode='0' or relationship!='' or entity/type!='' or entity/instance_info/constant!='' ">
                <span data-editable="select" data-path="{concat($pathSoFar,'/additional[',$pos3,']/relationship')}">
                    <xsl:call-template name="substring-after-last-and-remove-prefix">
                        <xsl:with-param name="string" select="relationship" />
                        <xsl:with-param name="delimiter" select="'/'" />
                    </xsl:call-template>
                </span>  
                <a title="Delete Additional" style="float:right;" class="internalAndconstantNodesLinks" href="" onclick="action('Mapping?action=delete&amp;xpath={concat($pathSoFar,'/additional[',$pos3,']')}&amp;id={//output/id}');return false;">
                    <img src="formating/images/delete16.png"/><!--Delete Constant Node-->
                </a>
                <br/>  
                <span style="display:inline;" data-editable="select" data-path="{concat($pathSoFar,'/additional[',$pos3,']/entity/type')}">
                    <!--xsl:value-of select="entity/@tag"/-->
                    <xsl:call-template name="substring-after-last-and-remove-prefix">
                        <xsl:with-param name="string" select="entity/type" />
                        <xsl:with-param name="delimiter" select="'/'" />
                    </xsl:call-template>
                </span> &#160;=&#160;<span class="Value_Binding">"</span>
                <span style="display:inline;" class="Value_Binding" data-editable="" data-path="{concat($pathSoFar,'/additional[',$pos3,']/entity/instance_info/constant')}">
                    <xsl:value-of select="entity/instance_info/constant"/>                               
                </span>
                <span class="Value_Binding">"</span> 
                &#160;<a class="Value_Binding inline" title="Clone additional" href="" onclick="action('Mapping?action=clone&amp;xpath={concat($pathSoFar,'/additional[',$pos3,']')}&amp;id={//output/id}');return false;">
                    <img style="vertical-align:bottom;"  src="formating/images/plus16.png"/>           
                    Same Additional</a>
                
            
                <xsl:if test="following-sibling::additional">
                    <hr/>
                </xsl:if>
            </xsl:if>
        </div>
       
       
        <!--
        <xsl:call-template name="addIntermediate">
            <xsl:with-param name="pathSoFar" select="$pathSoFar"/>
        </xsl:call-template>-->
    </xsl:template>
</xsl:stylesheet>
