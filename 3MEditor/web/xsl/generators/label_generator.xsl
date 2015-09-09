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
    <xsl:import href="arg.xsl"/>
   
    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template name="label_generator" match="label_generator">
        <xsl:param name="pathSoFar"/>

        <xsl:variable name="container">
            <xsl:choose>
                <xsl:when test="$pathSoFar!=''">
                    <xsl:value-of select="$pathSoFar"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="@xpath"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
                   
        <div class="label_generator" id="{$container}" data-xpath="{$container}">
            <button title="Delete Label Generator" type="button" class="close" id="{concat('delete***',$container)}" >
                <span class="fa fa-times smallerIcon" style="color:black;"></span>

            </button>
            <div class="row">
                <div class="col-sm-12">

                    <label class="control-label" for="{concat($container,'/@name')}">Label Generator Name</label>
                    <input id="{concat($container,'/@name')}" type="text" class="form-control input-sm" placeholder="Fill in value" title="Label Generator Name" data-xpath="{concat($container,'/@name')}">
                        <xsl:attribute name="value">
                            <xsl:value-of select="@name"></xsl:value-of>
                        </xsl:attribute>
                    </input>
                </div>
            </div>
                      
            <div class="args" id="{concat($container,'/args')}">
                <xsl:for-each select="arg">
                      
                    <xsl:call-template name="arg">
                        <xsl:with-param name="pathSoFar" select="concat($container,'/arg[',position(),']')"/>
                    </xsl:call-template>
                           
                </xsl:for-each>
                    
            </div>
            <button data-xpath="{concat('add***',$container,'/arg')}" id="{concat('add***',$container,'/arg')}" title="Add Argument" type="button" class="btn btn-link btn-sm  add white">
                Add Argument</button>              
               
               
                
              
        </div>
                    
           
                      
        

    </xsl:template>

</xsl:stylesheet>
