package firis.lmavatar.common.event;

import firis.lmavatar.LittleMaidAvatar.LMAItems;
import firis.lmavatar.config.FirisConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.items.ItemHandlerHelper;

@EventBusSubscriber
public class PlayerEventHandler {

	private static final String TAG_HAS_PLAYER_MAID_BOOK = "lma_player_maid_book";
	
	/**
	 * プレイヤーログイン時の処理
	 * @param event
	 */
	@SubscribeEvent
	public static void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
		
		if (FirisConfig.cfg_lmavatar_first_login_add_inventory_item) {
			//PlayerData取得
			NBTTagCompound nbt = event.player.getEntityData();
			if (!nbt.hasKey(TAG_HAS_PLAYER_MAID_BOOK)) {
				//メイドさんになる本追加
				ItemHandlerHelper.giveItemToPlayer(event.player, new ItemStack(LMAItems.PLAYER_MAID_BOOK));
				nbt.setBoolean(TAG_HAS_PLAYER_MAID_BOOK, true);
			}
		}
		
	}
	
}
