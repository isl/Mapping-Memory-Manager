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
package isl.FIMS.servlet.fetch;

import isl.FIMS.servlet.ApplicationBasicServlet;
import isl.dbms.DBFile;
import isl.xdms.depository.DatabaseDepository;
import isl.xdms.depository.Depository;
import isl.xdms.depository.DiskDepository;



import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author samarita
 * @version
 */
public class FetchBinFile extends ApplicationBasicServlet {

    private Depository depository;
    private InputStream input;

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
        //    System.out.println("FetchBinFile");
        String mimeType = getServletContext().getMimeType(request.getParameter("file"));
        //System.out.println("ELANTE:"+xm);
        request.setCharacterEncoding("UTF-8");

        response.setContentType(mimeType + ";charset=UTF-8");
        response.setHeader("Content-disposition", "inline; filename=" + request.getParameter("file"));
        ServletOutputStream out = response.getOutputStream();

        try {
            // get the file and type parameters from request...
            String file = new String(request.getParameter("file"));
            String depository = request.getParameter("depository");
            String type = request.getParameter("type");
            String mime = request.getParameter("mime");
            String size = request.getParameter("size");
            DBFile uploadsDBFile = new DBFile(this.DBURI, adminDbCollection, this.uploadsFile, this.DBuser, this.DBpassword);

            if (mime.equals("Photos")) {
                // System.out.println("size="+size);
                if (size == null) {
                    size = "original";
                } else {

                    size = uploadsDBFile.queryString("//" + mime + "/sizes/" + size + "/text()")[0];
                }
            }
            if (mime.equals("audio")) {
                File file1 = new File(this.systemUploads + file);
                response.setContentLength((int) file1.length());
                // Open the file and output streams
                FileInputStream in = new FileInputStream(file1);
                out = response.getOutputStream();

                // Copy the contents of the file to the output stream
                byte[] buf = new byte[1024];
                int count = 0;
                while ((count = in.read(buf)) >= 0) {
                    out.write(buf, 0, count);
                }
                in.close();
                out.close();
            } else {
                this.depository = this.getDepository(depository, type, mime, size);
                this.depository.fetchBinDocument(this.depository.getUploadDirectory() + file, out);

            }
        } catch (Exception e) {
        }
    }

    /**
     * Internal method for getting the correct depository, depending on the
     * specified type.
     *
     * @param type the type of the depository.
     * @return a <code>Depository</code> instance.
     */
    private Depository getDepository(String depository, String type, String mime, String size) throws Exception {
        // find the type of remote depository to handle with...
        if (depository.equals("database")) {
            return new DatabaseDepository(this.DBURI, this.systemDbCollection + type, this.DBuser, this.DBpassword);
        } else if (depository.equals("disk")) {
            String pathSoFar = this.systemUploads + type + "\\" + mime + "\\";
            if (size == null) {
                return new DiskDepository(pathSoFar);
            } else {
                return new DiskDepository(pathSoFar + size + "\\");
            }
//            if (size.equals("normal")){
//                return new DiskDepository(pathSoFar+size+"\\");
//            } else if (size.equals("thumbs")){
//                return new DiskDepository(pathSoFar+size+"\\");
//            } else {
//                return new DiskDepository(pathSoFar);
//            }
        } else {
            throw new IllegalArgumentException("String argument must be one of the: \"database\", \"disk\"");
        }

    }

    private String findMime(DBFile uploads, String file) {

        file = file.substring(file.lastIndexOf(".") + 1);
        file = file.toLowerCase();
        String[] mimes = uploads.queryString("//mime[type='" + file + "']/../name()");
        if (mimes.length == 0) {
            return "Other";
        } else {
            return mimes[0];
        }


    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
// </editor-fold>
}
