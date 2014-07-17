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
package isl.FIMS.servlet.translation;

import isl.FIMS.utils.entity.GetEntityCategory;
import isl.FIMS.utils.Messages;
import isl.FIMS.utils.entity.XMLEntity;
import isl.FIMS.servlet.ApplicationBasicServlet;
import isl.FIMS.utils.UtilsQueries;
import isl.FIMS.utils.entity.Config;
import isl.FIMS.utils.UtilsXPaths;
import isl.dbms.DBCollection;
import isl.dbms.DBFile;
import isl.dms.DMSConfig;
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
public class TranslateServlet extends ApplicationBasicServlet {

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
            throws ServletException, IOException, DMSException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        this.initVars(request);
        UtilsQueries u = new UtilsQueries();
        String displayMsg = "";
        String type = request.getParameter("type");
        String fileId = request.getParameter("id");
        if (fileId == null) {
            fileId = type;
        }
        String mode = request.getParameter("mode");
        if (mode == null) {
            mode = "";
        }
        boolean isAdmin = this.getRights().equals("admin");

        if (isAdmin) {
            mode = "admin";
        }

        Config conf = new Config(type);

        StringBuffer xml = new StringBuffer(this.xmlStart(this.topmenu, this.username, this.pageTitle, this.lang, "", request));
        xml.append("<AdminMode>").append(mode).append("</AdminMode>\n");

        XMLEntity xmlE = new XMLEntity(this.DBURI, this.systemDbCollection + type, this.DBuser, this.DBpassword, type, fileId);

        String docOrg = xmlE.getAdminProperty("organization");
        String userOrg = this.getUserGroup();

        if (this.action.equals("unlocktotranslate")) {
            xmlE.setAdminProperty("locked", "no");
            response.sendRedirect("TranslateServlet?action=totranslate&type=" + type + "&id=" + fileId);
            return;
        } else if (this.action.equals("totranslate")) {
            boolean userCanWrite = xmlE.hasAdminProperty("write", this.username);
            boolean isUnpublished = xmlE.hasAdminProperty("status", Messages.UNPUBLISHED);
            boolean isRejected = xmlE.hasAdminProperty("status", Messages.REJECTED);
            boolean isPublished = xmlE.hasAdminProperty("status", Messages.PUBLISHED);
            boolean userCanRead = xmlE.hasAdminProperty("read", this.username);
            userCanRead = (isPublished || (userCanRead && docOrg.equals(userOrg)));
            String style = request.getParameter("style");
            if (style == null) {
                style = "";
            }
            boolean isSaved = xmlE.isSaved();
            String lockedBy = xmlE.isLockedBy();
            String unlock = "false";
            boolean canTraslate = true;
            boolean hasPublishedDependants = xmlE.exist("//admin/refs_by/ref_by[@isUnpublished=\"false\"]");
            if (GetEntityCategory.getEntityCategory(type).equals("primary")) {
                if (userCanWrite && (isUnpublished || isRejected)) {
                    if (isSaved && lockedBy != null && lockedBy.equals("no") == false) {
                        if (lockedBy.equals(this.getUsername(request)) == false) {
                            displayMsg = Messages.IS_EDITED_BY_USER;
                            canTraslate = false;
                        } else {
                            if (!style.equals("criteria")) {
                                canTraslate = false;
                                displayMsg = Messages.IS_EDITED_BY_YOU;
                                unlock = "TranslateServlet?action=unlocktotranslate&amp;type=" + type + "&amp;id=" + fileId;
                            }
                        }
                    }
                }
                if (isUnpublished == false && isRejected == false) {
                    if (xmlE.hasAdminProperty("status", Messages.PUBLISHED)) {
                        canTraslate = false;
                        displayMsg += Messages.DOC_IS_PUBLISHED;
                    } else if (xmlE.hasAdminProperty("status", Messages.PENDING)) {
                        canTraslate = false;
                        displayMsg += Messages.DOC_IS_PENDING;
                    }
                }
            } else {
                if (docOrg == null) {
                    canTraslate = false;
                    displayMsg = Messages.INTERNAL_ERROR;
                } else if (docOrg.equals(userOrg) == false) {
                    canTraslate = false;
                    displayMsg = Messages.CANNOT_EDIT;
                } else if (isSaved && lockedBy != null && lockedBy.equals("no") == false) {
                    if (lockedBy.equals(this.getUsername(request)) == false) {
                        displayMsg = Messages.IS_EDITED_BY_USER;
                        canTraslate = false;
                    } else {
                        if (!style.equals("criteria")) {
                            canTraslate = false;
                            displayMsg = Messages.IS_EDITED_BY_YOU;
                            unlock = "TranslateServlet?action=unlocktotranslate&amp;type=" + type + "&amp;id=" + fileId;
                        }
                    }
                } else if (hasPublishedDependants) {
                    canTraslate = false;
                    displayMsg = Messages.HAS_PUBLISHED_DEPENDANTS;
                }

            }
            String paging = request.getParameter("paging");
            if (paging == null) {
                paging = "";
            }

            if (canTraslate || paging.equals("true")) {
                if (isSaved) {
                    xmlE.setAdminProperty("locked", this.username);
                    StringBuffer outputsTag = new StringBuffer();
                    StringBuffer resultsTag = new StringBuffer("<results>\n");
                    String queryCond = "";
                    //syllegoume ola ta deltia pou mporei an dei o 'xrhsths'
                    String tmpMode = mode;
                    mode = "trans_" + mode;
                    //get results according to user's criteria
                    if (style.equals("criteria")) {
                        String lang = request.getParameter("lang");
                        String code = request.getParameter("code");
                        String name = request.getParameter("name");
                        queryCond = createCondition(lang, code, name, userOrg, mode, type, "");
                        xml.append("<hasCriteria>");
                        xml.append("<lang>").append(lang).append("</lang>");
                        xml.append("<code>").append(code).append("</code>");
                        xml.append("<name>").append(name).append("</name>");
                        xml.append("</hasCriteria>");
                    } else {
                        queryCond = u.getQueryConditionForTranslate(type, "", mode, userOrg, lang);
                    }
                    //========== paging code ==========
                    u.initListPaging(request);
                    //get all
                    String querySource = u.listAllEntityQuery(type, "", mode, userOrg, this.lang, this.username, outputsTag, queryCond);
                    DBCollection queryCol = new DBCollection(this.DBURI, this.systemDbCollection + type, this.DBuser, this.DBpassword);
                    DBFile[] queryRes = queryCol.query(querySource);
                    for (int j = 0; j < queryRes.length; j++) {
                        resultsTag.append(queryRes[j]).append("\n");
                    }
                    resultsTag.append("\n</results>\n");

                    mode = tmpMode;
                    xml.append("<query>\n");
                    xml.append(outputsTag);
                    xml.append(resultsTag);
                    xml.append("</query>\n");
                    conf.DISPLAY_XSL = conf.LIST_TO_TRANSLATE_XSL;
                }
            } else {
                displayMsg = Messages.CANNOT_EDIT + Messages.NL;
            }

            xml.append("<Langs>\n");
            for (int i = 0; i < this.systemLangs.length; i++) {
                xml.append("<Lang>");
                xml.append(this.systemLangs[i]);
                xml.append("</Lang>\n");
            }

            xml.append("</Langs>\n");
            xml.append("<FileId>").append(fileId).append("</FileId>");
            xml.append("<URI_Reference_Path>").append(this.URI_Reference_Path).append("</URI_Reference_Path>");
            xml.append("<ServletName>").append(this.servletName).append("</ServletName>\n");
            xml.append("<SearchName>").append(UtilsXPaths.getOutpuTitleForTranslateCriteria(type)).append("</SearchName>");
            xml.append("<Unlock>").append(unlock).append("</Unlock>\n");

        } else if (this.action.equals("translate")) {
            String fromId = request.getParameter("fromId");

            if (fromId != null) {
                XMLEntity fromXmlE = new XMLEntity(this.DBURI, this.systemDbCollection + type, this.DBuser, this.DBpassword, type, fromId);

                //set a conf for vocabularies [this is why we append 'Vocabulary/']
                DMSConfig translationConf = new DMSConfig(this.DBURI, this.systemDbCollection + "Vocabulary/", this.DBuser, this.DBpassword);

                xmlE.translateFrom(fromXmlE, translationConf);

                xmlE.setAdminProperty("locked", "no");

                displayMsg = Messages.ACTION_SUCCESS;

            } else {
                displayMsg = Messages.DID_NOT_CHOOSE_DOC;
            }

        } else {
            displayMsg = Messages.ACCESS_DENIED;
        }

        xml.append("<EntityType>").append(type).append("</EntityType>\n");
        xml.append("<FileId>").append(fileId).append("</FileId>\n");
        xml.append("<AdminAction>").append(this.action).append("</AdminAction>\n");
        xml.append("<Display>").append(displayMsg).append("</Display>\n");

        xml.append(this.xmlEnd());

        System.out.println(xml);
        String xsl = conf.DISPLAY_XSL;
        try {
            XMLTransform xmlTrans = new XMLTransform(xml.toString());
            xmlTrans.transform(out, xsl);
        } catch (DMSException e) {
            e.printStackTrace();
        }
        out.close();
    }

    private String createCondition(String lang, String code, String name, String userOrg, String mode, String type, String status) {
        StringBuffer queryCond = new StringBuffer();

        queryCond.append("\nwhere $i//admin/id != '0'\n");
        queryCond.append("and $i//admin/saved='yes'\n");

        if (!lang.equals("0")) {
            queryCond.append("and $i//admin/lang ='" + lang + "'\n");
        }
        if (code != "") {
            queryCond.append("and $i//admin/id ='" + code + "'\n");
        }
        if (name != "") {
            String xpath = "$i" + UtilsXPaths.getSearchXpathAtName(type);
            queryCond.append("and contains(" + xpath + ",'" + name + "') \n");
        }
        if (GetEntityCategory.getEntityCategory(type).equals("primary") && status != null && status.equals("published") == false && mode.equals("sys") == false && userHasAction("sysadmin") == false) {
            queryCond.append("and ($i//admin/organization='" + userOrg + "' or $i//admin/status='published') \n");
        }

        return queryCond.toString();

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
        try {
            processRequest(request, response);
        } catch (DMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
        try {
            processRequest(request, response);
        } catch (DMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
