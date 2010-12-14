package de.bht.jvr.portals;

import de.bht.jvr.core.CameraNode;
import de.bht.jvr.math.Vector3;

public class Portal {

	private float height;
	private float width;
	private Vector3 position;
	private Vector3 direction;
	private CameraNode camera;
	
	public Portal() {
		
	}
	
	public Portal(float height, float width, Vector3 direction, CameraNode cam) {
		this.height = height;
		this.width = width;
		this.direction = direction;
		this.camera = cam;
	}
	
	public void setHeight(float height) {
		this.height = height;
	}
	
	public float getHeight() {
		return this.height;
	}
	
	public void setWidth(float width) {
		this.width = width;
	}
	
	public float getWidth() {
		return this.width;
	}
	
	public void setPostion(Vector3 position) {
		this.position = position;
	}
	
	public Vector3 getPosition() {
		return this.position;
	}
	
	public void setDirection(Vector3 direction) {
		this.direction = direction;
	}
	
	public Vector3 getDirection() {
		return this.direction;
	}
	
	public void setCamera(CameraNode cam) {
		this.camera = cam;
	}
	
	public CameraNode getCamera() {
		return this.camera;
	}
}
