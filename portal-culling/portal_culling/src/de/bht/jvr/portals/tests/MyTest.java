package de.bht.jvr.portals.tests;

import java.awt.Color;
import java.io.File;

import de.bht.jvr.collada14.loader.ColladaLoader;
import de.bht.jvr.core.CameraNode;
import de.bht.jvr.core.ClipPlaneNode;
import de.bht.jvr.core.Finder;
import de.bht.jvr.core.GroupNode;
import de.bht.jvr.core.PointLightNode;
import de.bht.jvr.core.SceneNode;
import de.bht.jvr.core.ShaderMaterial;
import de.bht.jvr.core.ShaderProgram;
import de.bht.jvr.core.ShapeNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.core.pipeline.Pipeline;
import de.bht.jvr.core.uniforms.UniformBool;
import de.bht.jvr.renderer.NewtRenderWindow;
import de.bht.jvr.renderer.RenderWindow;
import de.bht.jvr.renderer.Viewer;
import de.bht.jvr.tests.TestBase;

public class MyTest extends TestBase{

	public static void main(String[] args) {
		try {
			new MyTest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public MyTest() throws Exception {
		GroupNode root = new GroupNode();
		
//		Cell cell = new Cell();
//		root.addChildNode(cell);
		
		SceneNode portal1 = ColladaLoader.load(new File("meshes/plane.dae"));
		portal1.setTransform(Transform.scale(50, 100, 1)
						.mul(Transform.translate(0, 0.05f, 0)
						.mul(Transform.scale(0.1f)
								)));
		root.addChildNode(portal1);
		
		ClipPlaneNode clipPlane = new ClipPlaneNode();
		clipPlane.setTransform(Transform.translate(0, 0.05f, 0)
						.mul(Transform.rotateYDeg(180))
								);
		root.addChildNode(clipPlane);
			
		SceneNode portal2 = ColladaLoader.load(new File("meshes/plane.dae"));
		portal2.setTransform(Transform.scale(50, 100, 1)
						.mul(Transform.translate(0, 0.05f, -400)
						.mul(Transform.scale(0.1f)
								)));
		root.addChildNode(portal2);
		
		ClipPlaneNode clipPlane2 = new ClipPlaneNode();
		clipPlane2.setTransform(Transform.translate(0, 0.05f, -400)
						.mul(Transform.rotateYDeg(180))
								);
		root.addChildNode(clipPlane2);
		
		SceneNode scene = ColladaLoader.load(new File("meshes/testwelt01.dae"));
		scene.setTransform(Transform.rotateXDeg(-90).mul(Transform.scale(0.1f)));
		GroupNode sceneGroup1 = new GroupNode();
		sceneGroup1.addChildNode(scene);
		root.addChildNode(sceneGroup1);
		
		GroupNode sceneGroup2 = new GroupNode();
		sceneGroup2.addChildNode(scene);
		sceneGroup2.setTransform(Transform.translate(0, 0, -400));
		root.addChildNode(sceneGroup2);
		
        PointLightNode pLight = new PointLightNode();
        pLight.setColor(new Color(1.0f, 1.0f, 1.0f));
        pLight.setTransform(Transform.translate(0, 400, 200));
        root.addChildNode(pLight);
        
        CameraNode cam = new CameraNode("cam", 4/3, 60f);
        cam.setTransform(Transform.translate(0, 0.5f, 10));
        this.cams.add(cam);
        root.addChildNode(cam);
        
        // Portal1 camera
        CameraNode portal1Cam = new CameraNode("portal1Cam", 4/3, 60f);
        
        // Portal2 camera
        CameraNode portal2Cam = new CameraNode("portal2Cam", 4/3, 60f);
        
        // Portal1 material
        ShaderProgram prog = new ShaderProgram(new File("shader/portal.vs"), new File("shader/portal.fs"));
        ShaderMaterial portal1Mat = new ShaderMaterial("AMBIENT", prog);
        portal1Mat.setMaterialClass("portal1Mat");
        
        ShapeNode portal1Shape = Finder.find(portal1, ShapeNode.class, null);
        portal1Shape.setMaterial(portal1Mat);
        
        // Portal2 material
        ShaderMaterial portal2Mat = new ShaderMaterial("AMBIENT", prog);
        portal2Mat.setMaterialClass("portal2Mat");
        
        ShapeNode portal2Shape = Finder.find(portal2, ShapeNode.class, null);
        portal2Shape.setMaterial(portal2Mat);
        
		Pipeline p = new Pipeline(root);
		
		p.createFrameBufferObject("FBO", false, 1, 1.0f, 0);
		
		p.createFrameBufferObject("FBO2", false, 1, 1.0f, 0);
		
		// FBO for portal1
		p.switchFrameBufferObject("FBO");
		p.switchCamera(portal1Cam);
		p.clearBuffers(true, true, new Color(121, 188, 255));
		p.setUniform("jvr_UseClipPlane0", new UniformBool(false));
		p.setUniform("jvr_UseClipPlane1", new UniformBool(true));
		p.drawGeometry("AMBIENT", null);
		p.doLightLoop(true, true).drawGeometry("LIGHTING", null);
		
		// FBO for portal2
		p.switchFrameBufferObject("FBO2");
		p.switchCamera(portal2Cam);
		p.clearBuffers(true, true, new Color(121, 188, 255));		
		p.setUniform("jvr_UseClipPlane0", new UniformBool(true));
		p.setUniform("jvr_UseClipPlane1", new UniformBool(false));
		p.drawGeometry("AMBIENT", null);
		p.doLightLoop(true, true).drawGeometry("LIGHTING", null);
		
		// actual scene
		p.switchFrameBufferObject(null);
		p.switchCamera(cam);
		p.clearBuffers(true, true, new Color(121, 188, 255));
		p.setUniform("jvr_UseClipPlane0", new UniformBool(false));
		p.setUniform("jvr_UseClipPlane1", new UniformBool(false));
		p.drawGeometry("AMBIENT", "(?!portal).*");
		p.doLightLoop(true, true).drawGeometry("LIGHTING", "(?!portal).*");
		
		p.bindColorBuffer("jvr_PortalTexture", "FBO2", 0);
		p.drawGeometry("AMBIENT", "portal2Mat");	
		
		p.bindColorBuffer("jvr_PortalTexture", "FBO", 0);
		p.drawGeometry("AMBIENT", "portal1Mat");

		
		
		RenderWindow w = new NewtRenderWindow(p, 1024, 768);
		
		w.addKeyListener(this);
		w.addMouseListener(this);
		Viewer viewer = new Viewer(w);
		try {
			while(viewer.isRunning())
			{
				long start = System.currentTimeMillis();
				
				Transform camTrans = cam.getTransform();
				camTrans = portal1.getTransform().invert().mul(camTrans);
				camTrans = portal2.getTransform().mul(Transform.rotateYDeg(180)).mul(camTrans);
				portal1Cam.setTransform(camTrans);
				
				Transform camTrans2 = cam.getTransform();
				camTrans2 = portal2.getTransform().invert().mul(camTrans2);
				camTrans2 = portal1.getTransform().mul(Transform.rotateYDeg(180).mul(camTrans2));
				portal2Cam.setTransform(camTrans2);
								
				viewer.display();
				move(System.currentTimeMillis() - start, 0.1);
				
			}
			viewer.close();
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}
}