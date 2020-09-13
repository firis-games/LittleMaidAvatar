package firis.lmavatar.config;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import firis.lmavatar.LittleMaidAvatar;
import firis.lmavatar.common.manager.PlayerModelManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class FirisConfig {

	public static Configuration config;
	
	public static String CATEGORY_GENERAL = "General";
	public static String CATEGORY_AVATAR = "PlayerMaidAvatar";
	public static String CATEGORY_MOTION_KEY = "MotionKey";
	
	public static String DEFAULT_MAID_MODEL = "default_Orign";
	
	/** LittleMaidAvatarに登録するLayer設定 */
	public static List<String> cfg_lmavatar_include_layer = null;
	
	/** 初回ログイン時のアイテム追加機能設定 */
	public static boolean cfg_lmavatar_first_login_add_inventory_item = true;
	
	/** LMアバターのサイズをマルチモデルにアジャストする */
	public static boolean cfg_lmavatar_adjust_size = true;
	
	/** LMアバターのサイズをアジャストする際のサイズ倍率を指定する */
	public static float cfg_lmavatar_adjust_size_scale = 1.0F;
	
	/**
	 * メイドさんモデル
	 */
	public static String cfg_maid_model = "";
	
	/**
	 * メイドさんのカラーインデックス
	 */
	public static Integer cfg_maid_color = 0;
	
	/**
	 * メイドさんの頭防具モデル各種類
	 */
	public static String cfg_armor_model_head = "";
	public static String cfg_armor_model_body = "";
	public static String cfg_armor_model_leg = "";
	public static String cfg_armor_model_boots = "";
	
	/**
	 * LMアバターの有効化無効化
	 */
	public static boolean cfg_enable_lmavatar = true;
	
	//モーションキー設定
	public static boolean cfg_motion_key_reset = true;
	public static String cfg_motion_key1 = "";
	public static String cfg_motion_key2 = "";
	public static String cfg_motion_key3 = "";
	public static String cfg_motion_key4 = "";
	public static String cfg_motion_key5 = "";
	public static String cfg_motion_key6 = "";
	public static String cfg_motion_key7 = "";
	public static String cfg_motion_key8 = "";
	public static String cfg_motion_key9 = "";
	
	public static void init(File configDir) {
		
		File configFile = new File(configDir, "LittleMaidAvatar.cfg");
		
		config = new Configuration(configFile, LittleMaidAvatar.VERSION, true);
		
		//カテゴリーコメントの設定
		initCategory();
		
		//設定値の同期
		syncConfig(false);
		
	}
	
	/**
	 * カテゴリーのコメント設定
	 */
	protected static void initCategory() {
		
		config.addCustomCategoryComment(CATEGORY_GENERAL, "メイドさんの姿になる機能");
		
		config.addCustomCategoryComment(CATEGORY_AVATAR, "マルチモデルの設定");
		
		config.addCustomCategoryComment(CATEGORY_MOTION_KEY, "モーションキーの設定");
		
	}
	
	/**
	 * Config値の同期処理
	 */
	public static void syncConfig(boolean lmavatar) {
		
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
		
		//--------------------------------------------------
		
		//PlayerMaidAvatar
		cfg_maid_model = config.getString("01.MaidModel", CATEGORY_AVATAR,
				DEFAULT_MAID_MODEL, 
				"メイドさんのマルチモデル名");
		
		cfg_maid_color = config.getInt("02.MaidColorNo", CATEGORY_AVATAR, 
				0, 0, 15, 
				"メイドさんのカラー番号");
		
		//メイドさんの防具モデル
		//頭
		cfg_armor_model_head = config.getString("03.ArmorHelmetModel", CATEGORY_AVATAR,
				DEFAULT_MAID_MODEL, 
				"頭防具モデル名");
		
		cfg_armor_model_body = config.getString("04.ArmorChestplateModel", CATEGORY_AVATAR,
				DEFAULT_MAID_MODEL, 
				"胴防具モデル名");
		
		cfg_armor_model_leg = config.getString("05.ArmorLeggingsModel", CATEGORY_AVATAR,
				DEFAULT_MAID_MODEL, 
				"腰防具モデル名");
		
		cfg_armor_model_boots = config.getString("06.ArmorBootsModel", CATEGORY_AVATAR,
				DEFAULT_MAID_MODEL, 
				"靴防具モデル名");
		
		//LMAvatar有効化無効化
		cfg_enable_lmavatar = config.getBoolean("07.EnableLMAvatar", CATEGORY_AVATAR,
				true, 
				"LMアバターの反映");
		
		//--------------------------------------------------
		//MotionKey
		cfg_motion_key_reset = config.getBoolean("MotionKeyReset", CATEGORY_MOTION_KEY,
				true, 
				"モーションキー設定をリセットします。");
		cfg_motion_key1 = config.getString("MotionKey_1", CATEGORY_MOTION_KEY,
				"", 
				"モーションキー1");
		cfg_motion_key2 = config.getString("MotionKey_2", CATEGORY_MOTION_KEY,
				"", 
				"モーションキー2");
		cfg_motion_key3 = config.getString("MotionKey_3", CATEGORY_MOTION_KEY,
				"", 
				"モーションキー3");
		cfg_motion_key4 = config.getString("MotionKey_4", CATEGORY_MOTION_KEY,
				"", 
				"モーションキー4");
		cfg_motion_key5 = config.getString("MotionKey_5", CATEGORY_MOTION_KEY,
				"", 
				"モーションキー5");
		cfg_motion_key6 = config.getString("MotionKey_6", CATEGORY_MOTION_KEY,
				"", 
				"モーションキー6");
		cfg_motion_key7 = config.getString("MotionKey_7", CATEGORY_MOTION_KEY,
				"", 
				"モーションキー7");
		cfg_motion_key8 = config.getString("MotionKey_8", CATEGORY_MOTION_KEY,
				"", 
				"モーションキー8");
		cfg_motion_key9 = config.getString("MotionKey_9", CATEGORY_MOTION_KEY,
				"", 
				"モーションキー9");
		
		config.save();
		
		//LMAvatarの反映
		if (lmavatar) {
			//アバターへ反映する
			PlayerModelManager.syncConfig();
		}
	}
	
	/**
	 * Config値の同期処理
	 */
	public static void syncConfig() {
		syncConfig(true);
	}
	
	/**
	 * 
	 * @param motionList
	 */
	public static void resetMotionKey(List<String> motionList) {
		
		//9個になるように追加
		for (int i = motionList.size() + 1; i <= 9; i++) {
			motionList.add("");
		}
		
		//key1
		Property propReset = FirisConfig.config.get(FirisConfig.CATEGORY_MOTION_KEY, 
				"MotionKeyReset", 
				FirisConfig.cfg_motion_key_reset);
		propReset.set(false);
		
		//key1
		Property propKey1 = FirisConfig.config.get(FirisConfig.CATEGORY_MOTION_KEY, 
				"MotionKey_1", 
				FirisConfig.cfg_motion_key1);
		propKey1.set(motionList.get(0));
		//key2
		Property propKey2 = FirisConfig.config.get(FirisConfig.CATEGORY_MOTION_KEY, 
				"MotionKey_2", 
				FirisConfig.cfg_motion_key2);
		propKey2.set(motionList.get(1));
		//key3
		Property propKey3 = FirisConfig.config.get(FirisConfig.CATEGORY_MOTION_KEY, 
				"MotionKey_3", 
				FirisConfig.cfg_motion_key3);
		propKey3.set(motionList.get(2));
		//key4
		Property propKey4 = FirisConfig.config.get(FirisConfig.CATEGORY_MOTION_KEY, 
				"MotionKey_4", 
				FirisConfig.cfg_motion_key4);
		propKey4.set(motionList.get(3));
		//key5
		Property propKey5 = FirisConfig.config.get(FirisConfig.CATEGORY_MOTION_KEY, 
				"MotionKey_5", 
				FirisConfig.cfg_motion_key5);
		propKey5.set(motionList.get(4));		
		//key6
		Property propKey6 = FirisConfig.config.get(FirisConfig.CATEGORY_MOTION_KEY, 
				"MotionKey_6", 
				FirisConfig.cfg_motion_key6);
		propKey6.set(motionList.get(5));
		//key7
		Property propKey7 = FirisConfig.config.get(FirisConfig.CATEGORY_MOTION_KEY, 
				"MotionKey_7", 
				FirisConfig.cfg_motion_key7);
		propKey7.set(motionList.get(6));
		//key8
		Property propKey8 = FirisConfig.config.get(FirisConfig.CATEGORY_MOTION_KEY, 
				"MotionKey_8", 
				FirisConfig.cfg_motion_key8);
		propKey8.set(motionList.get(7));
		//key9
		Property propKey9 = FirisConfig.config.get(FirisConfig.CATEGORY_MOTION_KEY, 
				"MotionKey_9", 
				FirisConfig.cfg_motion_key9);
		propKey9.set(motionList.get(8));
		
		syncConfig(false);
		
	}

}
