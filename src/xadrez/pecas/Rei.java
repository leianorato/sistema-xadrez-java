package xadrez.pecas;

import jogo.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Rei extends PecaXadrez{

	public Rei(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
		// TODO Auto-generated constructor stub
	}
	
	//letra pra representar o Rei no tabuleiro impresso no terminal
	@Override
	public String toString() {
		return "K";
	}

}
