import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

class GanttChartPanel extends JPanel {
    private List<Process> processes = new ArrayList<>();
    private Vector<Map.Entry<Process, Map.Entry<Integer,Integer>>> processExecution = new Vector<>();


    public GanttChartPanel(List<Process> processes) {
        this.processes = processes;
    }
    public GanttChartPanel(Vector<Map.Entry<Process, Map.Entry<Integer,Integer>>> processExecution){
        this.processExecution = processExecution;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int x = 50; // Initial x-coordinate for Gantt chart bars
        int y = 20; // Initial y-coordinate for Gantt chart bars

        // Draw horizontal lines (time)
        for (int i = 0; i <= 100; i++) {
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(x + i * 10, 10, x + i * 10, y + processExecution.size() * 40);
            g.setColor(Color.BLACK);

            // Draw time labels with some spacing
            if (i % 5 == 0) {
                g.drawString(Integer.toString(i), x + i * 10 - 5, 10);
            }
        }

        // Draw vertical lines (process names)
        for (Map.Entry<Process, Map.Entry<Integer, Integer>> entry : processExecution) {
            Process process = entry.getKey();
            Map.Entry<Integer, Integer> values = entry.getValue();

            g.setColor(Color.LIGHT_GRAY);
            g.drawString(process.getName(), 10, y + 20);

            int startTime = values.getKey();
            int finishTime = values.getValue();
            int width = (finishTime - startTime) * 10;

            g.setColor(Color.BLUE.brighter());
            g.fillRect(x + startTime*10, y, width, 30);

            y += 40;
        }
    }
}
