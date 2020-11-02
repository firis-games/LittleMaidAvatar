package firis.lmavatar.common.manager;

import java.util.Iterator;

import firis.lmavatar.common.network.LMAvatarNetwork;
import firis.lmlib.api.LMLibraryAPI;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

/**
 * サーバー側のパケット送信処理
 * 
 * 一定タイミングごとに同期パケットを投げる？
 * 
 * @author firis-games
 *
 */
public class SyncPlayerModelServer extends AbstractSyncPlayerModel {
	
	/**
	 * インスタンス
	 */
	public static SyncPlayerModelServer instance = new SyncPlayerModelServer();
	
	/**
	 * サーバーサイドのtick処理
	 * @param event
	 */
	@SubscribeEvent
	public void onWorldTickEvent(WorldTickEvent event) {
		if(event.phase == TickEvent.Phase.END){
			onWorldTickEventPost(event);
		}
	}
	
	/**
	 * Tickの最後で同期処理を実行
	 * @param event
	 */
	protected void onWorldTickEventPost(WorldTickEvent event) {
		
		Iterator<String> syncPacketIterator = this.syncPacketQueue.iterator();
		NBTTagList tagList = new NBTTagList();
		while (syncPacketIterator.hasNext()) {
			//NBT取得
			NBTTagCompound tagCompound = this.getPlayerModelNbt(syncPacketIterator.next());
			tagList.appendTag(tagCompound);
			
			//キュー削除
			syncPacketIterator.remove();
		}
		
		//条件に一致すればパケット送信
		sendPacketToClient(tagList);
	}
	
	/**
	 * 同期キューにプレイヤー名を登録
	 * @param playerName
	 */
	protected void syncPlayerModel(String playerName) {
		this.syncPacketQueue.add(playerName);
	}
	
	/**
	 * クライアントへパケットを送信する
	 * @param uuid
	 */
	protected void sendPacketToClient(NBTTagList tagList) {
		//送信情報が0以上の場合はパケットを送信する
		if (tagList.tagCount() > 0) {
			NBTTagCompound send = new NBTTagCompound();
			send.setTag("avatar", tagList);
			//全クライアントへ送信する
			LMLibraryAPI.instance().sendPacketToClientAll(LMAvatarNetwork.CLIENT_SYNC_SERVER_LMAVATAR, send);
		}
	}
		
	
	/**
	 * クライアントから送信されたパケットを受け取る
	 */
	public void recivePacketFromClient(NBTTagCompound tagCompound) {
		
		//サーバーと同期するかの判断用
		String playerName = tagCompound.getString("name");
		
		//クライアントから受け取った場合は無条件で上書きする
		this.setPlayerModelNBTTagCompound(playerName, tagCompound);
		
		//パケットキューへ追加
		this.syncPlayerModel(playerName);
		
	}
}
