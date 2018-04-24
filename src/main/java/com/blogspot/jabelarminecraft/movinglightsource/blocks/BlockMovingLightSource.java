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

package com.blogspot.jabelarminecraft.movinglightsource.blocks;

import java.util.HashMap;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blogspot.jabelarminecraft.movinglightsource.registries.BlockRegistry;
import com.blogspot.jabelarminecraft.movinglightsource.tileentities.TileEntityMovingLightSource;
import com.blogspot.jabelarminecraft.movinglightsource.utilities.Utilities;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author jabelar
 *
 */
public class BlockMovingLightSource extends Block implements ITileEntityProvider
{
    private static final HashMap<Item, Block> lightSourceList = new HashMap<>();
    private static final AxisAlignedBB boundingBox = new AxisAlignedBB(0.5D, 0.5D, 0.5D, 0.5D, 0.5D, 0.5D);

    public BlockMovingLightSource(String parName)
    {
        super(Material.AIR);
        Utilities.setBlockName(this, parName);
        setDefaultState(blockState.getBaseState());
        setTickRandomly(false);
        setLightLevel(1.0F);
        // setBlockBounds(0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F);

    }

    // call only after you're sure that all items and blocks have been registered
    public static void initMapLightSources()
    {
        lightSourceList.put(Item.getItemFromBlock(Blocks.BEACON), BlockRegistry.movinglightsource_15);
        lightSourceList.put(Item.getItemFromBlock(Blocks.LIT_PUMPKIN), BlockRegistry.movinglightsource_15);
        lightSourceList.put(Items.LAVA_BUCKET, BlockRegistry.movinglightsource_15);
        lightSourceList.put(Item.getItemFromBlock(Blocks.GLOWSTONE), BlockRegistry.movinglightsource_15);
        lightSourceList.put(Items.GLOWSTONE_DUST, BlockRegistry.movinglightsource_15);
        lightSourceList.put(Item.getItemFromBlock(Blocks.SEA_LANTERN), BlockRegistry.movinglightsource_15);
        lightSourceList.put(Item.getItemFromBlock(Blocks.END_ROD), BlockRegistry.movinglightsource_14);
        lightSourceList.put(Item.getItemFromBlock(Blocks.TORCH), BlockRegistry.movinglightsource_14);
        lightSourceList.put(Item.getItemFromBlock(Blocks.REDSTONE_TORCH), BlockRegistry.movinglightsource_9);
        lightSourceList.put(Item.getItemFromBlock(Blocks.REDSTONE_ORE), BlockRegistry.movinglightsource_7);
        // not easy to tell which blocks may not have items
        // so need to clean up any AIR ItemBlocks that make it into
        // the list.
        lightSourceList.entrySet().removeIf(entry -> entry.getKey() == Items.AIR);
        // DEBUG
        System.out.println("List of all light-emitting items is " + lightSourceList);
    }

    public BlockMovingLightSource(String parName, float parLightLevel)
    {
        this(parName);
        setLightLevel(parLightLevel);
    }

    public static boolean isHoldingLightItem(EntityLivingBase parLivingBase)
    {
        return (lightSourceList.containsKey(parLivingBase.getHeldItemMainhand().getItem())
                || lightSourceList.containsKey(parLivingBase.getHeldItemOffhand().getItem()));
    }

    public static Block lightBlockToPlace(EntityLivingBase parEntityLivingBase)
    {
        if (parEntityLivingBase == null)
        {
            return Blocks.AIR;
        }

        BlockMovingLightSource blockMainHand = (BlockMovingLightSource) lightSourceList.get(parEntityLivingBase.getHeldItemMainhand().getItem());
        BlockMovingLightSource blockOffHand = (BlockMovingLightSource) lightSourceList.get(parEntityLivingBase.getHeldItemOffhand().getItem());
        // // DEBUG
        // System.out.println("Block for main hand = "+blockMainHand+" and block for off hand = "+blockOffHand);
        if (blockMainHand != null)
        {
            // // DEBUG
            // System.out.println("Block in main hand is not null");
            if (blockOffHand != null) // both hands have light emmitting item
            {
                // // DEBUG
                // System.out.println("Block in both hands is not null");
                if (blockMainHand.getDefaultState().getLightValue() >= blockOffHand.getDefaultState().getLightValue())
                {
                    // // DEBUG
                    // System.out.println("Block in main hand has higher light value");
                    return blockMainHand;
                }
                else
                {
                    // // DEBUG
                    // System.out.println("Block in off hand has higher light value");
                    return blockOffHand;
                }
            }
            else // only main hand has light emmitting item
            {
                return blockMainHand;
            }
        }
        else if (blockOffHand != null) // only off hand has light-emmitting item
        {
            // // DEBUG
            // System.out.println("Block in off hand is not null");
            return blockOffHand;
        }
        else // neither hand has light emmitting item
        {
            return Blocks.AIR;
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return boundingBox;
    }

    @ParametersAreNonnullByDefault
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }

    @Override
    public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState parIBlockState)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState parIBlockState)
    {
        return false;
    }

    @ParametersAreNonnullByDefault
    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return true;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
    }

    /**
     * Called when a neighboring block changes.
     */
    @Override
    public void onNeighborChange(IBlockAccess worldIn, BlockPos pos, BlockPos neighborPos)
    {
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
        // want entities to be able to fall through it
    }

    @Override
    public void onLanded(World worldIn, Entity entityIn)
    {
    }

    @ParametersAreNonnullByDefault
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityMovingLightSource();
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }
}