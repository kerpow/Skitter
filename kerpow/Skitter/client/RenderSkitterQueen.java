package kerpow.Skitter.client;

import kerpow.Skitter.Entities.EntitySkitterQueen;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSkitterQueen extends RenderLiving
{    
	private static final ResourceLocation Your_Texture = new ResourceLocation("skitter:textures/entities/spider.png");  //refers to:assets/yourmod/textures/entity/yourtexture.png

    /** Scale of the model to use */
    private float scale;

    public RenderSkitterQueen()
    {
        super(new ModelSpider(), 3F);
        this.scale = 3;
    }

    /**
     * Applies the scale to the transform matrix
     */
    protected void preRenderScale(EntitySkitterQueen par1EntityGiantZombie, float par2)
    {
        GL11.glScalef(this.scale, this.scale, this.scale);
    }


    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase par1EntityLivingBase, float par2)
    {
        this.preRenderScale((EntitySkitterQueen)par1EntityLivingBase, par2);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return Your_Texture;
    }
}
