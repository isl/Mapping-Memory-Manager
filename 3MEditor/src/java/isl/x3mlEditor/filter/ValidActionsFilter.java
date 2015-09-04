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
 * This file is part of the 3MEditor webapp of Mapping Memory Manager project.
 */
package isl.x3mlEditor.filter;

import isl.x3mlEditor.BasicServlet;

import isl.dbms.DBCollection;
import isl.dbms.DBFile;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author konsolak
 */
public class ValidActionsFilter extends BasicServlet implements Filter {

    private static final boolean debug = true;
    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured.
    private FilterConfig filterConfig = null;

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        if (editorType.equals("standalone")) {
            chain.doFilter(request, response);
        } else {
            if (request instanceof HttpServletRequest) {
                HttpServletRequest hrequest = (HttpServletRequest) request;
                HttpServletResponse hresponse = (HttpServletResponse) response;
                String action = request.getParameter("action");
                String lang = request.getParameter("lang");
                String id = request.getParameter("id");
                String type = request.getParameter("type");
                String output = request.getParameter("output");

                String category = "";
                if (type == null) {
                    type = "";
                }
                //SAM's addition
                if (id != null) {
                    if (!id.contains(type)) {
                        id = type + id;
                    }
                }

                String displayMsg = "";
                String goOnLink = "false";
                String xmlId = id + ".xml";
                String numericId = "";

                boolean isSaved = false;
                String lockedBy = "";

                DBCollection dbc = new DBCollection(super.DBURI, applicationCollection + "/" + type, super.DBuser, super.DBpassword);

                if (action == null) {
                    if (output!=null) {
                        if (output.equals("xml")) {
                            chain.doFilter(request, response);
                        }
                    }
                    action = "edit";
                }

                if (action.equals("edit") || action.equals("instance")) {
                    
                    if (output!=null) {
                        if (output.equals("xml")) {
                            chain.doFilter(request, response);
                        }
                    }
                    
                    numericId = id.replace(type, "");
                    String collectionPath = getPathforFile(dbc, xmlId, numericId);
                    DBFile dbf = new DBFile(super.DBURI, collectionPath, xmlId, super.DBuser, super.DBpassword);
                    isSaved = dbf.exist("//admin/saved[text() = 'yes']");
                    String status = dbf.queryString("//admin/status/string()")[0];
                    if (status.length() > 0) {
                        category = "primary";
                    } else {
                        category = "secondary";
                    }

                    String[] ret = dbf.queryString("//admin/" + "locked" + "/text()");
                    lockedBy = "";
                    if (ret.length == 0) {
                        lockedBy = null;
                    } else {
                        if (!ret[0].equals("no")) {
                            lockedBy = ret[0];
                        } else {
                            lockedBy = null;
                        }
                    }

                    if (category.equals("primary")) {
                        boolean userCanWrite = dbf.exist("//admin/" + "write" + "[text()='" + this.username + "' or text()='*']");
                        boolean isUnpublished = dbf.exist("//admin/" + "status" + "[text()='" + "unpublished" + "' or text()='*']");
                        boolean isRejected = dbf.exist("//admin/" + "status" + "[text()='" + "rejected" + "' or text()='*']");
                        boolean isPublished = dbf.exist("//admin/" + "status" + "[text()='" + "published" + "' or text()='*']");
                        boolean isPending = dbf.exist("//admin/" + "status" + "[text()='" + "pending" + "' or text()='*']");

                        if (userCanWrite && (isUnpublished || isRejected)) {
                            if (isSaved && lockedBy != null) {
                                if (lockedBy.equals(this.username) == false) {
                                    displayMsg = "IS_EDITED_BY_USER";
                                    System.out.println(displayMsg);
                                    hresponse.sendRedirect(systemURL + "/SystemMessages?editorName=3MEditor&message=" + displayMsg + "&file=" + xmlId + "&lang=" + lang + "&feXMLEditor=yes");

                                } else {
                                    //prepei na proste8ei to para8uro me th sunexeia
                                    //epeksergiasias pu se paei se action unlockedit
                                    displayMsg = "IS_EDITED_BY_YOU";
                                    System.out.println(displayMsg);
                                    hresponse.sendRedirect(systemURL + "/SystemMessages?editorName=3MEditor&message=" + displayMsg + "&file=" + xmlId + "&lang=" + lang + "&feXMLEditor=yes&type=" + type + "&category=" + category + "&link=yes" +"&action="+action);
//
                                    // goOnLink = "Index?type=" + type + "&amp;id=" + id + "&amp;langen&amp;category=primary&amp;action=unlockedit";
                                }
                            } else {
                                setAdminProperty("locked", this.username, dbf);
                                chain.doFilter(request, response);
                            }
                        } else if (!userCanWrite && (isUnpublished || isRejected)) {
                            displayMsg = "CANNOT_EDIT";
                            System.out.println(displayMsg);
                            hresponse.sendRedirect(systemURL + "/SystemMessages?editorName=3MEditor&message=" + displayMsg + "&file=" + xmlId + "&lang=" + lang + "&feXMLEditor=yes");

                        } else if (isUnpublished == false && isRejected == false) {
                            if (isPublished) {
                                displayMsg += "DOC_IS_PUBLISHED";
                                System.out.println(displayMsg);
                                hresponse.sendRedirect(systemURL + "/SystemMessages?editorName=3MEditor&message=" + displayMsg + "&file=" + xmlId + "&lang=" + lang + "&feXMLEditor=yes");

                            } else if (isPending) {
                                displayMsg += "DOC_IS_PENDING";
                                System.out.println(displayMsg);
                                hresponse.sendRedirect(systemURL + "/SystemMessages?editorName=3MEditor&message=" + displayMsg + "&file=" + xmlId + "&lang=" + lang + "&feXMLEditor=yes");

                            }
                        }
                    } else if (category.equals("secondary")) {
//                        boolean hasDependants = dbf.exist("//admin/refs_by/ref_by[@published='yes']");

                        boolean hasDependants = dbf.exist("//admin/refs_by/ref_by[@isUnpublished='false']");

                        String rights = getRights();
                        String userOrg = getUserGroup();
                        ret = dbf.queryString("//admin/" + "organization" + "/text()");
                        String docOrg = "";
                        if (ret.length == 0) {
                            docOrg = null;
                        } else {
                            docOrg = ret[0];
                        }
                        if (rights.equals("guest") || rights.equals("sysadmin")) {
                            displayMsg = "ACCESS_DENIED";
                            System.out.println(displayMsg);
                            hresponse.sendRedirect(systemURL + "/SystemMessages?editorName=3MEditor&message=" + displayMsg + "&file=" + xmlId + "&lang=" + lang + "&feXMLEditor=yes");

                        } else if (docOrg == null) {
                            displayMsg = "INTERNAL_ERROR";
                            System.out.println(displayMsg);

                            hresponse.sendRedirect(systemURL + "/SystemMessages?editorName=3MEditor&message=" + displayMsg + "&file=" + xmlId + "&lang=" + lang + "&feXMLEditor=yes");

                        } else if (!docOrg.equals(userOrg)) {
                            //only users of organization can edit an 'Entity'
                            //displayMsg = Messages.ACCESS_DENIED;
                            displayMsg = "CANNOT_EDIT";
                            System.out.println(displayMsg);
                            hresponse.sendRedirect(systemURL + "/SystemMessages?editorName=3MEditor&message=" + displayMsg + "&file=" + xmlId + "&lang=" + lang + "&feXMLEditor=yes");

                        } else if (hasDependants) {
                            displayMsg = "HAS_DEPENDANTS";

                            System.out.println(displayMsg);
                            hresponse.sendRedirect(systemURL + "/SystemMessages?editorName=3MEditor&message=" + displayMsg + "&file=" + xmlId + "&lang=" + lang + "&feXMLEditor=yes&type=" + type);

                        } else if (isSaved && lockedBy != null) {
                            if (!lockedBy.equals(username)) {
                                displayMsg = "IS_EDITED_BY_USER";
                                System.out.println(displayMsg);
                                hresponse.sendRedirect(systemURL + "/SystemMessages?editorName=3MEditor&message=" + displayMsg + "&file=" + xmlId + "&lang=" + lang + "&feXMLEditor=yes");

                            } else {
                                displayMsg = "IS_EDITED_BY_YOU";
                                System.out.println(displayMsg);
                                //   goOnLink = "Index?type=" + type + "&id=" + id + "&lang="+lang+"&category=secondary&action=unlockedit";
                                hresponse.sendRedirect(systemURL + "/SystemMessages?editorName=3MEditor&message=" + displayMsg + "&file=" + xmlId + "&lang=" + lang + "&feXMLEditor=yes&type=" + type + "&category=" + category + "&link=yes" +"&action="+action);
                            }
                        } else {
                            setAdminProperty("locked", this.username, dbf);
                            chain.doFilter(request, response);

                        }

                    }
                } else if (action.equals("unlockedit")) {
                    numericId = id.replace(type, "");
                    String collectionPath = getPathforFile(dbc, xmlId, numericId);
                    DBFile dbf = new DBFile(super.DBURI, collectionPath, xmlId, super.DBuser, super.DBpassword);
                        String action2 = request.getParameter("action2");
                    if (action2 == null) {
                        action2 = "";
                    }
                    setAdminProperty("locked", "no", dbf);
                    hresponse.sendRedirect("Index?type=" + type + "&id=" + id + "&lang=" + lang + "&category=" + category + "&action=" + action2);
                } else if (action.equals("New")) {
                    String rights = getRights();
                    if (rights.equals("guest") || rights.equals("sysadmin")) {
                        displayMsg = "ACCESS_DENIED";
                        System.out.println(displayMsg);
                        hresponse.sendRedirect(systemURL + "/SystemMessages?editorName=3MEditor&message=" + displayMsg + "&file=" + xmlId + "&lang=" + lang);

                    } else {
                        chain.doFilter(request, response);
                    }
                } else if (action.equals("Open") || action.equals("view") || action.equals("Close") || action.equals("Validate") || action.equals("SaveVocTerm") || action.equals("Save")) {
                    chain.doFilter(request, response);
                }
            }
        }

    }

    /**
     * Return the filter configuration object for this filter.
     */
    /**
     * Destroy method for this filter
     */
    public void destroy() {
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;

    }
}
