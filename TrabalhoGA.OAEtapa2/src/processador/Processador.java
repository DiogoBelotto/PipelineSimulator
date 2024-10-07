package processador;

import io.iO;

import java.util.ArrayList;
import java.util.Scanner;

public class Processador {
    private static int[] R;
    private static ArrayList<String> instrucoes;
    private final EtapaGeneric[] etapas;
    private static boolean desvioIncorreto;
    private static int[] memory;
    private static PredicaoPHT predicaoPHT;
    private static int totalInstrucoesExec, totalInstrucoesDescartadas, totalCiclos;
    private static boolean predicaoAtiva;

    public Processador() {
        totalInstrucoesExec = 0;
        totalInstrucoesDescartadas = 0;
        predicaoPHT = new PredicaoPHT();
        totalCiclos = 0;
        memory = new int[100];//A memoria tem arbitrariamente apenas 100 endereços
        desvioIncorreto = false;
        R = new int[32];
        R[0] = 0;
        instrucoes = iO.leArquivo();
        etapas = new EtapaGeneric[5];
        etapas[0] = new InstructionFetch();
        etapas[1] = new Decode();
        etapas[2] = new Execute();
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


        //Verifica se o usuário quer o mecanismo de predição ativo ou não
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.print(ANSI_BLUE + "[1] Mecanismo de predição PHT 2-bits\n[2] Mecanismo estático de Desvio Não Tomado\nEscolha a opção [1] ou [2]:  " + ANSI_RESET);
            String resposta = scanner.nextLine();
            if (resposta.equalsIgnoreCase("1")) {
                predicaoAtiva = true;
                break;
            } else if (resposta.equalsIgnoreCase("2")) {
                predicaoAtiva = false;
                break;
            }
            System.out.println("Entrada inválida, tente novamente!");
        } while (true);


        //Inicia o loop de execução das instruções
        while (InstructionFetch.pC < instrucoes.size() + 5) {
            totalCiclos++;
            etapas[4].setInstrucaoAtual(etapas[3].getInstrucaoAtual());
            etapas[3].setInstrucaoAtual(etapas[2].getInstrucaoAtual());
            etapas[2].setInstrucaoAtual(etapas[1].getInstrucaoAtual());
            ((Decode) etapas[1]).InstructionDecode(firstInstruction);
            if (desvioIncorreto) {
                etapas[1].getInstrucaoAtual().setValida(false);
                totalInstrucoesDescartadas++;
            }

            firstInstruction = ((InstructionFetch) etapas[0]).fetchInstruction();
            if (firstInstruction == null) {
                InstructionFetch.pC++;
                ((InstructionFetch) etapas[0]).setInstrucao(new String[]{"noop"});
            }


            System.out.print(ANSI_CYAN + "Registradores: ");
            for (int j = 0; j < R.length; j++) {
                System.out.print(R[j] + (j == R.length - 1 ? "" : " | "));
            }
            System.out.println("\n");
            System.out.println(ANSI_BLUE + "Execução: " + totalCiclos + ANSI_RESET);
            for (int j = 0; j < etapas.length; j++) {

                if (j == 0) {
                    System.out.print(ANSI_GREEN + etapas[j] + ((InstructionFetch) etapas[j]).getInstrucao() + " ");
                } else
                    System.out.print(ANSI_GREEN + etapas[j] + " [" + etapas[j].getInstrucaoAtual() + ANSI_GREEN + "] ");
            }
            System.out.println("\n" + ANSI_RESET);

            if (desvioIncorreto)
                desvioIncorreto = false;

            ((WriteBack) etapas[4]).writeBack();
            ((MemAcess) etapas[3]).memoryAcess();
            ((Execute) etapas[2]).execute();
            if (desvioIncorreto) {
                etapas[1].getInstrucaoAtual().setValida(false);
                if (!predicaoAtiva){
                    etapas[2].getInstrucaoAtual().setValida(false);
                    totalInstrucoesDescartadas++;
                }
                totalInstrucoesDescartadas++;
            }
        }
        System.out.println(ANSI_BLUE + "Total de Ciclos: " + totalCiclos + ANSI_RESET);
        System.out.println(ANSI_BLUE + "Total de Instruções executadas: " + totalInstrucoesExec + ANSI_RESET);
        System.out.println(ANSI_BLUE + "Total de Instruções Descartadas: " + totalInstrucoesDescartadas + ANSI_RESET);
    }

    //Como não foi implementado os labels, alteramos .fill para a seguinte lógica: (.fill, posicao na memoria, valor)
    //Essa função irá carregar os valores .fill para a memória e remove-los da lista de instruções
    //Foi feito isso para manter a lógica de acesso a memória da instrução lw mesmo sem os labels
    public void dataMemoryLoader() {
        for (int i = instrucoes.size() - 1; i >= 0; i--) {
            String[] vet = instrucoes.get(i).replaceFirst("^\\s*", "").split(" ");
            if (vet[0].equals(".fill")) {
                memory[Integer.parseInt(vet[1])] = Integer.parseInt(vet[2]);
                instrucoes.remove(i);
            } else
                break;
        }
    }

    public static int[] getR() {
        return R;
    }

    public static void setR(int[] r) {
        R = r;
    }

    public static ArrayList<String> getInstrucoes() {
        return instrucoes;
    }

    public static void setInstrucoes(ArrayList<String> instrucoes) {
        Processador.instrucoes = instrucoes;
    }

    public EtapaGeneric[] getEtapas() {
        return etapas;
    }

    public static boolean isDesvioIncorreto() {
        return desvioIncorreto;
    }

    public static void setDesvioIncorreto(boolean desvioIncorreto) {
        Processador.desvioIncorreto = desvioIncorreto;
    }

    public static int[] getMemory() {
        return memory;
    }

    public static void setMemory(int[] memory) {
        Processador.memory = memory;
    }

    public static int getTotalCiclos() {
        return totalCiclos;
    }

    public static void setTotalCiclos(int totalCiclos) {
        Processador.totalCiclos = totalCiclos;
    }

    public static PredicaoPHT getPredicaoPHT() {
        return predicaoPHT;
    }

    public static void setPredicaoPHT(PredicaoPHT predicaoPHT) {
        Processador.predicaoPHT = predicaoPHT;
    }

    public static int getTotalInstrucoesExec() {
        return totalInstrucoesExec;
    }

    public static void setTotalInstrucoesExec(int totalInstrucoesExec) {
        Processador.totalInstrucoesExec = totalInstrucoesExec;
    }

    public static int getTotalInstrucoesDescartadas() {
        return totalInstrucoesDescartadas;
    }

    public static void setTotalInstrucoesDescartadas(int totalInstrucoesDescartadas) {
        Processador.totalInstrucoesDescartadas = totalInstrucoesDescartadas;
    }

    public static boolean isPredicaoAtiva() {
        return predicaoAtiva;
    }

    public static void setPredicaoAtiva(boolean predicaoAtiva) {
        Processador.predicaoAtiva = predicaoAtiva;
    }
}
