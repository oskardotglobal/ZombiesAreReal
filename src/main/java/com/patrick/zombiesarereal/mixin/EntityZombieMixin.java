package com.patrick.zombiesarereal.mixin;


import com.patrick.zombiesarereal.entities.CustomBaseZombie;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityZombie.class)
public abstract class EntityZombieMixin extends EntityMob {
    public EntityZombieMixin(World worldIn) {
        super(worldIn);
    }

    @Inject(at = @At("TAIL"), method = "<init>", remap = false)
    private void init(CallbackInfo info) {
        this.setHealth(CustomBaseZombie.MAX_HEALTH);
    }
}
