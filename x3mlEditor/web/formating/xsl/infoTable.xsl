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

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:url="http://whatever/java/java.net.URLEncoder">
    <xsl:output method="html" encoding="UTF-8"/>
    <xsl:include href="externalFile.xsl"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template name="infoTable" match="/">
        <table border="0" cellpadding="2" class="infoTable">
            <tr> 
                <td colspan="2" class="titleRibbon">Mapping : <xsl:value-of select="//x3ml/info/title"/></td>
            </tr>
            <tr>
                <th align="center" />
                <th align="center" />
            </tr> 
            
            <tr>
                <td valign="top">
                    <div class="Map_Title" data-help="title">Title
                     </div>
                      
                </td>
                <td>
                    <div class="Map_Title">
                        <span data-editable="" data-path="//x3ml/info/title">
                            <xsl:value-of select="//x3ml/info/title"/>
                        </span>&#160;&#160;
                        <a  style="display:inline;" onclick="window.open(document.URL+'&amp;output=xml','_blank');return false;"  href="">view XML file</a>
                    </div>
                </td>
            </tr>
             <tr>
                <td valign="top">
                    <div class="Map_Title" data-help="source_type">Source type
                     </div>
                      
                </td>
                <td>
                    
                        <span data-editable="" data-path="//x3ml/@source_type">
                            <xsl:value-of select="//x3ml/@source_type"/>
                        </span>
                   
                </td>
            </tr>
            <tr>
                <td valign="top">
                    <div class="Map_Title" data-help="explanation">Explanation of project</div>
                </td>
                <td data-editable="textarea" data-path="//x3ml/info/general_description">
                    <xsl:value-of select="//x3ml/info/general_description"/>
                </td>
            </tr>
            <tr>
                <td/>
                <td>-----------------------------------------------------------------------------------------</td>
            </tr>
            <tr>
                <td valign="bottom">
                    <div class="Map_Title" data-help="source_schema">Source Schema</div>
                </td>
                <td>
                    <span data-editable="" data-path="//x3ml/info/source_info/source_schema">
                        <xsl:value-of select="//x3ml/info/source_info/source_schema"/>
                    </span>&#160;&#160;version&#160;&#160;
                    <span data-editable="" data-path="//x3ml/info/source_info/source_schema/@version">
                        <xsl:value-of select="//x3ml/info/source_info/source_schema/@version"/>
                    </span>&#160;&#160;type&#160;&#160;
                    <span data-editable="" data-path="//x3ml/info/source_info/source_schema/@type">
                        <xsl:value-of select="//x3ml/info/source_info/source_schema/@type"/>
                    </span>
			&#160;&#160;
                        <!--
                    <xsl:choose>
                        <xsl:when test="//x3ml/info/source_info/source_schema/@schema_file!=''">
                            <a>
                                <xsl:attribute name="href">
                                    <xsl:value-of select="//x3ml/info/source_info/source_schema/@schema_file"/>
                                </xsl:attribute> 
                                view
                            </a>
                                    
                        </xsl:when>
                        <xsl:otherwise></xsl:otherwise>
                    </xsl:choose>-->
                    <xsl:for-each select="//x3ml/info/source_info/source_schema">
                     <xsl:call-template name="externalFile" >       
                            <xsl:with-param name="pathSoFar" select="'//x3ml/info/source_info/source_schema/@schema_file'"/>
                        </xsl:call-template>
                    </xsl:for-each>
                           
                </td>
            </tr>
            <tr>
                <td valign="top">
                    <div class="Map_Title" data-help="source_collection">Source Collection</div>
                </td>
                <td data-editable="" data-path="//x3ml/info/source_info/source_collection">
                    <xsl:value-of select="//x3ml/info/source_info/source_collection"/>
                </td>
            </tr>
            <xsl:for-each select="//x3ml/info/target_info">
                <xsl:variable name="pos" select="position()"/>
                <tr>
                    <td valign="bottom">
                        <div class="Map_Title" data-help="target_schema">Target Schema</div>
                    </td>
                    <td >
                       
                        <span data-editable="" data-path="{concat('//x3ml/info/target_info[',$pos,']','/target_schema')}">
                            <xsl:value-of select="target_schema"/>
                        </span>
                        &#160;&#160;version:
                        <span  data-editable="" data-path="{concat('//x3ml/info/target_info[',$pos,']','/target_schema/@version')}">
                            <xsl:value-of select="target_schema/@version"/>
                        </span>&#160;&#160;type:
                        <span data-editable="" data-path="{concat('//x3ml/info/target_info[',$pos,']','/target_schema/@type')}">
                            <xsl:value-of select="target_schema/@type"/>
                        </span>
			&#160;&#160;
                                                
                        <xsl:call-template name="externalFile">       
                            <xsl:with-param name="pathSoFar" select="concat('//x3ml/info/target_info[',$pos,']','/target_schema/@schema_file')"/>
                        </xsl:call-template>
                        <xsl:if test="count(../target_info)>1">
                            <a  style="float:right;" title="Delete Target" href="" onclick="confirmDialog();action('Mapping?action=delete&amp;xpath={concat('//x3ml/info/target_info[',$pos,']')}&amp;id={//output/id}');action('Mapping?action=delete&amp;xpath={concat('//x3ml/namespaces/namespace[',$pos+2,']')}&amp;id={//output/id}');return false;">
                                <img src="formating/images/delete16.png"/>
                            </a>
                        </xsl:if>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        <div class="Map_Title" data-help="target_collection">Target Collection</div>
                    </td>
                    <td data-editable="" data-path="{concat('//x3ml/info/target_info[',$pos,']','/target_collection')}">
                        <xsl:value-of select="target_collection"/>
                    </td>
                </tr>
                <tr>
                    <xsl:variable name="namespacePos" select="$pos+2"/>
                        
                    <td valign="top">
                        <div class="Map_Title" data-help="namespaces">Namespaces</div>
                    </td>
                    <td>
                        prefix:
                        <span  data-editable="" data-path="{concat('//x3ml/namespaces/namespace[',$namespacePos,']','/@prefix')}">
                        <xsl:value-of select="//x3ml/namespaces/namespace[position()=$namespacePos]/@prefix"/>
                        </span>&#160;&#160;uri:
                        <span data-editable="" data-path="{concat('//x3ml/namespaces/namespace[',$namespacePos,']','/@uri')}">
                        <xsl:value-of select="//x3ml/namespaces/namespace[position()=$namespacePos]/@uri"/>
                        </span>                       


                    </td>
                </tr>
            </xsl:for-each>
            <tr><!---->
                <td colspan="2" align="middle">
                    <a class="inline" title="Add Target" href="" onclick="action('Mapping?action=addAfter&amp;xpath={concat('//x3ml/info/target_info[last()]')}&amp;id={//output/id}');action('Mapping?action=add&amp;xpath=//x3ml/namespaces/namespace&amp;id={//output/id}');return false;">
                        <img  src="formating/images/plus16.png"/>Target</a>
                </td>
            </tr>
            <tr>
                <td/>
                <td>-----------------------------------------------------------------------------------------</td>
            </tr>
            <tr>
                <td valign="top">
                    <div class="Map_Title" data-help="created_by">Mapping created by</div>
                </td>
                <td data-editable="" data-path="//x3ml/info/mapping_info/mapping_created_by_org">
                    <xsl:value-of select="//x3ml/info/mapping_info/mapping_created_by_org"/>
                </td>
            </tr>
            <tr>
                <td valign="top">
                    <div class="Map_Title" data-help="contact_person">Contact Person(s)</div>
                </td>
                <td data-editable="" data-path="//x3ml/info/mapping_info/mapping_created_by_person">
                    <xsl:value-of select="//x3ml/info/mapping_info/mapping_created_by_person"/>
                </td>
            </tr>
            <tr>
                <td valign="top">
                    <div class="Map_Title" data-help="collaboration">In collaboration with</div>
                </td>
                <td data-editable="" data-path="//x3ml/info/mapping_info/in_collaboration_with">
                    <xsl:value-of select="//x3ml/info/mapping_info/in_collaboration_with"/>
                </td>
            </tr>
            <tr>
                <td/>
                <td>-----------------------------------------------------------------------------------------</td>
            </tr>
            <tr>
                <td valign="top">
                    <div class="Map_Title" data-help="sample_data">Sample data: </div>
                </td>
                <td/>
            </tr>
            <tr>
                <td valign="top">
                    <div class="Map_Title" data-help="provided_by">&#160;&#160;&#160;provided by</div>
                </td>
                <td data-editable="" data-path="//x3ml/info/example_data_info/example_data_from">
                    <xsl:value-of select="//x3ml/info/example_data_info/example_data_from"/>
                </td>
            </tr>
            <tr>
                <td valign="top">
                    <div class="Map_Title" data-help="contact_person">&#160;&#160;&#160;contact person</div>
                </td>
                <td data-editable="" data-path="//x3ml/info/example_data_info/example_data_contact_person">
                    <xsl:value-of select="//x3ml/info/example_data_info/example_data_contact_person"/>
                </td>
            </tr>
            <tr>
                <td valign="top">
                    <div class="Map_Title" data-help="source_record">&#160;&#160;&#160;source record</div>
                </td>
                <td>
                    <span data-editable="" data-path="//x3ml/info/example_data_info/example_data_source_record">
                        <xsl:value-of select="//x3ml/info/example_data_info/example_data_source_record"/>
                    </span>
			&#160;&#160;
                    <!--xsl:choose>
                        <xsl:when test="//x3ml/info/example_data_info/example_data_source_record/@xml_link!=''">
                            <a>
                                <xsl:attribute name="href">
                                    <xsl:value-of select="//x3ml/info/example_data_info/example_data_source_record/@xml_link"/>
                                </xsl:attribute> 
                                view xml
                            </a>
                                    
                        </xsl:when>
                        <xsl:otherwise></xsl:otherwise>
                    </xsl:choose>-->
                    <xsl:for-each select="//x3ml/info/example_data_info/example_data_source_record">
                        <xsl:call-template name="externalFile">       
                            <xsl:with-param name="pathSoFar" select="'//x3ml/info/example_data_info/example_data_source_record'"/>
                        </xsl:call-template>
              
                   
                           
			
                        <!--xsl:call-template name="externalFile">       
                            <xsl:with-param name="pathSoFar" select="'//x3ml/info/example_data_info/example_data_source_record/@html_link'"/>
                        </xsl:call-template-->
                         </xsl:for-each>
                         
                         <!--
                    <xsl:choose>
                        <xsl:when test="//x3ml/info/example_data_info/example_data_source_record/@html_link!=''">
                            <a>
                                <xsl:attribute name="href">
                                    <xsl:value-of select="//x3ml/info/example_data_info/example_data_source_record/@html_link"/>
                                </xsl:attribute> 
                                view html
                            </a>
                                    
                        </xsl:when>
                        <xsl:otherwise></xsl:otherwise>
                    </xsl:choose>
                    -->
                           
                </td>
            </tr>
            <tr>
                <td valign="top">
                    <div class="Map_Title" data-help="target_record">&#160;&#160;&#160;target record</div>
                </td>
                <td>
                    
                    <span data-editable="" data-path="//x3ml/info/example_data_info/example_data_target_record">
                        <xsl:value-of select="//x3ml/info/example_data_info/example_data_target_record"/>
                    </span>
                    &#160;&#160;
                    <xsl:for-each select="//x3ml/info/example_data_info/example_data_target_record">
                        <xsl:call-template name="externalFile">       
                            <xsl:with-param name="pathSoFar" select="'//x3ml/info/example_data_info/example_data_target_record'"/>
                        </xsl:call-template>
                    </xsl:for-each>
              
                    
                    
                </td>
            </tr>
        </table>
    </xsl:template>

</xsl:stylesheet>
