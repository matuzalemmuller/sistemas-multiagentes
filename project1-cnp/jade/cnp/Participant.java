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
    private List <String> services = Arrays.asList( "pedreiro", "padeiro",
                                                    "mecanico", "encanador",
                                                    "pintor", "padre",
                                                    "chaveiro", "programador",
                                                    "carteiro", "bancario" );
    // Name of service provided by this agent
    private String service_name;
    // Randomizes which service will be provided
    private int service_index;
    // Price of service provided by this agent
    private int price;

    // Put agent initializations here
    protected void setup() {
        this.service_index = ThreadLocalRandom.current().nextInt(0, 10);
        this.service_name = services.get(service_index);

        if (DEBUG == true)
            System.out.println("Agent " + getAID().getName() + " will work as " + this.service_name);

        // Register the book-selling service in the yellow pages
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

        // Add the behaviour serving queries from buyer agents
        addBehaviour(new OfferRequestsServer());

        // Add the behaviour serving purchase orders from buyer agents
        addBehaviour(new PurchaseOrdersServer());
    }

    // Put agent clean-up operations here
    protected void takeDown() {
        // Deregister from the yellow pages
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        // Printout a dismissal message
        System.out.println("Participant " + getAID().getName() + " terminating.");
    }

    /**
         Inner class OfferRequestsServer.
         This is the behaviour used by Book-seller agents to serve incoming requests 
         for offer from buyer agents.
         If the requested book is in the local catalogue the seller agent replies 
         with a PROPOSE message specifying the price. Otherwise a REFUSE message is
         sent back.
    */
    private class OfferRequestsServer extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                // CFP Message received. Process it
                String title = msg.getContent();
                ACLMessage reply = msg.createReply();

                if (title.equals(service_name)){
                    reply.setPerformative(ACLMessage.PROPOSE);
                    price = ThreadLocalRandom.current().nextInt(0, 1000);
                    reply.setContent(String.valueOf(price));
                    if (DEBUG == true)
                        System.out.println("Offered to work for " + msg.getSender().getName() + " for price " + price);
                } else {
                    // The requested book is NOT available for sale.
                    reply.setPerformative(ACLMessage.REFUSE);
                    reply.setContent("not-available");
                }
                myAgent.send(reply);
            } else {
                block();
            }
        }
    } // End of inner class OfferRequestsServer

    /**
         Inner class PurchaseOrdersServer.
         This is the behaviour used by Book-seller agents to serve incoming 
         offer acceptances (i.e. purchase orders) from buyer agents.
         The seller agent removes the purchased book from its catalogue 
         and replies with an INFORM message to notify the buyer that the
         purchase has been sucesfully completed.
    */
    private class PurchaseOrdersServer extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                // ACCEPT_PROPOSAL Message received. Process it
                String serviceName = msg.getContent();
                ACLMessage reply = msg.createReply();

                if(serviceName.equals(service_name)){
                    reply.setPerformative(ACLMessage.INFORM);
                    if (DEBUG == true)
                        System.out.println("Hired by agent " + msg.getSender().getName());
                } else {
                    // Participant does not work in this profession
                    reply.setPerformative(ACLMessage.FAILURE);
                    reply.setContent("not-available");
                }
                myAgent.send(reply);
            } else {
                block();
            }
        }
    } // End of inner class OfferRequestsServer

} // end of the agent class