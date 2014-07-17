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
package isl.FIMS.servlet.entity;

import isl.FIMS.utils.entity.Config;
import isl.FIMS.utils.Messages;
import isl.FIMS.utils.entity.XMLEntity;
import isl.FIMS.servlet.ApplicationBasicServlet;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import isl.FIMS.utils.entity.GetEntityCategory;
import isl.FIMS.utils.Utils;
import isl.FIMS.utils.UtilsXPaths;
import isl.dms.DMSException;
import isl.dms.xml.XMLTransform;
import org.w3c.dom.Element;

public class DeleteEntity extends ApplicationBasicServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> methods.
     *
     *
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        this.initVars(request);

        String type = request.getParameter("type");
        String category = GetEntityCategory.getEntityCategory(type);
        String displayMsg = "";
        //sysadmin has no more rights to delete entities
        StringBuffer xml = new StringBuffer(this.xmlStart(this.topmenu, this.username, this.pageTitle, this.lang, "", request));

        String fileId = request.getParameter("id");

        String status = request.getParameter("status");
        String mode = request.getParameter("mode");
        if (status == null) {
            status = "";
        }
        XMLEntity xmlE = new XMLEntity(this.DBURI, this.systemDbCollection + type, this.DBuser, this.DBpassword, type, fileId);

        boolean isUnpublished = xmlE.hasAdminProperty("status", Messages.UNPUBLISHED);
        boolean isRejected = xmlE.hasAdminProperty("status", Messages.REJECTED);
        boolean isAdmin = this.userHasAction("admin");

        if (mode == null) {
            if (isAdmin) {
                mode = "admin";
            }
        }

        if (category.equals("primary")) {

            String userOrg = this.getUserGroup();
            String docOrg = xmlE.getAdminProperty("organization");
            boolean isWritable = Boolean.valueOf(xmlE.queryString("//admin/write='" + this.username + "'")[0]);

            if (userOrg.equals(docOrg) && isWritable) {
                //extra conditions
                boolean hasDependants = xmlE.exist("//admin/refs_by/ref_by");
                if (isUnpublished || isRejected) {
                    if (xmlE.isSaved() && xmlE.isLocked()) {
                        displayMsg = Messages.CANNOT_DELETE_IS_EDITED;
                    } else if (hasDependants) {
                        displayMsg = Messages.CANNOT_DELETE + Messages.NL;
                        displayMsg += Messages.HAS_DEPENDANTS;
                    } else {
                        //delete ref_by field from the files this xml used
                        String[] references = xmlE.queryString("//admin/refs/ref");
                        for (int i = 0; i < references.length; i++) {
                            Element e = Utils.getElement(references[i]);
                            String sps_type = e.getAttribute("sps_type");
                            String sps_id = e.getAttribute("sps_id");
                            XMLEntity removeXml = new XMLEntity(this.DBURI, this.systemDbCollection + sps_type, this.DBuser, this.DBpassword, sps_type, sps_type + sps_id);
                            removeXml.xRemove("//admin/refs_by/ref_by[@sps_type='" + type + "' and @sps_id='" + fileId.split(type)[1] + "' ]");
                        }

                        xmlE.delete();
                        if (!mode.equals("sys")) {
                            response.sendRedirect("ListEntity?type=" + type);
                        } else {
                            response.sendRedirect("AdminListEntity?type=" + type + "&status=" + status + "&mode=" + mode);
                        }
                        return;
                    }
                } else {
                    displayMsg = Messages.CANNOT_DELETE + Messages.NL;

                    if (xmlE.hasAdminProperty("status", Messages.PUBLISHED)) {
                        displayMsg += Messages.DOC_IS_PUBLISHED;
                    } else if (xmlE.hasAdminProperty("status", Messages.PENDING)) {
                        displayMsg += Messages.DOC_IS_PENDING;
                    }
                }
            } else {
                displayMsg += Messages.ACCESS_DENIED;
            }

        } else {
            boolean hasDependants = xmlE.exist("//admin/refs_by/ref_by");

            if (hasDependants) {

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

                displayMsg = Messages.CANNOT_DELETE + Messages.NL;
                displayMsg += Messages.HAS_DEPENDANTS;
            } else {
                //delete ref_by field from the files this xml used
                String[] references = xmlE.queryString("//admin/refs/ref");
                for (int i = 0; i < references.length; i++) {
                    Element e = Utils.getElement(references[i]);
                    String sps_type = e.getAttribute("sps_type");
                    String sps_id = e.getAttribute("sps_id");
                    XMLEntity removeXml = new XMLEntity(this.DBURI, this.systemDbCollection + sps_type, this.DBuser, this.DBpassword, sps_type, sps_type + sps_id);
                    removeXml.xRemove("//admin/refs_by/ref_by[@sps_type='" + type + "' and @sps_id='" + fileId.split(type)[1] + "' ]");
                }
                xmlE.delete();
                response.sendRedirect("ListEntity?type=" + type);
                return;
            }
        }

        if (displayMsg.length() != 0) {
            Config conf = new Config(type);

            xml.append("<Display>").append(displayMsg).append("</Display>\n");
            xml.append(this.xmlEnd());
            PrintWriter out = response.getWriter();
            String xsl = conf.DISPLAY_XSL;

            try {
                XMLTransform xmlTrans = new XMLTransform(xml.toString());
                xmlTrans.transform(out, xsl);
            } catch (DMSException e) {
                e.printStackTrace();
            }
            out.close();
        }
    }
}
