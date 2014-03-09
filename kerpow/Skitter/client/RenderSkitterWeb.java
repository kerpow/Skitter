package kerpow.Skitter.Client;

import kerpow.Skitter.Skitter;
import kerpow.Skitter.Blocks.BlockSkitteringPlague;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderSkitterWeb implements ISimpleBlockRenderingHandler {
	/** The minimum X value for rendering (default 0.0). */
	public double renderMinX;

	/** The maximum X value for rendering (default 1.0). */
	public double renderMaxX;

	/** The minimum Y value for rendering (default 0.0). */
	public double renderMinY;

	/** The maximum Y value for rendering (default 1.0). */
	public double renderMaxY;

	/** The minimum Z value for rendering (default 0.0). */
	public double renderMinZ;

	/** The maximum Z value for rendering (default 1.0). */
	public double renderMaxZ;

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		Icon c = Skitter.blockSkitterPlague.getIcon(0, 0);
		float u = c.getMinU();
		float v = c.getMinV();
		float U = c.getMaxU();
		float V = c.getMaxV();

		Tessellator tessellator = Tessellator.instance;
		
		int meta = world.getBlockMetadata(x, y, z);

		int brightness = Block.blocksList[Block.stone.blockID].getMixedBrightnessForBlock(world, x, y, z);
		tessellator.setBrightness(brightness);
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);

		tessellator.addTranslation(x, y, z);
		
		int blockId = 0;
		
		blockId = world.getBlockId(x, y - 1, z);
		if(blockId != 0 && blockId != Skitter.blockSkitterPlague.blockID && blockId != Skitter.blockSkitterWeb.blockID)
			this.renderCube(tessellator, 0, 0, 0, 1F, .1F, 1F, u, U, v, V);

		tessellator.addTranslation(-x, -y, -z);
		
		//render the web
		
		this.renderCobWeb(world, block,  x, y, z);

		return false;
	}
	

    public boolean renderCobWeb(IBlockAccess world, Block par1Block, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(world, par2, par3, par4));
        float f = 1.0F;
        int l = par1Block.colorMultiplier(world, par2, par3, par4);
        float f1 = (float)(l >> 16 & 255) / 255.0F;
        float f2 = (float)(l >> 8 & 255) / 255.0F;
        float f3 = (float)(l & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f4 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
            float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
            float f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
            f1 = f4;
            f2 = f5;
            f3 = f6;
        }

        tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
        double d0 = (double)par2;
        double d1 = (double)par3;
        double d2 = (double)par4;

        if (par1Block == Block.tallGrass)
        {
            long i1 = (long)(par2 * 3129871) ^ (long)par4 * 116129781L ^ (long)par3;
            i1 = i1 * i1 * 42317861L + i1 * 11L;
            d0 += ((double)((float)(i1 >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
            d1 += ((double)((float)(i1 >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
            d2 += ((double)((float)(i1 >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;
        }

        this.drawCrossedSquares(par1Block, world.getBlockMetadata(par2, par3, par4), d0, d1, d2, 1.0F);
        return true;
    }
	

    public void drawCrossedSquares(Block par1Block, int par2, double par3, double par5, double par7, float par9)
    {
        Tessellator tessellator = Tessellator.instance;
        Icon icon = Skitter.blockSkitterWeb.getIcon(0, par2);

        double d3 = (double)icon.getMinU();
        double d4 = (double)icon.getMinV();
        double d5 = (double)icon.getMaxU();
        double d6 = (double)icon.getMaxV();
        double d7 = 0.45D * (double)par9;
        double d8 = par3 + 0.5D - d7;
        double d9 = par3 + 0.5D + d7;
        double d10 = par7 + 0.5D - d7;
        double d11 = par7 + 0.5D + d7;
        tessellator.addVertexWithUV(d8, par5 + (double)par9, d10, d3, d4);
        tessellator.addVertexWithUV(d8, par5 + 0.0D, d10, d3, d6);
        tessellator.addVertexWithUV(d9, par5 + 0.0D, d11, d5, d6);
        tessellator.addVertexWithUV(d9, par5 + (double)par9, d11, d5, d4);
        tessellator.addVertexWithUV(d9, par5 + (double)par9, d11, d3, d4);
        tessellator.addVertexWithUV(d9, par5 + 0.0D, d11, d3, d6);
        tessellator.addVertexWithUV(d8, par5 + 0.0D, d10, d5, d6);
        tessellator.addVertexWithUV(d8, par5 + (double)par9, d10, d5, d4);
        tessellator.addVertexWithUV(d8, par5 + (double)par9, d11, d3, d4);
        tessellator.addVertexWithUV(d8, par5 + 0.0D, d11, d3, d6);
        tessellator.addVertexWithUV(d9, par5 + 0.0D, d10, d5, d6);
        tessellator.addVertexWithUV(d9, par5 + (double)par9, d10, d5, d4);
        tessellator.addVertexWithUV(d9, par5 + (double)par9, d10, d3, d4);
        tessellator.addVertexWithUV(d9, par5 + 0.0D, d10, d3, d6);
        tessellator.addVertexWithUV(d8, par5 + 0.0D, d11, d5, d6);
        tessellator.addVertexWithUV(d8, par5 + (double)par9, d11, d5, d4);
    }


	private void renderCube(Tessellator tessellator, float xStart, float yStart, float zStart, float width, float height, float depth, float u, float U, float v, float V){
		//left side
		tessellator.addVertexWithUV(xStart, yStart, zStart, u, V); 
		tessellator.addVertexWithUV(xStart, yStart, zStart + depth, u, v);
		tessellator.addVertexWithUV(xStart, yStart + height, zStart + depth, U, v);
		tessellator.addVertexWithUV(xStart, yStart + height, zStart, U, V);
		
		 //right side
		tessellator.addVertexWithUV(xStart + width, yStart + height, zStart, U, V);
		tessellator.addVertexWithUV(xStart + width, yStart + height, zStart + depth, U, v);
		tessellator.addVertexWithUV(xStart + width, yStart, zStart + depth, u, v);
		tessellator.addVertexWithUV(xStart + width, yStart, zStart, u, V); 

		//far side
		tessellator.addVertexWithUV(xStart, yStart, zStart + depth, u, V); 
		tessellator.addVertexWithUV(xStart + width, yStart, zStart + depth, u, v);
		tessellator.addVertexWithUV(xStart + width, yStart + height, zStart + depth, U, v);
		tessellator.addVertexWithUV(xStart, yStart + height, zStart + depth, U, V); 

		//close side
		tessellator.addVertexWithUV(xStart, yStart + height, zStart, U, V); 
		tessellator.addVertexWithUV(xStart + width, yStart + height, zStart, U, v);
		tessellator.addVertexWithUV(xStart + width, yStart, zStart, u, v);
		tessellator.addVertexWithUV(xStart, yStart, zStart, u, V); 
		

		//bottom side
		tessellator.addVertexWithUV(xStart, yStart , zStart, U, V); 
		tessellator.addVertexWithUV(xStart + width, yStart, zStart, U, v);
		tessellator.addVertexWithUV(xStart + width, yStart, zStart + depth, u, v);
		tessellator.addVertexWithUV(xStart, yStart, zStart + depth, u, V); 

		//top side
		tessellator.addVertexWithUV(xStart, yStart + height, zStart + depth, u, V); 
		tessellator.addVertexWithUV(xStart + width, yStart + height, zStart + depth, u, v);
		tessellator.addVertexWithUV(xStart + width, yStart + height, zStart, U, v);
		tessellator.addVertexWithUV(xStart, yStart + height, zStart, U, V); 
		
		

		
		
		
		/*
		tessellator.addVertexWithUV(0, 0, 0, u, v); //left side
		tessellator.addVertexWithUV(0, 0, 1, u, V);
		tessellator.addVertexWithUV(0, 1, 1, U, V);
		tessellator.addVertexWithUV(0, 1, 0, U, v);

		tessellator.addVertexWithUV(0, 1, 0, U, v);  //close side
		tessellator.addVertexWithUV(1, 1, 0, U, V);
		tessellator.addVertexWithUV(1, 0, 0, u, V);
		tessellator.addVertexWithUV(0, 0, 0, u, v);
		
		tessellator.addVertexWithUV(1, 1, 0, u, v); //right side
		tessellator.addVertexWithUV(1, 1, 1, u, V);
		tessellator.addVertexWithUV(1, 0, 1, U, V);
		tessellator.addVertexWithUV(1, 0, 0, U, v); 
		
		tessellator.addVertexWithUV(1, 0, 1, U, v);  //far side
		tessellator.addVertexWithUV(1, 1, 1, U, V);
		tessellator.addVertexWithUV(0, 1, 1, u, V);
		tessellator.addVertexWithUV(0, 0, 1, u, v);
		*/
		
	}
	
	
	
	private void renderBottom(Tessellator tessellator, int x, int y, int z, float u, float U, float v, float V){
		
		tessellator.addVertexWithUV(0, 0, 0, u, V); //bottom
		tessellator.addVertexWithUV(1, 0, 0, u, v);
		tessellator.addVertexWithUV(1, 0, 1, U, v);
		tessellator.addVertexWithUV(0, 0, 1, U, V);

		tessellator.addVertexWithUV(0, .125F, 1, u, v); //top
		tessellator.addVertexWithUV(1, .125F, 1, u, V);
		tessellator.addVertexWithUV(1, .125F, 0, U, V);
		tessellator.addVertexWithUV(0, .125F, 0, U, v);

		tessellator.addVertexWithUV(0, 0, 0, u, v); //left side
		tessellator.addVertexWithUV(0, 0, 1, u, V);
		tessellator.addVertexWithUV(0, .125F, 1, U, V);
		tessellator.addVertexWithUV(0, .125F, 0, U, v);

		tessellator.addVertexWithUV(0, .125F, 0, U, v);  //close side
		tessellator.addVertexWithUV(1, .125F, 0, U, V);
		tessellator.addVertexWithUV(1, 0, 0, u, V);
		tessellator.addVertexWithUV(0, 0, 0, u, v);
		
		tessellator.addVertexWithUV(1, .125F, 0, u, v); //right side
		tessellator.addVertexWithUV(1, .125F, 1, u, V);
		tessellator.addVertexWithUV(1, 0, 1, U, V);
		tessellator.addVertexWithUV(1, 0, 0, U, v); 
		
		tessellator.addVertexWithUV(1, 0, 1, U, v);  //far side
		tessellator.addVertexWithUV(1, .125F, 1, U, V);
		tessellator.addVertexWithUV(0, .125F, 1, u, V);
		tessellator.addVertexWithUV(0, 0, 1, u, v);
	}
	
	@Override
	public boolean shouldRender3DInInventory() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getRenderId() {
		return Skitter.rendererSkitterWeb;
	}

}
