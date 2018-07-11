//顶点着色器和片元着色器版本要保持一致
#version 310 es

uniform mat4 uMVPMatrix;
uniform mat4 uSTMatrix;
in vec4 aPosition;
in vec4 aTextureCoord;

out vec2 vTextureCoord;

void main() {
  gl_Position = uMVPMatrix * aPosition;
  vTextureCoord = (uSTMatrix * aTextureCoord).st;
}