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
	private Portal portalExit;
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

	public void update(CameraNode camera, double moveSpeed) {
		Transform camTrans = camera.getTransform();
		camTrans = this.getTransform().invert().mul(camTrans);
		camTrans = portalExit.getTransform().mul(Transform.rotateYDeg(180)).mul(camTrans);
		this.camera.setTransform(camTrans);
		this.getPickPoint(camera, moveSpeed);	
	}
	
	public void getPickPoint(CameraNode camera, double moveSpeed) {
		Transform trans = this.getTransform().invert().mul(camera.getTransform());
		Vector3 vec = trans.getMatrix().translation();
		
		if(vec.x() <= this.getBBox().getMax().x() && vec.x() >= this.getBBox().getMin().x()
		&& vec.y() <= this.getBBox().getMax().y() && vec.y() >= this.getBBox().getMin().y()
		&& vec.z() < this.getBBox().getMax().z() + moveSpeed && vec.z() > this.getBBox().getMin().z())
		{
			this.teleport(camera, moveSpeed);
			System.out.println("port");
		}		
	}
	
	public double distance(Vector3 vec1, Vector3 vec2) {
		double one = Math.pow(vec2.x() - vec1.x(), 2);
		double two = Math.pow(vec2.y() - vec1.y(), 2);
		double three = Math.pow(vec2.z() - vec1.z(), 2);
		double square = Math.sqrt(one + two + three);
		return square;
	}
	
	private void teleport(SceneNode node, double moveSpeed) {
		Transform newTrans = node.getTransform();
		newTrans = this.getTransform().invert().mul(newTrans);
		
		Transform rotTrans = newTrans.extractRotation();
		Transform transTrans = newTrans.extractTranslation();
		
		newTrans = this.getPortalExit().getTransform().mul(rotTrans);
		newTrans = newTrans.mul(Transform.translate(0, 0, (float)moveSpeed + 0.02f));
		newTrans = newTrans.mul(Transform.rotateYDeg(180));
		newTrans = newTrans.mul(Transform.translate(transTrans.getMatrix().translation()));
		node.setTransform(newTrans);
	}
	
	public abstract void render();
}