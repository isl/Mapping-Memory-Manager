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
import isl.FIMS.servlet.ApplicationBasicServlet;
import isl.FIMS.utils.entity.Config;
import static isl.FIMS.utils.ParseXMLFile.parseFile;
import isl.FIMS.utils.Utils;
import isl.dms.DMSException;
import isl.dms.file.DMSFile;
import isl.dms.file.DMSUser;
import isl.dms.xml.XMLTransform;
import java.io.IOException;
import java.io.PrintWriter;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author kkons_pc
 */
public class ForgetPass extends ApplicationBasicServlet {

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
            throws ServletException, IOException, MessagingException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String displayMsg = "";
        String type = "changePass";
        Config conf = new Config(type);
        String lang = request.getParameter("lang");
        StringBuffer xml = new StringBuffer();
        String xmlStart = this.xmlStart(lang);
        String xmlEnd = this.xmlEnd();
        xml.append(xmlStart);
        String xsl = conf.forgotPass;
        String email = request.getParameter("email");
        if (email != null) {

            try {
                DMSFile df = new DMSFile(this.conf.USERS_FILE, this.conf);
                String query = "data(//users/user[./info/email/text()='" + email + "']/@id)";
                String[] res = df.queryString(query);
                String userId = "";

                if (res.length == 0) {
                    displayMsg = "NoUserEmail";
                } else {
                    userId = res[0];
                    DMSUser dbUser = null;
                    int id = Integer.parseInt(userId);

                    dbUser = new DMSUser(id, this.conf);
                    String to = email;
                    Document doc = parseFile(ApplicationConfig.SYSTEM_ROOT + "formating/multi_lang.xml");
                    Element root = doc.getDocumentElement();
                    Element contextTag = (Element) root.getElementsByTagName("context").item(0);
                    Element emailSubject = (Element) contextTag.getElementsByTagName("emailSubject").item(0);
                    String subject = emailSubject.getElementsByTagName(lang).item(0).getTextContent();
                    if (subject.contains("systemName")) {
                        subject = subject.replace("systemName", this.systemName);
                    }
                    Element emailContext = (Element) contextTag.getElementsByTagName("emailContext").item(0);
                    String context = emailContext.getElementsByTagName(lang).item(0).getTextContent();
                    context = context.replaceAll("\\?", "<br>");
                    String newPass = Utils.generateRandomPassword();

                    String passwordSentence = context.split("<br>")[1];
                    context = context.replace(passwordSentence, passwordSentence + newPass);
                    boolean isSend = Utils.sendEmail(to, subject, context);
                    // String host = "enigma.ics.forth.gr";

                    if (isSend) {
                        dbUser.setPassword(newPass);
                        displayMsg = "NewPassSend";
                    } else {
                        displayMsg = "wentWorng";
                    }
                }
            } catch (DMSException ex) {
                ex.printStackTrace();
            }
        }

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
        try {
            processRequest(request, response);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
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
        try {
            processRequest(request, response);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
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
