package de.bht.jvr.portals.tests;

import java.awt.Color;
import java.io.File;

import de.bht.jvr.collada14.loader.ColladaLoader;
import de.bht.jvr.core.CameraNode;
import de.bht.jvr.core.GroupNode;
import de.bht.jvr.core.PointLightNode;
import de.bht.jvr.core.SceneNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.core.pipeline.Pipeline;
import de.bht.jvr.portals.Cell;
import de.bht.jvr.renderer.AwtRenderWindow;
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
		
		Cell cell = new Cell();
		root.addChildNode(cell);
		SceneNode portal1 = ColladaLoader.load(new File("meshes/plane.dae"));
		portal1.setTransform(Transform.scale(50.0f, 100.0f, 1.0f)
						.mul(Transform.translate(0, 0.05f, 0)
						.mul(Transform.scale(0.1f))));
		root.addChildNode(portal1);
			
		SceneNode portal2 = ColladaLoader.load(new File("meshes/plane.dae"));
		portal2.setTransform(Transform.scale(50.0f, 100.0f, 1.0f)
						.mul(Transform.translate(0, 0.05f, -400)
						.mul(Transform.scale(0.1f)
						.mul(Transform.rotateYDeg(-180)))));
		root.addChildNode(portal2);
		
		SceneNode scene = ColladaLoader.load(new File("meshes/testwelt01.dae"));
		scene.setTransform(Transform.rotateXDeg(-90).mul(Transform.scale(0.1f)));
		GroupNode sceneGroup1 = new GroupNode();
		sceneGroup1.addChildNode(scene);
		root.addChildNode(sceneGroup1);
		
		GroupNode sceneGroup2 = new GroupNode();
		sceneGroup2.addChildNode(scene);
		sceneGroup2.setTransform(Transform.translate(0, 0, -400f));
		root.addChildNode(sceneGroup2);
		
        PointLightNode pLight = new PointLightNode();
        pLight.setColor(new Color(1.0f, 1.0f, 1.0f));
        pLight.setTransform(Transform.translate(0, 400, 200));
        root.addChildNode(pLight);
        
        CameraNode cam = new CameraNode("cam", 4/3, 60f);
        cam.setTransform(Transform.translate(0, 0, 10));
        this.cams.add(cam);
        root.addChildNode(cam);
        
		Pipeline p = new Pipeline(root);
		p.switchCamera(cam);
		//OGLPrinter printer = new OGLPrinter(p);

		p.clearBuffers(true, true, new Color(121, 188, 255));
		p.drawGeometry("AMBIENT", null);
		p.doLightLoop(true, true).drawGeometry("LIGHTING", null);
		//printer.drawQuad();
		
		RenderWindow w = new AwtRenderWindow(p, 1024, 768);
		
		w.addKeyListener(this);
		w.addMouseListener(this);
		//w.setVSync(true);
		Viewer viewer = new Viewer(w);
		try {
			while(viewer.isRunning())
			{
				long start = System.currentTimeMillis();
				//Long x = new Long(System.currentTimeMillis());
				viewer.display();
				move(System.currentTimeMillis() - start, 0.1);
				
			}
			viewer.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}