package de.bht.jvr.portals;

import java.awt.Color;
import java.io.File;

import de.bht.jvr.collada14.loader.ColladaLoader;
import de.bht.jvr.core.CameraNode;
import de.bht.jvr.core.ClipPlaneNode;
import de.bht.jvr.core.Finder;
import de.bht.jvr.core.SceneNode;
import de.bht.jvr.core.ShaderMaterial;
import de.bht.jvr.core.ShaderProgram;
import de.bht.jvr.core.ShapeNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.core.pipeline.Pipeline;
import de.bht.jvr.math.Vector3;
import de.bht.jvr.portals.util.PortalList;
import de.bht.jvr.portals.util.TeleporterList;

/**
 * the teleporter class
 * 
 * @author Christopher Sierigk
 *
 */
public class Teleporter extends Portal {

	/** the shader program for the material */
	private static ShaderProgram shaderProg;
	
	/** the exit of the teleporter */
	private Portal portalExit;
	
	/** clipping plane for the teleporter */
	private ClipPlaneNode clipPlane;
	
	/** frame around the teleporter */
	private SceneNode frame;
	
	/**
	 * Sets the exit of the teleporter
	 * 
	 * @param portalExit
	 * 				the portal exit
	 */
	public void setPortalExit(Portal portalExit) {
		this.portalExit = portalExit;
	}
	
	/**
	 * Gets the portal exit
	 * 
	 * @return
	 */
	public Portal getPortalExit() {
		return portalExit;
	}
	
	/**
	 * Initialize shader program once for all teleporters
	 */
	static {
		try {
			shaderProg = new ShaderProgram(new File("shader/portal.fs"), new File("shader/portal.vs"));			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Instantiate a new Teleporter
	 * 
	 * @param p
	 * 			the pipeline of the current scene
	 * @param name
	 * 			the name of the teleporter
	 * @throws Exception
	 * 			the exception
	 */
	public Teleporter(Pipeline p, String name) throws Exception {
		this(p, name, 3, 2);
	}
	
	/**
	 * Instantiates a new Teleporter
	 * 
	 * @param p
	 * 			the pipeline of the current scene
	 * @param name
	 * 			the name of the teleporter
	 * @param height
	 * 			the height of the teleporter
	 * @param width
	 * 			the width of the teleporter
	 * @throws Exception
	 * 			the exception
	 */
	public Teleporter(Pipeline p, String name, float height, float width) throws Exception {
		super(p, name, height, width);
		
		ShaderMaterial shaderMat = new ShaderMaterial("AMBIENT", shaderProg);
		shaderMat.setMaterialClass(this.getName() + "Mat");
		
		frame = ColladaLoader.load(new File("meshes/plane.dae"));
		ShapeNode frameShape = Finder.find(frame, ShapeNode.class, null);
		frameShape.setTransform(Transform.scale(this.getWidth() + 0.5f, this.getHeight() + 0.5f, 1f).mul(Transform.translate(0, 0, -0.01f)));
		this.addChildNode(frame);

		this.getPortalShape().setMaterial(shaderMat);
		
		this.clipPlane = new ClipPlaneNode();
		this.clipPlane.setTransform(this.getTransform().mul(Transform.rotateYDeg(180)));
		
		this.addChildNode(clipPlane);
		this.addChildNode(this.getPortal());
		
		PortalList.add(this);
		TeleporterList.add(this);
		
		this.init();
	}
	
	/**
	 * Initialize the rendering for the teleporter
	 */
	private void init() {
		//getPipeline().setUniform("jvr_UseClipPlane0", new UniformBool(false));
		//getPipeline().setUniform("jvr_UseClipPlane1", new UniformBool(false));
				
		getPipeline().createFrameBufferObject(this.getName() + "FBO", false, 1, 1, 0);
		getPipeline().switchFrameBufferObject(this.getName() + "FBO");
		getPipeline().switchCamera(this.getCamera());
		getPipeline().clearBuffers(true, true, new Color(121, 188, 255));
		//getPipeline().setUniform("jvr_UseClipPlane0", new UniformBool(false));
		//getPipeline().setUniform("jvr_UseClipPlane1", new UniformBool(true));
		getPipeline().drawGeometry("AMBIENT", null);
		getPipeline().doLightLoop(true, true).drawGeometry("LIGHTING", null);
	}
	
	/**
	 * Teleports the scene node
	 * 
	 * @param node
	 * 			the scene node
	 * @param moveSpeed
	 * 			the moving speed of the scene node
	 */
	private void teleport(SceneNode node, double moveSpeed) {
		Transform newTrans = node.getTransform();
		newTrans = this.getTransform().invert().mul(newTrans);
		
		Transform rotTrans = newTrans.extractRotation();
		Transform transTrans = newTrans.extractTranslation();
		
		newTrans = this.getPortalExit().getTransform().mul(rotTrans);
		newTrans = newTrans.mul(Transform.translate(0, 0, -0.02f));
		newTrans = newTrans.mul(Transform.rotateYDeg(180));
		newTrans = newTrans.mul(Transform.translate(transTrans.getMatrix().translation()));		
		
		node.setTransform(newTrans);
	}

	/**
	 * Checks the distance between scene node and the teleporter
	 * and if it gets teleported
	 * 
	 * @param node
	 * 			the scene node
	 * @param moveSpeed
	 * 			the moving speed of the scene node
	 * @return
	 * 			if node is teleported or not
	 */
	public boolean checkDist(SceneNode node, double moveSpeed) {
		Transform trans = this.getTransform().invert().mul(node.getTransform());
		Vector3 vec = trans.getMatrix().translation();
		
		if(vec.x() <= this.getBBox().getMax().x() && vec.x() >= this.getBBox().getMin().x()
		&& vec.y() <= this.getBBox().getMax().y() && vec.y() >= this.getBBox().getMin().y()
		&& vec.z() <= this.getBBox().getMax().z() && vec.z() >= this.getBBox().getMin().z() - moveSpeed)
		{
			this.teleport(node, moveSpeed);
			return true;
		}
		
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see de.bht.jvr.portals.Portal#update(de.bht.jvr.core.CameraNode, double)
	 */
	@Override
	public void update(CameraNode node, double moveSpeed) {
		Transform camTrans = node.getTransform();
		camTrans = this.getTransform().invert().mul(camTrans);
		camTrans = this.getPortalExit().getTransform().mul(Transform.rotateYDeg(180)).mul(camTrans);
		this.getCamera().setTransform(camTrans);
		this.checkDist(node, moveSpeed);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.bht.jvr.portals.Portal#render()
	 */
	@Override
	public void render() {
		getPipeline().bindColorBuffer("jvr_PortalTexture", this.getName() + "FBO", 0);
		getPipeline().drawGeometry("AMBIENT", this.getName() + "Mat");
	}
}