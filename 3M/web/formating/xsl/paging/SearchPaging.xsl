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

This file is part of the 3M webapp of Mapping Memory Manager project.

-->

<xsl:stylesheet xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xdt="http://www.w3.org/2005/02/xpath-datatypes" xmlns:fn="http://www.w3.org/2005/02/xpath-functions" version="2.0">
    <xsl:template name="SearchPaging">
        <xsl:param name="url"/>
        <xsl:param name="queryPages"/>
        <xsl:param name="currentP"/>
        <xsl:param name="pageLoop"/>
        <xsl:param name="showPages"/>
        <xsl:param name="selected"/>

        <table align="center">
            <tr class="paging">
				<!--<td class="paging">-->
				
                <xsl:if test="$currentP != 1">
                    <xsl:if test="$queryPages > 2">
                        <td class="paging" valign="bottom">	
                            <a href="javascript:submitSearchPagingForm('move','first');">
                                <xsl:choose>
                                    <xsl:when test="$lang='ar'">
                                        <xsl:variable name="tag" select=" 'FirstPage' "/>
                                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                        <img src="formating/images/end.gif" title='{$translated}' alt="First" onClick="" style="border:0;margin:3px"/>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:variable name="tag" select=" 'FirstPage' "/>
                                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                        <img src="formating/images/start.gif" title='{$translated}' alt="First" style="border:0;margin:3px" onClick="" />
                                    </xsl:otherwise>
                                </xsl:choose>
                            </a>
                        </td>
                    </xsl:if>
                    <td class="paging" valign="bottom">	
                        <a href="javascript:submitSearchPagingForm('move','prev');">
                            <xsl:choose>
                                <xsl:when test="$lang='ar'">
                                    <xsl:variable name="tag" select=" 'PrevPage' "/>
                                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                    <img src="formating/images/next.gif" title='{$translated}' alt="Prev" onClick="" style="border:0;margin:3px"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:variable name="tag" select=" 'PrevPage' "/>
                                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                    <img src="formating/images/prev.gif" title='{$translated}' alt="Prev" style="border:0;margin:3px" onClick="" />
                                </xsl:otherwise>
                            </xsl:choose>
                        </a>
                    </td>
                    <td class="paging" valign="middle">	
                        <xsl:choose>
                            <xsl:when test="$showPages = 1">
					
                            </xsl:when>
                            <xsl:otherwise>
                                <b>...</b>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                </xsl:if>
                <td class="paging" valign="middle">	
                    <xsl:for-each select="$showPages">
                        <xsl:if test=".!= $currentP">
                            <a class="color1" href="javascript:submitSearchPagingForm('newP','{.}');">
                                <xsl:value-of select="."/>
                            </a>
                        </xsl:if>
                        <xsl:if test=". = $currentP">
                            <b>
                                <span class="color2">
                                    <xsl:value-of select="."/>
                                </span>
                            </b>
                        </xsl:if>
                        <xsl:if test="position() != last()">
                            <b>..</b>
                        </xsl:if>
                    </xsl:for-each>
                </td>
		
                <xsl:if test="$currentP!= $queryPages">
                    <xsl:if test="count($showPages) != $queryPages">
                        <td class="paging" valign="middle">	
                            <b>...</b>
                        </td>
                    </xsl:if>
                    <td class="paging" valign="bottom">	
		
                        <a href="javascript:submitSearchPagingForm('move','next');">
                            <xsl:choose>
                                <xsl:when test="$lang='ar'">
                                    <xsl:variable name="tag" select=" 'NextPage' "/>
                                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                    <img src="formating/images/prev.gif" title='{$translated}' alt="Next" style="border:0;margin:3px" onClick="" />
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:variable name="tag" select=" 'NextPage' "/>
                                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                    <img src="formating/images/next.gif" title='{$translated}' alt="Next" onClick="" style="border:0;margin:3px"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </a>
                    </td>	
                    <xsl:if test="$queryPages > 2">	
                        <td class="paging" valign="bottom">	
                            <a href="javascript:submitSearchPagingForm('move','last');">
                                <xsl:choose>
                                    <xsl:when test="$lang='ar'">
                                        <xsl:variable name="tag" select=" 'LastPage' "/>
                                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                        <img src="formating/images/start.gif" title='{$translated}' alt="Next" style="border:0;margin:3px" onClick="" />
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:variable name="tag" select=" 'LastPage' "/>
                                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                        <img src="formating/images/end.gif" title='{$translated}' alt="Next" onClick="" style="border:0;margin:3px"/>
                                    </xsl:otherwise>
                                </xsl:choose>
				
                            </a>
                        </td>
                    </xsl:if>
                </xsl:if>
		
                <td class="pagingnew">&#160;&#160;&#160;
                    <xsl:variable name="tag" select=" 'Selida' "/>
                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                    <xsl:value-of select="$translated"/>
                </td>
                <td class="pagingnew">
                    <input name="{$selected}" id="{$selected}" value="{$currentP}" type="text" style="width:25px;height:19px;" maxLength="4"   onkeypress="goToSearchPage(this.value,'{$queryPages}');" >
                    </input>/
                    <xsl:value-of select="$queryPages"/>
                </td>
                <td class="paging">
                    <a href="javascript:goToPage('{$selected}','{$queryPages}');">
                        <xsl:variable name="tag" select=" 'Go' "/>
                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                        <img src="formating/images/goarrow2.gif" title='{$translated}' alt="go" style="border:0;margin:3px"/>
                    </a>
                </td>
            </tr>
        </table>
    </xsl:template>
</xsl:stylesheet>