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
                Processador.getR()[instrucao.getOper2()] = Processador.getMemory()[instrucao.getTemp3()];
                break;
            case "sw":
                Processador.getMemory()[instrucao.getTemp3()] = instrucao.getOper3();
                break;
            case "beq":
                if (!Processador.isPredicaoAtiva())
                    if (instrucao.getTemp3() == -1) {
                        Processador.setTotalInstrucoesExec(Processador.getTotalInstrucoesExec() - 1);
                        Processador.setDesvioIncorreto(true);
                        InstructionFetch.pC = instrucao.getOper3() - 1;
                    }
            default:
                break;
        }
    }
}
