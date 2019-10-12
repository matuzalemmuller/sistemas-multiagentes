package src;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;


public class Test {

    ContainerController cc;
    private int initiators = 2;
    private int participants = 5;
    private int num_services_requested = 1;
    
    public static void main(String[] args) throws Exception {
        Test s = new Test();

        s.startContainer();
        s.createAgents();         
    }

    void startContainer() {
        ProfileImpl p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, "localhost");
        //p.setParameter(Profile.GUI, "false");
        
        cc = Runtime.instance().createMainContainer(p);
    }

    void createAgents() throws Exception {
        //creating Participants
        for (int i=1; i<=participants; i++) {
            AgentController ac = cc.createNewAgent("Participant"+i, "cnp.Participant", new Object[] {});
            ac.start();
        }

        //creating Initiators
        for (int i=1; i<=initiators; i++) {
            AgentController ac = cc.createNewAgent("Initiator"+i, "cnp.Initiator", new Object[] { num_services_requested });
            ac.start();
        }
    }
}