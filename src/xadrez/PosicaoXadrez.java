package xadrez;

import jogo.Posicao;

public class PosicaoXadrez {

	//no tabuleiro as colunas são representadas de 'a' a 'h' e as linhas de 1 a 8
	private char coluna;
	private int linha;
	public PosicaoXadrez(char coluna, int linha) {
		if(coluna < 'a' || coluna > 'h' || linha < 1 || linha > 8) {
			throw new XadrezException("Erro: Valor inválido pra posição!");
		}
		this.coluna = coluna;
		this.linha = linha;
	}

	//converter a matriz que virá em formato de Tabuleiro para matriz
	protected Posicao converterPosicaoPara() {
		return new Posicao(8 - linha, coluna - 'a');
	}

	//converter a matriz normal para o formato de Xadrez
	protected static PosicaoXadrez converterPosicaoDe(Posicao posicao) {
		return new PosicaoXadrez((char)('a' - posicao.getColuna()), 8 - posicao.getLinha());
	}

	@Override
	public String toString() {
		//A1
		//E2
		return ""+coluna + linha;
	}

	public char getColuna() {
		return coluna;
	}


	public int getLinha() {
		return linha;
	}





}
