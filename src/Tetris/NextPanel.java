package Tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JPanel;

public class NextPanel extends JPanel{
    private PiecePanel[] panels;

    public NextPanel(int numPanels){
        super();

        this.setBackground(Color.BLACK);

        this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 3));

        panels = new PiecePanel[numPanels];

        for (int i = 0; i < numPanels; i++){
            panels[i] = new PiecePanel(125, 100);
            this.add(panels[i]);
        }
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(125, 409);
    }

    @Override
    public void paint(Graphics g){
        super.paintComponent(g);

        for (PiecePanel panel : panels){
            panel.repaint();
        }
    }

    public void updatePanels(Character[] pieces){
        for (int i = 0; i < pieces.length; i++){
            panels[i].updateHeldPiece(pieces[i]);
        }
        this.repaint();
    }

    public void resetNextPanel(){
        for (PiecePanel panel : panels){
            panel.resetHoldPanel();
            panel.repaint();
        }
    }
}