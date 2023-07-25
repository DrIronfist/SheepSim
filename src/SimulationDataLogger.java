import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimulationDataLogger {
    private List<Integer> sheepPopulationData;
    private List<Integer> predatorPopulationData;
    private List<Integer> grassEatenData;
    private List<Integer> sheepEatenData;

    public SimulationDataLogger() {
        sheepPopulationData = new ArrayList<>();
        predatorPopulationData = new ArrayList<>();
        grassEatenData = new ArrayList<>();
        sheepEatenData = new ArrayList<>();
    }

    public void updateData(int sheepPopulation, int predatorPopulation, int grassEaten, int sheepEaten) {
        sheepPopulationData.add(sheepPopulation);
        predatorPopulationData.add(predatorPopulation);
        grassEatenData.add(grassEaten);
        sheepEatenData.add(sheepEaten);
    }

    public void writeDataToCSV(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("TimeStep,SheepPopulation,PredatorPopulation,GrassEaten,SheepEaten\n");
            for (int i = 0; i < sheepPopulationData.size(); i++) {
                writer.append(i + 1 + ",");
                writer.append(sheepPopulationData.get(i) + ",");
                writer.append(predatorPopulationData.get(i) + ",");
                writer.append(grassEatenData.get(i) + ",");
                writer.append(sheepEatenData.get(i) + "\n");
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
