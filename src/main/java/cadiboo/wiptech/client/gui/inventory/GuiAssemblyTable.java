package cadiboo.wiptech.client.gui.inventory;

import cadiboo.wiptech.client.ClientUtil;
import cadiboo.wiptech.inventory.ContainerAssemblyTable;
import cadiboo.wiptech.tileentity.TileEntityAssemblyTable;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.ModResourceLocation;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAssemblyTable extends GuiContainer {

	private final InventoryPlayer			playerInventory;
	private final TileEntityAssemblyTable	assemblyTable;

	public GuiAssemblyTable(final InventoryPlayer playerInv, final TileEntityAssemblyTable assemblyTable) {
		super(new ContainerAssemblyTable(playerInv, assemblyTable));
		this.playerInventory = playerInv;
		this.assemblyTable = assemblyTable;

		this.xSize = 256;
		this.ySize = 256 - 42;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		this.mc.getTextureManager().bindTexture(new ModResourceLocation(ModReference.MOD_ID, "textures/gui/container/assembly_table.png"));
		final int x = (this.width - this.xSize) / 2;
		final int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		final int attachmentsSize = this.assemblyTable.getInventory().getSlots() - 1 - 2;

		// TODO: code cleanup!!!

		final int scale = 3;

		RenderHelper.enableGUIStandardItemLighting();

		GlStateManager.pushMatrix();
		GlStateManager.translate((x + 194) - ((18 / 2) * (scale)), (y + 38) - ((18 / 2) * (scale)), 0);
		GlStateManager.scale(scale, scale, 1);

		// this.zLevel += 50.0F;
		// translate 100 + this.zLevel

		final float iHateThis = 100 + 50 + this.zLevel;

		GlStateManager.translate(0, 0, iHateThis);
		GlStateManager.translate(8.0F, 8.0F, 0.0F);
		GlStateManager.scale(1.0F, -1.0F, 1.0F);
		GlStateManager.scale(16.0F, 16.0F, 16.0F);

		GlStateManager.rotate((this.assemblyTable.getWorld().getTotalWorldTime()), 0, 1, 0);

		GlStateManager.scale(1 / 16.0F, 1 / 16.0F, 1 / 16.0F);
		GlStateManager.scale(1 / 1.0F, 1 / -1.0F, 1 / 1.0F);
		GlStateManager.translate(-8.0F, -8.0F, -0.0F);
		GlStateManager.translate(0, 0, -iHateThis);

		// TODO: Camera transforms.type.fixed //FIXME
		this.itemRender.renderItemAndEffectIntoGUI(this.assemblyTable.getInventory().getStackInSlot(attachmentsSize + 1), 0, 0);

		GlStateManager.popMatrix();

		final int width = (((ContainerAssemblyTable.WIDTH / 2) - ContainerAssemblyTable.SLOT_WIDTH) + ContainerAssemblyTable.BORDER_SIZE) - 12;
		final int height = (ContainerAssemblyTable.TOP_HEIGHT - ContainerAssemblyTable.SLOT_WIDTH) + ContainerAssemblyTable.BORDER_SIZE;
		final int radiusX = width / 2;
		final int radiusY = height / 2;

		for (int attachmentSlotIndex = 0; attachmentSlotIndex < attachmentsSize; attachmentSlotIndex++) {

			this.mc.getTextureManager().bindTexture(new ModResourceLocation(ModReference.MOD_ID, "textures/gui/container/slot.png"));

			final double t = (2 * Math.PI * attachmentSlotIndex) / attachmentsSize;
			final int posX = (int) Math.round((width / 2) + (radiusX * Math.cos(t))) + 6;
			final int posY = (int) Math.round((height / 2) + (radiusY * Math.sin(t)));

			ClientUtil.drawNonStandardTexturedRect((x + posX + ContainerAssemblyTable.BORDER_SIZE) - 1, (y + posY + ContainerAssemblyTable.BORDER_SIZE) - 1, 0, 0, 18, 18, 18, 18);

			final ItemStack stack = this.assemblyTable.getInventory().getStackInSlot(attachmentSlotIndex);

			GlStateManager.pushMatrix();
			GlStateManager.translate((x + 194) - ((18 / 2) * (scale)), (y + 38) - ((18 / 2) * (scale)), 0);
			GlStateManager.scale(scale, scale, 1);
			this.itemRender.renderItemAndEffectIntoGUI(stack, 0, 0);
			GlStateManager.popMatrix();

		}

		this.mc.getTextureManager().bindTexture(new ModResourceLocation(ModReference.MOD_ID, "textures/gui/container/assembly_table.png"));

//		this.drawTexturedModalRect(x + 3, y + 3, 0, 0, ContainerAssemblyTable.WIDTH / 2, ContainerAssemblyTable.TOP_HEIGHT);

//		// fuel fire
//
//		final int fuelBurnTime = this.modFurnace.getMaxFuelTime();
//		final int fuelTimeRemaining = this.modFurnace.getFuelTimeRemaining();
//
//		final int k = (int) ModUtil.map(0, fuelBurnTime, 0, 16, fuelTimeRemaining);
//		this.drawTexturedModalRect(x + 56, (y + 36 + 13) - k, 176, 13 - k, 14, k);
//
//		// progress arrow
//
//		final int inputSmeltTime = this.modFurnace.getMaxSmeltTime();
//		final int modFurnaceSmeltTime = this.modFurnace.getSmeltTime();
//
//		final int l = (int) ModUtil.map(0, inputSmeltTime, 0, 24, modFurnaceSmeltTime);
//		this.drawTexturedModalRect(x + 79, y + 34, 176, 14, l + 1, 16);
	}

	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);

		final int x = (this.width - this.xSize) / 2;
		final int y = (this.height - this.ySize) / 2;

//		GuiInventory.drawEntityOnScreen(x + 51, y + 75, 30, (float) (x + 51) - mouseX, (float) ((y + 75) - 50) - mouseY, new EntityItem(this.mc.world, 0, 0, 0, new ItemStack(ModItems.FLAMETHROWER)));

//		final int x = (this.width - this.xSize) / 2;
//		final int y = (this.height - this.ySize) / 2;
//
//		if (ClientUtil.isInRect(x + 56, y + 36, 24, 16, mouseX, mouseY)) {
//			final int fuelBurnTime = this.modFurnace.getMaxFuelTime();
//			final int fuelTimeRemaining = this.modFurnace.getFuelTimeRemaining();
//
//			final int percentage = Math.round(Math.round(ModUtil.map(0, fuelBurnTime, 0, 100, fuelTimeRemaining)));
//
//			this.drawHoveringText(percentage + "%", mouseX, mouseY);
//		}
//
//		if (ClientUtil.isInRect(x + 79, y + 34, 24, 16, mouseX, mouseY)) {
//			final int inputSmeltTime = this.modFurnace.getMaxSmeltTime();
//			final int modFurnaceSmeltTime = this.modFurnace.getSmeltTime();
//
//			final int percentage = Math.round(Math.round(ModUtil.map(0, inputSmeltTime, 0, 100, modFurnaceSmeltTime)));
//
//			this.drawHoveringText(percentage + "%", mouseX, mouseY);
//		}

	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

}
