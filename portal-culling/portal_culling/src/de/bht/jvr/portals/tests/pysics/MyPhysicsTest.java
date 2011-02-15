package de.bht.jvr.portals.tests.pysics;

import java.awt.Color;
import java.io.File;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.RigidBody;

import de.bht.jvr.collada14.loader.ColladaLoader;
import de.bht.jvr.core.CameraNode;
import de.bht.jvr.core.Finder;
import de.bht.jvr.core.Geometry;
import de.bht.jvr.core.GroupNode;
import de.bht.jvr.core.SceneNode;
import de.bht.jvr.core.ShaderMaterial;
import de.bht.jvr.core.ShapeNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.core.pipeline.Pipeline;
import de.bht.jvr.math.Vector3;
import de.bht.jvr.portals.PortalConnector;
import de.bht.jvr.portals.Teleporter;
import de.bht.jvr.portals.culling.Cell;
import de.bht.jvr.portals.util.PortalList;
import de.bht.jvr.portals.util.PortalTestBase;
import de.bht.jvr.portals.util.TeleporterList;
import de.bht.jvr.renderer.NewtRenderWindow;
import de.bht.jvr.renderer.RenderWindow;
import de.bht.jvr.renderer.Viewer;

public class MyPhysicsTest extends PortalTestBase {

	private CameraNode cam;
	private GroupNode spheres;
	private SceneNode sphere;
	private ShapeNode sphereShape;
	private RigidBody sphereBody;
	private MyPhysics physics;
	
	public static void main(String[] args) {
		try {
			new MyPhysicsTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public MyPhysicsTest() throws Exception {
		
		physics = new MyPhysics();
		
		physics.addGroundPlane(-2.5f);
		
		GroupNode root = new GroupNode();
		
		spheres = new GroupNode();
		sphere = ColladaLoader.load(new File("meshes/sphere.dae"));
		ShaderMaterial phong = ShaderMaterial.makePhongShaderMaterial();
        Geometry sphereGeo = Finder.findGeometry(sphere, null);
        sphereShape = new ShapeNode("Sphere", sphereGeo, phong);
		//sphereShape.setTransform(Transform.scale(0.12f).mul(Transform.translate(0.0f, 1.272f, 5.405f)));
		root.addChildNode(spheres);
        
		SceneNode sphere = ColladaLoader.load(new File("meshes/sphere.dae"));
		sphere.setTransform(Transform.translate(5,5,0));
		physics.addRigidBody(sphere, new SphereShape(1), 1);
		root.addChildNode(sphere);
		
		Cell cell = new Cell("redCell", 10, 10, 5, new Color(1.0f, 0.0f, 0.0f));
		root.addChildNode(cell);		
		
		Cell cell2 = new Cell("greenCell", 10, 10, 5, new Color(0.0f, 1.0f, 0.0f));
		cell2.setTransform(Transform.translate(10, 0, 0));
		root.addChildNode(cell2);
		
		cell.removeChildNode(cell.getEastWall());
		cell2.removeChildNode(cell2.getWestWall());
		
		System.out.println(cell2.getBBox().getWidth());
		
		cam = new CameraNode("camera", 4/3f, 60);
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
		
		double delta = 0;
		
		while(v.isRunning()) {
			long start = System.currentTimeMillis();
			v.display();
			delta = System.currentTimeMillis() - start;
			this.move(delta, 0.005f);
			shoot();
			physics.update(delta);
			//System.out.println(CellList.checkCell(cam));
			
			double moveSpeed = (System.currentTimeMillis() - start) * 0.005f;
			
			PortalList.update(cam, moveSpeed);
			TeleporterList.check(spheres);
		}
	}
	
	protected void move(double renderDuration, double speed)
    {
        move(renderDuration*speed);
    }
    
    protected void move(double renderDuration)
    {
        synchronized (pressedKeys)
        {
            if (mouseDragged || !pressedKeys.isEmpty())
            {
                for (SceneNode cam: this.cams)
                {
                    for (Character key: pressedKeys)
                    {
                        switch (key)
                        {
                        case 'W':
                        	ry=0;
                        	cam.setTransform(cam.getTransform().mul(Transform.translate(0, 0, (float)-renderDuration)));
                            break;
                        case 'S':
                        	ry=0;
                        	cam.setTransform(cam.getTransform().mul(Transform.translate(0, 0, (float)renderDuration)));
                            break;
                        case 'A':
                        	ry=0;
                        	cam.setTransform(cam.getTransform().mul(Transform.translate((float)-renderDuration, 0, 0)));
                            break;
                        case 'D':
                        	ry=0;
                        	cam.setTransform(cam.getTransform().mul(Transform.translate((float)renderDuration, 0, 0)));
                            break;      
                        case 'R':
                        	ry=0;
                        	cam.setTransform(cam.getTransform().mul(Transform.translate(0, (float)renderDuration, 0)));
                        	break;
                        case 'F':
                        	ry=0;
                        	cam.setTransform(cam.getTransform().mul(Transform.translate(0, (float)-renderDuration, 0)));
                        	break;
                        case 'Q':
                            System.exit(0);
                            break;
                        }
                    }
                    
                    cam.setTransform(cam.getTransform().mul(Transform.rotateYDeg(ry)));
                    //cam.setTransform(cam.getTransform().mul(Transform.rotateXDeg(rx)));       
                }
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
					    	System.out.println("shoot");
					    	Vector3 vel = cam.getTransform().getMatrix().rotationMatrix().mul(new Vector3(0.0f, 0.0f, -30.0f));
					    	spheres.removeChildNode(sphereShape);
					    	spheres.addChildNode(sphereShape);
					    	spheres.setTransform(cam.getTransform());
					        sphereBody = physics.getRigidBody(spheres, new SphereShape(1.0f));
					        sphereBody.setLinearVelocity(new Vector3f(vel.x(), vel.y(), vel.z()));
					        sphereBody.setMassProps(10.0f, new Vector3f(1.0f, 1.0f, 1.0f));
					        physics.getDynamicsWorld().addRigidBody(sphereBody);
					        pressedKeys.remove('E');
					        break;
                        }
                    }
				}
            }
        }
    }
}