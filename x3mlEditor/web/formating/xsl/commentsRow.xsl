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
    <xsl:template name="commentsRow" match="/">
        
        <xsl:param name="pathSoFar"/>
            
         
        <tr class="comments" style="display:none;">
            <td colspan="5">
                <div >
                    <table border="0" cellpadding="0">
                        <tr>
                            <td>
                                <div class="comment_title">Rationale</div>
                            </td>
                            <td >
                                <div class="comment_content" data-editable="textarea" data-path="{concat($pathSoFar,'/rationale')}">
                                    <xsl:value-of select="rationale"/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div class="comment_title">Alternatives</div>
                            </td>
                            <td>
                                <div class="comment_content" data-editable="textarea" data-path="{concat($pathSoFar,'/alternatives')}">
                                    <xsl:value-of select="alternatives"/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div class="comment_title">Typical Mistakes</div>
                            </td>
                            <td>
                                <div class="comment_content" data-editable="textarea" data-path="{concat($pathSoFar,'/typical_mistakes')}">
                                    <xsl:value-of select="typical_mistakes"/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div class="comment_title">Local Habits</div>
                            </td>
                            <td>
                                <div class="comment_content" data-editable="textarea" data-path="{concat($pathSoFar,'/local_habits')}">
                                    <xsl:value-of select="local_habits"/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div class="comment_title">Link to Cook Book</div>
                            </td>
                            <td>
                                <div class="comment_content" data-editable="textarea" data-path="{concat($pathSoFar,'/link_to_cook_book')}">
                                    <xsl:value-of select="link_to_cook_book"/>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top">
                                <div class="comment_title">Example</div>
                            </td>
                           
                            <td>
                                <div class="comment_title">source:&#160;</div>
                                <div class="example_comment"  data-editable="textarea" data-path="{concat($pathSoFar,'/example/example_source')}">
                                    <xsl:value-of select="example/example_source"/>
                                </div>
                                <div class="comment_title">
                                    <br/>target:&#160;
                                </div>
                                <div class="example_comment" data-editable="textarea" data-path="{concat($pathSoFar,'/example/example_target')}">
                                    <xsl:value-of select="example/example_target"/>
                                </div>
                            </td>
                            
                        </tr>
                      

                        <tr>
                            <td>
                                <div class="Value_Binding">&#160;&#160;&#160; comments last update:</div>
                            </td>
                            <td>
                                <div class="Value_Binding">
                                    <span data-editable="textarea" data-path="{concat($pathSoFar,'/comments_last_update/@person')}">
                                        <xsl:value-of select="comments_last_update/@person"/>
                                    </span>&#160;&#160; 
                                    <span data-editable="textarea" data-path="{concat($pathSoFar,'/comments_last_update/@date')}">
                                        <xsl:value-of select="comments_last_update/@date"/>
                                    </span>
                                </div>
                            </td>
                        </tr>
                       
                    </table>
                    <!--
                    <a title="Delete Comments" style="float:right;" href="" onclick="confirmDialog();action('Mapping?action=delete&amp;xpath={$pathSoFar}&amp;id={//output/id}');return false;">
                        <img src="formating/images/delete16.png"/>
                    </a>                                        
-->
                </div>
            </td>
                       
        </tr>
    </xsl:template>

</xsl:stylesheet>
