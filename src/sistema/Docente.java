package sistema;

import sistema.util.MyCalendar;

/**
 * Classe para implementação dos docentes PPGI
 * @author Henrique Layber
 * @version 1.0
 */
public class Docente {
    private String nome;
    private long codigo;
    private MyCalendar dataNascimento;
    private MyCalendar dataIngresso;
    private boolean isCoodenador;

    // Getters and Setters
    public String getNome() {return this.nome;}
    public long getCodigo() {return this.codigo;}
    public MyCalendar getDataNascimento() {return this.dataNascimento;}
    public MyCalendar getDataIngresso() {return this.dataIngresso;}
    public boolean getIsCoodenador() {return this.isCoodenador;}
    private void setNome(String nome) {this.nome = nome;}
    private void setCodigo(long codigo) {this.codigo = codigo;}
    private void setDataIngresso(MyCalendar dataIngresso) {this.dataIngresso = dataIngresso;}
    private void setDataNascimento(MyCalendar dataNascimento) {this.dataNascimento = dataNascimento;}
    private void setDataIngresso(String dataIngresso) throws IllegalArgumentException{this.setDataIngresso(MyCalendar.toDate(dataIngresso));}
    private void setDataNascimento(String dataNascimento) throws IllegalArgumentException{this.setDataNascimento(MyCalendar.toDate(dataNascimento));}
    private boolean isIsCoodenador() {return this.isCoodenador;}
    private void setIsCoodenador(boolean isCoodenador) {this.isCoodenador = isCoodenador;}

    // To print with standard function
    @Override
    public String toString() {
        return "╔Nome:\t\t" + this.getNome() +
            "\n╠Codigo:\t\t" + this.getCodigo() +
            "\n╠Data de nascimento:" + this.getDataNascimento() +
            "\n╠Data de ingresso:\t" + this.getDataIngresso() +
            "\n╚Coodenador?\t" + this.isIsCoodenador();
    }
    
    // Constructor
    /**
     * @deprecated
     * @param nome string
     * @param codigo long
     * @param dataNascimento MyCalendar
     * @param dataIngresso MyCalendar
     * @param isCoodenador bool
     */
    public Docente(String nome, long codigo, MyCalendar dataNascimento, MyCalendar dataIngresso, boolean isCoodenador) {
        this.setNome(nome);
        this.setCodigo(codigo);
        this.setDataNascimento(dataNascimento);
        this.setDataIngresso(dataIngresso);
        this.setIsCoodenador(isCoodenador);
    }

    /**
     * @param nome String
     * @param codigo long
     * @param dataNascimento String
     * @param dataIngresso String
     * @param isCoodenador bool
     */
    public Docente(String nome, long codigo, String dataNascimento, String dataIngresso, boolean isCoodenador) {
        this.setNome(nome);
        this.setCodigo(codigo);
        this.setDataNascimento(dataNascimento);
        this.setDataIngresso(dataIngresso);
        this.setIsCoodenador(isCoodenador);
    }
}