package firis.lmavatar.common.modelmotion;

import firis.lmmm.api.caps.IModelCaps;
import firis.lmmm.api.caps.ModelCapsHelper;
import firis.lmmm.api.model.ModelLittleMaidBase;
import firis.lmmm.api.model.motion.ILMMotion;

/**
 * 追加モーション
 * @author firis-games
 *
 */
public class LMMotionCarryOn implements ILMMotion {

	/**
	 * メイドさんがチェストを持っている動き
	 */
	@Override
	public boolean postRotationAngles(ModelLittleMaidBase model, String motion, float limbSwing, float limbSwingAmount,
			float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, IModelCaps pEntityCaps) {
		
		String itemId = ModelCapsHelper.getCapsValueString(pEntityCaps, IModelCaps.caps_heldItems);
		if ("carryon:entity_item".equals(itemId)
				|| "carryon:tile_item".equals(itemId)) {
			//アイテム運びモード
			model.bipedRightArm.setRotateAngle(-1.0F, -0.2F, 0.0F);
			model.bipedLeftArm.setRotateAngle(-1.0F, 0.2F, 0.0F);
		}
		
		return true;
	}

	@Override
	public String getMotionId() {
		return null;
	}

}
