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
 * This file is part of the x3mlEditor webapp of Mapping Memory Manager project.
 */
package isl.x3ml.servlets;

import isl.dbms.DBCollection;
import isl.dbms.DBFile;
import isl.x3ml.BasicServlet;
import isl.x3ml.utilities.Schema;
import isl.x3ml.utilities.TargetSchemaTools;
import isl.x3ml.utilities.Utils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author samarita
 */
public class Mapping extends BasicServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String xpath = request.getParameter("xpath");
        String id = request.getParameter("id");
        String newValue = request.getParameter("value");
        String action = request.getParameter("action");
        String chosenAPI = request.getParameter("mode");

        int mode = 2;
//        System.out.println("CHOSEN=" + chosenAPI);
        if (chosenAPI != null) {
            mode = Integer.parseInt(chosenAPI);
        }

        servletParams(request, response);

        String xslPath = baseURL + "/formating/xsl/mapping.xsl";

        DBFile mappingFile = null;
        if (id != null && id.length() > 0) {

            String xmlId = "Mapping" + id + ".xml";
            DBCollection dbc = new DBCollection(super.DBURI, applicationCollection + "/Mapping", super.DBuser, super.DBpassword);
            String collectionPath = getPathforFile(dbc, xmlId, id);
            mappingFile = new DBFile(DBURI, collectionPath, xmlId, DBuser, DBpassword);

        }

        if (action != null) { //Add mode 
            if (action.equals("close")) { //Added from FeXML File servlet to unlock file
                mappingFile.xUpdate("//admin/locked", "no");
            } else if (action.equals("help")) {
                DBFile helpFile = new DBFile(DBURI, x3mlCollection, "Help.xml", DBuser, DBpassword);
                String field = request.getParameter("field");
                String[] results = helpFile.queryString2("//description[@field='" + field + "']/string()");
//                System.out.println("//description[@field='" + field + "']/string()");
                if (results != null) {
                    if (results.length > 0) {
                        out.println(results[0]);
                    }
                }

            } else {
                if (!xpath.startsWith("//")) {
                    xpath = "//" + xpath;
                }
//                System.out.println("XPATH=" + xpath);
//                System.out.println(action);
                if (action.startsWith("add")) {

                    if (action.startsWith("addAttr")) {
                        String attrName = request.getParameter("name");
                        mappingFile.xAddAttribute(xpath, attrName, "");
                    } else {
                        DBFile templateFile = new DBFile(DBURI, x3mlCollection, "Template.xml", DBuser, DBpassword);

                        String templateAdd = "";
                        if (action.contains("___")) { //Optional Part
                            String[] actionParts = action.split("___");
                            templateAdd = "//optionalPart/" + actionParts[1].trim();
                            if (actionParts.length > 2) {
                                templateAdd = templateAdd + "/*";
                            }
                        } else {
//                            System.out.println("1=" + xpath);
                            templateAdd = xpath.replaceAll("\\[\\d+\\]", "");
//                            System.out.println("2=" + templateAdd);

                        }
                        String[] mappingFrags = templateFile.queryString2(templateAdd);
                        String fatherXpath = xpath.substring(0, xpath.lastIndexOf("/"));
                        for (String mappingFrag : mappingFrags) {
//                            System.out.println("MAPPING_FRAG=" + mappingFrag);
//                            System.out.println(action);
//                            System.out.println(xpath);
                            if (action.startsWith("addBefore")) { //Inserts before specified element

//                                System.out.println("addBefore");
                                mappingFile.xInsertBefore(xpath, mappingFrag);

                            } else if (action.startsWith("addAfter")) {//Inserts after specified element
                                mappingFile.xInsertAfter(xpath, mappingFrag);

                            } else {
                                mappingFile.xAppend(fatherXpath, mappingFrag); //Appends to father element (LAST POSITION!)
                            }
                        }
                    }

                } else if (action.equals("delete")) {

                    mappingFile.xRemove(xpath);
                } else if (action.startsWith("move")) {

                    if (action.endsWith("Up")) {
//                        System.out.println("XPATH=" + xpath);
                        String elementToMove = "";
                        if (xpath.contains("link")) {
                            elementToMove = "link";
                        } else {
                            elementToMove = "mapping";
                        }

                        mappingFile.xCopyBefore(xpath, xpath + "/preceding-sibling::" + elementToMove + "[1]");
                        mappingFile.xRemove(xpath + "/following-sibling::" + elementToMove + "[1]");

                    } else if (action.endsWith("Down")) {

                        mappingFile.xMoveAfter(xpath, xpath);
                    }
                } else if (action.startsWith("clone")) {
                    mappingFile.xCopyAfter(xpath, xpath);
                }

                String html = transform("<xml>" + mappingFile.toString() + "<output><id>" + id + "</id><viewMode>0</viewMode></output></xml>", xslPath);
//                System.out.println(html);
                ArrayList<String> bodies = findReg("<body.*?</body>", html, Pattern.DOTALL);
                out.println(bodies.get(0));
            }
//
        } else {
            if (newValue == null) { //Get List mode
                String[] values = mappingFile.queryString2(xpath + "/string()");
                String currentValue = "";
                if (values.length > 0) {
                    currentValue = values[0];
                }

//                int mode = 2; //1 for Ourania's API, 2 for Giorgos' API, 3 for Konstantina's API
                String[] targetSchemas = mappingFile.queryString2("//target_info/target_schema/@schema_file/string()");

                ArrayList<String> resultsList = new ArrayList<String>();
                String[] results;

                out.println("[{'':[{'id':'','name':''}],"); //Adding empty default
                if (mode == 1) {
                    resultsList = getListValues(mappingFile, id, xpath, targetSchemas);

                } else if (mode == 2) {
                    String[] targetSchemaTitles = mappingFile.queryString2("//target_info/target_schema/string()");
                    String[] prefixes = mappingFile.queryString2("//namespace/@prefix/string()");
                    String[] uris = mappingFile.queryString2("//namespace/@uri/string()");

                    HashMap<String, String> filenameAndPrefix = new HashMap<String, String>();
                    HashMap<String, String> filenameAndType = new HashMap<String, String>();
                    HashMap<String, String> filenameAndURI = new HashMap<String, String>();

                    for (int i = 0; i < targetSchemas.length; i++) {
                        String targetSchema = targetSchemas[i];
                        String targetSchemaTitle = targetSchemaTitles[i];
                        String prefix = prefixes[i + 1];
                        String uri = uris[i + 1];

                        filenameAndType.put(targetSchema, targetSchemaTitle);
                        filenameAndPrefix.put(targetSchema, prefix);
                        filenameAndURI.put(targetSchema, uri);
//                        System.out.println(filenameAndPrefix);
//                        System.out.println(targetSchemaTitle);
                        if (targetSchemaTitle.equals("")) {
                            targetSchemaTitle = targetSchema; //Maybe replace with prefix in the future?
                        }

                        if (targetSchema.endsWith(".rdfs") || targetSchema.endsWith(".rdf")) {
                            if (targetSchema.startsWith("../")) {//Hack for CIDOC
                                targetSchema = targetSchema.replace("../", "");
                            }
                        }
                    }
                    HttpSession session = sessionCheck(request, response);
                    if (session == null) {
                        session = request.getSession();
                    }
                    results = getListValues(mappingFile, xpath, targetSchemas, session);
                    resultsList = new ArrayList(Arrays.asList(results));
//                    System.out.println(resultsList);
//                    System.out.println(results.length);

                    if (resultsList.size() > 0) {
                        out.println(tableToJSON(currentValue, resultsList, filenameAndType, filenameAndPrefix, filenameAndURI));
                    }
//                    if (i < targetSchemas.length - 1) {
//                        out.println(" }, {");
//                    }

                } else if (mode == 3) {//
//                    ArrayList<OntModel> modelList = new <OntModel>ArrayList();
//                    for (String targetSchema : targetSchemas) {
//                        initiateModel(baseURL + "/FetchBinFile?id=" + id + "&file=" + targetSchema);
//                    }
//                    resultsList = getListValues(mappingFile, xpath, modelList);
//                    System.out.println("MODE3 LENGTH is " + resultsList.size());
                }

                if (mode != 2) {
                    if (resultsList.size() > 0) {
                        out.println(tableToJSON("", currentValue, resultsList));
                    }
//                            if (i < targetSchemas.length - 1) {
//                                out.println(" }, {");
//                            }
                }
////
//                    }
//                }
                out.println("}];");
//                System.out.println(out.toString());

            } else { //Update value mode
                boolean isAttribute = false;
                String attributeName = "";

                String fatherXpath = xpath;
                if (pathIsAttribute(xpath)) { //Generic for attributes
                    attributeName = xpath.substring(xpath.lastIndexOf("/") + 2);
                    fatherXpath = xpath.substring(0, xpath.lastIndexOf("/"));
                    isAttribute = true;
                }
                String[] values = mappingFile.queryString2(xpath + "/string()");

                String currentValue = "";
//                System.out.println("LEN=" + values.length);
                if (values.length > 0) {
                    currentValue = values[0];

                    String strippedNewValue = newValue;
                    if ((xpath.contains("/entity/type") || xpath.contains("/relationship"))) {
                        if (newValue.startsWith("http://")) { //Stripping slashes...
                            strippedNewValue = newValue.substring(newValue.lastIndexOf("/") + 1);
                        } else if (newValue.contains(":")) {//Stripping prefixex. Is it safe? 
                            strippedNewValue = newValue.substring(newValue.indexOf(":") + 1);
                        }
                    }
                    out.println(strippedNewValue);
                    if (!currentValue.equals(newValue)) {
                        if (isAttribute) {
                            mappingFile.xAddAttribute(fatherXpath, attributeName, newValue);
                        } else {
                            mappingFile.xUpdate(xpath, newValue);
                        }

                    }
                } else {
                    out.println("XML field missing. Contact administrator!");
                }

            }
        }

        out.close();

    }

//    private ArrayList<String> getListValues(DBFile mappingFile, String xpath, ArrayList<OntModel> model) {
//
//        ArrayList<String> resultsList = null;
//
//        if (xpath.contains("/domain/target_node/entity/type")) {
//            resultsList = getAllClasses();
//        } else if (xpath.contains("/relationship")) {
//            xpath = xpathResolver(xpath, "relationship");
//
//            String[] selectedEntities = mappingFile.queryString2(xpath);
//            System.out.println("LENGTH is =" + selectedEntities.length);
//            if (selectedEntities.length > 0) {
//                if (selectedEntities.length == 1 && selectedEntities[0].equals("")) {
//                    resultsList = new ArrayList<String>();
//                } else {
//                    System.out.println("SEL=" + selectedEntities[0]);
//                    resultsList = listProperties(selectedEntities[0]);
//                }
//            } else {
//                resultsList = new ArrayList<String>();
//            }
//
//        } else if (xpath.contains("/entity")) {
//            xpath = xpathResolver(xpath, "entity");
//
//            String[] selectedProperty = mappingFile.queryString2(xpath);
//            if (selectedProperty.length == 0 && xpath.contains("internal_node")) { //No internal_node in P row
//                selectedProperty = mappingFile.queryString2(xpath.replace("internal_node[last()]/", ""));
//            }
//            if (selectedProperty.length > 0) {
//                if (selectedProperty.length == 1 && selectedProperty[0].equals("")) {
//                    resultsList = new ArrayList<String>();
//
//                } else {
//                    String path = selectedProperty[0];
//                    System.out.println("SEL PATH=" + path);
//                    resultsList = listObjects(path);
//
//                }
//            } else {
//                resultsList = new ArrayList<String>();
//
//            }
//
//        } else {
//            resultsList = getAllClasses();
//        }
//        return resultsList;
//    }
    private ArrayList<String> getListValues(DBFile mappingFile, String id, String xpath, String[] schemaFilenames) {

        TargetSchemaTools schema = new TargetSchemaTools(schemaFilenames, uploadsFolder + id + "/");

        ArrayList<String> resultsList = null;

        if (xpath.contains("/domain/target_node/entity/type")) {
            resultsList = schema.getAllClasses();
        } else if (xpath.contains("/relationship")) {
            xpath = xpathResolver(xpath, "relationship");

            String[] selectedEntities = mappingFile.queryString2(xpath);
            if (selectedEntities.length > 0) {
                if (selectedEntities.length == 1 && selectedEntities[0].equals("")) {
                    resultsList = new ArrayList<String>();
                } else {
                    resultsList = schema.getAcceptedValuesFor(selectedEntities[0]);
                }
            } else {
                resultsList = new ArrayList<String>();
            }

        } else if (xpath.contains("/entity")) {
            xpath = xpathResolver(xpath, "entity");

            String[] selectedProperty = mappingFile.queryString2(xpath);
            if (selectedProperty.length == 0 && xpath.contains("internal_node")) { //No internal_node in P row
                selectedProperty = mappingFile.queryString2(xpath.replace("internal_node[last()]/", ""));
            }
            if (selectedProperty.length > 0) {
                if (selectedProperty.length == 1 && selectedProperty[0].equals("")) {
                    resultsList = new ArrayList<String>();

                } else {
                    String path = selectedProperty[0];
                    resultsList = schema.getAcceptedValuesFor(path);
                }
            } else {
                resultsList = new ArrayList<String>();

            }

        } else {
            resultsList = schema.getAllClasses();
        }
        return resultsList;
    }

    private String xpathResolver(String xpath, String type) {
        if (type.equals("relationship")) {
            if (xpath.endsWith("/relationship[1]")) {
                xpath = xpath.replaceAll("/link\\[[^$]+", "/domain/target_node/entity/type/string()");
            } else if (xpath.contains("/additional[1]")) {
                xpath = xpath.replace("/relationship", "/../type/string()");
            } else if (xpath.contains("/additional[")) {
                xpath = xpath.replace("/relationship", "/preceding-sibling::*[1]//type/string()");
            } else {
                xpath = xpath + "/preceding-sibling::*[1]/type/string()";
            }
        } else {
            if (xpath.contains("/additional[")) {
                xpath = xpath + "/../../relationship/string()";
            } else if (xpath.contains("/range/target_node/entity")) {
                xpath = xpath.replaceAll("/range[^$]+", "/path/target_relation/relationship[last()]/string()");
            } else if (xpath.contains("/path/target_relation")) {
                xpath = xpath + "/../preceding-sibling::*[1]/string()";
            }
        }
        return xpath;
    }

    //MODE 2
    private String[] getListValues(DBFile mappingFile, String xpath, String[] schemaFilenames, HttpSession session) {
        String currentSchemaFilename = schemaFilenames[0].substring(3);
        DBFile cidocRdfsFile = new DBFile(DBURI, x3mlCollection, currentSchemaFilename, DBuser, DBpassword);

        Schema cidocSchema = new Schema(cidocRdfsFile, schemaFilenames);
        String[] resultsList = new String[]{};

        StringBuffer schemas = new StringBuffer();
        for (String tSchema : schemaFilenames) {
            schemas.append(tSchema);
        }
        HashMap<String, String[]> resourcesList = null;
        if (session != null) {
            resourcesList = (HashMap) session.getAttribute("resourcesList");
//            System.out.println("FROM SES=" + resourcesList);
        }

        if (xpath.contains("/domain/target_node/entity/type")) {
            if (resourcesList != null && resourcesList.containsKey(schemas.toString() + currentSchemaFilename)) {

                resultsList = resourcesList.get(schemas.toString() + currentSchemaFilename);
            } else {
                resultsList = cidocSchema.getAllClasses();
                System.out.println(schemas.toString());
                System.out.println(currentSchemaFilename);

                System.out.println(resultsList);
                if (resourcesList == null) {
                    resourcesList = new HashMap<String, String[]>();
                }
                resourcesList.put(schemas.toString() + currentSchemaFilename, resultsList);
                session.setAttribute("resourcesList", resourcesList);
            }
        } else if (xpath.contains("/relationship")) {
            xpath = xpathResolver(xpath, "relationship");

            String[] selectedEntities = mappingFile.queryString2(xpath);

            StringBuffer selEnts = new StringBuffer("ENTITIES");
            for (String selectedEntity : selectedEntities) {
                selEnts.append(selectedEntity);
            }

            if (resourcesList != null && resourcesList.containsKey(schemas.toString() + currentSchemaFilename + selEnts.toString())) {
                resultsList = resourcesList.get(schemas.toString() + currentSchemaFilename + selEnts.toString());
            } else {

                if (selectedEntities.length > 0) {
                    if (selectedEntities.length == 1 && selectedEntities[0].equals("")) {
                        resultsList = new String[]{};
                    } else {
                        ArrayList<String> mergedClassHierarchyList = new ArrayList<String>();
                        for (String entity : selectedEntities) {
//                            System.out.println("ENT=" + entity);
                            if (entity.contains(":")) {
                                entity = entity.substring(entity.indexOf(":") + 1);
                            }

                            ArrayList<String> classHierarchy = new ArrayList<String>();
                            cidocSchema.getSuperClassesOf(entity, classHierarchy);
                            for (String cl : classHierarchy) {
                                if (cl.contains("/")) { //in case class has full URI name, strip it
                                    cl = cl.substring(cl.lastIndexOf("/") + 1);
                                }
                                if (!mergedClassHierarchyList.contains(cl)) {
                                    mergedClassHierarchyList.add(cl);
                                }
                            }
                        }
//                        System.out.println(mergedClassHierarchyList);
                        ArrayList<String> propertiesList = cidocSchema.getPropertiesFor(mergedClassHierarchyList);
                        resultsList = propertiesList.toArray(new String[propertiesList.size()]);
                    }
                } else {
                    resultsList = new String[]{};
                }
//                System.out.println(schemas.toString() + currentSchemaFilename + selEnts.toString());
//                System.out.println(resultsList);
//                System.out.println(session);
                resourcesList.put(schemas.toString() + currentSchemaFilename + selEnts.toString(), resultsList);
                session.setAttribute("resourcesList", resourcesList);

            }
        } else if (xpath.contains("/entity")) {

            xpath = xpathResolver(xpath, "entity");

            String[] selectedProperty = mappingFile.queryString2(xpath);
            StringBuilder selProp = new StringBuilder("PROPERTIES");
            for (String selectedP : selectedProperty) {
                selProp.append(selectedP);
            }
            if (resourcesList != null && resourcesList.containsKey(schemas.toString() + currentSchemaFilename + selProp.toString())) {
                resultsList = resourcesList.get(schemas.toString() + currentSchemaFilename + selProp.toString());
            } else {

                if (selectedProperty.length > 0) {
                    if (selectedProperty.length == 1 && selectedProperty[0].equals("")) {
                        resultsList = new String[]{};

                    } else {
                        String path = selectedProperty[0];
                        if (path.contains(":")) {
                            path = path.substring(path.indexOf(":") + 1);
                        }
//                    System.out.println("PATH=" + path);
                        String range = cidocSchema.getRangeForProperty(path);
//                    System.out.println("RANGE=" + range);
                        ArrayList<String> classHierarchy = new ArrayList<String>();
                        cidocSchema.getSubClassesOf(range, classHierarchy);
//                        classHierarchy = new Utils().sort(classHierarchy);
//                        System.out.println("CLASSHIER=" + classHierarchy);
                        resultsList = classHierarchy.toArray(new String[classHierarchy.size()]);
                    }
                } else {
                    resultsList = new String[]{};

                }

                resourcesList.put(schemas.toString() + currentSchemaFilename + selProp.toString(), resultsList);
                session.setAttribute("resourcesList", resourcesList);
            }

        } else {
            resultsList = cidocSchema.getAllClasses();
        }
        return resultsList;
    }

    private String tableToJSON(String selectedValue, ArrayList<String> table, HashMap<String, String> filenameAndType, HashMap<String, String> filenameAndPrefix, HashMap<String, String> filenameAndURI) {
        StringBuilder output = new StringBuilder();

        String title = "";
        LinkedHashMap<String, ArrayList> valuesBySchema = new LinkedHashMap();
        for (String res : table) {

            if (res.contains("######")) {
                String[] resValues = res.split("######");
                title = resValues[0];
                res = resValues[1];

                if (res.startsWith("http://")) { //Probably have to change prefix!
                    //Following snippet finds proper prefix for a class using URI
                    String value = res.substring(0, res.lastIndexOf("/") + 1);
                    for (Entry<String, String> entry : filenameAndURI.entrySet()) {
                        if (value.equals(entry.getValue())) {
                            title = entry.getKey();
                            if (title.startsWith("../")) {
                                title = title.substring(3);
                            }
                            res = res.substring(res.lastIndexOf("/") + 1);
                        }
                    }
                }

                ArrayList valuesSoFar = valuesBySchema.get(title);
                if (valuesSoFar == null) {
                    valuesSoFar = new ArrayList();

                    valuesSoFar.add(res);

                } else {
                    if (!valuesSoFar.contains(res)) { //no duplicates
                        valuesSoFar.add(res);
                    }
                }
                valuesBySchema.put(title, valuesSoFar);

            }

        }

        for (Map.Entry<String, ArrayList> entry : valuesBySchema.entrySet()) {
            String key = entry.getKey();
            ArrayList<String> value = entry.getValue();
//            System.out.println("KEY=" + key);
//            System.out.println("VAL=" + value);
            value = new Utils().sort(value);

            //Replace filename with friendly name
            if (key.startsWith("cidoc_crm")) {
                key = "../" + key;
            }
            String prefix = filenameAndPrefix.get(key);
            String type = filenameAndType.get(key);

            output.append("'").append(type).append("': [");
            for (String val : value) {
                String strippedURL = val;
                if (val.startsWith("http://")) { //If URL then strip slashes part
                    strippedURL = val.substring(val.lastIndexOf("/") + 1);
                    
                     output.append("{'id':'").append(val).append("',");
                    if (selectedValue.equals(val)) {
                        output.append("\n'selected':'selected").append("',");
                    }
                    
                } else {
                    output.append("{'id':'").append(prefix + ":" + val).append("',");
                    if (selectedValue.equals(prefix + ":" + val)) {
                        output.append("\n'selected':'selected").append("',");
                    }
                }
                output.append("\n'name':'").append(strippedURL).append("'},");
            }
            output.append("],");

        }

//        
//        System.out.println(valuesBySchema);
        String out = output.substring(0, output.length() - 1);
//        System.out.println(out);
        return out;

    }

    private String tableToJSON(String title, String selectedValue, ArrayList<String> table) {
        StringBuilder output = new StringBuilder();
        if (title.equals("")) {
            output.append("'").append("': [");
        } else {
            output.append("'").append(title).append("': [");
        }
        for (String res : table) {
            String strippedURL = res;
            if (res.startsWith("http://")) { //If URL then strip slashes part
                strippedURL = res.substring(res.lastIndexOf("/") + 1);
            }
            output.append("{'id':'").append(res).append("',");
            if (res.equals(selectedValue)) {
                output.append("\n'selected':'selected").append("',");
            }
            output.append("\n'name':'").append(strippedURL).append("'},");
        }

        String out = output.substring(0, output.length() - 1);
        return out + "]";

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
