package ivorius.invinciblehamster;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import ivorius.invinciblehamster.entities.EntityInvincibleHamster;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = InvincibleHamster.MODID, version = InvincibleHamster.VERSION)
public class InvincibleHamster
{
    public static final String MODID = "invinciblehamster";
    public static final String VERSION = "1.4";

    @Instance(value = MODID)
    public static InvincibleHamster instance;

    @SidedProxy(clientSide = "ivorius.invinciblehamster.client.ClientProxy", serverSide = "ivorius.invinciblehamster.server.ServerProxy")
    public static IHProxy proxy;

    public static String filePathTextures = "textures/mod/";
    public static String textureBase = "invinciblehamster:";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
//        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
//
//        config.load();
//
//        config.save();

        int invincibleHamsterID = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(EntityInvincibleHamster.class, "invincibleHamster", invincibleHamsterID, 0xfefdfc, 0xff6811);
        EntityRegistry.addSpawn("invincibleHamster", 8, 4, 4, EnumCreatureType.creature, BiomeGenBase.forest, BiomeGenBase.plains);
    }

    @EventHandler
    public void load(FMLInitializationEvent event)
    {
        proxy.registerRenderers();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {

    }
}