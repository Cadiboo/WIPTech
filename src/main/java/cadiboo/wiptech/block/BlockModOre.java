package cadiboo.wiptech.block;

import java.util.Random;

import cadiboo.wiptech.material.ModMaterial;
import cadiboo.wiptech.util.ModReference;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Custom ore block for materials
 * @author Cadiboo
 */
public class BlockModOre extends Block implements IModBlock, IBlockModMaterial {

	protected final ModMaterial material;

	public BlockModOre(final ModMaterial material) {
		super(Material.ROCK);
		ModUtil.setRegistryNames(this, material, "ore");
		this.material = material;
	}

	@Override
	public final ModMaterial getModMaterial() {
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
		} else if (this.getModMaterial() == ModMaterial.GLITCH) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(final IBlockState stateIn, final World world, final BlockPos pos, final Random rand) {
		super.randomDisplayTick(stateIn, world, pos, rand);
		world.getLight(pos);
	}

	@Override
	public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
		final Item item = this.material.getProperties().getOreDrop();
		if (item == null) {
			return Item.getItemFromBlock(this);
		}
		return item;
	}

	@Override
	public int quantityDroppedWithBonus(final int fortune, final Random random) {
		return this.material.getProperties().getQuantityDroppedWithBonus(fortune, random);
	}

}
