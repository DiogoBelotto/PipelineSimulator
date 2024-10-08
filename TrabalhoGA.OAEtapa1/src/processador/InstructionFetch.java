package processador;
import java.util.Arrays;

public class InstructionFetch{
    public static int pC;
    private final Processador processador;
    //Como nessa etapa a instrucao ainda não foi decodificada não é possível definir um objeto InstrucaoGenerica como nas outras etapas,
    //portanto seta apenas um vetor de String referente a instrucao atual para identificação
    private String[] instrucao;

    //Retorna a instução atual junto com as cores respectivas para ficar legível na saída
    public String getInstrucaoAtual() {
        String retorno = "";
        retorno = switch (instrucao[0]) {
            case "add", "sub" -> "\u001B[34m" + Arrays.toString(instrucao) + "\u001B[0m";
            case "lw", "sw" -> "\u001B[93m" + Arrays.toString(instrucao) + "\u001B[0m";
            case "beq" -> "\u001B[35m" + Arrays.toString(instrucao) + "\u001B[0m";
            case "halt" -> "\u001B[33m" + Arrays.toString(instrucao) + "\u001B[0m";
            case "noop" -> "\u001B[31m [noop] \u001B[0m";
            default -> retorno;
        };
        return retorno;
    }

    public InstructionFetch(Processador processador) {
        this.processador = processador;
        pC = 0;
    }

    public String[] fetchInstruction() {
        if (pC >= processador.getInstrucoes().size())
            return null;
        instrucao = processador.getInstrucoes().get(pC).replaceFirst("^\\s*", "").split(" ");
        pC++;
        return instrucao;
    }

    public void setInstrucao(String[] instrucao) {
        this.instrucao = instrucao;
    }

    @Override
    public String toString() {
        return "Instruction Fetch: ";
    }
}
