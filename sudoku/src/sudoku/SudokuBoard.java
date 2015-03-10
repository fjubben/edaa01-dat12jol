package sudoku;

public class SudokuBoard {
	final static int EMPTY_SQUARE = 0;
	int[][] board = new int[9][9]; // skapar en matris med 9 rader/kolumner
	

	/**
	 * Parameterlös konstruktor som skapar ett sukodu fyllt med tomma rutor.
	 */
	public SudokuBoard() { // 
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				board[row][col] = EMPTY_SQUARE; // brädet fylls med tomma rutor
			}
		}

	}

	/**
	 * Konstruktor ifall man redan har ett sudokubräda (int[9][9])
	 * @param preBoard en matris med 9 rader och 9 kolumner
	 */
	public SudokuBoard(int[][] preBoard) { // konstruktorn ifall man redan har
											// en bräda
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				board[row][col] = preBoard[row][col];
			}
		}

	}

	/**
	 * Sätter värdet i en viss ruta till value.
	 * 
	 * @param i
	 *            , raden i
	 * @param j
	 *            , kolumnen j
	 * @param value
	 *            , det värde som ska läggas in i matrisen
	 */
	public void setSquare(int i, int j, int value) {
		board[i][j] = value;
	}

	/**
	 * Hämtar värdet i en viss ruta.
	 * 
	 * @param i
	 *            , raden i
	 * @param j
	 *            , kolumnen j
	 */
	public int getSquare(int i, int j) {
		return board[i][j];
	}

	/**
	 * Skickar tillbaka det aktuella spelbrädet.
	 */
	public int[][] getBoard() {
		return board;
	}

	/**
	 * Börjar lösa sudokut med start i första rutan (längst upp till vänster).
	 * 
	 * @return True om en lösning kan hittas
	 */
	public boolean solve() { // börja lösa sudokut från ruta 0, 0
		return solve(0, 0);
	}

	/**
	 * Backtracking algoritm för att lösa ett sudoku
	 */	
	private boolean solve(int row, int col) { // den rekursiva lösningen

		if (board[row][col] == EMPTY_SQUARE) {// ifall rutan är tom

			if (row == 8 && col == 8) { // ifall vi är på sista rutan i sudokut
				for (int i = 1; i <= 9; i++) {
					board[row][col] = i; // prova att sätta in i
					if (checkSquare(row, col)) { // om i är giltigt så är
						return true; // sudokut är löst
					}
				}
				return false;
			}
			for (int i = 1; i < 10; i++) {
				board[row][col] = i; // testa med första bästa
				if (checkSquare(row, col)) { // funkar det man testat?
					if (col < 8) {
						if (solve(row, col + 1)) {// går nästa ruta att lösa?
							return true;
						}
					} else if (solve(row + 1, 0)) {// går nästa ruta att lösa?
						return true;
					}
				}

			}
			board[row][col] = EMPTY_SQUARE; // sätt tillbaka rutan till tom
			return false; // ifall det inte funkar så får man hoppa tillbaka

		} else { // annars är ju rutan inte tom

			if (row == 8 && col == 8) {
				return checkSquare(row, col);
			}
			if (checkSquare(row, col)) { // då kollar vi om det funkar
				if (col < 8) {
					return solve(row, col + 1);
				} else {
					return solve(row + 1, 0);
				}
			}
		}

		return false;
	}

	/**
	 * Skriver ut det aktuella spelbrädet i konsollen
	 */
	public void printBoard() {
		System.out.println();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				System.out.print("|" + board[i][j] + "|");
			}
			System.out.println();
		}
	}

	/**
	 * Kollar om värdet i en viss ruta är tillåtet
	 * 
	 * @param row
	 *            är vilken rad rutan ligger på
	 * @param col
	 *            är vilken kolumn rutan ligger på
	 * @return true om siffran i rutan är efter sudokus spelregler
	 */
	private boolean checkSquare(int row, int col) {
		int numberToCheck = board[row][col];
		board[row][col] = 0; // plockar temporärt bort värdet för kontrollen

		// ifall alla kontroller går igenom så skickas true tillbaka annars
		// falsk
		if (checkRow(row, numberToCheck) && checkCol(col, numberToCheck)
				&& checkPanel(row, col, numberToCheck)) {
			board[row][col] = numberToCheck; // stoppar tillbaka värdet
			return true;
		} else {
			board[row][col] = numberToCheck;// stoppar tillbaka värdet
			return false;
		}
	}

	/**
	 * kollar om en rad inte innehåller dubletter
	 * 
	 * @param row
	 *            är vilken rad rutan ligger på
	 * @param number
	 *            värdet som man kollar dubletter utifrån
	 * @return true om number är unikt för raden
	 */
	private boolean checkRow(int row, int number) {
		for (int i = 0; i < 9; i++) {
			if (board[row][i] == number) {
				return false;
			}
		}
		return true;
	}

	/**
	 * kollar om en kolumn inte innehåller dubletter
	 * 
	 * @param col
	 *            vilken kolumn rutan ligger på
	 * @param number
	 *            värdet som man kollar dubletter utifrån
	 * @return true om number är unikt för kolumnen
	 */
	private boolean checkCol(int col, int number) {
		for (int i = 0; i < 9; i++) {
			if (board[i][col] == number) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Kontrollerar att nummret number är unik i panelen.
	 * 
	 * @param row
	 *            vilken rad nummret ligger på
	 * @param col
	 *            vilken kolumn nummret ligger på
	 * @param number
	 *            det nummer som ska kontrolleras
	 * @return true om number är unikt för panelen
	 */
	private boolean checkPanel(int row, int col, int number) {
		int panelStartRow = (row / 3) * 3;
		int panelStartCol = (col / 3) * 3;

		for (int i = panelStartRow; i < panelStartRow + 3; i++) {
			for (int j = panelStartCol; j < panelStartCol + 3; j++)
				if (board[i][j] == number) {
					return false;
				}
		}

		return true;
	}

	/**
	 * Fyller brädet med tomma rutor
	 */
	public void clear() {
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				board[col][row] = EMPTY_SQUARE; // brädet fylls med tomma rutor
			}
		}
	}

}
