import java.util.ArrayList;
import java.util.List;

public class Runner {
    public static int size = 100;
    static int eatenSheep = 0;
    static int grassEaten = 0;
    public static void main(String[] args) {

        Environment environment = new Environment(size);

        // Create a population of sheep
        int numSheep = 100;
        int numPredators = 15;
        for (int i = 0; i < numSheep; i++) {
            int x = environment.random.nextInt(size);
            int y = environment.random.nextInt(size);
            Sheep sheep = new Sheep(x, y);
            environment.addSheep(sheep);
        }
        for (int i = 0; i < numPredators; i++) {
            int x = environment.random.nextInt(size);
            int y = environment.random.nextInt(size);
            Predator predator = new Predator(x, y);
            environment.addPredator(predator);
        }

        SheepVisualization visualization = new SheepVisualization(environment);
        SimulationDataLogger dataLogger = new SimulationDataLogger(); // Create the data logger

        // Simulation loop
         int numTimeSteps = 50;
        for (int timeStep = 1; timeStep <= numTimeSteps; timeStep++) {
            // Update the environment
            environment.update();

            // Move and act sheep
            List<Sheep> population = environment.getPopulation();
            List<Sheep> newborns = new ArrayList<>(); // List to store newborn sheep

            for (int i = population.size() - 1; i >= 0; i--) {
                Sheep sheep = population.get(i);
                if (sheep.isAlive()) {
                    Cell[][] surroundings = getSurroundings(sheep, environment);
                    sheep.decideAction(surroundings);
                    boolean eaten = sheep.eat(environment.getCell(sheep.getX(), sheep.getY()));
                    if(eaten){
                        grassEaten++;
                    }

                    // Check if the sheep can reproduce
                    if (sheep.canReproduce()) {
                        for (int j = i - 1; j >= 0; j--) {
                            Sheep partner = population.get(j);
                            if (partner.isAlive() && partner.canReproduce() &&
                                    Math.abs(sheep.getX() - partner.getX()) <= 1 &&
                                    Math.abs(sheep.getY() - partner.getY()) <= 1) {
                                // Reproduce if adjacent to another sheep that can also reproduce
                                Sheep child = sheep.reproduce(partner);
                                newborns.add(child);
                                break;
                            }
                        }
                    }
                } else {
                    population.remove(i); // Remove dead sheep from the population
                }
            }

            // Add newborn sheep to the population
            population.addAll(newborns);
             // Move and act predators
            List<Predator> predators = environment.getPredators();
            List<Predator> newPredators = new ArrayList<>(); // List to store newborn predators

            for (int i = predators.size() - 1; i >= 0; i--) {
                Predator predator = predators.get(i);
                if (predator.isAlive()) {
                    Cell[][] surroundings = environment.getPredatorSurroundings(predator);
                    predator.decideAction(surroundings);
                    predator.decreaseHunger();

                    // Check if the predator can eat a sheep
                    for (Sheep sheep : population) {
                        if (sheep.isAlive() && Math.abs(predator.getX() - sheep.getX()) <= 1 &&
                                Math.abs(predator.getY() - sheep.getY()) <= 1) {
                            // Eat the sheep if adjacent to it
                            predator.eatSheep();
                            eatenSheep++;
                            sheep.setHungerLevel(0.0); // Sheep is eaten and dies
                        }
                    }

                    // Check if the predator can reproduce
                    if (predator.canReproduce()) {
                        for (int j = i - 1; j >= 0; j--) {
                            Predator partner = predators.get(j);
                            if (partner.isAlive() && partner.canReproduce() &&
                                    Math.abs(predator.getX() - partner.getX()) <= 1 &&
                                    Math.abs(predator.getY() - partner.getY()) <= 1) {
                                // Reproduce if adjacent to another predator that can also reproduce
                                Predator child = predator.reproduce(partner);
                                newPredators.add(child);
                                break;
                            }
                        }
                    }
                } else {
                    predators.remove(i); // Remove dead predators from the population
                }
            }
            predators.addAll(newPredators);

            int sheepPopulation = environment.getPopulation().size();
            int predatorPopulation = environment.getPredators().size();
            dataLogger.updateData(sheepPopulation, predatorPopulation, grassEaten, eatenSheep);

            // Update the visualization
            visualization.updateVisualization();

            // Pause for a moment to observe the simulation (optional)
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(population.size());
            System.out.println("num eaten: "+ eatenSheep);
        }
        // Write data to CSV file
        dataLogger.writeDataToCSV("simulation_data.csv");
    }


    private static Cell[][] getSurroundings(Sheep sheep, Environment environment) {
        // Get the 3x3 surroundings of the sheep's current position in the environment
        int x = sheep.getX();
        int y = sheep.getY();
        Cell[][] surroundings = new Cell[3][3];

        for (int i = x - 1, r = 0; i <= x + 1; i++, r++) {
            for (int j = y - 1, c = 0; j <= y + 1; j++, c++) {
                if (i >= 0 && i < environment.size && j >= 0 && j < environment.size) {
                    surroundings[r][c] = environment.getCell(i, j);
                } else {
                    surroundings[r][c] = new Cell(); // Empty cell outside the environment
                }
            }
        }

        return surroundings;
    }

    private static void printEnvironmentState(Environment environment) {
        // Print the state of the environment (presence of grass and predators)
        System.out.println("Environment State:");
        for (int i = 0; i < environment.size; i++) {
            for (int j = 0; j < environment.size; j++) {
                Cell cell = environment.getCell(i, j);
                System.out.print(cell.hasGrass() ? "G " : "- ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void printSheepState(List<Sheep> population) {
        // Print the state of the sheep (position and hunger level)
        System.out.println("Sheep State:");
        for (Sheep sheep : population) {
            System.out.println("Position: (" + sheep.getX() + ", " + sheep.getY() + ")");
            System.out.println("Hunger Level: " + sheep.getHungerLevel());
        }
        System.out.println();
    }
}
