package cadiboo.wiptech.client.gui.inventory;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.inventory.ContainerModFurnace;
import cadiboo.wiptech.tileentity.TileEntityModFurnace;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.ModResourceLocation;
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

		// GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		// this.mc.getTextureManager().bindTexture(FURNACE_GUI_TEXTURES);
		// int i = (this.width - this.xSize) / 2;
		// int j = (this.height - this.ySize) / 2;
		// this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
		//
		// if (TileEntityFurnace.isBurning(this.tileFurnace))
		// {
		// int k = this.getBurnLeftScaled(13);
		// this.drawTexturedModalRect(i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
		// }
		//
		// int l = this.getCookProgressScaled(24);
		// this.drawTexturedModalRect(i + 79, j + 34, 176, 14, l + 1, 16);

	}

	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		this.fontRenderer.drawString(WIPTech.proxy.localize(this.modFurnace.getBlockType().getUnlocalizedName() + ".name"), 8, 6, 4210752);
		this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, (this.ySize - 96) + 2, 4210752);
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

}
