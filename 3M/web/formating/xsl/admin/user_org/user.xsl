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

<xsl:stylesheet xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xdt="http://www.w3.org/2005/02/xpath-datatypes" xmlns:fn="http://www.w3.org/2005/02/xpath-functions" version="2.0">
    <xsl:output method="html" indent="yes" encoding="UTF-8"/>
    <xsl:include href="../../ui/page.xsl"/>

    <xsl:variable name="ErrorMsg" select="//context/ErrorMsg"/>
	
    <xsl:variable name="AdminAction" select="//context/AdminAction"/>
    <xsl:variable name="AdminMode" select="//context/AdminMode"/>
	
    <xsl:variable name="Organization" select="//context/result/Organization/Organization"/>
    <xsl:variable name="Id" select="//context/result/Id/Id"/>
    <xsl:variable name="Name" select="//context/result/Name/Name"/>
    <xsl:variable name="LastName" select="//context/result/LastName/lastname"/> <!-- einai mikra ta teleutaia epeidh etsi einai sto arxei DMSUsers.xml -->
    <xsl:variable name="FirstName" select="//context/result/FirstName/firstname"/> <!-- einai mikra ta teleutaia epeidh etsi einai sto arxei DMSUsers.xml -->
    <xsl:variable name="Address" select="//context/result/Address/address"/> <!-- einai mikra ta teleutaia epeidh etsi einai sto arxei DMSUsers.xml -->
    <xsl:variable name="Email" select="//context/result/Email/email"/> <!-- einai mikra ta teleutaia epeidh etsi einai sto arxei DMSUsers.xml -->
    <xsl:variable name="OrgId" select="//context/result/Group/group"/> <!-- einai mikra ta teleutaia epeidh etsi einai sto arxei DMSUsers.xml -->
    <xsl:variable name="Role" select="//context/result/Actions/Actions"/>
    <xsl:variable name="emailMsg" select="$locale/context/NotValidEmail/*[name()=$lang]"/>
    <xsl:variable name="notEmptyFields" select="$locale/context/notEmptyFields/*[name()=$lang]"/>

    <xsl:template match="/">
        <xsl:call-template name="page"/>
    </xsl:template>
    <xsl:template name="context">
        <td colSpan="{$columns}" vAlign="top" class="content">
            <br/>
            <br/>
            <form id="userForm" method="post" action="AdminUser?action={$AdminAction}&amp;mode={$AdminMode}" style="margin-bottom:0px;" onsubmit="return validateAll();">
                <table width="100%" class="contentText">
                    <tr>
                        <xsl:variable name="tag" select=" 'username' "/>
                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/> 
                        <td width="25%">
                            <xsl:value-of select="$translated"/>*
                        </td>
                        <td>
                            <input class="mandatary" type="text" id="username" name="username" style="width:150px" value="{$Name}"></input>
                            <input type="hidden" id="id" name="id" value="{$Id}"></input>
                        </td>
                    </tr>
                    <tr>
                        <xsl:variable name="tag" select=" 'password' "/>
                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                        <td>
                            <xsl:value-of select="$translated"/>*
                        </td>
                        <td>
                            <input class="mandatary" type="password" name="password" style="width:150px"></input>
                        </td>
                    </tr>
                    <tr>
                        <xsl:variable name="tag" select=" 'passwordV' "/>
                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                        <td>
                            <xsl:value-of select="$translated"/>*
                        </td>
                        <td>
                            <input class="mandatary" type="password" name="passwordV" style="width:150px"></input>
                        </td>
                    </tr>
                 
                    <xsl:choose>
                        <xsl:when test="$Role='sysadmin'">
                        </xsl:when>
                        <xsl:when test="$AdminMode='' and $Role='admin'">
                        </xsl:when>
                        <xsl:otherwise>
                            <tr>
                                <xsl:variable name="tag" select=" 'orgname' "/>
                                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                <td width="25%" >
                                    <xsl:value-of select="$translated"/>
                                </td>
                                <td>
                                    <xsl:variable name="tag" select=" 'AdminOrganization' "/>
                                    <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                    <select name="orgId" id="orgId" style="width:150px">
                                        <option value="0">----------
                                            <xsl:value-of select="$translated"/>----------
                                        </option>                                       
                                        <xsl:for-each select="//context/groups/group">
                                            <option value="{./id}">                                             
                                                <xsl:if test=" ./id = $OrgId ">   
                                                    <xsl:attribute name="selected">selected</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="./name"/>
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <xsl:variable name="tag" select=" 'userrole' "/>
                                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                <td>
                                    <xsl:value-of select="$translated"/>
                                </td>
                                <td>
                                    <select name="userrole" id="userrole" style="width:150px">
                                        <xsl:variable name="tag" select=" 'sintaktis' "/>
                                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                        <option value="editor">
                                            <xsl:value-of select="$translated"/>
                                        </option>
                                        <xsl:variable name="tag" select=" 'episkeptis' "/>
                                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                        <option value="guest">
                                            <xsl:if test=" $Role = 'guest' ">
                                                <xsl:attribute name="selected">selected</xsl:attribute>
                                            </xsl:if>
                                            <xsl:value-of select="$translated"/>
                                        </option>
                                        <xsl:choose>
                                            <xsl:when test="$AdminMode='sys'">
                                                <xsl:variable name="tag" select=" 'admin' "/>
                                                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                                <option value="admin">
                                                    <xsl:if test=" $Role = 'admin' ">
                                                        <xsl:attribute name="selected">selected</xsl:attribute>
                                                    </xsl:if>
                                                    <xsl:value-of select="$translated"/>
                                                </option>
                                            </xsl:when>								
                                        </xsl:choose>
                                    </select>
                                </td>
                            </tr>
                        </xsl:otherwise>
                    </xsl:choose>
					
                    <tr>
                        <xsl:variable name="tag" select=" 'lastname' "/>
                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                        <td >
                            <xsl:value-of select="$translated"/>*
                        </td>
                        <td>
                            <input class="mandatary" type="text" id="lastname" name="lastname" style="width:150px" value="{$LastName}"></input>
                        </td>
                    </tr>
                    <tr>
                        <xsl:variable name="tag" select=" 'firstname' "/>
                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                        <td >
                            <xsl:value-of select="$translated"/>*
                        </td>
                        <td>
                            <input class="mandatary" type="text" id="firstname" name="firstname" style="width:150px" value="{$FirstName}"></input>
                        </td>
                    </tr>
                    <tr>
                        <xsl:variable name="tag" select=" 'address' "/>
                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                        <td >
                            <xsl:value-of select="$translated"/>
                        </td>
                        <td>
                            <input type="text" id="address" name="address" style="width:150px" value="{$Address}"></input>
                        </td>
                    </tr>
                    <tr>
                        <xsl:variable name="tag" select=" 'email' "/>
                        <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                        <td >
                            <xsl:value-of select="$translated"/>*
                        </td>
                        <td>
                            <input class="mandatary" type="email" id="email" name="email" style="width:150px" value="{$Email}"></input>
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <br/>
                            <input type="hidden" name="lang" value="{$lang}"/>
                            <xsl:variable name="tag" select=" 'Oloklirwsi' "/>
                            <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                            <input type="submit" class="button"  id="finishButton" value="{$translated}"></input>	
                            <xsl:variable name="tag" select=" 'Epistrofi' "/>
                            <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                            <a id="backButton" href="javascript:window.history.go(-1);">
                                <xsl:value-of select="$translated"/>
                            </a>
                        </td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <xsl:choose>
                            <xsl:when test="$ErrorMsg != '' ">
                                <xsl:variable name="tag" select="$ErrorMsg"/>
                                <xsl:variable name="translated" select="$locale/context/*[name()=$tag]/*[name()=$lang]"/>
                                <td class="contentText" style="color:#EE0000;">
                                    <b>
                                        <xsl:value-of select="$translated"/>
                                    </b>
                                </td>
                            </xsl:when>
                            <xsl:otherwise>
                                <td style="color:#EE0000;">
                                    <br/>
                                </td>
                            </xsl:otherwise>
                        </xsl:choose>
                    </tr>
                </table>
            </form>
            <br/>
            <script language="javascript">document.getElementById('username').focus();</script>
            <script language="javascript">
                if('<xsl:value-of select="$AdminAction"/>'=="view"){
                $finish=$("#finishButton").hide();
                $input=  $("input:text, input:password, textarea, #orgId, #userrole");
                $input.attr("disabled",true);
                }else{
                $back=$("#backButton").hide();

                }
                if('<xsl:value-of select="$AdminAction"/>'=="edit"){
                $("#username").attr('readonly','readonly');
                }

                
                            
                function validateAll(){
                var value ="";
            
                $(".mandatary").each(function( index ) {
                var msg = '<xsl:value-of select="$notEmptyFields"/>';
                
                if($( this ).val()==''){ 
                    value ="false";

                alert(msg);
                return false;  
                }
                return true;                
                });
                    
                if(value=='false')
                return false;
                var email = $("#email").val();
                var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;  
                if(!email.match(mailformat))  
                {
                var msg = '<xsl:value-of select="$emailMsg"/>';
                alert(msg);  
                return false;
                
                }
                return true;
                }
            </script>
        </td>
    </xsl:template>
</xsl:stylesheet>