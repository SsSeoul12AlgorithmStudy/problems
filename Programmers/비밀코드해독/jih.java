class Solution {
    /*
    26/10/24 14:31 ~ 14:51
    
    해독 시도에 대한 정보가 주어질 때 가능한 비밀코드의 개수를 구하자
    
    ---
    비밀 코드는 오름차순이고 5개의 정수
    비밀코드는 반드시 존재한다
    
    m = 암호해독 시도 <= 10
    n <= 30
    ---
    
    완탐이 가능할까?
    - n개의 수 중에서 5개를 뽑아서 후보를 만들고 주어진 해독 시도에 부합하는지 판정한다?
    - 시간복잡도는 n^5 * m
    - 풀 수 있을 것 같다.
    
    */
    static int answer;
    static int[] code;
    static int[][] q;
    static int[] ans;
    static int n;
    
    public int solution(int n, int[][] q, int[] ans) {
        answer = 0;
        
        code = new int[5];
        
        this.n = n;
        this.q = q;
        this.ans = ans;
        
        findCode(0, 1);
        
        return answer;
    }
    
    private void findCode(int depth, int start){
        if(depth >= 5){
            if(possibleCode()){
                answer++;
            }
            return;
        }
        
        for(int i = start; i <= n; i++){
            code[depth] = i;
            findCode(depth + 1, i + 1);
            code[depth] = 0;
        }
    }
    
    private boolean possibleCode(){
        // m개의 시도에 부합하는지 확인
        for(int i = 0; i < q.length; i++){
            int matched = 0;
            for(int j = 0; j < 5; j++){
                for(int k = 0; k < 5; k++){
                    if(code[j] == q[i][k]){
                        matched++;
                    }
                }
            }
            if(matched != ans[i]){
                return false;
            }
        }
        return true;
    }
}