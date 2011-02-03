package de.bht.jvr.portals;

import java.util.ArrayList;

import de.bht.jvr.math.Vector3;

public class CellList {

	private static ArrayList<Cell> cells = new ArrayList<Cell>();
	
	public static String checkInside(Vector3 vec3) {
		String name = "";
		
		for(Cell cell : cells) {
			if(cell.isInsideCell(vec3))
			{
				name = cell.getName();
			}
		}
		
		return name;
	}
	
	public static void add(Cell cell) {
		cells.add(cell);
	}
	
	public static void remove(Cell cell) {
		cells.remove(cell);
	}
}
