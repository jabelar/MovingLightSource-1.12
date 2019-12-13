//package com.blogspot.jabelarminecraft.movinglightsource.registries;
//
//import com.blogspot.jabelarminecraft.movinglightsource.ItemFlashlight;
//import com.blogspot.jabelarminecraft.movinglightsource.MainMod;
//import com.blogspot.jabelarminecraft.movinglightsource.utilities.Utilities;
//
//import net.minecraft.client.renderer.block.model.ModelResourceLocation;
//import net.minecraft.item.Item;
//import net.minecraftforge.client.event.ModelRegistryEvent;
//import net.minecraftforge.client.model.ModelLoader;
//import net.minecraftforge.event.RegistryEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//import net.minecraftforge.registries.IForgeRegistry;
//
//@ObjectHolder(MainMod.MODID)
//public class ItemRegistry
//{
//    public static final ItemFlashlight flashlight = null;
//
//    @Mod.EventBusSubscriber(modid = MainMod.MODID)
//    static class RegistrationHandler
//    {
//        /**
//         * Register this mod's {@link Item}s.
//         *
//         * @param event The event
//         */
//        @SubscribeEvent
//        public static void registerItems(final RegistryEvent.Register<Item> event)
//        {
//            final IForgeRegistry<Item> registry = event.getRegistry();
//    
//            registry.register(Utilities.setItemName(new ItemFlashlight(), "flashlight"));
//        }
//    
//        /**
//         * ModelRegistryEvent handler.
//         *
//         * @param event the event
//         */
//        @SubscribeEvent
//        @SideOnly(Side.CLIENT)
//        public static void onModelEvent(final ModelRegistryEvent event)
//        {
//    //        // DEBUG
//    //        System.out.println("Registering item models");
//    
//            /*
//             *  Register standard model items
//             */
//            registerItemModel(flashlight);
//    
//    //        /*
//    //         *  Register custom model items
//    //         */
//    //        // DEBUG
//    //        System.out.println("Registering custom item models");
//    //        ModelLoaderRegistry.registerLoader(ModelSlimeBag.CustomModelLoader.INSTANCE);
//    //        ModelLoader.setCustomMeshDefinition(slime_bag, stack -> ModelSlimeBag.LOCATION);
//    //        ModelBakery.registerItemVariants(slime_bag, ModelSlimeBag.LOCATION);
//        }
//    }
//
//    /**
//     * Register item model.
//     *
//     * @param parItem the item
//     */
//    @SideOnly(Side.CLIENT)
//    public static void registerItemModel(Item parItem)
//    {
//        registerItemModel(parItem, 0);
//    }
//    
//    /**
//     * Register item model.
//     *
//     * @param parItem the par item
//     * @param parMetaData the par meta data
//     */
//    @SideOnly(Side.CLIENT)
//    public static void registerItemModel(Item parItem, int parMetaData)
//    {
//    //    // DEBUG
//    //    System.out.println("Registering item model for: " + parItem.getRegistryName());
//    
//        ModelLoader.setCustomModelResourceLocation(parItem, parMetaData,
//                new ModelResourceLocation(parItem.getRegistryName(), "inventory"));
//    }
//}
