package cadiboo.wiptech.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelEnamel extends ModelBase {

	private ModelRenderer enamel;

	public ModelEnamel() {
		enamel = new ModelRenderer(this, 0, 0);
		enamel.setTextureSize(16, 16);
		enamel.addBox(1, -2, -2, 7, 4, 4);
		enamel.mirror = true;
	}

	public void render(float size) {
		enamel.render(size);
	}

}
