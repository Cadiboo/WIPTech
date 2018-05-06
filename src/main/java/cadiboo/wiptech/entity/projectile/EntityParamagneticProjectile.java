package cadiboo.wiptech.entity.projectile;

import cadiboo.wiptech.init.Items;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityParamagneticProjectile extends EntityProjectileBase {

	private static final DataParameter<Integer>	AMMO_ID		= EntityDataManager.<Integer>createKey(EntityParamagneticProjectile.class, DataSerializers.VARINT);
	private static final DataParameter<Float>	TEMPERATURE	= EntityDataManager.<Float>createKey(EntityParamagneticProjectile.class, DataSerializers.FLOAT);

	public static final int	overheatFireTime		= 5;
	public static final int	overheatTemperature		= 50;
	public static final int	meltTemperature			= 100;
	public static final int	freezeTemperature		= -25;
	public static final int	ingiteTemperature		= 500;
	public static final int	vaporiseTemperature		= 5000;
	public static final int	plasmaKillTemperature	= 350;
	public static final int	plasmaAirLifespan		= 120;

	@Override
	public int getAirLifespan() {
		if (this.isPlasma())
			return plasmaAirLifespan;
		else
			return super.getAirLifespan();
	}

	@Override
	public int getGroundLifespan() {
		if (this.isPlasma())
			return 0;
		else
			return super.getGroundLifespan();
	}

	@Override
	public void onUpdate() {
		if (this.isPlasma()) {
			this.setTemperature(this.getTemperature() - 1F);
			// WIPTech.logger.info("Temperature: "+this.getTemperature());

			if (this.isInWater()) {
				this.setTemperature(this.getTemperature() / (float) 1.25);
				if (this.getTemperature() < plasmaKillTemperature)
					this.setDead();
			}
		}

		super.onUpdate();
	}

	public float getTemperature() {
		return this.dataManager.get(TEMPERATURE).floatValue();
	}

	public void setTemperature(float temperature) {
		this.dataManager.set(TEMPERATURE, Float.valueOf(temperature));
	}

	@Override
	public void updateGravity() {
		if (this.isPlasma())
			this.motionY -= 0.00500000074505806D;
		// 0.05000000074505806D;
		else
			super.updateGravity();
	}

	@Override
	public void entityInit() {
		super.entityInit();
		this.dataManager.register(AMMO_ID, Integer.valueOf(-1));
		this.dataManager.register(TEMPERATURE, Float.valueOf(25F));
	}

	public void setAmmoId(int ammoId) {
		this.dataManager.set(AMMO_ID, Integer.valueOf(ammoId));
	}

	public int getAmmoId() {
		return this.dataManager.get(AMMO_ID).intValue();
	}

	public boolean isOverheated() {
		return this.getTemperature() >= overheatTemperature;
	}

	public boolean isFrozen() {
		return this.getTemperature() <= freezeTemperature;
	}

	public boolean canMelt() {
		return this.getTemperature() >= meltTemperature;
	}

	public boolean canFreeze() {
		return this.isFrozen();
	}

	public boolean canIgnite() {
		return this.getTemperature() >= ingiteTemperature;
	}

	public EntityParamagneticProjectile(World worldIn) {
		super(worldIn);
	}

	public EntityParamagneticProjectile(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
	}

	public EntityParamagneticProjectile(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	// Called when the projectile hits a block or an entity
	@Override
	public void onHit(RayTraceResult raytraceResultIn) {
		Entity entity = raytraceResultIn.entityHit;

		if (entity != null) {
			onHitEntity(entity);
		} else {
			onHitBlock(raytraceResultIn);
		}
	}

	@Override
	public void customUpdateinAir() {
		BlockPos blockpos = this.getPosition();
		IBlockState iblockstate = this.world.getBlockState(blockpos);

		if (!this.world.isRemote) {
			if (this.canIgnite()) {
				if ((iblockstate.getBlock() == Blocks.AIR) && ((world.getBlockState(blockpos.down()).getBlock().isFlammable(world, blockpos.down(), EnumFacing.UP))
						|| (world.getBlockState(blockpos.up()).getBlock().isFlammable(world, blockpos.up(), EnumFacing.DOWN))
						|| (world.getBlockState(blockpos.north()).getBlock().isFlammable(world, blockpos.north(), EnumFacing.SOUTH))
						|| (world.getBlockState(blockpos.east()).getBlock().isFlammable(world, blockpos.east(), EnumFacing.WEST))
						|| (world.getBlockState(blockpos.south()).getBlock().isFlammable(world, blockpos.south(), EnumFacing.NORTH))
						|| (world.getBlockState(blockpos.west()).getBlock().isFlammable(world, blockpos.west(), EnumFacing.EAST))))
					this.world.setBlockState(blockpos, Blocks.FIRE.getDefaultState(), 2);
				if (iblockstate.getBlock() == Blocks.WATER)
					this.world.setBlockToAir(blockpos);
			}
		}
		super.customUpdateinAir();
	}

	private void onHitBlock(RayTraceResult raytraceResultIn) {

		BlockPos blockpos = raytraceResultIn.getBlockPos();
		this.xTile = blockpos.getX();
		this.yTile = blockpos.getY();
		this.zTile = blockpos.getZ();
		IBlockState iblockstate = this.world.getBlockState(blockpos);

		if (!this.world.isRemote) {

			if (this.canIgnite()) {
				if (iblockstate.getBlock() instanceof BlockTNT) {
					// iblockstate.getBlock().onBlockDestroyedByPlayer(world, blockpos,
					// iblockstate.withProperty(BlockTNT.EXPLODE, true));
					this.world.setBlockToAir(blockpos);
					world.createExplosion(this, blockpos.getX(), blockpos.getY(), blockpos.getZ(), 8, true);
				}
			}
			if (this.canFreeze()) {
				if (iblockstate.getMaterial() == Material.ICE) {
					this.world.setBlockState(blockpos, Blocks.PACKED_ICE.getDefaultState());
				}
				if (iblockstate.getMaterial() == Material.WATER) {
					this.world.setBlockState(blockpos, Blocks.ICE.getDefaultState());
				}
			}
			if (this.canMelt()) {
				if (iblockstate.getMaterial() == Material.ICE || iblockstate.getMaterial() == Material.PACKED_ICE) {
					this.world.setBlockState(blockpos, Blocks.WATER.getDefaultState());
				}
				if (iblockstate.getMaterial() == Material.SNOW || iblockstate.getMaterial() == Material.CRAFTED_SNOW) {
					this.world.setBlockToAir(blockpos);
				}
				if (iblockstate.getMaterial() == Material.WATER) {
					this.world.setBlockToAir(blockpos);
				}
			}
		}

		if (this.isPlasma()) {
			this.setDead();
			return;
		}

		this.inTile = iblockstate.getBlock();
		this.inData = this.inTile.getMetaFromState(iblockstate);

		if (iblockstate.getMaterial() != Material.AIR) {
			this.inTile.onEntityCollidedWithBlock(this.world, blockpos, iblockstate, this);
		}

		this.motionX = ((float) (raytraceResultIn.hitVec.x - this.posX));
		this.motionY = ((float) (raytraceResultIn.hitVec.y - this.posY));
		this.motionZ = ((float) (raytraceResultIn.hitVec.z - this.posZ));
		float f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
		this.posX -= this.motionX / f2 * 0.05000000074505806D;
		this.posY -= this.motionY / f2 * 0.05000000074505806D;
		this.posZ -= this.motionZ / f2 * 0.05000000074505806D;
		this.playSound(SoundEvents.BLOCK_STONE_BREAK, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
		this.inGround = true;
		this.projectileShake = 5;
	}

	private void onHitEntity(Entity entity) {

		DamageSource damagesource;
		boolean shootingEntityNull = this.shootingEntity == null;

		switch (this.getAmmoType(this.getAmmoId())) {
			case 0: // Big rod - MountedRailgun
				damagesource = shootingEntityNull ? cadiboo.wiptech.util.DamageSource.causeRailgunProjectileDamage(this, this)
						: cadiboo.wiptech.util.DamageSource.causeRailgunProjectileDamage(this, this.shootingEntity);
				break;
			default:
			case 1: // Medium rod - Handheld Railgun
				damagesource = shootingEntityNull ? cadiboo.wiptech.util.DamageSource.causeRailgunProjectileDamage(this, this)
						: cadiboo.wiptech.util.DamageSource.causeRailgunProjectileDamage(this, this.shootingEntity);
				break;
			case 2: // Small rod - Coilgun
				damagesource = shootingEntityNull ? cadiboo.wiptech.util.DamageSource.causeCoilgunProjectileDamage(this, this)
						: cadiboo.wiptech.util.DamageSource.causeCoilgunProjectileDamage(this, this.shootingEntity);
				break;
			case 3: // Nano rod - Plasma Cannon
				damagesource = shootingEntityNull ? cadiboo.wiptech.util.DamageSource.causePlasmaProjectileDamage(this, this)
						: cadiboo.wiptech.util.DamageSource.causePlasmaProjectileDamage(this, this.shootingEntity);
				break;
		}

		if (this.isOverheated())
			entity.setFire(5);

		if (this.isPlasma())
			entity.setFire(2);

		if (entity.attackEntityFrom(damagesource, (float) this.getDamage())) {
			if (entity instanceof EntityLivingBase) {
				EntityLivingBase entitylivingbase = (EntityLivingBase) entity;

				if (this.knockbackStrength > 0) {
					float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

					if (f1 > 0.0F) {
						entitylivingbase.addVelocity(this.motionX * this.knockbackStrength * 0.6000000238418579D / f1, 0.1D, this.motionZ * this.knockbackStrength * 0.6000000238418579D / f1);
					}
				}

				if (this.shootingEntity instanceof EntityLivingBase) {
					EnchantmentHelper.applyThornEnchantments(entitylivingbase, this.shootingEntity);
					EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase) this.shootingEntity, entitylivingbase);
				}

				/*
				 * this.arrowHit(entitylivingbase);
				 * 
				 * if (this.shootingEntity != null && entitylivingbase != this.shootingEntity &&
				 * entitylivingbase instanceof EntityPlayer && this.shootingEntity instanceof
				 * EntityPlayerMP) {
				 * ((EntityPlayerMP)this.shootingEntity).connection.sendPacket(new
				 * SPacketChangeGameState(6, 0.0F)); }
				 */
				// TODO make this do whatever its meant to do
				// TODO might have something to do with increasing arrow count in player?
			}

			switch (getAmmoType(getAmmoId())) {
				default:
				case 0:
				case 1:
					this.playSound(SoundEvents.ENTITY_SLIME_SQUISH, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
					break;
				case 2:
					this.playSound(SoundEvents.ENTITY_SHULKER_BULLET_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
					break;
				case 3:
					this.playSound(SoundEvents.ENTITY_PLAYER_HURT_ON_FIRE, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
					break;
			}

			if (this.isPlasma()) {
				this.setDead();
			}
		} else {
			/*
			 * this.motionX *= -0.10000000149011612D; this.motionY *= -0.10000000149011612D;
			 * this.motionZ *= -0.10000000149011612D; this.rotationYaw += 180.0F;
			 * this.prevRotationYaw += 180.0F; this.ticksInAir = 0;
			 */

			// WHY DO THIS EVER????
			// TODO Maybe remove this completely

			if (!this.world.isRemote && this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ < 0.0010000000474974513D) {
				if (this.pickupStatus == EntityProjectileBase.PickupStatus.ALLOWED) {
					this.entityDropItem(this.getAmmoStack(), 0.1F);
				}

				this.setDead();
			}
		}
	}

	public boolean isPlasma() {
		return this.getAmmoId() == 9;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("ammoId", this.getAmmoId());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setAmmoId(compound.getInteger("ammoId"));
	}

	// Called by a player entity when they collide with an entity
	@Override
	public void onCollideWithPlayer(EntityPlayer entityIn) {
		super.onCollideWithPlayer(entityIn);
	}

	@Override
	public ItemStack getAmmoStack() {
		return getAmmoStack(this.getAmmoId() > 9 ? 0 : this.getAmmoId());
	}

	public ItemStack getAmmoStack(int ammoId) {
		if (this.isPlasma()) {
			return ItemStack.EMPTY;
		}
		// itemIn, amount, meta
		return new ItemStack(Items.PARAMAGNETIC_PROJECILE, 1, ammoId);
	}

	public float getStuckDepth() {
		return getStuckDepth(this.getAmmoId());
	}

	public float getStuckDepth(int ammoId) {
		return getAmmoType(ammoId) < 2 ? 0.4F : 0;
	}

	public static int getAmmoType(int ammoId) {
		// WIPTech.logger.info("getAmmoLevel for id "+ammoId+":
		// "+Math.floor((ammoId+1)/3));
		return (int) Math.floor((ammoId) / 3);
		// TODO CHECK THIS
	}

	public static int getAmmoLevel(int ammoId) {
		// WIPTech.logger.info("getAmmoType for id "+ammoId+": "+ammoId%3);
		return ammoId % 3;
		// TODO CHECK THIS
	}

	public static float getProjectileVelocity(ItemStack stack) {
		float velocity = 0;
		int metaData = stack.getMetadata();
		switch (getAmmoType(metaData)) {
			case 0: // mounted Railgun
				switch (getAmmoLevel(metaData)) {

					// TODO make these higher
					default:
					case 0: // iron
						velocity = 3.5F;
						break;
					case 1: // osmium
						velocity = 3F;
						break;
					case 2: // tungsten
						velocity = 3.25F;
						break;
				}

				break;
			default:
			case 1: // handheld Railgun
				switch (getAmmoLevel(metaData)) {

					default:
					case 0: // iron
						velocity = 3.5F;
						break;
					case 1: // osmium
						velocity = 3F;
						break;
					case 2: // tungsten
						velocity = 3.25F;
						break;
				}

				break;
			case 2: // Coilgun / gausscannon
				switch (getAmmoLevel(metaData)) {

					// TODO change these

					default:
					case 0: // iron
						velocity = 3.5F;
						break;
					case 1: // osmium
						velocity = 3F;
						break;
					case 2: // tungsten
						velocity = 3.25F;
						break;
				}

				break;
			case 3: // plasma
				velocity = 7.5F; // some fucking crazy amount because of how light it is //yenah 20 is way over
									// the top

		}
		// WIPTech.logger.info("Velocity for meta '"+metaData+"' with Type
		// '"+getAmmoType(metaData)+"' and Level '"+getAmmoLevel(metaData)+"' =
		// "+velocity);
		return velocity;
	}

	public static float getProjectileDamage(ItemStack stack) {
		float damage = 0;
		int metaData = stack.getMetadata();
		switch (getAmmoType(metaData)) {
			case 0: // mounted Railgun
				switch (getAmmoLevel(metaData)) {

					default:
					case 0: // iron
						damage = 20F;
						break;
					case 1: // osmium
						damage = 32F;
						break;
					case 2: // tungsten
						damage = 40F;
						break;
				}

				break;
			default:
			case 1: // handheld Railgun
				switch (getAmmoLevel(metaData)) {

					default:
					case 0: // iron
						damage = 7.5F;
						break;
					case 1: // osmium
						damage = 12F;
						break;
					case 2: // tungsten
						damage = 15F;
						break;
				}

				break;
			case 2: // Coilgun / gausscannon
				switch (getAmmoLevel(metaData)) {

					default:
					case 0: // iron
						damage = 2F;
						break;
					case 1: // osmium
						damage = 3.2F;
						break;
					case 2: // tungsten
						damage = 4F;
						break;
				}

				break;
			case 3: // plasma
				damage = 0.5F;

		}
		// WIPTech.logger.info("Damage for meta '"+metaData+"' with Type
		// '"+getAmmoType(metaData)+"' and Level '"+getAmmoLevel(metaData)+"' =
		// "+damage);
		return damage;
	}

	public static float getProjectileKnockback(ItemStack stack) {
		float knockback = 0;
		int metaData = stack.getMetadata();
		switch (getAmmoType(metaData)) {
			case 0: // mounted Railgun
				switch (getAmmoLevel(metaData)) {

					default:
					case 0: // iron
						knockback = 2F;
						break;
					case 1: // osmium
						knockback = 3F;
						break;
					case 2: // tungsten
						knockback = 5F;
						break;
				}

				break;
			default:
			case 1: // handheld Railgun
				switch (getAmmoLevel(metaData)) {

					default:
					case 0: // iron
						knockback = 1F;
						break;
					case 1: // osmium
						knockback = 1.5F;
						break;
					case 2: // tungsten
						knockback = 2.5F;
						break;
				}

				break;
			case 2: // Coilgun / gausscannon
				switch (getAmmoLevel(metaData)) {

					// TODO change these

					default:
					case 0: // iron
						knockback = 0.1F;
						break;
					case 1: // osmium
						knockback = 0.15F;
						break;
					case 2: // tungsten
						knockback = 0.25F;
						break;
				}

				break;
			case 3: // plasma
				knockback = 0F;

		}
		// WIPTech.logger.info("Knockback for meta '"+metaData+"' with Type
		// '"+getAmmoType(metaData)+"' and Level '"+getAmmoLevel(metaData)+"' =
		// "+knockback);
		return knockback;
	}

}
