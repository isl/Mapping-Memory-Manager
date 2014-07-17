/*
 * Copyright 2014 Institute of Computer Science,
 * Foundation for Research and Technology - Hellas
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved
 * by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations
 * under the Licence.
 *
 * Contact:  POBox 1385, Heraklio Crete, GR-700 13 GREECE
 * Tel:+30-2810-391632
 * Fax: +30-2810-391638
 * E-mail: isl@ics.forth.gr
 * http://www.ics.forth.gr/isl
 *
 * Authors : Georgios Samaritakis, Konstantina Konsolaki.
 *
 * This file is part of the 3M webapp of Mapping Memory Manager project.
 */

Mapping Memory Manager(3M)
====

3M is a web application suitable for documenting, recording and managing XML files stored in an XML database. 
3M project dependecies are described in file [3M-Dependencies-LicensesUsed.txt](https://github.com/isl/Mapping-Memory-Manager/blob/master/3M/3M-Dependencies-LicensesUsed.txt)

##Build - Deploy - Run##

Folders src, web and lib contain all the files needed to build the web app and create a war file. 
You may use any application server that supports war files. (has been tested with Apache Tomcat version 7 or later). Currently, the database supported is [eXist version 1.4.2](http://www.exist-db.org).

##Setup##

Edit web.xml

	a. SystemWebRoot: Change the port 8084, with the port of the application server. Also if necessary, change localhost to the IP of your machine
	b. DBURI: Change the port 8090 with the port of the database server. Also if necessary, change localhost to the IP of your machine
	c. DBuser: database username-Change this parameter if necessary
	d. DBpassword: database password-Change this parameter if necessary
	e. DBdriver: database driver-Change this parameter if necessary
	f. schemaFolder: Change the path to point to the correct path at your local file system (where yours schema folder is located). 
	g. SystemUploads: Change the path to point to the correct path at your local file system (where yours uploads folder is located). 
	h. Export_Import_Folder: Change the path to point to the correct path at your local file system (where yours export_import folder is located)
	i. Backups: Change the path to point to the correct path at your local file system (where yours Backups folder is located). Initially this folder is empty but when you create a System Backup from the system, the backup files are stored to this folder. 
