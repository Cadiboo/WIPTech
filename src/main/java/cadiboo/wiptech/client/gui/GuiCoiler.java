package cadiboo.wiptech.client.gui;

import java.util.ArrayList;
import java.util.List;

import cadiboo.wiptech.Reference;
import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.block.coiler.ContainerCoiler;
import cadiboo.wiptech.block.coiler.TileEntityCoiler;
import cadiboo.wiptech.init.Blocks;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiCoiler extends GuiContainer {

	TileEntityCoiler tileEntity;
	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(Reference.ID, "textures/gui/coiler.png");
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

	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		drawDefaultBackground();
		GlStateManager.color(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(BG_TEXTURE);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		//gui background
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		//dust icon
		this.drawTexturedModalRect(x+DUST_X, y+DUST_Y, DUST_TEXTURE_X, DUST_TEXTURE_Y, DUST_WIDTH, getCoilProgressScaled(DUST_HEIGHT));
		//arrow icon
		this.drawTexturedModalRect(x+PROGRESS_X, y+PROGRESS_Y, PROGRESS_TEXTURE_X, PROGRESS_TEXTURE_Y, getCoilProgressScaled(PROGRESS_WIDTH), PROGRESS_HEIGHT);
		//Utils.getLogger().info("getCoilProgressScaled(24): "+getCoilProgressScaled(24));
		//24x17

		/*if (TileEntityCoiler.isCoiling(this.tileEntity))
        {
            int k = this.getBurnLeftScaled(13);
            this.drawTexturedModalRect(x + 56, y + 36 + 12 - k, 176, 12 - k, 14, k + 1);
        }*/

		//this.drawTexturedModalRect(x+79, y+31, 176, 0, 17, progress);

		/*int l = this.getCoilProgressScaled(24);
        this.drawTexturedModalRect(x + 79, y + 34, 176, 14, l + 1, 16);
		 */
	}

	private int getCoilProgressScaled(int textureSize) {
		int totalCoilTime = TileEntityCoiler.getTotalWindTime(tileEntity);
		int crushTime = Math.round(TileEntityCoiler.getWindTime(tileEntity));
		float result1 = (totalCoilTime - crushTime);
		float result2 = result1/ totalCoilTime;
		float result3 =  result2 * textureSize;
		//;	Utils.getLogger().info("CoilProgressScaled - result1: "+result1+", result2: "+result2+", result3: "+result3);
		return Math.round(result3);
		//return Math.round(((totalCoilTime - crushTime) / totalCoilTime) * textureSize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String name = WIPTech.proxy.localize(Blocks.CRUSHER.getUnlocalizedName() + ".name");
		fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
		fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 94, 0x404040);
		
		
		List<String> hoveringText = new ArrayList<String>();
		// If the mouse is over the progress bar add the progress bar hovering text
		if (isInRect(guiLeft + PROGRESS_X, guiTop + PROGRESS_Y, PROGRESS_WIDTH, PROGRESS_HEIGHT, mouseX, mouseY)){
			hoveringText.add("Progress: "+TileEntityCoiler.getPercentageOfWindTimeComplete(tileEntity)+"%");
		}

		// If hoveringText is not empty draw the hovering text
		if (!hoveringText.isEmpty()){
			drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRenderer);
		}
		//				// You must re bind the texture and reset the colour if you still need to use it after drawing a string
		//				Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		//				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

	}
	
	public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY){
		return ((mouseX >= x && mouseX <= x+xSize) && (mouseY >= y && mouseY <= y+ySize));
	}


}
