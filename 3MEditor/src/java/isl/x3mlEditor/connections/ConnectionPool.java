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
package isl.x3mlEditor.connections;

//import java.io.*;
import java.util.Hashtable;

import isl.dbms.DBCollection;

//import isl.dbms.ExistCollection;
import isl.dbms.eXist.ExistCollection;



	/**
	 * Class ConnectionPool is an intermediary between the user application and the database.
	 * It manages the connections to the database in an intelligent way, maintaining a pool of currently open connections
	 * and returning these to the application, instead of creating new connections every time one is needed. A unique connection is
	 * created for every database path and collection name, as provided by the caller in method connect(). Thereafter, 
	 * every time a caller requests a connection to this pair of [path, name] parameters, ConnectionPool will return
	 * the already established connection. This greatly speeds up the performance of the application, keeps overheads low,
	 * and preserves system resources, which might otherwise be depleted, if always new connections were being made.
	 * <p>
	 * ConnectionPool uses class isl.dbms.DBCollection to connect to the remote database. This insulates it from changes
	 * in database vendor or protocol. 
	 * <p>
	 * ConnectionPool is a singleton. To use its services, you must first configure it, by calling ConnectionPool.configure(), with the
	 * appropriate parameters. Then, get a reference to the unique instance using ConnectionPool.getInstance(). The connect() 
	 * methods can be used now to obtain DBCollection objects that connect to the specified collections.
	 * 
	 */	
public class ConnectionPool extends Hashtable {

	private static ConnectionPool instance = null;
	private static boolean configured = false;
	private static int defaultPerms = -1;
	
	private String username;
	private String password;
	private boolean verbose;
	
	
	/**
	 * Configures ConnectionPool. 
	 * @param	u	A default username to connect to the database
	 * @param	p	A default password to connect to the database
	 */	
	 public static void configure(String u, String p) {
		configure(u, p, defaultPerms, true);
	}
	
	/**
	 * Configures ConnectionPool. 
	 * @param	u	A default username to connect to the database
	 * @param	p	A default password to connect to the database
	 * @param	v	Sets the 'verbose' attribute. If true, details on what ConnectionPool does will be output to System.out
	 */	
	public static void configure(String u, String p, boolean v) {
		configure(u, p, defaultPerms, v);
	}
	
	/**
	 * Configures ConnectionPool. 
	 * @param	u	A default username to connect to the database
	 * @param	p	A default password to connect to the database
	 * @param	perms		The default permissions that will be set on all files created through this collection
	 * @param	v	Sets the 'verbose' attribute. If true, details on what ConnectionPool does will be output to System.out
	 */	
	public static void configure(String u, String p, int perms, boolean v) {
		instance = new ConnectionPool(u, p, perms, v);
	}
	
	/**
	 * Returns the unique instance of ConnectionPool. Method configure() must have been called first.
         * 
         * @return 
         */
	public static ConnectionPool getInstance() {
		if (!configured) {
            throw new Error("ConnectionPool:: not configured.");
        }

		return instance;
	}

	/**
	 * Returns a connection to the remote database. The default username and password will be used. If a connection already exists,
	 * that one will be returned. Otherwise, a new connection will be created.
	 * Property PROCESS_XSL_PI will be set to "no" on the connection.
	 * @param	eP	Database protocol to use
         * @param	dbP	Path to the collection
         * @return 
         * @throws java.io.IOException
	 */
	public DBCollection connect(String eP, String dbP)
	throws java.io.IOException {
		return connect(eP, dbP, "no", username, password, defaultPerms);
	}

	/**
	 * Returns a connection to the remote database. If a connection already exists,
	 * that one will be returned. Otherwise, a new connection will be created.
	 * @param	eP	Database protocol to use
	 * @param	dbP	Path to the collection
         * @param	s	The value of this parameter will be used to set property PROCESS_XSL_PI on the connection.
         * @return
         * @throws java.io.IOException 
	 */
	public DBCollection connect(String eP, String dbP, String s)
	throws java.io.IOException {
		return connect(eP, dbP, s, username, password, defaultPerms);
	}
	
	/**
	 * Returns a connection to the remote database. If a connection already exists,
	 * that one will be returned. Otherwise, a new connection will be created.
	 * @param	eP	Database protocol to use
	 * @param	dbP	Path to the collection
         * @param	perms		The default permissions that will be set on all files created through this collection
         * @return
         * @throws java.io.IOException 
	 */
	public DBCollection connect(String eP, String dbP, int perms)
	throws java.io.IOException {
		return connect(eP, dbP, "no", username, password, perms);
	}
	
	/**
	 * Returns a connection to the remote database. If a connection already exists,
	 * that one will be returned. Otherwise, a new connection will be created.
	 * @param	eP	Database protocol to use
	 * @param	dbP	Path to the collection
	 * @param	s	The value of this parameter will be used to set property PROCESS_XSL_PI on the connection.
	 * @param	uname	The username to use to connect to this collection
         * @param	pwd	The password to use to connect to this collection
         * @return
         * @throws java.io.IOException 
	 */
	public DBCollection connect(String eP, String dbP, String s, String uname, String pwd) 
	throws java.io.IOException {
		return connect(eP, dbP, s, uname, pwd, defaultPerms);
	}
	
	/**
	 * Returns a connection to the remote database. If a connection already exists,
	 * that one will be returned. Otherwise, a new connection will be created.
	 * @param	eP	Database protocol to use
	 * @param	dbP	Path to the collection
	 * @param	s	The value of this parameter will be used to set property PROCESS_XSL_PI on the connection.
	 * @param	uname	The username to use to connect to this collection
	 * @param	pwd	The password to use to connect to this collection
         * @param	perms		The default permissions that will be set on all files created through this collection
         * @return
         * @throws java.io.IOException 
	 */
	public DBCollection connect(String eP, String dbP, String s, String uname, String pwd, int perms)
	throws java.io.IOException {
		if (!configured) {
            throw new java.io.IOException("ConnectionPool:: not configured.");
        }
		
		if (!contains(eP, dbP, uname, pwd)) {
			
			if (verbose) {
                System.out.println("ConnectionPool:: opening new connection to " + eP + ", " + dbP + ", for " + uname + ", " + pwd);
            }

			return addCollection(eP, dbP, s, uname, pwd, perms);
		}

		if (verbose) {
            System.out.println("ConnectionPool:: reusing open connection to " + eP + ", " + dbP + ", for " + uname + ", " + pwd);
        }
			
                ExistCollection res = (ExistCollection)getCollection(eP, dbP, uname, pwd);
             // 4-12-2006 Samarita
//		DBCollection res = getCollection(eP, dbP, uname, pwd);
             
		res.setProperty("PROCESS_XSL_PI", s);
		return res;
	}
	
	/** Disconnects an open connection to the collection, if one exists. Currently, this method is empty.
         * 
         * @param eP
         * @param dbP 
         * @throws java.io.IOException
         */
	public void disconnect(String eP, String dbP)
	throws java.io.IOException {
		disconnect(eP, dbP, username, password);
	}
	
	/** Disconnects an open connection to the collection, if one exists. Currently, this method is empty.
         * 
         * @param eP
         * @param dbP
         * @param uname 
         * @param pwd 
         * @throws java.io.IOException
         */
	public void disconnect(String eP, String dbP, String uname, String pwd) 
	throws java.io.IOException {
		if (!configured) {
            throw new java.io.IOException("ConnectionPool:: not configured.");
        }
		
		// not doing anything for now...
		return;
	}
	
	private DBCollection addCollection(String existPath, String dbPath, String s, String uname, String pwd, int perms) {
		ExistCollection collection = null;
			
		try{
			if (verbose) {
                System.out.println("ConnectionPool:: connecting to " + existPath + ", " + dbPath);
            }
				
			collection = new ExistCollection(existPath, dbPath, uname, pwd);
			collection.setProperty("EXPAND_XINCLUDES", "no");
			collection.setProperty("INDENT_SPACES", "yes");
			collection.setProperty("PROCESS_XSL_PI", s);
			
			if (perms != -1) {
                collection.setDefaultPermissions(perms);
            }
				
			if (verbose) {
                System.out.println("ConnectionPool:: connection established with params: \n\t\t" + existPath + ",\n\t\t" + dbPath + ",\n\t\t" + uname + ",\n\t\t" + pwd + "\n\n");
            }
				
			put(existPath+dbPath+uname+pwd, collection);
		}
		catch(Exception e){
			System.out.println("ConnectionPool:: Exception thrown in addCollection");
                        e.printStackTrace(System.out);
			if (verbose) {
                e.printStackTrace(System.out);
            }
			
			throw new java.io.IOException("ConnectionPool:: could not connect");
		}
		finally {
			return collection;
		}
	}
		
	private boolean contains(String eP, String dbP, String uname, String pwd) {
		if (!containsKey(eP+dbP+uname+pwd)) {
            return false;
        }
		
		return true;
	}
	
	private DBCollection getCollection(String eP, String dbP, String uname, String pwd) {
		return ((DBCollection)get(eP+dbP+uname+pwd));
	}

	private ConnectionPool(String u, String p, int perms, boolean v) {
		username = u;
		password = p;
		verbose  = v;
		configured = true;
		defaultPerms = perms;
		
		if (verbose) {
            System.out.println("ConnectionPool:: constructed with " + u + ", " + p + ", " + perms + ", " + v);
        }
	}

}

