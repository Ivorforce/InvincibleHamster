package ivorius.invinciblehamster;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAINomCages extends EntityAIBase
{
    private EntityLiving theEntity;
    private World theWorld;

    private EntityItem cageEntity;
    private int cageBlockX, cageBlockY, cageBlockZ;

    /**
     * A decrementing tick used for the sheep's head offset and animation.
     */
    int eatCageTick = 0;

    public EntityAINomCages(EntityLiving par1EntityLiving)
    {
        this.theEntity = par1EntityLiving;
        this.theWorld = par1EntityLiving.worldObj;
        this.setMutexBits(0);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        EntityTameable entityTameable = (EntityTameable) this.theEntity;

        if (!entityTameable.isTamed() || theEntity.getRNG().nextInt(4) != 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.eatCageTick = 20;
        this.theWorld.setEntityState(this.theEntity, (byte) 10);
        this.theEntity.getNavigator().clearPathEntity();
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.eatCageTick = 0;
        cageEntity = null;
        cageBlockX = 0;
        cageBlockY = 0;
        cageBlockZ = 0;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return this.eatCageTick > 0;
    }

    public int func_75362_f()
    {
        return this.eatCageTick;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        if (cageEntity != null && cageEntity.isDead)
        {
            resetTask();
        }

        if (eatCageTick == 20)
        {
            if (teleportToNearestIron() == false)
            {
                resetTask();
            }
        }

        this.eatCageTick = Math.max(0, this.eatCageTick - 1);

        if (this.eatCageTick > 4)
        {
            if (cageEntity != null)
            {
                this.theEntity.setPosition(cageEntity.posX, cageEntity.posY, cageEntity.posZ);
            }
            else
            {
                this.theEntity.setPosition((float) cageBlockX + 0.5f, (float) cageBlockY + 0.5f, (float) cageBlockZ + 0.5f);
            }
        }
        if (this.eatCageTick == 4)
        {
            if (cageEntity != null && !cageEntity.isDead)
            {
                int var1 = MathHelper.floor_double(this.theEntity.posX);
                int var2 = MathHelper.floor_double(this.theEntity.posY);
                int var3 = MathHelper.floor_double(this.theEntity.posZ);

                this.theWorld.playAuxSFX(2001, var1, var2, var3, Block.getIdFromBlock(Blocks.iron_bars));
                cageEntity.setDead();
                this.theEntity.eatGrassBonus();
            }
            else if (cageEntity == null && this.theWorld.getBlock(cageBlockX, cageBlockY, cageBlockZ) == Blocks.iron_bars)
            {
                this.theWorld.playAuxSFX(2001, cageBlockX, cageBlockY, cageBlockZ, Block.getIdFromBlock(Blocks.iron_bars));
                this.theWorld.setBlock(cageBlockX, cageBlockY, cageBlockZ, Blocks.air, 0, 3);
                this.theEntity.eatGrassBonus();
            }
        }
    }

    public boolean teleportToNearestIron()
    {
        boolean foundIron = false;

        for (int x = 10; x > -11; x--)
        {
            for (int y = -4; y < 5; y++)
            {
                for (int z = -10; z < 11; z++)
                {
                    if (theWorld.getBlock(x + (int) theEntity.posX, y + (int) theEntity.posY, z + (int) theEntity.posZ) == Blocks.iron_bars)
                    {
                        if (x * x + y * y + z * z < (cageBlockX - (int) theEntity.posX) * (cageBlockX - (int) theEntity.posX) + (cageBlockY - (int) theEntity.posY) * (cageBlockY - (int) theEntity.posY) + (cageBlockZ - (int) theEntity.posZ) * (cageBlockZ - (int) theEntity.posZ))
                        {
                            cageBlockX = x + (int) theEntity.posX;
                            cageBlockY = y + (int) theEntity.posY;
                            cageBlockZ = z + (int) theEntity.posZ;

                            foundIron = true;
                        }
                    }
                }
            }
        }

        if (foundIron)
        {
            for (int i = 0; i < 40; i++)
            {
                double d = theEntity.posX + theWorld.rand.nextFloat() - 0.5F;
                double d2 = theEntity.posY + theWorld.rand.nextFloat() - 0.5F;
                double d4 = theEntity.posZ + theWorld.rand.nextFloat() - 0.5F;

                theWorld.spawnParticle("smoke", d, d2, d4, 0.0D, 0.0D, 0.0D);
            }

            theEntity.setPosition((cageBlockX) + 0.5, (cageBlockY), (cageBlockZ) + 0.5);

            for (int i = 0; i < 40; i++)
            {
                double d = theEntity.posX + theWorld.rand.nextFloat() - 0.5F;
                double d2 = theEntity.posY + theWorld.rand.nextFloat() - 0.5F;
                double d4 = theEntity.posZ + theWorld.rand.nextFloat() - 0.5F;

                theWorld.spawnParticle("smoke", d, d2, d4, 0.0D, 0.0D, 0.0D);
            }

            if (theWorld.getBlock(cageBlockX, cageBlockY - 1, cageBlockZ) == Blocks.air)
            {
                eatCageTick = 5;
            }
        }
        else
        {
            EntityItem iron = (EntityItem) theWorld.findNearestEntityWithinAABB(EntityItem.class, theEntity.boundingBox.expand(10D, 5D, 10D), this.theEntity);

            if (iron != null && Block.getBlockFromItem(iron.getEntityItem().getItem()) == Blocks.iron_bars)
            {
                for (int i = 0; i < 40; i++)
                {
                    double d = theEntity.posX + theWorld.rand.nextFloat() - 0.5F;
                    double d2 = theEntity.posY + theWorld.rand.nextFloat() - 0.5F;
                    double d4 = theEntity.posZ + theWorld.rand.nextFloat() - 0.5F;

                    theWorld.spawnParticle("smoke", d, d2, d4, 0.0D, 0.0D, 0.0D);
                }

                theEntity.setPosition(iron.posX, iron.posY, iron.posZ);

                this.cageEntity = iron;

                for (int i = 0; i < 40; i++)
                {
                    double d = theEntity.posX + theWorld.rand.nextFloat() - 0.5F;
                    double d2 = theEntity.posY + theWorld.rand.nextFloat() - 0.5F;
                    double d4 = theEntity.posZ + theWorld.rand.nextFloat() - 0.5F;

                    theWorld.spawnParticle("smoke", d, d2, d4, 0.0D, 0.0D, 0.0D);
                }

                foundIron = true;
            }
        }

        return foundIron;
    }
}
