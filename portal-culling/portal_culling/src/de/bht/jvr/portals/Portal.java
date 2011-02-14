package de.bht.jvr.portals;

import java.io.File;
import de.bht.jvr.collada14.loader.ColladaLoader;
import de.bht.jvr.core.CameraNode;
import de.bht.jvr.core.ClipPlaneNode;
import de.bht.jvr.core.GroupNode;
import de.bht.jvr.core.SceneNode;
import de.bht.jvr.core.ShapeNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.core.pipeline.Pipeline;
import de.bht.jvr.math.Vector3;

public abstract class Portal extends GroupNode {

	private float height;
	private float width;
	private CameraNode camera;
	private ClipPlaneNode clipPlane;
	private SceneNode portal;
	private Pipeline pipeline;
	private ShapeNode portalShape;
	
	public Portal(Pipeline pipeline, String name) throws Exception {
		this.setName(name);
		this.pipeline = pipeline;
		this.portal = ColladaLoader.load(new File("meshes/plane.dae"));
		this.camera = new CameraNode("portalCam", 4/3f, 60);
		this.clipPlane = new ClipPlaneNode();
		this.clipPlane.setTransform(this.getTransform().mul(Transform.rotateYDeg(180)));
		this.addChildNodes(this.portal, this.clipPlane);
	}
	
	public Portal(float height, float width, Vector3 direction, CameraNode cam) throws Exception {
		this.height = height;
		this.width = width;
		this.camera = cam;
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
	
	public void setCamera(CameraNode cam) {
		this.camera = cam;
	}
	
	public CameraNode getCamera() {
		return this.camera;
	}
	
	public ClipPlaneNode getClipPlane() {
		return clipPlane;
	}
	
	public void setClipPlane(ClipPlaneNode clipPlane) {
		this.clipPlane = clipPlane;
	}
	
	public SceneNode getPortal() {
		return portal;
	}
	
	public void setPortal(SceneNode portal) {
		this.portal = portal;
	}
	
	public Pipeline getPipeline() {
		return pipeline;
	}
	
	public void setPipeline(Pipeline pipeline) {
		this.pipeline = pipeline;
	}
	
	public ShapeNode getPortalShape() {
		return portalShape;
	}
	
	public void setPortalShape(ShapeNode portalShape) {
		this.portalShape = portalShape;
	}

	public abstract void update(CameraNode camera, double moveSpeed);
	
	public abstract void render();
}