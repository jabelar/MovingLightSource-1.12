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

import java.util.Collections;

import com.blogspot.jabelarminecraft.movinglightsource.blocks.BlockMovingLightSource;
import com.blogspot.jabelarminecraft.movinglightsource.tileentities.TileEntityMovingLightSource;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
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
            processLightPlacementForEntities(theWorld);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onEvent(PlayerEvent.PlayerLoggedInEvent event)
    {
    	processLightPlacementForEntity(event.player);
    }
    
    private static void processLightPlacementForEntities(World theWorld)
    {
        for (Entity theEntity : Collections.unmodifiableList(theWorld.loadedEntityList))
        {
        	processLightPlacementForEntity(theEntity);
        }
    }
    
    private static void processLightPlacementForEntity(Entity theEntity)
    {
        Block lightBlockToPlace = BlockMovingLightSource.lightBlockToPlace(theEntity);
        if (lightBlockToPlace instanceof BlockMovingLightSource)
        {
            // place light near entity where there is space to do so
            BlockPos blockLocation = new BlockPos(
                    MathHelper.floor(theEntity.posX), 
                    MathHelper.floor(theEntity.posY - 0.2D - theEntity.getYOffset()), 
                    MathHelper.floor(theEntity.posZ)).up();
            Block blockAtLocation = theEntity.world.getBlockState(blockLocation).getBlock();

            if (blockAtLocation == Blocks.AIR)
            {
                placeLightSourceBlock(theEntity, blockLocation, lightBlockToPlace, false);
            }
            else if (blockAtLocation instanceof BlockMovingLightSource)
            {
                if (blockAtLocation.getDefaultState().getLightValue() != lightBlockToPlace.getDefaultState()
                        .getLightValue())
                {
                    placeLightSourceBlock(theEntity, blockLocation, lightBlockToPlace, false);
                }
            }
            else // try one block up
            {
                blockLocation.up();
                blockAtLocation = theEntity.world.getBlockState(blockLocation).getBlock();
                
                if (blockAtLocation == Blocks.AIR)
                {
                    placeLightSourceBlock(theEntity, blockLocation, lightBlockToPlace, false);
                }
                else if (blockAtLocation instanceof BlockMovingLightSource)
                {
                    if (blockAtLocation.getDefaultState().getLightValue() != lightBlockToPlace.getDefaultState()
                            .getLightValue())
                    {
                        placeLightSourceBlock(theEntity, blockLocation, lightBlockToPlace, false);
                    }
                }
            }
        }
        
//        // process flashlight-holding entities
//        if (theEntity instanceof EntityLivingBase)
//        {
//            EntityLivingBase theEntityLiving = (EntityLivingBase) theEntity;
//            
//            if (isHoldingFlashlight(theEntityLiving))
//            {
//                RayTraceResult theRayTraceResult = Utilities.rayTrace(theEntityLiving, 30);
//                if (theRayTraceResult != null)
//                {
//                    BlockPos blockLocation = theRayTraceResult.getBlockPos().subtract(new Vec3i(theEntityLiving.getLookVec().x, theEntityLiving.getLookVec().y, theEntityLiving.getLookVec().z));
//                    Block blockAtLocation = theWorld.getBlockState(blockLocation).getBlock();
//
//                    // DEBUG
//                    System.out.println("Entity is holding flashlight at position "+blockLocation+"which currently has block = "+blockAtLocation);
//
//                    if (blockAtLocation == Blocks.AIR)
//                    {
//                        // DEBUG
//                        System.out.println("Placing light source to replace the air");
//                        
//                        placeLightSourceBlock(theEntity, blockLocation, BlockRegistry.movinglightsource_15, true);
//                    }
//                    else if (blockAtLocation instanceof BlockMovingLightSource)
//                    {
////                        if (blockAtLocation.getDefaultState().getLightValue() != lightBlockToPlace.getDefaultState()
////                                .getLightValue())
////                        {
////                            placeLightSourceBlock(theEntity, blockLocation, BlockRegistry.movinglightsource_15, true);
////                        }
////                        else
////                        {
////                            TileEntity theTileEntity = theWorld.getTileEntity(blockLocation);
////                            if (theTileEntity instanceof TileEntityMovingLightSource)
////                            {
////                                ((TileEntityMovingLightSource) theTileEntity).resetDeathTimer();
////                            }
////                        }
//                    }
//                }
//            }
//        }
    }
//    private static boolean isHoldingFlashlight(EntityLivingBase theEntityLiving)
//    {
//        return (theEntityLiving.getHeldItemMainhand().getItem() instanceof ItemFlashlight || theEntityLiving.getHeldItemOffhand().getItem() instanceof ItemFlashlight);
//    }
    
    private static void placeLightSourceBlock(Entity theEntity, BlockPos blockLocation, Block theLightBlock, boolean isFlashlight)
    {
        theEntity.world.setBlockState(
                blockLocation,
                theLightBlock.getDefaultState());
        TileEntity theTileEntity = theEntity.world.getTileEntity(blockLocation);
        if (theTileEntity instanceof TileEntityMovingLightSource)
        {
            TileEntityMovingLightSource theTileEntityMovingLightSource = (TileEntityMovingLightSource) theTileEntity;
            theTileEntityMovingLightSource.setEntity(theEntity);
            theTileEntityMovingLightSource.setFlashlight(isFlashlight);
        }
    }
   
}
