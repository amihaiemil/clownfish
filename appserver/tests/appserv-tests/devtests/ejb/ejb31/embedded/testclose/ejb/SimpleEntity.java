/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
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

/*
 * SimpleEntity.java
 *
 * @author Marina Vatkina
 */

package org.glassfish.tests.ejb.sample;

import javax.persistence.*;
import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@NamedQueries({
    @NamedQuery(name = "SimpleEntity.findAll", query = "select e from SimpleEntity e")
})
public class SimpleEntity {
    
    @Id 
    @GeneratedValue
    private int id;
    private String name;

    public SimpleEntity(String name) {
        setName(name);
    }
    
    public SimpleEntity() {
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
