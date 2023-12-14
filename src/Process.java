public class Process {
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
