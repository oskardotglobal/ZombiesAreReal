package com.patrick.zombiesarereal.ai;

import com.patrick.zombiesarereal.entities.CustomBaseZombie;
import com.patrick.zombiesarereal.helpers.PlayerLocationHelper;
import com.patrick.zombiesarereal.helpers.SoundAlertHelper;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;

public class ZombieAINearestAttackablePlayer extends EntityAINearestAttackableTarget<EntityPlayer>
{
    public ZombieAINearestAttackablePlayer(
            EntityZombie creature, Class<EntityPlayer> classTarget, boolean checkSight
    )
    {
        super(creature, classTarget, 0, checkSight, false, null);
    }

    @Override
    public boolean shouldExecute()
    {
        if (super.shouldExecute())
        {
            if (playerIsSneakingAndNotCloseEnough(targetEntity)) return false;
            if (!PlayerLocationHelper.hasChangePosition(targetEntity)) return false;
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
}
