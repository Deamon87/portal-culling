package de.bht.jvr.portals;

import java.io.File;

import de.bht.jvr.collada14.loader.ColladaLoader;
import de.bht.jvr.core.CameraNode;
import de.bht.jvr.core.GroupNode;
import de.bht.jvr.core.SceneNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.math.Vector3;

public class Portal extends GroupNode {

	private float height;
	private float width;
	private Vector3 position;
	private Vector3 direction;
	private CameraNode camera;
	private Portal portalExit;
	private SceneNode portal;
	
	public Portal() throws Exception {
		this.portal = ColladaLoader.load(new File("meshes/plane.dae"));
		this.camera = new CameraNode("portalCam", 4/3, 60);
		this.addChildNodes(this.portal, this.camera);
	}
	
	public Portal(float height, float width, Vector3 direction, CameraNode cam) throws Exception {
		this.height = height;
		this.width = width;
		this.direction = direction;
		this.camera = cam;
		this.portalExit = null;
		
		this.portal = ColladaLoader.load(new File("meshes/plane.dae"));
		this.addChildNode(this.portal);
		this.addChildNode(this.camera);
		
		PortalList.add(this);
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
	
	public void setPortalExit(Portal portalExit) {
		this.portalExit = portalExit;
	}
	
	public Portal getPortalExit() {
		return portalExit;
	}
	
	public SceneNode getPortal() {
		return portal;
	}
	
	public void setPortal(SceneNode portal) {
		this.portal = portal;
	}
	
	public void update(CameraNode camera) {
		Transform camTrans = camera.getTransform();
		camTrans = this.getTransform().invert().mul(camTrans);
		camTrans = portalExit.getTransform().mul(Transform.rotateYDeg(180)).mul(camTrans);
		this.camera.setTransform(camTrans);
	}
}
