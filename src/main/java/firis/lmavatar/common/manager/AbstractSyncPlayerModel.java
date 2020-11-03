package firis.lmavatar.common.manager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;

/**
 * LMアバターモデル同期用ベースクラス
 * @author firis-games
 * 
 * EntityPlayerインスタンスはワールド移動などで再生成されるためプレイヤー名で管理する
 *
 */
public abstract class AbstractSyncPlayerModel {
	
	
	protected Set<String> syncPacketQueue = new HashSet<>();
	
	/**
	 * サーバーとクライアント側でそれぞれ設定を持つ
	 */
	protected Map<String, NBTTagCompound> modelNbtMap = new HashMap<>();
		
	/**
	 * キャッシュから読み込んだNBTをセットする
	 * @param name
	 * @param modelCompound
	 */
	public void setPlayerModelNBTTagCompound(String name, NBTTagCompound modelCompound) {
		modelNbtMap.put(name, modelCompound);
	}
	
	/**
	 * プレイヤーモデルのNBTを取得する
	 * @param playerName
	 * @return
	 */
	protected NBTTagCompound getPlayerModelNbt(String playerName) {
		//存在しない場合
		if (!this.modelNbtMap.containsKey(playerName)) {
			//新規で追加
			this.setPlayerModelNBTTagCompound(playerName, this.createNewPlayerModelNBT());
		}
		return this.modelNbtMap.get(playerName);
	}
	
	/**
	 * NBTの初期値を生成する
	 * @return
	 */
	protected NBTTagCompound createNewPlayerModelNBT() {
		
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("maid", "default_Orign");
		nbt.setInteger("color", 0);
		nbt.setString("head", "default_Orign");
		nbt.setString("chest", "default_Orign");
		nbt.setString("legs", "default_Orign");
		nbt.setString("feet", "default_Orign");
		nbt.setBoolean("contract", true);
		nbt.setBoolean("enable", true);
		nbt.setInteger("action", 0);
		nbt.setBoolean("wait", false);
		
		return nbt;
	}	
}
