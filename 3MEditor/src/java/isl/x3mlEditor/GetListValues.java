/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isl.x3mlEditor;

import com.hp.hpl.jena.ontology.OntModel;
import isl.dbms.DBCollection;
import isl.dbms.DBFile;
import isl.reasoner.OntologyReasoner;
import static isl.x3mlEditor.BasicServlet.applicationCollection;
import isl.x3mlEditor.utilities.Schema;
//import isl.x3mlEditor.utilities.TargetSchemaTools;
import isl.x3mlEditor.utilities.Utils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author samarita
 */
public class GetListValues extends BasicServlet {

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
        response.setContentType("application/json;charset=UTF-8");

//        response.addHeader("Access-Control-Allow-Origin", "http://139.91.190.47");
//        response.addHeader("Access-Control-Allow-Origin", "http://skyros.no-ip.info");
        PrintWriter out = response.getWriter();

        String id = request.getParameter("id");
        String type = request.getParameter("type");
        String xpath = request.getParameter("xpath");
        String targetAnalyzer = request.getParameter("targetAnalyzer");

        int targetMode;
        if (targetAnalyzer == null) {
            targetAnalyzer = "2";
        }
        targetMode = Integer.parseInt(targetAnalyzer);

//        System.out.println("LIST MODE is " + targetMode);
        if (type == null) {
            type = "Mapping";
        }

        String xmlId = type + id + ".xml";
        DBCollection dbc = new DBCollection(super.DBURI, applicationCollection + "/" + type, super.DBuser, super.DBpassword);
        String collectionPath = getPathforFile(dbc, xmlId, id);
        DBFile mappingFile = new DBFile(DBURI, collectionPath, xmlId, DBuser, DBpassword);
//        System.out.println("X="+xpath);
//        System.out.println(mappingFile);
        String[] values = mappingFile.queryString(xpath + "/string()");
        String currentValue = "";
        if (values.length > 0) {
            currentValue = values[0];
        }

//                int mode = 2; //1 for DOM, 2 for eXist, 3 for Jena
//        String[] targetSchemas = mappingFile.queryString("//target_info/target_schema/@schema_file/string()");
        String[] targetSchemas = mappingFile.queryString("for $i in //target_info/target_schema\n"
                + "return\n"
                + "if ($i/@schema_file) \n"
                + "then $i/@schema_file/string()\n"
                + "else \n"
                + "\"\"");

        ArrayList<String> resultsList = new ArrayList<String>();
        String[] results;

//        out.println("[{'':[{'id':'','text':''}],"); //Adding empty default
        String output = "";
        if (targetSchemas.length > 0) {
            if (targetMode == 1) {
//                resultsList = getListValues(mappingFile, id, xpath, targetSchemas);

            } else if (targetMode == 2) {
                String[] prefixes = mappingFile.queryString("//namespace/@prefix/string()");
                String[] uris = mappingFile.queryString("//namespace/@uri/string()");

                HashMap<String, String> filenameAndPrefix = new HashMap<String, String>();
                HashMap<String, String> filenameAndType = new HashMap<String, String>();
                HashMap<String, String> filenameAndURI = new HashMap<String, String>();

                for (int i = 0; i < targetSchemas.length; i++) {
                    String targetSchema = targetSchemas[i];
                    String[] targetSchemaTitles = mappingFile.queryString("//target_info/target_schema/string()");
                    String targetSchemaTitle = targetSchemaTitles[i];

                    String prefix = "";
                    String uri = "";
                    if (i + 2 < prefixes.length) {
                        prefix = prefixes[i + 2];
                        uri = uris[i + 2];
                    } else {
                        System.out.println("WARNING! Schema files-namespaces mismatch!");
                    }

                    filenameAndType.put(targetSchema, targetSchemaTitle);
                    filenameAndPrefix.put(targetSchema, prefix);
                    filenameAndURI.put(targetSchema, uri);
//                    System.out.println("**************");
//                    System.out.println(targetSchemaTitle);
//                    System.out.println(filenameAndPrefix);
//                    System.out.println(filenameAndType);
//                    System.out.println("**************");

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
                    output = tableToJSON(currentValue, resultsList, filenameAndType, filenameAndPrefix, filenameAndURI);
                }

//                                System.out.println("OUTPUT="+output);
//                    if (i < targetSchemas.length - 1) {
//                        out.println(" }, {");
//                    }
            } else if (targetMode == 3) {//

                HttpSession session = sessionCheck(request, response);
                if (session == null) {
                    session = request.getSession();

                }
                OntologyReasoner ont = (OntologyReasoner) session.getAttribute("modelInstance_" + id);

                if (ont == null) {
                    ont = getOntModel(mappingFile, id);
                    session.setAttribute("modelInstance_" + id, ont);
                }

                resultsList = getListValues(mappingFile, xpath, ont);
//                System.out.println("MODE3 LENGTH is " + resultsList.size());
//                System.out.println(resultsList);
            }
        }

        if (targetMode != 2) {
            if (resultsList.size() > 0) {
                String[] prefixes = mappingFile.queryString("//namespace/@prefix/string()");
                String[] uris = mappingFile.queryString("//namespace/@uri/string()");
                HashMap<String, String> prefixAndURI = new HashMap<String, String>();

                for (int i = 0; i < prefixes.length; i++) {
                    prefixAndURI.put(uris[i], prefixes[i]);

                }
                output = tableToJSON(resultsList, prefixAndURI);
//                System.out.println("OUTPUT="+output);
            }
        }

        out.println("{\n"
                + "    \"results\": [\n"
                + "            { \"id\": \"\", \"text\": \"\" }");//Adding empty default
        if (!output.equals("")) {
            out.println(",\n" + output);
        }

        out.println("]\n"
                + "}");

//        System.out.println(out);
        out.close();

    }

    //MODE 3
    private ArrayList<String> getListValues(DBFile mappingFile, String xpath, OntologyReasoner ont) {
//        System.out.println("MODE3 path = " + xpath);
        ArrayList<String> resultsList = null;

        if ((xpath.contains("/domain/target_node/entity[") && !xpath.contains("/relationship")) || ((xpath.contains("type[") && !xpath.contains("type[1]")))) {
            resultsList = ont.getAllClasses();
        } else if (xpath.contains("/relationship")) {
            xpath = xpathResolver(xpath, "relationship");

            String[] selectedEntities = mappingFile.queryString(xpath);
//            System.out.println("LENGTH is =" + selectedEntities.length);
            if (selectedEntities.length > 0) {
                if (selectedEntities.length == 1 && selectedEntities[0].equals("")) {
                    resultsList = new ArrayList<String>();
                } else {
                    String selection = selectedEntities[0];
                    selection = replacePrefixWithURI(mappingFile, selection);
//                    System.out.println("Selection is="+selection);
//                    System.out.println(ont);
                    resultsList = ont.listProperties(selection);
                }
            } else {
                resultsList = new ArrayList<String>();
            }

        } else if (xpath.contains("/entity")) {
            xpath = xpathResolver(xpath, "entity");

            String[] selectedProperty = mappingFile.queryString(xpath);
            if (selectedProperty.length == 0 && xpath.contains("internal_node")) { //No internal_node in P row
                selectedProperty = mappingFile.queryString(xpath.replace("internal_node[last()]/", ""));
            }
            if (selectedProperty.length > 0) {
                if (selectedProperty.length == 1 && selectedProperty[0].equals("")) {
                    resultsList = new ArrayList<String>();

                } else {
                    String path = selectedProperty[0];
//                    System.out.println("SEL PATH=" + path);
                    path = replacePrefixWithURI(mappingFile, path);

                    resultsList = ont.listObjects(path);

                }
            } else {
                resultsList = new ArrayList<String>();

            }

        } else {
            resultsList = ont.getAllClasses();
        }
        return resultsList;
    }

    private String replacePrefixWithURI(DBFile mappingFile, String selection) {
        if (selection.contains(":")) {
            String prefix = selection.split(":")[0];
            String[] uris = mappingFile.queryString("//namespace[@prefix='" + prefix + "']/@uri/string()");
            if (uris.length > 0) {
                selection = selection.replaceFirst(prefix + ":", uris[0]);
            }
        }
        return selection;
    }

//    private ArrayList<String> getListValues(DBFile mappingFile, String id, String xpath, String[] schemaFilenames) {
//
//        TargetSchemaTools schema = new TargetSchemaTools(schemaFilenames, uploadsFolder + id + "/");
//
//        ArrayList<String> resultsList = null;
//
//        if (xpath.contains("/domain/target_node/entity/type")) {
//            resultsList = schema.getAllClasses();
//        } else if (xpath.contains("/relationship")) {
//            xpath = xpathResolver(xpath, "relationship");
//
//            String[] selectedEntities = mappingFile.queryString(xpath);
//            if (selectedEntities.length > 0) {
//                if (selectedEntities.length == 1 && selectedEntities[0].equals("")) {
//                    resultsList = new ArrayList<String>();
//                } else {
//                    resultsList = schema.getAcceptedValuesFor(selectedEntities[0]);
//                }
//            } else {
//                resultsList = new ArrayList<String>();
//            }
//
//        } else if (xpath.contains("/entity")) {
//            xpath = xpathResolver(xpath, "entity");
//
//            String[] selectedProperty = mappingFile.queryString(xpath);
//            if (selectedProperty.length == 0 && xpath.contains("internal_node")) { //No internal_node in P row
//                selectedProperty = mappingFile.queryString(xpath.replace("internal_node[last()]/", ""));
//            }
//            if (selectedProperty.length > 0) {
//                if (selectedProperty.length == 1 && selectedProperty[0].equals("")) {
//                    resultsList = new ArrayList<String>();
//
//                } else {
//                    String path = selectedProperty[0];
//                    resultsList = schema.getAcceptedValuesFor(path);
//                }
//            } else {
//                resultsList = new ArrayList<String>();
//
//            }
//
//        } else {
//            resultsList = schema.getAllClasses();
//        }
//        return resultsList;
//    }
    private String xpathResolver(String xpath, String type) {
//        System.out.println("3MINPUTPATH=" + xpath);

        if (type.equals("relationship")) {
            if (xpath.contains("/additional[")) {
                xpath = xpath.replaceAll("/additional\\[[^$]+", "/type/string()");
            } else if (xpath.endsWith("/relationship[1]") && !xpath.contains("domain/")) {
                xpath = xpath.replaceAll("/link\\[[^$]+", "/domain/target_node/entity/type/string()");
//            } else if (xpath.contains("/additional[")) {
//                xpath = xpath.replace("/relationship", "/preceding-sibling::*[1]//type/string()");
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
//        System.out.println("#MOUTPATH2=" + xpath);
        return xpath;
    }

    //MODE 2
    private String[] getListValues(DBFile mappingFile, String xpath, String[] schemaFilenames, HttpSession session) {
        String currentSchemaFilename = schemaFilenames[0];
        if (schemaFilenames[0].startsWith("../")) {
            currentSchemaFilename = schemaFilenames[0].substring(3);
        }
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
        System.out.println("XPATH=" + xpath);

        if ((xpath.contains("/domain/target_node/entity[") && !xpath.contains("/relationship") && !xpath.contains("/additional")) || ((xpath.contains("type[") && !xpath.contains("type[1]")))) {
//            if (resourcesList != null && resourcesList.containsKey(schemas.toString() + currentSchemaFilename)) {
//                System.out.println(schemas.toString() + currentSchemaFilename);
//                        System.out.println("1");
//
//                resultsList = resourcesList.get(schemas.toString() + currentSchemaFilename);
//                System.out.println(resultsList.length);
//            } else {
            resultsList = cidocSchema.getAllClasses();
//                System.out.println("XX");
//                System.out.println(schemas.toString());
//                System.out.println(currentSchemaFilename);
//
//                System.out.println(resultsList);
            if (resourcesList == null) {
                resourcesList = new HashMap<String, String[]>();
            }
            resourcesList.put(schemas.toString() + currentSchemaFilename, resultsList);
            session.setAttribute("resourcesList", resourcesList);
//            }
        } else if (xpath.contains("/relationship")) {
//            System.out.println("2");

            xpath = xpathResolver(xpath, "relationship");

            String[] selectedEntities = mappingFile.queryString(xpath);

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
//                System.out.println(resourcesList);
                if (resourcesList == null) {
                    resourcesList = new HashMap<String, String[]>();
                }
                resourcesList.put(schemas.toString() + currentSchemaFilename + selEnts.toString(), resultsList);
                session.setAttribute("resourcesList", resourcesList);

            }
        } else if (xpath.contains("/entity")) {
//            System.out.println("X="+xpath);
            xpath = xpathResolver(xpath, "entity");

            String[] selectedProperty = mappingFile.queryString(xpath);
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
                if (resourcesList == null) {
                    resourcesList = new HashMap<String, String[]>();
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
                    for (Map.Entry<String, String> entry : filenameAndURI.entrySet()) {
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

//        System.out.println(filenameAndPrefix);
        for (Map.Entry<String, ArrayList> entry : valuesBySchema.entrySet()) {
            String key = entry.getKey();
            ArrayList<String> value = entry.getValue();
//            System.out.println("KEY=" + key);
//            System.out.println("VAL=" + value);
            value = new Utils().sort(value);

            String prefix = "";
            String type = "";
            //Replace filename with friendly name
            if (key.startsWith("cidoc_crm")) {
                type = filenameAndType.get(key);
                if (type == null) {
                    key = "../" + key;
                    type = filenameAndType.get(key);
                }
                prefix = filenameAndPrefix.get(key);

            } else {
//                System.out.println("ACTUAL KEY=" + key);
                prefix = filenameAndPrefix.get(key);
                type = filenameAndType.get(key);
            }
//            +"        { \"text\": \"Western\", \"children\": [\n"
//                    + "            { \"id\": \"CA\", \"text\": \"California\" },\n"
//                    + "            { \"id\": \"AZ\", \"text\": \"Arizona\" }\n"
//                    + "        ] },\n" 
//            output.append("'").append(type).append("': [");
            output.append("{ \"text\": \"").append(type).append("\", \"children\": [\n");

            for (String val : value) {
                String strippedURL = val;
                if (val.startsWith("http://")) { //If URL then strip slashes part
                    strippedURL = val.substring(val.lastIndexOf("/") + 1);

//                    output.append("{'id':'").append(val).append("',");
                    output.append("{\"id\":\"").append(val).append("\",");

                    if (selectedValue.equals(val)) {
                        // output.append("\n \"selected\":true").append("\",");
                    }

                } else {
                    output.append("{\"id\":\"").append(prefix + ":" + val).append("\",");
                    if (selectedValue.equals(prefix + ":" + val)) {
                        //   output.append("\n \"selected\":true").append("\",");
                    }
                }
//                output.append("\n'name':'").append(strippedURL).append("'},");
                output.append(" \"text\":\"").append(strippedURL).append("\"},");

            }
            output = output.delete(output.length() - 1, output.length());
            //       output.append("],");
            output.append("]},");
        }
//        System.out.println("XM="+output.toString());
//        System.out.println(output.length());
//        
        String out = "";
        if (output.length() > 0) {
            out = output.substring(0, output.length() - 1);
        }
//        System.out.println("OUT="+out);
//        String out = "";

        return out;

    }

    private String tableToJSON(ArrayList<String> table, HashMap<String, String> prefixAndURI) {
        StringBuilder output = new StringBuilder();

        for (String res : table) {
            String strippedURL = res;
            if (res.startsWith("http://")) { //If URL then strip slashes part
                strippedURL = res.substring(res.lastIndexOf("/") + 1);

                for (String uri : prefixAndURI.keySet()) {
                    if (uri.length() > 0) {
                        if (res.startsWith(uri)) {
                            res = res.replace(uri, prefixAndURI.get(uri) + ":");
                            continue;
                        }
                    }
                }

            }
            output.append("{\"id\":\"").append(res).append("\",");
            output.append("\n\"text\":\"").append(strippedURL).append("\"},");
        }

        String out = output.substring(0, output.length() - 1);
        return out;

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
