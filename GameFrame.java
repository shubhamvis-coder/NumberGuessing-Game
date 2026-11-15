
import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.ArrayList;

public class GameFrame extends JFrame {

    private JLabel messageLabel;
    private JLabel attemptsLabel;
    private JLabel scoreLabel;

    private JTextField guessField;
    private JButton submitButton;
    private JButton restartButton;

    private JComboBox<String> difficultyBox;

    private Random random = new Random();

    private ArrayList<String> history = new ArrayList<>();

    private int secretNumber;
    private int attempts;
    private int maxAttempts;
    private int maxNumber;

    private int totalRounds = 0;
    private int totalWins = 0;

    private boolean gameOver = false;

    public GameFrame() {

        setTitle("Number Guessing Game");
        setSize(520, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        createGUI();
        startNewGame();

        setVisible(true);
    }

    private void createGUI() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(235, 243, 255));
        mainPanel.setBorder(
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        JLabel title = new JLabel(
            "NUMBER GUESSING GAME",
            SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 23));
        title.setForeground(new Color(30, 60, 110));

        mainPanel.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(8, 1, 8, 8));
        centerPanel.setBackground(new Color(235, 243, 255));

        centerPanel.add(new JLabel(
            "Select Difficulty:",
            SwingConstants.CENTER
        ));

        difficultyBox = new JComboBox<>(
            new String[]{"Easy", "Medium", "Hard"}
        );

        centerPanel.add(difficultyBox);

        guessField = new JTextField();
        guessField.setFont(new Font("Arial", Font.PLAIN, 18));
        guessField.setHorizontalAlignment(JTextField.CENTER);

        centerPanel.add(new JLabel(
            "Enter your guess:",
            SwingConstants.CENTER
        ));

        centerPanel.add(guessField);

        submitButton = new JButton("SUBMIT GUESS");
        submitButton.setBackground(new Color(40, 100, 200));
        submitButton.setForeground(Color.WHITE);

        centerPanel.add(submitButton);

        messageLabel = new JLabel(
            "Guess the secret number!",
            SwingConstants.CENTER
        );

        messageLabel.setFont(new Font("Arial", Font.BOLD, 15));
        centerPanel.add(messageLabel);

        attemptsLabel = new JLabel(
            "Attempts: 0",
            SwingConstants.CENTER
        );

        centerPanel.add(attemptsLabel);

        scoreLabel = new JLabel(
            "Wins: 0 | Rounds: 0",
            SwingConstants.CENTER
        );

        centerPanel.add(scoreLabel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(235, 243, 255));

        restartButton = new JButton("PLAY AGAIN");
        JButton historyButton = new JButton("GAME HISTORY");

        bottomPanel.add(restartButton);
        bottomPanel.add(historyButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        submitButton.addActionListener(e -> checkGuess());

        restartButton.addActionListener(e -> startNewGame());

        difficultyBox.addActionListener(e -> startNewGame());

        guessField.addActionListener(e -> checkGuess());

        historyButton.addActionListener(e -> showHistory());
    }

    private void startNewGame() {

        String difficulty =
            (String) difficultyBox.getSelectedItem();

        if ("Easy".equals(difficulty)) {
            maxNumber = 50;
            maxAttempts = 10;
        }
        else if ("Hard".equals(difficulty)) {
            maxNumber = 200;
            maxAttempts = 5;
        }
        else {
            maxNumber = 100;
            maxAttempts = 7;
        }

        secretNumber = random.nextInt(maxNumber) + 1;

        attempts = 0;
        gameOver = false;

        guessField.setText("");
        guessField.setEnabled(true);
        submitButton.setEnabled(true);

        messageLabel.setText(
            "Guess a number between 1 and " + maxNumber
        );

        attemptsLabel.setText(
            "Attempts: 0 / " + maxAttempts
        );

        scoreLabel.setText(
            "Wins: " + totalWins + " | Rounds: " + totalRounds
        );

        guessField.requestFocusInWindow();
    }

    private void checkGuess() {

        if (gameOver) {
            return;
        }

        String input = guessField.getText().trim();

        if (input.isEmpty()) {
            messageLabel.setText("Please enter a number!");
            return;
        }

        int guess;

        try {
            guess = Integer.parseInt(input);
        }
        catch (NumberFormatException ex) {
            messageLabel.setText("Enter a valid whole number!");
            return;
        }

        if (guess < 1 || guess > maxNumber) {
            messageLabel.setText(
                "Enter number between 1 and " + maxNumber
            );
            return;
        }

        attempts++;

        if (guess > secretNumber) {
            messageLabel.setText("Too High! Try a smaller number.");
        }
        else if (guess < secretNumber) {
            messageLabel.setText("Too Low! Try a bigger number.");
        }
        else {
            messageLabel.setText(
                "CORRECT! You won in " + attempts + " attempts!"
            );

            totalWins++;
            finishGame(true);
        }

        attemptsLabel.setText(
            "Attempts: " + attempts + " / " + maxAttempts
        );

        guessField.setText("");

        if (!gameOver && attempts >= maxAttempts) {

            messageLabel.setText(
                "GAME OVER! Number was " + secretNumber
            );

            finishGame(false);
        }
    }

    private void finishGame(boolean won) {

        gameOver = true;
        totalRounds++;

        history.add(
            "Round " + totalRounds +
            " - " + (won ? "Won" : "Lost") +
            " - Attempts: " + attempts
        );

        guessField.setEnabled(false);
        submitButton.setEnabled(false);

        scoreLabel.setText(
            "Wins: " + totalWins + " | Rounds: " + totalRounds
        );
    }

    private void showHistory() {

        if (history.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "No games played yet."
            );
            return;
        }

        StringBuilder records = new StringBuilder();

        for (String record : history) {
            records.append(record).append("\n");
        }

        JOptionPane.showMessageDialog(
            this,
            records.toString(),
            "Game History",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}