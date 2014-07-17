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
package isl.FIMS.servlet.search;

import isl.FIMS.utils.search.QueryTools;
import isl.FIMS.utils.search.Config;
import isl.dms.DMSException;
import isl.dms.file.DMSUser;
import isl.dms.xml.XMLTransform;
import isl.dms.file.DMSXQuery;
import isl.dms.file.EntryNotFoundException;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.*;
import javax.servlet.http.*;

public class SearchDelete extends BasicSearchServlet {
	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws DMSException 
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException, DMSException {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		this.initVars(request);

		StringBuffer xml = new StringBuffer();
		String xmlStart = this.xmlStart(this.topmenu, this.username, "Search Delete", this.lang, "",request);
		String xmlEnd = this.xmlEnd();

		Hashtable params = this.getParams(request);
		String xmlMiddle = QueryTools.getXML(params, this.conf,this.dataCol);

		String mnemonicName = (String) params.get("mnemonicName");
		if (mnemonicName == null || mnemonicName.equals("")){
			xml.append(xmlStart);
			xml.append(xmlMiddle);
			xml.append("<success return=\"0\">"+"EMPTY_FIELD_MnemonicName"+"</success>\n");
			xml.append(xmlEnd);

			XMLTransform xmlTrans = new XMLTransform(xml.toString());
			String xsl = Config.SEARCH_DELETE_XSL;
			xmlTrans.transform(out, xsl);
			out.close();
			return;
		}

		try{
                       
			int userId = this.getUserId(request);
			DMSXQuery q = new DMSXQuery(mnemonicName, userId, this.conf);
			q.remove();
		}catch(EntryNotFoundException Ex){
			//an den yparxei ....
			xml.append(xmlStart);
			xml.append(xmlMiddle);
			xml.append("<success return=\"0\">"+"QUERY_ΝΟΤ_EXIST"+"</success>\n");
			xml.append(xmlEnd);

			XMLTransform xmlTrans = new XMLTransform(xml.toString());
			String xsl = Config.SEARCH_DELETE_XSL;
			xmlTrans.transform(out, xsl);
			out.close();
			return;
		}

		xml.append(xmlStart);
		xml.append("<query>\n<info>\n")
			.append("<name>").append(mnemonicName).append("</name>\n")
			.append("<category>").append(params.get("category")).append("</category\n>")
			.append("</info>\n</query>\n");
		xml.append("<success return=\"1\"></success>\n");
		xml.append(xmlEnd);

		XMLTransform xmlTrans = new XMLTransform(xml.toString());
		String xsl = Config.SEARCH_DELETE_XSL;
		xmlTrans.transform(out, xsl);
		out.close();
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Returns a short description of the servlet.
	 */
	public String getServletInfo() {
		return "Short description";
	}
}
