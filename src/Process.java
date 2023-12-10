public class Process{
    private String Name;
    private String Color;
    private int arrivalTime;
    private int burstTime;
    private int priorityNumber;
    private int AGFactory;
    private int quantumTime;
    public int enteredTime;
    public int exitTime;
    public int waitingTime;
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

    public int getPriorityNumber() {
        return priorityNumber;
    }

    public void setPriorityNumber(int priorityNumber) {
        this.priorityNumber = priorityNumber;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }


}
