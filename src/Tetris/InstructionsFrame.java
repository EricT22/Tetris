package Tetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class InstructionsFrame extends JFrame{
    private final int WIDTH = 500;
    private final int HEIGHT = 475;

    private JTextArea instructionsTextArea;

    private final String RELATIVE_PATH = "instructions.txt";

    public InstructionsFrame(ImageIcon logo){
        super("How To Play");

        this.setSize(WIDTH, HEIGHT);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.setIconImage(logo.getImage());

        this.setAlwaysOnTop(true);
        this.setLocationRelativeTo(null);
        this.setResizable(true);

        this.setLayout(new BorderLayout(0, 0));

        prepareTextArea();

        this.add(instructionsTextArea, BorderLayout.CENTER);

        this.setVisible(true);
    }

    private void prepareTextArea() {
        InputStream in = getClass().getClassLoader().getResourceAsStream(RELATIVE_PATH);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String instructions = "";
        String curLine = "";

        try {
            while ((curLine = reader.readLine()) != null){
                instructions += curLine + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        instructionsTextArea = new JTextArea(instructions, WIDTH, HEIGHT);

        instructionsTextArea.setBackground(Color.BLACK);
        instructionsTextArea.setFont(new Font("Times", Font.PLAIN, 13));
        instructionsTextArea.setForeground(Color.GREEN);
    }
}
