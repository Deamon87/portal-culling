package de.bht.jvr.portals;

import java.awt.Color;
import de.bht.jvr.core.GroupNode;
import de.bht.jvr.core.LightNode;
import de.bht.jvr.core.PointLightNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.math.Vector3;

public class Cell extends GroupNode {

	private LightNode light;
	private float length;
	private float width;
	
	public Cell(String name, float length, float width, float height) {
		this.setName(name);
		this.length = length;
		this.width = width;
		PointLightNode pLight = new PointLightNode();
        pLight.setColor(new Color(1.0f, 1.0f, 1.0f));
        pLight.setShadowBias(0);
        pLight.setTransform(Transform.translate(0, 0, 0));
		Wall northWall = new Wall(height, width, new Color(1.0f, 1.0f, 1.0f));
		northWall.setTransform(Transform.translate(0, 0, -length / 2));
		Wall westWall = new Wall(height, length, new Color(1.0f, 1.0f, 1.0f));
		westWall.setTransform(Transform.translate(-width / 2, 0, 0).mul(Transform.rotateYDeg(90)));
		Wall eastWall = new Wall(height, length, new Color(1.0f, 1.0f, 1.0f));
		eastWall.setTransform(Transform.translate(width / 2, 0, 0).mul(Transform.rotateYDeg(-90)));
		Wall southWall = new Wall(height, width, new Color(1.0f, 1.0f, 1.0f));
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
	
	public boolean isInsideCell(Vector3 vec) {
		
		Vector3 trans = this.getTransform().getMatrix().translation();
		
		if(vec.x() <= this.getBBox().getMax().x() + trans.x() && vec.x() >= this.getBBox().getMin().x() + trans.x()
		&& vec.y() <= this.getBBox().getMax().y() + trans.y() && vec.y() >= this.getBBox().getMin().y() + trans.y()
		&& vec.z() <= this.getBBox().getMax().z() + trans.z() && vec.z() >= this.getBBox().getMin().z() + trans.z())
		{
			return true;
		}
		
		return false;
	}
	
}
