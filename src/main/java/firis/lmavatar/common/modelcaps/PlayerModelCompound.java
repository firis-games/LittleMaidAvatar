package firis.lmavatar.common.modelcaps;

import firis.lmavatar.LittleMaidAvatar;
import firis.lmavatar.common.manager.SyncPlayerModelClient;
import firis.lmavatar.config.FirisConfig;
import firis.lmlib.api.LMLibraryAPI;
import firis.lmlib.api.caps.IGuiTextureSelect;
import firis.lmlib.api.caps.IModelCapsEntity;
import firis.lmlib.api.caps.ModelCompoundEntityBase;
import firis.lmlib.api.motion.LMMotionSitdown;
import firis.lmlib.api.resource.LMTextureBox;
import firis.lmmm.api.model.ModelMultiBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

/**
 * PlayerAvatar用パラメータクラス
 * @author firis-games
 * 
 * モーションなどの表示フラグもここで管理する
 *
 */
public class PlayerModelCompound extends ModelCompoundEntityBase<EntityPlayer> implements IGuiTextureSelect {
	
	/**
	 * LMAvatarが有効化どうかの判断
	 */
	private boolean enableLMAvatar = true;
	
	/**
	 * 待機アクション
	 */
	private boolean lmAvatarWaitAction = false;  
	
	/**
	 * 待機アクション用カウンター
	 */
	private Integer lmAvatarWaitCounter = 0;
	
	/**
	 *　LMアバターアクション
	 *
	 * 0:アクションなし
	 * 1～:モーション割り当て
	 */
	private Integer lmAvatarAction = 0;
	
	/**
	 * Player
	 */
	public void setPlayer(EntityPlayer player) {
		this.owner = player;
		this.entityCaps = new PlayerModelCaps(player);
	}
	
	/**
	 * コンストラクタ
	 * @param entity
	 * @param caps
	 */
	public PlayerModelCompound(EntityPlayer entity, IModelCapsEntity caps) {
		super(entity, caps);
	}
	
	/**
	 * アクション状態を取得する
	 * @return
	 */
	public Integer getLMAvatarActionId() {
		return this.lmAvatarAction;
	}
	
	/**
	 * お座りモーション判定
	 * @return
	 */
	public boolean isLMAvatarActionSitdown() {
		return LMMotionSitdown.MOTION_ID.equals(LMLibraryAPI.instance().getLMMotionId(this.lmAvatarAction));
	}
	
	/**
	 * アクション中の判断
	 * @return
	 */
	public boolean isLMAvatarAction() {
		return this.lmAvatarAction != 0;
	}
	
	/**
	 * モーションIDを取得する
	 * @return
	 */
	public String getLMMotionId() {
		return LMLibraryAPI.instance().getLMMotionId(this.lmAvatarAction);
	}
	
	/**
	 * 待機状態を取得する
	 */
	public boolean getLMAvatarWaitAction() {
		return this.lmAvatarWaitAction;
	}

	/**
	 * LMアバターのアクションを設定する
	 */
	public void setLMAvatarAction(Integer isAction) {
		this.lmAvatarAction = isAction;
	}
	
	/**
	 * LMアバターの待機アクション
	 * 一定時間経過後にtrueと判断する
	 * @param isAction
	 */
	public void setLMAvatarWaitAction(boolean isAction) {
		//モーション継続状態と判断
		Integer counter = lmAvatarWaitCounter;
		
		//初期化
		if (counter == 0) lmAvatarWaitCounter = owner.ticksExisted;
		
		//100tickで待機状態On
		if ((owner.ticksExisted - counter) >= FirisConfig.cfg_lmavatar_wait_motion_time) {
			this.lmAvatarWaitAction = true;
		}
	}
	
	/**
	 * LMアバターのアクションをリセットする
	 */
	public void resetLMAvatarAction() {
		this.lmAvatarAction = 0;
	}
	
	/**
	 * LMアバターの待機アクションをリセットする
	 */
	public void resetLMAvatarWaitAction() {
		this.lmAvatarWaitAction = false;
		this.lmAvatarWaitCounter = owner.ticksExisted;
	}
	
	/**
	 * LMAvatarの有効無効設定
	 * @param enable
	 */
	public void setEnableLMAvatar(boolean enable) {
		this.enableLMAvatar = enable;
	}
	
	/**
	 * LMAvatarの設定
	 * @return
	 */
	public boolean getEnableLMAvatar() {
		return this.enableLMAvatar;
	}
	
	/**
	 * NBTへ変換する
	 */
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		
		super.writeToNBT(nbt);
		
		//モーション系
		nbt.setInteger("action", this.lmAvatarAction);
		nbt.setBoolean("wait", this.lmAvatarWaitAction);
		nbt.setBoolean("enable", this.enableLMAvatar);

		return nbt;
	}
	
	/**
	 * NBTから復元する
	 */
	public void readFromNBT(NBTTagCompound nbt) {
		
		super.readFromNBT(nbt);
		
		//モーション系
		this.lmAvatarAction = nbt.getInteger("action");
		this.lmAvatarWaitAction = nbt.getBoolean("wait");
		this.enableLMAvatar = nbt.getBoolean("enable");
		
	}
	
	/**
	 * インナー防具テクスチャ
	 */
	@Override
	public ResourceLocation getTextureInnerArmor(EntityEquipmentSlot slot) {
		
		LMTextureBox armorBox = this.getTextureBoxArmor(slot);
		
		if (armorBox == null) return null;
		
		ItemStack stack = this.owner.inventory.armorItemInSlot(slot.getIndex());
		return armorBox.getTextureInnerArmor(stack);
		
	}
	
	/**
	 * インナー発光防具テクスチャ
	 */
	@Override
	public ResourceLocation getLightTextureInnerArmor(EntityEquipmentSlot slot) {
		
		LMTextureBox armorBox = this.getTextureBoxArmor(slot);
		
		if (armorBox == null) return null;
		
		ItemStack stack = this.owner.inventory.armorItemInSlot(slot.getIndex());
		return armorBox.getLightTextureInnerArmor(stack);
	}
	
	/**
	 * アウター防具テクスチャ
	 */
	@Override
	public ResourceLocation getTextureOuterArmor(EntityEquipmentSlot slot) {
		
		LMTextureBox armorBox = this.getTextureBoxArmor(slot);
		
		if (armorBox == null) return null;
		
		ItemStack stack = this.owner.inventory.armorItemInSlot(slot.getIndex());
		return armorBox.getTextureOuterArmor(stack);
	}
	
	/**
	 * アウター発光防具テクスチャ
	 */
	@Override
	public ResourceLocation getLightTextureOuterArmor(EntityEquipmentSlot slot) {
		
		LMTextureBox armorBox = this.getTextureBoxArmor(slot);
		
		if (armorBox == null) return null;
		
		ItemStack stack = this.owner.inventory.armorItemInSlot(slot.getIndex());
		return armorBox.getLightTextureOuterArmor(stack);
	}

	@Override
	public String getGuiTargetLittleMaidName() {
		return this.getTextureBoxLittleMaid().getTextureModelName();
	}

	@Override
	public String getGuiTargetArmorName(EntityEquipmentSlot slot) {
		return this.getTextureBoxArmor(slot).getTextureModelName();
	}

	@Override
	public int getGuiTargetColor() {
		return this.color;
	}

	@Override
	public boolean getGuiTargetContract() {
		return true;
	}

	/**
	 * GUIからの変更時の同期処理
	 */
	@Override
	public void syncSelectTextureLittleMaid(String textureName, int color) {
		
		this.setPlayer(LittleMaidAvatar.proxy.getClientPlayer());
		
		//メイドモデル反映
		this.setTextureLittleMaid(textureName);
		this.setColor(color);
		
		//キャッシュへ反映
		this.syncPlayerModeCache();
	}

	/**
	 * GUIからの変更時の同期処理
	 */
	@Override
	public void syncSelectTextureArmor(String headTextureName, String chestTextureName, String legsTextureName, String feetTextureName) {

		this.setPlayer(LittleMaidAvatar.proxy.getClientPlayer());

		//防具モデル反映
		this.setTextureArmor(EntityEquipmentSlot.HEAD, headTextureName);
		this.setTextureArmor(EntityEquipmentSlot.CHEST, chestTextureName);
		this.setTextureArmor(EntityEquipmentSlot.LEGS, legsTextureName);
		this.setTextureArmor(EntityEquipmentSlot.FEET, feetTextureName);
		
		//キャッシュへ反映
		this.syncPlayerModeCache();
	}
	
	/***
	 * PlayerModel情報をキャッシュへ投入する
	 */
	public void syncPlayerModeCache() {
		SyncPlayerModelClient.instance.setPlayerModelNBTTagCompound(
				LittleMaidAvatar.proxy.getClientPlayer().getName(), 
				this.writeToNBT(new NBTTagCompound()));		
	}
	
	/**
	 * 試験用機能
	 * プレイヤーの幅
	 * @return
	 */
	public float getPlayerWidth() {
		ModelMultiBase mmb = getModelLittleMaid();
		if (mmb != null) {
			return Math.min(mmb.getWidth(getModelCaps()) * getPlayerScale(), 0.6F);
		}
		return 0.5F * getPlayerScale();
	}
	
	/**
	 * 試験用機能
	 * プレイヤーの高さ
	 * @return
	 */
	public float getPlayerHeight() {
		ModelMultiBase mmb = getModelLittleMaid();
		if (mmb != null) {
			return mmb.getHeight(getModelCaps()) * getPlayerScale();
		}
		return 1.35F * getPlayerScale();
	}
	
	/**
	 * 試験用機能
	 * プレイヤーの目線
	 * @return
	 */
	public float getPlayerEyeHeight() {
		return getPlayerHeight() * 0.85F;
	}
	
	/**
	 * 試験用機能
	 * プレイヤースケール取得
	 * @return
	 */
	public float getPlayerScale() {
		return FirisConfig.cfg_lmavatar_adjust_size ? FirisConfig.cfg_lmavatar_adjust_size_scale : 1.0F;
	}
	
}
