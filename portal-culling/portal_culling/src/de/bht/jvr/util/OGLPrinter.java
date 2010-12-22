package de.bht.jvr.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.bht.jvr.core.ShaderMaterial;
import de.bht.jvr.core.ShaderProgram;
import de.bht.jvr.core.Texture2D;
import de.bht.jvr.core.pipeline.Pipeline;
import de.bht.jvr.core.uniforms.UniformInt;
import de.bht.jvr.core.uniforms.UniformVector2;
import de.bht.jvr.math.Vector2;

public class OGLPrinter {

	private Pipeline pipeline;
	private ShaderMaterial mat;
	
	public OGLPrinter(Pipeline pipeline) {
		this.pipeline = pipeline;
		pipeline.drawQuad(mat, "OVERLAY");
		try {
			ShaderProgram prog = new ShaderProgram(new File("pipeline_shader/quad.vs"), new File("pipeline_shader/text_overlay.fs"));
			mat = new ShaderMaterial("OVERLAY", prog);
	        mat.setTexture("OVERLAY", "jvr_Texture0", new Texture2D(new File("textures/fonts.png")));
	        
	        this.pipeline.drawQuad(mat, "OVERLAY");
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void print(String text) {
        this.setScreenText(mat, 0.1f, 0.8f, 0.02f, 0.02f, text);

                
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
