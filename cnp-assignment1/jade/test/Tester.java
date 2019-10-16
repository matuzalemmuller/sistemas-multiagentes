/*
    Creates multiple Initiators and Participants to test MAS
*/
package test;
import src.Initiator;
import src.Participant;
import src.Watcher;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class Tester {

    ContainerController cc;
    private int initiators = 3;
    private int participants = 5;
    private int num_services_requested = 3;
    private boolean DEBUG = true;

    // Starts testing
    public static void main(String[] args) {
        Tester tester = new Tester();

        try {
            tester.createContainer();
            tester.createAgents();   
        } catch (Exception e){
            System.out.println(e);
        }
    }

    // Creates JADE container
    void createContainer() {
        ProfileImpl p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, "localhost");
        
        cc = Runtime.instance().createMainContainer(p);
    }

    // Creates Initiators, Participants and Wather to kill container at the end of execution
    void createAgents() {
        try {
            AgentController watcherAgent = cc.createNewAgent("Watcher", "src.Watcher", new Object[] { DEBUG, cc });
            watcherAgent.start();

            for (int i=1; i<=participants; i++) {
                AgentController participantAgent = cc.createNewAgent("Participant"+i, "src.Participant", new Object[] { DEBUG });
                participantAgent.start();
            }

            for (int i=1; i<=initiators; i++) {
                AgentController initiatorAgent = cc.createNewAgent("Initiator"+i, "src.Initiator", new Object[] { DEBUG, num_services_requested });
                initiatorAgent.start();
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }
}