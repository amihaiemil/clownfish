<!--

    Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.

    This program and the accompanying materials are made available under the
    terms of the Eclipse Public License v. 2.0, which is available at
    http://www.eclipse.org/legal/epl-2.0.

    This Source Code may also be made available under the following Secondary
    Licenses when the conditions for such availability set forth in the
    Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
    version 2 with the GNU Classpath Exception, which is available at
    https://www.gnu.org/software/classpath/license.html.

    SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0

-->

<!-- sslPrepare-copy.inc  -->

setPageSessionAttribute(key="edit" value="#{true}" )
gf.getEntityAttrs(endpoint="#{pageSession.selfUrl}.json" valueMap="#{pageSession.valueMap}");
gf.createEntityResponseOutput(endpoint="#{pageSession.selfUrl}" attrs="#{pageSession.valueMap}" throwException="false" result="#{pageSession.resultMap}");
if ("#{pageSession.resultMap.data.exit_code}=FAILURE") {
    setPageSessionAttribute(key="edit" value="#{false}" )
}
if (#{pageSession.edit}){
    gf.getEntityAttrs(endpoint="#{pageSession.selfUrl}.json" valueMap="#{pageSession.valueMap}");
    convertToDifferentCiphersGroup(ciphers="#{pageSession.valueMap['ssl3TlsCiphers']}"
        CommonCiphersList="#{pageSession.selectedCommon}"
        EphemeralCiphersList="#{pageSession.selectedEphemeral}"
        OtherCiphersList="#{pageSession.selectedOther}"
        EccCiphersList="#{pageSession.selectedEcc}")

}
if (! #{pageSession.edit}){
    <!-- this <ssl> does not exists, we cannot find a way to get the default value, so hard coded here for now.
         TODO: REST need to provide an endpoint to get the default value.
      -->
    createMap(result="#{pageSession.valueMap}");
    mapPut(map="#{pageSession.valueMap}" key="ssl3Enabled" value="true");
    mapPut(map="#{pageSession.valueMap}" key="tlsEnabled" value="true");
    mapPut(map="#{pageSession.valueMap}" key="trustMaxCertLength" value="5");

    convertToDifferentCiphersGroup(ciphers="#{pageSession.valueMap['ssl3TlsCiphers']}"
        CommonCiphersList="#{pageSession.selectedCommon}"
        EphemeralCiphersList="#{pageSession.selectedEphemeral}"
        OtherCiphersList="#{pageSession.selectedOther}"
        EccCiphersList="#{pageSession.selectedEcc}")
}

//set the following for including buttons.inc
setPageSessionAttribute(key="convertToFalseList" value={"ssl3Enabled" "tlsEnabled" "clientAuthEnabled" })
setPageSessionAttribute(key="skipAttrsList", value={"sslInactivityTimeout"});
setPageSessionAttribute(key="showDefaultButton" value="#{false}" )
setPageSessionAttribute(key="hasPropertySheet" value="#{true}" )
setPageSessionAttribute(key="hasPropertyTable" value="#{false}" )
