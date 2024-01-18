package xadrez;

import jogo.Peca;
import jogo.Posicao;
import jogo.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {

	private Tabuleiro tabuleiro;
	
	public PartidaXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		iniciarPartida();
	}
	
	public PecaXadrez[][] getPecas(){
		PecaXadrez[][] matriz = new PecaXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for(int i = 0; i < tabuleiro.getLinhas(); i++) {
			for(int j = 0; j < tabuleiro.getColunas(); j++) {
				matriz[i][j] = (PecaXadrez) tabuleiro.peca(i, j);
			}
		}
		return matriz;
	}
	
	public PecaXadrez executarJogadaXadrez(PosicaoXadrez origem, PosicaoXadrez destino) {
		Posicao origemAux = origem.converterPosicaoPara();
		Posicao destinoAux = destino.converterPosicaoPara();
		validarPosicaoOrigem(origemAux);
		Peca pecaCapturada = realizarMovimento(origemAux, destinoAux);
		return (PecaXadrez) pecaCapturada;
	}
	
	private Peca realizarMovimento(Posicao origem, Posicao destino) {
		Peca p = tabuleiro.removerPeca(origem);
		Peca pecaCapturada = tabuleiro.removerPeca(destino);
		
		tabuleiro.posicionarPeca(p, destino);
		return pecaCapturada;
	}
	
	private void validarPosicaoOrigem(Posicao posicao) {
		if(!tabuleiro.temPecaAqui(posicao)) {
			throw new XadrezException("Não tem peça nessa origem");
		}
	}
	
	private void posicionarNovaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.posicionarPeca(peca, new PosicaoXadrez(coluna, linha).converterPosicaoPara());
	}
	
	private void iniciarPartida() {
	
		posicionarNovaPeca('c', 1, new Torre(tabuleiro, Cor.BRANCO));
        posicionarNovaPeca('c', 2, new Torre(tabuleiro, Cor.BRANCO));
        posicionarNovaPeca('d', 2, new Torre(tabuleiro, Cor.BRANCO));
        posicionarNovaPeca('e', 2, new Torre(tabuleiro, Cor.BRANCO));
        posicionarNovaPeca('e', 1, new Torre(tabuleiro, Cor.BRANCO));
        posicionarNovaPeca('d', 1, new Rei(tabuleiro, Cor.BRANCO));

        posicionarNovaPeca('c', 7, new Torre(tabuleiro, Cor.PRETO));
        posicionarNovaPeca('c', 8, new Torre(tabuleiro, Cor.PRETO));
        posicionarNovaPeca('d', 7, new Torre(tabuleiro, Cor.PRETO));
        posicionarNovaPeca('e', 7, new Torre(tabuleiro, Cor.PRETO));
        posicionarNovaPeca('e', 8, new Torre(tabuleiro, Cor.PRETO));
        posicionarNovaPeca('d', 8, new Rei(tabuleiro, Cor.PRETO));
	}
	
}
