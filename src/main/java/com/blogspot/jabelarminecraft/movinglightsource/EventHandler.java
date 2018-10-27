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
    
    private static void processLightPlacementForEntities(World theWorld)
    {
        for (Entity theEntity : theWorld.loadedEntityList)
        {
            Block lightBlockToPlace = BlockMovingLightSource.lightBlockToPlace(theEntity);
            if (lightBlockToPlace instanceof BlockMovingLightSource)
            {
                // determine entity position
                int blockX = MathHelper.floor(theEntity.posX);
                int blockY = MathHelper.floor(theEntity.posY - 0.2D - theEntity.getYOffset());
                int blockZ = MathHelper.floor(theEntity.posZ);

                // place light where there is space to do so
                BlockPos blockLocation = new BlockPos(blockX, blockY, blockZ).up();
                Block blockAtLocation = theEntity.world.getBlockState(blockLocation).getBlock();

                if (blockAtLocation == Blocks.AIR)
                {
                    placeLightSourceBlock(theEntity, blockLocation, lightBlockToPlace);
                }
                else if (blockAtLocation instanceof BlockMovingLightSource)
                {
                    if (blockAtLocation.getDefaultState().getLightValue() != lightBlockToPlace.getDefaultState()
                            .getLightValue())
                    {
                        placeLightSourceBlock(theEntity, blockLocation, lightBlockToPlace);
                    }
                }
                else // try one block up
                {
                    blockLocation.up();
                    blockAtLocation = theEntity.world.getBlockState(blockLocation).getBlock();
                    
                    if (blockAtLocation == Blocks.AIR)
                    {
                        placeLightSourceBlock(theEntity, blockLocation, lightBlockToPlace);
                    }
                    else if (blockAtLocation instanceof BlockMovingLightSource)
                    {
                        if (blockAtLocation.getDefaultState().getLightValue() != BlockMovingLightSource.lightBlockToPlace(theEntity).getDefaultState()
                                .getLightValue())
                        {
                            placeLightSourceBlock(theEntity, blockLocation, lightBlockToPlace);
                        }
                    }
                }
            }
        }
    }
    
    private static void placeLightSourceBlock(Entity theEntity, BlockPos blockLocation, Block theLightBlock)
    {
        theEntity.world.setBlockState(
                blockLocation,
                theLightBlock.getDefaultState());
        TileEntity theTileEntity = theEntity.world.getTileEntity(blockLocation);
        if (theTileEntity instanceof TileEntityMovingLightSource)
        {
            TileEntityMovingLightSource theTileEntityMovingLightSource = (TileEntityMovingLightSource) theTileEntity;
            theTileEntityMovingLightSource.setEntity(theEntity);
        }
    }
   
}
