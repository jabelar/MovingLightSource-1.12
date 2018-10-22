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
import net.minecraft.init.Blocks;
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
//    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
//    public void onEvent(LivingUpdateEvent event)
//    {
//        Entity theEntity = event.getEntityLiving();
//
//        if (!theEntity.world.isRemote)
//        {
//            // check if entity is on fire
//            if (theEntity.isBurning() && MainMod.allowBurningEntitiesToGiveOffLight)
//            {
//                // // DEBUG
//                // System.out.println("Entity is burning");
//
//                // determine player position
//                int blockX = MathHelper.floor(theEntity.posX);
//                int blockY = MathHelper.floor(theEntity.posY - 0.2D - event.getEntityLiving().getYOffset());
//                int blockZ = MathHelper.floor(theEntity.posZ);
//
//                // place light where there is space to do so
//                BlockPos blockLocation = new BlockPos(blockX, blockY, blockZ).up();
//                Block blockAtLocation = theEntity.world.getBlockState(blockLocation).getBlock();
//                // // DEBUG
//                // System.out.println("Block at player position is "+event.player.world.getBlockState(blockLocation).getBlock());
//                if (blockAtLocation == Blocks.AIR)
//                {
//                    // // DEBUG
//                    // System.out.println("There is space at player location "+blockLocation+" to place block");
//
//                    // there is space to create moving light source block
//                    theEntity.world.setBlockState(
//                            blockLocation,
//                            BlockRegistry.movinglightsource_15.getDefaultState());
//                }
//            }
//        }
//    }

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

//    @SuppressWarnings("deprecation")
//    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
//    public void onEvent(PlayerTickEvent event)
//    {
//        if (event.phase == TickEvent.Phase.START && !event.player.world.isRemote)
//        {
//            // check if player is holding a light source
//            if (BlockMovingLightSource.isHoldingLightItem(event.player) && MainMod.allowHeldItemsToGiveOffLight)
//            {
////                 // DEBUG
////                 System.out.println("Holding light-emitting item");
//
//                // determine player position
//                int blockX = MathHelper.floor(event.player.posX);
//                int blockY = MathHelper.floor(event.player.posY - 0.2D - event.player.getYOffset());
//                int blockZ = MathHelper.floor(event.player.posZ);
//
//                // place light where there is space to do so
//                BlockPos blockLocation = new BlockPos(blockX, blockY, blockZ).up();
//                Block blockAtLocation = event.player.world.getBlockState(blockLocation).getBlock();
//                // // DEBUG
//                // System.out.println("Block at player position is "+event.player.world.getBlockState(blockLocation).getBlock());
//                if (blockAtLocation == Blocks.AIR)
//                {
//                    // // DEBUG
//                    // System.out.println("There is space at player location "+blockLocation+" to place block");
//
//                    // there is space to create moving light source block
//                    event.player.world.setBlockState(
//                            blockLocation,
//                            BlockMovingLightSource.lightBlockToPlace(event.player).getDefaultState());
//                    TileEntity theTileEntity = event.player.world.getTileEntity(blockLocation);
//                    if (theTileEntity instanceof TileEntityMovingLightSource)
//                    {
//                        TileEntityMovingLightSource theTileEntityMovingLightSource = (TileEntityMovingLightSource) theTileEntity;
//                        theTileEntityMovingLightSource.setEntity(event.player);
//                        
////                        // DEBUG
////                        System.out.println("Placing "+theTileEntityMovingLightSource+" placed by "+theTileEntityMovingLightSource.getEntity());
//                    }
//                }
//                else if (blockAtLocation instanceof BlockMovingLightSource)
//                {
//                    // // DEBUG
//                    // System.out.println("There is already a BlockMovingLight at player location "+blockLocation);
//                    // check if light value at location should change (due to change in held item)
//                    if (blockAtLocation.getDefaultState().getLightValue() != BlockMovingLightSource.lightBlockToPlace(event.player).getDefaultState()
//                            .getLightValue())
//                    {
//                        event.player.world.setBlockState(
//                                blockLocation,
//                                BlockMovingLightSource.lightBlockToPlace(event.player).getDefaultState());
//                        TileEntity theTileEntity = event.player.world.getTileEntity(blockLocation);
//                        if (theTileEntity instanceof TileEntityMovingLightSource)
//                        {
//                            TileEntityMovingLightSource theTileEntityMovingLightSource = (TileEntityMovingLightSource) theTileEntity;
//                            theTileEntityMovingLightSource.setEntity(event.player);
//                            
////                            // DEBUG
////                            System.out.println("Placing "+theTileEntityMovingLightSource+" placed by "+theTileEntityMovingLightSource.getEntity());
//                        }
//                    }
//                }
//            }
//        }
//    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onEvent(WorldTickEvent event)
    {
        World theWorld = event.world;
        if (event.phase == TickEvent.Phase.START && !theWorld.isRemote)
        {
            processEntityItems(theWorld);
            processBurningEntities(theWorld);
            processHeldItems(theWorld);
            processFireworks(theWorld);
        }
    }
    
    private void processHeldItems(World theWorld)
    {
        List<EntityLivingBase> entityList = theWorld.getEntities(EntityLivingBase.class, ENTITY_HOLDING_LIGHT_SOURCE);
        Iterator<EntityLivingBase> iterator = entityList.iterator();
        while(iterator.hasNext())
        {
            EntityLivingBase theEntityLiving = iterator.next();
            if (MainMod.allowHeldItemsToGiveOffLight)
            {
//                 // DEBUG
//                 System.out.println("Holding light-emitting item");

                // determine player position
                int blockX = MathHelper.floor(theEntityLiving.posX);
                int blockY = MathHelper.floor(theEntityLiving.posY - 0.2D - theEntityLiving.getYOffset());
                int blockZ = MathHelper.floor(theEntityLiving.posZ);

                // place light where there is space to do so
                BlockPos blockLocation = new BlockPos(blockX, blockY, blockZ).up();
                Block blockAtLocation = theEntityLiving.world.getBlockState(blockLocation).getBlock();
                // // DEBUG
                // System.out.println("Block at player position is "+theEntityLiving.world.getBlockState(blockLocation).getBlock());
                if (blockAtLocation == Blocks.AIR)
                {
                    // // DEBUG
                    // System.out.println("There is space at player location "+blockLocation+" to place block");

                    // there is space to create moving light source block
                    theEntityLiving.world.setBlockState(
                            blockLocation,
                            BlockMovingLightSource.lightBlockToPlace(theEntityLiving).getDefaultState());
                    TileEntity theTileEntity = theEntityLiving.world.getTileEntity(blockLocation);
                    if (theTileEntity instanceof TileEntityMovingLightSource)
                    {
                        TileEntityMovingLightSource theTileEntityMovingLightSource = (TileEntityMovingLightSource) theTileEntity;
                        theTileEntityMovingLightSource.setEntity(theEntityLiving);
                        
//                        // DEBUG
//                        System.out.println("Placing "+theTileEntityMovingLightSource+" placed by "+theTileEntityMovingLightSource.getEntity());
                    }
                }
                else if (blockAtLocation instanceof BlockMovingLightSource)
                {
                    // // DEBUG
                    // System.out.println("There is already a BlockMovingLight at player location "+blockLocation);
                    // check if light value at location should change (due to change in held item)
                    if (blockAtLocation.getDefaultState().getLightValue() != BlockMovingLightSource.lightBlockToPlace(theEntityLiving).getDefaultState()
                            .getLightValue())
                    {
                        theEntityLiving.world.setBlockState(
                                blockLocation,
                                BlockMovingLightSource.lightBlockToPlace(theEntityLiving).getDefaultState());
                        TileEntity theTileEntity = theEntityLiving.world.getTileEntity(blockLocation);
                        if (theTileEntity instanceof TileEntityMovingLightSource)
                        {
                            TileEntityMovingLightSource theTileEntityMovingLightSource = (TileEntityMovingLightSource) theTileEntity;
                            theTileEntityMovingLightSource.setEntity(theEntityLiving);
                            
//                            // DEBUG
//                            System.out.println("Placing "+theTileEntityMovingLightSource+" placed by "+theTileEntityMovingLightSource.getEntity());
                        }
                    }
                }
            }
        }
    }
    
    private void processEntityItems(World theWorld)
    {
        List<EntityItem> entityList = theWorld.getEntities(EntityItem.class, ENTITY_ITEM_LIGHT_SOURCE);
        Iterator<EntityItem> iterator = entityList.iterator();
        while(iterator.hasNext())
        {
            EntityItem theEntityItem = iterator.next();
            
            // determine entity position
            int blockX = MathHelper.floor(theEntityItem.posX);
            int blockY = MathHelper.floor(theEntityItem.posY - 0.2D - theEntityItem.getYOffset());
            int blockZ = MathHelper.floor(theEntityItem.posZ);

            // place light where there is space to do so
            BlockPos blockLocation = new BlockPos(blockX, blockY, blockZ).up(2);
            Block blockAtLocation = theEntityItem.world.getBlockState(blockLocation).getBlock();
            // // DEBUG
            // System.out.println("Block at player position is "+theEntityItem.world.getBlockState(blockLocation).getBlock());
            if (blockAtLocation == Blocks.AIR)
            {
                // // DEBUG
                // System.out.println("There is space at player location "+blockLocation+" to place block");

                // there is space to create moving light source block
                theEntityItem.world.setBlockState(
                        blockLocation,
                        BlockMovingLightSource.lightBlockToPlace(theEntityItem).getDefaultState());
                TileEntity theTileEntity = theEntityItem.world.getTileEntity(blockLocation);
                if (theTileEntity instanceof TileEntityMovingLightSource)
                {
                    TileEntityMovingLightSource theTileEntityMovingLightSource = (TileEntityMovingLightSource) theTileEntity;
                    theTileEntityMovingLightSource.setEntity(theEntityItem);
                }
            }
            else if (blockAtLocation instanceof BlockMovingLightSource)
            {
                // // DEBUG
                // System.out.println("There is already a BlockMovingLight at player location "+blockLocation);
                // check if light value at location should change (due to change in held item)
                if (blockAtLocation.getDefaultState().getLightValue() != BlockMovingLightSource.lightBlockToPlace(theEntityItem).getDefaultState()
                        .getLightValue())
                {
                    theEntityItem.world.setBlockState(
                            blockLocation,
                            BlockMovingLightSource.lightBlockToPlace(theEntityItem).getDefaultState());
                    TileEntity theTileEntity = theEntityItem.world.getTileEntity(blockLocation);
                    if (theTileEntity instanceof TileEntityMovingLightSource)
                    {
                        TileEntityMovingLightSource theTileEntityMovingLightSource = (TileEntityMovingLightSource) theTileEntity;
                        theTileEntityMovingLightSource.setEntity(theEntityItem);
                    }
                }
            }
        }
    }
    
    private void processBurningEntities(World theWorld)
    {
        List<Entity> entityList = theWorld.getEntities(Entity.class, FLAMING_ENTITY);
        Iterator<Entity> iterator = entityList.iterator();
        while(iterator.hasNext())
        {
            Entity theEntity = iterator.next();
            
            // determine entity position
            int blockX = MathHelper.floor(theEntity.posX);
            int blockY = MathHelper.floor(theEntity.posY - 0.2D - theEntity.getYOffset());
            int blockZ = MathHelper.floor(theEntity.posZ);

            // place light where there is space to do so
            BlockPos blockLocation = new BlockPos(blockX, blockY, blockZ).up(1);
            Block blockAtLocation = theEntity.world.getBlockState(blockLocation).getBlock();

            // // DEBUG
            // System.out.println("Block at player position is "+theEntityItem.world.getBlockState(blockLocation).getBlock());
            if (blockAtLocation == Blocks.AIR)
            {
                // // DEBUG
                // System.out.println("There is space at player location "+blockLocation+" to place block");

                // there is space to create moving light source block
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
            else if (blockAtLocation instanceof BlockMovingLightSource)
            {
                // // DEBUG
                // System.out.println("There is already a BlockMovingLight at player location "+blockLocation);
                // check if light value at location should change (due to change in held item)
                if (blockAtLocation.getDefaultState().getLightValue() != BlockMovingLightSource.lightBlockToPlace(theEntity).getDefaultState()
                        .getLightValue())
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
            }
            else // try one block up
            {
                blockLocation.up();
                blockAtLocation = theEntity.world.getBlockState(blockLocation).getBlock();
                
                // // DEBUG
                // System.out.println("Block at player position is "+theEntityItem.world.getBlockState(blockLocation).getBlock());
                if (blockAtLocation == Blocks.AIR)
                {
                    // // DEBUG
                    // System.out.println("There is space at player location "+blockLocation+" to place block");

                    // there is space to create moving light source block
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
                else if (blockAtLocation instanceof BlockMovingLightSource)
                {
                    // // DEBUG
                    // System.out.println("There is already a BlockMovingLight at player location "+blockLocation);
                    // check if light value at location should change (due to change in held item)
                    if (blockAtLocation.getDefaultState().getLightValue() != BlockMovingLightSource.lightBlockToPlace(theEntity).getDefaultState()
                            .getLightValue())
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
                }
            }
        }
    }
    private void processFireworks(World theWorld)
    {
        List<Entity> entityList = theWorld.getEntities(EntityFireworkRocket.class, ENTITY_FIREWORK_ROCKET);
        Iterator<Entity> iterator = entityList.iterator();
        while(iterator.hasNext())
        {
            Entity theEntity = iterator.next();
//            // DEBUG
//            System.out.println("Found EntityFireworkRocket at position"+theEntity.getPosition());
           
            // determine entity position
            int blockX = MathHelper.floor(theEntity.posX);
            int blockY = MathHelper.floor(theEntity.posY - 0.2D - theEntity.getYOffset());
            int blockZ = MathHelper.floor(theEntity.posZ);

            // place light where there is space to do so
            BlockPos blockLocation = new BlockPos(blockX, blockY, blockZ).up(1);
            Block blockAtLocation = theEntity.world.getBlockState(blockLocation).getBlock();

            // // DEBUG
            // System.out.println("Block at player position is "+theEntityItem.world.getBlockState(blockLocation).getBlock());
            if (blockAtLocation == Blocks.AIR)
            {
                // // DEBUG
                // System.out.println("There is space at player location "+blockLocation+" to place block");

                // there is space to create moving light source block
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
            else if (blockAtLocation instanceof BlockMovingLightSource)
            {
                // // DEBUG
                // System.out.println("There is already a BlockMovingLight at player location "+blockLocation);
                // check if light value at location should change (due to change in held item)
                if (blockAtLocation.getDefaultState().getLightValue() != BlockMovingLightSource.lightBlockToPlace(theEntity).getDefaultState()
                        .getLightValue())
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
            }
            else // try one block up
            {
                blockLocation.up();
                blockAtLocation = theEntity.world.getBlockState(blockLocation).getBlock();
                
                // // DEBUG
                // System.out.println("Block at player position is "+theEntityItem.world.getBlockState(blockLocation).getBlock());
                if (blockAtLocation == Blocks.AIR)
                {
                    // // DEBUG
                    // System.out.println("There is space at player location "+blockLocation+" to place block");

                    // there is space to create moving light source block
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
                else if (blockAtLocation instanceof BlockMovingLightSource)
                {
                    // // DEBUG
                    // System.out.println("There is already a BlockMovingLight at player location "+blockLocation);
                    // check if light value at location should change (due to change in held item)
                    if (blockAtLocation.getDefaultState().getLightValue() != BlockMovingLightSource.lightBlockToPlace(theEntity).getDefaultState()
                            .getLightValue())
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
                }
            }
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

    
    public static final Predicate<EntityLivingBase> ENTITY_HOLDING_LIGHT_SOURCE = new Predicate<EntityLivingBase>()
    {
        @Override
        public boolean apply(@Nullable EntityLivingBase theEntityLiving)
        {
            return BlockMovingLightSource.isHoldingLightItem(theEntityLiving);
        }
    };
            
    /** Selects entities which are either not players or players that are not spectating */
    public static final Predicate<EntityItem> ENTITY_ITEM_LIGHT_SOURCE = new Predicate<EntityItem>()
    {
        @Override
        public boolean apply(@Nullable EntityItem theEntityItem)
        {
            return  (BlockMovingLightSource.isLightItem(theEntityItem.getItem().getItem()) && MainMod.allowHeldItemsToGiveOffLight);
        }
    };
    
    /** Selects entities which are either not players or players that are not spectating */
    public static final Predicate<Entity> FLAMING_ENTITY = new Predicate<Entity>()
    {
        @Override
        public boolean apply(@Nullable Entity theEntity)
        {
            return  (theEntity.isBurning() && MainMod.allowBurningEntitiesToGiveOffLight);
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
