package com.patrick.zombiesarereal.mixin;

import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin {
    @Inject(at = @At("TAIL"), method = "getAlwaysRenderNameTagForRender", cancellable = true)
    private void zombiesarereal$getAlwaysRenderNameTagForRender(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
