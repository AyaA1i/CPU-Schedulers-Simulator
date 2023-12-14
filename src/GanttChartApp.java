import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

class GanttChartPanel extends JPanel {
    private final Vector<Map.Entry<Process, Map.Entry<Integer,Integer>>> processExecution;
    Map<String, Color> colorMap = new HashMap<>();
    private int maxFinish = -1;
    public GanttChartPanel(Vector<Map.Entry<Process, Map.Entry<Integer,Integer>>> processExecution){
        this.processExecution = processExecution;

        for (Map.Entry<Process, Map.Entry<Integer, Integer>> entry : processExecution) {
            Map.Entry<Integer, Integer> values = entry.getValue();
            maxFinish = Math.max(maxFinish,values.getKey());
        }
        colorMap.put("red", new Color(255, 0, 0));
        colorMap.put("green", new Color(0, 255, 0));
        colorMap.put("blue", new Color(0, 0, 255));
        colorMap.put("yellow", new Color(255, 255, 0));
        colorMap.put("purple", new Color(128, 0, 128));
        colorMap.put("orange", new Color(255, 165, 0));
        colorMap.put("cyan", new Color(0, 255, 255));
        colorMap.put("magenta", new Color(255, 0, 255));
        colorMap.put("pink", new Color(255, 192, 203));
        colorMap.put("brown", new Color(165, 42, 42));
        colorMap.put("gray", new Color(128, 128, 128));
        colorMap.put("white", new Color(255, 255, 255));
        colorMap.put("black", new Color(0, 0, 0));
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int x = 250; // Initial x-coordinate for Gantt chart bars
        int y = 20; // Initial y-coordinate for Gantt chart bars

        // Draw horizontal lines (time)
        for (int i = 0; i <= maxFinish*10; i++) {
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(x + i * 10, 10, x + i * 10, y + processExecution.size() * 40);
            g.setColor(Color.BLACK);

            // Draw time labels with some spacing
            if (i % 5 == 0) {
                g.drawString(Integer.toString(i), x + i * 10 - 5, 10);
            }
        }

        // Draw vertical lines (process names)
        Set<String> processSet = new HashSet<>();

        for (Map.Entry<Process, Map.Entry<Integer, Integer>> entry : processExecution) {
            Process process = entry.getKey();
            if(!processSet.contains(process.getName())){
                for (Map.Entry<Process, Map.Entry<Integer, Integer>> sameEntry : processExecution){
                    if(process.getName().equals(sameEntry.getKey().getName())){
                        Map.Entry<Integer, Integer> values = sameEntry.getValue();
                        int startTime = values.getKey();
                        int finishTime = values.getValue();
                        int width = (finishTime - startTime) * 10;
                        Color color = colorMap.getOrDefault(process.getColor(), Color.BLACK);
                        // Set the color for drawing
                        g.setColor(color);
                        g.fillRect(x + startTime * 10, y, width, 30);
                    }
                }
                g.setColor(Color.BLACK);
                g.drawString(process.getName(), 230, y + 20);
                y += 40;
            }
            processSet.add(process.getName());
        }
    }
}
