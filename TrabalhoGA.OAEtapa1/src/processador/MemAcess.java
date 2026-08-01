package processador;

import instrucoes.InstrucaoGenerica;

public class MemAcess {
    private final Processador processador;
    private InstrucaoGenerica instrucaoAtual;

    public MemAcess(Processador processador) {
        this.processador = processador;
        instrucaoAtual = InstrucaoGenerica.noop();
    }

    @Override
    public String toString() {
        return "Mem Acess: ";
    }

    public void memoryAcess() {
        InstrucaoGenerica instrucao = instrucaoAtual;
        if (!instrucao.isValida()) {
            return;
        }
        switch (instrucao.getOpcode()) {
            case "lw":
                processador.getR()[instrucao.getOper2()] = processador.getMemory()[instrucao.getTemp3()];
                break;
            case "sw":
                processador.getMemory()[instrucao.getTemp3()] = instrucao.getOper3();
                break;
            case "beq":
                if (instrucao.getTemp3() == -1) {
                    processador.setDesvioIncorreto(true);
                    InstructionFetch.pC = instrucao.getOper3() - 1;
                    processador.setTotalInstrucoesExec(processador.getTotalInstrucoesExec() - 1);
                }
                break;
            default:
                break;
        }
    }

    public InstrucaoGenerica getInstrucaoAtual() {
        return instrucaoAtual;
    }

    public void setInstrucaoAtual(InstrucaoGenerica instrucaoAtual) {
        this.instrucaoAtual = instrucaoAtual;
    }
}
