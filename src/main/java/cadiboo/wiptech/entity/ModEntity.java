package cadiboo.wiptech.entity;

import javax.annotation.Nullable;

import cadiboo.wiptech.capability.ModEnergyStorage;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.model.animation.AnimationStateMachine;
import net.minecraftforge.common.model.animation.CapabilityAnimation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class ModEntity extends Entity {

	public ModEntity(World worldIn) {
		super(worldIn);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return this.getCapability(capability, facing) != null;
	}

	@Override
	@Nullable
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY)
			return (T) this.getEnergy();
		if (capability == CapabilityAnimation.ANIMATION_CAPABILITY)
			return (T) this.getAnimation();
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T) this.getInventory();
		return super.getCapability(capability, facing);
	}

	@Nullable
	public abstract ModEnergyStorage getEnergy();

	@Nullable
	public abstract AnimationStateMachine getAnimation();

	@Nullable
	public abstract IItemHandler getInventory();

}
