package cadiboo.wiptech.item;

import cadiboo.wiptech.util.ModUtil;
import cadiboo.wiptech.util.resourcelocation.ModResourceLocation;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;

public class ModItemBlock extends ItemBlock implements IModItem {

	public ModItemBlock(final Block block) {
		this(block, new ModResourceLocation(block.getRegistryName()));
	}

	public ModItemBlock(final Block block, final ModResourceLocation registryName) {
		super(block);
		ModUtil.setRegistryNames(this, registryName);
	}

	@Override
	public CreativeTabs[] getCreativeTabs() {
		return ModUtil.getCreativeTabs(this);
	}

}
