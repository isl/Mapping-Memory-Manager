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
    <xsl:include href="../externalFileLink.xsl"></xsl:include>
    <xsl:output method="html"></xsl:output>
    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template match="info" name="info">
        <div class="well">
            <form role="form" class="infoForm">
                <fieldset>
                    <legend>General</legend>
                    <div class="form-group">
                        <div class="row">
                            <div class="col-sm-8 required">
                                <label class="control-label" for="generalTitle">Title</label>
                                <input id="generalTitle" type="text" class="form-control required input-sm" placeholder="Fill in value" data-xpath="//x3ml/info/title" required="required">
                                    <xsl:attribute name="value">
                                        <xsl:value-of select="title"></xsl:value-of>
                                    </xsl:attribute>
                                </input>
                                <a onclick="window.open(document.URL+'&amp;output=xml','_blank');return false;" href=""> view XML file</a>
                            </div>
                            <div class="col-sm-2">
                                <label class=" control-label" for="sourceType">Source Type</label>
                                <input id="sourceType" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="//x3ml/@source_type">
                                    <xsl:attribute name="value">
                                        <xsl:value-of select="//x3ml/@source_type"></xsl:value-of>
                                    </xsl:attribute>
                                </input>
                            </div>
                            <div class="col-sm-2">
                                <label class="control-label" for="version">Version</label>
                                <input id="version" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="//x3ml/@version">
                                    <xsl:attribute name="value">
                                        <xsl:value-of select="//x3ml/@version"></xsl:value-of>
                                    </xsl:attribute>
                                </input>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="row">
                            <div class="col-sm-12">
                                <label class="control-label" for="generalDescription">Explanation of project</label>
                                <textarea id="generalDescription" placeholder="Fill in value" class="form-control input-sm" rows="2" data-xpath="//x3ml/info/general_description">
                                    <xsl:value-of select="general_description"></xsl:value-of>
                                </textarea>
                            </div>
                        </div>
                    </div>
                </fieldset>
                <xsl:apply-templates select="source_info"></xsl:apply-templates>
                <fieldset>
                    <legend>Target</legend>
                    <xsl:apply-templates select="target_info"></xsl:apply-templates>
                    <div class="row">
                        <div class="col-sm-2 col-sm-offset-5">
                            <br></br>
                            <button id="addTarget" type="button" class="btn btn-default btn-block  add btn-sm">
                                <span class="glyphicon glyphicon-plus"></span>&#160;Target
                            </button>
                        </div>
                    </div>
                </fieldset>
                <xsl:apply-templates select="mapping_info"></xsl:apply-templates>
                <xsl:apply-templates select="example_data_info"></xsl:apply-templates>
            </form>
        </div>
    </xsl:template>
    <xsl:template match="source_info" name="source_info">
        <fieldset>
            <legend>Source</legend>
            <div class="form-group">
                <div class="row">
                    <div class="col-sm-4">
                        <label class="control-label" for="sourceSchema">Schema</label>
                        <input id="sourceSchema" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="//x3ml/info/source_info/source_schema">
                            <xsl:attribute name="value">
                                <xsl:value-of select="source_schema"></xsl:value-of>
                            </xsl:attribute>
                        </input>
                        <xsl:for-each select="source_schema">
                            <div>
                                <xsl:call-template name="externalFileLink">
                                </xsl:call-template>
                                <xsl:if test="@schema_file">
                                    <button class="btn btn-default btn-link btn-sm deleteFile" type="button" title="Delete source schema" id="{concat('delete***//x3ml/info/source_info/source_schema/@schema_file')}">
                                        <span class="glyphicon glyphicon-remove"></span>
                                    </button>
                                </xsl:if>
                            </div>
                            <span data-xpath="{concat('//x3ml/info/source_info/source_schema/@schema_file')}" class="fileUpload">
                                <xsl:if test="@schema_file">
                                    <xsl:attribute name="style">
                                        <xsl:text>display:none;</xsl:text>
                                    </xsl:attribute>
                                </xsl:if>
                            </span>
                        </xsl:for-each>
                    </div>
                    <div class="col-sm-2">
                        <label class="control-label" for="sourceSchemaType">Type</label>
                        <input id="sourceSchemaType" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="//x3ml/info/source_info/source_schema/@type">
                            <xsl:attribute name="value">
                                <xsl:value-of select="source_schema/@type"></xsl:value-of>
                            </xsl:attribute>
                        </input>
                    </div>
                    <div class="col-sm-2">
                        <label class=" control-label" for="sourceSchemaVersion">Version</label>
                        <input id="sourceSchemaVersion" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="//x3ml/info/source_info/source_schema/@version">
                            <xsl:attribute name="value">
                                <xsl:value-of select="source_schema/@version"></xsl:value-of>
                            </xsl:attribute>
                        </input>
                    </div>
                    <div class="col-sm-4">
                        <label class="control-label" for="sourceCollection">Collection</label>
                        <input type="text" id="sourceCollection" class="form-control input-sm" placeholder="Fill in value" data-xpath="//x3ml/info/source_info/source_collection">
                            <xsl:attribute name="value">
                                <xsl:value-of select="source_collection"></xsl:value-of>
                            </xsl:attribute>
                        </input>
                    </div>
                </div>
            </div>
        </fieldset>
    </xsl:template>
    <xsl:template match="target_info" name="target_info">
        <xsl:variable name="pos">
            <xsl:choose>
                <xsl:when test="@pos">
                    <xsl:value-of select="@pos"></xsl:value-of>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="position()"></xsl:value-of>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="namespacePos" select="$pos+2"></xsl:variable>
        <xsl:variable name="pathSoFar">
            <xsl:value-of select="concat('//x3ml/info/target_info[',$pos,']')"></xsl:value-of>
        </xsl:variable>
        <div id="{$pathSoFar}" class="target_info" data-xpath="{$pathSoFar}">
            <div class="form-group">
                <div class="row">
                    <xsl:if test="@pos">
                        <xsl:attribute name="style">
                            <xsl:text>border-top: 1px #e5e5e5 solid;</xsl:text>
                        </xsl:attribute>
                    </xsl:if>
                    <div class="col-sm-4">
                        <label class="control-label" for="{concat($pathSoFar,'/target_schema')}">Schema</label>
						
                        <input title="Target schema" id="{concat($pathSoFar,'/target_schema')}" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="{concat($pathSoFar,'/target_schema')}">
                            <xsl:attribute name="value">
                                <xsl:value-of select="target_schema"></xsl:value-of>
                            </xsl:attribute>
                        </input>
                        <div>
                            <xsl:call-template name="externalFileLink">
                            </xsl:call-template>
                            <xsl:if test="target_schema/@schema_file">
                                <button class="btn btn-default btn-link btn-sm deleteFile" type="button" title="Delete target schema" id="{concat('delete***//x3ml/info/target_info[',$pos,']','/target_schema/@schema_file')}">
                                    <span class="glyphicon glyphicon-remove"></span>
                                </button>
                            </xsl:if>
                        </div>
                        <span data-xpath="{concat('//x3ml/info/target_info[',$pos,']','/target_schema/@schema_file')}" class="fileUpload">
                            <xsl:if test="target_schema/@schema_file">
                                <xsl:attribute name="style">
                                    <xsl:text>display:none;</xsl:text>
                                </xsl:attribute>
                            </xsl:if>
                        </span>
                    </div>
                    <div class="col-sm-2">
                        <label class="control-label" for="{concat($pathSoFar,'/target_schema/@type')}">Type</label>
                        <input id="{concat($pathSoFar,'/target_schema/@type')}" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="{concat($pathSoFar,'/target_schema/@type')}">
                            <xsl:attribute name="value">
                                <xsl:value-of select="target_schema/@type"></xsl:value-of>
                            </xsl:attribute>
                        </input>
                    </div>
                    <div class="col-sm-2">
                        <label class=" control-label" for="{concat($pathSoFar,'/target_schema/@version')}">Version</label>
                        <input id="{concat($pathSoFar,'/target_schema/@version')}" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="{concat($pathSoFar,'/target_schema/@version')}">
                            <xsl:attribute name="value">
                                <xsl:value-of select="target_schema/@version"></xsl:value-of>
                            </xsl:attribute>
                        </input>
                    </div>
                    <div class="col-sm-3">
                        <label class="control-label" for="{concat($pathSoFar,'/target_collection')}">Collection</label>
                        <input id="{concat($pathSoFar,'/target_collection')}" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="{concat($pathSoFar,'/target_collection')}">
                            <xsl:attribute name="value">
                                <xsl:value-of select="target_collection"></xsl:value-of>
                            </xsl:attribute>
                        </input>
                    </div>
					
                                        
                    <button title="Delete Target" type="button"  class="close targetInfoDeleteButton" id="{concat('delete***',$pathSoFar)}">
                        <span class="fa fa-times smallerIcon" ></span>
                        <span class="sr-only">Close</span>
                    </button>
                                        
                </div>
            </div>
            <div class="form-group">
                <div class="row">
                    <xsl:choose>
                        <xsl:when test="position()!=last()">
                            <xsl:attribute name="style">
                                <xsl:text>border-bottom: 1px #e5e5e5 solid;padding-bottom:10px;</xsl:text>
                            </xsl:attribute>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:attribute name="style">
                                <xsl:text>padding-bottom:10px;</xsl:text>
                            </xsl:attribute>
                        </xsl:otherwise>
                    </xsl:choose>
                    <div class="col-sm-6">
                        <label class="control-label" for="{concat('//namespace[',$namespacePos,']/@prefix')}">Namespace prefix</label>
                        <input id="{concat('//namespace[',$namespacePos,']/@prefix')}" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="{concat('//namespace[',$namespacePos,']/@prefix')}">
                            <xsl:attribute name="value">
                                <xsl:value-of select="//namespace[position()=$namespacePos]/@prefix"></xsl:value-of>
                            </xsl:attribute>
                        </input>
                    </div>
                    <div class="col-sm-6">
                        <label class="control-label" for="{concat('//namespace[',$namespacePos,']/@uri')}">Namespace uri</label>
                        <input id="{concat('//namespace[',$namespacePos,']/@uri')}" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="{concat('//namespace[',$namespacePos,']/@uri')}">
                            <xsl:attribute name="value">
                                <xsl:value-of select="//namespace[position()=$namespacePos]/@uri"></xsl:value-of>
                            </xsl:attribute>
                        </input>
                    </div>
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
                        <label class="control-label" for="mappingInfoCreated">Created by (Organization)</label>
                        <input id="mappingInfoCreated" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="//info/mapping_info/mapping_created_by_org">
                            <xsl:attribute name="value">
                                <xsl:value-of select="mapping_created_by_org"></xsl:value-of>
                            </xsl:attribute>
                        </input>
                    </div>
                    <div class="col-sm-4">
                        <label class="control-label" for="mappingInfoPerson">Contact Person(s)</label>
                        <input id="mappingInfoPerson" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="//info/mapping_info/mapping_created_by_person">
                            <xsl:attribute name="value">
                                <xsl:value-of select="mapping_created_by_person"></xsl:value-of>
                            </xsl:attribute>
                        </input>
                    </div>
                    <div class="col-sm-4">
                        <label class=" control-label" for="mappingInfoColab">In collaboration with</label>
                        <input id="mappingInfoColab" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="//info/mapping_info/in_collaboration_with">
                            <xsl:attribute name="value">
                                <xsl:value-of select="in_collaboration_with"></xsl:value-of>
                            </xsl:attribute>
                        </input>
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
                        <label class="control-label" for="exampleDataFrom">Provided by</label>
                        <input id="exampleDataFrom" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="//info/example_data_info/example_data_from">
                            <xsl:attribute name="value">
                                <xsl:value-of select="example_data_from"></xsl:value-of>
                            </xsl:attribute>
                        </input>
                    </div>
                    <div class="col-sm-3">
                        <label class="control-label" for="exampleDataPerson">Contact Person(s)</label>
                        <input id="exampleDataPerson" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="//info/example_data_info/example_data_contact_person">
                            <xsl:attribute name="value">
                                <xsl:value-of select="example_data_contact_person"></xsl:value-of>
                            </xsl:attribute>
                        </input>
                    </div>
                    <div class="col-sm-3">
                        <label class=" control-label" for="exampleDataSourceRecord">Source record</label>
                        <input id="exampleDataSourceRecord" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="//info/example_data_info/example_data_source_record">
                            <xsl:attribute name="value">
                                <xsl:value-of select="example_data_source_record"></xsl:value-of>
                            </xsl:attribute>
                        </input>
                        <xsl:for-each select="example_data_source_record/@*">
                            <div class="{name()}">
                                                        
                                <xsl:call-template name="externalFileLink">
                                </xsl:call-template>
                                <xsl:choose>
                                    <xsl:when test="name()='html_link'">
                                        <button class="btn btn-default btn-link btn-sm deleteFile" type="button" title="Delete html file" id="{concat('delete***//x3ml/info/example_data_info/example_data_source_record/@html_link')}">
                                            <span class="glyphicon glyphicon-remove"></span>
                                        </button>
                                    </xsl:when>
                                    <xsl:when test="name()='xml_link'">
                                        <button class="btn btn-default btn-link btn-sm deleteFile" type="button" title="Delete xml file" id="{concat('delete***//x3ml/info/example_data_info/example_data_source_record/@xml_link')}">
                                            <span class="glyphicon glyphicon-remove"></span>
                                        </button>
                                    </xsl:when>
                                </xsl:choose>
                            </div>
                        </xsl:for-each>
                                                
                        <span data-xpath="//x3ml/info/example_data_info/example_data_source_record/@html_link" class="fileUpload html_link">
                            <xsl:if test="example_data_source_record/@html_link">
                                <xsl:attribute name="style">
                                    <xsl:text>display:none;</xsl:text>
                                </xsl:attribute>
                            </xsl:if>
                        </span>
                        <span data-xpath="//x3ml/info/example_data_info/example_data_source_record/@xml_link" class="fileUpload xml_link">
                            <xsl:if test="example_data_source_record/@xml_link">
                                <xsl:attribute name="style">
                                    <xsl:text>display:none;</xsl:text>
                                </xsl:attribute>
                            </xsl:if>
                        </span>

						
                    </div>
                    <div class="col-sm-3">
                        <label class=" control-label" for="exampleDataTargetRecord">Target record</label>
                        <input id="exampleDataTargetRecord" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="//info/example_data_info/example_data_target_record">
                            <xsl:attribute name="value">
                                <xsl:value-of select="example_data_target_record"></xsl:value-of>
                            </xsl:attribute>
                        </input>
                        <xsl:for-each select="example_data_target_record">
                                                    
                            <div>
                                <xsl:call-template name="externalFileLink">
                                </xsl:call-template>
                                <xsl:if test="@rdf_link">
                                    <button class="btn btn-default btn-link btn-sm deleteFile" type="button" title="Delete rdf link" id="{concat('delete***//x3ml/info/example_data_info/example_data_target_record/@rdf_link')}">
                                        <span class="glyphicon glyphicon-remove"></span>
                                    </button>
                                </xsl:if>
                            </div>
                            <span data-xpath="{concat('//x3ml/info/example_data_info/example_data_target_record/@rdf_link')}" class="fileUpload">
                                <xsl:if test="@rdf_link">
                                    <xsl:attribute name="style">
                                        <xsl:text>display:none;</xsl:text>
                                    </xsl:attribute>
                                </xsl:if>
                            </span>
                                                    
                                                    
                        </xsl:for-each>
                    </div>
                </div>
            </div>
        </fieldset>
	
    </xsl:template>
</xsl:stylesheet>
