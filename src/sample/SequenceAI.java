package sample;

import javafx.util.Pair;

public class SequenceAI extends MojaAI {

    private int counter;

    public SequenceAI() {
    }

    public Pair<Integer, Integer> nextMove() {
        setX(counter / 10);
        setY(counter % 10);
        counter++;
        return new Pair<>(getX(), getY());
    }
}
