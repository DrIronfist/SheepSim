import java.util.Random;

public class Sheep {
    private int x;
    private int y;
    private double hungerLevel;
    private double survivalTrait; // Genetic trait affecting survival

    private Random random;

    public Sheep(int x, int y) {
        this.x = x;
        this.y = y;
        hungerLevel = 0.5; // Initial hunger level
        survivalTrait = Math.random(); // Randomly initialize the survival trait
        random = new Random();
    }
    public boolean canReproduce() {
        // Sheep can reproduce if their hunger level is above a threshold (e.g., 0.7)
        return hungerLevel > 0.7;
    }

    public Sheep reproduce(Sheep partner) {
        // Create a new sheep as the offspring of this sheep and its partner
        int childX = (x + partner.getX()) / 2;
        int childY = (y + partner.getY()) / 2;

        // Inherit survival trait from the parents with a slight mutation
        double childSurvivalTrait = (survivalTrait + partner.getSurvivalTrait()) / 2;
        childSurvivalTrait += Math.random() * 0.1 - 0.05;

        // Create a new sheep with the inherited traits
        Sheep child = new Sheep(childX, childY);
        child.setSurvivalTrait(Math.max(0.0, Math.min(1.0, childSurvivalTrait)));

        // Reduce the hunger level of the parent sheep after reproduction
        hungerLevel = 0.4;
        partner.setHungerLevel(0.4);

        return child;
    }

    public void setHungerLevel(double hungerLevel) {
        // Ensure that the hunger level is within the range [0, 1]
        this.hungerLevel = Math.max(0.0, Math.min(1.0, hungerLevel));
    }

    public int getX() {
        return x;
    }
    public void setSurvivalTrait(double survivalTrait) {
        // Ensure that the survival trait is within the range [0, 1]
        this.survivalTrait = Math.max(0.0, Math.min(1.0, survivalTrait));
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

    public boolean eat(Cell cell) {
        if (cell.hasGrass()) {
            cell.removeGrass();
            hungerLevel = Math.min(1.0, hungerLevel + 0.3); // Increase hunger level by eating grass
            return true;
        }
        return false;
    }

    public void decideAction(Cell[][] surroundings) {
        // Sheep randomly move in a random direction
        int dx = random.nextInt(3) - 1; // -1, 0, or 1
        int dy = random.nextInt(3) - 1; // -1, 0, or 1

        move(dx, dy);
        hungerLevel -= 0.05; // Decrease hunger level at each step
    }

    public boolean isAlive() {
        return hungerLevel > 0.0;
    }

    public double getSurvivalTrait() {
        return survivalTrait;
    }
    
}
