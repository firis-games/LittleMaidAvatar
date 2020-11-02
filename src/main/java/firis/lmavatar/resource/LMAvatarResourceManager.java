package firis.lmavatar.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import firis.lmavatar.common.manager.SyncPlayerModelClient;
import firis.lmavatar.common.modelcaps.PlayerModelCompound;
import firis.lmavatar.resource.format.JsonFormatConfigLMAvatar;
import firis.lmavatar.resource.format.JsonFormatConfigLMAvatarMotion;
import firis.lmavatar.resource.format.JsonFormatConfigLMAvatarPlayerCache;
import firis.lmavatar.resource.format.JsonFormatLMAvatarModel;
import firis.lmlib.api.LMLibraryAPI;
import firis.lmlib.common.helper.ResourceFileHelper;
import net.minecraft.nbt.NBTTagCompound;

/**
 * カスタム設定取得
 * @author firis-games
 *
 */
public class LMAvatarResourceManager {

	/**
	 * LMアバターの初期値とリストのConfig
	 */
	private static String config_lmavatar = "lma_lmavatar.json";
	
	private static String config_motion_key = "lma_motion_key.json";
	
	private static String config_cache_player_model = "lma_cache_player_model.json";
	
	/**
	 * LMアバターの基本設定
	 */
	protected static JsonFormatConfigLMAvatar configLMAvatar = null;
	
	/**
	 * モーションキーの設定
	 */
	protected static JsonFormatConfigLMAvatarMotion configMotionKey = null;
	
	/**
	 * 実表示用のプレイヤーキャッシュ
	 */
	protected static JsonFormatConfigLMAvatarPlayerCache configPlayerCache = null;
	
	/**
	 * 独自設定ファイルの初期化
	 */
	public static void preInit() {
		
		//LMアバターの初期値とリストを取得
		configLMAvatar = ResourceFileHelper.readFromJson(config_lmavatar, JsonFormatConfigLMAvatar.class);
		if (configLMAvatar == null) {
			configLMAvatar = new JsonFormatConfigLMAvatar();
			ResourceFileHelper.writeToJson(config_lmavatar, configLMAvatar);
		}
		
	}
	
	/**
	 * モーションキー設定の初期化
	 */
	public static void init() {
		
		//モーションキー初期化
		configMotionKey = ResourceFileHelper.readFromJson(config_motion_key, JsonFormatConfigLMAvatarMotion.class);
		if (configMotionKey == null) {
			configMotionKey = new JsonFormatConfigLMAvatarMotion();
		}
		
		//モーションIDは常に再出力する
		configMotionKey.motionList = new ArrayList<>();
		for (String motion : LMLibraryAPI.instance().getLMMotionIdList()) {
			configMotionKey.motionList.add(motion);
		}
		
		//キー設定の生成
		if (configMotionKey.motionKeyMap == null || configMotionKey.motionKeyMap.size() == 0) {
			configMotionKey.motionKeyMap = new TreeMap<>();
			
			//キー登録
			for (Integer i = 1; i <=9; i++) {
				String value = "";
				if (configMotionKey.motionList.size() >= i) {
					value = configMotionKey.motionList.get(i - 1);
				}
				configMotionKey.motionKeyMap.put("Key" + i.toString(), value);
			}
		}
		ResourceFileHelper.writeToJson(config_motion_key, configMotionKey);
		
		//プレイヤーキャッシュの取得
		configPlayerCache = ResourceFileHelper.readFromJson(config_cache_player_model, JsonFormatConfigLMAvatarPlayerCache.class);
		if (configPlayerCache == null) {
			configPlayerCache = new JsonFormatConfigLMAvatarPlayerCache();
		}
		ResourceFileHelper.writeToJson(config_cache_player_model, configPlayerCache);
		
		//プレイヤーキャッシュからPlayerModelManagerのデータを生成する
		for (String key : configPlayerCache.lmAvatarPlayerCacheMap.keySet()) {
			JsonFormatLMAvatarModel model = configPlayerCache.lmAvatarPlayerCacheMap.get(key);
			NBTTagCompound nbt = model.serializeToNBT(new NBTTagCompound());
			
			//初期化したPlayerModelを設定
			PlayerModelCompound playerModel = new PlayerModelCompound(null, null);
			playerModel.deserializeFromNBT(nbt);
			
			//キャッシュを登録
			SyncPlayerModelClient.instance.initPlayerModelNBTTagCompound(key, nbt);
		}
	}
	
	/**
	 * KeyIdからモーションIDを取得する
	 * @param keyId
	 * @return
	 */
	public static String getMotionKey(String keyId) {
		if (!configMotionKey.motionKeyMap.containsKey(keyId)) return "";
		return configMotionKey.motionKeyMap.get(keyId);
	}
	
	/**
	 * LMアバターリストの存在チェック
	 * @param name
	 * @return
	 */
	public static boolean isLMAvatarList(String name) {
		return configLMAvatar.lmAvatarMap.containsKey(name);
	}
	/**
	 * LMアバターを保存する
	 */
	public static void savaLMAvatarList(String name, NBTTagCompound nbt) {
		JsonFormatLMAvatarModel avatar = new JsonFormatLMAvatarModel();
		avatar.deserializeFromNBT(nbt);
		configLMAvatar.lmAvatarMap.put(name, avatar);
		
		//書き込み実行
		ResourceFileHelper.writeToJson(config_lmavatar, configLMAvatar);
	}
	
	/**
	 * LMアバターをロードする
	 * @param name
	 * @return
	 */
	public static NBTTagCompound loadLMAvatarList(String name) {
		return configLMAvatar.lmAvatarMap.get(name).serializeToNBT(new NBTTagCompound());
	}
	
	/**
	 * LMアバターリストを取得する
	 * @return
	 */
	public static List<String> getListLMAvatarList() {
		return configLMAvatar.lmAvatarMap.keySet().stream().collect(Collectors.toList());
	}
	
	/**
	 * プレイヤーモデルキャッシュを保存する
	 */
	public static void savePlayerCache(Map<String, NBTTagCompound> cache) {
		
		//上書き
		for (String key : cache.keySet()) {
			JsonFormatLMAvatarModel model = new JsonFormatLMAvatarModel();
			model.deserializeFromNBT(cache.get(key));
			configPlayerCache.lmAvatarPlayerCacheMap.put(key, model);
		}
		
		//書き込み実行
		ResourceFileHelper.writeToJson(config_cache_player_model, configPlayerCache);
	}
}
