package de.bht.jvr.portals.tests;

import java.awt.Color;

import de.bht.jvr.core.CameraNode;
import de.bht.jvr.core.GroupNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.core.pipeline.Pipeline;
import de.bht.jvr.portals.Door;
import de.bht.jvr.portals.culling.Cell;
import de.bht.jvr.renderer.NewtRenderWindow;
import de.bht.jvr.renderer.RenderWindow;
import de.bht.jvr.renderer.Viewer;
import de.bht.jvr.util.PortalTestBase;

public class MyDoorTest extends PortalTestBase{

	public static void main(String[] args) {
		try {
			new MyDoorTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public MyDoorTest() throws Exception {		
		GroupNode root = new GroupNode();
		
		Cell cell = new Cell("redCell", 10, 10, 5, new Color(1.0f, 0.0f, 0.0f));
		root.addChildNode(cell);
		
		Door door = new Door("door", 4, 2);
		
		cell.getNorthWall().addDoor(door);
				
		CameraNode cam = new CameraNode("camera", 4/3f, 60);
		cam.setTransform(Transform.translate(0, 2, 10));
		this.cams.add(cam);
		root.addChildNode(cam);
		
		Pipeline p = new Pipeline(root);
		
		p.switchFrameBufferObject(null);
		p.switchCamera(cam);
		p.clearBuffers(true, true, new Color(121, 188, 255));
		//p.setBackFaceCulling(false);
		p.drawGeometry("AMBIENT", null);
		p.doLightLoop(true, true).drawGeometry("LIGHTING", null);
		
		//PortalList.render();
		
		RenderWindow win = new NewtRenderWindow(p, 800, 600);
		
		win.addKeyListener(this);
		win.addMouseListener(this);
		
		Viewer v = new Viewer(win);
		
		double delta = 0;
		
		while(v.isRunning()) {
			long start = System.currentTimeMillis();
			v.display();
			delta = System.currentTimeMillis() - start;
			move(delta, 0.005f);
			
			//double moveSpeed = (System.currentTimeMillis() - start) * 0.005f;
			
			//PortalList.update(cam, moveSpeed);
		}
	}
}
