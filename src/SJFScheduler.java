import java.util.*;

public class SJFScheduler {
    private final Vector<Process> processes;
    private final Map<Process, Map<String,Integer>> processExecution;
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
        int currentTime = Integer.parseInt(processes.get(0).getArrivalTime());

        while(!processes.isEmpty()){
            Vector<Process> readyQueue = new Vector<>();

            for(Process process: processes){
                if(Integer.parseInt(process.getArrivalTime()) <= currentTime){
                    readyQueue.add(process);
                }
            }

            readyQueue.sort(this::compareByBurstTime);
            Process currentProcess = readyQueue.get(0);
            int arrivalTime = Integer.parseInt(currentProcess.getArrivalTime());
            int waitingTime = currentTime - arrivalTime;
            int finishTime = currentTime + Integer.parseInt(currentProcess.getBurstTime());
            int turnaroundTime = finishTime - arrivalTime;


            processExecution.put(currentProcess, new HashMap<>());

            processExecution.get(currentProcess).put("startTime",currentTime);
            processExecution.get(currentProcess).put("finishTime",finishTime);
            processExecution.get(currentProcess).put("waitingTime",waitingTime);
            processExecution.get(currentProcess).put("turnaroundTime",turnaroundTime);


            processes.remove(currentProcess);
            currentTime = finishTime + contextSwitchTime;
        }
        setAverageWaitingTime();
        setAverageTurnaroundTime();
        print();
    }
    private void print(){
        System.out.println("Average waiting time =  " + averageWaitingTime);
        System.out.println("Average turnaround time =  " + averageTurnaroundTime);
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
        return Integer.compare(Integer.parseInt(a.getArrivalTime()) , Integer.parseInt(b.getArrivalTime()));
    }
    private int compareByBurstTime(Process a, Process b){
        return Integer.compare(Integer.parseInt(a.getBurstTime()), Integer.parseInt(b.getBurstTime()));
    }

    public void setAverageTurnaroundTime() {
        for (Map<String ,Integer> processData : processExecution.values()) {
            averageTurnaroundTime += processData.get("turnaroundTime");
        }
        averageTurnaroundTime /= processExecution.size();
    }

    public void setAverageWaitingTime() {
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
