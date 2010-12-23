package de.bht.jvr.portals.tests;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.bht.jvr.collada14.loader.ColladaLoader;
import de.bht.jvr.core.CameraNode;
import de.bht.jvr.core.GroupNode;
import de.bht.jvr.core.PointLightNode;
import de.bht.jvr.core.SceneNode;
import de.bht.jvr.core.ShaderMaterial;
import de.bht.jvr.core.ShaderProgram;
import de.bht.jvr.core.Texture2D;
import de.bht.jvr.core.Transform;
import de.bht.jvr.core.pipeline.Pipeline;
import de.bht.jvr.core.uniforms.UniformInt;
import de.bht.jvr.core.uniforms.UniformVector2;
import de.bht.jvr.math.Vector2;
import de.bht.jvr.portals.Cell;
import de.bht.jvr.renderer.AwtRenderWindow;
import de.bht.jvr.renderer.RenderWindow;
import de.bht.jvr.renderer.Viewer;
import de.bht.jvr.tests.TestBase;

public class MyTest extends TestBase{

	public static void main(String[] args) {
		try {
			new MyTest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public MyTest() throws Exception {
		GroupNode root = new GroupNode();
		
		Cell cell = new Cell();
		root.addChildNode(cell);
		SceneNode plane = ColladaLoader.load(new File("meshes/plane.dae"));
		plane.setTransform(Transform.scale(1.0f, 1.0f, 1.0f));
		//plane.setTransform(Transform.rotateXDeg(-90).mul(Transform.scale(500)));
		root.addChildNode(plane);
		
		ShaderProgram prog = new ShaderProgram(new File("pipeline_shader/quad.vs"), new File("pipeline_shader/text_overlay.fs"));
		ShaderMaterial mat = new ShaderMaterial("OVERLAY", prog);
        mat.setTexture("OVERLAY", "jvr_Texture0", new Texture2D(new File("textures/fonts.png")));
		
        PointLightNode pLight = new PointLightNode();
        pLight.setColor(new Color(1.0f, 1.0f, 1.0f));
        pLight.setTransform(Transform.translate(0, 400, 200));
        root.addChildNode(pLight);
        
        CameraNode cam = new CameraNode("cam", 4/3, 60f);
        cam.setTransform(Transform.translate(0, 0, 1));
        this.cams.add(cam);
        root.addChildNode(cam);
        
		Pipeline p = new Pipeline(root);
		p.switchCamera(cam);
		//OGLPrinter printer = new OGLPrinter(p);

		p.clearBuffers(true, true, new Color(121, 188, 255));
		p.drawGeometry("AMBIENT", null);
		p.doLightLoop(true, true).drawGeometry("LIGHTING", null);

		p.drawQuad(mat, "OVERLAY");
		//printer.drawQuad();
		
		RenderWindow w = new AwtRenderWindow(p, 1024, 768);
		
		w.addKeyListener(this);
		w.addMouseListener(this);
		w.setVSync(true);
		Viewer viewer = new Viewer(w);
		try {
			while(viewer.isRunning())
			{
				long start = System.currentTimeMillis();
				//Long x = new Long(System.currentTimeMillis());
		        this.setScreenText(mat, 0.1f, 0.8f, 0.02f, 0.02f, "test");
				viewer.display();
				move(System.currentTimeMillis() - start, 0.001);
				
			}
			viewer.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void setScreenText(ShaderMaterial mat, float posX, float posY, float xSize, float ySize, String text)
    {
        List<Vector2> letters = new ArrayList<Vector2>();
        List<Vector2> positions = new ArrayList<Vector2>();
        List<Vector2> size = new ArrayList<Vector2>();
        Vector2 gridSize = new Vector2(0.0625f, 0.0625f);
        
        for(int i=0; i<text.length(); i++)
        {
            char letter = text.charAt(i);
            Vector2 letterV = null;
            switch(letter)
            {
            case '0':
                letterV = new Vector2(0,3);
                break;
            case '1':
                letterV = new Vector2(1,3);
                break;
            case '2':
                letterV = new Vector2(2,3);
                break;
            case '3':
                letterV = new Vector2(3,3);
                break;
            case '4':
                letterV = new Vector2(4,3);
                break;
            case '5':
                letterV = new Vector2(5,3);
                break;
            case '6':
                letterV = new Vector2(6,3);
                break;
            case '7':
                letterV = new Vector2(7,3);
                break;
            case '8':
                letterV = new Vector2(8,3);
                break;
            case '9':
                letterV = new Vector2(9,3);
                break;
            case 't':
            	letterV = new Vector2(4,7);
            	break;
            case 'e':
            	letterV = new Vector2(5,6);
            	break;
            case 's':
            	letterV = new Vector2(3,7);
            	break;
            }

            if(letterV!=null)
            {
                letterV = new Vector2(letterV.x() * gridSize.x(), letterV.y() * gridSize.y());
                letters.add(letterV);
                positions.add(new Vector2((letters.size()-1)*xSize+posX ,posY));
                size.add(new Vector2(xSize, ySize));
            }
        }
        mat.setUniform("OVERLAY", "lettersCount", new UniformInt(letters.size()));
        mat.setUniform("OVERLAY", "letters", new UniformVector2(letters));
        mat.setUniform("OVERLAY", "positions", new UniformVector2(positions));
        mat.setUniform("OVERLAY", "size", new UniformVector2(size));
        mat.setUniform("OVERLAY", "gridSize", new UniformVector2(gridSize));
    }
}
