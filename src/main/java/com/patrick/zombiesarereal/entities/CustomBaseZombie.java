package com.patrick.zombiesarereal.entities;

import com.patrick.zombiesarereal.ai.ZombieAIEasternWander;
import com.patrick.zombiesarereal.ai.ZombieAIInvestigateSound;
import com.patrick.zombiesarereal.ai.ZombieAINearestAttackablePlayer;
import com.patrick.zombiesarereal.ai.ZombieAIWatchClosest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIZombieAttack;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.UUID;

public class CustomBaseZombie extends EntityZombie
{
    public static final  int    FOLLOW_RANGE                   = 32;
    public static final  int    VISION_RANGE                   = 16;
    public static final  int    TARGET_RANGE                   = 3;
    public static final  double MAX_SPEED                      = 0.40D;
    public static final  double MAX_ATTACK_DAMAGE              = 20.0F;
    public static final  double PASSIVE_SPEED_MULTIPLIER       = 0.4D;
    public static final  double INVESTIGATION_SPEED_MULTIPLIER = 0.8D;
    public static final  float  MAX_HEALTH                     = 10.0F;
    private static final float  MAX_EXTREMITY_HEALTH           = 4.0F;
    private static final UUID   ARMS_DAMAGE_MODIFIER_UUID      = UUID.fromString(
            "c3c2d6f8-7c16-4c37-a767-22e7abc12345"
    );
    private static final UUID   LEGS_SPEED_MODIFIER_UUID       = UUID.fromString(
            "d7c2c8c8-2f6a-4b69-8804-1f4f61212345"
    );

    private float headHealth     = MAX_EXTREMITY_HEALTH;
    private float leftArmHealth  = calcRandomExtremityHealth();
    private float rightArmHealth = calcRandomExtremityHealth();
    private float leftLegHealth  = calcRandomExtremityHealth();
    private float rightLegHealth = calcRandomExtremityHealth();

    public CustomBaseZombie(World worldIn)
    {
        super(worldIn);
        this.enablePersistence();
        this.setHealth(MAX_HEALTH);
        recalcSpeed();
        recalcAttackDamage();
        // TODO: check if must amputate it
    }

    @Override
    public boolean isArmsRaised()
    {
        return false;
    }

    @Override
    protected void initEntityAI()
    {

        this.targetTasks.addTask(1, new ZombieAINearestAttackablePlayer(this, EntityPlayer.class, true));
        this.tasks.addTask(2, new EntityAIZombieAttack(this, 1.0D, false));
        this.tasks.addTask(3, new ZombieAIInvestigateSound(this, INVESTIGATION_SPEED_MULTIPLIER));
        this.tasks.addTask(6, new ZombieAIEasternWander(this, PASSIVE_SPEED_MULTIPLIER, 1));
        this.tasks.addTask(8, new ZombieAIWatchClosest(this, EntityPlayer.class, VISION_RANGE));
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(FOLLOW_RANGE);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(MAX_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MAX_ATTACK_DAMAGE);
    }

    @Override
    protected boolean shouldBurnInDay()
    {
        return false;
    }

    @Override
    protected ResourceLocation getLootTable()
    {
        return null;
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn)
    {
        if (this.getDistance(entityIn) < 1.2)
        {
            return super.attackEntityAsMob(entityIn);
        }
        return false;
    }

    public void reduceHeadHealth(float health)
    {
        this.headHealth = Math.max(0, this.headHealth - health);
        if (this.headHealth == 0)
        {
            this.setHealth(0.0F);
            // TODO: amputate it
        }
    }

    public void reduceLeftArmHealth(float health)
    {
        this.leftArmHealth = Math.max(0, this.leftArmHealth - health);
        recalcAttackDamage();
        if (this.leftArmHealth == 0)
        {
            // TODO: amputate it
        }
    }

    public void reduceRightArmHealth(float health)
    {
        this.rightArmHealth = Math.max(0, this.rightArmHealth - health);
        recalcAttackDamage();
        if (this.rightArmHealth == 0)
        {
            // TODO: amputate it
        }
    }

    public void reduceLeftLegHealth(float health)
    {
        this.leftLegHealth = Math.max(0, this.leftLegHealth - health);
        recalcSpeed();
        if (this.leftLegHealth == 0)
        {
            // TODO: amputate it
        }
    }

    public void reduceRightLegHealth(float health)
    {
        this.rightLegHealth = Math.max(0, this.rightLegHealth - health);
        recalcSpeed();
        if (this.rightLegHealth == 0)
        {
            // TODO: amputate it
        }
    }

    private void recalcSpeed()
    {
        IAttributeInstance movementAttribute = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        AttributeModifier previousModifier = movementAttribute.getModifier(LEGS_SPEED_MODIFIER_UUID);
        if (previousModifier != null)
        {
            movementAttribute.removeModifier(previousModifier);
        }

        float legsHealth    = leftLegHealth + rightLegHealth;
        float maxLegsHealth = MAX_EXTREMITY_HEALTH + MAX_EXTREMITY_HEALTH;

        // Calculate the speed modifier based on legsHealth.
        // If legsHealth is 0, speed should be at 15% of its base value.
        // Otherwise, it scales linearly with legsHealth.
        float speedModifierAmount = (legsHealth / maxLegsHealth) * 0.85F - 0.85F;

        AttributeModifier legsSpeedModifier = new AttributeModifier(
                LEGS_SPEED_MODIFIER_UUID,
                "Legs speed modifier",
                speedModifierAmount,
                1
        );
        movementAttribute.applyModifier(legsSpeedModifier);
    }

    private void recalcAttackDamage()
    {
        IAttributeInstance attackAttribute = this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);

        AttributeModifier previousModifier = attackAttribute.getModifier(ARMS_DAMAGE_MODIFIER_UUID);
        if (previousModifier != null)
        {
            attackAttribute.removeModifier(previousModifier);
        }

        float armsHealth           = leftArmHealth + rightArmHealth;
        float maxArmsHealth        = MAX_EXTREMITY_HEALTH + MAX_EXTREMITY_HEALTH;
        float damageModifierAmount = (armsHealth / maxArmsHealth) - 1.0F;

        AttributeModifier armsDamageModifier = new AttributeModifier(
                ARMS_DAMAGE_MODIFIER_UUID,
                "Arms damage modifier",
                damageModifierAmount,
                1
        );
        attackAttribute.applyModifier(armsDamageModifier);
    }

    private float calcRandomExtremityHealth()
    {
        return (float) (0 + (MAX_EXTREMITY_HEALTH - 0) * rand.nextDouble());
    }
}
