package cadiboo.wiptech.handler.event;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.block.BlockMetalBlock;
import cadiboo.wiptech.client.render.entity.RenderEntityNapalmFactory;
import cadiboo.wiptech.client.render.entity.RenderEntityParamagneticProjectileFactory;
import cadiboo.wiptech.client.render.tileentity.TESRCrusher;
import cadiboo.wiptech.client.render.tileentity.TESRTurbine;
import cadiboo.wiptech.entity.projectile.EntityNapalm;
import cadiboo.wiptech.entity.projectile.EntityParamagneticProjectile;
import cadiboo.wiptech.handler.EnumHandler;
import cadiboo.wiptech.handler.EnumHandler.Metals;
import cadiboo.wiptech.handler.EnumHandler.ParamagneticProjectiles;
import cadiboo.wiptech.init.Blocks;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.item.ItemParamagneticProjectile;
import cadiboo.wiptech.tileentity.TileEntityCrusher;
import cadiboo.wiptech.tileentity.TileEntityTurbine;
import cadiboo.wiptech.util.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
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
		for (int i = 0; i < Blocks.BLOCKS.length; i++) {
			if (!new ItemBlock(Blocks.BLOCKS[i]).getHasSubtypes())
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Blocks.BLOCKS[i]), 0,
						new ModelResourceLocation(Blocks.BLOCKS[i].getRegistryName(), "inventory"));
			else {
				if (Blocks.BLOCKS[i] instanceof BlockMetalBlock)
					for (int j = 0; j < Metals.values().length; i++) {
						ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Blocks.BLOCKS[i]), 0,
								new ModelResourceLocation(Reference.ID + ":" + Metals.byID(j).getName() + "_block",
										"inventory"));
					}
			}
		}

		for (Item item : Items.ITEMS) {
			if (!item.getHasSubtypes()) {
				ModelLoader.setCustomModelResourceLocation(item, 0,
						new ModelResourceLocation(item.getRegistryName(), "inventory"));
			} else {

				if (item instanceof ItemParamagneticProjectile) {
					for (ParamagneticProjectiles rod : EnumHandler.ParamagneticProjectiles.values()) {
						ModelLoader.setCustomModelResourceLocation(item, rod.getID(),
								new ModelResourceLocation(Reference.ID + ":rods/" + rod.getName()));
					}
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
		RenderingRegistry.registerEntityRenderingHandler(EntityParamagneticProjectile.class,
				new RenderEntityParamagneticProjectileFactory());

		WIPTech.logger.info("Registered Entity Renders");
	}
}
