package com.patrick.zombiesarereal.ai;

import com.patrick.zombiesarereal.entities.CustomBaseZombie;
import com.patrick.zombiesarereal.helpers.SoundAlertHelper;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;

public class ZombieAINearestAttackablePlayer extends EntityAINearestAttackableTarget<EntityPlayer>
{
    private double  lastXPos     = 0;
    private double  lastZPos     = 0;
    private boolean isFirstCheck = true;

    public ZombieAINearestAttackablePlayer(
            EntityZombie creature, Class<EntityPlayer> classTarget,
            boolean checkSight)
    {
        super(creature, classTarget, checkSight);
    }

    @Override
    public boolean shouldExecute()
    {
        if (super.shouldExecute())
        {
            EntityPlayer nearestPlayer = this.taskOwner.world.getNearestPlayerNotCreative(
                    this.taskOwner,
                    CustomBaseZombie.VISION_RANGE
            );
            if (nearestPlayer == null) return false;
            if (playerIsSneakingAndNotCloseEnough(nearestPlayer)) return false;
            if (playerIsNotMoving(nearestPlayer)) return false;
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        if (super.shouldContinueExecuting())
        {
            if (this.taskOwner.getRNG().nextInt(40) == 1)
            {
                this.taskOwner.playLivingSound();
                SoundAlertHelper.onSound(
                        this.taskOwner,
                        this.taskOwner.world,
                        SoundAlertHelper.SoundSource.ALERT_GROWL,
                        this.taskOwner.getPosition()
                );
            }
            return true;
        }
        return false;
    }

    @Override
    public void startExecuting()
    {
        super.startExecuting();
        this.taskOwner.playLivingSound();
        SoundAlertHelper.onSound(
                this.taskOwner,
                this.taskOwner.world,
                SoundAlertHelper.SoundSource.ALERT_GROWL,
                this.taskOwner.getPosition()
        );
    }

    private boolean playerIsSneakingAndNotCloseEnough(EntityPlayer player)
    {
        return player.isSneaking() && this.taskOwner.getDistance(player) > CustomBaseZombie.TARGET_RANGE;
    }

    private boolean playerIsNotMoving(EntityPlayer player)
    {
        if (isFirstCheck)
        {
            lastXPos     = player.posX;
            lastZPos     = player.posZ;
            isFirstCheck = false;
        }
        if (player.posX == lastXPos && player.posZ == lastZPos)
        {
            return true;
        }

        lastXPos = player.posX;
        lastZPos = player.posZ;

        return false;
    }
}
