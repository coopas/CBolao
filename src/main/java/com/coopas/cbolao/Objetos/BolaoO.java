package com.coopas.cbolao.Objetos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.coopas.cbolao.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class BolaoO {
    private int valor;

    private int tempoMinutos;

    private int tempoSegundos;

    public static boolean rolando = false;

    private int participantes = 0;

    public List<Player> jogadores = new ArrayList<>();

    private BukkitTask task;

    private BukkitTask task2;

    public BolaoO(int valor, int tempoMinutos) {
        this.valor = valor;
        this.tempoMinutos = tempoMinutos;
    }

    public void aumentarUmParticipante() {
        this.participantes++;
    }

    public int getParticipantes() {
        return this.participantes;
    }

    public void addPlayer(Player player) {
        this.jogadores.add(player);
        aumentarUmParticipante();
    }

    public List<Player> getPlayers() {
        return this.jogadores;
    }

    public int getValor() {
        return this.valor;
    }

    public int getTempoMinutos() {
        return this.tempoMinutos;
    }

    private String getTempoRestante() {
        int minutos = this.tempoSegundos / 60;
        int segundos = this.tempoSegundos % 60;
        if (minutos > 0) {
            if (segundos > 0) {
                if (minutos == 1 && segundos != 1)
                    return String.valueOf("§c" +minutos) + " §fminuto e §c" + segundos + " §fsegundos";
                if (minutos != 1 && segundos != 1)
                    return String.valueOf("§c" +minutos) + " §fminutos e §c" + segundos + " §fsegundos";
                return String.valueOf("§c" + minutos) + " §fminuto e §c" + segundos + " §fsegundo";
            }
            if (minutos != 1)
                return String.valueOf("§c" +minutos) + " §fminutos";
            return String.valueOf("§c" +minutos) + " §fminuto";
        }
        if (segundos > 0) {
            if (segundos == 1)
                return String.valueOf("§c" +segundos) + " §fsegundo";
            return String.valueOf("§c" +segundos) + " §fsegundos";
        }
        return null;
    }

    public void iniciar() {

        rolando = true;
        rodarTimer();

        for(Player som : Bukkit.getOnlinePlayers()) {

            som.playSound(som.getLocation(), Sound.LEVEL_UP, 20, 100);

        }

       for (String iniciar : Main.msg.getConfig().getStringList("Mensagens.Bolao_Comecou")) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', iniciar)
                    .replace("[TEMPO]", (new StringBuilder(String.valueOf(getTempoRestante()))).toString()));
        }
    }

    public void finalizar() {

        Random rand = new Random();
        if (this.jogadores.size() > 0) {

            Player winner = this.jogadores.get(rand.nextInt(this.jogadores.size()));
            if(winner == null) {
                Bukkit.broadcastMessage(Main.msg.getConfig().getString("Mensagens.Avisos.VencedorKitou")
                        .replace("&", "§"));
            }

            for (String finalizar : Main.msg.getConfig().getStringList("Mensagens.Bolao_Acabou.AvisoGeral")) {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', finalizar)
                        .replace("[VENCEDOR]", winner.getName())
                        .replace("[VTOTAL]", (new StringBuilder(String.valueOf(this.participantes * this.valor))).toString()));
            }
            Main.econ.depositPlayer(winner.getName(), (this.valor * this.participantes));
            rolando = false;
        } else {
            if (this.jogadores.size() <= 0) {
                Bukkit.broadcastMessage(Main.msg.getConfig().getString("Mensagens.Bolao_Acabou.SemPlayers")
                        .replace("&", "§"));
                rolando = false;

            }
        }
    }

    public void rodarTimer() {
        this.tempoSegundos = this.tempoMinutos * 60;
        this.task2 = Bukkit.getScheduler().runTaskTimer((Plugin)Main.plugin, new Runnable() {
            public void run() {
                BolaoO.this.tempoSegundos = BolaoO.this.tempoSegundos - 1;
                if (BolaoO.this.tempoSegundos == 0) {
                    BolaoO.this.finalizar();
                    BolaoO.this.task2.cancel();
                } else if (BolaoO.this.tempoSegundos <= 5) {



                    Bukkit.broadcastMessage(Main.msg.getConfig().getString("Mensagens.Avisos.Tempo")
                            .replace("&", "§")
                            .replace("[TRESTANTE]", (new StringBuilder(String.valueOf(BolaoO.this.getTempoRestante()))).toString()));

                }
            }
        },20L, 20L);
        this.task = Bukkit.getScheduler().runTaskTimer((Plugin)Main.plugin, new Runnable() {
            public void run() {
                if (BolaoO.this.tempoSegundos > 0) {
                    if (BolaoO.this.tempoSegundos % 10 == 0) {
                        Bukkit.broadcastMessage(Main.msg.getConfig().getString("Mensagens.Avisos.Tempo")
                                .replace("&", "§")
                                .replace("[TRESTANTE]", (new StringBuilder(String.valueOf(BolaoO.this.getTempoRestante()))).toString()));
                    } else {
                        BolaoO.this.tempoSegundos = BolaoO.this.tempoSegundos - 1;
                        Bukkit.broadcastMessage(Main.msg.getConfig().getString("Mensagens.Avisos.Tempo")
                                .replace("&", "§")
                                .replace("[TRESTANTE]", (new StringBuilder(String.valueOf(BolaoO.this.getTempoRestante()))).toString()));
                    }
                } else {
                    BolaoO.this.task.cancel();
                }
            }
        },600L, 600L);
    }
}
