package cadiboo.wiptech.client.gui;

import java.util.ArrayList;
import java.util.List;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.container.ContainerCrusher;
import cadiboo.wiptech.init.Blocks;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.tileentity.TileEntityCrusher;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiCrusher extends GuiContainer {
	TileEntityCrusher tileEntity;
	private static final ResourceLocation BG_TEXTURE = new ResourceLocation("wiptech", "textures/gui/crusher.png");
	private static final int DUST_X = 26;
	private static final int DUST_Y = 37;
	private static final int DUST_TEXTURE_X = 177;
	private static final int DUST_TEXTURE_Y = 1;
	private static final int DUST_WIDTH = 12;
	private static final int DUST_HEIGHT = 12;
	private static final int PROGRESS_X = 48;
	private static final int PROGRESS_Y = 35;
	private static final int PROGRESS_TEXTURE_X = 176;
	private static final int PROGRESS_TEXTURE_Y = 14;
	private static final int PROGRESS_WIDTH = 24;
	private static final int PROGRESS_HEIGHT = 17;
	private static final int HOLDER_X = 24;
	private static final int HOLDER_Y = 17;
	private static final int HOLDER_TEXTURE_X = 176;
	private static final int HOLDER_TEXTURE_Y = 31;
	private static final int HOLDER_WIDTH = 16;
	private static final int HOLDER_HEIGHT = 16;
	private InventoryPlayer playerInv;

	public GuiCrusher(ContainerCrusher container, InventoryPlayer playerInv, TileEntityCrusher tileCrusher) {
		super(container);
		this.tileEntity = tileCrusher;
		this.playerInv = playerInv;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		// drawDefaultBackground();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(BG_TEXTURE);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;

		drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		drawTexturedModalRect(x + 26, y + 37, 177, 1, 12, getCrushProgressScaled(12));

		drawTexturedModalRect(x + 48, y + 35, 176, 14, getCrushProgressScaled(24), 17);

		/*
		 * float oldZ = this.zLevel; this.zLevel = 255.0F;
		 * this.mc.getTextureManager().bindTexture(BG_TEXTURE);
		 * drawModalRectWithCustomSizedTexture(x+1, y+1, 10, 10, 10, 10, 10, 10);
		 * this.zLevel = oldZ; this.mc.getTextureManager().bindTexture(BG_TEXTURE);
		 */
		// drawScaledCustomSizeModalRect(x+1, y+1, 177, 1, 12, 12, 12, 12, 24, 24);

	}

	private int getCrushProgressScaled(int textureSize) {
		return (int) Math.round(TileEntityCrusher.getFractionOfCrushTimeComplete(this.tileEntity) * textureSize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String name = WIPTech.proxy.localize(Blocks.CRUSHER.getUnlocalizedName() + ".name");
		this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 4210752);
		this.fontRenderer.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, this.ySize - 94, 4210752);

		List<String> hoveringText = new ArrayList();
		if (isInRect(this.guiLeft + 48, this.guiTop + 35, 24, 17, mouseX, mouseY)) {
			hoveringText.add("Progress: " + TileEntityCrusher.getPercentageOfCrushTimeComplete(this.tileEntity) + "%");
		}
		if (!hoveringText.isEmpty()) {
			drawHoveringText(hoveringText, mouseX - this.guiLeft, mouseY - this.guiTop, this.fontRenderer);
		}

		this.mc.getTextureManager().bindTexture(BG_TEXTURE);

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		if ((this.tileEntity.inventory.getStackInSlot(0) != null)
				&& (!this.tileEntity.inventory.getStackInSlot(0).isEmpty())
				&& (this.tileEntity.inventory.getStackInSlot(0).getItem() == Items.CRUSHER_BIT)) {
			GlStateManager.disableLighting();
			// GlStateManager.disableFog(); //Used to be the only thing that made it work,
			// now it absolutely destroys everything when called

			this.zLevel = 255.0F;
			// this.itemRender.zLevel = 255.0F;
			drawTexturedModalRect(24, 17, 176, 31, 16, 16);

			this.zLevel = 0.0F;
			// this.itemRender.zLevel = 0.0F;

			GlStateManager.enableLighting();
			// GlStateManager.enableFog(); //Used to be the only thing that made it work,
			// now it absolutely destroys everything when called
		}
	}

	public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY) {
		return (mouseX >= x) && (mouseX <= x + xSize) && (mouseY >= y) && (mouseY <= y + ySize);
	}
}
