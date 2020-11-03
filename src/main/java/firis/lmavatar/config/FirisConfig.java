package firis.lmavatar.config;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import firis.lmavatar.LittleMaidAvatar;
import net.minecraftforge.common.config.Configuration;

public class FirisConfig {

	public static Configuration config;
	
	public static String CATEGORY_GENERAL = "General";
	
	/** LittleMaidAvatarに登録するLayer設定 */
	public static List<String> cfg_lmavatar_include_layer = null;
	
	/** 初回ログイン時のアイテム追加機能設定 */
	public static boolean cfg_lmavatar_first_login_add_inventory_item = true;
	
	/** LMアバターのサイズをマルチモデルにアジャストする */
	public static boolean cfg_lmavatar_adjust_size = false;
	
	/** LMアバターのサイズをアジャストする際のサイズ倍率を指定する */
	public static float cfg_lmavatar_adjust_size_scale = 1.0F;
	
	/** LMアバターの待機モーションへの変更待ち時間 */
	public static int cfg_lmavatar_wait_motion_time = 100;
	
	/** マルチ環境の同期処理を有効化する */
	public static boolean cfg_lmavatar_multi_sync = false;
	
	/** マルチ環境のアクションの同期処理を有効化する */
	public static boolean cfg_lmavatar_multi_sync_action = false;
	
	public static void init(File configDir) {
		
		File configFile = new File(configDir, "LittleMaidAvatar.cfg");
		
		config = new Configuration(configFile, LittleMaidAvatar.VERSION, true);
		
		//カテゴリーコメントの設定
		initCategory();
		
		//設定値の同期
		syncConfig();
		
	}
	
	/**
	 * カテゴリーのコメント設定
	 */
	protected static void initCategory() {
		config.addCustomCategoryComment(CATEGORY_GENERAL, "メイドさんの姿になる機能");
	}
	
	/**
	 * Config値の同期処理
	 */
	protected static void syncConfig() {
		
		//General
		//メイドアバター機能
		//指定されたIDのLayerは登録する
		String[] lma_include_layer = new String[] {"LayerSlashBlade"};
		cfg_lmavatar_include_layer = Arrays.asList(config.getStringList("LittleMaidAvatar.IncludeLayer", CATEGORY_GENERAL, lma_include_layer, 
				"指定された文字を含むLayerクラスをLittleMaidAvatarに追加します。"));
		
		//初回ログイン時にインヴェントリにアイテム追加
		cfg_lmavatar_first_login_add_inventory_item = config.getBoolean("LittleMaidAvatar.FirstLoginAddInventoryItem", CATEGORY_GENERAL, true, 
				"初回ログイン時にプレイヤーインベントリへアイテムを追加します。");
		
		//プレイヤーモデルの当たり判定調整
		cfg_lmavatar_adjust_size = config.getBoolean("LittleMaidAvatarDev.AdjustSize", CATEGORY_GENERAL, false, 
				"LMアバター適応時にプレイヤーのサイズをマルチモデルのサイズに調整します。");
		
		//cfg_lmavatar_adjust_sizeが有効な際の倍率設定
		cfg_lmavatar_adjust_size_scale = config.getFloat("LittleMaidAvatarDev.AdjustSizeScale", CATEGORY_GENERAL, 1.0F, 0.1F, 2.0F, 
				"マルチモデルのサイズに調整機能が有効な場合にプレイヤーサイズの全体倍率を指定します。");
		
		//LMアバターの待機モーションへの変更待ち時間
		cfg_lmavatar_wait_motion_time = config.getInt("LittleMaidAvatar.WaitMotionTickTime", CATEGORY_GENERAL, 100, 0, 10000, 
				"LMアバターの待機モーションへの変更待ち時間をtikcで設定します。");
		
		//マルチ環境の同期処理を有効化する
		cfg_lmavatar_multi_sync = config.getBoolean("LittleMaidAvatarMulti.SyncModel", CATEGORY_GENERAL, false, 
				"マルチ環境でLMアバターモデルの同期を有効化する。");
		
		//マルチ環境の同期処理を有効化する
		cfg_lmavatar_multi_sync_action = config.getBoolean("LittleMaidAvatarMulti.SyncAction", CATEGORY_GENERAL, false, 
				"マルチ環境でLMアバターモデルのアクションの同期を有効化する。モデルの同期を無効化している場合は自動で無効化される。");
		
		config.save();
	}
	
}
