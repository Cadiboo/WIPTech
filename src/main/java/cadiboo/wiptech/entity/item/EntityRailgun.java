package cadiboo.wiptech.entity.item;

import cadiboo.wiptech.capability.ModEnergyStorage;
import cadiboo.wiptech.entity.ModEntity;
import cadiboo.wiptech.util.IEnergyTransferer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraftforge.common.model.animation.AnimationStateMachine;
import net.minecraftforge.items.IItemHandler;

public class EntityRailgun extends ModEntity implements IWorldNameable, IEnergyTransferer {

	private ModEnergyStorage	energy;
	private IItemHandler		inventory;

	public EntityRailgun(World worldIn) {
		super(worldIn);
		this.energy = new ModEnergyStorage(1000000);
		this.inventory = new IItemHandler() {

			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ItemStack getStackInSlot(int slot) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getSlots() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getSlotLimit(int slot) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public ItemStack extractItem(int slot, int amount, boolean simulate) {
				// TODO Auto-generated method stub
				return null;
			}
		};

		this.setSize(3, 2);
	}

	@Override
	public IBlockAccess getWorld() {
		return this.world;
	}

	@Override
	public ModEnergyStorage getEnergy() {
		return energy;
	}

	@Override
	public AnimationStateMachine getAnimation() {
		return null;
	}

	@Override
	public IItemHandler getInventory() {
		return inventory;
	}

	@Override
	protected void entityInit() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub

	}

}
