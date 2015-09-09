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
    
    
    <xsl:template match="target_relation" name="target_relation">
      
        
        <div class="row ">
            <div class="col-xs-1 icon">
                <label class="control-label" for="">&#160;</label>
                <img src="images/property.png"/>
            </div>
            <div class="col-xs-11 rels ">
                <div style="padding-left:0;padding-right:18px;" class="container col-xs-6 right-bordered">

                    <div class="form-group">
                        <label class="control-label topPadded" for="">Target Relation</label> 
                        <!--<div class="input-group input-group-sm">-->

                
                        <xsl:choose>
                            <xsl:when test="//*/@targetMode='0'">    
                                <input style="width:100%;" title="Relationship" id="{concat(../@xpath,'/target_relation/relationship[1]')}" type="text" class="form-control relationship" placeholder="Fill in value" data-xpath="{concat(../@xpath,'/target_relation/relationship[1]')}">
                                    <xsl:attribute name="value">
                                        <xsl:value-of select="relationship[1]"/>
                                    </xsl:attribute>
                                </input>
                            </xsl:when>
                            <xsl:otherwise>
                                <input style="width:100%;"  title="Relationship" type="hidden"  class="select2 input-sm relationship" data-id="{normalize-space(relationship[1])}" data-xpath="{concat(../@xpath,'/target_relation/relationship[1]')}">
                                    <xsl:attribute name="value">
                                        <xsl:call-template name="substring-after-last-and-remove-prefix">
                                            <xsl:with-param name="string" select="relationship[1]" />
                                            <xsl:with-param name="delimiter" select="'/'" />
                                        </xsl:call-template>
                                    </xsl:attribute>
                                    <img class="loader" src="js/select2-3.5.1/select2-spinner.gif"/>
                                </input>
                            </xsl:otherwise>                                
                  
                        </xsl:choose>
                
                        <!--<span class="input-group-btn">-->
                      
                        <!--</span>-->
                    </div>
                </div>
                
                
            </div>
        </div>
  
        <div class="intermediates " id="{concat(../@xpath,'/target_relation/intermediate')}" data-xpath="{concat(../@xpath,'/target_relation/intermediate')}">

            <xsl:for-each select="entity">
                <xsl:variable name="entPos" select="position()"/>
                <div class="intermediate" id="{concat(../../@xpath,'/target_relation/intermediate[',$entPos,']')}" data-xpath="{concat(../../@xpath,'/target_relation/intermediate[',$entPos,']')}">
  
                    <!--                    <button title="Delete Intermediate" type="button" style="" class="close" id="{concat('delete***',../../@xpath,'/target_relation/intermediate[',$entPos,']')}">
                        <span aria-hidden="true">x</span>
                        <span class="sr-only">Close</span>
                    </button>-->
                    <button title="Delete Intermediate" type="button"  class="close btn btn-sm"  id="{concat('delete***',../../@xpath,'/target_relation/intermediate[',$entPos,']')}">
                        <span class="fa fa-times smallerIcon" ></span>
                        <span class="sr-only">Close</span>
                    </button>
                    
                    
               
               
                    <div class="row  bottom-bordered" >
                        <div class="col-xs-1 icon">
                            <label class="control-label" for="">&#160;</label>
                            <img src="images/intermediate.png"/>
                        </div>
                        <div class="col-xs-11 rels">
               
                            <xsl:apply-templates select=".">
                                <xsl:with-param name="entPos" select="$entPos"/>

                            </xsl:apply-templates>
                        </div>
                    </div>
                    <div class="row  " >
                        <div class="col-xs-1 icon">
                            <label class="control-label" for="">&#160;</label>
                            <img src="images/property.png"/>
                        </div>
                        <div class="col-xs-11 rels ">
                            <!--<div class="container  col-lg-6 right-bordered">-->
                            <xsl:for-each select="following-sibling::relationship[1]">
                                <div style="padding-left:0;padding-right:18px;" class="container col-xs-6 right-bordered">

                                    <xsl:call-template name="relationship">
                                        <xsl:with-param name="pos" select="$entPos+1"/>

                                    </xsl:call-template>
                                </div>
                            </xsl:for-each>
                            <!--</div>-->
                        </div>
                    </div>
                </div>
            </xsl:for-each>
        </div>
        <div class="row">
            <div class="col-xs-1 icon">
            </div>
            <div class="col-xs-11 rels">
                <div style="padding-left:0;padding-right:18px;" class="container col-xs-6 right-bordered">
                    <button style="padding-right:0;" data-xpath="{concat(../@xpath,'/target_relation/intermediate')}" id="{concat('add***',../@xpath,'/target_relation/intermediate')}" title="Add Intermediate" type="button" class="btn btn-link btn-sm add  pull-right ">
                        Add Intermediate</button>
                </div>
            </div>
        </div>
          
                      
    </xsl:template>
    

</xsl:stylesheet>
