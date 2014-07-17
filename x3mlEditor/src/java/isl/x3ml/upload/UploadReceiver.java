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
 * This file is part of the x3mlEditor webapp of Mapping Memory Manager project.
 */
package isl.x3ml.upload;

//import image.utilities.resize_image;
import isl.dbms.DBCollection;
import isl.dbms.DBFile;
import isl.x3ml.BasicServlet;
import isl.x3ml.utilities.Utils;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.io.File;

public class UploadReceiver extends BasicServlet {

    private static File UPLOAD_DIR;
    private static File TEMP_DIR;
    private static String CONTENT_TYPE = "text/plain;charset=UTF-8";
    private static String CONTENT_LENGTH = "Content-Length";
    private static int RESPONSE_CODE = 200;

    //  final Logger log = LoggerFactory.getLogger(UploadReceiver.class);
    @Override
    public void init() throws ServletException {
//        UPLOAD_DIR.mkdirs();
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        String fileType = "targetSchema";
        String xpath = req.getParameter("path");
        String id = req.getParameter("id");
        String filePath = "";
        RequestParser requestParser = null;
        String filename = "";
        String mime = "";
        String msg = null;
        TEMP_DIR = new File(uploadsFolder + id + "/uploadsTemp");
        System.out.println(uploadsFolder + id + "/uploadsTemp");
        if (!TEMP_DIR.exists()) {
            TEMP_DIR.mkdirs();
        }
        try {
            if (ServletFileUpload.isMultipartContent(req)) {
                requestParser = RequestParser.getInstance(req, new MultipartUploadParser(req, TEMP_DIR, getServletContext()));
                filename = doWriteTempFileForPostRequest(requestParser);
            } else {
                requestParser = RequestParser.getInstance(req, null);
                filename = requestParser.getFilename();
            }
        } catch (Exception e) {
            System.out.println("Problem handling upload request");
            e.printStackTrace();
            filename = requestParser.getFilename();
            msg = e.getMessage();
        }

        UPLOAD_DIR = new File(uploadsFolder + id + filePath);
        System.out.println("2=" + uploadsFolder + id + filePath);
        System.out.println("FILENAME=" + filename);

        if (!UPLOAD_DIR.exists()) {
            UPLOAD_DIR.mkdirs();
        }

        String contentLengthHeader = req.getHeader(CONTENT_LENGTH);
        Long expectedFileSize = StringUtils.isBlank(contentLengthHeader) ? null : Long.parseLong(contentLengthHeader);

        resp.setContentType(CONTENT_TYPE);
        resp.setStatus(RESPONSE_CODE);

        if (!ServletFileUpload.isMultipartContent(req)) {
            writeToTempFile(req.getInputStream(), new File(UPLOAD_DIR, filename), expectedFileSize);
        }

        String xmlId = "Mapping" + id + ".xml";
        DBCollection dbc = new DBCollection(super.DBURI, applicationCollection + "/Mapping", super.DBuser, super.DBpassword);
        String collectionPath = getPathforFile(dbc, xmlId, id);
        DBFile mappingFile = new DBFile(DBURI, collectionPath, xmlId, DBuser, DBpassword);
        boolean isAttribute = false;
        String attributeName = "";
        if (pathIsAttribute(xpath)) { //Generic for attributes
            attributeName = xpath.substring(xpath.lastIndexOf("/") + 2);
            xpath = xpath.substring(0, xpath.lastIndexOf("/"));
            isAttribute = true;
        }
        if (isAttribute) {
//            System.out.println(xpath);
//            System.out.println(attributeName);

            mappingFile.xAddAttribute(xpath, attributeName, filename);

            if (xpath.endsWith("/target_schema") && attributeName.equals("schema_file") && (filename.endsWith("rdfs")||filename.endsWith("rdf"))) {
                //Uploading file to eXist!
                dbc = new DBCollection(super.DBURI, x3mlCollection, super.DBuser, super.DBpassword);
                DBFile dbf = dbc.createFile(filename, "XMLDBFile");
                String content = readFile(new File(UPLOAD_DIR, filename), "UTF-8");
                dbf.setXMLAsString(content);
                dbf.store();
            }

        } else {
            mappingFile.xUpdate(xpath, filename);
        }

        writeResponse(filename, resp.getWriter(), msg, fileType, mime);

    }

    private String doWriteTempFileForPostRequest(RequestParser requestParser) throws Exception {
        String filename = requestParser.getFilename();
        writeToTempFile(requestParser.getUploadItem().getInputStream(), new File(UPLOAD_DIR, filename), null);
        return filename;
    }

    private File writeToTempFile(InputStream in, File out, Long expectedFileSize) throws IOException {
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(out);

            IOUtils.copy(in, fos);

            if (expectedFileSize != null) {
                Long bytesWrittenToDisk = out.length();
                if (!expectedFileSize.equals(bytesWrittenToDisk)) {
                    //  log.warn("Expected file {} to be {} bytes; file on disk is {} bytes", new Object[] { out.getAbsolutePath(), expectedFileSize, 1 });
                    throw new IOException(String.format("Unexpected file size mismatch. Actual bytes %s. Expected bytes %s.", bytesWrittenToDisk, expectedFileSize));
                }
            }

            return out;
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            IOUtils.closeQuietly(fos);
        }
    }

    private void writeResponse(String filename, PrintWriter writer, String failureReason, String fileType, String mime) {
        if (failureReason == null) {
            String sourcePath = UPLOAD_DIR.getPath();

            String json = "{\"success\": true, \"filename\": \"" + filename + "\", \"mime\": \"" + mime + "\"}";
            writer.print(json);
            if (fileType.equals("photo")) {

            } else if (fileType.equals("zip")) {
                Utils utils = new Utils();
                utils.unZipIt(filename, sourcePath);
            } else if (fileType.equals("video") || fileType.equals("all")) {

                if (filename.endsWith("zip")) {
                    Utils utils = new Utils();
                    utils.unZipIt(filename, sourcePath);
                }
            }
        } else {
            writer.print("{\"error\": \"" + failureReason + "\"}");
        }
    }

     /**
     *
     * @param f
     * @param enc
     * @return
     */
    private String readFile(File f, String enc) {
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
            byte[] arr = new byte[(int) f.length()];
            in.read(arr, 0, arr.length);
            in.close();

            return new String(arr, enc);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            throw new Error("IO Error in readFile");
        }
    }
    
}