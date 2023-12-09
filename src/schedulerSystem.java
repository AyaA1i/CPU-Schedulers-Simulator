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
            process.setArrivalTime(in.next());
            System.out.println("Enter the " + (i + 1) + " process burst time :");
            process.setBurstTime(in.next());
            System.out.println("Enter the " + (i + 1) + " process priority number :");
            process.setPriorityNumber(in.next());
            processes.add(process);
        }
        SJFScheduler sjfScheduler = new SJFScheduler(processes,contextSwitch);
        sjfScheduler.schedule();
    }
}
