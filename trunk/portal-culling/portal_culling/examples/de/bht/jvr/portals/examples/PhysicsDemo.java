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
		// create physics
		physics = new MyPhysics();
		
		
		
		// create root node
		GroupNode root = new GroupNode();
		
		// add a ground plane for the physical objects
		physics.addGroundPlane(0);
		// to see the plane, add a scene node
		SceneNode plane = ColladaLoader.load(new File("meshes/plane.dae"));
        plane.setTransform(Transform.scale(100).mul(Transform.rotateXDeg(-90).mul(Transform.translate(0, 0, 0))));
		root.addChildNode(plane);
		
		// create a physical sphere
		spheres = new GroupNode();
		sphere = ColladaLoader.load(new File("meshes/sphere.dae"));
		// add some material, to see the sphere
		ShaderMaterial phong = ShaderMaterial.makePhongShaderMaterial();
		// get geometry for the sphere shape
        Geometry sphereGeo = Finder.findGeometry(sphere, null);
        sphereShape = new ShapeNode("Sphere", sphereGeo, phong);
        // get a rigid body for the sphere
        sphereBody = physics.getRigidBody(sphere, new SphereShape(1));
		root.addChildNode(spheres);
		
		// add some physical boxes to the scene
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
        
        // create some light	
        PointLightNode light = new PointLightNode();
        light.setTransform(Transform.translate(2, 20, 5));
        root.addChildNode(light);
        
        // create a camera 
		cam = new CameraNode("camera", 4/3f, 60);
		cam.setTransform(Transform.translate(0, 2, 10));
		this.cams.add(cam);
		root.addChildNode(cam);
		
		// create a pipeline
		Pipeline p = new Pipeline(root);
		
		// create 2 teleporters
		Teleporter portal1 = new Teleporter(p, "portal1");
		portal1.setTransform(Transform.translate(0, 1.5f, 0).mul(Transform.rotateYDeg(180)));
		root.addChildNode(portal1);
		
		Teleporter portal2 = new Teleporter(p, "portal2");
		portal2.setTransform(Transform.translate(8, 1.5f, 0));
		root.addChildNode(portal2);
		
		// connect the 2 teleporters
		PortalConnector.connect(portal1, portal2);
		
		// render the scene with the pipeline
		p.switchFrameBufferObject(null);
		p.switchCamera(cam);
		p.clearBuffers(true, true, new Color(121, 188, 255));
		p.setBackFaceCulling(false);
		p.drawGeometry("AMBIENT", null);
		p.doLightLoop(true, true).drawGeometry("LIGHTING", null);
		
		// render the portals
		PortalList.render();
		
		// create a render window
		RenderWindow win = new NewtRenderWindow(p, 800, 600);
		win.setWindowTitle("Physic Demo");
		
		// add some listeners
		win.addKeyListener(this);
		win.addMouseListener(this);
		
		// create a viewer
		Viewer v = new Viewer(win);
		
		// main loop
		while(v.isRunning()) {
			long start = System.currentTimeMillis();
			v.display();
			this.move(System.currentTimeMillis() - start, 0.005f);
			shoot();
			
			// update physics for every frame
			physics.update(System.currentTimeMillis() - start);
			
			// moving speed for portal update
			double moveSpeed = (System.currentTimeMillis() - start) * 0.005f;
			
			// update the portals
			PortalList.update(cam, moveSpeed);
			
			// check, if physical objects needs to teleport
			if(TeleporterList.check(spheres) && sphereBody != null)
			{
				sphereBody.setWorldTransform(new com.bulletphysics.linearmath.Transform(new Matrix4f(spheres.getTransform().getMatrix().getData())));
				Vector3f velo = sphereBody.getLinearVelocity(new Vector3f());
				sphereBody.setLinearVelocity(new Vector3f(velo.x, velo.y, velo.z));
			}
		}
	}
    
	// method for shooting spheres
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
					    	// gets the direction which the camera is pointing
					    	Vector3 vel = cam.getTransform().getMatrix().rotationMatrix().mul(new Vector3(0.0f, 0.0f, -15.0f));
					    	
					    	// remove before adding, to have only one sphere per shoot
					    	spheres.removeChildNode(sphereShape);
					    	spheres.addChildNode(sphereShape);
					    	// shoot sphere from the camera
					    	spheres.setTransform(cam.getTransform());
					    	// create new rigid body for each sphere
					        sphereBody = physics.getRigidBody(spheres, new SphereShape(0.5f));
					        sphereBody.setLinearVelocity(new Vector3f(vel.x(), vel.y(), vel.z()));
					        sphereBody.setMassProps(1.0f, new Vector3f(1.0f, 1.0f, 1.0f));
					        // add rigid body to the physical world
					        physics.getDynamicsWorld().addRigidBody(sphereBody);
					        // removes the key from key list, for shooting one sphere per tap
					        pressedKeys.remove(key);
					        break;
                        }
                    }
				}
            }
        }
    }
}
