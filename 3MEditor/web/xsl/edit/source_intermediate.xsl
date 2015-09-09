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
    <xsl:import href="../various.xsl"/>



    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    
    
    <xsl:template match="source_intermediate" name="source_intermediate">
        <xsl:param name="entPos"/>
       
        <xsl:variable name="container">           
            <xsl:value-of select="@container"/>                
        </xsl:variable>
        <xsl:variable name="pathSoFar">
            <xsl:value-of select="@xpath"/>
                   
        </xsl:variable>
        <div class="source_intermediate" id="{$pathSoFar}" data-xpath="{$pathSoFar}">
            
<!--            <button title="Delete Intermediate" type="button"  class="close" id="{concat('delete***',$pathSoFar)}">
                <span aria-hidden="true">x</span>
                <span class="sr-only">Close</span>
            </button>-->
             <button title="Delete Intermediate" type="button"  class="close btn btn-sm"  id="{concat('delete***',$pathSoFar)}">
                        <span class="fa fa-times smallerIcon" ></span>
                        <span class="sr-only">Close</span>
                    </button>
                    
            
                
            <xsl:for-each select="node">
                <xsl:variable name="nodePos" select="position()"/>               
                <xsl:apply-templates select=".">
                    <xsl:with-param name="nodePos" select="$nodePos"/>
                </xsl:apply-templates>
                <xsl:for-each select="following-sibling::relation[1]">
                    <xsl:call-template name="relation">
                        <xsl:with-param name="pos" select="$nodePos+1"/>
                    </xsl:call-template>
                </xsl:for-each>
               
            </xsl:for-each>
        </div>
        <!--</fieldset>-->
        
    </xsl:template>
   
    <xsl:template match="node">
        <xsl:param name="nodePos"/>
        
        <xsl:variable name="pathSoFar">
            <xsl:choose>
                <xsl:when test="//path/@xpath">
                    <xsl:value-of select="concat(//path/@xpath,'/source_relation','/node[',$nodePos,']')"/>
                </xsl:when>
                <xsl:otherwise>        
                    
                    <xsl:call-template name="string-replace-all">
                        <xsl:with-param name="text" select="//source_intermediate/@xpath" />
                        <xsl:with-param name="replace" select="'/intermediate['" />
                        <xsl:with-param name="by" select="'/node['" />
                    </xsl:call-template>           
                </xsl:otherwise>
            </xsl:choose>
                   
        </xsl:variable>
        
        <div class="row "  style="margin-left:0px;">
            <div class="col-xs-1 icon" >
                <label class="control-label" for="">&#160;</label>
                <img src="images/intermediate.png"/>
            </div>
            
            <div class="col-xs-10">
                <div class="form-group">

                    <label class="control-label topPadded" for="{concat('pathSourceNode',$nodePos)}">Source Node</label>

                    <xsl:choose>
                                   
                        <xsl:when test="//*/@sourceAnalyzer='off'">                                 
                            <input title="Source Node" id="{concat('pathSourceNode',$nodePos)}" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="{$pathSoFar}">
                                <xsl:attribute name="value">
                                    <xsl:value-of select="."/>
                                </xsl:attribute>
                            </input>
                        </xsl:when>
                        <xsl:otherwise>
                            <input style="width:100%" title="Source Node" type="hidden" class="select2 input-sm" data-id="{.}" id="{$pathSoFar}" data-xpath="{$pathSoFar}">
                                <xsl:attribute name="value">
                                    <xsl:value-of select="."></xsl:value-of>
                                </xsl:attribute>
                                <img class="loader" src="js/select2-3.5.1/select2-spinner.gif"></img>
                            </input>
                        </xsl:otherwise>
                    </xsl:choose>
                </div>
            </div>
        </div>
    </xsl:template>
    <xsl:template match="relation" name="relation">
        <xsl:param name="pos"/>
        
        <xsl:variable name="pathSoFar">
            <xsl:choose>
                <xsl:when test="//path/@xpath">
                    <xsl:value-of select="concat(//path/@xpath,'/source_relation/relation[',$pos,']')"/>
                </xsl:when>
                <xsl:otherwise>                    
                    
                    <xsl:variable name="relPath">
                        <xsl:call-template name="string-replace-all">
                            <xsl:with-param name="text" select="//source_intermediate/@container" />
                            <xsl:with-param name="replace" select="'/intermediate'" />
                            <xsl:with-param name="by" select="''" />
                        </xsl:call-template>
                    </xsl:variable>
                    <xsl:value-of select="concat($relPath,'/relation[',../@relPos,']')"/>
                    
                </xsl:otherwise>
            </xsl:choose>
                   
        </xsl:variable>
        
        <div class="row " style="margin-left:0px;">
            <div class="col-xs-1 icon">
                <label class="control-label" for="">&#160;</label>
                <img src="images/property.png"/>
            </div>
            <div class="col-xs-10" >
                <div class="form-group">
                    <label class="control-label topPadded" for="{concat('pathSourceRelation',$pos)}">Source Relation</label>
                  

                    <xsl:choose>
                                   
                        <xsl:when test="//*/@sourceAnalyzer='off'">                                 
                            <input  title="Source Relation" id="{concat('pathSourceRelation',$pos)}" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="{$pathSoFar}">
                                <xsl:attribute name="value">
                                    <xsl:value-of select="."/>
                                </xsl:attribute>
                            </input>         
                        </xsl:when>
                        <xsl:otherwise>
                            <input style="width:100%" title="Source Relation" type="hidden" class="select2 input-sm" data-id="{.}" id="{$pathSoFar}" data-xpath="{$pathSoFar}">
                                <xsl:attribute name="value">
                                    <xsl:value-of select="."></xsl:value-of>
                                </xsl:attribute>
                                <img class="loader" src="js/select2-3.5.1/select2-spinner.gif"></img>
                            </input>
                        </xsl:otherwise>
                    </xsl:choose>
                  
                   
                </div>
            
            </div>
        </div>
       
    </xsl:template>
    
   
</xsl:stylesheet>
