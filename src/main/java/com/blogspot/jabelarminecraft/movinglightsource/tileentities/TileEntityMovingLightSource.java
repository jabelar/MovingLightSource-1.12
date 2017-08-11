/**
    Copyright (C) 2015 by jabelar

    This file is part of jabelar's Minecraft Forge modding examples; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    For a copy of the GNU General Public License see <http://www.gnu.org/licenses/>.
*/

package com.blogspot.jabelarminecraft.movinglightsource.tileentities;

import com.blogspot.jabelarminecraft.movinglightsource.blocks.BlockMovingLightSource;
import com.blogspot.jabelarminecraft.movinglightsource.registries.BlockRegistry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

/**
 * @author jabelar
 *
 */
public class TileEntityMovingLightSource extends TileEntity implements ITickable
{
    public EntityPlayer thePlayer;
    
    public TileEntityMovingLightSource()
    {
        // after constructing the tile entity instance, remember to call the setPlayer() method.
        // DEBUG
        System.out.println("Constructing");
    }
 
    @Override
    public void update()
    {
    	// clean up in case the player disappears (teleports, dies, logs out, etc.)
        if (thePlayer == null)
        {
        	// DEBUG
        	System.out.println("Setting block to air because player is null");
            if (world.getBlockState(getPos()).getBlock() == BlockRegistry.MOVING_LIGHT_SOURCE)
            {
                world.setBlockToAir(getPos());
            }
            
            return;
        }

        // check if player has moved away from the tile entity or no longer holding light
        // emmitting item set block to air
        double distanceSquared = getDistanceSq(thePlayer.posX, thePlayer.posY, thePlayer.posZ);
        if (distanceSquared > 4.0D) 
        {
        	// DEBUG
        	System.out.println("Setting block to air because player moved away, with distance squared = "+distanceSquared+" from player at position = "+thePlayer.getPosition());
            if (world.getBlockState(getPos()).getBlock() == BlockRegistry.MOVING_LIGHT_SOURCE)
            {
            	// DEBUG
            	System.out.println("Comfirmed that there is moving light source there");
                world.setBlockToAir(getPos());
            }            
        }
    	if (! BlockMovingLightSource.isLightEmittingItem(thePlayer.getActiveItemStack().getItem()))
        {
        	// DEBUG
        	System.out.println("Setting block to air because player no longer holding light emmitting item");
            if (world.getBlockState(getPos()).getBlock() == BlockRegistry.MOVING_LIGHT_SOURCE)
            {
            	// DEBUG
            	System.out.println("Comfirmed that there is moving light source there");
                world.setBlockToAir(getPos());
            }            
        }
    }  
    
    public void setPlayer(EntityPlayer parPlayer)
    {
    	// DEBUG
    	System.out.println("Setting the player to "+parPlayer);
        thePlayer = parPlayer;
    }
    
    public EntityPlayer getPlayer()
    {
    	return thePlayer;
    }


    @Override
	public void setPos(BlockPos posIn)
    {
        pos = posIn.toImmutable();
     	setPlayer(world
    			.getClosestPlayer(
    					pos.getX(),
    					pos.getY(),
    					pos.getZ(), 
    					20.0D, 
    					false));
    }
}
