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
*/

package com.blogspot.jabelarminecraft.movinglightsource.utilities;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

/**
 * @author jabelar
 *
 */
public class Utilities
{
    // Need to call this on block instance prior to registering the block
    // chainable
    public static Block setBlockName(Block parBlock, String parBlockName)
    {
        parBlock.setRegistryName(parBlockName);
        return parBlock;
    }

    // Need to call this on item instance prior to registering the item
    /**
     * Sets the item name.
     *
     * @param parItem
     *            the par item
     * @param parItemName
     *            the par item name
     * @return the item
     */
    // chainable
    public static Item setItemName(Item parItem, String parItemName)
    {
        parItem.setRegistryName(parItemName);
        parItem.setTranslationKey(parItemName);
        return parItem;
    }

    /*
     * World utilities
     */

    public static Entity getClosestEntity(World parWorld, BlockPos parPos, double parMaxDistance)
    {
        if (parMaxDistance <= 0.0D)
        {
            return null;
        }

        Entity closest = null;
        double distanceSq = parMaxDistance * parMaxDistance;
        AxisAlignedBB aabb = new AxisAlignedBB(
                parPos.getX() - parMaxDistance,
                parPos.getY() - parMaxDistance,
                parPos.getZ() - parMaxDistance,
                parPos.getX() + parMaxDistance,
                parPos.getY() + parMaxDistance,
                parPos.getZ() + parMaxDistance);
        List<Entity> listEntitiesInRange = parWorld.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
        listEntitiesInRange.addAll(parWorld.getEntitiesWithinAABB(EntityItem.class, aabb));
        listEntitiesInRange.addAll(parWorld.getEntitiesWithinAABB(EntityTippedArrow.class, aabb));
        for (Entity next : listEntitiesInRange)
        {
            if (getDistanceSq(next.getPosition(), parPos) < distanceSq)
            {
                closest = next;
            }
        }
        
//        // DEBUG
//        if (closest == null)
//        {
//            System.out.println("Couldn't find closest entity");
//        }
        return closest;
    }

    private static double getDistanceSq(BlockPos parPos1, BlockPos parPos2)
    {
        return ((parPos1.getX() - parPos2.getX()) * (parPos1.getX() - parPos2.getX())
                + (parPos1.getY() - parPos2.getY()) * (parPos1.getY() - parPos2.getY())
                + (parPos1.getZ() - parPos2.getZ()) * (parPos1.getZ() - parPos2.getZ()));
    }

    /**
     * String to golden.
     *
     * @param parString
     *            the par string
     * @param parShineLocation
     *            the par shine location
     * @param parReturnToBlack
     *            the par return to black
     * @return the string
     */
    private static String stringToGolden(String parString, int parShineLocation, boolean parReturnToBlack)
    {
        int stringLength = parString.length();
        if (stringLength < 1)
        {
            return "";
        }
        String outputString = "";
        for (int i = 0; i < stringLength; i++)
        {
            if ((i + parShineLocation + Minecraft.getSystemTime() / 20) % 88 == 0)
            {
                outputString = outputString + TextFormatting.WHITE + parString.substring(i, i + 1);
            }
            else if ((i + parShineLocation + Minecraft.getSystemTime() / 20) % 88 == 1)
            {
                outputString = outputString + TextFormatting.YELLOW + parString.substring(i, i + 1);
            }
            else if ((i + parShineLocation + Minecraft.getSystemTime() / 20) % 88 == 87)
            {
                outputString = outputString + TextFormatting.YELLOW + parString.substring(i, i + 1);
            }
            else
            {
                outputString = outputString + TextFormatting.GOLD + parString.substring(i, i + 1);
            }
        }
        // return color to a common one after (most chat is white, but for other GUI might want black)
        if (parReturnToBlack)
        {
            return outputString + TextFormatting.BLACK;
        }
        return outputString + TextFormatting.WHITE;
    }

    /**
     * String to golden.
     *
     * @param parString
     *            the par string
     * @param parShineLocation
     *            the par shine location
     * @return the string
     */
    // by default return to white (for chat formatting).
    public static String stringToGolden(String parString, int parShineLocation)
    {
        return stringToGolden(parString, parShineLocation, false);
    }

    /**
     * Formatting needs to be reasserted for each new line. So this method splits up text by line and inserts the formatting for each new line.
     * 
     * @param parTextFormatting
     *            the formatting to apply
     * @param parString
     *            the string to apply formatting to
     * @return
     */
    public static String multiLineTextFormatting(TextFormatting parTextFormatting, String parString)
    {
        String theString = "";
        String textStr[] = parString.split("\\r\\n|\\n|\\r");
        for (int i = 0; i < textStr.length; i++)
        {
            theString += parTextFormatting + textStr[i] + "\n";
        }
        return theString;
    }
    
//    // This is mostly copied from the EntityRenderer#getMouseOver() method
//    public static RayTraceResult rayTrace(Entity theEntity, float dist)
//    {
//        AxisAlignedBB theViewBoundingBox = new AxisAlignedBB(
//                theEntity.posX-0.5D,
//                theEntity.posY-0.0D,
//                theEntity.posZ-0.5D,
//                theEntity.posX+0.5D,
//                theEntity.posY+1.5D,
//                theEntity.posZ+0.5D
//                );
//        RayTraceResult returnMOP = null;
//        if (theEntity.world != null)
//        {
//            double var2 = dist;
//            returnMOP = theEntity.rayTrace(var2, 0);
//            double calcdist = var2;
//            Vec3d pos = theEntity.getPositionEyes(0);
//            var2 = calcdist;
//            if (returnMOP != null)
//            {
//                calcdist = returnMOP.hitVec.distanceTo(pos);
//            }
//            
//            Vec3d lookvec = theEntity.getLook(0);
//            Vec3d var8 = pos.add(lookvec.x * var2, lookvec.y * var2, lookvec.z * var2);
//            Entity pointedEntity = null;
//            float var9 = 1.0F;
//            List<Entity> list = theEntity.world.getEntitiesWithinAABBExcludingEntity(theEntity, theViewBoundingBox.grow(lookvec.x * var2, lookvec.y * var2, lookvec.z * var2).expand(var9, var9, var9));
//            double d = calcdist;
//            
//            for (Entity entity : list)
//            {
//                if (entity.canBeCollidedWith())
//                {
//                    float bordersize = entity.getCollisionBorderSize();
//                    AxisAlignedBB aabb = new AxisAlignedBB(entity.posX-entity.width/2, entity.posY, entity.posZ-entity.width/2, entity.posX+entity.width/2, entity.posY+entity.height, entity.posZ+entity.width/2);
//                    aabb.expand(bordersize, bordersize, bordersize);
//                    RayTraceResult mop0 = aabb.calculateIntercept(pos, var8);
//                    
//                    if (aabb.contains(pos))
//                    {
//                        if (0.0D < d || d == 0.0D)
//                        {
//                            pointedEntity = entity;
//                            d = 0.0D;
//                        }
//                    } else if (mop0 != null)
//                    {
//                        double d1 = pos.distanceTo(mop0.hitVec);
//                        
//                        if (d1 < d || d == 0.0D)
//                        {
//                            pointedEntity = entity;
//                            d = d1;
//                        }
//                    }
//                }
//            }
//            
//            if (pointedEntity != null && (d < calcdist || returnMOP == null))
//            {
//                returnMOP = new RayTraceResult(pointedEntity);
//            }
//        
//        }
//        return returnMOP;
//    }
    
    @Nullable
    public static RayTraceResult rayTrace(Entity theEntity, double reachDistance)
    {
        Vec3d startPos = theEntity.getPositionEyes(0);
        Vec3d lookVec = theEntity.getLookVec();
        Vec3d endPos = startPos.add(lookVec.x * reachDistance, lookVec.y * reachDistance, lookVec.z * reachDistance);
        return theEntity.world.rayTraceBlocks(startPos, endPos, false, true, true);
    }
}
