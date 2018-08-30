package cadiboo.wiptech.entity.projectile;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityNapalm extends EntityThrowable {

	public EntityNapalm(World worldIn) {
		super(worldIn);
	}

	public EntityNapalm(World worldIn, EntityLivingBase throwerIn) {
		super(worldIn, throwerIn);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		this.igniteBlocks();

		if (this.ticksExisted <= 2)
			return;

		for (int i = 0; i < Math.min(50, ticksExisted); i++) {
			double randX = new Random().nextGaussian() * 0.0025 * ticksExisted;
			double randY = new Random().nextDouble() * 0.025 * Math.min(25, ticksExisted);
			double randZ = new Random().nextGaussian() * 0.0025 * ticksExisted;

//			randX = randY = randZ = 0;

			world.spawnParticle(EnumParticleTypes.FLAME, true, posX, posY, posZ, randX, randY, randZ);
		}

//		if (this.ticksExisted > 2)
//			for (int i = 0; i < this.ticksExisted; i++)
//				world.spawnParticle(EnumParticleTypes.FLAME, posX, posY, posZ, (new Random().nextInt(3) - 1) * 0.025,
//						(new Random().nextInt(2)) * 0.2, (new Random().nextInt(3) - 1) * 0.025);
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (result == null)
			return;

		if (this.world.isRemote)
			return;

		if (result.entityHit != null)
			if ((result.entityHit != getThrower()) || (this.ticksExisted > 5))
				result.entityHit.setFire(100);

		this.igniteBlocks();
//		world.newExplosion(this, this.posX, posY, posZ, 10, false, false);
		this.setDead();
	}

	protected void igniteBlocks() {
		if (this.world.isRemote)
			return;
		BlockPos pos = new BlockPos(this.posX, this.posY, this.posZ);
		if (world.getBlockState(pos) != Blocks.AIR.getDefaultState() && world.getBlockState(pos) != Blocks.SNOW_LAYER.getDefaultState())
			return;

		int radius = Math.min(3, Math.round(ticksExisted / 50f));
		for (int x = -(radius); x <= radius; x++) {
			for (int y = -(radius); y <= radius; y++) {
				for (int z = -(radius); z <= radius; z++) {
					pos = new BlockPos(this.posX + x, this.posY + y, this.posZ + z);
					if (world.getBlockState(pos) == Blocks.AIR.getDefaultState()) {
						world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 11);
					} else if (world.getBlockState(pos) == Blocks.SNOW_LAYER.getDefaultState()) {
						world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
					}
				}
			}
		}

	}

}
