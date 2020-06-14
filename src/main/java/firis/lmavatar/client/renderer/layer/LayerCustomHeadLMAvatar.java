package firis.lmavatar.client.renderer.layer;

import firis.lmavatar.client.renderer.RendererLMAvatar;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * かぼちゃ頭用
 * net.minecraft.client.renderer.entity.layers.LayerCustomHeadをベースに作成する
 * @author firis-games
 *
 */
public class LayerCustomHeadLMAvatar extends LayerCustomHead
{
    private final RendererLMAvatar renderer;
    
    //無名クラス定義
    private static ModelRenderer dummyModelRenderer = new ModelRenderer(new ModelBase() {});

    public LayerCustomHeadLMAvatar(RendererLMAvatar renderer)
    {
    	super(dummyModelRenderer);
        this.renderer = renderer;
    }

    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
    	EntityPlayer player = (EntityPlayer) entitylivingbaseIn;
		
		GlStateManager.pushMatrix();
		
		//PostRender
		renderer.getLittleMaidMultiModel().headPostRender(scale);
		
		//微調整
		if (player.isSneaking()) {
			GlStateManager.translate(0.0F, -0.2F, 0.0F);
		}
		
		super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
		
		GlStateManager.popMatrix();
    }

}