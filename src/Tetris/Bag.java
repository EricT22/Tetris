package Tetris;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bag {
    private static List<Character> refillBag = new ArrayList<Character>(7);
    private static List<Character> totalBag = new ArrayList<Character>(11);
    
    static {
        refillBag.add('T');
        refillBag.add('L');
        refillBag.add('J');
        refillBag.add('O');
        refillBag.add('S');
        refillBag.add('Z');
        refillBag.add('I');
    }

    private static final int MINIMUM_SIZE = 4;
    
    public static void refillAndShuffle(){
        Collections.shuffle(refillBag);
        totalBag.addAll(refillBag);
    }

    public static Character getNextPiece() {
        if (totalBag.size() <= MINIMUM_SIZE){
            refillAndShuffle();
        }

        return totalBag.remove(0);
    }

    public static Character[] getNext(){
        Character[] arr = new Character[MINIMUM_SIZE];

        for (int i = 0; i < MINIMUM_SIZE; i++){
            arr[i] = totalBag.get(i);
        }

        return arr;
    }
    

    // TODO: KEEP TRACK OF BAG, REFILL BAG, TOTAL BAG

    private Bag(){
        // shouldn't instantiate Bag
    }
}