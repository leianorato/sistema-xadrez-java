package jogo;

public abstract class Peca {

	protected Posicao posicao;
	private Tabuleiro tabuleiro;

	public Peca(Tabuleiro tabuleiro) {
		posicao = null;
		this.tabuleiro = tabuleiro;
	}

	protected Tabuleiro getTabuleiro() {
		return tabuleiro;
	}

	public abstract boolean[][] movPossiveis();

	public boolean movPossivel(Posicao posicao) {
		return movPossiveis()[posicao.getLinha()][posicao.getColuna()];
	}

	public boolean haAlgumMovPossivel() {
		boolean[][] mat = movPossiveis();
		for (int i=0; i<mat.length; i++) {
			for (int j=0; j<mat.length; j++) {
				if (mat[i][j]) {
					return true;
				}
			}
		}
		return false;
	}
}
