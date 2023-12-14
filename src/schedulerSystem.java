import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class schedulerSystem {

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
        System.out.println("Enter the number of the scheduler you want to use :\n" +
                "1. SJF scheduler \n" +
                "2. AG scheduler \n" +
                "3. Priority scheduler \n" +
                "4. SRTF scheduler");
        choice = in.nextInt();
        if (choice == 1) {
            SJFScheduler sjfScheduler = new SJFScheduler(processes, contextSwitch);
            sjfScheduler.schedule();
            SwingUtilities.invokeLater(() -> {


                JFrame frame = new JFrame("CPU Gantt Chart");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                GanttChartPanel chartPanel = new GanttChartPanel(sjfScheduler.getProcessExecutionGui());
                frame.getContentPane().add(chartPanel);

                frame.setSize(600, 200);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            });
        } else if (choice == 2) {
            AGAlgorithm ag = new AGAlgorithm(processes);
            ag.buildAlgo();
            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("CPU Gantt Chart");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                GanttChartPanel chartPanel = new GanttChartPanel(ag.getProcessExecution());
                frame.getContentPane().add(chartPanel);

                frame.setSize(600, 200);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            });
        }else if(choice==3){
            PriorityScheduler priorityScheduler = new PriorityScheduler(processes);
            priorityScheduler.schedule();
        }else if(choice==4){

        }else{
            System.out.println("INVALID INPUT!");
        }
    }
}
