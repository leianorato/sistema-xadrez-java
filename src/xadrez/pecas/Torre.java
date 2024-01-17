package xadrez.pecas;

import jogo.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Torre extends PecaXadrez{

	public Torre(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
		// TODO Auto-generated constructor stub
	}
	
	//letra pra representar a Torre no tabuleiro impresso no terminal
	@Override
	public String toString() {
		return "T";
	}

	
}
