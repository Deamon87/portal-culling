package de.bht.jvr.portals;

import java.awt.Color;
import java.io.File;

import de.bht.jvr.core.CameraNode;
import de.bht.jvr.core.ClipPlaneNode;
import de.bht.jvr.core.Finder;
import de.bht.jvr.core.SceneNode;
import de.bht.jvr.core.ShaderMaterial;
import de.bht.jvr.core.ShaderProgram;
import de.bht.jvr.core.ShapeNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.core.pipeline.Pipeline;
import de.bht.jvr.core.uniforms.UniformBool;
import de.bht.jvr.math.Vector3;
import de.bht.jvr.portals.util.PortalList;
import de.bht.jvr.portals.util.TeleporterList;

public class Teleporter extends Portal {

	private static ShaderProgram shaderProg;
	private Portal portalExit;
	private ClipPlaneNode clipPlane;
	
	public void setPortalExit(Portal portalExit) {
		this.portalExit = portalExit;
	}
	
	public Portal getPortalExit() {
		return portalExit;
	}
	
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
		
		this.clipPlane = new ClipPlaneNode();
		this.clipPlane.setTransform(this.getTransform().mul(Transform.rotateYDeg(180)));
		
		this.addChildNode(clipPlane);
		this.addChildNode(this.getPortal());
		
		PortalList.add(this);
		TeleporterList.add(this);
		
		this.init();
	}

	public void init() {
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
	
	public void update(CameraNode node, double moveSpeed) {
		Transform camTrans = node.getTransform();
		camTrans = this.getTransform().invert().mul(camTrans);
		camTrans = this.getPortalExit().getTransform().mul(Transform.rotateYDeg(180)).mul(camTrans);
		this.getCamera().setTransform(camTrans);
		this.getPickPoint(node, moveSpeed);
	}
	
	public void getPickPoint(SceneNode node, double moveSpeed) {
		Transform trans = this.getTransform().invert().mul(node.getTransform());
		Vector3 vec = trans.getMatrix().translation();
		
		if(vec.x() <= this.getBBox().getMax().x() && vec.x() >= this.getBBox().getMin().x()
		&& vec.y() <= this.getBBox().getMax().y() && vec.y() >= this.getBBox().getMin().y()
		&& vec.z() < this.getBBox().getMax().z() + moveSpeed && vec.z() > this.getBBox().getMin().z())
		{
			this.teleport(node, moveSpeed);
			System.out.println("port");
		}		
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

	@Override
	public void render() {
		getPipeline().bindColorBuffer("jvr_PortalTexture", this.getName() + "FBO", 0);
		getPipeline().drawGeometry("AMBIENT", this.getName() + "Mat");
	}
	
//	private double distance(Vector3 vec1, Vector3 vec2) {
//		double one = Math.pow(vec2.x() - vec1.x(), 2);
//		double two = Math.pow(vec2.y() - vec1.y(), 2);
//		double three = Math.pow(vec2.z() - vec1.z(), 2);
//		double square = Math.sqrt(one + two + three);
//		return square;
//	}
}