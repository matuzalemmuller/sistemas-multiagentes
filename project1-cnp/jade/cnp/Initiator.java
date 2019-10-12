/*****************************************************************
JADE - Java Agent DEvelopment Framework is a framework to develop 
multi-agent systems in compliance with the FIPA specifications.
Copyright (C) 2000 CSELT S.p.A. 

GNU Lesser General Public License

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation, 
version 2.1 of the License. 

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the
Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA  02111-1307, USA.
*****************************************************************/

package jade.cnp;

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
    // List of services available
    private List <String> services = Arrays.asList( "pedreiro", "padeiro",
                                                    "mecanico", "encanador",
                                                    "pintor", "padre",
                                                    "chaveiro", "programador",
                                                    "carteiro", "bancario" );
    private int num_services_requested;
    private int services_hired = 0;
    // The list of known seller agents
    private AID[] sellerAgents;

    // Put agent initializations here
    protected void setup() {
        // Randomizes amount of services to be requested
        num_services_requested = ThreadLocalRandom.current().nextInt(1, 11);

        // Requests services
        for (int service_index, i = num_services_requested; i > 0; i--) {
            service_index = ThreadLocalRandom.current().nextInt(0, 10);
            RequestService(services.get(service_index));
        }
    }

    // Creates behavior for each service to be requested
    protected void RequestService(String service) {
        addBehaviour(new TickerBehaviour(this, 10000) {
            protected void onTick() {
                System.out.println("Trying to hire " + service);
                // Update the list of seller agents
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("yellow-page-services");
                template.addServices(sd);
                try {
                    DFAgentDescription[] result = DFService.search(myAgent, template);
                    System.out.println("Found the following seller agents:");
                    sellerAgents = new AID[result.length];
                    for (int i = 0; i < result.length; ++i) {
                        sellerAgents[i] = result[i].getName();
                        System.out.println(sellerAgents[i].getName());
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
        System.out.println("Initiator " + getAID().getName() + " terminating.");
    }

    /**
       Inner class RequestPerformer.
       This is the behaviour used by Initiator agents to request Participant 
       agents the target service.
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
                    for (int i = 0; i < sellerAgents.length; ++i) {
                        cfp.addReceiver(sellerAgents[i]);
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
                        if (repliesCnt >= sellerAgents.length) {
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
                            System.out.println(serviceName + " successfully hired service from agent " + reply.getSender().getName());
                            System.out.println("Price = " + bestPrice);
                        }
                        step = 4;
                        services_hired++;
                    } else {
                        block();
                    }
                case 4:
                    if(services_hired == num_services_requested){
                        myAgent.doDelete();
                        step = 5;
                    }
                    break;
            }
        }

        public boolean done() {
            if (step == 2 && bestParticipant == null) {
                System.out.println("Attempt failed: service not available for hiring");
                services_hired++;
            }
            if(services_hired == num_services_requested){
                myAgent.doDelete();
                return true;
            }
            return ((step == 2 && bestParticipant == null) || step == 5);
        }
    } // End of inner class RequestPerformer
}