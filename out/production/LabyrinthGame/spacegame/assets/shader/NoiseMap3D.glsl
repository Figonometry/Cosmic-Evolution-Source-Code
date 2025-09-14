#type compute
#version 460
layout(local_size_x = 8, local_size_y = 8, local_size_z = 8) in;

layout(std430, binding = 0) buffer NoiseMap {
    float noise[];
};

layout(std430, binding = 1) buffer PermBuffer {
    int perm[512];
    int permMod12[512];
};

uniform int width, height, depth, octaves;
uniform float offsets[16];

const float F2 = 0.3660254037844386f;
const float G2 = 0.21132486540518713f;
const float F3 = 1.0f / 3.0f;
const float G3 = 1.0f / 6.0f;
const float F4 = 0.30901699437494745f;
const float G4 = 0.1381966011250105f;

vec3 grad3[] = { vec3(1,1,0), vec3(-1,1,0), vec3(1,-1, 0), vec3(-1, -1, 0), vec3(1, 0, 1), vec3(-1, 0, 1), vec3(1, 0, -1), vec3(0, 1, 1), vec3(0, -1, 1),vec3(0, 1, -1), vec3(0, -1, -1) };

float dot(vec3 g, float x, float y, float z){
    return g.x * x + g.y * y + g.z * z;
}

int fastfloor(float x){
    int xi = int(x);
    return x < xi ? xi - 1 : xi;
}

float simplexNoise3D(vec3 pos){
    float x = pos.x;
    float y = pos.y;
    float z = pos.z;
    float n0, n1, n2, n3;
    float s = (x + y + z) * F3;
    int i = fastfloor(x + s);
    int j = fastfloor(y + s);
    int k = fastfloor(z + s);
    float t = (i + j + k) * G3;
    float X0 = i - t;
    float Y0 = j - t;
    float Z0 = k - t;
    float x0 = x - X0;
    float y0 = y - Y0;
    float z0 = z - Z0;
    int i1, j1, k1;
    int i2, j2, k2;
    if(x0 >= y0){
        if(y0 >= z0){
            i1 = 1;
            j1 = 0;
            k1 = 0;
            i2 = 1;
            j2 = 1;
            k2 = 0;
        } else if(x0 >= z0){
            i1 = 1;
            j1 = 0;
            k1 = 0;
            i2 = 1;
            j2 = 0;
            k2 = 1;
        } else {
            i1 = 0;
            j1 = 0;
            k1 = 1;
            i2 = 1;
            j2 = 0;
            k2 = 1;
        }
    } else {
        if (y0 < z0) {
            i1 = 0;
            j1 = 0;
            k1 = 1;
            i2 = 0;
            j2 = 1;
            k2 = 1;
        }
        else if (x0 < z0) {
            i1 = 0;
            j1 = 1;
            k1 = 0;
            i2 = 0;
            j2 = 1;
            k2 = 1;
        }
        else {
            i1 = 0;
            j1 = 1;
            k1 = 0;
            i2 = 1;
            j2 = 1;
            k2 = 0;
        }
    }

    float x1 = x0 - i1 + G3;
    float y1 = y0 - j1 + G3;
    float z1 = z0 - k1 + G3;
    float x2 = x0 - i2 + 2.0f * G3;
    float y2 = y0 - j2 + 2.0f * G3;
    float z2 = z0 - k2 + 2.0f * G3;
    float x3 = x0 - 1.0f + 3.0f * G3;
    float y3 = y0 - 1.0f + 3.0f * G3;
    float z3 = z0 - 1.0f + 3.0f * G3;

    int ii = i & 255;
    int jj = j & 255;
    int kk = k & 255;
    int gi0 = permMod12[ii + perm[jj + perm[kk]&0xFF]&0xFF]&0xFF;
    int gi1 = permMod12[ii + i1 + perm[jj + j1 + perm[kk + k1]&0xFF]&0xFF]&0xFF;
    int gi2 = permMod12[ii + i2 + perm[jj + j2 + perm[kk + k2]&0xFF]&0xFF]&0xFF;
    int gi3 = permMod12[ii + 1 + perm[jj + 1 + perm[kk + 1]&0xFF]&0xFF]&0xFF;
    // Calculate the contribution from the four corners
    float t0 = 0.6f - x0 * x0 - y0 * y0 - z0 * z0;
    if (t0 < 0.0f)
    n0 = 0.0f;
    else {
        t0 *= t0;
        n0 = t0 * t0 * dot(grad3[gi0], x0, y0, z0);
    }
    float t1 = 0.6f - x1 * x1 - y1 * y1 - z1 * z1;
    if (t1 < 0.0f)
    n1 = 0.0f;
    else {
        t1 *= t1;
        n1 = t1 * t1 * dot(grad3[gi1], x1, y1, z1);
    }
    float t2 = 0.6f - x2 * x2 - y2 * y2 - z2 * z2;
    if (t2 < 0.0f)
    n2 = 0.0f;
    else {
        t2 *= t2;
        n2 = t2 * t2 * dot(grad3[gi2], x2, y2, z2);
    }
    float t3 = 0.6f - x3 * x3 - y3 * y3 - z3 * z3;
    if (t3 < 0.0f)
    n3 = 0.0f;
    else {
        t3 *= t3;
        n3 = t3 * t3 * dot(grad3[gi3], x3, y3, z3);
    }


    return 32.0f * (n0 + n1 + n2 + n3);
}

void main() {

    ivec3 id = ivec3(gl_GlobalInvocationID.xyz);
    if (id.x >= width || id.y >= height || id.z >= depth) return;

    float noiseValue = 0.0;
    vec3 normPos = vec3(id.x / float(width) - 0.5, id.y / float(height) - 0.5, id.z / float(depth) - 0.5);

    for (int i = 0; i < octaves; i++) {
        float weight = 1.0 / pow(2.0, i);
        noiseValue += weight * simplexNoise3D(normPos * pow(2.0, i) + offsets[i]);
    }

    int index = id.x + (id.y * width * depth) + (id.z * width);
    noise[index] = noiseValue;
}
