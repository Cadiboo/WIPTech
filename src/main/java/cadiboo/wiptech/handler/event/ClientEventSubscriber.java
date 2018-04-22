package cadiboo.wiptech.handler.event;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.block.BlockPeripheralBlock;
import cadiboo.wiptech.block.BlockResourceBlock113;
import cadiboo.wiptech.block.BlockWire;
import cadiboo.wiptech.client.render.entity.RenderEntityNapalmFactory;
import cadiboo.wiptech.client.render.entity.RenderEntityParamagneticProjectileFactory;
import cadiboo.wiptech.client.render.tileentity.TESRCrusher;
import cadiboo.wiptech.client.render.tileentity.TESRTurbine;
import cadiboo.wiptech.entity.projectile.EntityNapalm;
import cadiboo.wiptech.entity.projectile.EntityParamagneticProjectile;
import cadiboo.wiptech.handler.EnumHandler.ParamagneticProjectiles;
import cadiboo.wiptech.handler.EnumHandler.ResourceBlocks;
import cadiboo.wiptech.init.Blocks;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.item.ItemParamagneticProjectile;
import cadiboo.wiptech.tileentity.TileEntityCrusher;
import cadiboo.wiptech.tileentity.TileEntityTurbine;
import cadiboo.wiptech.util.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Reference.ID)
public class ClientEventSubscriber {

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {

		blocks: for (int i = 0; i < Blocks.BLOCKS.length; i++) {
			if (Blocks.BLOCKS[i] instanceof BlockPeripheralBlock)
				continue blocks;

			WIPTech.logger.info(Blocks.BLOCKS[i]);

			if (Blocks.BLOCKS[i] instanceof BlockResourceBlock113) {

				for (int j = 0; j < ResourceBlocks.values().length; j++) {

					ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Blocks.BLOCKS[i]), ResourceBlocks.values()[j].getID(),
							new ModelResourceLocation(Blocks.BLOCKS[i].getRegistryName(), "resource=" + ResourceBlocks.values()[j].getName()));

					// WIPTech.logger.info(new
					// ModelResourceLocation(Blocks.BLOCKS[i].getRegistryName(), "inventory"));
					// ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Blocks.BLOCKS[i]),
					// Metals.values()[j].getID(),
					// new ModelResourceLocation(Reference.ID + ":" + Metals.values()[j].getName() +
					// "_block"));
					// WIPTech.logger.info(Item.getItemFromBlock(Blocks.BLOCKS[i]) + " | " +
					// Metals.values()[j].getID()
					// + " | "
					// + new ModelResourceLocation(Reference.ID + ":" + Metals.values()[j].getName()
					// + "_block"));
				}

				// for (int j = 0; j < Metals.values().length; j++) {
				//
				// ModelResourceLocation blockModelResourceLocation = new ModelResourceLocation(
				// new ResourceLocation(Reference.ID, Metals.byID(j).getName() + "_block"),
				// "normal");
				//
				// ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(
				// Reference.ID + ":" + Metals.byID(j).getName() + "_block", "inventory");
				// //
				// ModelLoader.setCustomModelResourceLocation(StartupCommon.itemBlockVariants,
				// // BlockVariants.EnumColour.BLUE.getMetadata(), itemModelResourceLocation);
				//
				// // Metals.byID(j).getName() + "_block"
				// ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Blocks.BLOCKS[j]),
				// j,
				// blockModelResourceLocation);
				// }
			} else if (Blocks.BLOCKS[i] instanceof BlockWire) {

			} else {
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Blocks.BLOCKS[i]), 0,
						new ModelResourceLocation(Blocks.BLOCKS[i].getRegistryName(), "inventory"));
			}
		}

		for (int i = 0; i < Items.ITEMS.length; i++) {
			if (!Items.ITEMS[i].getHasSubtypes()) {
				ModelLoader.setCustomModelResourceLocation(Items.ITEMS[i], 0,
						new ModelResourceLocation(Items.ITEMS[i].getRegistryName(), "inventory"));
			} else if (Items.ITEMS[i] instanceof ItemParamagneticProjectile) {
				for (int j = 0; j < ParamagneticProjectiles.values().length; j++) {
					ModelLoader.setCustomModelResourceLocation(Items.ITEMS[i], ParamagneticProjectiles.values()[j].getID(),
							new ModelResourceLocation(Reference.ID + ":rods/" + ParamagneticProjectiles.values()[j].getName()));
				}
			}
		}
		WIPTech.logger.info("Registered models");

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrusher.class, new TESRCrusher());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTurbine.class, new TESRTurbine());
		WIPTech.logger.info("Registered TileEntity Renders");

		/*
		 * for (EntityEntry entity: Entities.ENTITIES) {
		 * 
		 * }
		 */

		RenderingRegistry.registerEntityRenderingHandler(EntityNapalm.class, new RenderEntityNapalmFactory());
		RenderingRegistry.registerEntityRenderingHandler(EntityParamagneticProjectile.class, new RenderEntityParamagneticProjectileFactory());

		WIPTech.logger.info("Registered Entity Renders");
	}
}
