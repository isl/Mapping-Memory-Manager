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

import isl.FIMS.utils.Vocabulary;
import isl.FIMS.utils.Messages;
import isl.FIMS.utils.entity.XMLEntity;
import isl.FIMS.utils.entity.Config;
import isl.FIMS.utils.UtilsXPaths;
import java.io.IOException;
import java.io.PrintWriter;
import isl.dbms.DBFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import isl.dms.DMSConfig;
import isl.dms.DMSException;
import isl.dms.xml.XMLTransform;
import java.util.ArrayList;

//Handles the 'insert', 'edit', 'delete', 'list' of voc terms
public class AdminVoc extends AdminBasicServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, DMSException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        this.setStatus("insert");
        this.initVars(request);
        String dependencies = "";
        String mode = request.getParameter("mode");
        String menuId = request.getParameter("menuId");
        if (mode == null) {
            mode = "";
        }

        //sto antistoixo ths glwssas vocabulary
        String type = "Vocabulary/" + this.lang;
        //the (vocabulary) file we want is the 'file' parameter.
        String file = request.getParameter("file");
        String[] vocFileName = {""};
        Config conf = new Config(type);

        String userOrg = this.getUserGroup();

        String adminAction = this.action;
        StringBuffer xml = new StringBuffer(this.xmlStart(this.topmenu, this.username, this.pageTitle, this.lang, "", request));
        xml.append("<AdminMode>").append(mode).append("</AdminMode>\n");
        xml.append("<ListOf>").append(type).append("</ListOf>\n");
        xml.append("<EntityType>").append(menuId).append("</EntityType>\n");

        if (this.action.equals("list_vocs") || this.action.equals("list_vocs_trans")) {

            String allVocsXML = Vocabulary.listAllAsXML(this.conf);
            xml.append("<VocsList>").append(allVocsXML).append("</VocsList>\n");

            if (this.action.equals("list_vocs")) {
                adminAction = "list";
            } else {
                adminAction = "list_trans";
            }

            conf.DISPLAY_XSL = conf.LIST_VOCS_XSL;
        } else {
            //set a conf for vocabularies [this is why we append 'Vocabulary/']
            DMSConfig vocConf = new DMSConfig(this.DBURI, this.systemDbCollection + "Vocabulary/", this.DBuser, this.DBpassword);

            Vocabulary voc = new Vocabulary(file, this.lang, vocConf);
            DBFile dbf = new DBFile(this.DBURI, this.systemDbCollection + "DMSFILES/", "Vocabularies.xml", this.DBuser, this.DBpassword);
            vocFileName = dbf.queryString("//Vocabularies/*/*[file='" + file + "']/displayname/" + this.lang + "/text()");

            //--------------------actions for vocabulary--------------------
            if (this.action.equals("list")) {
                adminAction = "list";
                String[] terms = voc.terms();
                for (int i = 0; i < terms.length; i++) {
                    xml.append(terms[i]).append("\n");
                }
                conf.DISPLAY_XSL = conf.VOC_TERMS_XSL;

            } else if (this.action.equals("search")) {
                String input = request.getParameter("inputvalue");
                String[] terms = voc.terms();
                boolean foundTerm = false;
                for (int i = 0; i < terms.length; i++) {
                    if (terms[i].contains(input)) {
                        xml.append(terms[i]).append("\n");
                        foundTerm = true;
                    }
                }
                if (foundTerm) {
                    conf.DISPLAY_XSL = conf.VOC_TERMS_XSL;
                } else {
                    this.displayMsg = "DenBrethikanArxeia" + Messages.NL;
                }
            } else if (this.action.equals("toinsert")) {
                adminAction = "insert"; //default action is 'insert'
                String[] terms = voc.terms();
                for (int i = 0; i < terms.length; i++) {
                    xml.append(terms[i]).append("\n");
                }
                conf.DISPLAY_XSL = conf.VOC_TERMS_XSL;

            } else if (this.action.equals("toedit")) {
                adminAction = "edit";
                int id = Integer.parseInt(request.getParameter("id"));

                //check term dependancies...
                ArrayList<String> dependants = voc.findDependants(id, this.entityTypes);
                if (!dependants.isEmpty()) {
                    this.displayMsg += Messages.CANNOT_EDIT + Messages.NL;
                    this.displayMsg += Messages.HAS_DEPENDANTS;
                    StringBuffer resultsTag = new StringBuffer("<results>\n");
                    StringBuffer outputsTag = new StringBuffer("<outputs>\n");
                    outputsTag.append("<path>").append("Name").append("</path>\n");
                    outputsTag.append("<path>").append("URI_ID").append("</path>\n");
                    outputsTag.append("</outputs>\n");
                    for (int i = 0; i < dependants.size(); i++) {
                        resultsTag.append("<result>\n");
                        String tmp = dependants.get(i).split(".xml")[0];
                        String sps_id = tmp.replaceAll("\\D+", "");
                        String sps_type = tmp.split(sps_id)[0];
                        String xpath = UtilsXPaths.getSearchXpathAtName(sps_type) + "/text()";
                        XMLEntity xmlD = new XMLEntity(this.DBURI, this.systemDbCollection + sps_type, this.DBuser, this.DBpassword, sps_type, sps_type + sps_id);
                        String name = xmlD.queryString(xpath)[0];
                        String uri_id = xmlD.queryString("fn:tokenize(//admin/uri_id/text(),'" + this.URI_Reference_Path + "')[last()]")[0];
                        resultsTag.append("<name>").append(name).append("</name>");
                        resultsTag.append("<uri_id>").append(uri_id).append("</uri_id>");
                        resultsTag.append("<id>").append(sps_type + sps_id).append("</id>");
                        resultsTag.append("<type>").append(sps_type).append("</type>");

                        resultsTag.append("</result>\n");

                    }
                    resultsTag.append("</results>\n");
                    xml.append("<query>\n");
                    xml.append(outputsTag);
                    xml.append(resultsTag);
                    xml.append("</query>\n");
                    xml.append("<URI_Reference_Path>").append(this.URI_Reference_Path).append("</URI_Reference_Path>\n");

                } else {
                    xml.append("<TermValue>").append(voc.termValueOf(id)).append("</TermValue>\n");
                    xml.append("<TermId>").append(id).append("</TermId>\n");

                    String[] terms = voc.terms();
                    for (int i = 0; i < terms.length; i++) {
                        xml.append(terms[i]).append("\n");
                    }

                    conf.DISPLAY_XSL = conf.VOC_TERMS_XSL;
                }
            } else if (this.action.equals("insert") || this.action.equals("edit")) {
                String newTerm = request.getParameter("newterm");

                if (this.displayMsg.length() == 0) {
                    if (this.action.equals("insert")) {
                        voc.addTerm(newTerm);

                    } else if (this.action.equals("edit")) {
                        int id = Integer.parseInt(request.getParameter("id"));
                        voc.updateTerm(id, newTerm);
                    }
                    String[] terms = voc.terms();
                    for (int i = 0; i < terms.length; i++) {
                        xml.append(terms[i]).append("\n");
                    }
                    response.sendRedirect("AdminVoc?action=list&mode=" + mode + "&file=" + file + "&menuId=" + menuId);
                    conf.DISPLAY_XSL = conf.VOC_TERMS_XSL;
                }
            } else if (this.action.equals("delete")) {
                int id = Integer.parseInt(request.getParameter("id"));

                //check term dependancies...
                ArrayList<String> dependants = voc.findDependants(id, this.entityTypes);
                if (!dependants.isEmpty()) {
                    this.displayMsg += Messages.HAS_DEPENDANTS + Messages.NL;
                    StringBuffer resultsTag = new StringBuffer("<results>\n");
                    StringBuffer outputsTag = new StringBuffer("<outputs>\n");
                    outputsTag.append("<path>").append("Name").append("</path>\n");
                    outputsTag.append("<path>").append("URI_ID").append("</path>\n");
                    outputsTag.append("</outputs>\n");
                    for (int i = 0; i < dependants.size(); i++) {
                        resultsTag.append("<result>\n");
                        String tmp = dependants.get(i).split(".xml")[0];
                        String sps_id = tmp.replaceAll("\\D+", "");
                        String sps_type = tmp.split(sps_id)[0];
                        String xpath = UtilsXPaths.getSearchXpathAtName(sps_type) + "/text()";
                        XMLEntity xmlD = new XMLEntity(this.DBURI, this.systemDbCollection + sps_type, this.DBuser, this.DBpassword, sps_type, sps_type + sps_id);
                        String[] names = xmlD.queryString(xpath);
                        String name = "";
                        if (names.length > 0) {
                            name = names[0];
                        }
                        String uri_id = xmlD.queryString("fn:tokenize(//admin/uri_id/text(),'" + this.URI_Reference_Path + "')[last()]")[0];
                        resultsTag.append("<name>").append(name).append("</name>");
                        resultsTag.append("<uri_id>").append(uri_id).append("</uri_id>");
                        resultsTag.append("<id>").append(sps_type + sps_id).append("</id>");
                        resultsTag.append("<type>").append(sps_type).append("</type>");

                        resultsTag.append("</result>\n");

                    }
                    resultsTag.append("</results>\n");
                    xml.append("<query>\n");
                    xml.append(outputsTag);
                    xml.append(resultsTag);
                    xml.append("</query>\n");
                    xml.append("<URI_Reference_Path>").append(this.URI_Reference_Path).append("</URI_Reference_Path>\n");

                }

                //check translation dependancies...
                if (voc.hasTranslationDependantsOf(id, this.lang)) {
                    this.displayMsg += Messages.HAS_TRANS_DEPENDANTS;
                }


                if (this.displayMsg.length() != 0) {
                    this.displayMsg = Messages.CANNOT_DELETE + Messages.NL + this.displayMsg;
                } else {
                    voc.removeTerm(id);
                    response.sendRedirect("AdminVoc?action=list&mode=" + mode + "&file=" + file + "&menuId=" + menuId);
                    return;
                }

            } //--------------------actions for vocabulary translation--------------------
            else if (this.action.endsWith("_trans")) {
                String trId = request.getParameter("id");
                if (trId == null || trId.length() == 0) {
                    trId = "0";
                }
                int id = Integer.parseInt(trId);

                String[] langs = this.systemLangs;

                for (int i = 0; i < langs.length; i++) {
                    xml.append("<TransLang>").append(langs[i]).append("</TransLang>\n");
                }
                if (this.action.equals("list_trans")) {
                    adminAction = "list_trans"; //default action is 'list_trans'

                    String[] terms = voc.getTermsTranslationVocValues(langs);
                    for (int i = 0; i < terms.length; i++) {
                        xml.append(terms[i]).append("\n");
                    }
                    conf.DISPLAY_XSL = conf.VOC_TERMS_TRANS_XSL;
                } else if (this.action.equals("search_trans")) {
                    String input = request.getParameter("inputvalue");
                    String[] terms = voc.getTermsTranslationVocValues(langs);
                    boolean foundTerm = false;
                    for (int i = 0; i < terms.length; i++) {
                        if (terms[i].contains(input)) {
                            xml.append(terms[i]).append("\n");
                            foundTerm = true;
                        }
                    }
                    if (foundTerm) {
                        conf.DISPLAY_XSL = conf.VOC_TERMS_TRANS_XSL;
                    } else {
                        this.displayMsg = "DenBrethikanArxeia" + Messages.NL;
                    }
                }
                if (this.action.equals("toinsert_trans")) {
                    adminAction = "insert_trans"; //default action is 'list_trans'

                    String[] terms = voc.getTermsTranslationVocValues(langs);
                    for (int i = 0; i < terms.length; i++) {
                        xml.append(terms[i]).append("\n");
                    }

                    conf.DISPLAY_XSL = conf.VOC_TERMS_TRANS_XSL;
                } else if (this.action.equals("toedit_trans")) {
                    adminAction = "edit_trans";

                    xml.append("<TransId>").append(id).append("</TransId>\n");

                    String[] terms = voc.getTermsTranslationVocValues(langs);
                    for (int i = 0; i < terms.length; i++) {
                        xml.append(terms[i]).append("\n");
                    }

                    conf.DISPLAY_XSL = conf.VOC_TERMS_TRANS_XSL;
                } else if (this.action.equals("insert_trans") || this.action.equals("edit_trans")) {
                    int checkEmptyFields = 0;
                    int[] newTrans = new int[langs.length];
                    for (int i = 0; i < langs.length; i++) {
                        int trans_id = Integer.parseInt(request.getParameter("trans_" + langs[i]));
                        newTrans[i] = trans_id;
                        if (trans_id == 0) {
                            checkEmptyFields++;
                        }
                    }

                    if (this.displayMsg.length() == 0) {
                        if (this.action.equals("insert_trans")) {
                            if (checkEmptyFields != langs.length) {
                                int transId = voc.addTranslation(langs);
                                for (int i = 0; i < langs.length; i++) {
                                    voc.setTranslation(transId, langs[i], newTrans[i]);
                                }
                            }
                        } else if (this.action.equals("edit_trans")) {
                            for (int i = 0; i < langs.length; i++) {
                                voc.setTranslation(id, langs[i], newTrans[i]);
                            }
                        }

                        String[] terms = voc.getTermsTranslationVocValues(langs);
                        for (int i = 0; i < terms.length; i++) {
                            xml.append(terms[i]).append("\n");
                        }

                        id = 0;
                        response.sendRedirect("AdminVoc?action=list_trans&mode=" + mode + "&file=" + file + "&menuId=" + menuId);
                        conf.DISPLAY_XSL = conf.VOC_TERMS_TRANS_XSL;
                    }
                } else if (this.action.equals("delete_trans")) {
                    voc.removeTranslation(id);
                    response.sendRedirect("AdminVoc?action=list_trans&mode=" + mode + "&file=" + file + "&menuId=" + menuId);
                    return;
                }

                for (int i = 0; i < langs.length; i++) {
                    Vocabulary v = new Vocabulary(file, langs[i], vocConf);
                    xml.append("<VocTerms>\n");
                    xml.append("<Lang>").append(langs[i]).append("</Lang>\n");
                    xml.append("<TermSelected>").append(voc.getTermTranslationValueOf(id, langs[i])).append("</TermSelected>\n");
                    String[] vTerms = v.terms();

                    for (int t = 0; t < vTerms.length; t++) {
                        xml.append(vTerms[t]).append("\n");
                    }
                    xml.append("</VocTerms>\n");
                }
            }

        }

        xml.append("<AdminAction>").append(adminAction).append("</AdminAction>\n");
        xml.append("<VocFile>").append(file).append("</VocFile>\n");
        xml.append("<VocFileName>").append(vocFileName[0]).append("</VocFileName>\n");
        xml.append("<Display>").append(this.displayMsg).append("</Display>\n");
        xml.append("<Dependencies>").append(dependencies).append("</Dependencies>\n");
        xml.append("<DisplayError>").append(this.error).append("</DisplayError>\n");
        xml.append(this.xmlEnd());
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
