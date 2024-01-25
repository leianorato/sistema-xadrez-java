package xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jogo.Peca;
import jogo.Posicao;
import jogo.Tabuleiro;
import xadrez.pecas.Bispo;
import xadrez.pecas.Cavalo;
import xadrez.pecas.Peao;
import xadrez.pecas.Rainha;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {

	private Tabuleiro tabuleiro;
	private int vez;
	private Cor jogador;
	private boolean xeque;
	private boolean xequeMate;

	//Quando um peão avança até a oitava fileira, ele pode ser promovido a uma peça de escolha do jogador (exceto um rei). 
	//A escolha geralmente é uma rainha, mas pode ser uma torre, bispo ou cavalo, dependendo da estratégia do jogador. 
	//Portanto, "promovido" indica que um peão atingiu a última fileira e foi transformado em outra peça mais poderosa.
	private PecaXadrez promovido;

	//expressão utilizada no xadrez para se referir à captura especial de um peão que 
	//se moveu duas casas à frente na jogada anterior
	private PecaXadrez enPassantVulnerable;

	private List<Peca> pecasNoTabuleiro = new ArrayList<>();
	private List<Peca> pecasCapturadas = new ArrayList<>();


	public PartidaXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		vez = 1;
		jogador = Cor.BRANCO;
		iniciarPartida();
	}

	public int getVez() {
		return vez;
	}

	public Cor getJogador() {
		return jogador;
	}

	public boolean getXeque() {
		return xeque;
	}

	public boolean getXequeMate() {
		return xequeMate;
	}

	public PecaXadrez getEnPassantVulnerable() {
		return enPassantVulnerable;
	}

	public PecaXadrez getPromovido() {
		return promovido;
	}

	public boolean[][] movPossiveis(PosicaoXadrez origem) {
		Posicao posicao = origem.converterPosicaoPara();
		validarPosicaoOrigem(posicao);
		return tabuleiro.peca(posicao).movPossiveis();
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

	private void proxVez() {
		vez++;
		jogador = (jogador == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
	}

	public PecaXadrez executarJogadaXadrez(PosicaoXadrez origem, PosicaoXadrez destino) {
		Posicao origemAux = origem.converterPosicaoPara();
		Posicao destinoAux = destino.converterPosicaoPara();
		validarPosicaoOrigem(origemAux);
		validarPosicaoDestino(origemAux, destinoAux);
		Peca pecaCapturada = realizarMovimento(origemAux, destinoAux);


		if(testXeque(jogador)) {
			desfazerMovimento(origemAux, destinoAux, pecaCapturada);
			throw new XadrezException("Você não pode se colocar em cheque!");
		}

		PecaXadrez pecaMovida = (PecaXadrez) tabuleiro.peca(destinoAux);

		promovido = null;
		if (pecaMovida instanceof Peao) {
			if ((pecaMovida.getCor() == Cor.BRANCO && destinoAux.getLinha() == 0) || (pecaMovida.getCor() == Cor.PRETO && destinoAux.getLinha() == 7)) {
				promovido = (PecaXadrez) tabuleiro.peca(destinoAux);
				promovido = substituirPecaPromovida("R");
			}
		}

		xeque = (testXeque(oponente(jogador))) ? true : false;

		if (testXequeMate(oponente(jogador))) {
			xequeMate = true;
		}
		else {
			proxVez();
		}

		// en passant
		if (pecaMovida instanceof Peao && (destinoAux.getLinha() == origemAux.getLinha() - 2 || destinoAux.getLinha() == origemAux.getLinha() + 2)) {
			enPassantVulnerable = pecaMovida;
		}
		else {
			enPassantVulnerable = null;
		}

		return (PecaXadrez) pecaCapturada;
	}

	private Peca realizarMovimento(Posicao origem, Posicao destino) {
		PecaXadrez p = (PecaXadrez)tabuleiro.removerPeca(origem);
		p.aumentarContMovimentos();
		Peca pecaCapturada = tabuleiro.removerPeca(destino);

		tabuleiro.posicionarPeca(p, destino);

		if(pecaCapturada != null) {
			pecasNoTabuleiro.remove(pecaCapturada);
			pecasCapturadas.add(pecaCapturada);
		}

		//movimento especial de roque
		if(p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaXadrez torre = (PecaXadrez)tabuleiro.removerPeca(origemT);
			tabuleiro.posicionarPeca(torre, destinoT);
			torre.aumentarContMovimentos();
		}

		if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaXadrez torre = (PecaXadrez)tabuleiro.removerPeca(origemT);
			tabuleiro.posicionarPeca(torre, destinoT);
			torre.aumentarContMovimentos();
		}		


		// movimento en passant
		if (p instanceof Peao) {
			if (origem.getColuna() != destino.getColuna() && pecaCapturada == null) {
				Posicao peaoPos;
				if (p.getCor() == Cor.BRANCO) {
					peaoPos = new Posicao(destino.getLinha() + 1, destino.getLinha());
				}
				else {
					peaoPos = new Posicao(destino.getLinha() - 1, destino.getLinha());
				}
				pecaCapturada = tabuleiro.removerPeca(peaoPos);
				pecasCapturadas.add(pecaCapturada);
				pecasNoTabuleiro.remove(pecaCapturada);
			}
		}
		return pecaCapturada;
	}

	private void desfazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada) {
		PecaXadrez p = (PecaXadrez)tabuleiro.removerPeca(destino);
		p.diminuirContMovimentos();
		tabuleiro.posicionarPeca(p, origem);

		if (pecaCapturada != null) {
			tabuleiro.posicionarPeca(pecaCapturada, destino);
			pecasCapturadas.remove(pecaCapturada);
			pecasNoTabuleiro.add(pecaCapturada);
		}

		// movimento especial roque
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaXadrez torre = (PecaXadrez)tabuleiro.removerPeca(destinoT);
			tabuleiro.posicionarPeca(torre, origemT);
			torre.diminuirContMovimentos();
		}


		if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaXadrez torre = (PecaXadrez)tabuleiro.removerPeca(destinoT);
			tabuleiro.posicionarPeca(torre, origemT);
			torre.diminuirContMovimentos();
		}

		// movimento especial en passant
		if (p instanceof Peao) {
			if (origem.getColuna() != destino.getColuna() && pecaCapturada == enPassantVulnerable) {
				PecaXadrez peao = (PecaXadrez)tabuleiro.removerPeca(destino);
				Posicao peaoPos;
				if (p.getCor() == Cor.BRANCO) {
					peaoPos = new Posicao(3, destino.getColuna());
				}
				else {
					peaoPos = new Posicao(4, destino.getColuna());
				}
				tabuleiro.posicionarPeca(peao, peaoPos);
			}
		}
	}

	private void validarPosicaoOrigem(Posicao posicao) {
		if(!tabuleiro.temPecaAqui(posicao)) {
			throw new XadrezException("Não tem peça nessa origem");
		}
		if(jogador != ((PecaXadrez)tabuleiro.peca(posicao)).getCor()) {
			throw new XadrezException("A peça escolhida não é sua!");

		}
		if(!tabuleiro.peca(posicao).haAlgumMovPossivel()) {
			throw new XadrezException("Não movimentos possíveis para a peça escolhida!");
		}
	}

	private void validarPosicaoDestino(Posicao origem, Posicao destino) {
		if(!tabuleiro.peca(origem).movPossivel(destino)) {
			throw new XadrezException("A peça escolhida não pode mover para essa posição!");
		}
	}

	private PecaXadrez rei(Cor cor) {
		List<Peca> lista = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == cor).collect(Collectors.toList());
		for (Peca p : lista) {
			if (p instanceof Rei) {
				return (PecaXadrez)p;
			}
		}
		throw new IllegalStateException("Não tem rei " + cor + " no tabuleiro");
	}

	private Cor oponente(Cor cor) {
		return (cor == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
	}

	private boolean testXeque(Cor cor) {
		Posicao reiPosicao = rei(cor).getPosicaoXadrez().converterPosicaoPara();
		List<Peca> opponentPieces = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == oponente(cor)).collect(Collectors.toList());
		for (Peca p : opponentPieces) {
			boolean[][] mat = p.movPossiveis();
			if (mat[reiPosicao.getLinha()][reiPosicao.getColuna()]) {
				return true;
			}
		}
		return false;
	}

	private boolean testXequeMate(Cor cor) {
		if (!testXeque(cor)) {
			return false;
		}
		List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez)x).getCor() == cor).collect(Collectors.toList());
		for (Peca p : list) {
			boolean[][] mat = p.movPossiveis();
			for (int i=0; i<tabuleiro.getLinhas(); i++) {
				for (int j=0; j<tabuleiro.getColunas(); j++) {
					if (mat[i][j]) {
						Posicao origem = ((PecaXadrez)p).getPosicaoXadrez().converterPosicaoPara();
						Posicao destino = new Posicao(i, j);
						Peca pecaCapturada = realizarMovimento(origem, destino);
						boolean testXeque = testXeque(cor);
						desfazerMovimento(origem, destino, pecaCapturada);
						if (!testXeque) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}	

	public PecaXadrez substituirPecaPromovida(String tipo) {
		if(promovido == null) {
			throw new IllegalStateException("Não tem peça a ser promovida!");
		}
		if(!tipo.equals("B") && !tipo.equals("C") && !tipo.equals("K") && !tipo.equals("R")) {
			return promovido;
		}

		Posicao posicao = promovido.getPosicaoXadrez().converterPosicaoPara();
		Peca peca = tabuleiro.removerPeca(posicao);
		pecasNoTabuleiro.remove(peca);

		PecaXadrez novaPeca = novaPeca(tipo, promovido.getCor());
		tabuleiro.posicionarPeca(novaPeca, posicao);
		pecasNoTabuleiro.add(novaPeca);

		return novaPeca;
	}



	private PecaXadrez novaPeca(String tipo, Cor cor) {
		if (tipo.equals("B")) return new Bispo(tabuleiro, cor);
		if (tipo.equals("C")) return new Cavalo(tabuleiro, cor);
		if (tipo.equals("R")) return new Rainha(tabuleiro, cor);
		return new Torre(tabuleiro, cor);
	}

	private void posicionarNovaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.posicionarPeca(peca, new PosicaoXadrez(coluna, linha).converterPosicaoPara());
		pecasNoTabuleiro.add(peca);
	}

	private void iniciarPartida() {
        posicionarNovaPeca('a', 8, new Torre(tabuleiro, Cor.PRETO));
        posicionarNovaPeca('b', 8, new Cavalo(tabuleiro, Cor.PRETO));
        posicionarNovaPeca('c', 8, new Bispo(tabuleiro, Cor.PRETO));
        posicionarNovaPeca('d', 8, new Rainha(tabuleiro, Cor.PRETO));
        posicionarNovaPeca('e', 8, new Rei(tabuleiro, Cor.PRETO, this));
        posicionarNovaPeca('f', 8, new Bispo(tabuleiro, Cor.PRETO));
        posicionarNovaPeca('g', 8, new Cavalo(tabuleiro, Cor.PRETO));
        posicionarNovaPeca('h', 8, new Torre(tabuleiro, Cor.PRETO));
        posicionarNovaPeca('a', 7, new Peao(tabuleiro, Cor.PRETO, this));
        posicionarNovaPeca('b', 7, new Peao(tabuleiro, Cor.PRETO, this));
        posicionarNovaPeca('c', 7, new Peao(tabuleiro, Cor.PRETO, this));
        posicionarNovaPeca('d', 7, new Peao(tabuleiro, Cor.PRETO, this));
        posicionarNovaPeca('e', 7, new Peao(tabuleiro, Cor.PRETO, this));
        posicionarNovaPeca('f', 7, new Peao(tabuleiro, Cor.PRETO, this));
        posicionarNovaPeca('g', 7, new Peao(tabuleiro, Cor.PRETO, this));
        posicionarNovaPeca('h', 7, new Peao(tabuleiro, Cor.PRETO, this));

		posicionarNovaPeca('a', 1, new Torre(tabuleiro, Cor.BRANCO));
        posicionarNovaPeca('b', 1, new Cavalo(tabuleiro, Cor.BRANCO));
        posicionarNovaPeca('c', 1, new Bispo(tabuleiro, Cor.BRANCO));
        posicionarNovaPeca('d', 1, new Rainha(tabuleiro, Cor.BRANCO));
        posicionarNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCO, this));
        posicionarNovaPeca('f', 1, new Bispo(tabuleiro, Cor.BRANCO));
        posicionarNovaPeca('g', 1, new Cavalo(tabuleiro, Cor.BRANCO));
        posicionarNovaPeca('h', 1, new Torre(tabuleiro, Cor.BRANCO));
        posicionarNovaPeca('a', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        posicionarNovaPeca('b', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        posicionarNovaPeca('c', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        posicionarNovaPeca('d', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        posicionarNovaPeca('e', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        posicionarNovaPeca('f', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        posicionarNovaPeca('g', 2, new Peao(tabuleiro, Cor.BRANCO, this));
        posicionarNovaPeca('h', 2, new Peao(tabuleiro, Cor.BRANCO, this));


	}

}
