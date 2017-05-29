LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := andengine_shared
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	C:\Users\nbraslav\StudioProjects\TowerOfHanoiFull\andEngineAnchorCenter\src\main\jni\Android.mk \
	C:\Users\nbraslav\StudioProjects\TowerOfHanoiFull\andEngineAnchorCenter\src\main\jni\Application.mk \
	C:\Users\nbraslav\StudioProjects\TowerOfHanoiFull\andEngineAnchorCenter\src\main\jni\build.sh \
	C:\Users\nbraslav\StudioProjects\TowerOfHanoiFull\andEngineAnchorCenter\src\main\jni\src\BufferUtils.cpp \
	C:\Users\nbraslav\StudioProjects\TowerOfHanoiFull\andEngineAnchorCenter\src\main\jni\src\GLES20Fix.c \

LOCAL_C_INCLUDES += C:\Users\nbraslav\StudioProjects\TowerOfHanoiFull\andEngineAnchorCenter\src\main\jni
LOCAL_C_INCLUDES += C:\Users\nbraslav\StudioProjects\TowerOfHanoiFull\andEngineAnchorCenter\src\release\jni

include $(BUILD_SHARED_LIBRARY)
