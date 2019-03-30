//Implementação de uma rede neural do XOR
public class RedeNeural{
    //Declarando constantes utilizadas com entradas e saídas da rede
    public static final double verdadeiro = 1;
    public static final double neutro = 0.0;
    public static final double falso = -1;

    //Declaração de erroTotal a ser calculdo durante a propagação
    public double erroTotal;

    public double inpA[]; //Sinais de entrada da rede
    private double hidW[][]; //Pesos entre a camada de entrada e a camada oculta [Camada escondida][Neurônios de entrada]
    private double hidA[]; //Sinais de Saída de cada neurônio da camada escondida
    private double outW[][]; //Pesos da camada de saída [Neurônios de Saída][Camada escondida]
    public double outA[]; //Saída da Rede
    private double outD[]; //Erro da Saída
    private double hidN[];		/* Vetor de saídas para cada neurônio da Camada escondida*/
    private double outN[];		//Soma dos Produtos da	Saída da Rede
    private int nInp; //Número de Neurôniios da camada de entrada do vetor inpA
    private int nHid; //Número de Neurônios da camada escondida da matriz hidW
    private int nOut; // Número de Neurônios da camada de saida

    public double eida; //Taxa de aprendizado
    public double theta; //Limiar da função Sigmóide
    public double elast; //Elasticidade da função Sigmóide


    /**
     * Construtor da Classe
     * @param i
     * @param h
     * @param o
     * @param ei
     * @param th
     * @param el
     */
    public RedeNeural(int i, int h, int o, double ei, double th, double el){
        /**
         * i - Número de Neurônios da Camada de Entrada
         * h - Número de Neurônios da Camada Escondida
         * 0 - Número de Neurônios da Camada de Saída
         * ei - Taxa de aprendizado
         * th - Limiar
         * el - Elasticidade
         */

        //Atribuindo os valores de entrada às variáveis da red
        nInp = i;
        nHid = h;
        nOut = o;

        this.inpA = new double[i]; //Sinais de entrada da rede
        this.hidW = new double[h][i];//Pesos entre a camada de entrada e a camada escondida [Nº camada escondida]
        this.hidA = new double[h];//Sinais de Saída de cada Neurônio da Camada escondida
        this.outW = new double[o][h];//Pesos da camada de saída [Nº Saída][Nº Camada Escondida]
        this.outA = new double[o];//Saída da rede
        this.outD = new double[o];//Erro da Saída
        this.hidN = new double[h]; /* Soma dos Produtos	da camada escondida*/
        this.outN = new double[o];	/* Soma dos Produtos da	Saída da Rede*/

        //Atribuindo os valores de entrada às variáveis da rede
        eida = ei;
        theta = th;
        elast = el;

        this.inicia();//Chama o método inicializar todas as variáveis
    }

    /**
     * Método para inicializar o vetor
    */
    public void inicia(){
        int i, m; // variáveis auxiliares

		for(i=0; i < nInp; i++) //percorre todos os neurônios da camada de entrada
            inpA[i] = fRandom(-1.0, 1.0); //sinais de entrada da rede inicializados com valores randômicos entre -1 e 1
            
		for(i=0; i < nHid;i++){ //percorre todos os neurônios da camada escondida
			hidA[i] = fRandom(-1.0, 1.0);  //sinais de Saída da camada escondida inicializados com valores randômicos entre -1 e 1
			for(m=0; m < nInp; m++)  //percorre todos os pesos entre a camada de entrada e a camada escondida
				hidW[i][m] = fRandom(-1.0, 1.0); //pesos entre a camada de entrada e a camada escondida inicializados com valores randômicos entre -1 e 1
        }
        
		for(i=0; i < nOut; i++) //percorre todos os neurônios da camada de saída
			for(m=0; m < nHid; m++) ////percorre todos os pesos entre a camada escondida e a camada de entrada
				outW[i][m] = fRandom(-1.0, 1.0); //pesos entre a camada escondida e a camada de saída inicializados com valores randômicos entre -1 e 1

		erroTotal = 0.0; //inicializa o Erro Total com 0;
	}   
    
    /**
     * Método randômico para gerar valores aleatórios entre os intervalors [max][min]
     * @param min
     * @param max
     * @return
     */
    public double fRandom(double min, double max){
        return Math.random()*(max - min) + min;
    }

    /**
     * Método para Propagar os pesos e armazenar no vetor de saídas das camadas: escondida e saída
     */
    public void feedForward(){
        int i,j; // variaveis auxiliares
        double sum2; //somatório para receber os sinais ponderados pelos pesos de cada neurônio (como o net do caderno)
    
        for(i=0; i < nHid; i++){ //percorrendo todos os neurônio da camada escondida
            sum2 = 0.0;   //inicializando o somatório com 0
            for(j=0; j < nInp; j++)  //percorrendo todas as entradas para cada neurônio da camada escondida
                sum2 += hidW[i][j]* inpA[j]; //realizando o somatório
            hidN[i] = sum2;  //atribuindo o resultado do somatório no vetor de resultados para cada neurônio (como o net' do caderno)
            hidA[i] = funçãoSigmoide(sum2); // calculando a função sigmóide do somatório e armazenando no vetor de sinais de saída da camada escondida
        }
        
        for(i=0; i < nOut; i++){ //percorrendo todos os neurônios da camada de saída
            sum2 = 0.0;			 //inicializando o somatório com 0
            for(j=0; j < nHid; j++) 
                sum2 += outW[i][j]* hidA[j]; //calculando o somatório para a camada  de saída pegando os pesos entre a camada de saída e a camada escondida e multiplicando pelo resultado de cada sinal resultante da camada escondida
            outN[i] = sum2; // armazenando o valor do somatório no vetor de saídas do neurônio i.
        }
    }

    /**
     * Método para cálcular a função sigmóide
     * @param x
     * @return
     */
    private double funçãoSigmoide(double x){
        //recebe como argumento a saída x do neurônio
        //cálculo da função
        double sig = (1.0/(1.0 + Math.exp(-1.0*elast* x + theta))*2.0-1.0);
        return sig;
    }

    /**
     * Método para propagar os pesos com as entradas e retornar o valor final das saídas
     * @param vetorx
     * @throws ArrayIndexOutOfBoundsException
     */
    public void propagação(double[] vetorx) throws ArrayIndexOutOfBoundsException{
        //recebe como parâmetro o vetor de entradas X um sinal de entrada qualquer, por EX: in = {1, 1, 1} (no caso de dois neuronios na camada de entrada + o bias - 1 elemento do vetor)
        int		i,j; //variáveis auxiliares
        double	sum2; //somatório
        
        if(vetorx.length != nInp) //verificando se o vetor de entradas é compatível com o número de neurônios de entrada
            throw new ArrayIndexOutOfBoundsException("Erro: Tamanho do vetor não compatível com o número de entradas!");
        
        for(i=0; i<nInp; i++) //percorrendo os neuronios de entrada
            inpA[i] = vetorx[i]; //atribuindo o valor da entrada para o neurônio de entrada
        
        for(i=0;i < nHid ; i++){ //percorrendo os neurônios da camada escondida
            sum2 = 0.0;   //zerando o somatório
            for(j=0;j < nInp;j++) //percorrendo cada neurônio da camada de entrada
                sum2 += hidW[i][j] * inpA[j]; //calculando o somatório
            hidA[i] = funçãoSigmoide(sum2); //aplicando a função sigmóide e armazenando no vetor de resultados
            //System.out.println("Hida["+i+"]:"+hidA[i]);
        }

        for(i=0;i < nOut;i++){ //percorrendo os neurônios da camada de saída
            sum2 = 0.0; //zerando o somatório
            for(j=0;j < nHid;j++)  //percorrendo os neurônios da camada escondida
                sum2 += outW[i][j] * hidA[j]; //realizando o somaório
            outA[i] = funçãoSigmoide(sum2); //aplicando a função sigmóide e armazenando no vetor de resultados
        }	
    }

    /**
     * Método do Aprendizado Neural
     * @param in[]
     * @param out[]
     * @throws ArrayIndexOutOfBoundsException
     */
    public void aprendizado(double[] in, double out []) throws ArrayIndexOutOfBoundsException{
        //método que é chamado pela classe principal para realizar o aprendizado e que recebe os seguintes argumentos:
        //in[] um sinal de entrada qualquer, por EX: in = {1, 1, 1} (no caso de dois neuronios na camada de entrada + o bias - 1 elemento do vetor)
        //out [] target, por Ex: out = {1} (no caso de um neurônio na camada de saída)
        int i,j; // variáveis auxiliares
          if(in.length != nInp) // caso o tamanho do vetor de entradas seja diferente com p número de neuronios da camada de entradas
                throw new ArrayIndexOutOfBoundsException("Erro: Tamanho do vetor de entradas não compatível com o número de entradas!");
            if(out.length != nOut)  // caso o tamanho do vetor de entradas seja diferente com p número de neuronios da camada de entradas
                throw new ArrayIndexOutOfBoundsException("Erro: Tamanho do vetor de saídas não compatível com o número de saídas!");
        
        for(i=0; i<nInp; i++)  //percorrendo os neurônios da camada de entrada
            inpA[i] = in[i]; //o sinal de entrada do neurônio i recebe a entrada i
        for(i=0; i< nOut; i++) //percorrendo os neurônios da camada de saída
            outA[i] = out[i]; //o target do neurônio i recebe o target i
        
        this.feedForward(); //realiza a alimentação do sistema
        
        erroTotal= 0.0;	// zerando o erro	
        for(j=0; j < nOut ; j++){ //percorrendo todos os neurônios da camada de saída para calcular o erro total
              this.erroTotal	+= Math.abs(this.calculaDelta(j)); //calcula o erro total de acordo com o erro de cada neurônio de saída J, e ainda atualiza os pesos da camada de saída
          }
      
      this.atualizaPesos(); //atualiza os pesos
    }

    /**
     * Função Para Calcular o erro de cada neurônio de saída
     * @param m
     * @return 
     */
    public double calculaDelta(int m){
        //recebe como parâmetro o número do neurônio de saída
        int	i;
        //o erro de saída é calculado a partir do target OutA subtraído com a saída obtida
        outD[m] = (outA[m] - funçãoSigmoide(outN[m]))*(d1sigmoid(outN[m])+0.1);
    
        for(i=0; i < nHid; i++)  //percorrendo todos os neurônios da camada escondida
            outW[m][i] += outD[m]* hidA[i]* eida ; //calculando os novos pesos para servirem de parametro de ajuste para os pesos da camada de saída
        System.out.println("\nTAXA DE ERRO = " + outD[m]);
        return outD[m]; // retornando o erro para o neurônio de saída m
    }

    /**
     * Método para atualizar os pesos
     */
    public void atualizaPesos(){
        int i,m; //variáveis auxuliares
        double sum2; //somatório

        for(m=0;m < nHid; m++){ //percorrendo todos os neurônios da camada escondida para atulizar seus pesos
            sum2 = 0.0; //zerando o somatório
            for(i=0;i < nOut;i++){  //percorrendo os neurônios da camada de saída para calcular o somatório
                sum2 += outD[i]* outW[i][m]; //realiza o somatório ponderado com as entradas
                // System.out.println("\nERRO ...."+ outD[i]);
            };
            sum2 *= d1sigmoid(hidN[m]); // aplica a função sigmóide
            for(i=0;i < nInp;i++)  // percorre os neurônio de entrada
                hidW[m][i] += eida * sum2 * inpA[i]; //atualiza os pesos entre a camada escondida e a camada de saída
        }
    }

    /**
     * Método 
     * @param x
     */
    private double d1sigmoid(double x){
        //double sig = sigmoid(n,x);
        return 2.0 * Math.exp(-1.0 * elast * x -  theta)/(1+Math.exp(-2.0 * elast * x -  theta)); 
    }
        
}