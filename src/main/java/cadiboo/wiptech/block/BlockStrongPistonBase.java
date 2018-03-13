package cadiboo.wiptech.block;

import java.util.List;

import net.minecraft.block.BlockPistonBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockStrongPistonBase extends BlockPistonBase {

	public BlockStrongPistonBase(String name) {
		super(false);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

		tooltip.add("\u00A76\u00A7o"+"Should be able to push obsidian but for some reason it just can\'t.");
		tooltip.add("After about 4 hours non stop trying to do the eqivalent of changing ONE LINE of minecraft source code I give up.");
		tooltip.add("Update - After more attepts this block doesn\'t even do anything at all anymore.");
	}

}
