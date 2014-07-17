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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;
import schematools.CrmLoader;
import schematools.TargetPathSuggestionModule;

/**
 *
 * @author samarita
 */
public class TargetSchemaTools {

    CrmLoader CRML;

    public TargetSchemaTools(String[] schemaFiles, String schemaFilesFolderPath) {
        ArrayList<String> parserdfsFiles = new ArrayList<String>();

        for (String schemaFile : schemaFiles) {
            parserdfsFiles.add(schemaFilesFolderPath + schemaFile);
        }
        try {
            CRML = new CrmLoader(parserdfsFiles);
        } catch (XPathExpressionException ex) {
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<String> getAllClasses() {
        return sort(CRML.getAllClasses());
    }

    public ArrayList<String> getAcceptedValuesFor(String selectedValue) {
//        System.out.println(selectedValue);
        TargetPathSuggestionModule TPSM = new TargetPathSuggestionModule();
        ArrayList<String> values = TPSM.TargetPathSuggestionMethod(selectedValue, CRML);
        values = removeDuplicates(values);
//        System.out.println(values);
        return sort(values);
    }

    private ArrayList<String> removeDuplicates(ArrayList<String> list) {
        // add elements to al, including duplicates
        HashSet hs = new HashSet();
        hs.addAll(list);
        list.clear();
        list.addAll(hs);
        return list;
    }

    public ArrayList<String> sort(ArrayList<String> list) {

        try {
            Collections.sort(list, new Comparator<String>() {
                @Override
                public int compare(String x, String y) {
                    x = x.split("_")[0];
                    y = y.split("_")[0];
                    if (x.endsWith("i")) {
                        x = x.replace("i", "");
                    }
                    if (y.endsWith("i")) {
                        y = y.replace("i", "");
                    }
                     if (y.endsWith("b")) {
                        y = y.replace("b", ".2");
                    }
                      if (y.endsWith("a")) {
                        y = y.replace("a", ".1");
                    }
                        if (x.endsWith("b")) {
                        x = x.replace("b", ".2");
                    }
                         if (x.endsWith("a")) {
                        x = x.replace("a", ".1");
                    }
                    if (x.startsWith(y.substring(0, 1))) {
                        String xWithoutFirstLetter = x.substring(1);
                        String yWithoutFirstLetter = y.substring(1);

                        if (xWithoutFirstLetter.matches("[\\.0-9]+") && yWithoutFirstLetter.matches("[\\.0-9]+")) {
                            Double a = Double.parseDouble(xWithoutFirstLetter);
                            Double b = Double.parseDouble(yWithoutFirstLetter);
                            return a.compareTo(b);
                        } else {
                            return x.compareTo(y);
                        }

                    } else {
                        return x.compareTo(y);
                    }

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            return list;
        }
        return list;
    }
}
