package ivorius.invinciblehamster.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import ivorius.invinciblehamster.IHProxy;
import ivorius.invinciblehamster.entities.EntityInvincibleHamster;
import ivorius.invinciblehamster.client.rendering.RenderInvincibleHamster;

public class ClientProxy implements IHProxy
{
    @Override
    public void registerRenderers()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityInvincibleHamster.class, new RenderInvincibleHamster());
    }
}
