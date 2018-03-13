package cadiboo.wiptech.entity.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBasic<T extends Entity>
  extends Render<T>
{
  protected final Item item;
  private final RenderItem itemRenderer;
  private float scale;
  
  public RenderBasic(RenderManager renderManagerIn, Item itemIn, RenderItem itemRendererIn, float scale)
  {
    super(renderManagerIn);
    this.item = itemIn;
    this.itemRenderer = itemRendererIn;
    this.scale = scale;
  }
  
  public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
  {
    GlStateManager.pushMatrix();
    GlStateManager.translate((float)x, (float)y, (float)z);
    GlStateManager.enableRescaleNormal();
    GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotate((this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
    bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    GlStateManager.scale(this.scale, this.scale, this.scale);
    if (this.renderOutlines)
    {
      GlStateManager.enableColorMaterial();
      GlStateManager.enableOutlineMode(getTeamColor(entity));
    }
    this.itemRenderer.renderItem(getStackToRender(entity), ItemCameraTransforms.TransformType.GROUND);
    if (this.renderOutlines)
    {
      GlStateManager.disableOutlineMode();
      GlStateManager.disableColorMaterial();
    }
    GlStateManager.disableRescaleNormal();
    GlStateManager.popMatrix();
    super.doRender(entity, x, y, z, entityYaw, partialTicks);
  }
  
  public ItemStack getStackToRender(T entityIn)
  {
    return new ItemStack(this.item);
  }
  
  protected ResourceLocation getEntityTexture(Entity entity)
  {
    return TextureMap.LOCATION_BLOCKS_TEXTURE;
  }
}
