package kerpow.Skitter.Client;

import kerpow.Skitter.CommonProxy;
import kerpow.Skitter.Skitter;
import kerpow.Skitter.Entities.EntitySkitterQueen;
import kerpow.Skitter.Entities.EntitySkitterWarrior;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
        
    @Override
    public void registerRenderThings() 
    {
        RenderingRegistry.registerEntityRenderingHandler(EntitySkitterWarrior.class, new RenderSkitterWarrior());
        RenderingRegistry.registerEntityRenderingHandler(EntitySkitterQueen.class, new RenderSkitterQueen());

        
        Skitter.rendererSkitterPlague = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(Skitter.rendererSkitterPlague, new RenderSkitterPlague() );
        
        Skitter.rendererSkitterWeb = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(Skitter.rendererSkitterWeb, new RenderSkitterWeb() );
//the 0.5F is the shadowsize
    }
    
    @Override
    public void registerSound() {
            //MinecraftForge.EVENT_BUS.register(new YourSoundEvent());//register the sound event handling class
    }
}