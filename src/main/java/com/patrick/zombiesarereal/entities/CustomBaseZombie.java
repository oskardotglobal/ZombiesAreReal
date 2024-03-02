package com.patrick.zombiesarereal.entities;

import com.flemmli97.improvedmobs.entity.ai.EntityAIBlockBreaking;
import com.flemmli97.improvedmobs.entity.ai.EntityAIClimbLadder;
import com.p1ut0nium.roughmobsrevamped.ai.combat.RoughAILeapAtTargetChanced;
import com.patrick.zombiesarereal.ai.ZombieAIEasternWander;
import com.patrick.zombiesarereal.ai.ZombieAIInvestigateSound;
import com.patrick.zombiesarereal.ai.ZombieAINearestAttackablePlayer;
import com.patrick.zombiesarereal.ai.ZombieAIWatchClosest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIZombieAttack;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class CustomBaseZombie extends EntityZombie {
    public static final int FOLLOW_RANGE = 32;
    public static final int VISION_RANGE = 16;
    public static final int TARGET_RANGE = 8;
    public static final int LEAP_CHANCE = 5;
    public static final double MAX_SPEED = 0.40D;
    public static final double MAX_ATTACK_DAMAGE = 20.0F;
    public static final double PASSIVE_SPEED_MULTIPLIER = 0.4D;
    public static final double INVESTIGATION_SPEED_MULTIPLIER = 0.8D;
    public static final float MAX_HEALTH = 40.0F;
    public static final float LEAP_HEIGHT = 0.2F;

    public CustomBaseZombie(World worldIn) {
        super(worldIn);
        this.enablePersistence();

        this.setHealth(MAX_HEALTH);
        this.setChild(false);
    }

    @Override
    public boolean isArmsRaised() {
        return false;
    }

    @Override
    protected void initEntityAI() {
        this.targetTasks.addTask(1, new ZombieAINearestAttackablePlayer(this, EntityPlayer.class, true));

        this.tasks.addTask(1, new RoughAILeapAtTargetChanced(this, LEAP_HEIGHT, LEAP_CHANCE));
        this.tasks.addTask(1, new EntityAIClimbLadder(this));
        this.tasks.addTask(1, new EntityAIBlockBreaking(this));
        this.tasks.addTask(2, new EntityAIZombieAttack(this, 1.0D, false));
        this.tasks.addTask(3, new ZombieAIInvestigateSound(this, INVESTIGATION_SPEED_MULTIPLIER));
        this.tasks.addTask(6, new ZombieAIEasternWander(this, PASSIVE_SPEED_MULTIPLIER, 1));
        this.tasks.addTask(8, new ZombieAIWatchClosest(this, EntityPlayer.class, VISION_RANGE));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(FOLLOW_RANGE);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(MAX_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MAX_ATTACK_DAMAGE);
    }

    @Override
    protected boolean shouldBurnInDay() {
        return false;
    }

    @Override
    protected ResourceLocation getLootTable() {
        return null;
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getDistance(entityIn) < 1.2) {
            return super.attackEntityAsMob(entityIn);
        }
        return false;
    }
}
