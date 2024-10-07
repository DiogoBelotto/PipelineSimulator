package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class iO {
    public static ArrayList<String> leArquivo() {
        ArrayList<String> instrucoes = new ArrayList<>();
        //Lê cada linha de casoTeste1.txt e retorna um Arraylist das instruções
        File arquivo = new File("casoTeste1.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha = br.readLine();
            while (linha != null) {
                instrucoes.add(linha);
                linha = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return instrucoes;
    }
}