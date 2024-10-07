package processador;

import instrucoes.*;


public class Decode extends EtapaGeneric{
    public Decode() {
    }

    @Override
    public String toString() {
        return "Decode: ";
    }

    public void InstructionDecode(String[] vet){
        if(vet == null){
            InstrucaoGenerica instrucaoReturn = new InstrucaoGenerica();
            instrucaoReturn.setOpcode("noop");
            super.setInstrucaoAtual(instrucaoReturn); 
            return;
        }

        InstrucaoGenerica instrucao;
        switch (vet[0]){
            case "noop", "halt":
                instrucao = new InstrucaoGenerica();
                instrucao.setOpcode(vet[0]);
                super.setInstrucaoAtual(instrucao);
                return;
            case "beq":
                instrucao = new InstrucaoGenerica();
                instrucao.setOper1(Integer.parseInt(vet[1]));
                instrucao.setOper2(Integer.parseInt(vet[2]));
                instrucao.setOper3(Integer.parseInt(vet[3]));
                instrucao.setTemp1(Integer.parseInt(vet[4]));
                instrucao.setOpcode(vet[0]);
                super.setInstrucaoAtual(instrucao);
                return;
            default:
                instrucao = new InstrucaoGenerica();
                instrucao.setOper1(Integer.parseInt(vet[1]));
                instrucao.setOper2(Integer.parseInt(vet[2]));
                instrucao.setOper3(Integer.parseInt(vet[3]));
                instrucao.setOpcode(vet[0]);
                super.setInstrucaoAtual(instrucao);
                return;
        }
    }

}
