package processador;

import io.iO;

import java.util.ArrayList;

public class Processador {
    public static int[] R;
    public static ArrayList<String> instrucoes;
    private final EtapaGeneric[] etapas;
    public static boolean desvioIncorreto;
    public static int[] memory;
    public static int totalExec;

    public Processador() {
        totalExec = 0;
        memory = new int[100];//A memoria tem arbitrariamente apenas 100 endereços
        desvioIncorreto = false;
        R = new int[32];
        R[0] = 0;
        instrucoes = iO.leArquivo();
        etapas = new EtapaGeneric[5];
        etapas[0] = new InstructionFetch();
        etapas[1] = new Decode();
        etapas[2] = new Excute();
        etapas[3] = new MemAcess();
        etapas[4] = new WriteBack();
    }

    public void startExecution() {
        dataMemoryLoader();
        String[] firstInstruction = null;
        String ANSI_RESET = "\u001B[0m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_CYAN = "\u001B[36m";
        String ANSI_BLUE = "\u001B[34m";

        while (InstructionFetch.pC < instrucoes.size()+5) {
            totalExec++;
            etapas[4].setInstrucaoAtual(etapas[3].getInstrucaoAtual());
            etapas[3].setInstrucaoAtual(etapas[2].getInstrucaoAtual());
            etapas[2].setInstrucaoAtual(etapas[1].getInstrucaoAtual());
            ((Decode) etapas[1]).InstructionDecode(firstInstruction);
            if (desvioIncorreto)
                etapas[1].getInstrucaoAtual().setValida(false);
            firstInstruction = ((InstructionFetch) etapas[0]).fetchInstruction();
            if(firstInstruction == null) {
                InstructionFetch.pC++;
                ((InstructionFetch) etapas[0]).setInstrucao(new String[]{"noop"});
            }


            System.out.print(ANSI_CYAN + "Registradores: ");
            for (int j = 0; j < 10; j++) {
                System.out.print(R[j] + "|");
            }
            System.out.println("\n");
            System.out.print(ANSI_BLUE + "Execução " + totalExec + ": " + ANSI_RESET);
            for (int j = 0; j < etapas.length; j++) {

                if (j == 0){
                    System.out.print(ANSI_GREEN + etapas[j] + ((InstructionFetch) etapas[j]).getInstrucao() + " ");
                }
                else
                    System.out.print(ANSI_GREEN + etapas[j] + " [" + etapas[j].getInstrucaoAtual() + ANSI_GREEN + "] ");
            }
            System.out.println("\n" + ANSI_RESET);

            if (desvioIncorreto)
                desvioIncorreto = false;

            ((WriteBack) etapas[4]).writeBack();
            ((MemAcess) etapas[3]).memoryAcess();
            ((Excute) etapas[2]).execute();
            if (desvioIncorreto) {
                etapas[1].getInstrucaoAtual().setValida(false);
                etapas[2].getInstrucaoAtual().setValida(false);
            }
        }
        System.out.println(ANSI_BLUE + "Total de Execuções: " + totalExec + ANSI_RESET);
    }

    //Como não foi implementado os labels, alteramos .fill para a seguinte lógica: (.fill, posicao na memoria, valor)
    //Essa função irá carregar os valores .fill para a memória e remove-los da lista de instruções
    //Foi feito isso para manter a lógica de acesso a memória da instrução lw mesmo sem os labels
    public void dataMemoryLoader(){
        for(int i=instrucoes.size()-1; i>=0; i--){
            String[] vet = instrucoes.get(i).replaceFirst("^\\s*", "").split(" ");
            if(vet[0].equals(".fill")){
                memory[Integer.parseInt(vet[1])] = Integer.parseInt(vet[2]);
                instrucoes.remove(i);
            }
            else
                break;
        }
    }
}
