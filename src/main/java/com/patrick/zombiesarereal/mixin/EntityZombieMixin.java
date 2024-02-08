package com.patrick.zombiesarereal.mixin;


import com.patrick.zombiesarereal.entities.CustomBaseZombie;
import net.minecraft.entity.monster.EntityZombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityZombie.class)
public abstract class EntityZombieMixin {
    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(CallbackInfo info) {
        ((EntityZombie)(Object)this).setHealth(CustomBaseZombie.MAX_HEALTH);
    }
}
