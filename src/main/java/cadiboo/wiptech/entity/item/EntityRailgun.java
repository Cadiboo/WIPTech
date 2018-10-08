package cadiboo.wiptech.entity.item;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.capability.energy.IEnergyUserAdvanced;
import cadiboo.wiptech.capability.energy.ModEnergyStorage;
import cadiboo.wiptech.capability.inventory.IInventoryUser;
import cadiboo.wiptech.capability.inventory.ModItemStackHandler;
import cadiboo.wiptech.entity.IEntitySyncable;
import cadiboo.wiptech.entity.IModEntity;
import cadiboo.wiptech.entity.projectile.EntitySlug;
import cadiboo.wiptech.init.ModItems;
import cadiboo.wiptech.item.ItemCasedSlug;
import cadiboo.wiptech.item.ItemSlug;
import cadiboo.wiptech.material.ModMaterial;
import cadiboo.wiptech.util.ExistsForDebugging;
import cadiboo.wiptech.util.ModGuiHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

public class EntityRailgun extends Entity implements IModEntity, IWorldNameable, IEnergyUserAdvanced, IInventoryUser, IEntitySyncable {

	private float momentum;
	private float deltaRotation;
	private int lerpSteps;
	private double lerpX;
	private double lerpY;
	private double lerpZ;
	private double lerpYaw;
	private double lerpPitch;

	private final ModEnergyStorage energy;
	private final ModItemStackHandler inventory;
	private int shootCooldown;

	public EntityRailgun(final World world) {
		super(world);
		this.preventEntitySpawning = true;
		this.setSize(3, 2);

		this.energy = new ModEnergyStorage(100000) {
			@Override
			public void onEnergyChanged() {
				super.onEnergyChanged();
				if (!world.isRemote) {
					EntityRailgun.this.syncToTracking();
				}
			}
		};
		this.inventory = new ModItemStackHandler(3) {
			@Override
			protected void onContentsChanged(final int slot) {
				super.onContentsChanged(slot);
				/* we trust vanilla/forge to do inventory syncing */
			}
		};
	}

	public EntityRailgun(final World world, final double x, final double y, final double z) {
		this(world);
		this.setPosition(x, y, z);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	public int getShootCooldown() {
		return this.shootCooldown;
	}

	@Override
	protected void entityInit() {
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBox(final Entity entity) {
		return entity.getEntityBoundingBox();
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox() {
		return this.getEntityBoundingBox();
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public double getMountedYOffset() {
		return 1.5;
	}

	@Override
	public boolean attackEntityFrom(final DamageSource source, final float amount) {
		if (this.isEntityInvulnerable(source)) {
			return false;
		} else if (!this.world.isRemote && !this.isDead) {
			if ((source instanceof EntityDamageSourceIndirect) && (source.getTrueSource() != null) && this.isPassenger(source.getTrueSource())) {
				return false;
			} else {
				this.markVelocityChanged();
				final boolean flag = (source.getTrueSource() instanceof EntityPlayer) && ((EntityPlayer) source.getTrueSource()).capabilities.isCreativeMode;

				if (!flag && this.world.getGameRules().getBoolean("doEntityDrops")) {
					this.dropItemWithOffset(ModItems.RAILGUN, 1, 0.0F);
					this.inventory.dropItems(this.world, this.posX, this.posY, this.posZ);
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

	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotationDirect(final double x, final double y, final double z, final float yaw, final float pitch, final int posRotationIncrements, final boolean teleport) {
		this.lerpX = x;
		this.lerpY = y;
		this.lerpZ = z;
		this.lerpYaw = yaw;
		this.lerpPitch = pitch;
		this.lerpSteps = 10;
	}

	@Override
	public EnumFacing getAdjustedHorizontalFacing() {
		return this.getHorizontalFacing().rotateY();
	}

	@Override
	public void onUpdate() {

		this.shoot();

		this.shootCooldown--;

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		super.onUpdate();
		this.tickLerp();

		if (this.canPassengerSteer()) {
			this.updateMotion();

			if (this.world.isRemote) {
				this.controlRailgun();
			}

			this.move(MoverType.SELF, 0, this.motionY, 0);
		}

		this.doBlockCollisions();

		final int width = Math.round(this.width / 2);
		final int height = Math.round(this.height / 2);
		final int length = width;
		for (int x = -(width); x <= width; x++) {
			for (int y = -(height); y <= height; y++) {
				for (int z = -(length); z <= length; z++) {
					final BlockPos pos = new BlockPos(this.posX + x, this.posY + y, this.posZ + z);
					if (this.world.getTileEntity(pos) != null) {
						if (this.world.getTileEntity(pos).hasCapability(CapabilityEnergy.ENERGY, null)) {
							if (this.world.getTileEntity(pos).getCapability(CapabilityEnergy.ENERGY, null) != null) {
								if (this.world.getTileEntity(pos).getCapability(CapabilityEnergy.ENERGY, null).canExtract()) {
									this.energy.receiveEnergy(this.world.getTileEntity(pos).getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(this.energy.receiveEnergy(Integer.MAX_VALUE, true), false), false);
								}
							}
						}
					}
					for (final Entity entity : this.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos))) {
						if (entity == null) {
							continue;
						}
						if (entity.getCapability(CapabilityEnergy.ENERGY, null) != null) {
							if (entity.getCapability(CapabilityEnergy.ENERGY, null).canExtract()) {
								this.energy.receiveEnergy(entity.getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(this.energy.receiveEnergy(Integer.MAX_VALUE, true), false), false);
							}
						}
					}

				}
			}
		}

	}

	private void tickLerp() {
		if ((this.lerpSteps > 0) && !this.canPassengerSteer()) {
			final double d0 = this.posX + ((this.lerpX - this.posX) / this.lerpSteps);
			final double d1 = this.posY + ((this.lerpY - this.posY) / this.lerpSteps);
			final double d2 = this.posZ + ((this.lerpZ - this.posZ) / this.lerpSteps);
			final double d3 = MathHelper.wrapDegrees(this.lerpYaw - this.rotationYaw);
			this.rotationYaw = (float) (this.rotationYaw + (d3 / this.lerpSteps));
			this.rotationPitch = (float) (this.rotationPitch + ((this.lerpPitch - this.rotationPitch) / this.lerpSteps));
			--this.lerpSteps;
			this.setPosition(d0, d1, d2);
			this.setRotation(this.rotationYaw, this.rotationPitch);
		}
	}

	private void updateMotion() {
		final double d1 = this.hasNoGravity() ? 0.0D : -0.03999999910593033D;
		this.motionY += d1;
	}

	private void controlRailgun() {
		if (this.isBeingRidden()) {
			float pitch = this.getControllingPassenger().rotationPitch;
			pitch = Math.max(-this.getMaxPitch(), pitch);
			pitch = Math.min(this.getMaxPitch(), pitch);
			this.setRotation(this.getControllingPassenger().rotationYaw, pitch);
		}
	}

	public float getMaxPitch() {
		return 60;
	}

	@Override
	public void updatePassenger(final Entity passenger) {
		if (this.isPassenger(passenger)) {
			float f = 0.0F;
			final float f1 = (float) ((this.isDead ? 0.009999999776482582D : this.getMountedYOffset()) + passenger.getYOffset());

			if (this.getPassengers().size() > 1) {
				final int i = this.getPassengers().indexOf(passenger);

				if (i == 0) {
					f = 0.2F;
				} else {
					f = -0.6F;
				}

				if (passenger instanceof EntityAnimal) {
					f = (float) (f + 0.2D);
				}
			}

			final Vec3d vec3d = (new Vec3d(f, 0.0D, 0.0D)).rotateYaw((-this.rotationYaw * 0.017453292F) - ((float) Math.PI / 2F));
			passenger.setPosition(this.posX + vec3d.x, this.posY + f1, this.posZ + vec3d.z);
			passenger.rotationYaw += this.deltaRotation;
			passenger.setRotationYawHead(passenger.getRotationYawHead() + this.deltaRotation);
			this.applyYawToEntity(passenger);

			if ((passenger instanceof EntityAnimal) && (this.getPassengers().size() > 1)) {
				final int j = (passenger.getEntityId() % 2) == 0 ? 90 : 270;
				passenger.setRenderYawOffset(((EntityAnimal) passenger).renderYawOffset + j);
				passenger.setRotationYawHead(passenger.getRotationYawHead() + j);
			}
		}
	}

	protected void applyYawToEntity(final Entity entityToUpdate) {
		entityToUpdate.setRenderYawOffset(this.rotationYaw);
		final float f = MathHelper.wrapDegrees(entityToUpdate.rotationYaw - this.rotationYaw);
		final float f1 = MathHelper.clamp(f, -105.0F, 105.0F);
		entityToUpdate.prevRotationYaw += f1 - f;
		entityToUpdate.rotationYaw += f1 - f;
		entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void applyOrientationToEntity(final Entity entityToUpdate) {
		this.applyYawToEntity(entityToUpdate);
	}

	@Override
	public boolean processInitialInteract(final EntityPlayer player, final EnumHand hand) {
		if (super.processInitialInteract(player, hand)) {
			return true;
		}

		if (!this.world.isRemote) {
			if (player.isSneaking()) {
				player.openGui(WIPTech.instance, ModGuiHandler.RAILGUN, this.world, this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ());
				return true;
			} else {
				player.startRiding(this);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canRiderInteract() {
		return true;
	}

	@Override
	public EnumActionResult applyPlayerInteraction(final EntityPlayer player, final Vec3d vec, final EnumHand hand) {

		return super.applyPlayerInteraction(player, vec, hand);
	}

	private ItemStack findAmmo() {
		final Iterator<ItemStack> it = this.getInventory().getStacks().iterator();
		while (it.hasNext()) {
			final ItemStack check = it.next();
			if (this.isAmmo(check)) {
				return check;
			}
		}
		return ItemStack.EMPTY;
	}

	protected boolean isAmmo(final ItemStack stack) {
		return stack.getItem() instanceof ItemCasedSlug;
	}

	public void shoot() {
		if ((this.getControllingPassenger() != null) && (this.getControllingPassenger() instanceof EntityPlayer)) {
			final EntityPlayer player = (EntityPlayer) this.getControllingPassenger();

			if (this.world.isRemote) {
				return;
			}

			if (this.getShootCooldown() > 0) {
				return;
			}

			final boolean shootAnyway = player.isCreative();

			if (!shootAnyway && ((this.energy.getEnergyStored() < this.getShootEnergy()) || (this.energy.extractEnergy(this.getShootEnergy(), true) != this.getShootEnergy()))) {
				return;
			}

			ItemStack ammo = this.findAmmo();

			if (ammo.isEmpty()) {
				if (shootAnyway) {
					ammo = new ItemStack(ModMaterial.IRON.getCasedSlug());
				} else {
					return;
				}
			}

			final ItemStack projectile = new ItemStack(((ItemCasedSlug) ammo.getItem()).getModMaterial().getSlugItem());

			this.energy.extractEnergy(this.getShootEnergy(), false);

			final float velocity = 4f;

			final Vec3d look = this.getLookVec();

			final EntitySlug slug = new EntitySlug(this.world, ((ItemSlug) projectile.getItem()).getModMaterial());
			slug.setPosition(this.getPosition().getX() + 0.5, this.getPosition().getY() + this.getEyeHeight(), this.getPosition().getZ() + 0.5);

			slug.prevRotationPitch = this.rotationPitch;
			slug.prevRotationYaw = this.rotationYaw;

			slug.setThrower(player);

			slug.shoot(this.getControllingPassenger(), this.rotationPitch, this.rotationYaw, 0, velocity, 0);
			this.world.spawnEntity(slug);

			// TODO FIXME THIS IS BAD, FORGE DOCS SAYS EXPLICITLY "SERIOUSLY: DO NOT MODIFY THE RETURNED ITEMSTACK."
			ammo.shrink(1);
		}
	}

	@ExistsForDebugging
	public void shootInternal() {
		if (this.world.isRemote) {
			return;
		}

		if (this.getShootCooldown() > 0) {
			return;
		}

		final boolean shootAnyway = Boolean.valueOf(true);

		if (!shootAnyway && ((this.energy.getEnergyStored() < this.getShootEnergy()) || (this.energy.extractEnergy(this.getShootEnergy(), true) != this.getShootEnergy()))) {
			return;
		}

		ItemStack ammo = this.findAmmo();

		if (ammo.isEmpty()) {
			if (shootAnyway) {
				ammo = new ItemStack(ModMaterial.IRON.getCasedSlug());
			} else {
				return;
			}
		}

		final ItemStack projectile = new ItemStack(((ItemCasedSlug) ammo.getItem()).getModMaterial().getSlugItem());

		this.energy.extractEnergy(this.getShootEnergy(), false);

		final float velocity = 4f;

		final Vec3d look = this.getLookVec();

		final EntitySlug slug = new EntitySlug(this.world, ((ItemSlug) projectile.getItem()).getModMaterial());
		slug.setPosition(this.getPosition().getX() + 0.5, this.getPosition().getY() + this.getEyeHeight(), this.getPosition().getZ() + 0.5);

		slug.prevRotationPitch = this.rotationPitch;
		slug.prevRotationYaw = this.rotationYaw;

		slug.shoot(this, this.rotationPitch, this.rotationYaw, 0, velocity, 0);

		this.world.spawnEntity(slug);

		// TODO FIXME THIS IS BAD, FORGE DOCS SAYS EXPLICITLY "SERIOUSLY: DO NOT MODIFY THE RETURNED ITEMSTACK."
		ammo.shrink(1);

	}

	public int getShootEnergy() {
		return 1000;
	}

	@Override
	protected boolean canFitPassenger(final Entity passenger) {
		return this.getPassengers().size() < 1;
	}

	@Override
	@Nullable
	public Entity getControllingPassenger() {
		final List<Entity> list = this.getPassengers();
		return list.isEmpty() ? null : (Entity) list.get(0);
	}

	// Forge: Fix MC-119811 by instantly completing lerp on board
	@Override
	protected void addPassenger(final Entity passenger) {
		super.addPassenger(passenger);
		this.shootCooldown = 10;
		if (this.canPassengerSteer() && (this.lerpSteps > 0)) {
			this.lerpSteps = 0;
			this.posX = this.lerpX;
			this.posY = this.lerpY;
			this.posZ = this.lerpZ;
			this.rotationYaw = (float) this.lerpYaw;
			this.rotationPitch = (float) this.lerpPitch;
		}
	}

	@Override
	public World getWorld() {
		return this.world;
	}

	@Override
	public boolean hasCapability(final Capability<?> capability, final EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return true;
		}
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(final Capability<T> capability, final EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return (T) this.energy;
		}
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) this.inventory;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public ModEnergyStorage getEnergy() {
		return this.energy;
	}

	@Override
	public ModItemStackHandler getInventory() {
		return this.inventory;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return super.getRenderBoundingBox().grow(1);
	}

	@Override
	public Entity getEntity() {
		return this;
	}

	@Override
	protected void readEntityFromNBT(final NBTTagCompound compound) {
		IEnergyUserAdvanced.super.deserializeNBT(compound);
		IInventoryUser.super.deserializeNBT(compound);
	}

	@Override
	protected void writeEntityToNBT(final NBTTagCompound compound) {
		compound.merge(IEnergyUserAdvanced.super.serializeNBT());
		compound.merge(IInventoryUser.super.serializeNBT());
	}

	@Override
	public void writeSyncTag(final NBTTagCompound compound) {
		this.writeEntityToNBT(compound);
	}

	@Override
	public void readSyncTag(final NBTTagCompound compound) {
		this.readEntityFromNBT(compound);
	}

}
