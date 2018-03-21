package cadiboo.wiptech.client.gui;

import cadiboo.wiptech.container.ContainerTestLauncher;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

public class GuiTestLauncher extends GuiContainer {

	public GuiTestLauncher(Container inventorySlotsIn) {
		super(inventorySlotsIn);
		// TODO Auto-generated constructor stub
	}

	private IItemHandler i;

	public GuiTestLauncher( IItemHandler i, EntityPlayer p ) {
		super( new ContainerTestLauncher( i, p ));

		this.xSize = 175;
		this.ySize = 221;
		this.i = i;
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color( 1.0F, 1.0F, 1.0F, 1.0F );
		//this.mc.getTextureManager().bindTexture( new ResourceLocation( Reference.ID, "textures/gui/container/kitbag.png" ) );
		this.mc.getTextureManager().bindTexture( new ResourceLocation( "minecraft", "textures/gui/container/generic_54.png" ) );
		this.drawTexturedModalRect( this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize );
	}
}