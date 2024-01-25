package xadrez;

import jogo.Peca;
import jogo.Posicao;
import jogo.Tabuleiro;

public abstract class PecaXadrez extends Peca{

	private Cor cor;
	private int contMovimentos;
		
	public PecaXadrez(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro);
		this.cor = cor;
	}

	//cor não é modificada
	public Cor getCor() {
		return cor;
	}
	
	public int getContMovimentos() {
		return contMovimentos;
	}
	
	protected void aumentarContMovimentos() {
		contMovimentos++;
	}

	protected void diminuirContMovimentos() {
		contMovimentos--;
	}

	public PosicaoXadrez getPosicaoXadrez() {
		return PosicaoXadrez.converterPosicaoDe(posicao);
	}
	
	protected boolean existePecaOponente(Posicao posicao) {
		PecaXadrez p = (PecaXadrez)getTabuleiro().peca(posicao);
		return p != null && p.getCor() != cor;
	}
}
