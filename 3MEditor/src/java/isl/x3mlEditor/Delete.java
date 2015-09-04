/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isl.x3mlEditor;

import isl.dbms.DBCollection;
import isl.dbms.DBFile;
import isl.reasoner.OntologyReasoner;
import static isl.x3mlEditor.BasicServlet.applicationCollection;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author samarita
 */
public class Delete extends BasicServlet {

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

        String xpath = request.getParameter("xpath");
        String id = request.getParameter("id");
        String type = request.getParameter("type");
        String targetAnalyzer = request.getParameter("targetAnalyzer");
        if (targetAnalyzer == null) {
            targetAnalyzer = targetPathSuggesterAlgorithm;
        }

        if (type == null) {
            type = "Mapping";
        }

//        String output = "";
        String xmlId = type + id + ".xml";
        DBCollection dbc = new DBCollection(super.DBURI, applicationCollection + "/" + type, super.DBuser, super.DBpassword);
        String collectionPath = getPathforFile(dbc, xmlId, id);
        DBFile mappingFile = new DBFile(DBURI, collectionPath, xmlId, DBuser, DBpassword);

        if (!xpath.startsWith("//")) {
            xpath = "//" + xpath;
        }

//        System.out.println("XPATH is " + xpath);
        if (xpath.contains("/intermediate[")) { //Faux element intermediate
            String position = xpath.substring(xpath.lastIndexOf("[") + 1, xpath.lastIndexOf("]"));

            if (xpath.contains("source_relation")) { //Source intermediate
                int relationPosition = Integer.parseInt(position) + 1;
                String nodePath = xpath.replaceAll("/intermediate", "/node");
                mappingFile.xRemove(nodePath);
                String relationPath = xpath.replaceAll("/intermediate\\[\\d+\\]", "/relation[" + relationPosition + "]");
                mappingFile.xRemove(relationPath);
            } else { //Target intermediate
                int relationshipPosition = Integer.parseInt(position) + 1;
                String entityPath = xpath.replaceAll("/intermediate", "/entity");
                mappingFile.xRemove(entityPath);
                String relationshipPath = xpath.replaceAll("/intermediate\\[\\d+\\]", "/relationship[" + relationshipPosition + "]");
                mappingFile.xRemove(relationshipPath);
            }
        } else if (xpath.endsWith("/equals") || xpath.endsWith("/exists") || xpath.endsWith("/narrower")) {

//            System.out.println("NEW CODE!");
//            System.out.println("PATH=" + xpath);
            String rootIfTagPath = xpath.substring(0, xpath.indexOf("/if"));

            String grandpaTagQuery = xpath + "/../../name()";
            String grandpaTag = mappingFile.queryString(grandpaTagQuery)[0];
//            System.out.println("GRANDPA=" + grandpaTag);

            if (grandpaTag.equals("and") || grandpaTag.equals("or") || grandpaTag.equals("not")) {

                String ifBlockPath = "/../..";
                if (grandpaTag.equals("not")) {
                    ifBlockPath = "/../../../..";
                }

                String countQuery = "count(" + xpath + ifBlockPath + "/if)";
                String ifCount = mappingFile.queryString(countQuery)[0];
//                System.out.println("IFCOUNT=" + ifCount);

                if (ifCount.equals("1")) { //Last if, should delete entire or block instead
                    mappingFile.xRemove(xpath + ifBlockPath);
                } else if (ifCount.equals("2")) {
                    String xpathToRemove = xpath.substring(0, xpath.lastIndexOf("/"));

                    if (grandpaTag.equals("not")) {
                        xpathToRemove = xpath.substring(0, xpath.lastIndexOf("/not"));

                    }
//                    System.out.println("REMOVING:" + xpathToRemove);

                    mappingFile.xRemove(xpathToRemove);
                    String xpathToMove = xpathToRemove.substring(0, xpathToRemove.lastIndexOf("["));
//                    System.out.println("MOVING FROM:" + xpathToMove);
//                    System.out.println("MOVING TO:" + xpathToMove + "/../..");

                    mappingFile.xCopyInside(xpathToMove + "/*", xpathToMove + "/../..");
                    xpathToRemove = xpathToMove.substring(0, xpathToMove.lastIndexOf("/"));
                    mappingFile.xRemove(xpathToRemove);

                } else {
                    if (grandpaTag.equals("not")) {
                        mappingFile.xRemove(xpath + "/../../..");
                    } else {
                        mappingFile.xRemove(xpath + "/..");
                    }
                }

//            } else if (grandpaTag.equals("not")) {
//
//                String countQuery = "count(" + xpath + "/../../../../if)";
//                String ifCount = mappingFile.queryString(countQuery)[0];
//                System.out.println("IFs=" + ifCount);
                //mappingFile.xRemove(xpath + "/../../..");
            } else {
                mappingFile.xRemove(xpath + "/..");
            }

            //New approach (return entire if block instead)
            String[] entireIfBlock = mappingFile.queryString(rootIfTagPath + "/if");
//            System.out.println("ROOTIF=" + rootIfTagPath);
            if (entireIfBlock != null && entireIfBlock.length > 0) {
                String frag = entireIfBlock[0];
                frag = frag.replaceFirst("/?>", " targetMode='' container='" + rootIfTagPath + "' xpath='" + rootIfTagPath + "' relPos=''$0");

                String output = transform(frag, super.baseURL + "/xsl/edit/if-rule.xsl");

                out.println(output);
            }

        } else {

            mappingFile.xRemove(xpath);
        }
        if (xpath.startsWith("//x3ml/info/target_info")) { //Special case, need to delete relative namespace (+2)

            if (targetAnalyzer.equals("3")) { //Deleting target schema means replacing ont model
                OntologyReasoner ont = getOntModel(mappingFile, id);

                HttpSession session = sessionCheck(request, response);
                if (session == null) {
                    session = request.getSession();
                }
                session.setAttribute("modelInstance_" + id, ont);
            }

            String position = xpath.substring(xpath.lastIndexOf("[") + 1, xpath.lastIndexOf("]"));

            if (position != null) {
                int namespacePos = Integer.parseInt(position) + 2;
                mappingFile.xRemove("//namespaces/namespace[position()=" + namespacePos + "]");
            }
        }

//        mappingFile.xRemove(xpath);
//                    if (xpath.endsWith("/target_schema/@schema_file") && mode == 3) { //Deleting target schema means replacing ont model
//                        ArrayList<OntModel> modelList = getOntModel(mappingFile, id);
//
//                        HttpSession session = sessionCheck(request, response);
//                        if (session == null) {
//                            session = request.getSession();
//                        }
//                        session.setAttribute("modelList_" + id, modelList);
//                    }
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
