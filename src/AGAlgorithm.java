import java.util.*;

public class AGAlgorithm {
    Queue<Process> diedProcesses = new ArrayDeque<>();
    Queue<Process> readyProcesses = new ArrayDeque<>();
    Vector<Process> jobQueue;

    AGAlgorithm(Vector<Process> jobQueue) {
        this.jobQueue = jobQueue;
    }


    void printQ() {
        System.out.print("Quantum (");
        for (Process p : jobQueue) {
            System.out.print((p.getQuantumTime()));
            if (p != jobQueue.lastElement()) System.out.print(",");
        }
        System.out.print(")");
    }

    void buildAlgo() {
        int i = 0;
        Process currentProcess = jobQueue.elementAt(i++);
        Process currentFromJob = null;
        int t = 0;
        while (true) {
            //print quantum times every time
            printQ();

            // Check if all processes have finished
            boolean end = true;
            for (Process p : jobQueue) {
                if (p.getBurstTime() != 0)
                    end = false;
            }
            if (end) break;


            //print current working process
            System.out.print(">> ");
            System.out.print(currentProcess.getName());
            System.out.println(" running");

            int timeTakenByTheProcess = 0;
            currentProcess.waitingTime = Math.max(currentProcess.waitingTime, currentProcess.getBurstTime());
            if(currentProcess.enteredTime == -1)
                currentProcess.enteredTime = t;

            // the process will run until half of it's quantum time ends
            t += Math.min(currentProcess.getBurstTime(),
                    (currentProcess.getQuantumTime() + 1) / 2);
            timeTakenByTheProcess = Math.min(currentProcess.getBurstTime(),
                    (currentProcess.getQuantumTime() + 1) / 2);
            //update it's burst time
            currentProcess.
                    setBurstTime(Math.max(0, (currentProcess.getBurstTime() - timeTakenByTheProcess)));

            if (currentProcess.getBurstTime() == 0) {
                currentProcess.setQuantumTime(0);
                currentProcess.exitTime = Math.max(t, currentProcess.exitTime);
                while (!readyProcesses.isEmpty()
                        && readyProcesses.peek().getBurstTime() == 0)
                    readyProcesses.poll();
                if (!readyProcesses.isEmpty()) {
                    currentProcess = readyProcesses.poll();

                }else{
                    currentProcess = currentFromJob;
                }
                continue;
            }
            //if there is a process with less ag factor
            Process minAG = currentProcess;
            for (Process p : jobQueue) {
                if ((t >= p.getArrivalTime() &&
                        minAG.getAGFactory() > p.getAGFactory()) &&
                        p.getBurstTime() != 0) {
                    minAG = p;
                }
            }
            if (minAG != currentProcess) {
                if (i + 1 < jobQueue.size()) i++;
                if (currentProcess.getBurstTime() == 0) {
                    diedProcesses.add(currentProcess);
                    currentProcess.setQuantumTime(0);
                } else {
                    readyProcesses.add(currentProcess);
                }
                calcQ(currentProcess, timeTakenByTheProcess);
                currentProcess.exitTime = Math.max(t, currentProcess.exitTime);
                currentProcess = minAG;
                continue;
            }
            if (currentFromJob!=null&&
                    currentFromJob.getArrivalTime() <= t &&
                    currentFromJob.getAGFactory() >= currentProcess.getAGFactory()) {
                readyProcesses.add(currentFromJob);
                if (i + 1 < jobQueue.size()) i++;
            }

            //process didn't finish yet
            // check the ready queue if there is new process came with less AGFactor
            // will do it every sec

            currentFromJob = jobQueue.elementAt(i);
            boolean found = false;
            // if a new process will less AG Factor came it will take the lead
            // else the current process will continue running
            while (true) {
                t++;
                timeTakenByTheProcess++;
                currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
                // if a new process came
                if (currentFromJob.getArrivalTime() <= t &&
                        currentFromJob.getAGFactory() < currentProcess.getAGFactory()) {
                    if (currentProcess.getBurstTime() != 0)
                        readyProcesses.add(currentProcess);
                    calcQ(currentProcess, timeTakenByTheProcess);
                    currentProcess.exitTime = Math.max(t, currentProcess.exitTime);
                    currentProcess = currentFromJob;
                    if (i + 1 < jobQueue.size()) i++;
                    currentFromJob = jobQueue.get(i);
                    found = true;
                    break;
                }
                if (currentFromJob.getArrivalTime() <= t &&
                        currentFromJob.getAGFactory() >= currentProcess.getAGFactory()) {
                    readyProcesses.add(currentFromJob);
                    if (i + 1 < jobQueue.size()) i++;
                }
                if (timeTakenByTheProcess == currentProcess.getQuantumTime()) break;
                if (currentProcess.getBurstTime() == 0) break;
            }
            if (found) continue;

            //if the process finished it's burst time
            if (currentProcess.getBurstTime() == 0) {
                diedProcesses.add(currentProcess);
                while (!readyProcesses.isEmpty()
                        && readyProcesses.peek().getBurstTime() == 0)
                    readyProcesses.poll();
                calcQ(currentProcess, timeTakenByTheProcess);
                currentProcess.exitTime = Math.max(t, currentProcess.exitTime);
                if (readyProcesses.isEmpty()) currentProcess = currentFromJob;
                else currentProcess = readyProcesses.poll();
            }

            // if the process finished it's quantum time
            else if (timeTakenByTheProcess == currentProcess.getQuantumTime()) {
                if (currentProcess.getBurstTime() != 0)
                    readyProcesses.add(currentProcess);
                while (!readyProcesses.isEmpty()
                        && readyProcesses.peek().getBurstTime() == 0)
                    readyProcesses.poll();
                calcQ(currentProcess, timeTakenByTheProcess);
                currentProcess.exitTime = Math.max(t, currentProcess.exitTime);
                currentProcess = readyProcesses.poll();
            } else {
                if (currentProcess.getBurstTime() != 0)
                    readyProcesses.add(currentProcess);

            }
        }
        print();
    }

    void calcQ(Process p, int timeTakenByTheProcess) {
        // if the process finished it's burst time
        if (p.getBurstTime() == 0) {
            p.setQuantumTime(0);
            diedProcesses.add(p);

        }

        // if the process finished it's quantum time
        else if (timeTakenByTheProcess == p.getQuantumTime()) {
            int sumQ = 0;
            for (Process pp : jobQueue) {
                sumQ += pp.getQuantumTime();
            }
            p.setQuantumTime(p.getQuantumTime() +
                    ((int) Math.ceil(((double) sumQ / jobQueue.size()) * 0.1)));
        } else {
            p.setQuantumTime(p.getQuantumTime() +
                    (p.getQuantumTime() - timeTakenByTheProcess));
        }
    }


    void print() {
        int sumWaiting = 0;
        int sumTurnAround = 0;
        System.out.println('\n');
        System.out.println("--------------------------------------------------------------------");
        System.out.println("Process name---------Waiting Time---------Turnaround Time");
        for (Process p : jobQueue) {
            System.out.println(p.getName() + "                          " + ((p.exitTime - p.enteredTime) - p.waitingTime)
                    + "                       " + (p.exitTime - p.enteredTime));
            sumWaiting += ((p.exitTime - p.enteredTime) - p.waitingTime);
            sumTurnAround += (p.exitTime - p.enteredTime);
        }
        System.out.println("---------------------------------------------------------------------");
        System.out.println("Average waiting time is :");
        System.out.println(sumWaiting / jobQueue.size());
        System.out.println("Average turnaround time is :");
        System.out.println(sumTurnAround / jobQueue.size());
    }

}
