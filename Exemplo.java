public class ExemploXOR{
    public static void main(String[] args) {
		
		// para entender a composição da rede olhar na classe BPN2 o constutor da mesma
		// a rede será composta por:
		// 1 Neurônios na Camada de Entrada 
		// 4 Neurônios na Camada Escondida
		// 1 Neurônio na Camada de Saída
		// Taxa de Aprendizado = 0.02
		// Limiar para a função Sigmóide = 1;
		// Elasticidade da função Sigmóide = 2; 	
		
		//criando um rede neural com os argumentos citados acima:
        RedeNeural n = new RedeNeural(3, 4, 1, 0.02, 1, 2);

        //atribuindo os possíveis valores para as entradas
	    double falso = RedeNeural.falso; //olhar os valores na classe BPN2  falso =-1
        double verdadeiro = RedeNeural.verdadeiro;  //olhar os valores na classe BPN2  verdadeiro =1
        
        int i; //variável auxiliar
	
		//atribuindo os valores para o vetor de entradas PS: 0 primeiro valor de cada posição é o BIAS
		double entradas[][] = {
            {
                1,
                falso,
                falso,
            },
			{
                1,
                falso,
                verdadeiro,
            },
			{
                1,
                verdadeiro,
                falso,
            },
			{
                1,
                verdadeiro,
                verdadeiro,
            }
		};
		// atribuindo os valores para os targets de acordo com as entradas
		double targets[][] = {
            {
                verdadeiro
            },
            {
                verdadeiro
            },
            {
                verdadeiro
            },
            {
                verdadeiro
            }
        };

        System.out.println("\nAntes do Aprendizado ....\n");
		for(i=0;i<entradas.length;i++){ //calculando a saída da propagação para cada entrada
			n.propagação(entradas[i]); //propaga o sinal de entrada i pela rede
			System.out.print(bool(entradas[i][1])+" "+bool(entradas[i][2])+" -> XOR -> "+bool(n.outA[0])+"\n"); //dá o resultado
        }
        
        int iteração =2; //inicializando a itereação com 2
        n.erroTotal = 1; //inicializando o erro total com 1
        while(iteração <10000 && n.erroTotal > 0.002){ // o número de épocas máximo é 10000 e o valor do erro tolerável é 0.002
            iteração = iteração + 1; //incrementando a iteração
            n.aprendizado(entradas[iteração%entradas.length],targets[iteração%entradas.length]); //realizando o aprendizado para a entrada com seu respectivo target
        }

        System.out.println("\nDepois do Aprendizado ....\n");
		for(i=0;i<entradas.length;i++){ //calculando a saída da propagação para cada entrada
			n.propagação(entradas[i]); //propaga o sinal de entrada i pela rede
			System.out.print(bool(entradas[i][1])+" "+bool(entradas[i][2])+" -> XOR -> "+bool(n.outA[0])+"\n"); //dá o resultado
        }
    }

    public static String bool(double x){
		return (x>0.5)? "Verdadeiro ":(x< -0.5)? "Falso      ": "Indefinido";
    }
    
    
}