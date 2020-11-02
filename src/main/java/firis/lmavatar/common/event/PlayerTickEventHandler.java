package firis.lmavatar.common.event;

import java.lang.reflect.Method;

import firis.lmavatar.common.manager.PlayerModelManager;
import firis.lmavatar.common.modelcaps.PlayerModelCompound;
import firis.lmavatar.config.FirisConfig;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

@EventBusSubscriber
public class PlayerTickEventHandler {

	// サイズ変更メソッド
	private static Method methodSetSize = ReflectionHelper.findMethod(Entity.class, "setSize", "func_70105_a",
			float.class, float.class);

	@SubscribeEvent
	public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {

		if (event.phase != Phase.END) return;
		
		//設定が無効な場合は何もしない
		if (!FirisConfig.cfg_lmavatar_adjust_size) return;

		PlayerModelCompound playerModel = PlayerModelManager.getModelConfigCompound(event.player);

		// 通常モデルの場合は何もしない
		if (!playerModel.getEnableLMAvatar()) {
			// 目線の高さを戻す
			event.player.eyeHeight = event.player.getDefaultEyeHeight();
			return;
		}

		// 各種パラメータ取得
		float width = playerModel.getPlayerWidth();
		float height = playerModel.getPlayerHeight();
		float eyeHeight = playerModel.getPlayerEyeHeight();
		
		//　プレイヤーの状態でサイズを制御
		if (event.player.isElytraFlying()) {
			width = 0.6F;
			height = 0.6F;
		} else if (event.player.isPlayerSleeping()) {
			width = 0.2F;
			height = 0.2F;
		} else if (event.player.isSneaking()) {
			height -= 0.15F;
		}

		// サイズ変更
		try {
			methodSetSize.invoke(event.player, width, height);
			event.player.eyeHeight = eyeHeight;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
