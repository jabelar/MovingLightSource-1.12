package com.blogspot.jabelarminecraft.movinglightsource.registries;

import com.blogspot.jabelarminecraft.movinglightsource.MainMod;
import com.blogspot.jabelarminecraft.movinglightsource.blocks.BlockMovingLightSource;
import com.blogspot.jabelarminecraft.movinglightsource.utilities.Utilities;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(MainMod.MODID)
public class BlockRegistry
{
     // instantiate blocks
    public final static BlockMovingLightSource movinglightsource = null;
    public final static BlockMovingLightSource movinglightsource_15 = null;
    public final static BlockMovingLightSource movinglightsource_14 = null;
    public final static BlockMovingLightSource movinglightsource_13 = null;
    public final static BlockMovingLightSource movinglightsource_12 = null;
    public final static BlockMovingLightSource movinglightsource_11 = null;
    public final static BlockMovingLightSource movinglightsource_9 = null;
    public final static BlockMovingLightSource movinglightsource_7 = null;
    
    // instantiate itemblocks
    @ObjectHolder("movinglightsource")
    public final static ItemBlock itemblock_movinglightsource = null;
    @ObjectHolder("movinglightsource_15")
    public final static ItemBlock itemblock_movinglightsource_15 = null;
    @ObjectHolder("movinglightsource_14")
    public final static ItemBlock itemblock_movinglightsource_14 = null;
    @ObjectHolder("movinglightsource_13")
    public final static ItemBlock itemblock_movinglightsource_13 = null;
    @ObjectHolder("movinglightsource_12")
    public final static ItemBlock itemblock_movinglightsource_12 = null;
    @ObjectHolder("movinglightsource_11")
    public final static ItemBlock itemblock_movinglightsource_11 = null;
    @ObjectHolder("movinglightsource_9")
    public final static ItemBlock itemblock_movinglightsource_9 = null;
    @ObjectHolder("movinglightsource_7")
    public final static ItemBlock itemblock_movinglightsource_7 = null;
    

    @Mod.EventBusSubscriber(modid = MainMod.MODID)
    static class RegistrationHandler
    {
        /**
         * Register this mod's {@link Block}s.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void onEvent(final RegistryEvent.Register<Block> event)
        {
            final IForgeRegistry<Block> registry = event.getRegistry();
            
            registry.register(new BlockMovingLightSource("movinglightsource"));
            registry.register(new BlockMovingLightSource("movinglightsource_15", 1.0F));
            registry.register(new BlockMovingLightSource("movinglightsource_14", 14.0F / 15.0F));
            registry.register(new BlockMovingLightSource("movinglightsource_13", 13.0F / 15.0F));
            registry.register(new BlockMovingLightSource("movinglightsource_12", 12.0F / 15.0F));
            registry.register(new BlockMovingLightSource("movinglightsource_11", 11.0F / 15.0F));
            registry.register(new BlockMovingLightSource("movinglightsource_9", 9.0F / 15.0F));
            registry.register(new BlockMovingLightSource("movinglightsource_7", 7.0F / 15.0F));
        }

        /**
         * Register this mod's {@link ItemBlock}s.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void registerItemBlocks(final RegistryEvent.Register<Item> event)
        {
            final IForgeRegistry<Item> registry = event.getRegistry();

            registry.register(Utilities.setItemName(new ItemBlock(movinglightsource), movinglightsource.getRegistryName().getPath()));
            registry.register(Utilities.setItemName(new ItemBlock(movinglightsource_15), movinglightsource_15.getRegistryName().getPath()));
            registry.register(Utilities.setItemName(new ItemBlock(movinglightsource_14), movinglightsource_14.getRegistryName().getPath()));
            registry.register(Utilities.setItemName(new ItemBlock(movinglightsource_13), movinglightsource_13.getRegistryName().getPath()));
            registry.register(Utilities.setItemName(new ItemBlock(movinglightsource_12), movinglightsource_12.getRegistryName().getPath()));
            registry.register(Utilities.setItemName(new ItemBlock(movinglightsource_11), movinglightsource_11.getRegistryName().getPath()));
            registry.register(Utilities.setItemName(new ItemBlock(movinglightsource_9), movinglightsource_9.getRegistryName().getPath()));
            registry.register(Utilities.setItemName(new ItemBlock(movinglightsource_7), movinglightsource_7.getRegistryName().getPath()));
        }
        
        /**
         * On model event.
         *
         * @param event the event
         */
        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void onModelEvent(final ModelRegistryEvent event)
        {
            // DEBUG
            System.out.println("Registering block models");

            registerBlockModel(movinglightsource);
            registerBlockModel(movinglightsource_15);
            registerBlockModel(movinglightsource_14);
            registerBlockModel(movinglightsource_13);
            registerBlockModel(movinglightsource_12);
            registerBlockModel(movinglightsource_11);
            registerBlockModel(movinglightsource_9);
            registerBlockModel(movinglightsource_7);
            registerItemBlockModels();
        }
    }

    /**
     * Register block model.
     *
     * @param parBlock the par block
     */
    @SideOnly(Side.CLIENT)
    public static void registerBlockModel(Block parBlock)
    {
        registerBlockModel(parBlock, 0);
    }

    /**
     * Register block model.
     *
     * @param parBlock the par block
     * @param parMetaData the par meta data
     */
    @SideOnly(Side.CLIENT)
    public static void registerBlockModel(Block parBlock, int parMetaData)
    {
//        // DEBUG
//        System.out.println("Registering block model for"
//                + ": " + parBlock.getRegistryName());

        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(parBlock), parMetaData,
                new ModelResourceLocation(MainMod.MODID + ":" + parBlock.getTranslationKey().substring(5), "inventory"));
    }

    /**
     * Register item block models.
     */
    @SideOnly(Side.CLIENT)
    public static void registerItemBlockModels()
    {
        registerItemBlockModel(itemblock_movinglightsource);
        registerItemBlockModel(itemblock_movinglightsource_15);
        registerItemBlockModel(itemblock_movinglightsource_14);
        registerItemBlockModel(itemblock_movinglightsource_13);
        registerItemBlockModel(itemblock_movinglightsource_12);
        registerItemBlockModel(itemblock_movinglightsource_11);
        registerItemBlockModel(itemblock_movinglightsource_9);
        registerItemBlockModel(itemblock_movinglightsource_7);
    }

    /**
     * Register item block model.
     *
     * @param parBlock the item block
     */
    @SideOnly(Side.CLIENT)
    public static void registerItemBlockModel(ItemBlock parBlock)
    {
        registerItemBlockModel(parBlock, 0);
    }

    /**
     * Register item block model.
     *
     * @param parBlock the item block
     * @param parMetaData the metadata
     */
    @SideOnly(Side.CLIENT)
    public static void registerItemBlockModel(ItemBlock parBlock, int parMetaData)
    {
//        // DEBUG
//        System.out.println("Registering item block model for"
//                + ": " + parBlock.getRegistryName());
        
        ModelLoader.setCustomModelResourceLocation(parBlock, parMetaData,
                new ModelResourceLocation(parBlock.getRegistryName(), "inventory"));
    }
}
