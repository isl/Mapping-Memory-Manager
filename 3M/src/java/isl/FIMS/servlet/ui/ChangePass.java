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
package isl.FIMS.servlet.ui;

import isl.FIMS.utils.Messages;
import isl.FIMS.servlet.ApplicationBasicServlet;
import isl.FIMS.utils.entity.Config;
import isl.dms.DMSException;
import isl.dms.file.DMSFile;
import isl.dms.file.DMSUser;
import isl.dms.file.EntryNotFoundException;
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
public class ChangePass extends ApplicationBasicServlet {

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
        String displayMsg = "";
        this.initVars(request);

        String userOrg = this.getUserGroup();
        boolean isEditor = this.userHasAction("editor");
        StringBuffer resultsTag = new StringBuffer();
        String type = "changePass";
        Config conf = new Config(type);
        String userAction = this.action;
        StringBuffer xml = new StringBuffer(this.xmlStart(this.topmenu, this.username, this.pageTitle, this.lang, "", request));

        if (this.action.equals("toedit")) {
            try {
                int id = this.getUserId(request);

                if (displayMsg.length() == 0) {

                    userAction = "edit";
                    // xml.append(this.getGroupsXML(userOrg));
                    StringBuffer querySource = new StringBuffer("for $i in document('" + this.adminDbCollection + this.conf.USERS_FILE + "')//user[@id='" + id + "']");
                    querySource.append("\nlet $groupname := string(document('" + this.adminDbCollection + this.conf.GROUPS_FILE + "')//group[@id=$i//groups/group]/@groupname)");

                    querySource.append("\nreturn\n").append("<result>\n").append("<Id><Id>{$i//@id/string()}</Id></Id>\n").append("<Name><Name>{$i//@username/string()}</Name></Name>\n").append("<LastName>{$i//lastname}</LastName>\n").append("<FirstName>{$i//firstname}</FirstName>\n").append("<Address>{$i//address}</Address>\n").append("<Email>{$i//email}</Email>\n").append("<Group>{$i//group}</Group>\n").append("<Organization><Organization>{$groupname}</Organization></Organization>\n").append("<Actions><Actions>{$i//actions/*[1]/name()}</Actions></Actions>\n").append("</result>");

                    DMSFile df = new DMSFile(this.conf.USERS_FILE, this.conf);
                    String[] res = df.queryString(querySource.toString());

                    xml.append(res[0]);
                    conf.DISPLAY_XSL = conf.EDITOR_CHANGE_PASS;
                }
            } catch (DMSException ex) {
            }

        } else if (this.action.equals("edit")) {
            try {

                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String passwordV = request.getParameter("passwordV");
                String lastname = request.getParameter("lastname");
                String firstname = request.getParameter("firstname");
                String address = request.getParameter("address");
                String email = request.getParameter("email");
                String oldPassword = request.getParameter("oldpassword");

                DMSUser user = null;
                int id = Integer.parseInt(request.getParameter("id"));

                //...ta updates pou xreiazontai ston user
                user = new DMSUser(id, this.conf);

                if (username.length() == 0) {
                    displayMsg = Messages.EMPTY_FIELD_UserName + Messages.NL;
                }

                if (password.length() == 0) {
                    displayMsg += Messages.EMPTY_FIELD_Password + Messages.NL;
                }

                if (password.equals(passwordV) == false) {
                    displayMsg += Messages.FIELD_Password_NOT_VERIFIED + Messages.NL;
                }

                if (lastname.length() == 0) {
                    displayMsg += Messages.EMPTY_FIELD_LastName + Messages.NL;
                }

                if (firstname.length() == 0) {
                    displayMsg += Messages.EMPTY_FIELD_FirstName + Messages.NL;
                }
                if (!oldPassword.equals(user.getPassword())) {
                    displayMsg += Messages.PASSNPTMATCH + Messages.NL;

                }

                if (displayMsg.length() == 0) {

                    user.setUsername(username);
                    if (password.length() != 0) {
                        user.setPassword(password);
                    }

                    user.setInfo("lastname", lastname);
                    user.setInfo("firstname", firstname);
                    user.setInfo("address", address);
                    user.setInfo("email", email);

                

                    displayMsg = Messages.ACTION_SUCCESS;
                }
            } catch (EntryNotFoundException ex) {
                ex.printStackTrace();
            } catch (DMSException ex) {
                displayMsg += Messages.USER_EXIST;
                ex.printStackTrace();
            }

        }

        xml.append("<userAction>").append(userAction).append("</userAction>\n");
        xml.append("<query>\n").append(resultsTag).append("</query>\n");
        xml.append("<Display>").append(displayMsg).append("</Display>\n");
        xml.append(this.xmlEnd());

        String xsl = conf.DISPLAY_XSL;
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
