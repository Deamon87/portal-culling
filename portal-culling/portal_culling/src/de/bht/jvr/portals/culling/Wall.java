package de.bht.jvr.portals.culling;

import java.awt.Color;
import java.io.File;

import de.bht.jvr.collada14.loader.ColladaLoader;
import de.bht.jvr.core.Finder;
import de.bht.jvr.core.GroupNode;
import de.bht.jvr.core.SceneNode;
import de.bht.jvr.core.ShaderMaterial;
import de.bht.jvr.core.ShapeNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.core.uniforms.UniformColor;
import de.bht.jvr.portals.Door;

public class Wall extends GroupNode {

	private float height;
	private float width;
	private Color color;
	private SceneNode wall;
	
	public Wall(float height, float width, Color color) {
		this.height = height;
		this.width = width;
		this.color = color;
		this.init();
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
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void init() {
		try {
			wall = ColladaLoader.load(new File("meshes/plane.dae"));
			
			ShaderMaterial phong = ShaderMaterial.makePhongShaderMaterial();
			phong.setUniform("LIGHTING", "jvr_Material_Diffuse", new UniformColor(this.color));
			
			ShapeNode wallShape = Finder.find(wall, ShapeNode.class, null);
			wallShape.setTransform(Transform.scale(width, height, 1));
			wallShape.setMaterial(phong);
			this.addChildNode(wall);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addDoor(Door door) {
		door.getHeight();
		door.getWidth();
		this.removeChildNode(wall);
		door.setTransform(Transform.rotateYDeg(180));
		this.addChildNode(door);
		GroupNode leftPiece, rightPiece, upPiece;
		leftPiece = new GroupNode();
		//leftPiece.addChildNode(wall);
		rightPiece = new GroupNode();
		//rightPiece.addChildNode(wall);
		upPiece = new GroupNode();
		//upPiece.addChildNode(wall);
	}
}