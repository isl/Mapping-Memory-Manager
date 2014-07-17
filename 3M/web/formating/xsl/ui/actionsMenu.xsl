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

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>
    <xsl:template name="actions">
        <xsl:variable name="userOrg" select="//page/@userOrg"/>
        <xsl:variable name="user" select="//page/@UserRights"/>
        <xsl:variable name="username" select="//page/@user"/>
        <xsl:variable name="EntityType" select="//context/EntityType"/>
        <xsl:variable name="VocFile" select="//context/VocFile"/>

        <xsl:variable name="DocStatus" select="//context/DocStatus"/>
        <xsl:variable name="TargetCol" select="//context/TargetCol"/>
        <xsl:variable name="root" select="//context/query/Root"/>

        <table border="0" >
            <tr>   
                <xsl:for-each select="//actions/menugroup[menu//actionPerType[@id=$EntityType]/userRights=$user and @id!='Anazitisi' ]">
                    <td class="colMenu">   
                        <xsl:choose>
                            <xsl:when test="count(./menu/submenu)=0">
                                <xsl:for-each select="./menu[actionPerType[@id=$EntityType]/userRights=$user]">

                                    <xsl:call-template name="enableLinks">
                                        <xsl:with-param name="id" select="./actionPerType[@id=$EntityType]/userRights[text()=$user]/@id"/>
                                        <xsl:with-param name="href" select="./actionPerType[@id=$EntityType]/userRights[text()=$user]/@href"/>
                                        <xsl:with-param name="onclick" select="./actionPerType[@id=$EntityType]/userRights[text()=$user]/@onclick"/>
                                        <xsl:with-param name="image" select="string(./@img_src)"/>
                                        <xsl:with-param name="help" select="string(./@help)"/>
                                    </xsl:call-template>               
                                </xsl:for-each>
                            </xsl:when>
                            <xsl:otherwise>  
                                <ul id="sddm">
                                    <li>
                                        <a  href="#" onmouseover="mopen('m{position()}')" onmouseout="mclosetime()">
                                            <xsl:variable name="tag" select="./label"/>
                                            <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                            <xsl:value-of select="$translated"/>&#8595;
                                        </a>
                                        <xsl:variable name="menusNum" select="count(./menu[submenu/actionPerType[@id=$EntityType]/userRights=$user])"/>
                                        <div id="m{position()}"  onmouseover="mcancelclosetime()" onmouseout="mclosetime()">
                                            <xsl:for-each select="./menu[submenu/actionPerType[@id=$EntityType]/userRights=$user]">
                                                <xsl:variable name="tag" select="./label"/>                                        
                                                <xsl:if test="$tag!=''">
                                                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                                    <a class="newMenu" style="color:grey; font-style:italic;"  href="#">
                                                        <xsl:value-of select="$translated"/> 
                                                    </a>
                                                </xsl:if>
                                                <xsl:variable name="image_menu" select="string(./@img_src)"/>                                         
                                                <xsl:for-each select="./submenu[actionPerType[@id=$EntityType]/userRights=$user]">
                                                    <xsl:variable name="image_submenu" select="string(./@img_src)"/>                                         
                                                    <xsl:variable name="help_submenu" select="string(./@help)"/>                                         
                                                    <xsl:variable name="image">
                                                        <xsl:choose>
                                                            <xsl:when test="$image_menu!=''">
                                                                <xsl:value-of select="$image_menu" />
                                                            </xsl:when>
                                                            <xsl:when test="$image_submenu!=''">                                                       
                                                                <xsl:value-of select="$image_submenu"/>
                                                            </xsl:when>                                                     
                                                        </xsl:choose>
                                                    </xsl:variable>
                                                    <xsl:call-template name="enableLinks">
                                                        <xsl:with-param name="id" select="./actionPerType[@id=$EntityType]/userRights[text()=$user]/@id"/>
                                                        <xsl:with-param name="href" select="./actionPerType[@id=$EntityType]/userRights[text()=$user]/@href"/>
                                                        <xsl:with-param name="onclick" select="./actionPerType[@id=$EntityType]/userRights[text()=$user]/@onclick"/>
                                                        <xsl:with-param name="image" select="$image"/>
                                                        <xsl:with-param name="help" select="$help_submenu"/>

                                                    </xsl:call-template>       
                                                </xsl:for-each>
                                                <xsl:if test="position() &lt; $menusNum">
                                                    <hr/>
                                                </xsl:if>
                                            </xsl:for-each>
                                        </div>
                                    </li>
                                </ul>                                         
                            </xsl:otherwise>                               
                        </xsl:choose>
                    </td>
                </xsl:for-each>                
                <td id="firstSearchTd" class="searchDiv newMenu" >
                    <xsl:choose>
                        <xsl:when test="$EntityType='AdminVoc'">
                            <form id="searchForm" method="post" action="AdminVoc?action=search&amp;file={$VocFile}&amp;menuId=AdminVoc">
                                <xsl:variable name="tag" select=" 'Anazitisi' "/>
                                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                <xsl:value-of select="$translated"/>
                                <input type="image" src="{concat($systemRoot,'/formating/images/searchdoc.png')}"/>
                                <input class="text" type="text" width='10' height='1' name="inputvalue"/>
                            </form>
                        </xsl:when>
                        <xsl:when test="$EntityType='AdminVocTrans'">
                            <form id="searchForm" method="post" action="AdminVoc?action=search_trans&amp;file={$VocFile}&amp;menuId=AdminVocTrans">
                                <xsl:variable name="tag" select=" 'Anazitisi' "/>
                                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                <xsl:value-of select="$translated"/>
                                <input type="image" src="{concat($systemRoot,'/formating/images/searchdoc.png')}"/>
                                <input class="text" type="text" width='10' height='1' name="inputvalue"/>
                            </form>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:if test="$EntityType!='AdminOrg' and $EntityType!='AdminUser' and $EntityType!='Backup'">    
                                <form id="searchForm" method="post" action="SearchResults?style=simpleSearch">
                                    <xsl:variable name="tag" select=" 'Anazitisi' "/>
                                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                    <xsl:value-of select="$translated"/>
                                    <input type="image" src="{concat($systemRoot,'/formating/images/searchdoc.png')}"/>
                                    <input class="text" type="text" width='10' height='1' name="inputvalue"/>
                                    <input type="hidden" name="category" value="{$EntityType}"/>
                                    <input type="hidden" name="status" value="all"/>
                                    <input type="hidden" name="target" value="{$TargetCol}"/>
                                    <input type="hidden" name="input" value="{$root}"/>
                                    <input type="hidden" name="inputoper" value="contains"/>
                                    <input type="hidden" name="inputparameter" value="1"/>
                                    <input type="hidden" id="inputid" name="inputid" value="1"/>
                                </form>
                            </xsl:if>
                        </xsl:otherwise>
                    </xsl:choose>
                </td>
                <xsl:for-each select="//actions/menugroup[menu//actionPerType[@id=$EntityType]/userRights=$user and @id='Anazitisi' ]">
                    <td  class="searchDiv newMenu" >
                        <ul id="sddm">
                            <li>
                                <a href="#" onmouseover="mopen('search')" onmouseout="mclosetime()">
                                    <xsl:variable name="image" select="string(./@img_src)"/>    
                                    <img src="{concat($systemRoot,$image)}"/>                            
                                </a>
                                <div id="search" style=" margin-left:-115px;" onmouseover="mcancelclosetime()" onmouseout="mclosetime()">
                                    <xsl:for-each select="./menu[submenu/actionPerType[@id=$EntityType]/userRights=$user]">
                                        <xsl:variable name="tag" select="./label"/>
                                        <xsl:if test="$tag!=''">
                                            <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                            <xsl:value-of select="$translated"/> 
                                        </xsl:if>                                
                                        <xsl:variable name="image" select="string(./@img_src)"/>
                                        <xsl:variable name="help" select="string(./@help)"/>                           
                                        <xsl:for-each select="./submenu[actionPerType[@id=$EntityType]/userRights=$user]">
                                            <xsl:call-template name="enableLinks">
                                                <xsl:with-param name="id" select="./actionPerType[@id=$EntityType]/userRights[text()=$user]/@id"/>
                                                <xsl:with-param name="href" select="./actionPerType[@id=$EntityType]/userRights[text()=$user]/@href"/>
                                                <xsl:with-param name="onclick" select="./actionPerType[@id=$EntityType]/userRights[text()=$user]/@onclick"/>
                                                <xsl:with-param name="image" select="$image"/>
                                                <xsl:with-param name="help" select="$help"/>

                                            </xsl:call-template>                                                     
                                        </xsl:for-each>                               
                                    </xsl:for-each>
                                </div>
                            </li>
                        </ul>
                    </td>
                </xsl:for-each>                      
                <script type="text/javascript">                
                    
                    function enableLink($current,$item,id,file){
                    var lang = '<xsl:value-of select="$lang"/>';
                    var onclick= $item.getAttribute("onclick");
                    var hasID=$item.getAttribute('id');
                    var hasLang = (hasID.indexOf("lang=")>-1);
                    if(onclick!=null){
                    onclick=hasID;
                    if(hasID.indexOf("id=")!=-1){                                
                    onclick=hasID.replace("id=","id="+id);                                  
                    }
                        
                    if(hasLang){
                    onclick=onclick.replace("lang=","lang="+lang); 
                    }                                   
                    if(file!=""){
                    onclick=onclick.replace("file=","file="+file)
                    }
                    $current.attr('href',"javascript:void(0)");
                    $item.onclick=function(){
                    popUp(onclick, id, 900, 700);
                    }
                    }

                    var href= $item.getAttribute('href'); 
                    if(href!=null &amp;&amp; onclick==null){
                    if(hasID!=""){
                    href=hasID;
                    var hasLang = (href.indexOf("lang=")>-1);
                    if(href.indexOf("id=")!=-1){                                
                    href=href.replace("id=","id="+id);                                   
                    }
                                
                    if(hasLang){
                    href=href.replace("lang=","lang="+lang); 
                    } 
                    if(file!=""){
                    href=href.replace("file=","file="+file);                                       
                    }
                    }
                    $current.attr('href',href);
                    }
                    }

                    $(function() {
                    var lang = '<xsl:value-of select="$lang"/>';
                    var tr = $('#results').find('tr.resultRow');
                    var file=document.getElementById('fileType');
                    var fileName="";
                          
                    if(file!=null)
                    fileName=file.innerHTML;    
                    var a=$('.colMenu').find('a');
                    $.each(a, function(index, item) {   
                    var curr=$(this);                                       
                    var href=item.getAttribute('href');  
                    var hasID=item.getAttribute('id');
                    var hasLang = (href.indexOf("lang=")>-1);
                    if(hasLang){
                    href=href.replace("lang=","lang="+lang); 
                    } 
                    if(href!="" &amp;&amp; (hasID=="" || hasID==null)){
                    href=href.replace("file=","file="+fileName);  
                    item.setAttribute("href",href);
                    }
                    });
                    tr.bind('click', function(event) {
                    var values = '';
                    tr.removeClass('highlighted_resultRow');                             
                    $(this).addClass('highlighted_resultRow'); 
                    var tds = $(this).find('td');
                    var id=tds[0].innerHTML;                                      
                    $.each(a, function(index, item) {   
                    var curr=$(this);                                       
                    var hasID=item.getAttribute('id');
                                    
                    if(hasID!=null){                                      
                    enableLink(curr,item,id,fileName);
                    }
                    });

                    });
                    });                   
                </script>
            </tr>
        </table>
    </xsl:template>
    <xsl:template name="enableLinks" match="/">
        <xsl:param name="href" />
        <xsl:param name="onclick" />
        <xsl:param name="image" />
        <xsl:param name="id" />
        <xsl:param name="help" />
      
        <xsl:variable name="EntityType" select="//context/EntityType"/>        
        <xsl:variable name="translated" select="$locale/context/*[name()=$help]/*[name()=$lang]"/>
        <a class="newMenu" title="{$translated}">
            <xsl:if test="count($id)>0">
                <xsl:attribute name="id">
                    <xsl:value-of select="string($id)"/>                  
                </xsl:attribute>
            </xsl:if>
            <xsl:if test="count($href)>0">
                <xsl:attribute name="href">
                    <xsl:value-of select="string($href)"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:if test="count($onclick)>0">
                <xsl:attribute name="onclick">
                    <xsl:value-of select="string($onclick)"/>
                </xsl:attribute>
            </xsl:if>                                
            <xsl:variable name="tag" select="./label"/>
            <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
            <xsl:value-of select="$translated"/>
            <xsl:if test="$image!=''">
                <xsl:text> </xsl:text>
                <img src="{concat($systemRoot,$image)}"/>
            </xsl:if>
        </a>
    </xsl:template>
</xsl:stylesheet>