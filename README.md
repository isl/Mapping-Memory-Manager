
Copyright 2015 Institute of Computer Science,
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

Authors : Konstantina Konsolaki, Georgios Samaritakis

This file is part of the the Mapping Memory Manager.

 

Mapping Memory Manager (3M)
====

Mapping Memory Manager is a package for managing mapping definition files. 
It’s based on [FIMS] (https://github.com/isl/FIMS) management system for the administration of the files and also on the 
[3MEditor] (https://github.com/isl/3MEditor) for editing and viewing the files. 

##Run##
1.	Download FIMS from https://github.com/isl/FIMS
2.	Download the 3MEditor from https://github.com/isl/3MEditor

Each one of these repositories contains further instructions on how to setup and deploy.

##Database Setup##

In order to store the database, open the eXist client application and select from the top menu "File-->Store files/directories" and then select
the DMSCOLLECTION folder located inside "db". 

##Application Setup##

Folder Structure

1.	In order some functionalities of the system to execute, an example structure of the folders needed is provided inside “3M”. 
Copy “3M” folder in your desired destination and then adjust the corresponding paths in the web.xml file. In the folder “Schema”, we provide
the schema of the Mapping.

2.	Replace context.xml located at FIMS\web\META-INF with the context.xml from “3M_configuration”.

3.	Replace web.xml located at FIMS\web\WEB-INF with the web.xml from “3M_configuration”.  Afterwards, edit the new web.xml file and make the 
changes described at FIMS repository at [README.md](https://github.com/isl/FIMS/blob/master/README.md)

a.	You will find the needed .xsd files at 3M/Schema folder

b.	You will find the uploads folder at 3M/uploads

4.	Replace the images located at the FIMS\web\formatting\images folder with the images from 3M_configuration\images

5.	Replace the manuals located at the FIMS\web\Manuals folder with the manuals from 3M_configuration\Manuals






