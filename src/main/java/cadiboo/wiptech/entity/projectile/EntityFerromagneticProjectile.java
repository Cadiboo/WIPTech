package cadiboo.wiptech.entity.projectile;

import cadiboo.wiptech.init.Items;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityFerromagneticProjectile extends EntityProjectileBase {

	private static final DataParameter<Integer> AMMO_ID = EntityDataManager.<Integer>createKey(EntityFerromagneticProjectile.class, DataSerializers.VARINT);

	protected void entityInit()
	{
		this.dataManager.register(AMMO_ID, Integer.valueOf(-1));
	}

	public void setAmmoId(int rodId)
	{
		this.dataManager.set(AMMO_ID, Integer.valueOf(rodId));
	}

	public int getAmmoId()
	{
		return ((Integer)this.dataManager.get(AMMO_ID)).intValue();
	}

	public EntityFerromagneticProjectile(World worldIn)
	{
		super(worldIn);
		this.xTile = -1;
		this.yTile = -1;
		this.zTile = -1;
		this.pickupStatus = EntityProjectileBase.PickupStatus.DISALLOWED;
		this.damage = 2.0D;
		this.setSize(0.5F, 0.5F);
	}

	public EntityFerromagneticProjectile(World worldIn, double x, double y, double z)
	{
		this(worldIn);
		this.setPosition(x, y, z);
	}

	public EntityFerromagneticProjectile(World worldIn, EntityLivingBase shooter)
	{
		this(worldIn, shooter.posX, shooter.posY + (double)shooter.getEyeHeight() - 0.10000000149011612D, shooter.posZ);
		this.shootingEntity = shooter;

		if (shooter instanceof EntityPlayer)
		{
			this.pickupStatus = EntityProjectileBase.PickupStatus.ALLOWED;
		}
	}

	/**
	 * Called when the arrow hits a block or an entity
	 */
	protected void onHit(RayTraceResult raytraceResultIn)
	{
		Entity entity = raytraceResultIn.entityHit;

		if (entity != null)
		{
			float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
			int i = MathHelper.ceil((double)f * this.damage);

			DamageSource damagesource;

			boolean shootingEntityNull = this.shootingEntity == null;

			switch(this.getAmmoType(this.getAmmoId()))
			{
			case 0: //Big rod - MountedRailgun
				damagesource = shootingEntityNull? cadiboo.wiptech.util.DamageSource.causeRailgunProjectileDamage(this, this): cadiboo.wiptech.util.DamageSource.causeRailgunProjectileDamage(this, this.shootingEntity);
				break;
			default: case 1: //Medium rod - Handheld Railgun
				damagesource = shootingEntityNull? cadiboo.wiptech.util.DamageSource.causeRailgunProjectileDamage(this, this): cadiboo.wiptech.util.DamageSource.causeRailgunProjectileDamage(this, this.shootingEntity);
				break;
			case 2: //Small rod - Coilgun
				damagesource = shootingEntityNull? cadiboo.wiptech.util.DamageSource.causeCoilgunProjectileDamage(this, this): cadiboo.wiptech.util.DamageSource.causeCoilgunProjectileDamage(this, this.shootingEntity);
				break;
			case 3: //Nano rod - Plasma Cannon
				damagesource = shootingEntityNull? cadiboo.wiptech.util.DamageSource.causePlasmaProjectileDamage(this, this): cadiboo.wiptech.util.DamageSource.causePlasmaProjectileDamage(this, this.shootingEntity);
				break;
			}

			if (this.isBurning())
			{
				entity.setFire(5);
			}

			if (entity.attackEntityFrom(damagesource, (float)i))
			{
				if (entity instanceof EntityLivingBase)
				{
					EntityLivingBase entitylivingbase = (EntityLivingBase)entity;

					if (this.knockbackStrength > 0)
					{
						float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

						if (f1 > 0.0F)
						{
							entitylivingbase.addVelocity(this.motionX * (double)this.knockbackStrength * 0.6000000238418579D / (double)f1, 0.1D, this.motionZ * (double)this.knockbackStrength * 0.6000000238418579D / (double)f1);
						}
					}

					if (this.shootingEntity instanceof EntityLivingBase)
					{
						EnchantmentHelper.applyThornEnchantments(entitylivingbase, this.shootingEntity);
						EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase)this.shootingEntity, entitylivingbase);
					}

					/*this.arrowHit(entitylivingbase);

					if (this.shootingEntity != null && entitylivingbase != this.shootingEntity && entitylivingbase instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP)
					{
						((EntityPlayerMP)this.shootingEntity).connection.sendPacket(new SPacketChangeGameState(6, 0.0F));
					}
					 */
					//TODO make this do whatever its meant to do
					//TODO might have something to do with increasing arrow count in player?
				}

				switch(getAmmoType(getAmmoId())) {
				default: case 0: case 1:
					this.playSound(SoundEvents.ENTITY_SLIME_SQUISH, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
					break;
				case 2:
					this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
					break;
				case 3:
					this.playSound(SoundEvents.ENTITY_PLAYER_HURT_ON_FIRE, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
					break;
				}

				if(getAmmoType(getAmmoId())>2)
				{
					this.setDead();
				}
			}
			else
			{
				this.motionX *= -0.10000000149011612D;
				this.motionY *= -0.10000000149011612D;
				this.motionZ *= -0.10000000149011612D;
				this.rotationYaw += 180.0F;
				this.prevRotationYaw += 180.0F;
				this.ticksInAir = 0;

				if (!this.world.isRemote && this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ < 0.0010000000474974513D)
				{
					if (this.pickupStatus == EntityProjectileBase.PickupStatus.ALLOWED)
					{
						this.entityDropItem(this.getAmmoStack(), 0.1F);
					}

					this.setDead();
				}
			}
		}
		else
		{
			BlockPos blockpos = raytraceResultIn.getBlockPos();
			this.xTile = blockpos.getX();
			this.yTile = blockpos.getY();
			this.zTile = blockpos.getZ();
			IBlockState iblockstate = this.world.getBlockState(blockpos);
			this.inTile = iblockstate.getBlock();
			this.inData = this.inTile.getMetaFromState(iblockstate);
			this.motionX = (double)((float)(raytraceResultIn.hitVec.x - this.posX));
			this.motionY = (double)((float)(raytraceResultIn.hitVec.y - this.posY));
			this.motionZ = (double)((float)(raytraceResultIn.hitVec.z - this.posZ));
			float f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
			this.posX -= this.motionX / (double)f2 * 0.05000000074505806D;
			this.posY -= this.motionY / (double)f2 * 0.05000000074505806D;
			this.posZ -= this.motionZ / (double)f2 * 0.05000000074505806D;
			this.playSound(SoundEvents.BLOCK_STONE_BREAK, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
			this.inGround = true;

			if (iblockstate.getMaterial() != Material.AIR)
			{
				this.inTile.onEntityCollidedWithBlock(this.world, blockpos, iblockstate, this);
			}
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setInteger("xTile", this.xTile);
		compound.setInteger("yTile", this.yTile);
		compound.setInteger("zTile", this.zTile);
		compound.setShort("life", (short)this.ticksInGround);
		ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(this.inTile);
		compound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
		compound.setByte("inData", (byte)this.inData);
		compound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
		compound.setByte("pickup", (byte)this.pickupStatus.ordinal());
		compound.setDouble("damage", this.damage);
		compound.setInteger("rodId", this.getAmmoId());
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		this.xTile = compound.getInteger("xTile");
		this.yTile = compound.getInteger("yTile");
		this.zTile = compound.getInteger("zTile");
		this.ticksInGround = compound.getShort("life");
		this.setAmmoId(compound.getInteger("rodId"));

		if (compound.hasKey("inTile", 8))
		{
			this.inTile = Block.getBlockFromName(compound.getString("inTile"));
		}
		else
		{
			this.inTile = Block.getBlockById(compound.getByte("inTile") & 255);
		}

		this.inData = compound.getByte("inData") & 255;
		this.inGround = compound.getByte("inGround") == 1;

		if (compound.hasKey("damage", 99))
		{
			this.damage = compound.getDouble("damage");
		}

		if (compound.hasKey("pickup", 99))
		{
			this.pickupStatus = EntityProjectileBase.PickupStatus.getByOrdinal(compound.getByte("pickup"));
		}
		else if (compound.hasKey("player", 99))
		{
			this.pickupStatus = compound.getBoolean("player") ? EntityProjectileBase.PickupStatus.ALLOWED : EntityProjectileBase.PickupStatus.DISALLOWED;
		}

	}

	/**
	 * Called by a player entity when they collide with an entity
	 */
	public void onCollideWithPlayer(EntityPlayer entityIn)
	{
		if (!this.world.isRemote && this.inGround)
		{
			boolean flag = this.pickupStatus == EntityProjectileBase.PickupStatus.ALLOWED || this.pickupStatus == EntityProjectileBase.PickupStatus.CREATIVE_ONLY && entityIn.capabilities.isCreativeMode;

			if (this.pickupStatus == EntityProjectileBase.PickupStatus.ALLOWED && !entityIn.inventory.addItemStackToInventory(this.getAmmoStack()))
			{
				flag = false;
			}

			if (flag)
			{
				entityIn.onItemPickup(this, 1);
				this.setDead();
			}
		}
	}

	protected ItemStack getAmmoStack()
	{
		//itemIn, amount, meta
		return new ItemStack(Items.FERROMAGNETIC_PROJECILE, 1, this.getAmmoId());
	}

	public static int getAmmoType(int ammoId)
	{
		return (int) Math.floor((ammoId+1)/3);
		//TODO CHECK THIS
	}

	public static int getAmmoTier(int ammoId)
	{
		return ammoId%3;
		//TODO CHECK THIS
	}

	public static float getProjectileVelocity(ItemStack stack) {
		float velocity = 0;
		int metaData = stack.getMetadata();
		switch(getAmmoType(metaData)) {
		case 0: //mounted Railgun
			switch(getAmmoTier(metaData)) {

			//TODO make these higher
			default: case 0: //iron
				velocity = 3.5F;break;
			case 1: //osmium
				velocity = 3F; break;
			case 2: //tungsten
				velocity = 3.25F; break;
			}

			break;
		default: case 1: //handheld Railgun
			switch(getAmmoTier(metaData)) {

			default: case 0: //iron
				velocity = 3.5F;break;
			case 1: //osmium
				velocity = 3F; break;
			case 2: //tungsten
				velocity = 3.25F; break;
			}

			break;
		case 2: //Coilgun / gausscannon
			switch(getAmmoTier(metaData)) {

			//TODO change these

			default: case 0: //iron
				velocity = 3.5F;break;
			case 1: //osmium
				velocity = 3F; break;
			case 2: //tungsten
				velocity = 3.25F; break;
			}

			break;
		case 3: //plasma
			velocity = 20; //some fucking crazy amount because of how light it is

		}
		return velocity;
	}


	public static float getProjectileDamage(ItemStack stack) {
		float damage = 0;
		int metaData = stack.getMetadata();
		switch(getAmmoType(metaData)) {
		case 0: //mounted Railgun
			switch(getAmmoTier(metaData)) {

			//TODO make these higher
			default: case 0: //iron
				damage = 7.5F;break;
			case 1: //osmium
				damage = 12F; break;
			case 2: //tungsten
				damage = 15F; break;
			}

			break;
		default: case 1: //handheld Railgun
			switch(getAmmoTier(metaData)) {

			default: case 0: //iron
				damage = 5F;break;
			case 1: //osmium
				damage = 8F; break;
			case 2: //tungsten
				damage = 10F; break;
			}

			break;
		case 2: //Coilgun / gausscannon
			switch(getAmmoTier(metaData)) {

			//TODO change these

			default: case 0: //iron
				damage = 1F;break;
			case 1: //osmium
				damage = 1.75F; break;
			case 2: //tungsten
				damage = 2F; break;
			}

			break;
		case 3: //plasma
			damage = 0.5F;

		}
		return damage;
	}

	public static float getProjectileKnockback(ItemStack stack) {
		float knockback = 0;
		int metaData = stack.getMetadata();
		switch(getAmmoType(metaData)) {
		case 0: //mounted Railgun
			switch(getAmmoTier(metaData)) {

			default: case 0: //iron
				knockback = 2F;break;
			case 1: //osmium
				knockback = 3F; break;
			case 2: //tungsten
				knockback = 5F; break;
			}

			break;
		default: case 1: //handheld Railgun
			switch(getAmmoTier(metaData)) {

			default: case 0: //iron
				knockback = 1F;break;
			case 1: //osmium
				knockback = 1.5F; break;
			case 2: //tungsten
				knockback = 2.5F; break;
			}

			break;
		case 2: //Coilgun / gausscannon
			switch(getAmmoTier(metaData)) {

			//TODO change these

			default: case 0: //iron
				knockback = 0.1F;break;
			case 1: //osmium
				knockback = 0.15F; break;
			case 2: //tungsten
				knockback = 0.25F; break;
			}

			break;
		case 3: //plasma
			knockback = 0F;

		}
		return knockback;
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
	 * prevent them from trampling crops
	 */
	protected boolean canTriggerWalking()
	{
		return false;
	}

	public void setDamage(double damageIn)
	{
		this.damage = damageIn;
	}

	public double getDamage()
	{
		return this.damage;
	}

	/**
	 * Sets the amount of knockback the arrow applies when it hits a mob.
	 */
	public void setKnockbackStrength(float f)
	{
		this.knockbackStrength = f;
	}

	/**
	 * Returns true if it's possible to attack this entity with an item.
	 */
	public boolean canBeAttackedWithItem()
	{
		return false;
	}

	public static enum PickupStatus
	{
		DISALLOWED,
		ALLOWED,
		CREATIVE_ONLY;

		public static EntityFerromagneticProjectile.PickupStatus getByOrdinal(int ordinal)
		{
			if (ordinal < 0 || ordinal > values().length)
			{
				ordinal = 0;
			}

			return values()[ordinal];
		}
	}

}
