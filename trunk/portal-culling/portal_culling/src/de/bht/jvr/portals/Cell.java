package de.bht.jvr.portals;

import java.util.ArrayList;

import de.bht.jvr.core.GroupNode;

public class Cell extends GroupNode {

	private ArrayList<Wall> walls;
	private float length;
	private float width;
	
	public ArrayList<Wall> getWalls() {
		return walls;
	}
	
	public void setWalls(ArrayList<Wall> walls) {
		this.walls = walls;
	}
	
	public float getLength() {
		return length;
	}
	
	public void setLength(float length) {
		this.length = length;
	}
	
	public float getWidth() {
		return width;
	}
	
	public void setWidth(float width) {
		this.width = width;
	}
	
	public void drawWalls() {
		for(Wall wall : this.walls)
		{
			wall.draw();
		}
	}
	
}
