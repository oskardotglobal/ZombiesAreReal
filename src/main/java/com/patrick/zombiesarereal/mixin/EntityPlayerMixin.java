package com.patrick.zombiesarereal.mixin;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SideOnly(Side.CLIENT)
@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin extends EntityLivingBase {
    public EntityPlayerMixin(World worldIn) {
        super(worldIn);
    }

    @Unique
    @Inject(at = @At("TAIL"), method = "getAlwaysRenderNameTagForRender", cancellable = true, remap = false)
    private void zombiesarereal$getAlwaysRenderNameTagForRender(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
