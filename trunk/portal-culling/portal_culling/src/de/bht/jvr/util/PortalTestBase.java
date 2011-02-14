package de.bht.jvr.util;

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

public abstract class PortalTestBase implements KeyListener, MouseListener
{
    protected List<SceneNode> cams = new ArrayList<SceneNode>();
    protected boolean run = true;
    protected Set<Character> pressedKeys = Collections.synchronizedSet(new HashSet<Character>());
    protected boolean mouseDragged = false;
    protected Point mousePos = new Point();
    protected float rx = 0;
    protected float ry = 0;

    protected void move(double renderDuration, double speed)
    {
        move(renderDuration*speed);
    }
    
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
    
    @Override
    public void keyPressed(KeyEvent e)
    {
        pressedKeys.add(Character.toUpperCase((char)e.getKeyCode()));

    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        pressedKeys.remove(Character.toUpperCase((char)e.getKeyCode()));

    }

    @Override
    public void keyTyped(KeyEvent arg0)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseClicked(MouseEvent arg0)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        ry += (mousePos.getX() - e.getX())/ 200;
        rx += (mousePos.getY() - e.getY())/ 200;
        //System.out.println("ry: " + ry + "\nrx: " + rx);
        mousePos.setLocation(e.getX(), e.getY());
        mouseDragged = true;
    }

    @Override
    public void mouseEntered(MouseEvent arg0)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent arg0)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMoved(MouseEvent arg0)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        mousePos.setLocation(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent arg0)
    {
        mouseDragged = false;
    }

    @Override
    public void mouseWheelMoved(MouseEvent arg0)
    {

    }
}
