package processador;

import instrucoes.InstrucaoGenerica;


public class Decode {
    private InstrucaoGenerica instrucaoAtual;

    public Decode() {
        instrucaoAtual = InstrucaoGenerica.noop();
    }

    @Override
    public String toString() {
        return "Decode: ";
    }

    public void instructionDecode(String[] componentes) {
        if (componentes == null) {
            instrucaoAtual = InstrucaoGenerica.noop();
            return;
        }

        InstrucaoGenerica instrucao = new InstrucaoGenerica();
        String opcode = componentes[0];
        instrucao.setOpcode(opcode);

        switch (opcode) {
            case "noop", "halt":
                instrucaoAtual = instrucao;
                return;
            default:
                instrucao.setOper1(Integer.parseInt(componentes[1]));
                instrucao.setOper2(Integer.parseInt(componentes[2]));
                instrucao.setOper3(Integer.parseInt(componentes[3]));
                instrucaoAtual = instrucao;
        }
    }

    /**
     * Mantém compatibilidade com a assinatura original do projeto.
     */
    public void InstructionDecode(String[] componentes) {
        instructionDecode(componentes);
    }

    public InstrucaoGenerica getInstrucaoAtual() {
        return instrucaoAtual;
    }

}
