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
package isl.FIMS.utils.entity;

import isl.FIMS.utils.ApplicationConfig;

public class Config {

    public String ENTITY_TYPE;
    //---------- ENTITY ----------//
    public String ENTITY_XSL_PATH;
    public String ENTITY_XSL;
    public String LIST_ENTITY_XSL;
    public String VOCABULARY_ENTITY_XSL;
    //---------- ADMIN ----------//
    //Entity-AP
    public String ADMIN_LIST_ENTITY_XSL;
    public String LIST_TO_TRANSLATE_XSL;
    public String LIST_PENDING_XSL;
    public String LIST_PUBLISHED_XSL;
    public String DOC_RIGHTS_XSL;
    public String INSERT_DOC_XSL;
    public String REJECT_PUBLISH_DOC_XSL;
    //User-Org
    public String LIST_XSL;
    public String USER_XSL;
    public String ORG_XSL;
    //Vocabulary
    public String LIST_VOCS_XSL;
    public String VOC_TERMS_XSL;
    public String VOC_TERMS_TRANS_XSL;
    //RelationalDB
    //---------- VARIOUS ----------//
    public String LIST_VERSIONS;
//    mdaskal
    public String RESTORE_XSL;
//    
    public String DISPLAY_XSL;
    public String POPUP_DISPLAY_XSL;
    public String DISPLAY_WIN_XSL;
    public String TIMED_DISPLAY_WIN_XSL;
    public String WELCOME_XSL;
    public String FIRSTE_PAGE_XSL;
    //change pass for editor
    public String EDITOR_CHANGE_PASS;
    public String IMPORT_XML;
    public String IMPORT_Vocabulary;
    public String Audio_XSL;
    public String xml2rdf;
    public String forgotPass = "";
    public String signup = "";
    public String comments_versions = "";

    public Config(String type) {
        this.ENTITY_TYPE = type;
        this.ENTITY_XSL_PATH = ApplicationConfig.SYSTEM_ROOT + "formating/xsl/entity/";
        this.ENTITY_XSL = ENTITY_XSL_PATH + "entity.xsl";
        this.LIST_ENTITY_XSL = ENTITY_XSL_PATH + "list_entity.xsl";
        //this.VIEW_ENTITY_XSL	= ENTITY_XSL_PATH + "view_entity.xsl";
        this.VOCABULARY_ENTITY_XSL = ENTITY_XSL_PATH + "voc_entity.xsl";

        //Samarita

        //----- Entity-AP -----
        this.ADMIN_LIST_ENTITY_XSL = ENTITY_XSL_PATH + "../admin/entity/admin_list_entity.xsl";
        this.LIST_TO_TRANSLATE_XSL = ENTITY_XSL_PATH + "../translate/list_to_translate.xsl";
        this.LIST_PENDING_XSL = ENTITY_XSL_PATH + "../admin/entity/list_pending.xsl";
        this.LIST_PUBLISHED_XSL = ENTITY_XSL_PATH + "../admin/entity/list_published.xsl";
        this.DOC_RIGHTS_XSL = ENTITY_XSL_PATH + "../admin/entity/doc_rights.xsl";
        this.INSERT_DOC_XSL = ENTITY_XSL_PATH + "../admin/entity/insert_doc.xsl";
        this.REJECT_PUBLISH_DOC_XSL = ENTITY_XSL_PATH + "../admin/entity/reject_publish.xsl";
        //----- User-Org -----
        this.LIST_XSL = ENTITY_XSL_PATH + "../admin/user_org/list.xsl";
        this.USER_XSL = ENTITY_XSL_PATH + "../admin/user_org/user.xsl";
        this.ORG_XSL = ENTITY_XSL_PATH + "../admin/user_org/org.xsl";
        //----- Vocabulary -----
        this.LIST_VOCS_XSL = ENTITY_XSL_PATH + "../admin/vocabulary/list_vocs.xsl";
        this.VOC_TERMS_XSL = ENTITY_XSL_PATH + "../admin/vocabulary/voc_terms.xsl";
        this.VOC_TERMS_TRANS_XSL = ENTITY_XSL_PATH + "../admin/vocabulary/voc_terms_trans.xsl";

        this.RESTORE_XSL = ApplicationConfig.SYSTEM_ROOT + "formating/xsl/storage/restore.xsl";
        this.POPUP_DISPLAY_XSL = ApplicationConfig.SYSTEM_ROOT + "formating/xsl/ui/display.xsl";
        this.DISPLAY_XSL = ApplicationConfig.SYSTEM_ROOT + "formating/xsl/ui/WelcomePage.xsl";
        this.DISPLAY_WIN_XSL = ApplicationConfig.SYSTEM_ROOT + "formating/xsl/ui/display_win.xsl";
        this.TIMED_DISPLAY_WIN_XSL = ApplicationConfig.SYSTEM_ROOT + "formating/xsl/ui/timed_display_win.xsl";

        this.WELCOME_XSL = ApplicationConfig.SYSTEM_ROOT + "formating/xsl/ui/WelcomePage.xsl";
        this.FIRSTE_PAGE_XSL = ApplicationConfig.SYSTEM_ROOT + "formating/xsl/ui/firstpage.xsl";
        this.EDITOR_CHANGE_PASS = ApplicationConfig.SYSTEM_ROOT + "formating/xsl/ui/changePass.xsl";
        this.LIST_VERSIONS = ApplicationConfig.SYSTEM_ROOT + "formating/xsl/version/list_versions.xsl";
        this.IMPORT_XML = ApplicationConfig.SYSTEM_ROOT + "formating/xsl/import/ImportXML.xsl";
        this.IMPORT_Vocabulary = ApplicationConfig.SYSTEM_ROOT + "formating/xsl/import/ImportVocabulary.xsl";
        this.Audio_XSL = ApplicationConfig.SYSTEM_ROOT + "formating/xsl/ui/audioplayer.xsl";
        this.xml2rdf = ApplicationConfig.SYSTEM_ROOT + "formating/xsl/xml2rdf/xml2rdf.xsl";
        this.forgotPass = ApplicationConfig.SYSTEM_ROOT + "formating/xsl/ui/forgotPass.xsl";
        this.signup = ApplicationConfig.SYSTEM_ROOT + "formating/xsl/ui/signup.xsl";
        this.comments_versions = ApplicationConfig.SYSTEM_ROOT + "formating/xsl/version/comment_versions.xsl";
    }
}