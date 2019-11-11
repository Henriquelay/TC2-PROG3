package sistema;

import java.io.Serializable;
import sistema.util.*;
import java.util.Map;
import java.util.TreeMap;

/**
 * Classe para implementação das Regras de avaliação PPGI
 * @author Henrique Layber
 * @version 1.0
 */
public class Regra implements Comparable<Regra>, Serializable {
    private int anosAvaliados;
    private float multPeriodicos;
    private float pontuacaoMinima;
    private MyCalendar dataInicio;
    private MyCalendar dataFinal;
    private TreeMap<String,Integer> pontos;


    public int getAnosAvaliados() {return this.anosAvaliados;}
    public float getMultPeriodicos() {return this.multPeriodicos;}
    public float getPontuacaoMinima() {return this.pontuacaoMinima;}
    public MyCalendar getDataInicio() {return this.dataInicio;}
    public MyCalendar getDataFinal() {return this.dataFinal;}
    public TreeMap<String,Integer> getPontos() {return this.pontos;}
    private void setAnosAvaliados(int anosAvaliados) {this.anosAvaliados = anosAvaliados;}
    private void setMultPeriodicos(float multPeriodicos) {this.multPeriodicos = multPeriodicos;}
    private void setPontuacaoMinima(float pontuacaoMinima) {this.pontuacaoMinima = pontuacaoMinima;}
    /**
     * 
     * @deprecated
     */
    private void setDataInicio(MyCalendar dataInicio) {this.dataInicio = dataInicio;}
    /**
     * 
     * @param dataInicio <code>dd/mm/aaaa</code>
     */
    private void setDataInicio(String dataInicio) throws IllegalArgumentException {
        String[] datePart = dataInicio.split("/");
        if(datePart.length != 3)
            throw new IllegalArgumentException(dataInicio);
        this.dataInicio = new MyCalendar(Integer.parseInt(datePart[2]), Integer.parseInt(datePart[1]), Integer.parseInt(datePart[0]));
    }
    /**
     * 
     * @deprecated
     */
    private void setDataFinal(MyCalendar dataFinal) {this.dataFinal = dataFinal;}
    /**
     * 
     * @param dataFinal <code>dd/mm/aaaa</code>
     */
    private void setDataFinal(String dataFinal) throws IllegalArgumentException {
        String[] datePart = dataFinal.split("/");
        if(datePart.length != 3)
            throw new IllegalArgumentException(dataFinal);
        this.dataFinal = new MyCalendar(Integer.parseInt(datePart[2]), Integer.parseInt(datePart[1]), Integer.parseInt(datePart[0]));
    }
    public void setPontos(String qualis, int pontos) {
        this.getPontos().put(qualis, new Integer(pontos));
    }

    // To compare Regras. It returns the comparation of this dataInicio
    @Override public int compareTo(Regra r) {
        return this.getDataInicio().compareTo(r.getDataInicio());
    }

    // To print with standard function
    @Override public String toString() {
        String str = "Data Início: " + this.getDataInicio().toString() +
        " Data final: " + this.getDataFinal().toString() +
        "\nPontuação mínima: " + this.getPontuacaoMinima() +
        "\nMultiplicador de periódicos: " + this.getMultPeriodicos() + "\nAnos Avaliados: " + this.getAnosAvaliados() + "\nPontos {\n";

        // Iterating through the Treemap
        for(Map.Entry<String,Integer> e : this.getPontos().entrySet()) {
            str += "\t" + e.getKey() + ":" + e.getValue() + "\n";
        }

        str += "}";
        return str;
    }

    // Constructor
    /**
     * @deprecated
     */
    public Regra(MyCalendar dataInicio, MyCalendar dataFinal, int anosAvaliados, float multPeriodicos, float pontuacaoMinima) {
        this.setDataInicio(dataInicio);
        this.setDataFinal(dataFinal);
        this.setAnosAvaliados(anosAvaliados);
        this.setMultPeriodicos(multPeriodicos);
        this.setPontuacaoMinima(pontuacaoMinima);
    }
    /**
     * 
     * @param dataInicio <code>"dd/mm/aaaa"</code>
     * @param dataFinal <code>"dd/mm/aaaa"</code>
     */
    public Regra(String dataInicio, String dataFinal, int anosAvaliados, float multPeriodicos, float pontuacaoMinima) {
        this.setDataInicio(dataInicio);
        this.setDataFinal(dataFinal);
        this.setAnosAvaliados(anosAvaliados);
        this.setMultPeriodicos(multPeriodicos);
        this.setPontuacaoMinima(pontuacaoMinima);
        this.pontos = new TreeMap<String,Integer>();
    }
}