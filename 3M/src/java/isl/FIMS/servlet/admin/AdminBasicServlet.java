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
package isl.FIMS.servlet.admin;

import isl.FIMS.servlet.ApplicationBasicServlet;
import java.util.Enumeration;
import java.util.Hashtable;
import isl.dms.DMSException;
import isl.dms.file.DMSGroup;
import isl.dms.file.DMSUser;


import javax.servlet.http.HttpServletRequest;

public class AdminBasicServlet extends ApplicationBasicServlet {

    protected String displayMsg;
    boolean error;

    public void initVars(HttpServletRequest request) {
        super.initVars(request);

        this.displayMsg = "";
        this.error = true;
    }

    public boolean isAdminUser(HttpServletRequest request) {
        try {
            DMSUser user = new DMSUser(this.username, this.conf);
            return user.hasAction("admin");
        } catch (DMSException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean isSysAdminUser(HttpServletRequest request) {
        try {
            DMSUser user = new DMSUser(this.username, this.conf);
            return user.hasAction("sysadmin");
        } catch (DMSException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getRights(String usernam) {
        String UserRights = null;
        try {

            DMSUser Actions = new DMSUser(usernam, this.conf);
            String[] ActionArray = Actions.getActions();
            UserRights = ActionArray[0];
        } catch (DMSException e) {
            e.printStackTrace();
        }
        return UserRights;
    }

    public String getGroupsXML(String gId) throws DMSException {
        StringBuffer groupsXML = new StringBuffer("<groups>\n");
        Hashtable groups = DMSGroup.getGroupToIdMapping(this.conf);
        if (gId == null || gId.length() == 0) {
            //get all groups
            for (Enumeration e = groups.keys(); e.hasMoreElements();) {
                String id = (String) e.nextElement();
                String groupname = (String) groups.get(id);

                groupsXML.append("<group>\n")
                        .append("<id>").append(id).append("</id>\n")
                        .append("<name>").append(groupname).append("</name>\n")
                        .append("</group>\n");
            }
        } else {
            //get only the one specified
            String groupname = (String) groups.get(gId);

            groupsXML.append("<group>\n")
                    .append("<id>").append(gId).append("</id>\n")
                    .append("<name>").append(groupname).append("</name>\n")
                    .append("</group>\n");

        }

        groupsXML.append("</groups>");

        return groupsXML.toString();
    }

  
    
    
}
