package com.patrick.zombiesarereal.ai;

import com.patrick.zombiesarereal.entities.CustomBaseZombie;
import com.patrick.zombiesarereal.entities.HordeZombie;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class ZombieAIEasternWander extends EntityAIWander {
    private final Random rand;

    public ZombieAIEasternWander(CustomBaseZombie creature, double speedIn, int chance) {
        super(creature, speedIn, chance);
        setMutexBits(2);
        rand = new Random();
    }

    @Override
    public boolean shouldExecute() {
        if (this.entity.getAttackTarget() != null) {
            return false;
        }

        if (!this.mustUpdate) {
            if (this.entity.getRNG().nextInt(this.executionChance) != 0) {
                return false;
            }
        }

        Vec3d vec3d = this.getPosition();

        if (vec3d == null) {
            return false;
        } else {
            this.x = vec3d.x;
            this.y = vec3d.y;
            this.z = vec3d.z;
            this.mustUpdate = false;
            return true;
        }
    }

    @Override
    public void updateTask() {
        this.entity
                .getLookHelper()
                .setLookPosition(
                        this.entity.posX, this.entity.posY, this.entity.posZ,
                        (float) this.entity.getHorizontalFaceSpeed(),
                        (float) this.entity.getVerticalFaceSpeed()
                );
    }

    @Override
    protected Vec3d getPosition() {
        double randomXMovement = rand.nextDouble() * 6;
        double randomZMovement = rand.nextDouble() * 6;

        if (this.entity instanceof HordeZombie) {
            HordeZombie hordeZombie = (HordeZombie) this.entity;

            BlockPos targetPos = hordeZombie.getHordeTargetPos();
            HordeZombie.Direction targetDirection = hordeZombie.getHordeTargetDirection();

            if (targetPos != null && targetDirection != null) {
                BlockPos currentPos = this.entity.getPosition();

                // Keep moving in the direction of the target if not within 2 chunk
                if (!(currentPos.getDistance(targetPos.getX(), currentPos.getY(), targetPos.getZ()) <= 32)) {
                    switch (targetDirection) {
                        case SOUTHEAST:
                            return new Vec3d(
                                    this.entity.posX + (randomXMovement - 1.5),
                                    this.entity.posY,
                                    this.entity.posZ + (3 + randomZMovement)
                            );

                        case NORTHEAST:
                            return new Vec3d(
                                    this.entity.posX + (randomXMovement - 1.5),
                                    this.entity.posY,
                                    this.entity.posZ - (3 + randomZMovement)
                            );
                    }
                }
            }
        }

        return new Vec3d(
                this.entity.posX + (3 + randomXMovement),
                this.entity.posY,
                this.entity.posZ + (randomZMovement - 3)
        );
    }

}