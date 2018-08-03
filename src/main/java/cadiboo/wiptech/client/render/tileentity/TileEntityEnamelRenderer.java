package cadiboo.wiptech.client.render.tileentity;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.block.BlockEnamel;
import cadiboo.wiptech.block.BlockWire;
import cadiboo.wiptech.client.ClientUtil;
import cadiboo.wiptech.client.model.ModelsCache;
import cadiboo.wiptech.tileentity.TileEntityEnamel;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModWritingUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TileEntityEnamelRenderer extends ModTileEntitySpecialRenderer<TileEntityEnamel> {

	protected final ItemStack stack = new ItemStack(Items.IRON_INGOT);

	@Override
	public void renderAtCentre(TileEntityEnamel te, float partialTicks, int destroyStage, float alpha) {

		if (!(te.getBlockType() instanceof BlockEnamel)) {
			WIPTech.error("Error rendering TileEntityWire! block at location is not an instanceof BlockWire!");
			new Exception().printStackTrace();
			return;
		}

		ModMaterials material = ((BlockWire) te.getBlockType()).getModMaterial();
		ResourceLocation enamelRegistryName = material.getEnamel().getRegistryName();
		ResourceLocation extensionRegistryName = new ResourceLocation(enamelRegistryName.getResourceDomain(), enamelRegistryName.getResourcePath() + "_extension");
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
