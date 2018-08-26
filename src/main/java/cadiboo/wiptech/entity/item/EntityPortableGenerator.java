package cadiboo.wiptech.entity.item;

import java.util.Random;

import javax.annotation.Nullable;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.capability.IEnergyUser;
import cadiboo.wiptech.capability.IInventoryUser;
import cadiboo.wiptech.capability.ModEnergyStorage;
import cadiboo.wiptech.capability.ModItemStackHandler;
import cadiboo.wiptech.entity.IEntitySyncable;
import cadiboo.wiptech.init.ModItems;
import cadiboo.wiptech.util.ModGuiHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;

public class EntityPortableGenerator extends Entity implements IWorldNameable, IEnergyUser, IInventoryUser, IEntitySyncable {

	private final ModEnergyStorage energy;
	private final ModItemStackHandler inventory;
	private Entity handleHolder;

	public EntityPortableGenerator(World worldIn) {
		super(worldIn);
		this.setSize(1, 1);
		this.preventEntitySpawning = true;
		this.energy = new ModEnergyStorage(100000) {
			@Override
			public void onEnergyChanged() {
				super.onEnergyChanged();
				if (!world.isRemote)
					syncToTracking();
			}
		};
		this.inventory = new ModItemStackHandler(4) {
			@Override
			protected void onContentsChanged(int slot) {
				super.onContentsChanged(slot);
				/* we trust vanilla/forge to do inventory syncing */
			}
		};
	}

	@Override
	protected void entityInit() {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		if (compound.hasKey("energy"))
			this.getEnergy().setEnergy(compound.getInteger("energy"));
		if (compound.hasKey("inventory"))
			this.getInventory().deserializeNBT(compound.getCompoundTag("inventory"));
		if (compound.hasKey("handleHolderId"))
			if (compound.getInteger("handleHolderId") == -1)
				handleHolder = null;
			else
				handleHolder = world.getEntityByID(compound.getInteger("handleHolderId"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setInteger("energy", this.getEnergy().getEnergyStored());
		compound.setTag("inventory", this.getInventory().serializeNBT());
		if (handleHolder == null)
			compound.setInteger("handleHolderId", -1);
		else
			compound.setInteger("handleHolderId", handleHolder.getEntityId());
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		this.doBlockCollisions();

		if (!this.hasNoGravity())
			this.motionY += -0.03999999910593033D;

		this.updateHandleHeld();

		this.motionX *= 0.85;
		this.motionZ *= 0.85;

		this.move(MoverType.SELF, motionX, motionY, motionZ);

		if (world.isRemote)
			return;

		getEnergy().receiveEnergy(5, false);

		getInventory().getStacks().forEach(stack -> {
			if (isFuel(stack) && getEnergy().receiveEnergy(TileEntityFurnace.getItemBurnTime(stack), true) == TileEntityFurnace.getItemBurnTime(stack)) {
				stack.shrink(1);
				getEnergy().receiveEnergy(TileEntityFurnace.getItemBurnTime(stack), false);
				for (int i = 0; i < Math.min(50, ticksExisted); i++) {
					double randX = new Random().nextGaussian() * 0.0025 * ticksExisted;
					double randY = new Random().nextDouble() * 0.025 * Math.min(25, ticksExisted);
					double randZ = new Random().nextGaussian() * 0.0025 * ticksExisted;

//					randX = randY = randZ = 0;

					world.spawnParticle(EnumParticleTypes.FLAME, true, posX, posY, posZ, randX, randY, randZ);
				}
			}
		});

		this.transferEnergyToAllAround();

		this.handleSync();

	}

	private void updateHandleHeld() {
		if (this.getHandleHolder() == null)
			return;

		if (this.getHandleHolder().world != this.world) {
			clearHandleHolder();
			return;
		}

		Entity entity = this.getHandleHolder();

		RayTraceResult rayTraceResult = world.rayTraceBlocks(new Vec3d(entity.posX, entity.posY, entity.posZ), new Vec3d(entity.posX, 0, entity.posZ), true, false, false);

		if (rayTraceResult.hitVec == null)
			rayTraceResult.hitVec = new Vec3d(entity.posX, 0, entity.posZ);

		boolean flag = entity.posY - rayTraceResult.hitVec.y > 2;

		if (flag) {
			this.motionY = entity.motionY;
		}

		double yaw = Math.atan2(entity.getPositionVector().x - getPositionVector().x, entity.getPositionVector().z - getPositionVector().z);
		yaw = yaw * (180 / Math.PI);
		yaw = yaw < 0 ? 360 - (-yaw) : yaw;
		this.rotationYaw = 180 - (float) yaw;

		float distance = this.getDistance(entity);

		if (distance < this.width * 2)
			return;

		if (distance > 5) {
			clearHandleHolder();
			return;
		}

		double d0 = (entity.posX - this.posX) / distance;
		double d1 = (entity.posY - this.posY) / distance;
		double d2 = (entity.posZ - this.posZ) / distance;
		if ((Math.abs(d0 * distance) > 2 || Math.abs(d2 * distance) > 2) && entity.posY > this.posY)
			d1 += 0.3 * Math.abs(entity.posY - this.posY);
		this.motionX += d0 * Math.abs(d0) * 0.4D;
		this.motionY += d1 * Math.abs(d1) * 0.4D;
		this.motionZ += d2 * Math.abs(d2) * 0.4D;
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY)
			return true;
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY)
			return (T) getEnergy();
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T) getInventory();
		return super.getCapability(capability, facing);
	}

	@Override
	public ModEnergyStorage getEnergy() {
		return this.energy;
	}

	@Override
	public ModItemStackHandler getInventory() {
		return inventory;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return super.getRenderBoundingBox().grow(1);
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBox(Entity entityIn) {
		return entityIn.canBePushed() ? entityIn.getEntityBoundingBox() : null;
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox() {
		return getEntityBoundingBox().shrink(0.25f);
	}

	@Override
	public boolean canBePushed() {
		return !this.isDead;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isEntityInvulnerable(source)) {
			return false;
		} else if (!this.world.isRemote && !this.isDead) {
			if (source instanceof EntityDamageSourceIndirect && source.getTrueSource() != null && this.isPassenger(source.getTrueSource())) {
				return false;
			} else {
				this.markVelocityChanged();
				boolean flag = source.getTrueSource() instanceof EntityPlayer && ((EntityPlayer) source.getTrueSource()).capabilities.isCreativeMode;

				if (!flag && this.world.getGameRules().getBoolean("doEntityDrops")) {
					this.dropItemWithOffset(ModItems.PORTABLE_GENERATOR, 1, 0.0F);
					inventory.dropItems(world, posX, posY, posZ);
				}

				this.setDead();

				return true;
			}
		} else {
			return true;
		}
	}

	@Override
	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public void setHandleHolder(Entity handleHolder) {
		this.handleHolder = handleHolder;
		this.syncToTracking();
	}

	public void clearHandleHolder() {
		this.handleHolder = null;
		this.syncToTracking();
	}

	public Entity getHandleHolder() {
		return handleHolder;
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if (super.processInitialInteract(player, hand))
			return true;

		if (!world.isRemote) {
			if (hand == EnumHand.OFF_HAND)/* don't care, just don't want the event firing twice */
				return false;
			if (player.isSneaking()) {
				player.openGui(WIPTech.instance, ModGuiHandler.PORTABLE_GENERATOR, world, getPosition().getX(), getPosition().getY(), getPosition().getZ());
				return true;
			} else {
				if (getHandleHolder() == player) {
					clearHandleHolder();
					return true;
				}
				setHandleHolder(player);
				return true;
			}
		}
		return false;
	}

	public boolean isFuel(ItemStack stack) {
		return TileEntityFurnace.getItemBurnTime(stack) > 0;
	}

	@Override
	public Entity getEntity() {
		return this;
	}

	@Override
	public void writeSyncTag(NBTTagCompound compound) {
		writeEntityToNBT(compound);
	}

	@Override
	public void readSyncTag(NBTTagCompound compound) {
		readEntityFromNBT(compound);
	}

}
