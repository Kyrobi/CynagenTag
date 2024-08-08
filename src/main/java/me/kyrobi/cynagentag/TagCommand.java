package me.kyrobi.cynagentag;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Random;

import static me.kyrobi.cynagentag.TagHandler.*;

public class TagCommand implements CommandExecutor {

    private CynagenTag plugin;

    public TagCommand(CynagenTag plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args){
        Player player = (Player) commandSender;
        if(player.isOp()|| player.getName().equals("Kyrobi")){
            String tagCommand = args[0];

            if(tagCommand.equals("start")){
                Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + " has started tag!");
                hasGameStarted = true;
                taggedCounter.clear();

                Collections.shuffle(onlinePlayers);
                Player setIt = onlinePlayers.get(0);
                currentlyTagged.add(setIt);

                extraTaggers.remove(setIt); // Remove it to that it can't pick this again

                Bukkit.broadcastMessage(ChatColor.GOLD + setIt.getName() + ChatColor.GREEN + " is it!");
                setIt.setGlowing(true);
            }
            else if(tagCommand.equals("stop")){
                Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " has stopped tag!");
                hasGameStarted = false;
                taggedCounter.clear();

                for(Player onlinePlayers: Bukkit.getOnlinePlayers()){
                    onlinePlayers.setGlowing(false);
                }
            }

            else if(tagCommand.equals("more")){
                Player extraIt = extraTaggers.remove();
                currentlyTagged.add(extraIt);
                Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + " is now ALSO it!");
            }

            else if(tagCommand.equals("clearglow")){
                for(Player player1: Bukkit.getOnlinePlayers()){
                    player1.setGlowing(false);
                }
            }
        }
        else{
            player.sendMessage(ChatColor.RED + "You can't use this command");
        }

        return false;
    }
}