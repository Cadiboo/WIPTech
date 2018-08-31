package cadiboo.wiptech.item;

import cadiboo.wiptech.entity.item.EntityRailgun;
import cadiboo.wiptech.util.ModUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemRailgun extends Item {

	public ItemRailgun(final String name) {
		ModUtil.setRegistryNames(this, name);
	}

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
		final EntityRailgun railgun = new EntityRailgun(worldIn);
		railgun.setPosition(blockpos.getX() + 0.5, blockpos.getY(), blockpos.getZ() + 0.5);

		if (stack.hasDisplayName()) {
			railgun.setCustomNameTag(stack.getDisplayName());
		}

		worldIn.spawnEntity(railgun);

		return EnumActionResult.SUCCESS;
	}

	@Override
	public CreativeTabs[] getCreativeTabs() {
		return ModUtil.getCreativeTabs(this);
	}

}
