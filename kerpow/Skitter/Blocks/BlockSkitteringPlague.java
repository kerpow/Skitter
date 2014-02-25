package kerpow.Skitter.Blocks;

import java.util.ArrayList;
import java.util.Random;

import kerpow.Skitter.Skitter;
import kerpow.Skitter.Entities.EntitySkitterBase;
import kerpow.Skitter.Entities.EntitySkitterWarrior;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSkitteringPlague extends Block {
	public BlockSkitteringPlague(int par1) {
		super(par1, Material.materialCarpet);
		// this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.05F, 1.0F);
		this.setTickRandomly(true);
		this.setCreativeTab(CreativeTabs.tabDecorations);

		this.setTextureName("skitter:plague");
		this.setLightOpacity(1);
		this.setHardness(.1F);
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
     * Return true if a player with Silk Touch can harvest this block directly, and not its normal drops.
     */
    protected boolean canSilkHarvest()
    {
        return false;
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
		if(this.canPlaceBlockAt(world, x, y, z))
		{
			this.destroyTreeAbove(world, x, y, z);
			return true;
		}

		return false;
	}

	@Override
	public int getRenderType() {
		return -1;
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
					if (this.spreadHere(world, x, y, z))
						return;

	}

	@Override
	public void onEntityWalking(World world, int par2, int par3, int par4, Entity entity) {
		super.onEntityWalking(world, par2, par3, par4, entity);

	}

	@Override
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity) {
		// TODO Auto-generated method stub
		super.onEntityCollidedWithBlock(par1World, par2, par3, par4, par5Entity);
		if(!par1World.isRemote)
		{ 
			if(par5Entity instanceof EntityPlayer){
				par1World.setBlockToAir(par2, par3, par4);
			}
		}
	}

	public void updateTick(World world, int xpos, int ypos, int zpos, Random par5Random) {
		if (!world.isDaytime()) {
			// check spread
			this.doPlagueSpread(world, xpos, ypos, zpos);
		}

		// check spawn
		if (world.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(xpos - 10, ypos - 3, zpos - 10, xpos + 10, ypos + 3, zpos + 10)).size() > 0
				&& world.getEntitiesWithinAABB(EntitySkitterBase.class, AxisAlignedBB.getBoundingBox(xpos - 10, ypos - 3, zpos - 10, xpos + 10, ypos + 3, zpos + 10)).size() < 10) {
			// spawn skitter
			EntitySkitterWarrior skitter = new EntitySkitterWarrior(world);
			skitter.setLocationAndAngles(xpos, ypos, zpos, 0, 0.0F);
			if (skitter.getCanSpawnHere()) {
				this.removeNearbyPlague(world, xpos, ypos, zpos);
				world.spawnEntityInWorld(skitter);
			}
		}
	}

	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune) {

		return new ArrayList<ItemStack>();
	}

	public int removeNearbyPlague(World world, int xpos, int ypos, int zpos) {
		int plagueCount = 0;

		// i'm to lazy to make a proper radius algorithm so here's this instead
		for (int y = ypos - 1; y <= ypos + 1; y++) {
			for(int x = xpos - 1; x <= xpos + 1; x++)
				for(int z = zpos - 1; z <= zpos + 1; z++)
					this.removePlague(world, x, y, z);
		
			//remove a little extra so it's a perfect square
			this.removePlague(world, xpos + 2, y, zpos);
			this.removePlague(world, xpos - 1, y, zpos);
			this.removePlague(world, xpos, y, zpos + 2);
			this.removePlague(world, xpos, y, zpos - 2);
		}

		return plagueCount;
	}

	public boolean removePlague(World world, int x, int y, int z) {
		if (world.getBlockId(x, y, z) == Skitter.skitterPlague.blockID) {

			world.setBlockToAir(x, y, z);
			return true;

		}
		return false;
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
		return true;
	}
}
