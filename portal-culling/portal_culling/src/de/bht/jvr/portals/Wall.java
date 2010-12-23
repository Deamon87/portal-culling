package de.bht.jvr.portals;

import java.io.File;

import de.bht.jvr.collada14.loader.ColladaLoader;
import de.bht.jvr.core.Finder;
import de.bht.jvr.core.GroupNode;
import de.bht.jvr.core.SceneNode;
import de.bht.jvr.core.ShaderMaterial;
import de.bht.jvr.core.ShaderProgram;
import de.bht.jvr.core.ShapeNode;
import de.bht.jvr.core.Transform;

public class Wall extends ShapeNode {

	private float height;
	private float width;
	
	public Wall(float height, float width) throws Exception {
		GroupNode root = new GroupNode();
		
		SceneNode mirrorPlane = ColladaLoader.load(new File("meshes/plane.dae"));
        mirrorPlane.setTransform(Transform.translate(0, -30, 0).mul(Transform.rotateXDeg(-90).mul(Transform.scale(10000))));
        root.addChildNode(mirrorPlane);
        
        ShaderProgram prog = new ShaderProgram(new File("shader/simple_mirror.vs"), new File("shader/simple_mirror.fs"));
        ShaderMaterial mat = new ShaderMaterial("AMBIENT", prog); 
        mat.setMaterialClass("MirrorClass");
        
        ShapeNode mirrorShape = Finder.find(mirrorPlane, ShapeNode.class, null);
        mirrorShape.setMaterial(mat);
	}
	
	public float getHeight() {
		return height;
	}
	
	public void setHeight(float height) {
		this.height = height;
	}
	
	public float getWidth() {
		return width;
	}
	
	public void setWidth(float width) {
		this.width = width;
	}
	
	public void draw() {
		
	}
}
