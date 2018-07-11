//顶点着色器和片元着色器版本要保持一致
#version 310 es

precision highp int;
precision highp uint;
precision mediump image2D;
precision highp float;

in vec2 vTextureCoord;
layout(binding = 0, rgba32f) readonly uniform  image2D srcImage;
uniform int width;
uniform int height;

out vec4 gl_FragColor;

void main() {
  ivec2 location = ivec2(vTextureCoord.s * float(width), vTextureCoord.t * float(height));
  gl_FragColor = imageLoad(srcImage, location);
}