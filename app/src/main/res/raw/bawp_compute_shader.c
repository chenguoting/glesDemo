#version 310 es
#extension GL_OES_EGL_image_external_essl3 : require

layout(local_size_x = 4, local_size_y = 4, local_size_z = 1) in;
layout(binding = 0, rgba32f) writeonly uniform  image2D desImage;

uniform sampler2D uTextureSampler;
uniform int width;
uniform int height;

void main()
{
    vec4 color = texture(uTextureSampler, vec2(gl_GlobalInvocationID.xy) / vec2(width, height));
    float y = 0.299*color.r + 0.587*color.g + 0.114*color.b;
    float xc = float(gl_GlobalInvocationID.x)/float(width);
    //if(gl_LocalInvocationID.x >= uint(1)) {
    imageStore(desImage, ivec2(gl_GlobalInvocationID.xy), vec4(y, y, y, 1.0));
    //}
    //else {
    //imageStore(desImage, ivec2(72, 72), vec4(1, 0, 0, 1.0));
    //}
    //imageStore(desImage, ivec2(71, 72), vec4(1, 0, 0, 1.0));
    //imageStore(desImage, ivec2(70, 72), vec4(1, 0, 0, 1.0));
    //imageStore(desImage, ivec2(69, 72), vec4(1, 0, 0, 1.0));
    //imageStore(desImage, ivec2(68, 72), vec4(1, 0, 0, 1.0));
    //imageStore(desImage, ivec2(67, 72), vec4(1, 0, 0, 1.0));
    //imageStore(desImage, ivec2(66, 72), vec4(1, 0, 0, 1.0));
    //imageStore(desImage, ivec2(65, 72), vec4(1, 0, 0, 1.0));
    //imageStore(desImage, ivec2(64, 72), vec4(1, 0, 0, 1.0));
    //imageStore(desImage, ivec2(63, 72), vec4(1, 0, 0, 1.0));
    //imageStore(desImage, ivec2(62, 72), vec4(1, 0, 0, 1.0));
    //imageStore(desImage, ivec2(61, 72), vec4(1, 0, 0, 1.0));
    //imageStore(desImage, ivec2(60, 72), vec4(1, 0, 0, 1.0));
    //imageStore(desImage, ivec2(59, 72), vec4(1, 0, 0, 1.0));
}