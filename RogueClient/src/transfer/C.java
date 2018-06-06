package transfer;

import java.io.Serializable;

/*
 * This class is used to transfer chunk data
 * it is a simplified version of the Chunk class
*/

public class C implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int SIZE = 64;

	private int chunkX = 0;
	private int chunkY = 0;

	private O[][] objects = new O[64][64];

	public C(int chunkX, int chunkY) {
		this.chunkX = chunkX;
		this.chunkY = chunkY;
	}

	public void setObjectAt(O obj, int x, int y) {
		int relX = x - (SIZE * x);
		int relY = y - (SIZE * y);
		objects[relX][relY] = obj;
	}

	public O getObjectAt(int x, int y) {
		return objects[x][y];
	}

	public int getX() {
		return chunkX;
	}

	public int getY() {
		return chunkY;
	}

}