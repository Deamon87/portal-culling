package de.bht.jvr.portals;

import java.io.File;
import de.bht.jvr.collada14.loader.ColladaLoader;
import de.bht.jvr.core.CameraNode;
import de.bht.jvr.core.ClipPlaneNode;
import de.bht.jvr.core.GroupNode;
import de.bht.jvr.core.PickRay;
import de.bht.jvr.core.SceneNode;
import de.bht.jvr.core.ShapeNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.core.pipeline.Pipeline;
import de.bht.jvr.math.Vector3;
import de.bht.jvr.math.Vector4;

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
		this.camera = new CameraNode("portalCam", 4/3, 60);
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

	public void update(CameraNode camera) {
		Transform camTrans = camera.getTransform();
		camTrans = this.getTransform().invert().mul(camTrans);
		camTrans = portalExit.getTransform().mul(Transform.rotateYDeg(180)).mul(camTrans);
		this.camera.setTransform(camTrans);
	}
	
	public Vector3 getPickPoint(CameraNode camera) {
		// transform camera into world space
		Vector3 orig = camera.getTransform().getMatrix().translation();
		Vector3 dir = camera.getTransform().getMatrix().mul(camera.getProjectionMatrix()).translation().normalize();
		
		// transform pick ray to object space
        Vector4 localOrigin = this.getTransform().getInverseMatrix().mul(new Vector4(orig, 1));
        Vector4 localDir = this.getTransform().getInverseMatrix().mul(new Vector4(dir, 0));
        PickRay localRay = new PickRay(localOrigin.xyz(), localDir.xyz());
				
		Vector3 pickPoint = this.getPortalShape().getGeometry().pick(localRay);
		
		if(pickPoint != null) {
            pickPoint = this.getTransform().getMatrix().mul(new Vector4(pickPoint, 1)).homogenize().xyz();
            
            if(distance(orig, pickPoint) <= 0.4) {
    			this.teleport(camera);
    		}
		}
		
		return pickPoint;
	}
	
	public double distance(Vector3 vec1, Vector3 vec2) {
		double one = Math.pow(vec2.x() - vec1.x(), 2);
		double two = Math.pow(vec2.y() - vec1.y(), 2);
		double three = Math.pow(vec2.z() - vec1.z(), 2);
		double square = Math.sqrt(one + two + three);
		return square;
	}
	
	private void teleport(SceneNode node) {		
		Transform newTrans = node.getTransform();
		newTrans = this.getTransform().invert().mul(newTrans);
		newTrans = this.getPortalExit().getTransform();
		node.setTransform(newTrans.mul(Transform.translate(0, 0, 0.5f).mul(Transform.rotateYDeg(180))));
		System.out.println(this.getName() + " Exit is: " + this.getPortalExit().getName());
	}
	
	public abstract void render();
	
	public abstract void init();
}
