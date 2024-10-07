package instrucoes;

public class InstrucaoGenerica {
    private String opcode;
    private int oper1, oper2, oper3, temp1, temp2, temp3;
    private boolean valida;

    public InstrucaoGenerica() {
        valida = true;
    }

    public boolean isValida() {
        return valida;
    }

    public int getTemp3() {
        return temp3;
    }

    public int getTemp2() {
        return temp2;
    }

    public int getTemp1() {
        return temp1;
    }

    public int getOper3() {
        return oper3;
    }

    public int getOper2() {
        return oper2;
    }

    public int getOper1() {
        return oper1;
    }

    public void setOper1(int oper1) {
        this.oper1 = oper1;
    }

    public void setOper2(int oper2) {
        this.oper2 = oper2;
    }

    public void setOper3(int oper3) {
        this.oper3 = oper3;
    }

    public void setTemp1(int temp1) {
        this.temp1 = temp1;
    }

    public void setTemp2(int temp2) {
        this.temp2 = temp2;
    }

    public String getOpcode() {
        return opcode;
    }

    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    public void setTemp3(int temp3) {
        this.temp3 = temp3;
    }

    public void setValida(boolean valida) {
        this.valida = valida;
    }

    public String toString() {
        String retorno = "";
        if(!valida)
            retorno += "\u001B[9m";//Tachado para indicar instrução descartada
        switch (opcode) {
            case "add", "sub":
                retorno += "\u001B[34m" + opcode + " " + oper1 + " " + oper2 + " " + oper3 + "\u001B[0m";
                break;
            case "lw", "sw":
                retorno += "\u001B[93m" + opcode + " " + oper1 + " " + oper2 + " " + oper3 + "\u001B[0m";
                break;
            case "beq":
                retorno += "\u001B[35m" + opcode + " " + oper1 + " " + oper2 + " " + oper3 + "\u001B[0m";
                break;
            case "halt":
                retorno += "\u001B[33m" + opcode + "\u001B[0m";
                break;
            case "noop":
                retorno += "\u001B[31m noop \u001B[0m";
        }
        return retorno;
    }
}
