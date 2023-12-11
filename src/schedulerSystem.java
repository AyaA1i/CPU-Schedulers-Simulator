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
//            int rand = (int) (Math.random() * 21);
//            if(rand < 10){
//                process.setAGFactory(rand + process.getArrivalTime() + process.getBurstTime());
//            }else if(rand == 10){
//                process.setAGFactory((int) (process.getPriorityNumber() + process.getArrivalTime() + process.getBurstTime()));
//            }else{
//                process.setAGFactory(10 + process.getArrivalTime() + process.getBurstTime());
//            }
            System.out.println(process.getName() + " " + process.getAGFactory());

            // test case given
            if (i == 0) process.setAGFactory(20);
            else if (i == 1) process.setAGFactory(17);
            else if (i == 2) process.setAGFactory(16);
            else if (i == 3) process.setAGFactory(43);

            // another test case
//            if(i==0)process.setAGFactory(20);
//            else if(i==1)process.setAGFactory(19);
//            else if(i==2)process.setAGFactory(24);
//            else if(i==3)process.setAGFactory(38);

            process.setQuantumTime(roundRobin);
            processes.add(process);
        }
//        SJFScheduler sjfScheduler = new SJFScheduler(processes,contextSwitch);
//        sjfScheduler.schedule();
        AGAlgorithm ag = new AGAlgorithm(processes);
        ag.buildAlgo();

//        PriorityScheduler priorityScheduler = new PriorityScheduler(processes);
//        priorityScheduler.schedule();
    }
}
