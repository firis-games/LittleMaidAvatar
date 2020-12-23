package firis.lmavatar.common.manager;

import java.util.Iterator;

import firis.lmavatar.common.network.LMAvatarNetwork;
import firis.lmavatar.resource.LMAvatarResourceManager;
import firis.lmlib.api.LMLibraryAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

/**
 * クライアント側マルチ環境用同期処理
 * 
 * マルチモデル変更・アクション変更の際に同期キューへ登録する
 * クライアント側はMinecraft.getMinecraft().playerのみパケットを送信する
 * Minecraft.getMinecraft().playerは起動時には存在しないため初期化時にはアクセスしない
 * EntityPlayerインスタンスはワールド移動などで再生成されるためプレイヤー名で管理する
 * 
 * @author firis-games
 *
 */
public class SyncPlayerModelClient extends AbstractSyncPlayerModel {
	
	/**
	 * インスタンス
	 */
	public static SyncPlayerModelClient instance = new SyncPlayerModelClient();
	
	/**
	 * キャッシュファイルからの初期化時のみ呼び出す
	 * @param name
	 * @param modelCompound
	 */
	public void initPlayerModelNBTTagCompound(String name, NBTTagCompound modelCompound) {
		super.setPlayerModelNBTTagCompound(name, modelCompound);
	}
	
	/**
	 * マルチモデルの変更/サーバー側からの他プレイヤーの同期の際に呼び出される
	 */
	@Override
	public void setPlayerModelNBTTagCompound(String name, NBTTagCompound modelCompound) {
		
		boolean isSave = false;
		NBTTagCompound oldNbt = null;
		if (!this.modelNbtMap.containsKey(name)) {
			isSave = true;
		} else {
			oldNbt = this.getPlayerModelNbt(name);
		}
		
		//内部変数へ設定
		super.setPlayerModelNBTTagCompound(name, modelCompound);
		
		//自身の場合はキューへ登録
		if (name.equals(Minecraft.getMinecraft().player.getName())) {
			this.syncPlayerModel();
		}
		
		//セーブの判定
		if (!isSave && !this.equalsNBT(oldNbt, this.getPlayerModelNbt(name))) {
			isSave = true;
		}
		
		//保存処理
		if (isSave) {
			LMAvatarResourceManager.savePlayerCache(this.modelNbtMap);
		}
	}
	
	/**
	 * メイドモデルのNBT比較
	 * @param nbt1
	 * @param nbt2
	 * @return
	 */
	protected boolean equalsNBT(NBTTagCompound nbt1, NBTTagCompound nbt2) {
		
		if (nbt1.getString("maid").equals(nbt2.getString("maid"))
				&& nbt1.getInteger("color") == nbt2.getInteger("color")
				&& nbt1.getString("head").equals(nbt2.getString("head"))
				&& nbt1.getString("chest").equals(nbt2.getString("chest"))
				&& nbt1.getString("legs").equals(nbt2.getString("legs"))
				&& nbt1.getString("feet").equals(nbt2.getString("feet"))
				&& nbt1.getBoolean("enable") == nbt2.getBoolean("enable")) {
			return true;
		}
		return false;
	}
	
	/**
	 * クライアント側は自分自身のみサーバーへ送信する
	 */
	protected void syncPlayerModel() {
		this.syncPacketQueue.add(Minecraft.getMinecraft().player.getName());
	}
	
	/**
	 * クライアントサイドのtick処理
	 * @param event
	 */
	@SubscribeEvent
	public void onClientTickEvent(ClientTickEvent event) {
		if(event.phase == TickEvent.Phase.END && Minecraft.getMinecraft().player != null){
			onClientTickEventPost(event);
		}
	}
	
	/**
	 * Tickの最後で処理を行う
	 * @param event
	 */
	protected void onClientTickEventPost(ClientTickEvent event) {
		
		Iterator<String> syncPacketIterator = this.syncPacketQueue.iterator();
		NBTTagCompound tagCompound = null;
		
		//クライアントではプレイヤーの情報しか使わない前提
		while (syncPacketIterator.hasNext()) {
			//NBT取得
			String name = syncPacketIterator.next();
			tagCompound = this.getPlayerModelNbt(name);
			tagCompound.setString("name", name);
			//削除
			syncPacketIterator.remove();
			break;
		}
		//条件に一致すればパケット送信
		sendPacketToServer(tagCompound);
	}
	
	/**
	 * サーバーへパケットを送信する
	 */
	protected void sendPacketToServer(NBTTagCompound nbt) {
		if (nbt != null) {
			//サーバーへ送信する
			LMLibraryAPI.instance().sendPacketToServer(LMAvatarNetwork.SERVER_SYNC_CLIENT_LMAVATAR, nbt);
		}
	}
	
	/**
	 * サーバーから送信されたパケットを受け取る
	 * 自分自身の情報は上書きしない
	 */
	public void recivePacketFromServer(NBTTagCompound tagCompound) {
		
		NBTTagList tagList = tagCompound.getTagList("avatar", 10);
		
		String playerName = Minecraft.getMinecraft().player.getName();
		
		//サーバーとの同期
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound nbt = tagList.getCompoundTagAt(i);
			String name = nbt.getString("name");
			//自分自身はスキップ
			if (!name.equals(playerName)) {
				//キャッシュへ反映
				this.setPlayerModelNBTTagCompound(name, nbt);
				//PlayerModelへ反映
				PlayerModelManager.setModelConfigCompound(name, nbt);
			}
		}
	}
}
