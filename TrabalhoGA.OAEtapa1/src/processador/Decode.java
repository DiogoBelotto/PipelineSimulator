package processador;

import instrucoes.*;


public class Decode{
    private final Processador processador;
    private InstrucaoGenerica instrucaoAtual;

    public Decode(Processador processador) {
        this.processador = processador;
    }

    @Override
    public String toString() {
        return "Decode: ";
    }

    public void InstructionDecode(String[] vet){
        if(vet == null){
            InstrucaoGenerica instrucaoReturn = new InstrucaoGenerica();
            instrucaoReturn.setOpcode("noop");
            instrucaoAtual = instrucaoReturn;
            return;
        }

        InstrucaoGenerica instrucao;
        switch (vet[0]){
            case "noop", "halt":
                instrucao = new InstrucaoGenerica();
                instrucao.setOpcode(vet[0]);
                instrucaoAtual = instrucao;
                return;
            default:
                instrucao = new InstrucaoGenerica();
                instrucao.setOper1(Integer.parseInt(vet[1]));
                instrucao.setOper2(Integer.parseInt(vet[2]));
                instrucao.setOper3(Integer.parseInt(vet[3]));
                instrucao.setOpcode(vet[0]);
                instrucaoAtual = instrucao;
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
