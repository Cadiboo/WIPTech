package cadiboo.wiptech.block;

import cadiboo.wiptech.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

public class BlockBase extends Block {

	public BlockBase(Material materialIn, String name) {
		super(materialIn);
		this.setRegistryName(new ResourceLocation(Reference.ID, name));
		this.setUnlocalizedName(name);
	}

}
