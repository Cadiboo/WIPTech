package cadiboo.wiptech.item;

import cadiboo.wiptech.entity.item.EntityPortableGenerator;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemPortableGenerator extends Item implements IModItem {

	private static final IBehaviorDispenseItem PORTABLE_GENERATOR_DISPENSER_BEHAVIOR = new BehaviorDefaultDispenseItem() {

		private final BehaviorDefaultDispenseItem behaviourDefaultDispenseItem = new BehaviorDefaultDispenseItem();

		/**
		 * Dispense the specified stack, play the dispense sound and spawn particles.
		 */
		@Override
		public ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
			final EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
			final World world = source.getWorld();
			final BlockPos blockpos = source.getBlockPos().offset(enumfacing);

			final EntityPortableGenerator generator = new EntityPortableGenerator(world);
			generator.setPosition(blockpos.getX() + 0.5, blockpos.getY() + 0.5, blockpos.getZ() + 0.5);

			if (stack.hasDisplayName()) {
				generator.setCustomNameTag(stack.getDisplayName());
			}

			world.spawnEntity(generator);
			stack.shrink(1);
			return stack;
		}

		/**
		 * Play the dispense sound from the specified block.
		 */
		@Override
		protected void playDispenseSound(final IBlockSource source) {
			source.getWorld().playEvent(1000, source.getBlockPos(), 0);
		}
	};

	public ItemPortableGenerator(final String name) {
		ModUtil.setRegistryNames(this, name);
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, PORTABLE_GENERATOR_DISPENSER_BEHAVIOR);
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	@Override
	public EnumActionResult onItemUse(final EntityPlayer player, final World worldIn, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {

		final ItemStack stack = player.getHeldItem(hand);

		if (worldIn.isRemote) {
			return EnumActionResult.SUCCESS;
		}
		if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) {
			return EnumActionResult.FAIL;
		}

		final BlockPos blockpos = pos.offset(facing);
		final EntityPortableGenerator generator = new EntityPortableGenerator(worldIn);
		generator.setPosition(blockpos.getX() + 0.5, blockpos.getY(), blockpos.getZ() + 0.5);

		if (stack.hasDisplayName()) {
			generator.setCustomNameTag(stack.getDisplayName());
		}

		worldIn.spawnEntity(generator);

		return EnumActionResult.SUCCESS;
	}

	@Override
	public CreativeTabs[] getCreativeTabs() {
		return ModUtil.getCreativeTabs(this);
	}

}