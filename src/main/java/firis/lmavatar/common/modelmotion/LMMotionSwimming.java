package firis.lmavatar.common.modelmotion;

import firis.lmmm.api.caps.IModelCaps;
import firis.lmmm.api.caps.ModelCapsHelper;
import firis.lmmm.api.model.ModelBase;
import firis.lmmm.api.model.ModelLittleMaidBase;
import firis.lmmm.api.model.motion.ILMMotion;
import net.minecraft.block.state.IBlockState;

/**
 * 追加モーション
 * 
 * @author firis-games
 *
 */
public class LMMotionSwimming implements ILMMotion {

	/**
	 * メイドさんが泳ぐ
	 */
	@Override
	public boolean postRotationAngles(ModelLittleMaidBase model, String motion, float limbSwing, float limbSwingAmount,
			float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, IModelCaps pEntityCaps) {

		// 動いている + 水中判定
		if (limbSwingAmount >= 0.05F
				&& ModelCapsHelper.getCapsValueBoolean(pEntityCaps, IModelCaps.caps_isInWater)) {

			//水中に完全に沈んでいるかのチェック
			IBlockState state = (IBlockState) pEntityCaps.getCapsValue(IModelCaps.caps_PosBlockState, 0.0D, 1.0D, 0.0D);
			if (state == null || !state.getMaterial().isLiquid()) return false;
			
			// モーション定義
			// 初期化
			model.setDefaultPause(0, 0, ageInTicks, 0, 0, 0, pEntityCaps);

			// うつ伏せ
			model.mainFrame.setRotateAngleDegX(90);
			model.mainFrame.setRotationPoint(0F, 22F, -4F);

			// 頭の位置調整
			model.bipedHead.setRotateAngleDegX(-45);
			model.bipedHead.addRotateAngleDegX(headPitch * 0.5F);
			model.bipedHead.addRotateAngleDegZ(-netHeadYaw * 0.8F);

			// バタ手
			model.bipedRightArm.setRotateAngle(
					ModelBase.mh_cos(limbSwing * 0.6662F + 3.141593F) * 2.0F * limbSwingAmount * 0.5F, 0F, 0F);
			model.bipedLeftArm.setRotateAngle(ModelBase.mh_cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F, 0F,
					0F);
			model.bipedRightArm.addRotateAngleDegZ(15F);
			model.bipedLeftArm.addRotateAngleDegZ(-15F);

			// バタ足
			model.bipedRightLeg.setRotateAngle(ModelBase.mh_cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount, 0F, 0F);
			model.bipedLeftLeg
					.setRotateAngle(ModelBase.mh_cos(limbSwing * 0.6662F + 3.141593F) * 1.4F * limbSwingAmount, 0F, 0F);

			return true;
		}

		return false;
	}

	@Override
	public String getMotionId() {
		return null;
	}

}
