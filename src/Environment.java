import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Environment {
    private Cell[][] cells;
    private List<Sheep> population;
    public int size;
    public Random random;

    public Environment(int size) {
        this.size = size;
        cells = new Cell[size][size];
        population = new ArrayList<>();
        random = new Random();

        // Initialize cells with grass and predators as needed
        // For simplicity, we assume grass is randomly distributed at the start
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new Cell();
                if (random.nextDouble() < 0.3) {
                    
                    cells[i][j].addGrass();
                }
            }
        }
    }

    public void addSheep(Sheep sheep) {
        population.add(sheep);
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public void update() {
        // Update the environment over time (e.g., grass growth, predator movement)
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!cells[i][j].hasGrass() && random.nextDouble() < 0.2) {
                    cells[i][j].addGrass(); // Grass grows with a certain probability
                }
            }
        }
    }

    public List<Sheep> getPopulation() {
        return population;
    }

    // Other methods...
}
