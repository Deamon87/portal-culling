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
		// create root node
		GroupNode root = new GroupNode();
		
		// create a cell
		Cell cell = new Cell("blueCell", 20, 20, 5, new Color(0.0f, 0.0f, 1.0f));
		root.addChildNode(cell);		
		
		// create a camera
		CameraNode cam = new CameraNode("camera", 4/3f, 60);
		cam.setTransform(Transform.translate(0, 2, 10));
		this.cams.add(cam);
		root.addChildNode(cam);
		
		// create a pipeline
		Pipeline p = new Pipeline(root);
		
		// create a mirror
		Mirror mirror = new Mirror(p, "portal1");
		root.addChildNode(mirror);

		// render the scene with pipeline
		p.switchFrameBufferObject(null);
		p.switchCamera(cam);
		p.clearBuffers(true, true, new Color(121, 188, 255));
		p.setBackFaceCulling(false);
		p.drawGeometry("AMBIENT", null);
		p.doLightLoop(true, true).drawGeometry("LIGHTING", null);
		
		// render each portal, in this case only the mirror
		PortalList.render();
		
		// create render window
		RenderWindow win = new NewtRenderWindow(p, 800, 600);
		win.setWindowTitle("Mirror Demo");
		
		// add listeners
		win.addKeyListener(this);
		win.addMouseListener(this);
		
		// create viewer
		Viewer v = new Viewer(win);
		
		// set start value for rotation
		float i = 0;
		
		// main loop
		while(v.isRunning()) {
			long start = System.currentTimeMillis();
			v.display();
			move(System.currentTimeMillis() - start, 0.005f);
				
			// change rotation of the mirror every frame
			Transform portalTrans = mirror.getTransform().mul(Transform.rotateYDeg(i));
			mirror.setTransform(portalTrans);
			
			// increment
			i++;
			i = i * 0.05f;
			
			// update portal cameras, needs no moving speed 
			// because nothing to teleport
			PortalList.update(cam, 0);
		}
	}
}
