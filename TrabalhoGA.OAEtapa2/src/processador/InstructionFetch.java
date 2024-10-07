package processador;

import java.util.Arrays;

public class InstructionFetch{
    public static int pC;
    //Como nessa etapa a instrucaoAtual ainda não foi decodificada não é possível definir um objeto InstrucaoGenerica como nas outras etapas,
    //portanto seta apenas um vetor de String referente a instrucaoAtual atual para identificação
    private String[] instrucaoAtual;
    private final Processador processador;

    //Retorna a instução atual junto com as cores respectivas para ficar legível na saída
    public String getInstrucaoAtual() {
        String retorno = "";
        if(processador.isDesvioIncorreto() && processador.isPredicaoAtiva()) //Tachado para indicar instrução descartada
            retorno += "\u001B[9m";
        retorno += switch (instrucaoAtual[0]) {
            case "add", "sub" -> "\u001B[34m" + Arrays.toString(instrucaoAtual) + "\u001B[0m";
            case "lw", "sw" -> "\u001B[93m" + Arrays.toString(instrucaoAtual) + "\u001B[0m";
            case "beq" -> "\u001B[35m" + Arrays.toString(instrucaoAtual) + "\u001B[0m";
            case "halt" -> "\u001B[33m" + Arrays.toString(instrucaoAtual) + "\u001B[0m";
            case "noop" -> "\u001B[31m [noop] \u001B[0m";
            default -> retorno;
        };
        return retorno;
    }

    public InstructionFetch(Processador processador) {
        this.processador  = processador;
        pC = 0;
    }

    public String[] fetchInstruction() {
        //Caso o PC seja maior que a lista de instruções retorna apenas Null (noop)
        if (pC >= processador.getInstrucoes().size())
            return null;
        //Divide a instrução em um vetor de String para posterior decodifiação
        instrucaoAtual = processador.getInstrucoes().get(pC).replaceFirst("^\\s*", "").split(" ");

        if (processador.isPredicaoAtiva()) {
            if (instrucaoAtual[0].equals("beq")) {//Caso esteja usando predição e a instrução seja de desvio
                //PC referente ao BEQ atual para posterior analise no Execute
                String pCofBEQ;
                //Utiliza a Predicao PHT para verificar se o desvio deve ser tomado ou nao
                if (processador.getPredicaoPHT().predict(pC)) {
                    //Desvio tomado - Altera o PC para a nova posicao
                    pC = Integer.parseInt(instrucaoAtual[3]) - 1;
                    pCofBEQ = String.valueOf(pC);
                } else {
                    pCofBEQ = String.valueOf(pC);
                    pC++;
                }
                //Adiciona à posição 4 do vetor de String da instrucaoAtual, o PC referente a esse BEQ (será utilizado posteriormente na etapa de Execute para verificar se a predição tomada estava correta)
                String[] instrucaoBEQ = new String[instrucaoAtual.length + 1];
                System.arraycopy(instrucaoAtual, 0, instrucaoBEQ, 0, instrucaoAtual.length);
                instrucaoBEQ[instrucaoBEQ.length - 1] = pCofBEQ;
                instrucaoAtual = instrucaoBEQ;
            } else pC++;
        } else
            pC++;
        return instrucaoAtual;
    }

    public void setInstrucaoAtual(String[] instrucaoAtual) {
        this.instrucaoAtual = instrucaoAtual;
    }

    @Override
    public String toString() {
        return "Instruction Fetch: ";
    }
}
