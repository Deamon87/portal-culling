package de.bht.jvr.portals.examples;

import java.awt.Color;
import java.io.File;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.RigidBody;

import de.bht.jvr.collada14.loader.ColladaLoader;
import de.bht.jvr.core.CameraNode;
import de.bht.jvr.core.Finder;
import de.bht.jvr.core.Geometry;
import de.bht.jvr.core.GroupNode;
import de.bht.jvr.core.PointLightNode;
import de.bht.jvr.core.SceneNode;
import de.bht.jvr.core.ShaderMaterial;
import de.bht.jvr.core.ShapeNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.core.pipeline.Pipeline;
import de.bht.jvr.math.Vector3;
import de.bht.jvr.portals.Teleporter;
import de.bht.jvr.portals.tests.physics.MyPhysics;
import de.bht.jvr.portals.util.PortalConnector;
import de.bht.jvr.portals.util.PortalList;
import de.bht.jvr.portals.util.PortalTestBase;
import de.bht.jvr.portals.util.TeleporterList;
import de.bht.jvr.renderer.NewtRenderWindow;
import de.bht.jvr.renderer.RenderWindow;
import de.bht.jvr.renderer.Viewer;

public class PhysicsDemo extends PortalTestBase{

	private CameraNode cam;
	private GroupNode spheres;
	private SceneNode sphere;
	private ShapeNode sphereShape;
	private RigidBody sphereBody;
	private MyPhysics physics;
	
	public static void main(String[] args) {
		try {
			new PhysicsDemo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public PhysicsDemo() throws Exception {
		
		physics = new MyPhysics();
		
		physics.addGroundPlane(0);
		
		GroupNode root = new GroupNode();
		
		spheres = new GroupNode();
		sphere = ColladaLoader.load(new File("meshes/sphere.dae"));
		ShaderMaterial phong = ShaderMaterial.makePhongShaderMaterial();
        Geometry sphereGeo = Finder.findGeometry(sphere, null);
        sphereShape = new ShapeNode("Sphere", sphereGeo, phong);
        sphereBody = physics.getRigidBody(sphere, new SphereShape(1));
		root.addChildNode(spheres);
		
		GroupNode boxes = new GroupNode();
        SceneNode box = ColladaLoader.load(new File("meshes/box.dae"));
        for(int x=-2; x<3; x++)
        {
            for(int y=2; y<6; y++)
            {
                GroupNode boxRoot = new GroupNode();
                boxRoot.addChildNode(box);
                boxRoot.setTransform(Transform.translate(x * 1.2f, y * 1.2f, -5)); // set start transformation for this box
                physics.addRigidBody(boxRoot, new BoxShape(new Vector3f(0.5f, 0.5f, 0.5f)), 1); // add the box to physics
                boxes.addChildNode(boxRoot);
            }
        }
        		
        root.addChildNode(boxes);
        
        SceneNode plane = ColladaLoader.load(new File("meshes/plane.dae"));
        plane.setTransform(Transform.scale(100).mul(Transform.rotateXDeg(-90).mul(Transform.translate(0, 0, 0))));
		root.addChildNode(plane);
		
        PointLightNode light = new PointLightNode();
        light.setTransform(Transform.translate(2, 20, 5));
        root.addChildNode(light);
        
		cam = new CameraNode("camera", 4/3f, 60);
		cam.setTransform(Transform.translate(0, 2, 10));
		this.cams.add(cam);
		root.addChildNode(cam);
		
		Pipeline p = new Pipeline(root);
		
		Teleporter portal1 = new Teleporter(p, "portal1");
		portal1.setTransform(Transform.translate(0, 1.5f, 0).mul(Transform.rotateYDeg(180)));
		root.addChildNode(portal1);
		
		Teleporter portal2 = new Teleporter(p, "portal2");
		portal2.setTransform(Transform.translate(8, 1.5f, 0));
		root.addChildNode(portal2);
		
		System.out.println(portal1.getBBox());
		
		PortalConnector.connect(portal1, portal2);
		
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
		
		double delta = 0;
		
		while(v.isRunning()) {
			long start = System.currentTimeMillis();
			v.display();
			delta = System.currentTimeMillis() - start;
			this.move(delta, 0.005f);
			shoot();
			physics.update(delta);
			
			double moveSpeed = (System.currentTimeMillis() - start) * 0.005f;
			
			PortalList.update(cam, moveSpeed);
			
			if(TeleporterList.check(spheres) && sphereBody != null)
			{
				sphereBody.setWorldTransform(new com.bulletphysics.linearmath.Transform(new Matrix4f(spheres.getTransform().getMatrix().getData())));
				Vector3f velo = sphereBody.getLinearVelocity(new Vector3f());
				sphereBody.setLinearVelocity(new Vector3f(velo.x, velo.y, velo.z));
			}
		}
	}
    
    private void shoot(){
    	synchronized (pressedKeys)
        {
            if (!pressedKeys.isEmpty())
            {
                for (SceneNode cam: this.cams)
                {
                    for (Character key: pressedKeys)
                    {
                        switch (key)
                        {
					    case 'E':
					    	Vector3 vel = cam.getTransform().getMatrix().rotationMatrix().mul(new Vector3(0.0f, 0.0f, -15.0f));
					    	spheres.removeChildNode(sphereShape);
					    	spheres.addChildNode(sphereShape);
					    	spheres.setTransform(cam.getTransform());
					        sphereBody = physics.getRigidBody(spheres, new SphereShape(0.5f));
					        sphereBody.setLinearVelocity(new Vector3f(vel.x(), vel.y(), vel.z()));
					        sphereBody.setMassProps(1.0f, new Vector3f(1.0f, 1.0f, 1.0f));
					        physics.getDynamicsWorld().addRigidBody(sphereBody);
					        pressedKeys.remove(key);
					        break;
                        }
                    }
				}
            }
        }
    }
}
