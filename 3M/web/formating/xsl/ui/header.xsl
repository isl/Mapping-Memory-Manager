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
    <xsl:template name="header">
        <xsl:param name="logo"/>
        <xsl:param name="name"/>
        <xsl:param name="user"/>
        <xsl:variable name="tag" select=" 'User_Manual' "/>
        <xsl:variable name="User_Manual" select="$locale/topmenu/*[name()=$tag]/*[name()=$lang]"/>  
        <xsl:variable name="tag" select=" 'Guest_Manual' "/>
        <xsl:variable name="Guest_Manual" select="$locale/topmenu/*[name()=$tag]/*[name()=$lang]"/> 
        <xsl:variable name="tag" select=" 'Admin_Manual' "/>
        <xsl:variable name="Admin_Manual" select="$locale/topmenu/*[name()=$tag]/*[name()=$lang]"/> 
        <xsl:variable name="tag" select=" 'SysAdmin_Manual' "/>
        <xsl:variable name="SysAdmin_Manual" select="$locale/topmenu/*[name()=$tag]/*[name()=$lang]"/>
        <script>
            var language='<xsl:value-of select="$lang"/>';
            var LanguageOfManual = '';			
            if (language=='it' || language=='ar'){
            LanguageOfManual='en';
            }else{
            LanguageOfManual= '<xsl:value-of select="$lang"/>';
            }
        </script>
        <tr>
            <td width="18%" class="headerLogo" align="center">
                <img src="{ concat($systemRoot,  'formating/images/layout_header_left.jpg') }"></img>
            </td>
            <td width="82%" class="headerLogo" align="left" colspan="{$columns}">
                <img  src="{ concat($systemRoot,  'formating/images/layout_header_right_', $lang, '.jpg') }">
                </img>
          
                <xsl:choose>                   
                    <xsl:when test="$user!=''">
                        <xsl:variable name="topmenus" select="//topmenu/menugroup/menu"/>
                        <xsl:variable name="image" select="string(//topmenu/menugroup/@img_src)"/>
                   
                        <div style="position:absolute;z-index:10;top: 60px;left:890px;">
                            <ul id="sddm">
                                <li>              
                                    <a style="white-space:nowrap; color:white;" onmouseout="mclosetime()" href="#" onmouseover="mopen('topMenu')" >
                                        <img src="{concat($systemRoot,'/formating/images/user.png')}"/>
                                        <xsl:text> </xsl:text>
                                        <xsl:value-of select="$user"/>
                                        <xsl:text> </xsl:text>
                                        <img  src="{concat($systemRoot,$image)}"/>                            
                                    </a>
                                    <div id="topMenu"    onmouseover="mcancelclosetime()" onmouseout="mclosetime()">
                                        <xsl:for-each select="$topmenus">
                                            <xsl:variable name="langArg">
                                                <xsl:choose>
                                                    <xsl:when test="./@id='BugReport'">
                                                        <xsl:choose>
                                                            <xsl:when test="$lang='ar' or $lang='it' or $lang='fr'or $lang='sv'">
                                                                <xsl:text>&amp;language=en&amp;username=</xsl:text>
                                                                <xsl:value-of select="$user"/>
                                                            </xsl:when>
                                                            <xsl:otherwise>
                                                                <xsl:text>&amp;language=</xsl:text>
                                                                <xsl:value-of select="$lang"/>
                                                                <xsl:text>&amp;username=</xsl:text>
                                                                <xsl:value-of select="$user"/>
                                                            </xsl:otherwise>
                                                        </xsl:choose>
                                                    </xsl:when>
                                                    <!--plays with manual of roles(admin/efitor/guest/sysadmin) -->
                                                    <!--xsl:when test="./@id='Help'">
                                                        <xsl:choose>
                                                            <xsl:when test="$lang='ar' or $lang='it'"> 
                                                               <xsl:text>_en.html</xsl:text>
                                                            </xsl:when>
                                                            <xsl:otherwise>
                                                                <xsl:text>_</xsl:text>
                                                                <xsl:value-of select="$lang"/>
                                                                <xsl:text>.html</xsl:text>
                                                            </xsl:otherwise>
                                                        </xsl:choose>
                                                    </xsl:when-->
                                                       <xsl:when test="./@id='Help'">
                                                          <xsl:attribute name="target">_blank</xsl:attribute>
                                                        <xsl:choose>
                                                            <xsl:when test="$lang='gr' or $lang='en'">
                                                                <xsl:value-of select="$lang"/> 
                                                                <xsl:text>/manual.pdf</xsl:text>
                                                            </xsl:when>
                                                            <xsl:otherwise>
                                                                <xsl:value-of select="$lang"/>
                                                                <xsl:text>/manual.html</xsl:text>
                                                            </xsl:otherwise>
                                                        </xsl:choose>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                        <xsl:value-of select="''"/> 
                                                    </xsl:otherwise>
                                                </xsl:choose>
                                            </xsl:variable>                                        
                                            <a class="newMenu">
                                            <!--plays with manual of roles(admin/efitor/guest/sysadmin) -->
                                                <!--xsl:if test="./@id!='Help'"-->
                                                    <xsl:if test="./@id='BugReport'">
                                                        <xsl:attribute name="target">_blank</xsl:attribute>
                                                    </xsl:if>
                                                    <xsl:attribute name="href">
                                                        <xsl:value-of select="./@href"/>
                                                        <xsl:value-of select="$langArg"/>
                                                    </xsl:attribute>
                                                <!--/xsl:if-->
                                                <xsl:if test="./@id='Help'">                                                 
                                                    <xsl:attribute name="onmouseover">
                                                        <xsl:value-of select="./@onmouseover"/>
                                                    </xsl:attribute>
                                                </xsl:if>
                                                <xsl:variable name="tag" select="."/>
                                                <xsl:variable name="translated" select="$locale/topmenu/*[name()=$tag]/*[name()=$lang]"/>
                                                <xsl:value-of select="$translated"/>  
                                            </a>
                                            <!--plays with manual of roles(admin/efitor/guest/sysadmin) -->
                                            <!--xsl:if test="./@id='Help'"> 
                                                <xsl:if test=" $UserRights = 'admin' ">                                                            
                                                    <a  style="display:none;" class="help">
                                                        <xsl:attribute name="onclick">javascript:window.open('Manuals/'+LanguageOfManual+'/manual_anagnwsth/manual.html')</xsl:attribute>
                                                        <xsl:attribute name="href">javascript:void(0)</xsl:attribute>
                                                        -
                                                        <xsl:value-of select="$Guest_Manual"/>                            
                                                    </a>
                                                    <a style="display:none;"  class="help ">
                                                        <xsl:attribute name="onclick">javascript:window.open('Manuals/'+LanguageOfManual+'/manual_syntakth/manual.html')</xsl:attribute>
                                                        <xsl:attribute name="href">javascript:void(0)</xsl:attribute>
                                                        -
                                                        <xsl:value-of select="$User_Manual"/>                            
                                                    </a>
                                                    <a style="display:none;" class="help">
                                                        <xsl:attribute name="onclick">javascript:window.open('Manuals/'+LanguageOfManual+'/manual_diax_forea/manual.html')</xsl:attribute>
                                                        <xsl:attribute name="href">javascript:void(0)</xsl:attribute>
                                                        -
                                                        <xsl:value-of select="$Admin_Manual"/>                            
                                                    </a>
                                                </xsl:if>
                                                <xsl:if test="$UserRights = 'editor' "> 
                                                    <a style="display:none;" class="help">
                                                        <xsl:attribute name="onclick">javascript:window.open('Manuals/'+LanguageOfManual+'/manual_anagnwsth/manual.html')</xsl:attribute>
                                                        <xsl:attribute name="href">javascript:void(0)</xsl:attribute>
                                                        -
                                                        <xsl:value-of select="$Guest_Manual"/>                            
                                                    </a>
                                                    <a style="display:none;" class="help">
                                                        <xsl:attribute name="onclick">javascript:window.open('Manuals/'+LanguageOfManual+'/manual_syntakth/manual.html')</xsl:attribute>
                                                        <xsl:attribute name="href">javascript:void(0)</xsl:attribute>
                                                        -
                                                        <xsl:value-of select="$User_Manual"/>                            
                                                    </a> 
                                                </xsl:if>
                                                <xsl:if test=" $UserRights = 'guest' "> 
                                                    <a style="display:none;" class="help">
                                                        <xsl:attribute name="onclick">javascript:window.open('Manuals/'+LanguageOfManual+'/manual_anagnwsth/manual.html')</xsl:attribute>
                                                        <xsl:attribute name="href">javascript:void(0)</xsl:attribute>
                                                        -
                                                        <xsl:value-of select="$Guest_Manual"/>                            
                                                    </a>
                                                </xsl:if>
                                                <xsl:if test=" $UserRights = 'sysadmin' "> 
                                                    <a style="display:none;" class="help">
                                                        <xsl:attribute name="onclick">javascript:window.open('Manuals/'+LanguageOfManual+'/manual_anagnwsth/manual.html')</xsl:attribute>
                                                        <xsl:attribute name="href">javascript:void(0)</xsl:attribute>
                                                        -
                                                        <xsl:value-of select="$Guest_Manual"/>                            
                                                    </a>
                                                    <a style="display:none;" class="help">
                                                        <xsl:attribute name="onclick">javascript:window.open('Manuals/'+LanguageOfManual+'/manual_syntakth/manual.html')</xsl:attribute>
                                                        <xsl:attribute name="href">javascript:void(0)</xsl:attribute>
                                                        -
                                                        <xsl:value-of select="$User_Manual"/>                            
                                                    </a> 
                                                    <a style="display:none;" class="help">
                                                        <xsl:attribute name="onclick">javascript:window.open('Manuals/'+LanguageOfManual+'/manual_diax_forea/manual.html')</xsl:attribute>
                                                        <xsl:attribute name="href">javascript:void(0)</xsl:attribute>
                                                        -
                                                        <xsl:value-of select="$Admin_Manual"/>                            
                                                    </a>
                                                    <a style="display:none;" class="help">
                                                        <xsl:attribute name="onclick">javascript:window.open('Manuals/'+LanguageOfManual+'/manual_diax_syst/manual.html')</xsl:attribute>
                                                        <xsl:attribute name="href">javascript:void(0)</xsl:attribute>
                                                        -
                                                        <xsl:value-of select="$SysAdmin_Manual"/>                            
                                                    </a>  
                                                </xsl:if>                                            
                                            </xsl:if-->
                                            
                                        </xsl:for-each>
                                    </div>
                                </li>
                            </ul>                             
                        </div>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:variable name="topmenus" select="//topmenu/menugroup/menu"/>
                        <xsl:variable name="image" select="string(//topmenu/menugroup/@img_src)"/>
                        <xsl:if test="count(//context/Langs/Lang) &gt; 1">
                        <div style="position:absolute;z-index:10;top: 70px;left:900px;">                            
                            <ul id="sddm">
                                <li>              
                                    <a style="white-space:nowrap; color:white;" onmouseout="mclosetime()" href="#" onmouseover="mopen('topMenu')" >
                                        <xsl:variable name="tag" select=" $lang "/>
                                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                        <xsl:value-of select="$translated"/>
                                        (<xsl:value-of select="$lang"/>)<xsl:text> </xsl:text>
                                        <img  src="{concat($systemRoot,'formating/images/arrowDownWhite.png')}"/>                            
                                    </a>
                                    <div id="topMenu"    onmouseover="mcancelclosetime()" onmouseout="mclosetime()">
                                    <xsl:for-each select="//context/Langs/Lang">
                                        <a class="newMenu" href="SetLanguage?lang={./text()}">
                                            <xsl:variable name="tag" select="./text()"/>
                                            <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$tag]"/>
                                            <xsl:value-of select="$translated"/> (<xsl:value-of select="./text()"/>)
                                        </a>
                                    </xsl:for-each>                                                         
                                    </div>
                                </li>
                            </ul>                             
                        </div>
                        </xsl:if>
                    </xsl:otherwise>
                  
                </xsl:choose>

            </td>            
        </tr>
    </xsl:template>
</xsl:stylesheet>