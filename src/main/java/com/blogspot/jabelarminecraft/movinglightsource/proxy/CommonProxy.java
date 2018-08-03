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

import com.blogspot.jabelarminecraft.movinglightsource.EventHandler;
import com.blogspot.jabelarminecraft.movinglightsource.MainMod;
import com.blogspot.jabelarminecraft.movinglightsource.gui.GuiHandler;
import com.blogspot.jabelarminecraft.movinglightsource.tileentities.TileEntityMovingLightSource;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy
{
    public void fmlLifeCycleEvent(FMLPreInitializationEvent event)
    {
        // load configuration before doing anything else
        // got config tutorial from http://www.minecraftforge.net/wiki/How_to_make_an_advanced_configuration_file
        initConfig(event);

        // register stuff
        registerTileEntities();
    }

    public void fmlLifeCycleEvent(FMLInitializationEvent event)
    {
        // register custom event listeners
        registerEventListeners();

        // register gui handlers
        registerGuiHandlers();
    }

    private void registerGuiHandlers()
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(MainMod.instance, new GuiHandler());
    }

    /**
     * Process the configuration
     * 
     * @param event
     *            the event
     */
    private void initConfig(FMLPreInitializationEvent event)
    {
        // might need to use suggestedConfigFile (event.getSuggestedConfigFile) location to publish
        MainMod.configFile = event.getSuggestedConfigurationFile();
        // DEBUG
        System.out.println(MainMod.MODNAME + " config path = " + MainMod.configFile.getAbsolutePath());
        System.out.println("Config file exists = " + MainMod.configFile.canRead());

        MainMod.config = new Configuration(MainMod.configFile);

        syncConfig();
    }

    /*
     * sync the configuration want it public so you can handle case of changes made in-game
     */
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

        // save is useful for the first run where config might not exist, and doesn't hurt
        MainMod.config.save();
    }

    /**
     * Registers tile entities
     */
    private void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TileEntityMovingLightSource.class, "tileEntityMovingLightSource");
    }

    /**
     * Register event listeners
     */
    private void registerEventListeners()
    {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }
}