/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.drools.demo201804dm;

import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNDecisionResult;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;

import static demo.Utils.b;
import static demo.Utils.entry;
import static demo.Utils.mapOf;

public class MyTest {

    @Test
    public void test() {
        KieServices kieServices = KieServices.Factory.get();

        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        
        DMNRuntime dmnRuntime = kieContainer.newKieSession().getKieRuntime(DMNRuntime.class);

        DMNModel dmnModel = dmnRuntime.getModel("http://www.trisotech.com/dmn/definitions/_73732c1d-f5ff-4219-a705-f551a5161f88", "Bank monthly fees");

        DMNContext dmnContext = dmnRuntime.newContext();  
        dmnContext.set("Account holder", mapOf(entry("age", b(47)),
                                               entry("employed", true)));
        dmnContext.set("Account balance", b(10001));

        DMNResult dmnResult = dmnRuntime.evaluateAll( dmnModel, dmnContext );  
        System.out.println(dmnResult);

        for ( DMNDecisionResult dr : dmnResult.getDecisionResults() ) {
            System.out.println( "Decision '" + dr.getDecisionName() + "' : " + dr.getResult() );
        }
    }
}