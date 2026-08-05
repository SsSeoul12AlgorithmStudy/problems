// 고민되는 부분 : 경우의 수만 세어서 곱해주면 2번 예제와 같은 예외 케이스가 생긴다.
// 비트 이용해서 해결해야 하나?
// 백트래킹 + 비트?
// 백트래킹 결과 => 비트 => 해쉬셋에 저장
import java.util.*;

class Solution {
    // n은 사용자 수, m은 밴 당한 사용자 수
    Set<Integer> set = new HashSet<>();
    // 백트래킹에 사용할 배열
    boolean [] arr;
    int n, m;
    String[] user_id, banned_id;
    
    public int solution(String[] user_id, String[] banned_id) {
        int answer = 0;
        // 전역적으로 사용할 수 있도록
        this.user_id = user_id;
        this.banned_id = banned_id;
        n = user_id.length;
        m = banned_id.length;
        arr = new boolean[n];
        
        func(0);
        
        return set.size();
    }
    
    // 백트래킹 함수
    void func(int k) {
        if (k == m) {
            set.add(arrToBit());
               
            return;
        }
        
        for (int i = 0; i < n; i++) {
            // 이미 이전에 금지된 사용자라면 패스
            if (arr[i]) continue;
            if (canBanned(i, k)) {
                arr[i] = true;
                func(k + 1);
                arr[i] = false;
            }
        }    
    }
    
    // 현재 arr를 숫자로 바꿔주는 함수
    int arrToBit() {
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum *= 10;
            sum += arr[i] ? 1 : 0;
        }
        
        return sum;
    }
    
    // i번째 유저가 j번째 금지 사용자 명단이 될 수 있는지를 확인하는 함수
    // 문자열 -> char[]
    boolean canBanned(int i, int j) {
        if (user_id[i].length() != banned_id[j].length())   return false;
        char[] u = user_id[i].toCharArray();
        char[] b = banned_id[j].toCharArray();
        int len = u.length;
        for (int k = 0; k < len; k++) {
            if (b[k] != '*' && u[k] != b[k])    return false;
        }
        return true;
    }
}