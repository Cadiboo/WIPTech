package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;

public class ModItemBlock extends ItemBlock {

	public ModItemBlock(Block block) {

		this(block, block.getRegistryName());

	}

	public ModItemBlock(Block block, ResourceLocation registryName) {
		super(block);
		this.setRegistryName(registryName);
		ModUtil.setCreativeTab(this);
	}

	@Override
	public CreativeTabs[] getCreativeTabs() {
		return ModUtil.getCreativeTabs(this);
	}

}
