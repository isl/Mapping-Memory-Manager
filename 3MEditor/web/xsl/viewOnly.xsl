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
    <xsl:template match="target_relation" name="target_relation">
        <xsl:param name="pathSoFar"/>
        
        <div class="row">
            <div class="col-xs-12 iconContainer">
                <span >
                    <xsl:attribute name="style">margin-right:30px;</xsl:attribute>
                    <img src="images/property.png"/>
                </span>          
                <i>
                    <xsl:call-template name="substring-after-last-and-remove-prefix">
                        <xsl:with-param name="string" select="relationship[1]" />
                        <xsl:with-param name="delimiter" select="'/'" />
                    </xsl:call-template>
                </i>
            </div>
        </div>

        <xsl:for-each select="relationship[position()>1]|entity">
            
            <xsl:choose>
                <xsl:when test="name()='entity'">

                    <xsl:apply-templates select=".">
                        <xsl:with-param name="pathSoFar" select="concat($pathSoFar,'/entity[',round(position() div 2),']')"/>
                    </xsl:apply-templates>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select=".">
                    </xsl:apply-templates>
                </xsl:otherwise>
            </xsl:choose>
            
      
        </xsl:for-each>
         
    </xsl:template>
    
    <xsl:template match="relationship" name="relationship">
        <div class="row">
            
            <div>

                <xsl:attribute name="class">col-xs-12 iconContainer</xsl:attribute>

                <span >
                    <xsl:attribute name="style">margin-right:30px;</xsl:attribute>
                    <img src="images/property.png"/>
                </span>   
              
                <i>
                    <xsl:call-template name="substring-after-last-and-remove-prefix">
                        <xsl:with-param name="string" select="." />
                        <xsl:with-param name="delimiter" select="'/'" />
                    </xsl:call-template>
                </i>
        
                <!--<br/>-->

        
            </div>
        </div>
         
    </xsl:template>

</xsl:stylesheet>
