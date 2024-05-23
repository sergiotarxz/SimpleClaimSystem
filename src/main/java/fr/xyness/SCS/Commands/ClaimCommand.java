package fr.xyness.SCS.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import fr.xyness.SCS.CPlayer;
import fr.xyness.SCS.CPlayerMain;
import fr.xyness.SCS.ClaimMain;
import fr.xyness.SCS.Config.ClaimLanguage;
import fr.xyness.SCS.Config.ClaimSettings;
import fr.xyness.SCS.Guis.ClaimGui;
import fr.xyness.SCS.Guis.ClaimListGui;
import fr.xyness.SCS.Guis.ClaimMembersGui;
import fr.xyness.SCS.Support.ClaimWorldGuard;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ClaimCommand implements CommandExecutor,TabCompleter {
	
	private static Set<Player> isOnCreate = new HashSet<>();
	
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if(sender instanceof Player) {
        	Player player = (Player) sender;
        	if(!player.hasPermission("scs.command.claim")) return completions;
	        if (args.length == 1) {
	        	if(player.hasPermission("scs.command.claim.settings")) completions.add("settings");
	            if(player.hasPermission("scs.command.claim.add")) completions.add("add");
	            if(player.hasPermission("scs.command.claim.remove")) completions.add("remove");
	            if(player.hasPermission("scs.command.claim.list")) completions.add("list");
	            if(player.hasPermission("scs.command.claim.setspawn")) completions.add("setspawn");
	            if(player.hasPermission("scs.command.claim.setname")) completions.add("setname");
	            if(player.hasPermission("scs.command.claim.members")) completions.add("members");
	            if(player.hasPermission("scs.command.claim.setdesc")) completions.add("setdesc");
	            if(player.hasPermission("scs.command.claim.chat")) completions.add("chat");
	            if(player.hasPermission("scs.command.claim.map")) completions.add("map");
	            if(player.hasPermission("scs.command.claim.autoclaim")) completions.add("autoclaim");
	            if(player.hasPermission("scs.command.claim.automap")) completions.add("automap");
	            if(player.hasPermission("scs.command.claim.see")) completions.add("see");
	            if(player.hasPermission("scs.command.claim.tp")) completions.add("tp");
	            if(player.hasPermission("scs.command.claim.ban")) completions.add("ban");
	            if(player.hasPermission("scs.command.claim.unban")) completions.add("unban");
	            return completions;
	        }
	        if (args.length == 2 && args[0].equalsIgnoreCase("see") && player.hasPermission("scs.command.claim.see.others")) {
	        	completions.addAll(ClaimMain.getClaimsOwners());
	        	return completions;
	        }
	        if (args.length == 2 && args[0].equalsIgnoreCase("setname") && player.hasPermission("scs.command.claim.setname")) {
	        	completions.addAll(ClaimMain.getClaimsNameFromOwner(player.getName()));
	        	return completions;
	        }
	        if (args.length == 2 && args[0].equalsIgnoreCase("chat") && player.hasPermission("scs.command.claim.chat")) {
	        	completions.addAll(ClaimMain.getClaimsNameFromOwner(player.getName()));
	        	return completions;
	        }
	        if (args.length == 2 && args[0].equalsIgnoreCase("setdesc") && player.hasPermission("scs.command.claim.setdesc")) {
	        	completions.addAll(ClaimMain.getClaimsNameFromOwner(player.getName()));
	        	return completions;
	        }
	        if (args.length == 2 && args[0].equalsIgnoreCase("settings") && player.hasPermission("scs.command.claim.settings")) {
	        	completions.addAll(ClaimMain.getClaimsNameFromOwner(player.getName()));
	        	return completions;
	        }
	        if (args.length == 2 && args[0].equalsIgnoreCase("tp") && player.hasPermission("scs.command.claim.tp")) {
	        	completions.addAll(ClaimMain.getClaimsNameFromOwner(player.getName()));
	        	return completions;
	        }
	        if (args.length == 2 && args[0].equalsIgnoreCase("add") && player.hasPermission("scs.command.claim.add")) {
	        	completions.add("*");
	        	completions.addAll(ClaimMain.getClaimsNameFromOwner(player.getName()));
	        	return completions;
	        }
	        if (args.length == 2 && args[0].equalsIgnoreCase("ban") && player.hasPermission("scs.command.claim.ban")) {
	        	completions.add("*");
	        	completions.addAll(ClaimMain.getClaimsNameFromOwner(player.getName()));
	        	return completions;
	        }
	        if (args.length == 2 && args[0].equalsIgnoreCase("unban") && player.hasPermission("scs.command.claim.unban")) {
	        	completions.add("*");
	        	completions.addAll(ClaimMain.getClaimsNameFromOwner(player.getName()));
	        	return completions;
	        }
	        if (args.length == 2 && args[0].equalsIgnoreCase("remove") && player.hasPermission("scs.command.claim.remove")) {
	        	completions.add("*");
	        	completions.addAll(ClaimMain.getClaimsNameFromOwner(player.getName()));
	        	return completions;
	        }
	        if (args.length == 2 && args[0].equalsIgnoreCase("members") && player.hasPermission("scs.command.claim.members")) {
	        	completions.addAll(ClaimMain.getClaimsNameFromOwner(player.getName()));
	        	return completions;
	        }
	        if (args.length == 3 && args[0].equalsIgnoreCase("remove") && player.hasPermission("scs.command.claim.remove")) {
	        	Chunk chunk = ClaimMain.getChunkByClaimName(player.getName(), args[1]);
	        	completions.addAll(ClaimMain.getClaimMembers(chunk));
	        	completions.remove(player.getName());
	        	return completions;
	        }
	        if (args.length == 3 && args[0].equalsIgnoreCase("unban") && player.hasPermission("scs.command.claim.unban")) {
	        	Chunk chunk = ClaimMain.getChunkByClaimName(player.getName(), args[1]);
	        	completions.addAll(ClaimMain.getClaimBans(chunk));
	        	return completions;
	        }
	        if (args.length == 3 && args[0].equalsIgnoreCase("add") && player.hasPermission("scs.command.claim.add")) {
	        	for(OfflinePlayer p : Bukkit.getOfflinePlayers()) {
	        		completions.add(p.getName());
	        	}
	        	completions.remove(player.getName());
	        	return completions;
	        }
	        if (args.length == 3 && args[0].equalsIgnoreCase("ban") && player.hasPermission("scs.command.claim.ban")) {
	        	for(OfflinePlayer p : Bukkit.getOfflinePlayers()) {
	        		completions.add(p.getName());
	        	}
	        	completions.remove(player.getName());
	        	return completions;
	        }
        }

        return completions;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	
        if (!(sender instanceof Player)) {
            sender.sendMessage(ClaimLanguage.getMessage("command-only-by-players"));
            return true;
        }

        Player player = (Player) sender;
        String playerName = player.getName();
        CPlayer cPlayer = CPlayerMain.getCPlayer(playerName);
        
        if(!player.hasPermission("scs.command.claim")) {
        	sender.sendMessage(ClaimLanguage.getMessage("cmd-no-permission"));
        	return false;
        }
        
        if (args.length > 1 && args[0].equals("setdesc")) {
        	if (!player.hasPermission("scs.command.claim.setdesc")) {
            	player.sendMessage(ClaimLanguage.getMessage("cmd-no-permission"));
            	return false;
        	}
        	if (ClaimMain.getClaimsNameFromOwner(playerName).contains(args[1])) {
        		String description = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
    			if(description.length() > Integer.parseInt(ClaimSettings.getSetting("max-length-claim-description"))) {
    				player.sendMessage(ClaimLanguage.getMessage("claim-description-too-long"));
    				return true;
    			}
        		Chunk chunk = ClaimMain.getChunkByClaimName(playerName, args[1]);
            	if(ClaimMain.setChunkDescription(player, chunk, description)) {
            		player.sendMessage(ClaimLanguage.getMessage("claim-set-description-success").replaceAll("%name%", args[1]).replaceAll("%description%", description));
            		return true;
            	}
        		player.sendMessage(ClaimLanguage.getMessage("error"));
        		return true;
        	}
    		player.sendMessage(ClaimLanguage.getMessage("claim-player-not-found"));
    		return true;
        }
        
        if (args.length == 3) {
        	if(args[0].equalsIgnoreCase("setname")) {
            	if (!player.hasPermission("scs.command.claim.setname")) {
                	player.sendMessage(ClaimLanguage.getMessage("cmd-no-permission"));
                	return false;
            	}
        		if (ClaimMain.getClaimsNameFromOwner(playerName).contains(args[1])) {
        			if(args[2].length() > Integer.parseInt(ClaimSettings.getSetting("max-length-claim-name"))) {
        				player.sendMessage(ClaimLanguage.getMessage("claim-name-too-long"));
        				return true;
        			}
        			if(args[2].contains("claim-")) {
        				player.sendMessage(ClaimLanguage.getMessage("you-cannot-use-this-name"));
        				return true;
        			}
            		if(ClaimMain.checkName(playerName,args[2])) {
            			Chunk chunk = ClaimMain.getChunkByClaimName(playerName, args[1]);
                    	ClaimMain.setClaimName(player,chunk, args[2]);
                    	player.sendMessage(ClaimLanguage.getMessage("name-change-success").replaceAll("%name%", args[2]));
                    	return true;
            		}
            		player.sendMessage(ClaimLanguage.getMessage("error-name-exists").replaceAll("%name%", args[1]));
                	return true;
        		}
        		player.sendMessage(ClaimLanguage.getMessage("claim-player-not-found"));
        		return true;
        	}
        	if(args[0].equalsIgnoreCase("ban")) {
            	if (!player.hasPermission("scs.command.claim.ban")) {
                	player.sendMessage(ClaimLanguage.getMessage("cmd-no-permission"));
                	return false;
            	}
        		if(args[1].equalsIgnoreCase("*")) {
            		if(cPlayer.getClaimsCount() == 0) {
            			player.sendMessage(ClaimLanguage.getMessage("player-has-no-claim"));
            			return true;
            		}
            		OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);
            		if(!target.hasPlayedBefore()) {
            			player.sendMessage(ClaimLanguage.getMessage("player-never-played").replaceAll("%player%", args[2]));
            			return true;
            		}
            		String targetName = target.getName();
            		if(targetName.equals(player.getName())) {
            			player.sendMessage(ClaimLanguage.getMessage("cant-ban-yourself"));
            			return true;
            		}
	        		if(ClaimMain.addAllClaimBan(player, targetName)) {
	        			String message = ClaimLanguage.getMessage("add-ban-all-success").replaceAll("%player%", targetName);
	        			player.sendMessage(message);
	        			return true;
	        		}
	        		player.sendMessage(ClaimLanguage.getMessage("error"));
	        		return true;
        		}
        		Chunk chunk = ClaimMain.getChunkByClaimName(playerName, args[1]);
        		if(chunk == null) {
        			player.sendMessage(ClaimLanguage.getMessage("claim-player-not-found"));
        			return true;
        		}
        		OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);
        		if(!target.hasPlayedBefore() && !target.isOnline()) {
        			player.sendMessage(ClaimLanguage.getMessage("player-never-played").replaceAll("%player%", args[2]));
        			return true;
        		}
        		String targetName = target.getName();
        		if(targetName.equals(player.getName())) {
        			player.sendMessage(ClaimLanguage.getMessage("cant-ban-yourself"));
        			return true;
        		}
        		if(ClaimMain.addClaimMembers(player, chunk, targetName)) {
        			String message = ClaimLanguage.getMessage("add-ban-success").replaceAll("%player%", targetName).replaceAll("%claim-name%", ClaimMain.getClaimNameByChunk(chunk));
        			player.sendMessage(message);
        			return true;
        		}
        		player.sendMessage(ClaimLanguage.getMessage("error"));
        		return true;
        	}
        	if(args[0].equalsIgnoreCase("unban")) {
            	if (!player.hasPermission("scs.command.claim.unban")) {
                	player.sendMessage(ClaimLanguage.getMessage("cmd-no-permission"));
                	return false;
            	}
        		if(args[1].equalsIgnoreCase("*")) {
            		if(cPlayer.getClaimsCount() == 0) {
            			player.sendMessage(ClaimLanguage.getMessage("player-has-no-claim"));
            			return true;
            		}
            		OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);
            		if(!target.hasPlayedBefore() && !target.isOnline()) {
            			player.sendMessage(ClaimLanguage.getMessage("player-never-played").replaceAll("%player%", args[2]));
            			return true;
            		}
            		String targetName = target.getName();
	        		if(ClaimMain.removeAllClaimBan(player, targetName)) {
	        			String message = ClaimLanguage.getMessage("remove-ban-all-success").replaceAll("%player%", targetName);
	        			player.sendMessage(message);
	        			return true;
	        		}
	        		player.sendMessage(ClaimLanguage.getMessage("error"));
	        		return true;
        		}
        		Chunk chunk = ClaimMain.getChunkByClaimName(playerName, args[1]);
        		if(chunk == null) {
        			player.sendMessage(ClaimLanguage.getMessage("claim-player-not-found"));
        			return true;
        		}
        		OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);
        		if(!target.hasPlayedBefore()) {
        			player.sendMessage(ClaimLanguage.getMessage("player-never-played").replaceAll("%player%", args[2]));
        			return true;
        		}
        		String targetName = target.getName();
        		if(!ClaimMain.getClaimBans(chunk).contains(targetName)) {
        			String message = ClaimLanguage.getMessage("not-banned").replaceAll("%player%", targetName);
        			player.sendMessage(message);
        			return true;
        		}
        		if(ClaimMain.removeClaimBan(player, chunk, targetName)) {
        			String message = ClaimLanguage.getMessage("remove-ban-success").replaceAll("%player%", targetName).replaceAll("%claim-name%", ClaimMain.getClaimNameByChunk(chunk));
        			player.sendMessage(message);
        			return true;
        		}
        		player.sendMessage(ClaimLanguage.getMessage("error"));
        		return true;
        	}
        	if(args[0].equalsIgnoreCase("add")) {
            	if (!player.hasPermission("scs.command.claim.add")) {
                	player.sendMessage(ClaimLanguage.getMessage("cmd-no-permission"));
                	return false;
            	}
        		if(args[1].equalsIgnoreCase("*")) {
            		if(cPlayer.getClaimsCount() == 0) {
            			player.sendMessage(ClaimLanguage.getMessage("player-has-no-claim"));
            			return true;
            		}
        			Set<Chunk> chunks = ClaimMain.getChunksFromOwner(playerName);
        			for(Chunk c : chunks) {
                		if(!CPlayerMain.canAddMember(player, c)) {
                			player.sendMessage(ClaimLanguage.getMessage("cant-add-member-anymore"));
                			return true;
                		}
        			}
            		OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);
            		if(!target.hasPlayedBefore()) {
            			player.sendMessage(ClaimLanguage.getMessage("player-never-played").replaceAll("%player%", args[2]));
            			return true;
            		}
            		String targetName = target.getName();
            		if(targetName.equals(player.getName())) {
            			player.sendMessage(ClaimLanguage.getMessage("cant-add-yourself"));
            			return true;
            		}
	        		if(ClaimMain.addAllClaimMembers(player, targetName)) {
	        			String message = ClaimLanguage.getMessage("add-member-success").replaceAll("%player%", targetName);
	        			player.sendMessage(message);
	        			return true;
	        		}
	        		player.sendMessage(ClaimLanguage.getMessage("error"));
	        		return true;
        		}
        		Chunk chunk = ClaimMain.getChunkByClaimName(playerName, args[1]);
        		if(chunk == null) {
        			player.sendMessage(ClaimLanguage.getMessage("claim-player-not-found"));
        			return true;
        		}
        		if(!CPlayerMain.canAddMember(player, chunk)) {
        			player.sendMessage(ClaimLanguage.getMessage("cant-add-member-anymore"));
        			return true;
        		}
        		OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);
        		if(!target.hasPlayedBefore() && !target.isOnline()) {
        			player.sendMessage(ClaimLanguage.getMessage("player-never-played").replaceAll("%player%", args[2]));
        			return true;
        		}
        		String targetName = target.getName();
        		if(targetName.equals(player.getName())) {
        			player.sendMessage(ClaimLanguage.getMessage("cant-add-yourself"));
        			return true;
        		}
        		if(ClaimMain.addClaimMembers(player, chunk, targetName)) {
        			String message = ClaimLanguage.getMessage("add-member-success").replaceAll("%player%", targetName);
        			player.sendMessage(message);
        			return true;
        		}
        		player.sendMessage(ClaimLanguage.getMessage("error"));
        		return true;
        	}
        	if(args[0].equalsIgnoreCase("remove")) {
            	if (!player.hasPermission("scs.command.claim.remove")) {
                	player.sendMessage(ClaimLanguage.getMessage("cmd-no-permission"));
                	return false;
            	}
        		if(args[1].equalsIgnoreCase("*")) {
            		if(cPlayer.getClaimsCount() == 0) {
            			player.sendMessage(ClaimLanguage.getMessage("player-has-no-claim"));
            			return true;
            		}
        			if(args[2].equals(player.getName())) {
            			player.sendMessage(ClaimLanguage.getMessage("cant-remove-owner"));
            			return true;
        			}
            		OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);
            		if(!target.hasPlayedBefore() && !target.isOnline()) {
            			player.sendMessage(ClaimLanguage.getMessage("player-never-played").replaceAll("%player%", args[2]));
            			return true;
            		}
            		String targetName = target.getName();
	        		if(ClaimMain.removeAllClaimMembers(player, targetName)) {
	        			String message = ClaimLanguage.getMessage("remove-member-success").replaceAll("%player%", targetName);
	        			player.sendMessage(message);
	        			return true;
	        		}
	        		player.sendMessage(ClaimLanguage.getMessage("error"));
	        		return true;
        		}
        		Chunk chunk = ClaimMain.getChunkByClaimName(playerName, args[1]);
        		if(chunk == null) {
        			player.sendMessage(ClaimLanguage.getMessage("claim-player-not-found"));
        			return true;
        		}
        		OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);
        		if(!target.hasPlayedBefore()) {
        			player.sendMessage(ClaimLanguage.getMessage("player-never-played").replaceAll("%player%", args[2]));
        			return true;
        		}
        		String targetName = target.getName();
    			if(targetName.equals(player.getName())) {
        			player.sendMessage(ClaimLanguage.getMessage("cant-remove-owner"));
        			return true;
    			}
        		if(!ClaimMain.getClaimMembers(chunk).contains(targetName)) {
        			String message = ClaimLanguage.getMessage("not-member").replaceAll("%player%", targetName);
        			player.sendMessage(message);
        			return true;
        		}
        		if(ClaimMain.removeClaimMembers(player, chunk, targetName)) {
        			String message = ClaimLanguage.getMessage("remove-member-success").replaceAll("%player%", targetName);
        			player.sendMessage(message);
        			return true;
        		}
        		player.sendMessage(ClaimLanguage.getMessage("error"));
        		return true;
        	}
        }
        
        if (args.length == 2) {
        	if(args[0].equalsIgnoreCase("see")) {
        		if(!player.hasPermission("scs.command.claim.see.others")) {
        			player.sendMessage(ClaimLanguage.getMessage("cmd-no-permission"));
        			return true;
        		}
        		if(ClaimMain.getPlayerClaimsCount(args[1]) == 0) {
        			player.sendMessage(ClaimLanguage.getMessage("target-does-not-have-claim").replaceAll("%name%", args[1]));
        			return true;
        		}
        		Set<Chunk> chunks = ClaimMain.getChunksFromOwner(args[1]);
        		for(Chunk c : chunks) {
        			ClaimMain.displayChunk(player,c);
        		}
        		return true;
        	}
        	if(args[0].equalsIgnoreCase("settings")) {
            	if (!player.hasPermission("scs.command.claim.settings")) {
                	player.sendMessage(ClaimLanguage.getMessage("cmd-no-permission"));
                	return false;
            	}
        		Chunk chunk = ClaimMain.getChunkByClaimName(playerName, args[1]);
        		if(chunk == null) {
        			player.sendMessage(ClaimLanguage.getMessage("claim-player-not-found"));
        			return true;
        		}
        		ClaimGui menu = new ClaimGui(player,chunk);
        		menu.openInventory(player);
        		return true;
        	}
        	if(args[0].equalsIgnoreCase("members")) {
            	if (!player.hasPermission("scs.command.claim.members")) {
                	player.sendMessage(ClaimLanguage.getMessage("cmd-no-permission"));
                	return false;
            	}
        		Chunk chunk = ClaimMain.getChunkByClaimName(playerName, args[1]);
        		if(chunk == null) {
        			player.sendMessage(ClaimLanguage.getMessage("claim-player-not-found"));
        			return true;
        		}
        		cPlayer.setGuiPage(1);
        		ClaimMembersGui menu = new ClaimMembersGui(player,chunk,1);
        		menu.openInventory(player);
        		return true;
        	}
        	if(args[0].equalsIgnoreCase("tp")) {
        		if(!player.hasPermission("scs.command.claim.tp")) {
        			player.sendMessage(ClaimLanguage.getMessage("cmd-no-permission"));
        			return true;
        		}
        		Chunk chunk = ClaimMain.getChunkByClaimName(playerName, args[1]);
        		if(chunk == null) {
        			player.sendMessage(ClaimLanguage.getMessage("claim-player-not-found"));
        			return true;
        		}
        		ClaimMain.goClaim(player, ClaimMain.getClaimLocationByChunk(chunk));
        		return true;
        	}
        }
        
        if (args.length == 1) {
        	if(args[0].equalsIgnoreCase("chat")) {
            	if (!player.hasPermission("scs.command.claim.chat")) {
                	player.sendMessage(ClaimLanguage.getMessage("cmd-no-permission"));
                	return false;
            	}
        		if(cPlayer.getClaimChat()) {
            		cPlayer.setClaimChat(false);
            		player.sendMessage(ClaimLanguage.getMessage("talking-now-in-public"));
            		return true;
        		}
        		cPlayer.setClaimChat(true);
        		player.sendMessage(ClaimLanguage.getMessage("talking-now-in-claim"));
        		return true;
        	}
        	if(args[0].equalsIgnoreCase("automap")) {
        		if(!player.hasPermission("scs.command.claim.automap")) {
        			player.sendMessage(ClaimLanguage.getMessage("cmd-no-permission"));
        			return true;
        		}
        		if(cPlayer.getClaimAutomap()) {
        			cPlayer.setClaimAutomap(false);
        			player.sendMessage(ClaimLanguage.getMessage("automap-off"));
        			return true;
        		}
    			cPlayer.setClaimAutomap(true);
    			player.sendMessage(ClaimLanguage.getMessage("automap-on"));
    			return true;
        	}
        	if(args[0].equalsIgnoreCase("map")) {
        		if(!player.hasPermission("scs.command.claim.map")) {
        			player.sendMessage(ClaimLanguage.getMessage("cmd-no-permission"));
        			return true;
        		}
        		ClaimMain.getMap(player,player.getLocation().getChunk());
        		return true;
        	}
        	if(args[0].equalsIgnoreCase("autoclaim")) {
        		if(!player.hasPermission("scs.command.claim.autoclaim")) {
        			player.sendMessage(ClaimLanguage.getMessage("cmd-no-permission"));
        			return true;
        		}
        		if(cPlayer.getClaimAutoclaim()) {
        			cPlayer.setClaimAutoclaim(false);
        			player.sendMessage(ClaimLanguage.getMessage("autoclaim-off"));
        			return true;
        		}
    			cPlayer.setClaimAutoclaim(true);
    			player.sendMessage(ClaimLanguage.getMessage("autoclaim-on"));
    			return true;
        	}
        	if(args[0].equalsIgnoreCase("setspawn")) {
            	if (!player.hasPermission("scs.command.claim.setspawn")) {
                	player.sendMessage(ClaimLanguage.getMessage("cmd-no-permission"));
                	return false;
            	}
        		Chunk chunk = player.getLocation().getChunk();
        		String owner = ClaimMain.getOwnerInClaim(chunk);
        		if(!ClaimMain.checkIfClaimExists(chunk)) {
        			player.sendMessage(ClaimLanguage.getMessage("free-territory"));
        			return true;
        		}
        		if(!owner.equals(player.getName())) {
        			player.sendMessage(ClaimLanguage.getMessage("territory-not-yours"));
        			return true;
        		}
            	Location l = player.getLocation();
            	ClaimMain.setClaimLocation(player, chunk, l);
            	player.sendMessage(ClaimLanguage.getMessage("loc-change-success").replaceAll("%coords%", ClaimMain.getClaimCoords(chunk)));
            	return true;
        	}
        	if(args[0].equalsIgnoreCase("settings")) {
            	if (!player.hasPermission("scs.command.claim.settings")) {
                	player.sendMessage(ClaimLanguage.getMessage("cmd-no-permission"));
                	return false;
            	}
        		Chunk chunk = player.getLocation().getChunk();
        		String owner = ClaimMain.getOwnerInClaim(chunk);
        		if(!ClaimMain.checkIfClaimExists(chunk)) {
        			player.sendMessage(ClaimLanguage.getMessage("free-territory"));
        			return true;
        		}
        		if(!owner.equals(player.getName())) {
        			player.sendMessage(ClaimLanguage.getMessage("territory-not-yours"));
        			return true;
        		}
                ClaimGui menu = new ClaimGui(player,chunk);
                menu.openInventory(player);
                return true;
        	}
        	if(args[0].equalsIgnoreCase("members")) {
            	if (!player.hasPermission("scs.command.claim.members")) {
                	player.sendMessage(ClaimLanguage.getMessage("cmd-no-permission"));
                	return false;
            	}
        		Chunk chunk = player.getLocation().getChunk();
        		String owner = ClaimMain.getOwnerInClaim(chunk);
        		if(!ClaimMain.checkIfClaimExists(chunk)) {
        			player.sendMessage(ClaimLanguage.getMessage("free-territory"));
        			return true;
        		}
        		if(!owner.equals(player.getName())) {
        			player.sendMessage(ClaimLanguage.getMessage("territory-not-yours"));
        			return true;
        		}
        		cPlayer.setGuiPage(1);
                ClaimMembersGui menu = new ClaimMembersGui(player,chunk,1);
                menu.openInventory(player);
                return true;
        	}
        	if(args[0].equalsIgnoreCase("list")) {
            	if (!player.hasPermission("scs.command.claim.list")) {
                	player.sendMessage(ClaimLanguage.getMessage("cmd-no-permission"));
                	return false;
            	}
            	cPlayer.setGuiPage(1);
        		ClaimListGui.removeLastChunk(player);
                ClaimListGui menu = new ClaimListGui(player,1);
                menu.openInventory(player);
        		return true;
        	}
        	if(args[0].equalsIgnoreCase("see")) {
        		if(!player.hasPermission("scs.command.claim.see")) {
        			player.sendMessage(ClaimLanguage.getMessage("cmd-no-permission"));
        			return true;
        		}
        		ClaimMain.displayChunk(player,player.getLocation().getChunk());
        		return true;
        	}
        	if (!player.hasPermission("scs.claim.radius")) {
            	player.sendMessage(ClaimLanguage.getMessage("cmd-no-permission"));
            	return false;
        	}
            if(ClaimSettings.isWorldDisabled(player.getWorld().getName())) {
            	player.sendMessage(ClaimLanguage.getMessage("world-disabled").replaceAll("%world%", player.getWorld().getName()));
            	return true;
            }
    		try {
    			int radius = Integer.parseInt(args[0]);
        		if(!player.hasPermission("scs.command.claim.radius")) {
        			player.sendMessage(ClaimLanguage.getMessage("cmd-no-permission"));
        			return true;
        		}
    			if(!cPlayer.canRadiusClaim(radius)) {
    				player.sendMessage(ClaimLanguage.getMessage("cant-radius-claim"));
    				return true;
    			}
    			Set<Chunk> chunks = new HashSet<>(getChunksInRadius(player.getLocation(),radius));
    	        if(ClaimSettings.getBooleanSetting("claim-confirmation")) {
    	            if(isOnCreate.contains(player)) {
    	            	isOnCreate.remove(player);
    	            	ClaimMain.createClaimRadius(player, chunks, radius);
    	    			return true;
    	            }
    	            isOnCreate.add(player);
    	            String AnswerA = ClaimLanguage.getMessage("claim-confirmation-button");
    	            TextComponent AnswerA_C = new TextComponent(AnswerA);
    	            AnswerA_C.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ClaimLanguage.getMessage("claim-confirmation-button")).create()));
    	            AnswerA_C.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/claim "+String.valueOf(radius)));
    	            TextComponent finale = new TextComponent(ClaimLanguage.getMessage("claim-confirmation-ask"));
    	            finale.addExtra(AnswerA_C);
    	            player.sendMessage(finale);
    	            return true;
    	        }
    			ClaimMain.createClaimRadius(player, chunks, radius);
    			return true;
    		} catch(NumberFormatException e){
    			ClaimMain.getHelp(player,args[0],"claim");
                return true;
    		}
        }
        
        if(args.length > 0) {
        	ClaimMain.getHelp(player,args[0],"claim");
        	return true;
        }
        
    	if (!player.hasPermission("scs.claim")) {
        	player.sendMessage(ClaimLanguage.getMessage("cmd-no-permission"));
        	return false;
    	}
        
        if(ClaimSettings.isWorldDisabled(player.getWorld().getName())) {
        	player.sendMessage(ClaimLanguage.getMessage("world-disabled").replaceAll("%world%", player.getWorld().getName()));
        	return true;
        }
        
        if(ClaimSettings.getBooleanSetting("worldguard")) {
        	if(!ClaimWorldGuard.checkFlagClaim(player)) {
        		player.sendMessage(ClaimLanguage.getMessage("worldguard-cannot-claim-in-region"));
        		return true;
        	}
        }
        
        if(ClaimSettings.getBooleanSetting("claim-confirmation")) {
            if(isOnCreate.contains(player)) {
            	isOnCreate.remove(player);
                Chunk chunk = player.getLocation().getChunk();
                ClaimMain.createClaim(player, chunk);
                return true;
            }
            isOnCreate.add(player);
            ClaimMain.displayChunk(player,player.getChunk());
            String AnswerA = ClaimLanguage.getMessage("claim-confirmation-button");
            TextComponent AnswerA_C = new TextComponent(AnswerA);
            AnswerA_C.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ClaimLanguage.getMessage("claim-confirmation-button")).create()));
            AnswerA_C.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/claim"));
            TextComponent finale = new TextComponent(ClaimLanguage.getMessage("claim-confirmation-ask"));
            finale.addExtra(AnswerA_C);
            player.sendMessage(finale);
            return true;
        }

        Chunk chunk = player.getLocation().getChunk();
        ClaimMain.createClaim(player, chunk);

        return true;
    }
    
    public static List<Chunk> getChunksInRadius(Location center, int radius) {
        List<Chunk> chunks = new ArrayList<>();
        Chunk centerChunk = center.getChunk();
        int startX = centerChunk.getX() - radius;
        int startZ = centerChunk.getZ() - radius;
        int endX = centerChunk.getX() + radius;
        int endZ = centerChunk.getZ() + radius;

        for (int x = startX; x <= endX; x++) {
            for (int z = startZ; z <= endZ; z++) {
                chunks.add(center.getWorld().getChunkAt(x, z));
            }
        }
        return chunks;
    }
}
