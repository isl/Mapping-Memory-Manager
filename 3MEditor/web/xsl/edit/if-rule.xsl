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
    <xsl:template name="if-ruleBlock">
        <xsl:variable name="pathSoFar">
            <xsl:choose>
                <xsl:when test="../@xpath">
                    <xsl:value-of select="concat(../@xpath,'/',name())"/>
                </xsl:when>
                <xsl:otherwise>

                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable> 
        <xsl:apply-templates select="if">
            <xsl:with-param name="pathSoFar" select="$pathSoFar"/>
        </xsl:apply-templates>
           

    </xsl:template>

    <xsl:template match="if">
        <xsl:param name="pathSoFar"/>
       
        <xsl:variable name="path">
            <xsl:choose>
                <xsl:when test="@xpath">
                    <!--                    <xsl:variable name="ifPos">
                        <xsl:value-of select="number(@relPos)-1"/>
                    </xsl:variable>-->
                    <!--<xsl:value-of select="concat(@xpath,'/if[',$ifPos,']')"/>-->
                    <xsl:value-of select="concat(@xpath,'/if')"/>

                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="concat($pathSoFar,'/if[',position(),']')"/>
          
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="name(..)='not'">
                <xsl:text>
                    <span class="col-sm-12 text-center">
                        <strong>NOT</strong>
                    </span>
                </xsl:text>
            </xsl:when>
            <xsl:when test="(name(..)='or' or name(..)='and') and position()!=1">
                
                <div class="text-center">
                    <!--<label class="control-label" for="operator">Operator: </label>-->
                    <div class="btn-group operator" title="{concat($path,'/..')}" id="{concat($path,'/..')}" data-toggle="buttons">
                        <label>
    
                            <xsl:attribute name="class">
                                <xsl:choose>
                                    <xsl:when test="name(..)='or'">
                                        <xsl:text>btn btn-default btn-sm active</xsl:text>

                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:text>btn btn-default btn-sm</xsl:text>

                                    </xsl:otherwise>
                                </xsl:choose>
               
                            </xsl:attribute>
                            <input name="operator" class="toggle" value="OR" autocomplete="off" checked="" type="radio"/>
                            OR</label>
                        <label>
                            <xsl:attribute name="class">
                                <xsl:choose>
                                    <xsl:when test="name(..)='and'">
                                        <xsl:text>btn btn-default btn-sm active</xsl:text>

                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:text>btn btn-default btn-sm</xsl:text>

                                    </xsl:otherwise>
                                </xsl:choose>
               
                            </xsl:attribute>
                            <input name="operator" class="toggle" value="AND" autocomplete="off" checked="" type="radio"/>
                            AND</label>

                    </div>
                </div>
                
            </xsl:when>
        </xsl:choose>
       
        <xsl:apply-templates select="*">
            <xsl:with-param name="pathSoFar" select="$path"/>
        </xsl:apply-templates>

          
        
    </xsl:template>
    <xsl:template match="or|and|not">                            
        <xsl:param name="pathSoFar"/>
        <xsl:apply-templates select="if">
            <xsl:with-param name="pathSoFar" select="concat($pathSoFar,'/',name())" />

        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="equals"> 
        <xsl:param name="pathSoFar"/>
        <xsl:variable name="equalsPath">
            <xsl:value-of select="concat($pathSoFar,'/equals')"></xsl:value-of>
        </xsl:variable>
        
        
        <div id="{$equalsPath}" data-xpath="{$equalsPath}" class="rule">
            <button title="Delete if-rule" type="button"  class="close btn btn-sm" id="{concat('delete***',$equalsPath)}">
                <span class="fa fa-times smallerIcon" ></span>
                <span class="sr-only">Close</span>
            </button>
        
            <label class="control-label" for="{$equalsPath}">Equality</label>
              
            <input title="Equality" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="{$equalsPath}">
                <xsl:attribute name="value">
                    <xsl:value-of select="."/>
                </xsl:attribute>
            </input>
            <span class="input-group-addon">=</span>

            <input title="Equality @value" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="{concat($equalsPath,'/@value')}">
                <xsl:attribute name="value">
                    <xsl:value-of select="@value"/>
                </xsl:attribute>
            </input>       
        </div>     
    </xsl:template>
    <xsl:template match="exists">
        <xsl:param name="pathSoFar"/>
 
        <xsl:variable name="existsPath">
            <xsl:value-of select="concat($pathSoFar,'/exists')"></xsl:value-of>
        </xsl:variable>
        <div id="{$existsPath}" data-xpath="{$existsPath}" class="rule">
            <button title="Delete if-rule" type="button"  class="close btn btn-sm" id="{concat('delete***',$existsPath)}">
                <span class="fa fa-times smallerIcon" ></span>
                <span class="sr-only">Close</span>
            </button>
       
                                 
            <label class="control-label" for="{$existsPath}">Existence</label>

            <input title="Existence" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="{$existsPath}">
                <xsl:attribute name="value">
                    <xsl:value-of select="."/>
                </xsl:attribute>
            </input>
        </div>     
        
    </xsl:template>
    <xsl:template match="narrower">
        <xsl:param name="pathSoFar"/>

        <xsl:variable name="narrowerPath">
            <xsl:value-of select="concat($pathSoFar,'/narrower')"></xsl:value-of>
        </xsl:variable>
                    
        <div id="{$narrowerPath}" data-xpath="{$narrowerPath}" class="rule">
            <button title="Delete if-rule" type="button"  class="close btn btn-sm" id="{concat('delete***',$narrowerPath)}">
                <span class="fa fa-times smallerIcon" ></span>
                <span class="sr-only">Close</span>
            </button>
            <label class="control-label" for="{$narrowerPath}">Narrowness</label>
              
            <input title="Narrowness" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="{$narrowerPath}">
                <xsl:attribute name="value">
                    <xsl:value-of select="."/>
                </xsl:attribute>
            </input>
            <span class="input-group-addon">nt</span>

            <input title="Narrowness @value" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="{concat($narrowerPath,'/@value')}">
                <xsl:attribute name="value">
                    <xsl:value-of select="@value"/>
                </xsl:attribute>
            </input>     
        </div>       
    </xsl:template>

    <xsl:template name="addRuleButton">
        
        <xsl:variable name="pathSoFar">          
            <xsl:value-of select="concat(../@xpath,'/',name())"/>              
        </xsl:variable> 
      
        
        <div class=" btn-group">
            <button type="button" class="btn btn-link btn-sm dropdown-toggle" data-toggle="dropdown" id="addRuleButton">
                Add Rule
                <span class="caret"></span>
            </button>
            <ul class="dropdown-menu" role="menu" id="addRuleMenu">
                <li role="presentation" class="dropdown-header">OR</li>
                <li>
                    <a class="add" href="" id="{concat('add***',$pathSoFar,'***OR_Equality')}" >Equality</a>
                </li>
                <li>                   
                    <a class="add" href="" id="{concat('add***',$pathSoFar,'***OR_Inequality')}">Inequality</a>
                </li>   
                <li>
                    <a class="add" href="" id="{concat('add***',$pathSoFar,'***OR_Existence')}" >Existence</a>
                </li>
                <li>                   
                    <a class="add" href="" id="{concat('add***',$pathSoFar,'***OR_Nonexistence')}">Nonexistence</a>
                </li>      
                <li>                   
                    <a class="add" href="" id="{concat('add***',$pathSoFar,'***OR_Narrowness')}">Narrowness</a>
                </li>           
                <li role="presentation" class="divider"></li>
                <li role="presentation" class="dropdown-header">AND</li>
                <li>
                    <a class="add" href="" id="{concat('add***',$pathSoFar,'***AND_Equality')}" >Equality</a>
                </li>
                <li>                   
                    <a class="add" href="" id="{concat('add***',$pathSoFar,'***AND_Inequality')}">Inequality</a>
                </li>   
                <li>
                    <a class="add" href="" id="{concat('add***',$pathSoFar,'***AND_Existence')}" >Existence</a>
                </li>
                <li>                   
                    <a class="add" href="" id="{concat('add***',$pathSoFar,'***AND_Nonexistence')}">Nonexistence</a>
                </li>      
                <li>                   
                    <a class="add" href="" id="{concat('add***',$pathSoFar,'***AND_Narrowness')}">Narrowness</a>
                </li>           
            </ul>
        </div>
    </xsl:template>
</xsl:stylesheet>
