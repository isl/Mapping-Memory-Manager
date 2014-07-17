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
package isl.FIMS.utils.search;

import fixDates.SISdate;
import isl.FIMS.utils.entity.GetEntityCategory;
import isl.FIMS.utils.Messages;
import isl.FIMS.servlet.ApplicationBasicServlet;
import isl.FIMS.servlet.search.SearchResults;
import isl.FIMS.utils.UtilsXPaths;
import isl.dbms.DBMSException;
import isl.dms.DMSConfig;
import isl.dms.DMSException;
import isl.dms.file.DMSTag;
import isl.dms.file.DMSUser;
import isl.dms.file.DMSXQuery;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QueryTools {

    public static String getSource(Hashtable params, DMSConfig conf, String dataCol) {
        String[] targets = (String[]) params.get("targets");
        String operator = (String) params.get("operator");
        String[] inputs = (String[]) params.get("inputs");
        String[] inputsOpers = (String[]) params.get("inputsOpers");
        String[] inputsValues = (String[]) params.get("inputsValues");
        String[] outputs = (String[]) params.get("outputs");
        String[] users = (String[]) params.get("user");
        String[] extraOrgs = (String[]) params.get("extraOrg");
        String code = (String) params.get("code");
        String category = (String) params.get("category");
        String lang = (String) params.get("lang");
        String status = (String) params.get("status");
        if (status.equals("null")) {
            status = null;
        }
        StringBuffer queryEnd = new StringBuffer("return\n<result pos='{$j}'>\n");

        if (outputs.length == 0) {
            outputs = UtilsXPaths.getOutputResultForSimpleSearch(category);
            for (int i = 0; i < outputs.length; i++) {
                String[] outs = outputs[i].split("/");
                String outTag = outs[outs.length - 1];

                queryEnd.append("<" + outTag + ">\n").append("{$current").append(outputs[i]).append("}\n").append("</" + outTag + ">\n");
            }
        } else {
            Map<String, String> listing = new HashMap();
            for (int i = 0; i < outputs.length; i++) {
                if (outputs[i] != null) {

                    String[] outs = outputs[i].split("/");
                    String outTag = outs[outs.length - 1];
                    listing.put(outTag, outputs[i]);
                }
            }

            String temp[] = new String[outputs.length];
            for (int i = 0; i < outputs.length; i++) {
                if (outputs[i] != null) {
                    String[] outs = outputs[i].split("/");
                    temp[i] = outs[outs.length - 1];
                }
            }
            Arrays.sort(temp);
            for (int i = 0; i < temp.length; i++) {
                if (temp[i] != null) {
                    queryEnd.append("<" + temp[i] + ">\n").append("{$current").append(listing.get(temp[i])).append("}\n").append("</" + temp[i] + ">\n");
                }
            }
        }

        String username = (String) params.get("username");
        String organization = (String) params.get("organization");
        String mode = "";
        if (userHasAction("sysadmin", username, conf)) {
            mode = "sys";
        }

        StringBuffer paginSource = SearchResults.getPaging();

        StringBuffer queryTargets = new StringBuffer();
        StringBuffer queryWhere = new StringBuffer();
        for (int i = 0; i < targets.length; i++) {
            queryTargets.append(targets[i]).append(",");

            // string() is needed for time...
            //Samarita's hack part2 to handle eXist bug...
            //queryWhere.append("\n$i//admin/id/string() != '0'\n and");
            queryWhere.append("\n$id != '0'\n and");
            //end hack
        }

        try {
            queryTargets.delete(queryTargets.lastIndexOf(","), queryTargets.length());
            queryWhere.delete(queryWhere.lastIndexOf("\n and"), queryWhere.length());
        } catch (StringIndexOutOfBoundsException se) {
        }
        queryWhere.append("\n");

        String type = category;

        //komnini pros8hkh...
        //---------- Conditions ----------//
        String rootXPath = "";
        try {
            rootXPath = DMSTag.valueOf("rootxpath", "target", type, conf)[0];
        } catch (DBMSException e) {
            e.printStackTrace();
        } catch (DMSException e) {
            e.printStackTrace();
        }

        boolean langCheck = true;

        //show only the saved ones
        queryWhere.append("and $i//admin/saved='yes'\n");

        //show only docs of specified lang, if user is not 'sys' admin
        if (langCheck && lang != null && lang.equals("*") == false) {
            queryWhere.append("and $i//admin/lang='" + lang + "'\n");
        }
        //show only organization's ones, if it is about AP and user is not 'sys' admin
        if (GetEntityCategory.getEntityCategory(type).equals("primary") && status != null && !status.equals("all") && status.equals("published") == false && mode.equals("sys") == false) {
            queryWhere.append("and ($i//admin/organization='" + organization + "' or $i//admin/status='published') \n");
        }

        if (status != null && status.length() != 0) {
            if (status.equals("all" + Messages.UNPUBLISHED)) {
                queryWhere.append("and ($i//admin/write='*' or $i//admin/write='" + username + "')\n");
                queryWhere.append("and ($i//admin/status='" + Messages.UNPUBLISHED + "' or $i//admin/status='" + Messages.PENDING + "' or $i//admin/status='" + Messages.REJECTED + "')\n");
            } else if (status.equals("org")) {
                queryWhere.append("and not ($i//admin/write='*' or $i//admin/write='" + username + "')\n");
                queryWhere.append("and ($i//admin/read='*' or $i//admin/read='" + username + "')\n");
                queryWhere.append("and ($i//admin/status='" + Messages.UNPUBLISHED + "' or $i//admin/status='" + Messages.PENDING + "' or $i//admin/status='" + Messages.REJECTED + "')\n");
            } else if (status.equals(Messages.UNPUBLISHED)) {
                queryWhere.append("and $i//admin/status='" + status + "'\n");
            } else if (status.equals("notPending")) {
                queryWhere.append("and (($i//admin/status='" + Messages.UNPUBLISHED + "' or $i//admin/status='" + Messages.REJECTED + "')\n");
                queryWhere.append("or ($i//admin/status='published'))\n");
            } else if (status.equals("notPublished")) {
                queryWhere.append("and ($i//admin/status='" + Messages.UNPUBLISHED + "' or $i//admin/status='" + Messages.PENDING + "' or $i//admin/status='" + Messages.REJECTED + "')\n");
            } else if (status.equals("notUnpublished")) {
                queryWhere.append("and (($i//admin/status='" + Messages.PENDING + "')\n");
                queryWhere.append("or ($i//admin/status='published'))\n");
            } else if (!status.equals("all")) {
                queryWhere.append("and $i//admin/status='" + status + "'\n");
            }
        }
        if (extraOrgs != null) {
            if (extraOrgs.length > 1) {
                queryWhere.append("and ($i//admin/organization='" + extraOrgs[0] + "'\n");
                for (int i = 1; i < extraOrgs.length; i++) {
                    queryWhere.append("or $i//admin/organization='" + extraOrgs[i] + "'\n");
                }
                queryWhere.append(")\n");
            } else {
                queryWhere.append("and $i//admin/organization='" + extraOrgs[0] + "'\n");

            }
        }
        if (users != null) {
            if (GetEntityCategory.getEntityCategory(type).equals("primary")) {
                if (users.length > 1) {
                    queryWhere.append("and ($i//admin/write='" + users[0] + "'\n");
                    for (int i = 0; i < users.length; i++) {
                        queryWhere.append("or $i//admin/write='" + users[i] + "'\n");
                    }
                    queryWhere.append(")\n");
                } else {
                    queryWhere.append("and $i//admin/write='" + users[0] + "'\n");
                }
            } else {
                if (users.length > 1) {
                    queryWhere.append("and ($i//admin/creator='" + users[0] + "'\n");
                    for (int i = 0; i < users.length; i++) {
                        queryWhere.append("or $i//admin/creator='" + users[i] + "'\n");
                    }
                    queryWhere.append(")\n");
                } else {
                    queryWhere.append("and $i//admin/creator='" + users[0] + "'\n");
                }
            }
        }
        if (code != "") {
            queryWhere.append("and $i//admin/id='" + code + "'\n");
        }
        //---------- End Of Conditions ----------//

        StringBuffer queryMiddle = new StringBuffer();

        for (int i = 0; i < inputs.length; i++) {
            if (inputsValues[i].equals("")) {
                continue;
            }
            inputsValues[i] = inputsValues[i].replaceAll("'", "");
            inputsValues[i] = inputsValues[i].replaceAll("\"", "");
            inputsValues[i] = inputsValues[i].replaceAll("&", "");

            queryMiddle.append(getPredicate(inputs[i], inputsOpers[i], inputsValues[i])).append(operator).append("\n");
        }
        try {
            queryMiddle.delete(queryMiddle.lastIndexOf(operator), queryMiddle.length());
        } catch (StringIndexOutOfBoundsException se) {
        }

        if (queryMiddle.length() > 0) {
            queryWhere.append(" and (\n").append(queryMiddle).append("\n)\n");
        }
        queryMiddle = queryWhere;

        //ws edw...
        ///gia to paging///
        queryMiddle.append(paginSource);

        ///telos paging//////
        // queryEnd.append("<organization><organization>\n{string(document('" + ApplicationBasicServlet.adminDbCollection + ApplicationBasicServlet.conf.GROUPS_FILE + "')//group[@id=$current/" + rootXPath + "/admin/organization]/@groupname)}\n</organization></organization>\n");
        String[] adminPartsTitle;
        String[] adminPartsPath;
        try {
            adminPartsTitle = DMSTag.valueOf("tagname", "adminOutput", category, conf);
            adminPartsPath = DMSTag.valueOf("xpath", "adminOutput", category, conf);

            for (int i = 0; i < adminPartsTitle.length; i++) {
                if (adminPartsTitle[i].equals("AdminOrganization")) {
                    queryEnd.append("<" + adminPartsTitle[i] + "><" + adminPartsTitle[i] + ">\n{string(document('" + ApplicationBasicServlet.adminDbCollection + ApplicationBasicServlet.conf.GROUPS_FILE + "')//group[@id=$current/" + rootXPath + adminPartsPath[i] + "]/@groupname)}\n</" + adminPartsTitle[i] + "></" + adminPartsTitle[i] + ">\n");
                } else {
                    queryEnd.append("<" + adminPartsTitle[i] + ">\n{$current/" + rootXPath + adminPartsPath[i] + "}\n</" + adminPartsTitle[i] + ">\n");

                }
            }
        } catch (DMSException ex) {
            ex.printStackTrace();
        } catch (DBMSException ex) {
            ex.printStackTrace();
        }


        /* if (GetEntityCategory.getEntityCategory(type).equals("primary")) {
         queryEnd.append("<status>\n{$current/" + rootXPath + "/admin/status}\n</status>\n");
         }*/
        //lang comment
        //    queryEnd.append("<lang>\n{$current/" + rootXPath + "/admin/lang}\n</lang>\n");
     queryEnd.append("<info>\n{$current/" + rootXPath + "/admin/info}\n</info>\n");
        //CRINNO Pros8hkh...
        queryEnd.append("<FileId>{replace(util:document-name($current),\".xml\",\"\")}</FileId>\n");
        //ws edw...
        queryEnd.append("<hiddenResults>");
        queryEnd.append("<FileId>{replace(util:document-name($current),\".xml\",\"\")}</FileId>\n");
        queryEnd.append("{$current/" + rootXPath + "/admin/organization}\n");
        queryEnd.append("<hasPublicDependants>{exists($current/" + rootXPath + "/admin/refs_by/ref_by[./@isUnpublished='false'])}\n</hasPublicDependants>");
        queryEnd.append("<userHasWrite>{$current/" + rootXPath + "/admin/write/text()='" + username + "'}\n</userHasWrite>");
        queryEnd.append("<isImported>{exists($current/" + rootXPath + "/admin/imported)}\n</isImported>");
        queryEnd.append("<type>{$current/" + rootXPath + "/admin/type}</type>\n");
        queryEnd.append("</hiddenResults>");
        //diplo 'filename' gia na akolou8oume th symbash me ta outputs
        queryEnd.append("<filename><filename>{fn:tokenize($current/" + rootXPath + "/admin/uri_id/text(),'" + ApplicationBasicServlet.URI_Reference_Path + "')[last()]}</filename></filename>\n");

        queryEnd.append("</result>}</stats>");

//        StringBuffer query = new StringBuffer("let $results:=\n");
//        query.append("for $i in collection('").append(queryTargets).append("')\n");
//        
        StringBuffer query = new StringBuffer("let $collections:=\n");
        query.append("let $b:= xmldb:get-child-collections('" + queryTargets + "')");
        query.append("return count($b),");
        query.append("$collcount:=$collections+" + dataCol + ",");
        query.append("$results:=\n");
        //query.append("for $i in collection('").append(queryTargets).append("')\n");
        query.append("for $m in " + dataCol + " to  $collcount\n");
        query.append("return\n");
        query.append("for $i in collection(concat('" + queryTargets + "/',$m))");

        //eXist 1.1 had a problem with this type of queries, so we decided to change it slightly so that we dont
        //have typecast XQuery exceptions. We probably have to change saved queries too...(if they are useful!)
        //Samarita's hack
        query.append("let $id := $i//admin/id\n");
        //end hack

        if (queryMiddle.length() > 0) {
            query.append("where ").append(queryMiddle).append(queryEnd);
        } else {
            query.append(queryEnd);
        }
        return query.toString();
    }

    //Builds XML representing query (XQueries-like format)
    public static String getXML(Hashtable params, DMSConfig conf, String dataCol) throws DMSException {
        String qId = (String) params.get("qId");
        String category = (String) params.get("category");
        String lang = (String) params.get("lang");
        String status = (String) params.get("status");
        String mnemonicName = (String) params.get("mnemonicName");
        String[] targets = (String[]) params.get("targets");
        String operator = (String) params.get("operator");
        String[] inputs = (String[]) params.get("inputs");
        String[] inputsIds = (String[]) params.get("inputsIds");
        String[] inputsParameters = (String[]) params.get("inputsParameters");
        String[] inputsValues = (String[]) params.get("inputsValues");
        String[] outputs = (String[]) params.get("outputs");

        //Samarita (selectable operator)
        String[] inputsOpers = (String[]) params.get("inputsOpers");

        String[] tagXPaths = DMSTag.valueOf("xpath", "target", category, conf);
        String[] tagDisplayNames = DMSTag.valueOf("displayname/" + conf.LANG, "target", category, conf);

        java.util.Arrays.sort(targets);
        StringBuffer targetsTag = new StringBuffer("<targets>\n");
        for (int i = 0; i < tagXPaths.length; i++) {
            String xPath = tagXPaths[i];
            targetsTag.append("<path xpath=\"").append(xPath).append("\"");
            if (java.util.Arrays.binarySearch(targets, xPath) >= 0) {
                targetsTag.append(" selected=\"yes\">");
            } else {
                targetsTag.append(" selected=\"no\">");
            }
            targetsTag.append(tagDisplayNames[i]).append("</path>\n");
        }
        targetsTag.append("</targets>\n");

        tagXPaths = DMSTag.valueOf("xpath", "input", category, conf);
        tagDisplayNames = DMSTag.valueOf("displayname/" + conf.LANG, "input", category, conf);
        //Samarita (selectable operator)
        String[] tagOpers = DMSTag.valueOf("oper", "input", category, conf);
        String[] tagType = DMSTag.valueOf("dataType", "input", category, conf);

        Arrays.sort(inputsParameters);
        StringBuffer inputsTag = new StringBuffer("<inputs>\n");
        for (int i = 0; i < inputs.length; i++) {
            String inputId = inputsIds[i];
            String parameter = (Arrays.binarySearch(inputsParameters, inputId) >= 0) ? "yes" : "no";
            inputsTag.append("<input id=\"" + inputId + "\" parameter=\"" + parameter + "\">\n");

            for (int j = 0; j < tagXPaths.length; j++) {
                String xPath = tagXPaths[j];
                inputsTag.append("<path xpath=\"").append(xPath).append("\"");
                inputsTag.append(" dataType=\"").append(tagType[j] + "\"");
                inputsTag.append(" oper=\"").append(tagOpers[j]).append("\"");

                if (xPath.equals(inputs[i])) {
                    inputsTag.append(" selected=\"yes\">");
                } else {
                    inputsTag.append(" selected=\"no\">");
                }
                inputsTag.append(tagDisplayNames[j]).append("</path>\n");
            }
            /*
             * check inputsOpers[i] =null
             */
            if ((inputsOpers.length - 1) >= i) {
                inputsTag.append("<oper>").append(inputsOpers[i]);
                inputsTag.append("</oper>\n");
            } else {
                System.out.println("Null");
            }

            inputsTag.append("<value>").append(inputsValues[i]);
            inputsTag.append("</value>\n</input>\n");
        }
        inputsTag.append("</inputs>\n");
        Arrays.sort(outputs);

        StringBuffer outputsTag = new StringBuffer("<outputs>\n");
        String querySource = getSource(params, conf, dataCol);

        StringBuffer infoTag = new StringBuffer("<info>\n<source>\n<![CDATA[" + querySource + "]]>\n</source>\n");

        if (outputs.length != 0) {
            Map<String, String> listing = new HashMap();
            for (int i = 0; i < tagXPaths.length; i++) {
                listing.put(tagXPaths[i], tagDisplayNames[i]);
            }

            for (int i = 0; i < outputs.length; i++) {
                String xPath = tagXPaths[i];
                outputsTag.append("<path xpath=\"").append(outputs[i]).append("\"");
                outputsTag.append(" selected=\"yes\">");
                outputsTag.append(listing.get(outputs[i])).append("</path>\n");
            }
        } else {

            String[] outputsTitle = UtilsXPaths.getOutpuTitleForSimpleSearch(category);
            for (int i = 0; i < outputsTitle.length; i++) {
                outputsTag.append("<path xpath=\"").append("").append("\"");
                outputsTag.append(" selected=\"yes\">");
                outputsTag.append(outputsTitle[i]).append("</path>\n");
            }
        }

        outputsTag.append("</outputs>\n");
        outputsTag.append("<adminParts>\n");
        String[] adminPartsTitle = DMSTag.valueOf("tagname", "adminOutput", category, conf);
        for (String title : adminPartsTitle) {

            outputsTag.append("<title>").append(title).append("</title>\n");
        }
        outputsTag.append("</adminParts>");

        infoTag.append("<name>" + mnemonicName + "</name>\n");
        infoTag.append("<category>" + category + "</category>\n");
        infoTag.append("<lang>" + lang + "</lang>\n");
        infoTag.append("<status>" + status + "</status>\n");
        infoTag.append("<operator>" + operator + "</operator>\n");
        infoTag.append("</info>\n");

        int queryId = (qId.length() > 0) ? Integer.parseInt(qId) : 0;
        StringBuffer ret = buildXML(queryId, category, infoTag, targetsTag, inputsTag, outputsTag);
        return ret.toString();
    }

    public static String getXML(DMSXQuery query, DMSConfig conf) throws DMSException {
        String category = query.getInfo("category");

        String status = query.getInfo("status");
        String lang = query.getInfo("lang");

        int[] qInputs = query.getInputs();

        String[] tagXPaths = DMSTag.valueOf("xpath", "target", category, conf);
        String[] tagDisplayNames = DMSTag.valueOf("displayname/" + conf.LANG, "target", category, conf);

        StringBuffer targetsTag = new StringBuffer("<targets>\n");
        for (int i = 0; i < tagXPaths.length; i++) {
            targetsTag.append("<path xpath=\"").append(tagXPaths[i]).append("\"");;
            if (query.hasTarget(tagXPaths[i])) {
                targetsTag.append(" selected=\"yes\">");
            } else {
                targetsTag.append(" selected=\"no\">");
            }
            targetsTag.append(tagDisplayNames[i]).append("</path>\n");
        }
        targetsTag.append("</targets>\n");

        tagXPaths = DMSTag.valueOf("xpath", "input", category, conf);
        tagDisplayNames = DMSTag.valueOf("displayname/" + conf.LANG, "input", category, conf);
        String[] tagOpers = DMSTag.valueOf("oper", "input", category, conf);

        //Samarita (selectable operator)
        String[] tagType = DMSTag.valueOf("dataType", "input", category, conf);

        StringBuffer inputsTag = new StringBuffer("<inputs>\n");
        for (int i = 0; i < qInputs.length; i++) {
            int inId = qInputs[i];
            String parameter = query.isParameter(inId) ? "yes" : "no";
            inputsTag.append("<input id=\"" + inId + "\" parameter=\"" + parameter + "\">\n");
            for (int p = 0; p < tagXPaths.length; p++) {
                inputsTag.append("<path xpath=\"").append(tagXPaths[p]).append("\"");
                //Samarita
                inputsTag.append(" dataType=\"").append(tagType[p] + "\"");
                inputsTag.append(" oper=\"").append(tagOpers[i]).append("\"");

                if (tagXPaths[p].equals(query.getFromInput(inId, "path"))) {
                    inputsTag.append(" selected=\"yes\">");
                } else {
                    inputsTag.append(" selected=\"no\">");
                }
                inputsTag.append(tagDisplayNames[p]).append("</path>\n");
            }

            //Samarita (selectable operator)
            inputsTag.append("<oper>").append(query.getFromInput(inId, "oper"));
            inputsTag.append("</oper>\n");

            inputsTag.append("<value>").append(query.getFromInput(inId, "value"));
            inputsTag.append("</value>\n</input>\n");
        }
        inputsTag.append("</inputs>\n");

        StringBuffer outputsTag = new StringBuffer("<outputs>\n");
        for (int i = 0; i < tagXPaths.length; i++) {
            outputsTag.append("<path xpath=\"").append(tagXPaths[i]).append("\"");
            if (query.hasOutput(tagXPaths[i])) {
                outputsTag.append(" selected=\"yes\">");
            } else {
                outputsTag.append(" selected=\"no\">");
            }
            outputsTag.append(tagDisplayNames[i]).append("</path>\n");
        }
        outputsTag.append("</outputs>\n");

        StringBuffer infoTag = new StringBuffer("<info>\n");
        infoTag.append("<name>" + query.getInfo("name") + "</name>\n");
        infoTag.append("<category>" + category + "</category>\n");
        infoTag.append("<lang>" + lang + "</lang>\n");
        infoTag.append("<status>" + status + "</status>\n");
        infoTag.append("<operator>" + query.getInfo("operator") + "</operator>\n");
        infoTag.append("</info>\n");

        StringBuffer ret = buildXML(query.getId(), category, infoTag, targetsTag, inputsTag, outputsTag);
        return ret.toString();
    }

    public static String xmlForGet(String category, String lang, String status, DMSConfig conf) throws DMSException {
        StringBuffer infoTag = new StringBuffer("<info>\n");
        infoTag.append("<operator>and</operator>\n");	//default value
        infoTag.append("<category>" + category + "</category>\n");
        infoTag.append("<lang>" + lang + "</lang>\n");
        infoTag.append("<status>" + status + "</status>\n");
        infoTag.append("<name></name>\n");	//default name
        infoTag.append("</info>\n");

        String[] tagXPaths = DMSTag.valueOf("xpath", "target", category, conf);
        String[] tagDisplayNames = DMSTag.valueOf("displayname/" + conf.LANG, "target", category, conf);

        StringBuffer targetsTag = new StringBuffer("<targets>\n");
        for (int i = 0; i < tagXPaths.length; i++) {
            targetsTag.append("<path xpath=\"").append(tagXPaths[i]).append("\"");;
            targetsTag.append(" selected=\"yes\">");
            targetsTag.append(tagDisplayNames[i]).append("</path>\n");
        }
        targetsTag.append("</targets>\n");

        tagXPaths = DMSTag.valueOf("xpath", "input", category, conf);
        tagDisplayNames = DMSTag.valueOf("displayname/" + conf.LANG, "input", category, conf);
        String[] tagOpers = DMSTag.valueOf("oper", "input", category, conf);

//        System.out.println(category);
        String[] tagType = DMSTag.valueOf("dataType", "input", category, conf);

        StringBuffer xmlTmp = new StringBuffer();
        for (int i = 0; i < tagXPaths.length; i++) {
            if (i == 0) {
                xmlTmp.append("<path xpath=\"").append(tagXPaths[i]).append("\"").append(" dataType=\"").append(tagType[i] + "\"").append(" oper=\"").append(tagOpers[i]).append("\" selected=\"yes\">").append(tagDisplayNames[i]).append("</path>\n");
            } else {

                xmlTmp.append("<path xpath=\"").append(tagXPaths[i]).append("\"").append(" dataType=\"").append(tagType[i] + "\"").append(" oper=\"").append(tagOpers[i]).append("\" selected=\"no\">").append(tagDisplayNames[i]).append("</path>\n");
            }
        }

        StringBuffer inputsTag = new StringBuffer("<inputs>\n");

        //Samarita
//        System.out.println(xmlTmp);
//        inputsTag.append(xmlTmp);
//        inputsTag.append("<input id=\"1\" parameter=\"no\">\n").append("<value/>\n</input>\n");
//        inputsTag.append("<input id=\"2\" parameter=\"no\">\n").append("<value/>\n</input>\n");
        inputsTag.append("<input id=\"1\" parameter=\"no\">\n").append(xmlTmp).append("<value/>\n</input>\n");
        //Konstantina To Get only one criteria at the begining
        //  inputsTag.append("<input id=\"2\" parameter=\"no\">\n").append(xmlTmp).append("<value/>\n</input>\n");
        inputsTag.append("</inputs>\n");
//        System.out.println(inputsTag);

        StringBuffer outputsTag = new StringBuffer("<outputs>\n");
        for (int i = 0; i < tagXPaths.length; i++) {
            outputsTag.append("<path xpath=\"").append(tagXPaths[i]).append("\" selected=\"no\">").append(tagDisplayNames[i]).append("</path>\n");
        }
        outputsTag.append("</outputs>\n");

        StringBuffer ret = buildXML(0, category, infoTag, targetsTag, inputsTag, outputsTag);
        return ret.toString();
    }

    private static StringBuffer buildXML(int qId, String category, StringBuffer infoTag, StringBuffer targetsTag, StringBuffer inputsTag, StringBuffer outputsTag) {
        StringBuffer ret = new StringBuffer("<query id=\"" + qId + "\">\n");
        ret.append(infoTag).append(targetsTag).append(inputsTag).append(outputsTag);
        ret.append("\n</query>\n");
        ret.append("<LeftMenuId>").append("search" + category).append("</LeftMenuId>");
        ret.append("<EntityType>").append(category).append("</EntityType>\n");
        return ret;
    }

    private static String getPredicate(String input, String oper, String value) {

        // gmessarit: perform a case-insensitive match
        if (oper.equals("matches")) {
            return " matches($i" + input + ", \'" + value + "\', \'i\') ";
        }

        // gmessarit: assume operators like: =, !=, etc.
        // for support for '<' or '>', we replace '&lt;' and '&gt;'
        oper = oper.replaceAll("&gt;", ">");
        oper = oper.replaceAll("&lt;", "<");
        if (oper.length() <= 2) {
            return " ($i" + input + oper + "\'" + value + "\') ";
        }

        //TIME-HANDLING (Samarita)
        //   if (oper.equals("ΠΡΙΝ")||oper.equals("ΜΕΤΑ")||oper.equals("ΣΥΜΠΙΠΤΕΙ")||oper.equals("ΠΕΡΙΕΧΕΤΑΙ")||oper.equals("ΤΟΜΗ")||oper.equals("ΤΟΜΗ ΔΕΞΙΑ")||oper.equals("ΤΟΜΗ ΑΡΙΣΤΕΡΑ")) {
        if (oper.startsWith("time")) {
            SISdate sisTime = new SISdate(value);

            String from = Integer.toString(sisTime.getFrom());
            String to = Integer.toString(sisTime.getTo());

            String newOper = "";
            if (oper.endsWith("1")) {
                //ΠΡΙΝ
                newOper = " ($i" + input + "/@y/number() <" + from + ") ";
            } else if (oper.endsWith("2")) {
                //ΜΕΤΑ
                newOper = " ($i" + input + "/@x/number() >" + to + ") ";
            } else if (oper.endsWith("3")) {
                //ΣΥΜΠΙΠΤΕΙ
                newOper = " ($i" + input + "/@x/number() =" + from + " and $i" + input + "/@y/number() =" + to + ") ";
            } else if (oper.endsWith("4")) {
                //ΠΕΡΙΕΧΕΤΑΙ
                newOper = " ($i" + input + "/@x/number() >" + from + " and $i" + input + "/@y/number() <" + to + ") ";
            } else if (oper.endsWith("5")) {
                //ΠΕΡΙΕΧΕΙ
                newOper = " ($i" + input + "/@x/number() <" + from + " and $i" + input + "/@y/number() >" + to + ") ";
            } else if (oper.endsWith("6")) {
                //ΤΟΜΗ
                newOper = " ($i" + input + "/@x/number() <" + to + " and $i" + input + "/@y/number() >" + from + ") ";
            } else if (oper.endsWith("7")) {
                //ΤΟΜΗ ΔΕΞΙΑ
                newOper = " ($i" + input + "/@x/number() >=" + from + " and $i" + input + "/@x/number() <=" + to + " and $i" + input + "/@y/number() >" + to + ") ";
            } else if (oper.endsWith("8")) {
                //ΤΟΜΗ ΑΡΙΣΤΕΡΑ
                newOper = " ($i" + input + "/@x/number() <" + from + " and $i" + input + "/@y/number() >=" + from + " and $i" + input + "/@y/number() <=" + to + ") ";
            }
            return newOper;
//            System.out.println("FROM="+from);
//            System.out.println("TO="+to);

        } else {

            // gmessarit: assume operators like: like, contains, not, etc.
            return " " + oper + "($i" + input + ", \'" + value + "\') ";
        }
    }

    public static boolean userHasAction(String action, String username, DMSConfig conf) {
        //from the database
        DMSUser user;
        boolean ret = false;
        try {
            user = new DMSUser(username, conf);
            ret = user.hasAction(action);
        } catch (DMSException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
