package cadiboo.wiptech.capability.attachments.burst;

import cadiboo.wiptech.util.ModEnums.CircuitTypes;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
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
			public NBTTagCompound writeNBT(final Capability<CircuitBurstShots> capability, final CircuitBurstShots instance, final EnumFacing side) {
				final NBTTagCompound compound = new NBTTagCompound();

				return compound;
			}

			@Override
			public void readNBT(final Capability<CircuitBurstShots> capability, final CircuitBurstShots instance, final EnumFacing side, final NBTBase nbt) {

			}
		}, () -> new CircuitBurstShots(CircuitTypes.MANUAL));
	}
}
