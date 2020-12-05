package firis.lmavatar;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import firis.lmavatar.client.event.KeyBindingHandler;
import firis.lmavatar.client.event.LittleMaidAvatarClientTickEventHandler;
import firis.lmavatar.client.renderer.RendererLMAvatar;
import firis.lmavatar.common.command.LMAvatarCommand;
import firis.lmavatar.common.item.LMItemPlayerMaidBook;
import firis.lmavatar.common.manager.SyncPlayerModelClient;
import firis.lmavatar.common.manager.SyncPlayerModelServer;
import firis.lmavatar.common.modelmotion.LMMotionCarryOn;
import firis.lmavatar.common.modelmotion.LMMotionSwimming;
import firis.lmavatar.common.network.LMAvatarNetwork;
import firis.lmavatar.common.proxy.IProxy;
import firis.lmavatar.config.FirisConfig;
import firis.lmavatar.resource.LMAvatarResourceManager;
import firis.lmlib.api.LMLibraryAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(
		modid = LittleMaidAvatar.MODID, 
		name = LittleMaidAvatar.NAME,
		version = LittleMaidAvatar.VERSION,
		dependencies = LittleMaidAvatar.MOD_DEPENDENCIES,
		acceptedMinecraftVersions = LittleMaidAvatar.MOD_ACCEPTED_MINECRAFT_VERSIONS
)
@EventBusSubscriber(modid=LittleMaidAvatar.MODID)
public class LittleMaidAvatar {

    public static final String MODID = "lmavatar";
    public static final String NAME = "LittleMaidAvatar";
    public static final String VERSION = "1.2.1";
    public static final String MOD_DEPENDENCIES = "required-after:forge@[1.12.2-14.23.5.2768,);"
    		+ "required-after:lmlibrary@[1.1.3,);";
    public static final String MOD_ACCEPTED_MINECRAFT_VERSIONS = "[1.12.2]";
    
    @Instance(MODID)
	public static LittleMaidAvatar instance;
    
    /** logger */
    public static Logger logger = LogManager.getLogger(MODID);
    
    /** proxy */
    @SidedProxy(serverSide = "firis.lmavatar.common.proxy.CommonProxy", 
    		clientSide = "firis.lmavatar.client.proxy.ClientProxy")
	public static IProxy proxy;
	
	/**
     * アイテムインスタンス保持用
     */
    @ObjectHolder(MODID)
    public static class LMAItems {
    	public final static Item PLAYER_MAID_BOOK = null;
    }
    
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	
		//設定読込
        FirisConfig.init(event.getModConfigurationDirectory());
        
        //独自設定初期化
        LMAvatarResourceManager.preInit();
        
		//追加モーション設定
		LMLibraryAPI.instance().registerLittleMaidMotion(new LMMotionCarryOn());
		LMLibraryAPI.instance().registerLittleMaidMotion(new LMMotionSwimming());
    	
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	
    	//LMアバター同期用
    	if (FirisConfig.cfg_lmavatar_multi_sync) {
    		MinecraftForge.EVENT_BUS.register(SyncPlayerModelServer.instance);
    	}
    	
    	//Renderer差し替え
    	if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
    		registerClient();
    	}
    	
    	//ネットワーク登録
    	LMAvatarNetwork.init();
    	
    	//独自設定初期化
        LMAvatarResourceManager.init();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {}
    
    
    /**
     * サーバーサイドの登録処理
     * @param event
     */
	@EventHandler
	public void serverStatingEvent(FMLServerStartingEvent event) {
		
		//コマンド登録
		event.registerServerCommand(new LMAvatarCommand());
		
	}
	
    /**
     * Renderer登録
     */
    public void registerClient() {
    	
    	//Playerのスキン差し替え
		//Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().skinMap;
		Map<String, RenderPlayer> skinMap = ObfuscationReflectionHelper.getPrivateValue(RenderManager.class, 
				Minecraft.getMinecraft().getRenderManager(), 
				new String[] { "skinMap", "field_178636_l" });
		
		RenderPlayer renderPlayerDefault = skinMap.get("default");
		RenderPlayer renderPlayerSlim = skinMap.get("slim");
		//RendererMaidPlayer renderMaidPlayer = new RendererMaidPlayer(renderPlayer);
		RendererLMAvatar renderMaidPlayerDefault = new RendererLMAvatar(renderPlayerDefault);
		RendererLMAvatar renderMaidPlayerSlim = new RendererLMAvatar(renderPlayerSlim);
		
		
		//Minecraft.getMinecraft().getRenderManager().playerRenderer = renderMaidPlayer;
		ObfuscationReflectionHelper.setPrivateValue(RenderManager.class, 
				Minecraft.getMinecraft().getRenderManager(),
				renderMaidPlayerDefault, 
				new String[] { "playerRenderer", "field_178637_m" });
		
		skinMap.put("default", renderMaidPlayerDefault);
		skinMap.put("slim", renderMaidPlayerSlim);
				
		KeyBindingHandler.init();
		
		MinecraftForge.EVENT_BUS.register(KeyBindingHandler.class);
		MinecraftForge.EVENT_BUS.register(LittleMaidAvatarClientTickEventHandler.class);
		
		//LMアバター管理用イベント登録
		if (FirisConfig.cfg_lmavatar_multi_sync) {
			MinecraftForge.EVENT_BUS.register(SyncPlayerModelClient.instance);
		}
    }
    
    /**
     * アイテム登録
     * @param event
     */
    @SubscribeEvent
    protected static void registerItems(RegistryEvent.Register<Item> event) {
    	
    	//メイドさんになる本
    	event.getRegistry().register(new LMItemPlayerMaidBook()
    			.setRegistryName(LittleMaidAvatar.MODID, "player_maid_book")
    			.setUnlocalizedName("player_maid_book"));
    	
    }
    
    /**
     * モデル登録
     * @param event
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    protected static void registerModels(ModelRegistryEvent event) {
    	
    	// メイドさんになる本
		ModelLoader.setCustomModelResourceLocation(LMAItems.PLAYER_MAID_BOOK, 0,
				new ModelResourceLocation(LMAItems.PLAYER_MAID_BOOK.getRegistryName(), "inventory"));
		
    }
	
}
