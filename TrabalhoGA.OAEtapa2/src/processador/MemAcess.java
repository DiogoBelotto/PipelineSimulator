package processador;

import instrucoes.InstrucaoGenerica;

public class MemAcess{
    private InstrucaoGenerica instrucaoAtual;
    private final Processador processador;
    @Override
    public String toString() {
        return "Mem Acess: ";
    }

    public MemAcess(Processador processador) {
        this.processador = processador;
    }

    public void memoryAcess() {
        InstrucaoGenerica instrucao = instrucaoAtual;
        if (!instrucao.isValida())
            return;
        switch (instrucao.getOpcode()) {
            case "lw":
                processador.getR()[instrucao.getOper2()] = processador.getMemory()[instrucao.getTemp3()];
                break;
            case "sw":
                processador.getMemory()[instrucao.getTemp3()] = instrucao.getOper3();
                break;
            case "beq":
                if (processador.isPredicaoAtiva()) {
                    if (instrucao.getTemp3() == -1) {//Caso esteja usando predição e ela foi erroneamente tomada
                        if (instrucao.getTemp2() == -1)
                            InstructionFetch.pC = instrucao.getOper3() - 1;//Caso o desvio devia ter sido tomado
                        else
                            InstructionFetch.pC = instrucao.getTemp1();//Caso o desvio NÃO devia ter sido tomado
                    }
                } else {
                    //Sem predição assume desvio NÂO Tomado estático, portanto sempre que -1 o desvio não devia ter sido tomado
                    if (instrucao.getTemp3() == -1) {
                        processador.setTotalInstrucoesExec(processador.getTotalInstrucoesExec() - 1);
                        processador.setDesvioIncorreto(true);
                        InstructionFetch.pC = instrucao.getOper3() - 1;
                    }
                }
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
