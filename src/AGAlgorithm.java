import java.util.*;

public class AGAlgorithm {
    private final Vector<Map.Entry<Process, Map.Entry<Integer,Integer>>> processExecution = new Vector<>();
    Queue<Process> diedProcesses = new ArrayDeque<>();
    Queue<Process> readyProcesses = new ArrayDeque<>();
    private double awt;
    private double atat;

    public double getAwt() {
        return awt;
    }

    public double getAtat() {
        return atat;
    }


    Vector<Process> jobQueue;
    public Vector<Map.Entry<Process, Map.Entry<Integer,Integer>>>  getProcessExecution() {
        return processExecution;
    }


    AGAlgorithm(Vector<Process> jobQueue) {
        this.jobQueue = jobQueue;
    }


    void printQ(int t) {
        System.out.print("t = " + t);
        System.out.print(" Quantum (");
        for (Process p : jobQueue) {
            System.out.print((p.getQuantumTime()));
            if (p != jobQueue.lastElement()) System.out.print(",");
        }
        System.out.print(")");
    }

    boolean END() {
        boolean end = true;
        for (Process p : jobQueue) {
            if (p.getBurstTime() != 0){
                end = false;
                break;
            }
        }
        return end;
    }

    void buildAlgo() {
        jobQueue.sort(Comparator.comparingInt(Process::getArrivalTime));
        int i = 0;
        int t = jobQueue.get(0).getArrivalTime();
        Map.Entry<Process, Integer> CJob = getfromJob(i, t);
        Process currentProcess = CJob.getKey();
        i = CJob.getValue();
        Process currentFromJob;
        while (true) {
            //print quantum times every time
            printQ(t);
            if (END()) break;

            currentProcess.enteredTime =t;
            // Check if all processes have finished

            //print current working process
            System.out.print(">> ");
            System.out.print(currentProcess.getName());
            System.out.println(" running");

            int timeTakenByTheProcess;
            currentProcess.waitingTime = Math.max(currentProcess.waitingTime, currentProcess.getBurstTime());

            // the process will run until half of it's quantum time ends
            t += Math.min(currentProcess.getBurstTime(), (currentProcess.getQuantumTime() + 1) / 2);
            timeTakenByTheProcess = Math.min(currentProcess.getBurstTime(), (currentProcess.getQuantumTime() + 1) / 2);
            //update it's burst time
            currentProcess.setBurstTime(Math.max(0, (currentProcess.getBurstTime() - timeTakenByTheProcess)));


            if (currentProcess.getBurstTime() == 0) {
                processExecution.add(Map.entry(currentProcess, Map.entry(currentProcess.enteredTime, t)));
                calcQ(currentProcess, timeTakenByTheProcess);
                currentProcess = processCompletedBurst(currentProcess, t, timeTakenByTheProcess);
                continue;
            }


            //if there is a process with less ag factor

            CJob = getfromJob(i, t);
            i = CJob.getValue();
            Process minAG = CJob.getKey();

            if (minAG.getArrivalTime() <= t &&
                    minAG.getAGFactory() < currentProcess.getAGFactory() &&
            minAG.getBurstTime()>0) {
                if (currentProcess.getBurstTime() == 0) {
                    diedProcesses.add(currentProcess);
                    currentProcess.setQuantumTime(0);
                } else {
                    readyProcesses.add(currentProcess);
                    calcQ(currentProcess, timeTakenByTheProcess);
                }
                currentProcess.exitTime = Math.max(t, currentProcess.exitTime);
                processExecution.add(Map.entry(currentProcess, Map.entry(currentProcess.enteredTime, t)));
                currentProcess = minAG;
                continue;
            }

            boolean found = false;
            // if a new process will less AG Factor came it will take the lead
            // else the current process will continue running
            while (true) {
                t++;
                timeTakenByTheProcess++;
                currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);

                if (timeTakenByTheProcess == currentProcess.getQuantumTime()) break;
                if (currentProcess.getBurstTime() == 0) break;

                CJob = getfromJob(i, t);
                currentFromJob = CJob.getKey();
                i = CJob.getValue();

                // if a new process came
                if (currentFromJob != currentProcess &&
                currentFromJob.getAGFactory() < currentProcess.getAGFactory() &&
                currentFromJob.getBurstTime()>0) {

                    calcQ(currentProcess, timeTakenByTheProcess);
                    currentProcess.exitTime = Math.max(t, currentProcess.exitTime);
                    readyProcesses.add(currentProcess);
                    processExecution.add(Map.entry(currentProcess, Map.entry(currentProcess.enteredTime, t)));
                    currentProcess = currentFromJob;
                    found = true;
                    break;
                }

            }
            if (found) continue;

            processExecution.add(Map.entry(currentProcess, Map.entry(currentProcess.enteredTime, t)));
            //if the process finished it's burst time
            if (currentProcess.getBurstTime() == 0) {

                currentProcess = processCompletedBurst(currentProcess, t, timeTakenByTheProcess);

            }

            // if the process finished it's quantum time
            else if (timeTakenByTheProcess == currentProcess.getQuantumTime()) {
                currentProcess = processCompletedQuantumTime(currentProcess, t, timeTakenByTheProcess);
            } else {
                readyProcesses.add(currentProcess);

            }
        }
        print();
    }

    void calcQ(Process p, int timeTakenByTheProcess) {
        // if the process finished it's burst time
        if (p.getBurstTime() == 0) {
            p.setQuantumTime(0);
        }
        // if the process finished it's quantum time
        else if (timeTakenByTheProcess == p.getQuantumTime()) {
            int sumQ = 0;
            for (Process pp : jobQueue) {
                if(pp.waitingTime == pp.getBurstTime())break;
                sumQ += pp.getQuantumTime();
            }
            p.setQuantumTime(p.getQuantumTime() +
                    ((int) Math.ceil(((double) sumQ / jobQueue.size()) * 0.1)));
        } else {
            p.setQuantumTime(p.getQuantumTime() +
                    (p.getQuantumTime() - timeTakenByTheProcess));
        }
    }


    Map.Entry<Process, Integer> getfromJob(Integer index, int t) {
        if (index == jobQueue.size()) {
            return Map.entry(jobQueue.get(index - 1), index);
        }
        Process current = jobQueue.get(0);
        int i = 0;
        for (; i < jobQueue.size(); i++) {
            if (jobQueue.get(i).getArrivalTime() > t) break;
            if (current.getAGFactory() > jobQueue.get(i).getAGFactory() &&
            jobQueue.get(i).getBurstTime() > 0) {
                current = jobQueue.get(i);
            }
        }
        for (int j = index; j < i; j++) {
            if (jobQueue.get(j) != current) {
                readyProcesses.add(jobQueue.get(j));
            }
        }
        return Map.entry(current, i);
    }


    void print() {
        double sumWaiting = 0;
        double sumTurnAround = 0;
        System.out.println('\n');
        System.out.println("--------------------------------------------------------------------");
        System.out.println("Process name---------Waiting Time---------Turnaround Time");
        for (Process p : jobQueue) {
            System.out.println(p.getName() + "                          " + ((p.exitTime - p.getArrivalTime()) - p.waitingTime)
                    + "                       " + (p.exitTime - p.getArrivalTime()));
            sumWaiting += ((p.exitTime - p.getArrivalTime()) - p.waitingTime);
            sumTurnAround += (p.exitTime - p.getArrivalTime());

        }
        System.out.println("---------------------------------------------------------------------");
        System.out.println("Average waiting time is :");
        System.out.println(sumWaiting / jobQueue.size());
        System.out.println("Average turnaround time is :");
        System.out.println(sumTurnAround / jobQueue.size());
        awt = sumWaiting;
        atat = sumTurnAround;
    }

    Process processCompletedBurst(Process currentProcess, int t, int timeTakenByTheProcess) {
        diedProcesses.add(currentProcess);
        while (!readyProcesses.isEmpty()
                && readyProcesses.peek().getBurstTime() == 0)
            readyProcesses.poll();
        calcQ(currentProcess, timeTakenByTheProcess);
        currentProcess.exitTime = Math.max(t, currentProcess.exitTime);
        return readyProcesses.poll();
    }

    Process processCompletedQuantumTime(Process currentProcess,
                                        int t, int timeTakenByTheProcess) {
        while (!readyProcesses.isEmpty()
                && readyProcesses.peek().getBurstTime() == 0)
            readyProcesses.poll();
        calcQ(currentProcess, timeTakenByTheProcess);
        readyProcesses.add(currentProcess);
        currentProcess.exitTime = Math.max(t, currentProcess.exitTime);
        while (!readyProcesses.isEmpty()
                && readyProcesses.peek().getBurstTime() == 0)
            readyProcesses.poll();
        return readyProcesses.poll();
    }

}
