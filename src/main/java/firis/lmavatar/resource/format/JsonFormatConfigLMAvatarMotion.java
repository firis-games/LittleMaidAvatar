package firis.lmavatar.resource.format;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.annotations.SerializedName;

/**
 * LMアバター独自キー設定用のフォーマット
 * @author firis-games
 *
 */
public class JsonFormatConfigLMAvatarMotion {
	
	/***
	 * モーションキーの設定
	 * key1~key9まで
	 */
	@SerializedName("MotionKey")
	public Map<String, String> motionKeyMap = new TreeMap<>();
	
	/**
	 * モーションID確認用に出力する
	 */
	@SerializedName("MotionList")
	public List<String> motionList = new ArrayList<>();
	
}
