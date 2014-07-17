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
package isl.FIMS.listener;

import java.io.FileOutputStream;
import java.util.Date;
import javax.servlet.http.*;

/**
 *
 * @author samarita
 * @version
 *
 * Web application lifecycle listener.
 */
public class SessionListener implements HttpSessionListener, HttpSessionAttributeListener {

    public static int sessionsNO = 0;
    public static int activesessionsNO = 0;
    //  public static int dbsessionsNO = 0;
    public static String lastevent;
    // Log file mechanism
    static FileOutputStream LogFile;
    static String LogFilePath = isl.FIMS.servlet.ApplicationBasicServlet.returnLogfilePath();
    static String CreatedStr = "<font face=\"arial\" color=\"green\">created</font>";
    static String DestroyedStr = "<font face=\"arial\" color=\"red\">destroyed</font>";
    static String LoggedInStr = "<font face=\"arial\" color=\"green\">logged IN</font>";
    static String LoggedOutStr = "<font face=\"arial\" color=\"red\">logged OUT</font>";
    private String sessionId;

    public SessionListener() {
        super();

        // get the log file path


        // inform log file
        String LogMessage = "</br><b>WEB SERVER OPENED</b> at: " + (new Date(System.currentTimeMillis())).toString() + "</br>";
        LogMessage += "-------------------------------------------------------------------------------------------</br>";
        InformLogFile(LogMessage);
    }

    /**
     * ### Method from HttpSessionListener ###
     *
     * Called when a session is created.
     *
     * @param se
     */
    public void sessionCreated(HttpSessionEvent se) {

        HttpSession session = se.getSession();
        ++sessionsNO;
        ++activesessionsNO;

        String currentDate = (new Date(System.currentTimeMillis())).toString();
        lastevent = "Session " + session.getId() + " Created on " + currentDate;

        // inform log file
        String LogMessage = "**************************************************</br>";
        LogMessage += "Session <b>" + session.getId() + "</b> " + CreatedStr + " on " + currentDate + "</br>";
        // LogMessage += activesessionsNO+" Active Sessions<br/>";
        InformLogFile(LogMessage);


    }

    /**
     * ### Method from HttpSessionListener ###
     *
     * Called when a session is destroyed(invalidated).
     *
     * @param se
     */
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();

        if (activesessionsNO > 0) {
            --activesessionsNO;
        }
        String currentDate = (new Date(System.currentTimeMillis())).toString();
        lastevent = "Session " + session.getId() + " destroyed on " + currentDate;
        // inform log file
        String LogMessage = "Session <b>" + session.getId() + "</b> " + DestroyedStr + " on " + currentDate + "</br>";
        InformLogFile(LogMessage);
    }

    /**
     * ### Method from HttpSessionAttributeListener ###
     *
     * Called when an attribute is added to a session.
     *
     * @param be
     */
    public void attributeAdded(HttpSessionBindingEvent be) {


        String currentDate = (new Date(System.currentTimeMillis())).toString();
//        lastevent = be.getName() + " with id " + be.getValue() + " opened on " + currentDate;
        String sId = be.getSession().getId();


        // inform log file
        if ((be.getName()).compareTo("username") == 0) {
            String LogMessage = "User <b> " + be.getValue() + "</b> (" + sId + ") " + LoggedInStr + " on " + currentDate + "</br>";
            InformLogFile(LogMessage);
        }
        if ((be.getName()).compareTo("userIP") == 0) {
            String LogMessage = "User's IP is <b>" + be.getValue() + "</b> (" + sId + ")</br>";
            InformLogFile(LogMessage);
        }
        if ((be.getName()).compareTo("topmenu") == 0) {
            String LogMessage = "Topmenu value is <b>" + be.getValue() + "</b> (" + sId + ")</br>";
            InformLogFile(LogMessage);
        }
        if ((be.getName()).compareTo("lang") == 0) {
            String LogMessage = "Selected language is <b>" + be.getValue() + "</b> (" + sId + ")</br>";

            InformLogFile(LogMessage);
        }
        if ((be.getName()).compareTo("browser") == 0) {
            String LogMessage = "Used browser is <b>" + be.getValue() + "</b> (" + sId + ")</br>";

            InformLogFile(LogMessage);
        }
    }

    /**
     * ### Method from HttpSessionAttributeListener ###
     *
     * Called when an attribute is removed from a session.
     *
     * @param be
     */
    public void attributeRemoved(HttpSessionBindingEvent be) {

        String currentDate = (new Date(System.currentTimeMillis())).toString();
        lastevent = be.getName() + " with id " + be.getValue() + " closed on " + currentDate;
        String sId = be.getSession().getId();
        // inform log file
        if ((be.getName()).compareTo("username") == 0) {
            String LogMessage = "User <b>" + be.getValue() + "</b> (" + sId + ") " + LoggedOutStr + " on " + currentDate + "</br>";
            InformLogFile(LogMessage);
        }

    }

    /*
     * -----------------------------------------------------------------------
     * InformLogFile()
     * -------------------------------------------------------------------------
     * INPUT: - actionCode : LOG_ACTION_START or LOG_ACTION_END FUNCTION: -
     * informs log file CALLED BY:
     -----------------------------------------------------------------------
     */
    /**
     *
     * @param message
     */
    private void InformLogFile(String message) {

        message = "<font face=\"Arial\" size=\"2\">" + message + "</font>\n";
        try {

            String currentDate = (new Date(System.currentTimeMillis())).toString();
            String[] dateParts = currentDate.split(" ");
            String month = dateParts[1];
            String year = dateParts[5];
            LogFile = new FileOutputStream(isl.FIMS.servlet.ApplicationBasicServlet.returnLogfilePath() + month + "-" + year + "LogFile.html", true);
            LogFile.write(message.getBytes("UTF-8"));
            LogFile.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
    }
}
