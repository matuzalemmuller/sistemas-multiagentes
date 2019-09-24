Class notes from Multiagent Systems Course @ PGEAS: http://jomi.das.ufsc.br/mas/

## BDI (Belief, Desire & Intention)

* BDI was created to aim problems that were hard to solve using traditional programming languages: proactive and reactive
* Agents easily adds and removes intentions. Objects do not support dynamic methods (easily)

## Interaction

* Agents are reactive, proactive and social (they interact with other agents)
* _Speech Act_:
  * _tell_, _achieve_: "type" of the message. In Portuguese, these "types" are called `performativas`
    * _tell_(open(door)): the door is open
    * _achieve_(open(door)): open the door
  * "type" + "message"
* _ACL_: _Agent Communication Language_. Standard format for message exchange of agents (similar to JSON, REST, etc)
  * KQML e FIPA-ACEL
* _White pages_: agent X can be reached at Y
    * name (symbolic) + address (physical)
* _Yellow pages_: address of agent X that does Y
