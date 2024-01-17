package xadrez;

import jogo.Peca;
import jogo.Tabuleiro;

public class PecaXadrez extends Peca{

	private Cor cor;
		
	public PecaXadrez(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro);
		this.cor = cor;
	}

	//cor não é modificada
	public Cor getCor() {
		return cor;
	}
	
}
