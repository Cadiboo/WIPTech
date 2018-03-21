package cadiboo.wiptech.handler;

import cadiboo.wiptech.client.gui.GuiCoiler;
import cadiboo.wiptech.client.gui.GuiCrusher;
import cadiboo.wiptech.client.gui.GuiTestLauncher;
import cadiboo.wiptech.container.ContainerCoiler;
import cadiboo.wiptech.container.ContainerCrusher;
import cadiboo.wiptech.container.ContainerTestLauncher;
import cadiboo.wiptech.tileentity.TileEntityCoiler;
import cadiboo.wiptech.tileentity.TileEntityCrusher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public class GuiHandler implements IGuiHandler {

	public static final int CRUSHER = 0;
	public static final int COILER = 1;
	public static final int TEST_LAUNCHER = 2;

	@Override
	public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case CRUSHER:
			return new ContainerCrusher(player.inventory, (TileEntityCrusher)world.getTileEntity(new BlockPos(x, y, z)));
		case COILER:
			return new ContainerCoiler(player.inventory, (TileEntityCoiler)world.getTileEntity(new BlockPos(x, y, z)));
		case TEST_LAUNCHER:
			return new ContainerTestLauncher( player.getHeldItemMainhand().getCapability( CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null ), player );
		default:
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case CRUSHER:
			return new GuiCrusher((ContainerCrusher) getServerGuiElement(ID, player, world, x, y, z), player.inventory, (TileEntityCrusher) world.getTileEntity(new BlockPos(x,y,z)));
		case COILER:
			return new GuiCoiler((ContainerCoiler) getServerGuiElement(ID, player, world, x, y, z), player.inventory, (TileEntityCoiler) world.getTileEntity(new BlockPos(x,y,z)));
		case TEST_LAUNCHER:
			return new GuiTestLauncher(player.getHeldItemMainhand().getCapability( CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null ), player );
		default:
			return null;
		}
	}


}
