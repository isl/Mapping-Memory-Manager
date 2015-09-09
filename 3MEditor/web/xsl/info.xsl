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
    <xsl:include href="externalFileLink.xsl"/>
    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template match="info" name="info">
        <div class="well" id="info" >                     
            <form role="form" class="infoForm">
                <fieldset >
                    <legend >General</legend>
                    <div class="form-group">
                        <div class="row">
                            <div class="col-sm-8">
                                <label class="control-label">Title</label>                                   
                                <p class="form-control-static">
                                    <xsl:value-of select="title"/>
                                    <a  onclick="window.open(document.URL+'&amp;output=xml','_blank');return false;"  href=""> view XML file</a>

                                </p>
                            </div>
                            <div class="col-sm-2"> 
                                <label class=" control-label">Source Type</label>
                                <p class="form-control-static">
                                    <xsl:value-of select="//x3ml/@source_type"/>
                                </p>
                            </div>
                            <div class="col-sm-2">          
                                <label class="control-label">Version</label>
                                <p class="form-control-static">
                                    <xsl:value-of select="//x3ml/@version"/>
                                </p>
                            </div>
                        </div>
                    </div>  
                    <div class="form-group">    
                        <div class="row">  
                            <div class="col-sm-12">
                                <label class="control-label">Explanation of project</label>
                                <p class="form-control-static">
                                    <xsl:value-of select="general_description"/>
                                </p>
                            </div>
                        </div>
                    
                    </div>
                </fieldset>       
                <xsl:apply-templates select="source_info"/>     
                <fieldset>
                    <legend>Target</legend>
                    <xsl:apply-templates select="target_info"/>     
                </fieldset>       
                <xsl:apply-templates select="mapping_info"/>      
                <xsl:apply-templates select="example_data_info"/>    
            </form>
        </div>
    </xsl:template>
    
    <xsl:template match="source_info" name="source_info">
        <fieldset>
            <legend>Source</legend>
            <div class="form-group">
                <div class="row">
                    <div class="col-sm-4">
                        <label class="control-label">Schema</label>                                   
                        <p class="form-control-static">
                            <xsl:value-of select="source_schema"/>   
                            <!--                            <a href="#"> view</a>                         -->
                            &#160; 
                            <xsl:for-each select="source_schema">
                                <xsl:call-template name="externalFileLink" >       

                                </xsl:call-template>
                            </xsl:for-each>
                        </p>
                    </div> 
                    <div class="col-sm-2">          
                        <label class="control-label">Type</label>
                        <p class="form-control-static">
                            <xsl:value-of select="source_schema/@type"/>
                        </p>
                    </div>
                    <div class="col-sm-2"> 
                        <label class=" control-label">Version</label>
                        <p class="form-control-static">
                            <xsl:value-of select="source_schema/@version"/>
                        </p>
                    </div>
                    <div class="col-sm-4"> 
                        <label class="control-label">Collection</label>
                        <p class="form-control-static">
                            <xsl:value-of select="source_collection"/>
                        </p>
                    </div>
                   
                </div>               
            </div>
           
        </fieldset>         
    </xsl:template>
    
    <xsl:template match="target_info" name="target_info">
        <xsl:variable name="pos">
            <xsl:value-of select="position()"/>
        </xsl:variable>
        <xsl:variable name="namespacePos" select="$pos+2"/>
     
        <div class="form-group">
            <div class="row">
                <div class="col-sm-4">
                    <label class="control-label">Schema</label>                                   
                    <p class="form-control-static">
                        <xsl:value-of select="target_schema"/>   
                        <!--<a href="#"> view</a>--> 
                        &#160;                        
                        <xsl:call-template name="externalFileLink">       

                        </xsl:call-template>
                    </p>
                </div> 
                <div class="col-sm-2">          
                    <label class="control-label">Type</label>
                    <p class="form-control-static">
                        <xsl:value-of select="target_schema/@type"/>
                    </p>
                </div>
                <div class="col-sm-2"> 
                    <label class=" control-label">Version</label>
                    <p class="form-control-static">
                        <xsl:value-of select="target_schema/@version"/>
                    </p>
                </div>
                <div class="col-sm-4">
                    <label class="control-label">Collection</label>
                    <p class="form-control-static">
                        <xsl:value-of select="target_collection"/>
                    </p>
                </div> 
            </div>               
        </div>
        <div class="form-group">
            
            <div class="row" >
                <xsl:if test="position()!=last()">
                    <xsl:attribute name="style">
                        <xsl:text>border-bottom: 1px #e5e5e5 solid;"</xsl:text>
                    </xsl:attribute>
                </xsl:if>
                <div class="col-sm-6">
                    <label class="control-label">Namespace prefix</label>                                   
                    <p class="form-control-static">
                        <xsl:value-of select="//namespace[position()=$namespacePos]/@prefix"/>   
                                     
                    </p>
                </div> 
                <div class="col-sm-6">          
                    <label class="control-label">Namespace uri</label>
                    <p class="form-control-static">
                        <xsl:value-of select="//namespace[position()=$namespacePos]/@uri"/>
                    </p>
                </div>
                
            </div>               
        </div>
       
           
    </xsl:template>
    
    <xsl:template match="mapping_info" name="mapping_info">
        <fieldset>
            <legend>Mapping</legend>
            <div class="form-group">
                <div class="row">
                    <div class="col-sm-4">
                        <label class="control-label">Created by (Organization)</label>                                   
                        <p class="form-control-static">
                            <xsl:value-of select="mapping_created_by_org"/>   
                                                
                        </p>
                    </div> 
                    <div class="col-sm-4">          
                        <label class="control-label">Contact Person(s)</label>
                        <p class="form-control-static">
                            <xsl:value-of select="mapping_created_by_person"/>
                        </p>
                    </div>
                    <div class="col-sm-4"> 
                        <label class=" control-label">In collaboration with</label>
                        <p class="form-control-static">
                            <xsl:value-of select="in_collaboration_with"/>
                        </p>
                    </div>
                   
                   
                </div>               
            </div>
           
        </fieldset>         
    </xsl:template>
    <xsl:template match="example_data_info" name="example_data_info">
        <fieldset>
            <legend>Sample data</legend>
            <div class="form-group">
                <div class="row">
                    <div class="col-sm-3">
                        <label class="control-label">Provided by</label>                                   
                        <p class="form-control-static">
                            <xsl:value-of select="example_data_from"/>   
                                                
                        </p>
                    </div> 
                    <div class="col-sm-3">          
                        <label class="control-label">Contact Person(s)</label>
                        <p class="form-control-static">
                            <xsl:value-of select="example_data_contact_person"/>
                        </p>
                    </div>
                    <div class="col-sm-3"> 
                        <label class=" control-label">Source record</label>
                        <p class="form-control-static">
                            <xsl:value-of select="example_data_source_record"/>
                            <br/>
                            <!--                            <a href="#"> view xml</a> -->
                            <xsl:for-each select="example_data_source_record/@*">
                                                     
                                                        
							<xsl:call-template name="externalFileLink">
							</xsl:call-template>
                                                      
						<br/>
                                                </xsl:for-each>
                            
                            <!--<a href="#"> view html</a>--> 
                            <!--                            <xsl:for-each select="example_data_source_record">
                                <xsl:call-template name="externalFile" >       
                                    <xsl:with-param name="pathSoFar" select="'//x3ml/info/example_data_info/example_data_source_record'"/>
                                </xsl:call-template>
                            </xsl:for-each>-->
                        </p>
                    </div>
                    <div class="col-sm-3"> 
                        <label class=" control-label">Target record</label>
                        <p class="form-control-static">
                            <xsl:value-of select="example_data_target_record"/>
                            <!--                            <a href="#"> view rdf</a> -->
                            <br/>
                            <xsl:for-each select="example_data_target_record">
                        <xsl:call-template name="externalFileLink">       

                                </xsl:call-template>
                            </xsl:for-each>
                        </p>
                    </div>
                   
                </div>               
            </div>
           
        </fieldset>         
    </xsl:template>
</xsl:stylesheet>
