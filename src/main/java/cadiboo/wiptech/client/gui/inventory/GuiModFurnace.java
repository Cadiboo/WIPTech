package cadiboo.wiptech.client.gui.inventory;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.client.ClientUtil;
import cadiboo.wiptech.inventory.ContainerModFurnace;
import cadiboo.wiptech.tileentity.TileEntityModFurnace;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.ModUtil;
import cadiboo.wiptech.util.resourcelocation.ModResourceLocation;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiModFurnace extends GuiContainer {

	private final InventoryPlayer playerInventory;
	private final TileEntityModFurnace modFurnace;

	public GuiModFurnace(final InventoryPlayer playerInv, final TileEntityModFurnace modFurnace) {
		super(new ContainerModFurnace(playerInv, modFurnace));
		this.playerInventory = playerInv;
		this.modFurnace = modFurnace;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		this.mc.getTextureManager().bindTexture(new ModResourceLocation(ModReference.MOD_ID, "textures/gui/container/mod_furnace.png"));
		final int x = (this.width - this.xSize) / 2;
		final int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		// fuel fire

		final int fuelBurnTime = this.modFurnace.getMaxFuelTime();
		final int fuelTimeRemaining = this.modFurnace.getFuelTimeRemaining();

		final int k = (int) ModUtil.map(0, fuelBurnTime, 0, 16, fuelTimeRemaining);
		this.drawTexturedModalRect(x + 56, (y + 36 + 13) - k, 176, 13 - k, 14, k);

		// progress arrow

		final int inputSmeltTime = this.modFurnace.getMaxSmeltTime();
		final int modFurnaceSmeltTime = this.modFurnace.getSmeltTime();

		final int l = (int) ModUtil.map(0, inputSmeltTime, 0, 24, modFurnaceSmeltTime);
		this.drawTexturedModalRect(x + 79, y + 34, 176, 14, l + 1, 16);
	}

	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);

		final int x = (this.width - this.xSize) / 2;
		final int y = (this.height - this.ySize) / 2;

		if (ClientUtil.isInRectangle(x + 56, y + 36, 24, 16, mouseX, mouseY)) {
			final int fuelBurnTime = this.modFurnace.getMaxFuelTime();
			final int fuelTimeRemaining = this.modFurnace.getFuelTimeRemaining();

			final int percentage = Math.round(Math.round(ModUtil.map(0, fuelBurnTime, 0, 100, fuelTimeRemaining)));

			this.drawHoveringText(percentage + "%", mouseX, mouseY);
		}

		if (ClientUtil.isInRectangle(x + 79, y + 34, 24, 16, mouseX, mouseY)) {
			final int inputSmeltTime = this.modFurnace.getMaxSmeltTime();
			final int modFurnaceSmeltTime = this.modFurnace.getSmeltTime();

			final int percentage = Math.round(Math.round(ModUtil.map(0, inputSmeltTime, 0, 100, modFurnaceSmeltTime)));

			this.drawHoveringText(percentage + "%", mouseX, mouseY);
		}

	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		this.fontRenderer.drawString(WIPTech.proxy.localize(this.modFurnace.getBlockType().getTranslationKey() + ".name"), 8, 6, 4210752);
		this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, (this.ySize - 96) + 2, 4210752);
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

}
