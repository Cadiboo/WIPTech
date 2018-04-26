package cadiboo.wiptech.client.gui;

import java.util.ArrayList;
import java.util.List;

import cadiboo.wiptech.container.ContainerTurbine;
import cadiboo.wiptech.init.Blocks;
import cadiboo.wiptech.tileentity.TileEntityTurbine;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;

public class GuiTurbine extends GuiContainer {
	TileEntityTurbine						tileEntity;
	private static final ResourceLocation	BG_TEXTURE	= new ResourceLocation("wiptech", "textures/gui/turbine.png");
	private InventoryPlayer					playerInv;

	public GuiTurbine(ContainerTurbine container, InventoryPlayer playerInv, TileEntityTurbine tileTurbine) {
		super(container);
		this.tileEntity = tileTurbine;
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
		drawTexturedModalRect(x + 84, y + 17 + 52 - (int) Math.round(52D * (getEnergyPercentage() / 100D)), this.xSize, 0, 10, (int) Math.round(52D * (getEnergyPercentage() / 100D)));
	}

	protected double getEnergyPercentage() {
		return ((double) this.tileEntity.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored() / (double) this.tileEntity.getCapability(CapabilityEnergy.ENERGY, null).getMaxEnergyStored()
				* 100D);
	}

	protected int getEnergyPercentageInt() {
		return (int) Math.round(getEnergyPercentage());
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String name = I18n.format(Blocks.TURBINE.getUnlocalizedName() + ".name");
		this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 4210752);
		this.fontRenderer.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, this.ySize - 94, 4210752);

		List<String> hoveringText = new ArrayList();
		if (isInRect(this.guiLeft + 83, this.guiTop + 16, 10, 54, mouseX, mouseY)) {
			hoveringText.add("Energy: " + getEnergyPercentageInt() + "%");
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
