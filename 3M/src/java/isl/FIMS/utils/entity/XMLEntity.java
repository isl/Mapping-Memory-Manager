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

import isl.FIMS.servlet.ApplicationBasicServlet;
import isl.FIMS.utils.Utils;
import isl.FIMS.utils.UtilsQueries;
import isl.FIMS.utils.UtilsXPaths;
import isl.FIMS.utils.Vocabulary;
import isl.dbms.DBCollection;
import isl.dbms.DBFile;
import isl.dbms.DBMSException;
import isl.dms.DMSConfig;
import isl.dms.DMSException;
import isl.dms.file.DMSFile;
import isl.dms.file.DMSTag;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <code>XMLEntity</code> is a container for XML 'Entities'. An
 * <code>XMLEntity</code> represents an 'Entity' of a particular type. The
 * 'type' varies and depends on each application.
 */
//perhaps some functionality should move into 'isl.dms.xml.XMLDocument'
public class XMLEntity extends ApplicationBasicServlet {

    String db;
    String dbUser;
    String dbPassword;
    String type;
    String fileId; //for now the id = fileId = filename
    String id;
    DBCollection col;
    DBFile dbFile;

    /**
     * Constructs a new
     * <code>XMLEntity</code> instance associated with an 'Entity' of a
     * particular type. The type of the
     * <code>XMLEntity</code> is determined by the 'type' argument. <br>
     *
     * @param database the database where the collection (next argument) is in.
     * @param collection the collection where the files, describing the
     * 'Entities' of 'type' (argument), are in.
     * @param user the username to use for authentication to the database or
     * <code>null</code> if the database does not support authentication.
     * @param password the password to use for authentication to the database or
     * <code>null</code> if the database does not support authentication.
     * @param type the type of the 'Entity' we want to have.
     * @param id the system 'id' of the 'Entity'.
     */
    public XMLEntity(String database, String collection, String username, String password, String type, String fileId) {
        this.db = database;
        this.dbUser = username;
        this.dbPassword = password;
        this.type = type;
        this.fileId = fileId;
        if (fileId.equals(type)) {
            this.id = "0";
        } else {
            this.id = fileId.split(type)[1];
        }

        if (!id.equals("0")) {
            collection = UtilsQueries.getpath(database, collection, username, password, type, id);
        }
        this.col = new DBCollection(database, collection, username, password);

        this.dbFile = this.col.getFile(this.fileId + ".xml");
    }

    /**
     * Constructs a new
     * <code>XMLEntity</code> instance associated with an 'Entity' of a
     * particular type. The type of the
     * <code>XMLEntity</code> is determined by the 'type' argument. <br> <b>If
     * this constructor is used then functionality involving traslations might
     * NOT work.</b> The reason is that the 'db', 'dbUser' and 'dbPassword'
     * fields are not filled. If they are filled by hand, after the object is
     * constructed, then it is ok.
     *
     * @param sysCollection the system collection (root collection) where the
     * files, describing the 'Entities' of 'type' (argument), are in.
     * @param file the <code>DBFile</code> representing the file, describing the
     * 'Entity'.
     * @param type the type of the 'Entity' we want to have.
     */
    public XMLEntity(DBFile file, String type) {
        this.type = type;
        this.fileId = file.getName().split("\\.")[0];
        this.id = this.fileId.split(type)[1];
        this.col = file.getCollection();
        this.dbFile = file;
    }

    /**
     * Returns
     * <code>true</code> if this
     * <code>XMLEntity</code> is saved or
     * <code>false</code> otherwise.
     *
     * @return <code>true</code> if this <code>XMLEntity</code> is saved or
     * <code>false</code> otherwise.
     */
    public boolean isSaved() {
        return this.dbFile.exist("//admin/saved[text() = 'yes']");
    }

    /**
     * Returns
     * <code>true</code> if this
     * <code>XMLEntity</code> is locked or
     * <code>false</code> otherwise.
     *
     * @return <code>true</code> if this <code>XMLEntity</code> is locked or
     * <code>false</code> otherwise.
     */
    public boolean isLocked() {
        return this.dbFile.exist("//admin/locked[text() != 'no']");
    }

    /**
     * Returns the username of the user which this
     * <code>XMLEntity</code> is locked by.
     *
     * @return the username of the user which this <code>XMLEntity</code> is
     * locked by or <code>null</code> if this <code>XMLEntity</code> is not
     * locked by anyone.
     */
    public String isLockedBy() {
        String ret = this.getAdminProperty("locked");
        if (ret.equals("no")) {
            return null;
        }
        return ret;
    }

    /**
     * Adds an admin property. Admin properties are in the 'admin part' of a
     * file. This method is used when 'admin part' is kept inside the file
     * represented by this
     * <code>XMLEntity</code>.
     *
     * @param property the name of the admin property
     * @param value the value to set
     */
    public void addAdminProperty(String property, String value) {
        this.dbFile.xAppend("//admin", "<" + property + ">" + value + "</" + property + ">");
    }

    /**
     * Gets the value of an admin property. Admin properties are in the 'admin
     * part' of a file. This method is used when 'admin part' is kept inside the
     * file represented by this
     * <code>XMLEntity</code>.
     *
     * @param property the name of the admin property
     * @return the value of the (first) specified admin property, or
     * <code>null</code> if there is no such admin property.
     */
    public String getAdminProperty(String property) {
        String[] ret = this.dbFile.queryString("//admin/" + property + "/text()");
        if (ret.length == 0) {
            return null;
        } else {
            return ret[0];
        }
    }

    /**
     * Sets the value of an admin property. Admin properties are in the 'admin
     * part' of a file. This method is used when 'admin part' is kept inside the
     * file represented by this
     * <code>XMLEntity</code>.
     *
     * @param property the name of the admin property
     * @param value the value to set
     */
    public void setAdminProperty(String property, String value) {
        //should not check, but demand to exist...temporary allow
        if (this.dbFile.exist("//admin/" + property) == false) {
            this.dbFile.xAppend("//admin", "<" + property + "/>");
        }

        if (value.length() == 0) {
            this.dbFile.xRemove("//admin/" + property);
            this.addAdminProperty(property, value);
        } else {
            this.dbFile.xUpdate("//admin/" + property, value);
        }
    }

    /**
     * Checks if an admin property has a particular value. Admin properties are
     * in the 'admin part' of a file. This method is used when 'admin part' is
     * kept inside the file represented by this
     * <code>XMLEntity</code>.
     *
     * @param property the name of the admin property
     * @param value the value of the admin property
     * @return <code>true</code> if the property has that value,
     * <code>false</code> otherwise
     */
    public boolean hasAdminProperty(String property, String value) {
        return this.dbFile.exist("//admin/" + property + "[text()='" + value + "' or text()='*']");
    }

    /**
     * Removes an admin property with a particular value. Admin properties are
     * in the 'admin part' of a file. This method is used when 'admin part' is
     * kept inside the file represented by this
     * <code>XMLEntity</code>.
     *
     * @param property the name of the admin property
     * @param value the value of the admin property
     */
    public void removeAdminProperty(String property, String value) {
        this.dbFile.xRemove("//admin/" + property + "[text()='" + value + "']");
    }

    /**
     * Gets the values of an admin property. Admin properties are in the 'admin
     * part' of a file. This method is used when 'admin part' is kept inside the
     * file represented by this
     * <code>XMLEntity</code>.
     *
     * @param property the name of the admin property
     * @return the values of the specified admin property
     */
    public String[] getValuesOfAdminProperty(String property) {
        String[] ret = this.dbFile.queryString("//admin/" + property + "/text()");
        return ret;
    }

    /**
     * Deletes the file associated with the
     * <code>XMLEntity</code> from the database.
     */
    public void delete() {
        this.dbFile.remove();
    }

    /**
     * Executes a query (either XPath or XQuery) against this
     * <code>DBFile</code>. The result is an array (of
     * <code>String</code>) containing the results of the query.
     *
     * @return an array of <code>String (String[])</code> containing the results
     * of the query.
     * @param query The XPath or XQuery query string to use.
     * @throws DBMSException with expected error codes
     */
    public String[] queryString(String query) {
        String[] ret;
        ret = this.dbFile.queryString(query);

        return ret;
    }

    /**
     * Runs an update operation using XUpdate.
     *
     * @return the number of modified nodes.
     * @param selectQuery XPath that selects what to update.
     * @param xml What to update as <code>String</code>.
     * @throws DBMSException with expected error codes.
     */
    public long xUpdate(String selectQuery, String xml) {
        return this.dbFile.xUpdate(selectQuery, xml);

    }

    public long xAppend(String selectQuery, String xml) {
        return this.dbFile.xAppend(selectQuery, xml);

    }

    public long xRemove(String selectQuery) {
        return this.dbFile.xRemove(selectQuery);

    }

    public boolean exist(String selectQuery) {
        return this.dbFile.exist(selectQuery);

    }

    /**
     * Returns the filename of the file represented by this
     * <code>XMLEntity</code>.
     *
     * @return a <code>String</code> representing the filename of the file
     * represented by this <code>XMLEntity</code>
     */
    public String getFileName() {
        return this.fileId + ".xml";
    }

    /**
     * Returns the XML of the
     * <code>XMLEntity</code> as a
     * <code>String</code>.
     *
     * @return a <code>String</code> representing the XML of the
     * <code>XMLEntity</code>
     */
    public String getXML() {
        String xml = this.dbFile.toString();
        if (xml.lastIndexOf("?>") != -1) {
            xml = xml.substring(xml.lastIndexOf("?>") + 3);
        }

        return xml;
    }

    /**
     * Returns the XML of the
     * <code>XMLEntity</code> as a
     * <code>String</code>.
     *
     * @return a <code>String</code> representing the XML of the
     * <code>XMLEntity</code>
     */
    public String toString() {
        return this.getXML();
    }

    /**
     * Returns a new system 'id' for the given collection.
     *
     * @param col the collection for which we want to get a new 'id'
     * @param type the type of 'Entity', for which we want a new 'id'
     * @return a new system 'id' for the given collection
     */
    public static String newId(DBCollection col, String type) {
        String fileId = "";
        String[] files = col.listFiles();

        if (files.length != 1) {
            //if there are files in the collection

            int[] ids = new int[files.length - 1];
            //get the 'numbers' id only (full id is: typeXX.xml)
            //we want 'XX' only
            int ind = 0;
            for (int i = 0; i < files.length; i++) {
                if (files[i].equals(type + ".xml")) {
                    continue;
                }
                ids[ind++] = Integer.parseInt(files[i].split(type)[1].split("\\.")[0]);
            }
            //sort the ids
            java.util.Arrays.sort(ids);
            //and get the max + 1
            fileId = String.valueOf(ids[ids.length - 1] + 1);
        } else //if there not any files in the collection, it is the first
        {
            fileId = "1";
        }

        return fileId;
    }

    public void copyFrom(XMLEntity fromXmlE) throws DMSException {
        //to admin gia na to 8esoume 3ana meta to 'copy'

        String admin = this.dbFile.queryString("//admin")[0];
        //  String timiKwdikou = this.dbFile.queryString("//Οντότητα/Δ" + typos + "/Ταυτότητα" + typos + "/Κωδικός/ΤιμήΚωδικού/text()")[0];
        //  String onomasia = this.dbFile.queryString("//Οντότητα/Δ" + typos + "/Ταυτότητα" + typos + "/ΚύριαΤρέχουσαΟνομασία/text()")[0];
        String pathTimiKwdiku = UtilsXPaths.getPathUriField(this.type);

        String timiKwdikou = "";
        if (!"".equals(pathTimiKwdiku)) {
            timiKwdikou = this.dbFile.queryString(pathTimiKwdiku + "/text()")[0];
        }
        this.dbFile.setXMLAsString(fromXmlE.getXML());
        this.dbFile.store();

        //epanafora tou admin [bale meta to uparxon kai sbhse to prwto]
        this.dbFile.xInsertAfter("//admin", admin);
        this.dbFile.xRemove("//admin[1]");
        if (!"".equals(pathTimiKwdiku)) {

            this.dbFile.xUpdate(pathTimiKwdiku, timiKwdikou);
        }
        //   this.dbFile.xUpdate("//Οντότητα/Δ" + typos + "/Ταυτότητα" + typos + "/ΚύριαΤρέχουσαΟνομασία/text()", onomasia);

        //remove the 'info', if any
        String info = this.getAdminProperty("info");
        if (info != null) {
            this.removeAdminProperty("info", info);
        }
        this.setAdminProperty("copyFrom", fromXmlE.id);
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
        DMSFile entTrans;
        entTrans = this.getTransEntityFile();
        String id = entTrans.queryString("if (exists(//translation[./" + this.getAdminProperty("lang") + "/text()='" + this.id + "'] )) then data(//translation[./" + this.getAdminProperty("lang") + "/text()='" + this.id + "']/@id) else '0'")[0];
        int transId = 0;
        if (id.equals("0")) {
            transId = entTrans.addEntity("translation", "");
            for (int i = 0; i < langs.length; i++) {
                entTrans.addIntoEntity(transId, langs[i], "");
            }
        } else {
            transId = Integer.parseInt(id);
        }
        return transId;
    }

    public XMLEntity getTranslation(String fromLang, String toLang) throws DBMSException, DMSException {
        XMLEntity ret = null;

        DMSFile entTrans = this.getTransEntityFile();
        String entId = this.id;
        String[] r = entTrans.queryString("//translation[" + fromLang + "/text()='" + entId + "']/" + toLang + "/text()");

        if (r.length == 0) {
            return null;
        };

        String newEntId = this.type + r[0];
        ret = new XMLEntity(this.db, this.getSystemDbCollection() + this.type, this.dbUser, this.dbPassword, this.type, newEntId);
        // ret = new XMLEntity(this.db, this.col.getName(), this.dbUser, this.dbPassword, this.type, newEntId);

        return ret;
    }

    /**
     * Sets the value of a lang in a translation to a particular termId.
     *
     * @param id the id of the translation.
     * @param lang the lang of which the value will be set.
     * @param entityId the entityId to which the specified lang will be set.
     * @throws DMSException with expected error codes.
     */
    public void setTranslation(int id, String lang, String entityId) throws DMSException {
        DMSFile entTrans = this.getTransEntityFile();
        entTrans.setIntoEntity(id, lang, entityId);
    }

    private DMSFile getTransEntityFile() {
        String parentCol = ApplicationBasicServlet.returnDBSystem();
        DMSConfig transConf = new DMSConfig(this.db, parentCol + "EntityTrans/", this.dbUser, this.dbPassword);
        DMSFile entTrans = new DMSFile(this.type + ".xml", transConf);
        return entTrans;
    }

    private String getSystemDbCollection() {
        //ola ta collections einai se prwto mono epipedo
        //opote PANTA ena pisw einai auto pou 8eloume (systemDbCol)
        //den einai kompso...alla DEN einai la8os

        DBCollection col = this.col.getParentCollection();
        String[] collectionLenght = col.getPath().split("/");
        int len = collectionLenght.length;
        if (len < 5) {
            return col.getPath() + "/";
        } else {
            return col.getParentCollection().getName() + "/";
        }
    }

    public void translateAsEntityFrom(XMLEntity fromXmlE, DMSConfig translationConf) throws DMSException {
        String toLang = this.getAdminProperty("lang");
        String fromLang = fromXmlE.getAdminProperty("lang");
        DBFile toDbF = this.getDbFile();

        //to admin gia na to 8esoume 3ana meta to 'copy'
        String admin = toDbF.queryString("//admin")[0];
        String pathTimiKwdiku = UtilsXPaths.getPathUriField(this.type);
        String timiKwdikou = "";
        if (pathTimiKwdiku != "") {
            timiKwdikou = toDbF.queryString(pathTimiKwdiku + "/text()")[0];
        }

        toDbF.setXMLAsString(fromXmlE.getXML());
        toDbF.store();

        //epanafora tou admin [bale meta to uparxon kai sbhse to prwto]
        toDbF.xInsertAfter("//admin", admin);
        toDbF.xRemove("//admin[1]");
        if (pathTimiKwdiku != "") {
            toDbF.xUpdate(pathTimiKwdiku, timiKwdikou);
        }
        String info = this.getAdminProperty("info");
        if (info != null) {
            this.removeAdminProperty("info", info);
        }
        //  System.out.println("admin " + admin);
        toDbF.xRemove("//admin/refs_by");

        this.setAdminProperty("translationFrom", fromXmlE.id);


        this.translateVocTerms(fromLang, toLang, translationConf);
        this.translateFreeTexts(fromLang, toLang, translationConf);
    }

    public void translateFrom(XMLEntity fromXmlE, DMSConfig translationConf) throws DMSException {
        String toLang = this.getAdminProperty("lang");
        String fromLang = fromXmlE.getAdminProperty("lang");

        this.copyFrom(fromXmlE);

        //remove the 'copyFrom' property that was set by copyFrom method
        this.removeAdminProperty("copyFrom", fromXmlE.id);
        this.setAdminProperty("translationFrom", fromXmlE.id);
        //pros8hkh sta EntityTrans

        String[] langs = ApplicationBasicServlet.systemLangs;
        int trId = this.addTranslation(langs);
        this.setTranslation(trId, fromLang, fromXmlE.id);
        this.setTranslation(trId, toLang, this.id);
        this.translate(fromLang, toLang, translationConf, fromXmlE);
        Utils.updateReferences(this, this.db, this.id, this.type, this.dbPassword, this.dbUser);

    }

    public void translate(String fromLang, String toLang, DMSConfig translationConf, XMLEntity fromXmlE) throws DMSException {


        long t1 = System.currentTimeMillis();
        this.translateVocTerms(fromLang, toLang, translationConf);
        long vocTime = System.currentTimeMillis();

        // this.translateEntities(fromLang, toLang, translationConf);
        Utils u = new Utils();
        System.out.println("Starting .................");
        ArrayList<String> allPrevious = new ArrayList();
        allPrevious.add(fromXmlE.type + fromXmlE.id);
        ArrayList<DBFile> l = u.findDependats(fromXmlE.getDbFile(), fromXmlE.id, fromXmlE.type, this.db, this.dbUser, this.dbPassword, fromXmlE.id, fromXmlE.type, allPrevious);
        System.out.println("Ends:-)");
        l.add(fromXmlE.getDbFile());
        /*  for (int i = 0; i < l.size(); i++) {
         System.out.println("filename " + l.get(i).getName());
         }*/
        translateAllEntities(fromLang, toLang, translationConf, l);

        long translateEntitiesTime = System.currentTimeMillis();

        translateFreeTexts(fromLang, toLang, translationConf);
        long t2 = System.currentTimeMillis();

        long upTime = System.currentTimeMillis();
        //    Thread t = new Thread(r1);
        //   t.start();

        long upTimeEnd = System.currentTimeMillis();


    }

    public void translateVocTerms(String fromLang, String toLang, DMSConfig translationConf) throws DMSException {

        DBFile toDbF = this.getDbFile();

        //-----Vocabulary-----
        //String nodeQ = "//*[@sps_vocabulary and @sps_id != '' and @sps_id != '0']";
        String nodeQ = "//*[@sps_vocabulary and @sps_id != '0' and text() != '']";

        String query = nodeQ + "/@sps_vocabulary/string()";
        String queryIds = nodeQ + "/@sps_id/string()";
        String queryTexts = nodeQ + "/text()";

        //upo8etoume oti erxontai me thn idia seira [praktika etsi ginetai panta]
        //efoson einai idia h sun8hkh sta queries
        String[] nodes = toDbF.queryString(query);
        String[] ids = toDbF.queryString(queryIds);
        String[] texts = toDbF.queryString(queryTexts);

        for (int i = 0; i < texts.length; i++) {
            String vocName = nodes[i];
            int oldTermId = -1;
            try {

                oldTermId = Integer.parseInt(ids[i]);
            } catch (java.lang.NumberFormatException e) {
            }
            String oldTerm = texts[i];
            Vocabulary voc = new Vocabulary(vocName, toLang, translationConf);
            int newTermId = voc.translateTerm(oldTermId, fromLang, toLang);

            String newTerm;
            //an den uparxei tote pros8ese ton sto (toLang) le3ilogio, ws '[fromLang] ...'
            if (newTermId == -1) {

                newTerm = fromLang + "// " + oldTerm;
                newTermId = voc.addTerm(newTerm);
                String[] langs = ApplicationBasicServlet.systemLangs;
                int trId = voc.addTranslation(langs);
                voc.setTranslation(trId, fromLang, oldTermId);
                voc.setTranslation(trId, toLang, newTermId);

            } else {
                newTerm = voc.termValueOf(newTermId);
            }

            //fix value...
            oldTerm = this.fixText(oldTerm);
            String updQ = "//*[@sps_vocabulary = '" + vocName + "' and text() = '" + oldTerm + "']";
            toDbF.xUpdate(updQ + "/@sps_id", String.valueOf(newTermId)); //new id
            //Last the text
            toDbF.xUpdate(updQ, newTerm); //new value
            // queryMap.put(updQ + "/@sps_id", String.valueOf(newTermId));
            //Last the text           
            // queryMap.put(updQ, newTerm);


        }
    }

    public void translateAllEntities(String fromLang, String toLang, DMSConfig translationConf, ArrayList<DBFile> entitiesForTranslation) throws DMSException {

        //DBFile toDbF = this.getDbFile();
        for (int j = 0; j < entitiesForTranslation.size(); j++) {
            DBFile transl = entitiesForTranslation.get(j);
            //-----Entities-----
            //    System.out.println("name " + transl.getName());

            String docOrg = transl.queryString2("//admin/organization/text()")[0];

            //String nodeQ = "//*[@sps_type and @sps_id != '0' and @sps_id != '']";

            String[] t = transl.getPath().split("/");
            String parentCol = this.getSystemDbCollection();
            String upType = t[t.length - 3];
            String upId = transl.getName().split(".xml")[0].split(upType)[1];
            XMLEntity upxmlEnt = new XMLEntity(translationConf.DB, parentCol + t[t.length - 3], translationConf.DB_USERNAME, translationConf.DB_PASSWORD, t[t.length - 3], transl.getName().split(".xml")[0]);

            XMLEntity updateXmlE = upxmlEnt.getTranslation(fromLang, toLang);

            if (updateXmlE == null) {

                DBCollection dbCol = new DBCollection(this.db, parentCol + upType, this.dbUser, this.dbPassword);
                String[] results = initInsertFile(upType,true);
                String newId = results[0];
                newId = newId.split(upType)[1];
                String collName = results[1];

                DBFile dbF = new DBFile(this.db, collName, upType + newId + ".xml", this.dbUser, this.dbPassword);

                dbF.setXMLAsString(upxmlEnt.getDbFile().getXMLAsString());
                dbF.store();

                //create new XMLEntity for the new 'Entity'
                updateXmlE = new XMLEntity(dbF, upType);

                //fill by hand the fields that are not filled [see this constructor java doc]
                updateXmlE.db = upxmlEnt.db;
                updateXmlE.dbUser = upxmlEnt.dbUser;
                updateXmlE.dbPassword = upxmlEnt.dbPassword;

                updateXmlE.setAdminProperty("id", newId);
                String uri_name = "";
                try {
                    uri_name = DMSTag.valueOf("uri_name", "target", upType, this.conf)[0];
                } catch (DMSException ex) {
                    ex.printStackTrace();
                }
                String uriValue = this.URI_Reference_Path + uri_name + "/" + newId;
                String uriPath = UtilsXPaths.getPathUriField(upType);

                updateXmlE.setAdminProperty("uri_id", uriValue);
                if (!uriPath.equals("")) {
                    updateXmlE.xUpdate(uriPath, uriValue);
                }
                updateXmlE.setAdminProperty("lang", toLang);
                updateXmlE.setAdminProperty("organization", docOrg);
                updateXmlE.setAdminProperty("translationFrom", upId);
                updateXmlE.id = newId;
                updateXmlE.translateAsEntityFrom(upxmlEnt, translationConf);


                //pros8hkh sta EntityTrans
                String[] langs = ApplicationBasicServlet.systemLangs;
                int trId = updateXmlE.addTranslation(langs);
                updateXmlE.setTranslation(trId, fromLang, upId);
                updateXmlE.setTranslation(trId, toLang, newId);
            }
            String nodeQ = "//*[@sps_type and @sps_id!='' and @sps_id != '0' and text() != '']";

            String queryTypes = nodeQ + "/@sps_type/string()";
            String queryIds = nodeQ + "/@sps_id/string()";
            String queryTexts = nodeQ + "/text()";
            String[] types = updateXmlE.queryString(queryTypes);
            String[] ids = updateXmlE.queryString(queryIds);
            String[] texts = updateXmlE.queryString(queryTexts);
            for (int i = 0; i < texts.length; i++) {
                String type = types[i];
                String id = ids[i];
                String oldVal = texts[i];
                String fileId = type + id;
                String newId = "";
                parentCol = this.getSystemDbCollection();
                XMLEntity xmlEnt = new XMLEntity(translationConf.DB, parentCol + type, translationConf.DB_USERNAME, translationConf.DB_PASSWORD, type, fileId);
                XMLEntity newXmlE = xmlEnt;
                if (!xmlEnt.getDbFile().queryString2("//admin/lang/text()")[0].equals(toLang)) {
                    newXmlE = xmlEnt.getTranslation(fromLang, toLang);
                }

                if (newXmlE == null) {

                    DBCollection dbCol = new DBCollection(this.db, parentCol + type, this.dbUser, this.dbPassword);
                    String[] results = initInsertFile(type,true);
                    newId = results[0];
                    newId = newId.split(type)[1];
                    String collName = results[1];

                    DBFile dbF = new DBFile(this.db, collName, type + newId + ".xml", this.dbUser, this.dbPassword);

                    dbF.setXMLAsString(xmlEnt.getDbFile().getXMLAsString());
                    dbF.store();

                    //create new XMLEntity for the new 'Entity'
                    newXmlE = new XMLEntity(dbF, type);

                    //fill by hand the fields that are not filled [see this constructor java doc]
                    newXmlE.db = xmlEnt.db;
                    newXmlE.dbUser = xmlEnt.dbUser;
                    newXmlE.dbPassword = xmlEnt.dbPassword;

                    newXmlE.setAdminProperty("id", newId);
                    String uri_name = "";
                    try {
                        uri_name = DMSTag.valueOf("uri_name", "target", type, this.conf)[0];
                    } catch (DMSException ex) {
                        ex.printStackTrace();
                    }
                    String uriValue = this.URI_Reference_Path + uri_name + "/" + newId;
                    String uriPath = UtilsXPaths.getPathUriField(type);
                    newXmlE.setAdminProperty("uri_id", uriValue);
                    if (!uriPath.equals("")) {
                        newXmlE.xUpdate(uriPath, uriValue);
                    }
                    newXmlE.setAdminProperty("lang", toLang);
                    newXmlE.setAdminProperty("organization", docOrg);
                    newXmlE.setAdminProperty("translationFrom", id);

                    newXmlE.translateAsEntityFrom(xmlEnt, translationConf);
                    //updateReferences(newXmlE, newXmlE.db, newId, type, newXmlE.dbPassword, newXmlE.dbUser);


                    //pros8hkh sta EntityTrans
                    String[] langs = ApplicationBasicServlet.systemLangs;
                    int trId = newXmlE.addTranslation(langs);
                    newXmlE.setTranslation(trId, fromLang, id);
                    newXmlE.setTranslation(trId, toLang, newId);
                }
                String newNameQ = "";
                //pernei to onoma tu kainurgiou xml kai to vazei sto antistoixo pedio tu arxiku mas xml
                newNameQ = UtilsXPaths.getSearchXpathAtName(type) + "/text()";

                String newName = "";
                try {
                    newName = newXmlE.queryString(newNameQ)[0];
                } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                }

                //fix value...
                oldVal = this.fixText(oldVal);

                String updQ = "//*[@sps_type = '" + type + "' and text() = '" + oldVal + "']";
                updateXmlE.xUpdate(updQ + "/@sps_id", newXmlE.id); //new id

                //  queryMap.put(updQ + "/@sps_id", newXmlE.id);

                String newVal = "";
                newVal = newName + " #" + newXmlE.id;
                updateXmlE.xUpdate(updQ, newVal); //new value

                // queryMap.put(updQ, newVal);
            }
            Utils.updateReferences(updateXmlE, updateXmlE.db, updateXmlE.id, upType, updateXmlE.dbPassword, updateXmlE.dbUser);

        }
    }

    public void translateFreeTexts(String fromLang, String toLang, DMSConfig translationConf) {

        DBFile toDbF = this.getDbFile();

        //-----Free Texts-----
        //String nodeQ = "//*[name()!='admin']/*[count(*[1])=0 and count(@*)=0 and text()!='']";
        String nodeQ = "//*[name()!='admin' and name()!='versions']/*[count(@*)=0 and text()!='' and (name()!='ΤιμήΚωδικού' and name()!='IdentificationNumber')]";
        String queryTexts = nodeQ + "/text()";
        String queryText = nodeQ + "/name()";
        String[] text = toDbF.queryString(queryText);
        String[] texts = toDbF.queryString(queryTexts);

        for (int i = 0; i < texts.length; i++) {
            String oldText = texts[i];
            //fix Text...
            oldText = this.fixText(oldText);

            String newText = fromLang + "// " + oldText;
            //update by text
            String updQ = "//*[name()!='admin' and name()!='versions']/*[count(@*)=0 and text() = '" + oldText + "']";
            if (!newText.contains("&amp;")) {
                toDbF.xUpdate(updQ, newText); //new value
            }

            //queryMap.put(updQ, newText);
        }
    }

    private String fixText(String str) {
        str = str.replaceAll("[ \t]+", " ");
        str = str.replaceAll("'", "''");
        str = str.replaceAll("\"", "&quot;");
        return str;
    }

    /**
     * Returns a
     * <code>DBFile</code> associated with the 'Entity' with which this
     * <code>XMLEntity</code> is associated.
     *
     * @return a <code>DBFile</code> associated with the 'Entity' with which
     * this <code>XMLEntity</code> is associated.
     */
    public DBFile getDbFile() {
        return dbFile;
    }
 
}
