package cadiboo.wiptech.util;

import java.util.List;

import cadiboo.wiptech.client.gui.inventory.GuiPortableGenerator;
import cadiboo.wiptech.client.gui.inventory.GuiRailgun;
import cadiboo.wiptech.entity.item.EntityPortableGenerator;
import cadiboo.wiptech.entity.item.EntityRailgun;
import cadiboo.wiptech.inventory.ContainerPortableGenerator;
import cadiboo.wiptech.inventory.ContainerRailgun;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModGuiHandler implements IGuiHandler {

	public static final int RAILGUN = 0;
	public static final int PORTABLE_GENERATOR = 1;

	@Override
	public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case RAILGUN:
			List<Entity> railguns = world.getEntitiesWithinAABB(EntityRailgun.class, new AxisAlignedBB(new BlockPos(x, y, z)));
			if (railguns.size() > 0 && railguns.get(0) != null && railguns.get(0) instanceof EntityRailgun)
				return new ContainerRailgun(player.inventory, (EntityRailgun) railguns.get(0));
			else {
				assert false;
				return null;
			}
		case PORTABLE_GENERATOR:
			List<Entity> portableGenerators = world.getEntitiesWithinAABB(EntityPortableGenerator.class, new AxisAlignedBB(new BlockPos(x, y, z)));
			if (portableGenerators.size() > 0 && portableGenerators.get(0) != null && portableGenerators.get(0) instanceof EntityPortableGenerator)
				return new ContainerPortableGenerator(player.inventory, (EntityPortableGenerator) portableGenerators.get(0));
			else {
				assert false;
				return null;
			}
		default:
			return null;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Gui getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case RAILGUN:
			List<Entity> railguns = world.getEntitiesWithinAABB(EntityRailgun.class, new AxisAlignedBB(new BlockPos(x, y, z)));
			if (railguns.size() > 0 && railguns.get(0) != null && railguns.get(0) instanceof EntityRailgun)
				return new GuiRailgun(player.inventory, (EntityRailgun) railguns.get(0));
			else {
				assert false;
				return null;
			}
		case PORTABLE_GENERATOR:
			List<Entity> portableGenerators = world.getEntitiesWithinAABB(EntityPortableGenerator.class, new AxisAlignedBB(new BlockPos(x, y, z)));
			if (portableGenerators.size() > 0 && portableGenerators.get(0) != null && portableGenerators.get(0) instanceof EntityPortableGenerator)
				return new GuiPortableGenerator(player.inventory, (EntityPortableGenerator) portableGenerators.get(0));
			else {
				assert false;
				return null;
			}
		default:
			return null;
		}
	}

}
