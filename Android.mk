LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := UnifiedNlpApi
LOCAL_SRC_FILES := $(call all-java-files-under, unifiednlp-api/src/main/java)
LOCAL_SRC_FILES += unifiednlp-api/src/main/aidl/org/microg/nlp/api/LocationBackend.aidl \
                   unifiednlp-api/src/main/aidl/org/microg/nlp/api/GeocoderBackend.aidl \
                   unifiednlp-api/src/main/aidl/org/microg/nlp/api/LocationCallback.aidl
LOCAL_AIDL_INCLUDES := $(LOCAL_PATH)/unifiednlp-api/src/main/aidl
LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/unifiednlp-api/src/main/res
LOCAL_MANIFEST_FILE := unifiednlp-api/src/main/AndroidManifest.xml

include $(BUILD_STATIC_JAVA_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional
LOCAL_SDK_VERSION := current
LOCAL_PROGUARD_ENABLED := disabled
LOCAL_DEX_PREOPT := false

LOCAL_PACKAGE_NAME := MPermissionHelper
LOCAL_SRC_FILES := unifiednlp-api/src/main/java/org/microg/nlp/api/MPermissionHelperActivity.java
LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/unifiednlp-api/src/main/res
LOCAL_MANIFEST_FILE := unifiednlp-api/src/main/AndroidManifest.xml

include $(BUILD_PACKAGE)

