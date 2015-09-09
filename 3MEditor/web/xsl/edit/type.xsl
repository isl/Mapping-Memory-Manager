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
    
    
    <xsl:template  name="type" match="*">
        <xsl:param name="entPos"/>
        <xsl:param name="pathSoFar"/>
       
       
        
        <xsl:variable name="container">
            <xsl:choose>
                <xsl:when test="../../@xpath!=''">
                    <xsl:value-of select="concat(../../../@xpath,'/',name(../..),'/entity[',$entPos,']/type')"/>
                </xsl:when>

                <xsl:otherwise>
                    <xsl:value-of select="//type/@container"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <xsl:for-each select="type">
           
            <xsl:variable name="path">
                <xsl:choose>
                    <xsl:when test="$pathSoFar!=''">
                        <xsl:value-of select="concat($pathSoFar,'/type[',position(),']')"/>
                    </xsl:when>
                    <xsl:when test="@xpath!=''">
                        <xsl:value-of select="@xpath"/>
                    </xsl:when>

                    <xsl:otherwise>
                        <xsl:value-of select="../../@xpath"/>
                    </xsl:otherwise>
                </xsl:choose>
            
            
            </xsl:variable>
               
           
            <xsl:variable name="strippedURL">
                <xsl:choose>
                    <xsl:when test="contains(.,'rdf-schema#Literal')">
                        <xsl:text>rdf-schema#Literal</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:call-template name="substring-after-last-and-remove-prefix">
                            <xsl:with-param name="string" select="."></xsl:with-param>
                            <xsl:with-param name="delimiter" select="'/'"></xsl:with-param>
                        </xsl:call-template>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:variable>
             
                
            <xsl:choose>
                <xsl:when test="name(../..)!='additional'">
                    <div class="col-xs-12 type">
                        <xsl:if test="//intermediate/@xpath">
                            <xsl:attribute name="style">margin-left:0px;</xsl:attribute>
                        </xsl:if>

                        <xsl:if test="position()=1 and not(@targetMode)"> 
                            <label class="control-label topPadded" for="{$path}">Target Entity</label>  
                        </xsl:if>
                        <div class="input-group input-group-sm">
 
                            <xsl:choose>
                                   
                                <xsl:when test="//*/@targetMode='0'">                                 

                                    <input title="Entity" id="{$path}" type="text" class="form-control" placeholder="Fill in value" data-xpath="{$path}">
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="."/>
                                        </xsl:attribute>
                                    </input>
                                </xsl:when> 
                                <xsl:otherwise>
                                    <input  style="width:100%" title="Entity" type="hidden" class="select2 input-sm" data-id="{.}" id="{$path}" data-xpath="{$path}">
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="$strippedURL"></xsl:value-of>
                                        </xsl:attribute>
                                        <img class="loader" src="js/select2-3.5.1/select2-spinner.gif"></img>
                                    </input>
                                </xsl:otherwise>
                            </xsl:choose>
                                
                    
                            <span class="input-group-btn">
                        
                                <button class="btn btn-default delete" type="button" title="Delete Entity" id="{concat('delete***',$path)}" >
                                    <!--<span class="glyphicon glyphicon-remove"></span>-->
                                    <span class="fa fa-times "></span>
                                </button>   
                            </span>
                   

                        </div>
                    </div>
                       
                        
                </xsl:when>
                <xsl:otherwise>
                    <!--<div class="col-xs-11 type">-->
                    <!--<xsl:attribute name="style">margin-left:25px;</xsl:attribute>-->



                    <div class="form-group" style="padding-right:0;margin-right:15px;padding-left:15px;">
                        <label class="control-label" for="" style="margin-left:13px;">Entity</label>
                        
                        <div class="input-group input-group-sm">
                            <span class="input-group-addon inputAddonMod" >
                                <img src="images/constantBox.png"/>

                            </span>

                            <xsl:choose>
                              
                                <xsl:when test="//*/@targetMode='0'">    
                                    <input style="width:100%;"  title="Entity" id="{$path}" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="{$path}">
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="."/>
                                        </xsl:attribute>
                                    </input>
                                </xsl:when> 
                                <xsl:otherwise>
                                    <input style="width:100%;" title="Entity" type="hidden" class="select2 input-sm" data-id="{.}" id="{$path}" data-xpath="{$path}">
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="$strippedURL"></xsl:value-of>
                                        </xsl:attribute>
                                        <img class="loader" src="js/select2-3.5.1/select2-spinner.gif"></img>
                                    </input>
                                </xsl:otherwise>
                            </xsl:choose>
                        </div>
                    </div>
                    <!--</div>-->
                    <div class="form-group" style="padding-right:0;margin-right:15px;padding-left:20px;">

                                     
                            <label class=" control-label" for="sourceType" style="margin-left:13px;">Constant</label>

                            <div class="input-group input-group-sm">
                            
                                <span class="input-group-addon inputAddonMod" >:</span>
                                <input id="{concat($path,'/../instance_info/constant')}" title="Constant" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="{concat($path,'/../instance_info/constant')}">
                                    <xsl:attribute name="value">
                                        <xsl:value-of select="../instance_info/constant"></xsl:value-of>
                                    </xsl:attribute>
                                </input>
                                             
                               
                            </div>
                        
                    </div>
                   
               
                    <div class="language form-group " >
                        <xsl:attribute name="style">
                            <xsl:choose>
                                <xsl:when test="../instance_info/language">
                                    <xsl:text>display:block;padding-left:10px;margin-right:15px;</xsl:text>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:text>display:none;padding-left:10px;margin-right:15px;</xsl:text>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:attribute>
                        <label class=" control-label" for="sourceType" style="font-weight:normal;">Language</label>
                        <div class="input-group input-group-sm">
                            <input id="{concat($path,'/../instance_info/language')}" title="Language" type="text" class="form-control" placeholder="Fill in value" data-xpath="{concat($path,'/../instance_info/language')}">
                                <xsl:attribute name="value">
                                    <xsl:value-of select="../instance_info/language"></xsl:value-of>
                                </xsl:attribute>
                            </input>
                                           
                            <span class="input-group-btn">
                                <button class="btn btn-default btn-sm toggle" type="button" title="Delete Language">
                                    <!--                                    <span class="glyphicon glyphicon-remove"></span>-->
                                    <span class="fa fa-times "></span>
                                </button>      
                            </span>
                        </div>
                    </div>
                    <div class="description form-group">
                        <xsl:attribute name="style">
                            <xsl:choose>
                                <xsl:when test="../instance_info/description">
                                    <xsl:text>display:block;padding-left:10px;margin-right:15px;</xsl:text>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:text>display:none;padding-left:10px;margin-right:15px;</xsl:text>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:attribute>
                        <label class=" control-label" for="sourceType" style="font-weight:normal;">Description</label>
                        <div class="input-group">

                            <textarea id="{concat($path,'/../instance_info/description')}" placeholder="Fill in value" class="form-control input-sm" rows="3" data-xpath="{concat($path,'/../instance_info/description')}">
                                <xsl:value-of select="../instance_info/description"></xsl:value-of>
                            </textarea>
                            <span class="input-group-addon" style="background-color:white;">
                                <button class="btn btn-default btn-sm toggle" type="button" title="Delete Description"  style="border:0;">
                                    <span class="fa fa-times "></span>
                                </button>      
                            </span>
                                
                        </div>
                    </div>          
                  
                    
                    <div class="form-group" style="padding-left:10px;padding-top:5px;">
                        <!--<label class="control-label" for="">&#160;</label>-->
                        <!--<div class="input-group ">-->
                        <div class=" btn-group ">
                            <button type="button" class="btn btn-link btn-sm  dropdown-toggle instance" data-toggle="dropdown" style="padding:0;border:0;">
                                Add instance info

                                <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu" role="menu">
                                <li>
                                    <xsl:if test="../instance_info/language">
                                        <xsl:attribute name="class">disabled</xsl:attribute>
                                    </xsl:if>
                                    <a class="add" href="" id="{concat('add***',$path,'/../instance_info/language')}">Language</a>
                                </li>
                                <li>
                                    <xsl:if test="../instance_info/description">
                                        <xsl:attribute name="class">disabled</xsl:attribute>
                                    </xsl:if>
                                    <a class="add" href="" id="{concat('add***',$path,'/../instance_info/description')}">Description</a>
                                </li>
                                    
                               
                                  
                            </ul>
                        </div>
                        <!--</div>-->
                    </div>
             
                    
                </xsl:otherwise>
            </xsl:choose>
                    
                  
                        
        </xsl:for-each>
        
    </xsl:template>

</xsl:stylesheet>
