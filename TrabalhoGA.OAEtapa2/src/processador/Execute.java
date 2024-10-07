package processador;

import instrucoes.InstrucaoGenerica;

public class Execute extends EtapaGeneric {
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
                Processador.setTotalInstrucoesExec(Processador.getTotalInstrucoesExec() + 1);
                instrucao.setTemp3(Processador.getR()[instrucao.getOper2()] + Processador.getR()[instrucao.getOper3()]);
                break;
            case "sub":
                Processador.setTotalInstrucoesExec(Processador.getTotalInstrucoesExec() + 1);
                instrucao.setTemp3(Processador.getR()[instrucao.getOper2()] - Processador.getR()[instrucao.getOper3()]);
                break;

            case "lw", "sw":
                Processador.setTotalInstrucoesExec(Processador.getTotalInstrucoesExec() + 1);
                ;
                instrucao.setTemp3(instrucao.getOper3() + Processador.getR()[instrucao.getOper1()]);
                break;
            case "beq":
                Processador.setTotalInstrucoesExec(Processador.getTotalInstrucoesExec() + 1);
                if (Processador.isPredicaoAtiva()) {
                    //Verifica a qual o Desvio real tomado pela instrução BEQ
                    boolean realDesvio = Processador.getR()[instrucao.getOper1()] == Processador.getR()[instrucao.getOper2()];
                    //Verifica a previsão que foi tomada na fase IF
                    boolean desvioPrevisto = Processador.getPredicaoPHT().predict(instrucao.getTemp1());

                    //Se a predição foi diferente do desvio real, seta a varivael desvioIncorreto para true, assim descartando as instruções erradas
                    //Também atualiza o PC para o desvio real tomado
                    if (!(desvioPrevisto == realDesvio)) {
                        Processador.setDesvioIncorreto(true);
                        if (realDesvio)
                            InstructionFetch.pC = instrucao.getOper3() - 1;
                        else
                            InstructionFetch.pC = instrucao.getTemp1();
                    }
                    //Atualiza a Tabela PHT com base no desvio real tomado
                    Processador.getPredicaoPHT().updatePHT(instrucao.getTemp1(), realDesvio);
                } else {
                    if (Processador.getR()[instrucao.getOper1()] == Processador.getR()[instrucao.getOper2()]) {
                        instrucao.setTemp3(-1);
                    }
                }
                break;
            default:
                Processador.setTotalInstrucoesExec(Processador.getTotalInstrucoesExec() + 1);
                break;
        }
    }
}
