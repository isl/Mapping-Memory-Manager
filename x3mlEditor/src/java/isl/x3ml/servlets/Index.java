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
import isl.x3ml.BasicServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author samarita
 */
public class Index extends BasicServlet {

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
        String chosenAPI = request.getParameter("mode");

        int mode = 2; //Default is SAM's;
//        System.out.println("CHOSEN=" + chosenAPI);
        if (chosenAPI != null) {
            mode = Integer.parseInt(chosenAPI);
        }

        if (action == null) {
            action = "edit";
        }

        if (lang == null && type == null && id == null && file == null) {
            response.sendRedirect("Index?id=1");
            return;

        }
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
        if (output.equals("html")) {
            xmlMiddle.append("<output><xml>");
        }
        String xmlId = type + id + ".xml";
        DBCollection dbc = new DBCollection(super.DBURI, applicationCollection + "/" + type, super.DBuser, super.DBpassword);
        String collectionPath = getPathforFile(dbc, xmlId, id);
        xmlMiddle.append(getDBFileContent(collectionPath, xmlId));
        if (output.equals("html")) {
            xmlMiddle.append("</xml>");
        }
        if (output.equals("html")) {

            String xsl = super.baseURL + "/formating/xsl/mapping.xsl";
            if (stateOfSite.equals("off")) {
//                action = "view";
//                file = "Message_" + lang;
                response.sendRedirect("maintenance.html");
                return;
            }

            if (action.equals("view")) {
                xmlMiddle.append("<viewMode>").append("1").append("</viewMode>");

                xmlMiddle.append("<versions>").append(request.getParameter("versions")).append("</versions>");
                xmlMiddle.append("<collectionID>").append(request.getParameter("collectionID")).append("</collectionID>");
                xmlMiddle.append("<xmlId>").append(request.getParameter("xmlId")).append("</xmlId>");
                xmlMiddle.append("<entityType>").append(request.getParameter("entityType")).append("</entityType>");

            } else {
                xmlMiddle.append("<viewMode>").append("0").append("</viewMode>");

                HttpSession session = sessionCheck(request, response);
                if (session == null) {
                    session = request.getSession(true);
                    session.setAttribute("resourcesList", new HashMap<String, String[]>());
                } else {
//                    
//                    session.removeAttribute("resourcesList");
//                    session.invalidate();

                }
            }

            xmlMiddle.append("<lang>").append(lang).append("</lang>");
            xmlMiddle.append("<editorType>").append(editorType).append("</editorType>");

            if (file != null) {
                xmlMiddle.append("<file>").append(file).append("</file>");
            } else {
                xmlMiddle.append("<mode>").append(mode).append("</mode>");
                xmlMiddle.append("<type>").append(type).append("</type>");
                xmlMiddle.append("<id>").append(id).append("</id>");
            }

            xmlMiddle.append("</output>");
            out.write(transform(xmlMiddle.toString(), xsl));
        } else {
            String content = xmlMiddle.toString();
            content = content.replace("<?xml-stylesheet type=\"text/xsl\" href=\"crm_mapping-v2.0.xsl\"?>", "");
            content = content.replaceAll("(?s)<admin>.*?</admin>", "");
            out.write(content);
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
            throws ServletException,
            IOException {
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
            throws ServletException,
            IOException {
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
