package firis.lmavatar.common.command;

import firis.lmavatar.common.manager.PlayerModelManager;
import firis.lmavatar.common.modelcaps.PlayerModelCompound;
import firis.lmavatar.resource.LMAvatarResourceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * クライアント側でコマンド処理を行う用
 * @author firis-games
 */
public class LMAvatarCommandClient {

	/**
	 * コマンドを実行する
	 */
	public static void execute(String command, String param) {
		
		//各コマンドを実行する
		if ("save".equals(command)) executeSave(param);
		if ("load".equals(command)) executeLoad(param);
		if ("list".equals(command)) executeList(param);
		
	}
	
	/**
	 * LMAvatarの今の状態を保存する
	 * @param param
	 */
	private static void executeSave(String param) {
		
		//クライアント側のPlayer
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		boolean isNew = !LMAvatarResourceManager.isLMAvatarList(param);
		
		PlayerModelCompound playerModel = PlayerModelManager.getModelConfigCompound(player);
		
		//設定ファイルへ保存
		LMAvatarResourceManager.savaLMAvatarList(param, playerModel.serializeToNBT(new NBTTagCompound()));
		
		//終了後のメッセージ
		if (isNew) {
			//新規
			player.sendMessage(new TextComponentTranslation("commands.lmacommand.save.new", new Object[] {param}));
		} else {
			//更新
			player.sendMessage(new TextComponentTranslation("commands.lmacommand.save.update", new Object[] {param}));
		}
	}
	
	/**
	 * LMAvatarの状態を指定名で更新する
	 * @param param
	 */
	private static void executeLoad(String param) {
		
		//クライアント側のPlayer
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		//パラメータが存在しない場合
		if (!LMAvatarResourceManager.isLMAvatarList(param)) {
			//エラーメッセージ
			player.sendMessage(new TextComponentTranslation("commands.lmacommand.load.error", new Object[] {param}));
			return;
		}
		
		NBTTagCompound nbtModel = LMAvatarResourceManager.loadLMAvatarList(param);
		
		PlayerModelCompound playerModel = PlayerModelManager.getModelConfigCompound(player);
		playerModel.deserializeFromNBT(nbtModel);
		
		//キャッシュへ反映
		playerModel.syncPlayerModeCache();
		
		//メッセージ表示
		player.sendMessage(new TextComponentTranslation("commands.lmacommand.load", new Object[] {param}));
		
	}
	
	/**
	 * リスト表示
	 * @param param
	 */
	private static void executeList(String param) {
		
		//クライアント側のPlayer
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		if (param.equals("")) {
			//空の場合は登録名の一覧を表示する
			for (String key :LMAvatarResourceManager.getListLMAvatarList()) {
				player.sendMessage(new TextComponentTranslation(key, new Object[] {}));
			}
		} else {
			//指定名
			if (!LMAvatarResourceManager.isLMAvatarList(param)) {
				//エラーメッセージ
				player.sendMessage(new TextComponentTranslation("commands.lmacommand.list.error", new Object[] {param}));
				return;
			}
			
			//指定名の一覧を表示する
			NBTTagCompound nbt = LMAvatarResourceManager.loadLMAvatarList(param);
			player.sendMessage(new TextComponentTranslation("MaidModel:" + nbt.getString("maid"), new Object[] {}));
			player.sendMessage(new TextComponentTranslation("MaidColor:" + ((Integer) nbt.getInteger("color")).toString(), new Object[] {}));
			player.sendMessage(new TextComponentTranslation("ArmorHelmetModel:" + nbt.getString("head"), new Object[] {}));
			player.sendMessage(new TextComponentTranslation("ArmorChestplateModel:" + nbt.getString("chest"), new Object[] {}));
			player.sendMessage(new TextComponentTranslation("ArmorLeggingsModel:" + nbt.getString("legs"), new Object[] {}));
			player.sendMessage(new TextComponentTranslation("ArmorBootsModel:" + nbt.getString("feet"), new Object[] {}));
		}
	}
}
