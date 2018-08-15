package cadiboo.wiptech.item;

import cadiboo.wiptech.entity.item.EntityRailgun;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemRailgun extends ModItem {

	public ItemRailgun(String name) {
		super(name);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		ItemStack stack = player.getHeldItem(hand);

		if (worldIn.isRemote)
			return EnumActionResult.SUCCESS;
		if (!player.canPlayerEdit(pos.offset(facing), facing, stack))
			return EnumActionResult.FAIL;

		BlockPos blockpos = pos.offset(facing);
		EntityRailgun railgun = new EntityRailgun(worldIn);
		railgun.setPosition(blockpos.getX() + 0.5, blockpos.getY(), blockpos.getZ() + 0.5);

		if (stack.hasDisplayName()) {
			railgun.setCustomNameTag(stack.getDisplayName());
		}

		worldIn.spawnEntity(railgun);

		return EnumActionResult.SUCCESS;
	}

}
