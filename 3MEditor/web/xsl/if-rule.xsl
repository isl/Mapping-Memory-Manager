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
    <xsl:template name="if-rule">
        <xsl:apply-templates select="if"/>
    </xsl:template>

    <xsl:template match="if">
        <span class="text-center">
            <strong>
                <xsl:choose>
                    <xsl:when test="name(..)='not'">
                        <xsl:text>
                            NOT 
                        </xsl:text>
                    </xsl:when>
                    <xsl:when test="(name(..)='or' or name(..)='and') and position()!=1">
                        <xsl:text>
                   
                            <xsl:choose>
                                <xsl:when test="name(..)='or'">
                                    OR
                                </xsl:when>
                                <xsl:when test="name(..)='and'">
                                    AND
                                </xsl:when>
                            </xsl:choose>
                    
                        </xsl:text>
                    </xsl:when>
                </xsl:choose>
            </strong>
        </span>
       
         
        <xsl:apply-templates select="*"/>
     
    </xsl:template>
    <xsl:template match="or|and|not">                            
        
        <xsl:apply-templates select="if"/>
    </xsl:template>

    <xsl:template match="equals"> 
        <xsl:call-template name="stripPath">
            <xsl:with-param name="path" select="." />
        </xsl:call-template>
        <xsl:text> = </xsl:text>
        <xsl:call-template name="stripPath">
            <xsl:with-param name="path" select="@value" />
        </xsl:call-template>
        <!--<br/>-->
    </xsl:template>
    <xsl:template match="exists">
        exists(<xsl:call-template name="stripPath">
            <xsl:with-param name="path" select="." />
        </xsl:call-template>)
                                   
        <!--<br/>-->
        
    </xsl:template>
    <xsl:template match="narrower">
        <xsl:call-template name="stripPath">
            <xsl:with-param name="path" select="." />
        </xsl:call-template>
        <xsl:text> nt </xsl:text>
        <xsl:call-template name="stripPath">
            <xsl:with-param name="path" select="@value" />
        </xsl:call-template>
        <!--<br/>-->
    </xsl:template>

</xsl:stylesheet>
