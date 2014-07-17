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
package isl.FIMS.servlet.versions;

import isl.FIMS.utils.entity.GetEntityCategory;
import isl.FIMS.utils.Messages;
import isl.FIMS.utils.entity.XMLEntity;
import isl.FIMS.servlet.ApplicationBasicServlet;
import isl.FIMS.utils.entity.Config;
import isl.FIMS.utils.Utils;
import isl.dbms.DBCollection;
import isl.dbms.DBFile;
import isl.dms.DMSException;
import isl.dms.xml.XMLTransform;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author konsolak
 */
public class CreateVersion extends ApplicationBasicServlet {

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
        String action = request.getParameter("action");
        Config conf = new Config(type);
        StringBuffer xml = new StringBuffer(this.xmlStart(this.topmenu, this.username, this.pageTitle, this.lang, "", request));
        boolean hasAccess = true;
        String fileId = request.getParameter("id");
        String id = fileId.split(type)[1];
        XMLEntity xmlE = new XMLEntity(this.DBURI, this.systemDbCollection + type, this.DBuser, this.DBpassword, type, fileId);
        String xsl = "";

        if (GetEntityCategory.getEntityCategory(type).equals("primary")) {
            boolean userCanWrite = xmlE.hasAdminProperty("write", this.username);
            if (!userCanWrite) {
                displayMsg = Messages.ACCESS_DENIED;
                hasAccess = false;
                xsl = conf.DISPLAY_XSL;
                xml.append("<Display>").append(displayMsg).append("</Display>\n");

            }
        }
        if (hasAccess) {
            if (action.equals("insertComment")) {
                String comment = "";
                int lastVersionId = Integer.parseInt(xmlE.queryString("//admin//versions/versionId/text()")[0]) - 1;
                if (lastVersionId >= 1) {
                    String versionCol = this.versionDbCollection + type + "/" + fileId + "/" + lastVersionId;
                    DBFile versionDBF = new DBFile(this.DBURI, versionCol, fileId + ".xml", this.DBuser, this.DBpassword);
                    String[] comments = versionDBF.queryString2("//admin/versions/comment/text()");
                    if (comments.length > 0) {
                        comment = comments[0] + " ";
                    }
                }
                xsl = conf.comments_versions;
                xml.append("<Comment>").append(comment).append("</Comment>\n");
                xml.append("<EntityType>").append(type).append("</EntityType>\n");
                xml.append("<FileId>").append(fileId).append("</FileId>\n");

            } else if (action.equals("create")) {
                DBCollection versionCol = new DBCollection(this.DBURI, this.versionDbCollection + type, this.DBuser, this.DBpassword);
                String comment = request.getParameter("comment");
                Utils u = new Utils();
                ArrayList<String> allPrevious = new ArrayList();
                allPrevious.add(type + id);
                ArrayList<DBFile> l = u.findDependats(xmlE.getDbFile(), id, type, this.DBURI, this.DBuser, this.DBpassword, id, type, allPrevious);

                String versionId = xmlE.queryString("//admin/versions/versionId/text()")[0];
                int newVersionId = Integer.parseInt(versionId) + 1;
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                xmlE.setAdminProperty("/versionDate", dateFormat.format(date));
                xmlE.setAdminProperty("/versionUser", this.username);
                xmlE.xAppend("//admin/versions", "<comment/>");
                xmlE.xAppend("//admin/versions/comment", comment);
                DBCollection fileCollection = versionCol.createCollection(fileId);
                DBCollection versionIdCol = fileCollection.createCollection(versionId);
                xmlE.getDbFile().storeInto(versionIdCol);
                xmlE.setAdminProperty("/versionId", Integer.toString(newVersionId));
                xmlE.xRemove("//admin/versions/comment");
                for (int i = 0; i < l.size(); i++) {
                    l.get(i).storeInto(versionIdCol);
                    //System.out.println("filename " + l.get(i).getName());
                }
                xsl = conf.DISPLAY_XSL;
                displayMsg = Messages.ACTION_SUCCESS;
                xml.append("<Display>").append(displayMsg).append("</Display>\n");

            }
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
