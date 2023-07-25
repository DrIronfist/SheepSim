public class Cell {
    private boolean hasGrass;
    private boolean hasPredator;

    public boolean hasGrass() {
        return hasGrass;
    }

    public boolean hasPredator() {
        return hasPredator;
    }

    public void addGrass() {
        hasGrass = true;
    }

    public void removeGrass() {
        hasGrass = false;
    }

    public void addPredator() {
        hasPredator = true;
    }

    public void removePredator() {
        hasPredator = false;
    }
}
