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
	private GroupNode wallNode;
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
			wallNode = new GroupNode();
			
			ShaderMaterial phong = ShaderMaterial.makePhongShaderMaterial();
			phong.setUniform("LIGHTING", "jvr_Material_Diffuse", new UniformColor(this.color));
			
			ShapeNode wallShape = Finder.find(wall, ShapeNode.class, null);
			//wallShape.setTransform(Transform.scale(width, height, 1));
			wallShape.setMaterial(phong);
			wallNode.addChildNode(wall);
			wallNode.setTransform(Transform.scale(width, height, 1));
			this.addChildNode(wallNode);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addDoor(Door door) {
		Cell cell = new Cell(door.getName() + "Cell",  10, 10, this.getHeight());
		cell.setTransform(Transform.translate(0, 0, cell.getLength() / 2.0f));
		cell.removeChildNode(cell.getNorthWall());
		door.setCell(cell);
		this.removeChildNode(wallNode);
		door.setTransform(Transform.rotateYDeg(180));
		this.addChildNode(door);
		GroupNode leftPiece, rightPiece, upPiece;
		
		float width = (cell.getNorthWall().getWidth() / 2.0f) - (door.getWidth() / 2.0f);
		leftPiece = new GroupNode();
		leftPiece.addChildNode(wall);
		leftPiece.setTransform(Transform.translate((-(cell.getNorthWall().getWidth()) / 2.0f) + (width / 2.0f), 0, 0)
				.mul(Transform.scale(width, this.getHeight(), 1)));
		
		rightPiece = new GroupNode();
		rightPiece.addChildNode(wall);
		rightPiece.setTransform(Transform.translate((cell.getNorthWall().getWidth() / 2.0f) - (width / 2.0f), 0, 0)
				.mul(Transform.scale(width, this.getHeight(), 1)));
				
		float height = this.getHeight() - door.getHeight();
		upPiece = new GroupNode();
		upPiece.addChildNode(wall);
		upPiece.setTransform(Transform.scale(door.getWidth(), height, 1)
				.mul(Transform.translate(0, (cell.getNorthWall().getHeight() / 2.0f) - (height / 2.0f), 0)));
		
		this.addChildNodes(leftPiece, rightPiece, upPiece);		
	}
}