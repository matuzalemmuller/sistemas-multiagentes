package src;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import java.util.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Participant extends Agent {
    // Print state messages
    private boolean DEBUG = true;
    // List of services available
    private List<String> services = Arrays.asList("builder", "baker", "mechanic", "plumber", "painter", "priest",
            "locksmith", "programmer", "postman", "banker");
    // Name of service provided by this agent
    private String service_name;
    // Randomizes which service will be provided
    private int service_index;
    // Price of service provided by this agent
    private int price;

    protected void setup() {
        Object[] args = getArguments();
        DEBUG = Boolean.valueOf(args[0].toString());

        // Randomizes choosing service
        this.service_index = ThreadLocalRandom.current().nextInt(0, 10);
        this.service_name = services.get(service_index);

        if (DEBUG == true)
            System.out.println(getAID().getName() + " will work as " + this.service_name);

        // Register the service in the yellow pages
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("yellow-page-services");
        sd.setName("service-hire");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        // Add the behaviour serving queries from Initiator agents
        addBehaviour(new OfferService());

        // Add the behaviour serving hiring requests from Initiator agents
        addBehaviour(new HiringServer());
    }

    protected void takeDown() {
        // Deregister from the yellow pages
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        if (DEBUG == true)
            System.out.println(getAID().getName() + " terminating.");
    }

    /**
     * Ihis is the behaviour used by Participant agents to serve incoming requests for offer from Initiator agents.
     */
    private class OfferService extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                String title = msg.getContent();
                ACLMessage reply = msg.createReply();

                if (title.equals(service_name)) {
                    reply.setPerformative(ACLMessage.PROPOSE);
                    price = ThreadLocalRandom.current().nextInt(0, 100);
                    reply.setContent(String.valueOf(price));
                    if (DEBUG == true)
                        System.out.println(getAID().getName() + " offered to work for " + msg.getSender().getName()
                                + " for price " + price);
                } else {
                    reply.setPerformative(ACLMessage.REFUSE);
                    reply.setContent("not-available");
                }
                myAgent.send(reply);
            } else {
                block();
            }
        }
    } 

    /**
     * This is the behaviour used by Participant agents to serve incoming offer acceptances from Initiator agents.
     */
    private class HiringServer extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                String serviceName = msg.getContent();
                ACLMessage reply = msg.createReply();

                if (serviceName.equals(service_name)) {
                    reply.setPerformative(ACLMessage.INFORM);
                    if (DEBUG == true)
                        System.out.println(getAID().getName() + " hired by agent " + msg.getSender().getName());
                } else {
                    reply.setPerformative(ACLMessage.FAILURE);
                    reply.setContent("not-available");
                }
                myAgent.send(reply);
            } else {
                block();
            }
        }
    } 

} 