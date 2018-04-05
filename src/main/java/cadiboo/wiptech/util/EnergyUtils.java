package cadiboo.wiptech.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyUtils {
	public static String formatEnergy(IEnergyStorage energy) {
		return String.format("%s/%s", formatEnergy(energy.getEnergyStored()),
				formatEnergy(energy.getMaxEnergyStored()));
	}

	public static String formatEnergy(int energy) {
		if (energy < 1000) {
			return energy + " " + Reference.ENERGY_UNIT;
		}
		final int exp = (int) (Math.log(energy) / Math.log(1000));
		final char unitType = "kmg".charAt(exp - 1);
		return String.format("%.1f %s%s", energy / Math.pow(1000, exp), unitType, Reference.ENERGY_UNIT);

		// NumberFormat format = NumberFormat.getInstance();
		// tooltip.add(String.format("%s/%s Pirate Ninjas",
		// format.format(energy.getEnergyStored()),
		// format.format(energy.getMaxEnergyStored())));

	}

	public static int pushEnergy(World world, BlockPos pos, IEnergyStorage energy) {
		return pushEnergy(world, pos, energy, EnumFacing.values());
	}

	public static int pushEnergy(World world, BlockPos pos, IEnergyStorage energy, EnumFacing... sides) {
		if (!world.isRemote && energy.canExtract()) {
			for (EnumFacing side : sides) {
				TileEntity tile = world.getTileEntity(pos.offset(side));
				if (tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, side.getOpposite())) {
					IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
					if (storage != null && storage.canReceive()) {
						return energy.extractEnergy(
								storage.receiveEnergy(energy.extractEnergy(Integer.MAX_VALUE, true), false), false);
					}
				}
			}
		}
		return 0;
	}
}
