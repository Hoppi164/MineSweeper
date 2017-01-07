import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

// import java.util.Arrays.*;

class Game extends JFrame {
	Cell board[][];
	JPanel div;
	JPanel topPanel;
	JPanel bottomPanel;
	JLabel timeLabel;
	javax.swing.Timer scoreTimer;
	JLabel smile;
	int numRows;
	int numBombs;
	boolean gameRunning;
	public static void main(String[] args) {
		Game g = new Game(22);//11 is passed in as a numRows value todo: create a numRows setting selector

	}

	public Game(int numRows) {
		gameRunning = true;
		topPanel = new JPanel();
		bottomPanel = new JPanel();
		div = new JPanel();//create main panel window
		this.add(div);//todo: add scoring feature
		div.add(topPanel);
		div.add(bottomPanel);
		//todo: add menu bars and a restart button
		//todo: add a timer

		this.numRows = numRows;//copy across numRows value
		numBombs = 50;//numRows starts off at default 50 bombs
		board = new Cell[numRows][numRows];
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numRows; j++) {
				board[i][j] = new Cell(i, j);
				board[i][j].setPreferredSize(new Dimension(25, 25));
				board[i][j].setMargin(new Insets(0, 0, 0, 0));
			}
		}
		newBoard();//create game board
		createWindow();//call gui show
	}
	private void floodFill(int i, int j) {//selects given cell. and reveals the value
		Cell cell = board[i][j];		//if that cell is a 0: reveal all surrounding cells as these are sure to not be bombs
		if (! cell.getVisible()) {
			cell.setVisible(true);
			cell.setText(cell.getValue() + "");
			cell.setBackground(Color.lightGray);
			if (cell.getValue() == 0) {
				cell.setText(" ");
				boolean hasRowNorth = false;
				boolean hasRowEast = false;
				boolean hasRowSouth = false;
				boolean hasRowWest = false;
				if (i > 0) hasRowNorth = true;
				if (j < numRows - 1) hasRowEast = true;
				if (i < numRows - 1 ) hasRowSouth = true;
				if (j > 0) hasRowWest = true;

				//check all cells in clockwise order starting from North
				if (hasRowNorth) 				floodFill(i - 1, j  );
				if (hasRowNorth && hasRowEast) 	floodFill(i - 1, j + 1);
				if (hasRowEast) 				floodFill(i, j + 1);
				if (hasRowEast && hasRowSouth) 	floodFill(i + 1, j + 1);
				if (hasRowSouth) 				floodFill(i + 1, j  );
				if (hasRowSouth && hasRowWest) 	floodFill(i + 1, j - 1);
				if (hasRowWest) 				floodFill(i, j - 1);
				if (hasRowWest && hasRowNorth) 	floodFill(i - 1, j - 1);
			} else if (cell.getValue() == 99) {
				cell.setBackground(Color.RED);
				cell.setText("*");
				endGame();
			} else if (cell.getValue() == 1) {
				cell.setForeground(Color.BLUE);
			} else if (cell.getValue() == 2) {
				cell.setForeground(Color.GREEN.darker());
			} else if (cell.getValue() == 3) {
				cell.setForeground(Color.RED);
			} else if (cell.getValue() == 4) {
				cell.setForeground(Color.BLUE.darker());
			} else if (cell.getValue() == 5) {
				cell.setForeground(Color.MAGENTA);
			} else if (cell.getValue() == 6) {
				cell.setForeground(Color.CYAN);
			} else if (cell.getValue() == 7) {
				cell.setForeground(Color.ORANGE);
			} else if (cell.getValue() == 8) {
				cell.setForeground(Color.YELLOW);
			}
		}

	}
	private void newBoard() {
		for (int i = 0; i < numRows; i++) {//Instantiate Cells
			for (int j = 0; j < numRows; j++) {
				final Cell cell = board[i][j];
				Font f = new Font("Garamond", Font.BOLD, 12);
				cell.setFont(f);
				cell.setBackground(null);
				board[i][j].addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						if (SwingUtilities.isLeftMouseButton(e)) {
							if (gameRunning) {
								floodFill(cell.getI(), cell.getJ());
							}
						} else if (SwingUtilities.isRightMouseButton(e)) {
							if (gameRunning && cell.getVisible() == false) {
								board[cell.getI()][cell.getJ()].setBackground(Color.YELLOW);
								cell.setVisible(true);
							} else if (gameRunning && cell.getVisible()) {
								board[cell.getI()][cell.getJ()].setBackground(null);
								cell.setVisible(false);
							}
						}
					}
				});
				bottomPanel.add(board[i][j]);
			}
		}

		int bombLocations[] = new int[numBombs + 1]; //Create a list of bombs
		for (int bomb : bombLocations) {//Place Bombs Randomly
			bomb = getRandom(0, numRows * numRows);
		}
		validateBombPlacement(bombLocations);//Make sure two bombs arent in same cell
		for (int bomb : bombLocations) {//Make those cell's contain Bombs
			board[bomb / numRows][bomb % numRows].setValue(99);
		}
		System.out.println("TOTAL BOMBS: " + numBombs + "");
		board[0][0].setValue(0);//Hard Reset of cell 0, --> always safe ;)
		getCellNumbers();
	}
	private void createWindow() {//Set all default values
		String[] difficultyOptions = {"Easy", "Medium", "Hard", "Hardcore", "Impossible"};
		JButton newGame = new JButton("New Game");
		JButton solve = new JButton("Solve");
		JComboBox diff = new JComboBox(difficultyOptions);
		timeLabel = new JLabel("0");
		smile = new JLabel(":)");
		diff.setSelectedIndex(0);
		topPanel.add(newGame);
		topPanel.add(solve);
		topPanel.add(diff);
		topPanel.add(smile);
		topPanel.add(timeLabel);
		scoreTimer = new javax.swing.Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timeLabel.setText((Integer.parseInt(timeLabel.getText()) + 1) + "");
				repaint();
			}
		});
		scoreTimer.start();
		newGame.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					newGame();
				}
			}
		});
		solve.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					showNumbers();
				}
			}
		});
		diff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedOption = (String)diff.getSelectedItem();
				changeDifficulty(selectedOption);
			}
		});
		topPanel.setLayout(new GridLayout(1, 5));
		bottomPanel.setLayout(new GridLayout(numRows, numRows, 0, 0));
		setSize(new Dimension(800, 800));
		// pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	private int getRandom(int minimum, int maximum) {//Random Magic
		return minimum + (int)(Math.random() * maximum);
	}
	private void validateBombPlacement(int[] bombLocations) {//Make sure bombs arent in same cell as another bomb
		Arrays.sort(bombLocations);//sort list
		for (int i = 1; i < bombLocations.length; i++) {//check that the next bomb in list isnt the same vaue.
			if (bombLocations[i] == bombLocations[i - 1]) {//-- if it is: then it's the same cell
				bombLocations[i] = getRandom(0, numRows * numRows);//replant bomb in random cell
				validateBombPlacement(bombLocations);//revalidate (Recursive Inception Magic)
			}
		}
	}
	private void endGame() {
		scoreTimer.stop();
		smile.setText(":(");
		gameRunning = false;
	}
	private void newGame() {
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j <  numRows; j++ ) {
				Cell cell = board[i][j];
				cell.setText(" ");
				cell.setVisible(false);
				cell.setValue(0);
				cell.setForeground(null);
				cell.setBackground(null);
			}
		}
		newBoard();
		scoreTimer.stop();
		timeLabel.setText(0+"");
		smile.setText(":)");
		scoreTimer.start();
		gameRunning = true;
		// repaint();
	}
	private void showNumbers() {
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j <  numRows; j++ ) {
				Cell cell = board[i][j];
				cell.setText(cell.getValue() + "");
				if (cell.getValue() == 99) {
					cell.setText("*");
					cell.setBackground(Color.RED);
				} else if (cell.getValue() == 1) {
					cell.setForeground(Color.BLUE);
				} else if (cell.getValue() == 2) {
					cell.setForeground(Color.GREEN.darker());
				} else if (cell.getValue() == 3) {
					cell.setForeground(Color.RED);
				} else if (cell.getValue() == 4) {
					cell.setForeground(Color.BLUE.darker());
				} else if (cell.getValue() == 5) {
					cell.setForeground(Color.MAGENTA);
				} else if (cell.getValue() == 6) {
					cell.setForeground(Color.CYAN);
				} else if (cell.getValue() == 7) {
					cell.setForeground(Color.ORANGE);
				} else if (cell.getValue() == 8) {
					cell.setForeground(Color.YELLOW);
				} else {
					cell.setText(" ");
					cell.setBackground(Color.lightGray);
				}
			}
		}

	}
	public void changeDifficulty(String option) {
		if (option.equals("Easy")) {
			numBombs = 50;
		} else if (option.equals("Medium")) {
			numBombs = 75;
		} else if (option.equals("Hard")) {
			numBombs = 100;
		} else if (option.equals("Hardcore")) {
			numBombs = 150;
		} else if (option.equals("Impossible")) {
			numBombs = 250;
		}
		newGame();
	}
	private void getCellNumbers() { //calculate the numbers for each cell. !! IMPORTANT !! ONLY CALL THIS AFTER BOMBS HAVE BEEN ALLOCATED
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numRows; j++) {
				if (board[i][j].getValue() != 99) {
					int cellValue = 0;
					//determine which cells to not check for bombs (Edge and Corner Cases)
					boolean hasRowNorth = false;
					boolean hasRowEast = false;
					boolean hasRowSouth = false;
					boolean hasRowWest = false;
					if (i > 0) hasRowNorth = true;
					if (j < numRows - 1) hasRowEast = true;
					if (i < numRows - 1 ) hasRowSouth = true;
					if (j > 0) hasRowWest = true;

					//check all cells in clockwise order starting from North
					if (hasRowNorth && board[i - 1][j].getValue() == 99) cellValue++;
					if (hasRowNorth && hasRowEast && board[i - 1][j + 1].getValue() == 99) cellValue++;
					if (hasRowEast && board[i][j + 1].getValue() == 99) cellValue++;
					if (hasRowEast && hasRowSouth && board[i + 1][j + 1].getValue() == 99) cellValue++;
					if (hasRowSouth && board[i + 1][j].getValue() == 99) cellValue++;
					if (hasRowSouth && hasRowWest && board[i + 1][j - 1].getValue() == 99) cellValue++;
					if (hasRowWest && board[i][j - 1].getValue() == 99) cellValue++;
					if (hasRowWest && hasRowNorth && board[i - 1][j - 1].getValue() == 99) cellValue++;

					board[i][j].setValue(cellValue);
				}
			}
		}

	}
}