package processador;

import instrucoes.*;


public class Decode {
    private InstrucaoGenerica instrucaoAtual;
    private final Processador processador;

    public Decode(Processador processador) {
        this.processador = processador;

    }

    @Override
    public String toString() {
        return "Decode: ";
    }

    public void InstructionDecode(String[] vet) {
        if (vet == null) {
            InstrucaoGenerica instrucaoReturn = new InstrucaoGenerica();
            instrucaoReturn.setOpcode("noop");
            instrucaoAtual = instrucaoReturn;
            return;
        }

        //No vet[0] se encontra a informação de válida da instrução, para aquelas instruções ainda não decodificas mas já descartadas
        InstrucaoGenerica instrucao;
        switch (vet[1]) {
            case "noop", "halt":
                instrucao = new InstrucaoGenerica();
                instrucao.setOpcode(vet[1]);
                if (vet[0].equals("true")) {
                    instrucao.setValida(false);
                    processador.setTotalInstrucoesDescartadas(processador.getTotalInstrucoesDescartadas() + 1);
                }
                instrucaoAtual = instrucao;
                return;
            case "beq":
                instrucao = new InstrucaoGenerica();
                instrucao.setOper1(Integer.parseInt(vet[2]));
                instrucao.setOper2(Integer.parseInt(vet[3]));
                instrucao.setOper3(Integer.parseInt(vet[4]));
                if (processador.isPredicaoAtiva())
                    instrucao.setTemp1(Integer.parseInt(vet[5]));
                instrucao.setOpcode(vet[1]);
                if (vet[0].equals("true")) {
                    instrucao.setValida(false);
                    processador.setTotalInstrucoesDescartadas(processador.getTotalInstrucoesDescartadas() + 1);
                }

                instrucaoAtual = instrucao;
                return;
            default:
                instrucao = new InstrucaoGenerica();
                instrucao.setOper1(Integer.parseInt(vet[2]));
                instrucao.setOper2(Integer.parseInt(vet[3]));
                instrucao.setOper3(Integer.parseInt(vet[4]));
                instrucao.setOpcode(vet[1]);
                if (vet[0].equals("true")) {
                    instrucao.setValida(false);
                    processador.setTotalInstrucoesDescartadas(processador.getTotalInstrucoesDescartadas() + 1);
                }
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
}
