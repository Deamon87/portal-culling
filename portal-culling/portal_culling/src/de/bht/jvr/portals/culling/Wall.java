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

/**
 * the wall class
 */
public class Wall extends GroupNode {

	/**	the height of the wall */
	private float height;
	
	/** the width of the wall */
	private float width;
	
	/** the color of the wall */
	private Color color;
	
	/** the wall group node */
	private GroupNode wallNode;
	
	/** the scene node of the wall */
	private SceneNode wall;
	
	/**
	 * Instantiate a new wall
	 * 
	 * @param height
	 * 			the height of the wall
	 * @param width
	 * 			the width of the wall
	 * @param color
	 * 			the color of the wall
	 */
	public Wall(float height, float width, Color color) {
		this.height = height;
		this.width = width;
		this.color = color;
		this.init();
	}
	
	/**
	 * Gets the height
	 * 
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}
	
	/**
	 * Sets the height
	 * 
	 * @param height
	 * 			the height
	 */
	public void setHeight(float height) {
		this.height = height;
	}
	
	/**
	 * Gets the width
	 * 
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}
	
	/**
	 * Sets the width
	 * 
	 * @param width
	 * 			the width
	 */
	public void setWidth(float width) {
		this.width = width;
	}
	
	/**
	 * Gets the color
	 * 
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Sets the color
	 * 
	 * @param color
	 * 			the color
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * Initialize the wall
	 */
	private void init() {
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
	
	/**
	 * Adding a door into the wall
	 * 
	 * @param door
	 * 			the door to add
	 */
	public void addDoor(Door door) {
		Cell cell = new Cell(door.getName() + "Cell",  10, 10, this.getHeight(), new Color(0.5f, 0.5f, 0.f));
		cell.setTransform(Transform.translate(0, 0, cell.getLength() / 2.0f));
		cell.removeChildNode(cell.getNorthWall());
		door.setCell(cell);
		this.removeChildNode(wallNode);
		door.setTransform(Transform.rotateYDeg(180));
		
		door.getPortal().setTransform(Transform.translate(0, -0.5f, 0));
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
		upPiece.setTransform(Transform.translate(0, (cell.getNorthWall().getHeight() / 2.0f) - (height / 2.0f), 0)
				.mul(Transform.scale(door.getWidth(), height, 1)));
		
		this.addChildNodes(leftPiece, rightPiece, upPiece);		
	}
}