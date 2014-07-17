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
package isl.FIMS.servlet.admin;

import isl.FIMS.utils.entity.GetEntityCategory;
import isl.FIMS.utils.Messages;
import isl.FIMS.servlet.ApplicationBasicServlet;
import isl.FIMS.utils.UtilsQueries;
import isl.FIMS.utils.entity.Config;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import isl.dbms.DBCollection;
import isl.dbms.DBFile;
import isl.dms.DMSException;
import isl.dms.xml.XMLTransform;

public class AdminListEntity extends ApplicationBasicServlet {

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
        request.setCharacterEncoding("UTF-8");

        this.initVars(request);

        String type = request.getParameter("type");
        String status = request.getParameter("status");
        if (status == null) {
            status = "";
        }
        this.setStatus(status);

        Config conf = new Config(type);

        String mode = request.getParameter("mode");
        if (mode == null) {
            mode = "";
        }

        String displayMsg = "";


        StringBuffer xml = new StringBuffer(this.xmlStart(this.topmenu, this.username, this.pageTitle, this.lang, "", request));

        if (displayMsg.length() == 0) {
            //depending on 'type' this varies
            StringBuffer outputsTag = new StringBuffer();
            String userOrg = this.getUserGroup();

            //========== paging code ==========
            UtilsQueries u = new UtilsQueries();
            u.initListPaging(request);

            String querySource = u.listEntityQuery(type, this.status, mode, userOrg, this.lang, this.username, outputsTag);
            DBCollection queryCol = new DBCollection(this.DBURI, this.systemDbCollection + type, this.DBuser, this.DBpassword);
            DBFile[] queryRes = queryCol.query(querySource);

            StringBuffer resultsTag = new StringBuffer("<results>\n");
            for (int j = 0; j < queryRes.length; j++) {
                resultsTag.append(queryRes[j]).append("\n");
            }
            resultsTag.append("\n</results>\n");

            xml.append("<ListMode>").append(mode).append("</ListMode>\n");
            xml.append("<DocStatus>").append(status).append("</DocStatus>\n");
            xml.append("<LeftMenuId>").append("all" + type).append("</LeftMenuId>\n");
            xml.append("<EntityType>").append(conf.ENTITY_TYPE).append("</EntityType>\n");
            xml.append("<TargetCol>").append(queryCol.getName()).append("</TargetCol>");
            xml.append("<ServletName>").append(this.servletName).append("</ServletName>\n");
            xml.append("<EntityCategory>").append(GetEntityCategory.getEntityCategory(type)).append("</EntityCategory>\n");
            xml.append("<URI_Reference_Path>").append(this.URI_Reference_Path).append("</URI_Reference_Path>\n");
            xml.append("<IsGuestUser>").append(this.userHasAction("guest")).append("</IsGuestUser>\n");

            xml.append("<query>\n");
            xml.append(outputsTag);
            xml.append(resultsTag);
            xml.append("</query>\n");
            if (status != null && status.equals(Messages.PENDING)) {
                conf.ADMIN_LIST_ENTITY_XSL = conf.LIST_PENDING_XSL;
            } else if (status != null && status.equals(Messages.PUBLISHED)) {
                conf.ADMIN_LIST_ENTITY_XSL = conf.LIST_PUBLISHED_XSL;
            }
        }

        xml.append("<Display>").append(displayMsg).append("</Display>\n");
        xml.append("<IsGuestUser>").append(this.userHasAction("guest")).append("</IsGuestUser>\n");

        xml.append(this.xmlEnd());
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String xsl = conf.ADMIN_LIST_ENTITY_XSL;
        try {
            XMLTransform xmlTrans = new XMLTransform(xml.toString());
            xmlTrans.transform(out, xsl);
        } catch (DMSException e) {
            e.printStackTrace();
        }
        out.close();
    }

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
}
