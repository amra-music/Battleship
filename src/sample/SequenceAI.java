package sample;

public class SequenceAI extends AI {
    private int counter;

    public SequenceAI() {
    }

    public void nextMove(boolean isHit) {
        setX(counter / 10);
        setY(counter % 10);
        counter++;
    }

    @Override
    public void feedback(boolean b, boolean destroyed) {
    }

    public void reset(){
        counter = 0;
    }
}
