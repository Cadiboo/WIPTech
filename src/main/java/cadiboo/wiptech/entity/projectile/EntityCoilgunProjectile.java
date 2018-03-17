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

public class EntityCoilgunProjectile extends EntityProjectileBase {

	private static final DataParameter<Integer> NUGGET_ID = EntityDataManager.<Integer>createKey(EntityCoilgunProjectile.class, DataSerializers.VARINT);

	protected void entityInit()
	{
		this.dataManager.register(NUGGET_ID, Integer.valueOf(-1));
	}
	
	public void setNuggetId(int nuggetId)
	{
		this.dataManager.set(NUGGET_ID, Integer.valueOf(nuggetId));
	}

	public int getNuggetId()
	{
		return ((Integer)this.dataManager.get(NUGGET_ID)).intValue();
	}

	public EntityCoilgunProjectile(World worldIn)
	{
		super(worldIn);
		this.xTile = -1;
		this.yTile = -1;
		this.zTile = -1;
		this.pickupStatus = EntityProjectileBase.PickupStatus.DISALLOWED;
		this.damage = 2.0D;
		this.setSize(0.5F, 0.5F);
	}

	public EntityCoilgunProjectile(World worldIn, double x, double y, double z)
	{
		this(worldIn);
		this.setPosition(x, y, z);
	}

	public EntityCoilgunProjectile(World worldIn, EntityLivingBase shooter)
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

			if (this.shootingEntity == null)
			{
				damagesource = cadiboo.wiptech.util.DamageSource.causeCoilgunProjectileDamage(this, this);
			}
			else
			{
				damagesource = cadiboo.wiptech.util.DamageSource.causeCoilgunProjectileDamage(this, this.shootingEntity);
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
				}

				//the sound it makes as it makes a hole straight through an entity, not slowing at all
				this.playSound(SoundEvents.ENTITY_SLIME_SQUISH, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));

				/*if (!(entity instanceof EntityEnderman))
				{
					this.setDead();
				}*/
				//MUAHAHAHA it can kill a line of entities at once
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
						this.entityDropItem(this.getStack(), 0.1F);
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
		compound.setInteger("nuggetId", this.getNuggetId());
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
		this.setNuggetId(compound.getInteger("nuggetId"));

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

	@Override
	protected ItemStack getStack()
	{
		ItemStack stack;
		switch(this.getNuggetId())
		{
		case 0: stack = new ItemStack(net.minecraft.item.Item.REGISTRY.getObject(new ResourceLocation("minecraft", "gold_nugget"))); break;
		case 1: stack = new ItemStack(Items.OSMIUM_NUGGET);
		default:stack = new ItemStack(Items.TUNGSTEN_NUGGET);
		}
		
		return stack;
	}

}
