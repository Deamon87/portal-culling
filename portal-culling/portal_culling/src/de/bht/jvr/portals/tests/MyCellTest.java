package de.bht.jvr.portals.tests;

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

public class MyCellTest extends PortalTestBase {

	public static void main(String[] args) {
		try {
			new MyCellTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public MyCellTest() throws Exception {
		GroupNode root = new GroupNode();
		
		Cell cell = new Cell("redCell", 10, 10, 5, new Color(1.0f, 0.0f, 0.0f));
		root.addChildNode(cell);		
		
		Cell cell2 = new Cell("greenCell", 10, 10, 5, new Color(0.0f, 1.0f, 0.0f));
		cell2.setTransform(Transform.translate(10, 0, 0));
		root.addChildNode(cell2);
		
		cell.removeChildNode(cell.getEastWall());
		cell2.removeChildNode(cell2.getWestWall());
		
		System.out.println(cell2.getBBox().getWidth());
		
		CameraNode cam = new CameraNode("camera", 4/3f, 60);
		cam.setTransform(Transform.translate(0, 2, 10));
		this.cams.add(cam);
		root.addChildNode(cam);
		
		Pipeline p = new Pipeline(root);
		
		Teleporter portal1 = new Teleporter(p, "portal1");
		portal1.setTransform(Transform.translate(0, 0, 0));
		root.addChildNode(portal1);
		
		Teleporter portal2 = new Teleporter(p, "portal2");
		portal2.setTransform(Transform.translate(12, 0, 0));
		root.addChildNode(portal2);
		
		System.out.println(portal1.getBBox());
		
		PortalConnector.connect(portal1, portal2);
		
		p.switchFrameBufferObject(null);
		p.switchCamera(cam);
		p.clearBuffers(true, true, new Color(121, 188, 255));
		//p.setBackFaceCulling(false);
		p.drawGeometry("AMBIENT", null);
		p.doLightLoop(true, true).drawGeometry("LIGHTING", null);
		
		PortalList.render();
		
		RenderWindow win = new NewtRenderWindow(p, 800, 600);
		
		win.addKeyListener(this);
		win.addMouseListener(this);
		
		Viewer v = new Viewer(win);
		
		while(v.isRunning()) {
			long start = System.currentTimeMillis();
			v.display();
			move(System.currentTimeMillis() - start, 0.005f);
			
			//System.out.println(CellList.checkCell(cam));
			
			double moveSpeed = (System.currentTimeMillis() - start) * 0.005f;
			
			PortalList.update(cam, moveSpeed);
		}
	}
}
