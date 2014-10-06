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
    <xsl:template  match="/" name="intermediate">
        <xsl:param name="pathSoFar"/>
        <xsl:param name="pos3"/>        
        <xsl:if test="//viewMode='0' or relationship!='' or entity/type!=''">

            <div style="float:left;width:90%;" class="Internal_Node">
               
                 <xsl:variable name="entPos" select="number($pos3)-1"/>
                <xsl:if test="//viewMode=0">
                    <div style="height:5px;">
                    <a title="Delete Intermediate" style="float:right;"  class="internalAndconstantNodesLinks" href="" onclick="action('Mapping?action=delete&amp;xpath={concat($pathSoFar,'/relationship[',$pos3,']')}&amp;id={//output/id}');action('Mapping?action=delete&amp;xpath={concat($pathSoFar,'/entity[',$entPos,']')}&amp;id={//output/id}');return false;">
                        <img src="formating/images/delete16.png"/><!--Delete Constant Node-->
                    </a>    
                    </div>
                    <!--br/-->
                </xsl:if>

          
          <xsl:for-each select="entity[position()=$entPos]">
                <xsl:call-template name="entity">                   
                    <xsl:with-param name="pathSoFar" select="concat($pathSoFar,'/entity[',$entPos,']')">
                    </xsl:with-param>
                </xsl:call-template>     
          </xsl:for-each>
          
                         
                <span class="relationship"  data-editable="select" data-path="{concat($pathSoFar,'/relationship[',$pos3,']')}">
                    <xsl:call-template name="substring-after-last-and-remove-prefix">
                        <xsl:with-param name="string" select="relationship[position()=$pos3]" />
                        <xsl:with-param name="delimiter" select="'/'" />
                    </xsl:call-template> 
                </span>
           
                <xsl:if test="//viewMode=0"> 
                    <xsl:call-template name="addAdditional">
                        <xsl:with-param name="pathSoFar" select="concat($pathSoFar,'/entity[',$entPos,']')"/>
                    </xsl:call-template>
                </xsl:if>
           
            </div>
            
        </xsl:if>
        <xsl:if test="//viewMode=0">
            <div title="Add Additional" class="internalAndconstantNodesLinks" href="" >&#160;</div>
        
            <br/>
            <br/>       
            <br/>     
        </xsl:if>          
                                                 
        <!--a class="internalAndconstantNodesLinks" href="" onclick="action('Mapping?action=delete&amp;xpath={concat($pathSoFar,'/internal_node[',$pos3,']')}&amp;id={//output/id}');return false;">Delete Internal Node</a>
        <br/--> 
    </xsl:template>
</xsl:stylesheet>
