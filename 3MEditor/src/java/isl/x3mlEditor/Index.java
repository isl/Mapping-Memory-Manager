/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isl.x3mlEditor;

import isl.dbms.DBCollection;
import isl.dbms.DBFile;
import isl.x3mlEditor.utilities.Utils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author samarita
 */
public class Index extends BasicServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String output = request.getParameter("output");

        if (output == null || output.equals("") || output.equals("html")) {
            response.setContentType("text/html;charset=UTF-8");
            output = "html";
        } else {
            response.setContentType("text/xml;charset=UTF-8");

        }
        PrintWriter out = response.getWriter();
        this.servletParams(request, response);

        String id = request.getParameter("id");

        String type = request.getParameter("type");
        String file = request.getParameter("file");
        String lang = request.getParameter("lang");
        String action = request.getParameter("action");
        String sourceAnalyzer = request.getParameter("sourceAnalyzer");

        String sourceAnalyzerFiles = "***";
        String targetAnalyzer = request.getParameter("targetAnalyzer");
        if (targetAnalyzer == null) {
            targetAnalyzer = targetPathSuggesterAlgorithm;
        }

        if (action == null) {
            action = "edit";
        }

        if (lang == null && type == null && id == null && file == null) {
            response.sendRedirect("Index?id=1");
            return;

        }
        if (lang == null) {
            lang = "en";
        }
        if (id == null) {
            id = "1";
        }
        if (type == null) {
            type = "Mapping";
        }
        id = id.replace(type, "");
        StringBuilder xmlMiddle = new StringBuilder();
        if (output.equals("html")) {
            xmlMiddle.append("<output><xml>");
        }
        String xmlId = type + id + ".xml";
        DBCollection dbc = new DBCollection(super.DBURI, applicationCollection + "/" + type, super.DBuser, super.DBpassword);

        //Cleanup code 
        String duplicateInstanceInfoQuery = "//entity[count(instance_info)>1]/instance_info[position()>1]";
        dbc.xRemove(duplicateInstanceInfoQuery);
        dbc.xRemove("//text");
        dbc.xRemove("//if[not(*)]");

        String collectionPath = getPathforFile(dbc, xmlId, id);

        xmlMiddle.append(getDBFileContent(collectionPath, xmlId));
        if (output.equals("html")) {
            xmlMiddle.append("</xml>");
        }
        if (output.equals("html")) {

            String xsl = super.baseURL + "/xsl/x3ml.xsl";
            if (stateOfSite.equals("off")) {
                response.sendRedirect("maintenance.html");
                return;
            }

            //Actions: 0=edit, 1=view, 2=instance
            if (action.equals("instance")) {
                xmlMiddle.append("<viewMode>").append("2").append("</viewMode>");
                xmlMiddle.append("<generator>").append("instance").append("</generator>");

                HttpSession session = sessionCheck(request, response);
                if (session == null) {
                    session = request.getSession(true);
//                    session.setAttribute("resourcesList", new HashMap<String, String[]>());
                    session.setAttribute("id", id);
                    session.setAttribute("action", action);

                    DBFile mappingFile = new DBFile(DBURI, collectionPath, xmlId, DBuser, DBpassword);

                    String[] usernames = mappingFile.queryString("//admin/locked/string()");
                    if (usernames.length > 0) {
                        session.setAttribute("username", usernames[0]);
                    }

                }

            } else if (action.equals("view")) {
                xmlMiddle.append("<viewMode>").append("1").append("</viewMode>");
            } else if (action.equals("edit")) {
                xmlMiddle.append("<viewMode>").append("0").append("</viewMode>");

                //Components
                DBFile mappingFile = new DBFile(DBURI, collectionPath, xmlId, DBuser, DBpassword);

                if (mappingFile.queryString("//target_info/target_schema/@schema_file/string()").length == 0) {
                    targetAnalyzer = "0"; //If no target schemas specified, then choose None!
                }

//                if (sourceAnalyzer == null) {
                String sourceQuery = "let $i := //source_info/source_schema/@schema_file\n"
                        + "let $k := //example_data_source_record/@xml_link\n"
                        + "return\n"
                        + "<sourceAnalyzer>\n"
                        + "{$i}\n"
                        + "{$k}\n"
                        + "</sourceAnalyzer>";
//                System.out.println(sourceQuery);
                String[] results = mappingFile.queryString(sourceQuery);
//                System.out.println("LEN=" + results.length);
                if (results.length == 1) { //First check for source schema
                    String res = results[0];
//                    System.out.println(res);
                    if (!res.equals("<sourceAnalyzer/>")) { //Something exists...
                        String schemaFile = new Utils().getMatch(res, "(?<=schema_file=\").*?(?=\")");
                        String instanceFile = new Utils().getMatch(res, "(?<=xml_link=\").*?(?=\")");
                        if (!(schemaFile.endsWith(".xsd") || schemaFile.endsWith(".xml"))) {
                            schemaFile = "";
                        }
                        if (!(instanceFile.endsWith(".xml"))) {
                            instanceFile = "";
                        }

                        sourceAnalyzerFiles = schemaFile + "***" + instanceFile;
                        if (sourceAnalyzerFiles.equals("***")) {
                            sourceAnalyzer = "off";
                        }

                    } else {
                        sourceAnalyzer = "off";
                    }
                }
//                }

                if (sourceAnalyzer == null) {
                    sourceAnalyzer = sourceAnalyzerStatus;
                }

//                System.out.println("SOURCE_ANAL=" + sourceAnalyzer);
                HttpSession session = sessionCheck(request, response);
                if (session == null) {
                    session = request.getSession(true);
                    session.setAttribute("resourcesList", new HashMap<String, String[]>());
                    session.setAttribute("id", id);
                    session.setAttribute("action", action);

                    String[] usernames = mappingFile.queryString("//admin/locked/string()");
                    if (usernames.length > 0) {
                        session.setAttribute("username", usernames[0]);
                    }

                }
            }

            xmlMiddle.append("<lang>").append(lang).append("</lang>");
            xmlMiddle.append("<editorType>").append(editorType).append("</editorType>");

            if (file != null) {
                xmlMiddle.append("<file>").append(file).append("</file>");
            } else {
                xmlMiddle.append("<sourceAnalyzer>").append(sourceAnalyzer).append("</sourceAnalyzer>");
                xmlMiddle.append("<sourceAnalyzerFiles>").append(sourceAnalyzerFiles).append("</sourceAnalyzerFiles>");

                xmlMiddle.append("<targetAnalyzer>").append(targetAnalyzer).append("</targetAnalyzer>");
                xmlMiddle.append("<type>").append(type).append("</type>");
                xmlMiddle.append("<id>").append(id).append("</id>");
            }

            xmlMiddle.append("</output>");
//            if (action.equals("edit") && mode == 3) {
//
//                DBFile mappingFile = new DBFile(DBURI, collectionPath, xmlId, DBuser, DBpassword);
//                ArrayList<OntModel> modelList = getOntModel(mappingFile, id);
//
//                HttpSession session = sessionCheck(request, response);
//                if (session == null) {
//                    session = request.getSession();
//                }
//                session.setAttribute("modelList_" + id, modelList);
//
//            }
//             System.out.println(xmlMiddle);
            out.write(transform(xmlMiddle.toString(), xsl));

        } else {
            String content = xmlMiddle.toString();
            content = content.replace("<?xml-stylesheet type=\"text/xsl\" href=\"crm_mapping-v2.0.xsl\"?>", "");
            content = content.replaceAll("(?s)<admin>.*?</admin>", "");
            out.write(content);
        }

        out.close();

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
