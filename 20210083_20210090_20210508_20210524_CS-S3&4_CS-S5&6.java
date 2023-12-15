import javax.swing.*;
import java.awt.*;
import java.util.*;

class Process {
    private String Name;
    private String Color;
    private int arrivalTime;
    private int burstTime;
    private double priorityNumber;
    private int AGFactory;
    private int quantumTime;
    public int enteredTime = -1;
    public int exitTime;
    public int waitingTime;
    private boolean executionCompleted = false;
    private int turnAroundTime;
    public int getQuantumTime() {
        return quantumTime;
    }

    public void setQuantumTime(int quantumTime) {
        this.quantumTime = quantumTime;
    }

    public int getAGFactory() {
        return AGFactory;
    }

    public void setAGFactory(int AGFactory) {
        this.AGFactory = AGFactory;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public double getPriorityNumber() {
        return priorityNumber;
    }

    public void setPriorityNumber(double priorityNumber) {
        this.priorityNumber = priorityNumber;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public boolean isExecutionCompleted() {
        return executionCompleted;
    }

    public void setExecutionCompleted(boolean executionCompleted) {
        this.executionCompleted = executionCompleted;
    }

    public int getTurnAroundTime() {
        return turnAroundTime;
    }

    public void setTurnAroundTime(int turnAroundTime) {
        this.turnAroundTime = turnAroundTime;
    }
}

class schedulerSystem {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Vector<Process> processes = new Vector<>();
        int numOfProcesses, roundRobin, contextSwitch;
        System.out.println("Enter the number of processes :");
        numOfProcesses = in.nextInt();
        System.out.println("Enter the round robin time :");
        roundRobin = in.nextInt();
        System.out.println("Enter the context switch :");
        contextSwitch = in.nextInt();
        for (int i = 0; i < numOfProcesses; i++) {
            Process process = new Process();
            System.out.println("Enter the " + (i + 1) + " process name :");
            process.setName(in.next());
            System.out.println("Enter the " + (i + 1) + " process color :");
            process.setColor(in.next());
            System.out.println("Enter the " + (i + 1) + " process arrival time  :");
            process.setArrivalTime(in.nextInt());
            System.out.println("Enter the " + (i + 1) + " process burst time :");
            process.setBurstTime(in.nextInt());
            System.out.println("Enter the " + (i + 1) + " process priority number :");

            process.setPriorityNumber(in.nextInt());
            // this part is related to the Ag algorithm
            int rand = (int) (Math.random() * 21);
            if (rand < 10) {
                process.setAGFactory(rand + process.getArrivalTime() + process.getBurstTime());
            } else if (rand == 10) {
                process.setAGFactory((int) (process.getPriorityNumber() + process.getArrivalTime() + process.getBurstTime()));
            } else {
                process.setAGFactory(10 + process.getArrivalTime() + process.getBurstTime());
            }
            process.setQuantumTime(roundRobin);
            processes.add(process);
        }

        int choice;
        System.out.println("""
                Enter the number of the scheduler you want to use :
                1. SJF scheduler\s
                2. AG scheduler\s
                3. Priority scheduler\s
                4. SRTF scheduler""");
        choice = in.nextInt();

        if (choice == 1) {
            SJFScheduler sjfScheduler = new SJFScheduler(processes, contextSwitch);
            sjfScheduler.schedule();
            guiInvoker(sjfScheduler.getProcessExecutionGui(),sjfScheduler.getAverageWaitingTime(), sjfScheduler.getAverageTurnaroundTime(), "SJF Scheduler");
        } else if (choice == 2) {
            AGAlgorithm ag = new AGAlgorithm(processes);
            ag.buildAlgo();
            guiInvoker(ag.getProcessExecution(), ag.getAwt(), ag.getAtat(), "AG Scheduler");
        } else if (choice == 3) {
            PriorityScheduler priorityScheduler = new PriorityScheduler(processes);
            priorityScheduler.schedule();
            guiInvoker(priorityScheduler.getProcessExecution(), priorityScheduler.getAwt(), priorityScheduler.getAtat(), "Priority Scheduler");
        } else if (choice == 4) {
            SRTFScheduler srtfScheduler = new SRTFScheduler(processes);
            srtfScheduler.schedule();
            guiInvoker(srtfScheduler.getProcessExecutionGui(), srtfScheduler.getAverageWaitingTime(), srtfScheduler.getAverageTurnAroundTime(), "SRTF Scheduler");
        } else {
            System.out.println("INVALID INPUT!");
        }

    }

    private static void guiInvoker(Vector<Map.Entry<Process, Map.Entry<Integer, Integer>>> processExecution,
                                   double awt, double atat, String schedulerName) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("CPU Gantt Chart");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            GanttChartPanel chartPanel = new GanttChartPanel(processExecution);

            JPanel titlePanel = new JPanel();
            titlePanel.setBounds(0, 20, 200, 30);
            titlePanel.setBackground(new Color(10, 100, 200));
            JLabel titleLabel = new JLabel();
            titleLabel.setText("Statistics of " + schedulerName);
            titlePanel.add(titleLabel);
            titleLabel.setForeground(Color.white);

            JPanel awtPanel = new JPanel();
            awtPanel.setBounds(0, 60, 200, 30);
            awtPanel.setBackground(new Color(12, 10, 100));
            JLabel awtLabel = new JLabel();
            awtLabel.setText("AWT: " + (awt));
            awtPanel.add(awtLabel);
            awtLabel.setForeground(Color.white);

            JPanel atatPanel = new JPanel();
            atatPanel.setBounds(0, 100, 200, 30);
            atatPanel.setBackground(new Color(100, 100, 100));
            JLabel atatLabel = new JLabel();
            atatLabel.setText("ATAT: " + (atat));
            atatPanel.add(atatLabel);
            atatLabel.setForeground(Color.white);

            frame.add(titlePanel);
            frame.add(awtPanel);
            frame.add(atatPanel);

            frame.add(chartPanel);
            frame.setSize(1500, 700);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
class SJFScheduler {
    private final Vector<Process> processes;
    private final Map<Process, Map<String,Integer>> processExecution;
    private final Vector<Map.Entry<Process, Map.Entry<Integer,Integer>>> processExecutionGui
            = new Vector<>();

    private final int contextSwitchTime;
    private double averageWaitingTime;
    private double averageTurnaroundTime;

    public SJFScheduler(Vector<Process> processes, int contextSwitch){
        processExecution = new LinkedHashMap<>();
        averageWaitingTime = 0.0;
        averageTurnaroundTime = 0.0;
        this.processes = processes;
        contextSwitchTime = contextSwitch;
    }
    public void schedule(){
        processes.sort(this::compareByArrivalTime);
        int currentTime = processes.get(0).getArrivalTime();

        while(!processes.isEmpty()){
            Vector<Process> readyQueue = new Vector<>();

            for(Process process: processes){
                if(process.getArrivalTime() <= currentTime){
                    readyQueue.add(process);
                }
            }

            readyQueue.sort(this::compareByBurstTime);
            Process currentProcess = readyQueue.get(0);
            int arrivalTime = currentProcess.getArrivalTime();
            int waitingTime = currentTime - arrivalTime;
            int finishTime = currentTime + currentProcess.getBurstTime();
            int turnaroundTime = finishTime - arrivalTime;


            processExecution.put(currentProcess, new HashMap<>());

            processExecution.get(currentProcess).put("startTime",currentTime);
            processExecution.get(currentProcess).put("finishTime",finishTime);
            processExecution.get(currentProcess).put("waitingTime",waitingTime);
            processExecution.get(currentProcess).put("turnaroundTime",turnaroundTime);
            processExecutionGui.add(Map.entry(currentProcess, Map.entry(currentTime, finishTime)));



            processes.remove(currentProcess);
            currentTime = finishTime + contextSwitchTime;
        }
        calculateAverageTurnaroundTime();
        calculateAverageWaitingTime();
        print();
    }
    private void print(){
        System.out.println("Average waiting time =  " + averageWaitingTime);
        System.out.println("Average turnaround time =  " + averageTurnaroundTime);
        System.out.println();
        System.out.println("Processes execution : ");
        for (Map.Entry<Process, Map<String ,Integer>> entry : processExecution.entrySet()) {
            Process process = entry.getKey();
            System.out.print(process.getName() + " ");
        }
        System.out.println('\n');
        for (Map.Entry<Process, Map<String ,Integer>> entry : processExecution.entrySet()) {
            Process process = entry.getKey();
            Map<String ,Integer> values = entry.getValue();
            System.out.println("====Process: " + process.getName() + "===="+
                    "\nStart time: " + values.get("startTime") +
                    "\nFinish time " + values.get("finishTime")  +
                    "\nWaiting time "+ values.get("waitingTime")  +
                    "\nTurnaround time " + values.get("turnaroundTime")  + '\n');
        }
    }

    private int compareByArrivalTime(Process a, Process b){
        return Integer.compare(a.getArrivalTime() ,b.getArrivalTime());
    }
    private int compareByBurstTime(Process a, Process b){
        return Integer.compare(a.getBurstTime(),b.getBurstTime());
    }

    private void calculateAverageTurnaroundTime() {
        for (Map<String ,Integer> processData : processExecution.values()) {
            averageTurnaroundTime += processData.get("turnaroundTime");
        }
        averageTurnaroundTime /= processExecution.size();
    }

    private void calculateAverageWaitingTime() {
        for (Map<String ,Integer> processData : processExecution.values()) {
            averageWaitingTime += processData.get("waitingTime");
        }
        averageWaitingTime /= processExecution.size();
    }

    public Vector<Process> getProcesses() {
        return processes;
    }

    public Map<Process, Map<String, Integer>> getProcessExecution() {
        return processExecution;
    }
    public Vector<Map.Entry<Process, Map.Entry<Integer,Integer>>> getProcessExecutionGui() {
        return processExecutionGui;
    }

    public int getContextSwitchTime() {
        return contextSwitchTime;
    }

    public double getAverageWaitingTime() {
        return averageWaitingTime;
    }

    public double getAverageTurnaroundTime() {
        return averageTurnaroundTime;
    }
}
class SRTFProcess {
    int remainingTime;
    int swapsCounter = 0;
    Process process;

    public SRTFProcess(int remainingTime, Process process) {
        this.remainingTime = remainingTime;
        this.process = process;
    }
}

class SRTFScheduler {
    private final Vector<Process> processes;
    private final Vector<SRTFProcess> processesExecution;
    private final Vector<SRTFProcess> executionOrder;
    private final Vector<Map.Entry<Process, Map.Entry<Integer, Integer>>> processExecutionGui;
    private final int maxSwaps = 10;
    private double averageWaitingTime;
    private double averageTurnAroundTime;

    public Vector<Map.Entry<Process, Map.Entry<Integer, Integer>>> getProcessExecutionGui() {

        return processExecutionGui;
    }

    public double getAverageWaitingTime() {
        return averageWaitingTime;
    }

    public double getAverageTurnAroundTime() {
        return averageTurnAroundTime;
    }

    public SRTFScheduler(Vector<Process> processes) {
        this.processes = processes;
        this.processesExecution = new Vector<>();
        this.executionOrder = new Vector<>();
        this.processExecutionGui = new Vector<>();
        averageWaitingTime = 0.0;
        averageTurnAroundTime = 0.0;
        for (Process process : processes) {
            processesExecution.add(new SRTFProcess(process.getBurstTime(), process));
        }
        processesExecution.sort(this::compareByArrivalTime);
    }


    public void schedule() {
        int timeCounter = 0;
        int executionCompletedProcesses = 0;
        SRTFProcess currentSRTFProcess = null;
        int currentPairIdx = -1;
        while (executionCompletedProcesses < processes.size()) {
            SRTFProcess tmp = null;
            if (currentSRTFProcess == null) { // first process
                currentSRTFProcess = processesExecution.get(0);
                currentSRTFProcess.process.enteredTime = timeCounter;
            } else {
                SRTFProcess tmpSRTFProcess = getMinRemainingTimeProcess(currentSRTFProcess, timeCounter);
                if (tmpSRTFProcess != null) {
                    currentSRTFProcess.swapsCounter++; // finish process
                    tmp = currentSRTFProcess;
                    tmp.process.exitTime = timeCounter;
                    currentSRTFProcess = tmpSRTFProcess;
                    currentSRTFProcess.process.enteredTime = timeCounter;
                }
            }

            currentPairIdx = processesExecution.indexOf(currentSRTFProcess);
            currentSRTFProcess.remainingTime--;

            if (currentSRTFProcess.remainingTime == 0) {
                executionCompletedProcesses++;
                processesExecution.get(currentPairIdx).process.exitTime = timeCounter + 1;
                processesExecution.get(currentPairIdx).process.setExecutionCompleted(true);
                tmp = processesExecution.get(currentPairIdx);
            }

            timeCounter++;

            if (executionOrder.isEmpty() || executionOrder.lastElement() != currentSRTFProcess) {
                executionOrder.add(processesExecution.get(currentPairIdx));
            }

            if (tmp != null && (processExecutionGui.isEmpty() ||
                    !processExecutionGui.lastElement().equals(Map.entry(tmp.process, Map.entry(tmp.process.enteredTime, tmp.process.exitTime))))) {
                processExecutionGui.add(Map.entry(tmp.process, Map.entry(tmp.process.enteredTime, tmp.process.exitTime)));
            }
        }
        //  processExecutionGui.add(Map.entry(currentSRTFProcess.process, Map.entry(currentSRTFProcess.process.enteredTime, currentSRTFProcess.process.exitTime)));
        calculateAverageTurnaroundTime();
        calculateAverageWaitingTime();
        print();
    }

    private void calculateAverageTurnaroundTime() {
        for (SRTFProcess SRTFProcess : processesExecution) {
            averageTurnAroundTime += (SRTFProcess.process.exitTime - SRTFProcess.process.getArrivalTime());
            SRTFProcess.process.setTurnAroundTime(SRTFProcess.process.exitTime - SRTFProcess.process.getArrivalTime());
        }
        averageTurnAroundTime /= processes.size();
    }

    private void calculateAverageWaitingTime() {
        for (SRTFProcess SRTFProcess : processesExecution) {
            averageWaitingTime += (SRTFProcess.process.getTurnAroundTime() - SRTFProcess.process.getBurstTime());
            SRTFProcess.process.waitingTime = SRTFProcess.process.getTurnAroundTime() - SRTFProcess.process.getBurstTime();
        }
        averageWaitingTime /= processes.size();
    }

    private SRTFProcess getMinRemainingTimeProcess(SRTFProcess currentExecutionSRTFProcess, int currentTime) {
        int minRemainingTime = (currentExecutionSRTFProcess.remainingTime == 0 ? 1000000000 : currentExecutionSRTFProcess.remainingTime);
        SRTFProcess currentSRTFProcess = null;
        for (SRTFProcess SRTFProcess : processesExecution) {
            if (SRTFProcess != currentExecutionSRTFProcess
                    && SRTFProcess.process.getArrivalTime() <= currentTime && SRTFProcess.swapsCounter == maxSwaps) {
                return SRTFProcess;
            }
            if (SRTFProcess.remainingTime < minRemainingTime && !SRTFProcess.process.isExecutionCompleted()
                    && SRTFProcess != currentExecutionSRTFProcess && SRTFProcess.process.getArrivalTime() <= currentTime) {
                currentSRTFProcess = SRTFProcess;
                minRemainingTime = SRTFProcess.remainingTime;
            }
        }
        return currentSRTFProcess;
    }

    private int compareByArrivalTime(SRTFProcess a, SRTFProcess b) {
        return Integer.compare(a.process.getArrivalTime(), b.process.getArrivalTime());
    }

    private void print() {
        System.out.println('\n');
        System.out.println("Execution Order :");
        for (Map.Entry<Process, Map.Entry<Integer, Integer>> entry : processExecutionGui) {
            Process process = entry.getKey();
            Map.Entry<Integer, Integer> values = entry.getValue();
            System.out.println("====Process: " + process.getName() + "====" +
                    "\nStart time: " + values.getKey() +
                    "\nFinish time " + values.getValue());
        }
        System.out.println('\n');

        for (SRTFProcess SRTFProcess : processesExecution) {

            System.out.println("==== Process: " + SRTFProcess.process.getName() + " ====" +
                    "\nWaiting time = " + SRTFProcess.process.waitingTime +
                    "\nTurnaround time = " + SRTFProcess.process.getTurnAroundTime() + '\n');
        }
        System.out.println("\nAverage waiting time =  " + averageWaitingTime);
        System.out.println("Average turnaround time =  " + averageTurnAroundTime);


    }
}
class AGAlgorithm {
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

            currentProcess.enteredTime = t;
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
            Process minAG = currentProcess;
            for (Process process : jobQueue) {
                if (process.getArrivalTime() > t) break;
                if (minAG.getAGFactory() > process.getAGFactory() &&
                        process.getBurstTime() > 0) {
                    minAG = process;
                }
            }

            if (minAG != currentProcess) {
                if (currentProcess.getBurstTime() == 0) {
                    diedProcesses.add(currentProcess);
                    currentProcess.setQuantumTime(0);
                } else {
                    readyProcesses.add(currentProcess);
                    calcQ(currentProcess, timeTakenByTheProcess);
                }
                currentProcess.exitTime = Math.max(t, currentProcess.exitTime);
                processExecution.add(Map.entry(currentProcess,
                        Map.entry(currentProcess.enteredTime, t)));
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

                CJob = getfromJob(i, t);
                currentFromJob = CJob.getKey();
                i = CJob.getValue();

                if (timeTakenByTheProcess == currentProcess.getQuantumTime()) break;
                if (currentProcess.getBurstTime() == 0) break;

                // if a new process came
                if (currentFromJob != currentProcess &&
                        currentFromJob.getAGFactory() < currentProcess.getAGFactory() &&
                        currentFromJob.getBurstTime() > 0) {

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
        System.out.println("Execution Order :");
        for (Map.Entry<Process, Map.Entry<Integer, Integer>> entry : processExecution) {
            Process process = entry.getKey();
            Map.Entry<Integer, Integer>values = entry.getValue();
            System.out.println("====Process: " + process.getName() + "===="+
                    "\nStart time: " + values.getKey() +
                    "\nFinish time " + values.getValue());
        }
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
        awt = sumWaiting / jobQueue.size();
        atat = sumTurnAround / jobQueue.size();
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
class PriorityScheduler {
    private Vector<Process> processes;
    private Vector<Map.Entry<Process, Map.Entry<Integer,Integer>>> processExecution = new Vector<>();
    private double agingFactor;
    private double awt;
    private double atat;

    public PriorityScheduler(Vector<Process> processes) {
        this.processes = processes;
        agingFactor = 0.1;
    }
    public void schedule() {
        int totalTime = 100000;

        double waitingAvg = 0;
        double turnaroundAvg = 0;
        String output = "";
        ArrayList<String> executionOrder = new ArrayList<>();
        for (int i = 0; i < totalTime; i++) {
            Process tmpProcess = null;
            for (int j = 0; j < processes.size(); j++) {
                Process curProcess = processes.get(j);
                if (curProcess.getArrivalTime() <= i) {
                    if (tmpProcess != null) {
                        if (tmpProcess.getPriorityNumber() > curProcess.getPriorityNumber()) {
                            tmpProcess = curProcess;
                        } else if (tmpProcess.getPriorityNumber() == curProcess.getPriorityNumber()) {
                            if (tmpProcess.getArrivalTime() > curProcess.getArrivalTime()) {
                                tmpProcess = curProcess;
                            }
                        }
                    } else {
                        tmpProcess = curProcess;
                    }
                }
            }

            if (tmpProcess != null) {
                executionOrder.add(tmpProcess.getName());

                output += ("Process " + tmpProcess.getName() +
                        " turnaround time: " + (i + tmpProcess.getBurstTime() - tmpProcess.getArrivalTime()) + "\n");
                output += ("Process " + tmpProcess.getName() +
                        " waiting time: " + (i - tmpProcess.getArrivalTime()) + "\n");

                turnaroundAvg += i + tmpProcess.getBurstTime() - tmpProcess.getArrivalTime();
                waitingAvg += i - tmpProcess.getArrivalTime();

                // aging
                for (int j = i; j < i + tmpProcess.getBurstTime(); j++) {
                    if (j > 0 && j % 2 == 0) {
                        for (int k = 0; k < processes.size(); k++) {
                            Process process = processes.get(k);
                            if (process == tmpProcess || process.getArrivalTime() == 1000000000
                                    || process.getArrivalTime() > i) continue;

                            if (process.getPriorityNumber() > 0) {
                                processes.get(k).setPriorityNumber(process.getPriorityNumber() -
                                        (agingFactor * (i - process.getArrivalTime())));
                            }
                        }
                    }
                }

                processExecution.add(Map.entry(tmpProcess, Map.entry(i, i + tmpProcess.getBurstTime())));

                i += tmpProcess.getBurstTime() - 1;

                // set arrival time to a large number so that it is marked as visited
                tmpProcess.setArrivalTime(1000000000);
            }
        }

        for (int i = 0; i < executionOrder.size(); i++) {
            System.out.print(executionOrder.get(i) + " ");
        }
        System.out.println("\n");

        System.out.println(output);

        System.out.println("Average Turnaround Time: " + (turnaroundAvg / processes.size()));
        System.out.println("Average Waiting Time: " + (waitingAvg / processes.size()));

        awt = waitingAvg / processes.size();
        atat = turnaroundAvg / processes.size();

        System.out.println('\n');
        System.out.println("Execution Order :");
        for (Map.Entry<Process, Map.Entry<Integer, Integer>> entry : processExecution) {
            Process process = entry.getKey();
            Map.Entry<Integer, Integer>values = entry.getValue();
            System.out.println("====Process: " + process.getName() + "===="+
                    "\nStart time: " + values.getKey() +
                    "\nFinish time " + values.getValue());
        }
        System.out.println('\n');
    }

    public Vector<Map.Entry<Process, Map.Entry<Integer, Integer>>> getProcessExecution() {
        return processExecution;
    }
    public double getAwt() {
        return awt;
    }

    public double getAtat() {
        return atat;
    }
}
class GanttChartPanel extends JPanel {
    private final Vector<Map.Entry<Process, Map.Entry<Integer,Integer>>> processExecution;
    Map<String, Color> colorMap = new HashMap<>();
    private int maxFinish = -1;
    public GanttChartPanel(Vector<Map.Entry<Process, Map.Entry<Integer,Integer>>> processExecution){
        this.processExecution = processExecution;

        for (Map.Entry<Process, Map.Entry<Integer, Integer>> entry : processExecution) {
            Map.Entry<Integer, Integer> values = entry.getValue();
            maxFinish = Math.max(maxFinish,values.getKey());
        }
        colorMap.put("red", new Color(255, 0, 0));
        colorMap.put("green", new Color(0, 255, 0));
        colorMap.put("blue", new Color(0, 0, 255));
        colorMap.put("yellow", new Color(255, 255, 0));
        colorMap.put("purple", new Color(128, 0, 128));
        colorMap.put("orange", new Color(255, 165, 0));
        colorMap.put("cyan", new Color(0, 255, 255));
        colorMap.put("magenta", new Color(255, 0, 255));
        colorMap.put("pink", new Color(255, 192, 203));
        colorMap.put("brown", new Color(165, 42, 42));
        colorMap.put("gray", new Color(128, 128, 128));
        colorMap.put("white", new Color(255, 255, 255));
        colorMap.put("black", new Color(0, 0, 0));
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int x = 250; // Initial x-coordinate for Gantt chart bars
        int y = 20; // Initial y-coordinate for Gantt chart bars

        // Draw horizontal lines (time)
        for (int i = 0; i <= maxFinish*10; i++) {
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(x + i * 10, 10, x + i * 10, y + processExecution.size() * 40);
            g.setColor(Color.BLACK);

            // Draw time labels with some spacing
            if (i % 5 == 0) {
                g.drawString(Integer.toString(i), x + i * 10 - 5, 10);
            }
        }

        g.drawString("Process Info: ", 0, 150);
        g.drawString("Name: ", 10, 170);
        g.drawString("Color: ", 60, 170);
        g.drawString("Priority: ", 120, 170);

        // Draw vertical lines (process names)
        Set<String> processSet = new HashSet<>();

        for (Map.Entry<Process, Map.Entry<Integer, Integer>> entry : processExecution) {
            Process process = entry.getKey();
            if(!processSet.contains(process.getName())){
                for (Map.Entry<Process, Map.Entry<Integer, Integer>> sameEntry : processExecution){
                    if(process.getName().equals(sameEntry.getKey().getName())){
                        Map.Entry<Integer, Integer> values = sameEntry.getValue();
                        int startTime = values.getKey();
                        int finishTime = values.getValue();
                        int width = (finishTime - startTime) * 10;
                        Color color = colorMap.getOrDefault(process.getColor(), Color.BLACK);
                        // Set the color for drawing
                        g.setColor(color);
                        g.fillRect(x + startTime * 10, y, width, 30);
                    }
                }
                g.setColor(Color.BLACK);
                g.drawString(process.getName(), 230, y + 20);
                g.drawString(process.getName(), 10, y + 170);
                g.drawString(process.getColor(), 60, y + 170);
                String priority = Double.toString(process.getPriorityNumber());
                g.drawString(priority, 120, y + 170);
                y += 40;
            }
            processSet.add(process.getName());
        }
    }
}

