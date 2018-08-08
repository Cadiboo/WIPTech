package cadiboo.wiptech.entity.item;

import cadiboo.wiptech.capability.ModEnergyStorage;
import cadiboo.wiptech.entity.ModEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.model.animation.AnimationStateMachine;
import net.minecraftforge.items.IItemHandler;

public class PortableGenerator extends ModEntity {

	protected ModEnergyStorage energy;

	public PortableGenerator(World worldIn) {
		super(worldIn);
		this.setSize(1, 1);
		this.energy = new ModEnergyStorage(1000);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void entityInit() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		if (compound.hasKey("energy"))
			this.getEnergy().setEnergy(compound.getInteger("energy"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setInteger("energy", this.getEnergy().getEnergyStored());
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.energy.receiveEnergy(10, false);
		// TODO Auto-generated method stub
	}

	@Override
	public ModEnergyStorage getEnergy() {
		return this.energy;
	}

	@Override
	public AnimationStateMachine getAnimation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IItemHandler getInventory() {
		// TODO Auto-generated method stub
		return null;
	}

}
