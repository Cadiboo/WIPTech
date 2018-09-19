package cadiboo.wiptech.capability.attachments.circuitdata;

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
public class CapabilityCircuitData {
	@CapabilityInject(CircuitData.class)
	public static Capability<CircuitData> CIRCUIT_DATA = null;

	public static void register() {

		CapabilityManager.INSTANCE.register(CircuitData.class, new IStorage<CircuitData>() {
			@Override
			public NBTTagCompound writeNBT(final Capability<CircuitData> capability, final CircuitData instance, final EnumFacing side) {
				return instance.serializeNBT();
			}

			@Override
			public void readNBT(final Capability<CircuitData> capability, final CircuitData instance, final EnumFacing side, final NBTBase nbt) {
				if (nbt instanceof NBTTagCompound) {
					instance.deserializeNBT((NBTTagCompound) nbt);
				}
			}
		}, () -> new CircuitData(CircuitTypes.SEMI_AUTO));
	}
}
