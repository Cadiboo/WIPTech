package cadiboo.wiptech.entity.item;

import javax.annotation.Nullable;

import cadiboo.wiptech.capability.ModEnergyStorage;
import cadiboo.wiptech.entity.ModEntity;
import cadiboo.wiptech.util.IEnergyTransferer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraftforge.common.model.animation.AnimationStateMachine;
import net.minecraftforge.items.IItemHandler;

public class EntityPortableGenerator extends ModEntity implements IWorldNameable, IEnergyTransferer {

	protected ModEnergyStorage energy;

	public EntityPortableGenerator(World worldIn) {
		super(worldIn);
		this.setSize(1, 1);
		this.preventEntitySpawning = true;
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

		IBlockState iblockstate = getWorld().getBlockState(this.getPosition());
		Block block = iblockstate.getBlock();

		if (iblockstate.getMaterial() != Material.AIR) {
			AxisAlignedBB axisalignedbb = iblockstate.getCollisionBoundingBox(this.world, getPosition());

			if (axisalignedbb != Block.NULL_AABB && axisalignedbb.offset(getPosition().add(0, this.height / 2, 0)).contains(new Vec3d(this.posX, this.posY, this.posZ))) {
				this.onGround = true;
			}
		}

		if (this.onGround) {
			this.motionY = 0;
		}

		if (!this.hasNoGravity() && !this.onGround)
			this.motionY -= 0.05000000074505806D;
//
//		this.posX += this.motionX;
		this.posY += this.motionY;
//		this.posZ += this.motionZ;

		this.motionX = 0;
//		this.motionY = 0;
		this.motionZ = 0;

		getEnergy().receiveEnergy(5, false);

		this.transferEnergyToAllAround();

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

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBox(Entity entityIn) {
		return entityIn.canBePushed() ? entityIn.getEntityBoundingBox() : null;
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox() {
		return Block.FULL_BLOCK_AABB;
	}

	@Override
	public boolean canBePushed() {
		return true;
	}

	@Override
	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	@Override
	public IBlockAccess getWorld() {
		return this.getEntityWorld();
	}

}
