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

## Organization

* Defines pattern of agent cooperation

#### Conceituação

* Especificação (Pós-graduação)
  * Estrutural: Grupo, Papel (definido quando entra na organização: aluno, prof)
  * Funcional: Schema, Missão, Objetivos
   Quais são os objetivos e como são decompostos (da organização, não dos agentes!)

    ```
    T(a1)			    |
        \				|	Schema e objetivos
        Y(a2)		    |
        /  \			|
        Z(a1) X(a1)		|	a1, a2: Missões (agentes que são responsáveis pelos objetivos)
    ```

      * Agentes se comprometem a alcançar os objetivos
      * Schema só começa a ser "executado" quando há agentes suficientes para todas as missões (caso contrário, os objetivos não serão cumpridos)

 * Normativa: Normas (Agente A tem que fazer B: mestrando precisa apresentar dissertação; professor é permitido a participar de banca)
   * Proibições, permissões e obrigações para papeis


* Entidade (PGEAS): instância da especificação