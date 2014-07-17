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
package isl.x3ml.utilities;

import isl.dbms.DBCollection;
import isl.dbms.DBFile;
import java.util.ArrayList;

/**
 *
 * @author samarita
 */
public class Schema {

    DBCollection col;
    DBFile file;
    String namespaces, forPart;
    String[] filenames;

    public Schema(DBFile schemaFile) {
        file = schemaFile;

        namespaces = "declare namespace rdfs = \"http://www.w3.org/2000/01/rdf-schema#\";\n"
                + "declare namespace rdf = \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\";";

    }

    public Schema(DBFile schemaFile, String[] schemaFilenames) {
        file = schemaFile;
        col = schemaFile.getCollection();
        filenames = schemaFilenames;

        namespaces = "declare namespace rdfs = \"http://www.w3.org/2000/01/rdf-schema#\";\n"
                + "declare namespace rdf = \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\";";
        forPart = createForPart();

    }

//    public Schema(DBCollection schemaCol) {
//        col = schemaCol;
//
//        namespaces = "declare namespace rdfs = \"http://www.w3.org/2000/01/rdf-schema#\";\n"
//                + "declare namespace rdf = \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\";";
//
//    }
    public String[] getAllClasses() {
//        String query = namespaces + "\n//rdfs:Class/@rdf:about/string()";
        String query = namespaces + buildQuery("//rdfs:Class/@rdf:about");

//        System.out.println(query);
        String[] entities = file.queryString2(query);
        return entities;
    }

    /* Not used for now
     public String[] getAllProperties() {
     String query = namespaces + "\n//rdf:Property/@rdf:about/string()";
     String[] entities = file.queryString2(query);
     return entities;
     }
    
     public ArrayList<String> getSubPropertiesOf(String propertyName, ArrayList<String> propertiesNames) {
     if (!propertiesNames.contains(propertyName)) {
     propertiesNames.add(propertyName);
     }

     if (!propertyName.equals("")) {

     String query = "";
     String[] properties = null;
     if (filenames.length > 1) {
     query = namespaces + buildQuery("//rdf:Property[rdfs:subPropertyOf/@rdf:resource[ends-with(.,\"" + propertyName + "\")]]/@rdf:about");

     //                query = namespaces + forPart + "//rdf:Property[rdfs:subPropertyOf/@rdf:resource[ends-with(.,\"" + propertyName + "\")]]/@rdf:about/string()";
     properties = col.query(query);
     } else {
     query = namespaces + "\n//rdf:Property[rdfs:subPropertyOf/@rdf:resource[ends-with(.,\"" + propertyName + "\")]]/@rdf:about/string()";
     properties = file.queryString2(query);

     }
     for (String property : properties) {
     getSubClassesOf(property, propertiesNames);
     }
     }
     return propertiesNames;
     }
     */
    public ArrayList<String> getSubClassesOf(String className, ArrayList<String> classNames) {
        if (!classNames.contains(className)) {
            classNames.add(className);
        }

        if (!className.equals("")) {

            String query = "";
            String[] entities = null;
            if (filenames.length > 1) {
                if (className.contains("######")) {
                    className = className.split("######")[1];
                }
                query = namespaces + buildQuery("//rdfs:Class[rdfs:subClassOf/@rdf:resource[ends-with(.,\"" + className + "\")]]/@rdf:about");
//                System.out.println(query);
                entities = col.query(query);
            } else {
                query = namespaces + "\n//rdfs:Class[rdfs:subClassOf/@rdf:resource[ends-with(.,\"" + className + "\")]]/@rdf:about/string()";
                entities = file.queryString2(query);

            }

            for (String entity : entities) {
                getSubClassesOf(entity, classNames);
            }
        }
        return classNames;
    }

    public ArrayList<String> getSuperClassesOf(String className, ArrayList<String> classNames) {
        if (!classNames.contains(className)) {
            classNames.add(className);
        }

        if (!className.equals("")) {

            if (className.contains("/")) { //in case class has full URI name, strip it
                className = className.substring(className.lastIndexOf("/") + 1);
            }
            String query = "";
            String[] entities = null;
            if (filenames.length > 1) {

                query = namespaces + forPart + "//rdfs:Class[@rdf:about[ends-with(.,\"" + className + "\")]]/rdfs:subClassOf/@rdf:resource/string()";
//                System.out.println(query);

                entities = col.query(query);

            } else {
                query = namespaces + "\n//rdfs:Class[@rdf:about[ends-with(.,\"" + className + "\")]]/rdfs:subClassOf/@rdf:resource/string()";
                entities = file.queryString2(query);

            }
//            System.out.println(query);

            for (String entity : entities) {
//                System.out.println("SUPER ENT is " + entity);
                getSuperClassesOf(entity, classNames);
            }
        }
        return classNames;
    }

    public String getRangeForProperty(String property) {
        String query = "";
        String[] ranges = null;

        query = namespaces + buildQuery("//rdf:Property[@rdf:about[ends-with(.,\"" + property + "\")]]/rdfs:range/@rdf:resource");
        ranges = file.queryString2(query);
//        System.out.println(query);
        if (ranges != null && ranges.length > 0) {
            return ranges[0];
        } else {
            return "";
        }

    }

    public ArrayList<String> getPropertiesFor(ArrayList<String> classNames) {
        ArrayList<String> properties = new ArrayList<String>();
        String criterion = "";
        for (int i = 0; i < classNames.size(); i++) {
            criterion = criterion + "ends-with(.,\"" + classNames.get(i) + "\")";

            if (i < classNames.size() - 1) {
                criterion = criterion + " or ";
            }
        }
        String query = namespaces + buildQuery("//rdf:Property[rdfs:domain/@rdf:resource[" + criterion + "]]/@rdf:about");
        String[] propertyNames = file.queryString2(query);

        for (String property : propertyNames) {
            if (!properties.contains(property)) {
                properties.add(property);
            }
        }
//
//        }
//        properties = sort(properties);
//        Collections.sort(properties);
        return properties;

    }

    private String buildQuery(String xpath) {
        StringBuilder query = new StringBuilder("\nlet $i := ");
        for (String filename : filenames) {
            if (filename.startsWith("../")) {//Hack for CIDOC
                filename = filename.replace("../", "");
            }
            query = query.append("document('").append(filename).append("')|");

        }
        String forPart = "\nfor $j in $i" + xpath + "\norder by upper-case(util:document-name($j))\nreturn\n concat(util:document-name($j),'######',$j/string())";

        return query.substring(0, query.length() - 1) + forPart;

    }

    private String createForPart() {
        StringBuilder forPart = new StringBuilder("\nfor $i in ");
        for (String filename : filenames) {
            if (filename.startsWith("../")) {//Hack for CIDOC
                filename = filename.replace("../", "");
            }
            forPart = forPart.append("document('").append(filename).append("')|");

        }
        return forPart.substring(0, forPart.length() - 1) + "\nreturn $i";
    }

}
