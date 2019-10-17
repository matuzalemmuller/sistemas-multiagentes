# Assignment 1 - _Contract Net Protocol_

## Description

Implementation of [Contract Net Protocol](https://en.wikipedia.org/wiki/Contract_Net_Protocol) in JADE and JASON. The complete assignment guidelines can be found on [Assignment-ptBR.pdf](Assignment-ptBR.pdf).

Both implementations of the MAS will have at least three agents:

* `Initiator`: requests a service;
* `Participant`: offers a service;
* `Watcher`: stops simulation when all negotiations are completed.

Each simulation can be run individually inside each project folder.

## Benchmark and testing

A Python code has been included in the project to run both simulations and store the CPU and memory usage during each run.

To run the benchmark program it is necessary to install the code dependencies, compile the JADE code, and execute the `benchmark.py` file:

```
pip3 install -r requirements.txt

cd jade
make

cd ..
python3 benchmark.py
```

The benchmark program will first run the JADE MAS and then the JASON simulation. The CPU and memory usage for each simulation will be saved in `.csv` files inside the `benchmark` folder.

* _Note_: _make sure that there are no `jason-ide` and `RunCentralisedMAS` processes running when executing the benchmark for the JASON MAS_.
