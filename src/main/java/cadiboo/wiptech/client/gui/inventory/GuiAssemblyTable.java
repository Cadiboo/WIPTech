package cadiboo.wiptech.client.gui.inventory;

import org.lwjgl.input.Mouse;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.client.ClientUtil;
import cadiboo.wiptech.client.render.ModRenderItem;
import cadiboo.wiptech.inventory.ContainerAssemblyTable;
import cadiboo.wiptech.network.ModNetworkManager;
import cadiboo.wiptech.network.play.client.CPacketStartAssembly;
import cadiboo.wiptech.tileentity.TileEntityAssemblyTable;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.ModResourceLocation;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
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

	private static final int				ASSEMBLY_START_BUTTON	= 0;
	private final InventoryPlayer			playerInventory;
	private final TileEntityAssemblyTable	assemblyTable;

	public GuiAssemblyTable(final InventoryPlayer playerInv, final TileEntityAssemblyTable assemblyTable) {
		super(new ContainerAssemblyTable(playerInv, assemblyTable));
		this.playerInventory = playerInv;
		this.assemblyTable = assemblyTable;

		this.xSize = 256;
		this.ySize = 256 - 31;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		this.mc.getTextureManager().bindTexture(new ModResourceLocation(ModReference.MOD_ID, "textures/gui/container/assembly_table.png"));
		final int x = (this.width - this.xSize) / 2;
		final int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		final ItemStack assembleItem = this.assemblyTable.getInventory().getStackInSlot(TileEntityAssemblyTable.ASSEMBLY_SLOT).copy();

		renderItemAndEffectIntoGUI(assembleItem, x + 188, y + 55);

		final int width = 118 - 23;
		final int height = width;
		final int radiusX = width / 2;
		final int radiusY = height / 2;

		for (int attachmentSlotIndex = 0; attachmentSlotIndex < TileEntityAssemblyTable.ATTACHMENT_SLOTS_SIZE; attachmentSlotIndex++) {

			this.mc.getTextureManager().bindTexture(new ModResourceLocation(ModReference.MOD_ID, "textures/gui/container/slot.png"));

			final double t = (2 * Math.PI * attachmentSlotIndex) / TileEntityAssemblyTable.ATTACHMENT_SLOTS_SIZE;
			final int posX = (int) Math.round((width / 2) + (radiusX * Math.cos(t + 90))) + 9;
			final int posY = (int) Math.round((height / 2) + (radiusY * Math.sin(t + 90))) + 20;

			ClientUtil.drawNonStandardTexturedRect((x + posX + ContainerAssemblyTable.BORDER_SIZE) - 1, (y + posY + ContainerAssemblyTable.BORDER_SIZE) - 1, 0, 0, 18, 18, 18, 18);

			final int arrowPosX = (int) Math.round((width / 2) + ((radiusX / 2) * Math.cos(t + 90 + 90))) + 9;
			final int arrowPosY = (int) Math.round((height / 2) + ((radiusY / 2) * Math.sin(t + 90 + 90))) + 20;

			final ItemStack stack = this.assemblyTable.getInventory().getStackInSlot(attachmentSlotIndex);

			renderItemAndEffectIntoGUI(stack, x + 188, y + 55);

		}

		// assembly progress
		this.mc.getTextureManager().bindTexture(new ModResourceLocation(ModReference.MOD_ID, "textures/gui/container/assembly_table.png"));
		if (!this.assemblyTable.isAssembling()) {
			return;
		}
		final int assemblyProgress = 118 - (int) ModUtil.map(0, this.assemblyTable.getMaxAssemblyTime(), 0, 118, this.assemblyTable.getAssemblyTime());
		this.drawTexturedModalRect(x + 129, y + 93, 0, 225, assemblyProgress, 16);

	}

	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);

		final int x = (this.width - this.xSize) / 2;
		final int y = (this.height - this.ySize) / 2;

		// assembly progress
		if (ClientUtil.isInRect(x + 129, y + 93, 118, 16, mouseX, mouseY)) {
			final int percentage = Math.round(Math.round(100 - ModUtil.map(0, this.assemblyTable.getMaxAssemblyTime(), 0, 100, this.assemblyTable.getAssemblyTime())));
			this.drawHoveringText(percentage + "%", mouseX, mouseY);
		}

	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		this.fontRenderer.drawString(WIPTech.proxy.localize(this.assemblyTable.getBlockType().getUnlocalizedName() + ".name"), 8, 6, 4210752);
	}

	public static void renderItemAndEffectIntoGUI(final ItemStack stack, final int xPos, final int yPos) {

		IBakedModel bakedmodel = ModRenderItem.getItemModelWithOverrides(stack, null, null);
		final ItemCameraTransforms.TransformType transformType = ItemCameraTransforms.TransformType.FIXED;

		GlStateManager.pushMatrix();
		GlStateManager.translate(xPos - 8, yPos - 8, 0);

		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);

		GlStateManager.enableRescaleNormal();
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		ModRenderItem.setupGuiTransform(0, 0, bakedmodel.isGui3d());

		GlStateManager.scale(4.4, 4.4, 4.4);

		GlStateManager.rotate(45 + (float) ModUtil.map(0, Minecraft.getMinecraft().displayWidth, 0, 360, Mouse.getX()), 0, 1, 0);

		GlStateManager.rotate(90 + (float) ModUtil.map(0, Minecraft.getMinecraft().displayHeight, 0, 360, Mouse.getY()), 1, 0, 0);

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

	@Override
	public void initGui() {
		super.initGui();
		final int x = (this.width - this.xSize) / 2;
		final int y = (this.height - this.ySize) / 2;
		this.buttonList.add(new GuiButton(ASSEMBLY_START_BUTTON, x + 132, y + 114, 113, 20, WIPTech.proxy.localize("startassembly")));
	}

	@Override
	protected void actionPerformed(final GuiButton button) {
		if (button.id == ASSEMBLY_START_BUTTON) {
			ModNetworkManager.NETWORK.sendToServer(new CPacketStartAssembly(this.assemblyTable.getPos(), this.assemblyTable.getWorld().provider.getDimension()));
			WIPTech.info("ASSEMBLY_START_BUTTON was clicked");
		}
	}

}
