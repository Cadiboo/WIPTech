package cadiboo.wiptech.block;

import cadiboo.wiptech.util.ModReference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

public class ModBlock extends Block {

	public ModBlock(Material materialIn, String name) {
		this(materialIn, new ResourceLocation(ModReference.MOD_ID, name));
	}

	public ModBlock(Material materialIn, ResourceLocation resourceLocation) {
		this(materialIn, resourceLocation, resourceLocation.getResourcePath());
	}

	public ModBlock(Material materialIn, ResourceLocation registryName, String unlocalizedName) {
		super(materialIn);
		this.setRegistryName(registryName);
		this.setUnlocalizedName(unlocalizedName);
	}

}
