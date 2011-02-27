package de.bht.jvr.portals.util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.bht.jvr.core.SceneNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.input.KeyEvent;
import de.bht.jvr.input.KeyListener;
import de.bht.jvr.input.MouseEvent;
import de.bht.jvr.input.MouseListener;

/**
 * the portal test base class
 * This class is a modified version of the class <code>TestBase</code> 
 * from the jvr-Framework
 * 
 * @author Christopher Sierigk
 */
public abstract class PortalTestBase implements KeyListener, MouseListener
{
	/** list of cameras that have to move  */
    protected List<SceneNode> cams = new ArrayList<SceneNode>();
    
    /** list of pressed keys by the user */
    protected Set<Character> pressedKeys = Collections.synchronizedSet(new HashSet<Character>());
    
    /** is mouse dragged or not */
    protected boolean mouseDragged = false;
    
    /** position of the mouse on the screen */
    protected Point mousePos = new Point();
    
    /** rotation of the x-axis */
    protected float rx = 0;
    
    /** rotation of the y-axis */
    protected float ry = 0;

    /**
     * Moves the current camera
     * 
     * @param renderDuration
     * 			the duration for rendering one frame
     * @param speed
     * 			the moving speed
     */
    protected void move(double renderDuration, double speed)
    {
        move(renderDuration*speed);
    }
    
    /**
     * Moves the current camera
     * 
     * @param renderDuration
     * 			the duration for rendering one frame
     */
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
    
    /*
     * (non-Javadoc)
     * @see de.bht.jvr.input.KeyListener#keyPressed(de.bht.jvr.input.KeyEvent)
     */
    @Override
    public void keyPressed(KeyEvent e)
    {
        pressedKeys.add(Character.toUpperCase((char)e.getKeyCode()));

    }

    /*
     * (non-Javadoc)
     * @see de.bht.jvr.input.KeyListener#keyReleased(de.bht.jvr.input.KeyEvent)
     */
    @Override
    public void keyReleased(KeyEvent e)
    {
        pressedKeys.remove(Character.toUpperCase((char)e.getKeyCode()));

    }

    /*
     * (non-Javadoc)
     * @see de.bht.jvr.input.KeyListener#keyTyped(de.bht.jvr.input.KeyEvent)
     */
    @Override
    public void keyTyped(KeyEvent arg0)
    {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * @see de.bht.jvr.input.MouseListener#mouseClicked(de.bht.jvr.input.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent arg0)
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see de.bht.jvr.input.MouseListener#mouseDragged(de.bht.jvr.input.MouseEvent)
     */
    @Override
    public void mouseDragged(MouseEvent e)
    {
        ry += (mousePos.getX() - e.getX())/ 200;
        rx += (mousePos.getY() - e.getY())/ 200;
        //System.out.println("ry: " + ry + "\nrx: " + rx);
        mousePos.setLocation(e.getX(), e.getY());
        mouseDragged = true;
    }

    /*
     * (non-Javadoc)
     * @see de.bht.jvr.input.MouseListener#mouseEntered(de.bht.jvr.input.MouseEvent)
     */
    @Override
    public void mouseEntered(MouseEvent arg0)
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see de.bht.jvr.input.MouseListener#mouseExited(de.bht.jvr.input.MouseEvent)
     */
    @Override
    public void mouseExited(MouseEvent arg0)
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see de.bht.jvr.input.MouseListener#mouseMoved(de.bht.jvr.input.MouseEvent)
     */
    @Override
    public void mouseMoved(MouseEvent arg0)
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see de.bht.jvr.input.MouseListener#mousePressed(de.bht.jvr.input.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent e)
    {
        mousePos.setLocation(e.getX(), e.getY());
    }

    /*
     * (non-Javadoc)
     * @see de.bht.jvr.input.MouseListener#mouseReleased(de.bht.jvr.input.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent arg0)
    {
        mouseDragged = false;
    }

    /*
     * (non-Javadoc)
     * @see de.bht.jvr.input.MouseListener#mouseWheelMoved(de.bht.jvr.input.MouseEvent)
     */
    @Override
    public void mouseWheelMoved(MouseEvent arg0)
    {

    }
}
