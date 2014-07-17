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
package isl.FIMS.servlet.upload;

import isl.FIMS.servlet.ApplicationBasicServlet;
import isl.FIMS.utils.UtilsQueries;
import isl.dbms.DBCollection;
import isl.dbms.DBFile;
import isl.xdms.depository.DatabaseDepository;
import isl.xdms.depository.Depository;
import isl.xdms.depository.DiskDepository;
import java.io.*;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utilities.resizer.resize_image;

public class UploadBinFile extends ApplicationBasicServlet {

    private Depository depository;
    private InputStream input;

    private void processRequest(HttpServletRequest request, HttpServletResponse response) {

        try {
            response.setContentType("text/html;charset=UTF-8");
            request.setCharacterEncoding("UTF-8");
            String file = request.getParameter("file");
            // System.out.println("PARAM="+file);

            String depository = request.getParameter("depository");
            String type = request.getParameter("type");
            String xmlFile = request.getParameter("XML");
            String id = xmlFile.substring(xmlFile.indexOf(type) + type.length(), xmlFile.lastIndexOf(".xml"));
            DBCollection col = new DBCollection(this.DBURI, this.systemDbCollection + type, this.DBuser, this.DBpassword);
            String collectionPath = UtilsQueries.getPathforFile(col, xmlFile, id);
            // get input stream from request and read all the contents
            this.input = request.getInputStream();
            System.out.println("this.input: "+ this.input);
            // create an output stream to handle the byte stream
            ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();

            // write data to ByteArrayInputStream
            this.write(bytesOut);

//            System.out.println(xmlFile);
//            System.out.println(file);
            DBFile uploadsDBFile = new DBFile(this.DBURI, adminDbCollection, this.uploadsFile, this.DBuser, this.DBpassword);
            String mime = findMime(uploadsDBFile, file);
        
            if (mime == null) {
                mime = "Other";
            }

            // instantiate the appropriate Depository object...
            this.depository = this.getDepository(depository, type, mime);
            // call the Depository method for uploading the byteStream with the given filename
            this.depository.uploadBinDocument(file, bytesOut.toByteArray());

            System.out.println(this.depository.getUploadDirectory());

            if (mime.equals("Photos")) {
                //Added by samarita...
                System.out.println("Photos-------------------------------");
                resizeImage(type, file, mime + "\\thumbs", 73);
                resizeImage(type, file, mime + "\\normal", 250);
            }
            System.out.println("----->1 "+adminDbCollection + type);
            System.out.println("----->2 "+xmlFile);
            //DBFile XMLfile = new DBFile(this.DBURI, systemDbCollection+type, xmlFile ,this.DBuser, this.DBpassword);

            DBFile XMLfile = new DBFile(this.DBURI, collectionPath, xmlFile, this.DBuser, this.DBpassword);
            XMLfile.xUpdate("//admin/type", mime);



        } catch (Exception e) {
        }
    }

    private String findMime(DBFile uploads, String file) {

        file = file.substring(file.lastIndexOf(".") + 1);
        file = file.toLowerCase();
        //System.out.println("//mime[type='"+file+"']/../name()");

        String[] mimes = uploads.queryString("//mime[type='" + file + "']/../name()");
        if (mimes.length == 0) {
            return "Other";
        } else {
            return mimes[0];
        }


    }

    private boolean resizeImage(String type, String file, String folder, int pixels) {
        resize_image img = new resize_image();

        img.set_dir_location(this.systemUploads + type + "\\Photos\\original", this.systemUploads + type + "\\" + folder);
       img.set_width_or_height(pixels, img.HEIGHT);
        return img.resize_imageFile(file);
    }

    /**
     * Internal method for getting the correct depository, depending on the
     * specified type.
     *
     * @param type the type of the depository.
     * @return a
     * <code>Depository</code> instance.
     */
    private Depository getDepository(String depository, String type, String mime) throws Exception {
        // find the type of remote depository to handle with...
        if (depository.equals("database")) {
            return new DatabaseDepository(this.DBURI, this.systemDbCollection + type, this.DBuser, this.DBpassword);
        } else if (depository.equals("disk")) {
            String pathSoFar;
            if (mime.equals("Photos")) {
                pathSoFar = this.systemUploads + type + "\\" + mime + "\\original\\";
            } else {
                pathSoFar = this.systemUploads + type + "\\" + mime + "\\";
            }
            return new DiskDepository(pathSoFar + "\\");
        } //            return new DiskDepository(this.systemUploads+type+"\\");
        else {
            throw new IllegalArgumentException("String argument must be one of the: \"database\", \"disk\"");
        }
    }

    /**
     * Internal method to write this file part; doesn't check to see if it has
     * contents first.
     *
     * @return number of bytes written.
     * @exception IOException	if an input or output exception has occurred.
     */
    private long write(OutputStream out) throws IOException {
        long size = 0;
        int read;
        byte[] buf = new byte[8 * 1024];
        while ((read = this.input.read(buf)) != -1) {
            out.write(buf, 0, read);
            size += read;
        }
        return size;
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

   
}
