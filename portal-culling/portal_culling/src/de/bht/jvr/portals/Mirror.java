package de.bht.jvr.portals;

import java.io.File;

import de.bht.jvr.collada14.loader.ColladaLoader;
import de.bht.jvr.core.CameraNode;
import de.bht.jvr.core.Finder;
import de.bht.jvr.core.GroupNode;
import de.bht.jvr.core.SceneNode;
import de.bht.jvr.core.ShaderMaterial;
import de.bht.jvr.core.ShaderProgram;
import de.bht.jvr.core.ShapeNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.core.pipeline.Pipeline;

public class Mirror extends Portal {

	public Mirror(Pipeline p, String name) throws Exception {
		
		super(p, name);
		
		GroupNode root = new GroupNode();
		
		// mirror plane
        SceneNode mirrorPlane = ColladaLoader.load(new File("meshes/plane.dae"));
        mirrorPlane.setTransform(Transform.translate(0, -30, 0).mul(Transform.rotateXDeg(-90).mul(Transform.scale(10000))));
        root.addChildNode(mirrorPlane);
		
		// mirror camera
        CameraNode mirrorCam = new CameraNode("cam2", -4f / 3f, 60f);
        mirrorCam.setTransform(Transform.translate(0, 0, 0));
        root.addChildNode(mirrorCam);        
        
        // mirror material
        ShaderProgram prog = new ShaderProgram(new File("shader/simple_mirror.vs"), new File("shader/simple_mirror.fs"));
        ShaderMaterial mat = new ShaderMaterial("AMBIENT", prog); 
        mat.setMaterialClass("MirrorClass");
        
        ShapeNode mirrorShape = Finder.find(mirrorPlane, ShapeNode.class, null);
        mirrorShape.setMaterial(mat);
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
}
