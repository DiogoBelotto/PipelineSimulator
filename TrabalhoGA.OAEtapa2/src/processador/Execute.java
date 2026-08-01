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
        instrucaoAtual = InstrucaoGenerica.noop();
    }

    public void execute() {
        InstrucaoGenerica instrucao = instrucaoAtual;
        if (!instrucao.isValida()) {
            return;
        }

        processador.incrementarInstrucoesExecutadas();
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
                executarDesvio(instrucao);
                break;
            default:
                break;
        }
    }

    private void executarDesvio(InstrucaoGenerica instrucao) {
        boolean desvioReal = processador.getR()[instrucao.getOper1()]
                == processador.getR()[instrucao.getOper2()];

        if (!processador.isPredicaoAtiva()) {
            if (desvioReal) {
                instrucao.setTemp3(-1);
            }
            return;
        }

        boolean desvioPrevisto = processador.getPredicaoPHT().predict(instrucao.getTemp1());
        if (desvioPrevisto != desvioReal) {
            processador.setDesvioIncorreto(true);
            instrucao.setTemp3(-1);
            instrucao.setTemp2(desvioReal ? -1 : 1);
        }
        processador.getPredicaoPHT().updatePHT(instrucao.getTemp1(), desvioReal);
    }

    public InstrucaoGenerica getInstrucaoAtual() {
        return instrucaoAtual;
    }

    public void setInstrucaoAtual(InstrucaoGenerica instrucaoAtual) {
        this.instrucaoAtual = instrucaoAtual;
    }

}
