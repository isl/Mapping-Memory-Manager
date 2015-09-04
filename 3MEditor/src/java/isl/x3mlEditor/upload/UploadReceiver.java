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
 * This file is part of the 3MEditor webapp of Mapping Memory Manager project.
 */
package isl.x3mlEditor.upload;

//import image.utilities.resize_image;
//import com.hp.hpl.jena.ontology.OntModel;
import isl.Tidy;
import isl.dbms.DBCollection;
import isl.dbms.DBFile;
import isl.reasoner.OntologyReasoner;
import isl.x3mlEditor.BasicServlet;
import isl.x3mlEditor.utilities.Utils;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.io.File;
import java.net.URLEncoder;
import javax.servlet.http.HttpSession;

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
        String chosenAPI = req.getParameter("target");

//        int mode = Integer.parseInt(targetPathSuggesterAlgorithm);
////        System.out.println("CHOSEN=" + chosenAPI);
//        if (chosenAPI != null) {
//            mode = Integer.parseInt(chosenAPI);
//        }
        String targetAnalyzer = req.getParameter("targetAnalyzer");
        if (targetAnalyzer == null) {
            targetAnalyzer = targetPathSuggesterAlgorithm;
        }

        String fileType = "targetSchema";
        String xpath = req.getParameter("path");
        String id = req.getParameter("id");
        String filePath = "";
        RequestParser requestParser = null;
        String filename = "";
//        String mime = "";
        String msg = null;

//        TEMP_DIR = new File(uploadsFolder + id + "/uploadsTemp");
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

//        System.out.println("UPLOADED FILENAME is :" + filename);

        DBFile uploadsDBFile = new DBFile(super.DBURI, super.adminCollection, "Uploads.xml", super.DBuser, super.DBpassword);
        String mime = new Utils().findMime(uploadsDBFile, filename);

        filename = URLEncoder.encode(filename, "UTF-8");

        TEMP_DIR = new File(uploadsFolder + "uploadsTemp");

//        System.out.println(uploadsFolder + id + "/uploadsTemp");
        if (!TEMP_DIR.exists()) {
            TEMP_DIR.mkdirs();
        }

//        UPLOAD_DIR = new File(uploadsFolder + id + filePath);
        UPLOAD_DIR = new File(uploadsFolder + mime + System.getProperty("file.separator") + filePath);

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

        Tidy tidy = new Tidy(DBURI, rootCollection, DBuser, DBpassword, uploadsFolder);
        String duplicate = tidy.getDuplicate(UPLOAD_DIR + System.getProperty("file.separator") + filename, UPLOAD_DIR.getAbsolutePath());
//        System.out.println("DUPLICATE="+duplicate);
        boolean duplicateFound = false;

        if (duplicate != null) {
            new File(UPLOAD_DIR, filename).delete(); //Delete uploaded file!
            filename = duplicate;
            duplicateFound = true;
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

            mappingFile.xAddAttribute(xpath, attributeName, filename);

            if (xpath.endsWith("/target_schema") && attributeName.equals("schema_file") && (filename.endsWith("rdfs") || filename.endsWith("rdf"))) {

                if (!duplicateFound) {
                    //Uploading file to eXist!
                    dbc = new DBCollection(super.DBURI, x3mlCollection, super.DBuser, super.DBpassword);
                    DBFile dbf = dbc.createFile(filename, "XMLDBFile");
                    String content = readFile(new File(UPLOAD_DIR, filename), "UTF-8");
                    dbf.setXMLAsString(content);
                    dbf.store();
                }

                if (targetAnalyzer.equals("3")) {
//                    Changing OntModel
                    OntologyReasoner ont = getOntModel(mappingFile, id);
                    HttpSession session = sessionCheck(req, resp);
                    if (session == null) {
                        session = req.getSession();
                    }
                    session.setAttribute("modelInstance_" + id, ont);
                }

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
