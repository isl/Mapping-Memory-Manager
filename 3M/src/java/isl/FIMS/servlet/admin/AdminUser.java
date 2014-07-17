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
import isl.FIMS.utils.entity.Config;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import isl.dms.DMSException;
import isl.dms.file.DMSFile;
import isl.dms.file.DMSUser;
import isl.dms.file.EntryExistException;
import isl.dms.file.EntryNotFoundException;
import isl.dms.xml.XMLTransform;
import java.util.Arrays;

//Handles the 'insert', 'edit', 'delete', 'list' of Users
public class AdminUser extends AdminBasicServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DMSException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        this.setStatus("insert");
        this.initVars(request);

        String mode = request.getParameter("mode");

        String type = "User";

        Config conf = new Config("AdminUser");

        String userOrg = this.getUserGroup();
        boolean isAdmin = this.isAdminUser(request);
        boolean isSysAdmin = this.isSysAdminUser(request);
        if (isSysAdmin) {
            mode = "sys";
        } else {
            mode = "";
        }
        StringBuffer resultsTag = new StringBuffer();

        String adminAction = this.action;
        StringBuffer xml = new StringBuffer(this.xmlStart(this.topmenu, this.username, this.pageTitle, this.lang, "", request));
        xml.append("<AdminMode>").append(mode).append("</AdminMode>\n");
        xml.append("<ListOf>").append(type).append("</ListOf>\n");
        xml.append("<EntityType>").append(conf.ENTITY_TYPE).append("</EntityType>\n");

        if (this.action.equals("list")) {
            resultsTag.append("<outputs>\n");
            resultsTag.append("<path xpath=\"xxx\" selected=\"yes\">").append("username").append("</path>\n").append("<path xpath=\"xxx\" selected=\"yes\">").append("lastname").append("</path>\n").append("<path xpath=\"xxx\" selected=\"yes\">").append("firstname").append("</path>\n").append("<path xpath=\"xxx\" selected=\"yes\">").append("address").append("</path>\n").append("<path xpath=\"xxx\" selected=\"yes\">").append("email").append("</path>\n").append("<path xpath=\"xxx\" selected=\"yes\">").append("orgname").append("</path>\n").append("<path xpath=\"xxx\" selected=\"yes\">").append("role").append("</path>\n");
            resultsTag.append("</outputs>\n");

            StringBuffer querySource = new StringBuffer("for $i in document('" + this.adminDbCollection + this.conf.USERS_FILE + "')//user");
            querySource.append("\nlet $groupname := string(document('" + this.adminDbCollection + this.conf.GROUPS_FILE + "')//group[@id=$i//groups/group]/@groupname)");

            //an den einai 'sys admin' mono tous users tou org ston opoio anhkei
            //Epishs oxi toys 'sys admin'
            if (mode.equals("sys") == false) {
                querySource.append("\nwhere $i//groups/group=" + userOrg);
                querySource.append("\nand not ($i//actions/sysadmin)");
            }

            querySource.append("\nreturn\n").append("<result>\n").append("<Id><Id>{string($i//@id/string())}</Id></Id>\n").append("<Name><Name>{$i//@username/string()}</Name></Name>\n").append("<LastName>{$i//lastname}</LastName>\n").append("<FirstName>{$i//firstname}</FirstName>\n").append("<Address>{$i//address}</Address>\n").append("<Email>{$i//email}</Email>\n").append("<Organization><Organization>{$groupname}</Organization></Organization>\n").append("<Actions><Actions>{$i//actions/*[1]/name()}</Actions></Actions>\n").append("<hiddenResults>").append("<FileId>{string($i//@id/string())}</FileId>\n").append("</hiddenResults>").append("</result>");

            //Xrhsimopoioume to DMSFile kai oxi to DMSUser
            //gia na kanoume kateu8eian to 'sun8eto' query
            DMSFile df = new DMSFile(this.conf.USERS_FILE, this.conf);
            String[] res = df.queryString(querySource.toString());

            resultsTag.append("<results>\n");

            for (int r = 0; r < res.length; r++) {
                resultsTag.append(res[r]);
            }

            resultsTag.append("\n</results>\n");

            conf.DISPLAY_XSL = conf.LIST_XSL;
        } else if (this.action.equals("toinsert")) {

            adminAction = "insert";
            //gia na parei to xsl to orgId (userOrg tou admin) kai na ton kanei selected
            xml.append("\n<result>\n<Group><group>").append(userOrg).append("</group></Group>\n</result>\n");
            xml.append(this.getGroupsXML(userOrg));
            conf.DISPLAY_XSL = conf.USER_XSL;

        } else if (this.action.equals("view")) {
            String id = request.getParameter("id");
            if (id == null) {
                id = "0";
            }
            if (isSysAdmin) {
                xml.append(this.getGroupsXML(""));
            } else {

                xml.append(this.getGroupsXML(userOrg));
            }
            StringBuffer querySource = new StringBuffer("for $i in document('" + this.adminDbCollection + this.conf.USERS_FILE + "')//user[@id='" + id + "']");
            querySource.append("\nlet $groupname := string(document('" + this.adminDbCollection + this.conf.GROUPS_FILE + "')//group[@id=$i//groups/group]/@groupname)");

            querySource.append("\nreturn\n").append("<result>\n").append("<Id><Id>{$i//@id/string()}</Id></Id>\n").append("<Name><Name>{$i//@username/string()}</Name></Name>\n").append("<LastName>{$i//lastname}</LastName>\n").append("<FirstName>{$i//firstname}</FirstName>\n").append("<Address>{$i//address}</Address>\n").append("<Email>{$i//email}</Email>\n").append("<Group>{$i//group}</Group>\n").append("<Organization><Organization>{$groupname}</Organization></Organization>\n").append("<Actions><Actions>{$i//actions/*[1]/name()}</Actions></Actions>\n").append("</result>");

            DMSFile df = new DMSFile(this.conf.USERS_FILE, this.conf);
            String[] res = df.queryString(querySource.toString());

            xml.append(res[0]);

            conf.DISPLAY_XSL = conf.USER_XSL;

        } else if (this.action.equals("toedit")) {


            String id = request.getParameter("id");
            if (id == null) {
                id = "0";
            }

            if (mode.equals("sys") == false) {
                //h 'checkUserGroup' 8etei sto 'displayMsg'
                this.checkUserGroup(Integer.parseInt(id), userOrg);
            }

            if (this.displayMsg.length() == 0) {
                boolean sysCanEdit = false;
                if (isSysAdmin == true) {
                    String username = DMSUser.getUsernameOf(Integer.parseInt(id), this.conf);
                    String rights = this.getRights(username);
                    if (!rights.equals("admin")) {
                        this.displayMsg = Messages.ACCESS_DENIED;
                    } else {
                        xml.append(this.getGroupsXML(""));
                        sysCanEdit = true;
                    }

                }
                if (sysCanEdit || isAdmin) {
                    adminAction = "edit";
                    xml.append(this.getGroupsXML(userOrg));
                    StringBuffer querySource = new StringBuffer("for $i in document('" + this.adminDbCollection + this.conf.USERS_FILE + "')//user[@id='" + id + "']");
                    querySource.append("\nlet $groupname := string(document('" + this.adminDbCollection + this.conf.GROUPS_FILE + "')//group[@id=$i//groups/group]/@groupname)");

                    querySource.append("\nreturn\n").append("<result>\n").append("<Id><Id>{$i//@id/string()}</Id></Id>\n").append("<Name><Name>{$i//@username/string()}</Name></Name>\n").append("<LastName>{$i//lastname}</LastName>\n").append("<FirstName>{$i//firstname}</FirstName>\n").append("<Address>{$i//address}</Address>\n").append("<Email>{$i//email}</Email>\n").append("<Group>{$i//group}</Group>\n").append("<Organization><Organization>{$groupname}</Organization></Organization>\n").append("<Actions><Actions>{$i//actions/*[1]/name()}</Actions></Actions>\n").append("</result>");

                    DMSFile df = new DMSFile(this.conf.USERS_FILE, this.conf);
                    String[] res = df.queryString(querySource.toString());
                    xml.append(res[0]);
                    conf.DISPLAY_XSL = conf.USER_XSL;
                }
            }

        } else if (this.action.equals("insert") || this.action.equals("edit")) {
            String orgId = request.getParameter("orgId");

            //an den einai 'sys admin' kai 'balei' org diaforetiko tou org ston opoio anhkei
            if (orgId.equals("0") == false && orgId.equals(userOrg) == false) {
                this.displayMsg = Messages.ACCESS_DENIED;
            } else {

                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String passwordV = request.getParameter("passwordV");
                String lastname = request.getParameter("lastname");
                String firstname = request.getParameter("firstname");
                String address = request.getParameter("address");
                String email = request.getParameter("email");
                String role = request.getParameter("userrole");

                if (username.length() == 0) {
                    this.displayMsg = Messages.EMPTY_FIELD_UserName + Messages.NL;
                }

                if (this.action.equals("insert") && password.length() == 0) {
                    this.displayMsg += Messages.EMPTY_FIELD_Password + Messages.NL;
                }

                if (password.equals(passwordV) == false) {
                    this.displayMsg += Messages.FIELD_Password_NOT_VERIFIED + Messages.NL;
                }

                if (orgId.equals("0")) {
                    this.displayMsg += Messages.EMPTY_FIELD_OrgName + Messages.NL;
                }

                if (lastname.length() == 0) {
                    this.displayMsg += Messages.EMPTY_FIELD_LastName + Messages.NL;
                }

                if (firstname.length() == 0) {
                    this.displayMsg += Messages.EMPTY_FIELD_FirstName + Messages.NL;
                }

                if (email.length() == 0) {
                    displayMsg += Messages.EMPTY_FIELD_Email + Messages.NL;
                }
                if (this.displayMsg.length() == 0) {
                    try {
                        DMSFile df = new DMSFile(this.conf.USERS_FILE, this.conf);
                        DMSUser user = null;
                        if (this.action.equals("insert")) {
                            if (isSysAdmin) {
                                this.displayMsg = Messages.ACCESS_DENIED;
                            } else {
                                if (df.exist("/DMS/users/user/info/email[./text()='" + email + "']")) {
                                    displayMsg += Messages.Email_EXIST;
                                    throw new EntryExistException("User already exists: " + email);

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
                                }
                            }
                        } else if (this.action.equals("edit")) {
                            int id = Integer.parseInt(request.getParameter("id"));

                            //...ta updates pou xreiazontai ston user
                            user = new DMSUser(id, this.conf);
                            //user.setUsername(username);
                            if (df.exist("/DMS/users/user/info/email[./text()='" + email + "']")) {
                                displayMsg += Messages.Email_EXIST;
                            } else {
                                if (password.length() != 0) {
                                    user.setPassword(password);
                                }

                                user.setInfo("lastname", lastname);
                                user.setInfo("firstname", firstname);
                                user.setInfo("address", address);
                                user.setInfo("email", email);

                                //to update se organization [group]
                                //ginetai ws afairesh kai pros8hkh
                                //an prokeitai gia diaforetiko neo organization [group]
                                String groupPrev = user.getGroups()[0];
                                if (groupPrev.equals(orgId) == false) {
                                    user.removeFromGroup(groupPrev);
                                    user.addToGroup(orgId);
                                }
                                //to idio kai gia to action [role]
                                String rolePrev = user.getActions()[0];
                                if (rolePrev.equals(role) == false) {
                                    user.removeAction(rolePrev);
                                    user.addAction(role, "normal");
                                }
                            }
                        }

                        this.displayMsg = Messages.ACTION_SUCCESS;
                    } catch (EntryExistException e) {
                        if (!this.displayMsg.equals("Email_EXIST")) {
                            this.displayMsg += Messages.USER_EXIST;
                        }
                    }
                }
            }
        } else if (this.action.equals("delete")) {

            int id = Integer.parseInt(request.getParameter("id"));
            if (mode.equals("sys") == false) {
                this.checkUserGroup(id, userOrg);
            }

            if (this.displayMsg.length() == 0) {
                try {
                    DMSUser user = new DMSUser(id, this.conf);
                    String actions[] = user.getActions();
                    if (Arrays.asList(actions).contains("admin")) {
                        this.displayMsg = Messages.ACCESS_DENIED;
                    } else {
                        user.remove();
                        response.sendRedirect("AdminUser?action=list&mode=" + mode);
                        return;
                    }
                } catch (EntryNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }

        xml.append("<AdminAction>").append(adminAction).append("</AdminAction>\n");
        xml.append("<query>\n").append(resultsTag).append("</query>\n");
        xml.append("<Display>").append(this.displayMsg).append("</Display>\n");
        xml.append("<DisplayError>").append(this.error).append("</DisplayError>\n");
        xml.append(this.xmlEnd());
        String xsl = conf.DISPLAY_XSL;
        System.out.println("xsl--->"+xsl);
        try {
            XMLTransform xmlTrans = new XMLTransform(xml.toString());
            xmlTrans.transform(out, xsl);
        } catch (DMSException e) {
            e.printStackTrace();
        }
        out.close();
    }

    private void checkUserGroup(int id, String userOrg) throws DMSException {
        String uOrg = "";
        try {
            DMSUser u = new DMSUser(id, this.conf);
            //na mhn einai 'sys admin' o xrhsths
            if (u.hasAction("sysadmin") == false) {
                uOrg = u.getGroups()[0];
            }
        } catch (EntryNotFoundException e) {
            //this.displayMsg = Messages.USER_NOT_FOUND;
        }

        if (uOrg.equals(userOrg) == false) {
            this.displayMsg = Messages.ACCESS_DENIED;
        }
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
