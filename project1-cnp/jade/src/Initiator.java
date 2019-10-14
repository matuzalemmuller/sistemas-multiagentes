import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.security.Provider.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Initiator extends Agent {
    // Print state messages
    private boolean DEBUG = true;
    // List of services available
    private List<String> services = Arrays.asList("builder", "baker", "mechanic", "plumber", "painter", "priest",
            "locksmith", "programmer", "postman", "banker");
    // Number of services to be requested
    private int num_services_requested;
    // Tracks how many services were hired from participants
    private int services_hired = 0;
    // The list of known seller agents
    private AID[] participantAgents;

    // Put agent initializations here
    protected void setup() {
        Object[] args = getArguments();
        DEBUG = Boolean.valueOf(args[0].toString());

        // Randomizes amount of services to be requested
        num_services_requested = Integer.valueOf(args[1].toString());

        if (DEBUG == true)
            System.out.println(getAID().getName() + " starting");

        // Requests services
        for (int service_index, i = num_services_requested; i > 0; i--) {
            service_index = ThreadLocalRandom.current().nextInt(0, 10);
            RequestService(services.get(service_index));

            if (DEBUG == true)
                System.out.println(getAID().getName() + " sent request to hire " + services.get(service_index));
        }
    }

    // Creates behavior for each service to be requested
    protected void RequestService(String service) {
        addBehaviour(new TickerBehaviour(this, 10000) {
            protected void onTick() {
                if (DEBUG == true)
                    System.out.println(getAID().getName() + " trying to hire " + service);
                // Update the list of seller agents
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("yellow-page-services");
                template.addServices(sd);
                try {
                    DFAgentDescription[] result = DFService.search(myAgent, template);
                    participantAgents = new AID[result.length];
                    for (int i = 0; i < result.length; ++i) {
                        participantAgents[i] = result[i].getName();
                    }
                } catch (FIPAException fe) {
                    fe.printStackTrace();
                }

                // Perform the request
                myAgent.addBehaviour(new RequestPerformer(service));
            }
        });
    }

    // Put agent clean-up operations here
    protected void takeDown() {
        // Printout a dismissal message
        System.out.println(getAID().getName() + " terminating.");
    }

    /**
     * Inner class RequestPerformer. This is the behaviour used by Initiator agents
     * to hire Participant agents
     */
    private class RequestPerformer extends Behaviour {
        private AID bestParticipant; // The agent who provides the best offer
        private int bestPrice; // The best offered price
        private int repliesCnt = 0; // The counter of replies from seller agents
        private MessageTemplate mt; // The template to receive replies
        private int step = 0;
        private String serviceName;

        public RequestPerformer(String serviceName) {
            super();
            this.serviceName = serviceName;
        }

        public void action() {
            switch (step) {
            case 0:
                // Send the cfp to all sellers
                ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                for (int i = 0; i < participantAgents.length; ++i) {
                    cfp.addReceiver(participantAgents[i]);
                }
                cfp.setContent(serviceName);
                cfp.setConversationId("service-hire");
                cfp.setReplyWith("cfp" + System.currentTimeMillis()); // Unique value
                myAgent.send(cfp);
                // Prepare the template to get proposals
                mt = MessageTemplate.and(MessageTemplate.MatchConversationId("service-hire"),
                        MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
                step = 1;
                break;
            case 1:
                // Receive all proposals/refusals from seller agents
                ACLMessage reply = myAgent.receive(mt);
                if (reply != null) {
                    // Reply received
                    if (reply.getPerformative() == ACLMessage.PROPOSE) {
                        // This is an offer
                        int price = Integer.parseInt(reply.getContent());
                        if (bestParticipant == null || price < bestPrice) {
                            // This is the best offer at present
                            bestPrice = price;
                            bestParticipant = reply.getSender();
                        }
                    }
                    repliesCnt++;
                    if (repliesCnt >= participantAgents.length) {
                        // We received all replies
                        step = 2;
                    }
                } else {
                    block();
                }
                break;
            case 2:
                // Send the purchase order to the seller that provided the best offer
                ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                order.addReceiver(bestParticipant);
                order.setContent(serviceName);
                order.setConversationId("service-hire");
                order.setReplyWith("order" + System.currentTimeMillis());
                myAgent.send(order);
                // Prepare the template to get the purchase order reply
                mt = MessageTemplate.and(MessageTemplate.MatchConversationId("service-hire"),
                        MessageTemplate.MatchInReplyTo(order.getReplyWith()));
                step = 3;
                break;
            case 3:
                // Receive the purchase order reply
                reply = myAgent.receive(mt);
                if (reply != null) {
                    // Purchase order reply received
                    if (reply.getPerformative() == ACLMessage.INFORM) {
                        // Purchase successful. We can terminate
                        if (DEBUG == true) {
                            System.out.println(getAID().getName() + " successfully hired agent "
                                    + reply.getSender().getName() + " as " + serviceName);
                        }
                    }
                    step = 4;
                    services_hired++;
                } else {
                    block();
                }
            case 4:
                if (services_hired == num_services_requested) {
                    myAgent.doDelete();
                    step = 5;
                }
                break;
            }
        }

        public boolean done() {
            if (step == 2 && bestParticipant == null) {
                if (DEBUG == true)
                    System.out.println(getAID().getName() + ": " + serviceName + " not available for hiring");
                services_hired++;
            }

            if (services_hired == num_services_requested) {
                myAgent.doDelete();
                return true;
            }
            return ((step == 2 && bestParticipant == null) || step == 5);
        }
    } // End of inner class RequestPerformer
}