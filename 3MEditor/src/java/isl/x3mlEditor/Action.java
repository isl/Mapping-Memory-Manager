/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isl.x3mlEditor;

import isl.dbms.DBCollection;
import isl.dbms.DBFile;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author samarita
 */
public class Action extends BasicServlet {

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
        String action = request.getParameter("action");
        String type = "Mapping";
//        System.out.println("ID="+id);
//        System.out.println("XPATH="+xpath);
        String xmlId = type + id + ".xml";
        DBCollection dbc = new DBCollection(super.DBURI, applicationCollection + "/" + type, super.DBuser, super.DBpassword);
        String collectionPath = getPathforFile(dbc, xmlId, id);
        DBFile mappingFile = new DBFile(DBURI, collectionPath, xmlId, DBuser, DBpassword);

        if (action.equals("close")) { //Added from FeXML File servlet to unlock file
            System.out.println("UNLOCKING " + id);
            mappingFile.xUpdate("//admin/locked", "no");
        } else if (action.equals("paste")) {
            String xpath = request.getParameter("xpath");
//            System.out.println("XPATH=" + xpath);
            if (xpath.contains("***")) {
                String[] paths = xpath.split("\\*\\*\\*");
                mappingFile.xCopyAfter(paths[0], paths[1]);

            }

//        } else if (action.equals("X")) {
//            final Generator VALUE_POLICY = X3MLGeneratorPolicy.load(null, X3MLGeneratorPolicy.createUUIDSource(1));
//
//            Mapper map = new Mapper();
//
//            X3MLEngine engine = map.engine("/coin_a/01-coin-simple.x3ml");
//            X3MLEngine.Output output = engine.execute(map.document("/coin_a/00-coin-input.xml"), VALUE_POLICY);
//            String[] mappingResult = output.toStringArray();
        } else if (action.equals("raw")) {
            String xpath = request.getParameter("xpath");

            String output = "";
            String[] results = mappingFile.queryString(xpath);

            if (results != null) {
                if (results.length > 0) {
                    for (String res : results) {
                        output = output  + res+"\n";
                    }
                }
            }
            out.println(output);

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
