package de.bht.jvr.portals.culling;

import java.awt.Color;
import de.bht.jvr.core.GroupNode;
import de.bht.jvr.core.LightNode;
import de.bht.jvr.core.PointLightNode;
import de.bht.jvr.core.SceneNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.math.Vector3;

public class Cell extends GroupNode {

	private LightNode light;
	private float length;
	private float width;
	private Wall northWall, westWall, eastWall, southWall;
	
	public Cell(String name, float length, float width, float height) {
		this.setName(name);
		this.length = length;
		this.width = width;
		PointLightNode pLight = new PointLightNode();
        pLight.setColor(new Color(1.0f, 1.0f, 1.0f));
        pLight.setShadowBias(0);
        pLight.setTransform(Transform.translate(0, 0, 0));
		northWall = new Wall(height, width, new Color(1.0f, 1.0f, 1.0f));
		northWall.setTransform(Transform.translate(0, 0, -length / 2));
		westWall = new Wall(height, length, new Color(1.0f, 1.0f, 1.0f));
		westWall.setTransform(Transform.translate(-width / 2, 0, 0).mul(Transform.rotateYDeg(90)));
		eastWall = new Wall(height, length, new Color(1.0f, 1.0f, 1.0f));
		eastWall.setTransform(Transform.translate(width / 2, 0, 0).mul(Transform.rotateYDeg(-90)));
		southWall = new Wall(height, width, new Color(1.0f, 1.0f, 1.0f));
		southWall.setTransform(Transform.translate(0, 0, length / 2).mul(Transform.rotateYDeg(180)));
		Wall floor = new Wall(length, width, new Color(1.0f, 1.0f, 1.0f));
		floor.setTransform(Transform.rotateXDeg(-90).mul(Transform.translate(0, 0, -height / 2f)));
		Wall roof = new Wall(length, width, new Color(1.0f, 1.0f, 1.0f));
		roof.setTransform(Transform.rotateXDeg(90).mul(Transform.translate(0, 0, -height / 2f)));
		this.addChildNodes(northWall, westWall, eastWall, southWall, floor, roof, pLight);
		
		CellList.add(this);
	}
	
	public Cell(String name, float length, float width, float height, Color color) {
		this.setName(name);
		this.length = length;
		this.width = width;
		PointLightNode pLight = new PointLightNode();
        pLight.setColor(new Color(1.0f, 1.0f, 1.0f));
        pLight.setShadowBias(0);
        pLight.setTransform(Transform.translate(0, 0, 0));
		northWall = new Wall(height, width, color);
		northWall.setTransform(Transform.translate(0, 0, -length / 2));
		westWall = new Wall(height, length, color);
		westWall.setTransform(Transform.translate(-width / 2, 0, 0).mul(Transform.rotateYDeg(90)));
		eastWall = new Wall(height, length, color);
		eastWall.setTransform(Transform.translate(width / 2, 0, 0).mul(Transform.rotateYDeg(-90)));
		southWall = new Wall(height, width, color);
		southWall.setTransform(Transform.translate(0, 0, length / 2).mul(Transform.rotateYDeg(180)));
		Wall floor = new Wall(length, width, color);
		floor.setTransform(Transform.rotateXDeg(-90).mul(Transform.translate(0, 0, -height / 2f)));
		Wall roof = new Wall(length, width, color);
		roof.setTransform(Transform.rotateXDeg(90).mul(Transform.translate(0, 0, -height / 2f)));
		this.addChildNodes(northWall, westWall, eastWall, southWall, floor, roof, pLight);
		
		CellList.add(this);
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
	
	public Wall getEastWall() {
		return eastWall;
	}
	
	public void setEastWall(Wall eastWall) {
		this.eastWall = eastWall;
	}
	
	public Wall getNorthWall() {
		return northWall;
	}
	
	public void setNorthWall(Wall northWall) {
		this.northWall = northWall;
	}
	
	public Wall getSouthWall() {
		return southWall;
	}
	
	public void setSouthWall(Wall southWall) {
		this.southWall = southWall;
	}
	
	public Wall getWestWall() {
		return westWall;
	}
	
	public void setWestWall(Wall westWall) {
		this.westWall = westWall;
	}
	
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
