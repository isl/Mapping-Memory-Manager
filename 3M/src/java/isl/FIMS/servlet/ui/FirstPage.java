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

import isl.FIMS.servlet.ApplicationBasicServlet;
import isl.FIMS.utils.entity.Config;
import isl.dms.DMSException;
import isl.dms.xml.XMLTransform;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.*;

public class FirstPage extends ApplicationBasicServlet {

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        this.initVars(request);

        if (userHasAction("sysadmin")) {
            request.getSession().setAttribute("topmenu", "sys");
            // response.sendRedirect("LeftMenu?menu=sysadmin&welcome=yes");
            // return;
        } else if (userHasAction("admin")) {
            request.getSession().setAttribute("topmenu", "admin");
            // response.sendRedirect("LeftMenu?menu=admin&welcome=yes");
            // return;
        }

        // Create xml output
        StringBuffer xml = new StringBuffer(this.xmlStart(this.topmenu, this.username, this.pageTitle, this.lang, "", request));

        String xmlEnd = this.xmlEnd();

        StringBuffer xmlMiddle = new StringBuffer();

        xml.append(xmlMiddle);
        xml.append("<Welcome>").append("yes").append("</Welcome>\n");
        xml.append(xmlEnd);
        Config conf = new Config(type);
        String xsl = conf.WELCOME_XSL;
        try {
            XMLTransform xmlTrans = new XMLTransform(xml.toString());
            xmlTrans.transform(out, xsl);
        } catch (DMSException e) {
            e.printStackTrace();
        }
        out.close();


    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
