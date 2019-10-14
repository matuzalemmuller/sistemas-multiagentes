import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class Test {

    ContainerController cc;
    private int initiators = 200;
    private int participants = 50;
    private int num_services_requested = 10;
    private boolean DEBUG = false;

    public static void main(String[] args) {
        Test test = new Test();

        try {
            test.createContainer();
            test.createAgents();   
        } catch (Exception e){
            System.out.println(e);
        }
    }

    void createContainer() {
        ProfileImpl p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, "localhost");
        
        cc = Runtime.instance().createMainContainer(p);
    }

    void createAgents() {
        try {
            AgentController watcherAgent = cc.createNewAgent("Watcher", "Watcher", new Object[] { cc });
            watcherAgent.start();

            for (int i=1; i<=participants; i++) {
                AgentController participantAgent = cc.createNewAgent("Participant"+i, "Participant", new Object[] { DEBUG });
                participantAgent.start();
            }

            for (int i=1; i<=initiators; i++) {
                AgentController initiatorAgent = cc.createNewAgent("Initiator"+i, "Initiator", new Object[] { DEBUG, num_services_requested });
                initiatorAgent.start();
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }
}