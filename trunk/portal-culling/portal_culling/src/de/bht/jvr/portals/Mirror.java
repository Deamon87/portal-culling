package de.bht.jvr.portals;

import java.awt.Color;
import java.io.File;

import de.bht.jvr.core.CameraNode;
import de.bht.jvr.core.Finder;
import de.bht.jvr.core.ShaderMaterial;
import de.bht.jvr.core.ShaderProgram;
import de.bht.jvr.core.ShapeNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.core.pipeline.Pipeline;

public class Mirror extends Portal {

	private static ShaderProgram shaderProg;
	
	static {
		try {
			shaderProg = new ShaderProgram(new File("shader/simple_mirror.fs"), new File("shader/simple_mirror.vs"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Mirror(Pipeline p, String name) throws Exception {		
		super(p, name);
		
		ShaderMaterial shaderMat = new ShaderMaterial("AMBIENT", shaderProg);
		shaderMat.setMaterialClass(this.getName() + "Mat");
		
		this.setPortalShape(Finder.find(this, ShapeNode.class, null));
		this.getPortalShape().setMaterial(shaderMat);
		
		this.getCamera().setAspectRatio(-4/3f);
		
		PortalList.add(this);
		
		this.render();
	}
	
	@Override
	public void update(CameraNode camera, double moveSpeed) {
		Transform camTrans = camera.getTransform();
		camTrans = this.getTransform().invert().mul(camTrans);
		camTrans = this.getTransform().mul(Transform.scale(1, 1, -1).mul(camTrans));
		this.getCamera().setTransform(camTrans);
	}

	@Override
	public void render() {
		this.getPipeline().createFrameBufferObject(this.getName() + "FBO", false, 1, 1, 0);
		this.getPipeline().switchFrameBufferObject(this.getName() + "FBO");
		this.getPipeline().switchCamera(this.getCamera());
		this.getPipeline().clearBuffers(true, true, new Color(121, 188, 255));
		this.getPipeline().drawGeometry("AMBIENT", null);
		this.getPipeline().doLightLoop(true, true).drawGeometry("LIGHTING", null);
	}
}
