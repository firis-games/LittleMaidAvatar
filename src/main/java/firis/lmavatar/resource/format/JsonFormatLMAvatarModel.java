package firis.lmavatar.resource.format;

import com.google.gson.annotations.SerializedName;

import net.minecraft.nbt.NBTTagCompound;

/**
 * LMアバターのモデル用保存クラス
 * @author firis-games
 *
 */
public class JsonFormatLMAvatarModel {
	
	//メイドモデル
	@SerializedName("LittleMaidModel")
	public String littleMaidModel = "default_Orign";

	//メイドカラー
	@SerializedName("LittleMaidColor")
	public Integer littleMaidColor = 0;
	
	//頭防具モデル
	@SerializedName("HelmetModel")
	public String armorHelmetModel = "default_Orign";
	
	//胴防具モデル
	@SerializedName("ChestplateModel")
	public String armorChestplateModel = "default_Orign";
	
	//腰防具モデル
	@SerializedName("LeggingsModel")
	public String armorLeggingsModel = "default_Orign";
	
	//足防具モデル
	@SerializedName("BootsModel")
	public String armorBootsModel = "default_Orign";
	
	//アバターの有効無効設定
	@SerializedName("EnableLittleMaidAvatar")
	public boolean enableLMAvatar = true;
	
	/**
	 * NBTへ変換する
	 */
	public NBTTagCompound serializeToNBT(NBTTagCompound nbt) {
		
		//必要な情報のみNBT化
		nbt.setString("maid", this.littleMaidModel);
		nbt.setInteger("color", this.littleMaidColor);
		nbt.setString("head", this.armorHelmetModel);
		nbt.setString("chest", this.armorChestplateModel);
		nbt.setString("legs", this.armorLeggingsModel);
		nbt.setString("feet", this.armorBootsModel);
		nbt.setBoolean("enable", this.enableLMAvatar);
		nbt.setBoolean("contract", true);
		
		return nbt;
	}
	
	/**
	 * NBTから復元する
	 */
	public void deserializeFromNBT(NBTTagCompound nbt) {
		
		String maid = nbt.getString("maid");
		Integer color = nbt.getInteger("color");
		String armorHead = nbt.getString("head");
		String armorChest = nbt.getString("chest");
		String armorLegs = nbt.getString("legs");
		String armorFeet = nbt.getString("feet");
		boolean enable = nbt.getBoolean("enable");
		
		//展開
		this.littleMaidModel = maid;
		this.littleMaidColor = color;
		this.armorHelmetModel = armorHead;
		this.armorChestplateModel = armorChest;
		this.armorLeggingsModel = armorLegs;
		this.armorBootsModel = armorFeet;
		this.enableLMAvatar = enable;
		
	}

}
