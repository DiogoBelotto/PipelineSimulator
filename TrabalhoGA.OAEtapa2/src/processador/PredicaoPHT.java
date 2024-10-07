package processador;

import java.util.HashMap;

public class PredicaoPHT {
    private HashMap<Integer, Integer> pht; // Tabela de História de Desvios (PHT) armazenada em um HashMap
    private static final int FORTEMENTE_TOMADO = 3;  // 11
    private static final int FRACAMENTE_TOMADO = 2;    // 10
    private static final int FRACAMENTE_NAO_TOMADO = 1; // 01
    private static final int FORTEMENTE_NAO_TOMADO = 0; // 00

    public PredicaoPHT() {
        pht = new HashMap<>();
    }

    // Prediz se o desvio será tomado com base no contador
    public boolean predict(int pc) {
        int estado = pht.getOrDefault(pc, FORTEMENTE_NAO_TOMADO); // Valor default: fortemente não tomado (00)
        return (estado >= FRACAMENTE_TOMADO);// Retorna verdadeiro caso o estado seja fracamente tomado ou maior
    }

    public void updatePHT(int pc, boolean desvioTomado) {
        int estado = pht.getOrDefault(pc, FORTEMENTE_NAO_TOMADO);
        if(desvioTomado)
            if(estado < FORTEMENTE_TOMADO)
                estado++;
        else
            if(estado > FORTEMENTE_NAO_TOMADO)
                estado--;
        pht.put(pc, estado);
    }

}
