package processador;

import instrucoes.InstrucaoGenerica;

public class WriteBack{
    private InstrucaoGenerica instrucaoAtual;
    private final Processador processador;
    public WriteBack(Processador processador) {
        this.processador = processador;
    }

    @Override
    public String toString() {
        return "Write Back: ";
    }

    public void writeBack() {
        InstrucaoGenerica instrucao = instrucaoAtual;
        if (!instrucao.isValida())
            return;
        switch (instrucao.getOpcode()) {
            case "add", "sub":
                processador.getR()[instrucao.getOper1()] = instrucao.getTemp3();
                break;
            default:
                break;
        }
    }
    public InstrucaoGenerica getInstrucaoAtual() {
        if(instrucaoAtual == null){
            InstrucaoGenerica instrucaoReturn = new InstrucaoGenerica();
            instrucaoReturn.setOpcode("noop");
            return instrucaoReturn;
        }
        return instrucaoAtual;
    }

    public void setInstrucaoAtual(InstrucaoGenerica instrucaoAtual) {
        this.instrucaoAtual = instrucaoAtual;
    }
}

