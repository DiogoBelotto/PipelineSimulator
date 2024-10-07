package processador;

import instrucoes.InstrucaoGenerica;

public class Excute extends EtapaGeneric {
    @Override
    public String toString() {
        return "Execute: ";
    }

    public void execute() {
        InstrucaoGenerica instrucao = super.getInstrucaoAtual();
        if (!instrucao.isValida())
            return;
        switch (instrucao.getOpcode()) {
            case "add":
                instrucao.setTemp3(Processador.getR()[instrucao.getOper2()] + Processador.getR()[instrucao.getOper3()]);
                break;
            case "sub":
                instrucao.setTemp3(Processador.getR()[instrucao.getOper2()] - Processador.getR()[instrucao.getOper3()]);
                break;

            case "lw", "sw":
                instrucao.setTemp3(instrucao.getOper3() + Processador.getR()[instrucao.getOper1()]);
                break;
            case "beq":
                if (Processador.getR()[instrucao.getOper1()] == Processador.getR()[instrucao.getOper2()]) {
                    instrucao.setTemp3(-1);
                }
                break;
            default:
                break;
        }
    }
}
