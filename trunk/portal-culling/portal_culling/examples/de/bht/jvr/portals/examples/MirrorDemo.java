package de.bht.jvr.portals.examples;

import java.awt.Color;

import de.bht.jvr.core.CameraNode;
import de.bht.jvr.core.GroupNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.core.pipeline.Pipeline;
import de.bht.jvr.portals.Mirror;
import de.bht.jvr.portals.culling.Cell;
import de.bht.jvr.portals.util.PortalList;
import de.bht.jvr.portals.util.PortalTestBase;
import de.bht.jvr.renderer.NewtRenderWindow;
import de.bht.jvr.renderer.RenderWindow;
import de.bht.jvr.renderer.Viewer;

public class MirrorDemo extends PortalTestBase {

	public static void main(String[] args)
	{
		try {
			new MirrorDemo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public MirrorDemo() throws Exception {
		GroupNode root = new GroupNode();
		
		Cell cell = new Cell("blueCell", 20, 20, 5, new Color(0.0f, 0.0f, 1.0f));
		root.addChildNode(cell);		
		
		CameraNode cam = new CameraNode("camera", 4/3f, 60);
		cam.setTransform(Transform.translate(0, 2, 10));
		this.cams.add(cam);
		root.addChildNode(cam);
		
		Pipeline p = new Pipeline(root);
		
		Mirror mirror = new Mirror(p, "portal1");
		mirror.setTransform(Transform.translate(0, 0, 0));
		root.addChildNode(mirror);

		p.switchFrameBufferObject(null);
		p.switchCamera(cam);
		p.clearBuffers(true, true, new Color(121, 188, 255));
		p.setBackFaceCulling(false);
		p.drawGeometry("AMBIENT", null);
		p.doLightLoop(true, true).drawGeometry("LIGHTING", null);
		
		PortalList.render();
		
		RenderWindow win = new NewtRenderWindow(p, 800, 600);
		
		win.addKeyListener(this);
		win.addMouseListener(this);
		
		Viewer v = new Viewer(win);
		
		float i = 0;
		
		while(v.isRunning()) {
			long start = System.currentTimeMillis();
			v.display();
			move(System.currentTimeMillis() - start, 0.005f);
						
			Transform portalTrans = mirror.getTransform().mul(Transform.rotateYDeg(i));
			mirror.setTransform(portalTrans);
			
			i++;
			i = i * 0.05f;
			
			double moveSpeed = (System.currentTimeMillis() - start) * 0.005f;
			
			PortalList.update(cam, moveSpeed);
		}
	}
}
