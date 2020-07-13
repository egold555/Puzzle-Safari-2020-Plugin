package org.golde.puzzlesafari.cmds.admin;

import org.bukkit.entity.Player;
import org.golde.puzzlesafari.challenges.archery.ChallengeArchery;
import org.golde.puzzlesafari.challenges.basketball.ChallengeBasketball;
import org.golde.puzzlesafari.cmds.base.AdminCommand;

public class CommandFix extends AdminCommand {

	private static final String HELP = "/fix <basketball | sheep>";
	
	@Override
	public void execute2(Player sender, String[] args) {
		
		if(args.length != 1) {
			sender.sendMessage(HELP);
			return;
		}
		
		String param = args[0].toLowerCase();
		if(param.equals("sheep")) {
			ChallengeArchery.ericFixCommand();
		}
		else if(param.equals("basketball")) {
			ChallengeBasketball.ericFixCommand();
		}
		else {
			sender.sendMessage(HELP);
			return;
		}
		sender.sendMessage("Success!");
	}

}
