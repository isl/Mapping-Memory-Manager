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

import java.util.Hashtable;

import isl.dbms.DBCollection;
import isl.dbms.DBFile;
import isl.dbms.DBMSException;
import isl.dms.DMSConfig;
import isl.dms.DMSException;
import isl.dms.file.DMSFile;
import java.util.ArrayList;

public class Vocabulary {

    private String vocName;
    private String vocLang;
    private DBFile vocFile;
    private DMSFile transVocFile;
    private DMSConfig transConf;

    public Vocabulary(String vocabulary, String lang, DMSConfig conf) {
        this.vocName = vocabulary;
        this.vocLang = lang;

        this.vocFile = new DBFile(conf.DB, conf.COLLECTION + lang, vocabulary, conf.DB_USERNAME, conf.DB_PASSWORD);

        this.transConf = new DMSConfig(conf.DB, conf.COLLECTION + "trans/", conf.DB_USERNAME, conf.DB_PASSWORD);
        this.transVocFile = new DMSFile(this.vocName, transConf);
    }

    public static String listAllAsXML(DMSConfig conf) {
        DBFile dbf = new DBFile(conf.DB, conf.COLLECTION, "Vocabularies.xml", conf.DB_USERNAME, conf.DB_PASSWORD);
        String[] r = dbf.queryString("/Vocabularies");

        if (r.length == 0) {
            return null;
        }

        return r[0];
    }

    /**
     * Returns the value of a term by the term id.
     *
     * @param termId the id of the term.
     * @return the value of the term with the specified termId or
     * <code>null</code> if there is no value for the specified id.
     */
    public String termValueOf(int termId) {
        String[] t = this.vocFile.queryString("//Όρος[@id='" + termId + "']/text()");

        if (t.length == 0) {
            return null;
        }

        return t[0];
    }

    /**
     * Gets the 'translated' id of a term from a lang to another lang.
     *
     * @param termId the termId of the term.
     * @param fromLang the [from] lang of the term.
     * @param toLang the [to] lang of which the value we want.
     * @return the 'translated' id value of a term in a particular lang.
     *
     * @throws DMSException with expected error codes.
     */
    public int translateTerm(int termId, String fromLang, String toLang) throws DMSException {
        return this.getTermTranslation(termId, fromLang, toLang);
    }

    public int translateTermFrom(int termId, String lang) throws DMSException {
        return this.translateTerm(termId, lang, this.vocLang);
    }

    public int translateTermTo(int termId, String lang) throws DMSException {
        return this.translateTerm(termId, this.vocLang, lang);
    }

    /**
     * Returns a list of all terms in this
     * <code>Vocabulary</code>.<br> <br> The returned list contains the whole
     * 'tag' (xml formated), not only the value. <br><br> Example:<br>
     * <b>&lt;Όρος id="..."&gt;...&lt;/Όρος&gt;</b>
     *
     * @return a list of all terms in this <code>Vocabulary</code>.
     */
    public String[] terms() {
        String path = this.getSystemDbCollection() + "Vocabulary/" + this.vocLang + "/" + this.vocName;
        String query = "for $b in doc('" + path + "')//Όρος order by $b/text() collation '?lang=el-gr' return $b ";
        return this.vocFile.queryString(query);
    }

    /**
     * Returns a list of all term values in this
     * <code>Vocabulary</code>.
     *
     * @return a list of all terms in this <code>Vocabulary</code>.
     */
    public String[] termValues() {
        return this.vocFile.queryString("//Όρος/text()");
    }

    /**
     * Returns a list of all term ids in this
     * <code>Vocabulary</code>.
     *
     * @return a list of all term ids in this <code>Vocabulary</code>.
     */
    public String[] termIds() {
        return this.vocFile.queryString("//Όρος/@id/string()");
    }

    /**
     * Adds a term to this
     * <code>Vocabulary</code>. The term is assigned with an automatic generated
     * id.
     *
     * @param term the value of the term.
     * @return the id of the term.
     */
    public int addTerm(String term) {
        int id = this.newId();
        this.addTerm(id, term);
        return id;
    }

    /**
     * Adds a term with a particular id to this
     * <code>Vocabulary</code>.
     *
     * @param id the id of the term to be added.
     * @param term the value of the term.
     */
    private void addTerm(int id, String term) {
        String newTermQ = "<Όρος id=\"" + id + "\">" + term + "</Όρος>";
        this.vocFile.xAppend("/Όροι", newTermQ);
    }

    /**
     * Updates a term (with a particular id) of this
     * <code>Vocabulary</code>.
     *
     * @param id the id of the term to be updated.
     * @param term the (new) value of the term.
     */
    public void updateTerm(int id, String term) {
        String termQ = "//Όρος[@id='" + id + "']";
        this.vocFile.xUpdate(termQ, term);
    }

    /**
     * Removes a term (with a particular id) from this
     * <code>Vocabulary</code>.
     *
     * @param id the id of the term to be removed.
     */
    public void removeTerm(int id) {
        String termQ = "//Όρος[@id='" + id + "']";
        this.vocFile.xRemove(termQ);
    }

    /**
     * Adds an empty translation and returns its id. This id can be later used
     * for setting values for each language in this translation.
     *
     * @param langs an array of <code>String</code> containing the languages we
     * want to add.
     * @return the id of the new [empty] translation.
     * @throws DMSException with expected error codes.
     * @throws DBMSException with expected error codes.
     */
    public int addTranslation(String[] langs) throws DBMSException, DMSException {
        int trId = this.transVocFile.addEntity("translation", "");

        for (int i = 0; i < langs.length; i++) {
            this.transVocFile.addIntoEntity(trId, langs[i], "");
        }

        return trId;
    }

    /**
     * Sets the value of a lang in a translation to a particular termId.
     *
     * @param id the id of the translation.
     * @param lang the lang of which the value will be set.
     * @param termId the termId to which the specified lang will be set.
     * @throws DMSException with expected error codes.
     */
    public void setTranslation(int id, String lang, int termId) throws DMSException {
        try {
            this.transVocFile.setIntoEntity(id, lang, String.valueOf(termId));
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    /**
     * Removes a translation (with a particular id) for this
     * <code>Vocabulary</code>.
     *
     * @param id the id of the term to be removed.
     */
    public void removeTranslation(int id) {
        this.transVocFile.removeEntity("id", String.valueOf(id));
    }

    /**
     * Returns a list of all terms translations for this
     * <code>Vocabulary</code>.<br> <br> The returned list contains the whole
     * translation 'tag' (xml formated), with the value taken from the
     * appropriate vocabulary (for each language). <br><br> Example:<br> <b><code>
     * &lt;translation id="..."&gt;
     * <br>&nbsp&nbsp&nbsp&nbsp
     * 		&lt;gr&gt;value_gr&lt;/gr&gt;
     * <br>&nbsp&nbsp&nbsp&nbsp
     * 		&lt;en&gt;value_en&lt;/en&gt;
     * <br>&nbsp&nbsp&nbsp&nbsp
     * 		&lt;fr&gt;value_fr&lt;/fr&gt;
     * <br>&nbsp&nbsp&nbsp&nbsp
     * 		...other langs...
     * <br>
     * &lt;/translation&gt;
     * </code></b>
     *
     * @return a list of all terms translations (with their appropriate values)
     * for this <code>Vocabulary</code>.
     *
     * @throws DMSException with expected error codes.
     */
    public String[] getTermsTranslationVocValues(String[] langs) throws DMSException {
        String sysVocDbCol = this.getSystemDbCollection() + "Vocabulary/";

        StringBuffer q = new StringBuffer();
        q.append("for $i in document('" + this.transConf.COLLECTION + this.vocName + "')//translation\n");

        for (int i = 0; i < langs.length; i++) {
            String lang = langs[i];
            q.append("let $" + lang + "Id := $i/" + lang + "\n");
            q.append("let $" + lang + " := document('" + sysVocDbCol + lang + "/" + this.vocName + "')//Όρος[@id=$" + lang + "Id]/text()\n");
        }
        q.append("order by $"+this.vocLang+" collation '?lang=el-gr'\n");
        q.append("return\n");
        q.append("<translation id=\"{$i/@id}\">\n");
        for (int i = 0; i < langs.length; i++) {
            String lang = langs[i];
            q.append("<" + lang + " id=\"{$" + lang + "Id}\">{$" + lang + "}</" + lang + ">\n");
        }
        q.append("</translation>\n");
        System.out.println(q.toString());
        return this.transVocFile.queryString(q.toString());
    }

    /**
     * Returns a list of all terms translations for this
     * <code>Vocabulary</code>.<br> <br> The returned list contains the whole
     * translation 'tag' (xml formated), not only the value. <br><br>
     * Example:<br> <b><code>
     * &lt;translation id="..."&gt;
     * <br>&nbsp&nbsp&nbsp&nbsp
     * 		&lt;gr&gt;...&lt;/gr&gt;
     * <br>&nbsp&nbsp&nbsp&nbsp
     * 		&lt;en&gt;...&lt;/en&gt;
     * <br>&nbsp&nbsp&nbsp&nbsp
     * 		&lt;fr&gt;...&lt;/fr&gt;
     * <br>&nbsp&nbsp&nbsp&nbsp
     * 		...other langs...
     * <br>
     * &lt;/translation&gt;
     * </code></b>
     *
     * @return a list of all terms translations for this
     * <code>Vocabulary</code>.
     *
     * @throws DMSException with expected error codes.
     */
    public String[] getTermsTranslations() throws DMSException {
        return this.transVocFile.queryString("//translation");
    }

    /**
     * Returns a term's translation value for this
     * <code>Vocabulary</code>, for a particular translation id and language.
     *
     * @param id the id of the translation.
     * @param lang the lang of the translation.
     * @return a term's translation value for this <code>Vocabulary</code>, or
     * <code>null</code> if there is no such value.
     *
     * @throws DMSException with expected error codes.
     */
    public String getTermTranslationValueOf(int id, String lang) throws DMSException {
        String[] ret = this.transVocFile.queryString("//translation[@id='" + id + "']/" + lang + "/text()");

        if (ret.length == 0) {
            return null;
        }

        return ret[0];
    }

    /**
     * Returns the translation languages for this
     * <code>Vocabulary</code>.
     *
     * @return an array containing the translation languages for this
     * <code>Vocabulary</code>.
     * @throws DMSException with expected error codes.
     */
    public String[] getTermsTranslationsLangs() throws DMSException {
        if (this.transVocFile.exist("//translation[1]/*")) {
            return this.transVocFile.queryString("//translation[1]/*/name()");
        } else {
            return new String[0];
        }
    }

    /**
     * Gets the 'translated' id of a term from a lang to another lang.
     *
     * @param termId the termId of the term.
     * @param fromLang the [from] lang of the term.
     * @param toLang the [to] lang of which the value we want.
     * @return the id value of a term in a particular lang.
     *
     * @throws DMSException with expected error codes.
     */
    public int getTermTranslation(int termId, String fromLang, String toLang) throws DMSException {
        String[] r = this.transVocFile.queryString("//translation[" + fromLang + "/text()='" + termId + "']/" + toLang + "/text()");

        if (r.length == 0) {
            return -1;
        }

        return Integer.parseInt(r[0]);
    }

    /**
     * Checks if this term is used in any translation, in a particular language.
     *
     * @param termId the id of the term we want to check
     * @param lang the language of translation we want to check
     * @return <code>true</code> if the term is used, <code>false</code>
     * otherwise
     * @throws DMSException with expected error codes.
     * @throws DBMSException with expected error codes.
     */
    public boolean hasTranslationDependantsOf(int termId, String lang) throws DBMSException, DMSException {
        String[] res = this.transVocFile.queryString("//translation[" + lang + "='" + termId + "']");

        if (res.length > 0) {
            return true;
        }

        return false;
    }

    /**
     * Generates a new term id for this
     * <code>Vocabulary</code>.
     *
     * @return a new id for this <code>Vocabulary</code>.
     */
    public int newId() {
        int newId;
        DBFile[] maxId = this.vocFile.query("max(//Όρος/@id)");

        if (maxId.length == 0) {
            newId = 1;
        } else {
            newId = (int) Double.parseDouble(maxId[0].getXMLAsString()) + 1;
        }

        return newId;
    }

    /**
     * Checks if this term is used by other docs of some type.
     *
     * @param termId the id of the term we want to check
     * @param types the types of doc we want to check in
     * @return <code>true</code> if the term is used, <code>false</code>
     * otherwise
     */
    public boolean hasTermDependantsOf(int termId, String[] types) {
        String sysDbCol = this.getSystemDbCollection();
        String names = getTags();
        DBCollection dbc = new DBCollection(this.transConf.DB, sysDbCol, this.transConf.DB_USERNAME, this.transConf.DB_PASSWORD);
        String col = sysDbCol.substring(0, sysDbCol.length() - 1);
        String query = "let $c :=  collection('" + col + "')"
                + " for $b in $c//(" + names + ") "
                + "where $b//@sps_id='" + termId + "' and  $b//@sps_vocabulary='" + this.vocName + "'  and $c//admin/lang/text()='" + this.vocLang + "' "
                + "return "
                + "util:document-name($b) ";
        DBFile[] dbFs = dbc.query(query);
        if (dbFs.length > 0) {
            return true;
        }


        return false;
    }

    /**
     * Finds if this term is used by other docs of some type.
     *
     * @param termId the id of the term we want to check
     * @param types the types of doc we want to check in
     * @return a table with the names of the files.
     */
    public ArrayList findDependants(int termId, String[] types) {
        String sysDbCol = this.getSystemDbCollection();
        String names = getTags();
        System.out.println("name--->"+names);
        ArrayList fileNames = new ArrayList();
        DBCollection dbc = new DBCollection(this.transConf.DB, sysDbCol, this.transConf.DB_USERNAME, this.transConf.DB_PASSWORD);
        String col = sysDbCol.substring(0, sysDbCol.length() - 1);
        String query = "let $c :=  collection('" + col + "')"
                + " for $b in $c//(" + names + ") "
                + "where $b//@sps_id='" + termId + "' and  $b//@sps_vocabulary='" + this.vocName + "'  and $c//admin/lang/text()='" + this.vocLang + "' "
                + "return "
                + "util:document-name($b) ";
        System.out.println("query---> "+query);
        DBFile[] dbFs = dbc.query(query);
        for (int k = 0; k < dbFs.length; k++) {
            String name = dbFs[k].toString();
            fileNames.add(name);
        }


        return fileNames;
    }

    public String getTags() {

        String names = "";
        String sysDbCol = this.getSystemDbCollection();

        try {
            DBCollection dbcol = new DBCollection(this.transConf.DB, sysDbCol, this.transConf.DB_USERNAME, this.transConf.DB_PASSWORD);
            String query = "let $col := string('" + sysDbCol + "')"
                    + "return(distinct-values(document(concat($col, 'AP/AP.xml'), concat($col, 'Equipment/Equipment.xml'), concat($col, 'KAP/KAP.xml'),concat($col, 'Person/Person.xml'),"
                    + "concat($col, 'Archive/Archive.xml'),concat($col, 'Material/Material.xml'), concat($col, 'Location/Location.xml'), "
                    + "concat($col, 'Bibliography/Bibliography.xml'), concat($col, 'Event/Event.xml'),"
                    + "concat($col, 'Organization/Organization.xml'), concat($col, 'Part/Part.xml'))"
                    + "//*[@sps_vocabulary='" + this.vocName + "']/name() ) )";

            String q = "let $col := string('" + sysDbCol + "')"
                    + "for $b in document(concat($col, 'LaAndLi/Route.xml'), concat($col, 'LaAndLi/Study.xml'),concat($col, 'LaAndLi/Glossary.xml'), concat($col, 'LaAndLi/InfoText.xml'),concat($col, 'LaAndLi/DigitalPhoto.xml'))"
                    + "return "
                    + "replace($b//node[./vocabulary/text()='" + this.vocName + "']/xpath/text(),'.*/','')";

            DBFile[] tagNames2 = dbcol.query(q);
            DBFile[] tagNames = dbcol.query(query);
            for (int m = 0; m < tagNames.length; m++) {
                names = names + "|" + tagNames[m].toString();
            }

            if (names.startsWith("|")) {
                names = names.substring(1);
            }
            for (int m = 0; m < tagNames2.length; m++) {
                if (!tagNames2[m].toString().equals("")) {
                    names = names + "|" + tagNames2[m].toString();
                }
            }

            if (names.startsWith("|")) {
                names = names.substring(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return names;
    }

    /**
     * Finds the docs of some type, in which a particular term is used.
     *
     * @param termId the id of the term we want to check
     * @param types the types of doc we want to check in
     * @return a <code>Hastable</code> containing a map of the names of the
     * files in which the term is used to their type
     */
    public Hashtable findTermDependantsOf(int termId, String[] types) {
        Hashtable ret = new Hashtable();

        String sysDbCol = this.getSystemDbCollection();

        for (int i = 0; i < types.length; i++) {
            DBCollection dbc = new DBCollection(this.transConf.DB, sysDbCol + types[i], this.transConf.DB_USERNAME, this.transConf.DB_PASSWORD);
            String[] files = dbc.listFiles();
            for (int f = 0; f < files.length; f++) {
                DBFile dbF = dbc.getFile(files[f]);
                DBFile[] dbFs = dbc.query("//*[@sps_vocabulary='" + this.vocName + "' and @sps_id='" + termId + "']");
                if (dbFs.length > 0) {
                    ret.put(dbF.getName(), types[i]);
                }
            }
        }

        return ret;
    }

    private String getSystemDbCollection() {
        //ola ta collections einai se prwto mono epipedo
        //opote PANTA 'duo' pisw einai auto pou 8eloume (systemDbCol)
        //den einai kompso...alla DEN einai la8os
        return this.vocFile.getCollection().getParentCollection().getParentCollection().getName() + "/";
    }
}
