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
package isl.FIMS.servlet.versions;

import isl.FIMS.utils.Messages;
import isl.FIMS.utils.entity.XMLEntity;
import isl.FIMS.servlet.ApplicationBasicServlet;
import isl.FIMS.utils.entity.Config;
import isl.FIMS.utils.UtilsXPaths;
import isl.dbms.DBCollection;
import isl.dbms.DBFile;
import isl.dms.DMSException;
import isl.dms.xml.XMLTransform;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author konsolak
 */
public class ViewVersions extends ApplicationBasicServlet {

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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        this.initVars(request);
        String displayMsg = "";
        String type = request.getParameter("type");
        Config conf = new Config(type);
        StringBuffer xml = new StringBuffer(this.xmlStart(this.topmenu, this.username, this.pageTitle, this.lang, "", request));
        String fileId = request.getParameter("id");
        String id = fileId.split(type)[1];

        String xsl = "";

        XMLEntity xmlE = new XMLEntity(this.DBURI, this.systemDbCollection + type, this.DBuser, this.DBpassword, type, fileId);

        try {
            StringBuffer outputsTag = new StringBuffer();
            StringBuffer resultsTag = new StringBuffer("<results>\n");

            DBCollection versionColOfId = new DBCollection(this.DBURI, this.versionDbCollection + type + "/" + fileId, this.DBuser, this.DBpassword);
            String query = "for $i in collection('" + versionColOfId.getName() + "')\n"
                    + "where util:document-name($i) = '" + fileId + ".xml'\n"
                    + "order by $i//admin//versionId/text() descending\n"
                    + "return\n"
                    + "<result>\n"
                    + "<versionId>{$i//admin//versionId/text()}</versionId>\n"
                    + "<versionUser>{$i//admin//versionUser/text()}</versionUser>\n"
                    + "<versionDate>{$i//admin//versionDate/text()}</versionDate>\n"
                    + "<comment>{$i//admin//versions/comment/text()}</comment>\n"
                    + "</result>\n";

            DBFile[] queryRes = versionColOfId.query(query);
            for (int j = 0; j < queryRes.length; j++) {
                resultsTag.append(queryRes[j]).append("\n");
            }
            resultsTag.append("\n</results>\n");
            outputsTag.append("<outputs>\n");
            outputsTag.append("<path>").append("VersionId").append("</path>\n")
                    .append("<path>").append("User").append("</path>\n")
                    .append("<path>").append("date").append("</path>\n")
                    .append("<path>").append("Sxolio").append("</path>\n");
            outputsTag.append("</outputs>\n");
            xml.append("<query>\n");
            xml.append(outputsTag);
            xml.append(resultsTag);
            xml.append("</query>\n");
            String xpathName = UtilsXPaths.getSearchXpathAtName(type);
            String nameValue = xmlE.queryString(xpathName + "/text()")[0];
            xml.append("<nameValue>").append(nameValue).append("</nameValue>\n");

            xml.append("<EntityType>").append(type).append("</EntityType>\n");
            xml.append("<FileId>").append(fileId).append("</FileId>\n");
            xsl = conf.LIST_VERSIONS;

        } catch (isl.dbms.DBMSException e) {
            displayMsg = Messages.NO_Versions;
            xsl = conf.DISPLAY_XSL;
        }

        xml.append("<Display>").append(displayMsg).append("</Display>\n");

        xml.append(this.xmlEnd());
        try {
            XMLTransform xmlTrans = new XMLTransform(xml.toString());
            xmlTrans.transform(out, xsl);
        } catch (DMSException e) {
            e.printStackTrace();
        }
        out.close();

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
