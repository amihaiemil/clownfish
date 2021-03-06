/*
 * Copyright (c) 2001, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.s1peqe.jndi.url.ejb;

import javax.naming.*;
import javax.ejb.*;
import java.net.*;
import java.io.*;

public class HTMLReaderBean implements SessionBean {
 
  public StringBuffer getContents() throws HTTPResponseException {

     Context context;
     URL url;
     StringBuffer buffer;
     String line;
     int responseCode;
     HttpURLConnection connection;
     InputStream input;
     BufferedReader dataInput;
 
     try {
        context = new InitialContext();
        url = (URL)context.lookup("java:comp/env/url/MyURL");  
        connection = (HttpURLConnection)url.openConnection();
        responseCode = connection.getResponseCode();
     } catch (Exception ex) {
         throw new EJBException(ex.getMessage());
     }

     if (responseCode != HttpURLConnection.HTTP_OK) {
        throw new HTTPResponseException("HTTP response code: " + 
           String.valueOf(responseCode));
     }

     try {
        buffer = new StringBuffer();
        input = connection.getInputStream();
        dataInput = new BufferedReader(new InputStreamReader(input));
        while ((line = dataInput.readLine()) != null) {
           buffer.append(line);
           buffer.append('\n');
        }  
     } catch (Exception ex) {
         throw new EJBException(ex.getMessage());
     }

     return buffer;

  } // getContents()

   public HTMLReaderBean() {}
   public void ejbCreate() {}
   public void ejbRemove() {}
   public void ejbActivate() {}
   public void ejbPassivate() {}
   public void setSessionContext(SessionContext sc) {}

} // HTMLReaderBean
