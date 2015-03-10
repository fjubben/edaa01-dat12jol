package sudoku;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;

public class SudokuWindow extends JFrame {

	private JFrame frame;
	private JPanel buttonPanel;
	private JPanel boardPanel;
	private OneDigitField[][] fields;
	private JLabel messagePromt;
	private SudokuBoard sudBoard;

	public SudokuWindow() {

		/** initierar alla komponenter **/
		frame = new JFrame("Sudoku");
		boardPanel = new JPanel();
		buttonPanel = new JPanel();
		sudBoard = new SudokuBoard();
		fields = new OneDigitField[9][9];
		messagePromt = new JLabel("message promt");
		/*****************************/

		/** inställningar för meddelanderutan **/
		messagePromt.setHorizontalAlignment(SwingConstants.CENTER);
		messagePromt.setPreferredSize(new Dimension(this.getWidth(), 30));
		messagePromt.setBackground(new Color(255, 220, 220));
		messagePromt.setForeground(new Color(255, 80, 255));
		messagePromt.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		/*****************************/

		/** inställningar för huvudfönstret **/
		frame.setMinimumSize(new Dimension(250, 300));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/**********************************/

		/** stoppar in knapparna i knappanelen */
		buttonPanel.add(new SolveButton("solve"));
		buttonPanel.add(new ClearButton("clear"));
		/************************************/

		boardPanel.setLayout(new GridLayout(9, 9)); // panelen för sudokut får
													// en fin rutnäts layout

		/** lägger in alla textfält i själva sudokurutan **/
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				fields[i][j] = new OneDigitField();

				/** rutorna som är i vissa paneler skiljs åt av en ny fin färg **/
				if (i < 3 && (j < 3 || j >= 6) || i >= 3 && i < 6 && j >= 3
						&& j < 6 || i >= 6 && (j < 3 || j >= 6)) {
					fields[i][j].setBackground(new Color(220, 220, 220));
				}
				/**************************************************************/

				/* vi vill att texten ska vara i mitten för det är snyggt */
				fields[i][j].setHorizontalAlignment(SwingConstants.CENTER);

				boardPanel.add(fields[i][j]);// slutligen lägger vi in dem i
												// panelen

			}

		}
		/***************************************************/

		/** Alla komponenter slängs in i huvudfönstret **/
		frame.add(boardPanel, BorderLayout.CENTER);
		frame.add(buttonPanel, BorderLayout.SOUTH);
		frame.add(messagePromt, BorderLayout.NORTH);
		/**********************************************/

		frame.setVisible(true);// Gör huvudfönstret synligt

	}

	/**
	 * SolveButton, en knapp som lyssnar på sig själv och försöker lösa
	 * sudokut när man trycker på den.
	 * 
	 * @param name
	 *            namnet som står inne i knappen
	 */
	@SuppressWarnings("serial")
	private class SolveButton extends JButton implements ActionListener {

		private SolveButton(String name) {
			super(name);// JButton får fixa namnet
			this.setCursor(Cursor.getPredefinedCursor(12));// så man vet att det
															// är en knapp
			addActionListener(this);

		}

		public void actionPerformed(ActionEvent e) {

			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {

					try {// försöker lägga in texten från textfältet som ett int
							// tal i sudokubrädet
						sudBoard.setSquare(i, j,
								Integer.parseInt(fields[i][j].getText()));
					} catch (NumberFormatException e1) {// ifall det inte står
														// ett int tal i rutan
														// så fixar vi det genom
														// att skicka in en
														// nolla
						sudBoard.setSquare(j, i, 0);
					}

				}
			}

			if (sudBoard.solve()) {// vi testar om vår bräda går att lösa
				for (int i = 0; i < 9; i++) {// ifall det går så skickar vi ut
												// lösningen
					for (int j = 0; j < 9; j++) {
						fields[i][j].setText(String.valueOf(sudBoard.getSquare(
								i, j)));
					}
				}
				messagePromt.setText("sudokut är nu löst");// användaren
															// meddelas att den
															// är löst
			} else {// annars går sudokut inte att lösas och då meddelas
					// användaren
				messagePromt.setText("sudokut kan ej lösas");
			}

		}

	}

	/**
	 * ClearButton är en knapp som nollställer sudokut 
	 * @param name
	 *            namnet som står inne i knappen
	 */
	@SuppressWarnings("serial")
	private class ClearButton extends JButton implements ActionListener {

		public ClearButton(String name) {
			super(name);
			this.setCursor(Cursor.getPredefinedCursor(12));
			addActionListener(this);

		}

		public void actionPerformed(ActionEvent e) {

			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					sudBoard.setSquare(i, j, 0);
					fields[i][j].setText("0");
				}
			}
			messagePromt.setText("sudokut är nu nollställt");

		}

	}

	/**
	 * Skapar ett fält där man bara kan lägga in siffror och max ett tecken.
	 * 
	 * source: http://fileadmin.cs.lth.se/cs/Education/EDAA01/inl/sudoku/
	 * OneLetterField.java
	 * 
	 */
	@SuppressWarnings("serial")
	private class OneDigitField extends JTextField {

		/**
		 * Creates a text field to display only one character.
		 */
		public OneDigitField() {
			super("0");
			((AbstractDocument) this.getDocument())
					.setDocumentFilter(new OneDigitFilter());
		}

		private class OneDigitFilter extends DocumentFilter {
			OneDigitFilter() {
				super();

			}

			public void insertString(FilterBypass fb, int offset, String str,
					AttributeSet attr) throws BadLocationException {
				if ((fb.getDocument().getLength() + str.length()) > 1) {
					return;
				}
				if (!str.isEmpty() && !Character.isDigit(str.charAt(0))) {
					return;
				}
				fb.insertString(offset, str, attr);
			}

			public void replace(FilterBypass fb, int offset, int length,
					String str, AttributeSet attr) throws BadLocationException {
				if ((fb.getDocument().getLength() + str.length() - length) > 1) {
					return;
				}
				if (!str.isEmpty() && !Character.isDigit(str.charAt(0))) {
					messagePromt.setText("ogiltligt tecken");// ifall man
																// försöker
																// lägga in ett
																// ogiltligt
																// tecken så
																// meddelas
																// användaren
					return;
				}
				fb.replace(offset, length, str, attr);
			}
		}

	}

	/** Öppnar ett sudokufönster. */
	public static void main(String[] args) {
		new SudokuWindow();
	}
}
