package firis.lmavatar.client.proxy;

import firis.lmavatar.common.manager.PlayerModelManager;
import firis.lmavatar.common.proxy.IProxy;
import firis.lmlib.api.LMLibraryAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

/**
 * クライアントサイドProxy
 * @author firis-games
 *
 */
public class ClientProxy implements IProxy {

	/**
	 * クライアントプレイヤーを取得する
	 */
	@Override
	public EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().player;
	}
	
	/**
	 * テクスチャ設定用GUIを表示する
	 */
	@Override
	public void openGuiTextureSelect() {
		
		//テクスチャ選択画面表示
		LMLibraryAPI.instance().openGuiTextureSelect(null, 
				PlayerModelManager.getModelConfigCompound(getClientPlayer()), 
				"LittleMaidAvatar Texture Select");
		
	}
}
