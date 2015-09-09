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
    <xsl:include href="path.xsl"/>
    <xsl:include href="range.xsl"/>    
    <xsl:import href="target.xsl"/>
    <xsl:import href="../various.xsl"/>
    <xsl:import href="entity.xsl"/>
    <xsl:include href="comments.xsl"/>
    <xsl:import href="if-rule.xsl"/>


    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template match="link">
       
        <!--<tbody class="link" data-xpath="{concat(//path/@xpath,'/..')}" id="{concat(//path/@xpath,'/..')}">-->
        <xsl:apply-templates>
               

        </xsl:apply-templates>  
        <!--<script src="js/after.js"></script>-->    

        <!--</tbody>-->
    </xsl:template>

</xsl:stylesheet>
