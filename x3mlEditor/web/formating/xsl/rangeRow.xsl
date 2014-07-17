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
	<xsl:include href="additional.xsl"/>
	<xsl:include href="intermediate.xsl"/>
	<!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
	<xsl:template name="rangeRow" match="/">
		<xsl:param name="pathSoFar"/>
		<tr bgcolor="#C0C0C0" class="rangeRow">
			<td>
				<a title="Range">R</a>
			</td>
			<td>
				<span data-editable="" data-path="{concat($pathSoFar,'/source_node')}">
					<xsl:value-of select="source_node"/>
				</span>
			</td>
			<td>
                            <xsl:for-each select="target_node/entity">
				<xsl:call-template name="entity">
					<xsl:with-param name="pathSoFar" select="concat($pathSoFar,'/target_node/entity')"/>
                                </xsl:call-template>
                            </xsl:for-each>
                            <!--
				<xsl:for-each select="internal_node|constant_node">
					<xsl:variable name="name" select="name()"/>
					<xsl:variable name="precedingSiblingsWithSameName">
						<xsl:choose>
							<xsl:when test="$name='constant_node'">
								<xsl:value-of select="count(preceding-sibling::constant_node)"/>
							</xsl:when>
							<xsl:when test="$name='internal_node'">
								<xsl:value-of select="count(preceding-sibling::internal_node)"/>
							</xsl:when>
						</xsl:choose>
					</xsl:variable>
					<xsl:variable name="pos3" select="$precedingSiblingsWithSameName+1"/>
					<xsl:apply-templates select=".">
						<xsl:with-param name="pathSoFar" select="$pathSoFar"/>
						<xsl:with-param name="pos3" select="$pos3"/>
					</xsl:apply-templates>
				</xsl:for-each>
                                
                                -->
				<xsl:if test="//viewMode=0">
					<!--<br/>-->
					<xsl:call-template name="addAdditional">
						<xsl:with-param name="pathSoFar" select="concat($pathSoFar,'/target_node/entity')"/>
					</xsl:call-template>
				</xsl:if>
				<!--
              
                <xsl:call-template name="addIntermediate">
                    <xsl:with-param name="pathSoFar" select="$pathSoFar"/>
                </xsl:call-template>
                -->
            </td>
            <xsl:for-each select="target_node">
                <xsl:call-template name="if-ruleCell">
                    <xsl:with-param name="pathSoFar" select="concat($pathSoFar,'/target_node')"/>
                    <!--xsl:with-param name="entityOrProperty" select="'entity'"/-->
                </xsl:call-template>
            </xsl:for-each>
            <xsl:call-template name="commentsCell">
                <xsl:with-param name="pathSoFar" select="$pathSoFar"/>
            </xsl:call-template>
        </tr>
    </xsl:template>
</xsl:stylesheet>
