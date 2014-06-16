package net.ivorius.invinciblehamster.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.ivorius.invinciblehamster.CommonProxy;
import net.ivorius.invinciblehamster.EntityInvincibleHamster;
import net.ivorius.invinciblehamster.RenderInvincibleHamster;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerRenderers()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityInvincibleHamster.class, new RenderInvincibleHamster());
    }
}
