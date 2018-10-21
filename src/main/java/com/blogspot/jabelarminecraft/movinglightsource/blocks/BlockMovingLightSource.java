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
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

import com.blogspot.jabelarminecraft.movinglightsource.MainMod;
import com.blogspot.jabelarminecraft.movinglightsource.registries.BlockRegistry;
import com.blogspot.jabelarminecraft.movinglightsource.tileentities.TileEntityMovingLightSource;
import com.blogspot.jabelarminecraft.movinglightsource.utilities.Utilities;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
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
    }

    // call only after you're sure that all items and blocks have been registered
    public static void initMapLightSources()
    {
        addVanillaItemsToLightSourceList();
        addModItemsToLightSourceList(); 
        addModCompabilityItemsToLightSourceList();
        
        // not easy to tell which blocks may not have items
        // so need to clean up any AIR ItemBlocks that make it into
        // the list.
        lightSourceList.entrySet().removeIf(entry -> entry.getKey() == Items.AIR);
        
        dumpLightSourceListToConsole();
    }
    
    private static void addVanillaItemsToLightSourceList()
    {
        // Add known vanilla items to list
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
    }
    
    private static void addModItemsToLightSourceList()
    {
        // Add any mod blocks that emit light to list
        Set<Entry<ResourceLocation, Block>> setModBlocksWithLight = ForgeRegistries.BLOCKS.getEntries();
        Iterator<Entry<ResourceLocation, Block>> iterator = setModBlocksWithLight.iterator();
        while (iterator.hasNext())
        {
            Entry<ResourceLocation, Block> entry = iterator.next();
            if (!(entry.getKey().getNamespace().contains("minecraft") || entry.getKey().getNamespace().contains(MainMod.MODID)))
            {
//                // DEBUG
//                if (!(entry.getKey().getNamespace().contains("adchimneys")))
//                {
//                    List<ItemStack> subBlockList = NonNullList.create();
//                    entry.getValue().getSubBlocks(null, (NonNullList<ItemStack>) subBlockList);
//                    System.out.println("Found a mod block = "+entry.getKey()+" with sub blocks = "+subBlockList);
//                }
                try
                {
                    int lightValue = entry.getValue().getDefaultState().getLightValue(null, null);
                    if (lightValue > 0)
                    {
                        Block lightBlock = BlockRegistry.movinglightsource_15;
                        switch (entry.getValue().getDefaultState().getLightValue(null, null))
                        {
                            case 14: lightBlock = BlockRegistry.movinglightsource_14; break;
                            case 13: lightBlock = BlockRegistry.movinglightsource_13; break;
                            case 12: lightBlock = BlockRegistry.movinglightsource_12; break;
                            case 11: lightBlock = BlockRegistry.movinglightsource_11; break;
                            case 10: case 9: case 8: lightBlock = BlockRegistry.movinglightsource_9; break;
                            case 7: case 6: case 5: case 4: case 3: case 2: case 1: lightBlock = BlockRegistry.movinglightsource_7; break;
                        }
                        lightSourceList.put(Item.getItemFromBlock(entry.getValue()), lightBlock);
                    }
                }
                catch (NullPointerException e)
                {
//                    // DEBUG
//                    System.out.println(entry.getValue()+" has dynamic lighting");
                }
            }
        }
    }
    
    private static void addModCompabilityItemsToLightSourceList()
    {
        if (Loader.isModLoaded("jetorches"))
        {
            jetorchesCompatibility();
        }
    }
    
    private static void jetorchesCompatibility()
    {
        // Add any mod blocks that emit light to list
        Set<Entry<ResourceLocation, Block>> setModBlocksWithLight = ForgeRegistries.BLOCKS.getEntries();
        Iterator<Entry<ResourceLocation, Block>> iterator = setModBlocksWithLight.iterator();
        while (iterator.hasNext())
        {
            Entry<ResourceLocation, Block> entry = iterator.next();
            if (entry.getKey().getNamespace().contains("jetorches") && 
                    (
                        entry.getKey().getPath().contains("torch") || 
                        entry.getKey().getPath().contains("lamp"))
                    )
            {
//                // DEBUG
//                System.out.println("Found mod block = "+entry.getKey()+" with instance = "+entry.getValue()+" with item from block = "+Item.getItemFromBlock(entry.getValue()));
                lightSourceList.put(Item.getItemFromBlock(entry.getValue()), BlockRegistry.movinglightsource_14);
            }
        }
        // Add any mod blocks that emit light to list
        Set<Entry<ResourceLocation, Item>> setModItemsWithLight = ForgeRegistries.ITEMS.getEntries();
        Iterator<Entry<ResourceLocation, Item>> iterator2 = setModItemsWithLight.iterator();
        while (iterator2.hasNext())
        {
            Entry<ResourceLocation, Item> entry = iterator2.next();
            if (entry.getKey().getNamespace().contains("jetorches") && 
                    (
                        entry.getKey().getPath().contains("torch") || 
                        entry.getKey().getPath().contains("lamp"))
                    )
            {
                // DEBUG
                System.out.println("Found mod item = "+entry.getKey()+" with instance = "+entry.getValue());
                lightSourceList.put(entry.getValue(), BlockRegistry.movinglightsource_14);
            }
        }
    }
    
    private static void dumpLightSourceListToConsole()
    {
        // DEBUG
        System.out.print("List of all light-emitting items is: ");
        Iterator<Entry<Item, Block>> iterator2 = lightSourceList.entrySet().iterator();
        while (iterator2.hasNext())
        {
            Entry<Item, Block> entry = iterator2.next();
            System.out.print(entry.getKey().getRegistryName()+" ");
        }
        System.out.print("\n");
    }

    public BlockMovingLightSource(String parName, float parLightLevel)
    {
        this(parName);
        setLightLevel(parLightLevel);
    }

    public static boolean isHoldingLightItem(EntityLivingBase parLivingBase)
    {        
        return (isLightItem(parLivingBase.getHeldItemMainhand().getItem())
                || isLightItem(parLivingBase.getHeldItemOffhand().getItem())
                || (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, parLivingBase.getHeldItemMainhand()) > 0)
                || (EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, parLivingBase.getHeldItemMainhand()) > 0)
                || (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, parLivingBase.getHeldItemOffhand()) > 0)
                || (EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, parLivingBase.getHeldItemOffhand()) > 0)
                );
    }
    
    public static boolean isHoldingLightItem(EntityItem theEntityItem)
    {
        return (isLightItem(theEntityItem.getItem().getItem()));
    }
   
    public static boolean isLightItem(Item parItem)
    {
        return lightSourceList.containsKey(parItem);
    }

    @SuppressWarnings("deprecation")
    public static Block lightBlockToPlace(Entity parEntity)
    {
        if (parEntity == null)
        {
            return Blocks.AIR;
        }
        
        if (parEntity.isBurning())
        {
            return BlockRegistry.movinglightsource_15;
        }
                
        if (parEntity instanceof EntityLivingBase)
        {
            EntityLivingBase theEntityLiving = (EntityLivingBase)parEntity;
            
            // Handle case of holding item with a flame type enchantment
            if (MainMod.allowFireEnchantmentsToGiveOffLight && (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME,theEntityLiving.getHeldItemMainhand()) > 0
                    || EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME,theEntityLiving.getHeldItemOffhand()) > 0
                    || EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT,theEntityLiving.getHeldItemMainhand()) > 0
                    || EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT,theEntityLiving.getHeldItemOffhand()) > 0))
            {
                return BlockRegistry.movinglightsource_15;
            }
            
            BlockMovingLightSource blockMainHand = (BlockMovingLightSource) lightSourceList.get(theEntityLiving.getHeldItemMainhand().getItem());
            BlockMovingLightSource blockOffHand = (BlockMovingLightSource) lightSourceList.get(theEntityLiving.getHeldItemOffhand().getItem());
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
            else
            {
                if (blockOffHand != null)
                {
                    return blockOffHand;
                }
            }
        }
        
        if (parEntity instanceof EntityItem)
        {
            EntityItem theEntityItem = (EntityItem)parEntity;
            return lightSourceList.get(theEntityItem.getItem().getItem());
        }
        
        return Blocks.AIR;
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
    public BlockRenderLayer getRenderLayer()
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