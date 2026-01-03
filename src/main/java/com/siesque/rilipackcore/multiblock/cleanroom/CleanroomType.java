package com.siesque.rilipackcore.multiblock.cleanroom;

public enum CleanroomType {
    CLEANROOM,
    CLEANROOM_STERILE;

    public String getTranslationKey() {
        return "rilipackcore.multiblock.cleanroom." + name().toLowerCase();
    }
}