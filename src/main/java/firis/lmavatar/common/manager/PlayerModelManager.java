package firis.lmavatar.common.manager;

import java.util.HashMap;
import java.util.Map;

import firis.lmavatar.common.modelcaps.PlayerModelCompound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * LMアバターの管理クラス
 * @author firis-games
 * 
 * LMアバターを描画時にクライアント側の処理より参照される
 * SyncPlayerModelClientのcacheをベースにPlayerModelCompoundを生成する
 *
 */
public class PlayerModelManager {

	/**
	 * プレイヤーモデルの一覧
	 */
	private static Map<String, PlayerModelCompound> playerModelCompoundMap = new HashMap<>();
	
	/**
	 * EntityPlayerに紐づくModelConfigCompoundを取得する
	 * @return
	 */
	public static PlayerModelCompound getModelConfigCompound(EntityPlayer player) {
		
		String key = player.getName();
		
		//対象の名前が存在していなければ初期化
		if (!playerModelCompoundMap.containsKey(key)) {
			createModelConfigCompound(player);
		}
		
		//PlayerModel取得
		PlayerModelCompound modelConfig = playerModelCompoundMap.get(key);
		modelConfig.setPlayer(player);
		
		return modelConfig; 
	}
	
	/**
	 * キャッシュからPlayerModelCompoundへ反映する
	 * @param playerName
	 * @param nbt
	 */
	public static void setModelConfigCompound(String playerName, NBTTagCompound nbt) {
		if (playerModelCompoundMap.containsKey(playerName)) {
			playerModelCompoundMap.get(playerName).readFromNBT(nbt);
		}
	}
	
	/**
	 * キャッシュ情報からPlayerModelCompoundを生成する
	 */
	private static void createModelConfigCompound(EntityPlayer player) {
		
		String playerName = player.getName();
		
		NBTTagCompound nbt = SyncPlayerModelClient.instance.getPlayerModelNbt(playerName);
		
		PlayerModelCompound playerModelConfig = new PlayerModelCompound(null, null);
		playerModelConfig.readFromNBT(nbt);
		
		//セット
		playerModelCompoundMap.put(playerName, playerModelConfig);
	}
	
}
