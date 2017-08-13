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
import com.blogspot.jabelarminecraft.movinglightsource.utilities.Utilities;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

/**
 * @author jabelar
 *
 */
public class TileEntityMovingLightSource extends TileEntity implements ITickable
{
    public EntityLivingBase theEntityLiving;
    protected boolean shouldDie = false;
    protected int deathTimer = 2; // number of ticks after entityLiving moves away
    
    public TileEntityMovingLightSource()
    {
        // after constructing the tile entity instance, remember to call the setentityLiving() method.
//        // DEBUG
//        System.out.println("Constructing");
    }
 
    @Override
    public void update()
    {
//    	// DEBUG
//    	System.out.println("Tile entity for entityLiving = "+theEntityLiving+" still ticking");
  
    	// check if already dying
    	if (shouldDie)
    	{
//			// DEBUG
//			System.out.println("Should die = "+shouldDie+" with deathTimer = "+deathTimer);
    		if (deathTimer > 0)
    		{
    			deathTimer--;
    			return;
    		}
    		else
    		{
                world.setBlockToAir(getPos());
    		}
    	}
    	
    	Block blockAtLocation = world.getBlockState(getPos()).getBlock();
    	
    	// clean up in case the entityLiving disappears (teleports, dies, logs out, etc.)
        if (theEntityLiving == null)
        {
//        	// DEBUG
//        	System.out.println("Setting block to air because entityLiving is null");
            if (blockAtLocation instanceof BlockMovingLightSource)
            {
                shouldDie = true;
            }
            return;
        }
    	
    	// clean up in case the entityLiving disappears (teleports, dies, logs out, etc.)
        if (theEntityLiving.isDead)
        {
//        	// DEBUG
//        	System.out.println("Setting block to air because entityLiving is null");
            if (blockAtLocation instanceof BlockMovingLightSource)
            {
                shouldDie = true;
            }
            return;
        }

        // check if entityLiving has moved away from the tile entity or no longer holding light
        // emmitting item set block to air
        double distanceSquared = getDistanceSq(theEntityLiving.posX, theEntityLiving.posY, theEntityLiving.posZ);
        if (distanceSquared > 5.0D) 
        {
//        	// DEBUG
//        	System.out.println("Setting block to air because entityLiving moved away, with distance squared = "+distanceSquared+" from entityLiving at position = "+theEntityLiving.getPosition());
            if (blockAtLocation instanceof BlockMovingLightSource)
            {
//            	// DEBUG
//            	System.out.println("Comfirmed that there is moving light source there");
                shouldDie = true;
            }        
        }
        
        // handle case where entityLiving no longer holding light emitting item
        // and also not on fire
        if (! theEntityLiving.isBurning())
        {
//        	// DEBUG
//        	System.out.println("theEntityLiving is not burning");
	    	if (! BlockMovingLightSource.isHoldingLightItem(theEntityLiving))
	        {
//	        	// DEBUG
//	        	System.out.println("Setting block to air because entityLiving ("+theEntityLiving+") is no longer holding light emmitting item and burning state = "+theEntityLiving.isBurning());
	            if (world.getBlockState(getPos()).getBlock() instanceof BlockMovingLightSource)
	            {
	//            	// DEBUG
	//            	System.out.println("Comfirmed that there is moving light source there");
	                shouldDie = true;
	            }            
	        }
	    	else 
	    	{
		    	// handle the case where the light-emitting item has changed so light level needs to be adjusted
		    	if (blockAtLocation != BlockMovingLightSource.lightBlockToPlace(theEntityLiving))
		    	{
	//	    		// DEBUG
	//	    		System.out.println("theEntityLiving = "+theEntityLiving+" and tile entity pos = "+getPos());
		    		// replace with proper block
		    		shouldDie = true;
		    	}
	    	}
        }
    }  
    
    public void setEntityLiving(EntityLivingBase parEntityLiving)
    {
//    	// DEBUG
//    	System.out.println("Setting the entity living to "+parEntityLiving);
        theEntityLiving = parEntityLiving;
    }
    
    public EntityLivingBase getEntityLiving()
    {
    	return theEntityLiving;
    }


    @Override
	public void setPos(BlockPos posIn)
    {
        pos = posIn.toImmutable();
     	setEntityLiving(Utilities.getClosestEntityLiving(world, pos, 2.0D));
    }
}


