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
package isl.FIMS.servlet.ui;

import isl.FIMS.servlet.ApplicationBasicServlet;
import isl.FIMS.utils.ApplicationConfig;
import static isl.FIMS.utils.ParseXMLFile.parseFile;
import isl.FIMS.utils.entity.Config;
import isl.FIMS.utils.Utils;
import isl.dbms.eXist.ExistCollection;
import isl.dms.*;
import isl.dms.xml.XMLTransform;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import javax.servlet.*;
import javax.servlet.http.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmldb.api.base.Database;

public class Storage extends ApplicationBasicServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * @throws EXistException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ExistCollection Col = new ExistCollection(this.DBURI, this.systemDbCollection, this.DBuser, this.DBpassword);
        Database database = null;

        StringBuffer resultsTag = new StringBuffer();
        String prefix = "backup";
        String eXistBackups = this.BackupsFile;
        File BackupDirectory = new File(eXistBackups);
        File directory;
        String backupName = "";
        String xsl = "";
        String displayMsg = "", errorMsg = "", errorImg = "";
        this.initVars(request);

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        request.setCharacterEncoding("UTF-8");

        String menu = request.getParameter("menu");
        String action = request.getParameter("action");
        String file = request.getParameter("id");
        String menuId = request.getParameter("menuId");
        if (action == null) {
            action = "";
        }

        this.initVars(request);

        Document doc = parseFile(ApplicationConfig.SYSTEM_ROOT + "formating/multi_lang.xml");
        Element root = doc.getDocumentElement();
        Element contextTag = (Element) root.getElementsByTagName("context").item(0);




        Config conf = new Config(menuId);
        if (action.equals("backup")) {

            Element succ_Backup_Message = (Element) contextTag.getElementsByTagName("succ_Backup_Message").item(0);
            displayMsg = succ_Backup_Message.getElementsByTagName(this.lang).item(0).getTextContent();

            Element error_Backup_Message = (Element) contextTag.getElementsByTagName("error_Backup_Message").item(0);
            errorMsg = error_Backup_Message.getElementsByTagName(this.lang).item(0).getTextContent();

            Element error_BackupImg_Message = (Element) contextTag.getElementsByTagName("error_BackupImg_Message").item(0);
            errorImg = error_BackupImg_Message.getElementsByTagName(this.lang).item(0).getTextContent();


            if (!BackupDirectory.exists()) {
                BackupDirectory.mkdirs();
            }
            backupName = Utils.GenerateBackupFileName(this.username, this.getTime(), this.getDate(), prefix, this.picSeperator);
            Col.backup(this.DBuser, this.DBpassword, eXistBackups + backupName, this.DBURI + this.systemDbCollection, this.DBURI, this.systemDbCollection);
            ////////backup images
            File BackupFolder = new File(eXistBackups + backupName);

            if (BackupFolder.exists()) {
                File imageBackup = new File(BackupFolder.getAbsolutePath() + "/Archive");
                File uploads = new File(this.systemUploads + "Archive");

                if (!imageBackup.exists()) {
                    imageBackup.mkdirs();
                }

                try {
                    Utils.copy(imageBackup, uploads);
                } catch (Exception e) {
                    e.printStackTrace();
                    out.write(errorImg);
                }
                out.write(displayMsg);
            }
//                    
        } else if (action.equals("list")) {
            resultsTag.append("<space>").append(Utils.checkDiskSpace(new File(this.systemUploads).getAbsolutePath(), 50)).append("</space>\n");
            resultsTag.append("<outputs>\n");
            resultsTag.append("<heading>").append("username").append("</heading>\n").append("<heading>").append("date").append("</heading>\n").append("<heading>").append("time").append("</heading>\n");
            resultsTag.append("</outputs>\n");

            resultsTag.append("<results>\n");
            File[] allFiles = BackupDirectory.listFiles();

            for (int i = 0; i < allFiles.length; i++) {

                String fileName = allFiles[i].getName();
                if (fileName.startsWith(prefix) && allFiles[i].isDirectory()) {
                    String userName = fileName.split(this.picSeperator)[1];
                    String date = fileName.split(this.picSeperator)[3];
                    date = date.substring(6, 8) + "/" + date.substring(4, 6) + "/" + date.substring(0, 4);
                    String time = fileName.split(this.picSeperator)[2];
                    time = time.substring(0, 2) + ":" + time.substring(2, 4) + ":" + time.substring(4, 6);
                    resultsTag.append("<result>\n").append("<filename>").append(fileName).append("</filename>\n").append("<username>").append(userName).append("</username>\n").append("<date>").append(date).append("</date>\n").append("<time>").append(time).append("</time>\n").append("</result>\n");
                }
            }

            resultsTag.append("\n</results>\n");
            xsl = conf.RESTORE_XSL;
        } else if (action.equals("restore")) {

            Element succ_Restore_Message = (Element) contextTag.getElementsByTagName("succ_Restore_Message").item(0);
            displayMsg = succ_Restore_Message.getElementsByTagName(this.lang).item(0).getTextContent();

            Element error_Restore_Message = (Element) contextTag.getElementsByTagName("error_Restore_Message").item(0);
            errorMsg = error_Restore_Message.getElementsByTagName(this.lang).item(0).getTextContent();

            Element error_RestoreImg_Message = (Element) contextTag.getElementsByTagName("error_RestoreImg_Message").item(0);
            errorImg = error_RestoreImg_Message.getElementsByTagName(this.lang).item(0).getTextContent();
            this.deleteCollectionsBeforeRestore();

            directory = new File(eXistBackups + file + this.systemDbCollection + "__contents__.xml");
            Col.restore(this.DBuser, this.DBpassword, null, directory, this.DBURI, this.systemDbCollection);
            //restore images
            File imageBackup = new File(eXistBackups + file + "/Archive");
            File uploads = new File(this.systemUploads + "Archive");

            if (!imageBackup.exists()) {
                imageBackup.mkdirs();
            }

            try {
                Utils.copy(uploads, imageBackup);
            } catch (Exception e) {
                e.printStackTrace();
                out.write(errorImg);
            }
            out.write(displayMsg);


        } else if (action.equals("delete")) {
            Utils.deleteDir(eXistBackups + file);
            response.sendRedirect("Storage?action=list&menuId=" + menuId);

        }


        request.getSession().setAttribute("topmenu", menu);

        StringBuffer xml = new StringBuffer(this.xmlStart(menu, this.username, this.pageTitle, this.lang, "", request));

        xml.append("<EntityType>").append(conf.ENTITY_TYPE).append("</EntityType>\n");
        xml.append("<query>\n").append(resultsTag).append("</query>\n");
        xml.append("<Display>").append(displayMsg).append("</Display>\n");
        xml.append("<Action>").append(action).append("</Action>\n");
        xml.append("<Welcome>").append("yes").append("</Welcome>\n");

        xml.append(this.xmlEnd());


        if (action.equals("list")) {
            try {
                XMLTransform xmlTrans = new XMLTransform(xml.toString());
                xmlTrans.transform(out, xsl);
            } catch (DMSException e) {
                e.printStackTrace();
            }
        }

        out.close();

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * @throws EXistException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * @throws EXistException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    public static String getDate() {

        Calendar cal = new GregorianCalendar(Locale.getDefault());
        DecimalFormat myformat = new DecimalFormat("00");

        // Get the components of the date
        // int era = cal.get(Calendar.ERA);               // 0=BC, 1=AD
        int year = cal.get(Calendar.YEAR);             // 2002
        int month = cal.get(Calendar.MONTH) + 1;           // 0=Jan, 1=Feb, ...
        int day = cal.get(Calendar.DAY_OF_MONTH);      // 1...

        //   int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK); // 1=Sunday, 2=Monday

        //return new String(myformat.format(day)+"-"+myformat.format(month)+"-"+year);
        return new String(year + myformat.format(month) + myformat.format(day));
    }

    /**
     * Time method
     *
     * @return Current time as <CODE>String</CODE> in hh:mm:ss format
     */
    public static String getTime() {
        Calendar cal = new GregorianCalendar(Locale.getDefault());

        // Get the components of the time
        //    int hour12 = cal.get(Calendar.HOUR);            // 0..11
        // Create the DecimalFormat object only one time.
        DecimalFormat myformat = new DecimalFormat("00");

        int hour24 = cal.get(Calendar.HOUR_OF_DAY);     // 0..23
        int min = cal.get(Calendar.MINUTE);             // 0..59
        int sec = cal.get(Calendar.SECOND);             // 0..59
        return new String(myformat.format(hour24) + myformat.format(min) + myformat.format(sec));
    }
}
