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

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:url="http://whatever/java/java.net.URLEncoder" version="1.0">
    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template name="externalFileLink">
        <xsl:choose>
            <xsl:when test="@schema_file">
                <xsl:call-template name="link">
                    <xsl:with-param name="filename" select="@schema_file">

                    </xsl:with-param>
                    <xsl:with-param name="message" select="'view'"/>

                </xsl:call-template>
                
                  
               
            </xsl:when>
            <xsl:when test="target_schema/@schema_file">
                <xsl:call-template name="link">
                    <xsl:with-param name="filename" select="target_schema/@schema_file">

                    </xsl:with-param>                                            
                    <xsl:with-param name="message" select="'view'"/>

                </xsl:call-template>
               
            </xsl:when>
            <xsl:when test="name()='dexample_data_source_record'">
                

            </xsl:when>
            <xsl:when test="name()='example_data_target_record'">
                <xsl:choose>
                    <xsl:when test="@rdf_link">
                        <xsl:call-template name="link">
                            <xsl:with-param name="filename" select="@rdf_link">
                        
                            </xsl:with-param>
                            <xsl:with-param name="message" select="'view rdf'"/>

                        </xsl:call-template>
                      
                    </xsl:when>
                   
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
           
                <xsl:choose>
                    
                    <xsl:when test="name()='xml_link'">
                        <xsl:call-template name="link">
                            <xsl:with-param name="filename" select=".">
                        
                            </xsl:with-param>                                            
                            <xsl:with-param name="message" select="'view xml'"/>

                        </xsl:call-template>
                        
                      
                    </xsl:when>
                   
                </xsl:choose>
                
                <xsl:choose>
                    <xsl:when test="name()='html_link'">
                        <xsl:call-template name="link">
                            <xsl:with-param name="filename" select="."/>
                        
                            <xsl:with-param name="message" select="'view html'"/>

                        </xsl:call-template>
                          
                    </xsl:when>
                  
                    
                </xsl:choose>
            </xsl:otherwise>
              
          
        </xsl:choose>
        
    </xsl:template>

    <xsl:template name="link">
        <xsl:param name="filename"></xsl:param>
        <xsl:param name="message"></xsl:param>

        <a title="{$filename}" target="_blank" style="display:inline;position:relative;top:1px;" >
            <xsl:attribute name="href">
                <xsl:text>FetchBinFile?</xsl:text>
                        
                <xsl:text>file=</xsl:text>
                <xsl:value-of select="url:encode($filename)"/>
            </xsl:attribute> 
            <xsl:value-of select="$message"/>
        </a>
        
    </xsl:template>

</xsl:stylesheet>
