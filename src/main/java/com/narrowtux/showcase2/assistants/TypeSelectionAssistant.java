package com.narrowtux.showcase2.assistants;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.narrowtux.narrowtuxlib.assistant.Assistant;
import com.narrowtux.narrowtuxlib.assistant.AssistantAction;
import com.narrowtux.narrowtuxlib.assistant.AssistantPage;
import com.narrowtux.showcase2.ShowcaseMain;
import com.narrowtux.showcase2.ShowcasePlayer;
import com.narrowtux.showcase2.event.ShowcaseCreationEvent;
import com.narrowtux.showcase2.types.Showcase;
import com.narrowtux.showcase2.types.ShowcaseType;

public class TypeSelectionAssistant extends Assistant {
	Block block;
	ItemStack stack;
	ShowcasePlayer player;
	
	public TypeSelectionAssistant(Block b, ItemStack s, ShowcasePlayer p) {
		super(p.getPlayer());
		this.block = b;
		this.stack = s;
		this.player = p;
		addPage(new AssistantPage(this) {
			
			{
				setTitle("Type selection");
				String text = "";
				Collection<ShowcaseType> types = ShowcaseType.getAllTypes();
				for(int i = 0;i<types.size();i++) {
					ShowcaseType type = types.iterator().next();
					text += type.getName();
					if(i + 1 < types.size()) {
						text+=", ";
					}
				}
				setText(text);
			}
			
			@Override
			public AssistantAction onPageInput(String text) {
				text = text.trim();
				text = text.toLowerCase();
				ShowcaseType type = ShowcaseType.getType(text);
				if(type != null) {
					Showcase sc = type.createShowcase(block, stack, player, null);
					sc.setShowcaseType(type);
					if(!sc.onCreate()) {
						sc.remove();
						return AssistantAction.CANCEL;
					}
					ShowcaseCreationEvent event = new ShowcaseCreationEvent(sc);
					Bukkit.getPluginManager().callEvent(event);
					if(!event.isCancelled()) {
						ShowcaseMain.getInstance().addShowcase(sc);
						return AssistantAction.FINISH;
					} else {
						sc.remove();
						return AssistantAction.CANCEL;
					}
				} else {
					return AssistantAction.CANCEL;
				}
			}
			
		});
	}

}
