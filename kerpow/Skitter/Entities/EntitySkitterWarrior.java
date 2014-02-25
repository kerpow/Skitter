package kerpow.Skitter.Entities;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

import kerpow.Skitter.Skitter;
import kerpow.Skitter.AI.EntityAISiege;
import kerpow.Skitter.AI.EntityAISiegeCeiling;
import kerpow.Skitter.AI.EntityAISiegeTargeting;
import kerpow.Skitter.AI.EntityAISiegeWalls;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentThorns;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeInstance;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class EntitySkitterWarrior extends EntitySkitterBase {

	private int specialActionTick = 0;

	private enum PowerType {
		FireImmune, Runner, Healthy, Climber, Dangerous
	}

	private PathEntity pathToEntity;

	public EntitySkitterWarrior(World world) {
		super(world);

		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAISiegeWalls(this));
		this.tasks.addTask(1, new EntityAISiegeCeiling(this));
		this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
		this.tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityVillager.class, 1.0D, true));
		this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(5, new EntityAIMoveThroughVillage(this, 1.0D, false));
		this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		// this.targetTasks.addTask(2, new EntityAISiegeTargeting(this,
		// EntityPlayer.class, 0, false));
		this.targetTasks.addTask(2, new EntityAISiegeTargeting(this, EntityVillager.class, 0, false));
		this.targetTasks.addTask(2, new EntityAISiegeTargeting(this, EntityPlayer.class, 0, false));

		if (!world.isRemote) {

			// random power
			PowerType[] powers = PowerType.values();
			int powerIndex = world.rand.nextInt(powers.length);

			//this.setPower(powers[powerIndex]);

		}
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	protected boolean isAIEnabled() {
		return true;

	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(5.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.3D);

	}

	protected void entityInit() {
		super.entityInit();
		this.getDataWatcher().addObject(20, Byte.valueOf((byte) 0));
		this.dataWatcher.addObject(16, new Byte((byte) 0));

	}

	/**
	 * Finds the closest player within 16 blocks to attack, or null if this
	 * Entity isn't interested in attacking (Animals, Spiders at day, peaceful
	 * PigZombies).
	 */
	protected Entity findPlayerToAttack() {
		EntityPlayer player = this.worldObj.getClosestVulnerablePlayerToEntity(this, 40D);
		if (player != null)
			Skitter.l("Found Player");
		return player;
	}

	/**
	 * Sets the Entity inside a web block.
	 */
	public void setInWeb() {
		this.isInWeb = false;
		this.fallDistance = 0.0F;
	}

	/**
	 * Get this Entity's EnumCreatureAttribute
	 */
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.ARTHROPOD;
	}

	/**
	 * Basic mob attack. Default to touch of death in EntityCreature. Overridden
	 * by each mob to define their attack.
	 */
	protected void attackEntity(Entity par1Entity, float par2) {
		float f1 = this.getBrightness(1.0F);

		if (f1 > 0.5F && this.rand.nextInt(100) == 0) {
			this.entityToAttack = null;
		} else {
			if (par2 > 2.0F && par2 < 6.0F && this.rand.nextInt(10) == 0) {
				if (this.onGround) {
					double d0 = par1Entity.posX - this.posX;
					double d1 = par1Entity.posZ - this.posZ;
					float f2 = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
					this.motionX = d0 / (double) f2 * 0.5D * 0.800000011920929D + this.motionX * 0.20000000298023224D;
					this.motionZ = d1 / (double) f2 * 0.5D * 0.800000011920929D + this.motionZ * 0.20000000298023224D;
					this.motionY = 0.4000000059604645D;
				}
			} else {
				super.attackEntity(par1Entity, par2);
			}
		}
	}

	public boolean attackEntityAsMob(Entity par1Entity) {
		float f = (float) this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
		int i = 0;

		if (par1Entity instanceof EntityLivingBase) {
			f += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLivingBase) par1Entity);
			i += EnchantmentHelper.getKnockbackModifier(this, (EntityLivingBase) par1Entity);
		}

		boolean flag = par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), f);

		if (flag) {
			if (i > 0) {
				par1Entity.addVelocity((double) (-MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F)
						* (float) i * 0.5F), 0.1D,
						(double) (MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F) * (float) i * 0.5F));
				this.motionX *= 0.6D;
				this.motionZ *= 0.6D;
			}

			int j = EnchantmentHelper.getFireAspectModifier(this);

			if (j > 0) {
				par1Entity.setFire(j * 4);
			}

			if (par1Entity instanceof EntityLivingBase) {
				EnchantmentThorns.func_92096_a(this, (EntityLivingBase) par1Entity, this.rand);
			}
		}

		return flag;
	}

	/**
	 * returns true if this entity is by a ladder, false otherwise
	 */
	@Override
	public boolean isOnLadder() {

		return false;
	}

	/**
	 * Return the power
	 */
	public PowerType getPower() {
		int powerIndex = this.getDataWatcher().getWatchableObjectByte(20);

		return PowerType.values()[powerIndex];
	}

	/**
	 * Sets the power
	 */
	public void setPower(PowerType power) {
		byte powerIndex = 0;
		PowerType[] powers = PowerType.values();
		for (byte x = 0; x < powers.length; x++)
			if (powers[x] == power) {
				powerIndex = x;
				break;
			}

		this.getDataWatcher().updateObject(20, powerIndex);

		this.setupPowerEffects(power);
	}

	private void setupPowerEffects(PowerType power) {

		switch (power) {
		case Runner:
			this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(.4d);
			break;
		case FireImmune:
			this.isImmuneToFire = true;
			break;
		case Healthy:
			this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(20D);
			break;
		case Dangerous:
			this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setAttribute(6D);

		}
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	public void onLivingUpdate() {

		if (!this.worldObj.isRemote) {

			// if there are no nearby players
			List players = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.boundingBox.expand(20,20,20));

			if (players.size() == 0){
				this.despawnEntity();
			}

			// climb webs
			if (this.worldObj.getBlockId((int) this.posX, (int) this.posY, (int) this.posZ) == Skitter.skitterWeb.blockID
					&& this.getAttackTarget() != null
					&& this.getDistance(this.getAttackTarget().posX, posY, this.getAttackTarget().posZ) < 3
					&& this.posY < this.getAttackTarget().posY) {
				// this.entity.isCollidedHorizontally = true;
				this.motionY = .4;
				// this.entity.isAirBorne = true;
			}

			// get block at feet
			/*
			 * int y = (int) this.posY - 1; int blockIDatFeet =
			 * this.worldObj.getBlockId((int) this.posX, y, (int) this.posZ); if
			 * (blockIDatFeet == Block.dirt.blockID || blockIDatFeet ==
			 * Block.grass.blockID || blockIDatFeet == Block.stone.blockID) {
			 * this.worldObj.setBlock((int) this.posX, y, (int) this.posZ,
			 * Block.netherrack.blockID);
			 * 
			 * }
			 */
			// if my target is above me and there is a web move up

		}

		super.onLivingUpdate();
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return "mob.zombie.say";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.zombie.hurt";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.zombie.death";
	}

	/**
	 * Plays step sound at given x, y, z for the entity
	 */
	protected void playStepSound(int par1, int par2, int par3, int par4) {
		this.playSound("mob.zombie.step", 0.15F, 1.0F);
	}

	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

}