package cadiboo.wiptech.block.crusher;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.utils.Clausen;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
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

	private boolean isItem(ItemStack stack)
	{
		return !(stack.getItem() instanceof ItemBlock);
	}

	private void renderModel(ItemStack stack, World world) {
		IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, world, null);
		model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);

		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);
	}

	@Override
	public void render(TileEntityCrusher te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		ItemStack stackCrusherBit = te.inventory.getStackInSlot(0);
		ItemStack stackToCrush = te.inventory.getStackInSlot(1);

		GlStateManager.enableRescaleNormal();
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
		GlStateManager.enableBlend();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		EnumFacing enumfacing = (EnumFacing)((IBlockState) te.getWorld().getBlockState(te.getPos())).getValue(FACING);

		if(!stackCrusherBit.isEmpty())
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5, y + 0.56, z + 0.5);
			if(TileEntityCrusher.isCrushing(te))
			{
				//double pos = Math.cos(TileEntityCrusher.getFractionOfCrushTimeRemaining(te)*(2*Math.PI));
				double pos = Clausen.cl2(TileEntityCrusher.getFractionOfCrushTimeRemaining(te)*25);
				GlStateManager.translate(0, pos/16, 0);
				if(pos<-0.9) {
					((BlockCrusher) te.getWorld().getBlockState(te.getPos()).getBlock()).animateCrush(te.getWorld().getBlockState(te.getPos()), te.getWorld(), te.getPos());
				}
			}
			//Utils.getLogger().info(pos);

			double offset = 0.2;

			switch(enumfacing){
			case NORTH:GlStateManager.translate(0, 0, offset);break;
			case EAST:GlStateManager.translate(-offset, 0, 0);break;
			case SOUTH:GlStateManager.translate(0, 0, -offset);break;
			case WEST:GlStateManager.translate(offset, 0, 0);break;
			default:GlStateManager.translate(0, 0, 0);break;
			}

			GlStateManager.scale(-0.374, 0.374, -0.374);
			renderModel(stackCrusherBit, te.getWorld());
			renderModel(new ItemStack(Items.CRUSHER_BIT_HOLDER), te.getWorld());

			GlStateManager.popMatrix();
		}
		if (!stackToCrush.isEmpty())
		{
			ItemStack stack = stackToCrush;
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5, y, z + 0.5);

			double blockoffset = 0.2;
			double itemoffset = 0.0; //could potentially be 0.1

			boolean isItem = isItem(stack);

			switch(enumfacing)
			{
			case NORTH:
				GlStateManager.translate(0, 0, (isItem?itemoffset:blockoffset));
				break;
			case EAST:
				GlStateManager.translate(-(isItem?itemoffset:blockoffset), 0, 0);
				break;
			case WEST:
				GlStateManager.translate((isItem?itemoffset:blockoffset), 0, 0);
				break;
			case SOUTH:
				GlStateManager.translate(0, 0, -(isItem?itemoffset:blockoffset));
				break;
			default:
				GlStateManager.translate(0, 0, 0);
				break;
			}

			//int tester = (int)(te.getWorld().getTotalWorldTime() + partialTicks) * 4;

			GlStateManager.rotate(-90 * ((IBlockState) te.getWorld().getBlockState(te.getPos())).getValue(FACING).getHorizontalIndex(), 0, 1, 0);
			//translates up a little to make it closer to crushing thing
			//only need to rotate if its an item
			if(isItem) {
				GlStateManager.translate(0, 0.26, 0);
				GlStateManager.rotate(-90, 1, 0, 0);
			} else {
				GlStateManager.translate(0, 0.0, 0);
			}

			renderModel(stack, te.getWorld());
			GlStateManager.popMatrix();
		}
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableBlend();
	}

	//@Override
	public void render_NEW(TileEntityCrusher te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		ItemStack stackCrusherBit = te.inventory.getStackInSlot(0);
		ItemStack stackToCrush = te.inventory.getStackInSlot(1);
		if (!stackToCrush.isEmpty() || !stackCrusherBit.isEmpty()) {
			List<ItemStack> stacksToRender = new ArrayList<ItemStack>();

			if(!stackCrusherBit.isEmpty()) {
				stacksToRender.add(stackCrusherBit);
			}
			if(!stackToCrush.isEmpty()) {
				stacksToRender.add(stackToCrush);
			}

			GlStateManager.enableRescaleNormal();
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
			GlStateManager.enableBlend();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

			double blockoffset = 0.0;
			double itemoffset = 0.0;
			double blocktranslateoffset = 0.0;
			double itemtranslateoffset = 0.25;

			boolean isItem = true;

			EnumFacing enumfacing = (EnumFacing)((IBlockState) te.getWorld().getBlockState(te.getPos())).getValue(FACING);

			for(ItemStack stack : stacksToRender) {
				if(!stackCrusherBit.isEmpty()) {
					//isItem = isItem(stackCrusherBit);//true
					itemtranslateoffset = 0.35;
				}
				else if(!stackToCrush.isEmpty()) {
					blockoffset = 0.2;
					itemoffset = 0.0; //could potentially be 0.1

					isItem = isItem(stackToCrush);
				}

				GlStateManager.pushMatrix();
				GlStateManager.translate(x + 0.5, y + 0.2, z + 0.5);

				switch(enumfacing) {
				case NORTH: GlStateManager.translate(0, (isItem?itemtranslateoffset:blocktranslateoffset), (isItem?itemoffset:blockoffset)); break;
				case EAST: GlStateManager.translate(-(isItem?itemoffset:blockoffset), (isItem?itemtranslateoffset:blocktranslateoffset), 0); break;
				case WEST: GlStateManager.translate((isItem?itemoffset:blockoffset), (isItem?itemtranslateoffset:blocktranslateoffset), 0); break;
				case SOUTH: GlStateManager.translate(0, (isItem?itemtranslateoffset:blocktranslateoffset), -(isItem?itemoffset:blockoffset)); break;
				default: GlStateManager.translate(0, 0, 0); break;
				}

				GlStateManager.rotate(-90 * ((IBlockState) te.getWorld().getBlockState(te.getPos())).getValue(FACING).getHorizontalIndex(), 0, 1, 0);

				if(isItem) {
					GlStateManager.rotate(-90, 1, 0, 0);
				}

				IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, te.getWorld(), null);
				model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);

				Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);

				GlStateManager.popMatrix();
			}

			GlStateManager.disableRescaleNormal();
			GlStateManager.disableBlend();
		}
	}

	//@Override
	public void render_OLD(TileEntityCrusher te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		ItemStack stack = te.inventory.getStackInSlot(0);
		if (!stack.isEmpty()) {
			GlStateManager.enableRescaleNormal();
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
			GlStateManager.enableBlend();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
			GlStateManager.pushMatrix();
			EnumFacing enumfacing = (EnumFacing)((IBlockState) te.getWorld().getBlockState(te.getPos())).getValue(FACING);
			GlStateManager.translate(x + 0.5, y + 0.2, z + 0.5);

			double blockoffset = 0.2;
			double itemoffset = 0.0; //could potentially be 0.1
			double blocktranslateoffset = 0.0;
			double itemtranslateoffset = 0.25;

			boolean isItem = isItem(stack);


			switch(enumfacing)
			{
			case NORTH:
				GlStateManager.translate(0, 0, (isItem?itemoffset:blockoffset));
				break;
			case EAST:
				GlStateManager.translate(-(isItem?itemoffset:blockoffset), 0, 0);
				break;
			case WEST:
				GlStateManager.translate((isItem?itemoffset:blockoffset), 0, 0);
				break;
			case SOUTH:
				GlStateManager.translate(0, 0, -(isItem?itemoffset:blockoffset));
				break;
			default:
				GlStateManager.translate(0, 0, 0);
				break;
			}

			//int tester = (int)(te.getWorld().getTotalWorldTime() + partialTicks) * 4;

			GlStateManager.rotate(-90 * ((IBlockState) te.getWorld().getBlockState(te.getPos())).getValue(FACING).getHorizontalIndex(), 0, 1, 0);
			//translates up a little to make it closer to crushing thing
			//only need to rotate if its an item
			if(isItem) {
				GlStateManager.translate(0, 0.25, 0);
				GlStateManager.rotate(-90, 1, 0, 0);
			}


			IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, te.getWorld(), null);
			model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);

			Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);

			GlStateManager.popMatrix();
			GlStateManager.disableRescaleNormal();
			GlStateManager.disableBlend();
		}
	}

}