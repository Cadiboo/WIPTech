package cadiboo.wiptech.block;

import cadiboo.wiptech.handler.EnumHandler.ConductiveMetals;
import net.minecraft.block.material.Material;

public class BlockSpool extends BlockBase {

	private final ConductiveMetals metal;

	public BlockSpool(String name, Material materialIn, ConductiveMetals metalIn) {
		super(name, materialIn);
		this.setBeaconBase();
		this.setNonSolidBlock();
		this.metal = metalIn;
	}

}
