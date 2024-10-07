package processador;

import instrucoes.InstrucaoGenerica;

public class Execute {
    @Override
    public String toString() {
        return "Execute: ";
    }

    private InstrucaoGenerica instrucaoAtual;
    private final Processador processador;

    public Execute(Processador processador) {
        this.processador = processador;
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
                //Caso seja com predicao
                if (processador.isPredicaoAtiva()) {
                    //Verifica a qual o Desvio real tomado pela instrução BEQ
                    boolean realDesvio = processador.getR()[instrucao.getOper1()] == processador.getR()[instrucao.getOper2()];
                    //Verifica a previsão que foi tomada na fase IF
                    boolean desvioPrevisto = processador.getPredicaoPHT().predict(instrucao.getTemp1());

                    //Se a predição foi diferente do desvio real, seta a varivael desvioIncorreto para true, assim descartando as instruções erradas
                    //Também atualiza o PC para o desvio real tomado
                    if (!(desvioPrevisto == realDesvio)) {
                        processador.setDesvioIncorreto(true);
                        instrucao.setTemp3(-1);//Altera essa variavel temporaria para identificação na etapa memAcess que o desvio foi tomado erroneamente e alteração do PC nela
                        if (realDesvio)
                            instrucao.setTemp2(-1); //Caso desvio devia ter sido tomado
                        else
                            instrucao.setTemp2(1); //Caso desvio não devia ter sido tomado
                    }
                    //Atualiza a Tabela PHT com base no desvio real computado
                    processador.getPredicaoPHT().updatePHT(instrucao.getTemp1(), realDesvio);
                } else {
                    //Sem predição assume NÂO tomado fixo, portanto caso o desvio devia ter sido tomado altera temp3 para -1
                    if (processador.getR()[instrucao.getOper1()] == processador.getR()[instrucao.getOper2()]) {
                        instrucao.setTemp3(-1);
                    }
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
