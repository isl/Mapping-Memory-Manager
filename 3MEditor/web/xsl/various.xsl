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
    <xsl:variable name="action">
        <xsl:value-of select="//output/viewMode"/>
    </xsl:variable>
      <xsl:variable name="sourceAnalyzer">
        <xsl:value-of select="//output/sourceAnalyzer"/>
    </xsl:variable>
    <xsl:variable name="sourceAnalyzerFiles">
        <xsl:value-of select="//output/sourceAnalyzerFiles"/>
    </xsl:variable>
    
    <xsl:template match="target_node" name="target_node">
        <xsl:param name="pathSoFar"/>
        <xsl:apply-templates select="entity">
            <xsl:with-param name="pathSoFar" select="concat($pathSoFar,'/entity')"/>
        </xsl:apply-templates>    
    </xsl:template>
    
    <xsl:template name="substring-after-last">
        <xsl:param name="string" />
        <xsl:param name="delimiter" />
        <xsl:choose>
            <xsl:when test="contains($string, $delimiter)">
                <xsl:call-template name="substring-after-last">
                    <xsl:with-param name="string"
                                    select="substring-after($string, $delimiter)" />
                    <xsl:with-param name="delimiter" select="$delimiter" />
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>                
                <xsl:value-of select="$string" /> 
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="substring-after-last-and-remove-prefix">
        <xsl:param name="string" />
        <xsl:param name="delimiter" />
        <xsl:choose>
            <xsl:when test="contains($string, $delimiter)">
                <xsl:call-template name="substring-after-last-and-remove-prefix">
                    <xsl:with-param name="string"
                                    select="substring-after($string, $delimiter)" />
                    <xsl:with-param name="delimiter" select="$delimiter" />
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:choose>
                    <xsl:when test="contains($string,':')">
                        <xsl:value-of select="substring-after($string,':')" /> <!-- Also remove prefix-->
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="$string" /> 
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>


    <xsl:template name="stripPath">
        <xsl:param name="path"></xsl:param>
               
        <xsl:choose>
            <xsl:when test="starts-with($path,'//')">
                <xsl:value-of select="$path"/>

            </xsl:when>
            
            <xsl:when test="contains($path,'/')">
                <span title="{$path}">
                    <xsl:text>../</xsl:text>
                    <xsl:choose>
                        <xsl:when test="contains($path,'/Text()') or contains($path,'/text()')">
                            <xsl:call-template name="substring-after-last">
                                <xsl:with-param name="string">
                                    <xsl:call-template name="string-replace-all">
                                        <xsl:with-param name="text" select="$path" />
                                        <xsl:with-param name="replace" select="'/Text()'" />
                                        <xsl:with-param name="by" select="''" />
                                    </xsl:call-template>     
                                </xsl:with-param>
                                <xsl:with-param name="delimiter" select="'/'" />
                            </xsl:call-template>    
                            <xsl:choose>
                                <xsl:when test="contains($path,'/Text()')">
                                    <xsl:text>/Text()</xsl:text>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:text>/text()</xsl:text>
                                </xsl:otherwise>
                            </xsl:choose>                                                                                          
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:call-template name="substring-after-last">
                                <xsl:with-param name="string" select="$path"/>         
                                <xsl:with-param name="delimiter" select="'/'" />
                            </xsl:call-template>   
                        </xsl:otherwise>
                    </xsl:choose>
                </span>         
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$path"/>
            </xsl:otherwise>
            
        </xsl:choose>
    </xsl:template>

    <xsl:template name="string-replace-all">
        <xsl:param name="text" />
        <xsl:param name="replace" />
        <xsl:param name="by" />
        <xsl:choose>
            <xsl:when test="contains($text, $replace)">
                <xsl:value-of select="substring-before($text,$replace)" />
                <xsl:value-of select="$by" />
                <xsl:call-template name="string-replace-all">
                    <xsl:with-param name="text"
                                    select="substring-after($text,$replace)" />
                    <xsl:with-param name="replace" select="$replace" />
                    <xsl:with-param name="by" select="$by" />
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$text" />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>
