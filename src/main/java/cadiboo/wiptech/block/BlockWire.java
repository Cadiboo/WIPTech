package cadiboo.wiptech.block;

import cadiboo.wiptech.handler.EnumHandler.ConductiveMetals;
import net.minecraft.block.material.Material;

public class BlockWire extends BlockBase {

	private final ConductiveMetals	metal;
	private boolean					enamel;

	public BlockWire(String nameIn, Material materialIn, ConductiveMetals metalIn, boolean enamelIn) {
		super(nameIn, materialIn);
		this.metal = metalIn;
		this.enamel = enamelIn;
	}

}