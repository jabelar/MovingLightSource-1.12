package com.blogspot.jabelarminecraft.movinglightsource.creativetabs;

import com.blogspot.jabelarminecraft.movinglightsource.MainMod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CustomCreativeTab extends CreativeTabs {

	public CustomCreativeTab() 
	{
		super(MainMod.MODID);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ItemStack getTabIconItem() 
	{
		return new ItemStack(Items.BANNER);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void displayAllRelevantItems(final NonNullList<ItemStack> items) 
	{
		super.displayAllRelevantItems(items);
	}
}