package processador;

import io.iO;

import java.util.ArrayList;

public class Processador {
    private final int[] R;
    private final ArrayList<String> instrucoes;
    private boolean desvioIncorreto;
    private final int[] memory;
    private int totalCiclos;

    // Dependências de Etapas
    private final InstructionFetch instructionFetch;
    private final Decode decode;
    private final Execute execute;
    private final MemAcess memAcess;
    private final WriteBack writeBack;

    public Processador() {
        totalCiclos = 0;
        memory = new int[100];//A memoria tem arbitrariamente apenas 100 endereços
        desvioIncorreto = false;
        R = new int[32];
        R[0] = 0;
        instrucoes = iO.leArquivo();
        // Inicializa as Etapas
        this.instructionFetch = new InstructionFetch(this);
        this.decode = new Decode(this);
        this.execute = new Execute(this);
        this.memAcess = new MemAcess(this);
        this.writeBack = new WriteBack(this);
    }

    public void startExecution() {
        dataMemoryLoader();
        String[] firstInstruction = null;
        String ANSI_RESET = "\u001B[0m";
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_CYAN = "\u001B[36m";
        String ANSI_BLUE = "\u001B[34m";

        //Inicia o loop de execução das instruções
        while (InstructionFetch.pC < instrucoes.size() + 5) {
            totalCiclos++;
            //Realiza a passagem de instruções entre as etapas final-inicio
            writeBack.setInstrucaoAtual(memAcess.getInstrucaoAtual());
            memAcess.setInstrucaoAtual(execute.getInstrucaoAtual());
            execute.setInstrucaoAtual(decode.getInstrucaoAtual());
            decode.InstructionDecode(firstInstruction);
            //Caso um desvio incorreto foi tomado, descarta da instrução da codificação (acabou de ser decodificada)
            if (desvioIncorreto)
                decode.getInstrucaoAtual().setValida(false);
            firstInstruction = instructionFetch.fetchInstruction();

            //Caso a primeira instrução seja nula, seta a instrução como noop
            if(firstInstruction == null) {
                InstructionFetch.pC++;
                instructionFetch.setInstrucao(new String[]{"noop"});
            }


            //Printa os dados do ciclo, registradores, etapas e suas instruções, etc
            System.out.print(ANSI_CYAN + "Registradores: ");
            for (int j = 0; j < R.length; j++) {
                System.out.print(R[j] + (j == R.length - 1 ? "" : " | "));
            }
            System.out.println("\n");
            System.out.print(ANSI_BLUE + "Execução " + totalCiclos + ": " + ANSI_RESET);

            System.out.print(ANSI_GREEN + instructionFetch + instructionFetch.getInstrucaoAtual() + " ");
            System.out.print(ANSI_GREEN + decode + " [" + decode.getInstrucaoAtual() + ANSI_GREEN + "] ");
            System.out.print(ANSI_GREEN + execute + " [" + execute.getInstrucaoAtual() + ANSI_GREEN + "] ");
            System.out.print(ANSI_GREEN + memAcess + " [" + memAcess.getInstrucaoAtual() + ANSI_GREEN + "] ");
            System.out.print(ANSI_GREEN + writeBack + " [" + writeBack.getInstrucaoAtual() + ANSI_GREEN + "] ");

            System.out.println("\n" + ANSI_RESET);

            if (desvioIncorreto)
                desvioIncorreto = false;

            //Executa a lógica de cada Etapa
            writeBack.writeBack();
            memAcess.memoryAcess();
            execute.execute();
            //Caso exista um desvio incorretamente tomado desativa invalida as instruções anteriores
            if (desvioIncorreto) {
                decode.getInstrucaoAtual().setValida(false);
                execute.getInstrucaoAtual().setValida(false);
            }
        }
        System.out.println(ANSI_BLUE + "Total de Execuções: " + totalCiclos + ANSI_RESET);
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

    public  int[] getR() {
        return R;
    }

    public  ArrayList<String> getInstrucoes() {
        return instrucoes;
    }

    public  void setDesvioIncorreto(boolean desvioIncorreto) {
        this.desvioIncorreto = desvioIncorreto;
    }

    public  int[] getMemory() {
        return memory;
    }

}
