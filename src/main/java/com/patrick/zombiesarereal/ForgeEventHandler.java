package com.patrick.zombiesarereal;

import com.patrick.zombiesarereal.helpers.CombatHelper;
import com.patrick.zombiesarereal.helpers.KnockbackHelper;
import com.patrick.zombiesarereal.helpers.SoundAlertHelper;
import com.patrick.zombiesarereal.helpers.SpeedHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@EventBusSubscriber
public class ForgeEventHandler
{

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (!event.world.isRemote && event.phase == TickEvent.Phase.START)
        {
            long timeOfDay = event.world.getWorldTime() % 24000;
            if (timeOfDay == 13000 || timeOfDay == 23000)
            {
                ZombieHordeGenerator.generate(event.world);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerAttackEntity(AttackEntityEvent event)
    {
        EntityPlayer player = event.getEntityPlayer();
        Item         heldItem = player.getHeldItemMainhand().getItem();
        if (!CombatHelper.isCoherentWeapon(heldItem) && event.isCancelable())
        {
            event.setCanceled(true);
            Entity target = event.getTarget();
            KnockbackHelper.applyKnockbackToLivingEntity(player, target, false);
        }
    }

    @SubscribeEvent
    public static void onEntityKnockback(LivingKnockBackEvent event)
    {
        if (event.isCancelable()) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPlayerRightClickEntity(PlayerInteractEvent.EntityInteract event)
    {
        EntityPlayer player = event.getEntityPlayer();
        Entity       target = event.getTarget();
        KnockbackHelper.applyKnockbackToLivingEntity(player, target, true);
    }

    @SubscribeEvent
    public static void onPlayerJump(LivingEvent.LivingJumpEvent event)
    {
        if (event.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            SpeedHelper.onPlayerJumped(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerUpdate(LivingEvent.LivingUpdateEvent event)
    {
        if ((event.getEntity() instanceof EntityPlayer))
        {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            SpeedHelper.updatePlayerSpeed(player);
        }
    }

    @SubscribeEvent
    public static void onFOVUpdate(FOVUpdateEvent event)
    {
        // Prevent the fov reduction to bee too annoying when jumping
        event.setNewfov(Math.max(0.95F, event.getFov()));
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event)
    {
        Block brokenBlock = event.getState().getBlock();

        boolean brokenBlockIsCristal = brokenBlock == Blocks.GLASS ||
                brokenBlock == Blocks.STAINED_GLASS ||
                brokenBlock == Blocks.STAINED_GLASS_PANE;
        if (brokenBlockIsCristal)
            SoundAlertHelper.onSound(event.getPlayer(), event.getWorld(),
                    SoundAlertHelper.SoundSource.CRISTAL_BROKEN, event.getPos()
            );
        else
            SoundAlertHelper.onSound(event.getPlayer(), event.getWorld(), SoundAlertHelper.SoundSource.BLOCK_BROKEN,
                    event.getPos()
            );
    }

    @SubscribeEvent
    public static void onEntityPlaceEvent(BlockEvent.EntityPlaceEvent event)
    {
        SoundAlertHelper.onSound(event.getEntity(), event.getWorld(), SoundAlertHelper.SoundSource.BLOCK_PLACED,
                event.getPos()
        );
    }

    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event)
    {
        SoundAlertHelper.onSound(
                event.getEntity(),
                event.getEntity().world,
                SoundAlertHelper.SoundSource.ENTITY_HURT,
                event.getEntity().getPosition()
        );
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event)
    {
        SoundAlertHelper.onSound(
                event.getEntity(),
                event.getEntity().world,
                SoundAlertHelper.SoundSource.ENTITY_DEATH,
                event.getEntity().getPosition()
        );
    }

    @SubscribeEvent
    public static void onItemUse(PlayerInteractEvent.RightClickItem event)
    {
        SoundAlertHelper.onSound(
                event.getEntity(),
                event.getWorld(),
                SoundAlertHelper.SoundSource.ITEM_USED,
                event.getEntity().getPosition()
        );
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        if (event.getWorld().getBlockState(event.getPos()).getBlock() instanceof BlockDoor)
        {
            SoundAlertHelper.onSound(
                    event.getEntity(),
                    event.getWorld(),
                    SoundAlertHelper.SoundSource.DOOR_TOGGLE,
                    event.getPos()
            );
        }
    }

    @SubscribeEvent
    public static void onExplosion(ExplosionEvent event)
    {
        BlockPos pos = new BlockPos(event.getExplosion().getPosition());
        SoundAlertHelper.onSound(null, event.getWorld(), SoundAlertHelper.SoundSource.EXPLOSION, pos);
    }

    @SubscribeEvent
    public static void onArrowShoot(ArrowLooseEvent event)
    {
        SoundAlertHelper.onSound(event.getEntityPlayer(), event.getEntity().world,
                SoundAlertHelper.SoundSource.ARROW_SHOOT,
                event.getEntity().getPosition()
        );
    }

    @SubscribeEvent
    public static void onArrowImpact(ArrowNockEvent event)
    {
        SoundAlertHelper.onSound(
                event.getEntity(), event.getEntity().world, SoundAlertHelper.SoundSource.ARROW_HIT,
                event.getEntity().getPosition()
        );
    }

    @SubscribeEvent
    public static void onMobBreed(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntityAgeable && ((EntityAgeable) event.getEntity()).isChild())
        {
            SoundAlertHelper.onSound(event.getEntity(),
                    event.getWorld(), SoundAlertHelper.SoundSource.ANIMAL_BREED,
                    event.getEntity().getPosition()
            );
        }
    }

    @SubscribeEvent
    public static void onHorseGallop(LivingEvent.LivingUpdateEvent event)
    {
        if (event.getEntity() instanceof EntityHorse && event.getEntity().isBeingRidden())
        {
            SoundAlertHelper.onSound(event.getEntity(), event.getEntity().world,
                    SoundAlertHelper.SoundSource.HORSE_GALLOP,
                    event.getEntity()
                         .getPosition()
            );
        }
    }

    @SubscribeEvent
    public static void onItemBreak(PlayerDestroyItemEvent event)
    {
        SoundAlertHelper.onSound(event.getEntityPlayer(), event.getEntity().world,
                SoundAlertHelper.SoundSource.ITEM_BREAK,
                event.getEntity()
                     .getPosition()
        );
    }

}
