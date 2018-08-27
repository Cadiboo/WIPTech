package cadiboo.wiptech.entity.item;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import cadiboo.wiptech.WIPTech;
import cadiboo.wiptech.capability.energy.IEnergyUser;
import cadiboo.wiptech.capability.energy.ModEnergyStorage;
import cadiboo.wiptech.capability.inventory.IInventoryUser;
import cadiboo.wiptech.capability.inventory.ModItemStackHandler;
import cadiboo.wiptech.entity.IEntitySyncable;
import cadiboo.wiptech.entity.projectile.EntitySlug;
import cadiboo.wiptech.init.ModItems;
import cadiboo.wiptech.item.ItemSlug;
import cadiboo.wiptech.util.ModEnums.ModMaterials;
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

public class EntityRailgun extends Entity implements IWorldNameable, IEnergyUser, IInventoryUser, IEntitySyncable {

	private float momentum;
	private float deltaRotation;
	private int lerpSteps;
	private double lerpX;
	private double lerpY;
	private double lerpZ;
	private double lerpYaw;
	private double lerpPitch;

	private ModEnergyStorage energy;
	private ModItemStackHandler inventory;
	private int shootCooldown;

	public EntityRailgun(World worldIn) {
		super(worldIn);
		this.preventEntitySpawning = true;
		this.setSize(3, 2);

		this.energy = new ModEnergyStorage(100000) {
			@Override
			public void onEnergyChanged() {
				super.onEnergyChanged();
				if (!world.isRemote)
					syncToTracking();
			}
		};
		this.inventory = new ModItemStackHandler(3) {
			@Override
			protected void onContentsChanged(int slot) {
				super.onContentsChanged(slot);
				/* we trust vanilla/forge to do inventory syncing */
			}
		};
	}

	public EntityRailgun(World worldIn, double x, double y, double z) {
		this(worldIn);
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
		return shootCooldown;
	}

	@Override
	protected void entityInit() {
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBox(Entity entityIn) {
		return entityIn.getEntityBoundingBox();
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
					this.dropItemWithOffset(ModItems.RAILGUN, 1, 0.0F);
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

	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
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

		shootCooldown--;

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

		int width = Math.round(this.width / 2);
		int height = Math.round(this.height / 2);
		int length = width;
		for (int x = -(width); x <= width; x++) {
			for (int y = -(height); y <= height; y++) {
				for (int z = -(length); z <= length; z++) {
					BlockPos pos = new BlockPos(this.posX + x, this.posY + y, this.posZ + z);
					if (world.getTileEntity(pos) != null)
						if (world.getTileEntity(pos).hasCapability(CapabilityEnergy.ENERGY, null))
							if (world.getTileEntity(pos).getCapability(CapabilityEnergy.ENERGY, null) != null)
								if (world.getTileEntity(pos).getCapability(CapabilityEnergy.ENERGY, null).canExtract())
									energy.receiveEnergy(world.getTileEntity(pos).getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(energy.receiveEnergy(Integer.MAX_VALUE, true), false),
											false);
					for (Entity entity : world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos))) {
						if (entity == null)
							continue;
						if (entity.getCapability(CapabilityEnergy.ENERGY, null) != null)
							if (entity.getCapability(CapabilityEnergy.ENERGY, null).canExtract())
								energy.receiveEnergy(entity.getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(energy.receiveEnergy(Integer.MAX_VALUE, true), false), false);
					}

				}
			}
		}

	}

	private void tickLerp() {
		if (this.lerpSteps > 0 && !this.canPassengerSteer()) {
			double d0 = this.posX + (this.lerpX - this.posX) / this.lerpSteps;
			double d1 = this.posY + (this.lerpY - this.posY) / this.lerpSteps;
			double d2 = this.posZ + (this.lerpZ - this.posZ) / this.lerpSteps;
			double d3 = MathHelper.wrapDegrees(this.lerpYaw - this.rotationYaw);
			this.rotationYaw = (float) (this.rotationYaw + d3 / this.lerpSteps);
			this.rotationPitch = (float) (this.rotationPitch + (this.lerpPitch - this.rotationPitch) / this.lerpSteps);
			--this.lerpSteps;
			this.setPosition(d0, d1, d2);
			this.setRotation(this.rotationYaw, this.rotationPitch);
		}
	}

	private void updateMotion() {
		double d1 = this.hasNoGravity() ? 0.0D : -0.03999999910593033D;
		this.motionY += d1;
	}

	private void controlRailgun() {
		if (this.isBeingRidden()) {
			float pitch = getControllingPassenger().rotationPitch;
			pitch = Math.max(-getMaxPitch(), pitch);
			pitch = Math.min(getMaxPitch(), pitch);
			setRotation(getControllingPassenger().rotationYaw, pitch);
		}
	}

	public float getMaxPitch() {
		return 60;
	}

	@Override
	public void updatePassenger(Entity passenger) {
		if (this.isPassenger(passenger)) {
			float f = 0.0F;
			float f1 = (float) ((this.isDead ? 0.009999999776482582D : this.getMountedYOffset()) + passenger.getYOffset());

			if (this.getPassengers().size() > 1) {
				int i = this.getPassengers().indexOf(passenger);

				if (i == 0) {
					f = 0.2F;
				} else {
					f = -0.6F;
				}

				if (passenger instanceof EntityAnimal) {
					f = (float) (f + 0.2D);
				}
			}

			Vec3d vec3d = (new Vec3d(f, 0.0D, 0.0D)).rotateYaw(-this.rotationYaw * 0.017453292F - ((float) Math.PI / 2F));
			passenger.setPosition(this.posX + vec3d.x, this.posY + f1, this.posZ + vec3d.z);
			passenger.rotationYaw += this.deltaRotation;
			passenger.setRotationYawHead(passenger.getRotationYawHead() + this.deltaRotation);
			this.applyYawToEntity(passenger);

			if (passenger instanceof EntityAnimal && this.getPassengers().size() > 1) {
				int j = passenger.getEntityId() % 2 == 0 ? 90 : 270;
				passenger.setRenderYawOffset(((EntityAnimal) passenger).renderYawOffset + j);
				passenger.setRotationYawHead(passenger.getRotationYawHead() + j);
			}
		}
	}

	protected void applyYawToEntity(Entity entityToUpdate) {
		entityToUpdate.setRenderYawOffset(this.rotationYaw);
		float f = MathHelper.wrapDegrees(entityToUpdate.rotationYaw - this.rotationYaw);
		float f1 = MathHelper.clamp(f, -105.0F, 105.0F);
		entityToUpdate.prevRotationYaw += f1 - f;
		entityToUpdate.rotationYaw += f1 - f;
		entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void applyOrientationToEntity(Entity entityToUpdate) {
		this.applyYawToEntity(entityToUpdate);
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setInteger("energy", energy.getEnergyStored());
		compound.setTag("inventory", getInventory().serializeNBT());
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		if (compound.hasKey("energy"))
			energy.setEnergyStored(compound.getInteger("energy"));
		if (compound.hasKey("inventory"))
			getInventory().deserializeNBT(compound.getCompoundTag("inventory"));
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
		if (super.processInitialInteract(player, hand))
			return true;

		if (!world.isRemote) {
			if (player.isSneaking()) {
				player.openGui(WIPTech.instance, ModGuiHandler.RAILGUN, world, getPosition().getX(), getPosition().getY(), getPosition().getZ());
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
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand) {

		return super.applyPlayerInteraction(player, vec, hand);
	}

	private ItemStack findAmmo() {
		Iterator<ItemStack> it = getInventory().getStacks().iterator();
		while (it.hasNext()) {
			if (isAmmo(it.next()))
				return it.next();
		}
		return ItemStack.EMPTY;
	}

	protected boolean isAmmo(ItemStack stack) {
		return stack.getItem() instanceof ItemSlug;
	}

	public void shoot() {
		if (getControllingPassenger() != null && getControllingPassenger() instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) getControllingPassenger();

			if (world.isRemote)
				return;// EnumActionResult.SUCCESS;

			if (getShootCooldown() > 0)
				return;

			if (energy.getEnergyStored() < getShootEnergy() || energy.extractEnergy(getShootEnergy(), true) != getShootEnergy())
				return;

			ItemStack ammo = this.findAmmo();

			if (ammo.isEmpty())
				if (player.isCreative())
					ammo = new ItemStack(ModMaterials.IRON.getSlugItem());
				else
					return;

			energy.extractEnergy(getShootEnergy(), false);

			float velocity = 4f;

			Vec3d look = this.getLookVec();

			BlockPos pos = getPosition();

//			float dx = endX - startX;
//			float dy = endY - startY;
//			float scale = 100 / sqrt (dx * dx + dy * dy);
//			CGPoint p1 = CGPointMake (startX, startY);
//			CGPoint p2 = CGPointMake (startX + dx * scale, startY + dy * scale); 

			double startX = 0;
			double startY = 0;
			double startZ = 0;

			double endX = look.x;
			double endY = look.y;
			double endZ = look.z;

			double dx = endX - startX;
			double dy = endY - startY;
			double dz = endZ - startZ;
			double scale = 100 / Math.sqrt(dx * dx + dy * dy + dz * dz);
//			double scale = 50;// 100 / Math.sqrt(dx * dx + dy * dy + dz * dz);
//			CGPoint p1 = CGPointMake (startX, startY);
			Vec3d p2 = new Vec3d(startX + dx * scale, startY + dy * scale, startZ + dz * scale);

			EntitySlug slug = new EntitySlug(world, ((ItemSlug) ammo.getItem()).getModMaterial());
			slug.setPosition(pos.getX() + 0.5, pos.getY() + getEyeHeight() - 1, pos.getZ() + 0.5);

			slug.setThrower((EntityPlayer) getControllingPassenger());

//			EntityArrow slug = new EntityTippedArrow(world, (EntityPlayer) getControllingPassenger());
//
//			slug.setPosition(pos.getX() + 0.5, pos.getY() + 2.5, pos.getZ() + 0.5);

//			p2 = p2.addVector(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5);

			slug.shoot(p2.x, p2.y, p2.z, velocity, 0.0F);

			world.spawnEntity(slug);

			// TODO FIXME THIS IS BAD, FORGE DOCS SAYS EXPLICITLY "SERIOUSLY: DO NOT MODIFY
			// THE RETURNED ITEMSTACK."
			ammo.shrink(1);
		}
	}

	public int getShootEnergy() {
		return 1000;
	}

	@Override
	protected boolean canFitPassenger(Entity passenger) {
		return this.getPassengers().size() < 1;
	}

	@Override
	@Nullable
	public Entity getControllingPassenger() {
		List<Entity> list = this.getPassengers();
		return list.isEmpty() ? null : (Entity) list.get(0);
	}

	// Forge: Fix MC-119811 by instantly completing lerp on board
	@Override
	protected void addPassenger(Entity passenger) {
		super.addPassenger(passenger);
		this.shootCooldown = 10;
		if (this.canPassengerSteer() && this.lerpSteps > 0) {
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
			return (T) energy;
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T) inventory;
		return super.getCapability(capability, facing);
	}

	@Override
	public ModEnergyStorage getEnergy() {
		return energy;
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
