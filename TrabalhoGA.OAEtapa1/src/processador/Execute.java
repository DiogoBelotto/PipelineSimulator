package processador;

import instrucoes.InstrucaoGenerica;

public class Execute {
    private final Processador processador;
    private InstrucaoGenerica instrucaoAtual;

    public Execute(Processador processador) {
        this.processador = processador;
    }

    @Override
    public String toString() {
        return "Execute: ";
    }

    public void execute() {
        InstrucaoGenerica instrucao = instrucaoAtual;
        if (!instrucao.isValida())
            return;
        switch (instrucao.getOpcode()) {
            case "add":
                processador.setTotalInstrucoesExec(processador.getTotalInstrucoesExec() + 1);
                instrucao.setTemp3(processador.getR()[instrucao.getOper2()] + processador.getR()[instrucao.getOper3()]);
                break;
            case "sub":
                processador.setTotalInstrucoesExec(processador.getTotalInstrucoesExec() + 1);
                instrucao.setTemp3(processador.getR()[instrucao.getOper2()] - processador.getR()[instrucao.getOper3()]);
                break;

            case "lw", "sw":
                processador.setTotalInstrucoesExec(processador.getTotalInstrucoesExec() + 1);
                instrucao.setTemp3(instrucao.getOper3() + processador.getR()[instrucao.getOper1()]);
                break;
            case "beq":
                processador.setTotalInstrucoesExec(processador.getTotalInstrucoesExec() + 1);
                if (processador.getR()[instrucao.getOper1()] == processador.getR()[instrucao.getOper2()]) {
                    instrucao.setTemp3(-1);
                }
                break;
            default:
                processador.setTotalInstrucoesExec(processador.getTotalInstrucoesExec() + 1);
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
