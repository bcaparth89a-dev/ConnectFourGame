import javax.swing.*;
import java.awt.*;

public class ConnectFourGUI extends JFrame {

    int rows = 6, cols = 7;
    JButton[][] grid = new JButton[rows][cols];
    char[][] board = new char[rows][cols];

    char player = 'X';
    boolean vsComputer = true;

    JLabel status;

    public ConnectFourGUI() {

        setTitle("Connect Four - AI");
        setSize(700, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize board
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                board[i][j] = '.';

        // Mode selection
        String[] options = {"2 Players", "Vs Computer"};
        int choice = JOptionPane.showOptionDialog(
                this, "Select Mode", "Connect Four",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, options, options[1]
        );

        vsComputer = (choice == 1);

        // Top panel
        JPanel top = new JPanel();
        status = new JLabel("Player X Turn");
        JButton reset = new JButton("Restart");

        reset.addActionListener(e -> resetGame());

        top.add(status);
        top.add(reset);
        add(top, BorderLayout.NORTH);

        // Board UI
        JPanel panel = new JPanel(new GridLayout(rows, cols, 8, 8));
        panel.setBackground(new Color(0, 70, 160));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                RoundButton btn = new RoundButton();
                int col = j;

                btn.addActionListener(e -> {
                    if (player == 'X' || !vsComputer) {
                        drop(col);
                    }
                });

                grid[i][j] = btn;
                panel.add(btn);
            }
        }

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    // DROP WITH ANIMATION
    void drop(int col) {

        new Thread(() -> {

            for (int i = rows - 1; i >= 0; i--) {

                if (board[i][col] == '.') {

                    // animation
                    for (int k = 0; k <= i; k++) {

                        if (k > 0) grid[k - 1][col].setBackground(Color.WHITE);

                        grid[k][col].setBackground(
                                player == 'X' ? Color.RED : Color.YELLOW
                        );

                        sleep(60);
                    }

                    board[i][col] = player;

                    // win check
                    if (checkWin(player)) {
                        showMessage("🎉 " + player + " Wins!");
                        resetGame();
                        return;
                    }

                    if (isFull()) {
                        showMessage("Game Draw!");
                        resetGame();
                        return;
                    }

                    // switch player
                    player = (player == 'X') ? 'O' : 'X';
                    updateStatus();

                    // AI move
                    if (vsComputer && player == 'O') {
                        int aiMove = getBestMove();
                        drop(aiMove);
                    }

                    return;
                }
            }

            showMessage("Column Full!");

        }).start();
    }

    // 🔥 MINIMAX AI
    int getBestMove() {

        int bestScore = Integer.MIN_VALUE;
        int bestCol = 0;

        for (int c = 0; c < cols; c++) {

            if (isValidMove(c)) {

                int r = getRow(c);
                board[r][c] = 'O';

                int score = minimax(5, false);

                board[r][c] = '.';

                if (score > bestScore) {
                    bestScore = score;
                    bestCol = c;
                }
            }
        }

        return bestCol;
    }

    int minimax(int depth, boolean isMax) {

        if (checkWin('O')) return 1000;
        if (checkWin('X')) return -1000;

        if (depth == 0 || isFull()) return 0;

        if (isMax) {
            int best = Integer.MIN_VALUE;

            for (int c = 0; c < cols; c++) {
                if (isValidMove(c)) {
                    int r = getRow(c);
                    board[r][c] = 'O';

                    best = Math.max(best, minimax(depth - 1, false));

                    board[r][c] = '.';
                }
            }
            return best;

        } else {
            int best = Integer.MAX_VALUE;

            for (int c = 0; c < cols; c++) {
                if (isValidMove(c)) {
                    int r = getRow(c);
                    board[r][c] = 'X';

                    best = Math.min(best, minimax(depth - 1, true));

                    board[r][c] = '.';
                }
            }
            return best;
        }
    }

    boolean isValidMove(int col) {
        return board[0][col] == '.';
    }

    int getRow(int col) {
        for (int i = rows - 1; i >= 0; i--) {
            if (board[i][col] == '.') return i;
        }
        return -1;
    }

    // UI Button
    class RoundButton extends JButton {
        public RoundButton() {
            setBackground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
        }

        protected void paintComponent(Graphics g) {
            g.setColor(getBackground());
            g.fillOval(0, 0, getWidth(), getHeight());
        }

        protected void paintBorder(Graphics g) {
            g.setColor(Color.BLACK);
            g.drawOval(0, 0, getWidth()-1, getHeight()-1);
        }
    }

    void updateStatus() {
        status.setText(
                player == 'X'
                        ? "Player X Turn"
                        : (vsComputer ? "Computer Turn" : "Player O Turn")
        );
    }

    void sleep(int ms) {
        try { Thread.sleep(ms); } catch (Exception e) {}
    }

    void showMessage(String msg) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(this, msg)
        );
    }

    void resetGame() {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++) {
                board[i][j] = '.';
                grid[i][j].setBackground(Color.WHITE);
            }

        player = 'X';
        updateStatus();
    }

    // Win logic
    boolean checkWin(char p) {

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols - 3; j++)
                if (board[i][j]==p && board[i][j+1]==p && board[i][j+2]==p && board[i][j+3]==p)
                    return true;

        for (int i = 0; i < rows - 3; i++)
            for (int j = 0; j < cols; j++)
                if (board[i][j]==p && board[i+1][j]==p && board[i+2][j]==p && board[i+3][j]==p)
                    return true;

        for (int i = 0; i < rows - 3; i++)
            for (int j = 0; j < cols - 3; j++)
                if (board[i][j]==p && board[i+1][j+1]==p && board[i+2][j+2]==p && board[i+3][j+3]==p)
                    return true;

        for (int i = 3; i < rows; i++)
            for (int j = 0; j < cols - 3; j++)
                if (board[i][j]==p && board[i-1][j+1]==p && board[i-2][j+2]==p && board[i-3][j+3]==p)
                    return true;

        return false;
    }

    boolean isFull() {
        for (int j = 0; j < cols; j++)
            if (board[0][j] == '.') return false;
        return true;
    }

    public static void main(String[] args) {
        new ConnectFourGUI();
    }
}