package dev.smithed.radon.mixin;

import dev.smithed.radon.Radon;
import dev.smithed.radon.mixin_interface.IEntitySelectorExtender;
import dev.smithed.radon.mixin_interface.IServerWorldExtender;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Predicate;

@Mixin(EntitySelector.class)
public class EntitySelectorMixin implements IEntitySelectorExtender {

    private String entityTag = null;
    @Shadow private Box box;
    @Shadow private TypeFilter<Entity, ?> entityFilter;

    @Override
    public String getTag() {
        return entityTag;
    }

    @Override
    public void setTag(String tag) {
        this.entityTag = tag;
    }

    /*
    @Author dragoncommands
    @TODO make this a redirect to avoid double implementation
     */
    @Inject(method = "appendEntitiesFromWorld", at=@At("HEAD"), cancellable = true)
    void appendEntitiesFromWorldInject(List<Entity> result, ServerWorld world, Vec3d pos, Predicate<Entity> predicate, CallbackInfo ci) {
        if(Radon.CONFIG.entitySelectorOptimizations && getTag() != null && world instanceof IServerWorldExtender extender) {
            result.addAll(extender.getEntitiesByTag(entityFilter, predicate, getTag()));
        } else if (this.box != null) {
            result.addAll(world.getEntitiesByType(this.entityFilter, this.box.offset(pos), predicate));
        } else {
            result.addAll(world.getEntitiesByType(this.entityFilter, predicate));
        }
        // don't want to append twice
        ci.cancel();
    }


}
