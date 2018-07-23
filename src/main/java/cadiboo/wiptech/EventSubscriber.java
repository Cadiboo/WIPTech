package cadiboo.wiptech;

import cadiboo.wiptech.block.BlockResource;
import cadiboo.wiptech.init.ModBlocks;
import cadiboo.wiptech.init.ModItems;
import cadiboo.wiptech.item.ItemRail;
import cadiboo.wiptech.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = Reference.ID)
public final class EventSubscriber {

	/**
	 * Register this mod's {@link Item}s.
	 *
	 * @param event The event
	 */

	@SubscribeEvent
	public static void onRegisterItems(final RegistryEvent.Register<Item> event) {

		final IForgeRegistry<Item> registry = event.getRegistry();

		registry.register(new ItemRail("rail"));

		registry.register(new ItemBlock(ModBlocks.COPPER_BLOCK) {
			@Override
			public int getItemBurnTime(ItemStack itemStack) {
				return 300;
			}
		});

	}

	/**
	 * Register this mod's {@link Block}s.
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void onRegisterBlocks(final RegistryEvent.Register<Block> event) {
		final IForgeRegistry<Block> registry = event.getRegistry();

		registry.register(new BlockResource(Material.IRON, "copper_block"));

	}

	/**
	 * ModelRegistryEvent handler.
	 *
	 * @param event the event
	 */
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static final void onRegisterModels(final ModelRegistryEvent event) {

		/*
		 * Register standard model items
		 */

		ModelLoader.setCustomModelResourceLocation(ModItems.COPPER_RAIL, 0,
				new ModelResourceLocation(ModItems.COPPER_RAIL.getRegistryName(), "inventory"));

		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.COPPER_BLOCK), 0,
				new ModelResourceLocation(Reference.ID + ":" + ModBlocks.COPPER_BLOCK.getRegistryName(), "inventory"));
	}

	/*
	 * Register custom model items
	 */
	// ModelLoaderRegistry.registerLoader(ModelSlimeBag.CustomModelLoader.INSTANCE);
	// ModelLoader.setCustomMeshDefinition(slime_bag, stack ->
	// ModelSlimeBag.LOCATION);
	// ModelBakery.registerItemVariants(slime_bag, ModelSlimeBag.LOCATION);
	}

	private static final void registerBlockModel(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0,
				new ModelResourceLocation(block.getRegistryName(), "inventory"));
	}

}
