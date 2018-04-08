package cadiboo.wiptech.client.render.tileentity;

import java.util.Random;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.block.BlockCrusher;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.tileentity.TileEntityCrusher;
import cadiboo.wiptech.util.Clausen;
import cadiboo.wiptech.util.Utils;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;

public class TESRCrusher extends TileEntitySpecialRenderer<TileEntityCrusher> {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final ItemStack CRUSHER_BIT_HOLDER = new ItemStack(Items.CRUSHER_BIT_HOLDER);

	private boolean isItemBlock(ItemStack stack) {
		return stack.getItem() instanceof ItemBlock;
	}

	private void renderStack(ItemStack stack, World world) {
		IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, world, null);
		model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);

		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);
	}

	@Override
	public void render(TileEntityCrusher te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		ItemStack stackCrusherBit = te.inventory.getStackInSlot(0);
		ItemStack stackToCrush = te.inventory.getStackInSlot(1);

		// GlStateManager.enableRescaleNormal();
		// GlStateManager.alphaFunc(516, 0.1F);
		// GlStateManager.enableBlend();
		// RenderHelper.enableStandardItemLighting();
		// GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

		EnumFacing enumfacing = te.getWorld().getBlockState(te.getPos()).getValue(FACING);
		if (!stackCrusherBit.isEmpty()) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5D, y + 0.56D - 0.3D, z + 0.5D);
			if (te.isCrushing()) {
				double pos = Clausen.cl2(te.getFractionOfCrushTimeRemaining() * 25.0D) / 13;
				GlStateManager.translate(0.0D, pos, 0.0D);
				if (stackCrusherBit.getItem() == Items.CRUSHER_BIT && Minecraft.getMinecraft().inGameHasFocus) {
					if (pos > -0.05 && pos < 0.05 && pos < te.lastCrushAnimation)
						((BlockCrusher) Utils.getBlockFromPos(te.getWorld(), te.getPos())).animateCrush(
								Utils.getStateFromPos(te.getWorld(), te.getPos()), te.getWorld(), te.getPos(),
								new Random());
				}
				te.lastCrushAnimation = pos;
			}
			double offset = 0.2D;
			switch (enumfacing) {
			case NORTH:
				GlStateManager.translate(0.0D, 0.0D, offset);
				break;
			case EAST:
				GlStateManager.translate(-offset, 0.0D, 0.0D);
				break;
			case SOUTH:
				GlStateManager.translate(0.0D, 0.0D, -offset);
				break;
			case WEST:
				GlStateManager.translate(offset, 0.0D, 0.0D);
				break;
			default:
				GlStateManager.translate(0.0F, 0.0F, 0.0F);
				WIPTech.logger.info("Crusher Rendering Offset Error!!");
			}
			double smalloffset = 0.01D;
			switch (enumfacing) {
			case NORTH:
				GlStateManager.translate(0.0D, 0.0D, -smalloffset);
				break;
			case EAST:
				GlStateManager.translate(smalloffset, 0.0D, 0.0D);
				break;
			case SOUTH:
				GlStateManager.translate(0.0D, 0.0D, smalloffset);
				break;
			case WEST:
				GlStateManager.translate(-smalloffset, 0.0D, 0.0D);
				break;
			default:
				GlStateManager.translate(0.0F, 0.0F, 0.0F);
				WIPTech.logger.info("Crusher Rendering Small Offset Error!!");
			}
			if (stackCrusherBit.getItem() == Items.HAMMER) {
				GlStateManager.translate(0.0D, 0.5D, 0.0D);
				GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
				renderStack(stackCrusherBit, te.getWorld());
			} else {
				double itemScale = 0.25D;
				double renderScale = 0.375D;
				double scale = 1.4925D;

				GlStateManager.scale(scale, scale, scale);
				renderStack(CRUSHER_BIT_HOLDER, te.getWorld());
				scale *= 2.65D;

				GlStateManager.translate(0.0D, -0.73D, 0.0D);
				GlStateManager.scale(scale, scale, scale);

				renderStack(stackCrusherBit, te.getWorld());
			}
			GlStateManager.popMatrix();
		}
		if (!stackToCrush.isEmpty()) {
			ItemStack stack = stackToCrush;
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5D, y, z + 0.5D);

			double blockoffset = 0.2D;
			double itemoffset = 0.0D;

			boolean isItem = !isItemBlock(stack);
			switch (enumfacing) {
			case NORTH:
				GlStateManager.translate(0.0D, 0.0D, isItem ? itemoffset : blockoffset);
				break;
			case EAST:
				GlStateManager.translate(-(isItem ? itemoffset : blockoffset), 0.0D, 0.0D);
				break;
			case SOUTH:
				GlStateManager.translate(0.0D, 0.0D, -(isItem ? itemoffset : blockoffset));
				break;
			case WEST:
				GlStateManager.translate(isItem ? itemoffset : blockoffset, 0.0D, 0.0D);
				break;
			default:
				GlStateManager.translate(0.0F, 0.0F, 0.0F);
				WIPTech.logger.info("Crusher Rendering StackToCrush Offset Error!!");
			}
			GlStateManager.rotate(-90 * te.getWorld().getBlockState(te.getPos()).getValue(FACING).getHorizontalIndex(),
					0.0F, 1.0F, 0.0F);
			if (isItem) {
				GlStateManager.translate(0.0D, 0.26D, 0.0D);
				GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
			} else {
				GlStateManager.translate(0.0D, 0.0D, 0.0D);
			}
			renderStack(stack, te.getWorld());
			GlStateManager.popMatrix();
		}

		// GlStateManager.disableRescaleNormal();
		// RenderHelper.disableStandardItemLighting();
		// GlStateManager.disableBlend();
	}

}
