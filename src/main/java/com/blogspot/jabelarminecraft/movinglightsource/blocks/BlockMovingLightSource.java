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

import java.util.ArrayList;
import java.util.List;

import com.blogspot.jabelarminecraft.movinglightsource.tileentities.TileEntityMovingLightSource;
import com.blogspot.jabelarminecraft.movinglightsource.utilities.Utilities;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
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
    public static List<Item> lightSourceList = new ArrayList<Item>() {
        {
            add(Item.getItemFromBlock(Blocks.TORCH));
            add(Item.getItemFromBlock(Blocks.REDSTONE_TORCH));
            add(Item.getItemFromBlock(Blocks.REDSTONE_LAMP));
            add(Item.getItemFromBlock(Blocks.REDSTONE_BLOCK));
            add(Item.getItemFromBlock(Blocks.REDSTONE_ORE));
            add(Items.REDSTONE);
            add(Item.getItemFromBlock(Blocks.REDSTONE_WIRE));
            add(Item.getItemFromBlock(Blocks.GLOWSTONE));
            add(Items.GLOWSTONE_DUST);
            add(Item.getItemFromBlock(Blocks.LAVA));
            add(Items.LAVA_BUCKET);
            add(Item.getItemFromBlock(Blocks.LIT_REDSTONE_LAMP));
            add(Item.getItemFromBlock(Blocks.BEACON));
            add(Item.getItemFromBlock(Blocks.SEA_LANTERN));
            add(Item.getItemFromBlock(Blocks.END_PORTAL));
            add(Item.getItemFromBlock(Blocks.END_PORTAL_FRAME));
        }
    };

    public BlockMovingLightSource()
    {
        super(Material.AIR );
        Utilities.setBlockName(this, "movinglightsource");
        setDefaultState(blockState.getBaseState());
        setTickRandomly(false);
        setLightLevel(1.0F);
        // setBlockBounds(0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F);
    }
    
    public BlockMovingLightSource(int parLightLevel)
    {
        this();
        setLightLevel(parLightLevel);
    }
    
    public static boolean isLightEmittingItem(Item parItem)
    {
        return lightSourceList.contains(parItem);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }
    
    @Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return getBoundingBox(blockState, worldIn, pos);
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

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return true;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        return;
    }

    /**
     * Called when a neighboring block changes.
     */
    @Override
    public void onNeighborChange(IBlockAccess worldIn, BlockPos pos, BlockPos neighborPos)
    {
        return;
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
        return;
    }

    @Override
    public void onLanded(World worldIn, Entity entityIn)
    {
        return;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
    	TileEntityMovingLightSource theTileEntity = new TileEntityMovingLightSource();
        return theTileEntity;
    }
    
    @Override
	public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }


}