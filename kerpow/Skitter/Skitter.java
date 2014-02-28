package kerpow.Skitter;

import java.util.logging.Level;

import kerpow.Skitter.Blocks.BlockSkitterWeb;
import kerpow.Skitter.Blocks.BlockSkitteringPlague;
import kerpow.Skitter.Entities.EntitySkitterQueen;
import kerpow.Skitter.Entities.EntitySkitterWarrior;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "SkitterModID", name = "Skitter", version = "0.0.0")
@NetworkMod(clientSideRequired = true)
public class Skitter {

	public static int rendererSkitterPlague  = -1;
	public static int rendererSkitterWeb  = -1;
	
	public static Block TrampledBlock = Block.netherrack;

	public static Block blockSkitterWeb = (new BlockSkitterWeb(2000)).setUnlocalizedName("skitterWeb");
	public static Block blockSkitterPlague = (new BlockSkitteringPlague(2001)).setUnlocalizedName("skitterPlague");

	// The instance of your mod that Forge uses.
	@Instance(value = "SkitterModID")
	public static Skitter instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "kerpow.Skitter.Client.ClientProxy", serverSide = "kerpow.Skitter.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	// used in 1.6.2
	// @PreInit // used in 1.5.2
	public void preInit(FMLPreInitializationEvent event) {
		// Stub Method
	}

	@EventHandler
	// used in 1.6.2
	// @Init // used in 1.5.2
	public void load(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new SkitterEventHandler());

		GameRegistry.registerBlock(blockSkitterWeb, "skitterWeb");
		LanguageRegistry.instance().addStringLocalization("tile.skitterWeb.name", "en_US", "Skittering Web");
		GameRegistry.registerBlock(blockSkitterPlague, "skitterPlague");
		LanguageRegistry.instance().addStringLocalization("tile.skitterPlague.name", "en_US", "Skittering Plague");

		int id = 0;

		EntityRegistry.registerModEntity(EntitySkitterQueen.class, "SkitterQueen", id, this, 80, 1, true);
		EntityList.addMapping(EntitySkitterQueen.class, "SkitterQueen", id, 14342901, 8026845);
		LanguageRegistry.instance().addStringLocalization("entity.SkitterQueen.name", "en_US", "Skitter Queen");
		id++;

		EntityRegistry.registerModEntity(EntitySkitterWarrior.class, "SkitterWarrior", id, this, 80, 1, true);
		EntityList.addMapping(EntitySkitterWarrior.class, "SkitterWarrior", id, 14342901, 8026845);
		LanguageRegistry.instance().addStringLocalization("entity.SkitterWarrior.name", "en_US", "Skitter Warrior");
		id++;

		EntityRegistry.addSpawn(EntitySkitterQueen.class, 0, 0, 1, EnumCreatureType.monster, BiomeGenBase.beach,
				BiomeGenBase.extremeHills, BiomeGenBase.extremeHillsEdge, BiomeGenBase.forest,
				BiomeGenBase.forestHills, BiomeGenBase.jungle, BiomeGenBase.jungleHills, BiomeGenBase.mushroomIsland,
				BiomeGenBase.mushroomIslandShore, BiomeGenBase.ocean, BiomeGenBase.plains, BiomeGenBase.river,
				BiomeGenBase.swampland);

		
		
		proxy.registerRenderThings();
		proxy.registerSound();
	}

	public static void l(String msg) {
		FMLLog.log(Level.INFO, msg);

	}

	@EventHandler
	// used in 1.6.2
	// @PostInit // used in 1.5.2
	public void postInit(FMLPostInitializationEvent event) {
		// Stub Method
	}

}