package dev.smithed.radon.mixin;

import dev.smithed.radon.Radon;
import dev.smithed.radon.mixin_interface.IEntitySelectorExtender;
import dev.smithed.radon.mixin_interface.IServerWorldExtender;
import dev.smithed.radon.utils.SelectorContainer;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.predicate.NumberRange;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Mixin(EntitySelector.class)
public class EntitySelectorMixin implements IEntitySelectorExtender {

    @Shadow private Box box;
    @Shadow private TypeFilter<Entity, ?> entityFilter;

    /**
     * @author dragoncommands
     * TODO: make this a redirect to avoid double implementation
     */
    @Inject(method = "appendEntitiesFromWorld", at=@At("HEAD"), cancellable = true)
    void appendEntitiesFromWorldInject(List<Entity> result, ServerWorld world, Vec3d pos, Predicate<Entity> predicate, CallbackInfo ci) {
        if(Radon.CONFIG.entitySelectorOptimizations && world instanceof IServerWorldExtender extender) {
            result.addAll(extender.getEntitiesByTag(entityFilter, predicate, this.container));
            ci.cancel();
        }
    }

    private SelectorContainer container;

    @Override
    public void setContainer(SelectorContainer container) {
        this.container = container;
    }

    @Override
    public SelectorContainer getContainer(SelectorContainer container) {
        return this.container;
    }
}
