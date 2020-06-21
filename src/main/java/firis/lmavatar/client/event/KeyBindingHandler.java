package firis.lmavatar.client.event;

import org.lwjgl.input.Keyboard;

import firis.lmavatar.LittleMaidAvatar;
import firis.lmavatar.common.manager.PlayerModelManager;
import firis.lmavatar.common.manager.SyncPlayerModelClient;
import firis.lmavatar.common.modelcaps.PlayerModelConfigCompound;
import firis.lmavatar.config.FirisConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Property;
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
	 * キーバインド初期化
	 */
	public static void init() {
		ClientRegistry.registerKeyBinding(keyLittleMaidAvatarAction);
		ClientRegistry.registerKeyBinding(keyLittleMaidAvatarChange);
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
			PlayerModelConfigCompound lmAvatar = PlayerModelManager.getModelConfigCompound(player);
			lmAvatar.setLMAvatarAction(true);
			
			//同期する
			SyncPlayerModelClient.syncModel();
			
		} else if (keyLittleMaidAvatarChange.isKeyDown()) {
			//GuiConfigの変更
			Property propEnableLMAvatar = FirisConfig.config.get(FirisConfig.CATEGORY_AVATAR, "07.EnableLMAvatar", FirisConfig.cfg_enable_lmavatar);
			propEnableLMAvatar.set(!FirisConfig.cfg_enable_lmavatar);
			FirisConfig.syncConfig();
		}
	}
	
}
