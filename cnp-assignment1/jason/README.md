## Description

* `src/ContractNetProtocol.mas2j`: declares agent and environment
* `src/initiator.asl`: implements initiator
* `src/participant.asl`: implements participant
* `src/watcher.asl`: helps to start initiator and stop MAS execution after all CNPs are established

## Requirements

* Unix-like operating system
* [Java 8 or later](https://www.java.com/en/download/)
* [Jason](https://sourceforge.net/projects/jason/files/jason/)

## Instructions

To run the JASON program:

1. Make sure that the `jason` script is added to the `$PATH`. If it is not, run the following commands:
    ```
    PATH="/path/to/folder/jason-2.4/scripts:${PATH}"
    export PATH
    ```
2. Run the program:
    ```
    jason src/ContractNetProtocol.mas2j
    ```
* Alternatively, you can run the `jason-ide` and manually load the files in the IDE to run the program.