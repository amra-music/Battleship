package sample;

public class Carrier extends Ship {

    private ShipType shipType = ShipType.BOAT_CARRIER;
    private int health = 5;

    public Carrier(double width, double firstPositionX, double firstPositionY) {
        super(width, firstPositionX, firstPositionY);
    }

    public Carrier(int size) {
        super(size);
    }

    public ShipType getShipType() {
        return shipType;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
