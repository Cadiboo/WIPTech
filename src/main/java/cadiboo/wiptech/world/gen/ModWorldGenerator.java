package cadiboo.wiptech.world.gen;

import java.util.Random;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

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
				Block ore = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(material.getResouceLocationDomain("ore", ForgeRegistries.BLOCKS), material.getNameLowercase() + "_ore"));
				if (ore != null)
					generateOre(ore.getDefaultState(), world, random, chunkX * 16, chunkZ * 16, 16, 64, 4 + random.nextInt(4), 6);
			}

		}
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
