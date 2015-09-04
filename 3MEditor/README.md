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

3MEditor
==========

3MEditor is a web application used to edit/view [x3ml](https://github.com/delving/x3ml "x3ml") mapping files stored in an [eXist] (http://www.exist-db.org "eXist") database (Has been tested with eXist versions 2.1+)..

## Build - Deploy - Run
Folders src, web and lib contain all the files needed to build the web app and create a war file.
You may use any application server that supports war files. (Has been tested with Apache Tomcat versions 5,6,7,8).

## eXist setup 
Once you have an eXist database instance up and running, you may use an example database setup (such as the one in [3M_configuration_files] (https://github.com/isl/Mapping-Memory-Manager/tree/master/3M_configuration_files "3M_configuration_files")) to begin with. Download [3M_configuration_files] (https://github.com/isl/Mapping-Memory-Manager/tree/master/3M_configuration_files "3M_configuration_files") and store [DMSCollection] (https://github.com/isl/Mapping-Memory-Manager/tree/master/3M_configuration_files/DMSCOLLECTION "rootCollection") into eXist's collection db.

## Configuration
Once you have deployed the 3MEditor war and you have stored files from [3M_configuration_files] (https://github.com/isl/Mapping-Memory-Manager/tree/master/3M_configuration_files "3M_configuration_files") into your eXist DB, you should check 3MEditor's web.xml and make any necessary adjustments (Mostly IPs, ports and filepaths). There is a description tag for every parameter, explaining what may be changed and what should be left untouched.

## Usage
3MEditor's default usage is as a [3M] (https://github.com/isl/Mapping-Memory-Manager "3M") plugin.
It may also be used independently, but new mapping files should be created and deleted either manually or using some other software.

[x3ml](https://github.com/delving/x3ml "x3ml") mapping files are stored inside eXist's Mapping collection (subcollection 1,2,3 and so on...). 

If you want to edit a mapping file, use:
**http://(server IP):(server port)>/3MEditor/Index?id=(Mapping id found in admin part)** 
e.g. **http://localhost:8080/3MEditor/Index?id=6**

If you want to simply view a mapping file, add parameter action with value view:
**http://(server IP):(server port)>/3MEditor/Index?id=(Mapping id found in admin part)&action=view** 
e.g. **http://localhost:8080/3MEditor/Index?id=6&action=view**

If you want to view a mapping file as raw XML, add parameter output with value xml:
**http://(server IP):(server port)>/3MEditor/Index?id=(Mapping id found in admin part)&output=xml** 
e.g. **http://localhost:8080/3MEditor/Index?id=6&output=xml**

The 3MEditor webapp dependecies and licenses used are described in file 3MEditor-Dependencies-LicensesUsed.txt 


