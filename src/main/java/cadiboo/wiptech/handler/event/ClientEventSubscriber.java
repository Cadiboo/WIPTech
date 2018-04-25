package cadiboo.wiptech.handler.event;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.block.BlockBase;
import cadiboo.wiptech.block.BlockStrongPistonExtension;
import cadiboo.wiptech.client.render.entity.RenderEntityNapalmFactory;
import cadiboo.wiptech.client.render.entity.RenderEntityParamagneticProjectile113;
import cadiboo.wiptech.client.render.entity.RenderEntityParamagneticProjectileFactory;
import cadiboo.wiptech.client.render.tileentity.TESRCrusher;
import cadiboo.wiptech.client.render.tileentity.TESRTurbine;
import cadiboo.wiptech.entity.projectile.EntityNapalm;
import cadiboo.wiptech.entity.projectile.EntityParamagneticProjectile;
import cadiboo.wiptech.entity.projectile.EntityParamagneticProjectile113;
import cadiboo.wiptech.handler.EnumHandler.ParamagneticProjectiles;
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

		blockItemModels: for (int i = 0; i < Blocks.BLOCKS.length; i++) {
			if (Blocks.BLOCKS[i] instanceof BlockStrongPistonExtension)
				continue blockItemModels;
			if (Blocks.BLOCKS[i] instanceof BlockBase && ((BlockBase) Blocks.BLOCKS[i]).isHiddenBlock())
				continue blockItemModels;

			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Blocks.BLOCKS[i]), 0, new ModelResourceLocation(Blocks.BLOCKS[i].getRegistryName(), "inventory"));
		}

		for (int i = 0; i < Items.ITEMS.length; i++) {
			if (!Items.ITEMS[i].getHasSubtypes()) {
				ModelLoader.setCustomModelResourceLocation(Items.ITEMS[i], 0, new ModelResourceLocation(Items.ITEMS[i].getRegistryName(), "inventory"));
			} else if (Items.ITEMS[i] instanceof ItemParamagneticProjectile) {
				for (int j = 0; j < ParamagneticProjectiles.values().length - 1; j++) {
					ModelLoader.setCustomModelResourceLocation(Items.ITEMS[i], ParamagneticProjectiles.values()[j].getID(),
							new ModelResourceLocation(Reference.ID + ":rods/" + ParamagneticProjectiles.values()[j].getName()));
				}
			}
		}

		// for (int i = 0; i < Items.ITEMS.length; i++) {
		// ModelLoader.setCustomModelResourceLocation(Items.ITEMS[i], 0, new
		// ModelResourceLocation(Items.ITEMS[i].getRegistryName(), "inventory"));
		// }
		WIPTech.logger.info("Registered models");

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrusher.class, new TESRCrusher());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTurbine.class, new TESRTurbine());
		WIPTech.logger.info("Registered TileEntity Renders");

		// for (int i = 0; i < Entities.ENTITIES.length; i++) {
		RenderingRegistry.registerEntityRenderingHandler(EntityParamagneticProjectile113.class, renderManager -> new RenderEntityParamagneticProjectile113(renderManager));
		// }

		RenderingRegistry.registerEntityRenderingHandler(EntityNapalm.class, new RenderEntityNapalmFactory());
		RenderingRegistry.registerEntityRenderingHandler(EntityParamagneticProjectile.class, new RenderEntityParamagneticProjectileFactory());

		WIPTech.logger.info("Registered Entity Renders");
	}
}
