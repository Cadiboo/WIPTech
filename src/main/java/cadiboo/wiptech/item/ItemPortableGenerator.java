package cadiboo.wiptech.item;

import cadiboo.wiptech.entity.item.EntityPortableGenerator;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemPortableGenerator extends ModItem {

	private static final IBehaviorDispenseItem PORTABLE_GENERATOR_DISPENSER_BEHAVIOR = new BehaviorDefaultDispenseItem() {

		private final BehaviorDefaultDispenseItem behaviourDefaultDispenseItem = new BehaviorDefaultDispenseItem();

		/**
		 * Dispense the specified stack, play the dispense sound and spawn particles.
		 */
		@Override
		public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
			EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
			World world = source.getWorld();
			BlockPos blockpos = source.getBlockPos().offset(enumfacing);

			EntityPortableGenerator generator = new EntityPortableGenerator(world);
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
		protected void playDispenseSound(IBlockSource source) {
			source.getWorld().playEvent(1000, source.getBlockPos(), 0);
		}
	};

	public ItemPortableGenerator(String name) {
		super(name);
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, PORTABLE_GENERATOR_DISPENSER_BEHAVIOR);
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		ItemStack stack = player.getHeldItem(hand);

		if (worldIn.isRemote)
			return EnumActionResult.SUCCESS;
		if (!player.canPlayerEdit(pos.offset(facing), facing, stack))
			return EnumActionResult.FAIL;

		BlockPos blockpos = pos.offset(facing);
		EntityPortableGenerator generator = new EntityPortableGenerator(worldIn);
		generator.setPosition(blockpos.getX() + 0.5, blockpos.getY(), blockpos.getZ() + 0.5);

		if (stack.hasDisplayName()) {
			generator.setCustomNameTag(stack.getDisplayName());
		}

		worldIn.spawnEntity(generator);

		return EnumActionResult.SUCCESS;
	}
}