LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all--java-files-under, src)

LOCAL_RESOURCE_DIR := $(addprefix $(LOCAL_PATH)/, res)

LOCAL_PROGUARD_ENABLED := disabled

LOCAL_PACKAGE_NAME := LibSwitcher

LOCAL_CERTIFICATE := platform

include $(BUILD_PACKAGE)
