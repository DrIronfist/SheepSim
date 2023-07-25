import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SheepVisualization extends JFrame {
    private static final int CELL_SIZE = 10; // Size of each cell in pixels

    private Environment environment;

    public SheepVisualization(Environment environment) {
        this.environment = environment;
        setTitle("Sheep Simulation");
        setSize(environment.size * CELL_SIZE, environment.size * CELL_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

         List<Point> sheepPositions = new ArrayList<>();
        List<Point> predatorPositions = new ArrayList<>();

        // Collect the positions of alive sheep and predators
        List<Sheep> sheepPopulation = environment.getPopulation();
        for (Sheep sheep : sheepPopulation) {
            if (sheep.isAlive()) {
                sheepPositions.add(new Point(sheep.getX(), sheep.getY()));
            }
        }

        List<Predator> predatorPopulation = environment.getPredators();
        for (Predator predator : predatorPopulation) {
            if (predator.isAlive()) {
                predatorPositions.add(new Point(predator.getX(), predator.getY()));
            }
        }

        // Draw the environment (grid and grass)
        for (int i = 0; i < environment.size; i++) {
            for (int j = 0; j < environment.size; j++) {
                Cell cell = environment.getCell(i, j);
                int x = i * CELL_SIZE;
                int y = j * CELL_SIZE;

                // Draw grass in green if the cell has grass
                if (cell.hasGrass()) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                }

                // Draw the cell border in black
                g.setColor(Color.BLACK);
                g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
            }
        }

        // Draw the sheep as dots
        List<Sheep> population = environment.getPopulation();
        for (Sheep sheep : population) {
            if (sheep.isAlive()) {
                int x = sheep.getX() * CELL_SIZE + CELL_SIZE / 2;
                int y = sheep.getY() * CELL_SIZE + CELL_SIZE / 2;

                // Draw the sheep as a red dot
                g.setColor(Color.ORANGE);
                g.fillOval(x - 2, y - 2, 5, 5);
            }
        }
        List<Predator> predators = environment.getPredators();
        for (Predator predator : predators) {
            if (predator.isAlive()) {
                int x = predator.getX() * CELL_SIZE + CELL_SIZE / 2;
                int y = predator.getY() * CELL_SIZE + CELL_SIZE / 2;

                // Draw the predator as a blue dot
                g.setColor(Color.BLUE);
                g.fillOval(x - 2, y - 2, 5, 5);
            }
        }
    }

    public void updateVisualization() {
        // Repaint the visualization after each time step
        repaint();
    }
}
