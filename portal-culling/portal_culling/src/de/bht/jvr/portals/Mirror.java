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
import de.bht.jvr.portals.util.PortalList;

/**
 * the mirror class
 * 
 * @author Christopher Sierigk
 *
 */
public class Mirror extends Portal {

	/** the shader program for the material */
	private static ShaderProgram shaderProg;
	
	/** clipping plane for the mirror */
	private ClipPlaneNode clipPlane;
	
	/** frame around the mirror */
	private SceneNode frame;
	
	/**
	 * Initialize shader program once for all mirrors
	 */
	static {
		try {
			shaderProg = new ShaderProgram(new File("shader/simple_mirror.fs"), new File("shader/simple_mirror.vs"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Instantiate a new mirror
	 * 
	 * @param p
	 * 			the pipeline of the current scene
	 * @param name
	 * 			the name of the mirror
	 * @throws Exception
	 * 			the exception
	 */
	public Mirror(Pipeline p, String name) throws Exception {		
		this(p, name, 2, 3);
	}
	
	public Mirror(Pipeline p, String name, float height, float width) throws Exception{
		super(p, name, height, width);
		
		ShaderMaterial shaderMat = new ShaderMaterial("AMBIENT", shaderProg);
		shaderMat.setMaterialClass(this.getName() + "Mat");
		
		frame = ColladaLoader.load(new File("meshes/plane.dae"));
		ShapeNode frameShape = Finder.find(frame, ShapeNode.class, null);
		frameShape.setTransform(Transform.scale(this.getWidth() + 0.5f, this.getHeight() + 0.5f, 1).mul(Transform.translate(0, 0, -0.01f)));
		this.addChildNode(frame);
		
		this.getPortalShape().setMaterial(shaderMat);
		
		this.getCamera().setAspectRatio(-4/3f);
		
		this.clipPlane = new ClipPlaneNode();
		this.clipPlane.setTransform(this.getTransform().mul(Transform.rotateYDeg(180)));
		
		this.addChildNode(clipPlane);
		this.addChildNode(this.getPortal());
		
		PortalList.add(this);
		
		this.init();
	}
	
	/**
	 * Initialize the rendering for the mirror
	 */
	private void init() {
		this.getPipeline().createFrameBufferObject(this.getName() + "FBO", false, 1, 1, 0);
		this.getPipeline().switchFrameBufferObject(this.getName() + "FBO");
		this.getPipeline().switchCamera(this.getCamera());
		this.getPipeline().clearBuffers(true, true, new Color(121, 188, 255));
		this.getPipeline().drawGeometry("AMBIENT", null);
		this.getPipeline().doLightLoop(true, true).drawGeometry("LIGHTING", null);
	}

	/*
	 * (non-Javadoc)
	 * @see de.bht.jvr.portals.Portal#update(de.bht.jvr.core.CameraNode, double)
	 */
	@Override
	public void update(CameraNode camera, double moveSpeed) {
		Transform camTrans = camera.getTransform();
		camTrans = this.getTransform().invert().mul(camTrans);
		camTrans = this.getTransform().mul(Transform.scale(1, 1, -1).mul(camTrans));
		this.getCamera().setTransform(camTrans);
	}

	/*
	 * (non-Javadoc)
	 * @see de.bht.jvr.portals.Portal#render()
	 */
	@Override
	public void render() {
		this.getPipeline().bindColorBuffer("jvr_PortalTexture", this.getName() + "FBO", 0);
		this.getPipeline().drawGeometry("AMBIENT", this.getName() + "Mat");
	}
}
