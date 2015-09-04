/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isl.x3mlEditor;

import isl.dbms.DBCollection;
import isl.dbms.DBFile;
import static isl.x3mlEditor.BasicServlet.applicationCollection;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author samarita
 */
public class Add extends BasicServlet {

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
        String type = request.getParameter("type");
        String action = request.getParameter("action");
        String xsl = request.getParameter("xsl");
        String sibs = request.getParameter("sibs");
        String targetAnalyzer = request.getParameter("targetAnalyzer");
        String sourceAnalyzer = request.getParameter("sourceAnalyzer");

        int targetMode;
//        System.out.println(targetAnalyzer);
        if (targetAnalyzer == null || targetAnalyzer.equals("undefined")) {
            targetAnalyzer = "2";
        }
        targetMode = Integer.parseInt(targetAnalyzer);

        if (sourceAnalyzer == null) {
            sourceAnalyzer = "off";
        }

//        System.out.println(xpath);
//        System.out.println(action);
        if (type == null) {
            type = "Mapping";
        }

        String output = "";
        String xmlId = type + id + ".xml";
        DBCollection dbc = new DBCollection(super.DBURI, applicationCollection + "/" + type, super.DBuser, super.DBpassword);
        String collectionPath = getPathforFile(dbc, xmlId, id);
        DBFile mappingFile = new DBFile(DBURI, collectionPath, xmlId, DBuser, DBpassword);

        if (!xpath.startsWith("//")) {
            xpath = "//" + xpath;
        }
//                System.out.println("XPATH=" + xpath);
//                System.out.println(action);
        if (action.startsWith("add")) {

            if (action.startsWith("addAttr")) {
                String attrName = request.getParameter("name");
                if (attrName == null) {
                    String fatherXpath = xpath.substring(0, xpath.lastIndexOf("/"));
//                    System.out.println("FATH="+fatherXpath);
                    attrName = xpath.substring(xpath.lastIndexOf("/") + 2);
//                    System.out.println(attrName);
                    mappingFile.xAddAttribute(fatherXpath, attrName, "");
                } else {
                    mappingFile.xAddAttribute(xpath, attrName, "");
                }
            } else if (action.startsWith("addOptional")) {
                String[] actionParts = action.split("___");
                String fullXpath = actionParts[1];
//                System.out.println("XPATH=" + fullXpath);
                String mappingFrag = addOptionalPart(mappingFile, fullXpath);
//                System.out.println(mappingFrag);
                if (mappingFrag.startsWith("<arg name") || mappingFrag.startsWith("<instance_generator") || mappingFrag.startsWith("<label_generator")|| mappingFrag.startsWith("<type") || mappingFrag.startsWith("<additional") || mappingFrag.startsWith("<intermediate") || mappingFrag.startsWith("<comment") || mappingFrag.startsWith("<source_intermediate") || mappingFrag.startsWith("<if>")) { //Edit mode
//                    System.out.println("SIBS=" + sibs);
                    if (sibs == null) {
                        sibs = "0";
                    }
                    int pos = Integer.parseInt(sibs) + 1;
                    String container = xpath.replaceAll("\\[last\\(\\)\\]", "");
                    xpath = xpath.replaceAll("last\\(\\)", "" + pos);
                    int relPos = pos + 1; //Only used for relationship in intermediate?
                    mappingFrag = mappingFrag.replaceFirst("/?>", " targetMode='" + targetMode + "' container='" + container + "' xpath='" + xpath + "' relPos='" + relPos + "'$0");

                    if (mappingFrag.startsWith("<type")) {
                        mappingFrag = "<entity>" + mappingFrag + "</entity>"; //dummy root to make it work with existing type.xsl
                    }

                }

                if (xsl != null) {
                    System.out.println("FINAL=" + mappingFrag);
                    if (mappingFrag.startsWith("<source")) {
                        mappingFrag = mappingFrag.replaceFirst("targetMode=", "sourceAnalyzer='" + sourceAnalyzer + "' targetMode=");
                        xsl = "source_intermediate.xsl";

                    }

                    if (xsl.equals("instance_generator.xsl") || xsl.equals("arg.xsl")||xsl.equals("label_generator.xsl")) {
                        output = transform(mappingFrag, super.baseURL + "/xsl/generators/" + xsl);
                    } else {
                        output = transform(mappingFrag, super.baseURL + "/xsl/edit/" + xsl);
                    }
//                    System.out.println("OUT=" + output);
                }

            } else {
//                DBFile templateFile = new DBFile(DBURI, x3mlCollection, "Template.xml", DBuser, DBpassword);
//                String templateAdd = "";
//                String[] mappingFrags = null;
//                if (action.contains("___")) { //Optional Part
//                    String[] actionParts = action.split("___");
//
//                    templateAdd = "//optionalPart/" + actionParts[1].trim();
//                    if (actionParts.length > 2) {
//                        templateAdd = templateAdd + "/*";
//                    }
//
//                } else {
//                templateAdd = xpath.replaceAll("\\[\\d+\\]", "");
//                }

//                System.out.println("TEMPLA=" + templateAdd);
//                mappingFrags = templateFile.queryString(templateAdd);
                String[] mappingFrags = getFragmentFromTemplate(xpath);
                String fatherXpath = xpath.substring(0, xpath.lastIndexOf("/"));
                for (String mappingFrag : mappingFrags) {
//                    System.out.println("MANDATORY_MAPPING_FRAG=" + mappingFrag);

                    if (action.startsWith("addBefore")) { //Inserts before specified element
                        mappingFile.xInsertBefore(xpath, mappingFrag);
                    } else if (action.startsWith("addAfter")) {//Inserts after specified element
                        mappingFile.xInsertAfter(xpath, mappingFrag);
                    } else {
                        mappingFile.xAppend(fatherXpath, mappingFrag); //Appends to father element (LAST POSITION!)
                    }
//                    System.out.println("SIBS=" + sibs);
//                    System.out.println("x=" + xpath);
                    if (sibs == null && xpath.endsWith("]")) { //Add Mapping case

                        sibs = xpath.substring(xpath.lastIndexOf("[") + 1, xpath.length() - 1);
                        xpath = xpath.replaceAll("\\[\\d+\\]", "");

                    }

                    if (sibs != null) {
                        int pos = Integer.parseInt(sibs) + 1;
                        if (mappingFrag.startsWith("<mapping>")) {
                            mappingFrag = mappingFrag.replaceFirst("<mapping", "<mapping targetMode='" + targetMode + "' xpath='" + xpath + "[" + pos + "]'");

                            mappingFrag = mappingFrag.replaceFirst("<domain", "<domain sourceAnalyzer='" + sourceAnalyzer + "' xpath='" + xpath + "[" + pos + "]/domain'");
                            mappingFrag = mappingFrag.replaceFirst("<path>", "<path sourceAnalyzer='" + sourceAnalyzer + "' xpath='" + xpath + "[" + pos + "]/link[1]/path" + "'>");
                            mappingFrag = mappingFrag.replaceFirst("<range>", "<range sourceAnalyzer='" + sourceAnalyzer + "' xpath='" + xpath + "[" + pos + "]/link[1]/range" + "'>");
                        } else if (mappingFrag.startsWith("<link>")) { //Edit mode
//                result = result.replaceFirst("<link>", "<link xpath='" + xpath + "'>");
                            mappingFrag = mappingFrag.replaceFirst("<path>", "<path sourceAnalyzer='" + sourceAnalyzer + "' targetMode='" + targetMode + "' xpath='" + xpath + "[" + pos + "]/path" + "'>");
                            mappingFrag = mappingFrag.replaceFirst("<range>", "<range sourceAnalyzer='" + sourceAnalyzer + "' targetMode='" + targetMode + "' xpath='" + xpath + "[" + pos + "]/range" + "'>");
                        } else if (mappingFrag.startsWith("<type")) { //Edit mode
                            String container = xpath.replaceAll("\\[last\\(\\)\\]", "");
                            xpath = xpath.replaceAll("last\\(\\)", "" + pos);
                            mappingFrag = mappingFrag.replaceFirst("/>", " targetMode='" + targetMode + "' container='" + container + "' xpath='" + xpath + "'/>");
                            mappingFrag = "<entity>" + mappingFrag + "</entity>"; //dummy root to make it work with existing type.xsl
                        } else {
                            mappingFrag = mappingFrag.replaceFirst(">", " pos='" + pos + "'>");
                        }
                    }
//                    System.out.println("MAP=" + mappingFrag);
                    if (xsl != null) {
                        output = transform(mappingFrag, super.baseURL + "/xsl/edit/" + xsl);
//                        System.out.println("XSL="+super.baseURL + "/xsl/edit/" + xsl);
//                                            System.out.println("MAPHTML=" + output);

                    }
                }

            }

            out.println(output);

        }
        out.close();
    }

    private String[] getFragmentFromTemplate(String xpath) {
        DBFile templateFile = new DBFile(DBURI, x3mlCollection, "3MTemplate.xml", DBuser, DBpassword);
//         if (action.contains("___")) { //Optional Part
//                    String[] actionParts = action.split("___");
//
//                    templateAdd = "//optionalPart/" + actionParts[1].trim();
//                    if (actionParts.length > 2) {
//                        templateAdd = templateAdd + "/*";
//                    }
//
//                } else {
//                templateAdd = xpath.replaceAll("\\[\\d+\\]", "");
//                }

        String templateAdd = xpath.replaceAll("\\[(\\d+|last\\(\\))\\]", "");

//        System.out.println("TEMPLA=" + templateAdd);
        String[] mappingFrags = templateFile.queryString(templateAdd);
        return mappingFrags;

    }

    private String fatherXpath(String xpath) {
        return xpath.substring(0, xpath.lastIndexOf("/"));
    }

    private String addOptionalPart(DBFile mappingFile, String fullXpath) {
//        HashMap<String, String> returnValue = new HashMap<String, String>();
//        String action = "";
        String frag = "";
        String fatherXpath = fatherXpath(fullXpath);
        String child = fullXpath.substring(fullXpath.lastIndexOf("/") + 1);
//        System.out.println("CHILD is " + child);

        if (child.equals("type[last()]")) { //Adding entity type in target_relation
            mappingFile.xInsertAfter(fullXpath, "<type/>");
            return "<type/>";

        } else if (child.equals("additional[last()]")) { //Adding additional
            String[] mappingFrags = getFragmentFromTemplate("//optionalPart/additional");
//            System.out.println(fullXpath);
            if (mappingFrags != null) {
                if (mappingFrags.length > 0) {
//                    System.out.println("MAPFRAG=" + mappingFrags[0]);
                    frag = mappingFrags[0];

                    if (mappingFile.queryString(fatherXpath + "/additional").length > 0) { //has additional
                        mappingFile.xInsertAfter(fullXpath, frag);
                    } else { //does not have additional
                        mappingFile.xAppend(fatherXpath, frag); //Appends to father element (LAST POSITION!)
                    }

                }
            }
            return frag;
//            mappingFile.xInsertAfter(fullXpath, "<type/>");
//            return "<type/>";
        } else if (child.equals("intermediate[last()]")) { //Adding intermediate
            String[] mappingFrags;
            if (fatherXpath.contains("source_relation")) {
                mappingFrags = getFragmentFromTemplate("//optionalPart/source_intermediate");
            } else {
                mappingFrags = getFragmentFromTemplate("//optionalPart/intermediate");
            }
//            System.out.println(fullXpath);
            if (mappingFrags != null) {
                if (mappingFrags.length > 0) {
//                    System.out.println("MAPFRAG=" + mappingFrags[0]);
                    frag = mappingFrags[0];

                    String noRootXML;
                    //Removing intermediate container
                    if (fatherXpath.contains("source_relation")) {
                        noRootXML = frag.replaceAll("</?source_intermediate>", "");
                    } else {
                        noRootXML = frag.replaceAll("</?intermediate>", "");
                    }
//                    System.out.println("MAPFRAG2=" + frag);

                    mappingFile.xAppend(fatherXpath, noRootXML); //Appends to father element (LAST POSITION!)

//                    if (mappingFile.queryString(fatherXpath + "/additional").length > 0) { //has additional
//                        mappingFile.xInsertAfter(fullXpath, frag);
//                    } else { //does not have additional
//                        mappingFile.xAppend(fatherXpath, frag); //Appends to father element (LAST POSITION!)
//                    }
                }
            }
            return frag;
//            mappingFile.xInsertAfter(fullXpath, "<type/>");
//            return "<type/>";
        } else if (child.equals("comments")) {
            String[] mappingFrags = getFragmentFromTemplate("//optionalPart/comments");
            if (mappingFrags != null) {
                if (mappingFrags.length > 0) {
//                    System.out.println("MAPFRAG=" + mappingFrags[0]);
//                    System.out.println(fatherXpath);
                    frag = mappingFrags[0];
                    mappingFile.xAppend(fatherXpath, frag); //Appends to father element (LAST POSITION!)
                }
            }
            return frag;

        } else if (child.equals("instance_generator")) {
            String[] mappingFrags = getFragmentFromTemplate("//optionalPart/instance_generator");
            if (mappingFrags != null) {
                if (mappingFrags.length > 0) {
                    frag = mappingFrags[0];

                    if (mappingFile.queryString(fatherXpath + "/instance_info").length > 0) { //has instance_info
                        mappingFile.xInsertAfter(fatherXpath + "/instance_info", frag); //Inserts after instance_info
                    } else {
                        mappingFile.xInsertAfter(fatherXpath + "/type[last()]", frag); //Inserts after last type
                    }

                }
            }
            return frag;
        } else if (child.equals("label_generator[last()]")) {
            String[] mappingFrags = getFragmentFromTemplate("//optionalPart/label_generator");
            if (mappingFrags != null) {
                if (mappingFrags.length > 0) {
                    frag = mappingFrags[0];

                    if (mappingFile.queryString(fatherXpath + "/instance_generator").length > 0) { //has instance_generator
                        mappingFile.xInsertAfter(fatherXpath + "/instance_generator", frag); //Inserts after instance_generator
                    } else if (mappingFile.queryString(fatherXpath + "/instance_info").length > 0) { //has instance_info
                        mappingFile.xInsertAfter(fatherXpath + "/instance_info", frag); //Inserts after instance_info
                    } else {
                        mappingFile.xInsertAfter(fatherXpath + "/type[last()]", frag); //Inserts after last type
                    }

                }
            }
            return frag;
        } else if (child.equals("arg[last()]")) {
            String[] mappingFrags = getFragmentFromTemplate("//optionalPart/arg");
            if (mappingFrags != null) {
                if (mappingFrags.length > 0) {
//                    System.out.println("MAPFRAG=" + mappingFrags[0]);
//                    System.out.println(fatherXpath);
                    frag = mappingFrags[0];
                    mappingFile.xAppend(fatherXpath, frag); //Appends to father element (LAST POSITION!)

                }
            }
            return frag;
        } else if (child.endsWith("quality") || child.endsWith("xistence") || child.endsWith("Narrowness")) {

//            System.out.println("FATHER=" + fatherXpath);
            String childPath = "";
            String operator = "";
            if (child.contains("_")) {
                String[] parts = child.split("_");
                operator = parts[0].toLowerCase();
                child = parts[1];
            }

            if (child.equals("Equality")) {
                childPath = "if[equals]";
            } else if (child.equals("Inequality")) {
                childPath = "if[not/if/equals]";
            } else if (child.equals("Existence")) {
                childPath = "if[exists]";
            } else if (child.equals("Nonexistence")) {
                childPath = "if[not/if/exists]";
            } else if (child.equals("Narrowness")) {
                childPath = "if[narrower]";
            }
            String[] mappingFrags = getFragmentFromTemplate("//optionalPart/" + childPath);
            if (mappingFrags != null) {
                if (mappingFrags.length > 0) {
//                    System.out.println("MAPFRAG=" + mappingFrags[0]);
//                    System.out.println("OPERATOR=" + operator);
                    frag = mappingFrags[0];
                    //Check if there are multiple rules
                    String[] lastIfRule = mappingFile.queryString(fatherXpath + "//if[name(..)!='not'][not or exists or equals or narrower][last()][not(following-sibling::if)]");
//                    String[] orAndRules = mappingFile.queryString(fatherXpath + "//*[name()='or' or name()='and']");
                    if (lastIfRule != null && lastIfRule.length > 0) {
//                        mappingFile.xAppend(fatherXpath + "/if/*[name()='or' or name()='and']", frag); //Appends to father element (LAST POSITION!)
                        String[] lastIfRuleFather = mappingFile.queryString(fatherXpath + "//if[name(..)!='not'][not or exists or equals or narrower][last()][not(following-sibling::if)]/../name()");
                        if (lastIfRuleFather != null && lastIfRuleFather.length > 0) {
//                            System.out.println("LASTIFRULEFATHER=" + lastIfRuleFather[0]);

                            if (lastIfRuleFather[0].equals("or") || lastIfRuleFather[0].equals("and")) { //Already has operator
                                if (lastIfRuleFather[0].equals(operator)) {
                                    mappingFile.xInsertAfter(fatherXpath + "//if[name(..)!='not'][not or exists or equals or narrower][last()][not(following-sibling::if)]", frag); //Needs further refining
                                } else {
                                    //TODO (add inside or or vice versa)
                                    mappingFile.xUpdate(fatherXpath + "//if[name(..)!='not'][not or exists or equals or narrower][last()][not(following-sibling::if)]", "<" + operator + ">" + lastIfRule[0] + "</" + operator + ">");
                                    mappingFile.xInsertAfter(fatherXpath + "//if[name(..)!='not'][not or exists or equals or narrower][last()][not(following-sibling::if)]", frag); //Needs further refining

                                }
                            } else { //Single if, have to enclose with operator
//                                System.out.println("MUST ENCLOSE");
//                                System.out.println(lastIfRule[0]);
                                mappingFile.xUpdate(fatherXpath + "//if[name(..)!='not'][not or exists or equals or narrower][last()][not(following-sibling::if)]", "<" + operator + ">" + lastIfRule[0] + "</" + operator + ">");
                                mappingFile.xInsertAfter(fatherXpath + "//if[name(..)!='not'][not or exists or equals or narrower][last()][not(following-sibling::if)]", frag); //Needs further refining
                            }
                        } else {
                            mappingFile.xInsertAfter(fatherXpath + "//if[name(..)!='not'][not or exists or equals or narrower][last()][not(following-sibling::if)]", frag); //Needs further refining
                        }

                    } else {
//                        System.out.println("2");
                        mappingFile.xInsertBefore(fatherXpath + "/*[1]", frag); //Appends to father element (LAST POSITION!)
                        //    String[] orAndRules = mappingFile.queryString(fatherXpath + "//*[name()='or' or name()='and']");
//                        mappingFile.xInsertBefore(fatherXpath + "/*[1]", frag); //Insert before first element

                    }
//                    System.out.println(fatherXpath);
//                    
//                    mappingFile.xAppend(fatherXpath, frag); //Appends to father element (LAST POSITION!)
                }
            }

            //New approach (return entire if block instead)
            String[] entireIfBlock = mappingFile.queryString(fatherXpath + "/if");
            if (entireIfBlock != null && entireIfBlock.length > 0) {
                return entireIfBlock[0];
            } else {
                return frag;
            }
        } else {

            String[] instanceInfoChildren = mappingFile.queryString(fatherXpath + "/*");
            if (instanceInfoChildren.length > 0) { //has instance_info and at least one child
//                System.out.println("exei paidia");
                if (child.equals("language")) {
                    mappingFile.xInsertBefore(fatherXpath + "/*", "<" + child + "/>"); //Inserts as first child
                } else if (child.equals("description")) {
                    mappingFile.xAppend(fatherXpath, "<" + child + "/>"); //Appends to father element (LAST POSITION!)
                } else { //constant
                    if (mappingFile.queryString(fatherXpath + "/description").length > 0) { //has description
                        mappingFile.xInsertBefore(fatherXpath + "/description", "<" + child + "/>"); //Inserts before description
                    } else {
                        mappingFile.xAppend(fatherXpath, "<" + child + "/>"); //Appends to father element (LAST POSITION!)
                    }
                }
            } else {
                String[] instanceInfo = mappingFile.queryString(fatherXpath);
                if (instanceInfo.length > 0) { //has instance_info without children                
                    mappingFile.xAppend(fatherXpath, "<" + child + "/>"); //Appends to father element (LAST POSITION!)
                } else { //has NOT instance_info
                    mappingFile.xInsertAfter(fatherXpath(fatherXpath) + "/type[last()]", "<instance_info><" + child + "/></instance_info>");

                }
            }
            return "";
        }

//        if (fullXpath.endsWith("instance_info/language")) {
//
//        }
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
