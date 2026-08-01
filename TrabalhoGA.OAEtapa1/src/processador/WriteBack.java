package processador;

import instrucoes.InstrucaoGenerica;

public class WriteBack {
    private final Processador processador;
    private InstrucaoGenerica instrucaoAtual;

    @Override
    public String toString() {
        return "Write Back: ";
    }

    public void writeBack() {
        InstrucaoGenerica instrucao = instrucaoAtual;
        if (!instrucao.isValida()) {
            return;
        }
        switch (instrucao.getOpcode()) {
            case "add", "sub":
                processador.getR()[instrucao.getOper1()] = instrucao.getTemp3();
                break;
            default:
                break;
        }
    }
    public WriteBack(Processador processador) {
        this.processador = processador;
        instrucaoAtual = InstrucaoGenerica.noop();
    }

    public InstrucaoGenerica getInstrucaoAtual() {
        return instrucaoAtual;
    }

    public void setInstrucaoAtual(InstrucaoGenerica instrucaoAtual) {
        this.instrucaoAtual = instrucaoAtual;
    }
}

