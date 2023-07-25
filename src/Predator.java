import java.util.List;
import java.util.Random;

public class Predator {
    private int x;
    private int y;
    private double hungerLevel;
    private static final double HUNGER_INCREASE_RATE = 0.01; // Adjust the rate as needed

    private Random random;

    public Predator(int x, int y) {
        this.x = x;
        this.y = y;
        hungerLevel = 0.5; // Initial hunger level
        random = new Random();
    }

    public void increaseHunger() {
        hungerLevel += HUNGER_INCREASE_RATE;
        if (hungerLevel > 1.0) {
            hungerLevel = 1.0; // Cap the hunger level at 1.0
        }
    }
    private static final double REPRODUCE_THRESHOLD = 0.6;
    private static final double HUNGER_TO_REPRODUCE = 0.3;

    public void decideAction(Cell[][] surroundings) {
        int dx = 0;
        int dy = 0;

        // Find the nearest sheep to chase
        int minDistance = Integer.MAX_VALUE;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (surroundings[i][j].hasSheep()) {
                    int distance = Math.abs(i - 1) + Math.abs(j - 1); // Manhattan distance to sheep
                    if (distance < minDistance) {
                        dx = i - 1;
                        dy = j - 1;
                        minDistance = distance;
                    }
                }
            }
        }

        // Check if there are other predators nearby
        boolean foundPredator = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (surroundings[i][j].hasPredator() && !(i == 1 && j == 1)) {
                    // Avoid the other predator
                    dx = i - 1;
                    dy = j - 1;
                    foundPredator = true;
                    break;
                }
            }
            if (foundPredator) {
                break;
            }
        }

        // Perform the move
        move(dx, dy);

        // If well-fed and there is another predator nearby, attempt to reproduce
        if (hungerLevel < REPRODUCE_THRESHOLD && foundPredator) {
            tryReproduce();
        }
    }

    private void tryReproduce() {
        Predator partner = findPartner();
        if (partner != null) {
            if (hungerLevel >= HUNGER_TO_REPRODUCE && partner.hungerLevel >= HUNGER_TO_REPRODUCE) {
                // Both parents are well-fed, perform reproduction
                Predator offspring = reproduce(partner);
                if (offspring != null) {
                    // Add the offspring to the population
                    Environment.getInstance().addPredator(offspring);
                }
            }
        }
    }

    private Predator findPartner() {
        List<Predator> predators = Environment.getInstance().getPredators();
        for (Predator predator : predators) {
            if (this != predator && predator.isAlive()) {
                return predator;
            }
        }
        return null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getHungerLevel() {
        return hungerLevel;
    }

    public void move(int dx, int dy) {
        int newX = x + dx;
        int newY = y + dy;

        // Check if the new position is within the bounds of the environment
        if (newX >= 0 && newX < Runner.size && newY >= 0 && newY < Runner.size) {
            x = newX;
            y = newY;
        }
    }

    public void eatSheep() {
        hungerLevel = 1;
    }

    public void decreaseHunger() {
        hungerLevel -= 0.1; // Decrease hunger level at each step
    }

    public boolean isAlive() {
        return hungerLevel > 0.0;
    }
    
    public void setHungerLevel(double hungerLevel) {
        // Ensure that the hunger level is within the range [0, 1]
        this.hungerLevel = Math.max(0.0, Math.min(1.0, hungerLevel));
    }
    public boolean canReproduce() {
        // Predator can reproduce if its hunger level is above a threshold (e.g., 0.7)
        // and there are no other predators adjacent to it
        return hungerLevel > 0.7 && !hasAdjacentPredator();
    }
    public Predator reproduce(Predator partner) {
        // Check if both parents can reproduce
        if (!canReproduce() || !partner.canReproduce()) {
            return null; // Reproduction is not possible
        }

        // Calculate the average hunger level of the parents
        double averageHunger = (hungerLevel + partner.hungerLevel) / 2.0;

        // Create a new offspring predator with the average hunger level
        Predator offspring = new Predator(getX(), getY());
        offspring.setHungerLevel(averageHunger);

        return offspring;
    }

    private boolean hasAdjacentPredator() {
        Environment environment = Environment.getInstance();
        int predatorX = getX();
        int predatorY = getY();

        // Check if there is another predator adjacent to the current predator
        for (int i = predatorX - 1; i <= predatorX + 1; i++) {
            for (int j = predatorY - 1; j <= predatorY + 1; j++) {
                if (i == predatorX && j == predatorY) {
                    continue; // Skip checking the current cell
                }

                // Wrap around the environment to handle boundaries
                int wrappedX = (i + environment.size) % environment.size;
                int wrappedY = (j + environment.size) % environment.size;

                if (environment.getCell(wrappedX, wrappedY).hasPredator()) {
                    return true; // Found an adjacent predator
                }
            }
        }

        return false; // No adjacent predators found
    }
}
