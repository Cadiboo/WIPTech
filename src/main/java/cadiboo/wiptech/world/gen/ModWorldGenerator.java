package cadiboo.wiptech.world.gen;

import java.util.Random;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

public class ModWorldGenerator implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		switch (world.provider.getDimensionType()) {
		case NETHER:
			break;
		case OVERWORLD:
			generateOverworld(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
			break;
		case THE_END:
			break;
		default:
			break;

		}

	}

	private void generateOverworld(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		for (ModMaterials material : ModMaterials.values()) {
			if (material.getProperties().hasOre()) {
				Block ore = material.getOre();
				if (ore != null) {
					generateOre(ore.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, getMinY(material), getMaxY(material), getSize(material), getChance(material));
				}
			}
		}
	}

	private int getMinY(ModMaterials material) {
		return 4;
	}

	private int getMaxY(ModMaterials material) {
		return Math.max(1 + getMinY(material), Math.round(

				Math.round(128 * ModUtil.map(0,

						ModMaterials.getHighestHardness(), 0, 1,

						maxHardnessMinusMaterialHardness(material)

								* ModUtil.map(0,

										ModMaterials.getHighestConductivity(),

										0, 1,

										material.getProperties().getConductivity()

								)

				)

				)

		)

		);
	}

	private int getChance(ModMaterials material) {
		int chance = Math.round(Math.round(ModUtil.map(0, ModMaterials.getHighestHardness(), 0, 10, maxHardnessMinusMaterialHardness(material))));
		if (chance <= 1)
			chance = 1;
		chance += 3;
		return chance;
	}

	private int getSize(ModMaterials material) {
		int size = Math.round(Math.round(ModUtil.map(0, ModMaterials.getHighestHardness(), 0, 10, maxHardnessMinusMaterialHardness(material))));
		if (size <= 2)
			size = 2;
		return size;
	}

	private int maxHardnessMinusMaterialHardness(ModMaterials material) {
		float highest = ModMaterials.getHighestHardness();
		float hardness = material.getProperties().getHardness();

		if (hardness == highest)
			return 1;

		return Math.round(ModMaterials.getHighestHardness()) - Math.round(material.getProperties().getHardness());
	}

	private void generateOre(IBlockState ore, World world, Random random, int x, int z, int minY, int maxY, int size, int chances) {
		int deltaY = maxY - minY;
		for (int i = 0; i < chances; i++) {
			BlockPos pos = new BlockPos(x + random.nextInt(16), minY + random.nextInt(deltaY), z + random.nextInt(16));
			WorldGenMinable generator = new WorldGenMinable(ore, size);
			generator.generate(world, random, pos);
		}
	}
}
