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

import isl.FIMS.servlet.ApplicationBasicServlet;
import isl.FIMS.utils.Messages;
import isl.FIMS.utils.ParseXMLFile;
import isl.FIMS.utils.Utils;
import isl.FIMS.utils.UtilsQueries;
import isl.FIMS.utils.entity.Config;
import isl.FIMS.utils.entity.GetEntityCategory;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ExportXML extends ApplicationBasicServlet {

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
    private static final int BUFSIZE = 8192;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.initVars(request);
        boolean isGuest = this.getRights().equals("guest");
        if (!isGuest) {
            try {
                String filePath = this.export_import_Folder;
                java.util.Date date = new java.util.Date();
                Timestamp t = new Timestamp(date.getTime());
                String currentDir = filePath + t.toString().replaceAll(":", "");
                File saveDir = new File(currentDir);
                saveDir.mkdir();
                Config conf = new Config("EksagwghXML");
                String type = request.getParameter("type");
                String id = request.getParameter("id");
                request.setCharacterEncoding("UTF-8");
                DBCollection col = new DBCollection(this.DBURI, this.systemDbCollection + type, this.DBuser, this.DBpassword);
                String collectionPath = UtilsQueries.getPathforFile(col, id + ".xml", id.split(type)[1]);
                col = new DBCollection(this.DBURI, collectionPath, this.DBuser, this.DBpassword);
                DBFile dbf = col.getFile(id + ".xml");
                /*  ArrayList<DBFile> l = findDependats(dbf);

                 System.out.println("size: " + l.size());
                 for (int i = 0; i < l.size(); i++) {
                 System.out.println("file: " + l.get(i).getName());
                 }*/
                String isWritable = "true";
                if (GetEntityCategory.getEntityCategory(type).equals("primary")) {
                    isWritable = dbf.queryString2("//admin/write='" + this.username + "'" + "or //admin/status='published'")[0];
                }
                if (isWritable.equals("true")) {
                    String[] res = dbf.queryString2("//admin/refs/ref");
                    ServletOutputStream outStream = response.getOutputStream();
                    response.setContentType("application/octet-stream");
                    response.setHeader("Content-Disposition", "attachment;filename=\"" + id + ".zip\"");
                    writeFile(type, id, currentDir);
                    for (int i = 0; i < res.length; i++) {
                        Element e = Utils.getElement(res[i]);
                        String sps_type = e.getAttribute("sps_type");
                        String sps_id = e.getAttribute("sps_id");
                        sps_id = sps_type + sps_id;
                        writeFile(sps_type, sps_id, currentDir);
                    }
                    File f = new File(currentDir + "/" + "zip");
                    f.mkdir();
                    String zip = f.getAbsolutePath() + "/" + id + ".zip";
                    Utils.createZip(zip, currentDir);
                    Utils.downloadZip(outStream, new File(zip));
                } else {
                    String displayMsg = "";
                    response.setContentType("text/html;charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    displayMsg = Messages.ACCESS_DENIED;
                    StringBuilder xml = new StringBuilder(this.xmlStart(this.topmenu, this.username, this.pageTitle, this.lang, "", request));
                    xml.append("<Display>").append(displayMsg).append("</Display>\n");
                    xml.append(this.xmlEnd());

                    String xsl = conf.DISPLAY_XSL;
                    try {
                        XMLTransform xmlTrans = new XMLTransform(xml.toString());
                        xmlTrans.transform(out, xsl);
                    } catch (DMSException e) {
                    }
                    out.close();


                }
                Utils.deleteDir(currentDir);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    //write to temp File at server

    private void writeFile(String type, String id, String filePath) throws IOException {
        BufferedWriter outTemp = null;

        try {

            DBCollection col = new DBCollection(this.DBURI, this.systemDbCollection + type, this.DBuser, this.DBpassword);
            String collectionPath = UtilsQueries.getPathforFile(col, id + ".xml", id.split(type)[1]);
            col = new DBCollection(this.DBURI, collectionPath, this.DBuser, this.DBpassword);
            DBFile dbf = col.getFile(id + ".xml");
            String isWritable = "true";
            if (GetEntityCategory.getEntityCategory(type).equals("primary")) {
                isWritable = dbf.queryString2("//admin/write='" + this.username + "'" + "or //admin/status='published'")[0];
            }
            //check for each file if user has writes(only if type of file is primary)
            if (isWritable.equals("true")) {
                String xmlToString = dbf.getXMLAsString();
                xmlToString = "<?xml version=\"1.0\"?>" + "\n" + xmlToString;
                File file = new File(filePath + "/" + id + ".xml");
                outTemp = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath + "/" + id + ".xml"), "UTF-8"));
                outTemp.write(xmlToString);
                outTemp.close();
                /*Remove admin part from output xml*/
                 Document doc = ParseXMLFile.parseFile(filePath + "/" + id + ".xml");
                 Element root = doc.getDocumentElement();
                 Node admin = root.getElementsByTagName("admin").item(0);
                 root.removeChild(admin);
                 ParseXMLFile.saveXMLDocument(filePath + "/" + id + ".xml", doc);
                 
            }
        } catch (IOException ex) {
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
