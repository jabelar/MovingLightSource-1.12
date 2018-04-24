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
import com.blogspot.jabelarminecraft.movinglightsource.registries.BlockRegistry;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class EventHandler
{
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onEvent(LivingUpdateEvent event)
    {
        EntityLivingBase theEntityLiving = event.getEntityLiving();

        if (!theEntityLiving.world.isRemote)
        {
            // check if entity is on fire
            if (theEntityLiving.isBurning() && MainMod.allowBurningEntitiesToGiveOffLight)
            {
                // // DEBUG
                // System.out.println("Entity is burning");

                // determine player position
                int blockX = MathHelper.floor(theEntityLiving.posX);
                int blockY = MathHelper.floor(theEntityLiving.posY - 0.2D - event.getEntityLiving().getYOffset());
                int blockZ = MathHelper.floor(theEntityLiving.posZ);

                // place light where there is space to do so
                BlockPos blockLocation = new BlockPos(blockX, blockY, blockZ).up();
                Block blockAtLocation = theEntityLiving.world.getBlockState(blockLocation).getBlock();
                // // DEBUG
                // System.out.println("Block at player position is "+event.player.world.getBlockState(blockLocation).getBlock());
                if (blockAtLocation == Blocks.AIR)
                {
                    // // DEBUG
                    // System.out.println("There is space at player location "+blockLocation+" to place block");

                    // there is space to create moving light source block
                    theEntityLiving.world.setBlockState(
                            blockLocation,
                            BlockRegistry.movinglightsource_15.getDefaultState());
                }
            }
        }
    }

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
                // DEBUG
                System.out.println("Setting entity " + event.getEntity() + " on fire");
                event.getTarget().setFire(10);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onEvent(PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START && !event.player.world.isRemote)
        {
            // check if player is holding a light source
            if (BlockMovingLightSource.isHoldingLightItem(event.player) && MainMod.allowHeldItemsToGiveOffLight)
            {
                // // DEBUG
                // System.out.println("Holding torch");

                // determine player position
                int blockX = MathHelper.floor(event.player.posX);
                int blockY = MathHelper.floor(event.player.posY - 0.2D - event.player.getYOffset());
                int blockZ = MathHelper.floor(event.player.posZ);

                // place light where there is space to do so
                BlockPos blockLocation = new BlockPos(blockX, blockY, blockZ).up();
                Block blockAtLocation = event.player.world.getBlockState(blockLocation).getBlock();
                // // DEBUG
                // System.out.println("Block at player position is "+event.player.world.getBlockState(blockLocation).getBlock());
                if (blockAtLocation == Blocks.AIR)
                {
                    // // DEBUG
                    // System.out.println("There is space at player location "+blockLocation+" to place block");

                    // there is space to create moving light source block
                    event.player.world.setBlockState(
                            blockLocation,
                            BlockMovingLightSource.lightBlockToPlace(event.player).getDefaultState());
                }
                else if (blockAtLocation instanceof BlockMovingLightSource)
                {
                    // // DEBUG
                    // System.out.println("There is already a BlockMovingLight at player location "+blockLocation);
                    // check if light value at location should change (due to change in held item)
                    if (blockAtLocation.getDefaultState().getLightValue() != BlockMovingLightSource.lightBlockToPlace(event.player).getDefaultState()
                            .getLightValue())
                    {
                        event.player.world.setBlockState(
                                blockLocation,
                                BlockMovingLightSource.lightBlockToPlace(event.player).getDefaultState());
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onEvent(OnConfigChangedEvent eventArgs)
    {
        // DEBUG
        System.out.println("OnConfigChangedEvent");
        if (eventArgs.getModID().equals(MainMod.MODID))
        {
            System.out.println("Syncing config for mod =" + eventArgs.getModID());
            MainMod.config.save();
            MainMod.proxy.syncConfig();
        }
    }
}
