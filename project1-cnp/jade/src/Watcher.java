import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.*;
import jade.wrapper.ContainerController;

public class Watcher extends Agent {

    private ContainerController cc;

    protected void setup() {
        if (DEBUG == true)
            System.out.println("Starting simulation");

        cc = (ContainerController) getArguments()[0];

        addBehaviour(new OneShotBehaviour() {
            public void action() {
                boolean run = true;

                while (run) {
                    AMSAgentDescription[] agents = null;
                    try {
                        Thread.sleep(1000);

                        SearchConstraints c = new SearchConstraints();
                        c.setMaxResults(new Long(-1));
                        agents = AMSService.search(myAgent, new AMSAgentDescription(), c);

                    } catch (Exception e) {
                        System.out.println(e);
                    }

                    for (int i = 0; i < agents.length; i++) {
                        AID agentID = agents[i].getName();
                        if (agentID.getLocalName().contains("Initiator")) {
                            break;
                        }
                        if (i == (agents.length - 1)) {
                            run = false;
                        }
                    }
                }
                myAgent.doDelete();
            }            
        });
    }

    protected void takeDown() {
        if (DEBUG == true)
            System.out.println(getAID().getName() + " terminating.");
        try {
            if(DEBUG == true)
                System.out.println("Ending simulation");
            cc.getPlatformController().kill();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}