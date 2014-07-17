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

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
       xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
       xmlns:xs="http://www.w3.org/TR/2008/REC-xml-20081126#">

       <xsl:strip-space elements="*"/>
       <xsl:output method="xml" indent="yes"/>

       <xsl:param name="BaseURI"><!-- Define if not completed by application-->
              <!-- http://www.exampleURI.net/STELLA-I -->
       </xsl:param>

       <!-- Begin RDF document -->
       <xsl:template match="/">
              <xsl:element name="rdf:RDF">
                     <rdf:Description>
                            <xsl:attribute name="rdf:about"/>
                            <xsl:apply-templates select="/*|/@*"/>
                     </rdf:Description>
              </xsl:element>
       </xsl:template>

       <!-- Turn XML elements into RDF triples. -->
       <xsl:template match="*">
              <xsl:param name="subjectname"/>

              <!-- Build URI for subjects resources from acestors elements -->
              <xsl:variable name="newsubjectname">
                     <xsl:if test="$subjectname=''">
                            <xsl:value-of select="$BaseURI"/>
                            <xsl:text>#</xsl:text>
                     </xsl:if>
                     <xsl:value-of select="$subjectname"/>
                     <xsl:value-of select="name()"/>
                     <!-- Add an ID to sibling element of identical name -->
                     <xsl:variable name="number">
                            <xsl:number/>
                     </xsl:variable>
                     <xsl:if test="$number > 1">
                            <xsl:text>_</xsl:text>
                            <xsl:number/>
                     </xsl:if>
              </xsl:variable>

              <xsl:element name="{name()}" namespace="{concat(namespace-uri(),'#')}">
                     <rdf:Description>
                            <xsl:attribute name="rdf:about">
                                   <xsl:value-of select="$newsubjectname"/>
                            </xsl:attribute>
                            <xsl:apply-templates select="@*|node()">
                                   <xsl:with-param name="subjectname"
                                          select="concat($newsubjectname,'/')"/>
                            </xsl:apply-templates>
                     </rdf:Description>
              </xsl:element>

              <!-- rdf:_no triple to preserve the order of elements, 
                   comment out if not needed -->
              <xsl:if test="count(../*) >1">
                     <xsl:element name="{concat('rdf:_',count(preceding-sibling::*)+1)}">
                            <rdf:Description>
                                   <xsl:attribute name="rdf:about">
                                          <xsl:value-of select="$newsubjectname"/>
                                   </xsl:attribute>
                            </rdf:Description>
                     </xsl:element>
              </xsl:if>
       </xsl:template>

       <!-- Create attribute triples. -->
       <xsl:template match="@*" name="attributes">
              <xsl:variable name="ns">
                     <!-- If attribute doesn't have a namespace use element namespace -->
                     <xsl:choose>
                            <xsl:when test="namespace-uri()=''">
                                   <xsl:value-of select="concat(namespace-uri(..),'#')"/>
                            </xsl:when>
                            <xsl:otherwise>
                                   <xsl:value-of select="concat(namespace-uri(),'#')"/>
                            </xsl:otherwise>
                     </xsl:choose>
              </xsl:variable>
              <xsl:element name="{name()}" namespace="{$ns}">
                     <xsl:value-of select="."/>
              </xsl:element>
       </xsl:template>

       <!-- Enclose text in an rdf:value element -->
       <xsl:template match="text()">
              <xsl:element name="rdf:value">
                     <xsl:value-of select="."/>
              </xsl:element>
       </xsl:template>

       <!-- Add triple to preserve comments -->
       <xsl:template match="comment()">
              <xsl:element name="xs:comment">
                     <xsl:value-of select="."/>
              </xsl:element>
       </xsl:template>

</xsl:stylesheet>
