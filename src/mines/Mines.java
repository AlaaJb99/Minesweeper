package mines;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mines {
	private int height;
	private int width;
	private CellS[][] Mines;
	private int numMins;
	private boolean showAll = false;

	private class CellS {
		private int i = 0;
		private int j = 0;
		private boolean flag = false;
		private boolean open = false;
		private boolean mine = false;
		private int neighbors;
	}

	private class OneCell {
		private int i, j;
		private List<CellS> l = new ArrayList<Mines.CellS>();

		public OneCell(int i, int j) {
			this.i = i;
			this.j = j;
		}

		public List<CellS> neighbors() {
			if (i == 0) {
				l.add(Mines[i + 1][j]);
				if (j == 0 || j < width - 1) {
					l.add(Mines[i + 1][j + 1]);
					l.add(Mines[i][j + 1]);
				}
				if ((j < width || j < width - 1) && j > 0) {
					l.add(Mines[i + 1][j - 1]);
					l.add(Mines[i][j - 1]);
				}
			} else if (i < height - 1) {
				l.add(Mines[i + 1][j]);
				l.add(Mines[i - 1][j]);
				if (j == 0 || j < width - 1) {
					l.add(Mines[i - 1][j + 1]);
					l.add(Mines[i][j + 1]);
					l.add(Mines[i + 1][j + 1]);
				}
				if ((j < width || j < width - 1) && j > 0) {
					l.add(Mines[i - 1][j - 1]);
					l.add(Mines[i][j - 1]);
					l.add(Mines[i + 1][j - 1]);
				}
			} else if (i == height - 1) {
				l.add(Mines[i - 1][j]);
				if (j == 0 || j < width - 1) {
					l.add(Mines[i - 1][j + 1]);
					l.add(Mines[i][j + 1]);
				}
				if ((j < width || j < width - 1) && j > 0) {
					l.add(Mines[i - 1][j - 1]);
					l.add(Mines[i][j - 1]);
				}
			}
			return l;
		}
	}

	public Mines(int height, int width, int numMins) {
		Random rand = new Random();
		int c = 0;
		Mines = new CellS[height][width];// initialize the matrix
		this.height = height;// initialize the height of the matrix (table of the game)
		this.width = width;// initialize the width
		this.numMins = numMins;// initialize the number of mines in the beginning
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				Mines[i][j] = new CellS();// initialize cell in the matrix
		// put the Mines in a randomly way
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				if (c != numMins) {
					Mines[rand.nextInt(height)][rand.nextInt(width)].mine = true;
					c++;
				}
		// initialize i and j of every place
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++) {
				Mines[i][j].i = i;
				Mines[i][j].j = j;
			}
		OneCell oc;
		List<CellS> l;
		c = 0;
		// get the neighbors mines number
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++) {
				oc = new OneCell(i, j);
				l = oc.neighbors();
				for (CellS s : l) {
					if (s.mine != true)
						c++;
				}
				if (c != l.size()) {
					Mines[i][j].neighbors = l.size() - c;
				}
				c = 0;
			}
	}

	public boolean addMine(int i, int j) {
		OneCell oc;
		List<CellS> l;
		if (Mines[i][j].mine == true)// check if there is mine in this cell
			return false;
		Mines[i][j].mine = true;
		numMins++;
		oc = new OneCell(i, j);
		l = oc.neighbors();
		for (CellS s : l) {// run over all the neighbors and check which one of them is not mine
			if (s.mine != true)
				s.neighbors++;
		}
		return true;
	}

	public boolean open(int i, int j) {
		OneCell oc = new OneCell(i, j);
		List<CellS> l = oc.neighbors();
		int c = 0;
		// Mines[i][j].neighbors = oc.l.size();
		if (Mines[i][j].mine != true && Mines[i][j].open != true) {// that means it's closed and it's not Mine
			Mines[i][j].open = true;// sign that we opened it
			for (CellS s : oc.l) {
				if (s.mine != true)
					c++;
			}
			if (c == oc.l.size())
				for (CellS s : oc.l) {
					open(s.i, s.j);
				}
			else
				Mines[i][j].neighbors = l.size() - c;// how much mines neighbors
			return true;
		}
		return false;
	}

	public void toggleFlag(int x, int y) {
		if (Mines[x][y].flag == true)// check if there is flag
			Mines[x][y].flag = false;
		else
			Mines[x][y].flag = true;
	}

	public boolean isDone() {
		int c = height * width - numMins, c1 = 0;
		// count the places that is not mine and opened
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++) {
				if (Mines[i][j].mine == false && Mines[i][j].open == true)
					c1++;
			}
		// c number of the places that not mines
		if (c1 == c)
			return true;
		else
			return false;
	}

	public String get(int i, int j) {
		if (Mines[i][j].open == false && showAll == false) {// if closed and showAll false
			if (Mines[i][j].flag == true)
				return "F";// if there is flag return string "F"
			else
				return ".";// there is not flag as return the string "."
		} else if (Mines[i][j].open == false) {// if closed and there is not flag
			if (Mines[i][j].mine == true && showAll == true)// if showAll true and there is mine
				return "X";
			else if (Mines[i][j].neighbors > 0 && showAll == true)// if there is not mine and showAll true and the mines
																	// around it more than 1
				return Mines[i][j].neighbors + "";
			else// if showAll true and the mines around are 0
				return " ";
		} else {// if opened
			if (Mines[i][j].mine == true)
				return "X";
			else if (Mines[i][j].neighbors > 0)
				return Mines[i][j].neighbors + "";
			else
				return " ";
		}
	}

	public void setShowAll(boolean showAll) {
		this.showAll = showAll;// give showAll the given value
	}

	public String toString() {
		String s = new String();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++)
				s += get(i, j);// get the returned string from get that is one point
			s += "\n";
		}
		return s;
	}

	public int getHeight() {// get the height of the matrix
		return height;
	}

	public int getWidth() {// get the width of the matrix
		return width;
	}

	public boolean getIsOpen(int i, int j) {// get the boolean value of the field open in the cell i j
		return Mines[i][j].open;
	}

	public boolean getIsMine(int i, int j) {// get the boolean value of the field mine in the cell i j
		return Mines[i][j].mine;
	}
}