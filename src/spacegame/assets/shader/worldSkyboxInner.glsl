#type vertex
#version 460 core

layout (location=0) in vec3 aPos;

uniform dmat4 uProjection;
uniform dmat4 uView;

out vec3 fTexCoords;

void main()
{
    fTexCoords = aPos;
    gl_Position = vec4(uProjection * uView * vec4(aPos, 1.0));

}


#type fragment
#version 460 core

in vec3 fTexCoords;

uniform vec3 normalizedSunVector;
uniform vec3 sunFlareColor;
uniform float rainFogFactor;

out vec4 color;

void main()
{

    vec3 dir = normalize(fTexCoords);

    // --- Day/Night factor based on sun height ---
    float sunHeight = normalizedSunVector.y;

    // 1.0 = full day
    // 0.0 = full night
    float nightFactor = smoothstep(-0.25, 0.05, sunHeight);

    // --- Base sky color ---
    vec3 dayColor   = mix(vec3(0.45, 0.55, 0.75),   // horizon blue
    vec3(0.15, 0.25, 0.45),   // zenith blue
    clamp(dir.y, 0.0, 1.0));

    vec3 nightColor = vec3(0.0, 0.0, 0.0);

    // Blend day → night
    vec3 sky = mix(nightColor, dayColor, nightFactor);

    // --- Directional factors ---
    float towardSun = max(dot(dir, normalizedSunVector), 0.0);
    float awayFromSun = max(dot(dir, -normalizedSunVector), 0.0);

    // --- Sun glow (wide, soft) ---
    float sunGlow = pow(towardSun, 150.0) * nightFactor;
    sky += sunFlareColor * (sunGlow * 0.5);

    // --- Sun disk (tight, sharp) ---
    float sunDisk = pow(towardSun, 50000.0) * nightFactor;
    sky += sunFlareColor * (sunDisk * 1.0);

    // --- Warm band propagation ---
    float warmSpread = smoothstep(0.1, -0.3, sunHeight);
    float horizonFactor = 1.0 - clamp(dir.y, 0.0, 1.0);

    vec3 warmColor = vec3(1.0, 0.5, 0.2);
    sky += warmColor * (towardSun * warmSpread * horizonFactor * nightFactor);

    // --- Cool band opposite the sun ---
    vec3 coolColor = vec3(0.2, 0.3, 0.5);
    sky += coolColor * (awayFromSun * warmSpread * 0.5 * nightFactor);


    float rainFogColor = 0.4f;

    float colorDifRed = sky.x - rainFogColor;
    float colorDifGreen = sky.y - rainFogColor;
    float colorDifBlue = sky.z - rainFogColor;

    sky.x -= (colorDifRed * rainFogFactor);
    sky.y -= (colorDifGreen * rainFogFactor);
    sky.z -= (colorDifBlue * rainFogFactor);


    // --- Output ---
    color = vec4(sky, 1.0);
}