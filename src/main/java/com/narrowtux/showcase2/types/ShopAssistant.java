package com.narrowtux.showcase2.types;

import java.util.Set;

import org.bukkit.inventory.ItemStack;

import com.narrowtux.narrowtuxlib.NarrowtuxLib;
import com.narrowtux.narrowtuxlib.assistant.Assistant;
import com.narrowtux.narrowtuxlib.assistant.AssistantAction;
import com.narrowtux.narrowtuxlib.assistant.AssistantPage;
import com.narrowtux.showcase2.ShowcasePlayer;
import com.nijikokun.register.payment.Method;
import com.nijikokun.register.payment.Method.MethodAccount;

public class ShopAssistant extends Assistant {

	ShopShowcase showcase;
	ShowcasePlayer player;
	Method method;
	MethodAccount account;
	public ShopAssistant(ShowcasePlayer p, ShopShowcase s) {
		super(p.getPlayer());
		player = p;
		this.showcase = s;
		setTitle("Shop assistant");
		
		
		
		method = NarrowtuxLib.getMethod();
		account = method.getAccount(player.getName());
		
		addPage(new AssistantPage(this) {

			{
				String text = "";
				text += "This showcase has "+showcase.getAmount()+" "+showcase.getType().getType()+".\n";
				double buy = showcase.getBuyPrice();
				double sell = showcase.getSellPrice();
				if(buy > 0) {
					text += ("Cost: "+method.format(buy) + "\n");
				} else if(buy == 0) {
					text += ("Free Items!\n");
				}
				
				if(sell > 0) {
					text += ("Buys for: "+method.format(sell) + "\n");
				} else if(sell == 0) {
					text += ("Gift items!\n");
				}
				
				if(sell >= 0) {
					text += ("Takes up to "+showcase.getMaxAmount()+ " items.\n");
				}
				
				text += ("To buy, type 'buy <amount>'\n");
				text += ("To sell, type 'sell <amount>'\n");
				text += ("Type 'done' to leave.\n");
				setTitle("");
				setText(text);
			}
			
			@Override
			public AssistantAction onPageInput(String text) {
				if(text.equals("done")) {
					return AssistantAction.FINISH;
				}
				
				String args[] = text.split(" ");
				if(args.length == 2) {
					String cmd = args[0];
					int amount = 0;
					try {
						amount = Integer.valueOf(args[1]);
					} catch(Exception e) {
						sendMessage("Invalid number. Try again.");
						return AssistantAction.SILENT_REPEAT;
					}
					if(cmd.equals("buy")) {
						if(showcase.getBuyPrice() < 0) {
							sendMessage("This showcase doesn't sell stuff.");
							return AssistantAction.SILENT_REPEAT;
						}
						amount = Math.max(0, amount);
						amount = Math.min(amount, showcase.getAmount());
						double price = amount * showcase.getBuyPrice();
						if(!account.hasEnough(price)) {
							sendMessage("You can't afford so much items ("+amount+" for "+method.format(price)+")");
							return AssistantAction.SILENT_REPEAT;
						}
						if(amount == 0) {
							return AssistantAction.CANCEL;
						}
						sendMessage("Bought "+amount+" items for "+method.format(price));
						showcase.setAmount(showcase.getAmount() - amount);
						ItemStack add = showcase.getType().clone();
						add.setAmount(amount);
						player.addItems(add);
						account.subtract(price);
						Set<ShowcasePlayer> owners = showcase.getOwners();
						double hprice = price/owners.size();
						for(ShowcasePlayer owner:owners) {
							NarrowtuxLib.getNotificationManager().sendNotification(owner.getName(), "Showcase sold items.", "Showcase sold "+amount+" of "+showcase.getType().getType()+" for "+method.format(price)+". You got "+method.format(hprice)+".");
							owner.getAccount().add(hprice);
						}
						return AssistantAction.FINISH;
					}
					if(cmd.equals("sell")) {
						if(showcase.getSellPrice() < 0) {
							sendMessage("This showcase doesn't buy stuff. ");
							return AssistantAction.SILENT_REPEAT;
						}
						amount = Math.min(amount, player.getAmountOfType(showcase.getType()));
						amount = Math.min(amount, showcase.getMaxAmount() - showcase.getAmount());
						amount = Math.max(0, amount);
						
						if(amount == 0) {
							return AssistantAction.CANCEL;
						}
						
						double balance = 0;
						Set<ShowcasePlayer> owners = showcase.getOwners();
						for(ShowcasePlayer owner:owners) {
							balance += owner.getAccount().balance();
						}
						double price = amount * showcase.getSellPrice();
						if(price > balance) {
							sendMessage("The owner(s) of this showcase can't afford so much items. Try with a fewer items.");
							return AssistantAction.SILENT_REPEAT;
						}
						
						ItemStack take = showcase.getType().clone();
						take.setAmount(amount);
						player.removeItems(take);
						
						showcase.setAmount(showcase.getAmount() + amount);
						
						player.getAccount().add(price);

						sendMessage("You sold "+amount+" items and got "+method.format(price)+".");
						
						int handled = 0;
						double hprice = price;
						double pricePerOwner = hprice / owners.size();
						for(ShowcasePlayer owner:owners) {
							MethodAccount account = owner.getAccount();
							double taken;
							if(account.balance() < pricePerOwner) {
								taken = account.balance();
								account.set(0);
							} else {
								account.subtract(pricePerOwner);
								taken = pricePerOwner;
							}
							NarrowtuxLib.getNotificationManager().sendNotification(owner.getName(), "Showcase bought items", "Showcase bought "+amount+" "+showcase.getType().getType()+" for "+method.format(price) + ". You paid "+method.format(taken) + ".");
							handled++;
							hprice -= taken;
							pricePerOwner = hprice / (owners.size() - taken);
							return AssistantAction.FINISH;
						}
					}
				}
				return AssistantAction.SILENT_REPEAT;
			}
			
		});
	}

}
