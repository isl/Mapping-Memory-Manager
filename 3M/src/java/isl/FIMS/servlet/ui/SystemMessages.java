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
package isl.FIMS.servlet.ui;

import isl.FIMS.utils.Messages;
import isl.FIMS.utils.entity.XMLEntity;
import isl.FIMS.servlet.ApplicationBasicServlet;
import isl.FIMS.utils.entity.Config;
import isl.FIMS.utils.Utils;
import isl.FIMS.utils.UtilsXPaths;
import isl.dms.DMSException;
import isl.dms.xml.XMLTransform;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import org.w3c.dom.Element;

/**
 *
 * @author samarita
 * @version
 */
public class SystemMessages extends ApplicationBasicServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        this.initVars(request);


        String file = request.getParameter("file");
        String displayMsg = request.getParameter("message");
        String time = request.getParameter("time");
        String feXMLEditor = request.getParameter("feXMLEditor");
        if (feXMLEditor == null) {
            feXMLEditor = "";
        }
        String type = "";
        Config conf = new Config(type);
        String language = request.getParameter("lang");
        if (language != null) {
            this.lang = language;
        }
        if (displayMsg == null) {
            displayMsg = Messages.DOC_UNLOCKING;
        }

        StringBuffer xml = new StringBuffer(this.xmlStart(this.topmenu, this.username, this.pageTitle, this.lang, "", request));
        xml.append("<Display>").append(displayMsg).append("</Display>\n");
        xml.append("<Time>").append(time).append("</Time>\n");

        String xsl = conf.DISPLAY_WIN_XSL;
        if (feXMLEditor.equals("yes")) {
            if (displayMsg.equals("HAS_DEPENDANTS")) {
                String EntityType = request.getParameter("type");
                String xmlId = request.getParameter("file").split(".xml")[0];
                XMLEntity xmlE = new XMLEntity(this.DBURI, this.systemDbCollection + EntityType, this.DBuser, this.DBpassword, EntityType, xmlId);
                String[] references = xmlE.queryString("//admin/refs_by/ref_by");
                StringBuffer resultsTag = new StringBuffer("<results>\n");
                StringBuffer outputsTag = new StringBuffer("<outputs>\n");
                outputsTag.append("<path>").append("Name").append("</path>\n");
                outputsTag.append("<path>").append("URI_ID").append("</path>\n");
                outputsTag.append("</outputs>\n");


                for (int i = 0; i < references.length; i++) {
                    resultsTag.append("<result>\n");

                    Element e = Utils.getElement(references[i]);
                    String sps_type = e.getAttribute("sps_type");
                    String sps_id = e.getAttribute("sps_id");
                    String xpath = UtilsXPaths.getSearchXpathAtName(sps_type) + "/text()";
                    XMLEntity xmlD = new XMLEntity(this.DBURI, this.systemDbCollection + sps_type, this.DBuser, this.DBpassword, sps_type, sps_type + sps_id);
                    String name = xmlD.queryString(xpath)[0];
                    String uri_id = xmlD.queryString("fn:tokenize(//admin/uri_id/text(),'" + this.URI_Reference_Path + "')[last()]")[0];
                    resultsTag.append("<name>").append(name).append("</name>");
                    resultsTag.append("<uri_id>").append(uri_id).append("</uri_id>");
                    resultsTag.append("<id>").append(sps_type + sps_id).append("</id>");
                    resultsTag.append("<type>").append(sps_type).append("</type>");

                    resultsTag.append("</result>\n");

                }
                resultsTag.append("</results>\n");
                xml.append("<query>\n");
                xml.append(outputsTag);
                xml.append(resultsTag);
                xml.append("</query>\n");
                xml.append("<URI_Reference_Path>").append(this.URI_Reference_Path).append("</URI_Reference_Path>\n");


            }
            String link = request.getParameter("link");
            if (link == null) {
                link = "";
            }
            if (link.equals("yes")) {
                String EntityType = request.getParameter("type");
                String xmlId = request.getParameter("file").split(".xml")[0];
                String category = request.getParameter("category");

                String goOnLink = "/" + this.editorWebapp + "/Index?type=" + EntityType + "&amp;id=" + xmlId + "&amp;category=" + category + "&amp;action=unlockedit&amp;lang=" + this.lang;
                xml.append("<Unlock>").append(goOnLink).append("</Unlock>\n");
            }
            xsl = conf.POPUP_DISPLAY_XSL;
        }

        if (time != null) {
            xsl = conf.TIMED_DISPLAY_WIN_XSL;
        }
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
     */
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
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}
