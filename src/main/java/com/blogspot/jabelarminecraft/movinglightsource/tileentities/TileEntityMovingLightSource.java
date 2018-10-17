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
    private Entity theEntity; // the entity holding the light-emitting item
    private boolean shouldDie = false;
    private int deathTimer = 2; // number of ticks after entityLiving moves away

    public TileEntityMovingLightSource()
    {
        // after constructing the tile entity instance, remember to call the setentityLiving() method.
        // // DEBUG
        // System.out.println("Constructing");
    }

    @Override
    public void update()
    {
//         // DEBUG
//         System.out.println(this+" at pos = "+getPos()+" for block "+world.getBlockState(getPos()).getBlock()+" placed by entity = "+theEntity+" still ticking with shouldDie = "+shouldDie);

        // check if already dying
        if (shouldDie)
        {
            // // DEBUG
            // System.out.println("Should die = "+shouldDie+" with deathTimer = "+deathTimer);
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
        if (theEntity == null || theEntity.isDead)
        {
//            // DEBUG
//            System.out.println(this+" is setting block shouldDie because entity is null or dead");

            if (blockAtLocation instanceof BlockMovingLightSource)
            {
                shouldDie = true;
            }
            return;
        }

        /*
         * check if entityLiving has moved away from the tile entity or no longer holding light emitting item set block to air
         */
        double distanceSquared = getDistanceSq(theEntity.posX, theEntity.posY, theEntity.posZ);
        if (distanceSquared > 5.0D)
        {
//             // DEBUG
//             System.out.println(this+" is setting block to shouldDie because entity moved away, with distance squared = "+distanceSquared+" from entity at position ="+theEntity.getPosition());
            if (blockAtLocation instanceof BlockMovingLightSource)
            {
//                 // DEBUG
//                 System.out.println("Comfirmed that there is moving light source there");
                shouldDie = true;
            }
        }

        // handle case where entityLiving no longer holding light emitting item
        // and also not on fire
        if (!theEntity.isBurning())
        {
            if (theEntity instanceof EntityLivingBase)
            {
                EntityLivingBase theEntityLiving = (EntityLivingBase) theEntity;
                
                // // DEBUG
                // System.out.println("theEntityLiving is not burning");
                if (!BlockMovingLightSource.isHoldingLightItem(theEntityLiving))
                {
                    // // DEBUG
                    // System.out.println("Setting block to air because entityLiving ("+theEntityLiving+") is no longer holding light emmitting item and burning state =
                    // "+theEntityLiving.isBurning());
                    if (world.getBlockState(getPos()).getBlock() instanceof BlockMovingLightSource)
                    {
                        // // DEBUG
                        // System.out.println("Comfirmed that there is moving light source there");
                        shouldDie = true;
                    }
                }
                else
                {
                    // handle the case where the light-emitting item has changed so light level needs to be adjusted
                    if (blockAtLocation != BlockMovingLightSource.lightBlockToPlace(theEntityLiving))
                    {
                        // // DEBUG
                        // System.out.println("theEntityLiving = "+theEntityLiving+" and tile entity pos = "+getPos());
                        // replace with proper block
                        shouldDie = true;
                    }
                }
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

    @Override
    public void setPos(BlockPos posIn)
    {
        pos = posIn.toImmutable();
        setEntity(Utilities.getClosestEntity(world, pos, 2.0D));
    }
}
