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

import isl.FIMS.utils.search.QueryTools;
import isl.FIMS.utils.search.Config;
import isl.FIMS.utils.entity.GetEntityCategory;
import isl.dbms.DBFile;
import isl.dms.DMSException;
import isl.dms.file.DMSGroup;
import isl.dms.xml.XMLTransform;
import isl.dms.file.DMSUser;
import isl.dms.file.DMSXQuery;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.*;
import javax.servlet.http.*;

public class Search extends BasicSearchServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws DMSException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DMSException {
        response.setContentType("text/html; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        boolean personal = false;
        this.initVars(request);

        StringBuffer xml = new StringBuffer();

        String xmlStart = this.xmlStart(this.topmenu, this.username, "Search", this.lang, "", request);

        String xmlEnd = this.xmlEnd();
        String xmlMiddle = "";

        StringBuffer queriesNames = new StringBuffer("");

        String category = request.getParameter("category");
        int qId = 0;
        String qIdStr = request.getParameter("qid");
        // if there is 'savedquery', it means we want a particular query.
        // if not, qId is 0 indicating no query.
        if (qIdStr != null && qIdStr.length() != 0) {
            qId = Integer.parseInt(qIdStr);
        }

        DMSUser user = new DMSUser(this.username, this.conf);
        int userId = user.getId();
        int[] savedQueryIds = user.getQueryIds(category);
        int[] personalQueries = user.getQueryIds_Personal(category);
        int[] publicQueries = user.getQueryIds_Public(category);

        for (int i = 0; i < savedQueryIds.length; i++) {
            queriesNames.append("<savedQuery id=\"").append(savedQueryIds[i]).append("\"");
            if (savedQueryIds[i] == qId) {
                queriesNames.append(" selected=\"yes\"");
            }
            queriesNames.append(">").append(DMSXQuery.getNameOf(savedQueryIds[i], userId, this.conf)).append("</savedQuery>\n");
        }


        for (int i = 0; i < personalQueries.length; i++) {
            queriesNames.append("<personalQueries id=\"").append(personalQueries[i]).append("\"");
            if (personalQueries[i] == qId) {
                queriesNames.append(" selected=\"yes\"");
                personal = true;
            }
            queriesNames.append(">").append(DMSXQuery.getNameOf(personalQueries[i], userId, this.conf)).append("</personalQueries>\n");
        }

        for (int i = 0; i < publicQueries.length; i++) {
            queriesNames.append("<publicQueries id=\"").append(publicQueries[i]).append("\"");
            if (publicQueries[i] == qId) {
                queriesNames.append(" selected=\"yes\"");
                if (this.userHasAction("sysadmin")) {
                    personal = true;
                }

            }
            queriesNames.append(">").append(DMSXQuery.getNameOfNoUser(publicQueries[i], this.conf)).append("</publicQueries>\n");
        }
        queriesNames.append("<isPersonal>").append(personal).append("</isPersonal>\n");



        // Something like flag, meaning that we came here
        // from another page (SearchResults, SearchSave, SearchDelete).
        String mode = request.getParameter("mode");
        if (request.getMethod().equals("POST") && mode != null) {
            // if there is 'mode', it means that we came here
            // from another page (SearchResults, SearchSave, SearchDelete).
            Hashtable params = this.getParams(request);
            // Get XML depending on our params, that depends on request.
            xmlMiddle = QueryTools.getXML(params, this.conf, this.dataCol);
        } else if (qId != 0) {
            // if there is the 'qid', it means we want a particular query.
            String qName = DMSXQuery.getNameOf(qId, userId, this.conf);
            if (qName == null) {
                qName = DMSXQuery.getNameOfNoUser(qId, this.conf);
            }
            DMSXQuery query = new DMSXQuery(qName, userId, this.conf);
            try {
                query.removeInfo("status");
            } catch (Exception e) {
            }
            query.addInfo("status", this.status);
            // Get XML for that query.
            xmlMiddle = QueryTools.getXML(query, this.conf);
        } else {
            xmlMiddle = QueryTools.xmlForGet(category, this.lang, this.status, this.conf);
        }

        //Samarita's hack
        DBFile dataTypes = new DBFile(this.DBURI, this.adminDbCollection, this.dataTypesFile, this.DBuser, this.DBpassword);
        //StringBuffer queriesNames = new StringBuffer("");

        //added for extra conditions in search
        String entityCategory = "secondary";
        StringBuffer statusType = null;
        if (GetEntityCategory.getEntityCategory(category).equals("primary")) {
            entityCategory = "primary";
            statusType = new StringBuffer();
            statusType.append("\n<statusType>\n");
            statusType.append("<status>");
            statusType.append("unpublished");
            statusType.append("</status>\n");
            statusType.append("<status>");
            statusType.append("pending");
            statusType.append("</status>\n");
            statusType.append("<status>");
            statusType.append("published");
            statusType.append("</status>\n");
            statusType.append("</statusType>\n");
        }
        Hashtable groups = DMSGroup.getGroupToIdMapping(conf);

        StringBuffer users = new StringBuffer();
        Set g = groups.entrySet();
        users.append("<Users>\n");
        Iterator it = g.iterator();
        String[] myUsers = DMSUser.getUsersInGroup(this.getUserGroup(), conf);
        Arrays.sort(myUsers);
        StringBuffer groupsInDB = new StringBuffer();

        groupsInDB.append("<Groups>\n");
        int id = Integer.parseInt(this.getUserGroup());
        groupsInDB.append("<groups id='" + id + "'>");
        groupsInDB.append(DMSGroup.getGroupnameOf(id, conf));
        groupsInDB.append("</groups>\n");
        users.append("<group name='" + DMSGroup.getGroupnameOf(id, conf) + "'>");
        for (int i = 0; i < myUsers.length; i++) {
            users.append("<userInGroup>\n");
            users.append(myUsers[i]);
            users.append("</userInGroup>\n");
        }
        users.append("</group>");
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (!entry.getKey().equals(this.getUserGroup())) {
                String[] usersInGroup = DMSGroup.getUsersOf((String) entry.getKey(), conf);
                groupsInDB.append("<groups id='"+entry.getKey()+"'>");
                groupsInDB.append(entry.getValue());
                groupsInDB.append("</groups>\n");
                users.append("<group name='" + entry.getValue() + "'>");

                for (int k = 0; k < usersInGroup.length; k++) {
                    Arrays.sort(usersInGroup);
                    users.append("<userInGroup>\n");
                    users.append(usersInGroup[k]);
                    users.append("</userInGroup>\n");
                }
                users.append("</group>");

            }
        }
        users.append("</Users>\n");
        groupsInDB.append("</Groups>\n");


        xml.append(xmlStart);
        xml.append(queriesNames);
        xml.append(xmlMiddle);
        xml.append(dataTypes.toString());
        xml.append("\n<EntityCategory>").append(entityCategory).append("</EntityCategory>");
        if (statusType != null) {
            xml.append(statusType.toString());
        }
        xml.append(users.toString());
        xml.append(groupsInDB.toString());
        xml.append(xmlEnd);
        XMLTransform xmlTrans = new XMLTransform(xml.toString());
        String xsl = Config.SEARCH_XSL;
        xmlTrans.transform(out, xsl);
        out.close();
    }

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ServletException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ServletException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
