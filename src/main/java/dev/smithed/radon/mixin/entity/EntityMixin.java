package dev.smithed.radon.mixin.entity;

import dev.smithed.radon.mixin_interface.ICustomNBTMixin;
import dev.smithed.radon.mixin_interface.IEntityIndexExtender;
import dev.smithed.radon.mixin_interface.IEntityMixin;
import dev.smithed.radon.mixin_interface.IServerWorldExtender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

@Mixin(Entity.class)
public abstract class EntityMixin implements IEntityMixin, ICustomNBTMixin {
    @Shadow
    public World world;
    @Shadow
    protected DataTracker dataTracker;
    @Shadow
    private Entity vehicle;
    @Shadow
    private int fireTicks;
    @Shadow
    protected boolean onGround;
    @Shadow
    private boolean invulnerable;
    @Shadow
    private int portalCooldown;
    @Shadow
    private boolean glowing;
    @Shadow
    private boolean hasVisualFire;
    @Shadow
    private Set<String> scoreboardTags;
    @Shadow
    protected UUID uuid;
    @Shadow
    protected abstract NbtList toNbtList(double... values);
    @Override
    public boolean writeCustomDataToNbtFiltered(NbtCompound nbt, String path, String topLevelNbt) {
        return false;
    }

    /**
     * @author ImCoolYeah105
     * @reason 1 line overwrite
     */
    @Overwrite
    public boolean addScoreboardTag(String tag) {
        if(this.scoreboardTags.size() < 1024 && this.scoreboardTags.add(tag)) {
            if(this.world instanceof IServerWorldExtender world && world.getEntityIndex() instanceof IEntityIndexExtender index)
                index.addEntityToTagMap(tag, this.uuid);
            return true;
        }
        return false;
    }

    /**
     * @author ImCoolYeah105
     * @reason 1 line overwrite
     */
    @Overwrite
    public boolean removeScoreboardTag(String tag) {
        if(this.scoreboardTags.remove(tag)) {
            if(this.world instanceof IServerWorldExtender world && world.getEntityIndex() instanceof IEntityIndexExtender index)
                index.removeEntityFromTagMap(tag, this.uuid);
            return true;
        }
        return false;
    }

    @Inject(method = "readNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At(value = "INVOKE", target = "Ljava/util/Set;clear()V"))
    private void clearTags(CallbackInfo ci) {
        if (this.world instanceof IServerWorldExtender world && world.getEntityIndex() instanceof IEntityIndexExtender index)
            this.scoreboardTags.forEach(tag -> index.removeEntityFromTagMap(tag, this.uuid));
    }

    @Inject(method = "readNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void addTags(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("Tags", 9) && this.world instanceof IServerWorldExtender world && world.getEntityIndex() instanceof IEntityIndexExtender index) {
            NbtList nbtList4 = nbt.getList("Tags", 8);
            int i = Math.min(nbtList4.size(), 1024);

            for(int j = 0; j < i; ++j) {
                index.addEntityToTagMap(nbtList4.getString(j), this.uuid);
            }
        }
    }

    @Override
    public NbtCompound writeFilteredNbt(NbtCompound nbt, String path) {
        String topLevelNbt = path.split("[\\.\\{\\[]")[0];
        Entity entity = ((Entity)(Object)this);

        try {
            switch(topLevelNbt) {
                case "Pos":
                    if (this.vehicle != null) {
                        nbt.put("Pos", toNbtList(this.vehicle.getX(), entity.getY(), this.vehicle.getZ()));
                    } else {
                        nbt.put("Pos", this.toNbtList(entity.getX(), entity.getY(), entity.getZ()));
                    }
                    break;
                case "Motion":
                    Vec3d vec3d = entity.getVelocity();
                    nbt.put("Motion", this.toNbtList(vec3d.x, vec3d.y, vec3d.z));
                    break;
                case "Rotation":
                    nbt.put("Rotation", this.toNbtList(entity.getYaw(), entity.getPitch()));
                    break;
                case "FallDistance":
                    nbt.putFloat("FallDistance", entity.fallDistance);
                    break;
                case "Fire":
                    nbt.putShort("Fire", (short) this.fireTicks);
                    break;
                case "Air":
                    nbt.putShort("Air", (short) entity.getAir());
                    break;
                case "OnGround":
                    nbt.putBoolean("OnGround", this.onGround);
                    break;
                case "Invulnerable":
                    nbt.putBoolean("Invulnerable", this.invulnerable);
                    break;
                case "PortalCooldown":
                    nbt.putInt("PortalCooldown", this.portalCooldown);
                    break;
                case "UUID":
                    nbt.putUuid("UUID", entity.getUuid());
                    break;
                case "CustomName":
                    Text text = entity.getCustomName();
                    if (text != null) {
                        nbt.putString("CustomName", Text.Serializer.toJson(text));
                    }
                    break;
                case "CustomNameVisible":
                    if (entity.isCustomNameVisible()) {
                        nbt.putBoolean("CustomNameVisible", entity.isCustomNameVisible());
                    }
                    break;
                case "Silent":
                    if (entity.isSilent()) {
                        nbt.putBoolean("Silent", entity.isSilent());
                    }
                    break;
                case "NoGravity":
                    if (entity.hasNoGravity()) {
                        nbt.putBoolean("NoGravity", entity.hasNoGravity());
                    }
                    break;
                case "Glowing":
                    if (this.glowing) {
                        nbt.putBoolean("Glowing", true);
                    }
                    break;
                case "TicksFrozen":
                    int i = entity.getFrozenTicks();
                    if (i > 0) {
                        nbt.putInt("TicksFrozen", entity.getFrozenTicks());
                    }
                    break;
                case "HasVisualFire":
                    if (this.hasVisualFire) {
                        nbt.putBoolean("HasVisualFire", this.hasVisualFire);
                    }
                    break;
                case "Tags":
                    NbtList nbtList;
                    Iterator var6;
                    if (!this.scoreboardTags.isEmpty()) {
                        nbtList = new NbtList();
                        var6 = this.scoreboardTags.iterator();

                        while (var6.hasNext()) {
                            String string = (String) var6.next();
                            nbtList.add(NbtString.of(string));
                        }

                        nbt.put("Tags", nbtList);
                    }
                    break;
                case "Passengers":
                    if (entity.hasPassengers()) {
                        nbtList = new NbtList();
                        var6 = entity.getPassengerList().iterator();

                        while (var6.hasNext()) {
                            Entity riding_entity = (Entity) var6.next();
                            NbtCompound nbtCompound = new NbtCompound();
                            if (riding_entity.saveSelfNbt(nbtCompound)) {
                                nbtList.add(nbtCompound);
                            }
                        }

                        if (!nbtList.isEmpty()) {
                            nbt.put("Passengers", nbtList);
                        }
                    }
                    break;
                default:
                    if(this.writeCustomDataToNbtFiltered(nbt, path, topLevelNbt))
                        return nbt;
                    else
                        return null;
            }
            return nbt;
        } catch (Throwable var9) {
            CrashReport crashReport = CrashReport.create(var9, "Saving entity NBT");
            CrashReportSection crashReportSection = crashReport.addElement("Entity being saved");
            entity.populateCrashReport(crashReportSection);
            throw new CrashException(crashReport);
        }
    }

}
