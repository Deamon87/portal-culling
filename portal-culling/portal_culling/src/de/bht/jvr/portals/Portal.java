package de.bht.jvr.portals;

import java.io.File;

import de.bht.jvr.collada14.loader.ColladaLoader;
import de.bht.jvr.core.CameraNode;
import de.bht.jvr.core.ClipPlaneNode;
import de.bht.jvr.core.GroupNode;
import de.bht.jvr.core.SceneNode;
import de.bht.jvr.core.ShaderMaterial;
import de.bht.jvr.core.Transform;
import de.bht.jvr.core.pipeline.Pipeline;
import de.bht.jvr.math.Vector3;

public abstract class Portal extends GroupNode {

	private String portalName;
	private float height;
	private float width;
	private CameraNode camera;
	private ClipPlaneNode clipPlane;
	private Portal portalExit;
	private SceneNode portal;
	private Pipeline pipeline;
	private ShaderMaterial material;
	
	
	public Portal(Pipeline pipeline, String name) throws Exception {
		this.portalName = name;
		this.pipeline = pipeline;
		this.portal = ColladaLoader.load(new File("meshes/plane.dae"));
		this.camera = new CameraNode("portalCam", 4/3, 60);
		this.clipPlane = new ClipPlaneNode();
		this.clipPlane.setTransform(this.getTransform().mul(Transform.rotateYDeg(180)));
		this.addChildNodes(this.portal, this.camera, this.clipPlane);
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
	
	public String getPortalName() {
		return portalName;
	}
	
	public void setPortalName(String portalName) {
		this.portalName = portalName;
	}
	
	public ShaderMaterial getMaterial() {
		return material;
	}
	
	public void setMaterial(ShaderMaterial material) {
		this.material = material;
	}

	public void update(CameraNode camera) {
		Transform camTrans = camera.getTransform();
		camTrans = this.getTransform().invert().mul(camTrans);
		camTrans = portalExit.getTransform().mul(Transform.rotateYDeg(180)).mul(camTrans);
		this.camera.setTransform(camTrans);
	}
	
	public abstract void render();
	
	public abstract void init();
}
