public class Rede_neural {

	// declarando as constantes utilizadas como entradas e saídas da rede
	public static final double VERDADEIRO = 1;
	public static final double NEUTRO = 0.0;
	public static final double FALSO = -1;

	// declarando o ErroTotal a ser calculado durante a propagação
	public double ErroTotal;

	// declarando as variáveis utilizadas na rede
	public double inpA[]; /* Sinais de Entrada da Rede */
	private double hidA[]; /* Sinais de Saída de cada Neurônio da Camada escondida */
	private double hidN[]; /* Vetor de saídas para cada neurônio da Camada escondida */
	private double hidW[][]; /*
														 * Pesos entre a camada de entrada e a camada escondida [Camada
														 * Escondida][Neuronios de entrada]
														 */
	public double outA[]; /* Saída da Rede */
	private double outN[]; /* Soma dos Produtos da Saída da Rede */
	private double outD[]; /* Erro da Saída */
	private double outW[][]; /* Pesos da camada de saída [Neuronios de Saída][Camada escondida] */
	private int Ninp; /* Número de Neurônios da camada de entrada */
	private int Nhid; /* Número de Neurônios da camada escondida */
	private int Nout; /* Número de Neurônios da camada de saída */
	public double eida; /* Taxa de aprendizado */
	private double theta; /* Limiar da função Sigmóide */
	private double elast; /* Elasticidade da função Sigmóide */

/*************************************************************************************************************
*  construtor da classe                                                                                      *
**************************************************************************************************************/

public Rede_neural (int i,int h,int o, double ei, double th, double el){
	//		método construtor da classe que recebe os seguintes argumentos:
	//		i  - Número de Neurônios da Camada de Entrada 
	//		h  - Número de Neurônios da Camada Escondida
	//		o  - Número de Neurônios da Camada de Saída
	//		ei - Taxa de Aprendizado
	//		th - Limiar
	//		el - Elasticidade 
	
	//atribuindo os valores de entrada às variáveis da rede
	Ninp = i;
	Nhid = h;
	Nout = o;
	
	//atribuindo os vetores com os tamanhos fornecidos pela chamada do método constutor
	this.inpA = new double[i]; /* Sinais de Entrada da Rede*/
	
	this.hidW = new double[h][i]; /* Pesos entre a camada de entrada e a camada escondida [Nº Camada Escondida][Nº Entrada]*/

	this.hidA = new double[h];  /* Sinais de Saída de cada Neurônio da Camada escondida	*/
	this.hidN = new double[h]; /* Soma dos Produtos	da camada escondida*/
	
	this.outW = new double[o][h];	/* Pesos da camada de saída [Nº Saída][Nº Camada Escondida]*/

	this.outA = new double[o];  /* Saída da Rede*/
	this.outN = new double[o];	/* Soma dos Produtos da	Saída da Rede*/
	this.outD = new double[o]; /* Erro da Saída*/
	
	//atribuindo os valores de entrada às variáveis da rede
	eida 	= ei;
	theta	= th;
	elast	= el;
	
	//iniciando todos os vetores com valores aleatórios
	this.inicia();
	
}

	/*************************************************************************************************************
	 * Método para inicializar o vetores *
	 **************************************************************************************************************/
	public void inicia() {

		int i, m; // variáveis auxiliares

		for (i = 0; i < Ninp; i++) // percorre todos os neurônios da camada de entrada
			inpA[i] = frandom(-1.0, 1.0); // sinais de entrada da rede inicializados com valores randômicos entre -1 e 1
		for (i = 0; i < Nhid; i++) { // percorre todos os neurônios da camada escondida
			hidA[i] = frandom(-1.0, 1.0); // sinais de Saída da camada escondida inicializados com valores randômicos
																		// entre -1 e 1
			for (m = 0; m < Ninp; m++) // percorre todos os pesos entre a camada de entrada e a camada escondida
				hidW[i][m] = frandom(-1.0, 1.0); // pesos entre a camada de entrada e a camada escondida inicializados com
																					// valores randômicos entre -1 e 1
		}
		for (i = 0; i < Nout; i++) // percorre todos os neurônios da camada de saída
			for (m = 0; m < Nhid; m++) //// percorre todos os pesos entre a camada escondida e a camada de entrada
				outW[i][m] = frandom(-1.0, 1.0); // pesos entre a camada escondida e a camada de saída inicializados com valores
																					// randômicos entre -1 e 1

		ErroTotal = 0.0; // inicializa o Erro Total com 0;
	}

	/*************************************************************************************************************
	 * Método para gerar valores aleatórios entre o intervalo: [min][max] *
	 **************************************************************************************************************/
	public double frandom(double min, double max) {
		return Math.random() * (max - min) + min;
	}

	/*************************************************************************************************************
	 * Método do Aprendizado Neural *
	 **************************************************************************************************************/
	public void aprendizado(double[] in, double out[]) throws ArrayIndexOutOfBoundsException {
		// método que é chamado pela classe principal para realizar o aprendizado e que
		// recebe os seguintes argumentos:
		// in[] um sinal de entrada qualquer, por EX: in = {1, 1, 1} (no caso de dois
		// neuronios na camada de entrada + o bias - 1 elemento do vetor)
		// out [] target, por Ex: out = {1} (no caso de um neurônio na camada de saída)
		int i, j; // variáveis auxiliares
		if (in.length != Ninp) // caso o tamanho do vetor de entradas seja diferente com p número de neuronios
														// da camada de entradas
			throw new ArrayIndexOutOfBoundsException(
					"Erro: Tamanho do vetor de entradas não compatível com o número de entradas!");
		if (out.length != Nout) // caso o tamanho do vetor de entradas seja diferente com p número de neuronios
														// da camada de entradas
			throw new ArrayIndexOutOfBoundsException(
					"Erro: Tamanho do vetor de saídas não compatível com o número de saídas!");

		for (i = 0; i < Ninp; i++) // percorrendo os neurônios da camada de entrada
			inpA[i] = in[i]; // o sinal de entrada do neurônio i recebe a entrada i
		for (i = 0; i < Nout; i++) // percorrendo os neurônios da camada de saída
			outA[i] = out[i]; // o target do neurônio i recebe o target i

		this.feedForward(); // realiza a alimentação do sistema

		ErroTotal = 0.0; // zerando o erro
		for (j = 0; j < Nout; j++) { // percorrendo todos os neurônios da camada de saída para calcular o erro total
			this.ErroTotal += Math.abs(this.calculaDelta(j)); // calcula o erro total de acordo com o erro de cada neurônio de
																												// saída J, e ainda atualiza os pesos da camada de saída
		}

		this.atualizaPesos(); // atualiza os pesos

	}

	/*************************************************************************************************************
	 * Função Para Propagar os pesos e armazenar no vetor de saídas das camadas:
	 * escondida e saída *
	 *************************************************************************************************************/
	public void feedForward() {
		int i, j; // variaveis auxiliares
		double sum2; // somatório para receber os sinais ponderados pelos pesos de cada neurônio
									// (como o net do caderno)

		for (i = 0; i < Nhid; i++) { // percorrendo todos os neurônio da camada escondida
			sum2 = 0.0; // inicializando o somatório com 0
			for (j = 0; j < Ninp; j++) // percorrendo todas as entradas para cada neurônio da camada escondida
				sum2 += hidW[i][j] * inpA[j]; // realizando o somatório
			hidN[i] = sum2; // atribuindo o resultado do somatório no vetor de resultados para cada neurônio
											// (como o net' do caderno)
			hidA[i] = funçãoSigmoide(sum2); // calculando a função sigmóide do somatório e armazenando no vetor de sinais de
																			// saída da camada escondida
		}

		for (i = 0; i < Nout; i++) { // percorrendo todos os neurônios da camada de saída
			sum2 = 0.0; // inicializando o somatório com 0
			for (j = 0; j < Nhid; j++)
				sum2 += outW[i][j] * hidA[j]; // calculando o somatório para a camada de saída pegando os pesos entre a camada
																			// de saída e a camada escondida e multiplicando pelo resultado de cada sinal
																			// resultante da camada escondida
			outN[i] = sum2; // armazenando o valor do somatório no vetor de saídas do neurônio i.
		}
	}

	/*************************************************************************************************************
	 * Função Para Calcular o erro de cada neurônio de saída *
	 *************************************************************************************************************/
	public double calculaDelta(int m) {
		// recebe como parâmetro o número do neurônio de saída
		int i;
		// o erro de saída é calculado a partir do target OutA subtraído com a saída
		// obtida
		outD[m] = (outA[m] - funçãoSigmoide(outN[m])) * (d1sigmoid(outN[m]) + 0.1);

		for (i = 0; i < Nhid; i++) // percorrendo todos os neurônios da camada escondida
			outW[m][i] += outD[m] * hidA[i] * eida; // calculando os novos pesos para servirem de parametro de ajuste para os
																							// pesos da camada de saída
		//System.out.print("ERRO \n" + outD[m]);
		return outD[m]; // retornando o erro para o neurônio de saída m
	}

	/*************************************************************************************************************
	 * Função Sigmóide *
	 **************************************************************************************************************/
	private double funçãoSigmoide(double x) {
		// recebe como argumento a saída x do neurônio

		// cálculo da função
		double sig = (1.0 / (1.0 + Math.exp(-1.0 * elast * x + theta)) * 2.0 - 1.0);

		return sig;
	}

	/*************************************************************************************************************
	 * Função D1 Sigmóide *
	 **************************************************************************************************************/
	private double d1sigmoid(double x) {
		// double sig = sigmoid(n,x);

		return 2.0 * Math.exp(-1.0 * elast * x - theta) / (1 + Math.exp(-2.0 * elast * x - theta));
	}

	/*************************************************************************************************************
	 * Função para atualizar os pesos *
	 **************************************************************************************************************/
	public void atualizaPesos() {
		int i, m; // variáveis auxuliares
		double sum2; // somatório

		for (m = 0; m < Nhid; m++) { // percorrendo todos os neurônios da camada escondida para atulizar seus pesos
			sum2 = 0.0; // zerando o somatório
			for (i = 0; i < Nout; i++) { // percorrendo os neurônios da camada de saída para calcular o somatório
				sum2 += outD[i] * outW[i][m]; // realiza o somatório ponderado com as entradas
			}
			;
			sum2 *= d1sigmoid(hidN[m]); // aplica a função sigmóide
			for (i = 0; i < Ninp; i++) // percorre os neurônio de entrada
				hidW[m][i] += eida * sum2 * inpA[i]; // atualiza os pesos entre a camada escondida e a camada de saída
		}
	}

	/*************************************************************************************************************
	 * Função para propagar os pesos com as entradas e retornar o valor final das
	 * saídas *
	 **************************************************************************************************************/
	public void propagação(double[] vetorx) throws ArrayIndexOutOfBoundsException {
		// recebe como parâmetro o vetor de entradas X um sinal de entrada qualquer, por
		// EX: in = {1, 1, 1} (no caso de dois neuronios na camada de entrada + o bias -
		// 1 elemento do vetor)
		int i, j; // variáveis auxiliares
		double sum2; // somatório

		if (vetorx.length != Ninp) // verificando se o vetor de entradas é compatível com o número de neurônios de
																// entrada
			throw new ArrayIndexOutOfBoundsException("Erro: Tamanho do vetor não compatível com o número de entradas!");

		for (i = 0; i < Ninp; i++) // percorrendo os neuronios de entrada
			inpA[i] = vetorx[i]; // atribuindo o valor da entrada para o neurônio de entrada

		for (i = 0; i < Nhid; i++) { // percorrendo os neurônios da camada escondida
			sum2 = 0.0; // zerando o somatório
			for (j = 0; j < Ninp; j++) // percorrendo cada neurônio da camada de entrada
				sum2 += hidW[i][j] * inpA[j]; // calculando o somatório
			hidA[i] = funçãoSigmoide(sum2); // aplicando a função sigmóide e armazenando no vetor de resultados
			// System.out.println("Hida["+i+"]:"+hidA[i]);
		}
		for (i = 0; i < Nout; i++) { // percorrendo os neurônios da camada de saída
			sum2 = 0.0; // zerando o somatório
			for (j = 0; j < Nhid; j++) // percorrendo os neurônios da camada escondida
				sum2 += outW[i][j] * hidA[j]; // realizando o somaório
			outA[i] = funçãoSigmoide(sum2); // aplicando a função sigmóide e armazenando no vetor de resultados
		}
	}

}