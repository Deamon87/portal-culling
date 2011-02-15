package de.bht.jvr.portals;

import de.bht.jvr.core.CameraNode;
import de.bht.jvr.core.Finder;
import de.bht.jvr.core.ShapeNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.portals.culling.Cell;

public class Door extends Portal {

	private Cell cell, neighbor;
	
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
	
	public Cell getCell() {
		return cell;
	}
	
	public void setCell(Cell cell) {
		this.cell = cell;
		this.addChildNode(cell);
	}
	
	public Cell getNeighbor() {
		return neighbor;
	}
	
	public void setNeighbor(Cell neighbor) {
		this.neighbor = neighbor;
	}
	
	@Override
	public void render() {
	}

	@Override
	public void update(CameraNode camera, double moveSpeed) {
	}
}