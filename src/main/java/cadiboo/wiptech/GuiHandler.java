package cadiboo.wiptech;

import cadiboo.wiptech.block.coiler.ContainerCoiler;
import cadiboo.wiptech.block.coiler.TileEntityCoiler;
import cadiboo.wiptech.block.crusher.ContainerCrusher;
import cadiboo.wiptech.block.crusher.TileEntityCrusher;
import cadiboo.wiptech.client.gui.GuiCoiler;
import cadiboo.wiptech.client.gui.GuiCrusher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	public static final int CRUSHER = 0;
	public static final int COILER = 1;
	
	@Override
	public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
			case CRUSHER:
				return new ContainerCrusher(player.inventory, (TileEntityCrusher)world.getTileEntity(new BlockPos(x, y, z)));
			case COILER:
				return new ContainerCoiler(player.inventory, (TileEntityCoiler)world.getTileEntity(new BlockPos(x, y, z)));
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
			default:
				return null;
		}
	}


}
