package cadiboo.wiptech.client;

import org.lwjgl.opengl.GL11;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.block.BlockPeripheralBlock;
import cadiboo.wiptech.block.BlockWire;
import cadiboo.wiptech.client.render.entity.Render2D;
import cadiboo.wiptech.client.render.entity.RenderEntityParamagneticProjectile113;
import cadiboo.wiptech.client.render.entity.RenderEntityParamagneticProjectileFactory;
import cadiboo.wiptech.client.render.tileentity.TESRCrusher;
import cadiboo.wiptech.client.render.tileentity.TESRTurbine;
import cadiboo.wiptech.client.render.tileentity.TESRWire;
import cadiboo.wiptech.entity.projectile.EntityNapalm;
import cadiboo.wiptech.entity.projectile.EntityParamagneticProjectile;
import cadiboo.wiptech.entity.projectile.EntityParamagneticProjectile113;
import cadiboo.wiptech.handler.EnumHandler.ParamagneticProjectiles;
import cadiboo.wiptech.init.Blocks;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.item.ItemParamagneticProjectile;
import cadiboo.wiptech.tileentity.TileEntityCrusher;
import cadiboo.wiptech.tileentity.TileEntityTurbine;
import cadiboo.wiptech.tileentity.TileEntityWire;
import cadiboo.wiptech.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Reference.ID)
public class EventSubscriber {

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {

		blockItemModels: for (int i = 0; i < Blocks.BLOCKS.length; i++) {
			if (Blocks.getHiddenBlocks().contains(Blocks.BLOCKS[i]))
				continue blockItemModels;

			WIPTech.logger.info(Blocks.BLOCKS[i]);

			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Blocks.BLOCKS[i]), 0, new ModelResourceLocation(Blocks.BLOCKS[i].getRegistryName(), "inventory"));
		}

		for (int i = 0; i < Items.ITEMS.length; i++) {
			WIPTech.logger.info(Items.ITEMS[i]);
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
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWire.class, new TESRWire());
		WIPTech.logger.info("Registered TileEntity Renders");

		RenderingRegistry.registerEntityRenderingHandler(EntityParamagneticProjectile113.class, renderManager -> new RenderEntityParamagneticProjectile113(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityNapalm.class, renderManager -> new Render2D(renderManager, new ResourceLocation(Reference.ID, "textures/entities/napalm.png"), 0.25F));
		RenderingRegistry.registerEntityRenderingHandler(EntityParamagneticProjectile.class, new RenderEntityParamagneticProjectileFactory());
		WIPTech.logger.info("Registered Entity Renders");
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onRenderGameOverlay(final RenderGameOverlayEvent.Post event) {

		if (!(event.getType() == RenderGameOverlayEvent.ElementType.ALL) || Minecraft.getMinecraft().currentScreen != null)
			return;
		Minecraft mc = Minecraft.getMinecraft();
		RayTraceResult posHit = mc.objectMouseOver;
		if (posHit != null && posHit.getBlockPos() != null) {
			Block blockHit = mc.world.getBlockState(posHit.getBlockPos()).getBlock();
			TileEntity tileHit = mc.world.getTileEntity(posHit.getBlockPos());

			// WIPTech.logger.info(posHit);
			// WIPTech.logger.info(posHit.getBlockPos());

			if (blockHit instanceof BlockPeripheralBlock)
				tileHit = mc.world.getTileEntity(((BlockPeripheralBlock) blockHit).getTileEntityPos(mc.world, posHit.getBlockPos()));

			if (tileHit != null) {
				IEnergyStorage energy = tileHit.getCapability(CapabilityEnergy.ENERGY, null);
				if (energy != null) {
					double power = (double) energy.getEnergyStored() / (double) energy.getMaxEnergyStored();
					int scaled_height = (int) Math.round((1 - power) * 52D);
					ScaledResolution Scaled = new ScaledResolution(Minecraft.getMinecraft());
					int Width = Scaled.getScaledWidth() - 10;
					int Height = Scaled.getScaledHeight() - 54;

					Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(Reference.ID, "textures/gui/turbine.png"));

					drawNonStandardTexturedRect(Width, Height, 83, 16, 10, 54, 256, 256);
					drawNonStandardTexturedRect(Width + 1, Height + 1 + scaled_height, 176, 0, 8, 52 - scaled_height, 256, 256);
					int percent = (int) Math.round(power * 100);
					mc.fontRenderer.drawStringWithShadow(percent + "%", Width - 7 - String.valueOf(percent).length() * 6, Height + 35, 0xFFFFFF);
					String outOf = energy.getEnergyStored() + "/" + energy.getMaxEnergyStored();
					mc.fontRenderer.drawStringWithShadow(outOf, Width - 1 - outOf.length() * 6, Height + 45, 0xFFFFFF);

				}
			}

		}
	}

	protected static void drawNonStandardTexturedRect(int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight) {
		double f = 1F / (double) textureWidth;
		double f1 = 1F / (double) textureHeight;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(x, y + height, 0).tex(u * f, (v + height) * f1).endVertex();
		bufferbuilder.pos(x + width, y + height, 0).tex((u + width) * f, (v + height) * f1).endVertex();
		bufferbuilder.pos(x + width, y, 0).tex((u + width) * f, v * f1).endVertex();
		bufferbuilder.pos(x, y, 0).tex(u * f, v * f1).endVertex();
		tessellator.draw();
	}

	@SubscribeEvent
	public static void onTooltipEvent(final ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();

		if (Block.getBlockFromItem(stack.getItem()) instanceof BlockWire) {
			event.getToolTip().add(WIPTech.proxy.localize("wire.tooltip", new Object[0]));
		}

		if (stack.getItem().getRegistryName().getResourceDomain().equalsIgnoreCase(Reference.ID)) {
			String itemTooltip = WIPTech.proxy.localize(stack.getUnlocalizedName() + ".tooltip", new Object[0]);
			if (!itemTooltip.equalsIgnoreCase(stack.getUnlocalizedName() + ".tooltip"))
				event.getToolTip().add(itemTooltip);
		}
	}

}
