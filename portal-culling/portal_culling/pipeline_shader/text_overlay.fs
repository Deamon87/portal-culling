uniform sampler2D jvr_Texture0;
uniform vec2 positions[128];
uniform vec2 letters[128];
uniform vec2 size[128];
uniform int lettersCount;
uniform vec2 gridSize;

varying vec2 texCoord;

void main (void)
{
	vec2 screenPos = vec2(texCoord.x, 1.0-texCoord.y);

	bool found = false;
	for(int i=0; i<lettersCount; i++)
	{
		if(
		(screenPos.x >= positions[i].x) && 
		(screenPos.x <= (positions[i].x + size[i].x)) &&
		(screenPos.y >= positions[i].y) &&
		(screenPos.y <= (positions[i].y + size[i].y))
		)
		{
			vec2 fontCoord = letters[i];
			fontCoord.x += ((screenPos.x - positions[i].x)/size[i].x) * gridSize.x;
			fontCoord.y += ((screenPos.y - positions[i].y)/size[i].y) * gridSize.y;
			gl_FragColor = texture2D(jvr_Texture0, fontCoord);
			found = true;
		}
	}
	
	if(!found) discard;
}
