package cadiboo.wiptech.client.gui;

import java.util.ArrayList;
import java.util.List;

import cadiboo.wiptech.container.ContainerCoiler;
import cadiboo.wiptech.init.Blocks;
import cadiboo.wiptech.tileentity.TileEntityCoiler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiCoiler extends GuiContainer {
	TileEntityCoiler tileEntity;
	private static final ResourceLocation BG_TEXTURE = new ResourceLocation("wiptech", "textures/gui/coiler.png");
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
	private InventoryPlayer playerInv;

	public GuiCoiler(ContainerCoiler container, InventoryPlayer playerInv, TileEntityCoiler tileCoiler) {
		super(container);
		this.tileEntity = tileCoiler;
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

		drawTexturedModalRect(x + 26, y + 37, 177, 1, 12, getWindProgressScaled(12));

		drawTexturedModalRect(x + 48, y + 35, 176, 14, getWindProgressScaled(24), 17);
	}

	private int getWindProgressScaled(int textureSize) {
		return (int) Math.round(TileEntityCoiler.getFractionOfWindTimeComplete(this.tileEntity) * textureSize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String name = I18n.format(Blocks.COILER.getUnlocalizedName() + ".name", new Object[0]);
		this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 4210752);
		this.fontRenderer.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, this.ySize - 94, 4210752);

		List<String> hoveringText = new ArrayList();
		if (isInRect(this.guiLeft + 48, this.guiTop + 35, 24, 17, mouseX, mouseY)) {
			hoveringText.add("Progress: " + TileEntityCoiler.getPercentageOfWindTimeComplete(this.tileEntity) + "%");
		}
		if (!hoveringText.isEmpty()) {
			drawHoveringText(hoveringText, mouseX - this.guiLeft, mouseY - this.guiTop, this.fontRenderer);
		}
		this.mc.getTextureManager().bindTexture(BG_TEXTURE);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY) {
		return (mouseX >= x) && (mouseX <= x + xSize) && (mouseY >= y) && (mouseY <= y + ySize);
	}
}
