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
package isl.FIMS.servlet.export;

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
import java.io.*;
import java.sql.Timestamp;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExportVersions extends ApplicationBasicServlet {

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
        String displayMsg = "";
        Config conf = new Config("EksagwghXML_RDF");
        boolean hasAccess = true;


        String type = request.getParameter("type");
        String fileId = request.getParameter("id");
        String id = fileId.split(type)[1];
        String collectionIDForVersions = request.getParameter("collectionID");

        XMLEntity xmlE = new XMLEntity(this.DBURI, this.versionDbCollection + type + "/" + fileId + "/" + collectionIDForVersions, this.DBuser, this.DBpassword, type, fileId);

        if (GetEntityCategory.getEntityCategory(type).equals("primary")) {
            boolean userCanWrite = xmlE.hasAdminProperty("write", this.username);
            if (!userCanWrite) {
                hasAccess = false;
            }
        }
        if (!hasAccess) {
            displayMsg = Messages.ACCESS_DENIED;
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            StringBuilder xml = new StringBuilder(this.xmlStart(this.topmenu, this.username, this.pageTitle, this.lang, "", request));
            xml.append("<Display>").append(displayMsg).append("</Display>\n");
            xml.append(this.xmlEnd());

            String xsl = conf.DISPLAY_XSL;
            try {
                XMLTransform xmlTrans = new XMLTransform(xml.toString());
                xmlTrans.transform(out, xsl);
            } catch (DMSException e) {
                e.printStackTrace();
            }
            out.close();
        } else {
            try {
                String filePath = this.export_import_Folder;
                java.util.Date date = new java.util.Date();
                Timestamp t = new Timestamp(date.getTime());
                String currentDir = filePath + t.toString().replaceAll(":", "");
                File saveDir = new File(currentDir);
                saveDir.mkdir();
                request.setCharacterEncoding("UTF-8");
                DBCollection col = new DBCollection(this.DBURI, this.versionDbCollection + type + "/" + fileId + "/" + collectionIDForVersions, this.DBuser, this.DBpassword);

                String[] files = col.listFiles();

                ServletOutputStream outStream = response.getOutputStream();
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment;filename=\"" + "Version" + collectionIDForVersions + "_" + type + id + ".zip\"");
                for (int i = 0; i < files.length; i++) {

                    try {
                        BufferedWriter outTemp = null;
                        DBFile dbf = col.getFile(files[i]);
                        String isWritable = "true";
                        if (GetEntityCategory.getEntityCategory(type).equals("primary")) {
                            isWritable = dbf.queryString2("//admin/write='" + this.username + "'" + "or //admin/status='published'")[0];
                        }
                        //check for each file if user has writes(only if type of file is primary)
                        if (isWritable.equals("true")) {
                            String xmlToString = dbf.getXMLAsString();
                            xmlToString = "<?xml version=\"1.0\"?>" + "\n" + xmlToString;
                            outTemp = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(currentDir + "/" + files[i]), "UTF-8"));
                            outTemp.write(xmlToString);
                            outTemp.close();
                        }
                    } catch (IOException ex) {
                    }
                }
                File f = new File(currentDir + "/" + "zip");
                f.mkdir();
                String zip = f.getAbsolutePath() + "/" + type + id + ".zip";
                Utils.createZip(zip, currentDir);
                Utils.downloadZip(outStream, new File(zip));

                Utils.deleteDir(currentDir);
            } catch (Exception ex) {
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
