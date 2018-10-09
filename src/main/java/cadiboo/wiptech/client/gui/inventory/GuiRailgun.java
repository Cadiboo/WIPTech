package cadiboo.wiptech.client.gui.inventory;

import cadiboo.wiptech.entity.item.EntityRailgun;
import cadiboo.wiptech.inventory.ContainerRailgun;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.resourcelocation.ModResourceLocation;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiRailgun extends GuiContainer {

	private final InventoryPlayer playerInventory;
	private final EntityRailgun railgun;

	public GuiRailgun(final InventoryPlayer playerInv, final EntityRailgun railgun) {
		super(new ContainerRailgun(playerInv, railgun));
		this.playerInventory = playerInv;
		this.railgun = railgun;
		this.ySize = 133;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		this.mc.getTextureManager().bindTexture(new ModResourceLocation(ModReference.MOD_ID, "textures/gui/container/railgun.png"));
		final int x = (this.width - this.xSize) / 2;
		final int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}

	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		this.fontRenderer.drawString(this.railgun.getDisplayName().getUnformattedText(), 8, 6, 4210752);
		this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, (this.ySize - 96) + 2, 4210752);
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

}
