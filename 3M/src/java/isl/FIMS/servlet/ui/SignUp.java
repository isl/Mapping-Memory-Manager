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

import isl.FIMS.utils.ApplicationConfig;
import isl.FIMS.utils.Messages;
import isl.FIMS.servlet.ApplicationBasicServlet;
import isl.FIMS.utils.entity.Config;
import static isl.FIMS.utils.ParseXMLFile.parseFile;
import isl.FIMS.utils.Utils;
import isl.dms.DMSException;
import isl.dms.file.DMSFile;
import isl.dms.file.DMSUser;
import isl.dms.file.EntryExistException;
import isl.dms.xml.XMLTransform;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author konsolak
 */
public class SignUp extends ApplicationBasicServlet {

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
        String displayMsg = "";
        String type = "signup";
        Config conf = new Config(type);
        String lang = request.getParameter("lang");
        StringBuffer xml = new StringBuffer();
        String xmlStart = this.xmlStart(lang);
        String xmlEnd = this.xmlEnd();
        xml.append(xmlStart);
        String xsl = conf.signup;
        String isSuccess = "false";

        String submit = request.getParameter("submit");
        if (submit != null) {

            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String passwordV = request.getParameter("passwordV");
            String lastname = request.getParameter("lastname");
            String firstname = request.getParameter("firstname");
            String address = request.getParameter("address");
            String email = request.getParameter("email");
            String role = "editor";
            String orgId = "1";
            if (username.length() == 0) {
                displayMsg = Messages.EMPTY_FIELD_UserName + Messages.NL;
            }

            if (password.length() == 0) {
                displayMsg += Messages.EMPTY_FIELD_Password + Messages.NL;
            }

            if (password.equals(passwordV) == false) {
                displayMsg += Messages.FIELD_Password_NOT_VERIFIED + Messages.NL;
            }

            if (orgId.equals("0")) {
                displayMsg += Messages.EMPTY_FIELD_OrgName + Messages.NL;
            }

            if (lastname.length() == 0) {
                displayMsg += Messages.EMPTY_FIELD_LastName + Messages.NL;
            }

            if (firstname.length() == 0) {
                displayMsg += Messages.EMPTY_FIELD_FirstName + Messages.NL;
            }

            if (email.length() == 0) {
                displayMsg += Messages.EMPTY_FIELD_Email + Messages.NL;
            }

            if (displayMsg.length() == 0) {
                try {
                    DMSUser user = null;
                    DMSFile df = new DMSFile(this.conf.USERS_FILE, this.conf);

                    if (df.exist("/DMS/users/user/info/email[./text()='" + email + "']")) {
                        displayMsg += Messages.Email_EXIST;
                    } else {
                        user = DMSUser.addUser(username, password, this.conf);
                        user.setInfo("lastname", lastname);
                        user.setInfo("firstname", firstname);
                        user.setInfo("address", address);
                        user.setInfo("email", email);

                        //pros8hkh se organization [group]
                        user.addToGroup(orgId);
                        //pros8hkh tou action [role]
                        user.addAction(role, "normal");
                        displayMsg = Messages.ACTION_SUCCESS;

                        Document doc = parseFile(ApplicationConfig.SYSTEM_ROOT + "formating/multi_lang.xml");
                        Element root = doc.getDocumentElement();
                        Element contextTag = (Element) root.getElementsByTagName("context").item(0);
                        Element emailSubject = (Element) contextTag.getElementsByTagName("emailSubject_SignUp").item(0);
                        String subject = emailSubject.getElementsByTagName(lang).item(0).getTextContent();
                        if (subject.contains("systemName")) {
                            subject = subject.replace("systemName", this.systemName);
                        }
                        Element emailContext = (Element) contextTag.getElementsByTagName("emailContext_SignUp").item(0);
                        String context = emailContext.getElementsByTagName(lang).item(0).getTextContent();
                        context = context.replaceFirst(":", ": " + username);
                        context = context.replace("*", " " + ApplicationConfig.SYSTEM_ROOT);
                        context = context.replaceAll("\\?", "<br>");
                          if (context.contains("systemName")) {
                            context = context.replace("systemName", this.systemName);
                        }
                        boolean isSend = Utils.sendEmail(email, subject, context);

                        isSuccess = "true";
                    }
                } catch (EntryExistException ex) {
                    displayMsg += Messages.USER_EXIST;
                } catch (DMSException ex) {
                }
            }
        }
        xml.append("<isSuccess>").append(isSuccess).append("</isSuccess>\n");

        xml.append("<Display>").append(displayMsg).append("</Display>\n");
        xml.append(xmlEnd);

        try {

            XMLTransform xmlTrans = new XMLTransform(xml.toString());
            xmlTrans.transform(out, xsl);
        } catch (DMSException e) {
            e.printStackTrace();
        }
    }

    public String xmlStart(String lang) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<page  language=\"" + lang + "\">\n"
                + "<systemRoot>" + ApplicationConfig.SYSTEM_ROOT + "</systemRoot>\n"
                + "<header>\n"
                + "</header>\n"
                + "<topmenu>\n"
                + "</topmenu>\n"
                + "<leftmenu>\n"
                + "</leftmenu>\n" + "<context>\n";
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
