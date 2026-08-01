package processador;

import instrucoes.InstrucaoGenerica;


public class Decode {
    private InstrucaoGenerica instrucaoAtual;
    private final Processador processador;

    public Decode(Processador processador) {
        this.processador = processador;
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

        // A primeira posição informa se a instrução foi descartada antes da decodificação.
        InstrucaoGenerica instrucao = new InstrucaoGenerica();
        String opcode = componentes[1];
        instrucao.setOpcode(opcode);

        switch (opcode) {
            case "noop", "halt":
                break;
            case "beq":
                preencherOperandos(instrucao, componentes);
                if (processador.isPredicaoAtiva()) {
                    instrucao.setTemp1(Integer.parseInt(componentes[5]));
                }
                break;
            default:
                preencherOperandos(instrucao, componentes);
        }

        if (componentes[0].equals("true")) {
            instrucao.setValida(false);
            processador.incrementarInstrucoesDescartadas();
        }
        instrucaoAtual = instrucao;
    }

    /**
     * Mantém compatibilidade com a assinatura original do projeto.
     */
    public void InstructionDecode(String[] componentes) {
        instructionDecode(componentes);
    }

    private void preencherOperandos(InstrucaoGenerica instrucao, String[] componentes) {
        instrucao.setOper1(Integer.parseInt(componentes[2]));
        instrucao.setOper2(Integer.parseInt(componentes[3]));
        instrucao.setOper3(Integer.parseInt(componentes[4]));
    }

    public InstrucaoGenerica getInstrucaoAtual() {
        return instrucaoAtual;
    }
}
