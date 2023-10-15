#ifdef GL_ES
#define LOWP lowp
#define MED mediump
#define HIGH highp
precision mediump float;
#else
#define MED
#define LOWP
#define HIGH
#endif

#if defined(diffuseTextureFlag) && defined(blendedFlag)
#define blendedTextureFlag
varying MED vec2 v_texCoords0;
uniform sampler2D u_diffuseTexture;
uniform float u_alphaTest;
#endif
uniform vec2 u_cameraNearFar;


varying HIGH float v_depth;

float linearizeDepth(float depth, float near, float far) {

	return (2.0 * near) / (far + near - depth * (far - near));
}

void main() {
	#ifdef blendedTextureFlag
		if (texture2D(u_diffuseTexture, v_texCoords0).a < u_alphaTest)
			discard;
	#endif // blendedTextureFlag

//		convert clip space z into window space z
//float color = (clipZ - u_cameraNearFar.x) / (-u_cameraNearFar.y - u_cameraNearFar.x);
//	#ifdef PackedDepthFlag
//		HIGH float depth = v_depth;
		const HIGH vec4 bias = vec4(1.0 / 255.0, 1.0 / 255.0, 1.0 / 255.0, 0.0);
		HIGH vec4 color = vec4(v_depth, fract(v_depth * 255.0), fract(v_depth * 65025.0), fract(v_depth * 16581375.0));
		HIGH vec4 frag = color - (color.yzww * bias);
		gl_FragColor = vec4(frag.r,frag.r,frag.r,frag.r);
//	#endif //PackedDepthFlag
}
