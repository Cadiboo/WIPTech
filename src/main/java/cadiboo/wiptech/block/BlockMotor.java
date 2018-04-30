package cadiboo.wiptech.block;

import cadiboo.wiptech.handler.EnumHandler.ConductiveMetals;
import net.minecraft.block.material.Material;

public class BlockMotor extends BlockBase {

	private final ConductiveMetals metal;

	public ConductiveMetals getMetal() {
		return metal;
	}

	public BlockMotor(String nameIn, Material materialIn, ConductiveMetals metalIn) {
		super(nameIn, materialIn);
		this.metal = metalIn;
		this.setTransparentBlock();
	}

}