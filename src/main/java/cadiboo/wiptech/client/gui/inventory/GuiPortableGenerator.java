package cadiboo.wiptech.client.gui.inventory;

import cadiboo.wiptech.entity.item.EntityPortableGenerator;
import cadiboo.wiptech.inventory.ContainerPortableGenerator;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.resourcelocation.ModResourceLocation;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPortableGenerator extends GuiContainer {

	private final InventoryPlayer playerInventory;
	private final EntityPortableGenerator portableGenerator;

	public GuiPortableGenerator(final InventoryPlayer playerInv, final EntityPortableGenerator portableGenerator) {
		super(new ContainerPortableGenerator(playerInv, portableGenerator));
		this.playerInventory = playerInv;
		this.portableGenerator = portableGenerator;
		this.ySize = 133;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		this.mc.getTextureManager().bindTexture(new ModResourceLocation(ModReference.MOD_ID, "textures/gui/container/portable_generator.png"));
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
		this.fontRenderer.drawString(this.portableGenerator.getDisplayName().getUnformattedText(), 8, 6, 4210752);
		this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, (this.ySize - 96) + 2, 4210752);
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

}
