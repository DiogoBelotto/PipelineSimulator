package processador;

import instrucoes.InstrucaoGenerica;

public class WriteBack extends EtapaGeneric{
    @Override
    public String toString() {
        return "Write Back: ";
    }

    public void writeBack() {
        InstrucaoGenerica instrucao = super.getInstrucaoAtual();
        if (!instrucao.isValida())
            return;
        switch (instrucao.getOpcode()) {
            case "add", "sub":
                Processador.getR()[instrucao.getOper1()] = instrucao.getTemp3();
                break;
            default:
                break;
        }
    }
}

