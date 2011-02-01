package de.bht.jvr.portals;

import java.awt.Color;
import java.io.File;

import de.bht.jvr.core.Finder;
import de.bht.jvr.core.ShaderMaterial;
import de.bht.jvr.core.ShaderProgram;
import de.bht.jvr.core.ShapeNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.core.pipeline.Pipeline;
import de.bht.jvr.core.uniforms.UniformBool;

public class Teleporter extends Portal {

	private static ShaderProgram shaderProg;
	
	static {
		try {
			shaderProg = new ShaderProgram(new File("shader/portal.fs"), new File("shader/portal.vs"));			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Teleporter(Pipeline p, String name) throws Exception {
		super(p, name);
		
		ShaderMaterial shaderMat = new ShaderMaterial("AMBIENT", shaderProg);
		shaderMat.setMaterialClass(this.getName() + "Mat");
		
		
		ShapeNode shape = Finder.find(this, ShapeNode.class, null);
		shape.setTransform(Transform.scale(2, 3, 1));
		this.setPortalShape(shape);
		this.getPortalShape().setMaterial(shaderMat);
		
		PortalList.add(this);
		
		this.render();
	}

	@Override
	public void render() {
		getPipeline().setUniform("jvr_UseClipPlane0", new UniformBool(false));
		getPipeline().setUniform("jvr_UseClipPlane1", new UniformBool(false));
		
		getPipeline().createFrameBufferObject(this.getName() + "FBO", false, 1, 1, 0);
		getPipeline().switchFrameBufferObject(this.getName() + "FBO");
		getPipeline().switchCamera(this.getCamera());
		getPipeline().clearBuffers(true, true, new Color(121, 188, 255));
		//getPipeline().setUniform("jvr_UseClipPlane0", new UniformBool(false));
		//getPipeline().setUniform("jvr_UseClipPlane1", new UniformBool(true));
		getPipeline().drawGeometry("AMBIENT", null);
		getPipeline().doLightLoop(true, true).drawGeometry("LIGHTING", null);
	}
}