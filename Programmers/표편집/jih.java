import java.util.*;

class Solution {
     /*
    26-9-5 19:58 ~ 22:30, 
    26-9-6 23:40 ~ 24:20
    
    n행의 테이블에서 명령어들을 수행했을 때, 기존 행에서 삭제되었는지 여부를 문자열로 반환하자
    
    ===
    
    제약조건
    - 테이블 행의 개수 = n <= 100만
    - 명령어의 개수 = M <= 20만
    - cmd내 X의 합 <= 100만
    - 행의 이름은 중복이 없다고 가정
    
    ===
    
    완탐으로 푸는 방법
    - 시뮬레이션처럼 풀면 된다
    - 행은 리스트로, 선택된 행은 포인터로 관리하면 된다.
    - 최근에 삭제된 이름은 스택에 저장해두면 된다.
    - 시간복잡도 : M * 1
    
    문제 : 원래 표에서 나보다 위에 있던 것들이 지워졌을 수도 있다
    - 표의 정보가 바뀌므로 복원해야할 위치가 매번 다를 수 있다
    
    - 리스트에서 노드를 지우고 삽입할 필요는 없다
        - 그냥 valid한지만 관리하면 된다.
        
    - 그러면 포인터를 이동할 때, +X로 이동한 위치가 어디인지는 어떻게 알까?
        - 내가 이동하는 구간 사이에 없어진 행이 있으면 그만큼 더 이동해야한다.
        - L, R 구간 사이에 삭제된 행의 수를 구하는 방법...
            세그먼트 트리로 구간 내에 삭제된 노드의 개수를 저장할 수 있을 것 같다.
        - 이렇게 하면 시간복잡도는?
            - M * log n <= 20 * 20 -> 시간초과는 안날 것 같다.
        - 그러나 시간초과가 발생하더라
            - 이유는 정확한 x + removed를 구하기 위해서 query를 반복하는 것이 최악의 경우 
            n의 시간의 걸리는 것으로 추정된다
            
        - 지피티의 힌트 : 
            - 사실 알고 싶은 내용은 삭제된 행의 수가 아니라 이전 행과 다음 행이 무엇인가이다
                - 그 방법은 배열을 이용한 이중 연결리스트!
            - 전체 cmd에서 X의 합이 100만 이하라는 조건이 있으니
            U,D 커맨드는 시간복잡도에 큰 영향을 주지 않는다!
            - 행이 삭제되면 스택으로 이동하고, 행이 복원되면 스택에서 꺼낸다
                - 스택에는 이전의 연결 정보가 복원되어야하니 연결 관계도 저장해야 한다
    
    */
    public String solution(int n, int k, String[] cmd) {
        boolean[] removed = new boolean[n];
        int[] prev = new int[n];
        int[] next = new int[n];
        Stack<Integer> st = new Stack<>();
        int cur = k;
        
        prev[0] = -1;
        for(int i = 1; i < n; i++){
            prev[i] = i - 1;
        }
        
        next[n - 1] = -1;
        for(int i = 0; i < n - 1; i++){
            next[i] = i + 1;
        }
        
        // cmd에 대해서 작업을 수행
        for(String c : cmd){
            char oper = c.charAt(0);
            
            if(oper == 'U'){
                int X = Integer.parseInt(c.substring(2));
                
                for(int i = 0; i < X; i++){
                    cur = prev[cur];
                }
            }
            if(oper == 'D'){
                int X = Integer.parseInt(c.substring(2));
                
                for(int i = 0; i < X; i++){
                    cur = next[cur];
                }
            }
            if(oper == 'C'){
                if(prev[cur] != -1) next[prev[cur]] = next[cur];
                if(next[cur] != -1) prev[next[cur]] = prev[cur];
                st.push(cur);
                
                removed[cur] = true;
                
                if(next[cur] != -1){
                    cur = next[cur];
                }
                else{
                    cur = prev[cur];
                }
                
            }
            if(oper == 'Z'){
                int restored = st.pop();
                
                if(prev[restored] != -1) next[prev[restored]] = restored;
                if(next[restored] != -1) prev[next[restored]] = restored;
                
                removed[restored] = false;
            }
        }
        
        // 결과 반환
        
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < n; i++){
            if(removed[i]){
                sb.append("X");
            }
            else{
                sb.append("O");
            }
        }
        return sb.toString();
    }
}