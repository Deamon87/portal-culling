uniform mat4 jvr_ProjectionMatrix;
uniform sampler2D jvr_PortalTexture;

uniform float waveTime;
uniform float waveScale;

varying vec3 eyeDir;
varying vec4 vertex;
varying vec2 texCoord;

void main (void)
{	
	// Moving from [-1,1] to [0,1]
	mat4 DCtoTC = mat4(	0.5, 0.0, 0.0, 0.0,
						0.0, 0.5, 0.0, 0.0,
						0.0, 0.0, 0.5, 0.0,
						0.5, 0.5, 0.5, 1.0);

  
    // Project an homogenize vertex to yield texture coordinates.
	vec4 vertexNDC = DCtoTC * jvr_ProjectionMatrix * vertex;	
	vec2 texCoord = vertexNDC.xy / vertexNDC.w;

	//texCoord.x = 1.0-texCoord.x;
	
	// Make a cyanish tint.
	gl_FragColor = texture2D(jvr_PortalTexture, texCoord);
}