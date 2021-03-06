package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import game.Game;
import main.ClientMain;
import main.KeyInput;
import transfer.O;

public class Screen extends JTextPane {

	private static final long serialVersionUID = 1L;
	private static final int DIALOG_HEIGHT = 20;

	private int fontWidth = -1;
	private int fontHeight = 18;// 12;
	private Style style;
	private Font font = new Font("monospaced", Font.PLAIN, fontHeight);
	private char[][] view;

	public Screen() {
		setBackground(Color.BLACK);
		style = addStyle("style", null);
		StyleConstants.setLineSpacing(style, 0.0f);
		StyleConstants.setForeground(style, Color.WHITE);
		StyleConstants.setFontFamily(style, "monospaced");
		StyleConstants.setFontSize(style, fontHeight);
		setEditable(false);
		setHighlighter(null);
		setParagraphAttributes(style, true);
		setFont(font);
		addKeyListener(new KeyInput());

		addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
			}

			@Override
			public void componentResized(ComponentEvent e) {
				init();
			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});

	}

	public void drawToScreen() {
		StyledDocument doc = getStyledDocument();
		StringBuffer buffer = new StringBuffer();
		for (int j = 0; j < view[0].length; j++) {
			for (int i = 0; i < view.length; i++) {
				buffer.append(view[i][j]);
			}
			buffer.append("\n");
		}
		clearScreen();
		try {
			doc.insertString(0, buffer.toString(), style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		new Exception().getStackTrace();

	}

	private void clearScreen() {
		setText("");
	}

	private int getFontWidth() {
		if (fontWidth == -1) {
			BufferedImage bf = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
			FontMetrics fontMetrics = bf.createGraphics().getFontMetrics(font);
			fontWidth = fontMetrics.charWidth('l');
		}
		return fontWidth;
	}

	public int getFontHeight() {
		return (int) (StyleConstants.getFontSize(style) + StyleConstants.getSpaceBelow(style)
				+ StyleConstants.getSpaceAbove(style) + 5);

	}

	public int getViewWidth() {
		return (int) Math.floor(getWidth() / getFontWidth());

	}

	public int getViewHeight() {
		return (int) Math.floor(getHeight() / getFontHeight());
	}

	public void init() {
		view = new char[getViewWidth()][getViewHeight()];
		drawAll();
	}

	public void makeWorldView() {
		Game game = ClientMain.getGame();
		if (game.getPlayer() != null) {
			for (int i = 0; i < getViewWidth(); i++) {
				for (int j = 0; j < getViewHeight() - DIALOG_HEIGHT; j++) {
					int x = i + game.getPlayer().getX() - (getViewWidth() / 2);
					int y = j - game.getPlayer().getY() - (getViewHeight() / 2);
					O obj = game.getWorld().getObjectAt(x, -y);
					setCharAt(obj.getDisplay(), i, j);
				}
			}
		}
	}

	private void setCharAt(char c, int i, int j) {
		if (i < view.length && i >= 0 && j < view[i].length && j >= 0) {
			view[i][j] = c;
		}
	}

	public void drawAll() {
		makeDialogView();
		makeWorldView();
		drawToScreen();
	}

	private void makeDialogView() {
		Game game = ClientMain.getGame();
		char[][] dialogArr = game.getDialog().getDialog(getViewWidth(), DIALOG_HEIGHT);
		for (int x = 0; x < dialogArr.length; x++) {
			for (int y = 0; y < dialogArr[y].length; y++) {
				setCharAt(dialogArr[x][y], x, getViewHeight() - DIALOG_HEIGHT + y);
			}			
		}
	}

	public void updateObject(O obj) {
		Game game = ClientMain.getGame();
		int x = obj.getX() - game.getPlayer().getX() + (getViewWidth() / 2);
		int y = obj.getY() - game.getPlayer().getY() + (getViewHeight() / 2);
		setCharAt(obj.getDisplay(), x, y);
	}

}