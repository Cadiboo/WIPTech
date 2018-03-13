package cadiboo.wiptech.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockOsmiumOre extends BlockBase
{

	public BlockOsmiumOre(String name, Material material)
	{
		super(name, material);
		this.setOreBlock();
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("\u00A76\u00A7o"+"Very Dense!");
	}
	
}