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
package isl.FIMS.utils.search;

public class Config {
	/**
	 * The xsl file for transforming the output of the
	 * <code>Search</code> servlet.
	 * The Default value is <b>Search.xsl</b> in the directory
	 * <b>/formating/xsl/search</b>, under the 'systemRoot'.
	 */
	public static String SEARCH_XSL;
	
	/**
	 * The xsl file for transforming the output of the
	 * <code>SearchResults</code> servlet.
	 * The Default value is <b>SearchResults.xsl</b> in the directory
	 * <b>/formating/xsl/search</b>, under the 'systemRoot'.
	 */
	public static String SEARCH_RESULTS_XSL;
	/**
	 * The xsl file for transforming the output of the
	 * <code>SearchSave</code> servlet.
	 * The Default value is SearchSave.xsl in the directory
	 * <b>/formating/xsl/search</b>, under the 'systemRoot'.
	 */
	public static String SEARCH_SAVE_XSL;
	/**
	 * The xsl file for transforming the output of the
	 * <code>SearchDelete</code> servlet.
	 * The Default value is <b>SearchDelete.xsl</b> in the directory
	 * <b>/formating/xsl/search</b>, under the 'systemRoot'.
	 */
	public static String SEARCH_DELETE_XSL;

	
	public static String LANG;

	/**
	 * Method for the configuration of the Search servlets.
	 * 
	 * The 'systemRoot' is the 'root' directory of the system.
	 * Under that there are the various directories and files
	 * that are used by the servlets. the root direcotry can be
	 * either on 'disk' (e.g. Tomcat, denoted by an http URL)
	 * or in a DB (e.g. eXist, denoted by a rest URI).
	 * 
	 * @param systemRoot the 'root' directory of the system
	 */
	public static void init(String systemWebRoot){
		String XSL_PATH 	= systemWebRoot + "formating/xsl/search/";
		SEARCH_XSL 			= XSL_PATH + "Search.xsl";
		SEARCH_RESULTS_XSL 	= XSL_PATH + "SearchResults.xsl";
		SEARCH_SAVE_XSL 	= XSL_PATH + "SearchSave.xsl";
		SEARCH_DELETE_XSL 	= XSL_PATH + "SearchDelete.xsl";
		LANG				= "";
	}
}
