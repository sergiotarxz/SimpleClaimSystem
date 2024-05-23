package fr.xyness.SCS;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.xyness.SCS.Config.ClaimSettings;

public class CPlayerMain {
	
	
	// ***************
	// *  Variables  *
	// ***************
	
	private static JavaPlugin plugin;
	private static Map<String,CPlayer> players = new HashMap<>();
    private static Map<String,Map<String,Double>> playersConfigSettings = new HashMap<>();
    
    
	// ******************
	// *  Constructors  *
	// ******************
    
	
    public CPlayerMain(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    
	// ********************
	// *  Others Methods  *
	// ********************
    
    
    // Get the CPlayer by his name
    public static CPlayer getCPlayer(String playerName) {
    	if(players.containsKey(playerName)) return players.get(playerName);
    	return null;
    }
    
    // Set the settings of player (from config.yml)
    public static void setPlayersConfigSettings(Map<String,Map<String,Double>> p) {
    	playersConfigSettings = p;
    }
	
	// Check if a player can add a member to his claim
    public static boolean canAddMember(Player player, Chunk chunk) {
    	if(player.hasPermission("scs.admin")) return true;
    	CPlayer cPlayer = players.get(player.getName());
    	int i = ClaimMain.getClaimMembers(chunk).size();
    	int nb_members = cPlayer.getMaxMembers();
    	return nb_members == 0 || nb_members > i;
    }
    
    // Method to set the perms of a player (when he joins)
    public static void addPlayerPermSetting(Player player) {
    	if(SimpleClaimSystem.isFolia()) {
    		Bukkit.getAsyncScheduler().runNow(plugin, task -> {
    			Map<String,Map<String,Double>> groupsSettings = ClaimSettings.getGroupsSettings();
    			LinkedHashMap<String,String> groups = ClaimSettings.getGroupsValues();
    	        Map<String,Double> groupPlayerSettings = new HashMap<>();
    			groupPlayerSettings.put("max-claims", groupsSettings.get("default").get("max-claims"));
    			groupPlayerSettings.put("max-radius-claims", groupsSettings.get("default").get("max-radius-claims"));
    			groupPlayerSettings.put("teleportation-delay", groupsSettings.get("default").get("teleportation-delay"));
    			groupPlayerSettings.put("max-members", groupsSettings.get("default").get("max-members"));
    			groupPlayerSettings.put("claim-cost", groupsSettings.get("default").get("claim-cost"));
    			groupPlayerSettings.put("claim-cost-multiplier", groupsSettings.get("default").get("claim-cost-multiplier"));
            	for(String group : groups.keySet()) {
            		if(player.hasPermission(groups.get(group))) {
            			groupPlayerSettings.put("max-claims", groupsSettings.get(group).get("max-claims"));
            			groupPlayerSettings.put("max-radius-claims", groupsSettings.get(group).get("max-radius-claims"));
            			groupPlayerSettings.put("teleportation-delay", groupsSettings.get(group).get("teleportation-delay"));
            			groupPlayerSettings.put("max-members", groupsSettings.get(group).get("max-members"));
            			groupPlayerSettings.put("claim-cost", groupsSettings.get(group).get("claim-cost"));
            			groupPlayerSettings.put("claim-cost-multiplier", groupsSettings.get(group).get("claim-cost-multiplier"));
            			break;
            		}
            	}
    	        
    	        String playerName = player.getName();
    	        if(!playersConfigSettings.containsKey(playerName)) {
    	        	players.put(playerName, new CPlayer(player,ClaimMain.getPlayerClaimsCount(playerName),
    	        			(int) Math.round(groupPlayerSettings.get("max-claims")),
    	        			(int) Math.round(groupPlayerSettings.get("max-radius-claims")),
    	        			(int) Math.round(groupPlayerSettings.get("teleportation-delay")),
    	        			(int) Math.round(groupPlayerSettings.get("max-members")),
    	        			groupPlayerSettings.get("claim-cost"),
    	        			groupPlayerSettings.get("claim-cost-multiplier")));
    	    		
    	        } else {
    	        	players.put(playerName, new CPlayer(player,ClaimMain.getPlayerClaimsCount(playerName),
    	        			(int) Math.round(playersConfigSettings.get(playerName).getOrDefault("max-claims", groupPlayerSettings.get("max-claims"))),
    	        			(int) Math.round(playersConfigSettings.get(playerName).getOrDefault("max-radius-claims", groupPlayerSettings.get("max-radius-claims"))),
    	        			(int) Math.round(playersConfigSettings.get(playerName).getOrDefault("teleportation-delay", groupPlayerSettings.get("teleportation-delay"))),
    	        			(int) Math.round(playersConfigSettings.get(playerName).getOrDefault("max-members", groupPlayerSettings.get("max-members"))),
    	        			playersConfigSettings.get(playerName).getOrDefault("claim-cost", groupPlayerSettings.get("claim-cost")),
    	        			playersConfigSettings.get(playerName).getOrDefault("claim-cost-multiplier", groupPlayerSettings.get("claim-cost-multiplier"))));
    	        }
    		});
    	} else {
    		Bukkit.getScheduler().runTaskAsynchronously(plugin, task -> {
    			Map<String,Map<String,Double>> groupsSettings = ClaimSettings.getGroupsSettings();
    			LinkedHashMap<String,String> groups = ClaimSettings.getGroupsValues();
    	        Map<String,Double> groupPlayerSettings = new HashMap<>();
    			groupPlayerSettings.put("max-claims", groupsSettings.get("default").get("max-claims"));
    			groupPlayerSettings.put("max-radius-claims", groupsSettings.get("default").get("max-radius-claims"));
    			groupPlayerSettings.put("teleportation-delay", groupsSettings.get("default").get("teleportation-delay"));
    			groupPlayerSettings.put("max-members", groupsSettings.get("default").get("max-members"));
    			groupPlayerSettings.put("claim-cost", groupsSettings.get("default").get("claim-cost"));
    			groupPlayerSettings.put("claim-cost-multiplier", groupsSettings.get("default").get("claim-cost-multiplier"));
            	for(String group : groups.keySet()) {
            		if(player.hasPermission(groups.get(group))) {
            			groupPlayerSettings.put("max-claims", groupsSettings.get(group).get("max-claims"));
            			groupPlayerSettings.put("max-radius-claims", groupsSettings.get(group).get("max-radius-claims"));
            			groupPlayerSettings.put("teleportation-delay", groupsSettings.get(group).get("teleportation-delay"));
            			groupPlayerSettings.put("max-members", groupsSettings.get(group).get("max-members"));
            			groupPlayerSettings.put("claim-cost", groupsSettings.get(group).get("claim-cost"));
            			groupPlayerSettings.put("claim-cost-multiplier", groupsSettings.get(group).get("claim-cost-multiplier"));
            			break;
            		}
            	}
    	        
    	        String playerName = player.getName();
    	        if(!playersConfigSettings.containsKey(playerName)) {
    	        	players.put(playerName, new CPlayer(player,ClaimMain.getPlayerClaimsCount(playerName),
    	        			(int) Math.round(groupPlayerSettings.get("max-claims")),
    	        			(int) Math.round(groupPlayerSettings.get("max-radius-claims")),
    	        			(int) Math.round(groupPlayerSettings.get("teleportation-delay")),
    	        			(int) Math.round(groupPlayerSettings.get("max-members")),
    	        			groupPlayerSettings.get("claim-cost"),
    	        			groupPlayerSettings.get("claim-cost-multiplier")));
    	    		
    	        } else {
    	        	players.put(playerName, new CPlayer(player,ClaimMain.getPlayerClaimsCount(playerName),
    	        			(int) Math.round(playersConfigSettings.get(playerName).getOrDefault("max-claims", groupPlayerSettings.get("max-claims"))),
    	        			(int) Math.round(playersConfigSettings.get(playerName).getOrDefault("max-radius-claims", groupPlayerSettings.get("max-radius-claims"))),
    	        			(int) Math.round(playersConfigSettings.get(playerName).getOrDefault("teleportation-delay", groupPlayerSettings.get("teleportation-delay"))),
    	        			(int) Math.round(playersConfigSettings.get(playerName).getOrDefault("max-members", groupPlayerSettings.get("max-members"))),
    	        			playersConfigSettings.get(playerName).getOrDefault("claim-cost", groupPlayerSettings.get("claim-cost")),
    	        			playersConfigSettings.get(playerName).getOrDefault("claim-cost-multiplier", groupPlayerSettings.get("claim-cost-multiplier"))));
    	        }
    		});
    	}
    }
}
