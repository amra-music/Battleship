package sample;

import sample.Board;
import sample.NoviAI;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class NoviAIPametni extends NoviAI {
    private int availableCoordinates = 50;
    private int[] arrayX = {1, 3, 5, 7, 9};
    private int[] arrayY = {0, 2, 4, 6, 8};

    public NoviAIPametni(Board playerBoard) {
        super(playerBoard);
    }

    private boolean isAvailableCoordinates(int x, int y) {
        return ((y % 2 == 0 && x % 2 == 1) || (x % 2 == 0 && y % 2 == 1));
    }

    public void run() {
        //  These ints record the X and Y coordinates of the shot I just made
        int Xinitial = 0;
        int Yinitial = 0;
        //  random hit acts simply as a switch to activate my other method invocation
        boolean randomHit = false;
        //  sunk is useless and sad. But I love him anyway.
        boolean sunk = false;
        int brojac = 0;
        while (getPlayerBoard().getHealth() != 0) {
            System.out.println("Zdravlje " + getPlayerBoard().getHealth());
            if(availableCoordinates!=0){
                if (ThreadLocalRandom.current().nextInt(0, 2) == 0) {
                    int randomX = ThreadLocalRandom.current().nextInt(0, 5);
                    Xinitial = (arrayX[randomX]);
                    int randomY = ThreadLocalRandom.current().nextInt(0, 5);
                    Yinitial = (arrayY[randomY]);
                } else {
                    int randomX = ThreadLocalRandom.current().nextInt(0, 5);
                    Xinitial = (arrayY[randomX]);
                    int randomY = ThreadLocalRandom.current().nextInt(0, 5);
                    Yinitial = (arrayX[randomY]);
                }
            } else {
                Xinitial = ThreadLocalRandom.current().nextInt(0, 10);
                Yinitial = ThreadLocalRandom.current().nextInt(0, 10);
            }
            //  I use this while loop to choose new random X's and Y's
            //  until they represent a square I haven't shot at yet.
            System.out.println("Generisi X: " + Xinitial + " Y: " + Yinitial);
            while (getShots().contains(Xinitial + "-" + Yinitial)) {
                System.out.println("Vec gađano, generisi novo X: " + Xinitial + " Y: " + Yinitial);
                Xinitial = ThreadLocalRandom.current().nextInt(0, 10);
                Yinitial = ThreadLocalRandom.current().nextInt(0, 10);
                brojac++;
            }
            //  when they do, randomHit will tell me if it was successful
            randomHit = getPlayerBoard().shoot(Xinitial, Yinitial);
            System.out.println("Pogođen je brod " + randomHit);

            //  ABSOLUTELY EVERY TIME a shot is fired, I record the X and Y as an awkward little string.
            getShots().add(Xinitial + "-" + Yinitial);
            if (isAvailableCoordinates(Xinitial, Yinitial))
                availableCoordinates--;

            //  If the shot was successful, Xinitial and Yinitial will be used as the arguments for the sink method!
            if (randomHit) {
                //  ...After they are added to the hits list of course.
                System.out.println("Nadjen brod idemo na potapanje");
                getHits().add(Xinitial + "-" + Yinitial);
                sunk = sink(Xinitial, Yinitial);
            }
        }
    }

    //  After a random hit returns true, the sink method is invoked with two ints that will be called Xinitial and Yinitial again.
    public boolean sink(int Xinitial, int Yinitial) {
        boolean progress = false;
        ArrayList<Integer> hit = new ArrayList<>();
        ArrayList<Integer> clear = new ArrayList<>();
        boolean turnaround = false;
        //  However, for all intents and purposes they are now known as Xfire and Yfire.
        int Xfire = Xinitial;
        int Yfire = Yinitial;
        //  the "hit" and "clear" lists, will be used later to determine which direction the ship is oriented (i.e. East=0, South=1, West=2, North=3)
        if (!getShots().contains((Xfire + 1) + "-" + Yfire)) {
            //  I begin by checking the square to the east of the initial hit.
            //  Using the if statement to make sure that square hasn't already been hit,
            //  I adjust the X value by 1, moving the "crosshairs" to the east.
            Xfire = Xfire + 1;
            Yfire = Yinitial;
            //  checking again to be sure that I haven't already shot there
            //  and that the new X coordinate does not produce a bad colum error...
            // ******************** zar ovo nije vec sigurno za shots*****************
            if (!getShots().contains(Xfire + "-" + Yfire) && Xfire < 10) {
                //  I fire with the adjusted coordinates and record it into shots.
                progress = getPlayerBoard().shoot(Xfire, Yfire);
                getShots().add(Xfire + "-" + Yfire);
                if(isAvailableCoordinates(Xfire,Yfire)) availableCoordinates--;
            }
            //  If the coordinate would have been out of bounds, or if for some reason
            //  I had already shot there, the else statement assigns 0 or "East" to clear.
            else clear.add(0);
            //  If the shot was successful, it adds the coordinates to hits and
            //  Assigns 0 or "East" to the hit list, signifying the boat points that way.
            if (progress) {
                getHits().add(Xfire + "-" + Yfire);
                hit.add(0);
            }
            //  If the shot fired, but returned false, East will be added to the clear list.
            else clear.add(0);
        }
        //  I developed a compulsive need to reset my values when working on as14...
        Xfire = Xinitial;
        progress = false;
        //  For this section the exact same process is undertaken
        //  except it is checking the South square from the initial random hit.
        //  South is signified by 1.
        if (!getShots().contains(Xfire + "-" + (Yfire + 1))) {
            Yfire = Yfire + 1;
            Xfire = Xinitial;
            if (!getShots().contains(Xfire + "-" + Yfire) && Yfire < 10) {
                progress = getPlayerBoard().shoot(Xfire, Yfire);
                getShots().add(Xfire + "-" + Yfire);
                if(isAvailableCoordinates(Xfire,Yfire)) availableCoordinates--;
            } else clear.add(1);
            if (progress) {
                getHits().add(Xfire + "-" + Yfire);
                hit.add(1);
            } else clear.add(1);
        }
        Yfire = Yinitial;
        progress = false;
        //  Same procedure for the West, represented by 2.
        if (!getShots().contains((Xfire - 1) + "-" + Yfire)) {
            Xfire = Xfire - 1;
            Yfire = Yinitial;
            if (!getShots().contains(Xfire + "-" + Yfire) && Xfire >= 0) {
                progress = getPlayerBoard().shoot(Xfire, Yfire);
                getShots().add(Xfire + "-" + Yfire);
                if(isAvailableCoordinates(Xfire,Yfire)) availableCoordinates--;
            } else clear.add(2);
            if (progress) {
                getHits().add(Xfire + "-" + Yfire);
                hit.add(2);
            } else clear.add(2);
        }
        Xfire = Xinitial;
        progress = false;
        //  Same procedure for the North, represented by 3.
        if (!getShots().contains(Xfire + "-" + (Yfire - 1))) {
            Yfire = Yfire - 1;
            Xfire = Xinitial;
            if (!getShots().contains(Xfire + "-" + Yfire) && Yfire >= 0) {
                progress = getPlayerBoard().shoot(Xfire, Yfire);
                getShots().add(Xfire + "-" + Yfire);
                if(isAvailableCoordinates(Xfire,Yfire)) availableCoordinates--;
            } else clear.add(3);
            if (progress) {
                getHits().add(Xfire + "-" + Yfire);
                hit.add(3);
            } else clear.add(3);
        }
        Yfire = Yinitial;
        progress = false;
        //  Now with information about which of the four sides from the initial randomhit
        //  the ship continued in, I enter the process of continuing to fire in those directions.
        Xfire = Xinitial;
        Yfire = Yinitial;
        //  This section will run if the East shot,(0), had been a hit and listed as such.
        if (hit.contains(0)) {
            //  I use this while loop to shoot down the row until progress is false,
            //  When I then add the east side to the clear list.
            while (Xfire < 9 && getShots().contains(Xfire + "-" + Yfire) && !clear.contains(0)) {
                Xfire = Xfire + 1;
                Yfire = Yinitial;
                //  If I haven't already shot at the new square, progress will be determined
                //  by the outcome of the shot.
                if (!getShots().contains(Xfire + "-" + Yfire)) {
                    progress = getPlayerBoard().shoot(Xfire, Yfire);
                    getShots().add(Xfire + "-" + Yfire);
                    if(isAvailableCoordinates(Xfire,Yfire)) availableCoordinates--;
                    //  If progress is false, East is added to the clear list.
                    if (!progress) {
                        clear.add(0);
                    }
                }
            }

            Xfire = Xinitial;
            Yfire = Yinitial;
        }
//  In this section, I continue shooting to the south, if the first check was
//  Successful.
        if (hit.contains(1)) {
            while (Yfire < 9 && getShots().contains(Xfire + "-" + Yfire) && !clear.contains(1)) {
                Xfire = Xinitial;
                Yfire = Yfire + 1;
                if (!getShots().contains(Xfire + "-" + Yfire)) {
                    progress = getPlayerBoard().shoot(Xfire, Yfire);
                    getShots().add(Xfire + "-" + Yfire);
                    if(isAvailableCoordinates(Xfire,Yfire)) availableCoordinates--;
                    if (!progress) {
                        clear.add(1);
                    }
                }
            }
            Xfire = Xinitial;
            Yfire = Yinitial;
        }
        //  Sweeping to the West, if the original check was successful.
        if (hit.contains(2)) {
            while (Xfire > 0 && getShots().contains(Xfire + "-" + Yfire) && !clear.contains(2)) {
                Xfire = Xfire - 1;
                Yfire = Yinitial;
                if (!getShots().contains(Xfire + "-" + Yfire)) {
                    progress = getPlayerBoard().shoot(Xfire, Yfire);
                    getShots().add(Xfire + "-" + Yfire);
                    if(isAvailableCoordinates(Xfire,Yfire)) availableCoordinates--;
                    if (!progress) {
                        clear.add(2);
                    }
                }
            }
            Xfire = Xinitial;
            Yfire = Yinitial;
        }
        //  Finally, continuing to the North if there was a ship part on that side.
        if (hit.contains(3)) {
            while (Yfire > 0 && getShots().contains(Xfire + "-" + Yfire) && !clear.contains(3)) {
                Xfire = Xinitial;
                Yfire = Yfire - 1;
                if (!getShots().contains(Xfire + "-" + Yfire)) {
                    progress = getPlayerBoard().shoot(Xfire, Yfire);
                    getShots().add(Xfire + "-" + Yfire);
                    if(isAvailableCoordinates(Xfire,Yfire)) availableCoordinates--;
                    if (!progress) {

                        clear.add(3);
                    }
                }
            }
            Xfire = Xinitial;
            Yfire = Yinitial;
        }
        //  Here I reset the values for the next time sink is invoked...
        //  I probably didn't need to do that. but OCD is a bitch.
        progress = false;
        hit.clear();
        clear.clear();
        return true;
    }
}
