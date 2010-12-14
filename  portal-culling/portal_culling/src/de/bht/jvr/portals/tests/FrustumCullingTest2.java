package de.bht.jvr.portals.tests;

import java.awt.Color;
import java.io.File;

import de.bht.jvr.collada14.loader.ColladaLoader;
import de.bht.jvr.core.CameraNode;
import de.bht.jvr.core.GroupNode;
import de.bht.jvr.core.PointLightNode;
import de.bht.jvr.core.SceneNode;
import de.bht.jvr.core.Transform;
import de.bht.jvr.core.pipeline.Pipeline;
import de.bht.jvr.core.pipeline.PipelineCommandPtr;
import de.bht.jvr.input.KeyEvent;
import de.bht.jvr.renderer.NewtRenderWindow;
import de.bht.jvr.renderer.RenderWindow;
import de.bht.jvr.renderer.Viewer;
import de.bht.jvr.tests.TestBase;

/**
 * Press E and R to switch between VFC modes.
 * 
 * @author Marc
 *
 */

public class FrustumCullingTest2 extends TestBase
{
    public static void main(String[] args)
    {
        //Log.addLogListener(new LogPrinter());
        try
        {
            new FrustumCullingTest2();
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private PipelineCommandPtr vfc;
    public FrustumCullingTest2() throws Exception
    {
        
        GroupNode root = new GroupNode();        
        SceneNode teapot = ColladaLoader.load(new File("meshes/teapot.dae"));
        teapot.setTransform(Transform.scale(10));
        
        GroupNode all = new GroupNode();
        
        GroupNode cluster = new GroupNode();
        int k = 2;
        for(int x = 0; x<k; x++)
            for(int y = 0; y<k; y++)
                for(int z = 0; z<k; z++)
                {
                    GroupNode n = new GroupNode();
                    n.setTransform(Transform.translate(x*50,y*50,z*50));
                    n.addChildNode(teapot);
                    cluster.addChildNode(n);
                }
        
        int h = 5;
        for(int xx = 0; xx<h; xx++)
            for(int yy = 0; yy<h; yy++)
                for(int zz = 0; zz<h; zz++)
                {                   
                    GroupNode g = new GroupNode();
                    g.setTransform(Transform.translate(xx*50*k, yy*50*k, zz*50*k));
                    g.addChildNode(cluster);
                    all.addChildNode(g);
                }
        System.out.println("Count: "+h*h*h*k*k*k);
        all.setTransform(Transform.translate(-25*k*h, -25*k*h, -25*k*h));
        root.addChildNode(all);
        
        //root.addChildNode(teapot);
        PointLightNode pLight = new PointLightNode();
        pLight.setColor(new Color(1.0f, 1.0f, 1.0f));
        pLight.setTransform(Transform.translate(0, 400, 200));
        root.addChildNode(pLight);

        CameraNode cam1 = new CameraNode("cam1", 4f / 3f, 60f);
        cam1.setTransform(Transform.translate(0, 0, 0));
        root.addChildNode(cam1);
        this.cams.add(cam1);

        //Pipeline//////////////////////////////////////
        Pipeline p = new Pipeline(root);
        vfc = p.setViewFrustumCullingMode(0);
        p.switchCamera(cam1);
        p.clearBuffers(true, true, new Color(121, 188, 255));        
        p.drawGeometry("AMBIENT", null);
        p.doLightLoop(true, true).drawGeometry("LIGHTING", null);

        ///////////////////////////////////////////////
        RenderWindow w = new NewtRenderWindow(p, 1024, 768);
        w.addKeyListener(this);
        w.addMouseListener(this);
        w.setVSync(false);
        Viewer viewer = new Viewer(w);

        try
        {
            while (viewer.isRunning() && run)
            {
                long start = System.currentTimeMillis();
                viewer.display();
                move(System.currentTimeMillis() - start, 0.1);
            }
            viewer.close();
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void keyPressed(KeyEvent e)
    {
        super.keyPressed(e);

        switch (new Character((char)e.getKeyCode()))
        {
        case 'E': 
            System.out.println("DL-VFC");
            vfc.setViewFrustumCullingMode(1);
            break;
        case 'R':
            System.out.println("H-VFC");
            vfc.setViewFrustumCullingMode(2);
            break;
        }
    }
}
