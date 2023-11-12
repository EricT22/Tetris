package Tetris;

import java.awt.Point;

import Tetris.Tetris.GamePanel;

public class TetrisWorker extends TetrisPieceConstants implements Runnable{
    // TODO: Connect everything to main GUI
    
    private char[][][] universe = new char[2][GamePanel.ROWS][GamePanel.COLS];
    private int display = 0;

    private boolean pieceInPlay = false;
    private Point center;
    private char curPiece;
    private int orientation; // 0 up, 1 right, 2 down, 3 left
    Point[][] curPieceConsts;

    private int numLinesCleared = 0;

    private char heldPiece = 0;
    private boolean holdPieceTriggered = false;
    private boolean holdLockedOut = false;

    private int levelUpConstant = 10;
    private boolean gameOverReached = false;
    private boolean stop = true;
    private boolean paused = false;
    private int tickSpeed;

    private GamePanel gpanel;
    private PiecePanel hpanel;
    private NextPanel npanel;
    private Tetris.DisplayPanelLeft dpl;

    
    // -- User input methods
    public void movePieceDown(int scorePerMove){
        if (pieceInPlay && !stop){
            try {
                removePieceFromBoard();

                center.y += 1;
                
                for (int i = 0; i < curPieceConsts[orientation].length; i++){
                    if (!(universe[display][center.y + curPieceConsts[orientation][i].y][center.x + curPieceConsts[orientation][i].x] == 0)){
                        center.y -= 1;
                        returnPieceToBoard();
                        pieceInPlay = !pieceInPlay;
                        return;
                    }
                }

                for (int i = 0; i < curPieceConsts[orientation].length; i++){
                    universe[display][center.y + curPieceConsts[orientation][i].y][center.x + curPieceConsts[orientation][i].x] = curPiece;
                }

                copyToProcess();

                Tetris.score += scorePerMove;
                dpl.updateScoreField();
            } catch (IndexOutOfBoundsException e){
                center.y -= 1;
                returnPieceToBoard();
                pieceInPlay = !pieceInPlay;
            }
        }
    }

    public void autoDown(){
        while(pieceInPlay && !stop){
            movePieceDown(2);
        }
        Thread.currentThread().interrupt();
    }

    public void movePieceSideways(boolean moveRight){
        if (pieceInPlay && !stop){
            try {
                removePieceFromBoard();

                if (moveRight)
                    center.x += 1;
                else
                    center.x -= 1;
            
                for (int i = 0; i < curPieceConsts[orientation].length; i++){
                    if (!(universe[display][center.y + curPieceConsts[orientation][i].y][center.x + curPieceConsts[orientation][i].x] == 0)){
                        if (moveRight)
                            center.x -= 1;
                        else
                            center.x += 1;

                        returnPieceToBoard();
                        return;
                    }
                }

                for (int i = 0; i < curPieceConsts[orientation].length; i++){
                    universe[display][center.y + curPieceConsts[orientation][i].y][center.x + curPieceConsts[orientation][i].x] = curPiece;
                }

                copyToProcess();

            } catch (IndexOutOfBoundsException e){
                if (moveRight)
                    center.x -= 1;
                else
                    center.x += 1;

                returnPieceToBoard();
            }
        }
    }
    
    public void rotatePiece(boolean rotatingRight){
        if (pieceInPlay && curPiece != 'O' && !stop){
            int storedOrientation = orientation;
            try {
                removePieceFromBoard();

                if (rotatingRight){
                    orientation = (orientation + 1) % 4;
                } else {
                    orientation = (orientation -1 + 4) % 4;
                }

                for (int i = 0; i < curPieceConsts[orientation].length; i++){
                    if (!(universe[display][center.y + curPieceConsts[orientation][i].y][center.x + curPieceConsts[orientation][i].x] == 0)){
                        orientation = storedOrientation;
                        returnPieceToBoard();
                        return;
                    }
                }

                for (int i = 0; i < curPieceConsts[orientation].length; i++){
                    universe[display][center.y + curPieceConsts[orientation][i].y][center.x + curPieceConsts[orientation][i].x] = curPiece;
                }

                copyToProcess();

            } catch (IndexOutOfBoundsException e){
                orientation = storedOrientation;
                returnPieceToBoard();
            }
        }
    }

    public void holdPiecePressed(){
        
        if (holdLockedOut){
            return;
        }

        holdPieceTriggered = true;
        pieceInPlay = false;
        Thread.currentThread().interrupt();
    }

    
    

    // -- Methods used by GUI
    public TetrisWorker(int level, GamePanel gpanel, PiecePanel hpanel, NextPanel npanel, Tetris.DisplayPanelLeft dpl){
        tickSpeed = 1000 - (10 * level);
        this.gpanel = gpanel;
        this.hpanel = hpanel;
        this.npanel = npanel;
        this.dpl = dpl;
    }

    public char pieceAtPoint(int x, int y){
        return universe[display][x][y];
    }

    public void stop(){
        stop = true;
    }

    public void setTickSpeed(int level){
        tickSpeed = 1000 - (20 * level);
    }

    public void clearBoard(){
        universe = new char[2][GamePanel.ROWS][GamePanel.COLS];
        pieceInPlay = false;
    }

    public void pause() {
        paused = true;
    }

    public void unpause() {
        paused = false;
    }

    public void resetHeldPiece(){
        heldPiece = 0;
    }


    // -- Run method and its helpers 
    @Override
    public void run() {
        stop = false;
        while (!stop){
            try {
                if (gameOverReached){
                    clearBoard();
                    gameOverReached = false;
                }

                if (!pieceInPlay){
                    if (!holdPieceTriggered){
                        checkLineClear();
                        updateCounters();
                        System.out.println("Lines Cleared: " + numLinesCleared);
                        resetLineClearCounter();
                    }

                    spawnPiece();
                    updateNextPanel();
                } else {
                    movePieceDown(0);
                }

                updateGen();

                gpanel.repaint();
                Thread.sleep(tickSpeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (!paused){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                gameOver();
            }
        }
    }

    public void updateCounters(){
        Tetris.linesCleared += numLinesCleared;

        int lineScore = 0;
        switch(numLinesCleared){
            case 0 -> lineScore = 0;
            case 1 -> lineScore = 100 * Tetris.level;
            case 2 -> lineScore = 300 * Tetris.level;
            case 3 -> lineScore = 500 * Tetris.level;
            case 4 -> lineScore = 800 * Tetris.level;
        }

        Tetris.score += lineScore;

        if (Tetris.linesCleared != 0 && Tetris.linesCleared % levelUpConstant == 0){
            Tetris.level++;
            setTickSpeed(Tetris.level);
        }

        dpl.updateLinesField();
        dpl.updateScoreField();
        dpl.updateLevelField();
    }

    // May be taken out... have a counter tracking total line clears and then check how much
    // it incremented by to calculate score
    private void resetLineClearCounter(){
        numLinesCleared = 0;
    }

    private void checkLineClear() {
        for (int row = universe[1 - display].length - 1; row >= 0; row--){
            if (rowFilled(row)){
                removeRow(row);
                numLinesCleared++;
                row+=1;
            }
        }
        
        copyToProcess();
        gpanel.repaint();
    }

    private boolean rowFilled(int row) {
        for (char c : universe[display][row]){
            if (c == 0){
                return false;
            }
        }
        return true;
    }
    
    private void removeRow(int row) {
        for (int i = row; i > 0; i--){
            universe[display][i] = universe[display][i - 1];
        }
        universe[display][0] = new char[GamePanel.COLS];
    }

    private void spawnPiece() {

        // get new piece and set location variables
        if (!holdPieceTriggered){
            curPiece = Bag.getNextPiece();

            if (holdLockedOut)
                holdLockedOut = false;

        } else {
            holdPiece();
        }

        center = new Point(4, 1);
        orientation = 0;
        
        curPieceConsts = TetrisPieceConstants.getConstants(curPiece);
        
        // check if piece can spawn
        for (int i = 0; i < curPieceConsts[orientation].length; i++){
            if (universe[1 - display][center.y + curPieceConsts[orientation][i].y][center.x + curPieceConsts[orientation][i].x] != 0){
                stop = true;
                return;
            }
        }

        // spawn piece
        for (int i = 0; i < curPieceConsts[orientation].length; i++){
            universe[1 - display][center.y + curPieceConsts[orientation][i].y][center.x + curPieceConsts[orientation][i].x] = curPiece;
        }

        pieceInPlay = true;
    }

    private void holdPiece(){
        holdLockedOut = true;
        removePieceFromBoard();
        copyToProcess();

        if (heldPiece == 0){
            heldPiece = curPiece;
            curPiece = Bag.getNextPiece();
        } else {
            char tempPiece = heldPiece;
            heldPiece = curPiece;
            curPiece = tempPiece;
        }

        hpanel.updateHeldPiece(heldPiece);
        holdPieceTriggered = false;
    }

    private void updateNextPanel(){
        npanel.updatePanels(Bag.getNext());
    }

    private void gameOver() {
        clearBoard();
        drawEndMessage();
        resetHeldPiece();

        gameOverReached = true;
    }

    private void drawEndMessage(){
        for (int i = 4; i <= 9; i++){
            universe[display][i][1] = 'T';
        }
        universe[display][4][2] = 'T';
        universe[display][4][3] = 'T';
        universe[display][9][2] = 'T';
        universe[display][9][3] = 'T';
        universe[display][8][3] = 'T';
        universe[display][7][3] = 'T';

        for (int i = 9; i <= 14; i++){
            universe[display][i][5] = 'O';
        }
        universe[display][9][6] = 'O';
        universe[display][9][7] = 'O';
        universe[display][14][6] = 'O';
        universe[display][14][7] = 'O';
        universe[display][13][7] = 'O';
        universe[display][12][7] = 'O';

        gpanel.repaint();
    }


    // -- General Helper methods
    private void returnPieceToBoard() {
        for (int i = 0; i < curPieceConsts[orientation].length; i++){
            universe[display][center.y + curPieceConsts[orientation][i].y][center.x + curPieceConsts[orientation][i].x] = curPiece;
        }
    }

    private void removePieceFromBoard() {
        for (int i = 0; i < curPieceConsts[orientation].length; i++){
            universe[display][center.y + curPieceConsts[orientation][i].y][center.x + curPieceConsts[orientation][i].x] = 0;
        }
    }

    private void updateGen() {
        display = 1 - display;
        copyToProcess();
    }

    private void copyToProcess(){
        char[][] copy = new char[universe[display].length][universe[display][0].length];

        for (int i = 0; i < copy.length; i++){
            for (int j = 0; j < copy[i].length; j++){
                copy[i][j] = universe[display][i][j];
            }
        }

        universe[1 - display] = copy;
    }  
}
