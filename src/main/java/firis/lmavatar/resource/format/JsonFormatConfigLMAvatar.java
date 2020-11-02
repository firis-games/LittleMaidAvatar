package firis.lmavatar.resource.format;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

/**
 * LMアバター独自設定のフォーマット
 * @author firis-games
 *
 */
public class JsonFormatConfigLMAvatar {

	/**
	 * プレイヤーのデフォルトモデル設定
	 */
	@SerializedName("PlayerDefaultModel")
	public JsonFormatLMAvatarModel defaultModel = new JsonFormatLMAvatarModel();
	
	/***
	 * LMアバターのリスト
	 * 
	 * default:初期値
	 * xxxx:コマンド機能で保存したデータ
	 */
	@SerializedName("LittleMaidAvatarList")
	public Map<String, JsonFormatLMAvatarModel> lmAvatarMap = new HashMap<>();
	
}
