import java.util.ArrayList;
import java.util.Vector;

public class PriorityScheduler {
    private Vector<Process> processes;
    private double agingFactor;
    public PriorityScheduler(Vector<Process> processes) {
        this.processes = processes;
        agingFactor = 0.1;
    }
    void ageProcess(Process process) {

    }
    public void schedule() {
        int totalTime = 0;
        for (int i = 0; i < processes.size(); i++) {
            totalTime += processes.get(i).getBurstTime();
        }

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

            i += tmpProcess.getBurstTime() - 1;

            // set arrival time to a large number so that it is marked as visited
            tmpProcess.setArrivalTime(1000000000);
        }

        for (int i = 0; i < executionOrder.size(); i++) {
            System.out.print(executionOrder.get(i) + " ");
        }
        System.out.println("\n");

        System.out.println(output);

        System.out.println("Average Turnaround Time: " + (turnaroundAvg / processes.size()));
        System.out.println("Average Waiting Time: " + (waitingAvg / processes.size()));
    }
}
