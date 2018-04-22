package cadiboo.wiptech.block;

import cadiboo.wiptech.handler.EnumHandler.ResourceBlocks;
import net.minecraft.block.material.Material;

public class BlockResourceBlock extends BlockBase {

	private final ResourceBlocks resource;

	public BlockResourceBlock(String nameIn, Material materialIn, ResourceBlocks oreIn) {
		super(nameIn, materialIn);
		this.setBeaconBase();
		this.resource = oreIn;
	}

}
