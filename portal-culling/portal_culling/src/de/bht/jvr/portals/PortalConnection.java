package de.bht.jvr.portals;

public class PortalConnection {

	private Portal portal1;
	private Portal portal2;
	
	public PortalConnection(Portal portal1, Portal portal2) {
		this.portal1 = portal1;
		this.portal2 = portal2;
	}
	
	public void setPortal1(Portal portal1) {
		this.portal1 = portal1;
	}
	
	public Portal getPortal1() {
		return this.portal1;
	}
	
	public void setPortal2(Portal portal2) {
		this.portal2 = portal2;
	}
	
	public Portal getPortal2() {
		return this.portal2;
	}
	
	public static void connect(Portal portal1, Portal portal2) {
		portal1.getCamera();
		portal2.getCamera();
		portal1.getPosition();
		portal2.getPosition();
		portal1.getDirection();
		portal2.getDirection();
	}
}
