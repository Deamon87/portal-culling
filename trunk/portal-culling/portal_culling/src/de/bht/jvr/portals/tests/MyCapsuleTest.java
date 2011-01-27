package de.bht.jvr.portals.tests;

import java.awt.Color;
import java.io.File;

import de.bht.jvr.collada14.loader.ColladaLoader;
import de.bht.jvr.core.CameraNode;
import de.bht.jvr.core.Finder;
import de.bht.jvr.core.GroupNode;
import de.bht.jvr.core.PickRay;
import de.bht.jvr.core.PointLightNode;
import de.bht.jvr.core.SceneNode;
import de.bht.jvr.core.ShapeNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.core.pipeline.Pipeline;
import de.bht.jvr.math.Vector3;
import de.bht.jvr.math.Vector4;
import de.bht.jvr.portals.Door;
import de.bht.jvr.portals.Portal;
import de.bht.jvr.portals.PortalConnection;
import de.bht.jvr.portals.PortalList;
import de.bht.jvr.renderer.NewtRenderWindow;
import de.bht.jvr.renderer.RenderWindow;
import de.bht.jvr.renderer.Viewer;
import de.bht.jvr.util.TestBase;

public class MyCapsuleTest extends TestBase {
	
	private CameraNode camera;
	private Portal portal1;
	private GroupNode root;
	private SceneNode cursor;
	
	public static void main(String[] args) {
		try {
			new MyCapsuleTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public MyCapsuleTest() throws Exception {
		root = new GroupNode();
		
		cursor = ColladaLoader.load(new File("meshes/sphere.dae"));
        Finder.find(cursor, ShapeNode.class, null).setName("MyCursor");
        root.addChildNode(cursor);
		
		camera = new CameraNode("camera", 4/3, 60);
		camera.setTransform(Transform.translate(0, 0.5f, 10).mul(Transform.rotateYDeg(90)));
		this.cams.add(camera);
		root.addChildNode(camera);
		
		//camera.setTransform(camera.getTransform().mul(Transform.rotateYDeg(90)));
		
		//System.out.println(camera.getTransform().extractRotation());
		
		Pipeline p = new Pipeline(root);
		
		portal1 = new Door(p, "portal1");
		portal1.setTransform(Transform.translate(0, 2, 0));
		root.addChildNode(portal1);
		
		Portal portal2 = new Door(p, "portal2");
		portal2.setTransform(Transform.translate(-2, 2, -20).mul(Transform.rotateYDeg(90)));
		root.addChildNode(portal2);
		
		Portal portal3 = new Door(p, "portal3");
		portal3.setTransform(Transform.translate(-10, 2, 0).mul(Transform.rotateYDeg(180)));
		root.addChildNode(portal3);
		
		Portal portal4 = new Door(p, "portal4");
		portal4.setTransform(Transform.translate(0, 10, 0).mul(Transform.rotateXDeg(90)));
		root.addChildNode(portal4);
				
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
			
			if(PortalList.getPickPoint(camera) != null) {
				cursor.setTransform(Transform.translate(PortalList.getPickPoint(camera)).mul(Transform.scale(0.05f)));
				//System.out.println(this.distance(PortalList.getPickPoint(camera), camera.getTransform().getMatrix().translation()));
			}
			
			i++;
			i = i * 0.1f;
			
			v.display();
			move(System.currentTimeMillis() - start, 0.01f);
		}
	}
	
	public Vector3 getPickPoint(Portal portal) {
		
		// transform camera into world space
		Vector3 orig = camera.getTransform().getMatrix().translation();
		Vector3 dir = camera.getTransform().getMatrix().mul(camera.getProjectionMatrix()).translation().normalize();
		
		// transform pick ray to object space
        Vector4 localOrigin = portal.getTransform().getInverseMatrix().mul(new Vector4(orig, 1));
        Vector4 localDir = portal.getTransform().getInverseMatrix().mul(new Vector4(dir, 0));
        PickRay localRay = new PickRay(localOrigin.xyz(), localDir.xyz());
				
		Vector3 pickPoint = portal.getPortalShape().getGeometry().pick(localRay);
		
		if(pickPoint != null) {
            pickPoint = portal.getTransform().getMatrix().mul(new Vector4(pickPoint, 1)).homogenize().xyz();
		}
		
		return pickPoint;
	}
	
	public double distance(Vector3 vec1, Vector3 vec2) {
		double one = Math.pow(vec2.x() - vec1.x(), 2);
		double two = Math.pow(vec2.y() - vec1.y(), 2);
		double three = Math.pow(vec2.z() - vec1.z(), 2);
		double square = Math.sqrt(one + two + three);
		return square;
	}
}
