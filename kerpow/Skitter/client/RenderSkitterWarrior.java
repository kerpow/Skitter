package kerpow.Skitter.Client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSkitterWarrior extends RenderLiving
{
    private static final ResourceLocation Your_Texture = new ResourceLocation("skitter:textures/entities/spider.png");  //refers to:assets/yourmod/textures/entity/yourtexture.png

    
    public RenderSkitterWarrior()
    {
        super(new ModelSpider(), 1F);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return Your_Texture;
    }
}