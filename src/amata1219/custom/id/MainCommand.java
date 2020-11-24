package amata1219.custom.id;

import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public class MainCommand implements TabExecutor {
    private CustomID plugin;

    public MainCommand(CustomID plugin) {
        this.plugin = plugin;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.AQUA + "CustomID - バニラIDに自動で置換されるカスタムIDを追加する");
            sender.sendMessage("");
            sender.sendMessage(ChatColor.AQUA + "/customid reload");
            sender.sendMessage(ChatColor.WHITE + "コンフィグをリロードします。");
            sender.sendMessage(ChatColor.AQUA + "/customid list");
            sender.sendMessage(ChatColor.WHITE + "登録したカスタムIDとバニラIDの対応表を表示します。");
            sender.sendMessage(ChatColor.AQUA + "/customid add [カスタムID] [バニラID]");
            sender.sendMessage(ChatColor.WHITE + "指定されたバニラIDに置換されるカスタムIDを追加します。");
            sender.sendMessage(ChatColor.AQUA + "/customid remove [カスタムID]");
            sender.sendMessage(ChatColor.WHITE + "指定されたカスタムIDを削除します。");
            sender.sendMessage("");
            sender.sendMessage(ChatColor.GRAY + "Developed by amata1219(Twitter@amata1219)");
            return true;
        } else if (args[0].equals("reload")) {
            this.plugin.reloadConfig();
            this.plugin.setCmds(this.plugin.getConfig().getStringList("Commands"));
            this.plugin.getCustomIds().clear();
            this.plugin.getConfig().getStringList("List").forEach((sx) -> {
                String[] split = sx.split(" # ");
                if (split.length == 2) {
                    this.plugin.getCustomIds().put(split[0], split[1]);
                }

            });
            sender.sendMessage(ChatColor.AQUA + "コンフィグをリロードしました。");
            return true;
        } else if (args[0].equals("list")) {
            sender.sendMessage(ChatColor.AQUA + "カスタムID対応表");
            sender.sendMessage(ChatColor.GRAY + "------------------------------");
            boolean b = false;

            for(Iterator var7 = this.plugin.getCustomIds().keySet().iterator(); var7.hasNext(); b = !b) {
                String s = (String)var7.next();
                sender.sendMessage((b ? ChatColor.AQUA : ChatColor.WHITE) + s + " # " + (String)this.plugin.getCustomIds().get(s));
            }

            sender.sendMessage(ChatColor.GRAY + "------------------------------");
            return true;
        } else {
            List list;
            if (args[0].equals("add")) {
                if (args.length != 1 && args.length != 2) {
                    if (this.plugin.getCustomIds().containsKey(args[1])) {
                        sender.sendMessage(ChatColor.RED + "指定されたカスタムIDは既に追加されています。");
                        return true;
                    } else {
                        list = this.plugin.getConfig().getStringList("List");
                        list.add(args[1] + " # " + args[2]);
                        this.plugin.getConfig().set("List", list);
                        this.updateConfig();
                        sender.sendMessage(ChatColor.AQUA + "カスタムID: " + args[1] + " # バニラID: " + args[2] + " を追加しました。");
                        this.plugin.getCustomIds().put(args[1], args[2]);
                        return true;
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "カスタムIDとバニラIDを指定して下さい。");
                    sender.sendMessage(ChatColor.GRAY + "/customid add [カスタムID] [バニラID]");
                    return true;
                }
            } else if (args[0].equals("remove")) {
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.RED + "カスタムIDを指定して下さい。");
                    sender.sendMessage(ChatColor.GRAY + "/customid remove [カスタムID]");
                    return true;
                } else if (!this.plugin.getCustomIds().containsKey(args[1])) {
                    sender.sendMessage(ChatColor.RED + "指定されたカスタムIDは追加されていません。");
                    return true;
                } else {
                    list = this.plugin.getConfig().getStringList("List");
                    list.remove(args[1] + " # " + (String)this.plugin.getCustomIds().get(args[1]));
                    this.plugin.getConfig().set("List", list);
                    this.updateConfig();
                    sender.sendMessage(ChatColor.AQUA + "カスタムID: " + args[1] + " # バニラID: " + (String)this.plugin.getCustomIds().get(args[1]) + " を削除しました。");
                    this.plugin.getCustomIds().remove(args[1]);
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    private void updateConfig() {
        this.plugin.saveConfig();
        this.plugin.reloadConfig();
    }
}

