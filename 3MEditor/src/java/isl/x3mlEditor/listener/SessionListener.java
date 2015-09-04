/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isl.x3mlEditor.listener;

import isl.Tidy;
import isl.x3mlEditor.BasicServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Web application lifecycle listener.
 *
 * @author samarita
 */
public class SessionListener extends BasicServlet implements HttpSessionListener {

    public static int activesessionsNO = 0;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        ++activesessionsNO;

        System.out.println("ACTIVE SESSIONS:" + activesessionsNO);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();

        if (activesessionsNO > 0) {
            --activesessionsNO;
        }
        System.out.println("ACTIVE SESSIONS:" + activesessionsNO);
        if (activesessionsNO == 0) {
            Tidy tidy = new Tidy(DBURI, rootCollection, DBuser, DBpassword, uploadsFolder);
            tidy.run();
        }
    }
}
