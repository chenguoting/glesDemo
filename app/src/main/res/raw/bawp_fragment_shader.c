#version 310 es

precision highp int;
precision highp uint;
precision mediump image2D;
precision highp float;

in vec2 vTextureCoord;
out vec4 gl_FragColor;
layout(binding = 0, rgba32f) readonly uniform  image2D srcImage;
uniform int width;
uniform int height;

void main() {
  ivec2 location = ivec2(vTextureCoord.s * float(width), vTextureCoord.t * float(height));
  gl_FragColor = imageLoad(srcImage, location);
}