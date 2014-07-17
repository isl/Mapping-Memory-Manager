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

import isl.FIMS.utils.Messages;
import isl.FIMS.utils.Utils;
import isl.FIMS.utils.entity.Config;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import isl.dms.DMSException;
import isl.dms.file.DMSFile;
import isl.dms.file.DMSGroup;
import isl.dms.file.DMSUser;
import isl.dms.file.EntryExistException;
import isl.dms.xml.XMLTransform;

//Handles the 'insert', 'edit', 'delete', 'list' of Users
public class AdminOrg extends AdminBasicServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DMSException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        this.setStatus("insert");
        this.initVars(request);

        String mode = "sys";
        String type = "Org";

        Config conf = new Config("AdminOrg");

        boolean isSysAdmin = this.isSysAdminUser(request);
        boolean isAdmin = this.isAdminUser(request);

        StringBuffer resultsTag = new StringBuffer();

        String adminAction = this.action;
        StringBuffer xml = new StringBuffer(this.xmlStart(this.topmenu, this.username, this.pageTitle, this.lang, "", request));
        xml.append("<AdminMode>").append(mode).append("</AdminMode>\n");
        xml.append("<ListOf>").append(type).append("</ListOf>\n");
        xml.append("<EntityType>").append(conf.ENTITY_TYPE).append("</EntityType>\n");


        if (this.action.equals("list")) {
            resultsTag.append("<outputs>\n");
            resultsTag.append("<path xpath=\"xxx\" selected=\"yes\">").append("Επωνυμία").append("</path>\n");
            resultsTag.append("<path xpath=\"xxx\" selected=\"yes\">").append("Ακρώνυμο").append("</path>\n");
            resultsTag.append("<path xpath=\"xxx\" selected=\"yes\">").append("Έδρα").append("</path>\n");
            resultsTag.append("<path xpath=\"xxx\" selected=\"yes\">").append("Κράτος").append("</path>\n");
            resultsTag.append("<path xpath=\"xxx\" selected=\"yes\">").append("Πληροφορίες").append("</path>\n");
            resultsTag.append("</outputs>\n");
            resultsTag.append("<results>\n");
            DMSFile df = new DMSFile(this.conf.GROUPS_FILE, this.conf);

            if (isSysAdmin) {
                Hashtable groups = DMSGroup.getGroupToIdMapping(this.conf);

                for (Enumeration e = groups.keys(); e.hasMoreElements();) {
                    String id = (String) e.nextElement();
                    String groupName = (String) groups.get(id);

                    df = new DMSFile(this.conf.GROUPS_FILE, this.conf);
                    String selectQuery = "/DMS/groups/group[@id='" + id + "']/child::info/child::element()";
                    String[] res = df.queryString(selectQuery);

                    resultsTag.append("<result>\n").append("<Id>").append(id).append("</Id>\n").append("<GroupName>").append(groupName).append("</GroupName>\n");
                    for (int r = 0; r < res.length; r++) {
                        resultsTag.append(res[r]);
                    }
                    resultsTag.append("<hiddenResults>").append("<FileId>").append(id).append("</FileId>\n</hiddenResults>");
                    resultsTag.append("</result>\n");
                }
            } else if (isAdmin) {
                //admin can only see the group he belongs
                String id = this.getUserGroup();
                String selectQuery = "string(/DMS/groups/group[@id='" + id + "']/@groupname)";
                String groupName = df.queryString(selectQuery)[0];
                selectQuery = "/DMS/groups/group[@id='" + id + "']/child::info/child::element()";
                String[] res = df.queryString(selectQuery);
                resultsTag.append("<result>\n").append("<Id>").append(id).append("</Id>\n").append("<GroupName>").append(groupName).append("</GroupName>\n");
                for (int r = 0; r < res.length; r++) {
                    resultsTag.append(res[r]);
                }
                resultsTag.append("<hiddenResults>").append("<FileId>").append(id).append("</FileId>\n</hiddenResults>");
                resultsTag.append("</result>\n");
            }
            resultsTag.append("\n</results>\n");

            conf.DISPLAY_XSL = conf.LIST_XSL;

        } else if (this.action.equals("view")) {
            String id = request.getParameter("id");
            if (isAdmin && !id.equals(this.getUserGroup())) {
                this.displayMsg = Messages.ACCESS_DENIED;

            } else {
                if (id == null) {
                    id = "0";
                }
                xml.append("<Id>").append(id).append("</Id>\n");
                xml.append("<Name>").append(DMSGroup.getNameOf(Integer.parseInt(id), this.conf)).append("</Name>\n");
                xml.append("<initials>").append(DMSGroup.getGroupnameOf(Integer.parseInt(id), this.conf)).append("</initials>\n");
                xml.append("<seat>").append(DMSGroup.getSeatOf(Integer.parseInt(id), this.conf)).append("</seat>\n");
                xml.append("<information>").append(DMSGroup.getInformationOf(Integer.parseInt(id), this.conf)).append("</information>\n");
                xml.append("<country>").append(DMSGroup.getCountryOf(Integer.parseInt(id), this.conf)).append("</country>\n");
                conf.DISPLAY_XSL = conf.ORG_XSL;
            }
        } else if (this.action.equals("toinsert")) {
            if (!isAdmin) {
                adminAction = "insert";
                conf.DISPLAY_XSL = conf.ORG_XSL;
            } else {
                this.displayMsg = Messages.ACCESS_DENIED;
            }

        } else if (this.action.equals("toedit")) {
            String id = request.getParameter("id");
            if (isAdmin && !id.equals(this.getUserGroup())) {
                this.displayMsg = Messages.ACCESS_DENIED;

            } else {
                adminAction = "edit";
                if (id == null) {
                    id = "0";
                }
                xml.append("<Id>").append(id).append("</Id>\n");
                xml.append("<Name>").append(DMSGroup.getNameOf(Integer.parseInt(id), this.conf)).append("</Name>\n");
                xml.append("<initials>").append(DMSGroup.getGroupnameOf(Integer.parseInt(id), this.conf)).append("</initials>\n");
                xml.append("<seat>").append(DMSGroup.getSeatOf(Integer.parseInt(id), this.conf)).append("</seat>\n");
                xml.append("<information>").append(DMSGroup.getInformationOf(Integer.parseInt(id), this.conf)).append("</information>\n");
                xml.append("<country>").append(DMSGroup.getCountryOf(Integer.parseInt(id), this.conf)).append("</country>\n");
                conf.DISPLAY_XSL = conf.ORG_XSL;
            }
        } else if (this.action.equals("insert") || this.action.equals("edit")) {
            String orgName = request.getParameter("name") == null ? "-" : request.getParameter("name");
            String groupname = request.getParameter("initials") == null ? "-" : request.getParameter("initials");
            String seat = request.getParameter("seat") == null ? "-" : request.getParameter("seat").equals("") ? "-" : request.getParameter("seat");
            String country = request.getParameter("country") == null ? "-" : request.getParameter("country");
            String information = request.getParameter("information") == null ? "-" : request.getParameter("information").equals("") ? "-" : request.getParameter("information");
            String username = "";
            String password = "";
            String passwordV = "";
            String lastname = "";
            String firstname = "";
            String address = "";
            String email = "";
            String role = "";

            if (orgName.length() == 0) {
                this.displayMsg = Messages.EMPTY_FIELD_OrgName + Messages.NL;
            }

            if (groupname.length() == 0) {
                this.displayMsg += Messages.EMPTY_FIELD_initials + Messages.NL;
            }

            if (country.length() == 0) {
                this.displayMsg += Messages.EMPTY_FIELD_country + Messages.NL;
            }
            if (this.action.equals("insert")) {
                username = request.getParameter("username");
                password = request.getParameter("password");
                passwordV = request.getParameter("passwordV");
                lastname = request.getParameter("lastname");
                firstname = request.getParameter("firstname");
                address = request.getParameter("address");
                email = request.getParameter("email");
                role = "admin";
                if (username.length() == 0) {
                    this.displayMsg += Messages.EMPTY_FIELD_UserName + Messages.NL;
                }
                if (this.action.equals("insert") && password.length() == 0) {
                    this.displayMsg += Messages.EMPTY_FIELD_Password + Messages.NL;
                }
                if (password.equals(passwordV) == false) {
                    this.displayMsg += Messages.FIELD_Password_NOT_VERIFIED + Messages.NL;
                }
                if (lastname.length() == 0) {
                    this.displayMsg += Messages.EMPTY_FIELD_LastName + Messages.NL;
                }
                if (firstname.length() == 0) {
                    this.displayMsg += Messages.EMPTY_FIELD_FirstName + Messages.NL;
                }
            }

            if (this.displayMsg.length() == 0) {
                try {
                    DMSGroup group = null;
                    if (this.action.equals("insert")) {
                        if (isAdmin) {
                            this.displayMsg = Messages.ACCESS_DENIED;
                        } else {
                            group = DMSGroup.addGroup(orgName, groupname, seat, country, information, this.conf);
                            String orgId = group.queryString("string(/DMS/groups/group[@groupname='" + groupname + "']/@id)")[0];
                            DMSUser user = null;
                            try {
                                user = DMSUser.addUser(username, password, this.conf);
                                user.setInfo("lastname", lastname);
                                user.setInfo("firstname", firstname);
                                user.setInfo("address", address);
                                user.setInfo("email", email);
                                //pros8hkh se organization [group]
                                user.addToGroup(orgId);
                                //pros8hkh tou action [role]
                                user.addAction(role, "normal");
                                this.displayMsg = Messages.ACTION_SUCCESS;
                            } catch (EntryExistException e) {
                                group.remove();;
                                this.displayMsg += Messages.USER_EXIST;
                            }

                        }
                    } else if (this.action.equals("edit")) {
                        int id = Integer.parseInt(request.getParameter("id"));

                        String ordId = request.getParameter("id");
                        if (isAdmin && !ordId.equals(this.getUserGroup())) {
                            this.displayMsg = Messages.ACCESS_DENIED;
                        } else {
                            //...ta updates pou xreiazontai sto group...
                            //kai telos to groupname tou group
                            group = new DMSGroup(id, this.conf);
                            group.setGroupname(groupname);
                            group.setName(orgName);
                            group.setSeat(seat);
                            group.setCountry(country);
                            group.setInformation(information);
                            this.displayMsg = Messages.ACTION_SUCCESS;
                        }
                    }
                } catch (EntryExistException e) {
                    this.displayMsg += Messages.ORG_EXIST;
                }
            }
        } else if (this.action.equals("delete")) {
            if (isSysAdmin) {
                int id = Integer.parseInt(request.getParameter("id"));
                String ids = request.getParameter("id");
                Utils u = new Utils();

                if (u.findDependantsOfOrganizations(ids).equals("false")) {

                    DMSGroup group = new DMSGroup(id, this.conf);
                    group.remove();
                    response.sendRedirect("AdminOrg?action=list&mode=" + mode);
                    return;
                } else {
                    this.displayMsg = Messages.CAN_NOT_REMOVE_INSTITUTION;
                }
            } else {
                this.displayMsg = Messages.ACCESS_DENIED;
            }

        }


        xml.append("<AdminAction>").append(adminAction).append("</AdminAction>\n");
        xml.append("<query>\n").append(resultsTag).append("</query>\n");
        xml.append("<Display>").append(this.displayMsg).append("</Display>\n");
        xml.append("<DisplayError>").append(this.error).append("</DisplayError>\n");
        xml.append(this.xmlEnd());
        System.out.println(this.displayMsg);
        String xsl = conf.DISPLAY_XSL;
        try {
            XMLTransform xmlTrans = new XMLTransform(xml.toString());
            xmlTrans.transform(out, xsl);
        } catch (DMSException e) {
            e.printStackTrace();
        }
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
        } catch (DMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
