public class SRTFProcess {
    int remainingTime;
    int swapsCounter = 0;
    Process process;

    public SRTFProcess(int remainingTime, Process process) {
        this.remainingTime = remainingTime;
        this.process = process;
    }
}
