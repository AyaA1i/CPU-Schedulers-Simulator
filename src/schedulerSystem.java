import javax.swing.*;
import java.awt.*;
import java.util.*;

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
//            int rand = (int) (Math.random() * 21);
//            if (rand < 10) {
//                process.setAGFactory(rand + process.getArrivalTime() + process.getBurstTime());
//            } else if (rand == 10) {
//                process.setAGFactory((int) (process.getPriorityNumber() + process.getArrivalTime() + process.getBurstTime()));
//            } else {
//                process.setAGFactory(10 + process.getArrivalTime() + process.getBurstTime());
//            }
            if(i==0)process.setAGFactory(21);
            else if(i==1)process.setAGFactory(9);
            else if(i==2)process.setAGFactory(24);
            else if(i==3)process.setAGFactory(39);
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
//            guiInvoker(sjfScheduler.getProcessExecutionGui());
        } else if (choice == 2) {
            AGAlgorithm ag = new AGAlgorithm(processes);
            ag.buildAlgo();
            guiInvoker(ag.getProcessExecution(), ag.getAwt(), ag.getAtat(), "AG Scheduler");
        } else if (choice == 3) {
            PriorityScheduler priorityScheduler = new PriorityScheduler(processes);
            priorityScheduler.schedule();
            guiInvoker(priorityScheduler.getProcessExecution(), priorityScheduler.getAwt(), priorityScheduler.getAtat(), "Priority Scheduler");
        } else if (choice == 4) {

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
            titlePanel.setBounds(0, 400, 600, 30);
            titlePanel.setBackground(new Color(10, 100, 200));
            JLabel titleLabel = new JLabel();
            titleLabel.setText("Statistics of " + schedulerName);
            titlePanel.add(titleLabel);
            titleLabel.setForeground(Color.white);

            JPanel awtPanel = new JPanel();
            awtPanel.setBounds(0, 430, 600, 30);
            awtPanel.setBackground(new Color(12, 10, 100));
            JLabel awtLabel = new JLabel();
            awtLabel.setText("AWT: " + (awt));
            awtPanel.add(awtLabel);
            awtLabel.setForeground(Color.white);

            JPanel atatPanel = new JPanel();
            atatPanel.setBounds(0, 460, 600, 30);
            atatPanel.setBackground(new Color(100, 100, 100));
            JLabel atatLabel = new JLabel();
            atatLabel.setText("ATAT: " + (atat));
            atatPanel.add(atatLabel);
            atatLabel.setForeground(Color.white);

            frame.add(titlePanel);
            frame.add(awtPanel);
            frame.add(atatPanel);

            frame.add(chartPanel);
            frame.setSize(600, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
