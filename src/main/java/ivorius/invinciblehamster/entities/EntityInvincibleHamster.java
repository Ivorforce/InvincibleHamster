package ivorius.invinciblehamster.entities;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityInvincibleHamster extends EntityTameable
{
    public static int hamsterColorSlot = 25;

    private EntityAITempt aiTempt;

    public EntityInvincibleHamster(World par1World)
    {
        super(par1World);
        this.setSize(0.4F, 0.2F);

        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanicInvincibleHamster(this, 1.25F));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, new EntityAINomCages(this));
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0, true));
        this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0, 10.0F, 2.0F));
        this.tasks.addTask(6, new EntityAIMate(this, 1.0));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        this.tasks.addTask(10, new EntityAITempt(this, 0.25F, Item.getItemFromBlock(Blocks.red_flower), false));

        setHamsterColor(rand.nextInt(4));
        setTamed(false);
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();

        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.32);

        if (this.isTamed())
        {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(50.0D);
        }
        else
        {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0D);
        }
    }

    @Override
    public void setTamed(boolean par1)
    {
        super.setTamed(par1);

        if (par1)
        {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(50.0D);
        }
        else
        {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0D);
        }
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();

        this.dataWatcher.addObject(hamsterColorSlot, Integer.valueOf(0));
    }

    @Override
    public void onLivingUpdate()
    {

        super.onLivingUpdate();

        if (isTamed())
        {
            fleeingTick = 0;
        }
    }

    @Override
    protected String getLivingSound()
    {
        return null;
    }

    @Override
    protected String getHurtSound()
    {
        return null;
    }

    @Override
    protected String getDeathSound()
    {
        return null;
    }

    @Override
    protected boolean isAIEnabled()
    {

        return true;
    }

    @Override
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        ItemStack var2 = par1EntityPlayer.inventory.getCurrentItem();

        if (this.isTamed())
        {
            if (par1EntityPlayer.getCommandSenderName().equalsIgnoreCase(this.getOwnerName()) && !this.worldObj.isRemote && !this.isBreedingItem(var2))
            {
                this.aiSit.setSitting(!this.isSitting());
                this.isJumping = false;
                this.setPathToEntity((PathEntity) null);
            }
        }
        else if (/*this.aiTempt.func_75277_f() && */var2 != null && (var2.getItem() == Item.getItemFromBlock(Blocks.red_flower) || var2.getItem() == Item.getItemFromBlock(Blocks.yellow_flower)) && par1EntityPlayer.getDistanceSqToEntity(this) < 9.0D)
        {
            if (!par1EntityPlayer.capabilities.isCreativeMode)
            {
                --var2.stackSize;
            }

            if (var2.stackSize <= 0)
            {
                par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack) null);
            }

            if (!this.worldObj.isRemote)
            {
                if (this.rand.nextInt(3) == 0)
                {
                    this.setTamed(true);
                    this.setPathToEntity((PathEntity) null);
                    this.setAttackTarget((EntityLiving) null);
                    this.aiSit.setSitting(true);
                    this.setOwner(par1EntityPlayer.getCommandSenderName());
                    this.playTameEffect(true);
                    this.worldObj.setEntityState(this, (byte) 7);
                }
                else
                {
                    this.playTameEffect(false);
                    this.worldObj.setEntityState(this, (byte) 6);
                }
            }

            return true;
        }

        return super.interact(par1EntityPlayer);
    }

    @Override
    public EntityAgeable createChild(EntityAgeable var1)
    {
        EntityInvincibleHamster entityhamster = new EntityInvincibleHamster(worldObj);

        if (this.isTamed())
        {
            entityhamster.setOwner(this.getOwnerName());
            entityhamster.setTamed(true);
        }

        return entityhamster;
    }

    @Override
    public boolean isBreedingItem(ItemStack par1ItemStack)
    {
        return par1ItemStack != null && (par1ItemStack.getItem() == Item.getItemFromBlock(Blocks.red_flower) || par1ItemStack.getItem() == Item.getItemFromBlock(Blocks.yellow_flower));
    }

    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */
    @Override
    public boolean canMateWith(EntityAnimal par1EntityAnimal)
    {
        if (par1EntityAnimal == this)
        {
            return false;
        }
        else if (!this.isTamed())
        {
            return false;
        }
        else if (!(par1EntityAnimal instanceof EntityInvincibleHamster))
        {
            return false;
        }
        else
        {
            EntityInvincibleHamster var2 = (EntityInvincibleHamster) par1EntityAnimal;
            return !var2.isTamed() ? false : this.isInLove() && var2.isInLove();
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {

        if (par1DamageSource.canHarmInCreative() || !isTamed())
        {
            return super.attackEntityFrom(par1DamageSource, par2);
        }
        else
        {
            return super.attackEntityFrom(par1DamageSource, 0);
        }
    }

    @Override
    public boolean handleWaterMovement() //super contracts it by 0.4 in height >.>
    {
        this.boundingBox.setBounds(boundingBox.minX, boundingBox.minY - 0.4, boundingBox.minZ, boundingBox.maxX, boundingBox.maxY + 0.4, boundingBox.maxZ);

        boolean rValue = super.handleWaterMovement();

        this.boundingBox.setBounds(boundingBox.minX, boundingBox.minY + 0.4, boundingBox.minZ, boundingBox.maxX, boundingBox.maxY - 0.4, boundingBox.maxZ);

        return rValue;
    }

    @Override
    public boolean handleLavaMovement()
    {
        this.boundingBox.setBounds(boundingBox.minX, boundingBox.minY - 0.4, boundingBox.minZ, boundingBox.maxX, boundingBox.maxY + 0.4, boundingBox.maxZ);

        boolean rValue = super.handleLavaMovement();

        this.boundingBox.setBounds(boundingBox.minX, boundingBox.minY + 0.4, boundingBox.minZ, boundingBox.maxX, boundingBox.maxY - 0.4, boundingBox.maxZ);

        return rValue;
    }

    public void setHamsterColor(int color)
    {
        dataWatcher.updateObject(hamsterColorSlot, Integer.valueOf(color));
    }

    public int getHamsterColor()
    {
        return dataWatcher.getWatchableObjectInt(hamsterColorSlot);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound)
    {
        super.writeEntityToNBT(par1nbtTagCompound);

        par1nbtTagCompound.setInteger("hamsterColor", getHamsterColor());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound)
    {
        super.readEntityFromNBT(par1nbtTagCompound);

        setHamsterColor(par1nbtTagCompound.getInteger("hamsterColor"));
    }
}
