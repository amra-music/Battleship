package sample;

public class SequenceAI extends AI {
    private int counter;

    public SequenceAI() {
    }

    public void nextMove() {
        setX(counter / 10);
        setY(counter % 10);
        counter++;
    }
    public void reset(){
        counter = 0;
    }
}