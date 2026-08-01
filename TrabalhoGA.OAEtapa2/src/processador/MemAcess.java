package processador;

import instrucoes.InstrucaoGenerica;

public class MemAcess {
    private InstrucaoGenerica instrucaoAtual;
    private final Processador processador;
    @Override
    public String toString() {
        return "Mem Acess: ";
    }

    public MemAcess(Processador processador) {
        this.processador = processador;
        instrucaoAtual = InstrucaoGenerica.noop();
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
                ajustarDesvio(instrucao);
                break;
            default:
                break;
        }
    }

    private void ajustarDesvio(InstrucaoGenerica instrucao) {
        if (instrucao.getTemp3() != -1) {
            return;
        }

        if (processador.isPredicaoAtiva()) {
            InstructionFetch.pC = instrucao.getTemp2() == -1
                    ? instrucao.getOper3() - 1
                    : instrucao.getTemp1();
            return;
        }

        processador.setTotalInstrucoesExec(processador.getTotalInstrucoesExec() - 1);
        processador.setDesvioIncorreto(true);
        InstructionFetch.pC = instrucao.getOper3() - 1;
    }

    public InstrucaoGenerica getInstrucaoAtual() {
        return instrucaoAtual;
    }

    public void setInstrucaoAtual(InstrucaoGenerica instrucaoAtual) {
        this.instrucaoAtual = instrucaoAtual;
    }
}
