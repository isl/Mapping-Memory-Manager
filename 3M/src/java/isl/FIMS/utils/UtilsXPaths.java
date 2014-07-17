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

/**
 *
 * @author konsolak
 */
public class UtilsXPaths {

    //uri filed path
    public static String getPathUriField(String type) {
        String path = "";
        if (type.equals("Mapping")) {
            path = "";
        } else {
            path = "";
        }
        return path;
    }

    public static String getPrimaryEntitiesInsertPath(String type) {
        String path = "";
        if (type.equals("Mapping")) {
            path = "/x3ml/info/title";
        } else {
            path = "";
        }
        return path;
    }

    //simple search output result
    public static String[] getOutputResultForSimpleSearch(String type) {
        String[] outputResult = null;
        if (type.equals("Mapping")) {
            outputResult = new String[6];
            outputResult[0] = "/x3ml/info/title";
            outputResult[1] = "/x3ml/info/source_info/source_schema";
            outputResult[2] = "/x3ml/info/target_info/target_schema";
            outputResult[3] = "/x3ml/info/general_description";
            outputResult[4] = "/x3ml/admin/creator";
            outputResult[5] = "/x3ml/admin/versions/versionId";
        } else {
            outputResult = new String[1];
        }
        return outputResult;

    }
    //simple search results

    public static String[] getOutpuTitleForSimpleSearch(String type) {
        String[] outputTitle = null;
        if (type.equals("Mapping")) {
            outputTitle = new String[6];
            outputTitle[0] = "Τίτλος";
            outputTitle[1] = "source_schema";
            outputTitle[2] = "target_schema";
            outputTitle[3] = "general_description";
            outputTitle[4] = "Δημιουγός";
            outputTitle[5] = "VersionId";
        } else {
            outputTitle = new String[1];
        }
        return outputTitle;
    }
    //title for translate and version criteria

    public static String getOutpuTitleForTranslateCriteria(String type) {
        String outputTitle = new String();
        if (type.equals("Mapping")) {
            outputTitle = "";
        }
        return outputTitle;
    }

    /**
     *
     * @param type
     * @return path for search at name at each entity
     */
    public static String getSearchXpathAtName(String type) {
        String xpath = "";
        if (type.endsWith("Mapping")) {
            xpath = "/x3ml/info/title";
        }
        return xpath;
    }

    public static StringBuffer[] getXpathQuery(StringBuffer[] query, String type) {
        StringBuffer outputsTag = query[0];
        StringBuffer queryRet = query[1];
        StringBuffer queryOrderBy = query[2];

        if (type.equals("Mapping")) {
            outputsTag.append("<path xpath=\"xxx\" selected=\"yes\">").append("Τίτλος").append("</path>\n").append("<path xpath=\"xxx\" selected=\"yes\">").append("source_schema").append("</path>\n").append("<path xpath=\"xxx\" selected=\"yes\">").append("target_schema").append("</path>\n").append("<path xpath=\"xxx\" selected=\"yes\">").append("general_description").append("</path>\n").append("<path xpath=\"xxx\" selected=\"yes\">").append("Δημιουγός").append("</path>\n").append("<path xpath=\"xxx\" selected=\"yes\">").append("VersionId").append("</path>\n").append("<path xpath=\"xxx\" selected=\"yes\">").append("AdminStatus").append("</path>\n").append("<path xpath=\"xxx\" selected=\"no\">").append("Info").append("</path>\n");
            queryOrderBy.append("order by $i//title/text()\n");
            queryRet.append("return\n").append("<result pos=\"{$j}\">\n").append("<Τίτλος>\n{$current/x3ml/info/title}\n</Τίτλος>\n").append("<source_schema>\n{$current/x3ml/info/source_info/source_schema}\n</source_schema>\n").append("<target_schema>\n{$current//x3ml/info/target_info/target_schema}\n</target_schema>\n").append("<general_description>\n{$current//x3ml/info/general_description}\n</general_description>\n").append("<Δημιουγός>\n{$current//x3ml/admin/creator}\n</Δημιουγός>\n").append("<VersionId>\n{$current//x3ml/admin/versions/versionId}\n</VersionId>\n").append("<status>\n{$current//admin/status}\n</status>\n").append("<info>\n{$current//admin/info}\n</info>\n");
        } else {
            queryRet.append("return").append("<result pos=\"0\">");
        }
        query[0] = outputsTag;
        query[1] = queryRet;
        query[2] = queryOrderBy;
        return query;
    }
}
