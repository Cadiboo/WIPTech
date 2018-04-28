package cadiboo.wiptech.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelWire extends ModelBase {

	private ModelRenderer wire;

	public ModelWire() {
		wire = new ModelRenderer(this, 0, 438);
		wire.setTextureSize(16, 1000);
		wire.addBox(1, -1, -1, 7, 2, 2);
		wire.mirror = true;
	}

	public void render(float size) {
		wire.render(size);
	}

}
