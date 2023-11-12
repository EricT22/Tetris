package Tetris;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public abstract class TetrisPieceConstants {
    static final Point[][] T = {{new Point(0, 0), new Point(1, 0), new Point(-1, 0), new Point(0, -1)},
                                {new Point(0, 0), new Point(0, 1), new Point(0, -1), new Point(1, 0)},
                                {new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(0, 1)},
                                {new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(-1, 0)}};

    static final Point[][] L = {{new Point(0, 0), new Point(1, 0), new Point(-1, 0), new Point(1, -1)},
                                {new Point(0, 0), new Point(0, 1), new Point(0, -1), new Point(1, 1)},
                                {new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(-1, 1)},
                                {new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(-1, -1)}};

    static final Point[][] J = {{new Point(0, 0), new Point(1, 0), new Point(-1, 0), new Point(-1, -1)},
                                {new Point(0, 0), new Point(0, 1), new Point(0, -1), new Point(1, -1)},
                                {new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(1, 1)},
                                {new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(-1, 1)}};

    static final Point[][] O = {{new Point(0, 0), new Point(1, 0), new Point(0, -1), new Point(1, -1)}};

    static final Point[][] S = {{new Point(0, 0), new Point(1, -1), new Point(-1, 0), new Point(0, -1)},
                                {new Point(0, 0), new Point(1, 1), new Point(0, -1), new Point(1, 0)},
                                {new Point(0, 0), new Point(-1, 1), new Point(1, 0), new Point(0, 1)},
                                {new Point(0, 0), new Point(-1, -1), new Point(0, 1), new Point(-1, 0)}};
    
    static final Point[][] Z = {{new Point(0, 0), new Point(-1, -1), new Point(1, 0), new Point(0, -1)},
                                {new Point(0, 0), new Point(1, -1), new Point(0, 1), new Point(1, 0)},
                                {new Point(0, 0), new Point(1, 1), new Point(-1, 0), new Point(0, 1)},
                                {new Point(0, 0), new Point(-1, 1), new Point(0, -1), new Point(-1, 0)}};
    
    static final Point[][] I = {{new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(-1, 0)},
                                {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, -1)},
                                {new Point(1, 1), new Point(0, 1), new Point(-1, 1), new Point(2, 1)},
                                {new Point(0, 1), new Point(0, 0), new Point(0, -1), new Point(0, 2)}};

    private static Map<Character, Point[][]> pieces = new HashMap<Character, Point[][]>();

    static {
        pieces.put('T', T);
        pieces.put('L', L);
        pieces.put('J', J);
        pieces.put('O', O);
        pieces.put('S', S);
        pieces.put('Z', Z);
        pieces.put('I', I);
    }

    public static Point[][] getConstants(Character c){
        return pieces.get(c);
    }
}
