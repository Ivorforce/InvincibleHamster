package ivorius.invinciblehamster;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderInvincibleHamster extends RenderLiving
{
    private float scale;

    public ResourceLocation[] untamedTextures;
    public ResourceLocation[] tamedTextures;

    public RenderInvincibleHamster()
    {
        super(new ModelInvincibleHamster(), 0.2F);
        scale = 0.5F;

        untamedTextures = new ResourceLocation[4];
        tamedTextures = new ResourceLocation[untamedTextures.length];

        untamedTextures[0] = new ResourceLocation(InvincibleHamster.MODID, InvincibleHamster.filePathTextures + "hamsterOrange.png");
        untamedTextures[1] = new ResourceLocation(InvincibleHamster.MODID, InvincibleHamster.filePathTextures + "hamsterBrown.png");
        untamedTextures[2] = new ResourceLocation(InvincibleHamster.MODID, InvincibleHamster.filePathTextures + "hamsterDarkBrown.png");
        untamedTextures[3] = new ResourceLocation(InvincibleHamster.MODID, InvincibleHamster.filePathTextures + "hamsterLightBrown.png");

        tamedTextures[0] = new ResourceLocation(InvincibleHamster.MODID, InvincibleHamster.filePathTextures + "hamsterOrange_tame.png");
        tamedTextures[1] = new ResourceLocation(InvincibleHamster.MODID, InvincibleHamster.filePathTextures + "hamsterBrown_tame.png");
        tamedTextures[2] = new ResourceLocation(InvincibleHamster.MODID, InvincibleHamster.filePathTextures + "hamsterDarkBrown_tame.png");
        tamedTextures[3] = new ResourceLocation(InvincibleHamster.MODID, InvincibleHamster.filePathTextures + "hamsterLightBrown_tame.png");
    }

    protected void preRenderScale(EntityInvincibleHamster entityInvincibleHamster, float f)
    {
        GL11.glScalef(scale, scale, scale);
    }

    @Override
    protected void preRenderCallback(EntityLivingBase entityliving, float f)
    {
        super.preRenderCallback(entityliving, f);
        preRenderScale((EntityInvincibleHamster) entityliving, f);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity var1)
    {
        if (var1 instanceof EntityInvincibleHamster)
        {
            EntityInvincibleHamster hamster = (EntityInvincibleHamster) var1;

            ResourceLocation[] array = hamster.isTamed() ? tamedTextures : untamedTextures;

            int index = hamster.getHamsterColor();
            if (index < 0 || index > array.length)
            {
                return array[0];
            }

            return array[index];
        }

        return null;
    }
}
