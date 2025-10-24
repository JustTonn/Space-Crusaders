package entity;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.List;


import javax.swing.*;

public class SpaceCrusaders extends JPanel implements ActionListener, KeyListener, MouseListener {

    // primeira mudança para a adição do menu - lembrar que começou aqui.
    private EstadoDoJogo estado = EstadoDoJogo.MENU; // gerencia o estado do jogo.

    int tileSize = 64; // tamanho de cada quadradinho da tela.
    int colunas = 20; // quantos quadradinhos tem na vertical.
    int linhas = 10; // quantos quadradinhos tem na horizontal
    int larguraQuadro = tileSize * colunas;
    int alturaQuadro = tileSize * linhas;

    Image naveImg; // sprite da nave.
    Image alienImg; // sprite dos inimigos.
    Image alienCyanImg;
    Image alienMagentaImg;
    Image alienYellowImg;
    ArrayList<Image> alienImagemArray;

    // nave

    int naveX = tileSize;
    int naveAltura = tileSize;
    int naveY = (alturaQuadro / 2) - (naveAltura / 2);
    int naveLargura = tileSize * 2;
    int naveVelocidadeY = 3;

    // Combustível
    private int combustivelMax = 20; // combustível total em segundos
    private double combustivelAtual = combustivelMax;
    private long ultimoTempoCombustivel; // controle para decrementar 1s por segundo

    // Inimigos

    ArrayList<AlienTemplate> aliens;
    int alienAltura = tileSize;
    int alienLargura = tileSize;
    int alienX = 1280 - tileSize;
    int alienY = tileSize;

    int alienLinha = 3;
    int alienColunas = 1;
    int aliensContador;
    int pontosAlien = 1;
    int alienVelocidadeY = 1;
    private ReprodutorAudio audio = new EfeitosSonorosAdapter(); // Utilização do adapter

    @Override
    public void mouseClicked(MouseEvent e) {
        if (estado == EstadoDoJogo.MENU) {
            int mouseX = e.getX();
            int mouseY = e.getY();

            // área do botão
            int botaoX = larguraQuadro / 2 - 100;
            int botaoY = alturaQuadro / 2 - 40;
            int botaoLargura = 200;
            int botaoAltura = 80;

            if (mouseX >= botaoX && mouseX <= botaoX + botaoLargura &&
                    mouseY >= botaoY && mouseY <= botaoY + botaoAltura) {
                iniciarJogo();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public static class Nave extends Bloco {
        public Nave(int x, int y, int largura, int altura, Image img) {
            super(x, y, largura, altura, img);
        }

    }

    public static class Bala extends Bloco {
        public Bala(int x, int y, int largura, int altura, Image img) {
            super(x, y, largura, altura, null);
        }
    }

    public void atiraBala(TipoBala tipo) {
        Bala bala;

        switch (tipo) {
            case NORMAL:
                bala = new Bala(
                        nave.x + naveLargura,
                        nave.y + naveAltura / 2 - balaLargura / 6,
                        balaAltura,
                        balaLargura,
                        null);
                break;

            default:
                bala = new Bala(nave.x + naveLargura, nave.y, balaAltura, balaLargura, null);
        }

        balaArray.add(bala);
        audio.tocarEfeito("/som/ataque-nave.wav");
    }

    // declaração da nave
    Nave nave;

    //Sistema de Ranking

    private RankingManager rankingManager = new RankingManager();

    // tiros
    LinkedList<Bala> balaArray;
    LinkedList<Bala> alienBalas = new LinkedList<>();
    int balaLargura = tileSize / 2;
    int balaAltura = tileSize / 8;
    int balaVelocidadeX = +10; // velocidade de movimento do tiro do player
    int balaVelocidadealien = -8; // velocidade de movimento do alien
    Timer gameloop;
    int pontos = 0;
    boolean fimDoJogo = false;
    private Timer tiroTimer;
    private int intervaloTiro = 1000; // milissegundos, pode ajustar depois

    // lista de perks ?
    private Perk[] perksDisponiveis;

    SpaceCrusaders() {
        setPreferredSize(new Dimension(larguraQuadro, alturaQuadro));
        setBackground(Color.BLACK);
        setFocusable(true); // foca na tela do jogo
        addKeyListener(this); // pega o evento das teclas
        addMouseListener(this); // pega o evento do mouse

        // Sistema de Ranking



        naveImg = new ImageIcon(getClass().getResource("/imgs/ship.png")).getImage();
        alienImg = new ImageIcon(getClass().getResource("/imgs/alien.png")).getImage();
        alienCyanImg = new ImageIcon(getClass().getResource("/imgs/alien-cyan.png")).getImage();
        alienMagentaImg = new ImageIcon(getClass().getResource("/imgs/alien-magenta.png")).getImage();
        alienYellowImg = new ImageIcon(getClass().getResource("/imgs/alien-yellow.png")).getImage();

        alienImagemArray = new ArrayList<Image>();
        alienImagemArray.add(alienCyanImg);
        alienImagemArray.add(alienYellowImg);
        alienImagemArray.add(alienMagentaImg);
        alienImagemArray.add(alienImg);

        nave = new Nave(naveX, naveY, naveLargura, naveAltura, naveImg);
        aliens = new ArrayList<AlienTemplate>();
        balaArray = new LinkedList<Bala>();

        // sistema de combustível
        combustivelAtual = combustivelMax;
        ultimoTempoCombustivel = System.currentTimeMillis();

        // Temporizador do jogo

    }

    public void ajustarIntervaloTiro(int novoIntervalo) {
        tiroTimer.setDelay(novoIntervalo);
    }

    private void iniciarJogo() {
        estado = EstadoDoJogo.JOGANDO;
        combustivelAtual = combustivelMax;
        /*
         * inimigos.clear();
         * tiros.clear();
         */
        criaAliens();
        gameloop = new Timer(1000 / 60, this);
        gameloop.start();
        audio.tocarMusica("/som/background.wav", true);

        tiroTimer = new Timer(intervaloTiro, e -> atiraBala(TipoBala.NORMAL));
        tiroTimer.start(); // começa atirando automaticamente
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);

    }

    public void draw(Graphics g) {

        // Menu inicial

        if (estado == EstadoDoJogo.MENU) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, larguraQuadro, alturaQuadro);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("🚀 SPACE CRUSADERS", larguraQuadro / 2 - 250, alturaQuadro / 2 - 100);

            // botão iniciar
            int botaoX = larguraQuadro / 2 - 100;
            int botaoY = alturaQuadro / 2 - 40;
            int botaoLargura = 200;
            int botaoAltura = 80;

            g.setColor(Color.DARK_GRAY);
            g.fillRect(botaoX, botaoY, botaoLargura, botaoAltura);

            g.setColor(Color.WHITE);
            g.drawRect(botaoX, botaoY, botaoLargura, botaoAltura);

            g.setFont(new Font("Arial", Font.BOLD, 28));
            g.drawString("INICIAR", botaoX + 45, botaoY + 50);

            // Mostrar o ranking
            g.setFont(new Font("Arial", Font.BOLD, 28));
            g.drawString("🏆 Ranking:", larguraQuadro / 2 - 80, alturaQuadro / 2 + 80);

            g.setFont(new Font("Arial", Font.PLAIN, 24));
            List<Integer> pontuacoes = rankingManager.getPontuacoes();
            for (int i = 0; i < pontuacoes.size(); i++) {
                g.drawString((i + 1) + "º - " + pontuacoes.get(i) + " pontos",
                        larguraQuadro / 2 - 80, alturaQuadro / 2 + 120 + i * 30);
            }


            return;
        }

        // Desenha a nave
        g.drawImage(nave.img, nave.x, nave.y, nave.largura, nave.altura, null);

        // Desenha os inimigos
        for (AlienTemplate alien : aliens) {
            if (alien.vivo) {
                g.drawImage(alien.img, alien.x, alien.y, alien.largura, alien.altura, null);
            }
        }
        // Desenha as balas
        g.setColor(Color.red);
        for (int i = 0; i < balaArray.size(); i++) {
            Bala bala = balaArray.get(i);
            if (!bala.used) {
                g.fillRect(bala.x, bala.y, balaLargura, balaAltura);

            }
        }

        g.setColor(Color.cyan); // balas dos aliens azuis
        for (Bala bala : alienBalas) {
            if (!bala.used) {
                g.fillRect(bala.x, bala.y, balaLargura, balaAltura);
            }
        }

        // pontuação
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (fimDoJogo) {
            g.drawString("Game Over : " + String.valueOf(pontos), 10, 35);
        } else {
            g.drawString(String.valueOf(pontos), 10, 35);
        }

        // Escreve as opções de Perks

        if (estado == EstadoDoJogo.SELECIONANDO_PERK) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Escolha sua melhoria:", larguraQuadro / 2 - 200, alturaQuadro / 2 - 100);

            g.setFont(new Font("Arial", Font.PLAIN, 28));
            g.drawString("1 - " + perksDisponiveis[0].nome + " (" + perksDisponiveis[0].descricao + ")",
                    larguraQuadro / 2 - 300, alturaQuadro / 2);
            g.drawString("2 - " + perksDisponiveis[1].nome + " (" + perksDisponiveis[1].descricao + ")",
                    larguraQuadro / 2 - 300, alturaQuadro / 2 + 60);
            return; // não desenha o resto do jogo
        }

        // Mostra o tempo de combustível

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        String textoCombustivel = "Combustível: " + (int) combustivelAtual + "s";
        int textoLargura = g.getFontMetrics().stringWidth(textoCombustivel);
        g.drawString(textoCombustivel, larguraQuadro - textoLargura - 20, 30);

        // Combustível barrinha

        int barraLarguraMax = 200;
        int barraAltura = 20;
        int barraX = larguraQuadro - barraLarguraMax - 20;
        int barraY = 40;

        // calcula a proporção de combustível

        double proporcao = combustivelAtual / combustivelMax;
        int barraLarguraAtual = (int) (barraLarguraMax * proporcao);
        if (proporcao > 1.0)
            proporcao = 1.0;

        barraLarguraAtual = (int) (barraLarguraMax * proporcao);

        // cor da barra
        Color corBarra;
        if (proporcao > 0.5)
            corBarra = Color.GREEN;
        else if (proporcao > 0.25)
            corBarra = Color.YELLOW;
        else
            corBarra = Color.RED;

        g.setColor(Color.GRAY);
        g.drawRect(barraX, barraY, barraLarguraMax, barraAltura);
        g.setColor(corBarra);
        g.fillRect(barraX + 1, barraY + 1, barraLarguraAtual - 2, barraAltura - 2);

    }

    public void movimento() {
        // aliens
        for (AlienTemplate alien : aliens) {
            if (alien.vivo()) {
                alien.acaoDoAlien(nave, aliens, alienBalas);
            }
        }

        // Sistema de combustível - parte que atualiza o combustível

        if (estado == EstadoDoJogo.JOGANDO) {
            long agora = System.currentTimeMillis();
            if (agora - ultimoTempoCombustivel >= 1000) { // passou 1 segundo
                combustivelAtual -= 1;
                ultimoTempoCombustivel = agora;

                if (combustivelAtual <= 0) {
                    combustivelAtual = 0;
                    fimDoJogo = true;
                    gameloop.stop();
                    tiroTimer.stop();
                    audio.pararMusica();
                }
            }
        }

        // bala tiros
        for (int i = 0; i < balaArray.size(); i++) {
            Bala bala = balaArray.get(i);
            bala.x += balaVelocidadeX;

            // colisao dos tiros com os aliens
            for (AlienTemplate alien : aliens) {
                if (!bala.used && alien.vivo && detectarColisao(bala, alien)) {
                    bala.used = true;
                    alien.vivo = false;
                    aliensContador--;
                    pontos += 100;
                }
            }
        }

        for (int i = 0; i < alienBalas.size(); i++) {
            Bala bala = alienBalas.get(i);
            bala.x += balaVelocidadealien;

            // colisão com a nave
            if (!bala.used && detectarColisao(bala, nave)) {
                bala.used = true;
                fimDoJogo = true; // nave atingida = game over
            }
        }

        // limpar balas que saíram da tela
        while (!alienBalas.isEmpty() && (alienBalas.getFirst().used || alienBalas.getFirst().x < 0)) {
            alienBalas.removeFirst();
        }

        // Lista encadeada aqui é mais otimizado do que arraylist,obrigado mestre arthur
        while (!balaArray.isEmpty() && (balaArray.getFirst().used || balaArray.getFirst().y < 0)) {
            balaArray.removeFirst();
        }

        // Sistema de perks

        if (aliensContador == 0 && estado == EstadoDoJogo.JOGANDO) {
            estado = EstadoDoJogo.SELECIONANDO_PERK;
            gameloop.stop(); // pausa o jogo
            tiroTimer.stop();

            // gera duas opções de perk
            perksDisponiveis = new Perk[] {
                    new Perk("Tanque Extra", "Aumenta o combustível",
                            tipoPerk.MELHORAR_COMBUSTIVEL),
                    new Perk("Fogo Rápido", "Diminui o intervalo entre tiros", tipoPerk.MELHORAR_TAXA_DE_TIRO)
            };
        }

        // nova horda
        if (aliensContador == 0) {
            pontos += alienColunas * alienLinha * 100;
            alienColunas = Math.min(alienColunas + 1, colunas / 2 - 2);
            alienLinha = Math.min(alienLinha + 1, linhas - 6);
            aliens.clear();
            balaArray.clear();
            criaAliens();
        }

    }

    public void criaAliens() {
        Random random = new Random();

        for (int l = 0; l < alienLinha; l++) {
            int espacamentoHorizontal = 50;
            int espacamentoVertical = 30;
            for (int c = 0; c < alienColunas; c++) {
                int indiceAleatorio = random.nextInt(alienImagemArray.size()); // um índice aleatório de uma imagem de
                                                                               // // // // alien.
                int posY = alienY + l * (alienAltura + espacamentoVertical) + random.nextInt(40);
                AlienTemplate alien;
                alien = new AlienNormal(alienX + c * (alienLargura + espacamentoHorizontal),
                        posY, alienLargura, alienAltura, alienImagemArray.get(indiceAleatorio), 1);
                aliens.add(alien);
            }
        }
        aliensContador = aliens.size();
    }

    public boolean detectarColisao(Bloco a, Bloco b) {
        return a.x < b.x + b.largura &&
                a.x + a.largura > b.x &&
                a.y < b.y + b.altura &&
                a.y + a.altura > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        movimento();
        repaint();

        if (fimDoJogo) {
            rankingManager.adicionarPontuacao(pontos);
            gameloop.stop();
            tiroTimer.stop();
            audio.pararMusica();
        }
    }

    // Funçao que aplica os perks
    private void aplicarPerk(Perk perk) {
        switch (perk.tipo) {
            case MELHORAR_TAXA_DE_TIRO:
                intervaloTiro = Math.max(100, intervaloTiro - 200); // reduz o intervalo, mas nunca abaixo de 100ms
                tiroTimer.setDelay(intervaloTiro);
                break;

            case MELHORAR_COMBUSTIVEL:
                // Recarrega combustível entre hordas
                combustivelAtual += combustivelMax;
                ultimoTempoCombustivel = System.currentTimeMillis();

                break;
        }

        // retorna ao jogo
        estado = EstadoDoJogo.JOGANDO;
        gameloop.start();
        tiroTimer.start();
        audio.tocarMusica("/som/background.wav", true);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    // movimentação da nave

    boolean cima = false;
    boolean baixo = false;
    boolean esquerda = false;
    boolean direta = false;

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            cima = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            baixo = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            direta = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            esquerda = true;
        }

        if (cima && nave.y - naveVelocidadeY >= 0) {
            nave.y -= 3; // move para cima.
            if (direta && nave.x + nave.largura + naveVelocidadeY <= larguraQuadro) {
                nave.x += 3;
            }
            if (esquerda && nave.x - naveVelocidadeY >= 0) {
                nave.x -= 3;
            }
        }
        if (baixo && nave.y + nave.altura + naveVelocidadeY <= alturaQuadro) {
            nave.y += 3; // move para baixo.
            if (direta && nave.x + nave.largura + naveVelocidadeY <= larguraQuadro) {
                nave.x += 3;
            }
            if (esquerda && nave.x - naveVelocidadeY >= 0) {
                nave.x -= 3;
            }
        }
        if (direta && nave.x + nave.largura + naveVelocidadeY <= larguraQuadro) {
            nave.x += 3;
        }
        if (esquerda && nave.x - naveVelocidadeY >= 0) {
            nave.x -= 3;
        }

        // Mais coisas de perks

        if (estado == EstadoDoJogo.SELECIONANDO_PERK) {
            if (e.getKeyCode() == KeyEvent.VK_1) {
                aplicarPerk(perksDisponiveis[0]);
            } else if (e.getKeyCode() == KeyEvent.VK_2) {
                aplicarPerk(perksDisponiveis[1]);
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (fimDoJogo) {

            alienLinha = 3;
            alienColunas = 1;
            nave.x = naveX;
            balaArray.clear();
            alienBalas.clear();
            aliens.clear();
            pontos = 0;
            fimDoJogo = false;
            criaAliens();
            gameloop.start();
            intervaloTiro = 1000;
            tiroTimer.setDelay(intervaloTiro);
            tiroTimer.start();
            combustivelAtual = combustivelMax;
            audio.tocarMusica("/som/background.wav", true);
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            cima = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            baixo = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            direta = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            esquerda = false;
        }

    }

}
