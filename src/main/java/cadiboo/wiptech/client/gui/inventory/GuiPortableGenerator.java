package cadiboo.wiptech.client.gui.inventory;

import cadiboo.wiptech.entity.item.EntityPortableGenerator;
import cadiboo.wiptech.inventory.ContainerPortableGenerator;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.ModResourceLocation;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPortableGenerator extends GuiContainer {

	private final InventoryPlayer playerInventory;
	private final EntityPortableGenerator portableGenerator;

	public GuiPortableGenerator(InventoryPlayer playerInv, EntityPortableGenerator portableGenerator) {
		super(new ContainerPortableGenerator(playerInv, portableGenerator));
		this.playerInventory = playerInv;
		this.portableGenerator = portableGenerator;
		this.ySize = 133;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(new ModResourceLocation(ModReference.Version.getModId(), "textures/gui/container/railgun.png"));
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, 9 * 18 + 17);
		this.drawTexturedModalRect(i, j + 9 * 18 + 17, 0, 126, this.xSize, 96);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRenderer.drawString(this.portableGenerator.getDisplayName().getUnformattedText(), 8, 6, 4210752);
		this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

}
