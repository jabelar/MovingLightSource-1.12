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
     * @param event the event
     */
    private void initConfig(FMLPreInitializationEvent event)
    {
        // might need to use suggestedConfigFile (event.getSuggestedConfigFile) location to publish
        MainMod.configFile = event.getSuggestedConfigurationFile();
        // DEBUG
        System.out.println(MainMod.MODNAME+" config path = "+MainMod.configFile.getAbsolutePath());
        System.out.println("Config file exists = "+MainMod.configFile.canRead());
        
        MainMod.config = new Configuration(MainMod.configFile);

        syncConfig();
    }
    
    /*
     * sync the configuration
     * want it public so you can handle case of changes made in-game
     */
    public void syncConfig()
    {
        MainMod.config.load();
//        MainMod.allowEntityItemsToGiveOffLitght = MainMod.config.get(Configuration.CATEGORY_GENERAL, "Enitity items can give off light", true, "Certain items on the ground will give off light.").getBoolean(true);
//        // DEBUG
//        System.out.println("Allow entity items to give off light = "+MainMod.allowEntityItemsToGiveOffLitght);
        MainMod.allowHeldItemsToGiveOffLight = MainMod.config.get(Configuration.CATEGORY_GENERAL, "Held items can give off light", true, "Holding certain items like torches and glowstone will give off light.").getBoolean(true);
        // DEBUG
        System.out.println("Allow held items to give off light = "+MainMod.allowHeldItemsToGiveOffLight);
        MainMod.allowTorchesToBurnEntities  = MainMod.config.get(Configuration.CATEGORY_GENERAL, "Torches can burn entities", true, "Attacking with regular torch will set entities on fire.").getBoolean(true);
        // DEBUG
        System.out.println("Allow torches to burn entities = "+MainMod.allowTorchesToBurnEntities);
        MainMod.allowBurningEntitiesToGiveOffLight  = MainMod.config.get(Configuration.CATEGORY_GENERAL, "Burning entities give off light", true, "When an entity is burning it gives off same light as a fire block.").getBoolean(true);
        // DEBUG
        System.out.println("Burning entities give off light = "+MainMod.allowBurningEntitiesToGiveOffLight);

        
        // save is useful for the first run where config might not exist, and doesn't hurt
        MainMod.config.save();
    }
  
    /**
     * Registers tile entities
     */
    private void registerTileEntities()
    {
        // DEBUG
        System.out.println("Registering tile entities");
        GameRegistry.registerTileEntity(TileEntityMovingLightSource.class, "tileEntityMovingLightSource");               
   }
 
    /**
     * Register event listeners
     */
    private void registerEventListeners()
    {
        // DEBUG
        System.out.println("Registering event listeners");

        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }
}