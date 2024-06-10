import acm.program.*;

import java.io.*;
import java.util.*;

public class Hangman extends ConsoleProgram {

    // dimensions of window
    private static final int APPLICATION_WIDTH = 1080;
    private static final int APPLICATION_HEIGHT = 640;

    private int gamesPlayed = 0;
    private int gamesWon = 0;
    private int bestGame = Integer.MAX_VALUE;

    public void run() {
        intro();
        while (true) {
            String filename = promptUserForFile("Enter dictionary filename (without .txt): ", "src/assets");
            if (filename == null) {
                continue;
            }
            String randomWord = getRandomWord(filename + ".txt");
            playOneGame(randomWord);
        }
    }

    private void intro() {
        String[] lines = {
                "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
                "",
                "Welcome to Hangman!",
                "I will think of a random word.",
                "You'll try to guess its letters.",
                "Every time you guess a letter",
                "that isn't in my word, a new body",
                "part of the hanging man appears.",
                "Good luck!",
                "",
                "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
        };

        int consoleWidth = 60;

        for (String line : lines) {
            int padding = (consoleWidth - line.length()) / 2;
            println(" ".repeat(padding) + line);
        }
    }

    private int playOneGame(String secretWord) {
        int guessCount = 8;
        String guessedLetters = "";
        String hint = createHint(secretWord, guessedLetters);

        while (guessCount > 0 && hint.contains("-")) {
            println("Secret word: " + hint);
            println("Your guesses: " + guessedLetters);
            println("Guesses left: " + guessCount);
            canvas.updateHangmanDisplay(guessCount); // Display hangman state
            char guess = readGuess(guessedLetters);
            guessedLetters += guess;

            if (secretWord.indexOf(guess) >= 0) {
                println("Correct!");
            } else {
                println("Incorrect.");
                guessCount--;
            }
            hint = createHint(secretWord, guessedLetters);
        }

        canvas.updateHangmanDisplay(guessCount); // Display final hangman state

        if (!hint.contains("-")) {
            println("Congratulations! You've guessed the word: " + secretWord);
            gamesWon++;
        } else {
            println("Sorry, you've run out of guesses. The word was: " + secretWord);
        }

        gamesPlayed++;
        if (guessCount < bestGame) {
            bestGame = guessCount;
        }

        // Display guide if the user wants to see overall statistics
        String input = readLine("Do you want to see the overall statistics? (yes/no) ");
        if (input.equalsIgnoreCase("yes")) {
            displayStats();
        }


        return 0;
    }


    private String createHint(String secretWord, String guessedLetters) {
        StringBuilder hint = new StringBuilder();
        for (char c : secretWord.toCharArray()) {
            if (guessedLetters.indexOf(c) >= 0) {
                hint.append(c);
            } else {
                hint.append('-');
            }
        }
        return hint.toString();
    }

    private char readGuess(String guessedLetters) {
        char guess;
        while (true) {
            String input = readLine("Your guess? ");
            if (input.equalsIgnoreCase("stats")) {
                displayStats();
                continue;
            }
            if (input.length() != 1) {
                println("Please enter a single letter.");
                continue;
            }
            guess = input.toUpperCase().charAt(0);
            if (!Character.isLetter(guess)) {
                println("Please enter a valid letter.");
                continue;
            }
            if (guessedLetters.indexOf(guess) >= 0) {
                println("You've already guessed that letter.");
                continue;
            }
            break;
        }
        return guess;
    }

    private void displayStats() {
        double winPercent = (gamesPlayed != 0) ? ((double) gamesWon / gamesPlayed) * 100 : 0;
        int consoleWidth = 60; // Adjust this value based on your console width
        int padding = (consoleWidth - 28) / 2; // 28 is the length of the longest line "Overall statistics:"

        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        println(" ".repeat(padding) + "Overall statistics:" + " ".repeat(padding));
        println(" ".repeat(padding) + "Games played: " + gamesPlayed + " ".repeat(padding));
        println(" ".repeat(padding) + "Games won: " + gamesWon + " ".repeat(padding));
        println(" ".repeat(padding) + "Win percent: " + String.format("%.1f%%", winPercent) + " ".repeat(padding));
        println(" ".repeat(padding) + "Best game: " + ((bestGame == Integer.MAX_VALUE) ? "N/A" : (bestGame + " guess(es) remaining")) + " ".repeat(padding));
        println(" ".repeat(padding) + "Thanks for playing!" + " ".repeat(padding));
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    }


    public String promptUserForFile(String prompt, String directory) {
        while (true) {
            String filename = readLine(prompt);
            File file = new File(directory + "/" + filename + ".txt");
            if (file.exists() && file.isFile()) {
                return filename;
            } else {
                println("File not found. Please try again.");
            }
        }
    }

    private String getRandomWord(String filename) {
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/assets/" + filename))) {
            int n = Integer.parseInt(br.readLine().trim());
            for (int i = 0; i < n; i++) {
                words.add(br.readLine().trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        Random rand = new Random();
        return words.get(rand.nextInt(words.size()));
    }

    public void init() {
        canvas = new HangmanCanvas();
        setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
        add(canvas);
        canvas.reset();
    }

    public static void main(String[] args) {
        new Hangman().start(args);
    }

    public HangmanCanvas canvas;
}
