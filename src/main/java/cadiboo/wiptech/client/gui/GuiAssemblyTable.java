package cadiboo.wiptech.client.gui;

import java.util.ArrayList;
import java.util.List;

import cadiboo.wiptech.init.Blocks;
import cadiboo.wiptech.init.Items;
import cadiboo.wiptech.inventory.ContainerAssemblyTable;
import cadiboo.wiptech.tileentity.TileEntityAssemblyTable;
import cadiboo.wiptech.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiAssemblyTable extends GuiContainer {

	private static final ResourceLocation	BG_TEXTURE					= new ResourceLocation("wiptech", "textures/gui/assembly_table.png");
	public static final ItemStack[]			ASSEMBLEABLE_ITEMS			= { new ItemStack(Items.RAILGUN), new ItemStack(Items.COILGUN), new ItemStack(Items.STANDALONE_GUN), new ItemStack(Items.PLASMA_GUN), new ItemStack(Items.PLASMA_TOOL),
			new ItemStack(Items.TASER) };
	public static final int					MISSING_INGREDIENT_COLOR	= 1088888888;

	TileEntityAssemblyTable	te;
	private InventoryPlayer	playerInv;

	public GuiAssemblyTable(ContainerAssemblyTable container, InventoryPlayer playerInv, TileEntityAssemblyTable te) {
		super(container);
		this.te = te;
		this.playerInv = playerInv;
	}

	@Override
	public void initGui() {
		super.initGui();
		for (int i = 0; i < ASSEMBLEABLE_ITEMS.length; i++) {
			final ItemStack stack = ASSEMBLEABLE_ITEMS[i];
			// id, x, y, width, height, xTexStart, yTexStart, yDiffText, texture_location
			this.buttonList.add(new GuiButtonImage(i, guiLeft + 10 + (i % 2) * 18, guiTop + 16 + 9 * ((i & 0x1) == 0 ? i : i - 1), 18, 18, 0, 168, 0, BG_TEXTURE) {
				@Override
				public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
					super.drawButton(mc, mouseX, mouseY, partialTicks);
					if ((mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height) || te.getAssembleItem().isItemEqual(stack))
						this.drawTexturedModalRect(this.x, this.y, 18, 168, 18, 18);
					RenderHelper.enableGUIStandardItemLighting();
					Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(stack, x + 1, y + 1);

				}
			});
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		te.setAssembleItem(ASSEMBLEABLE_ITEMS[button.id]);
		System.out.println("Button " + button.id + " was Clicked!");
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
		drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		drawTexturedModalRect(this.guiLeft + 159, y + 17 + 52 - (int) Math.round(52D * Utils.getEnergyFraction(te.getEnergy(null))), this.xSize, 0, 10, (int) Math.round(52D * Utils.getEnergyFraction(te.getEnergy(null))));
		if (!te.getAssembleItem().isEmpty()) {

			for (int s = 0; s < 10; s++) {
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
				Utils.renderItemModelIntoGUIWithColor(te.getAssembleItem(), guiLeft + 13 * s, guiTop + 5 * s, Utils.getModelFromStack(te.getAssembleItem(), te.getWorld()), zLevel + 50,
						te.getAssemblyTime() > 0 ? (int) System.currentTimeMillis() : MISSING_INGREDIENT_COLOR);
			}

			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
			Utils.renderItemModelIntoGUIWithColor(te.getAssembleItem(), guiLeft + 130, guiTop + 35, Utils.getModelFromStack(te.getAssembleItem(), te.getWorld()), zLevel + 50,
					te.getAssemblyTime() > 0 ? (int) System.currentTimeMillis() : MISSING_INGREDIENT_COLOR);
		}
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
