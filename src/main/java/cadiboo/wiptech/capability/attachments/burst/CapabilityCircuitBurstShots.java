package cadiboo.wiptech.capability.attachments.burst;

import cadiboo.wiptech.util.ModEnums.CircuitTypes;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

/**
 * @author Cadiboo
 */
public class CapabilityCircuitBurstShots {
	@CapabilityInject(CircuitBurstShots.class)
	public static Capability<CircuitBurstShots> CIRCUIT_BURST_SHOTS = null;

	public static void register() {

		CapabilityManager.INSTANCE.register(CircuitBurstShots.class, new IStorage<CircuitBurstShots>() {
			@Override
			public NBTTagList writeNBT(final Capability<CircuitBurstShots> capability, final CircuitBurstShots instance, final EnumFacing side) {
				final NBTTagList nbtTagList = new NBTTagList();

				return nbtTagList;
			}

			@Override
			public void readNBT(final Capability<CircuitBurstShots> capability, final CircuitBurstShots instance, final EnumFacing side, final NBTBase nbt) {

			}
		}, () -> new CircuitBurstShots(CircuitTypes.MANUAL));
	}
}
