package firis.lmavatar.common.network;

import java.util.function.Consumer;

import firis.lmavatar.LittleMaidAvatar;
import firis.lmavatar.common.command.LMAvatarCommandClient;
import firis.lmavatar.common.manager.PlayerModelManager;
import firis.lmlib.api.LMLibraryAPI;
import net.minecraft.nbt.NBTTagCompound;

public class LMAvatarNetwork {

	/** クライアントの情報をサーバーへ送信 */
	public static String SERVER_SYNC_CLIENT_LMAVATAR = LittleMaidAvatar.MODID + ":SERVER_SYNC_CLIENT_LMAVATAR";
	
	/** サーバーの情報をクライアントへ送信 */
	public static String CLIENT_SYNC_SERVER_LMAVATAR = LittleMaidAvatar.MODID + ":CLIENT_SYNC_SERVER_LMAVATAR";
	
	/** コマンド同期 */
	public static String CLIENT_COMMAND_EXECUTE = LittleMaidAvatar.MODID + ":CLIENT_COMMAND_EXECUTE";
	
	/**
	 * ネットワーク登録
	 */
	public static void init() {
		
		//クライアントからサーバーへの通信
    	LMLibraryAPI.instance().registerNetwork(SERVER_SYNC_CLIENT_LMAVATAR, new Consumer<NBTTagCompound>() {
			@Override
			public void accept(NBTTagCompound arg0) {
				PlayerModelManager.reciveLMAvatarDataFromClient(arg0);
			}
    	});
    	
    	//サーバーの情報をクライアントへ送信
    	LMLibraryAPI.instance().registerNetwork(CLIENT_SYNC_SERVER_LMAVATAR, new Consumer<NBTTagCompound>() {
			@Override
			public void accept(NBTTagCompound arg0) {
				PlayerModelManager.receiveLMAvatarDataFromServer(arg0);
			}
    	});
    	
    	//コマンド同期
    	LMLibraryAPI.instance().registerNetwork(CLIENT_COMMAND_EXECUTE, new Consumer<NBTTagCompound>() {
			@Override
			public void accept(NBTTagCompound arg0) {
				LMAvatarCommandClient.execute(arg0.getString("command"), arg0.getString("param"));
			}
    	});
    	

	}
	
}
