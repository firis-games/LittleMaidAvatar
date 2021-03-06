package firis.lmavatar.client.event;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import firis.lmavatar.LittleMaidAvatar;
import firis.lmavatar.common.manager.PlayerModelManager;
import firis.lmavatar.common.modelcaps.PlayerModelCompound;
import firis.lmavatar.resource.LMAvatarResourceManager;
import firis.lmlib.api.LMLibraryAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * キーボードイベント
 * @author computer
 *
 */
@SideOnly(Side.CLIENT)
public class KeyBindingHandler {

	/**
	 * Avatarアクション用
	 * defalut:＠キー
	 */
	public static final KeyBinding keyLittleMaidAvatarAction = new KeyBinding("key.lmavatar.action", Keyboard.KEY_GRAVE, LittleMaidAvatar.NAME);
	
	public static final KeyBinding keyLittleMaidAvatarChange = new KeyBinding("key.lmavatar.change", Keyboard.KEY_SEMICOLON, LittleMaidAvatar.NAME);

	/**
	 * 各種モーション割り当て用
	 */
	public static final KeyBinding keyLittleMaidAvatarAction1 = new KeyBinding("key.lmavatar.action1", Keyboard.KEY_NUMPAD1, LittleMaidAvatar.NAME);
	public static final KeyBinding keyLittleMaidAvatarAction2 = new KeyBinding("key.lmavatar.action2", Keyboard.KEY_NUMPAD2, LittleMaidAvatar.NAME);
	public static final KeyBinding keyLittleMaidAvatarAction3 = new KeyBinding("key.lmavatar.action3", Keyboard.KEY_NUMPAD3, LittleMaidAvatar.NAME);
	public static final KeyBinding keyLittleMaidAvatarAction4 = new KeyBinding("key.lmavatar.action4", Keyboard.KEY_NUMPAD4, LittleMaidAvatar.NAME);
	public static final KeyBinding keyLittleMaidAvatarAction5 = new KeyBinding("key.lmavatar.action5", Keyboard.KEY_NUMPAD5, LittleMaidAvatar.NAME);
	public static final KeyBinding keyLittleMaidAvatarAction6 = new KeyBinding("key.lmavatar.action6", Keyboard.KEY_NUMPAD6, LittleMaidAvatar.NAME);
	public static final KeyBinding keyLittleMaidAvatarAction7 = new KeyBinding("key.lmavatar.action7", Keyboard.KEY_NUMPAD7, LittleMaidAvatar.NAME);
	public static final KeyBinding keyLittleMaidAvatarAction8 = new KeyBinding("key.lmavatar.action8", Keyboard.KEY_NUMPAD8, LittleMaidAvatar.NAME);
	public static final KeyBinding keyLittleMaidAvatarAction9 = new KeyBinding("key.lmavatar.action9", Keyboard.KEY_NUMPAD9, LittleMaidAvatar.NAME);
	
	/**
	 * 各種モーション割り当て用
	 */
	protected static Map<KeyBinding, String> keyLittleMaidAvatarActionList = new HashMap<>();
	
	/**
	 * キーバインド初期化
	 */
	public static void init() {
		ClientRegistry.registerKeyBinding(keyLittleMaidAvatarAction);
		ClientRegistry.registerKeyBinding(keyLittleMaidAvatarChange);
		
		ClientRegistry.registerKeyBinding(keyLittleMaidAvatarAction1);
		ClientRegistry.registerKeyBinding(keyLittleMaidAvatarAction2);
		ClientRegistry.registerKeyBinding(keyLittleMaidAvatarAction3);
		ClientRegistry.registerKeyBinding(keyLittleMaidAvatarAction4);
		ClientRegistry.registerKeyBinding(keyLittleMaidAvatarAction5);
		ClientRegistry.registerKeyBinding(keyLittleMaidAvatarAction6);
		ClientRegistry.registerKeyBinding(keyLittleMaidAvatarAction7);
		ClientRegistry.registerKeyBinding(keyLittleMaidAvatarAction8);
		ClientRegistry.registerKeyBinding(keyLittleMaidAvatarAction9);
		
		//キーリスト
		keyLittleMaidAvatarActionList.put(keyLittleMaidAvatarAction1, "Key1");
		keyLittleMaidAvatarActionList.put(keyLittleMaidAvatarAction2, "Key2");
		keyLittleMaidAvatarActionList.put(keyLittleMaidAvatarAction3, "Key3");
		keyLittleMaidAvatarActionList.put(keyLittleMaidAvatarAction4, "Key4");
		keyLittleMaidAvatarActionList.put(keyLittleMaidAvatarAction5, "Key5");
		keyLittleMaidAvatarActionList.put(keyLittleMaidAvatarAction6, "Key6");
		keyLittleMaidAvatarActionList.put(keyLittleMaidAvatarAction7, "Key7");
		keyLittleMaidAvatarActionList.put(keyLittleMaidAvatarAction8, "Key8");
		keyLittleMaidAvatarActionList.put(keyLittleMaidAvatarAction9, "Key9");
		
	}
	
	/**
	 * キー入力イベント
	 * @param event
	 */
	@SubscribeEvent
	public static void onKeyInputEvent(KeyInputEvent event) {
	
		if (keyLittleMaidAvatarAction.isKeyDown()) {
			//アクションの制御はすべてClient側で行う
			EntityPlayer player = Minecraft.getMinecraft().player;
			PlayerModelCompound lmAvatar = PlayerModelManager.getModelConfigCompound(player);
			lmAvatar.setLMAvatarAction(1);
			
			//同期する
			lmAvatar.syncPlayerModelCacheWithAction();
			
		} else if (keyLittleMaidAvatarChange.isKeyDown()) {
			//LMアバターの有効無効を変更
			PlayerModelCompound playerModel = PlayerModelManager.getModelConfigCompound(Minecraft.getMinecraft().player);
			playerModel.setEnableLMAvatar(!playerModel.getEnableLMAvatar());
			//キャッシュへ反映
			playerModel.syncPlayerModelCache();

		}
		
		//連番アクション
		for (KeyBinding keyAction : keyLittleMaidAvatarActionList.keySet()) {
			if (keyAction.isKeyDown()) {
				//アクションの制御はすべてClient側で行う
				PlayerModelCompound playerModel = PlayerModelManager.getModelConfigCompound(Minecraft.getMinecraft().player);
				String keyId = LMAvatarResourceManager.getMotionKey(keyLittleMaidAvatarActionList.get(keyAction));
				playerModel.setLMAvatarAction(LMLibraryAPI.instance().getLMMotionIndex(keyId));
				//同期する
				playerModel.syncPlayerModelCacheWithAction();
				break;
			}
		}
	}
	
}
