import java.util.*;

public class p2240 {
	static int T, W;
	static int[] Tree;
	static int[][] D;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		
		T = sc.nextInt();
		W = sc.nextInt();
		
		Tree = new int[T];
		D = new int[T][W+1];
		
		for (int i = 0; i < T; i++) {
			Tree[i] = sc.nextInt();
		}
		
		if (Tree[0] == 1) {
			D[0][0] = 1;
			D[0][1] = 0;
		} else {
			D[0][0] = 0;
			D[0][1] = 1;
		}
		
		for (int i = 1; i < T; i++) {
			for (int j = 0; j <= W; j++) {
				if (Tree[i] == 1) {
					if (j % 2 == 0) {
						D[i][j] = Math.max(D[i-1][j], (j > 0 ? D[i-1][j-1] : 0)) + 1;
					} else {
						D[i][j] = D[i-1][j];
					}
				} else {
					if (j % 2 == 0) {
						D[i][j] = D[i-1][j];
					} else {
						D[i][j] = Math.max(D[i-1][j], (j > 0 ? D[i-1][j-1] : 0)) + 1;
					}
				}
			}
		}

		int ans = D[T-1][0];
		for (int i = 1; i <= W; i++) {
			ans = Math.max(ans, D[T-1][i]);
		}
		
		System.out.println(ans);
	}

}
