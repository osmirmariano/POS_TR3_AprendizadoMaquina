
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
//classe para inicializar o vetor, treinar a rede e testar

public class exemplo {

    public static void main(String[] args) throws FileNotFoundException {
        Rede_neural n = new Rede_neural(500, 500, 10, 0.02, 1, 2);

        int i; //variável auxiliar
        File train_data = new File("train_dados.txt");
        Scanner sc = new Scanner(train_data);

        //atribuindo os valores para o vetor de entradas PS: 0 primeiro valor de cada posição é o BIAS
        double entradas[][] = new double[500][500];
        for (i = 0; i < 500; i++) {
            for (int j = 0; j < 500; j++) {
                entradas[i][j] = sc.nextDouble();
                // System.out.println("ENTR " + entradas[i][j]);
                
            }
        }
        //atribuindo os valores para os targets de acordo com as entradas
        double targets[][] = new double[500][10];
        File train_labels = new File("train_target.txt");
        Scanner sc2 = new Scanner(train_labels);
        Random gerador = new Random();
        for (i = 0; i < 500; i++) {
            int val = sc2.nextInt();
            for (int j = 0; j < 10; j++) {
                int a = gerador.nextInt(2);
                // System.out.println("A " + a + " VAL: " + val);
                if (a == val) {
                    targets[i][j] = 1;
                } else {
                    targets[i][j] = -1;
                }
            }
        }

        File test_data = new File("test_dados.txt");
        Scanner sc_t = new Scanner(test_data);

        //atribuindo os valores para o vetor de entradas PS: 0 primeiro valor de cada posição é o BIAS
        double entradas_test[][] = new double[500][500];
        for (i = 0; i < 500; i++) {
            for (int j = 0; j < 500; j++) {
                entradas_test[i][j] = sc_t.nextDouble();
            }
        }
        //atribuindo os valores para os targets de acordo com as entradas
        double targets_test[][] = new double[500][10];
        File test_labels = new File("test_target.txt");
        Scanner sc2_t = new Scanner(test_labels);
        for (i = 0; i < 500; i++) {
            int val = sc2_t.nextInt();
            for (int j = 0; j < 10; j++) {
                if (j == val) {
                    targets_test[i][j] = 1;
                } else {
                    targets_test[i][j] = -1;
                }
            }
        }

        /**
         * ******************************************************************************
         * Calculando os resultados obtidos pela rede inicializada com valores
         * aleatórios
         * ********************************************************************************
         */
//        System.out.println("\nAntes do Aprendizado ....\n");
//        for (i = 0; i < entradas_test.length; i++) { //calculando a saída da propagação para cada entrada
//            n.propagação(entradas_test[i]); //propaga o sinal de entrada i pela rede
//            double saida[] = new double[10];
//            for(int j = 0; j < 10; j++) {
//                saida[j] = n.outA[j];
//            }
//            //System.out.println(result(saida));
//            //break;
//        }

        /**
         * ****************************************************************************
         * REALIZANDO O TREINAMENTO DA REDE
         * ****************************************************************************
         */
        int iteração = 2; //inicializando a itereação com 2
        n.ErroTotal = 1; //inicializando o erro total com 1
        while (iteração < 10000000 && n.ErroTotal > 0.002) { // o número de épocas máximo é 10000 e o valor do erro tolerável é 0.002 
           n.aprendizado(entradas[iteração % entradas.length], targets[iteração % entradas.length]); //realizando o aprendizado para a entrada com seu respectivo target
           iteração = iteração + 1; //incrementando a iteração
        }

        /**
         * ****************************************************************************
         * Calculando os resultados obtidos pela rede após seu treinamento
         * ****************************************************************************
         */
        System.out.println("\nDepois do Aprendizado ....\n");
        double acertos = 0;
        double erros = 0;

        for (i = 0; i < entradas_test.length; i++) { //calculando a saída da propagação para cada entrada
            n.propagação(entradas_test[i]); //propaga o sinal de entrada i pela rede

            double saida[] = new double[10];
            double saida_esperada[] = new double[10];
            for (int j = 0; j < 10; j++) {
                saida[j] = n.outA[j];
                saida_esperada[j] = targets_test[i][j];
            }
            if(result(saida_esperada).equals(result(saida))) {
                acertos++;
            }
            else{
                erros++;
            }
            
            System.out.println("[" + i +"] Saida Esperada: [" + result(saida_esperada) +"]  Saida Real: [" + result(saida) + "]");
            //break;
        }

        System.out.println("\nACERTOS: " + acertos);
        System.out.println("PRECISÃO: " + (acertos/(acertos+acertos)));
        System.out.println("RECALL: " + (acertos/(acertos+erros)));
        System.out.println("ACURÁCIA: " + (acertos/entradas_test.length)*100 + "%\n");
    }

    public static String result(double saida[]) {
        double maior = -1;
        Random gerador = new Random();
        for (int j = 0; j < 10; j++){
            // System.out.println("saida " + saida[j]);
            maior = Double.max(maior, saida[j]);
        }
        
        //System.out.println("maior " + maior);
        for (int j = 0; j < 10; j++){
            int a = gerador.nextInt(2);
            // System.out.println("A " +a);

            if(saida[j] == maior){
                // System.out.println("maior " +j);
                return Integer.toString(a);
            }
        }
        return null;
    }

}
