package com.coopas.cbolao.Comandos;

import com.coopas.cbolao.Main;
import com.coopas.cbolao.Objetos.BolaoO;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BolaoC implements CommandExecutor {
    static BolaoO bolao = null;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        int nArgs = args.length;
        if (nArgs == 0) {
            if (!BolaoO.rolando) {
                naoTemBolao(sender);
                return true;
            }
            if (sender instanceof Player) {
                Player player = (Player)sender;
                int custo = bolao.getValor();
                boolean hasMoney = (Main.econ.getBalance(player.getName()) >= custo);
                if (hasMoney) {
                    if (!bolao.jogadores.contains(player)) {
                        adicionarPlayer(player);
                        Main.econ.withdrawPlayer(player.getName(), bolao.getValor());

                        for(String entrou : Main.msg.getConfig().getStringList("Mensagens.Bolao_Entrou")) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', entrou));
                        }

                        return true;
                    }
                    player.sendMessage(Main.msg.getConfig().getString("Mensagens.Avisos.JaParticipando")
                            .replace("&", "§"));
                } else {
                    player.sendMessage(Main.msg.getConfig().getString("Mensagens.Avisos.SemDinheiro")
                            .replace("&", "§"));
                    return true;
                }
            } else {
                sender.sendMessage(" ");
                sender.sendMessage("§4§l! §f§cApenas players!");
                sender.sendMessage(" ");
                return true;
            }
            return true;
        }
        if (nArgs == 1) {
            if (args[0].equalsIgnoreCase("iniciar")) {
                boolean isAdmin = sender.hasPermission(Main.config.getConfig().getString("Permissao"));
                if (isAdmin) {
                    if (BolaoO.rolando) {
                        sender.sendMessage(Main.msg.getConfig().getString("Mensagens.Avisos.TemBolao")
                                .replace("&", "§"));
                    } else {
                        sender.sendMessage(Main.msg.getConfig().getString("Mensagens.Avisos.ColoqueOValor")
                                .replace("&", "§"));
                    }

                } else {
                    sender.sendMessage(Main.msg.getConfig().getString("Mensagens.Avisos.SemPerm")
                            .replace("&", "§"));
                }
            } else {

                if (args[0].equalsIgnoreCase("status")) {
                    if (BolaoO.rolando) {

                        String inscricoes = String.valueOf(bolao.getValor());
                        String total = String.valueOf(bolao.getParticipantes() * bolao.getValor());

                        for(String status : Main.msg.getConfig().getStringList("Mensagens.Status")) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', status)
                                    .replace("[APOSTAS]",String.valueOf(bolao.getParticipantes()))
                                    .replace("[PINSCRICAO]", inscricoes)
                                    .replace("[PTOTAL]", total));
                        }
                    } else {
                        naoTemBolao(sender);
                    }
                } else {
                    if(args[0].equalsIgnoreCase("ajuda")) {

                        sender.sendMessage(" ");
                        sender.sendMessage("§6§lBOLAO:§f ");
                        sender.sendMessage(" ");
                        sender.sendMessage("§6§l-§f /Bolao Iniciar <VALOR> | Inicia o evento bolão");
                        sender.sendMessage("§6§l-§f /Bolao | Entra no evento bolão");
                        sender.sendMessage("§6§l-§f /Bolao Status | Vê o status atual do bolão");
                        sender.sendMessage(" ");
                    }
                }
            }

        }
         if (nArgs == 2) {
            if (args[0].equalsIgnoreCase("iniciar")) {
                boolean isAdmin = sender.hasPermission(Main.config.getConfig().getString("Permissao"));
                if (isAdmin) {
                    if (!BolaoO.rolando) {
                        iniciarBolao(Integer.parseInt(args[1]));
                        return true;
                    }
                    sender.sendMessage(Main.msg.getConfig().getString("Mensagens.Avisos.TemBolao")
                            .replace("&", "§"));
                } else {
                    sender.sendMessage(Main.msg.getConfig().getString("Mensagens.Avisos.SemPerm")
                            .replace("&", "§"));
                }
            }
        }
        return false;
    }

    private void iniciarBolao(int valor) {
        bolao = new BolaoO(valor, Main.config.getConfig().getInt("Bolao.Tempo"));
        bolao.iniciar();
    }

    private void naoTemBolao(CommandSender sender) {
        sender.sendMessage(Main.msg.getConfig().getString("Mensagens.Avisos.SemBolao")
                .replace("&", "§"));
    }

    private void adicionarPlayer(Player player) {
        bolao.addPlayer(player);
    }
}
