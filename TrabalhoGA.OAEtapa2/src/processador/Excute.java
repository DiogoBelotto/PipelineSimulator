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
                instrucao.setTemp3(Processador.R[instrucao.getOper2()] + Processador.R[instrucao.getOper3()]);
                break;
            case "sub":
                instrucao.setTemp3(Processador.R[instrucao.getOper2()] - Processador.R[instrucao.getOper3()]);
                break;

            case "lw", "sw":
                instrucao.setTemp3(instrucao.getOper3() + Processador.R[instrucao.getOper1()]);
                break;
            case "beq":
                //Verifica a qual o Desvio real tomado pela instrução BEQ
                boolean realDesvio = Processador.R[instrucao.getOper1()] == Processador.R[instrucao.getOper2()];
                //Verifica a previsão que foi tomada na fase IF
                boolean desvioPrevisto = Processador.predicaoPHT.predict(instrucao.getTemp1());

                //Se a predição foi diferente do desvio real, seta a varivael desvioIncorreto para true, assim descartando as instruções erradas
                //Também atualiza o PC para o desvio real tomado
                if(!(desvioPrevisto == realDesvio)){
                    Processador.desvioIncorreto = true;
                    if(realDesvio)
                        InstructionFetch.pC = instrucao.getOper3()-1;
                    else
                        InstructionFetch.pC = instrucao.getTemp1();
                }
                //Atualiza a Tabela PHT com base no desvio real tomado
                Processador.predicaoPHT.updatePHT(instrucao.getTemp1(), realDesvio);
                break;
            default:
                break;
        }
    }
}
