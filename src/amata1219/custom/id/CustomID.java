package amata1219.custom.id;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomID extends JavaPlugin implements Listener {
    private static CustomID plugin;
    private HashMap<String, TabExecutor> commands;
    private List<String> cmds = new ArrayList();
    private HashMap<String, String> customIds = new HashMap();

    public CustomID() {
    }

    public void onEnable() {
        plugin = this;
        this.saveDefaultConfig();
        this.cmds = this.getConfig().getStringList("Commands");
        this.getConfig().getStringList("List").forEach((s) -> {
            String[] split = s.split(" # ");
            if (split.length == 2) {
                this.customIds.put(split[0], split[1]);
            }

        });
        this.commands = new HashMap();
        this.commands.put("customid", new MainCommand(plugin));
        this.getServer().getPluginManager().registerEvents(plugin, plugin);
    }

    public void onDisable() {
    }

    public static CustomID getPlugin() {
        return plugin;
    }

    public List<String> getCmds() {
        return this.cmds;
    }

    public void setCmds(List<String> cmds) {
        this.cmds = cmds;
    }

    public HashMap<String, String> getCustomIds() {
        return this.customIds;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return ((TabExecutor)this.commands.get(command.getName())).onCommand(sender, command, label, args);
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        String command = e.getMessage();
        boolean b = false;
        Iterator var5 = this.cmds.iterator();

        while(var5.hasNext()) {
            String c = (String)var5.next();
            if (command.startsWith(c)) {
                b = true;
                break;
            }
        }

        if (b) {
            String[] space = command.split(" ");
            StringBuilder sb = new StringBuilder();
            String[] var9 = space;
            int var8 = space.length;

            for(int var7 = 0; var7 < var8; ++var7) {
                String s = var9[var7];
                String arg = null;
                String[] comma = s.split(",");
                if (comma.length == 1) {
                    arg = s;
                } else {
                    StringBuilder cb = new StringBuilder();
                    String[] var16 = comma;
                    int var15 = comma.length;
                    int var14 = 0;

                    while(true) {
                        String c;
                        if (var14 >= var15) {
                            c = cb.toString();
                            arg = c.substring(0, c.length() - 1);
                            break;
                        }

                        c = var16[var14];
                        String ca = c;

                        String k;
                        for(Iterator var19 = this.customIds.keySet().iterator(); var19.hasNext(); ca = this.replaceAll(ca, k, (String)this.customIds.get(k))) {
                            k = (String)var19.next();
                        }

                        cb.append(ca + ",");
                        ++var14;
                    }
                }

                String k;
                for(Iterator var23 = this.customIds.keySet().iterator(); var23.hasNext(); arg = this.replaceAll(arg, k, (String)this.customIds.get(k))) {
                    k = (String)var23.next();
                }

                sb.append(arg + " ");
            }

            e.setMessage(sb.toString().trim());
            System.out.println("- replaced: " + e.getMessage());
        }
    }

    @EventHandler
    public void onServerCommand(ServerCommandEvent e) {
        String command = "/" + e.getCommand();
        boolean b = false;
        Iterator var5 = this.cmds.iterator();

        while(var5.hasNext()) {
            String c = (String)var5.next();
            if (command.startsWith(c)) {
                b = true;
                break;
            }
        }

        if (b) {
            String[] space = command.split(" ");
            StringBuilder sb = new StringBuilder();
            String[] var9 = space;
            int var8 = space.length;

            for(int var7 = 0; var7 < var8; ++var7) {
                String s = var9[var7];
                String arg = null;
                String[] comma = s.split(",");
                if (comma.length == 1) {
                    arg = s;
                } else {
                    StringBuilder cb = new StringBuilder();
                    String[] var16 = comma;
                    int var15 = comma.length;
                    int var14 = 0;

                    while(true) {
                        String c;
                        if (var14 >= var15) {
                            c = cb.toString();
                            arg = c.substring(0, c.length() - 1);
                            break;
                        }

                        c = var16[var14];
                        String ca = c;

                        String k;
                        for(Iterator var19 = this.customIds.keySet().iterator(); var19.hasNext(); ca = this.replaceAll(ca, k, (String)this.customIds.get(k))) {
                            k = (String)var19.next();
                        }

                        cb.append(ca + ",");
                        ++var14;
                    }
                }

                String k;
                for(Iterator var23 = this.customIds.keySet().iterator(); var23.hasNext(); arg = this.replaceAll(arg, k, (String)this.customIds.get(k))) {
                    k = (String)var23.next();
                }

                sb.append(arg + " ");
            }

            e.setCommand(sb.toString().trim().substring(1));
            System.out.println("- replaced: " + e.getCommand());
        }
    }

    private String replaceAll(String s, String regex, String replacement) {
        StringBuilder replace = new StringBuilder();
        int sl = s.length();
        int rl = regex.length();
        replace.append(s);
        boolean m = false;

        for(int i = 0; i < sl; ++i) {
            int start = replace.indexOf(regex, i);
            if (start == -1) {
                if (start == 0) {
                    return s;
                }

                return replace.toString();
            }

            replace = replace.replace(start, start + rl, replacement);
            m = true;
        }

        return !m ? s : replace.toString();
    }
}

