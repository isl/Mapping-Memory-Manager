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
package isl.FIMS.servlet.copy;

import isl.FIMS.utils.Messages;
import isl.FIMS.utils.entity.XMLEntity;
import isl.FIMS.servlet.ApplicationBasicServlet;
import isl.FIMS.utils.Utils;
import isl.FIMS.utils.UtilsQueries;
import isl.FIMS.utils.entity.Config;
import isl.dbms.DBFile;
import isl.dms.DMSException;
import isl.dms.file.DMSTag;
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
public class CreateCopy extends ApplicationBasicServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        this.initVars(request);
        String displayMsg = "";
        String type = request.getParameter("type");
        Config conf = new Config(type);
        StringBuffer xml = new StringBuffer(this.xmlStart(this.topmenu, this.username, this.pageTitle, this.lang, "", request));
        String fileId = request.getParameter("id");
        String id = fileId.split(type)[1];

        XMLEntity xmlE = new XMLEntity(this.DBURI, this.systemDbCollection + type, this.DBuser, this.DBpassword, type, fileId);

        String newId = initInsertFile(type,true)[0];
        newId = newId.split(type)[1];
        XMLEntity xmlNew = new XMLEntity(this.DBURI, this.systemDbCollection + type, this.DBuser, this.DBpassword, type, type + newId);
        this.initAdminPart(xmlNew, newId);
        String collName = UtilsQueries.getpath(this.DBURI, this.systemDbCollection + type, this.DBuser, this.DBpassword, type, newId);
        DBFile dbF = new DBFile(this.DBURI, collName, type + newId + ".xml", this.DBuser, this.DBpassword);
        String adminPart = dbF.queryString("//admin")[0];
        dbF.setXMLAsString(xmlE.getDbFile().getXMLAsString());
        dbF.store();
        dbF.xInsertAfter("//admin", adminPart);
        dbF.xRemove("//admin[1]");
        String uri_name = "";
        try {
            uri_name = DMSTag.valueOf("uri_name", "target", type, this.conf)[0];
        } catch (DMSException ex) {
        }
        String uriValue = this.URI_Reference_Path + uri_name + "/" + newId;

        dbF.xAppend("//admin", "<copyFrom/>");
        dbF.xAppend("//admin/copyFrom", id);

        Utils.updateReferences(xmlNew, this.DBURI, newId, type, this.DBpassword, this.DBuser);

        displayMsg = Messages.ACTION_SUCCESS;
        displayMsg += Messages.NL + Messages.NL + Messages.URI_ID;
        xml.append("<codeValue>").append(uriValue).append("</codeValue>\n");
        String xsl = conf.DISPLAY_XSL;
        xml.append("<Display>").append(displayMsg).append("</Display>\n");
        xml.append(this.xmlEnd());
        try {
            XMLTransform xmlTrans = new XMLTransform(xml.toString());
            xmlTrans.transform(out, xsl);
        } catch (DMSException e) {
            e.printStackTrace();
        }
        out.close();

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
