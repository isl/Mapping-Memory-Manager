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
import isl.FIMS.utils.ParseXMLFile;
import isl.FIMS.utils.Utils;
import isl.dbms.DBCollection;
import isl.dbms.DBFile;
import java.io.*;
import java.sql.Timestamp;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class ExportTemplate extends ApplicationBasicServlet {

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
            throws ServletException, IOException, SAXException, TransformerConfigurationException {
        this.initVars(request);

        try {
            String filePath = this.export_import_Folder;

            java.util.Date date = new java.util.Date();
            Timestamp t = new Timestamp(date.getTime());
            String currentDir = filePath + t.toString().replaceAll(":", "");
            File saveDir = new File(currentDir);
            saveDir.mkdir();


            response.setContentType("application/octet-stream");
            ServletOutputStream outStream = response.getOutputStream();
            request.setCharacterEncoding("UTF-8");
            String type = request.getParameter("type");
            DBCollection col = new DBCollection(this.DBURI, this.systemDbCollection + type, this.DBuser, this.DBpassword);
            DBFile dbf = col.getFile(type + ".xml");
            String xmlToString = dbf.getXMLAsString();
            xmlToString = "<?xml version=\"1.0\"?>" + "\n" + xmlToString;
            File file = new File(currentDir + "/" + type + ".xml");

            BufferedWriter outTemp = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(currentDir + "/" + type + ".xml"), "UTF-8"));
            outTemp.write(xmlToString);
            outTemp.close();
            /*Remove admin part from template*/
             Document doc = ParseXMLFile.parseFile(currentDir + "/" + type + ".xml");
             Element root = doc.getDocumentElement();
             Node admin = root.getElementsByTagName("admin").item(0);
             root.removeChild(admin);
             ParseXMLFile.saveXMLDocument(currentDir + "/" + type + ".xml", doc);

            response.setHeader("Content-Disposition", "attachment; filename=\"" + type + ".xml\"");
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
        } catch (Exception ex) {
            ex.printStackTrace();
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
        try {
            processRequest(request, response);
        } catch (SAXException ex) {
        } catch (TransformerConfigurationException ex) {
        }
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
        try {
            processRequest(request, response);
        } catch (SAXException ex) {
        } catch (TransformerConfigurationException ex) {
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
