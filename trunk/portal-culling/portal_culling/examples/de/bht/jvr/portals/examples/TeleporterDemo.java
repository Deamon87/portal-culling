package de.bht.jvr.portals.examples;

import java.awt.Color;

import de.bht.jvr.core.CameraNode;
import de.bht.jvr.core.GroupNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.core.pipeline.Pipeline;
import de.bht.jvr.portals.Teleporter;
import de.bht.jvr.portals.culling.Cell;
import de.bht.jvr.portals.util.PortalConnector;
import de.bht.jvr.portals.util.PortalList;
import de.bht.jvr.portals.util.PortalTestBase;
import de.bht.jvr.renderer.NewtRenderWindow;
import de.bht.jvr.renderer.RenderWindow;
import de.bht.jvr.renderer.Viewer;

public class TeleporterDemo extends PortalTestBase{

	public static void main(String[] args) {
		try {
			new TeleporterDemo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public TeleporterDemo() throws Exception {
		GroupNode root = new GroupNode();
		
		Cell cell1 = new Cell("yellowCell", 20, 20, 5, new Color(1.0f, 1.0f, 0.0f));
		root.addChildNode(cell1);
		
		Cell cell2 = new Cell("cyanCell", 20, 20, 5, new Color(0.0f, 1.0f, 1.0f));
		cell2.setTransform(Transform.translate(40, 0, 0));
		root.addChildNode(cell2);
		
		CameraNode camera = new CameraNode("camera", 4/3f, 60);
		camera.setTransform(Transform.translate(0, 0.5f, 10));
		this.cams.add(camera);
		root.addChildNode(camera);
		
		Pipeline p = new Pipeline(root);
		
		Teleporter teleporter1 = new Teleporter(p, "portal1");
		teleporter1.setTransform(Transform.translate(0, 0, 0));
		root.addChildNode(teleporter1);
		
		Teleporter teleporter2 = new Teleporter(p, "portal2");
		teleporter2.setTransform(Transform.translate(40, 0, 0).mul(Transform.rotateYDeg(90)));
		root.addChildNode(teleporter2);
				
		PortalConnector.connect(teleporter1, teleporter2);
		
        p.switchFrameBufferObject(null);
		p.switchCamera(camera);
		p.clearBuffers(true, true, new Color(121, 188, 255));
		p.setBackFaceCulling(false);
		p.drawGeometry("AMBIENT", null);
		p.doLightLoop(true, true).drawGeometry("LIGHTING", null);
		
		PortalList.render();
		
		RenderWindow win = new NewtRenderWindow(p, 800, 600);
		win.addKeyListener(this);
		win.addMouseListener(this);
		
		Viewer v = new Viewer(false, win);
				
		while(v.isRunning()) {			
			long start = System.currentTimeMillis();
			
			v.display();
			move(System.currentTimeMillis() - start, 0.005f);
			
			double moveSpeed = (System.currentTimeMillis() - start) * 0.005f;
			
			PortalList.update(camera, moveSpeed);
		}
	}
}
