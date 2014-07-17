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
    <xsl:template name="footer">
        <xsl:param name="text"/>
        <tr align="right" class="footer">
            <span><td align="left">
                    <hr/>
                    <!--dn deixnume to xrhsh katw aistero sto footer-->
                    <!--br/-->
                    <!--xsl:choose>
                        <xsl:when test=" $user != '' ">	
                            <xsl:value-of select="$locale/topmenu/user/*[name()=$lang]"/>:
                            <xsl:value-of select="$user"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <br/>
                        </xsl:otherwise>
                    </xsl:choose-->
                      <br/>
            </td></span>
               <td colspan="{$columns}"  style="direction:ltr;" align="center">
     
                <hr/>
              <xsl:variable name="systemName" select="//context/systemName/text()"/>
                            <xsl:variable name="systemVersion" select="//context/systemVersion/text()"/>
                            <xsl:variable name="systemAcronymDescription" select="//context/systemAcronymDescription/text()"/>
                <a onmouseover="javascript:this.T_ABOVE = false;this.T_OFFSETY='-20';this.T_BGCOLOR='white';this.T_FONTSIZE='8pt';this.T_FONTFACE='verdana';return escape('&lt;b&gt;{$systemName}&amp;nbsp;{$systemVersion}&lt;br/&gt; 
                                   {$systemAcronymDescription}&lt;br/&gt;
                                    Copyright © FORTH-ICS. All rights reserved
                                &lt;b/&gt;');"  ><xsl:value-of select="$systemName" disable-output-escaping="yes"/></a>
                                <xsl:text> </xsl:text>
                                <xsl:value-of select="$locale/footer/text2/*[name()=$lang]" />
                <a href="http://www.ics.forth.gr/isl/"><xsl:value-of select="$locale/footer/text3/*[name()=$lang]" /></a>
                <xsl:value-of select="$locale/footer/text4/*[name()=$lang]" />
            </td>
        </tr>
        <script language="JavaScript">
            <xsl:attribute name="src"><xsl:value-of select="concat($systemRoot,'/formating/javascript/footer/wz_tooltip.js')"/></xsl:attribute>
        </script>
    </xsl:template>
</xsl:stylesheet>