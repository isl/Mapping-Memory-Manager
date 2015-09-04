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
 * This file is part of the 3MEditor webapp of Mapping Memory Manager project.
 */
package isl.x3mlEditor.upload;

import isl.x3mlEditor.utilities.Utils;
import org.apache.commons.fileupload.FileItem;

import javax.servlet.http.HttpServletRequest;

public class RequestParser {

    private static String FILENAME_PARAM = "qqfile";
    private String filename;
    private FileItem uploadItem;

    private RequestParser() {
    }

    //2nd param is null unless a MPFR
    static RequestParser getInstance(HttpServletRequest request, MultipartUploadParser multipartUploadParser) throws Exception {

        RequestParser requestParser = new RequestParser();

        if (multipartUploadParser != null) {
            requestParser.uploadItem = multipartUploadParser.getFirstFile();
            requestParser.filename = multipartUploadParser.getFirstFile().getName();
            //IE sends full path, so we have to strip it...
            if (requestParser.filename.contains(System.getProperty("file.separator"))) {
                requestParser.filename = requestParser.filename.substring(requestParser.filename.lastIndexOf(System.getProperty("file.separator"))+1);
            }
//            System.out.println(requestParser.filename );

        } else {
            requestParser.filename = request.getParameter(FILENAME_PARAM);

        }

        //grab other params here...

        return requestParser;
    }

    public String getFilename() {
      //  System.out.println("EDW="+createUniqueFilename(filename));
        return createUniqueFilename(filename);
    }

    //only non-null for MPFRs
    public FileItem getUploadItem() {
        return uploadItem;
    }

    private String createUniqueFilename(String filename) {

        //Create a pseudorandom number

        long randomNumber = Math.round(Math.random() * 14000);
        Utils utils = new Utils();
        String datestamp = utils.getDate();
        String timestamp = utils.getTime();


        String justName = filename.substring(0, filename.lastIndexOf('.'));
        String fileExtension = filename.substring(filename.lastIndexOf('.') + 1);

        //And use all of the above parts to create a unique random file name
        //var uniquename = results[0] + timestamp + datestamp + random +"." + results[1];
        String uniquename = justName + "___"+datestamp + timestamp +"___"+ randomNumber + "." + fileExtension;

        return uniquename;
    }
}
