#include <jni.h>
#include <string>
extern "C"
JNIEXPORT jstring

JNICALL
Java_com_universidad_bluenet_opencvagain_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT void JNICALL
Java_com_universidad_bluenet_opencvagain_MainActivity_faceDetecction(JNIEnv *env, jobject instance,
                                                                     jlong addrRgba) {
    // TODO
}