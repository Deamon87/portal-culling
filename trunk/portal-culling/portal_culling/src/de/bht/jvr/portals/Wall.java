package de.bht.jvr.portals;

import de.bht.jvr.core.SceneNode;
import de.bht.jvr.core.Traverser;

public class Wall extends SceneNode {

	private float height;
	private float width;
	
	public float getHeight() {
		return height;
	}
	
	public void setHeight(float height) {
		this.height = height;
	}
	
	public float getWidth() {
		return width;
	}
	
	public void setWidth(float width) {
		this.width = width;
	}
	
	public void draw() {
		
	}
	
	@Override
	public boolean accept(Traverser traverser) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	protected void updateBBox() {
		// TODO Auto-generated method stub
		
	}
}
