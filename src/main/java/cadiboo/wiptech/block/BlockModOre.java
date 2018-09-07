package cadiboo.wiptech.block;

import java.util.Random;

import cadiboo.wiptech.util.ModEnums.ModMaterials;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Custom ore block for materials
 * @author Cadiboo
 */
public class BlockModOre extends Block implements IBlockModMaterial {

	protected final ModMaterials material;

	public BlockModOre(final ModMaterials material) {
		super(Material.ROCK);
		ModUtil.setRegistryNames(this, material, "ore");
		this.material = material;
	}

	@Override
	public final ModMaterials getModMaterial() {
		return this.material;
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public int getLightValue(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
		return ModUtil.getMaterialLightValue(this.getModMaterial());
	}

	@Override
	public int getLightOpacity(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
		return ModUtil.getMaterialLightOpacity(this.getModMaterial());
	}

	@Override
	public boolean isFullCube(final IBlockState state) {
		return true;
	}

	@Override
	public boolean isOpaqueCube(final IBlockState state) {
		if (ModReference.Debug.debugOres()) {
			return false;
		} else if (this.getModMaterial() == ModMaterials.GLITCH) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
		switch (this.getModMaterial().getType()) {
		case GEM:
			return Item.getItemFromBlock(this.getModMaterial().getResource());
		case METAL:
		default:
			return Item.getItemFromBlock(this);
		}
	}

	@Override
	public int quantityDropped(final Random random) {
		switch (this.getModMaterial().getType()) {
		case GEM:
			return 1;
		case METAL:
		default:
			return super.quantityDropped(random);
		}
	}

	@Override
	public int quantityDroppedWithBonus(final int fortune, final Random random) {
		if ((fortune > 0) && (Item.getItemFromBlock(this) != this.getItemDropped(this.getBlockState().getValidStates().iterator().next(), random, fortune))) {
			int i = random.nextInt(fortune + 2) - 1;

			if (i < 0) {
				i = 0;
			}

			return this.quantityDropped(random) * (i + 1);
		} else {
			return this.quantityDropped(random);
		}
	}

}
