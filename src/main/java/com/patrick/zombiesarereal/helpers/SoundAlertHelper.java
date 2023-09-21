package com.patrick.zombiesarereal.helpers;

import com.patrick.utils.DebugUtil;
import com.patrick.zombiesarereal.ZombiesAreReal;
import com.patrick.zombiesarereal.ai.ZombieAIInvestigateSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoundAlertHelper
{
    private static final int WHISPER_RANGE  = 16;
    private static final int TALK_RANGE     = 32;
    // TODO: Paths more far than FOLLOW_RANGE needs an stepped pathfinding ai
    private static final int SHOUTING_RANGE = 48;
    private static final int SCREAM_RANGE   = 64;

    private static final int SOUND_COOLDOWN = 2000; // milliseconds

    private static final Map<String, SoundData> soundDataMap = new HashMap<>();

    private static class SoundData
    {
        long lastSoundTime = 0;
    }

    public enum SoundSource
    {
        BLOCK_BROKEN,
        CRISTAL_BROKEN,
        BLOCK_PLACED,
        ENTITY_HURT,
        ENTITY_DEATH,
        ITEM_USED,
        DOOR_TOGGLE,
        EXPLOSION,
        ARROW_SHOOT,
        ARROW_HIT,
        ANIMAL_BREED,
        HORSE_GALLOP,
        ITEM_BREAK,
        ALERT_GROWL,
        JUMP,
        HIT,
        INTERACT,
        WALKING,
        RUNNING,
    }

    /**
     * Returns false if it is too soon to execute again
     */
    public static boolean onSound(Entity sourceEntity, World world, SoundSource soundSource, BlockPos soundPos)
    {
        System.out.println(soundSource);
        System.out.println(soundPos);
        if (world.isRemote) return false;
        if (isTooSoon(soundPos)) return false;

        if (ZombiesAreReal.DEBBUGING) DebugUtil.spawnNoteParticleAtBlockPos(world, soundPos);

        double alertRadius = getRadius(soundSource);

        List<EntityZombie> zombies = world.getEntitiesWithinAABB(EntityZombie.class, new AxisAlignedBB(
                soundPos.add(-alertRadius, -alertRadius, -alertRadius),
                soundPos.add(alertRadius, alertRadius, alertRadius)
        ));

        for (EntityZombie zombie : zombies)
        {
            if (!zombie.equals(sourceEntity))
                alertZombieOfSound(zombie, soundPos);
        }
        return true;
    }

    private static boolean isTooSoon(BlockPos soundPos)
    {
        if (soundDataMap.size() > 1000) soundDataMap.clear();

        String    key  = makeBlockPosString(soundPos);
        SoundData data = soundDataMap.computeIfAbsent(key, k -> new SoundData());

        long currentTime = System.currentTimeMillis();

        boolean isToSoon = currentTime - data.lastSoundTime < SOUND_COOLDOWN;
        if (isToSoon) return true;

        data.lastSoundTime = currentTime;

        return false;
    }

    private static String makeBlockPosString(BlockPos soundPos)
    {
        return soundPos != null
                ? soundPos.getX() + "," + soundPos.getY() + "," + soundPos.getZ()
                : "null";
    }

    private static int getRadius(SoundSource soundSource)
    {
        switch (soundSource)
        {
            case EXPLOSION:
            case ANIMAL_BREED: // TO SIMULATE ZOMBIES SMELLING THE ANIMALS
            case CRISTAL_BROKEN:
            case HORSE_GALLOP:
            case ITEM_BREAK:
            case ALERT_GROWL:
//                return SCREAM_RANGE; // TODO: Paths more far than FOLLOW_RANGE needs an stepped pathfinding ai
            case BLOCK_BROKEN:
            case ENTITY_HURT:
            case RUNNING:
//                return SHOUTING_RANGE; // TODO: Paths more far than FOLLOW_RANGE needs an stepped pathfinding ai
            case BLOCK_PLACED:
            case DOOR_TOGGLE:
            case HIT:
                return TALK_RANGE;
            case WALKING:
            case INTERACT:
            case ITEM_USED:
            case ARROW_SHOOT:
            case ARROW_HIT:
            case JUMP:
            case ENTITY_DEATH:
            default:
                return WHISPER_RANGE;
        }
    }

    private static void alertZombieOfSound(EntityZombie zombie, BlockPos soundPos)
    {
        for (EntityAITasks.EntityAITaskEntry taskEntry : zombie.tasks.taskEntries)
        {
            if (taskEntry != null)
            {
                EntityAIBase task = taskEntry.action;
                if (task instanceof ZombieAIInvestigateSound)
                {
                    ((ZombieAIInvestigateSound) task).setSoundPos(soundPos);
                }
            }
        }
    }

}
