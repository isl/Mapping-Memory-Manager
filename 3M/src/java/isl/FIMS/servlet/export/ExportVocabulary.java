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
import isl.FIMS.utils.entity.Config;
import isl.FIMS.utils.Utils;
import isl.dbms.DBFile;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author konsolak
 */
public class ExportVocabulary extends ApplicationBasicServlet {

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
        String displayMsg = "";
        Config conf = new Config("ExportVoc");

        String file = request.getParameter("file");

        try {
            String filePath = this.export_import_Folder;
            java.util.Date date = new java.util.Date();
            Timestamp t = new Timestamp(date.getTime());
            String currentDir = filePath + t.toString().replaceAll(":", "");
            File saveDir = new File(currentDir);
            saveDir.mkdir();
            request.setCharacterEncoding("UTF-8");
            DBFile dbf = new DBFile(this.DBURI, this.systemDbCollection + "Vocabulary/" + this.lang + "/", file, this.DBuser, this.DBpassword);
            String xmlToString = dbf.getXMLAsString();
            xmlToString = "<?xml version=\"1.0\"?>" + "\n" + xmlToString;

            ServletOutputStream outStream = response.getOutputStream();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + file);
            File tempFile = new File(currentDir + "/" + file);
            try {
                BufferedWriter outTemp = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(currentDir + "/" + file), "UTF-8"));
                outTemp.write(xmlToString);
                outTemp.close();
                response.setHeader("Content-Disposition", "attachment; filename=\"" + file + "\"");
                byte[] byteBuffer = new byte[BUFSIZE];
                DataInputStream in = new DataInputStream(new FileInputStream(tempFile));
                // reads the file's bytes and writes them to the response stream
                int bytesRead;
                while ((bytesRead = in.read(byteBuffer)) != -1) {
                    outStream.write(byteBuffer, 0, bytesRead);
                }
                in.close();
                outStream.close();

            } catch (IOException ex) {
            }
            Utils.deleteDir(currentDir);
        } catch (Exception ex) {
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
