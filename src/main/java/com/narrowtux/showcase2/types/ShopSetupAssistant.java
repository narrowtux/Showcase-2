package com.narrowtux.showcase2.types;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.narrowtux.narrowtuxlib.NarrowtuxLib;
import com.narrowtux.narrowtuxlib.assistant.Assistant;
import com.narrowtux.narrowtuxlib.assistant.AssistantAction;
import com.narrowtux.narrowtuxlib.assistant.AssistantPage;
import com.narrowtux.showcase2.ShowcasePlayer;

public class ShopSetupAssistant extends Assistant {

	ShowcasePlayer player;
	ShopShowcase showcase;
	
	public ShopSetupAssistant(ShowcasePlayer p, ShopShowcase s) {
		super(p.getPlayer());
		player = p;
		showcase = s;
		
		setTitle("Shop Administration");
		sendMessage(formatLine("You can always skip a page with "+ChatColor.YELLOW+"skip"));
		sendMessage(formatLine("You are always able to modify all values by rightclicking on the showcase."));
		
		//Amount
		addPage(new AssistantPage(this) {
			{
				setTitle("Amount");
				String text = "How many items should be in the showcase?\n";
				text += "Pro Tip: You can also define a relative amount\n";
				text += "Examples:\n";
				text += ChatColor.YELLOW+"5"+ChatColor.WHITE+" sets the amount to exactly 5.\n";
				text += ChatColor.YELLOW+"+3"+ChatColor.WHITE+" adds 3 items from your inventory.\n";
				text += ChatColor.YELLOW+"-4"+ChatColor.WHITE+" takes 4 items and gives them to you.";
				setText(text);
			}
			
			@Override
			public AssistantAction onPageInput(String text) {
				if(showcase.isRemoved()) {
					return AssistantAction.CANCEL;
				}
				
				if(text.equals("skip")) {
					return AssistantAction.CONTINUE;
				}
				if(text.equals("cancel")) {
					return AssistantAction.CANCEL;
				}
				
				int multi = 0;
				int amount = 0;
				
				if(text.startsWith("+")) {
					text = text.replaceFirst("+", "");
					multi = 1;
				} else if (text.startsWith("-")) {
					text = text.replaceFirst("-", "");
					multi = -1;
				} else {
					multi = 0;
				}
				try {
					amount = Integer.valueOf(text);
				} catch(Exception e) {
					return invalidNumber();
				}
				
				int amountInShowcase = showcase.getAmount();
				int amountAddToPlayer;
				int amountPlayerHas = player.getAmountOfType(showcase.getType());
				

				if(multi == 0) {
					if(amount < amountInShowcase) {
						multi = -1;
						amount = amountInShowcase - amount;
					} else if(amount > amountInShowcase) {
						multi = 1;
						amount -= amountInShowcase;
					} else {
						return AssistantAction.CONTINUE;
					}
				}
				
				if(multi == -1) {
					amount = Math.max(0, amount);
					amount = Math.min(amount, amountInShowcase);
					amountAddToPlayer = amount;
					amountInShowcase -= amount;
					
					showcase.setAmount(amountInShowcase);
					ItemStack add = showcase.getType().clone();
					add.setAmount(amountAddToPlayer);
					player.addItems(add);
					sendMessage("Added "+amountAddToPlayer+" to your inventory. "+amountInShowcase+" left.");
					return AssistantAction.CONTINUE;
				}
				
				if(multi == 1) {
					amount = Math.max(0, amount);
					amount = Math.min(amount, amountPlayerHas);
					amountInShowcase += amount;
					amountAddToPlayer = -amount;
					
					showcase.setAmount(amountInShowcase);
					ItemStack remove = showcase.getType().clone();
					remove.setAmount(-amountAddToPlayer);
					player.removeItems(remove);
					sendMessage("Added "+amount+" to showcase.");
					return AssistantAction.CONTINUE;
				}
				
				return AssistantAction.CONTINUE;
			}
		});
		
		//Buy price
		addPage(new AssistantPage(this){
			{
				setTitle("Buy Price");
				setText("Set the price one item costs a player to buy.");
			}

			@Override
			public AssistantAction onPageInput(String text) {
				if(showcase.isRemoved()) {
					return AssistantAction.CANCEL;
				}
				
				if(text.equals("skip")) {
					return AssistantAction.CONTINUE;
				}
				if(text.equals("cancel")) {
					return AssistantAction.CANCEL;
				}
				
				double price = 0;
				try {
					price = Double.valueOf(text);
				} catch(Exception e) {
					return invalidNumber();
				}
				showcase.setBuyPrice(price);
				sendMessage("Set buy price to "+NarrowtuxLib.getMethod().format(price));
				
				return AssistantAction.CONTINUE;
			}
		});
		
		//Sell price
		addPage(new AssistantPage(this){
			{
				setTitle("Sell Price");
				setText("Set the price a player will get from selling one item.");
			}

			@Override
			public AssistantAction onPageInput(String text) {
				if(showcase.isRemoved()) {
					return AssistantAction.CANCEL;
				}
				
				if(text.equals("skip")) {
					return AssistantAction.CONTINUE;
				}
				if(text.equals("cancel")) {
					return AssistantAction.CANCEL;
				}
				
				double price = 0;
				try {
					price = Double.valueOf(text);
				} catch(Exception e) {
					return invalidNumber();
				}
				showcase.setSellPrice(price);
				sendMessage("Set sell price to "+NarrowtuxLib.getMethod().format(price));
				
				return AssistantAction.CONTINUE;
			}
		});
		
		//Max amount
		addPage(new AssistantPage(this){
			{
				setTitle("Max Buy-In amount");
				setText("Set the maximum amount of items this showcase will buy.");
			}

			@Override
			public AssistantAction onPageInput(String text) {
				if(showcase.isRemoved()) {
					return AssistantAction.CANCEL;
				}
				
				if(text.equals("skip")) {
					return AssistantAction.CONTINUE;
				}
				if(text.equals("cancel")) {
					return AssistantAction.CANCEL;
				}
				
				int amount = 0;
				try {
					amount = Integer.valueOf(text);
				} catch(Exception e) {
					return invalidNumber();
				}
				showcase.setMaxAmount(amount);
				sendMessage("Set max amount to "+amount);
				
				return AssistantAction.CONTINUE;
			}
		});
	}
	
	protected AssistantAction invalidNumber() {
		sendMessage("Invalid number. Try again.");
		return AssistantAction.REPEAT;
	}

	@Override
	public void onAssistantCancel() {
		sendMessage("Assistant cancelled.");
		sendMessage("When you remove the showcase now, you'll get your items back!");
	}

}
