#version 150

uniform float uTime;
uniform vec2 uResolution;
uniform vec3 uColor;
uniform vec3 uColor2;
uniform float uAlpha;
uniform float uSpeed;
uniform float uScale;
uniform float uIntensity;
uniform vec2 uCameraDir;
uniform float uFov;

out vec4 fragColor;

mat3 rotX(float a) { float c=cos(a),s=sin(a); return mat3(1,0,0,0,c,s,0,-s,c); }
mat3 rotY(float a) { float c=cos(a),s=sin(a); return mat3(c,0,s,0,1,0,-s,0,c); }
float hash(vec2 p) { return fract(sin(dot(p, vec2(127.1,311.7))) * 43758.5453); }
float noise(vec2 p) { vec2 i=floor(p), f=fract(p); f=f*f*(3.0-2.0*f); return mix(mix(hash(i),hash(i+vec2(1,0)),f.x),mix(hash(i+vec2(0,1)),hash(i+vec2(1,1)),f.x),f.y); }

void main() {
    vec2 uv=gl_FragCoord.xy/uResolution.xy*2.0-1.0;
    uv.x*=uResolution.x/uResolution.y;
    float fov=tan(radians(uFov)*0.5);
    vec3 ray=normalize(rotY(uCameraDir.x)*rotX(uCameraDir.y)*vec3(uv*fov,1.0));
    float t=uTime*uSpeed;
    vec2 sphere=vec2(atan(ray.z,ray.x), asin(clamp(ray.y,-1.0,1.0)));
    float scale=max(uScale,1.0);
    vec2 p=sphere*scale+vec2(t*0.012,-t*0.006);
    float cloud=noise(p*0.72)+0.5*noise(p*1.53+4.2)+0.25*noise(p*3.1-2.4);
    vec3 deep=mix(vec3(0.006,0.008,0.030),vec3(0.025,0.012,0.070),smoothstep(-0.3,0.8,ray.y));
    vec3 nebula=mix(uColor,uColor2,smoothstep(0.20,0.85,cloud));
    deep+=nebula*smoothstep(0.78,1.35,cloud)*0.42*uIntensity;
    vec2 cell=floor(p*34.0);
    vec2 local=fract(p*34.0)-0.5;
    float star=step(0.985,hash(cell));
    float twinkle=0.55+0.45*sin(t*3.0+hash(cell)*25.0);
    float point=smoothstep(0.08,0.0,length(local))*star;
    deep+=mix(uColor,uColor2,hash(cell+7.0))*(point*5.0+star*twinkle*1.7)*uIntensity;
    float horizon=smoothstep(-0.55,0.10,ray.y);
    deep*=mix(0.42,1.0,horizon);
    fragColor=vec4(clamp(deep,0.0,1.0),uAlpha);
}
