package entity;

import java.io.*;
import java.util.*;

public class RankingManager {
    private static final String FILE_PATH = "ranking.txt";
    private static final int MAX_PONTUACOES = 5;
    private List<Integer> pontuacoes = new ArrayList<>();

    public RankingManager() {
        carregarRanking();
    }

    public List<Integer> getPontuacoes() {
        return pontuacoes;
    }

    public void adicionarPontuacao(int pontos) {
        pontuacoes.add(pontos);
        Collections.sort(pontuacoes, Collections.reverseOrder()); // maior para menor
        if (pontuacoes.size() > MAX_PONTUACOES) {
            pontuacoes = pontuacoes.subList(0, MAX_PONTUACOES); // mantém só as 5 maiores
        }
        salvarRanking();
    }

    private void carregarRanking() {
        pontuacoes.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                pontuacoes.add(Integer.parseInt(linha.trim()));
            }
        } catch (IOException e) {
            // se o arquivo não existir ainda, tudo bem
        }
    }

    private void salvarRanking() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (int p : pontuacoes) {
                bw.write(String.valueOf(p));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
