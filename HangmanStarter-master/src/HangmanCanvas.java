import acm.graphics.*;

import java.io.*;
import java.util.*;

public class HangmanCanvas extends GCanvas {

    private static final int TEXT_HEIGHT = 20;   // you can modify this to suit your ascii art
    private static final int TEXT_X_OFFSET = 12;   // you can modify this to suit your ascii art
    private int textX;
    private int textY;

    /**
     * Resets the display so that only the hangman scaffold appears
     */
    public void reset() {
        // Clear previous drawings
        removeAll();
        textX = TEXT_X_OFFSET;
        textY = TEXT_HEIGHT;
    }

    public void printText(String text) {
        GLabel line = new GLabel(text);
        line.setFont("Monospaced-plain-12");
        add(line, textX, textY);
        textY += TEXT_HEIGHT;
    }

    public void updateHangmanDisplay(int guessCount) {
        reset(); // Clear previous drawings
        int x = getWidth() / 2; // Center x-coordinate
        int y = getHeight() / 2; // Center y-coordinate
        int bodyLength = getHeight() / 4; // Length of the body
        int limbLength = bodyLength / 4; // Length of arms and legs

        try {
            String filePath = "D:\\Users\\12013544\\Downloads\\HangmanStarter-master\\src\\assets\\display" + guessCount + ".txt";
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                printText(line); // Print to canvas
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
