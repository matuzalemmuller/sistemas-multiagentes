import jade.core.Agent;
import jade.core.AID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.*;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.List;

public class Test extends Agent {

    ContainerController cc;
    private int initiators = 2;
    private int participants = 5;
    private int num_services_requested = 1;
    private boolean DEBUG = true;

    public static void main(String[] args) {
        Test s = new Test();

        try {
            s.startContainer();
            s.createAgents();
            s.monitorTest();    
        } catch (Exception e){
            System.out.println(e);
        }
    }

    void startContainer() {
        ProfileImpl p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, "localhost");
        
        cc = Runtime.instance().createMainContainer(p);
    }

    void createAgents() {
        try {
            cc.acceptNewAgent("TestController", this);

            for (int i=1; i<=participants; i++) {
                AgentController ac = cc.createNewAgent("Participant"+i, "Participant", new Object[] { DEBUG });
                ac.start();
            }

            for (int i=1; i<=initiators; i++) {
                AgentController ac = cc.createNewAgent("Initiator"+i, "Initiator", new Object[] { DEBUG, num_services_requested });
                ac.start();
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }

    void monitorTest() {
        while(true){
            AMSAgentDescription [] agents = null;

            try {
                Thread.sleep(2000);

                SearchConstraints c = new SearchConstraints();
                c.setMaxResults ( new Long(-1) );
                agents = AMSService.search(this, new AMSAgentDescription (), c );
                
            } catch (Exception e) {
                System.out.println(e);
            }
            
            for (int i=0; i<agents.length; i++){
                AID agentID = agents[i].getName();
                if(agentID.getLocalName().contains("Initiator")){
                    break;
                }
                if(i == (agents.length - 1)) {
                    try {
                        cc.getPlatformController().kill();
                    } catch (Exception e) {
                        System.out.println(e);
                    } 
                }
            }
        }
    }
}