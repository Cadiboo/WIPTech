package cadiboo.wiptech.entity.projectile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityNapalm
extends EntityThrowable
{
	public EntityLivingBase shootingEntity;

	public EntityNapalm(World worldIn)
	{
		super(worldIn);
	}

	public EntityNapalm(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
	}

	public EntityNapalm(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}

	public void onUpdate()
	{
		EntityLivingBase entitylivingbase = getThrower();
		if ((entitylivingbase != null) && ((entitylivingbase instanceof EntityPlayer)) && (!entitylivingbase.isEntityAlive())) {
			setDead();
		} else {
			super.onUpdate();
		}
	}

	protected void onImpact(RayTraceResult result)
	{
		if (result != null) {
			if (!getEntityWorld().isRemote)
			{
				if (result.entityHit != null)
				{
					if ((result.entityHit != getThrower()) || (this.ticksExisted > 5)) {
						result.entityHit.setFire(100);
					}
				}
				else
				{
					World world = getEntityWorld();
					BlockPos pos = new BlockPos(this.posX, this.posY, this.posZ);
					IBlockState blockstate = world.getBlockState(pos);

					IBlockState BSADS = Blocks.AIR.getDefaultState();
					IBlockState FSADS = Blocks.FIRE.getDefaultState();
					if ((blockstate == BSADS) && ((world.getBlockState(pos.up()).getBlock().isFlammable(world, pos.up(), EnumFacing.DOWN)) || (world.getBlockState(pos.north()).getBlock().isFlammable(world, pos.north(), EnumFacing.NORTH)) || (world.getBlockState(pos.east()).getBlock().isFlammable(world, pos.east(), EnumFacing.EAST)) || (world.getBlockState(pos.south()).getBlock().isFlammable(world, pos.south(), EnumFacing.SOUTH)) || (world.getBlockState(pos.west()).getBlock().isFlammable(world, pos.west(), EnumFacing.WEST)))) {
						world.setBlockState(pos, FSADS, 3);
					}
					if ((blockstate == BSADS) && (world.getBlockState(pos.down()) != BSADS)) {
						world.setBlockState(pos, FSADS, 3);
					}
				}
				this.setDead();
			}
		}
	}
}