package de.bht.jvr.portals;

import java.awt.Color;
import java.util.ArrayList;

import de.bht.jvr.core.GroupNode;
import de.bht.jvr.core.LightNode;
import de.bht.jvr.core.PointLightNode;
import de.bht.jvr.core.Transform;

public class Cell extends GroupNode {

	private ArrayList<Wall> walls;
	private LightNode light;
	private float length;
	private float width;
	
	public Cell(float length, float width, float height, Color color) {
		walls = new ArrayList<Wall>();
		this.length = length;
		this.width = width;
		PointLightNode pLight = new PointLightNode();
        pLight.setColor(new Color(1.0f, 1.0f, 1.0f));
        pLight.setShadowBias(0);
        pLight.setTransform(Transform.translate(0, 0, 0));
		Wall northWall = new Wall(height, width, color);
		northWall.setTransform(Transform.translate(0, 0, -length / 2));
		Wall westWall = new Wall(height, length, color);
		westWall.setTransform(Transform.translate(-width / 2, 0, 0).mul(Transform.rotateYDeg(90)));
		Wall eastWall = new Wall(height, length, color);
		eastWall.setTransform(Transform.translate(width / 2, 0, 0).mul(Transform.rotateYDeg(-90)));
		Wall southWall = new Wall(height, width, color);
		southWall.setTransform(Transform.translate(0, 0, length / 2).mul(Transform.rotateYDeg(180)));
		Wall floor = new Wall(length, width, color);
		floor.setTransform(Transform.rotateXDeg(-90).mul(Transform.translate(0, 0, -height / 2f)));
		Wall roof = new Wall(length, width, color);
		roof.setTransform(Transform.rotateXDeg(90).mul(Transform.translate(0, 0, -height / 2f)));
		this.addChildNodes(northWall, westWall, eastWall, southWall, floor, roof, pLight);
		
		//this.drawWalls();
	}
	
	public ArrayList<Wall> getWalls() {
		return walls;
	}
	
	public void setWalls(ArrayList<Wall> walls) {
		this.walls = walls;
	}
	
	public void addWall(Wall wall) {
		this.walls.add(wall);
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
	
	public LightNode getLight() {
		return light;
	}
	
	public void setLight(LightNode light) {
		this.light = light;
	}
	
	public void drawWalls() {
		for(Wall wall : this.walls)
		{
			this.addChildNode(wall);
		}
	}
	
}
