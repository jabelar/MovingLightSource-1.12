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

    If you're interested in licensing the code under different terms you can
    contact the author at julian_abelar@hotmail.com 
*/

package com.blogspot.jabelarminecraft.movinglightsource.proxy;

import com.blogspot.jabelarminecraft.movinglightsource.MainMod;
import com.blogspot.jabelarminecraft.movinglightsource.registries.BlockRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy 
{
    @Override
    public void fmlLifeCycleEvent(FMLPreInitializationEvent event)
    {
        // DEBUG
        System.out.println("on Client side");

        /*
         *  do common stuff
         */
        super.fmlLifeCycleEvent(event);
    }

    @Override
    public void fmlLifeCycleEvent(FMLInitializationEvent event)
    {
        // DEBUG
        System.out.println("on Client side");

        /*
         *  do common stuff
         */
        super.fmlLifeCycleEvent(event);

        // register renderers
        registerBlockRenderers();
    }

   
    private void registerBlockRenderers()
    {
        // DEBUG
        System.out.println("Registering block renderers");
        
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(BlockRegistry.MOVING_LIGHT_SOURCE), 0, new ModelResourceLocation(MainMod.MODID + ":" + BlockRegistry.MOVING_LIGHT_SOURCE.getUnlocalizedName().substring(5), "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(BlockRegistry.MOVING_LIGHT_SOURCE_15), 0, new ModelResourceLocation(MainMod.MODID + ":" + BlockRegistry.MOVING_LIGHT_SOURCE_15.getUnlocalizedName().substring(5), "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(BlockRegistry.MOVING_LIGHT_SOURCE_14), 0, new ModelResourceLocation(MainMod.MODID + ":" + BlockRegistry.MOVING_LIGHT_SOURCE_14.getUnlocalizedName().substring(5), "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(BlockRegistry.MOVING_LIGHT_SOURCE_13), 0, new ModelResourceLocation(MainMod.MODID + ":" + BlockRegistry.MOVING_LIGHT_SOURCE_13.getUnlocalizedName().substring(5), "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(BlockRegistry.MOVING_LIGHT_SOURCE_12), 0, new ModelResourceLocation(MainMod.MODID + ":" + BlockRegistry.MOVING_LIGHT_SOURCE_12.getUnlocalizedName().substring(5), "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(BlockRegistry.MOVING_LIGHT_SOURCE_11), 0, new ModelResourceLocation(MainMod.MODID + ":" + BlockRegistry.MOVING_LIGHT_SOURCE_11.getUnlocalizedName().substring(5), "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(BlockRegistry.MOVING_LIGHT_SOURCE_9), 0, new ModelResourceLocation(MainMod.MODID + ":" + BlockRegistry.MOVING_LIGHT_SOURCE_9.getUnlocalizedName().substring(5), "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(BlockRegistry.MOVING_LIGHT_SOURCE_7), 0, new ModelResourceLocation(MainMod.MODID + ":" + BlockRegistry.MOVING_LIGHT_SOURCE_7.getUnlocalizedName().substring(5), "inventory"));
    }
}