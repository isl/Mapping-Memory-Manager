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
    <xsl:include href="target.xsl"/>
    <xsl:include href="../various.xsl"/>
    <xsl:include href="entity.xsl"/>
    <xsl:import href="comments.xsl"/>
    <xsl:include href="if-rule.xsl"/>

    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
  
    <xsl:template match="domain" name="domain">
        <xsl:param name="mappingPos"/>
                   
        <xsl:variable name="pathSoFar">
            <xsl:choose>
                <xsl:when test="$mappingPos!=''">
                    <xsl:value-of select="concat('//x3ml/mappings/mapping[',$mappingPos,']/domain')"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="concat(@xpath,'')"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
                          
        <tr class="edit" data-xpath="{$pathSoFar}">
            <td title="{$pathSoFar}">D</td>
            <td style="padding:0 0 0 0;min-width:200px;">
                 <div class="row "  style="margin-left:0px;">
                    <div class="col-xs-1 icon">
                        <label class="control-label" for="">&#160;</label>
                        <img src="images/domain.png"/>
                    </div>
            
                    <div class="col-xs-11">
                        <div class="form-group">

                              <label class="control-label topPadded" for="domainSourceNode">Source Node</label>
                              
                                <xsl:choose>
                                   
                        <xsl:when test="//*/@sourceAnalyzer='off'">                                 
                             <input title="Source Node" id="domainSourceNode" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="{concat($pathSoFar,'/source_node')}">
                    <xsl:attribute name="value">
                        <xsl:value-of select="source_node"/>
                    </xsl:attribute>
                </input>
                        </xsl:when>
                        <xsl:otherwise>
                            <input style="width:100%;" title="Source Node" type="hidden" class="select2 input-sm" data-id="{.}" id="domainSourceNode" data-xpath="{concat($pathSoFar,'/source_node')}">
                                <xsl:attribute name="value">
                                    <xsl:value-of select="source_node"></xsl:value-of>
                                </xsl:attribute>
                                <img class="loader" src="js/select2-3.5.1/select2-spinner.gif"></img>
                            </input>
                        </xsl:otherwise>
                    </xsl:choose>
                              
                              
             
                        </div>
                    </div>
                </div>
                
                
                
                
              
            </td>
            <td style="padding:0 0 0 0;min-width:600px;">
                <!--                <xsl:apply-templates select="target_node"/>-->
                <xsl:for-each select="target_node/entity">
                    <xsl:variable name="entPos" select="position()"/>

                      <div class="row  bottom-bordered">
                        <div class="col-xs-1 icon">
                            <label class="control-label" for="">&#160;</label>
                            <img src="images/domain.png"/>
                        </div>
                        <div class="col-xs-11 rels">
               
                            <xsl:call-template name="entity">
                        <xsl:with-param name="entPos" select="$entPos"/>

                    </xsl:call-template>
                        </div>
                    </div>
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    
                   
                </xsl:for-each>
            </td>
            <td>                
                <!--<label class="control-label" for="">Rules</label>--> 
                <xsl:for-each select="target_node">
                    <div class="rules" data-xpath="{concat(//domain/@xpath,'/target_node/if')}">
                        <xsl:call-template name="if-ruleBlock"></xsl:call-template> 
                        <xsl:call-template name="addRuleButton"></xsl:call-template>    
                    </div>
                </xsl:for-each>                   
            </td>
            <td>                
                 <div class="comments" id="{concat(//domain/@xpath,'/comments')}" data-xpath="{concat(//domain/@xpath,'/comments')}">                       
                    <xsl:apply-templates select="comments"/>                         
                </div>
                <xsl:call-template name="addCommentButton"></xsl:call-template>   
<!--                <label class="control-label" for="">Comments</label> 

            <div class="comments" id="{concat(//domain/@xpath,'/comments')}" data-xpath="{concat(//domain/@xpath,'/comments')}">                       
                <xsl:apply-templates select="comments"/>
            </div>
            <button data-xpath="{concat(//domain/@xpath,'/comments')}" id="{concat('add***',//domain/@xpath,'/comments')}" title="Add Comments" type="button" class="btn btn-default btn-sm  add">
                <xsl:if test="comments">                   
                    <xsl:attribute name="style">display:none;</xsl:attribute>
                </xsl:if>
                <span class="glyphicon glyphicon-plus"></span>&#160;Comments</button>-->
            </td>
            <td class="actions" style="display:none;">
                <div class="btn-group-vertical pull-right">
                  
                    
                    <!--                    <button title="Delete Link" type="button" class="close btn btn-sm" id="{concat('delete***',//path/@xpath,'/..')}">
                        <span aria-hidden="true">x</span>
                        <span class="fa fa-times"></span>
                        <span class="sr-only">Close</span>
                    </button>-->
                    
                    <button title="Copy Map" type="button" class="btn btn-default btn-sm closeLike copy" id="{concat('copy***',$pathSoFar,'/..')}"> 
                        <span class="fa fa-files-o smallerIcon" ></span> 
                    </button>
                    <br/>
                    <button title="Paste Map" type="button" class="btn btn-default btn-sm closeLike paste" id="{concat('paste***',$pathSoFar,'/..')}"> 
                        <span class="fa fa-clipboard smallerIcon" ></span> 
                    </button>
                    
                    <xsl:if test="@mappingsCount&gt;1">
                        <br/>
                        <button title="Delete Map" type="button" class="close btn btn-sm" id="{concat('delete***',$pathSoFar,'/..')}">
                            <!--<span aria-hidden="true">x</span>-->
                            <span class="fa fa-times smallerIcon" ></span>
                            <span class="sr-only" >Close</span>
                        </button>
                    </xsl:if>
                </div>
                
                
               
                
            </td>
            <!--<script src="js/after.js"></script>-->    

        </tr>

                         
    </xsl:template>
    

</xsl:stylesheet>
