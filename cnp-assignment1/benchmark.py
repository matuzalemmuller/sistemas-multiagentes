import os
import psutil
import subprocess
import time
from threading import Thread

# Runs jade code and saves CPU and memory usage data
def start_jade(project_dir):
    print("Starting JADE CNP...")
    os.chdir(project_dir+'/jade')

    # Start subprocess
    FNULL = open(os.devnull, 'w')
    p = subprocess.Popen(['java', '-cp', "lib/jade-4.3.jar:.", "test.Tester"], stdout=FNULL, stderr=subprocess.STDOUT, shell=False)
    pid = p.pid
    process = psutil.Process(pid)

    # Store memory and CPU usage in RAM
    text = []
    sec = 0
    while(p.poll() == None):
        cpu = psutil.cpu_percent()
        mem = process.memory_info().rss/1048576
        text.append(str(sec) +  "," + str(cpu) + "," + str(mem))
        time.sleep(1)
        sec += 1

    # Save memory and CPU usage data to results file
    os.makedirs(os.path.dirname('../benchmark/jade_results.csv'), exist_ok=True)
    with open('../benchmark/jade_results.csv','w+') as file:
        file.write("TIME,JADE:CPU,JADE:Memory")
        file.write('\n')
        for line in text:
            file.write(line)
            file.write('\n')
    print("Finished JADE CNP...")

# Runs jason code and saves CPU and memory usage data
def start_jason(project_dir):
    print("Starting JASON CNP...")
    os.chdir(project_dir+'/jason/src')

    # Start subprocess
    FNULL = open(os.devnull, 'w')
    p = subprocess.Popen(['jason', 'ContractNetProtocol.mas2j'], stdout=FNULL, stderr=subprocess.STDOUT, shell=False)

    # Locate PID of RunCentralisedMAS process, which is running the jason code
    pid = 0
    has_pid = False
    while(not has_pid):
        child = subprocess.Popen(['pgrep', '-f', "RunCentralisedMAS"], stdout=subprocess.PIPE, shell=False)
        response = child.communicate()[0]
        if response:
            gg = response.split()
            result = 0
            for b in gg:
                result = result * 256 + int(b)
            pid = result
            has_pid = True

    process = psutil.Process(pid)

    # Store memory and CPU usage in RAM
    text = []
    sec = 0
    while(p.poll() == None):
        cpu = psutil.cpu_percent()
        mem = process.memory_info().rss/1048576
        text.append(str(sec) +  "," + str(cpu) + "," + str(mem))
        time.sleep(1)
        sec += 1

    # Save memory and CPU usage data to results file
    os.makedirs(os.path.dirname('../benchmark/jason_results.csv'), exist_ok=True)
    with open('../../benchmark/jason_results.csv','w+') as file:
        file.write("TIME,JASON:CPU,JASON:Memory")
        file.write('\n')
        for line in text:
            file.write(line)
            file.write('\n')

    print("Finished JASON CNP...")

def main():
    project_dir = os.getcwd()
    start_jade(project_dir)
    start_jason(project_dir)

if __name__ == '__main__':
    main()
