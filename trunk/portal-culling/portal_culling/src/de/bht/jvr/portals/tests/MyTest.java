package de.bht.jvr.portals.tests;

import java.awt.Color;

import de.bht.jvr.core.GroupNode;
import de.bht.jvr.core.pipeline.Pipeline;
import de.bht.jvr.portals.Cell;
import de.bht.jvr.renderer.AwtRenderWindow;
import de.bht.jvr.renderer.RenderWindow;
import de.bht.jvr.renderer.Viewer;
import de.bht.jvr.tests.TestBase;

public class MyTest extends TestBase{

	public static void main(String[] args) {
		new MyTest();
	}
	
	public MyTest() {
		GroupNode root = new GroupNode();
		
		Cell cell = new Cell();
		root.addChildNode(cell);
		
		Pipeline p = new Pipeline(root);
		p.clearBuffers(true, true, new Color(121, 188, 255));
		
		RenderWindow w = new AwtRenderWindow(p, 640, 480);
		
		Viewer viewer = new Viewer(w);
		
		try {
			while(viewer.isRunning())
			{
				long start = System.currentTimeMillis();
				viewer.display();
				this.move(System.currentTimeMillis() - start, 0.1);
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
