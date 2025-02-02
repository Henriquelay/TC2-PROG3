package sistema;

import sistema.util.*;
import sistema.util.inconsistency.*;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

/**
* Classe para implementação do sistema de controle PPGI.
* É aqui que o pau quebra, o espaguete acontece, o compilador chora e mãe não vê.
* @author Henrique Layber
* @author Ezequiel Schneider Reinholtz
*/
public class SistemaPPGI implements Serializable {
// Relations
private TreeMap<Integer, Regra> regras;    // Ano início, Regras
private TreeMap<String, Veiculo> veiculos;  // Sigla, Veiculos
private TreeMap<Long, Docente> docentes; // Código docente, Docentes
private TreeMap<String, Publicacao> publicacoes; // Título, Publicacao
private int ano;

// Getters e Setters
public TreeMap<Integer,Regra> getRegras() {return this.regras;}
public TreeMap<String,Veiculo> getVeiculos() {return this.veiculos;}
public TreeMap<Long,Docente> getDocentes() {return this.docentes;}
public TreeMap<String,Publicacao> getPublicacoes() {return this.publicacoes;}
public int getAno() {return this.ano;}
private void setAno(int ano) {this.ano = ano;}
// private void setRegras(TreeMap<Integer, Regra> regras) {this.regras = regras;}
// private void setVeiculos(TreeMap<String, Veiculo> veiculos) {this.veiculos = veiculos;}
// private void setDocentes(TreeMap<Long, Docente> docentes) {this.docentes = docentes;}
// private void setPublicacoes(TreeMap<String, Publicacao> publicacoes) {this.publicacoes = publicacoes;}

// To print with standard function
@Override
public String toString() {
    String str = "";
    str += "\n=-=-=-=-=-=-=- Imprimindo Docentes =-=-=-=-=-=-=-\n\n";
    for(Docente e : this.getDocentes().values())
        str += e.toString() + "\n";
    str += "\n=-=-=-=-=-=-=- Imprimindo Veículos =-=-=-=-=-=-=-\n\n";
    for(Veiculo e : this.getVeiculos().values())
        str += e.toString() + "\n";
    str += "\n=-=-=-=-=-=-=- Imprimindo Publicações =-=-=-=-=-=-=-\n\n";
    for(Publicacao e : this.getPublicacoes().values())
        str += e.toString() + "\n";
    str += "\n=-=-=-=-=-=-=- Imprimindo Regras =-=-=-=-=-=-=-\n\n";
    for(Regra e : this.getRegras().values())
        str += e.toString() + "\n";
    str += "=-=- ANO: " + this.getAno() + "-=-=-=";
    return str;
}

/**
 * Constructor.
 * @param ano Ano que deve ser avaliado. Deve ser passado para que possa ser serializado junto com o resto do objeto.
 */
public SistemaPPGI(int ano) {
    this.regras = new TreeMap<Integer, Regra>();
    this.veiculos = new TreeMap<String, Veiculo>();
    this.docentes = new TreeMap<Long, Docente>();
    this.publicacoes = new TreeMap<String, Publicacao>();
    this.setAno(ano);
}

/**
 * Lê corretamente o arquivo de docentes.
 * @param fileName Nome do arquivo.
 * @throws IOException Arquivo inexistente ou sem permissão de acesso
 * @throws IllegalArgumentException Erro de parsing
 * @throws Inconsistencia Classe mãe abstrata das inconsistências do trabalho.Codigo Existencia de dois códigos iguais (docente ou veículo)
 */
private void lerArquivoDocentes(String fileName) throws IOException, IllegalArgumentException, InconsistenciaCodigo {
    FileReader fr = new FileReader(fileName);
    Scanner scanner = new Scanner(fr);
    String str = "";
    String[] strTok;
    scanner.nextLine(); // Ignora primeira linha

    try {
        while(scanner.hasNext()) {
            str = scanner.nextLine();
            strTok = str.split(";");
            if(strTok.length != 4 && strTok.length != 5) {
                throw new IllegalArgumentException("Erro de formatação");
            }
            for(int i = 0; i < strTok.length; i++) {
                strTok[i] = strTok[i].trim();   // Remove whitespace from beggining and end. Both spaces and tab will be removed.
            }

            long key = Long.parseLong(strTok[0]);
            if(this.getDocentes().containsKey(key)) {    // If key is already inserted
                throw new InconsistenciaCodigo("docente", Long.toString(key));
            }

            Docente docente = new Docente(strTok[1], key, strTok[2], strTok[3], strTok.length == 5);

            this.getDocentes().put(new Long(key), docente);
        }
    } finally {
        scanner.close();
    }
}

/**
 * Lê corretamente o arquivo de veículos.
 * @param fileName Nome do arquivo.
 * @throws IOException Arquivo inexistente ou sem permissão de acesso
 * @throws FileNotFoundException Arquivo não encontrado.
 * @throws IllegalArgumentException Erro de parsing
 * @throws Inconsistencia Classe mãe abstrata das inconsistências do trabalho.Codigo Existencia de dois códigos iguais (docente ou veículo)
 * @throws Inconsistencia Classe mãe abstrata das inconsistências do trabalho.Tipo Tipo de veículo inexistente
 */
private void lerArquivoVeiculos(String fileName) throws IOException, FileNotFoundException, IllegalArgumentException, InconsistenciaCodigo, InconsistenciaTipo {
    FileReader fr = new FileReader(fileName);
    Scanner scanner = new Scanner(fr);
    String str = "";
    String[] strTok;
    scanner.nextLine(); // Ignora primeira linha

    try {
        while(scanner.hasNext()) {
            Veiculo vei = null;
            str = scanner.nextLine();
            strTok = str.split(";");
            if(strTok.length != 4 && strTok.length != 5) {
                throw new IllegalArgumentException("Erro de formatação");
            }

            for(int i = 0; i < strTok.length; i++) {
                strTok[i] = strTok[i].trim();   // Remove whitespace from beggining and end. Both spaces and tab will be removed.
            }
            strTok[0] = strTok[0].trim(); // N sei pq mas isso funciona

            if(this.getVeiculos().containsKey(strTok[0])) {
                throw new InconsistenciaCodigo("veículo", strTok[0]);
            }
            strTok[3] = strTok[3].replace(',', '.');    // Trata a vírgula do impacto

            switch(strTok[2]) {
                case "c":
                case "C":
                    vei = new Conferencia(strTok[1], strTok[0], Double.parseDouble(strTok[3]));
                break;
                case "p":
                case "P":
                    vei = new Periodico(strTok[1], strTok[0], Double.parseDouble(strTok[3]), strTok[4]);
                break;
                default:
                    throw new InconsistenciaTipo(strTok[0], strTok[2]);
            }

            this.getVeiculos().put(strTok[0], vei);
        }
    } finally {
        scanner.close();
    }
}

/**
 * Lê corretamente o arquivo de publicações.
 * @param fileName Nome do arquivo
 * @throws IOException Arquivo inexistente ou sem permissão de acesso
 * @throws FileNotFoundException Arquivo não encontrado.
 * @throws IllegalArgumentException Erro de parsing
 * @throws Inconsistencia Classe mãe abstrata das inconsistências do trabalho.SiglaVeiculoPublicacao Sigla de veículo especificada para uma qualificação não foi definida na planilha de veículos.
 * @throws Inconsistencia Classe mãe abstrata das inconsistências do trabalho.DocentePublicacao
 */
private void lerArquivoPublicacoes(String fileName) throws IOException, FileNotFoundException, IllegalArgumentException, InconsistenciaSiglaVeiculoPublicacao, InconsistenciaDocentePublicacao {
    FileReader fr = new FileReader(fileName);
    Scanner scanner = new Scanner(fr);
    String str = "";
    String[] strTok;
    scanner.nextLine(); // Ignora primeira linha
    Publicacao pub;
    Veiculo vei;
    TreeMap<Long, Docente> docentes;

    try {
        while(scanner.hasNext()) {
            str = scanner.nextLine();
            strTok = str.split(";");
            if(strTok.length != 9) {
                throw new IllegalArgumentException("Erro de formatação");
            }

            for(int i = 0; i < strTok.length; i++) {
                strTok[i] = strTok[i].trim();   // Remove whitespace from beggining and end. Both spaces and tab will be removed.
            }

            docentes = new TreeMap<Long, Docente>();
            vei = this.getVeiculos().get(strTok[1]);
            if(vei == null) {
                throw new InconsistenciaSiglaVeiculoPublicacao(strTok[2], strTok[1]);
            }

            for(String s : strTok[3].split(",")) {
                s = s.trim();
                long key = Long.parseLong(s);
                docentes.put(key, this.getDocentes().get(key));
            }
            
            switch(strTok[6]) {
                case "":
                    pub = new PubPeriodico(Integer.parseInt(strTok[0]), vei, strTok[2], docentes, Integer.parseInt(strTok[4]), Integer.parseInt(strTok[5]), Integer.parseInt(strTok[7]), Integer.parseInt(strTok[8]));
                break;
                default:
                    pub = new PubConferencia(Integer.parseInt(strTok[0]), vei, strTok[2], docentes, Integer.parseInt(strTok[4]), strTok[6], Integer.parseInt(strTok[7]), Integer.parseInt(strTok[8]));
            }

            this.getPublicacoes().put(strTok[2], pub);
            vei.getPublicacoes().put(strTok[2], pub);
            for(Map.Entry<Long,Docente> e : docentes.entrySet()) {
                if(!this.getDocentes().containsKey(e.getKey())) {
                    throw new InconsistenciaDocentePublicacao(pub.getTitulo(), vei.getSigla());
                }
                e.getValue().getPublicacoes().put(strTok[2], pub);
            }

            strTok = null;
        }
    } finally {
        scanner.close();
    }
}

/**
 * Lê corretamente o arquivo de qualis.
 * @param fileName Nome do arquivo.
 * @throws IOException Arquivo inexistente ou sem permissão de acesso
 * @throws FileNotFoundException Arquivo não encontrado.
 * @throws IllegalArgumentException Erro de parsing
 * @throws Inconsistencia Classe mãe abstrata das inconsistências do trabalho.SiglaVeiculoQualis Qualis especificado para uma qualificação de veículo não é nenhuma das categorias existentes: A1, A2, B1, B2, B3, B4, B5 ou C.
 * @throws Inconsistencia Classe mãe abstrata das inconsistências do trabalho.SiglaVeiculoPublicacao Sigla de veículo especificada para uma qualificação não foi definida na planilha de veículos.
 * @throws Inconsistencia Classe mãe abstrata das inconsistências do trabalho.QualisVeiculo
 */
private void lerArquivoQualis(String fileName) throws IOException, FileNotFoundException, IllegalArgumentException, InconsistenciaSiglaVeiculoQualis, InconsistenciaSiglaVeiculoPublicacao, InconsistenciaQualisVeiculo {
    FileReader fr = new FileReader(fileName);
    Scanner jordana = new Scanner(fr);
    TreeMap<String, Veiculo> veiculos = this.getVeiculos();
    String str = "";
    String[] strTok;
    jordana.nextLine(); // Ignora primeira linha

    try {
        while(jordana.hasNext()) {
            str = jordana.nextLine();
            strTok = str.split(";");
            if(strTok.length != 3) {
                throw new IllegalArgumentException("Erro de formatação");
            }

            for(int i = 0; i < strTok.length; i++) {
                strTok[i] = strTok[i].trim();   // Remove whitespace from beggining and end. Both spaces and tab will be removed.
            }

            if(!this.getVeiculos().containsKey(strTok[1])) {
                throw new InconsistenciaSiglaVeiculoQualis(strTok[0], strTok[1]);
            } if(!isValidQualis(strTok[2])) {
                throw new InconsistenciaQualisVeiculo(strTok[1], Integer.parseInt(strTok[0]), strTok[2]);
            }
            veiculos.get(strTok[1]).getQualis().put(Integer.parseInt(strTok[0]), strTok[2]);
        }
    } finally {
        jordana.close();
    }
}

/**
 * Lê corretamente o arquivo de regras.
 * @param fileName Nome do arquivo.
 * @throws IOException Arquivo inexistente ou sem permissão de acesso
 * @throws FileNotFoundException Arquivo não encontrado.
 * @throws IllegalArgumentException Erro de parsing
 * @throws Inconsistencia Classe mãe abstrata das inconsistências do trabalho.QualisRegra
 */
private void lerArquivoRegras(String fileName) throws IOException, FileNotFoundException, IllegalArgumentException, InconsistenciaQualisRegra {
    FileReader fr = new FileReader(fileName);
    Scanner jordana
     = new Scanner(fr);
    String str = "";
    String[] strTok;
    jordana
    .nextLine(); // Ignora primeira linha

    try {
        while(jordana
        .hasNext()) {
            str = jordana
            .nextLine();
            strTok = str.split(";");
            if(strTok.length != 7) {
                throw new IllegalArgumentException("Erro de formatação");
            }
            for(String s : strTok) {
                s = s.trim();   // Remove whitespace from beggining and end. Both spaces and tab will be removed.
            }
            strTok[4] = strTok[4].replace(",", ".");    // To read as double
            TreeMap<String, Integer> pontos = new TreeMap<String,Integer>();
            String[] qualis = strTok[2].split(",");
            String[] valorPontos = strTok[3].split(",");
            if(qualis.length != valorPontos.length) {
                throw new IllegalArgumentException("Erro de formatação");
            }
            for(String s : qualis) {
                s = s.trim();
            }
            for(String s : valorPontos) {
                s = s.trim();
            }
            for(int i = 0; i < qualis.length; i++) {
                if(!isValidQualis(qualis[i])) {
                    throw new InconsistenciaQualisRegra(strTok[0], qualis[i]);
                }
                pontos.put(qualis[i], new Integer(Integer.parseInt(valorPontos[i])));
            }
            this.getRegras().put(new Integer(strTok[0].split("/")[2]), new Regra(strTok[0], strTok[1], Integer.parseInt(strTok[5]), Double.parseDouble(strTok[4]), Double.parseDouble(strTok[6]), pontos));
        }
    } finally {
        jordana
        .close();
    }
}

/**
 * Lê corretamente todos os arquivos que o programa precisa. Tudo nessa única função para garantir que não há arquivo faltando e que tudo seja feito na ordem correta.
 * @param fileDocentes Arquivo de docentes.
 * @param fileVeiculos Arquivo de veículos.
 * @param filePublicacoes Arquivo de publicacoes.
 * @param fileQualis Arquivo de qualis.
 * @param fileRegras Arquivo de regras.
 * @throws IOException Arquivo inexistente ou sem permissão de acesso
 * @throws FileNotFoundException Arquivo não encontrado.
 * @throws IllegalArgumentException Erro de parsing
 * @throws Inconsistencia Classe mãe abstrata das inconsistências do trabalho.
 */
public void lerArquivos(String fileDocentes, String fileVeiculos, String filePublicacoes, String fileQualis, String fileRegras) throws IOException, FileNotFoundException, IllegalArgumentException, Inconsistencia {
    this.lerArquivoDocentes(fileDocentes);
    this.lerArquivoVeiculos(fileVeiculos);
    this.lerArquivoPublicacoes(filePublicacoes);
    this.lerArquivoQualis(fileQualis);
    this.lerArquivoRegras(fileRegras);
}

/**
 * Printa o arquivo de recredenciamento.
 * @param fileName Nome do arquivo de recredenciamento.
 * @throws IOException Arquivo inexistente ou sem permissão de acesso
 */
public void printarRelatorioRecredenciamento(String fileName) throws IOException {
    FileWriter fw = new FileWriter(fileName);

    LinkedList<Docente> lld = new LinkedList<Docente>(this.getDocentes().values());
    lld.sort(Docente.ComparadorDocente);

    fw.append("Docente;Pontuação;Recredenciado?\n");

    for(Docente doc : lld) {
        fw.append(doc.getNome() + ";");

        // Pontos {
        double pontosdoc = 0;
        Regra regra = this.selectRegra(this.getAno());
        if(regra != null) {
            for(Publicacao pub : doc.getPublicacoes().values()) {
                if(pub.getAno() < this.getAno() - regra.getAnosAvaliados() || pub.getAno() >= this.getAno()) {
                    continue;
                }
                Map.Entry<Integer,String> entradaQualis = pub.getVeiculo().getQualis().floorEntry(this.getAno());
                if(entradaQualis == null) {
                    continue;
                }
                String qualis = entradaQualis.getValue();
                double pontospub = regra.getPontos().floorEntry(qualis).getValue();
                if(pub.getTipo() == 'P') {
                    pontospub *= regra.getMultPeriodicos();
                }
                pontosdoc += pontospub;
            }
            String pontos = String.format("%.1f", pontosdoc);
            fw.append(pontos.replace(".", ",") + ";");
            // } Pontos
            // Resultado {
            if(doc.getIsCoodenador()) {
                fw.append("Coordenador\n");
            } else {
                if(this.getAno() - doc.getDataIngresso().get(MyCalendar.YEAR) <= 3) {
                    fw.append("PPJ\n");
                } else {
                    if(this.getAno() - doc.getDataNascimento().get(MyCalendar.YEAR) >= 60) {
                        fw.append("PPS\n");
                    } else {
                        if(pontosdoc >= regra.getPontuacaoMinima()) {
                            fw.append("Sim\n");
                        } else {
                        fw.append("Não\n");
                        }
                    }
                }
            }
            // }Resultado;
        }
    }
    fw.close();
}

/**
 * Printa o arquivo de publicações.
 * @param fileName Nome do arquivo de publicações.
 * @throws IOException Arquivo inexistente ou sem permissão de acesso
 */
public void printarRelatorioPublicacoes(String fileName) throws IOException {
    FileWriter fw = new FileWriter(fileName);
    LinkedList<Publicacao> ll = new LinkedList<Publicacao>();
    fw.append("Ano;Sigla Veículo;Veículo;Qualis;Fator de Impacto;Título;Docentes\n");

    for(Map.Entry<String,Publicacao> e : this.getPublicacoes().entrySet()) {
        ll.add(e.getValue());
    }
    ll.sort(Publicacao.ComparadorPublicacao);
    for(Publicacao p : ll) {
        fw.append(p.toCSV(this.getAno()));
    }
    fw.close();
}

/**
 * Printa o arquivo de estatísticas.
 * @param fileName Nome do arquivo de estatísticas.
 * @throws IOException Arquivo inexistente ou sem permissão de acesso
 */
public void printarEstatisticas(String fileName) throws IOException {
    FileWriter fw = new FileWriter(fileName);

    TreeMap<String,LinkedList<Publicacao>> data = new TreeMap<String,LinkedList<Publicacao>>();
    // <Qualis,<Publicacao>>

    fw.append("Qualis;Qtd. Artigos;Média Artigos / Docente\n");
    // Data structure filling
    for(Map.Entry<String,Veiculo> eV : this.getVeiculos().entrySet()) {
        for(Map.Entry<Integer,String> eQ : eV.getValue().getQualis().entrySet()) {
            LinkedList<Publicacao> ll = new LinkedList<Publicacao>();
            ll.addAll(eV.getValue().getPublicacoes().values());
            if(data.containsKey(eQ.getValue())) {
                data.get(eQ.getValue()).addAll(ll);
            } else {
                data.put(eQ.getValue(), ll);
            }
        }
    }

    // Poderia navegar o TreeMap, mas a key que não existir (caso nenhuma publicação tenha esse valor) ficaria vazia.
    String[] qualises = {"A1", "A2", "B1", "B2", "B3", "B4", "B5", "C"};
    // Print
    for(String q : qualises) {
        if(!data.containsKey(q)) {
            fw.append(q + ";0;0,00\n"); // Sempre vai ser igual quando n tiver publicações com o qualis analizado
            continue;
        }
        LinkedList<Publicacao> llP = data.get(q);
        int qtdArtigos = llP.size();
        fw.append(q + ";" + qtdArtigos + ";");
        double artigosPorDocente = 0;
        for(Publicacao p : llP) {
            artigosPorDocente += (double) 1 / (double) p.getDocentes().size();
        }
        fw.append(String.format("%.2f", artigosPorDocente).replace(".", ",") + "\n");
    }
    
    fw.close();
}

/**
 * Macro para printar todos os arquivos.
 * @param fileNameRecred Nome do arquivo de recredenciamento.
 * @param fileNamePub Nome do arquivo de publicações.
 * @param fileNameEst Nome do arquivo de estatísticas.
 * @throws IOException Arquivo inexistente ou sem permissão de acesso
 */
public void printarTodosArquivos(String fileNameRecred, String fileNamePub, String fileNameEst) throws IOException {
    this.printarRelatorioRecredenciamento(fileNameRecred);
    this.printarRelatorioPublicacoes(fileNamePub);
    this.printarEstatisticas(fileNameEst);
}

/**
 * Macro para printar todos os arquivos.
 * Chamar sem argumento faz o programa usar os nomes definidos no relatório para a saída.
 * @param outFolder Diretório de saída. Incluir o '/' no final.
 * @throws IOException Arquivo inexistente ou sem permissão de acesso
 */
public void printarTodosArquivos (String outFolder) throws IOException {
    this.printarTodosArquivos(outFolder + "1-recredenciamento.csv", outFolder + "2-publicacoes.csv", outFolder + "3-estatisticas.csv");
}

/**
 * Acabou que nem reutilizei muito essa função né
 * @param s Qualis
 * @return Se o Qualis 's' é um dos tipos de Qualis reconhecido
 */
private static boolean isValidQualis(String s) {
    switch(s){
        case "A1":
        case "A2":
        case "B1":
        case "B2":
        case "B3":
        case "B4":
        case "B5":
        case "C":
        return true;
        default:
        return false;
    }
}

/**
* Seleciona a Regra em vigência para o ano passado.
* Considerações:
* As datas de início e fim da regra são o início e o fim do ano;
* @param anoInt Ano qual deve ser buscado uma regra pra ele
* @return Regra selecionada para o ano em questão. 'null' quando não há.
*/
private Regra selectRegra(int anoInt) {
    Regra selected = null;
    MyCalendar ano = new MyCalendar(anoInt, 1, 1);
    
    //NOTE Tentei o usar o TreeMap.floorEntry(ano) mas tava bugando pra desserializar
    for(Regra r : this.getRegras().values()) {
        if(r.getDataInicio().compareTo(ano) <= 0 && r.getDataFinal().compareTo(ano) >= 0) {
            selected = r;
        }
    }
    if(selected == null) { 
        return null;
    }
    return selected;
}

/**
 * Serializa a classe principal.
 * @param fileName Arquivo de serialização de deverá ser criado/substituído.
 * @throws IOException Arquivo inexistente ou sem permissão de acesso
 */
public void serialize(String fileName) throws IOException {
    // SistemaPPGI;
    FileOutputStream fos = new FileOutputStream(fileName);
    ObjectOutputStream out = new ObjectOutputStream(fos);
    out.writeObject(this);
    out.close();
    fos.close();
}

/**
 * Desserializa a classe principal
 * @param fileName Arquivo de serialização para ser carregado.
 * @return SistemaPPGI que estava armazenado em 'fileName'.
 * @throws IOException Arquivo inexistente ou sem permissão de acesso
 */
public SistemaPPGI desserialize(String fileName) throws IOException {
    SistemaPPGI sys = null;
    try {
        // SistemaPPGI
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream in = new ObjectInputStream(fis);
        sys = (SistemaPPGI) in.readObject();
        in.close();
        fis.close();
    } catch (ClassNotFoundException c) {
        System.out.println("Classe não encontada em 'recredenciamento.dat'. Fiz alguma merda muito feia.");
        System.exit(1);
    }

    return sys;
}

}
