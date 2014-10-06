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

This file is part of the x3mlEditor webapp of Mapping Memory Manager project.

-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template match="/" name="entity">
        <xsl:param name="pathSoFar"></xsl:param>
       
        <xsl:for-each select="type">
            <xsl:variable name="pos4" select="position()"/>

            <xsl:variable name="strippedURL">
                <xsl:choose>
                    <xsl:when test="contains(.,'rdf-schema#Literal')">
                        <xsl:text>rdf-schema#Literal</xsl:text>                        
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:call-template name="substring-after-last-and-remove-prefix">
                            <xsl:with-param name="string" select="." />
                            <xsl:with-param name="delimiter" select="'/'" />
                        </xsl:call-template>
                    </xsl:otherwise>
                </xsl:choose>
              
            </xsl:variable>
            
          
            
            <!--xsl:if test="//viewMode=0 or $strippedURL!=''"-->
            &#160;
            <span  data-editable="select" data-path="{concat($pathSoFar,'/type[',$pos4,']')}">
                <xsl:value-of select="$strippedURL"/>
            </span>
       
            <!--/xsl:if-->
            <xsl:if test="name(../../..)='range'">
                
                <xsl:choose>
                    <xsl:when test="../instance_info/constant">
                        <xsl:if test=".!=''">
                        &#160;=&#160;"<span style="display:inline;" class="Value_Binding" data-editable="" data-path="{concat($pathSoFar,'/instance_info/constant')}">
                                <xsl:value-of select="../instance_info/constant"/>                               
                            </span>"       
                            <xsl:if test="//viewMode=0">
                                <a style="display:inline;" title="Delete Value" href="" onclick="action('Mapping?action=delete&amp;xpath={concat($pathSoFar,'/instance_info/constant')}&amp;id={//output/id}');return false;">
                                    <img src="formating/images/delete16.png"/>
                                </a>       
                            </xsl:if>    
                        </xsl:if>       
                    </xsl:when>
                    <xsl:otherwise>&#160;
                        <xsl:if test="//viewMode=0">
                            <a class="smallerLetters" style="display:inline;" title="Add Constant" href="" >
                                <xsl:attribute name="onclick">
                                    <xsl:choose>
                                       
                                        <xsl:when test="../instance_info/description">
                                            <xsl:text>action('Mapping?action=addBefore___instance_info/constant&amp;xpath=</xsl:text>
                                            <xsl:value-of select="concat($pathSoFar,'/instance_info/description')"/>
                                            <xsl:text>&amp;id=</xsl:text>
                                            <xsl:value-of select="//output/id"/>
                                            <xsl:text>');return false;</xsl:text>
                                        </xsl:when>
                                        <xsl:when test="../instance_info">
                                            <xsl:text>action('Mapping?action=add___instance_info/constant&amp;xpath=</xsl:text>
                                            <xsl:value-of select="concat($pathSoFar,'/instance_info/*')"/>
                                            <xsl:text>&amp;id=</xsl:text>
                                            <xsl:value-of select="//output/id"/>
                                            <xsl:text>');return false;</xsl:text>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:text>action('Mapping?action=addAfter___instance_info_constantONLY___*&amp;xpath=</xsl:text>
                                            <xsl:value-of select="concat($pathSoFar,'/type[last()]')"/>
                                            <xsl:text>&amp;id=</xsl:text>
                                            <xsl:value-of select="//output/id"/>
                                            <xsl:text>');return false;</xsl:text>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:attribute>                           

                                <img src="formating/images/plus16.png"/>
                                <span style="display:inline;">Constant</span>
                            </a>
                        </xsl:if>
                    </xsl:otherwise>
                </xsl:choose>
               
            </xsl:if>


            <xsl:if test="//viewMode=0 and count(../entity)>1">
                <a style="display:inline;float:right;" title="Delete Type" href="" onclick="action('Mapping?action=delete&amp;xpath={concat($pathSoFar,'/entity/type[',$pos4,']')}&amp;id={//output/id}');return false;">
                    <img src="formating/images/delete16.png"/>
                </a>
            </xsl:if>
            <xsl:if test="$pos4='1'">       <!-- Language+Description-->              
                <xsl:choose>
                    <xsl:when test="../instance_info/language">
                        &#160;
                        <span style="display:inline;" class="Value_Binding" data-editable="" data-path="{concat($pathSoFar,'/instance_info/language')}">
                            <xsl:value-of select="../instance_info/language"/>                               
                        </span> 
                        <xsl:if test="//viewMode=0">      
                            <a style="display:inline;" title="Delete Language" href="" onclick="action('Mapping?action=delete&amp;xpath={concat($pathSoFar,'/instance_info/language')}&amp;id={//output/id}');return false;">
                                <img src="formating/images/delete16.png"/>
                            </a> 
                        </xsl:if>
                                         
                    </xsl:when>
                    <xsl:otherwise>&#160;
                        <xsl:if test="//viewMode=0">      

                            <a class="smallerLetters" style="display:inline;" title="Add Language" href="">
                                <xsl:attribute name="onclick">
                                    <xsl:choose>
                                        <xsl:when test="../instance_info/*">
                                            <xsl:text>action('Mapping?action=addBefore___instance_info/language&amp;xpath=</xsl:text>
                                            <xsl:value-of select="concat($pathSoFar,'/instance_info/*')"/>
                                            <xsl:text>&amp;id=</xsl:text>
                                            <xsl:value-of select="//output/id"/>
                                            <xsl:text>');return false;</xsl:text>
                                        </xsl:when>
                                        <xsl:when test="../instance_info">
                                            <xsl:text>action('Mapping?action=add___instance_info/language&amp;xpath=</xsl:text>
                                            <xsl:value-of select="concat($pathSoFar,'/instance_info/*')"/>
                                            <xsl:text>&amp;id=</xsl:text>
                                            <xsl:value-of select="//output/id"/>
                                            <xsl:text>');return false;</xsl:text>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:text>action('Mapping?action=addAfter___instance_info_languageONLY___*&amp;xpath=</xsl:text>
                                            <xsl:value-of select="concat($pathSoFar,'/type[last()]')"/>
                                            <xsl:text>&amp;id=</xsl:text>
                                            <xsl:value-of select="//output/id"/>
                                            <xsl:text>');return false;</xsl:text>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:attribute>                           


                                <img src="formating/images/plus16.png"/>
                                <span style="display:inline;">Language</span>
                            </a>
                        </xsl:if>
                        
                    </xsl:otherwise>
                </xsl:choose>
                
                <xsl:choose>
                    <xsl:when test="../instance_info/description">
                        &#160;
                        <!--span style="display:inline;" class="Value_Binding" data-editable="" data-path="{concat($pathSoFar,'/instance_info/description')}">
                            <xsl:value-of select="../instance_info/description"/>                               
                        </span-->
                        <a class="smallerLetters" style="display:inline;" title="View Description" href="" onclick="javascript:toggleDescription(this);return false;">
                            <span style="display:inline;">(Description...)</span>
                        </a>
                         
                        <xsl:if test="//viewMode=0">      
                            <a style="display:inline;" title="Delete Description" href="" onclick="action('Mapping?action=delete&amp;xpath={concat($pathSoFar,'/instance_info/description')}&amp;id={//output/id}');return false;">
                                <img src="formating/images/delete16.png"/>
                            </a> 
                        </xsl:if>
                                         
                    </xsl:when>
                    <xsl:otherwise>&#160;
                        <xsl:if test="//viewMode=0">      

                            <a class="smallerLetters" style="display:inline;" title="Add Description" href="">
                                <xsl:attribute name="onclick">
                                    <xsl:choose>
                                        
                                        <xsl:when test="../instance_info">
                                            <xsl:text>action('Mapping?action=add___instance_info/description&amp;xpath=</xsl:text>
                                            <xsl:value-of select="concat($pathSoFar,'/instance_info/*')"/>
                                            <xsl:text>&amp;id=</xsl:text>
                                            <xsl:value-of select="//output/id"/>
                                            <xsl:text>');return false;</xsl:text>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:text>action('Mapping?action=addAfter___instance_info_descriptionONLY___*&amp;xpath=</xsl:text>
                                            <xsl:value-of select="concat($pathSoFar,'/type[last()]')"/>
                                            <xsl:text>&amp;id=</xsl:text>
                                            <xsl:value-of select="//output/id"/>
                                            <xsl:text>');return false;</xsl:text>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:attribute>                           


                                <img src="formating/images/plus16.png"/>
                                <span style="display:inline;">Description</span>
                            </a>
                        </xsl:if>
                        
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:if>
            <xsl:if test="$strippedURL!='rdf-schema#Literal' and $pos4='1'">                     
                <xsl:choose>
                    <xsl:when test="not(../@variable)">&#160;
                        <xsl:if test="//viewMode=0">

                            <a class="smallerLetters" style="display:inline;" title="Add Variable" href="" onclick="action('Mapping?action=addAttr&amp;name=variable&amp;xpath={concat($pathSoFar,'')}&amp;id={//output/id}');return false;">
                                <img src="formating/images/plus16.png"/>
                                <span style="display:inline;">Variable</span>
                            </a>
                        </xsl:if>
                    </xsl:when>
                    <xsl:otherwise>
                        <span style="margin-left:10px;">
                            <xsl:text>[</xsl:text>
                            <span  data-editable="" data-path="{concat($pathSoFar,'/@variable')}">
                                <xsl:value-of select="../@variable"/>
                            </span>								
                            <xsl:text>]</xsl:text>
                        </span>
                        <xsl:if test="//viewMode=0">
                            <a style="display:inline;" title="Delete Variable" href="" onclick="action('Mapping?action=delete&amp;xpath={concat($pathSoFar,'/@variable')}&amp;id={//output/id}');return false;">
                                <img src="formating/images/delete16.png"/>
                            </a>
                        </xsl:if>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:if>
            
            <xsl:if test="//viewMode=0 or $strippedURL!=''">
                <xsl:choose>
                    <xsl:when test="../instance_info/description">
                        <div class="description" style="display:none;"> 
                            <div class="comment_title">Description:</div>
                            <span class="comment_content" data-editable="textarea" data-path="{concat($pathSoFar,'/instance_info/description')}">
                                <xsl:value-of select="../instance_info/description"/>
                            </span>
                        </div>
                        
                        <br class="description" style="display:block"></br>
                    </xsl:when>
                    <xsl:otherwise>
  
                        <br/>
                    </xsl:otherwise>
                </xsl:choose>
               
            </xsl:if>
           
           
           
           
           
        </xsl:for-each>
        <xsl:call-template name="instance_generator">
            <xsl:with-param name="pathSoFar" select="$pathSoFar"/>
        </xsl:call-template>

        <xsl:if test="//viewMode=0">
           
            <a   class="smallerLetters left" title="Add Type" href="" onclick="">
                <xsl:attribute name="onclick">
                    <xsl:choose>
                        <xsl:when test="name(..)='target_relation'">
                            <xsl:text>action('Mapping?action=addAfter___type&amp;xpath=</xsl:text>
                            <xsl:value-of select="concat($pathSoFar,'/type[last()]')"/>
                            <xsl:text>&amp;id=</xsl:text>
                            <xsl:value-of select="//output/id"/>
                            <xsl:text>');return false;</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:text>action('Mapping?action=addAfter&amp;xpath=</xsl:text>
                            <xsl:value-of select="concat($pathSoFar,'/type[last()]')"/>
                            <xsl:text>&amp;id=</xsl:text>
                            <xsl:value-of select="//output/id"/>
                            <xsl:text>');return false;</xsl:text>
                        </xsl:otherwise>
                    </xsl:choose>             
                </xsl:attribute>
                <img src="formating/images/plus16.png"/>
                <span style="display:inline;">Another Type</span>
                
            </a>
            <span style="clear:both;height:2px;display:block;">
            </span>

        </xsl:if>
              
              
        <xsl:for-each select="additional">
            <xsl:apply-templates select=".">
                <xsl:with-param name="pathSoFar" select="concat($pathSoFar,'')"/>
                <xsl:with-param name="pos3" select="position()"/>
            </xsl:apply-templates>
        </xsl:for-each>
        
        <xsl:if test="//viewMode=0 and additional">
            <br/>
        </xsl:if>         
       
      
    </xsl:template>

    <xsl:template name="instance_generator">
        <xsl:param name="pathSoFar"></xsl:param>
          
        <xsl:if test="//output/generator!=''">
            <xsl:choose>
                <xsl:when test="instance_generator">
                   
                    <div class="instance" > 
                        <div class="comment_title">&#160;&#160;&#160;Instance_Generator name:</div>
                        <span class="comment_content" data-generator_editable="" data-path="{concat($pathSoFar,'/instance_generator/@name')}">
                            <xsl:value-of select="instance_generator/@name"/>
                        </span>
                        <a  title="Delete Instance_Generator" style="display:inline;float:right;" href="" onclick="confirmDialog();action('Mapping?action=delete&amp;xpath={concat($pathSoFar,'/instance_generator')}&amp;id={//output/id}');return false;">
                            <img src="formating/images/delete16.png"/>
                        </a>   
                        <br/>
                        <xsl:for-each select="instance_generator/arg">
                            <xsl:variable name="argPos" select="position()"/>

                            <div class="comment_title">&#160;&#160;&#160;Argument name:</div>
                            <span class="comment_content" data-generator_editable="" data-path="{concat($pathSoFar,'/instance_generator/arg[',$argPos,']/@name')}">
                                <xsl:value-of select="@name"/>
                            </span>
                            <span class="comment_content"> and value: </span>
                            <span class="comment_content" data-generator_editable="" data-path="{concat($pathSoFar,'/instance_generator/arg[',$argPos,']')}">
                                <xsl:value-of select="."/>
                            </span>
                            <a  title="Delete Arg" style="display:inline;float:right;" href="" onclick="confirmDialog();action('Mapping?action=delete&amp;xpath={concat($pathSoFar,'/instance_generator/arg[',$argPos,']')}&amp;id={//output/id}');return false;">
                            <img src="formating/images/delete16.png"/>
                        </a>   
                            <br/>
                        </xsl:for-each>
                        <a style="display:inline;background-color:lightskyblue;width:100%;"  class="smallerLetters" title="Add Arg" href="" onclick="">
                            <xsl:attribute name="onclick">
                                
                                <xsl:text>action('Mapping?action=add___arg&amp;xpath=</xsl:text>
                                <xsl:value-of select="concat($pathSoFar,'/instance_generator/arg')"/>
                                <xsl:text>&amp;id=</xsl:text>
                                <xsl:value-of select="//output/id"/>
                                <xsl:text>');return false;</xsl:text>
                                  
                            </xsl:attribute>
                            <img src="formating/images/plus16.png"/>
                            <span style="display:inline;">Arg</span>
                            
                
                        </a>
                            
                            
                            
                    </div>
                    
                        
                        
                 
                </xsl:when>
                <xsl:otherwise>
                    <a class="smallerLetters" style="display:inline;" title="Add Instance_Generator" href="">
                        <xsl:attribute name="onclick">
                            <xsl:choose>
                                       
                                <xsl:when test="instance_info">
                                    <xsl:text>action('Mapping?action=addAfter___instance_generator&amp;xpath=</xsl:text>
                                    <xsl:value-of select="concat($pathSoFar,'/instance_info')"/>
                                    <xsl:text>&amp;id=</xsl:text>
                                    <xsl:value-of select="//output/id"/>
                                    <xsl:text>');return false;</xsl:text>
                                </xsl:when>
                               
                                <xsl:otherwise>
                                    <xsl:text>action('Mapping?action=addAfter___instance_generator&amp;xpath=</xsl:text>
                                    <xsl:value-of select="concat($pathSoFar,'/type[last()]')"/>
                                    <xsl:text>&amp;id=</xsl:text>
                                    <xsl:value-of select="//output/id"/>
                                    <xsl:text>');return false;</xsl:text>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:attribute>                             

                        <img src="formating/images/plus16.png"/>
                        <span style="display:inline;">Instance_Generator</span>
                    </a>
                 
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
        
        
    </xsl:template>

</xsl:stylesheet>
