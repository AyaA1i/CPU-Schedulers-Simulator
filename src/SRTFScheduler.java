import java.util.*;

// preemptive
// solution of starvation problem

public class SRTFScheduler {
    private final Vector<Process> processes;
    private final Vector<SRTFProcess> processesExecution;
    private final Vector<SRTFProcess> executionOrder;
    private final Vector<Map.Entry<Process, Map.Entry<Integer,Integer>>> processExecutionGui;
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
        for (Process process: processes) {
            processesExecution.add(new SRTFProcess(process.getBurstTime(), process));
        }
        processesExecution.sort(this::compareByArrivalTime);
    }


    public void schedule(){
        int timeCounter = 0;
        int  executionCompletedProcesses = 0;
        SRTFProcess currentSRTFProcess = null;
        int currentPairIdx = -1;
        while (executionCompletedProcesses < processes.size()){
            SRTFProcess tmp = null;
            if(currentSRTFProcess == null){ // first process
                currentSRTFProcess = processesExecution.get(0);
                currentSRTFProcess.process.enteredTime = timeCounter;
            }else{
                SRTFProcess tmpSRTFProcess = getMinRemainingTimeProcess(currentSRTFProcess, timeCounter);
                if(tmpSRTFProcess != null){
                    currentSRTFProcess.swapsCounter++; // finish process
                    tmp = currentSRTFProcess;
                    tmp.process.exitTime = timeCounter;
                    currentSRTFProcess = tmpSRTFProcess;
                    currentSRTFProcess.process.enteredTime = timeCounter;
                }
            }

            currentPairIdx = processesExecution.indexOf(currentSRTFProcess);
            currentSRTFProcess.remainingTime--;

            if(currentSRTFProcess.remainingTime == 0){
                executionCompletedProcesses++;
                processesExecution.get(currentPairIdx).process.exitTime = timeCounter + 1;
                processesExecution.get(currentPairIdx).process.setExecutionCompleted(true);
                tmp = processesExecution.get(currentPairIdx);
            }

            timeCounter++;

            if(executionOrder.isEmpty() || executionOrder.lastElement() != currentSRTFProcess) {
                executionOrder.add(processesExecution.get(currentPairIdx));
            }

            if(tmp != null && (processExecutionGui.isEmpty() ||
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

    private SRTFProcess getMinRemainingTimeProcess(SRTFProcess currentExecutionSRTFProcess, int currentTime){
        int minRemainingTime = (currentExecutionSRTFProcess.remainingTime == 0 ? 1000000000 : currentExecutionSRTFProcess.remainingTime);
        SRTFProcess currentSRTFProcess = null;
        for (SRTFProcess SRTFProcess : processesExecution) {
            if(SRTFProcess != currentExecutionSRTFProcess
                    && SRTFProcess.process.getArrivalTime() <= currentTime && SRTFProcess.swapsCounter == maxSwaps){
                return SRTFProcess;
            }
            if(SRTFProcess.remainingTime < minRemainingTime && !SRTFProcess.process.isExecutionCompleted()
                    && SRTFProcess != currentExecutionSRTFProcess && SRTFProcess.process.getArrivalTime() <= currentTime){
                currentSRTFProcess = SRTFProcess;
                minRemainingTime = SRTFProcess.remainingTime;
            }
        }
        return currentSRTFProcess;
    }

    private int compareByArrivalTime(SRTFProcess a, SRTFProcess b){
        return Integer.compare(a.process.getArrivalTime() ,b.process.getArrivalTime());
    }

    private void print(){
        System.out.println("Processes Execution Order:");
        for (SRTFProcess SRTFProcess : executionOrder) {
            System.out.print(SRTFProcess.process.getName() + "  ");
        }
        System.out.println('\n');
        for (SRTFProcess SRTFProcess : processesExecution) {

            System.out.println("==== Process: " + SRTFProcess.process.getName() + " ===="+
                    "\nWaiting time = "+ SRTFProcess.process.waitingTime +
                    "\nTurnaround time = " + SRTFProcess.process.getTurnAroundTime() + '\n');
        }
        System.out.println("\nAverage waiting time =  " + averageWaitingTime);
        System.out.println("Average turnaround time =  " + averageTurnAroundTime);
    }

}