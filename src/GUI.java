import javax.swing.*;
import javax.swing.JFrame;
import java.awt.*;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;
import java.io.FileWriter;

public class GUI  extends JFrame {
    private JButton toggleButton;
    public int toggleNum = 0;
    private JTextArea textArea;
    static boolean result = false;
    private JPanel chessboardPanel;
    private JPanel selectedPiecePanel;
    private QueenIconsPanel queensIconsPanel;
    private JButton answer;
    private JButton tryagain;
    private ImageIcon queenIcon;
    private Cursor originalCursor;
    private JPanel WelcomeToLeaderBoard;
    private JButton hallofFrame;
    private JButton showsolution;
    private JTextArea winnerName;
    private JTextArea hallofFame;
    // Flag to indicate whether the cursor is in queen mode
    private boolean queenMode = false;

    // Boolean array to track queens' visibility on the chessboard
    private boolean[][] queensVisible = new boolean[8][8];
    private int[][] chessboardState = new int[8][8];
    private boolean [][] simchessboardState = new boolean[8][8];
    private int queensPlaced = 0;
    public ChessboardPanel board;
    private static JLabel displayLabel;
    private static JTextArea displayWinner;
    private static JTextArea outline;

    private JPanel solution;
    public GUI() {

        //Queen Icon Setup. Loads from a Png File and Resizes to 60x60 pixels
        queenIcon = new ImageIcon("8541656_chess_queen_icon.png");
        queenIcon = new ImageIcon(queenIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
         JTextArea Denis = new JTextArea("Denis the Great");

        hallofFame = new JTextArea("This is The Hall of Fame:");

        //Setting Up the JFrame
        setTitle("N Queens");
        setSize(1200, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.BLACK);

        //ChessBoard Panel - Buttons
        chessboardPanel = createChessBoard();
        add(chessboardPanel, BorderLayout.CENTER);

        //The Bottom Check Button
        solution = checkButton();
        add(solution, BorderLayout.SOUTH);

        setLocationRelativeTo(null); // Center the JFrame
        //The Right Side Panel
        JPanel sidekick = sidePanel();
        add(sidekick, BorderLayout.EAST);;

        //The TOP Headline Component.
        JButton headline = welcome();
        add(headline, BorderLayout.NORTH);

        selectedPiecePanel = sidePanel();
        add(selectedPiecePanel, BorderLayout.EAST);

        //The Panel which Stores the 8 Queens
        queensIconsPanel = new QueenIconsPanel();
        add(queensIconsPanel, BorderLayout.WEST);

         //Get Cursor
        originalCursor = getCursor();

        displayLabel = new JLabel();
        displayWinner = new JTextArea();
        displayWinner.setBackground(new Color(0xDEC419));

        //selectedPiecePanel.add(outline);
        textArea = new JTextArea();



        tryagain = new JButton("TryAgain");

        displayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ///Toggle Listener method
        toggleButton = new JButton("Toggle Color Scheme");
        toggleButton.setPreferredSize(new Dimension(150, 30));
        toggleButton.addActionListener(new ToggleButtonListener());


        WelcomeToLeaderBoard = new JPanel();

        selectedPiecePanel.add( WelcomeToLeaderBoard);
        //selectedPiecePanel.add(displayWinner);
        selectedPiecePanel.add(toggleButton);

        WelcomeToLeaderBoard.add(hallofFame);
        WelcomeToLeaderBoard.add(Denis);
        selectedPiecePanel.setBackground(new Color(0x22A1EE));
        String [] hello = readNamesFromFile("output.txt");
        for (String name : hello) {
            JLabel nameLabel = new JLabel(name);
            WelcomeToLeaderBoard.add(nameLabel);
            System.out.print(nameLabel.getText());
        }
    }

    private static String[] readNamesFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Read all lines from the file
            return br.lines().toArray(String[]::new);
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0]; // Return an empty array in case of an error
        }
    }

    private JPanel checkButton() {
        JPanel CheckBar = new JPanel(new GridLayout(2, 1));
        CheckBar.setPreferredSize(new Dimension(300, 100));
        return CheckBar;
    }

    private JLabel selectedQueen;

    //Configuration for the Bottom Check Button
    private JButton QueensPanelBoard() {
        JButton Score = new JButton();
        Score.setPreferredSize(new Dimension(100, 100));
        Color hello = new Color(0xD0B2B2);
        Score.setBackground(hello);
        Score.setBounds(100, 100, 200, 200);
        return Score;
    }

    private JButton welcome() {
        JButton top = new JButton("<html>Welcome to 8 Queens Puzzle!<br>Click on a queen on the left side and click on the square on the chessboard you want to place it on.<br>Place the queens so that no queen can attack another queen.</html>");        top.setPreferredSize(new Dimension(100, 100));
        Color hello = new Color(0x7BBCE3);
        top.setBackground(hello);
        top.setBounds(100, 100, 200, 200);
        return top;
    }

    // This is the SidePanel of the GUI
    private JPanel sidePanel() {
        JPanel sideBar = new JPanel(new GridLayout(3, 1));
        sideBar.setPreferredSize(new Dimension(300, 100));
        return sideBar;

    }

    private JPanel createChessBoard() {
        JPanel chessBoardPanel = new JPanel(new GridLayout(8, 8));

        //chessBoardPanel.setBounds(100,0,400,400);
        chessBoardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        chessBoardPanel.setPreferredSize(new Dimension(600, 600));

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton square = new JButton();
                square.setPreferredSize(new Dimension(20, 20)); // change later
                square.addMouseListener(new ChessBoardMouseListener());
                if ((row + col) % 2 == 0) {
                    Color color1 = new Color(244, 246, 225);
                    square.setBackground(Color.white);
                } else {
                    Color color = new Color(22, 148, 3);
                    square.setBackground(color);
                }
                chessboardState[row][col] = 0;
                simchessboardState[row][col] = false;
                chessBoardPanel.add(square);
            }
        }
        setLocationRelativeTo(null);
        return chessBoardPanel;
    }

    // This code is the MouseListener Action User which detects the Queen
    private class ChessBoardMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (queenMode) {
                if (e.getSource() instanceof JButton) {
                    JButton clickedSquare = (JButton) e.getSource();
                    int row = chessboardPanel.getComponentZOrder(clickedSquare) / 8;
                    int col = chessboardPanel.getComponentZOrder(clickedSquare) % 8;
                    if (!queensVisible[row][col]) {
                        clickedSquare.add(new JLabel(queenIcon));
                        queensVisible[row][col] = true;
                        chessboardState[row][col] = 1;
                        simchessboardState[row][col] = true;
                        queensPlaced++;
                        queensIconsPanel.addQueenIcon(selectedQueen);
                        chessboardPanel.revalidate();
                        chessboardPanel.repaint();
                        // Set the cursor to the original cursor for the entire frame
                        setCursor(originalCursor);

                        queenMode = false;
                        System.out.println(queensPlaced);

                        if (queensPlaced == 8) {
                            // After 8 Queens are Placed this prints the State of the Board
                            printChessBoardState();
                            // boolean result = chessboardState.solvePuzzle();
                        }
                    }
                }
            } else if (selectedPiecePanel.getComponentCount() > 0) {
                JLabel selectedPiece = (JLabel) selectedPiecePanel.getComponent(0);

                // If clicked anywhere except the grid, revert the cursor to normal and bring back the selected queen
                setCursor(originalCursor);
                queensIconsPanel.revertLastRemovedQueen();
                queenMode = false;
            }
        }
    }

    private class QueenPieceMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            JLabel clickedPiece = (JLabel) e.getSource();

            if (!queenMode) {
                // Set the cursor to the queen icon for the entire frame
                setCursor(queenIconToCursor(clickedPiece.getIcon()));

                // Remove the queen icon
                queensIconsPanel.removeQueenIcon(clickedPiece);
                repaint();

                // Set the flag to indicate queen mode and store the selected queen
                queenMode = true;
                selectedQueen = clickedPiece;
            }
        }
    }

    private class QueenIconsPanel extends JPanel {

        private JLabel lastRemovedQueen;

        public QueenIconsPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


            setPreferredSize(new Dimension(100, 100));
            // Initialize queen pieces on the left panel
            for (int i = 0; i < 8; i++) {
                JLabel queenPiece = new JLabel(queenIcon);
                queenPiece.addMouseListener(new QueenPieceMouseListener());
                add(queenPiece);
            }
        }

        public void addQueenIcon(JLabel queenIcon) {
            //TODO
        }

        public void removeQueenIcon(JLabel queenIcon) {
            lastRemovedQueen = queenIcon;
            queenIcon.setVisible(false);
            repaint();
        }

        public void revertLastRemovedQueen() {
            if (lastRemovedQueen != null) {
                lastRemovedQueen.setVisible(true);
                repaint();
                lastRemovedQueen = null;
            }
        }
    }

    private Cursor queenIconToCursor(Icon icon) {
        Image image = ((ImageIcon) icon).getImage();
        return Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0), "customCursor");
    }

    // This is the State of the Board
    public void printChessBoardState()  {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(chessboardState[i][j] + " ");
            }
            System.out.println();
        }
       result = this.solvePuzzle(this.chessboardState);

        System.out.println(solvePuzzle(chessboardState));
        if(result)
        {
            System.out.println("Congratulations! You solved the puzzle.");
            System.out.println("Congratulations! You solved the puzzle.");
            // Save the player's name to the Hall of Fame
            Scanner nameScanner = new Scanner(System.in);
            System.out.print("Enter your name for the Hall of Fame: ");
            String playerName = nameScanner.next();


            try {
                // Create a FileWriter with the 'true' parameter for appending
                BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt", true));

                // Write the player name to the file
                bw.write(  "<html>"+playerName+"<br></html>");
                bw.newLine(); // Add a newline for better readability

                // Close the BufferedWriter
                bw.close();

                System.out.println("Name added to the file successfully.");

            } catch (IOException e) {
                e.printStackTrace();
            }

            tryagain.addActionListener(new ShowSolution ());
            //answer.addActionListener(new ToggleButtonListener());
            JButton display = new JButton("Display Solution");
            display.addActionListener(new TryAgainButtonListener());

            solution.add(tryagain);
            solution.add(display);
        }
        else {

            tryagain.addActionListener(new ShowSolution ());
            //answer.addActionListener(new ToggleButtonListener());
            JButton display = new JButton("Display Solution");
            display.addActionListener(new TryAgainButtonListener());

            solution.add(tryagain);
            solution.add(display);
            //resetGame();
        }
    }

    //This method adds a mouseListener to the Try Again Button
    private class TryAgainButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            displaySolution();
        }
    }

    //This method adds a mouseListener to the Show Solution Button
    private class ShowSolution implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            resetGame();
        }
    }

    private void resetGame() {
        // Make all queens on the board invisible
        for (int row = 0; row < 8; ++row) {
            for (int col = 0; col < 8; ++col) {
                if (queensVisible[row][col]) {
                    Component component = chessboardPanel.getComponent(row * 8 + col);
                    if (component instanceof JButton) {
                        JButton square = (JButton) component;
                        square.removeAll();
                        queensVisible[row][col] = false;
                    }
                }
            }
        }

        // Make the queens on the left panel visible and selectable again
        queensIconsPanel.removeAll();
        for (int i = 0; i < 8; ++i) {
            JLabel queenPiece = new JLabel(queenIcon);
            queenPiece.addMouseListener(new QueenPieceMouseListener());
            queensIconsPanel.add(queenPiece);
        }

        // Reset game state variables
        queensPlaced = 0;
        chessboardPanel.revalidate();
        chessboardPanel.repaint();
        queensIconsPanel.revalidate();
        queensIconsPanel.repaint();
        displayLabel.setText("");
        displayWinner.setText("");
    }

    //This is the Display Solution Method which shows the solution of the Board once the Player gets it wrong
    private void displaySolution() {
        int[][] solutionMatrix = {
                {0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0},
                {1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1},
                {0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0}
        };

        for (int row = 0; row < 8; ++row) {
            for (int col = 0; col < 8; ++col) {
                boolean isVisible = solutionMatrix[row][col] == 1;
                Component component = chessboardPanel.getComponent(row * 8 + col);

                if (component instanceof JButton) {
                    JButton square = (JButton) component;

                    if (isVisible && !queensVisible[row][col]) {
                        // Queen is in the corresponding spot and not already visible
                        square.add(new JLabel(queenIcon));
                        queensVisible[row][col] = true;
                    } else if (!isVisible && queensVisible[row][col]) {
                        // Queen is not in the corresponding spot and currently visible
                        square.removeAll();
                        queensVisible[row][col] = false;
                    }
                }
            }
        }
        chessboardPanel.revalidate();
        chessboardPanel.repaint();
    }

    // This Allows for Light More
    private class ToggleButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Check if the program is not in queen mode before toggling the color scheme
            if (!queenMode) {
                toggleNum++;
                toggleColorScheme();
            }
        }
    }

    //These are all the Toggle Scheme Methods
    private void toggleColorScheme() {
        if ((toggleNum % 2) == 1) {
            setDarkMode();
        } else {
            setLightMode();
        }
    }

    private void setDarkMode() {
        selectedPiecePanel.setBackground(Color.DARK_GRAY);
        chessboardPanel.setBackground(Color.DARK_GRAY);
        queensIconsPanel.setBackground(Color.DARK_GRAY);
        toggleButton.setBackground(Color.DARK_GRAY);

        Component[] components = chessboardPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                JButton square = (JButton) component;
                int index = chessboardPanel.getComponentZOrder(square);
                int row = index / 8;
                int col = index % 8;

                if ((row + col) % 2 == 0) {
                    square.setBackground(new Color(50, 50, 50));
                } else {
                    square.setBackground(new Color(30, 30, 30));
                }
            }
        }

        updateComponents();
    }

    private void setLightMode() {
        selectedPiecePanel.setBackground(Color.LIGHT_GRAY);
        chessboardPanel.setBackground(Color.LIGHT_GRAY);
        queensIconsPanel.setBackground(Color.LIGHT_GRAY);
        toggleButton.setBackground(Color.LIGHT_GRAY);

        Component[] components = chessboardPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                JButton square = (JButton) component;
                int index = chessboardPanel.getComponentZOrder(square);
                int row = index / 8;
                int col = index % 8;

                if ((row + col) % 2 == 0) {
                    square.setBackground(new Color(244, 246, 225));
                } else {
                    square.setBackground(new Color(22, 148, 3));
                }
            }
        }

        updateComponents();
    }

    //This is the Backend Solution of the N queens
    public boolean solvePuzzle(int[][] positions) {
        return validRow(positions) && validCol(positions) && validDiagonal(positions);
    }

    public boolean validRow(int[][] positions) {
        for (int row = 0; row < 8; row++) {
            int queenCount = 0;
            for (int col = 0; col < 8; col++) {
                if (positions[row][col] == 1) { // if there is a queen at this position
                    queenCount++; // increment queenCount
                }
            }
            if (queenCount > 1) {
                return false; // if there is more than 1 queen in a row, return false
            }
        }
        return true; // if there is not more than 1 queen in a row, return true
    }

    public boolean validCol(int[][] positions) {
        for (int col = 0; col < 8; col++) {
            int queenCount = 0;
            for (int row = 0; row < 8; row++) {
                if (positions[row][col] == 1) {
                    queenCount++;
                }
            }
            if (queenCount > 1) {
                return false;
            }
        }
        return true;
    }


    public boolean validDiagonal(int[][] board) {
        int size = board.length;

        // Check main diagonals (top-left to bottom-right)
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col] == 1) {
                    if (isDiagonalCollision(board, row, col, 1, 1) || // Check lower right
                            isDiagonalCollision(board, row, col, -1, -1)) { // Check upper left
                        return false;
                    }
                }
            }
        }

        // Check secondary diagonals (top-right to bottom-left)
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col] == 1) {
                    if (isDiagonalCollision(board, row, col, 1, -1) || // Check lower left
                            isDiagonalCollision(board, row, col, -1, 1)) { // Check upper right
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private static boolean isDiagonalCollision(int[][] board, int row, int col, int rowIncrement, int colIncrement) {
        int size = board.length;

        while (row + rowIncrement >= 0 && row + rowIncrement < size && col + colIncrement >= 0 && col + colIncrement < size) {
            row += rowIncrement;
            col += colIncrement;

            if (board[row][col] == 1) {
                return true; // There is a collision on the diagonal
            }
        }

        return false; // No collision on the diagonal
    }
    
    //This method updates the Board continously
    private void updateComponents() {
        chessboardPanel.revalidate();
        chessboardPanel.repaint();
        selectedPiecePanel.revalidate();
        selectedPiecePanel.repaint();
        queensIconsPanel.revalidate();
        queensIconsPanel.repaint();
        toggleButton.revalidate();
        toggleButton.repaint();
    }
    //The Main Method runs the GUI
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            GUI chessBoardGUI = new GUI();
            chessBoardGUI.setVisible(true);

        });


    }
}




