/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isl.x3mlEditor;

import isl.dbms.DBCollection;
import isl.dbms.DBFile;
import static isl.x3mlEditor.BasicServlet.applicationCollection;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author samarita
 */
public class GetPart extends BasicServlet {

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
        PrintWriter out = response.getWriter();

        String id = request.getParameter("id");
        String part = request.getParameter("part");
        String type = request.getParameter("type");
        String mode = request.getParameter("mode");
        String xpath = request.getParameter("xpath");
        String targetAnalyzer = request.getParameter("targetAnalyzer");
        String sourceAnalyzer = request.getParameter("sourceAnalyzer");

        int targetMode;
        if (targetAnalyzer == null) {
            targetAnalyzer = "2";
        }

        if (sourceAnalyzer == null) {
            sourceAnalyzer = "off";
        }
        targetMode = Integer.parseInt(targetAnalyzer);

//        System.out.println("TARGET=" + targetAnalyzer);
        if (type == null) {
            type = "Mapping";
        }

        String xmlId = type + id + ".xml";
        DBCollection dbc = new DBCollection(DBURI, applicationCollection + "/" + type, DBuser, DBpassword);
        String collectionPath = getPathforFile(dbc, xmlId, id);
        DBFile mappingFile = new DBFile(DBURI, collectionPath, xmlId, DBuser, DBpassword);
        String query = "";
        String xsl = "";
        if (part == null) { //using xpath instead

            if (xpath != null) {
//                System.out.println("X="+xpath);
                query = xpath;
                if (mode.equals("edit")) {
                    if (xpath.endsWith("domain")) {
                        xsl = baseURL + "/xsl/edit/domain.xsl";
                    } else {
                        xsl = baseURL + "/xsl/edit/link.xsl";

                    }
                } else {
                    if (xpath.endsWith("path")) {
                        xsl = baseURL + "/xsl/path.xsl";
                    } else if (xpath.endsWith("range")) {
                        xsl = baseURL + "/xsl/range.xsl";
                    } else if (xpath.endsWith("domain")) {
                        xsl = baseURL + "/xsl/domain.xsl";
                    } else if (xpath.endsWith("mappings")) {
                        xsl = baseURL + "/xsl/mappings.xsl";
                    }

                }
            }

        } else {

            if (part.equals("info")) { //Need to add extra info from x3ml tag
                query = "for $i in //x3ml/info\n"
                        + "return\n"
                        + "<x3ml>\n"
                        + "{$i/../@version}\n"
                        + "{$i/../@source_type}\n"
                        + "{$i}\n"
                        + "{$i/../namespaces}\n"
                        + "</x3ml>";
            } else {
                query = "//" + part;
            }
            xsl = baseURL + "/xsl/edit/" + part + ".xsl";
            if (mode.equals("view")) {
                xsl = baseURL + "/xsl/" + part + ".xsl";

            }
        }
//        System.out.println(query);

        String[] queryResults = mappingFile.queryString(query);
        String output = "";
        if (queryResults != null && queryResults.length > 0 && baseURL != null) {
            String result = queryResults[0];
            if (result.startsWith("<link>")) { //Edit mode
//                result = result.replaceFirst("<link>", "<link xpath='" + xpath + "'>");
                result = result.replaceFirst("<path>", "<path sourceAnalyzer='" + sourceAnalyzer + "' targetMode='" + targetMode + "' xpath='" + xpath + "/path" + "'>");
                result = result.replaceFirst("<range>", "<range sourceAnalyzer='" + sourceAnalyzer + "' targetMode='" + targetMode + "' xpath='" + xpath + "/range" + "'>");

            } else if (result.startsWith("<domain>")) { //Edit mode
                query = "count(//mapping)";
                queryResults = mappingFile.queryString(query);
                result = result.replaceFirst("<domain", "<domain sourceAnalyzer='" + sourceAnalyzer + "' targetMode='" + targetMode + "' xpath='" + xpath + "' mappingsCount='" + queryResults[0] + "'");
            }
//            System.out.println("RES=" + result);
//            System.out.println(xsl);
            output = transform(result, xsl);

            if (output != null) {
                if (output.contains("mapping[]") || output.contains("link[]")) { //Special case! Need to find out position
                    ArrayList<String> mappings = findReg("mapping\\[\\d+\\]", xpath, 0);
                    if (!mappings.isEmpty()) {
                        output = output.replaceAll("mapping\\[\\]", mappings.get(0));
                    }
                    ArrayList<String> links = findReg("link\\[\\d+\\]", xpath, 0);
                    if (!links.isEmpty()) {
                        output = output.replaceAll("link\\[\\]", links.get(0));
                    }
                }
            }

//            System.out.println("OUT="+output);
//            System.out.println(xpath);
        } else {
//                    System.out.println("BAD="+query);

            output = "Something went wrong! Please reload page.";
        }

//        System.out.println(mappingFile.queryString(query)[0]);
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println(output);
        } finally {
            out.close();
        }
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
