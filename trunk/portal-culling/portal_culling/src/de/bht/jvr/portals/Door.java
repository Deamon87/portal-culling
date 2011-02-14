package de.bht.jvr.portals;

import de.bht.jvr.core.CameraNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.portals.culling.Cell;

public class Door extends Portal {

	private Cell cell, neighbor;
	
	public Door(String name, float height, float width) throws Exception {
		super(null, name);
		this.setHeight(height);
		this.setWidth(width);
		cell = new Cell(this.getName() + "Cell", 3, 3, 3);
		cell.setTransform(Transform.translate(0, 0, cell.getLength() / 2.0f));
		this.addChildNode(cell);
	}
	
	public Cell getCell() {
		return cell;
	}
	
	public void setCell(Cell cell) {
		this.cell = cell;
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