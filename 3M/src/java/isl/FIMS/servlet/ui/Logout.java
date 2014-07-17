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
import isl.dbms.DBCollection;
import isl.dbms.DBFile;


import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.*;
import javax.servlet.http.*;

public class Logout extends ApplicationBasicServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String lang = this.getLang(request);
        initVars(request);

        //remove cookie from FeXML
        String sessionId = session.getId();
        String encodeUsername = URLEncoder.encode(username);
        Cookie cookie = new Cookie(encodeUsername, sessionId);
        cookie.setPath("/" + this.editorWebapp);
        cookie.setMaxAge(0);
        cookie.setValue("");
        response.addCookie(cookie);



        session.invalidate();

        response.sendRedirect(
                "Login?lang=" + lang);
    }
}
