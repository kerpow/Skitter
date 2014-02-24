package kerpow.Skitter.Blocks;

import java.util.Random;

import kerpow.Skitter.Skitter;
import kerpow.Skitter.Entities.EntitySkitterWarrior;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSkitteringPlague extends Block {
	public BlockSkitteringPlague(int par1) {
		super(par1, Material.ground);
		// this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
		this.setTickRandomly(true);
		this.setCreativeTab(CreativeTabs.tabDecorations);

		this.setTextureName("skitter:plague");
		this.setLightOpacity(1);
		this.setHardness(1.0F);

	}

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this
	 * box can change after the pool has been cleared to be reused)
	 */
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		int l = par1World.getBlockMetadata(par2, par3, par4) & 7;
		float f = 0.125F;
		return AxisAlignedBB.getAABBPool().getAABB((double) par2 + this.minX, (double) par3 + this.minY, (double) par4 + this.minZ, (double) par2 + this.maxX, (double) ((float) par3 + (float) l * f),
				(double) par4 + this.maxZ);
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube? This determines whether
	 * or not to render the shared face of two adjacent blocks and also whether
	 * the player can attach torches, redstone wire, etc to this block.
	 */
	public boolean isOpaqueCube() {
		return false;
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False
	 * (examples: signs, buttons, stairs, etc)
	 */
	public boolean renderAsNormalBlock() {
		return false;
	}

	/**
	 * calls setBlockBounds based on the depth of the snow. Int is any values
	 * 0x0-0x7, usually this blocks metadata.
	 */
	/*
	 * protected void setBlockBoundsForSnowDepth(int par1) { int j = par1 & 7;
	 * float f = (float) (2 * (1 + j)) / 16.0F; this.setBlockBounds(0.0F, 0.0F,
	 * 0.0F, 1.0F, f, 1.0F); }
	 */
	/**
	 * Checks to see if its valid to put this block at the specified
	 * coordinates. Args: world, x, y, z
	 */
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
		int l = par1World.getBlockId(par2, par3 - 1, par4);
		Block block = Block.blocksList[l];
		if (block == null)
			return false;
		if (block == this && (par1World.getBlockMetadata(par2, par3 - 1, par4) & 7) == 7)
			return true;
		if (!block.isLeaves(par1World, par2, par3 - 1, par4) && !Block.blocksList[l].isOpaqueCube())
			return false;
		return par1World.getBlockMaterial(par2, par3 - 1, par4).blocksMovement();
	}

	/**
	 * Checks if the block is destroyable
	 * 
	 * @param world
	 * @param xpos
	 * @param ypos
	 * @param zpos
	 */
	private boolean spreadHere(World world, int x, int y, int z) {

		// check if the block below is air
		if ((world.isAirBlock(x, y, z) || Block.blocksList[world.getBlockId(x, y, z)].canPlaceBlockAt(world, x, y, z) || Block.blocksList[world.getBlockId(x, y, z)].isWood(world, x, y, z))
				&& !(world.isAirBlock(x, y - 1, z) || Block.blocksList[world.getBlockId(x, y - 1, z)].canPlaceBlockAt(world, x, y - 1, z) || Block.blocksList[world.getBlockId(x, y - 1, z)].isWood(
						world, x, y - 1, z)) && world.getBlockId(x, y, z) != Skitter.skitterPlague.blockID && world.getBlockId(x, y - 1, z) != Skitter.skitterPlague.blockID) {
			world.setBlock(x, y, z, Skitter.skitterPlague.blockID);
			this.destroyTreeAbove(world, x, y, z);
			return true;
		}

		return false;
	}

	/**
	 * This gets the job done but i'd like a cooler effect for it
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	private void destroyTreeAbove(World world, int x, int y, int z) {
		for (int y1 = y; y1 < y + 50; y1++) {

			int l = world.getBlockId(x, y1, z);
			Block block = Block.blocksList[l];
			if (block != null && (block.isWood(world, x, y1, z) || block.isLeaves(world, x, y1, z)))
				world.setBlockToAir(x, y1, z);

		}
	}

	/**
	 * Ticks the block if it's been scheduled
	 */

	private void doPlagueSpread(World world, int xpos, int ypos, int zpos) {

		for (int x = xpos - 1; x <= xpos + 1; x++)
			for (int y = ypos - 1; y <= ypos + 1; y++)
				for (int z = zpos - 1; z <= zpos + 1; z++)
					this.spreadHere(world, x, y, z);

	}

	public void updateTick(World world, int xpos, int ypos, int zpos, Random par5Random) {
		// check spread
		this.doPlagueSpread(world, xpos, ypos, zpos);

		// destroy trees
		this.destroyTreeAbove(world, xpos, ypos, zpos);
		// check spawn
		if (!world.isDaytime() && world.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(xpos - 10, ypos - 10, zpos - 10, xpos + 10, ypos + 10, zpos + 10)).size() > 0) {

			// spawn skitter
			EntitySkitterWarrior skitter = new EntitySkitterWarrior(world);
			skitter.setLocationAndAngles(xpos, ypos, zpos, 0, 0.0F);
			if (skitter.getCanSpawnHere())
				world.spawnEntityInWorld(skitter);
		}

	}

	@SideOnly(Side.CLIENT)
	/**
	 * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
	 * coordinates.  Args: blockAccess, x, y, z, side
	 */
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return par5 == 1 ? true : super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
	}

	/**
	 * Determines if a new block can be replace the space occupied by this one,
	 * Used in the player's placement code to make the block act like water, and
	 * lava.
	 * 
	 * @param world
	 *            The current world
	 * @param x
	 *            X Position
	 * @param y
	 *            Y position
	 * @param z
	 *            Z position
	 * @return True if the block is replaceable by another block
	 */
	@Override
	public boolean isBlockReplaceable(World world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		return (meta >= 7 ? false : blockMaterial.isReplaceable());
	}
}
