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
    <xsl:template match="comments">
       
        <xsl:variable name="commentsPath">
            <xsl:choose>
                <xsl:when test="../@xpath">
                    <xsl:value-of select="concat(../@xpath,'/comments')"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="@container"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable> 

        <div class="comment" > <!--id="{$commentsPath}" data-xpath="{$commentsPath}"-->

            <xsl:for-each select="comment">

                <xsl:variable name="pathSoFar" select="concat($commentsPath,'/comment[',position(),']')"/>
                <xsl:if test="string-length(*/text())!=21"> <!-- 21 is the magic number-->
                    <button title="Delete Comments" type="button"  class="close btn btn-sm" id="{concat('delete***',$commentsPath)}">
                        <span class="fa fa-times smallerIcon" ></span>
                        <span class="sr-only">Close</span>
                    </button>
                </xsl:if>
                                                          
                <div class="form-group" id="{concat($pathSoFar,'/rationale')}">
                    <xsl:attribute name="style">                           
                        <xsl:choose>
                            <xsl:when test="rationale!=''">
                                display:block;
                            </xsl:when>
                            <xsl:otherwise>
                                display:none;
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:attribute>                            
                            
                    <label class="control-label" for="rationale">Rationale</label>
                    <textarea id="rationale" placeholder="Fill in value" class="form-control input-sm" rows="3" data-xpath="{concat($pathSoFar,'/rationale')}">
                        <xsl:value-of select="rationale"></xsl:value-of>
                    </textarea>

                </div>
                        
                        
                <div class="form-group" id="{concat($pathSoFar,'/alternatives')}">
                    <xsl:attribute name="style">                           
                        <xsl:choose>
                            <xsl:when test="alternatives!=''">
                                display:block;
                            </xsl:when>
                            <xsl:otherwise>
                                display:none;
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:attribute>              

                    <label class="control-label" for="alternatives">Alternatives</label>

                    <input title="Alternatives" id="alternatives" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="{concat($pathSoFar,'/alternatives')}">
                        <xsl:attribute name="value">
                            <xsl:value-of select="alternatives"/>
                        </xsl:attribute>
                    </input>
                </div>
                <div class="form-group" id="{concat($pathSoFar,'/typical_mistakes')}">
                    <xsl:attribute name="style">                           
                        <xsl:choose>
                            <xsl:when test="typical_mistakes!=''">
                                display:block;
                            </xsl:when>
                            <xsl:otherwise>
                                display:none;
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:attribute>          

                    <label class="control-label" for="typical_mistakes">Typical Mistakes</label>

                    <input title="Typical Mistakes" id="typical_mistakes" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="{concat($pathSoFar,'/typical_mistakes')}">
                        <xsl:attribute name="value">
                            <xsl:value-of select="typical_mistakes"/>
                        </xsl:attribute>
                    </input>
                </div>
                <div class="form-group" id="{concat($pathSoFar,'/local_habits')}">
                    <xsl:attribute name="style">                           
                        <xsl:choose>
                            <xsl:when test="local_habits!=''">
                                display:block;
                            </xsl:when>
                            <xsl:otherwise>
                                display:none;
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:attribute>          

                    <label class="control-label" for="local_habits">Local Habits</label>

                    <input title="Local Habits" id="local_habits" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="{concat($pathSoFar,'/local_habits')}">
                        <xsl:attribute name="value">
                            <xsl:value-of select="local_habits"/>
                        </xsl:attribute>
                    </input>
                </div>
                <div class="form-group" id="{concat($pathSoFar,'/link_to_cook_book')}">
                    <xsl:attribute name="style">                           
                        <xsl:choose>
                            <xsl:when test="link_to_cook_book!=''">
                                display:block;
                            </xsl:when>
                            <xsl:otherwise>
                                display:none;
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:attribute>          

                    <label class="control-label" for="link_to_cook_book">Link to Cook Book</label>

                    <input title="Link to Cook Book" id="local_habits" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="{concat($pathSoFar,'/link_to_cook_book')}">
                        <xsl:attribute name="value">
                            <xsl:value-of select="link_to_cook_book"/>
                        </xsl:attribute>
                    </input>
                </div>
                <div class="form-group" id="{concat($pathSoFar,'//example_source')}">
                    <xsl:attribute name="style">                           
                        <xsl:choose>
                            <xsl:when test=".//example_source!=''">
                                display:block;
                            </xsl:when>
                            <xsl:otherwise>
                                display:none;
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:attribute>          

                    <label class="control-label" for="example_source">Example Source</label>

                    <input title="Example Source" id="example_source" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="{concat($pathSoFar,'//example_source')}">
                        <xsl:attribute name="value">
                            <xsl:value-of select=".//example_source"/>
                        </xsl:attribute>
                    </input>
                </div>
                <div class="form-group" id="{concat($pathSoFar,'//example_target')}">
                    <xsl:attribute name="style">                           
                        <xsl:choose>
                            <xsl:when test=".//example_target!=''">
                                display:block;
                            </xsl:when>
                            <xsl:otherwise>
                                display:none;
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:attribute>          

                    <label class="control-label" for="example_target">Example Target</label>

                    <input title="Example Target" id="example_target" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="{concat($pathSoFar,'//example_target')}">
                        <xsl:attribute name="value">
                            <xsl:value-of select=".//example_target"/>
                        </xsl:attribute>
                    </input>
                </div>
                <div class="form-group" id="{concat($pathSoFar,'/comments_last_update/@person')}">
                    <xsl:attribute name="style">                           
                        <xsl:choose>
                            <xsl:when test="comments_last_update/@person!=''">
                                display:block;
                            </xsl:when>
                            <xsl:otherwise>
                                display:none;
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:attribute>        

                    <label class="control-label" for="comments_last_update_person">Comments Last Update Person</label>

                    <input title="Comments Last Update Person" id="comments_last_update_person" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="{concat($pathSoFar,'/comments_last_update/@person')}">
                        <xsl:attribute name="value">
                            <xsl:value-of select="comments_last_update/@person"/>
                        </xsl:attribute>
                    </input>
                </div>
                <div class="form-group" id="{concat($pathSoFar,'/comments_last_update/@date')}">
                    <xsl:attribute name="style">                           
                        <xsl:choose>
                            <xsl:when test="comments_last_update/@date!=''">
                                display:block;
                            </xsl:when>
                            <xsl:otherwise>
                                display:none;
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:attribute>        

                    <label class="control-label" for="comments_last_update_date">Comments Last Update Date</label>

                    <input title="Comments Last Update Date" id="comments_last_update_date" type="text" class="form-control input-sm" placeholder="Fill in value" data-xpath="{concat($pathSoFar,'/comments_last_update/@date')}">
                        <xsl:attribute name="value">
                            <xsl:value-of select="comments_last_update/@date"/>
                        </xsl:attribute>
                    </input>
                </div>
              
            </xsl:for-each>
        </div>
      
            
           
    </xsl:template>
    
    <xsl:template name="addCommentButton">
        
        <xsl:variable name="commentsPath">
            <xsl:choose>
                <xsl:when test="@xpath">
                    <xsl:value-of select="concat(@xpath,'/comments')"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="@container"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable> 
        
     
        
        
        <div class=" btn-group">
            <button type="button" class="btn btn-link btn-sm dropdown-toggle" data-toggle="dropdown">
                <!--                <span class="glyphicon glyphicon-plus"></span>&#160;Rule-->
                Add Comment about
                <span class="caret"></span>
            </button>
            <ul class="dropdown-menu" role="menu">
                <li>
                    <!--<a class="add" href="" id="{concat('add***',$pathSoFar,'***Equality')}" >Equality</a>-->
                    <xsl:if test="comments/comment/rationale!=''">
                        <xsl:attribute name="class">disabled</xsl:attribute>
                    </xsl:if>
                    <a class="add" href="" id="{concat('add***rationale***',$commentsPath)}" >Rationale</a>
                </li>
                <li>                  
                    <xsl:if test="comments/comment/alternatives!=''">
                        <xsl:attribute name="class">disabled</xsl:attribute>
                    </xsl:if>
                    <a class="add" href="" id="{concat('add***alternatives***',$commentsPath)}" >Alternatives</a> 
                </li>   
                <li>
                                      
                    <xsl:if test="comments/comment/typical_mistakes!=''">
                        <xsl:attribute name="class">disabled</xsl:attribute>
                    </xsl:if>
                    <a class="add" href="" id="{concat('add***typical_mistakes***',$commentsPath)}" >Typical mistakes</a>
                </li>
                <li>
                                      
                    <xsl:if test="comments/comment/local_habits!=''">
                        <xsl:attribute name="class">disabled</xsl:attribute>
                    </xsl:if>
                    <a class="add" href="" id="{concat('add***local_habits***',$commentsPath)}" >Local Habits</a>
                </li>         
                <li>
                                      
                    <xsl:if test="comments/comment/link_to_cook_book!=''">
                        <xsl:attribute name="class">disabled</xsl:attribute>
                    </xsl:if>
                    <a class="add" href="" id="{concat('add***link_to_cook_book***',$commentsPath)}" >Link to Cook Book</a>
                </li>     
                <li>
                                      
                    <xsl:if test="comments/comment//example_source!=''">
                        <xsl:attribute name="class">disabled</xsl:attribute>
                    </xsl:if>
                    <a class="add" href="" id="{concat('add***/example_source***',$commentsPath)}" >Example Source</a>
                </li>       
                <li>
                                      
                    <xsl:if test="comments/comment//example_target!=''">
                        <xsl:attribute name="class">disabled</xsl:attribute>
                    </xsl:if>
                    <a class="add" href="" id="{concat('add***/example_target***',$commentsPath)}" >Example Target</a>
                </li>              
                <li>
                                      
                    <xsl:if test="comments/comment/comments_last_update/@person!=''">
                        <xsl:attribute name="class">disabled</xsl:attribute>
                    </xsl:if>
                    <a class="add" href="" id="{concat('add***comments_last_update/@person***',$commentsPath)}" >Comments Last Update Person</a>
                </li>              
                <li>
                                      
                    <xsl:if test="comments/comment/comments_last_update/@date!=''">
                        <xsl:attribute name="class">disabled</xsl:attribute>
                    </xsl:if>
                    <a class="add" href="" id="{concat('add***comments_last_update/@date***',$commentsPath)}" >Comments Last Update Date</a>
                </li>          
                
               
            </ul>
        </div>
        
    </xsl:template>

</xsl:stylesheet>
