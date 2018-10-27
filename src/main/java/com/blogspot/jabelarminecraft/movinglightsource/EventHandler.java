/**
    Copyright (C) 2014 by jabelar

    This file is part of jabelar's Minecraft Forge modding examples; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    For a copy of the GNU General Public License see <http://www.gnu.org/licenses/>.

	If you're interested in licensing the code under different terms you can
	contact the author at julian_abelar@hotmail.com 
*/

package com.blogspot.jabelarminecraft.movinglightsource;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.blogspot.jabelarminecraft.movinglightsource.blocks.BlockMovingLightSource;
import com.blogspot.jabelarminecraft.movinglightsource.tileentities.TileEntityMovingLightSource;
import com.google.common.base.Predicate;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class EventHandler
{
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onEvent(AttackEntityEvent event)
    {
        EntityPlayer thePlayer = event.getEntityPlayer();
        if (thePlayer.world.isRemote)
        {
            return;
        }

        if (thePlayer.getHeldItemMainhand().getItem() == Item.getItemFromBlock(Blocks.TORCH))
        {
            if (MainMod.allowTorchesToBurnEntities)
            {
                event.getTarget().setFire(10);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onEvent(WorldTickEvent event)
    {
        World theWorld = event.world;
        if (event.phase == TickEvent.Phase.START && !theWorld.isRemote)
        {
            processLightPlacementForEntity(theWorld, ENTITY_ITEM_LIGHT_SOURCE);
            processLightPlacementForEntity(theWorld, FLAMING_ENTITY);
            processLightPlacementForEntity(theWorld, ENTITY_HOLDING_LIGHT_SOURCE);
            processLightPlacementForEntity(theWorld, ENTITY_FIREWORK_ROCKET);
            processLightPlacementForEntity(theWorld, ENTITY_SPECTRAL_ARROW);
            processLightPlacementForEntity(theWorld, GLOWING_ENTITY);
        }
    }
    
    private static void processLightPlacementForEntity(World theWorld, Predicate<Entity> thePredicate)
    {
        List<Entity> entityList = theWorld.getEntities(Entity.class, thePredicate);
        Iterator<Entity> iterator = entityList.iterator();
        while(iterator.hasNext())
        {
            Entity theEntity = iterator.next();
            
            // determine entity position
            int blockX = MathHelper.floor(theEntity.posX);
            int blockY = MathHelper.floor(theEntity.posY - 0.2D - theEntity.getYOffset());
            int blockZ = MathHelper.floor(theEntity.posZ);

            // place light where there is space to do so
            BlockPos blockLocation = new BlockPos(blockX, blockY, blockZ).up();
            Block blockAtLocation = theEntity.world.getBlockState(blockLocation).getBlock();

            if (blockAtLocation == Blocks.AIR)
            {
                placeLightSourceBlock(theEntity, blockLocation);
            }
            else if (blockAtLocation instanceof BlockMovingLightSource)
            {
                if (blockAtLocation.getDefaultState().getLightValue() != BlockMovingLightSource.lightBlockToPlace(theEntity).getDefaultState()
                        .getLightValue())
                {
                    placeLightSourceBlock(theEntity, blockLocation);
                }
            }
            else // try one block up
            {
                blockLocation.up();
                blockAtLocation = theEntity.world.getBlockState(blockLocation).getBlock();
                
                if (blockAtLocation == Blocks.AIR)
                {
                    placeLightSourceBlock(theEntity, blockLocation);
                }
                else if (blockAtLocation instanceof BlockMovingLightSource)
                {
                    if (blockAtLocation.getDefaultState().getLightValue() != BlockMovingLightSource.lightBlockToPlace(theEntity).getDefaultState()
                            .getLightValue())
                    {
                        placeLightSourceBlock(theEntity, blockLocation);
                    }
                }
            }
        }
    }
    
    
    private static void placeLightSourceBlock(Entity theEntity, BlockPos blockLocation)
    {
        theEntity.world.setBlockState(
                blockLocation,
                BlockMovingLightSource.lightBlockToPlace(theEntity).getDefaultState());
        TileEntity theTileEntity = theEntity.world.getTileEntity(blockLocation);
        if (theTileEntity instanceof TileEntityMovingLightSource)
        {
            TileEntityMovingLightSource theTileEntityMovingLightSource = (TileEntityMovingLightSource) theTileEntity;
            theTileEntityMovingLightSource.setEntity(theEntity);
        }
    }
    
    public static final Predicate<Entity> ENTITY_FIREWORK_ROCKET = new Predicate<Entity>()
    {
        @Override
        public boolean apply(@Nullable Entity theEntity)
        {
            return (theEntity instanceof EntityFireworkRocket);
        }
    };

    
    public static final Predicate<Entity> ENTITY_SPECTRAL_ARROW = new Predicate<Entity>()
    {
        @Override
        public boolean apply(@Nullable Entity theEntity)
        {
            return (theEntity instanceof EntitySpectralArrow);
        }
    };
    
    public static final Predicate<Entity> ENTITY_HOLDING_LIGHT_SOURCE = new Predicate<Entity>()
    {
        @Override
        public boolean apply(@Nullable Entity theEntity)
        {
            if (theEntity instanceof EntityLivingBase)
            {
                EntityLivingBase theEntityLiving = (EntityLivingBase)theEntity;
                return BlockMovingLightSource.isHoldingLightItem(theEntityLiving);
            }
            else
            {
                return false;
            }
        }
    };
            
    /** Selects entities which are either not players or players that are not spectating */
    public static final Predicate<Entity> ENTITY_ITEM_LIGHT_SOURCE = new Predicate<Entity>()
    {
        @Override
        public boolean apply(@Nullable Entity theEntity)
        {
            if (theEntity instanceof EntityItem)
            {
                EntityItem theEntityItem = (EntityItem)theEntity;
                return  (BlockMovingLightSource.isLightItem(theEntityItem.getItem().getItem()) && MainMod.allowHeldItemsToGiveOffLight);
            }
            else
            {
                return false;
            }
        }
    };
    
    public static final Predicate<Entity> FLAMING_ENTITY = new Predicate<Entity>()
    {
        @Override
        public boolean apply(@Nullable Entity theEntity)
        {
            return  (theEntity.isBurning() && MainMod.allowBurningEntitiesToGiveOffLight);
        }
    };
    
    public static final Predicate<Entity> GLOWING_ENTITY = new Predicate<Entity>()
    {
        @Override
        public boolean apply(@Nullable Entity theEntity)
        {
            if (theEntity instanceof EntityLivingBase)
            {
                EntityLivingBase theEntityLiving = (EntityLivingBase)theEntity;
                return (theEntityLiving.isPotionActive(MobEffects.GLOWING));
            }
            else
            {
                return false;
            }
        }
    };

    
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onEvent(OnConfigChangedEvent eventArgs)
    {
        if (eventArgs.getModID().equals(MainMod.MODID))
        {
            MainMod.config.save();
            MainMod.proxy.syncConfig();
        }
    }
}
