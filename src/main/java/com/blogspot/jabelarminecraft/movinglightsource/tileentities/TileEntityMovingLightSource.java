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
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

/**
 * @author jabelar
 *
 */
public class TileEntityMovingLightSource extends TileEntity implements ITickable
{
    private Entity theEntity; // the entity holding the light-emitting item
    private boolean shouldDie;
    private boolean typeFlashlight;

    public TileEntityMovingLightSource()
    {
        // after constructing the tile entity instance, remember to call the setentityLiving() method.
        shouldDie = false;
        typeFlashlight = false;
        
//        // DEBUG
//        System.out.println("Constructing");
    }
    
    @Override
    public void update()
    {
//         // DEBUG
//        if (theEntity instanceof EntityArrow)
//        {
//            System.out.println(this+" at pos = "+getPos()+" for block "+world.getBlockState(getPos()).getBlock()+" placed by entity = "+theEntity+" still ticking with shouldDie = "+shouldDie);
//        }
        
        // check if already dying
        if (!typeFlashlight && shouldDie)
        {
//             // DEBUG
//             System.out.println("Should die = "+shouldDie+" with deathTimer = "+deathTimer);
            world.setBlockToAir(getPos());
            world.removeTileEntity(getPos());;
            return;
        }
        
        if (theEntity == null || theEntity.isDead)
        {
//            // DEBUG
//            System.out.println("The associated entity is null or dead");
            shouldDie = true;
            world.setBlockToAir(getPos());
            world.removeTileEntity(getPos());;
            return;
        }
       
        if (!this.typeFlashlight)
        {
            Block theLightBlock = BlockMovingLightSource.lightBlockToPlace(theEntity);
            if (!(theLightBlock instanceof BlockMovingLightSource))
            {
                shouldDie = true;
                world.setBlockToAir(getPos());
                world.removeTileEntity(getPos());;
            }

            /*
             * check if entityLiving has moved away from the tile entity or no longer holding light emitting item set block to air
             */
            double distanceSquared = getDistanceSq(theEntity.posX, theEntity.posY, theEntity.posZ);
            if (distanceSquared > 5.0D)
            {
    //            // DEBUG
    //            if (theEntity instanceof EntityArrow)
    //            {
    //                System.out.println(this+" is setting block to shouldDie because entity moved away, with distance squared = "+distanceSquared+" from entity at position ="+theEntity.getPosition());
    //            }
    //            
    //            if (!(blockAtLocation instanceof BlockMovingLightSource))
    //            {
    //                // DEBUG
    //                System.out.println(this+" is unexpectedly not in position with a light block");
    //            }
                shouldDie = true;
    
                return;
            }
        }
    }

    public void setEntity(Entity parEntity)
    {
//        // DEBUG
//        if (parEntity == null) 
//        {
//            System.out.println(this+" is Setting the entity to null!");
//        }
        theEntity = parEntity;
    }

    public Entity getEntity()
    {
        return theEntity;
    }
    
    public void setFlashlight(boolean isFlashlight)
    {
//        // DEBUG
//        System.out.println("Setting tile entity into flashlight mode");
        
        typeFlashlight = isFlashlight;
    }
    
    public boolean getFlashlight()
    {
        return typeFlashlight;
    }

    @Override
    public void setPos(BlockPos posIn)
    {
        pos = posIn.toImmutable();
        setEntity(Utilities.getClosestEntity(world, pos, 2.0D));
    }
}
