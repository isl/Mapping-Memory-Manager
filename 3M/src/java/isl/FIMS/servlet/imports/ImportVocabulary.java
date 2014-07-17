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
package isl.FIMS.servlet.imports;

import isl.FIMS.utils.Messages;
import isl.FIMS.servlet.ApplicationBasicServlet;
import isl.FIMS.utils.Vocabulary;
import isl.FIMS.utils.entity.Config;
import isl.FIMS.utils.Utils;
import isl.dms.DMSConfig;
import isl.dms.DMSException;
import isl.dms.xml.XMLTransform;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;

/**
 *
 * @author konsolak
 */
public class ImportVocabulary extends ApplicationBasicServlet {

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
    private BufferedReader input;
    private static final int REQUEST_SIZE = 10000 * 1024;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.initVars(request);

        Config conf = new Config("AdminVoc");

        String displayMsg = "";
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        StringBuilder xml = new StringBuilder(this.xmlStart(this.topmenu, this.username, this.pageTitle, this.lang, "", request));
        String file = request.getParameter("file");

        if (!ServletFileUpload.isMultipartContent(request)) {
            displayMsg = "form";
        } else {
            // configures some settings
            String filePath = this.export_import_Folder;
            java.util.Date date = new java.util.Date();
            Timestamp t = new Timestamp(date.getTime());
            String currentDir = filePath + t.toString().replaceAll(":", "");
            File saveDir = new File(currentDir);
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD);
            factory.setRepository(saveDir);

            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setSizeMax(REQUEST_SIZE);
            // constructs the directory path to store upload file
            String uploadPath = currentDir;

            try {
                // parses the request's content to extract file data
                List formItems = upload.parseRequest(request);
                Iterator iter = formItems.iterator();
                // iterates over form's fields
                File storeFile = null;
                while (iter.hasNext()) {
                    FileItem item = (FileItem) iter.next();
                    // processes only fields that are not form fields
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        filePath = uploadPath + File.separator + fileName;
                        storeFile = new File(filePath);
                        // saves the file on disk
                        item.write(storeFile);
                    }
                }
                ArrayList<String> content = Utils.readFile(storeFile);
                DMSConfig vocConf = new DMSConfig(this.DBURI, this.systemDbCollection + "Vocabulary/", this.DBuser, this.DBpassword);
                Vocabulary voc = new Vocabulary(file, this.lang, vocConf);
                String[] terms = voc.termValues();

                for (int i = 0; i < content.size(); i++) {
                    String addTerm = content.get(i);
                    if (!Arrays.asList(terms).contains(addTerm)) {
                        voc.addTerm(addTerm);
                    }

                }
                displayMsg = Messages.ACTION_SUCCESS;
                Utils.deleteDir(currentDir);
                System.out.println("Upload has been done successfully!");
            } catch (Exception ex) {
                System.out.println("message: There was an error: " + ex.getMessage());
            }

        }

        xml.append("<Display>").append(displayMsg).append("</Display>\n");
        xml.append("<FileName>").append(file).append("</FileName>\n");
        xml.append("<EntityType>").append("AdminVoc").append("</EntityType>\n");

        xml.append(this.xmlEnd());
        String xsl = conf.IMPORT_Vocabulary;
        try {
            XMLTransform xmlTrans = new XMLTransform(xml.toString());
            xmlTrans.transform(out, xsl);
        } catch (DMSException e) {
            e.printStackTrace();
        }
        out.close();
    }

    private DiskFileItemFactory setupFileItemFactory(File repository, ServletContext context) {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD);
        factory.setRepository(repository);

        FileCleaningTracker pTracker = FileCleanerCleanup.getFileCleaningTracker(context);
        factory.setFileCleaningTracker(pTracker);

        return factory;
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
