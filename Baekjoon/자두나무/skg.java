import java.util.*;

/*

## 풀이 준비

홀수 번 움직이면 → 2번 나무

짝수 번 움직이면 → 1번 나무

1. **테이블 정의하기**
    
    D[i][j] : i번째 자두까지 j번 움직여서 받을 수 있는 최대 개수
    
2. **점화식 찾기**
    
    다음 자두가 
    
    - 1번 나무에서 떨어진다면: 받기 위해서는 짝수 번 움직이도록 해야 함.
        
        받는 경우에 j는 짝수가 되어야 함 (짝→짝 || 홀→짝)
        
        `D[i][j] = max(D[i-1][j], D[i-1][j-1]) + 1;`
        
        받지 않는 경우에 j는 홀수 (홀→홀)
        
        `D[i][j] = D[i-1][j];`
        
    - 2번 나무에서 떨어진다면
        
        받는 경우에 j는 홀수 (홀→홀 || 짝→홀)
        
        `D[i][j] = max(D[i-1][j], D[i-1][j-1]) + 1;`
        
        받지 않는 경우에 j는 짝수 (짝→짝)
        
        `D[i][j] = D[i-1][j];`
        
3. **초기값 정하기**
    
    첫 번째 시행에 대해서
    
    - 처음 1번 나무에서 떨어진다면
        
        `D[0][0] = 1;`
        
        `D[0][1] = 0;`
        
    - 처음 2번 나무에서 떨어진다면 반대.

## 총평

`D[i][j] = max(D[i-1][j], D[i-1][j-1]) + 1;`

이 부분에서 j가 0인 경우에 index 에러가 나서 j>0 기준 삼항 연산자로 처리해주었다.

### 실수

- 자두를 먹지 못할 때 기존 자리에 있는 경우만 생각해주었는데, 자리를 변경하는 경우도 생각해주어야 한다고 한다..
    
    ⇒ 즉, 자두가 가만히 있어도 나한테 오는데 자리를 변경하는 경우도 생각해야 한다..?
    

그냥 돌려도 testcase.ac에서는 반례가 안 나왔다.

*/
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
