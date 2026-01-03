package com.siesque.rilipackcore.multiblock.cleanroom;

public enum CleanroomType {
    CLEANROOM,
    CLEANROOM_STERILE,
    CLEANROOM_EUCLID,
    CLEANROOM_MAYO;

    public String getTranslationKey() {
        return "rilipackcore.multiblock.cleanroom." + name().toLowerCase();
    }
}