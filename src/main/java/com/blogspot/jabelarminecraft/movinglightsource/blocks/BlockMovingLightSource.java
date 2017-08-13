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
import java.util.Iterator;

import com.blogspot.jabelarminecraft.movinglightsource.registries.BlockRegistry;
import com.blogspot.jabelarminecraft.movinglightsource.tileentities.TileEntityMovingLightSource;
import com.blogspot.jabelarminecraft.movinglightsource.utilities.Utilities;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
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
    public static HashMap<Item, Block> lightSourceList = new HashMap<Item, Block>() {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
            put(Item.getItemFromBlock(Blocks.BEACON), BlockRegistry.MOVING_LIGHT_SOURCE_15);
            put(Item.getItemFromBlock(Blocks.LIT_PUMPKIN), BlockRegistry.MOVING_LIGHT_SOURCE_15);
            put(Items.LAVA_BUCKET, BlockRegistry.MOVING_LIGHT_SOURCE_15);
            put(Item.getItemFromBlock(Blocks.GLOWSTONE), BlockRegistry.MOVING_LIGHT_SOURCE_15);
            put(Items.GLOWSTONE_DUST, BlockRegistry.MOVING_LIGHT_SOURCE_15);
            put(Item.getItemFromBlock(Blocks.SEA_LANTERN), BlockRegistry.MOVING_LIGHT_SOURCE_15);
            put(Item.getItemFromBlock(Blocks.END_ROD), BlockRegistry.MOVING_LIGHT_SOURCE_14);
        	put(Item.getItemFromBlock(Blocks.TORCH), BlockRegistry.MOVING_LIGHT_SOURCE_14);
            put(Item.getItemFromBlock(Blocks.REDSTONE_TORCH), BlockRegistry.MOVING_LIGHT_SOURCE_9);
            put(Item.getItemFromBlock(Blocks.REDSTONE_ORE), BlockRegistry.MOVING_LIGHT_SOURCE_7);
        }
    };

    public BlockMovingLightSource(String parName)
    {
        super(Material.AIR );
        Utilities.setBlockName(this, parName);
        setDefaultState(blockState.getBaseState());
        setTickRandomly(false);
        setLightLevel(1.0F);
        // setBlockBounds(0.5F, 0.5F, 0.5F, 0.5F, 0.5F, 0.5F);
        
        // not easy to tell which blocks may not have items
        // so need to clean up any AIR ItemBlocks that make it into
        // the list.
        Iterator<HashMap.Entry<Item, Block>> iterator = lightSourceList.entrySet().iterator();
        while (iterator.hasNext())
        {
        	HashMap.Entry<Item, Block> entry = iterator.next();
        	if (entry.getKey() == Items.AIR)
        	{
        		iterator.remove();
        	}
        }
        // DEBUG
        System.out.println("List of all light-emmitting items is "+lightSourceList);
    }
    
    public BlockMovingLightSource(String parName, float parLightLevel)
    {
        this(parName);
        setLightLevel(parLightLevel);
    }
    
    public static boolean isHoldingLightItem(EntityPlayer parPlayer)
    {
        return (lightSourceList.containsKey(parPlayer.getHeldItemMainhand().getItem())
        		|| lightSourceList.containsKey(parPlayer.getHeldItemOffhand().getItem()));
    }
    
    public static Block lightBlockToPlace(EntityPlayer parPlayer)
    {
    	if (parPlayer == null)
    	{
    		return Blocks.AIR;
    	}
    	
    	BlockMovingLightSource blockMainHand = (BlockMovingLightSource) lightSourceList.get(parPlayer.getHeldItemMainhand().getItem());
    	BlockMovingLightSource blockOffHand = (BlockMovingLightSource) lightSourceList.get(parPlayer.getHeldItemOffhand().getItem());
    	// DEBUG
    	System.out.println("Block for main hand is = "+blockMainHand+" and block off hand = "+blockOffHand);
    	if (blockMainHand.getLightValue(blockMainHand.getDefaultState()) >= blockOffHand.getLightValue(blockOffHand.getDefaultState())) 
		{ 
			return blockMainHand;
		}
		else
		{
			return blockOffHand;
		}
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