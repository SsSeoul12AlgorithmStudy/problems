/*

1시간 30분 동안 문제 이해 x

원인 :
- 포화 이진트리 개념 X
- 중위순회는 캐치 But, 중위 순회 방식을 잘못하고 있었음.

해결 :
- 포화 이진트리 검색
- 중위순회 - PKG explain to me

p.s PKG 아니였으면 문제 이해도 못하고 3시간 문제봤음 청년.


--------------------------------------------------

1. numbers[i] 를 이진수로 변환
2. 포화 이진트리 여부 확인
    - O -> 다음 단계
    - X -> 포화 이진트리 만들기
3. 트리 여부 확인 -> 중위순회

*/

import java.util.Queue;
import java.util.ArrayDeque;

class Solution {
    public int[] solution(long[] numbers) {
        int[] result = new int[numbers.length];
        
        for(int i = 0; i < numbers.length; i++) result[i] = canBuildBTree(numbers[i]);
        
        return result;
    }
    
    private int canBuildBTree(long number) {
        
        // 1. 이진수로 변환
        // 이진수 역순 상태
        StringBuilder binaryNum = new StringBuilder();
        
        while(number > 1) {
            long val = number % 2;
            binaryNum.append(val);
            number /= 2;
        }
        
        if(number == 1) binaryNum.append(number);
        
        // System.out.println(binaryNum.toString());
        
        // 2. 포화 이진트리 여부 체크
        binaryNum = checkFullBinaryTree(binaryNum);
        
        System.out.println(binaryNum.toString());
        
        // 3. 이진 트리 여부 확인
        // 이진수 문자열 길이 + 1 / 2 값 인덱스 가 root가 됨
        // 해당 노드부터 bfs로 갭차이인 2^n 부터 확인하면서 길이가 2^(n-1) 씩 줄이면서 확인하면 됨.
        // ex) root = 8
        // 첫번째 값 8은 2^3
        // 요소에 8과 3을 넣어
        // 8 기준에 3-1 = 2 만큼 2에 제곱한 값을 빼고 더하고 값을 큐에 넣어
        // 다음 4랑 12 보임
        // 똑같이하면 4기준 (2-1) = 2를 빼고 더해 2, 6
        
        int[] info = getDepth(binaryNum.length);
        bfs(info);
        // return 1
        
        return 0;
    }
    
    private int getDepth(int len) {
        int[] arr = new int[2];
        arr[0] = (len + 1) / 2;
        
        if(len == 1) {
            arr[1] = 1;
        } else if(len == 3) {
            arr[1] = 2;
        } else if(len == 7) {
            arr[1] = 4;
        } else if(len == 15) {
            arr[1] = 8;
        }
        
        return arr;
    }
    
    private void bfs(int[] info) {
        Queue<int[]> q = new ArrayDeque<>();
        
        q.offer(info);
        
        while(!q.isEmpty()) {
            int[] now = q.poll();
            
            // now[1] = 0이면 리프노드
        }
    }
    
    private StringBuilder checkFullBinaryTree(StringBuilder number) {
        
        // 포화 이진트리 길이
        // 2-1. 맞다
        if(number.length() == 1 || number.length() == 3 || number.length() == 7 || number.length() == 15) {
            return number;
        }
        
        // 2-2. 아니면 더미(0) 추가
        if(number.length() < 3) {
            while(number.length() < 3) {
                number.append(0);
            }
        } else if(number.length() < 7) {
            while(number.length() < 7) {
                number.append(0);
            }   
        } else if(number.length() < 15) {
            while(number.length() < 15) {
                number.append(0);
            }
        }
        
        return number;
    }
}
