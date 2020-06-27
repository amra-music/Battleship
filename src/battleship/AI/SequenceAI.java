package battleship.AI;

public class SequenceAI extends DummyAI {
    private int counter = 0;

    public SequenceAI() {
        reset();
    }

    public void nextMove() {
        setX(counter / 10);
        setY(counter % 10);
        counter++;
    }

    public void reset() {
        counter = 0;
    }
}
