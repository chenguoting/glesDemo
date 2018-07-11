#version 310 es
//没有使用oes纹理，所以不需要这行
//#extension GL_OES_EGL_image_external_essl3 : require
//输入
layout(local_size_x = 4, local_size_y = 4, local_size_z = 1) in;
uniform sampler2D uTextureSampler;
uniform int width;
uniform int height;
//输出
layout(binding = 0, rgba32f) writeonly uniform  image2D desImage;

void main()
{
    vec2 textureCoord = vec2(gl_GlobalInvocationID.xy) / vec2(width, height);
    vec4 color = texture(uTextureSampler, textureCoord);
    float y = 0.299*color.r + 0.587*color.g + 0.114*color.b;
    imageStore(desImage, ivec2(gl_GlobalInvocationID.xy), vec4(y, y, y, 1.0));
}