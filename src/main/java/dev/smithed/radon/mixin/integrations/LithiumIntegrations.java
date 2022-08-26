package dev.smithed.radon.mixin.integrations;

import me.jellysquid.mods.lithium.common.entity.EquipmentEntity;

public class LithiumIntegrations {

    public static void triggerEquipmentUpdate(Object obj) {
        if(obj instanceof EquipmentEntity mixin)
            mixin.lithiumOnEquipmentChanged();
    }
}
