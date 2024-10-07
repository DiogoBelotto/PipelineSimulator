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
                instrucao.setTemp3(processador.getR()[instrucao.getOper2()] + processador.getR()[instrucao.getOper3()]);
                break;
            case "sub":
                instrucao.setTemp3(processador.getR()[instrucao.getOper2()] - processador.getR()[instrucao.getOper3()]);
                break;

            case "lw", "sw":
                instrucao.setTemp3(instrucao.getOper3() + processador.getR()[instrucao.getOper1()]);
                break;
            case "beq":
                if (processador.getR()[instrucao.getOper1()] == processador.getR()[instrucao.getOper2()]) {
                    instrucao.setTemp3(-1);
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
