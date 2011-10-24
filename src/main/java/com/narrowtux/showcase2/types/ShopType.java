package com.narrowtux.showcase2.types;

import java.util.logging.Level;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.avaje.ebean.enhance.asm.commons.Method;
import com.narrowtux.showcase2.ShowcaseMain;
import com.narrowtux.showcase2.ShowcasePlayer;
import com.nijikokun.register.payment.Methods;

public class ShopType extends ShowcaseType {

	public ShopType() {
		super("shop");
	}

	@Override
	public Showcase createShowcase(Block block, ItemStack stack, ShowcasePlayer player, String[] args) {
		ShopShowcase sh = new ShopShowcase(block, stack, player);
		if(args.length >= 2) {
			sh.setInit(true); //Set inited by arguments so no assistant will ever appear.
			//arguments: amount buyprice sellprice maxamount
			int pa = player.getAmountOfType(stack);
			ShowcaseMain.doLog("player has "+pa, Level.FINE);
			pa = Math.min(pa, Integer.valueOf(args[0]));
			ShowcaseMain.doLog("taking "+pa, Level.FINE);
			
			player.sendMessage("Taking "+pa+" items.");
			ItemStack remove = stack.clone();
			remove.setAmount(pa);
			player.removeItems(remove);
			sh.setAmount(pa);
			
			sh.setBuyPrice(Double.valueOf(args[1]));
			if(sh.getBuyPrice() > 0) {
				player.sendMessage("1 item costs "+sh.getBuyPrice());
			} else if (sh.getBuyPrice() == 0) {
				player.sendMessage("Players will get items for free.");
			} else {
				player.sendMessage("Showcase won't sell items.");
			}
			
			if(args.length == 4) {
				sh.setSellPrice(Double.valueOf(args[2]));
				if(sh.getSellPrice() > 0) {
					player.sendMessage("Players will get "+sh.getSellPrice() + " for 1 item.");
				} else if (sh.getSellPrice() == 0) {
					player.sendMessage("Players can put items in the Showcase, but won't get money.");
				} else {
					player.sendMessage("Showcase won't buy items.");
				}
				sh.setMaxAmount(Integer.valueOf(args[3]));
				player.sendMessage("Showcase will buy up to "+sh.getMaxAmount() + " items.");
			}
			
			player.sendMessage("Shop Showcase created.");
		}
		return sh;
	}

}
