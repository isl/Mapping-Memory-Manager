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
package isl.FIMS.utils;

import isl.FIMS.servlet.ApplicationBasicServlet;
import static isl.FIMS.servlet.ApplicationBasicServlet.conf;
import isl.FIMS.utils.entity.GetEntityCategory;
import isl.dbms.DBCollection;
import isl.dbms.DBFile;
import isl.dbms.DBMSException;
import isl.dms.DMSException;
import isl.dms.file.DMSTag;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author konsolak
 */
public class UtilsQueries extends ApplicationBasicServlet {
    
    
   
    public static String getpath(String database, String collection, String user, String password, String type, String id) {
        String collectionpath = "";
        String queryString = "for $i in //admin[id='" + id + "']return document-uri(root($i))";
        DBCollection col = new DBCollection(database, collection, user, password);
        try {
            DBFile[] dbf = col.query(queryString);
            String path = dbf[0].toString();

            collectionpath = path.split(type + id + ".xml")[0];
        } catch (DBMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return collectionpath;

    }

    public static String getPathforFile(DBCollection col, String filename, String id) {

        String queryString = "for $i in //admin[id='" + id + "']return document-uri(root($i))";
        DBFile[] dbf = col.query(queryString);
        String path = dbf[0].toString();
        String collectionpath = path.split("/" + filename)[0];
        return collectionpath;
    }

    public static int getLastId(DBCollection col) {
        int id = 0;
        try {
            String queryString = "let $results := for $i in //id/text() return $i,$maximun:=max($results)return $maximun";
            DBFile[] dbf = col.query(queryString);
            if (dbf.length != 0) {
                String idString = dbf[0].toString();
                id = Integer.parseInt(idString);
            }
        } catch (DBMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return id;
    }

    public static String getminColl(String systemDBCollection, String type, DBCollection col) {
        String results = "";
        String queryString = "let $results := for $i in xmldb:get-child-collections('" + systemDBCollection + type + "') " + "let  $b := xcollection(concat('" + systemDBCollection + type + "/',$i)) " + "return count($b), " + "$minimum:=min($results), " + "$coll:=for $c in xmldb:get-child-collections('" + systemDBCollection + type + "') " + "let  $b := xcollection(concat('" + systemDBCollection + type + "/',$c)) " + "where count($b)=$minimum return <coll>{$c}</coll> " + "return " + "<results> " + "<min>{$minimum}</min><res>{$coll}</res></results> ";
        try {
            DBFile[] dbf = col.query(queryString);
            results = dbf[0].toString();
        } catch (DBMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return results;
    }

    public static String getmaxCollName(String systemDBCollection, String type, DBCollection col) {
        String results = "";
        String queryString = "let $results :=" + "for $i in xmldb:get-child-collections('" + systemDBCollection + type + "')" + "let  $b := xcollection(concat('" + systemDBCollection + type + "/',$i))" + "return number($i)," + "$maximum:=max($results)" + "return $maximum";
        try {
            DBFile[] dbf = col.query(queryString);
            results = dbf[0].toString();
        } catch (DBMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return results;
    }
    
    
     public  String listEntityQuery(String type, String status, String mode, String organization, String lang, String username, StringBuffer outputsTag) {

        String rootXPath = "";
        String rootPath = "";
        try {
            rootXPath = DMSTag.valueOf("rootxpath", "target", type, conf)[0];
        } catch (DBMSException e) {
            e.printStackTrace();
        } catch (DMSException e) {
            e.printStackTrace();
        }
        if (!rootXPath.equals("")) {
            rootPath = rootXPath.substring(rootXPath.lastIndexOf("/") + 1);
        }

        String queryCond = this.getQueryConditions(type, status, mode, organization, lang,username);
        outputsTag.append("<Root>" + rootXPath + "/*</Root>\n");
        outputsTag.append("<outputs>\n");

        StringBuffer queryOrderBy = new StringBuffer();
        StringBuffer queryRet = new StringBuffer();
        StringBuffer[] query = new StringBuffer[3];
        query[0] = outputsTag;
        query[1] = queryRet;
        query[2] = queryOrderBy;

        query = UtilsXPaths.getXpathQuery(query, type);
        outputsTag = query[0];
        queryRet = query[1];
        queryOrderBy = query[2];
        //lang comment
//        outputsTag.append("<path xpath=\"xxx\" selected=\"yes\">").append("lang").append("</path>\n");
        outputsTag.append("<path xpath=\"xxx\" selected=\"yes\">").append("URI_ID").append("</path>\n");

        outputsTag.append("</outputs>\n");
//append("<lang>\n{$current/" + rootPath + "/admin/lang}\n</lang>\n")
        queryRet.append("<filename><filename>{fn:tokenize($current/" + rootPath + "/admin/uri_id/text(),'" + this.URI_Reference_Path + "')[last()]}</filename></filename>\n").append("<FileId>{replace(util:document-name($current),\".xml\",\"\")}</FileId>\n");
        queryRet.append("<hiddenResults>");
        queryRet.append("<FileId>{replace(util:document-name($current),\".xml\",\"\")}</FileId>\n");
        queryRet.append("{$current/" + rootPath + "/admin/organization}\n");
        queryRet.append("<hasPublicDependants>{exists($current/" + rootPath + "/admin/refs_by/ref_by[./@isUnpublished='false'])}\n</hasPublicDependants>");
        queryRet.append("<userHasWrite>{$current/" + rootPath + "/admin/write/text()='" + username + "'}\n</userHasWrite>");
        queryRet.append("<isImported>{exists($current/" + rootPath + "/admin/imported)}\n</isImported>");
        queryRet.append("</hiddenResults>");
        queryRet.append("</result>\n");
        //=====Build query source=====
        StringBuffer querySource = new StringBuffer();

        //PAGING CODE
        int firstPage = this.listStartPage;
        int lastPage = this.listLastPage;
        String step = this.listStep;
        String pagingM = this.pagingMax;
        int currentP = this.currentPage;
        int first = this.showfirst;
        int last = this.showlast;

        //String pagesLoop=this.pageLoop;

        StringBuffer inQuerySource = new StringBuffer();
        inQuerySource.append("return $i,\n").append("$count := count($results),\n").append("$k := 0,\n").append("$l := 0,\n").append("$start := " + firstPage + ",\n").append("$max := " + lastPage + ",\n").append("$first := " + first + ",\n").append("$last := " + last + ",\n").append("$end := if($count >= $max) then $max else $count,\n").append("$currentP := " + currentP + ",\n").append("$queryPages := if ($count mod " + step + " > 0) then ($count idiv " + step + ")+1 else $count idiv " + step + ",\n") // .append("$lastPage := if "+last+",\n")
                .append("$showfirst := if ($first = 0) then 1 else if ($first < 0) then 1 else $first,\n").append("$showlast := if ($last > $queryPages) then $queryPages else $last\n").append("return\n").append("<stats count=\"{$count}\" currentP=\"{$currentP}\" showfirst=\"{$showfirst}\" showlast=\"{$showlast}\" end=\"{$end}\" queryPages=\"{$queryPages}\" start=\"{$start}\" step=\"" + step + "\">\n").append("<pageLoop>\n").append("{for $k in 1 to $queryPages\n").append("return <lista>{$k}</lista>}\n").append("</pageLoop>\n").append("<showPages>\n").append("{for $l in $showfirst to $showlast\n").append("return <show>{$l}</show>}\n").append("</showPages>\n").append("{for $j in $start to $end\n").append("let $current := item-at($results, $j)\n");

        inQuerySource.append("let $groupname := string(document('" + this.adminDbCollection + this.conf.GROUPS_FILE + "')//group[@id=$current//admin/organization]/@groupname)\n");
        //until here paging

        //PAGING CODE
        querySource.append("let $results :=\n");
        querySource.append("for $i in collection ('" + this.systemDbCollection + type + "')/" + rootXPath + "\n");
        querySource.append(queryCond);
        querySource.append(queryOrderBy);
        querySource.append(inQuerySource);
        querySource.append(queryRet);

        //========== PAGING CODE ==========
        querySource.append("}\n</stats>");
        return querySource.toString();

    }

    public String listAllEntityQuery(String type, String status, String mode, String organization, String lang, String username, StringBuffer outputsTag, String queryCond) {

        String rootXPath = "";

        try {
            rootXPath = DMSTag.valueOf("rootxpath", "target", type, conf)[0];
        } catch (DBMSException e) {
            e.printStackTrace();
        } catch (DMSException e) {
            e.printStackTrace();
        }

        outputsTag.append("<outputs>\n");

        StringBuffer queryOrderBy = new StringBuffer();
        StringBuffer queryRet = new StringBuffer();

        StringBuffer[] query = new StringBuffer[3];
        query[0] = outputsTag;
        query[1] = queryRet;
        query[2] = queryOrderBy;
        query = UtilsXPaths.getXpathQuery(query, type);
        outputsTag = query[0];
        outputsTag.append("<path xpath=\"xxx\" selected=\"yes\">").append("lang").append("</path>\n");

        queryRet = query[1];
        queryRet.append("<lang>\n{$current" + rootXPath + "/admin/lang}\n</lang>\n");
        queryOrderBy = query[2];
//lang comment
        //    outputsTag.append("<path xpath=\"xxx\" selected=\"yes\">").append("lang").append("</path>\n");
        outputsTag.append("<path xpath=\"xxx\" selected=\"yes\">").append("URI_ID").append("</path>\n");

        outputsTag.append("</outputs>\n");
//append("<lang>\n{$current" + rootXPath + "/admin/lang}\n</lang>\n")
        queryRet.append("<filename><filename>{fn:tokenize($current/" + rootXPath + "/admin/uri_id/text(),'" + this.URI_Reference_Path + "')[last()]}</filename></filename>\n").append("<FileId>{replace(util:document-name($current),\".xml\",\"\")}</FileId>\n").append("</result>\n");
        //PAGING CODE
        int firstPage = this.listStartPage;
        int lastPage = this.listLastPage;
        String step = this.listStep;
        String pagingM = this.pagingMax;
        int currentP = this.currentPage;
        int first = this.showfirst;
        int last = this.showlast;
        StringBuffer inQuerySource = new StringBuffer();
        inQuerySource.append("return $i,\n").append("$count := count($results),\n").append("$k := 0,\n").append("$l := 0,\n").append("$start := " + firstPage + ",\n").append("$max := " + lastPage + ",\n").append("$first := " + first + ",\n").append("$last := " + last + ",\n").append("$end := if($count >= $max) then $max else $count,\n").append("$currentP := " + currentP + ",\n").append("$queryPages := if ($count mod " + step + " > 0) then ($count idiv " + step + ")+1 else $count idiv " + step + ",\n") // .append("$lastPage := if "+last+",\n")
                .append("$showfirst := if ($first = 0) then 1 else if ($first < 0) then 1 else $first,\n").append("$showlast := if ($last > $queryPages) then $queryPages else $last\n").append("return\n").append("<stats count=\"{$count}\" currentP=\"{$currentP}\" showfirst=\"{$showfirst}\" showlast=\"{$showlast}\" end=\"{$end}\" queryPages=\"{$queryPages}\" start=\"{$start}\" step=\"" + step + "\">\n").append("<pageLoop>\n").append("{for $k in 1 to $queryPages\n").append("return <lista>{$k}</lista>}\n").append("</pageLoop>\n").append("<showPages>\n").append("{for $l in $showfirst to $showlast\n").append("return <show>{$l}</show>}\n").append("</showPages>\n").append("{for $j in $start to $end\n").append("let $current := item-at($results, $j)\n");


        inQuerySource.append("let $groupname := string(document('" + this.adminDbCollection + this.conf.GROUPS_FILE + "')//group[@id=$current//admin/organization]/@groupname)\n");

        //=====Build query source=====
        //Diafora me prin einai oti exoume pantou '$i' anti gia '$current'
        //opws epishs to idio isxuei kai sto return-result meros...
        StringBuffer querySource = new StringBuffer();
        querySource.append("let $results :=\n");
        querySource.append("for $i in collection('" + this.systemDbCollection + type + "')\n");
        querySource.append("let $groupname := string(document('" + this.adminDbCollection + this.conf.GROUPS_FILE + "')//group[@id=$i//admin/organization]/@groupname)\n");
        querySource.append("let $j := -1\n");
        querySource.append("let $current := $i\n");
        querySource.append(queryCond);
        querySource.append(queryOrderBy);
        querySource.append(inQuerySource);
        querySource.append(queryRet);
        //========== PAGING CODE ==========
        querySource.append("}\n</stats>");
        return querySource.toString();
    }

    public String getQueryConditionForTranslate(String type, String status, String mode, String organization, String lang) {
        //'type'.xml is the prototype of each Entity (with admin/id=0). Do not show it
        StringBuffer queryCond = new StringBuffer();
        queryCond.append("\nwhere $i//admin/id != '0'\n");

        boolean langCheck = true;
        if (mode.startsWith("trans")) {
            if (mode.equals("trans_")) {
                mode = "";
            } else {
                mode = mode.split("_")[1];
            }

            langCheck = false;
        }

        //show only the saved ones
        queryCond.append("and $i//admin/saved='yes'\n");

        //show only docs of specified lang
        if (langCheck && lang != null && lang.equals("*") == false) {
            queryCond.append("and $i//admin/lang='" + lang + "'\n");
        } else {
            queryCond.append("and $i//admin/lang!='" + lang + "'\n");
        }

        if ((GetEntityCategory.getEntityCategory(type).equals("primary")) && status != null && status.equals("published") == false && mode.equals("sys") == false && userHasAction("sysadmin") == false) {
            queryCond.append("and ($i//admin/organization='" + organization + "' or $i//admin/status='published') \n");
        }

        if (status != null && status.length() != 0) {
            if (status.equals("all" + Messages.UNPUBLISHED)) {
                queryCond.append("and ($i//admin/write='*' or $i//admin/write='" + username + "')\n");
                queryCond.append("and ($i//admin/status='" + Messages.UNPUBLISHED + "' or $i//admin/status='" + Messages.PENDING + "' or $i//admin/status='" + Messages.REJECTED + "')\n");
            } else if (status.equals("org")) {
                queryCond.append("and not ($i//admin/write='*' or $i//admin/write='" + username + "')\n");
                queryCond.append("and ($i//admin/read='*' or $i//admin/read='" + username + "')\n");
                queryCond.append("and ($i//admin/status='" + Messages.UNPUBLISHED + "' or $i//admin/status='" + Messages.PENDING + "' or $i//admin/status='" + Messages.REJECTED + "')\n");
            } else if (status.equals(Messages.UNPUBLISHED)) {
                //queryCond.append("and $i//admin/status='"+Messages.UNPUBLISHED+"'\n");
                //Kai ta 'REJECTED' mazi...
                queryCond.append("and ($i//admin/status='" + Messages.UNPUBLISHED + "' or $i//admin/status='" + Messages.REJECTED + "')\n");
            } else {
                queryCond.append("and $i//admin/status='" + status + "'\n");
            }
        }

        return queryCond.toString();
    }

    public String getQueryConditions(String type, String status, String mode, String organization, String lang, String username) {
        //'type'.xml is the prototype of each Entity (with admin/id=0). Do not show it
        StringBuffer queryCond = new StringBuffer();

        queryCond.append("\n/admin[not (id='0') and saved ='yes' \n");

        boolean langCheck = true;
        if (mode.startsWith("trans")) {
            if (mode.equals("trans_")) {
                mode = "";
            } else {
                mode = mode.split("_")[1];
            }
            langCheck = false;
        }

        if (langCheck && lang != null && lang.equals("*") == false) // eva  queryCond.append("and $i//admin/lang='"+lang+"'\n");
        {
            queryCond.append("and lang='" + lang + "'\n");
        }
        if ((GetEntityCategory.getEntityCategory(type).equals("primary")) && status != null && status.equals("published") == false && mode.equals("sys") == false && userHasAction("sysadmin") == false) // eva queryCond.append("and $i//admin/organization='"+organization+"'\n");
        {
            queryCond.append("and (organization ='" + organization + "' or status='published')\n");
        }

        if (status != null && status.length() != 0) {
            if (status.equals("all" + Messages.UNPUBLISHED)) {
                queryCond.append("and write = ('*','" + username + "')\n");
                queryCond.append("and status = ('" + Messages.UNPUBLISHED + "', '" + Messages.PENDING + "', '" + Messages.REJECTED + "')");
            } else if (status.equals("org")) {
                queryCond.append("and not (write= ('*','" + username + "'))\n");
                queryCond.append("and read =('*','" + username + "')\n");
                queryCond.append("and status = ('" + Messages.UNPUBLISHED + "', '" + Messages.PENDING + "', '" + Messages.REJECTED + "')");
            } else if (status.equals(Messages.UNPUBLISHED)) {
                queryCond.append("and status = ('" + Messages.UNPUBLISHED + "', '" + Messages.REJECTED + "') ");
            } else if (status.equals("imported")) {
                queryCond.append("and exists(imported)");
            } else {
                queryCond.append("and status ='" + status + "'");
            }
        }
        queryCond.append("]/../..\n");
        return queryCond.toString();
    }

    public void initListPaging(HttpServletRequest request) {
        //String listStart = request.getParameter("start");
        String pages = request.getParameter("pages") != null ? request.getParameter("pages") : "";
        String current = request.getParameter("current") != null ? request.getParameter("current") : "";
        String newP = request.getParameter("newP") != null ? request.getParameter("newP") : "";
        String move = request.getParameter("move") != null ? request.getParameter("move") : "";

        if (pages == "") {
            this.currentPage = 1;
            this.listStartPage = 1;
            this.queryPages = "";
        } else if (current != "" && move != "") {
            this.queryPages = pages;
            int size = this.queryPages.length();

            if (move.trim().equals("next")) {
                this.currentPage = Integer.parseInt(current) + 1;
                this.listStartPage = (this.currentPage * Integer.parseInt(this.listStep)) - Integer.parseInt(this.listStep) + 1;
            } else if (move.trim().equals("prev")) {
                this.currentPage = Integer.parseInt(current) - 1;
                this.listStartPage = (this.currentPage * Integer.parseInt(this.listStep)) - Integer.parseInt(this.listStep) + 1;
            } else if (move.trim().equals("first")) {
                this.currentPage = 1;
                this.listStartPage = (this.currentPage * Integer.parseInt(this.listStep)) - Integer.parseInt(this.listStep) + 1;
            } else if (move.trim().equals("last")) {
                this.currentPage = Integer.parseInt(this.queryPages);
                this.listStartPage = (this.currentPage * Integer.parseInt(this.listStep)) - Integer.parseInt(this.listStep) + 1;
            }
        } else {
            if (newP != "") {
                this.currentPage = Integer.parseInt(newP);
                this.listStartPage = (this.currentPage * Integer.parseInt(this.listStep)) - Integer.parseInt(this.listStep) + 1;
            }
        }
        this.listLastPage = this.listStartPage + Integer.parseInt(this.listStep) - 1;

        if (this.currentPage >= 3) {
            this.showfirst = this.currentPage - Integer.parseInt(this.pagingMax);
            this.showlast = this.currentPage + Integer.parseInt(this.pagingMax);
        } else if (this.currentPage == 1) {
            this.showfirst = 1;
            this.showlast = this.currentPage + (2 * Integer.parseInt(this.pagingMax));
        } else if (this.currentPage == 2) {
            this.showfirst = 1;
            this.showlast = this.currentPage + (2 * Integer.parseInt(this.pagingMax)) - 1;
        }



    }
    
}
