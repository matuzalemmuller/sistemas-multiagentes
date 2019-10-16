import os
import psutil
import subprocess
import time
from threading import Thread

def start_jade():
    os.chdir('jade')
    FNULL = open(os.devnull, 'w')
    p = subprocess.Popen(['java', '-cp', "lib/jade-4.3.jar:.", "test.Tester"], stdout=FNULL, stderr=subprocess.STDOUT, shell=False)
    pid = p.pid
    process = psutil.Process(pid)
    
    while(p.poll() == None):
        mem = process.memory_info().rss/1048576
        cpu = psutil.cpu_percent()
        print(str(cpu) + " , " + str(mem))
        time.sleep(1)


def start_jason():
    # os.chdir('jade')
    # FNULL = open(os.devnull, 'w')
    # p = subprocess.Popen(['java', '-cp', "lib/jade-4.3.jar:.", "test.Tester"], stdout=FNULL, stderr=subprocess.STDOUT, shell=False)
    # pid = p.pid
    # process = psutil.Process(pid)
    
    # while(p.poll() == None):
    #     mem = process.memory_info().rss/1048576
    #     cpu = psutil.cpu_percent()
    #     print(str(cpu) + " , " + str(mem))
    #     time.sleep(1)
    pass


def main():
    jade_thread = Thread(target = start_jade)
    jade_thread.start()
    jade_thread.join()

    jason_thread = Thread(target = start_jason)
    jason_thread.start()
    jason_thread.join()


if __name__ == '__main__':
    main()