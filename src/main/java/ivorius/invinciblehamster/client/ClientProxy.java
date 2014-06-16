package ivorius.invinciblehamster.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import ivorius.invinciblehamster.CommonProxy;
import ivorius.invinciblehamster.EntityInvincibleHamster;
import ivorius.invinciblehamster.RenderInvincibleHamster;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerRenderers()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityInvincibleHamster.class, new RenderInvincibleHamster());
    }
}
