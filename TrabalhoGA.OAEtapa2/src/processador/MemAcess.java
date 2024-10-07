package processador;

import instrucoes.InstrucaoGenerica;

public class MemAcess extends EtapaGeneric {
    @Override
    public String toString() {
        return "Mem Acess: ";
    }

    public void memoryAcess() {
        InstrucaoGenerica instrucao = super.getInstrucaoAtual();
        if (!instrucao.isValida())
            return;
        switch (instrucao.getOpcode()) {
            case "lw":
                Processador.R[instrucao.getOper2()] = Processador.memory[instrucao.getTemp3()];
                break;
            case "sw":
                Processador.memory[instrucao.getTemp3()] = instrucao.getOper3();
                break;
            default:
                break;
        }
    }
}
