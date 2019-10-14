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
    private boolean DEBUG = true;
    
    public static void main(String[] args) {
        Test s = new Test();

        try {
            s.startContainer();
            s.createAgents();    
        } catch (Exception e){
            System.out.println(e);
        }
             
    }

    void startContainer() {
        ProfileImpl p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, "localhost");
        
        cc = Runtime.instance().createMainContainer(p);
    }

    void createAgents() throws Exception {
        for (int i=1; i<=participants; i++) {
            AgentController ac = cc.createNewAgent("Participant"+i, "Participant", new Object[] { DEBUG });
            ac.start();
        }

        for (int i=1; i<=initiators; i++) {
            AgentController ac = cc.createNewAgent("Initiator"+i, "Initiator", new Object[] { DEBUG, num_services_requested });
            ac.start();
        }
    }
}