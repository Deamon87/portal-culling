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
import de.bht.jvr.portals.Door;
import de.bht.jvr.portals.Portal;
import de.bht.jvr.portals.PortalConnection;
import de.bht.jvr.portals.PortalList;
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
		portal1.setTransform(Transform.translate(0, 2, 0));
		root.addChildNode(portal1);
		
		Portal portal2 = new Door(p, "portal2");
		portal2.setTransform(Transform.translate(-2, 2, -20).mul(Transform.rotateYDeg(90)));
//		portal2.getCamera().setTransform(Transform.translate(0, 0, -20));
		root.addChildNode(portal2);
		
		Portal portal3 = new Door(p, "portal3");
		portal3.setTransform(Transform.translate(-10, 2, 0).mul(Transform.rotateYDeg(180)));
		root.addChildNode(portal3);
		
		Portal portal4 = new Door(p, "portal4");
		portal4.setTransform(Transform.translate(0, 10, 0).mul(Transform.rotateXDeg(90)));
		root.addChildNode(portal4);
		
		System.out.println(portal1.getPortalShape().getTransform());
		
		PortalConnection.connect(portal1, portal2);
		
		PortalConnection.connect(portal3, portal4);
		
		SceneNode scene = ColladaLoader.load(new File("meshes/testwelt01.dae"));
		scene.setTransform(Transform.rotateXDeg(-90).mul(Transform.scale(0.1f)));
		GroupNode sceneGroup1 = new GroupNode();
		sceneGroup1.addChildNode(scene);
		root.addChildNode(sceneGroup1);
		
		PointLightNode pLight = new PointLightNode();
        pLight.setColor(new Color(1.0f, 1.0f, 1.0f));
        pLight.setTransform(Transform.translate(0, 400, 200));
        root.addChildNode(pLight);
		
        p.switchFrameBufferObject(null);
		p.switchCamera(camera);
		p.clearBuffers(true, true, new Color(121, 188, 255));
		//p.setBackFaceCulling(false);
		p.drawGeometry("AMBIENT", "(?!portal).*");
		p.doLightLoop(true, true).drawGeometry("LIGHTING", "(?!portal).*");
		
		PortalList.render(p);
		
		RenderWindow win = new NewtRenderWindow(p);
		win.addKeyListener(this);
		win.addMouseListener(this);
		
		Viewer v = new Viewer(false, win);
		
		float i = 0;
		
		while(v.isRunning()) {
			long start = System.currentTimeMillis();
			
			PortalList.update(camera);
			
			Transform portalTrans = portal3.getTransform().mul(Transform.rotateYDeg(i));
			portal3.setTransform(portalTrans);
			
			System.out.println(camera.getTransform().invert().mul(portal1.getTransform()).getMatrix().translation());
			
			i++;
			i = i * 0.1f;
			
			v.display();
			move(System.currentTimeMillis() - start, 0.01f);
		}
	}
}
