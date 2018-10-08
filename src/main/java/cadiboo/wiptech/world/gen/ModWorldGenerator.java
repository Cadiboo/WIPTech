package cadiboo.wiptech.world.gen;

import java.util.Random;

import cadiboo.wiptech.util.ModEnums.ModMaterial;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

/**
 * Basic world generator that generates ores
 * @author Cadiboo
 */
public class ModWorldGenerator implements IWorldGenerator {

	@Override
	public void generate(final Random random, final int chunkX, final int chunkZ, final World world, final IChunkGenerator chunkGenerator, final IChunkProvider chunkProvider) {
		switch (world.provider.getDimensionType()) {
			case NETHER :
				break;
			case OVERWORLD :
				this.generateOverworld(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
				break;
			case THE_END :
				break;
			default :
				break;

		}

	}

	private void generateOverworld(final Random random, final int chunkX, final int chunkZ, final World world, final IChunkGenerator chunkGenerator, final IChunkProvider chunkProvider) {
		for (final ModMaterial material : ModMaterial.values()) {
			if (material.getProperties().hasOre()) {
				final Block ore = material.getOre();
				if (ore != null) {
					this.generateOre(ore.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, this.getMinY(material), this.getMaxY(material), this.getSize(material), this.getChance(material));
				}
			}
		}
	}

	private int getMinY(final ModMaterial material) {
		return 5;
	}

	private int getMaxY(final ModMaterial material) {
		return Math.max(1 + this.getMinY(material), Math.round(

				Math.round(128 * ModUtil.map(0,

						ModMaterial.getHighestHardness(), 0, 1,

						this.maxHardnessMinusMaterialHardness(material)

								* ModUtil.map(0,

										ModMaterial.getHighestConductivity(),

										0, 1,

										material.getProperties().getConductivity()

								)

				)

				)

		)

		);
	}

	private int getChance(final ModMaterial material) {
		final int chance = Math.round(Math.round(ModUtil.map(0, ModMaterial.getHighestHardness(), 1, 5, this.maxHardnessMinusMaterialHardness(material))));
		return chance;
	}

	private int getSize(final ModMaterial material) {
		final int size = Math.round(Math.round(ModUtil.map(0, ModMaterial.getHighestHardness(), 3, 8, this.maxHardnessMinusMaterialHardness(material))));
		return size;
	}

	private int maxHardnessMinusMaterialHardness(final ModMaterial material) {
		final float highest = ModMaterial.getHighestHardness();
		final float hardness = material.getProperties().getHardness();

		if (hardness == highest) {
			return 1;
		}

		return Math.round(ModMaterial.getHighestHardness()) - Math.round(material.getProperties().getHardness());
	}

	private void generateOre(final IBlockState ore, final World world, final Random random, final int x, final int z, final int minY, final int maxY, final int size, final int chances) {
		final int deltaY = maxY - minY;
		for (int i = 0; i < chances; i++) {
			final BlockPos pos = new BlockPos(x + random.nextInt(16), minY + random.nextInt(deltaY), z + random.nextInt(16));
			final WorldGenMinable generator = new WorldGenMinable(ore, size);
			generator.generate(world, random, pos);
		}
	}
}
