package cadiboo.wiptech.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockTungstenOre extends BlockBase
{

	public BlockTungstenOre(String name, Material material)
	{
		super(name, material);
		this.setOreBlock();
	}
	
}