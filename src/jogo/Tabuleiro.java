package jogo;

public class Tabuleiro {

	private int linhas;
	private int colunas;
	private Peca[][] pecas;

	public Tabuleiro(int linhas, int colunas) {
		super();
		if( linhas < 1 || colunas < 1) {
			throw new JogoException("Erro ao criar tabuleiro: É necessário que haja ao menos uma linha e uma coluna");
		}
		this.linhas = linhas;
		this.colunas = colunas;
		pecas = new Peca[linhas][colunas];
	}
	public int getLinhas() {
		return linhas;
	}
	
	//após o tabuleiro criado é inviável que a quantidade de linhas e colunas seja alterada
//	public void setLinhas(int linhas) {
//		this.linhas = linhas;
//	}
	
	public int getColunas() {
		return colunas;
	}
	
	//após o tabuleiro criado é inviável que a quantidade de linhas e colunas seja alterada

//	public void setColunas(int colunas) {
//		this.colunas = colunas;
//	}

	public Peca peca(int linha, int coluna) {
		if(!posicaoExistente(linha, coluna)) {
			throw new JogoException("Erro ao criar tabuleiro: Posição não existe");
		}
		return pecas[linha][coluna];
	}
	//sobrecarga
	public Peca peca(Posicao posicao) {
		if(!posicaoExistente(posicao)) {
			throw new JogoException("Erro ao criar tabuleiro: Posição não existe");
		}
		return pecas[posicao.getLinha()][posicao.getColuna()];
	}

	public void posicionarPeca(Peca peca, Posicao posicao) {
		if(temPecaAqui(posicao)) {
			throw new JogoException("Já existe uma peça aqui! " + posicao);
		}
		pecas[posicao.getLinha()][posicao.getColuna()] = peca;
		peca.posicao = posicao;
	}

	private boolean posicaoExistente(int linha, int coluna) {
		return linha >= 0 && linha < linhas && coluna >= 0 && coluna < colunas; 
	}

	public boolean posicaoExistente(Posicao posicao) {
		return posicaoExistente(posicao.getLinha(), posicao.getColuna());
	}

	public boolean temPecaAqui(Posicao posicao) {
		if(!posicaoExistente(posicao)) {
			throw new JogoException("Erro ao criar tabuleiro: Posição não existe");
		}
		return peca(posicao) != null;
	}

}
