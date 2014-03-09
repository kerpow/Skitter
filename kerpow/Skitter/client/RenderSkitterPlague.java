package kerpow.Skitter.Client;

import kerpow.Skitter.Skitter;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderSkitterPlague implements ISimpleBlockRenderingHandler {
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
	public int getRenderId() {
		return Skitter.rendererSkitterPlague;
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

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		// TODO Auto-generated method stub

	}
	
	
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		final Icon c = Skitter.blockSkitterPlague.getIcon(0, 0);
		final float u = c.getMinU();
		final float v = c.getMinV();
		final float U = c.getMaxU();
		final float V = c.getMaxV();

		final Tessellator tessellator = Tessellator.instance;
		
		world.getBlockMetadata(x, y, z);

		final int brightness = Block.blocksList[Block.stone.blockID].getMixedBrightnessForBlock(world, x, y, z);
		tessellator.setBrightness(brightness);
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);

		tessellator.addTranslation(x, y, z);
		
		int blockId = 0;
		Block neighbor = null;
		
		blockId = world.getBlockId(x, y - 1, z);
		neighbor = Block.blocksList[blockId];
		if(blockId != 0 && neighbor.isOpaqueCube())
			this.renderCube(tessellator, 0, 0, 0, 1F, .1F, 1F, u, U, v, V);

		blockId = world.getBlockId(x, y + 1, z);
		neighbor = Block.blocksList[blockId];
		if(blockId != 0 && neighbor.isOpaqueCube())
			this.renderCube(tessellator, 0, .9F, 0, 1F, .1F, 1F, u, U, v, V);


		blockId = world.getBlockId(x - 1, y, z);
		neighbor = Block.blocksList[blockId];
		if(blockId != 0 && neighbor.isOpaqueCube())
			this.renderCube(tessellator, 0, 0, 0, .1F, 1F, 1F, u, U, v, V);

		blockId = world.getBlockId(x + 1, y, z);
		neighbor = Block.blocksList[blockId];
		if(blockId != 0 && neighbor.isOpaqueCube())
			this.renderCube(tessellator, .9F, 0, 0, .1F, 1F, 1F, u, U, v, V);

		blockId = world.getBlockId(x, y, z - 1);
		neighbor = Block.blocksList[blockId];
		if(blockId != 0 && neighbor.isOpaqueCube())
			this.renderCube(tessellator, 0, 0, 0, 1F, 1F, .1F, u, U, v, V);

		blockId = world.getBlockId(x, y, z + 1);
		neighbor = Block.blocksList[blockId];
		if(blockId != 0 && neighbor.isOpaqueCube())
			this.renderCube(tessellator, 0, 0, .9F, 1F, 1F, .1F, u, U, v, V);

		
		tessellator.addTranslation(-x, -y, -z);

		return false;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		// TODO Auto-generated method stub
		return false;
	}

}
