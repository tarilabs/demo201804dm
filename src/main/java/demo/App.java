package demo;

import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNDecisionResult;
import org.kie.dmn.api.core.DMNResult;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.DMNServicesClient;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;

import static demo.Utils.b;
import static demo.Utils.entry;
import static demo.Utils.mapOf;

public class App {

    private static final String URL = "http://rhdm7-summit18-kieserver-rhdm7-summit18-developer.127.0.0.1.nip.io/services/rest/server";
    private static final String USER = "kieserver";
    private static final String PASSWORD = "kieserver1!";

    private static final MarshallingFormat FORMAT = MarshallingFormat.JSON;

    private KieServicesConfiguration conf;
    private KieServicesClient kieServicesClient;

    public void initialize() {
        conf = KieServicesFactory.newRestConfiguration(URL, USER, PASSWORD);
        conf.setMarshallingFormat(FORMAT);
        kieServicesClient = KieServicesFactory.newKieServicesClient(conf);
    }

    private void demo() {
        initialize();

        DMNServicesClient dmnClient = kieServicesClient.getServicesClient(DMNServicesClient.class);

        DMNContext dmnContext = dmnClient.newContext();
        dmnContext.set("Account holder", mapOf(entry("age", b(47)),
                                               entry("employed", true)));
        dmnContext.set("Account balance", b(10_000));

        String containerId = "fees-application_1.0.0";
        ServiceResponse<DMNResult> serverResp = dmnClient.evaluateAll(containerId, dmnContext);

        DMNResult dmnResult = serverResp.getResult();

        for (DMNDecisionResult dr : dmnResult.getDecisionResults()) {
            System.out.println("--------------------------------------------");
            System.out.println("Decision name:   " + dr.getDecisionName());
            System.out.println("Decision status: " + dr.getEvaluationStatus());
            System.out.println("Decision result: " + dr.getResult());
        }
    }

    public static void main(String[] args) {
        App a = new App();
        a.demo();
    }
}
