package cadiboo.wiptech.client.gui;

import java.util.ArrayList;
import java.util.List;

import cadiboo.wiptech.container.ContainerAssemblyTable;
import cadiboo.wiptech.init.Blocks;
import cadiboo.wiptech.tileentity.TileEntityAssemblyTable;
import cadiboo.wiptech.util.Utils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiAssemblyTable extends GuiContainer {
	TileEntityAssemblyTable					te;
	private static final ResourceLocation	BG_TEXTURE	= new ResourceLocation("wiptech", "textures/gui/assembly_table.png");
	private InventoryPlayer					playerInv;

	public GuiAssemblyTable(ContainerAssemblyTable container, InventoryPlayer playerInv, TileEntityAssemblyTable te) {
		super(container);
		this.te = te;
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
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(BG_TEXTURE);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;

		// y + (int) Math.round(52D * (getEnergyPercentage() / 100D))

		drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		drawTexturedModalRect(this.guiLeft + 159, y + 17 + 52 - (int) Math.round(52D * Utils.getEnergyFraction(te.getEnergy(null))), this.xSize, 0, 10, (int) Math.round(52D * Utils.getEnergyFraction(te.getEnergy(null))));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String name = I18n.format(Blocks.ASSEMBLY_TABLE.getUnlocalizedName() + ".name");
		this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 4210752);
		this.fontRenderer.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, this.ySize - 94, 4210752);

		List<String> hoveringText = new ArrayList();
		if (Utils.isInRect(this.guiLeft + 159, this.guiTop + 16, 10, 54, mouseX, mouseY)) {
			hoveringText.add("Energy: " + Utils.getEnergyPercentage(te.getEnergy(null)) + "%");
		}
		if (!hoveringText.isEmpty()) {
			drawHoveringText(hoveringText, mouseX - this.guiLeft, mouseY - this.guiTop, this.fontRenderer);
		}
		this.mc.getTextureManager().bindTexture(BG_TEXTURE);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}
}
