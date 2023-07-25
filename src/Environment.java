import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Environment {
    private Cell[][] cells;
    private List<Sheep> population;
    public int size;
    public Random random;
    private List<Predator> predators;
    private static Environment instance;
    

    public Environment(int size) {
        this.size = size;
        cells = new Cell[size][size];
        population = new ArrayList<>();
        random = new Random();
        predators = new ArrayList<>();

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
    public static Environment getInstance() {
        if (instance == null) {
            // Create a new instance if it doesn't exist
            instance = new Environment(10); // You can adjust the size as needed
        }
        return instance;
    }


    public Cell[][] getPredatorSurroundings(Predator predator) {
        Cell[][] surroundings = new Cell[3][3];

        int predatorX = predator.getX();
        int predatorY = predator.getY();

        for (int i = predatorX - 1, r = 0; i <= predatorX + 1; i++, r++) {
            for (int j = predatorY - 1, c = 0; j <= predatorY + 1; j++, c++) {
                if (i >= 0 && i < size && j >= 0 && j < size) {
                    if (i == predatorX && j == predatorY) {
                        surroundings[r][c] = new Cell(); // Predator's position
                    } else {
                        surroundings[r][c] = cells[i][j];
                    }
                } else {
                    surroundings[r][c] = new Cell(); // Empty cell outside the environment
                }
            }
        }

        return surroundings;
    }
    public void addPredator(Predator predator) {
        predators.add(predator);
    }

    public List<Predator> getPredators() {
        return predators;
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
