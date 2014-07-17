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
package isl.FIMS.filter;

import isl.FIMS.utils.Messages;
import isl.FIMS.servlet.ApplicationBasicServlet;
import isl.FIMS.utils.entity.Config;
import isl.dbms.DBCollection;
import isl.dbms.DBFile;
import isl.dms.DMSConfig;
import isl.dms.DMSException;
import isl.dms.file.DMSUser;
import isl.dms.file.EntryNotFoundException;
import isl.dms.xml.XMLTransform;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author konsolak
 */
public class ValidateAction extends ApplicationBasicServlet implements Filter {

    private FilterConfig filterConfig = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");

            HttpServletRequest hrequest = (HttpServletRequest) request;
            this.initVars(hrequest);
            HttpSession session = hrequest.getSession();
            String systemDbCollection = session.getServletContext().getInitParameter("SystemDbCollection");
            String username = (String) session.getAttribute("username");
            String adminDbCollection = systemDbCollection + "DMSFILES/";
            String DBURI = session.getServletContext().getInitParameter("DBURI");
            String DBuser = session.getServletContext().getInitParameter("DBuser");
            String DBpassword = session.getServletContext().getInitParameter("DBpassword");

            DMSConfig conf = new DMSConfig(DBURI, adminDbCollection, DBuser, DBpassword);
            DMSUser Actions = new DMSUser(username, conf);
            String[] ActionArray = Actions.getActions();
            String userRights = ActionArray[0];

            String path = systemDbCollection + "DMSFILES/applicationFiles/management_xmlFiles/";
            String url = hrequest.getRequestURL().toString();
            String servlet_name = url.substring(url.lastIndexOf("/") + 1);
            String action = request.getParameter("action");
            String type = request.getParameter("type");
            DBCollection col = new DBCollection(DBURI, path, DBuser, DBpassword);

            String query = "//userRights[(contains(./@href,'" + servlet_name + "') or contains(./@id,'" + servlet_name + "')) ";
            if (action != null) {
                query += "and (contains(./@href,'action=" + action + "') or contains(./@id,'action=" + action + "'))";
            }
            if (type != null) {
                query += "and (contains(./@href,'type=" + type + "') or contains(./@id,'type=" + type + "'))";
            }
            // String TryQ= query + "and (contains(./@href,'action=*') or contains(./@id,'action=*')";
            //   Map<String, String[]> params = request.getParameterMap();

            // Iterator it = params.entrySet().iterator();
          /*  while (it.hasNext()) {
             Map.Entry pairs = (Map.Entry) it.next();
             String[] value = (String[]) pairs.getValue();
             if (!pairs.getKey().equals("id") && !pairs.getKey().equals("file")) {
             query += " and (contains(./@href,'" + pairs.getKey() + "=" + value[0] + "') or contains(./@id,'" + pairs.getKey() + "=" + value[0] + "'))";
             System.out.println(pairs.getKey() + " = " + value[0]);
             } else {
             query += " and (contains(./@href,'" + pairs.getKey() + "=') or contains(./@id,'" + pairs.getKey() + "='))";

             }
             it.remove(); // avoids a ConcurrentModificationException
             }*/
            //   query+= ") or ((contains(./@href,'" + servlet_name + "') or contains(./@id,'" + servlet_name + "')) and (contains(./@href,'action=*') or contains(./@id,'action=*')))";
            query += "]/text()";
            System.out.println("query-->" + query);
            DBFile[] res = col.query(query);
            boolean flag = false;
            for (DBFile db : res) {
                String result = db.toString();
                if (result.equals(userRights)) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                System.out.println("Go on!!!!");
                chain.doFilter(request, response);
            } else {
                Config config = new Config("filter");
                String xsl = config.DISPLAY_XSL;
                StringBuffer xml = new StringBuffer(this.xmlStart(this.topmenu, username, this.pageTitle, this.lang, "", hrequest));
                String displayMsg = Messages.ACCESS_DENIED;
                xml.append("<Display>").append(displayMsg).append("</Display>\n");
                xml.append(this.xmlEnd());

                try {
                    System.out.println("Got you hacker!!!!!");
                    PrintWriter out = response.getWriter();
                    XMLTransform xmlTrans = new XMLTransform(xml.toString());
                    xmlTrans.transform(out, xsl);
                } catch (DMSException e) {
                    e.printStackTrace();
                }
            }
        } catch (EntryNotFoundException ex) {
            ex.printStackTrace();
        } catch (DMSException ex) {
            ex.printStackTrace();
        }
    }
}
