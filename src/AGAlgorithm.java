import java.util.*;

public class AGAlgorithm {
    Queue<Process> diedProcesses = new ArrayDeque<>();
    Queue<Process> readyProcesses = new ArrayDeque<>();


    AGAlgorithm(Vector<Process> jobQueue) {
        int i = 0;
        Process currentProcess = jobQueue.elementAt(i++);
        Process currentFromJob = null;
        int t = 0;
        System.out.println("-----------------------------------------------");
        while (true) {
            System.out.print("Quantum (");
            for (Process p : jobQueue) {
                System.out.print((p.getQuantumTime()));
                if (p != jobQueue.lastElement()) System.out.print(",");
            }
            System.out.print(")");

            // Check if all processes have finished
            boolean end = true;
            for (Process p : jobQueue) {
                if (p.getBurstTime() != 0)
                    end = false;
            }
            if (end) break;
            System.out.print(">> ");
            System.out.print(currentProcess.getName());
            System.out.println(" running");
            int timeTakenByTheProcess = 0;
            Process old = currentProcess;
            old.waitingTime = Math.max(old.waitingTime , old.getBurstTime());
            old.enteredTime = Math.min(t,old.enteredTime);
            //will represent the current second
            // the process will run until half of it's quantum time ends
            t += Math.min(currentProcess.getBurstTime(),
                    (currentProcess.getQuantumTime() + 1) / 2);
            timeTakenByTheProcess = Math.min(currentProcess.getBurstTime(),
                    (currentProcess.getQuantumTime() + 1) / 2);
            //update it's burst time
            currentProcess.
                    setBurstTime(Math.max(0, (currentProcess.getBurstTime() - timeTakenByTheProcess)));

            //after 1/2 of the current process quantum time
            // check if a new process appeared
            //if current process finished it's burst time
            if (currentProcess.getBurstTime() == 0) {
                diedProcesses.add(currentProcess);
                currentProcess.setQuantumTime(0);
                if (!readyProcesses.isEmpty()) {
                    while (!readyProcesses.isEmpty()
                            && readyProcesses.peek().getBurstTime() == 0)
                        readyProcesses.poll();
                    currentProcess = readyProcesses.poll();
                } else {
                    if (i < jobQueue.size()) currentProcess = jobQueue.elementAt(i++);
                    t += (currentProcess.getArrivalTime() - t);
                }
            }
            //process didn't finish yet
            // check the ready queue if there is new process came with less AGFactor
            // will do it every sec
            else {
                if (i < jobQueue.size()) currentFromJob = jobQueue.elementAt(i++);
                boolean found = false;
                // if a new process will less AG Factor came it will take the lead
                // else the current process will continue running
                while ((t < currentFromJob.getArrivalTime() ||
                        currentFromJob.getAGFactory() >= currentProcess.getAGFactory())) {
                    found = false;
                    for (Process p : jobQueue) {
                        if (p.getAGFactory() < currentProcess.getAGFactory() &&
                                p.getBurstTime() != 0 && p.getArrivalTime() <= t) {
                            if (!found) {
                                currentProcess.setQuantumTime(currentProcess.getQuantumTime() +
                                        (currentProcess.getQuantumTime() - timeTakenByTheProcess));
                                if (currentProcess.getBurstTime() != 0)
                                    readyProcesses.add(currentProcess);
                                old.exitTime = Math.max(t,old.exitTime);
                            }
                            currentProcess = p;
                            found = true;
                        }
                    }
                    if (found) break;
                    t++;
                    timeTakenByTheProcess++;
                    currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
                    if (timeTakenByTheProcess == currentProcess.getQuantumTime()) break;
                    if (currentProcess.getBurstTime() == 0) break;
                    // if a new process will greater AG Factor came
                    if (currentFromJob.getArrivalTime() <= t &&
                            currentFromJob.getAGFactory() > currentProcess.getAGFactory()) {
                        if (currentProcess.getBurstTime() != 0)
                            readyProcesses.add(currentFromJob);
                        if (i < jobQueue.size())
                            currentFromJob = jobQueue.elementAt(i++);
                    }
                }
                if (found) continue;
                //if the process finished it's burst time
                if (currentProcess.getBurstTime() == 0) {
                    currentProcess.setQuantumTime(0);
                    diedProcesses.add(currentProcess);
                    if (currentFromJob.getArrivalTime() <= t)
                        currentProcess = currentFromJob;
                    else {
                        while (!readyProcesses.isEmpty()
                                && readyProcesses.peek().getBurstTime() == 0)
                            readyProcesses.poll();
                        currentProcess = readyProcesses.poll();
                    }

                }
                // if the process finished it's quantum time
                else if (timeTakenByTheProcess == currentProcess.getQuantumTime()) {
                    int sumQ = 0;
                    for (Process p : jobQueue) {
                        sumQ += p.getQuantumTime();
                    }
                    currentProcess.setQuantumTime(currentProcess.getQuantumTime() +
                            ((int) Math.ceil(((double) sumQ / jobQueue.size()) * 0.1)));
                    if (currentProcess.getBurstTime() != 0)
                        readyProcesses.add(currentProcess);
                    while (!readyProcesses.isEmpty()
                            && readyProcesses.peek().getBurstTime() == 0)
                        readyProcesses.poll();
                    currentProcess = readyProcesses.poll();
                } else {
                    if (currentProcess.getBurstTime() != 0)
                        readyProcesses.add(currentProcess);
                    currentProcess.setQuantumTime(currentProcess.getQuantumTime() +
                            (currentProcess.getQuantumTime() - timeTakenByTheProcess));
                    if (currentFromJob.getArrivalTime() <= t)
                        currentProcess = currentFromJob;
                }
            }
            old.exitTime = Math.max(t,old.exitTime);
        }
        int sumWaiting = 0;
        int sumTurnAround = 0;
        System.out.println('\n');
        System.out.println("--------------------------------------------------------------------");
        System.out.println("Process name---------Waiting Time---------Turnaround Time");
        for(Process p : jobQueue){
            System.out.println(p.getName() + "                          "+((p.exitTime - p.enteredTime) - p.waitingTime)
                    +"                       "+(p.exitTime - p.enteredTime));
            sumWaiting += ((p.exitTime - p.enteredTime) - - p.waitingTime);
            sumTurnAround += (p.exitTime - p.enteredTime);
        }
        System.out.println("---------------------------------------------------------------------");
        System.out.println("Average waiting time is :");
        System.out.println(sumWaiting/jobQueue.size());
        System.out.println("Average turnaround time is :");
        System.out.println(sumTurnAround/jobQueue.size());
    }

}
