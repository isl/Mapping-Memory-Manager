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
    <xsl:include href="type.xsl"/>

    <xsl:import href="../various.xsl"/>


    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    
    
    <xsl:template match="additional" name="additional">
        <xsl:param name="entPos"/>
       
        <xsl:variable name="container">
            <xsl:choose>
                <xsl:when test="../../@xpath!=''">
                    <xsl:value-of select="concat(../../@xpath,'/',name(..),'/entity[',$entPos,']/additional')"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="@container"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:for-each select="additional|../additional">
            <xsl:variable name="pathSoFar">

                <xsl:choose>
                    <xsl:when test="../../../@xpath!=''">
                        <xsl:value-of select="concat($container,'[',position(),']')"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="@xpath"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:variable>
         
            <div class="additional" id="{$pathSoFar}" data-xpath="{$pathSoFar}">
                
                <xsl:if test="position()=last() and ../../@xpath!=''">
                    <xsl:attribute name="style">border-bottom: 0;</xsl:attribute>
                </xsl:if>
               
                <button title="Delete Constant Expression" type="button"  class="close btn btn-sm" id="{concat('delete***',$pathSoFar)}">
                    <span class="fa fa-times smallerIcon" ></span>
                    <span class="sr-only">Close</span>
                </button>
                <xsl:for-each select="relationship">
                    <xsl:call-template name="relationship">
                        <xsl:with-param name="pos" select="1"/>
                        <xsl:with-param name="pathSoFar" select="$pathSoFar"/>

                    </xsl:call-template>
                       
                </xsl:for-each>
                        
                <xsl:apply-templates select="entity">
                    <xsl:with-param name="pos" select="1"/>
                    <xsl:with-param name="pathSoFar" select="$pathSoFar"/>
                </xsl:apply-templates>
            </div>
        </xsl:for-each>

                  
    </xsl:template>

    <xsl:template match="relationship" name="relationship">
        <xsl:param name="pos"/>
        <xsl:param name="pathSoFar"/>
        
        <xsl:variable name="path">
            <xsl:choose>
                <xsl:when test="$pathSoFar!=''">
                    <xsl:value-of select="$pathSoFar"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="concat(../../@xpath,'/',name(..))"/>
                </xsl:otherwise>            
            </xsl:choose>
            
            
        </xsl:variable>
        
      
        <div class="form-group">
            <xsl:choose>
                    
                <xsl:when test="name(..)='additional'">
                    <xsl:attribute name="style">margin-left:0px;margin-right:15px;</xsl:attribute>
                    <label class="control-label" for="" style="margin-left:13px;">Relation</label> 

                    <div class="input-group input-group-sm">
                        <span class="input-group-addon inputAddonMod" > 
                            <img src="images/constantArrow.png"/>
                        </span>
                        <xsl:call-template name="relationField">
                            <xsl:with-param name="path" select="$path"/>
                            <xsl:with-param name="pos" select="$pos"/>

                        </xsl:call-template>
                    </div>
                       
                </xsl:when>
                <xsl:otherwise>
                    
                    
                    <label class="control-label topPadded" for="" >Target Relation</label> 

                     
                    <xsl:call-template name="relationField">
                        <xsl:with-param name="path" select="$path"/>
                        <xsl:with-param name="pos" select="$pos"/>

                    </xsl:call-template>
                 
                </xsl:otherwise>
             
            </xsl:choose>
         

        </div>

               
           
       
        
    </xsl:template>

    <xsl:template name="relationField">
        <xsl:param name="path"/>
        <xsl:param name="pos"/>

        <xsl:choose>
                   
            <xsl:when test="//*/@targetMode='0'">    
                <input style="width:100%" title="Relationship" id="{concat($path,'/relationship[',$pos,']')}" type="text" class="form-control input-sm relationship" placeholder="Fill in value" data-xpath="{concat($path,'/relationship[',$pos,']')}">
                    <xsl:attribute name="value">
                        <xsl:value-of select="."/>
                    </xsl:attribute>
                </input>
            </xsl:when>
            <xsl:otherwise> 
                <input style="width:100%" type="hidden" class="select2 input-sm relationship" data-id="{normalize-space(.)}" title="Relationship" data-xpath="{concat($path,'/relationship[',$pos,']')}">
                    <xsl:attribute name="value">
                        <xsl:call-template name="substring-after-last-and-remove-prefix">
                            <xsl:with-param name="string" select="." />
                            <xsl:with-param name="delimiter" select="'/'" />
                        </xsl:call-template>
                    </xsl:attribute>
                    <img class="loader" src="js/select2-3.5.1/select2-spinner.gif"/>
                </input>
            </xsl:otherwise>
        </xsl:choose>
        
    </xsl:template>


    <xsl:template match="entity">
        <xsl:param name="mappingPos"></xsl:param>
        <xsl:param name="entPos"/>
        <xsl:param name="pathSoFar"/>
       
        <xsl:variable name="path">
            <xsl:choose>
                <xsl:when test="$pathSoFar!=''">
                    <xsl:value-of select="$pathSoFar"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="../../@xpath"/>
                </xsl:otherwise>
            </xsl:choose>
            
            
        </xsl:variable>
                   
        <div  data-xpath="{concat($path,'/',name(..),'/entity[1]/type')}">
            <xsl:call-template name="type">
                <xsl:with-param name="entPos" select="1"/>
                <xsl:with-param name="pathSoFar" select="concat($path,'/entity[1]')"/>

            </xsl:call-template>
        </div>
                    
                
    </xsl:template>
</xsl:stylesheet>
