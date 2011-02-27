package de.bht.jvr.portals.examples;

import java.awt.Color;

import de.bht.jvr.core.CameraNode;
import de.bht.jvr.core.GroupNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.core.pipeline.Pipeline;
import de.bht.jvr.portals.Door;
import de.bht.jvr.portals.culling.Cell;
import de.bht.jvr.portals.util.PortalTestBase;
import de.bht.jvr.renderer.NewtRenderWindow;
import de.bht.jvr.renderer.RenderWindow;
import de.bht.jvr.renderer.Viewer;

public class DoorDemo extends PortalTestBase{

	public static void main(String[] args) {
		try {
			new DoorDemo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public DoorDemo() throws Exception {
		// create root node
		GroupNode root = new GroupNode();
		
		// create a cell
		Cell cell = new Cell("redCell", 20, 20, 7, new Color(1.0f, 0.0f, 0.0f));
		root.addChildNode(cell);
		
		// create a door
		Door door = new Door("door", 5, 5);
		
		// add door to the east wall
		cell.getEastWall().addDoor(door);
				
		Door door2 = new Door("door2", 4, 3);
		
		door.getCell().getEastWall().addDoor(door2);
		
		// add cam
		CameraNode cam = new CameraNode("camera", 4/3f, 60);
		cam.setTransform(Transform.translate(0, 2, 10));
		this.cams.add(cam);
		root.addChildNode(cam);
		
		// create pipeline
		Pipeline p = new Pipeline(root);
		
		// render the scene with the pipeline
		p.switchFrameBufferObject(null);
		p.switchCamera(cam);
		p.clearBuffers(true, true, new Color(121, 188, 255));
		p.setBackFaceCulling(false);
		p.drawGeometry("AMBIENT", null);
		p.doLightLoop(true, true).drawGeometry("LIGHTING", null);
			
		// create render window
		RenderWindow win = new NewtRenderWindow(p, 800, 600);
		win.setWindowTitle("Door Demo");
		
		// add listeners
		win.addKeyListener(this);
		win.addMouseListener(this);
		
		// create viewer
		Viewer v = new Viewer(win);
		
		double delta = 0;
		
		// main loop
		while(v.isRunning()) {
			long start = System.currentTimeMillis();
			v.display();
			delta = System.currentTimeMillis() - start;
			move(delta, 0.005f);
		}
	}
}
