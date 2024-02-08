package com.patrick.zombiesarereal.ai;

import com.patrick.zombiesarereal.entities.CustomBaseZombie;
import com.patrick.zombiesarereal.helpers.SoundAlertHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;

public class ZombieAIWatchClosest extends EntityAIWatchClosest {
    private double lastXPos = 0;
    private double lastZPos = 0;
    private boolean isFirstCheck = true;

    public ZombieAIWatchClosest(CustomBaseZombie entityIn, Class<? extends Entity> watchTargetClass, float maxDistance) {
        super(entityIn, watchTargetClass, maxDistance);
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (!super.shouldExecute()) return false;
        if (playerIsNotMoving()) return false;

        return true;
    }

    private boolean playerIsNotMoving() {
        if (isFirstCheck) {
            lastXPos = closestEntity.posX;
            lastZPos = closestEntity.posZ;
            isFirstCheck = false;
        }
        if (closestEntity.posX == lastXPos && closestEntity.posZ == lastZPos) {
            return true;
        }

        lastXPos = closestEntity.posX;
        lastZPos = closestEntity.posZ;

        return false;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        this.entity.playLivingSound();
        SoundAlertHelper.onSound(
                this.entity,
                this.entity.world,
                SoundAlertHelper.SoundSource.ALERT_GROWL,
                this.entity.getPosition()
        );
    }
}
