package cadiboo.wiptech.block;

import cadiboo.wiptech.util.ModReference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

public class ModBlock extends Block {

	public ModBlock(Material materialIn, String name) {
		this(materialIn, new ResourceLocation(ModReference.ID, name));
	}

	public ModBlock(Material materialIn, ResourceLocation resourceLocation) {
		super(materialIn);
		this.setRegistryName(resourceLocation);
		this.setUnlocalizedName(resourceLocation.getResourcePath());
	}

}
