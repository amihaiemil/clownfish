/*
 * Copyright (c) 2002, 2018 Oracle and/or its affiliates. All rights reserved.
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

package connector;

import java.lang.reflect.Method;
import java.util.Iterator;
import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.UnavailableException;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.WorkManager;
import javax.resource.spi.work.ExecutionContext;
import javax.resource.spi.*;
import javax.resource.*;

/**
 *
 * @author	Qingqing Ouyang
 */
public class DeliveryWork implements Work, ResourceAdapterAssociation {

    private MessageEndpoint ep;
    private int num;
    private String op;
    private boolean keepCount;
    private static int counter = 0;
    protected ResourceAdapter raBean;
    
    public DeliveryWork(MessageEndpoint ep, int numOfMessages, String op) {
        this.ep = ep;
        this.num = numOfMessages;
        this.op = op;
        this.keepCount = false;
    }

    public DeliveryWork(MessageEndpoint ep, int numOfMessages, 
            String op, boolean keepCount) {
        this.ep = ep;
        this.num = numOfMessages;
        this.op = op;
        this.keepCount = keepCount;
    }
    public void setResourceAdapter(ResourceAdapter ra) throws ResourceException{
        debug("RA Bean set");
        raBean = ra;
    }

    public ResourceAdapter getResourceAdapter(){
        return raBean;
    }

    public void run() {

        debug("ENTER...");
        debug("RA Bean : " + raBean);

        // We are expecting RAA for Work instance to happen for a 1.5 RAR, GF does not restrict it and hence this is valid.
        if(raBean == null){
            throw new RuntimeException("ResourceAdapterAssociation did not happen for DeliveryWork");
        }
        try {
            //Method onMessage = getOnMessageMethod();
            //ep.beforeDelivery(onMessage);

            if (!keepCount) {
                for (int i = 0; i < num; i++) {
                    String msgId   = String.valueOf(i);
                    String msgBody = "This is message " + msgId;
                    String msg     = msgId + ":" + msgBody + ":" + op;
                    ((MyMessageListener) ep).onMessage(msg);
                }
            } else {
                for (int i = 0; i < num; i++) {
                    String msgId   = String.valueOf(i+counter);
                    String msgBody = "This is message " + msgId;
                    String msg     = msgId + ":" + msgBody + ":" + op;
                    ((MyMessageListener) ep).onMessage(msg);
                }
                counter = counter + num;
            }

            //ep.afterDelivery();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        debug("LEAVE...");
    }

    public void release() {}

    public String toString() {
       return op;
    }

    private Method getOnMessageMethod() {
        
        Method onMessageMethod = null;
        try {
            Class msgListenerClass = connector.MyMessageListener.class;
            Class[] paramTypes = { java.lang.String.class };
            onMessageMethod = 
                msgListenerClass.getMethod("onMessage", paramTypes);
            
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        }
        return onMessageMethod;
    }

    private void debug(String mesg) {
        System.out.println("DeliveryWork[" + op + "] --> " + mesg);
    }
}
