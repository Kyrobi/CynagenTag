package me.kyrobi.cynagentag;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.reflect.Array;
import java.util.*;

public class TagHandler implements Listener {

    CynagenTag plugin;
    public static ArrayList<Player> currentlyTagged = new ArrayList<>();
    public static HashMap<String, Integer> taggedCounter = new HashMap<>();
    public static boolean hasGameStarted = false;
    public static ArrayList<Player> onlinePlayers = new ArrayList<>();

    public static Queue<Player> extraTaggers = new LinkedList<>();
    public TagHandler(final CynagenTag plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        for(Player player: Bukkit.getOnlinePlayers()){
            onlinePlayers.add(player);
            extraTaggers.add(player);
        }

    }

    @EventHandler
    public void onGettingHit(EntityDamageByEntityEvent e){

        if(!hasGameStarted){ return; }

        Entity entity = e.getEntity();
        Entity attacker = e.getDamager();

        if (entity instanceof Player)
        {
            if (attacker instanceof Player)
            {
                Player newIt = ((Player) entity).getPlayer();
                Player prevIt = ((Player) attacker).getPlayer();

                System.out.println("It player: " + newIt.getName());
                System.out.println("PrevIt: " + prevIt.getName());
                if(currentlyTagged.contains(prevIt)){
                    // Remove speactral effect
                    newIt.setGlowing(true);
                    prevIt.setGlowing(false);

                    Bukkit.broadcastMessage(ChatColor.GOLD + newIt.getName() + ChatColor.GREEN + " is now it!");

                    String title = ChatColor.translateAlternateColorCodes('&',"&cYou're it!");
                    String subtitle = ChatColor.translateAlternateColorCodes('&',"&aGo tag someone else!");
                    newIt.sendTitle(title, subtitle, 0, 20 * 2, 20 * 3); //time in ticks

                    currentlyTagged.remove(prevIt);
                    currentlyTagged.add(newIt);
                }

            }
        }
    }

    /*
    If a player leaves, we pass the "it" to a next online player
     */
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        Player player = e.getPlayer();
        onlinePlayers.remove(player);
        extraTaggers.remove(player);

        if((currentlyTagged.contains(player)) && hasGameStarted){
            currentlyTagged.remove(player);
            player.setGlowing(false);

            Collections.shuffle(onlinePlayers);
            currentlyTagged.add(onlinePlayers.get(0));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        extraTaggers.add(e.getPlayer());
        onlinePlayers.add(e.getPlayer());
    }
}