package de.bht.jvr.portals.tests;

import java.awt.Color;

import de.bht.jvr.core.CameraNode;
import de.bht.jvr.core.GroupNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.core.pipeline.Pipeline;
import de.bht.jvr.portals.Door;
import de.bht.jvr.portals.Portal;
import de.bht.jvr.renderer.NewtRenderWindow;
import de.bht.jvr.renderer.RenderWindow;
import de.bht.jvr.renderer.Viewer;
import de.bht.jvr.tests.TestBase;

public class MyCapsuleTest extends TestBase {

	public static void main(String[] args) {
		try {
			new MyCapsuleTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public MyCapsuleTest() throws Exception {
		GroupNode root = new GroupNode();
		
		CameraNode camera = new CameraNode("camera", 4/3, 60);
		camera.setTransform(Transform.translate(0, 0.5f, 10));
		this.cams.add(camera);
		root.addChildNode(camera);
		
		Pipeline p = new Pipeline(root);
		
		Portal portal1 = new Door(p, "portal1");
		root.addChildNode(portal1);
		
		Portal portal2 = new Door(p, "portal2");
		portal2.setTransform(Transform.translate(0, 0, -20));
		root.addChildNode(portal2);
		
		p.switchCamera(camera);
		p.clearBuffers(true, true, new Color(121, 188, 255));
		p.setBackFaceCulling(false);
		p.drawGeometry("AMBIENT", null);
		p.doLightLoop(true, true).drawGeometry("LIGHTING", null);
		
		RenderWindow win = new NewtRenderWindow(p);
		win.addKeyListener(this);
		win.addMouseListener(this);
		
		Viewer v = new Viewer(false, win);
		
		float i = 0;
		
		while(v.isRunning()) {
			long start = System.currentTimeMillis();
			
			Transform portalTrans = portal1.getTransform();
			portalTrans = portal1.getTransform().invert();
			portalTrans = portal1.getTransform().mul(Transform.rotateXDeg(i));
			portal1.setTransform(portalTrans);
			
			v.display();
			move(System.currentTimeMillis() - start, 0.01f);
			i++;
			i = i * 0.1f; 
		}
	}
}
