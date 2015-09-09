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

    <xsl:template name="configuration">
        <div class="well" id="config">                     

            <form  role="form" >
                <fieldset>
                    <legend>Components Used</legend>
                    <br/>
                    <div class="form-group ">
                        <div class="row">
                            <div class="col-sm-12">
                                <label class="control-label" for="targetAnalyzer">Target Analyzer: </label>                                                                
                                <div class="btn-group" id="targetAnalyzer" data-toggle="buttons">
                                    <label id="label2">
                                        <xsl:choose>
                                            <xsl:when test="//output/targetAnalyzer='2'">
                                                <xsl:attribute  name="class">btn btn-default btn-sm active</xsl:attribute>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:attribute  name="class">btn btn-default btn-sm</xsl:attribute>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                        <input name="targetAnalyzer" type="radio" class="toggle" value="2" autocomplete="off">
                                            <xsl:if test="//output/targetAnalyzer='2'">
                                                <xsl:attribute  name="checked">checked</xsl:attribute>
                                            </xsl:if>
                                        </input>                                            
                                        <xsl:text> eXist queries</xsl:text>
                                    </label>
                                    <label id="label3">
                                        <xsl:choose>
                                            <xsl:when test="//output/targetAnalyzer='3'">
                                                <xsl:attribute  name="class">btn btn-default btn-sm active</xsl:attribute>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:attribute  name="class">btn btn-default btn-sm</xsl:attribute>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                        <input name="targetAnalyzer" type="radio" class="toggle" value="3" autocomplete="off">
                                            <xsl:if test="//output/targetAnalyzer='3'">
                                                <xsl:attribute  name="checked">checked</xsl:attribute>
                                            </xsl:if>
                                        </input>                                            
                                        <xsl:text> Jena reasoner</xsl:text>
                                    </label>
                                    <label id="label0">
                                        <xsl:choose>
                                            <xsl:when test="//output/targetAnalyzer='0'">
                                                <xsl:attribute  name="class">btn btn-default btn-sm active</xsl:attribute>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:attribute  name="class">btn btn-default btn-sm</xsl:attribute>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                        <input name="targetAnalyzer" type="radio" class="toggle" value="0" autocomplete="off">
                                            <xsl:if test="//output/targetAnalyzer='0'">
                                                <xsl:attribute  name="checked">checked</xsl:attribute>
                                            </xsl:if>
                                        </input>                                            
                                        <xsl:text> None</xsl:text>
                                    </label>
                                    
                                </div>
                                <br/><br/>
<!--                            </div>
                            <div class="col-sm-9">-->
                                <p>
                                    <b>eXist queries</b>: It only works with RDFS or RDF schema files. 
                                    Target analyzer engine is based on Xquery queries performed on RDFS schemas stored in eXist.
                                    Schemas have to be well formed XML files containg certain tags (<i>rdfs:Class, rdf:Property, rdfs:domain</i> etc.).
                                    User chooses valid options from a select box. If there are no target schemas, user simply fills input fields with free text.
                                </p>
                                <p>
                                    <b>Jena reasoner</b>: It works with RDFS, RDF or OWL schema files. 
                                    Target analyzer engine is based on Jena reasoner. User chooses valid options from a select box.
                                    If there are no target schemas, user simply fills input fields with free text. 
                                    <b>(WARNING! The first time user clicks a row to edit, it will take some time to create combos)</b>
                                </p>
                                <p>
                                    <b>None</b>: It works with any type of schema files (or even without schema files at all).
                                    User simply fills input fields with free text.
                                </p>
                            </div>
                        </div>
                         
                        <div class="row" style="border-top: 1px #e5e5e5 solid;">
                            <br/>
                            <div class="col-sm-12">
                                <label class="control-label" for="sourceAnalyzer">Source Analyzer: </label>
                                <div class="btn-group" id="sourceAnalyzer" data-toggle="buttons">
                                    <label id="label5">
                                        <xsl:choose>
                                            <xsl:when test="//output/sourceAnalyzer='on'">
                                                <xsl:attribute  name="class">btn btn-default btn-sm active</xsl:attribute>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:choose>
                                                    <xsl:when test="//output/sourceAnalyzerFiles='***'">
                                                        <xsl:attribute  name="class">btn btn-default btn-sm disabled</xsl:attribute>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:attribute  name="class">btn btn-default btn-sm</xsl:attribute>
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                        <input name="sourceAnalyzer" type="radio" class="toggle" value="on" autocomplete="off">
                                            <xsl:if test="//output/sourceAnalyzer">
                                                <xsl:attribute  name="checked">checked</xsl:attribute>
                                            </xsl:if>
                                        </input>                                            
                                        <xsl:text> On</xsl:text>
                                    </label>
                                    <label id="label6">
                                        <xsl:choose>
                                            <xsl:when test="//output/sourceAnalyzer='off'">
                                                <xsl:attribute  name="class">btn btn-default btn-sm active</xsl:attribute>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:attribute  name="class">btn btn-default btn-sm</xsl:attribute>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                        <input name="sourceAnalyzer" type="radio" class="toggle" value="off" autocomplete="off">
                                            <xsl:if test="//output/sourceAnalyzer">
                                                <xsl:attribute  name="checked">checked</xsl:attribute>
                                            </xsl:if>
                                        </input>                                            
                                        <xsl:text> Off</xsl:text>
                                    </label>
                                   
                                    
                                </div>
                                <br/><br/>
<!--                            </div>
                            <div class="col-sm-9">-->
                                <p>
                                    Once a source schema file or an example xml file is uploaded the source analyzer engine is enabled by default.
                                    User may choose to disable it. When it is enabled, source paths free text input fields are replaced by select boxes.
                                    Select box options are all possible xpaths. 
                                    <br/>
                                    <b>BEWARE! If both source schema and an example xml file are uploaded, schema is the one used to fill select boxes.</b>
                                    <br/>
                                    <b>WARNING! At the moment, source analyzer engine works with xml files and may work with xsd files. No other file format is accepted.</b>

                                    
                                </p>
                                 
                            </div>
                        </div>
                        
                        <div class="row" style="border-top: 1px #e5e5e5 solid;">
                            <br/>
                            <div class="col-sm-12">
                                <label class="control-label" for="targetAnalyzer">Mapping Suggester: </label>
                                                                
<!--                            </div>
                            <div class="col-sm-9">-->
                            <br/><br/>
                                <p>Not available yet!</p>
                                 
                            </div>
                        </div>
                    </div>
                </fieldset>
            </form>
        </div>
    </xsl:template>
</xsl:stylesheet>
