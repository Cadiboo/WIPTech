package cadiboo.wiptech.client.gui.inventory;

import cadiboo.wiptech.client.ClientUtil;
import cadiboo.wiptech.client.render.ModRenderItem;
import cadiboo.wiptech.inventory.ContainerAssemblyTable;
import cadiboo.wiptech.tileentity.TileEntityAssemblyTable;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.ModResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAssemblyTable extends GuiContainer {

	private final InventoryPlayer			playerInventory;
	private final TileEntityAssemblyTable	assemblyTable;

	public GuiAssemblyTable(final InventoryPlayer playerInv, final TileEntityAssemblyTable assemblyTable) {
		super(new ContainerAssemblyTable(playerInv, assemblyTable));
		this.playerInventory = playerInv;
		this.assemblyTable = assemblyTable;

		this.xSize = 256;
		this.ySize = 256 - 42;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		this.mc.getTextureManager().bindTexture(new ModResourceLocation(ModReference.MOD_ID, "textures/gui/container/assembly_table.png"));
		final int x = (this.width - this.xSize) / 2;
		final int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		final int attachmentsSize = this.assemblyTable.getInventory().getSlots() - 1 - 2;

		final ItemStack main = this.assemblyTable.getInventory().getStackInSlot(attachmentsSize + 1).copy();

		renderItemAndEffectIntoGUI(main, x + 182, y + 27);

		final int width = (((ContainerAssemblyTable.WIDTH / 2) - ContainerAssemblyTable.SLOT_WIDTH) + ContainerAssemblyTable.BORDER_SIZE) - 12;
		final int height = (ContainerAssemblyTable.TOP_HEIGHT - ContainerAssemblyTable.SLOT_WIDTH) + ContainerAssemblyTable.BORDER_SIZE;
		final int radiusX = width / 2;
		final int radiusY = height / 2;

		for (int attachmentSlotIndex = 0; attachmentSlotIndex < attachmentsSize; attachmentSlotIndex++) {

			this.mc.getTextureManager().bindTexture(new ModResourceLocation(ModReference.MOD_ID, "textures/gui/container/slot.png"));

			final double t = (2 * Math.PI * attachmentSlotIndex) / attachmentsSize;
			final int posX = (int) Math.round((width / 2) + (radiusX * Math.cos(t + 90))) + 6;
			final int posY = (int) Math.round((height / 2) + (radiusY * Math.sin(t + 90))) + 3;

			ClientUtil.drawNonStandardTexturedRect((x + posX + ContainerAssemblyTable.BORDER_SIZE) - 1, (y + posY + ContainerAssemblyTable.BORDER_SIZE) - 1, 0, 0, 18, 18, 18, 18);

			final ItemStack stack = this.assemblyTable.getInventory().getStackInSlot(attachmentSlotIndex);

			renderItemAndEffectIntoGUI(stack, x + 182, y + 27);

		}

		this.mc.getTextureManager().bindTexture(new ModResourceLocation(ModReference.MOD_ID, "textures/gui/container/assembly_table.png"));

//		this.drawTexturedModalRect(x + 3, y + 3, 0, 0, ContainerAssemblyTable.WIDTH / 2, ContainerAssemblyTable.TOP_HEIGHT);

//		// fuel fire
//
//		final int fuelBurnTime = this.modFurnace.getMaxFuelTime();
//		final int fuelTimeRemaining = this.modFurnace.getFuelTimeRemaining();
//
//		final int k = (int) ModUtil.map(0, fuelBurnTime, 0, 16, fuelTimeRemaining);
//		this.drawTexturedModalRect(x + 56, (y + 36 + 13) - k, 176, 13 - k, 14, k);
//
//		// progress arrow
//
//		final int inputSmeltTime = this.modFurnace.getMaxSmeltTime();
//		final int modFurnaceSmeltTime = this.modFurnace.getSmeltTime();
//
//		final int l = (int) ModUtil.map(0, inputSmeltTime, 0, 24, modFurnaceSmeltTime);
//		this.drawTexturedModalRect(x + 79, y + 34, 176, 14, l + 1, 16);
	}

	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);

		final int x = (this.width - this.xSize) / 2;
		final int y = (this.height - this.ySize) / 2;

//		GuiInventory.drawEntityOnScreen(x + 51, y + 75, 30, (float) (x + 51) - mouseX, (float) ((y + 75) - 50) - mouseY, new EntityItem(this.mc.world, 0, 0, 0, new ItemStack(ModItems.FLAMETHROWER)));

//		final int x = (this.width - this.xSize) / 2;
//		final int y = (this.height - this.ySize) / 2;
//
//		if (ClientUtil.isInRect(x + 56, y + 36, 24, 16, mouseX, mouseY)) {
//			final int fuelBurnTime = this.modFurnace.getMaxFuelTime();
//			final int fuelTimeRemaining = this.modFurnace.getFuelTimeRemaining();
//
//			final int percentage = Math.round(Math.round(ModUtil.map(0, fuelBurnTime, 0, 100, fuelTimeRemaining)));
//
//			this.drawHoveringText(percentage + "%", mouseX, mouseY);
//		}
//
//		if (ClientUtil.isInRect(x + 79, y + 34, 24, 16, mouseX, mouseY)) {
//			final int inputSmeltTime = this.modFurnace.getMaxSmeltTime();
//			final int modFurnaceSmeltTime = this.modFurnace.getSmeltTime();
//
//			final int percentage = Math.round(Math.round(ModUtil.map(0, inputSmeltTime, 0, 100, modFurnaceSmeltTime)));
//
//			this.drawHoveringText(percentage + "%", mouseX, mouseY);
//		}

	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	public static void renderItemAndEffectIntoGUI(final ItemStack stack, final int xPos, final int yPos) {

		IBakedModel bakedmodel = ModRenderItem.getItemModelWithOverrides(stack, null, null);
		final ItemCameraTransforms.TransformType transformType = ItemCameraTransforms.TransformType.FIXED;

		GlStateManager.pushMatrix();
		GlStateManager.translate(xPos, yPos, 0);

		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);

		GlStateManager.enableRescaleNormal();
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		ModRenderItem.setupGuiTransform(0, 0, bakedmodel.isGui3d());

		GlStateManager.scale(3.1, 3.1, 3.1);

		GlStateManager.rotate(Minecraft.getMinecraft().world.getTotalWorldTime(), 0, 1, 0);

		bakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(bakedmodel, transformType, false);

		ClientUtil.enableMaxLighting();

		ModRenderItem.renderItem(stack, bakedmodel);

		GlStateManager.disableAlpha();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableLighting();

		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();

		GlStateManager.popMatrix();

	}

}
