package com.example.examplemod;

import com.example.examplemod.ai.EntityAILookDown;
import com.example.examplemod.ai.EntityAIEasternWander;
import com.example.examplemod.entities.TerrainZombie;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Iterator;

@Mod.EventBusSubscriber
public class ZombieSpawnHandler
{
    public static final  double T               = 0.5;
    private static final int    MIN_DISTANCE    = 97;
    public static final  double MIN_SQ_DISTANCE = MIN_DISTANCE * MIN_DISTANCE;
    public static final  int    FOLLOW_RANGE    = 40;
    public static final  double PASSIVE_SPEED   = 0.4D;
    public static final  double ACTIVE_SPEED    = 0.37D;

    @SubscribeEvent()
    public static void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntityZombie)
        {
            EntityZombie zombie = (EntityZombie) event.getEntity();

            if (zombie.isChild())
            {
                event.setCanceled(true);
                return;
            }

            if (zombie.isRiding())
            {
                event.setCanceled(true);
                return;
            }

            // TODO: Test it
            zombie.enablePersistence();
            zombie.setHealth(4.0F);
            zombie.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(FOLLOW_RANGE);
            zombie.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0);
            zombie.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(ACTIVE_SPEED);

            Iterator<EntityAITasks.EntityAITaskEntry> it = zombie.tasks.taskEntries.iterator();
            while (it.hasNext())
            {
                EntityAIBase ai = it.next().action;
                if (ai instanceof EntityAISwimming ||
                        ai instanceof EntityAIWander ||
                        ai instanceof EntityAIMoveThroughVillage ||
                        ai instanceof EntityAILookIdle ||
                        ai instanceof EntityAIWatchClosest
                )
                {
                    it.remove();
                }
            }

            zombie.tasks.addTask(7, new EntityAIWatchClosest(zombie, EntityPlayer.class, FOLLOW_RANGE));
            zombie.tasks.addTask(10, new EntityAIWanderAvoidWater(zombie, PASSIVE_SPEED));
            zombie.tasks.addTask(4, new EntityAILookDown(zombie));
            zombie.tasks.addTask(6, new EntityAIEasternWander(zombie, PASSIVE_SPEED, 1));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingSpawn(LivingSpawnEvent.CheckSpawn event)
    {
        if (event.getEntity() instanceof EntityZombie && !(event.getEntity() instanceof TerrainZombie))
        {
            // TODO: Uncomment
            /*if (event.getY() >= 26)
            {
                event.setResult(Event.Result.DENY);
                return;
            }*/

            long time = event.getWorld().getWorldTime() % 24000;
            if (!((time >= 22000 && time <= 23000) || (time >= 14000 && time <= 15000)))
            {
                event.setResult(Event.Result.DENY);
                return;
            }

            EntityZombie zombie = (EntityZombie) event.getEntity();

            for (ItemStack itemStack : zombie.getEquipmentAndArmor())
            {
                if (!itemStack.isEmpty())
                {
                    event.setResult(Event.Result.DENY);
                    return;
                }
            }

            BlockPos    eventPos        = new BlockPos(event.getX(), event.getY(), event.getZ());
            IBlockState eventBlockState = event.getWorld().getBlockState(eventPos.down());
            if (!isAllowedBlock(eventBlockState))
            {
                event.setResult(Event.Result.DENY);
                return;
            }

            // TODO: Test it
            if (!event.getWorld().canSeeSky(eventPos))
            {
                event.setResult(Event.Result.DENY);
                return;
            }

            EntityPlayer closestPlayer = event.getWorld().getClosestPlayerToEntity(event.getEntity(), 256);
            if (closestPlayer != null)
            {
                BlockPos closestPlayerPos = closestPlayer.getPosition();
                int      eventX           = eventPos.getX();
                int      playerX          = closestPlayerPos.getX();
                boolean  isAtTheWest                = eventX > playerX;
                if (isAtTheWest)
                {
                    event.setResult(Event.Result.DENY);
                    return;
                }
                boolean tooFarInZ = Math.abs(closestPlayerPos.getZ() - eventPos.getZ()) > 16;
                double squaredDistance = eventPos.distanceSq(closestPlayerPos);
                if (squaredDistance < MIN_SQ_DISTANCE  || tooFarInZ)
                {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityUpdate(LivingEvent.LivingUpdateEvent event)
    {
        if (event.getEntity() instanceof EntityZombie)
        {
            World    currentWorld   = event.getEntity().world;
            boolean  isDaytime      = currentWorld.isDaytime();
            BlockPos entityPosition = event.getEntity().getPosition();
            applySunProtection(currentWorld, entityPosition, isDaytime, event);
        }
    }

    private static boolean isAllowedBlock(IBlockState state)
    {
        Block block = state.getBlock();
        return block == Blocks.GRASS || block == Blocks.DIRT || block == Blocks.SAND || block == Blocks.GRAVEL;
    }

    private static void applySunProtection(
            World world, BlockPos blockpos, boolean isDaytime, LivingEvent.LivingUpdateEvent event)
    {
        boolean isLit = world.getLight(blockpos) > 0;
        if (isLit && isDaytime && event.getEntity().isBurning() && !event.getEntity().isInWater())
        {
            event.getEntity().extinguish();
        }
    }

}
