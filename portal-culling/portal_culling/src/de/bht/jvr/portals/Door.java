package de.bht.jvr.portals;

import de.bht.jvr.core.CameraNode;
import de.bht.jvr.core.Finder;
import de.bht.jvr.core.ShapeNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.portals.culling.Cell;

/**
 * the door class
 * 
 * @author Christopher Sierigk
 *
 */
public class Door extends Portal {

	/** the current cell */
	private Cell cell;
	
	/** the neighboring cell */
	private Cell neighbor;
	
	/**
	 * Instantiates a new door
	 * 
	 * @param name
	 * 			the name of the door
	 * @param height
	 * 			the height of the door
	 * @param width
	 * 			the width of the door
	 * @throws Exception
	 * 			the exception
	 */
	public Door(String name, float height, float width) throws Exception {
		super(null, name);
		this.setHeight(height);
		this.setWidth(width);
		this.setHeight(height);
		this.setWidth(width);
		ShapeNode shape = Finder.find(this, ShapeNode.class, null);
		shape.setTransform(Transform.scale(this.getWidth(), this.getHeight(), 1));
		this.removeChildNode(this.getPortal());
		//this.getPortal();
	}
	
	/**
	 * Gets the current cell
	 * 
	 * @return the current cell
	 */
	public Cell getCell() {
		return cell;
	}
	
	/**
	 * Sets the current cell
	 * 
	 * @param cell
	 * 			the current cell
	 */
	public void setCell(Cell cell) {
		this.cell = cell;
		this.addChildNode(cell);
	}
	
	/**
	 * Gets the neighboring cell
	 * 
	 * @return the neighboring cell
	 */
	public Cell getNeighbor() {
		return neighbor;
	}
	
	/**
	 * Sets the neighboring cell
	 * 
	 * @param neighbor
	 * 			the neighboring cell
	 */
	public void setNeighbor(Cell neighbor) {
		this.neighbor = neighbor;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.bht.jvr.portals.Portal#render()
	 */
	@Override
	public void render() {
	}

	/*
	 * (non-Javadoc)
	 * @see de.bht.jvr.portals.Portal#update(de.bht.jvr.core.CameraNode, double)
	 */
	@Override
	public void update(CameraNode camera, double moveSpeed) {
	}
}