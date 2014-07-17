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

import isl.FIMS.utils.UtilsQueries;
import isl.FIMS.utils.search.QueryTools;
import isl.FIMS.utils.search.Config;
import isl.FIMS.utils.entity.GetEntityCategory;
import isl.dbms.DBCollection;
import isl.dbms.DBFile;
import isl.dms.DMSException;
import isl.dms.xml.XMLTransform;
import isl.dms.file.DMSXQuery;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.*;
import javax.servlet.http.*;

public class SearchResults extends BasicSearchServlet {

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
        UtilsQueries u = new UtilsQueries();
        this.initVars(request);
        u.initListPaging(request);

        StringBuffer paginSource = getPaging();

        StringBuffer xml = new StringBuffer();
        String xmlStart = this.xmlStart(this.topmenu, this.username, "Search Results", this.lang, "", request);
        String xmlEnd = this.xmlEnd();
        StringBuffer xmlMiddle = new StringBuffer();

        String[] targets = request.getParameterValues("target");
        String style = request.getParameter("style");
        if (style == null) {
            style = "";
        }
        String category = "";
        String queryXML = null;
        String status = "";
        Hashtable params = this.getParams(request);
        status = (String) params.get("status");
        if (targets == null || targets.length == 0 || targets[0].equals("0")) {
            xml.append(xmlStart);
            xml.append(xmlMiddle);

            //Hashtable params = this.getParams(request);
            category = (String) params.get("category");
            queryXML = QueryTools.getXML(params, this.conf, this.dataCol);

            xml.append(queryXML);

            xml.append("<success return=\"0\">" + "EMPTY_FIELD_TargetCollection" + "</success>\n");
            xml.append(xmlEnd);

            XMLTransform xmlTrans = new XMLTransform(xml.toString());
            String xsl = Config.SEARCH_RESULTS_XSL;
            //mallon auto to if de xreiazetai
          /*  if (status.equals("all")) {
             xsl = Config.ADMIN_SEARCH_RESULTS_XSL;
             }*/
            xmlTrans.transform(out, xsl);
            out.close();
            return;
        }

        String qId = request.getParameter("qid");

        String querySource = null;
        if (request.getMethod().equals("GET") && qId != null && qId.length() != 0) {
            int userId = this.getUserId(request);

            String qName = DMSXQuery.getNameOf(Integer.parseInt(qId), userId, this.conf);
            DMSXQuery query = new DMSXQuery(qName, userId, this.conf);
            targets = query.getTargets();
            querySource = query.getInfo("source");
            queryXML = QueryTools.getXML(query, this.conf);
        } else if (request.getMethod().equals("GET")) {
            //wrong access
            response.sendRedirect("Search");
            return;
        } else {
            //Hashtable params = this.getParams(request);
            category = (String) params.get("category");
            querySource = QueryTools.getSource(params, this.conf, this.dataCol);
            queryXML = QueryTools.getXML(params, this.conf, this.dataCol);
        }
        //System.out.println("SOURCE=" + querySource);
        // System.out.println("PARAMS=" + params);
        long start = System.currentTimeMillis();
        StringBuffer resultsTag = new StringBuffer("<results>\n");
        // If many targets then
        for (int i = 0; i < targets.length; i++) {
            DBCollection queryCol = new DBCollection(this.DBURI, targets[i], this.DBuser, this.DBpassword);
            DBFile[] queryRes = queryCol.query(querySource);
            for (int j = 0; j < queryRes.length; j++) {
                String res = queryRes[j].getXMLAsString().replaceAll("&lt;", "<");
                res = res.replaceAll("&gt;", ">");
                resultsTag.append(res).append("\n");
            }
        }
        resultsTag.append("\n</results>");
        long elapsedTimeMillis = System.currentTimeMillis() - start;
        float elapsedTimeSec = elapsedTimeMillis / 1000F;

        xmlMiddle.append(resultsTag).append("<querytime>" + elapsedTimeSec + "</querytime>\n").append(queryXML);

        xml.append(xmlStart);
        //CRINNO-KMNINI-IOS pros8hkh...for actions on file
        xml.append("<EntityCategory>").append(GetEntityCategory.getEntityCategory(category)).append("</EntityCategory>\n");
        xml.append("<URI_Reference_Path>").append(this.URI_Reference_Path).append("</URI_Reference_Path>");
        xml.append("<EntityType>").append(category).append("</EntityType>");
        xml.append("<IsGuestUser>").append(this.userHasAction("guest")).append("</IsGuestUser>\n");
        xml.append("<DocStatus>").append(this.status).append("</DocStatus>\n");
        xml.append("<ServletName>").append(this.servletName).append("</ServletName>\n");
        xml.append("<SearchMode>").append(style).append("</SearchMode>\n");

        if (targets != null) {
            xml.append("<TargetCol>").append(targets[0]).append("</TargetCol>");
        }

        //until here...

        //Samarita's hack
        DBFile dataTypes = new DBFile(this.DBURI, this.adminDbCollection, this.dataTypesFile, this.DBuser, this.DBpassword);



        xml.append(xmlMiddle);
        xml.append("<success return=\"1\"></success>\n");
        xml.append(dataTypes.toString());
        xml.append(xmlEnd);
        XMLTransform xmlTrans = new XMLTransform(xml.toString());
        String xsl = Config.SEARCH_RESULTS_XSL;
        xmlTrans.transform(out, xsl);
        out.close();
    }

    public static StringBuffer getPaging() {
        //PAGING CODE
        int firstPage = listStartPage;
        int lastPage = listLastPage;
        String step = listStep;
        String pagingM = pagingMax;
        int currentP = currentPage;
        int first = showfirst;
        int last = showlast;


        StringBuffer inQuerySource = new StringBuffer();
        inQuerySource.append("return $i,\n").append("$count := count($results),\n").append("$k := 0,\n").append("$l := 0,\n").append("$start := " + firstPage + ",\n").append("$max := " + lastPage + ",\n").append("$first := " + first + ",\n").append("$last := " + last + ",\n").append("$end := if($count >= $max) then $max else $count,\n").append("$currentP := " + currentP + ",\n").append("$queryPages := if ($count mod " + step + " > 0) then ($count idiv " + step + ")+1 else $count idiv " + step + ",\n").append("$showfirst := if ($first = 0) then 1 else if ($first < 0) then 1 else $first,\n").append("$showlast := if ($last > $queryPages) then $queryPages else $last\n").append("return\n").append("<stats count=\"{$count}\" currentP=\"{$currentP}\" showfirst=\"{$showfirst}\" showlast=\"{$showlast}\" end=\"{$end}\" queryPages=\"{$queryPages}\" start=\"{$start}\" step=\"" + step + "\">\n").append("<pageLoop>\n").append("{for $k in 1 to $queryPages\n").append("return <lista>{$k}</lista>}\n").append("</pageLoop>\n").append("<showPages>\n").append("{for $l in $showfirst to $showlast\n").append("return <show>{$l}</show>}\n").append("</showPages>\n").append("{for $j in $start to $end\n").append("let $current := item-at($results, $j)\n");


        return inQuerySource;
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
