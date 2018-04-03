package cadiboo.wiptech.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelTurbine extends ModelBase {

	public ModelRenderer pillar;
	public ModelRenderer top;
	public ModelRenderer blade1;
	public ModelRenderer blade1holderTop;
	public ModelRenderer blade1holderBottom;
	public ModelRenderer blade2;
	public ModelRenderer blade2holderTop;
	public ModelRenderer blade2holderBottom;
	public ModelRenderer blade3;
	public ModelRenderer blade3holderTop;
	public ModelRenderer blade3holderBottom;

	public ModelTurbine()
	{
		textureWidth = 16;
		textureHeight = 16;

		pillar = new ModelRenderer(this, 5, 64);
		pillar.setTextureSize(80, 1024);
		pillar.addBox(-14, -93, 2, 12, 92, 12);
		pillar.mirror = true;
		
		blade1 = new ModelRenderer(this, 20, 32);
		blade1.setTextureSize(128, 128);
		blade1.addBox(-26, -32F, -4F, 2, 48, 8);
		blade1.setRotationPoint(-8, -64, 8);
		blade1.mirror = true;
		setRotation(blade1, 0F, 22.45F, 0F);
		
		blade1holderTop = new ModelRenderer(this, 0, 0);
		blade1holderTop.setTextureSize(80, 1024);
		blade1holderTop.addBox(-25, -2, -2, 25, 2, 4);
		blade1holderTop.setRotationPoint(-8, -80, 8);
		
		blade1holderBottom = new ModelRenderer(this, 0, 0);
		blade1holderBottom.setTextureSize(80, 1024);
		blade1holderBottom.addBox(-25, -2, -2, 25, 2, 4);
		blade1holderBottom.setRotationPoint(-8, -60, 8);
		
		blade2 = blade1;
		blade2holderTop = blade1holderTop;
		blade2holderBottom = blade1holderBottom;
		
		blade3 = blade1;
		blade3holderTop = blade1holderTop;
		blade3holderBottom = blade1holderBottom;

	}

	public void render(float size, double angle) 
	{
		pillar.render(size);
		
		setRotation(blade1, -0.25F, getRotation(getAbsoluteAngle(angle - 000)), 0F);
		blade1.render(size);
		setRotation(blade1holderTop, 0F, getRotation(getAbsoluteAngle(angle - 000))+0.25F, 0F);
		blade1holderTop.render(size);
		setRotation(blade1holderBottom, 0F, getRotation(getAbsoluteAngle(angle - 000)), 0F);
		blade1holderBottom.render(size);
		
		setRotation(blade2, -0.25F, getRotation(getAbsoluteAngle(angle - 120)), 0F);
		blade2.render(size);
		setRotation(blade1holderTop, 0F, getRotation(getAbsoluteAngle(angle - 120))+0.25F, 0F);
		blade2holderTop.render(size);
		setRotation(blade2holderBottom, 0F, getRotation(getAbsoluteAngle(angle - 120)), 0F);
		blade2holderBottom.render(size);
		
		setRotation(blade3, -0.25F, getRotation(getAbsoluteAngle(angle + 120)), 0F);
		blade3.render(size);
		setRotation(blade1holderTop, 0F, getRotation(getAbsoluteAngle(angle + 120))+0.25F, 0F);
		blade3holderTop.render(size);
		setRotation(blade3holderBottom, 0F, getRotation(getAbsoluteAngle(angle + 120)), 0F);
		blade3holderBottom.render(size);
		
	}

	public float getRotation(double angle) 
	{
		return ((float)angle / (float) 180) * (float)Math.PI;
	}

	public double getAbsoluteAngle(double angle) 
	{
		return angle % 360;
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) 
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
