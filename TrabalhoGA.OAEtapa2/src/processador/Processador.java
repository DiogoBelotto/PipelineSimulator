package processador;

import io.iO;

import java.util.ArrayList;
import java.util.Scanner;

public class Processador {
    private static final int QUANTIDADE_REGISTRADORES = 32;
    private static final int TAMANHO_MEMORIA = 100;
    private static final int ESTAGIOS_PIPELINE = 5;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_BLUE = "\u001B[34m";

    private final int[] registradores;
    private final ArrayList<String> instrucoes;
    private final int[] memoria;
    private final PredicaoPHT predicaoPHT;
    private boolean desvioIncorreto;
    private boolean predicaoAtiva;
    private int totalInstrucoesExec;
    private int totalInstrucoesDescartadas;
    private int totalCiclos;

    // Dependências das etapas do pipeline.
    private final InstructionFetch instructionFetch;
    private final Decode decode;
    private final Execute execute;
    private final MemAcess memAcess;
    private final WriteBack writeBack;

    @SuppressWarnings("this-escape")
    public Processador() {
        registradores = new int[QUANTIDADE_REGISTRADORES];
        memoria = new int[TAMANHO_MEMORIA];
        instrucoes = iO.leArquivo();
        predicaoPHT = new PredicaoPHT();

        instructionFetch = new InstructionFetch(this);
        decode = new Decode(this);
        execute = new Execute(this);
        memAcess = new MemAcess(this);
        writeBack = new WriteBack(this);
    }

    public void startExecution() {
        dataMemoryLoader();
        selecionarMecanismoPredicao();
        String[] primeiraInstrucao = null;

        while (pipelineEmExecucao()) {
            totalCiclos++;
            avancarPipeline(primeiraInstrucao);
            descartarInstrucaoDecodificadaSeNecessario();

            primeiraInstrucao = prepararInstrucaoBuscada(instructionFetch.fetchInstruction());

            exibirEstadoPipeline();
            limparSinalDesvio();
            executarEtapas();
            descartarInstrucoesAposDesvio();
        }

        exibirResumo();
    }

    private void selecionarMecanismoPredicao() {
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.print(ANSI_BLUE
                    + "[1] Mecanismo de predição PHT 2-bits\n"
                    + "[2] Mecanismo estático de Desvio Não Tomado\n"
                    + "Escolha a opção [1] ou [2]:  "
                    + ANSI_RESET);
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
    }

    private boolean pipelineEmExecucao() {
        return InstructionFetch.pC < instrucoes.size() + ESTAGIOS_PIPELINE;
    }

    private void avancarPipeline(String[] primeiraInstrucao) {
        writeBack.setInstrucaoAtual(memAcess.getInstrucaoAtual());
        memAcess.setInstrucaoAtual(execute.getInstrucaoAtual());
        execute.setInstrucaoAtual(decode.getInstrucaoAtual());
        decode.instructionDecode(primeiraInstrucao);
    }

    private void descartarInstrucaoDecodificadaSeNecessario() {
        if (desvioIncorreto) {
            decode.getInstrucaoAtual().setValida(false);
            incrementarInstrucoesDescartadas();
        }
    }

    private String[] prepararInstrucaoBuscada(String[] instrucao) {
        boolean instrucaoDescartada = desvioIncorreto && predicaoAtiva;

        if (instrucao == null) {
            InstructionFetch.pC++;
            instructionFetch.setInstrucaoAtual(new String[]{"noop"});
            return new String[]{Boolean.toString(instrucaoDescartada), "noop"};
        }

        String[] instrucaoComValidade = new String[instrucao.length + 1];
        System.arraycopy(instrucao, 0, instrucaoComValidade, 1, instrucao.length);
        instrucaoComValidade[0] = Boolean.toString(instrucaoDescartada);
        return instrucaoComValidade;
    }

    private void exibirEstadoPipeline() {
        System.out.print(ANSI_CYAN + "Registradores: ");
        for (int i = 0; i < registradores.length; i++) {
            System.out.print(registradores[i] + (i == registradores.length - 1 ? "" : " | "));
        }
        System.out.println("\n");
        System.out.print(ANSI_BLUE + "Execução " + totalCiclos + ": " + ANSI_RESET);

        System.out.print(ANSI_GREEN + instructionFetch + instructionFetch.getInstrucaoAtual() + " ");
        System.out.print(ANSI_GREEN + decode + " [" + decode.getInstrucaoAtual() + ANSI_GREEN + "] ");
        System.out.print(ANSI_GREEN + execute + " [" + execute.getInstrucaoAtual() + ANSI_GREEN + "] ");
        System.out.print(ANSI_GREEN + memAcess + " [" + memAcess.getInstrucaoAtual() + ANSI_GREEN + "] ");
        System.out.print(ANSI_GREEN + writeBack + " [" + writeBack.getInstrucaoAtual() + ANSI_GREEN + "] ");

        System.out.println("\n" + ANSI_RESET);
    }

    private void limparSinalDesvio() {
        if (desvioIncorreto) {
            desvioIncorreto = false;
        }
    }

    private void executarEtapas() {
        writeBack.writeBack();
        memAcess.memoryAcess();
        execute.execute();
    }

    private void descartarInstrucoesAposDesvio() {
        if (desvioIncorreto) {
            decode.getInstrucaoAtual().setValida(false);
            if (!predicaoAtiva) {
                execute.getInstrucaoAtual().setValida(false);
                incrementarInstrucoesDescartadas();
            }
            incrementarInstrucoesDescartadas();
        }
    }

    private void exibirResumo() {
        System.out.println(ANSI_BLUE + "Total de Ciclos: " + totalCiclos + ANSI_RESET);
        System.out.println(ANSI_BLUE + "Total de Instruções executadas: " + totalInstrucoesExec + ANSI_RESET);
        System.out.println(ANSI_BLUE + "Total de Instruções Descartadas: " + totalInstrucoesDescartadas + ANSI_RESET);
    }

    // Como não foram implementados labels, .fill segue o formato: posição na memória e valor.
    public void dataMemoryLoader() {
        for (int i = instrucoes.size() - 1; i >= 0; i--) {
            String[] componentes = instrucoes.get(i).replaceFirst("^\\s*", "").split(" ");
            if (componentes[0].equals(".fill")) {
                memoria[Integer.parseInt(componentes[1])] = Integer.parseInt(componentes[2]);
                instrucoes.remove(i);
            } else {
                break;
            }
        }
    }

    void incrementarInstrucoesExecutadas() {
        totalInstrucoesExec++;
    }

    void incrementarInstrucoesDescartadas() {
        totalInstrucoesDescartadas++;
    }

    public int[] getR() {
        return registradores;
    }

    public ArrayList<String> getInstrucoes() {
        return instrucoes;
    }

    public boolean isDesvioIncorreto() {
        return desvioIncorreto;
    }

    public void setDesvioIncorreto(boolean desvioIncorreto) {
        this.desvioIncorreto = desvioIncorreto;
    }

    public int[] getMemory() {
        return memoria;
    }

    public PredicaoPHT getPredicaoPHT() {
        return predicaoPHT;
    }

    public int getTotalInstrucoesExec() {
        return totalInstrucoesExec;
    }

    public void setTotalInstrucoesExec(int totalInstrucoesExec) {
        this.totalInstrucoesExec = totalInstrucoesExec;
    }

    public int getTotalInstrucoesDescartadas() {
        return totalInstrucoesDescartadas;
    }

    public void setTotalInstrucoesDescartadas(int totalInstrucoesDescartadas) {
        this.totalInstrucoesDescartadas = totalInstrucoesDescartadas;
    }

    public boolean isPredicaoAtiva() {
        return predicaoAtiva;
    }
}
