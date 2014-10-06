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
    <xsl:template match="/" name="externalFile">
        <xsl:param name="pathSoFar"/>
        <xsl:choose>
            <xsl:when test="@schema_file">
                <a target="_blank">
                    <xsl:attribute name="href">
                        <xsl:text>FetchBinFile?</xsl:text>
                        
                        <xsl:text>file=</xsl:text>
                        <xsl:value-of select="@schema_file"/>
                    </xsl:attribute> 
                    <xsl:text>view </xsl:text>
                </a>
                   <xsl:if test="//viewMode=0">
                    <a style="display:inline;" title="Delete File" href="" onclick="confirmDialog();action('Mapping?action=delete&amp;xpath={$pathSoFar}&amp;id={//output/id}');return false;">
                        <img src="formating/images/delete16.png"/>
                    </a>
                </xsl:if>
               
            </xsl:when>
            <xsl:when test="target_schema/@schema_file">
                <a target="_blank">
                    <xsl:attribute name="href">
                        <xsl:text>FetchBinFile?</xsl:text>
                      
                        <xsl:text>file=</xsl:text>
                        <xsl:value-of select="target_schema/@schema_file"/>
                    </xsl:attribute> 
                     <xsl:text>view </xsl:text>
                </a>
                <xsl:if test="//viewMode=0">
                    <a style="display:inline;" title="Delete File" href="" onclick="confirmDialog();action('Mapping?action=delete&amp;xpath={$pathSoFar}&amp;id={//output/id}');return false;">
                        <img src="formating/images/delete16.png"/>
                    </a>
                </xsl:if>
            </xsl:when>
            <xsl:when test="name()='example_data_source_record'">
                <xsl:choose>
                    <xsl:when test="@xml_link">
                        <a target="_blank">
                            <xsl:attribute name="href">
                                <xsl:text>FetchBinFile?</xsl:text>
                               
                                <xsl:text>file=</xsl:text>
                                <xsl:value-of select="@xml_link"/>
                            </xsl:attribute> 
                            view xml
                        </a>
                         <xsl:if test="//viewMode=0">

                            <a style="display:inline;" title="Delete xml File" href="" onclick="confirmDialog();action('Mapping?action=delete&amp;xpath={concat($pathSoFar,'/@xml_link')}&amp;id={//output/id}');return false;">
                                <img src="formating/images/delete16.png"/>
                            </a>
                        </xsl:if>
                    </xsl:when>
                    <xsl:otherwise>
                        <span data-path="{concat($pathSoFar,'/@xml_link')}" class="fileUpload" ></span>
                        <xsl:if test="//viewMode=0">

                            <a style="display:none;" title="Delete xml File" href="" onclick="confirmDialog();action('Mapping?action=delete&amp;xpath={concat($pathSoFar,'/@xml_link')}&amp;id={//output/id}');return false;">
                                <img src="formating/images/delete16.png"/>
                            </a>
                        </xsl:if>
                    </xsl:otherwise>
                </xsl:choose>
                &#160;&#160;
                <xsl:choose>
                    <xsl:when test="@html_link">
                        <a target="_blank">
                            <xsl:attribute name="href">
                                <xsl:text>FetchBinFile?</xsl:text>
                                <xsl:text>file=</xsl:text>
                                <xsl:value-of select="@html_link"/>
                            </xsl:attribute> 
                            view html
                        </a>
                          <xsl:if test="//viewMode=0">

                            <a style="display:inline;" title="Delete html File" href="" onclick="confirmDialog();action('Mapping?action=delete&amp;xpath={concat($pathSoFar,'/@html_link')}&amp;id={//output/id}');return false;">
                                <img src="formating/images/delete16.png"/>
                            </a>
                        </xsl:if>
                    </xsl:when>
                    <xsl:otherwise>
                        <span data-path="{concat($pathSoFar,'/@html_link')}" class="fileUpload" ></span>
                        <xsl:if test="//viewMode=0">

                            <a style="display:none;" title="Delete html File" href="" onclick="confirmDialog();action('Mapping?action=delete&amp;xpath={concat($pathSoFar,'/@html_link')}&amp;id={//output/id}');return false;">
                                <img src="formating/images/delete16.png"/>
                            </a>
                        </xsl:if>
                    </xsl:otherwise>
                    
                </xsl:choose>
            </xsl:when>
            <xsl:when test="name()='example_data_target_record'">
                <xsl:choose>
                    <xsl:when test="@rdf_link">
                        <a target="_blank">
                            <xsl:attribute name="href">
                                <xsl:text>FetchBinFile?</xsl:text>
                               
                                <xsl:text>file=</xsl:text>
                                <xsl:value-of select="@rdf_link"/>
                            </xsl:attribute> 
                            view rdf
                        </a>
                        <xsl:if test="//viewMode=0">

                            <a style="display:inline;" title="Delete rdf File" href="" onclick="confirmDialog();action('Mapping?action=delete&amp;xpath={concat($pathSoFar,'/@rdf_link')}&amp;id={//output/id}');return false;">
                                <img src="formating/images/delete16.png"/>
                            </a>
                        </xsl:if>
                    </xsl:when>
                    <xsl:otherwise>                        
                        <span data-path="{concat($pathSoFar,'/@rdf_link')}" class="fileUpload" ></span>
                        <xsl:if test="//viewMode=0">

                            <a style="display:none;" title="Delete rdf File" href="" onclick="confirmDialog();action('Mapping?action=delete&amp;xpath={concat($pathSoFar,'/@rdf_link')}&amp;id={//output/id}');return false;">
                                <img src="formating/images/delete16.png"/>
                            </a>
                        </xsl:if>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
              
            
            
            <xsl:otherwise>
                <span data-path="{$pathSoFar}" class="fileUpload" ></span>
                <xsl:if test="//viewMode=0">
                    <a style="display:none;" title="Delete File" href="" onclick="confirmDialog();action('Mapping?action=delete&amp;xpath={$pathSoFar}&amp;id={//output/id}');return false;">
                        <img src="formating/images/delete16.png"/>
                    </a>
                </xsl:if>
            </xsl:otherwise>
        </xsl:choose>
        
    </xsl:template>

</xsl:stylesheet>
