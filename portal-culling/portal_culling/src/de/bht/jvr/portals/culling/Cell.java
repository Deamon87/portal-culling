package de.bht.jvr.portals.culling;

import java.awt.Color;
import de.bht.jvr.core.GroupNode;
import de.bht.jvr.core.LightNode;
import de.bht.jvr.core.PointLightNode;
import de.bht.jvr.core.SceneNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.math.Vector3;
import de.bht.jvr.portals.util.CellList;

/**
 * the cell class 
 * 
 * @author Christopher Sierigk
 *
 */
public class Cell extends GroupNode {

	/** the light node */
	private LightNode light;
	
	/** the length of the cell */
	private float length;
	
	/** the width of the cell */
	private float width;
	
	/** the height of the cell */
	private float height;
	
	/** the different  walls of the cell */
	private Wall northWall, westWall, eastWall, southWall, floor, roof;
	
	/** the color of the cell */
	private Color color;
	
	/**
	 * Instantiates a new cell
	 * 
	 * @param name
	 * 			the name of the cell
	 * @param length
	 * 			the length of the cell
	 * @param width
	 * 			the width of the cell
	 * @param height
	 * 			the height of the cell
	 */
	public Cell(String name, float length, float width, float height) {
		this(name, length, width, height, new Color(1.0f, 1.0f, 1.0f));
	}
	
	/**
	 * Instantiates a new cell
	 * 
	 * @param name
	 * 			the name of the cell
	 * @param length
	 * 			the length of the cell
	 * @param width
	 * 			the width of the cell
	 * @param height
	 * 			the height of the cell
	 * @param color
	 * 			the color of the cell
	 */
	public Cell(String name, float length, float width, float height, Color color) {
		this.setName(name);
		this.length = length;
		this.width = width;
		this.height = height;
		this.color = color;
		PointLightNode pLight = new PointLightNode();
        pLight.setColor(new Color(1.0f, 1.0f, 1.0f));
        pLight.setShadowBias(0);
        pLight.setTransform(Transform.translate(0, 0, 0));
		northWall = new Wall(height, width, this.color);
		northWall.setTransform(Transform.translate(0, 0, -length / 2.0f));
		westWall = new Wall(height, length, this.color);
		westWall.setTransform(Transform.translate(-width / 2.0f, 0, 0).mul(Transform.rotateYDeg(90)));
		eastWall = new Wall(height, length, this.color);
		eastWall.setTransform(Transform.translate(width / 2.0f, 0, 0).mul(Transform.rotateYDeg(-90)));
		southWall = new Wall(height, width, this.color);
		southWall.setTransform(Transform.translate(0, 0, length / 2.0f).mul(Transform.rotateYDeg(180)));
		floor = new Wall(length, width, this.color);
		floor.setTransform(Transform.rotateXDeg(-90).mul(Transform.translate(0, 0, -height / 2.0f)));
		roof = new Wall(length, width, this.color);
		roof.setTransform(Transform.rotateXDeg(90).mul(Transform.translate(0, 0, -height / 2.0f)));
		this.addChildNodes(northWall, westWall, eastWall, southWall, floor, roof, pLight);
		
		CellList.add(this);
	}
	
	/**
	 * Gets the length
	 * 
	 * @return the length
	 */
	public float getLength() {
		return length;
	}
	
	/** Sets the length
	 * 
	 * @param length
	 * 			the length
	 */
	public void setLength(float length) {
		this.length = length;
	}
	
	/**
	 * Gets the width
	 * 
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}
	
	/**
	 * Sets the width
	 * 
	 * @param width
	 * 			the width
	 */
	public void setWidth(float width) {
		this.width = width;
	}
	
	/**
	 * Gets the height
	 * 
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}
	
	/**
	 * Sets the height
	 * 
	 * @param height
	 * 			the height
	 */
	public void setHeight(float height) {
		this.height = height;
	}
	
	/**
	 * Gets the color
	 * 
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Sets the color
	 * 
	 * @param color
	 * 			the color
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * Gets the light node
	 * 
	 * @return the light node
	 */
	public LightNode getLight() {
		return light;
	}
	
	/**
	 * Sets the light node
	 * 
	 * @param light
	 * 			the light node
	 */
	public void setLight(LightNode light) {
		this.light = light;
	}
	
	/**
	 * Gets the east Wall
	 * 
	 * @return the east wall
	 */
	public Wall getEastWall() {
		return eastWall;
	}
	
	/** Sets the east wall
	 * 
	 * @param eastWall
	 * 			the east wall
	 */
	public void setEastWall(Wall eastWall) {
		this.eastWall = eastWall;
	}
	
	/**
	 * Gets the north wall
	 * 
	 * @return the north wall
	 */
	public Wall getNorthWall() {
		return northWall;
	}
	
	/**
	 * Sets the north wall
	 * 
	 * @param northWall
	 * 			the north wall
	 */
	public void setNorthWall(Wall northWall) {
		this.northWall = northWall;
	}
	
	/**
	 * Gets the south wall
	 * 
	 * @return the south wall
	 */
	public Wall getSouthWall() {
		return southWall;
	}
	
	/**
	 * Sets the south wall
	 * 
	 * @param southWall
	 * 			the south wall
	 */
	public void setSouthWall(Wall southWall) {
		this.southWall = southWall;
	}
	
	/**
	 * Gets the west wall
	 * 
	 * @return the west wall
	 */
	public Wall getWestWall() {
		return westWall;
	}
	
	/**
	 * Sets the west wall
	 * 
	 * @param westWall
	 * 			the west wall
	 */
	public void setWestWall(Wall westWall) {
		this.westWall = westWall;
	}
	
	/**
	 * Gets the floor
	 * 
	 * @return the floor
	 */
	public Wall getFloor() {
		return floor;
	}
	
	/**
	 * Sets the floor
	 * 
	 * @param floor
	 * 			the floor 
	 */
	public void setFloor(Wall floor) {
		this.floor = floor;
	}
	
	/**
	 * Gets the roof
	 * 
	 * @return the roof
	 */
	public Wall getRoof() {
		return roof;
	}
	
	/**
	 * Sets the roof
	 * 
	 * @param roof
	 * 			the roof
	 */
	public void setRoof(Wall roof) {
		this.roof = roof;
	}
	
	/**
	 * Checks if it contains the node
	 * 
	 * @param node
	 * 			the scene node
	 * @return true, if contains node
	 */
	public boolean contains(SceneNode node) {
		
		Transform trans = this.getTransform().invert().mul(node.getTransform());
		Vector3 vec = trans.getMatrix().translation();
		
		if(vec.x() <= this.getBBox().getMax().x() && vec.x() >= this.getBBox().getMin().x()
		&& vec.y() <= this.getBBox().getMax().y() && vec.y() >= this.getBBox().getMin().y()
		&& vec.z() <= this.getBBox().getMax().z() && vec.z() >= this.getBBox().getMin().z())
		{
			return true;
		}
		
		return false;
	}
	
}
