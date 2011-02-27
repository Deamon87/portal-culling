package de.bht.jvr.portals;

import java.io.File;

import de.bht.jvr.collada14.loader.ColladaLoader;
import de.bht.jvr.core.CameraNode;
import de.bht.jvr.core.Finder;
import de.bht.jvr.core.GroupNode;
import de.bht.jvr.core.SceneNode;
import de.bht.jvr.core.ShapeNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.core.pipeline.Pipeline;

/**
 * the abstract portal class
 * 
 * @author Christopher Sierigk
 *
 */
public abstract class Portal extends GroupNode {

	/** height of the portal */
	private float height;
	
	/** width of the portal */
	private float width;
	
	/** virtual camera of the portal */
	private CameraNode camera;
	
	/** scene node of the portal */
	private SceneNode portal;
	
	/** pipeline of the current scene */
	private Pipeline pipeline;
	
	/** shape node of the portal */
	private ShapeNode portalShape;
	
	/**
	 * Constructor of the portal, only for generalization
	 * 
	 * @param pipeline
	 * 			the pipeline of the current scene
	 * @param name
	 * 			the name of the portal
	 * @throws Exception
	 * 			the exception
	 */
	public Portal(Pipeline pipeline, String name) throws Exception {
		this(pipeline, name, 1.0f, 1.0f);
	}
	
	/**
	 * Constructor of the portal, only for generalization
	 * 
	 * @param pipeline
	 * 			the pipeline of the current scene
	 * @param name
	 * 			the name of the portal
	 * @param height
	 * 			the height of the portal
	 * @param width
	 * 			the width of the portal
	 * @throws Exception
	 * 			the exception
	 */
	public Portal(Pipeline pipeline, String name, float height, float width) throws Exception {
		this.setName(name);
		this.height = height;
		this.width = width;
		this.pipeline = pipeline;
		this.portal = ColladaLoader.load(new File("meshes/plane.dae"));
		this.portalShape = Finder.find(this.portal, ShapeNode.class, null);
		this.getPortalShape().setTransform(Transform.scale(width, height, 1));
		this.camera = new CameraNode("portalCam", 4/3f, 60);
		this.addChildNodes(this.portal);		
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
	 * Gets the height
	 * 
	 * @return the height
	 */
	public float getHeight() {
		return this.height;
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
	 * Gets the width
	 * 
	 * @return the width
	 */
	public float getWidth() {
		return this.width;
	}
	
	/**
	 * Sets the virtual camera
	 * 
	 * @param cam
	 * 			the virtual camera
	 */
	public void setCamera(CameraNode cam) {
		this.camera = cam;
	}
	
	/**
	 * Gets the virtual camera
	 * 
	 * @return the virtual camera
	 */
	public CameraNode getCamera() {
		return this.camera;
	}
	
	
	/**
	 * Sets the scene node
	 * 
	 * @param portal
	 * 			the scene node
	 */
	public void setPortal(SceneNode portal) {
		this.portal = portal;
	}
	
	/**
	 * Gets the scene node
	 * 
	 * @return the scene node
	 */
	public SceneNode getPortal() {
		return portal;
	}

	/**
	 * Sets the pipeline
	 * 
	 * @param pipeline
	 * 			the pipeline
	 */
	public void setPipeline(Pipeline pipeline) {
		this.pipeline = pipeline;
	}
	
	/**
	 * Gets the pipeline
	 * 
	 * @return the pipeline
	 */
	public Pipeline getPipeline() {
		return pipeline;
	}

	/**
	 * Sets the shape node
	 * 
	 * @param portalShape
	 * 			the shape node
	 */
	public void setPortalShape(ShapeNode portalShape) {
		this.portalShape = portalShape;
	}

	/**
	 * Gets the shape node
	 * 
	 * @return the shape node
	 */
	public ShapeNode getPortalShape() {
		return portalShape;
	}

	/**
	 * Updates the virtual camera transformation for every frame
	 * 
	 * @param camera
	 * 			the user camera
	 * @param moveSpeed
	 * 			the moving speed of the user camera
	 */
	public abstract void update(CameraNode camera, double moveSpeed);
	
	/**
	 * Renders the portal
	 */
	public abstract void render();
}