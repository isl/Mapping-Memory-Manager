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

This file is part of the 3MEditor webapp of Mapping Memory Manager project.

-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
   
    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template match="path">
        <xsl:import href="various.xsl"/>
        <xsl:import href="entity.xsl"/>
        <xsl:import href="viewOnly.xsl"/>
        <xsl:param name="mappingPos"/>
        <xsl:param name="linkPos"/>
        <xsl:include href="comments.xsl"/>
        <xsl:include href="if-rule.xsl"/>

        <tr class="path clickable" data-xpath="{concat('//x3ml/mappings/mapping[',$mappingPos,']/link[',$linkPos,']')}" >
            <xsl:if test="$action=0">
                <xsl:attribute name="title">Click to edit link</xsl:attribute>
            </xsl:if>           
            <td title="{concat($mappingPos,'_',$linkPos)}">P</td>
            <td>
                <xsl:apply-templates select="source_relation"/>
            </td>
            <td style="min-width:500px;">        
                <xsl:apply-templates select="target_relation">
                    <xsl:with-param name="pathSoFar" select="concat('//x3ml/mappings/mapping[',$mappingPos,']/link[',$linkPos,']/path/target_relation')"/>
                </xsl:apply-templates>
            </td>
            <td>
                <xsl:for-each select="target_relation">

                    <xsl:call-template name="if-rule"></xsl:call-template>    
                </xsl:for-each>         
            </td>
            <td>
                <xsl:apply-templates select="comments"/>
            </td>
            <!--<td class="actions" ></td>-->

        </tr>      
                   
    </xsl:template>

    <xsl:template match="source_relation">
       
        <xsl:for-each select="*">
            <div class="row">
                    <div class="col-xs-1 iconContainer">
                        <xsl:choose>
                            <xsl:when test="name()='relation'">
                                <img src="images/property.png"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <img src="images/intermediate.png"/>
                            </xsl:otherwise>
                        </xsl:choose>
                </div>
                <div class=" col-xs-11 nextToIcon">
                    <xsl:call-template name="stripPath">
                        <xsl:with-param name="path" select="." />
                    </xsl:call-template>
                </div>
            </div>
            
            <!--            <xsl:value-of select="."/>-->
        </xsl:for-each>

    </xsl:template>

</xsl:stylesheet>
