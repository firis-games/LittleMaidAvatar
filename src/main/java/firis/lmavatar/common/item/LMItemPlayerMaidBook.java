package firis.lmavatar.common.item;

import java.util.List;

import javax.annotation.Nullable;

import firis.lmavatar.LittleMaidAvatar;
import firis.lmavatar.common.manager.PlayerModelManager;
import firis.lmavatar.common.modelcaps.PlayerModelCompound;
import firis.lmlib.api.caps.IModelCompoundEntity;
import firis.lmlib.api.entity.ILMModelEntity;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LMItemPlayerMaidBook extends Item {
	
	/**
	 * コンストラクタ
	 */
	public LMItemPlayerMaidBook() {
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabs.MISC);
	}
	/**
	 * 左クリックからのアイテム化
	 */
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
		setDressUpPlayerFromMaid(player, entity);
		return true;
    }
	
	/**
	 * Shift＋右クリックからのアイテム化
	 */
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
    {
		setDressUpPlayerFromMaid(playerIn, target);
		return true;
    }
	
	/**
	 * プレイヤーがメイドさんの見た目になる
	 */
	public void setDressUpPlayerFromMaid(EntityPlayer player, Entity entity) {
		
		if (!player.world.isRemote) return;
		
		//マルチモデル対応Entity
		if (!(entity instanceof ILMModelEntity)) return;
		
		//対象のメイドさんからモデル情報を取得する
		ILMModelEntity lmModelEntity = (ILMModelEntity) entity;
		IModelCompoundEntity modelCompound = lmModelEntity.getModelCompoundEntity();
		
		//メイドモデル名取得
		String maidModelName = modelCompound.getTextureModelNameLittleMaid();
		Integer maidModelColor = (int) modelCompound.getColor();
		String armorModelNameHead = modelCompound.getTextureModelNameArmor(EntityEquipmentSlot.HEAD);
		String armorModelNameChest = modelCompound.getTextureModelNameArmor(EntityEquipmentSlot.CHEST);
		String armorModelNameLegs = modelCompound.getTextureModelNameArmor(EntityEquipmentSlot.LEGS);
		String armorModelNameFeet = modelCompound.getTextureModelNameArmor(EntityEquipmentSlot.FEET);
		
		
		//Playerへ反映
		PlayerModelCompound playerModel = PlayerModelManager.getModelConfigCompound(player);
		
		playerModel.setTextureArmor(EntityEquipmentSlot.HEAD, armorModelNameHead);
		playerModel.setTextureArmor(EntityEquipmentSlot.CHEST, armorModelNameChest);
		playerModel.setTextureArmor(EntityEquipmentSlot.LEGS, armorModelNameLegs);
		playerModel.setTextureArmor(EntityEquipmentSlot.FEET, armorModelNameFeet);
		
		
		//メイドモデルの設定
		if (!player.isSneaking()) {
			
			playerModel.setTextureLittleMaid(maidModelName);
			playerModel.setColor(maidModelColor);
			
		//アーマーモデルの設定
		} else {
						
			//スニーク中はアーマーモデルも設定
			if (player.getHeldItemOffhand().isEmpty()) {
				
				//全部のモデルを反映
				playerModel.setTextureArmor(EntityEquipmentSlot.HEAD, armorModelNameHead);
				playerModel.setTextureArmor(EntityEquipmentSlot.CHEST, armorModelNameChest);
				playerModel.setTextureArmor(EntityEquipmentSlot.LEGS, armorModelNameLegs);
				playerModel.setTextureArmor(EntityEquipmentSlot.FEET, armorModelNameFeet);
				
			} else {
				ItemStack offHandStack = player.getHeldItemOffhand();
				//頭防具
				if (offHandStack.getItem().isValidArmor(offHandStack, EntityEquipmentSlot.HEAD, player)) {
					playerModel.setTextureArmor(EntityEquipmentSlot.HEAD, armorModelNameHead);
				}
				//胴防具
				if (offHandStack.getItem().isValidArmor(offHandStack, EntityEquipmentSlot.CHEST, player)) {
					playerModel.setTextureArmor(EntityEquipmentSlot.CHEST, armorModelNameChest);
				}
				//腰防具
				if (offHandStack.getItem().isValidArmor(offHandStack, EntityEquipmentSlot.LEGS, player)) {
					playerModel.setTextureArmor(EntityEquipmentSlot.LEGS, armorModelNameLegs);
				}
				//足防具
				if (offHandStack.getItem().isValidArmor(offHandStack, EntityEquipmentSlot.FEET, player)) {
					playerModel.setTextureArmor(EntityEquipmentSlot.FEET, armorModelNameFeet);
				}
			}
		}
		
		//キャッシュへ反映
		playerModel.syncPlayerModeCache();

	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		tooltip.add(TextFormatting.LIGHT_PURPLE + I18n.format("item.player_maid_book.info"));
    }
	
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		LittleMaidAvatar.proxy.openGuiTextureSelect();
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
