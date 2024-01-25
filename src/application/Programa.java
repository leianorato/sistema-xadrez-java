package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;
import xadrez.PosicaoXadrez;
import xadrez.XadrezException;

public class Programa {

	public static void main(String[] args) {

		Scanner sc = new Scanner (System.in);
		PartidaXadrez partida = new PartidaXadrez();
		List<PecaXadrez> capturada = new ArrayList<>();

		while(!partida.getXequeMate()) {

			try {
				UI.limparTela();
				UI.printPartida(partida, capturada);
				System.out.println();
				System.out.println("Posição de origem: ");
				PosicaoXadrez origem = UI.lerPosicaoXadrez(sc);

				boolean [][] movPossiveis = partida.movPossiveis(origem);
				UI.limparTela();
				UI.printTabuleiro(partida.getPecas(), movPossiveis);
				System.out.println();
				System.out.println("Posição de destino: ");
				PosicaoXadrez destino = UI.lerPosicaoXadrez(sc);

				PecaXadrez pecaCapturada = partida.executarJogadaXadrez(origem, destino);

				if(pecaCapturada != null) {
					capturada.add(pecaCapturada);
				}

				if(partida.getPromovido() != null) {
					System.out.println("Digite peça a ser promovida (R/K/C/B): ");
					String tipo = sc.nextLine().toUpperCase();
					while (!tipo.equals("R") && !tipo.equals("K") && !tipo.equals("C") & !tipo.equals("B")) {
						System.out.print("Inválido! Digite peça a ser promovida (R/K/C/B): ");
						tipo = sc.nextLine().toUpperCase();
					}
					partida.substituirPecaPromovida(tipo);
				}

			} catch (XadrezException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}

		}
		UI.limparTela();
		UI.printPartida(partida, capturada);

	}
}
