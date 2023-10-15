varying vec3 v_normal;
varying vec2 v_texcoord;
varying vec3 v_position;

void main() {
    v_normal = normalize(normalMatrix * normal);
    v_texcoord = texcoord;
    v_position = (modelViewMatrix * vec4(position, 1.0)).xyz;
    gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 1.0);
}
