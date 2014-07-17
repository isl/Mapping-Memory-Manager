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
 * This file is part of the x3mlEditor webapp of Mapping Memory Manager project.
 */
package isl.x3ml.servlets;

import isl.dbms.DBCollection;
import isl.dbms.DBFile;
import isl.x3ml.BasicServlet;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author samarita
 */
public class ViewPublished extends BasicServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String output = request.getParameter("output");

        if (output == null || output.equals("") || output.equals("html")) {
            response.setContentType("text/html;charset=UTF-8");
            output = "html";
        } else {
            response.setContentType("text/xml;charset=UTF-8");
        }

        PrintWriter out = response.getWriter();
        this.servletParams(request, response);

        String id = request.getParameter("id");
        String type = request.getParameter("type");
        String file = request.getParameter("file");
        String lang = request.getParameter("lang");
        String action = request.getParameter("action");

        if (lang == null) {
            lang = "en";
        }
        if (id == null) {
            id = "1";
        }
        if (type == null) {
            type = "Mapping";
        }
        id = id.replace(type, "");
        StringBuilder xmlMiddle = new StringBuilder();

        String xmlId = type + id + ".xml";
        DBCollection dbc = new DBCollection(super.DBURI, applicationCollection + "/" + type, super.DBuser, super.DBpassword);
        String collectionPath = getPathforFile(dbc, xmlId, id);
        DBFile dbf = new DBFile(super.DBURI, collectionPath, xmlId, super.DBuser, super.DBpassword);
        String status = dbf.queryString2("//admin/status/string()")[0];
        String content = getDBFileContent(collectionPath, xmlId);

        if (!output.equals("html")) {
             content = content.replace("<?xml-stylesheet type=\"text/xsl\" href=\"crm_mapping-v2.0.xsl\"?>", "");
            content = content.replaceAll("(?s)<admin>.*?</admin>", "");
            out.println(content);

        } else {

            xmlMiddle.append("<output><xml>");
            if (status.equals("published")) {
                xmlMiddle.append(content);
            }
            xmlMiddle.append("</xml>");

            String xsl = super.baseURL + "/formating/xsl/mapping.xsl";
            xmlMiddle.append("<viewMode>").append("1").append("</viewMode>");
            xmlMiddle.append("<type>").append(type).append("</type>");
            xmlMiddle.append("<id>").append(id).append("</id>");
            xmlMiddle.append("</output>");
            out.write(transform(xmlMiddle.toString(), xsl));
        }
        out.close();

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
