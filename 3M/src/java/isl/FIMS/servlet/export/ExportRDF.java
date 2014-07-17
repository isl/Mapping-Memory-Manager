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
import isl.FIMS.servlet.ApplicationBasicServlet;
import isl.FIMS.utils.entity.Config;
import isl.FIMS.utils.Utils;
import isl.FIMS.utils.UtilsQueries;
import isl.dbms.DBCollection;
import isl.dbms.DBFile;
import isl.dms.DMSException;
import isl.dms.xml.XMLTransform;
import isl.xdms.depository.DatabaseDepository;
import isl.xdms.depository.Depository;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Timestamp;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author konsolak
 */
public class ExportRDF extends ApplicationBasicServlet {

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
        Config conf = new Config("EksagwghRDF");

        try {
            //create temp folder
            String filePath = this.export_import_Folder;
            java.util.Date date = new java.util.Date();
            Timestamp t = new Timestamp(date.getTime());
            String currentDir = filePath + t.toString().replaceAll(":", "");
            File saveDir = new File(currentDir);
            saveDir.mkdir();

            String type = request.getParameter("type");
            String id = request.getParameter("id");

            DBCollection col = new DBCollection(this.DBURI, this.systemDbCollection + type, this.DBuser, this.DBpassword);
            String collectionPath = UtilsQueries.getPathforFile(col, id + ".xml", id.split(type)[1]);
            col = new DBCollection(this.DBURI, collectionPath, this.DBuser, this.DBpassword);
            DBFile dbf = col.getFile(id + ".xml");
            BufferedWriter outTemp = null;

            String isWritable = "true";
            if (GetEntityCategory.getEntityCategory(type).equals("primary")) {
                isWritable = dbf.queryString2("//admin/write='" + this.username + "'" + "or //admin/status='published'")[0];
            }
            if (isWritable.equals("true")) {
                ServletOutputStream outStream = response.getOutputStream();
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment;filename=\"" + id + ".rdf\"");
                String xmlString = dbf.getXMLAsString();
                String xsl = conf.xml2rdf;
                XMLTransform xmlTrans = new XMLTransform(xmlString);
                String transformation = xmlTrans.transform(xsl);
                File file = new File(currentDir + "/" + id + ".xml");
                outTemp = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(currentDir + "/" + id + ".xml"), "UTF-8"));
                outTemp.write(transformation);
                outTemp.close();
                byte[] byteBuffer = new byte[BUFSIZE];
                DataInputStream in = new DataInputStream(new FileInputStream(file));
                // reads the file's bytes and writes them to the response stream
                int bytesRead;
                while ((bytesRead = in.read(byteBuffer)) != -1) {
                    outStream.write(byteBuffer, 0, bytesRead);
                }
                in.close();
                outStream.close();
                Utils.deleteDir(currentDir);

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
                    e.printStackTrace();
                }
                out.close();
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }



    }

    private Depository getDepository(String depository, String type, String file) throws Exception {
        // find the type of remote depository to handle with...
        String id = "";
        String collectionPath = "";
        id = file.substring(file.indexOf(type) + type.length(), file.lastIndexOf(".xml"));
        DBCollection col = new DBCollection(this.DBURI, this.systemDbCollection + type, this.DBuser, this.DBpassword);
        collectionPath = UtilsQueries.getPathforFile(col, file, id);
        return new DatabaseDepository(this.DBURI, collectionPath, this.DBuser, this.DBpassword);

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
