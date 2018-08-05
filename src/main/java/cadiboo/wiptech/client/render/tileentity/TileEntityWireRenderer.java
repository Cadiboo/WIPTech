package cadiboo.wiptech.client.render.tileentity;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.block.BlockWire;
import cadiboo.wiptech.client.ClientUtil;
import cadiboo.wiptech.client.model.ModelsCache;
import cadiboo.wiptech.tileentity.TileEntityWire;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModWritingUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TileEntityWireRenderer extends ModTileEntitySpecialRenderer<TileEntityWire> {

	protected static final ItemStack stack = new ItemStack(net.minecraft.init.Items.IRON_INGOT);

	@Override
	public void renderAtCentre(TileEntityWire te, float partialTicks, int destroyStage, float alpha) {

		if (true)
			return;

		if (!(te.getBlockType() instanceof BlockWire)) {
			WIPTech.error("Error rendering TileEntityWire! block at location is not an instanceof BlockWire!");
			new Exception().printStackTrace();
			return;
		}

		ModMaterials material = ((BlockWire) te.getBlockType()).getModMaterial();
		ResourceLocation wireRegistryName = material.getWire().getRegistryName();
		ResourceLocation extensionRegistryName = new ResourceLocation(wireRegistryName.getResourceDomain(), wireRegistryName.getResourcePath() + "_extension");
		ModelResourceLocation materialWireExtensionLocation = new ModelResourceLocation(extensionRegistryName, ModWritingUtil.default_variant_name);

		IBakedModel extension = ModelsCache.INSTANCE.getOrLoadBakedModel(materialWireExtensionLocation);

		for (EnumFacing side : EnumFacing.VALUES) {
			if (te.isConnectedTo(side)) {
				GlStateManager.pushMatrix();
				GlStateManager.enableLighting();
				ClientUtil.rotateForFace(side);

				Minecraft.getMinecraft().getRenderItem().renderItem(stack, extension);

				GlStateManager.disableLighting();
				GlStateManager.popMatrix();
			}
		}

	}

}