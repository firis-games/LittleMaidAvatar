package firis.lmavatar.resource.format;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

/**
 * LMアバターのプレイヤーすべての情報のキャッシュをもつ
 * @author firis-games
 *
 */
public class JsonFormatConfigLMAvatarPlayerCache {
	
	/***
	 * LMアバターのすべてのプレイヤーのキャッシュを保持する
	 */
	@SerializedName("PlayerCache")
	public Map<String, JsonFormatLMAvatarModel> lmAvatarPlayerCacheMap = new HashMap<>();
	
}
