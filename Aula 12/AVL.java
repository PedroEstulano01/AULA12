import java.io.*;
import java.util.*;

class ArvoreAVL {
    class No {
        int chave;
        No esquerda, direita;
        int altura;

        No(int chave) {
            this.chave = chave;
            esquerda = direita = null;
            altura = 1;
        }
    }

    private No raiz;

    // Obter a altura do nó
    private int altura(No no) {
        if (no == null) {
            return 0;
        }
        return no.altura;
    }

    // Obter o fator de balanceamento de cada nó
    private int obterBalanceamento(No no) {
        if (no == null) {
            return 0;
        }
        return altura(no.esquerda) - altura(no.direita);
    }

    // Rotação à direita
    private No rotacaoDireita(No y) {
        No x = y.esquerda;
        No T2 = x.direita;

        x.direita = y;
        y.esquerda = T2;

        y.altura = Math.max(altura(y.esquerda), altura(y.direita)) + 1;
        x.altura = Math.max(altura(x.esquerda), altura(x.direita)) + 1;

        return x;
    }

    // Rotação à esquerda
    private No rotacaoEsquerda(No x) {
        No y = x.direita;
        No T2 = y.esquerda;

        y.esquerda = x;
        x.direita = T2;

        x.altura = Math.max(altura(x.esquerda), altura(x.direita)) + 1;
        y.altura = Math.max(altura(y.esquerda), altura(y.direita)) + 1;

        return y;
    }

    public void inserir(int chave) {
        raiz = inserirNo(raiz, chave);
    }

    private No inserirNo(No no, int chave) {
        if (no == null) {
            return new No(chave);
        }

        if (chave < no.chave) {
            no.esquerda = inserirNo(no.esquerda, chave);
        } else if (chave > no.chave) {
            no.direita = inserirNo(no.direita, chave);
        } else {
            return no;
        }

        no.altura = Math.max(altura(no.esquerda), altura(no.direita)) + 1;

        int balanceamento = obterBalanceamento(no);

        // Caso esquerda-esquerda
        if (balanceamento > 1 && chave < no.esquerda.chave) {
            return rotacaoDireita(no);
        }

        // Caso direita-direita
        if (balanceamento < -1 && chave > no.direita.chave) {
            return rotacaoEsquerda(no);
        }

        // Caso esquerda-direita
        if (balanceamento > 1 && chave > no.esquerda.chave) {
            no.esquerda = rotacaoEsquerda(no.esquerda);
            return rotacaoDireita(no);
        }

        // Caso direita-esquerda
        if (balanceamento < -1 && chave < no.direita.chave) {
            no.direita = rotacaoDireita(no.direita);
            return rotacaoEsquerda(no);
        }

        return no;
    }

    public void deletar(int chave) {
        raiz = deletarNo(raiz, chave);
    }

    private No deletarNo(No raiz, int chave) {
        if (raiz == null) {
            return raiz;
        }

        if (chave < raiz.chave) {
            raiz.esquerda = deletarNo(raiz.esquerda, chave);
        } else if (chave > raiz.chave) {
            raiz.direita = deletarNo(raiz.direita, chave);
        } else {
            if ((raiz.esquerda == null) || (raiz.direita == null)) {
                No temp = null;
                if (temp == raiz.esquerda) {
                    temp = raiz.direita;
                } else {
                    temp = raiz.esquerda;
                }

                if (temp == null) {
                    temp = raiz;
                    raiz = null;
                } else {
                    raiz = temp;
                }
            } else {
                No temp = menorValorNo(raiz.direita);
                raiz.chave = temp.chave;
                raiz.direita = deletarNo(raiz.direita, temp.chave);
            }
        }

        if (raiz == null) {
            return raiz;
        }

        raiz.altura = Math.max(altura(raiz.esquerda), altura(raiz.direita)) + 1;

        int balanceamento = obterBalanceamento(raiz);

        // Caso esquerda-esquerda
        if (balanceamento > 1 && obterBalanceamento(raiz.esquerda) >= 0) {
            return rotacaoDireita(raiz);
        }

        // Caso esquerda-direita
        if (balanceamento > 1 && obterBalanceamento(raiz.esquerda) < 0) {
            raiz.esquerda = rotacaoEsquerda(raiz.esquerda);
            return rotacaoDireita(raiz);
        }

        // Caso direita-direita
        if (balanceamento < -1 && obterBalanceamento(raiz.direita) <= 0) {
            return rotacaoEsquerda(raiz);
        }

        // Caso direita-esquerda
        if (balanceamento < -1 && obterBalanceamento(raiz.direita) > 0) {
            raiz.direita = rotacaoDireita(raiz.direita);
            return rotacaoEsquerda(raiz);
        }

        return raiz;
    }

    private No menorValorNo(No no) {
        No atual = no;
        while (atual.esquerda != null) {
            atual = atual.esquerda;
        }
        return atual;
    }

    public boolean buscar(int chave) {
        return buscarNo(raiz, chave) != null;
    }

    private No buscarNo(No raiz, int chave) {
        if (raiz == null || raiz.chave == chave) {
            return raiz;
        }

        if (raiz.chave < chave) {
            return buscarNo(raiz.direita, chave);
        }

        return buscarNo(raiz.esquerda, chave);
    }
}

public class AVL {
    public static void main(String[] args) {
        try {
            File arquivo = new File("dados100mil.txt");
            BufferedReader br = new BufferedReader(new FileReader(arquivo));
            ArvoreAVL avl = new ArvoreAVL();
            String linha;
            Random aleatorio = new Random();

            // Medir o tempo de inserção na árvore AVL
            long tempoInicial = System.currentTimeMillis();
            while ((linha = br.readLine()) != null) {
                try {
                    int numero = Integer.parseInt(linha.trim());
                    avl.inserir(numero);
                } catch (NumberFormatException e) {
                    System.err.println("Linha ignorada (não é um número válido): " + linha);
                }
            }
            long tempoFinal = System.currentTimeMillis();
            System.out.println("Tempo de inserção na Árvore AVL: " + (tempoFinal - tempoInicial) + " ms");

            br.close();

            // Gerar números aleatórios e realizar operações
            int[] chavesAleatorias = new int[50000];
            for (int i = 0; i < chavesAleatorias.length; i++) {
                chavesAleatorias[i] = aleatorio.nextInt(19999) - 9999; // Números entre -9999 e 9999
            }

            tempoInicial = System.currentTimeMillis();
            for (int chave : chavesAleatorias) {
                if (chave % 3 == 0) {
                    avl.inserir(chave);
                } else if (chave % 5 == 0) {
                    avl.deletar(chave);
                } else {
                    if (avl.buscar(chave)) {
                        // Contar ocorrências (não implementado)
                    }
                }
            }
            tempoFinal = System.currentTimeMillis();
            System.out.println("Tempo de operações na Árvore AVL: " + (tempoFinal - tempoInicial) + " ms");

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }
}
