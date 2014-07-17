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
package isl.FIMS.servlet.search;

import isl.FIMS.utils.search.Config;
import isl.FIMS.servlet.ApplicationBasicServlet;
import isl.dms.DMSConfig;
import java.util.Arrays;


import java.util.Hashtable;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * Basic Servlet for the Search servlets. All 'Search' servlets extends the
 * BasicServlet.<br> <br> Depending on the application this servlet must extends
 * the 'ApplicationServlet', which provides global access to info, such as
 * system info (systemRoot, adminCollection, etc) and user info (userId,
 * username).<br> <br> The variables that are needed by the
 * <code>Search</code> servlets (and should inherited by the
 * 'ApplicationServlet') are: <li>DBURI</li> <li>adminCollection</li>
 * <li>DBuser</li> <li>DBpassword</li> <li>systemRoot</li> <li>userId</li>
 * <li>username</li> <br><br> and the methods for building the XML for the page:
 * <li>xmlStart</li> <li>xmlEnd</li> <br><br>
 *
 * @author npap
 *
 */
public class BasicSearchServlet extends ApplicationBasicServlet {

    protected String servletName, dataCol;
    protected DMSConfig conf;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.servletName = config.getServletName();
        this.dataCol = getServletContext().getInitParameter("dataCol");

        this.conf = new DMSConfig(this.DBURI, this.adminDbCollection, this.DBuser, this.DBpassword);
        Config.init(this.systemWebRoot);
    }

    protected void initVars(HttpServletRequest request) {
        super.initVars(request);

        this.conf.LANG = this.lang;
    }

    public Hashtable<String, Object> getParams(HttpServletRequest request) {
        String qId = request.getParameter("qid");
        if (qId == null) {
            qId = "";
        }
        String category = request.getParameter("category");
        if (category == null) {
            category = "";
        }
        String mnemonicName = request.getParameter("mnemonicName");
        if (mnemonicName != null) {
            mnemonicName = mnemonicName.trim();
        } else {
            mnemonicName = new String();
        }
        String[] targets = request.getParameterValues("target");
        if (targets == null) {
            targets = new String[0];
        }
        String operator = request.getParameter("operator");
        if (operator == null) {
            operator = new String();
        }
        String[] inputs = request.getParameterValues("input");

        if (inputs == null) {
            inputs = new String[0];
        }

        String[] inputsIds = request.getParameterValues("inputid");
        if (inputsIds == null) {
            inputsIds = new String[0];
        }
        String[] inputsOpers = request.getParameterValues("inputoper");
        if (inputsOpers == null) {
            inputsOpers = new String[0];
        }
        String[] inputsParameters = request.getParameterValues("inputparameter");
        if (inputsParameters == null) {
            inputsParameters = new String[0];
        }
        String[] inputsValues = request.getParameterValues("inputvalue");
        if (inputsValues == null) {
            inputsValues = new String[0];
        } else {
            inputsValues[0] = inputsValues[0].trim();
        }
        String[] outputs = request.getParameterValues("output");
        if (outputs == null) {
            outputs = new String[0];
        }
        String[] extraStatus = request.getParameterValues("extraStatus");
        if (extraStatus != null) {
            if (extraStatus.length == 3) {
                this.status = "all";
            } else if (extraStatus.length == 2) {
                if (Arrays.asList(extraStatus).contains("unpublished") && Arrays.asList(extraStatus).contains("pending")) {
                    this.status = "notPublished";
                } else if (Arrays.asList(extraStatus).contains("unpublished") && Arrays.asList(extraStatus).contains("published")) {
                    this.status = "notPending";
                } else if (Arrays.asList(extraStatus).contains("pending") && Arrays.asList(extraStatus).contains("published")) {
                    this.status = "notUnpublished";
                }
            } else if (extraStatus.length == 1) {
                this.status = extraStatus[0];
            }
        }

        Hashtable<String, Object> params = new Hashtable<String, Object>();
        String[] users = request.getParameterValues("user");
        if (users != null) {
            for (int i = 0; i < users.length; i++) {
                users[i] = users[i].replace("\n", "").replace("\r", "");
            }
            params.put("user", users);

        }
        String code = request.getParameter("code");
        if (code == null) {
            code = "";
        }
        String[] extraOrgs = request.getParameterValues("org");
        if (extraOrgs != null) {
            params.put("extraOrg", extraOrgs);
        }
        String vocFile = request.getParameter("vocFile");
        if (vocFile == null) {
            vocFile = "";
        }
        params.put("qId", qId);
        params.put("category", category);
        params.put("mnemonicName", mnemonicName);
        params.put("targets", targets);
        params.put("operator", operator);
        params.put("inputs", inputs);
        params.put("inputsIds", inputsIds);
        params.put("inputsOpers", inputsOpers);
        params.put("inputsParameters", inputsParameters);
        params.put("inputsValues", inputsValues);
        params.put("outputs", outputs);
        //anna komnini pros8hkh
        params.put("lang", this.lang);
        params.put("status", this.status);
        params.put("username", this.username);
        params.put("organization", this.getUserGroup());
        params.put("code", code);
        return params;
    }
}