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

import isl.FIMS.utils.entity.GetEntityCategory;
import isl.FIMS.utils.Messages;
import isl.FIMS.servlet.ApplicationBasicServlet;
import isl.FIMS.utils.entity.Config;
import isl.FIMS.utils.ParseXMLFile;
import isl.FIMS.utils.Utils;
import isl.FIMS.utils.UtilsXPaths;
import isl.dbms.DBCollection;
import isl.dbms.DBFile;
import isl.dms.DMSException;
import isl.dms.file.DMSTag;
import isl.dms.xml.XMLTransform;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import schemareader.SchemaFile;

/*
 * @author konsolak
 */
public class ImportXML extends ApplicationBasicServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
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

        String displayMsg = "";
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        StringBuilder xml = new StringBuilder(this.xmlStart(this.topmenu, this.username, this.pageTitle, this.lang, "", request));

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
                Document doc = ParseXMLFile.parseFile(storeFile.getPath());
                Element admin = (Element) doc.getElementsByTagName("admin").item(0);
                if (admin != null) {
                    //  Remove the node
                    admin.getParentNode().removeChild(admin);
                }
                String[] id = initInsertFile(type, false);
                File rename = new File(uploadPath + File.separator + id[0] + ".xml");
                boolean isRename = storeFile.renameTo(rename);
                // doc = createAdminPart(id[0], type, doc);
                ParseXMLFile.saveXMLDocument(rename.getPath(), doc);
                String schemaFilename = "";
                schemaFilename = type;
                SchemaFile sch = new SchemaFile(schemaFolder + schemaFilename + ".xsd");
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                StringWriter writer = new StringWriter();
                transformer.transform(new DOMSource(doc), new StreamResult(writer));
                String xmlString = writer.getBuffer().toString().replaceAll("\r", "");
                String validationMsg = sch.validate(xmlString);

                if (validationMsg.contains("δεν")) {
                    System.out.println("xmlString--->\n" + xmlString);
                    System.out.println("File Not stored to db");
                    System.out.println("Message: " + validationMsg);
                    displayMsg = Messages.NOT_VALID_IMPORT;
                } else {
                    doc = createAdminPart(id[0], type, doc);
                    writer = new StringWriter();
                    transformer.transform(new DOMSource(doc), new StreamResult(writer));
                    xmlString = writer.getBuffer().toString().replaceAll("\r", "");
                    System.out.println("Storing File");
                    DBCollection col = new DBCollection(this.DBURI, id[1], this.DBuser, this.DBpassword);
                    DBFile dbF = col.createFile(id[0] + ".xml", "XMLDBFile");
                    dbF.setXMLAsString(xmlString);
                    dbF.store();
                    displayMsg = Messages.ACTION_SUCCESS;
                }
                Utils.deleteDir(currentDir);
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("message: There was an error: " + ex.getMessage());
            }

        }

        xml.append("<Display>").append(displayMsg).append("</Display>\n");
        xml.append("<EntityType>").append(type).append("</EntityType>\n");
        xml.append(this.xmlEnd());
        Config conf = new Config("EisagwghXML_RDF");
        String xsl = conf.IMPORT_XML;
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

    private Document createAdminPart(String fileId, String type, Document doc) {
        //create admin element
        String query = "name(//admin/parent::*)";
        DBFile dbf = new DBFile(this.DBURI, this.systemDbCollection + type, type + ".xml", this.DBuser, this.DBpassword);
        String parentOfAdmin = dbf.queryString(query)[0];
        Element parentElement = (Element) doc.getElementsByTagName(parentOfAdmin).item(0);
        Element admin = doc.createElement("admin");
        parentElement.appendChild(admin);

        //create id element
        String id = fileId.split(type)[1];
        Element idE = doc.createElement("id");
        idE.appendChild(doc.createTextNode(id));
        admin.appendChild(idE);

        //create uri_id element
        String uri_name = "";
        try {
            uri_name = DMSTag.valueOf("uri_name", "target", type, this.conf)[0];
        } catch (DMSException ex) {
            ex.printStackTrace();
        }
        String uriValue = this.URI_Reference_Path + uri_name + "/" + id;
        String uriPath = UtilsXPaths.getPathUriField(type);
        Element uriId = doc.createElement("uri_id");
        uriId.appendChild(doc.createTextNode(uriValue));
        admin.appendChild(uriId);
        if (!uriPath.equals("")) {
            try {
                XPath xPath = XPathFactory.newInstance().newXPath();
                NodeList nodes = (NodeList) xPath.evaluate(uriPath,
                        doc.getDocumentElement(), XPathConstants.NODESET);
                Node oldChild = nodes.item(0);
                if (oldChild != null) {
                    oldChild.setTextContent(uriValue);
                }
            } catch (XPathExpressionException ex) {
                ex.printStackTrace();
            }
        }

        //create lang element
        Element lang = doc.createElement("lang");
        lang.appendChild(doc.createTextNode(this.lang));
        admin.appendChild(lang);

        //create organization element
        Element organization = doc.createElement("organization");
        organization.appendChild(doc.createTextNode(this.getUserGroup()));
        admin.appendChild(organization);

        //create creator element
        Element creator = doc.createElement("creator");
        creator.appendChild(doc.createTextNode(this.username));
        admin.appendChild(creator);

        //create creator element
        Element saved = doc.createElement("saved");
        saved.appendChild(doc.createTextNode("yes"));
        admin.appendChild(saved);

        //create locked element
        Element locked = doc.createElement("locked");
        locked.appendChild(doc.createTextNode("no"));
        admin.appendChild(locked);

        //create read element
        Element read = doc.createElement("read");
        read.appendChild(doc.createTextNode(this.username));
        admin.appendChild(read);

        //create write element
        Element write = doc.createElement("write");
        write.appendChild(doc.createTextNode(this.username));
        admin.appendChild(write);

        //create status element
        Element status = doc.createElement("status");
        if (GetEntityCategory.getEntityCategory(type).equals("primary")) {
            status.appendChild(doc.createTextNode("unpublished"));
        }
        admin.appendChild(status);

        //create version elemnt
        Element versions = doc.createElement("versions");

        //create versionidr elememt
        Element versionId = doc.createElement("versionId");
        versionId.appendChild(doc.createTextNode("1"));
        versions.appendChild(versionId);

        //create versionUser elememt
        Element versionUser = doc.createElement("versionUser");
        versionUser.appendChild(doc.createTextNode(this.username));
        versions.appendChild(versionUser);

        //create versionDate elememt
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Element versionDate = doc.createElement("versionDate");
        versionDate.appendChild(doc.createTextNode(dateFormat.format(date)));
        versions.appendChild(versionDate);

        admin.appendChild(versions);

        //create imported element
        Element imported = doc.createElement("imported");
        imported.appendChild(doc.createTextNode(this.username));
        admin.appendChild(imported);
        System.out.println("doc--->" + doc.getTextContent());
        return doc;
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
