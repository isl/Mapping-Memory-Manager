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
package isl.FIMS.servlet.fetch;

import isl.FIMS.servlet.ApplicationBasicServlet;
import isl.FIMS.utils.UtilsQueries;
import isl.FIMS.utils.entity.Config;
import isl.dbms.DBCollection;
import isl.dbms.DBFile;
import isl.dms.DMSException;
import isl.dms.xml.XMLTransform;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author konsolak
 */
public class FetchAudio extends ApplicationBasicServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.initVars(request);

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String id = request.getParameter("id");
        String type = request.getParameter("type");
        DBCollection col = new DBCollection(this.DBURI, this.systemDbCollection + type, this.DBuser, this.DBpassword);
        String collectionPath = UtilsQueries.getPathforFile(col, id + ".xml", id.split(type)[1]);
        col = new DBCollection(this.DBURI, collectionPath, this.DBuser, this.DBpassword);
        DBFile dbF = col.getFile(id + ".xml");
        String file = dbF.queryString2("//Podcast/DigitalFile/File/text()")[0];
        String output = "";
        if (file != "") {

            String xml = "<general>" + "<language>" + lang + "</language>"
                    + "<id>" + id + "</id><file>" + file + "</file></general>";
            try {
                XMLTransform trans = new XMLTransform(xml);
                Config conf = new Config("Listen");
                String xsl = conf.Audio_XSL;

                String html = trans.transform(xsl);
                output = html;
            } catch (DMSException ex) {
                ex.printStackTrace();
            }
            try {
                output = output.replaceAll("&amp;", "&");
                out.println(output.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&nbsp;", " "));
            } finally {
                out.close();
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
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
     * Handles the HTTP
     * <code>POST</code> method.
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
