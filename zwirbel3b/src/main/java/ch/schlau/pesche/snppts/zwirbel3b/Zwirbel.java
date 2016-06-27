package ch.schlau.pesche.snppts.zwirbel3b;

public final class Zwirbel extends Spielzeug {
    int currentRotation;

    public Zwirbel(int spielzeugId) {
        super(spielzeugId);
    }

    public int getCurrentRotation() {
        return currentRotation;
    }

    public void setCurrentRotation(int currentRotation) {
        this.currentRotation = currentRotation;
    }
}
