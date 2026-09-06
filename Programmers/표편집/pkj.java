import java.util.*;

class Solution {
    static int[] prev; // 내 앞의 행의 번호
    static int[] next; // 내 뒤의 행의 번호
    static Stack<Integer> trash;
    
    public String solution(int n, int k, String[] cmd) {
        prev = new int[n];
        next = new int[n];
        trash = new Stack<>();
        
        for (int i = 0; i < n; i++) {
            prev[i] = i - 1;
            next[i] = i + 1;
        }
        next[n - 1] = -1;
        
        command(k, cmd);
        
        boolean[] deleted = new boolean[n];
        while (!trash.isEmpty()) {
            deleted[trash.pop()] = true;
        }
        
        StringBuilder answer = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            if (deleted[i]) answer.append('X');
            else answer.append('O');
        }
        
        return answer.toString();
    }
    
    static private void command(int now, String[] cmd){
        
        for (String command : cmd) {
            char c = command.charAt(0);
            
            if (c == 'U') {
                int x = Integer.parseInt(command.substring(2));
                
                for (int i = 0; i < x; i++) {
                    now = prev[now];
                }
            }
            else if (c == 'D') {
                int x = Integer.parseInt(command.substring(2));
                
                for (int i = 0; i < x; i++) {
                    now = next[now];
                }
            }
            else if (c == 'C') {
                trash.push(now); // 현재 행 삭제 스택에 저장
                
                int p = prev[now]; // 내 앞의 행
                int nNext = next[now]; // 내 뒤의 행
                
                // 나의 이전 행과 다음 행 연결
                if (p != -1) {
                    next[p] = nNext;
                }
                if (nNext != -1) {
                    prev[nNext] = p;
                }
                
                // 다음 행이 있으면 다음 행 선택
                // 없으면 이전 행 선택
                if (nNext != -1) {
                    now = nNext;
                } 
                else {
                    now = p;
                }
            }
            else if (c == 'Z') {
                int restore = trash.pop();
                int p = prev[restore]; // 삭제됐던 행의 이전 행 번호
                int nNext = next[restore]; //삭제됐던 행의 다음 행 번호
                
                // 이전 행과 복구 행 연결
                if (p != -1) {
                    next[p] = restore;
                }
                // 복구 행과 다음 행 연결
                if (nNext != -1) {
                    prev[nNext] = restore;
                }
            }
        }
    }
}
import java.util.*;

class Solution {
    static int[] prev; // 내 앞의 행의 번호
    static int[] next; // 내 뒤의 행의 번호
    static Stack<Integer> trash;
    
    public String solution(int n, int k, String[] cmd) {
        prev = new int[n];
        next = new int[n];
        trash = new Stack<>();
        
        for (int i = 0; i < n; i++) {
            prev[i] = i - 1;
            next[i] = i + 1;
        }
        next[n - 1] = -1;
        
        command(k, cmd);
        
        boolean[] deleted = new boolean[n];
        while (!trash.isEmpty()) {
            deleted[trash.pop()] = true;
        }
        
        StringBuilder answer = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            if (deleted[i]) answer.append('X');
            else answer.append('O');
        }
        
        return answer.toString();
    }
    
    static private void command(int now, String[] cmd){
        
        for (String command : cmd) {
            char c = command.charAt(0);
            
            if (c == 'U') {
                int x = Integer.parseInt(command.substring(2));
                
                for (int i = 0; i < x; i++) {
                    now = prev[now];
                }
            }
            else if (c == 'D') {
                int x = Integer.parseInt(command.substring(2));
                
                for (int i = 0; i < x; i++) {
                    now = next[now];
                }
            }
            else if (c == 'C') {
                trash.push(now); // 현재 행 삭제 스택에 저장
                
                int p = prev[now]; // 내 앞의 행
                int nNext = next[now]; // 내 뒤의 행
                
                // 나의 이전 행과 다음 행 연결
                if (p != -1) {
                    next[p] = nNext;
                }
                if (nNext != -1) {
                    prev[nNext] = p;
                }
                
                // 다음 행이 있으면 다음 행 선택
                // 없으면 이전 행 선택
                if (nNext != -1) {
                    now = nNext;
                } 
                else {
                    now = p;
                }
            }
            else if (c == 'Z') {
                int restore = trash.pop();
                int p = prev[restore]; // 삭제됐던 행의 이전 행 번호
                int nNext = next[restore]; //삭제됐던 행의 다음 행 번호
                
                // 이전 행과 복구 행 연결
                if (p != -1) {
                    next[p] = restore;
                }
                // 복구 행과 다음 행 연결
                if (nNext != -1) {
                    prev[nNext] = restore;
                }
            }
        }
    }
}
