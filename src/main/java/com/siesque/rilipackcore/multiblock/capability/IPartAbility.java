package com.siesque.rilipackcore.multiblock.capability;

import com.siesque.rilipackcore.multiblock.part.IMultiPart;

public interface IPartAbility {
    String getName();
    boolean isDefaultFor(IMultiPart part);
}