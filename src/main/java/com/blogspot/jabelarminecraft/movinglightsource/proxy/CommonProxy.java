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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.blogspot.jabelarminecraft.movinglightsource.EventHandler;
import com.blogspot.jabelarminecraft.movinglightsource.MainMod;
import com.blogspot.jabelarminecraft.movinglightsource.OreGenEventHandler;
import com.blogspot.jabelarminecraft.movinglightsource.TerrainGenEventHandler;
import com.blogspot.jabelarminecraft.movinglightsource.gui.GuiHandler;
import com.blogspot.jabelarminecraft.movinglightsource.networking.MessageExtendedReachAttack;
import com.blogspot.jabelarminecraft.movinglightsource.networking.MessageRequestItemStackRegistryFromClient;
import com.blogspot.jabelarminecraft.movinglightsource.networking.MessageSendItemStackRegistryToServer;
import com.blogspot.jabelarminecraft.movinglightsource.networking.MessageSyncEntityToClient;
import com.blogspot.jabelarminecraft.movinglightsource.networking.MessageToClient;
import com.blogspot.jabelarminecraft.movinglightsource.networking.MessageToServer;
import com.blogspot.jabelarminecraft.movinglightsource.tileentities.TileEntityMovingLightSource;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy 
{
    
    protected int modEntityID = 0;
    
    /*
     * Sometimes useful to have list of all item types, including subtypes
     */
    protected List<ItemStack> itemStackRegistry = new ArrayList<ItemStack>();
     
    public void fmlLifeCycleEvent(FMLPreInitializationEvent event)
    { 
        // load configuration before doing anything else
        // got config tutorial from http://www.minecraftforge.net/wiki/How_to_make_an_advanced_configuration_file
        initConfig(event);

        // register stuff
        registerTileEntities();
        registerModEntities();
        registerEntitySpawns();
        registerFuelHandlers();
        registerSimpleNetworking();
//        VillagerRegistry.instance().registerVillagerId(10);
//      VillagerRegistry.instance().registerVillageTradeHandler(10, new VillageTradeHandlerMagicBeans());
//      VillagerRegistry.getRegisteredVillagers();
    }

    public void fmlLifeCycleEvent(FMLInitializationEvent event)
    {
        // register custom event listeners
        registerEventListeners();
         
        // register recipes here to allow use of items from other mods
        registerRecipes();
        
        // register Advancements here to allow use of items from other mods
        registerAdvancements();
        
        // register gui handlers
        registerGuiHandlers();
        
//        registerDeconstructingInit(event);
    }
    
    public void registerGuiHandlers() 
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(MainMod.instance, new GuiHandler());     
    }

    public void fmlLifeCycleEvent(FMLPostInitializationEvent event)
    {
        // can do some inter-mod stuff here
        initItemStackRegistry();    
    }

    public void fmlLifeCycleEvent(FMLServerAboutToStartEvent event) 
    {
        // TODO Auto-generated method stub
        
    }

    public void fmlLifeCycleEvent(FMLServerStartedEvent event) 
    {
        // TODO Auto-generated method stub

    }

    public void fmlLifeCycleEvent(FMLServerStoppingEvent event) 
    {
        // TODO Auto-generated method stub
        
    }

    public void fmlLifeCycleEvent(FMLServerStoppedEvent event) 
    {
        // TODO Auto-generated method stub
        
    }

    public void fmlLifeCycleEvent(FMLServerStartingEvent event) 
    {
        // // register server commands
        // event.registerServerCommand(new CommandStructureCapture());
    }
        
    /*
     * Thanks to diesieben07 tutorial for this code
     */
    /**
     * Registers the simple networking channel and messages for both sides
     */
    protected void registerSimpleNetworking() 
    {
        // DEBUG
        System.out.println("registering simple networking");
        MainMod.network = NetworkRegistry.INSTANCE.newSimpleChannel(MainMod.NETWORK_CHANNEL_NAME);

        int packetId = 0;
        // register messages from client to server
        MainMod.network.registerMessage(MessageToServer.Handler.class, MessageToServer.class, packetId++, Side.SERVER);
        // register messages from server to client
        MainMod.network.registerMessage(MessageToClient.Handler.class, MessageToClient.class, packetId++, Side.CLIENT);
        MainMod.network.registerMessage(MessageSyncEntityToClient.Handler.class, MessageSyncEntityToClient.class, packetId++, Side.CLIENT);
        MainMod.network.registerMessage(MessageExtendedReachAttack.Handler.class, MessageExtendedReachAttack.class, packetId++, Side.SERVER);
        MainMod.network.registerMessage(MessageSendItemStackRegistryToServer.Handler.class, MessageSendItemStackRegistryToServer.class, packetId++, Side.SERVER);
        MainMod.network.registerMessage(MessageRequestItemStackRegistryFromClient.Handler.class, MessageRequestItemStackRegistryFromClient.class, packetId++, Side.CLIENT);
    }
    
    /*   
     * Thanks to CoolAlias for this tip!
     */
    /**
     * Returns a side-appropriate EntityPlayer for use during message handling
     */
    public EntityPlayer getPlayerEntityFromContext(MessageContext ctx) 
    {
        return ctx.getServerHandler().player;
    }
    
    /**
     * Process the configuration
     * @param event
     */
    protected void initConfig(FMLPreInitializationEvent event)
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
        MainMod.allowEntityItemsToGiveOffLitght = MainMod.config.get(Configuration.CATEGORY_GENERAL, "Enitity items can give off light", true, "Certain items on the ground will give off light.").getBoolean(true);
        // DEBUG
        System.out.println("Allow entity items to give off light = "+MainMod.allowEntityItemsToGiveOffLitght);
        MainMod.allowHeldItemsToGiveOffLight = MainMod.config.get(Configuration.CATEGORY_GENERAL, "Held items can give off light", true, "Holding certain items like torches and glowstone will give off light.").getBoolean(true);
        // DEBUG
        System.out.println("Allow held items to give off light = "+MainMod.allowHeldItemsToGiveOffLight);
        MainMod.allowTorchesToBurnEntities  = MainMod.config.get(Configuration.CATEGORY_GENERAL, "Torches can burn entities", true, "Attacking with regular torch will set entities on fire.").getBoolean(true);
        // DEBUG
        System.out.println("Allow torches to burn entities = "+MainMod.allowTorchesToBurnEntities);

        
        // save is useful for the first run where config might not exist, and doesn't hurt
        MainMod.config.save();
    }

    /** 
     * Registers fluids
     */
    public void registerFluids()
    {
        // see tutorial at http://www.minecraftforge.net/wiki/Create_a_Fluid
        // Fluid testFluid = new Fluid("testfluid");
        // FluidRegistry.registerFluid(testFluid);
        // testFluid.setLuminosity(0).setDensity(1000).setViscosity(1000).setGaseous(false) ;
     }
    
    
    /**
     * Registers tile entities
     */
    public void registerTileEntities()
    {
        // DEBUG
        System.out.println("Registering tile entities");
        GameRegistry.registerTileEntity(TileEntityMovingLightSource.class, "tileEntityMovingLightSource");               
   }

    /**
     * Registers recipes
     */
    public void registerRecipes()
    {
//        // DEBUG
//        System.out.println("Registering recipes");
//                       
//        // examples:
//        //        GameRegistry.addRecipe(recipe);
//        //        GameRegistry.addShapedRecipe(output, params);
//        //        GameRegistry.addShapelessRecipe(output, params);
//        //        GameRegistry.addSmelting(input, output, xp);
//        GameRegistry.addShapedRecipe(new ItemStack(Item.getItemFromBlock(BlockSmith.blockGrinder), 1), 
//                new Object[]
//                {
//                    "ABA",
//                    "A A",
//                    "CCC",
//                    'A', Items.STICK, 'B', Blocks.STONE, 'C', Blocks.COBBLESTONE
//                });
//        GameRegistry.addShapedRecipe(new ItemStack(BlockSmith.blockDeconstructor), 
//                new Object[]
//                {
//                    "SSS", 
//                    "SXS", 
//                    "SSS", 
//                    'X', Blocks.CRAFTING_TABLE, 'S', Blocks.COBBLESTONE
//                });
//        GameRegistry.addShapedRecipe(new ItemStack(Items.IRON_HORSE_ARMOR), 
//                new Object[]
//                {
//                    "  S", 
//                    "SXS", 
//                    "SSS", 
//                    'X', Blocks.WOOL, 'S', Items.IRON_INGOT
//                });
//        GameRegistry.addShapedRecipe(new ItemStack(Items.GOLDEN_HORSE_ARMOR), 
//                new Object[]
//                {
//                    "  S", 
//                    "SXS", 
//                    "SSS", 
//                    'X', Blocks.WOOL, 'S', Items.GOLD_INGOT
//                });
//        GameRegistry.addShapedRecipe(new ItemStack(Items.DIAMOND_HORSE_ARMOR), 
//                new Object[]
//                {
//                    "  S", 
//                    "SXS", 
//                    "SSS", 
//                    'X', Blocks.WOOL, 'S', Items.DIAMOND
//                });
//        GameRegistry.addShapedRecipe(new ItemStack(Items.SADDLE), 
//                new Object[]
//                {
//                    "SSS", 
//                    "SXS", 
//                    "X X", 
//                    'X', Items.IRON_INGOT, 'S', Items.LEATHER
//                });
//
    }

    /**
     * Registers entities as mod entities
     */
    protected void registerModEntities()
    {    
         // DEBUG
        System.out.println("Registering entities");
        // if you want it with a spawn egg use
        // registerModEntityWithEgg(EntityManEatingTiger.class, "tiger", 0xE18519, 0x000000);
        // or without spawn egg use

        // registerModEntityWithEgg(EntityPigTest.class, "test_pig", 0xE18519, 0x000000);
    }
 
    /**
     * Registers an entity as a mod entity with no tracking
     * @param parEntityClass
     * @param parEntityName
     */
     protected void registerModEntity(Class parEntityClass, String parEntityName)
     {
 		final ResourceLocation resourceLocation = new ResourceLocation(MainMod.MODID, parEntityName);
        EntityRegistry.registerModEntity(resourceLocation, parEntityClass, parEntityName, ++modEntityID, MainMod.instance, 80, 3, false);
     }

     /**
      * Registers an entity as a mod entity with fast tracking.  Good for fast moving objects like throwables
      * @param parEntityClass
      * @param parEntityName
      */
     protected void registerModEntityFastTracking(Class parEntityClass, String parEntityName)
     {
  		final ResourceLocation resourceLocation = new ResourceLocation(MainMod.MODID, parEntityName);
        EntityRegistry.registerModEntity(resourceLocation, parEntityClass, parEntityName, ++modEntityID, MainMod.instance, 80, 10, true);
     }

     public void registerModEntityWithEgg(Class parEntityClass, String parEntityName, 
              int parEggColor, int parEggSpotsColor)
     {
   		final ResourceLocation resourceLocation = new ResourceLocation(MainMod.MODID, parEntityName);
        EntityRegistry.registerModEntity(resourceLocation, parEntityClass, parEntityName, ++modEntityID, MainMod.instance, 80, 3, false, parEggColor, parEggSpotsColor);
     }

     /**
      * Registers entity natural spawns
      */
     protected void registerEntitySpawns()
     {
        /*
         *  register natural spawns for entities
         * EntityRegistry.addSpawn(MyEntity.class, spawnProbability, minSpawn, maxSpawn, enumCreatureType, [spawnBiome]);
         * See the constructor in Biome.java to see the rarity of vanilla mobs; Sheep are probability 10 while Endermen are probability 1
         * minSpawn and maxSpawn are about how groups of the entity spawn
         * enumCreatureType represents the "rules" Minecraft uses to determine spawning, based on creature type. By default, you have three choices:
         *    EnumCreatureType.creature uses rules for animals: spawn everywhere it is light out.
         *    EnumCreatureType.monster uses rules for monsters: spawn everywhere it is dark out.
         *    EnumCreatureType.waterCreature uses rules for water creatures: spawn only in water.
         * [spawnBiome] is an optional parameter of type Biome that limits the creature spawn to a single biome type. Without this parameter, it will spawn everywhere. 
         */

         // DEBUG
        System.out.println("Registering natural spawns");

        // // savanna
        // EntityRegistry.addSpawn(EntityLion.class, 6, 1, 5, EnumCreatureType.creature, Biome.savanna); //change the values to vary the spawn rarity, biome, etc.              
        // EntityRegistry.addSpawn(EntityElephant.class, 10, 1, 5, EnumCreatureType.creature, Biome.savanna); //change the values to vary the spawn rarity, biome, etc.              
     }
 
     protected void addSpawnAllBiomes(EntityLiving parEntity, int parChance, int parMinGroup, int parMaxGroup)
     {
         Iterator<ResourceLocation> allBiomesIterator = Biome.REGISTRY.getKeys().iterator();
         while (allBiomesIterator.hasNext())
         {
             Biome nextBiome = Biome.REGISTRY.getObject(allBiomesIterator.next());
             EntityRegistry.addSpawn(parEntity.getClass(), parChance, parMinGroup, parMaxGroup, EnumCreatureType.CREATURE, 
                  nextBiome); //change the values to vary the spawn rarity, biome, etc.              
         }
     }
     
     
     /**
     * Register fuel handlers
     */
     protected void registerFuelHandlers()
     {
         // DEBUG
        System.out.println("Registering fuel handlers");
        
        // example: GameRegistry.registerFuelHandler(handler);
     }
 
    /**
     * Register event listeners
     */
    protected void registerEventListeners() 
    {
        // DEBUG
        System.out.println("Registering event listeners");

        MinecraftForge.EVENT_BUS.register(new EventHandler());
        MinecraftForge.TERRAIN_GEN_BUS.register(new TerrainGenEventHandler());
        MinecraftForge.ORE_GEN_BUS.register(new OreGenEventHandler());        
    }
    
    /**
     * Register Advancements
     */
    protected void registerAdvancements()
    {
//        BlockSmith.AdvancementTanningAHide = new Advancement("Advancement.tanningahide", "tanningahide", 0, 0, Items.LEATHER, (Advancement)null);
//        BlockSmith.AdvancementTanningAHide.registerStat().initIndependentStat(); // Eclipse is having trouble chaining these in previous line
////      BlockSmith.AdvancementGiantSlayer = new Advancement("Advancement.giantslayer", "giantslayer", 2, 1, (Item)null, BlockSmith.AdvancementTanningAHide).setSpecial();
////      BlockSmith.AdvancementGiantSlayer.registerStat(); // Eclipse is having trouble chaining this in previous line
//        BlockSmith.craftTable = new Advancement("createDecraftTable", "createDecraftTable", 1 - 2 - 2, -1 - 3, BlockSmith.blockDeconstructor, null).registerStat();
//        BlockSmith.deconstructAny = new Advancement("deconstructAnything", "deconstructAnything", 2 - 2, -2 - 2, Items.DIAMOND_HOE, BlockSmith.craftTable).registerStat();
//        BlockSmith.deconstructDiamondHoe = new Advancement("deconstructDiamondHoe", "deconstructDiamondHoe", 2 - 2, 0 - 2, Items.DIAMOND_HOE, BlockSmith.deconstructAny).registerStat();
//        BlockSmith.deconstructJunk = new Advancement("deconstructJunk", "deconstructJunk", 1 - 2, -1 - 2, Items.LEATHER_BOOTS, BlockSmith.deconstructAny).registerStat();
//        BlockSmith.deconstructDiamondShovel = new Advancement("deconstructDiamondShovel", "deconstructDiamondShovel", 3 - 2, -1 - 2, Items.DIAMOND_SHOVEL, BlockSmith.deconstructAny).registerStat();
//        BlockSmith.theHatStandAdvancement = new Advancement("porteManteauAdvancement", "porteManteauAdvancement", 3 - 2, -4 - 2, Blocks.OAK_FENCE, BlockSmith.craftTable).registerStat();
//        AdvancementPage.registerAdvancementPage(new AdvancementPage("BlockSmith",
//                new Advancement[]
//                {
//                BlockSmith.craftTable, BlockSmith.deconstructAny, BlockSmith.deconstructDiamondHoe, BlockSmith.deconstructJunk, BlockSmith.deconstructDiamondShovel, BlockSmith.theHatStandAdvancement
//                }));
//
//        BlockSmith.deconstructedItemsStat = (StatBasic) (new StatBasic("stat.deconstructeditems", new TextComponentTranslation("stat.deconstructeditems", new Object[0])).registerStat());
//        
    }
    
    protected void initItemStackRegistry()
    {
        return;
    }

    public void setItemStackRegistry(List parRegistry)
    {
        itemStackRegistry = parRegistry;
    }
    
    public List<ItemStack> getItemStackRegistry()
    {
        return itemStackRegistry;
    }
        
    /*
     * Works directly on passed in ByteBuf to put ItemStack registry into packet payload to be sent to the server
     */
    public void convertItemStackListToPayload(ByteBuf parBuffer)
    {
        Iterator<ItemStack> theIterator = itemStackRegistry.iterator();
       
        while (theIterator.hasNext())
        {          
            ItemStack theStack = theIterator.next();
            
            // write item id and metadata
            parBuffer.writeInt(Item.getIdFromItem(theStack.getItem()));
            parBuffer.writeInt(theStack.getMetadata());
            
//            // DEBUG
//            System.out.println(Item.getIdFromItem(theStack.getItem())+" "+theStack.getMetadata());
            boolean hasNBT = theStack.hasTagCompound();
            parBuffer.writeBoolean(hasNBT);
            if (hasNBT)
            {
                // DEBUG
                System.out.println("The stack "+theStack.toString()+" has NBT = "+theStack.getTagCompound().toString());
                ByteBufUtils.writeTag(parBuffer, theStack.getTagCompound());
            }
            theIterator.remove(); // avoids a ConcurrentModificationException
        }
        
        return ;
    }


    /*
     * Provides a list of item stacks giving every registered item along with its metadata variants
     * based on a message payload from the client that gives the valid metadata values for those
     * items with variants. Also will include NBT for mods like Tinker's Construct that use NBT on the
     * ItemStacks to make variants instead of metadata.
     */
    public List<ItemStack> convertPayloadToItemStackList(ByteBuf theBuffer)
    {
        List<ItemStack> theList = new ArrayList<ItemStack>();
        
        while (theBuffer.isReadable())
        {
            int theID = theBuffer.readInt();
            int theMetadata = theBuffer.readInt();
            ItemStack theStack = new ItemStack(Item.getItemById(theID), 1, theMetadata);
            
            // Handle the case of mods like Tinker's Construct that use NBT instead of metadata
            boolean hasNBT = theBuffer.readBoolean();
            if (hasNBT)
            {
                theStack.setTagCompound(ByteBufUtils.readTag(theBuffer));
                // DEBUG
                System.out.println("The stack "+theStack.toString()+" has NBT = "+theStack.getTagCompound().toString());
            }
            
           theList.add(theStack);
        }

        // DEBUG
        System.out.println(theList.toString());

        return theList;      
    }
}