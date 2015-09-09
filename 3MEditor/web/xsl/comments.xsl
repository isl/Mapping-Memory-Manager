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
       
         <xsl:variable name="commentsText">
             <xsl:for-each select="comment">
             &lt;b&gt;Rationale:&lt;/b&gt; <xsl:value-of select="rationale"/>
             &lt;br/&gt;
                          &lt;b&gt;Alternatives:&lt;/b&gt; <xsl:value-of select="alternatives"/>
                                       &lt;br/&gt;

             &lt;b&gt;Typical Mistakes:&lt;/b&gt; <xsl:value-of select="typical_mistakes"/>
                          &lt;br/&gt;

             &lt;b&gt;Local Habits:&lt;/b&gt; <xsl:value-of select="local_habits"/>
                          &lt;br/&gt;
 &lt;b&gt;Link to Cook Book:&lt;/b&gt; <xsl:value-of select="link_to_cook_book"/>
                          &lt;br/&gt;
                           &lt;b&gt;Example Source:&lt;/b&gt; <xsl:value-of select="example/example_source"/>
                          &lt;br/&gt;
                           &lt;b&gt;Example Target:&lt;/b&gt; <xsl:value-of select="example/example_target"/>
                          &lt;br/&gt;
                           &lt;b&gt;Comments Last Update Person:&lt;/b&gt; <xsl:value-of select="comments_last_update/@person"/>
                          &lt;br/&gt;
                             &lt;b&gt;Comments Last Update Date:&lt;/b&gt; <xsl:value-of select="comments_last_update/@date"/>
                          &lt;br/&gt;
             </xsl:for-each>
         </xsl:variable>
                        <a class="description" tabindex="0"
                           title="Comments" data-trigger="focus" data-toggle="popover" data-content="{$commentsText}"  data-html="true">
                            <span style="display:inline;" class="small">
                                (<xsl:value-of select="substring(comment//*/text(), 1, 30)"/>..)
                            </span>
                        </a>                         
                       
        
<!-- <xsl:choose>
                    <xsl:when test="../instance_info/description">
                        &#160;
                      
                        <xsl:variable name="description" select="../instance_info/description"/>
                        <a class="description" tabindex="0"
                           title="Description" data-trigger="focus" data-toggle="popover" data-content="{$description}">
                            <span style="display:inline;" class="small">(Description...)</span>
                        </a>                         
                       
                                         
                    </xsl:when>
                    <xsl:otherwise>&#160;
                      
                        
         </xsl:otherwise>
     </xsl:choose>
        -->
    </xsl:template>

</xsl:stylesheet>
