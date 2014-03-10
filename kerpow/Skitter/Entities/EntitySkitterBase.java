package kerpow.Skitter.Entities;

import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySkitterBase extends EntityMob {

	public EntitySkitterBase(World par1World) {
		super(par1World);

	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	protected void fall(float par1) {
	}

	/**
	 * Takes in the distance the entity has fallen this tick and whether its on
	 * the ground to update the fall distance and deal fall damage if landing on
	 * the ground. Args: distanceFallenThisTick, onGround
	 */
	protected void updateFallState(double par1, boolean par3) {
	}

	/**
	 * Moves the entity based on the specified heading. Args: strafe, forward
	 */
	public void moveEntityWithHeading(float par1, float par2) {
		double d0;

		if (this.isInWater()) {
			d0 = this.posY;
			this.moveFlying(par1, par2, this.isAIEnabled() ? 0.04F : 0.02F);
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.800000011920929D;
			this.motionY *= 0.800000011920929D;
			this.motionZ *= 0.800000011920929D;
			this.motionY -= 0.02D;

			if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d0, this.motionZ)) {
				this.motionY = 0.30000001192092896D;
			}
		} else if (this.handleLavaMovement()) {
			d0 = this.posY;
			this.moveFlying(par1, par2, 0.02F);
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.5D;
			this.motionY *= 0.5D;
			this.motionZ *= 0.5D;
			this.motionY -= 0.02D;

			if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d0, this.motionZ)) {
				this.motionY = 0.30000001192092896D;
			}
		} else {
			float f2 = 0.91F;

			if (this.onGround) {
				f2 = 0.54600006F;
				int i = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));

				if (i > 0) {
					f2 = Block.blocksList[i].slipperiness * 0.91F;
				}
			}

			float f3 = 0.16277136F / (f2 * f2 * f2);
			float f4;

			if (this.onGround) {
				f4 = this.getAIMoveSpeed() * f3;
			} else {
				f4 = this.jumpMovementFactor;
			}

			this.moveFlying(par1, par2, f4);
			f2 = 0.91F;

			if (this.onGround) {
				f2 = 0.54600006F;
				int j = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));

				if (j > 0) {
					f2 = Block.blocksList[j].slipperiness * 0.91F;
				}
			}

			if (this.isOnLadder()) {
				float f5 = 0.15F;

				if (this.motionX < (double) (-f5)) {
					this.motionX = (double) (-f5);
				}

				if (this.motionX > (double) f5) {
					this.motionX = (double) f5;
				}

				if (this.motionZ < (double) (-f5)) {
					this.motionZ = (double) (-f5);
				}

				if (this.motionZ > (double) f5) {
					this.motionZ = (double) f5;
				}

				this.fallDistance = 0.0F;

				if (this.motionY < -0.15D) {
					this.motionY = -0.15D;
				}

			}

			this.moveEntity(this.motionX, this.motionY, this.motionZ);

			if (this.isCollidedHorizontally && this.isOnLadder()) {
				this.motionY = 0.2D;
			}

			if (this.worldObj.isRemote && (!this.worldObj.blockExists((int) this.posX, 0, (int) this.posZ) || !this.worldObj.getChunkFromBlockCoords((int) this.posX, (int) this.posZ).isChunkLoaded)) {
				if (this.posY > 0.0D) {
					this.motionY = -0.1D;
				} else {
					this.motionY = 0.0D;
				}
			} else {
				this.motionY -= 0.08D;
			}

			this.motionY *= 0.9800000190734863D;
			this.motionX *= (double) f2;
			this.motionZ *= (double) f2;
		}

		this.prevLimbSwingAmount = this.limbSwingAmount;
		d0 = this.posX - this.prevPosX;
		double d1 = this.posZ - this.prevPosZ;
		float f6 = MathHelper.sqrt_double(d0 * d0 + d1 * d1) * 4.0F;

		if (f6 > 1.0F) {
			f6 = 1.0F;
		}

		this.limbSwingAmount += (f6 - this.limbSwingAmount) * 0.4F;
		this.limbSwing += this.limbSwingAmount;
	}

	/**
	 * returns true if this entity is by a ladder, false otherwise
	 */
	public boolean isOnLadder() {
		return false;
	}
}
