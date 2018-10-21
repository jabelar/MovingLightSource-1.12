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
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy
{    @Override
    public void fmlLifeCycleEvent(FMLInitializationEvent event)
    {
        super.fmlLifeCycleEvent(event);

        registerBlockRenderers();
    }

    private void registerBlockRenderers()
    {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

        renderItem.getItemModelMesher().register(Item.getItemFromBlock(BlockRegistry.movinglightsource), 0,
                new ModelResourceLocation(MainMod.MODID + ":" + BlockRegistry.movinglightsource.getTranslationKey().substring(5), "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(BlockRegistry.movinglightsource_15), 0,
                new ModelResourceLocation(MainMod.MODID + ":" + BlockRegistry.movinglightsource_15.getTranslationKey().substring(5), "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(BlockRegistry.movinglightsource_14), 0,
                new ModelResourceLocation(MainMod.MODID + ":" + BlockRegistry.movinglightsource_14.getTranslationKey().substring(5), "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(BlockRegistry.movinglightsource_13), 0,
                new ModelResourceLocation(MainMod.MODID + ":" + BlockRegistry.movinglightsource_13.getTranslationKey().substring(5), "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(BlockRegistry.movinglightsource_12), 0,
                new ModelResourceLocation(MainMod.MODID + ":" + BlockRegistry.movinglightsource_12.getTranslationKey().substring(5), "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(BlockRegistry.movinglightsource_11), 0,
                new ModelResourceLocation(MainMod.MODID + ":" + BlockRegistry.movinglightsource_11.getTranslationKey().substring(5), "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(BlockRegistry.movinglightsource_9), 0,
                new ModelResourceLocation(MainMod.MODID + ":" + BlockRegistry.movinglightsource_9.getTranslationKey().substring(5), "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(BlockRegistry.movinglightsource_7), 0,
                new ModelResourceLocation(MainMod.MODID + ":" + BlockRegistry.movinglightsource_7.getTranslationKey().substring(5), "inventory"));
    }
    
    /*
     * sync the configuration want it public so you can handle case of changes made in-game
     */
    @Override
    public void syncConfig()
    {
        MainMod.config.load();
        MainMod.allowHeldItemsToGiveOffLight = MainMod.config
                .get(Configuration.CATEGORY_GENERAL, I18n.format("config.held_items.name"), true,
                I18n.format("config.held_items.tooltip")).getBoolean(true);
        MainMod.allowTorchesToBurnEntities = MainMod.config
                .get(Configuration.CATEGORY_GENERAL, I18n.format("config.torches_burn.name"), true, 
                 I18n.format("config.torches_burn.tooltip"))
                .getBoolean(true);
        MainMod.allowBurningEntitiesToGiveOffLight = MainMod.config
                .get(Configuration.CATEGORY_GENERAL, I18n.format("config.burning_entities.name"), true,
                I18n.format("config.burning_entities.tooltip")).getBoolean(true);
        MainMod.allowFireEnchantmentsToGiveOffLight = MainMod.config
                .get(Configuration.CATEGORY_GENERAL, I18n.format("config.fire_enchantments.name"), true,
                        I18n.format("config.fire_enchantments.tooltip")).getBoolean(true);

        // save is useful for the first run where config might not exist, and doesn't hurt
        MainMod.config.save();
    }
}