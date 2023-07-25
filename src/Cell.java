public class Cell {
    private boolean hasGrass;
    private boolean hasSheep;
    private boolean hasPredator;

    public Cell() {
        hasGrass = false;
        hasSheep = false;
        hasPredator = false;
    }

    public boolean hasGrass() {
        return hasGrass;
    }

    public void addGrass() {
        hasGrass = true;
    }
    public void removeGrass() {
        hasGrass = false;
    }

    public void clearGrass() {
        hasGrass = false;
    }

    public boolean hasSheep() {
        return hasSheep;
    }

    public void setHasSheep(boolean hasSheep) {
        this.hasSheep = hasSheep;
    }

    public boolean hasPredator() {
        return hasPredator;
    }

    public void setHasPredator(boolean hasPredator) {
        this.hasPredator = hasPredator;
    }
}
