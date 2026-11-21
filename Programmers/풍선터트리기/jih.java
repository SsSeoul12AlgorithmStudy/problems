import java.util.*;

class Solution {
    /*
    26/11/21 
    
    15:10 ~ 15:59 (시간초과) ~ 16:15 (정답)
    
    
    a배열에서 인접한 두 풍선 중 하나를 계속 제거했을 때, 마지막까지 남을 수 있는 풍선은 몇 가지인지 구하는 함수를 작성하자.
    
    - 단 작은 풍선을 제거하는 것은 딱 한번만 가능하다
    
    ---
    
    제약조건
    - n = a의 길이 <= 10만
    
    ---
    
    단순하게 푸는 방법1
    - 풍선을 하나씩 제거하는 순열을 탐색하며 최후에 남는 풍선의 개수를 센다
    - 시간복잡도는 n!이라 무조건 시간초과가 발생!
    
    방법2
    - 각 풍선을 살릴 수 있는지 판정할 수 있을까
        - a[i]만 남으려면
            - 왼쪽에는 a[1..i-1] 중 하나만 남는다.
            - 오른쪽에는 a[i+1..n] 중 하나만 남는다.
            - 왼쪽과 오른쪽을 제거하여 a[i]만 선택할 수 있어야 한다.
        - 이렇게 제거했을 때 작은 것을 선택한 횟수가 1이하라는 것만 판정하면 되는데
        - 고려해야할 경우의 수
            - 왼쪽에서 a[i] 보다 작은 값이 남음, 오른쪽에서 a[i]보다 큰 값이 남음
            - 왼쪽에서 a[i] 보다 큰값이 남음, 오른쪽에서 a[i]보다 작은 값이 남음
            - 왼쪽에서 a[i] 보다 큰값이 남음, 오른쪽에서 a[i]보다 큰 값이 남음
        - 작은 값 구하기는 최솟값으로 결정 가능
        - 큰 값 구하기는 a[j..k]에서 a[i]보다 큰 값이 하나라도 남을 수 있는지 결정 필요
    
    시간복잡도는 n^2보다는 n log n에 가까워야할 것 같다
    
    */
    static class Node{
        int value, index;
        
        public Node(int value, int index){
            this.value = value;
            this.index = index;
        }
        
        @Override
        public String toString(){
            return "[value=" + value + ", index=" + index + "]";
        }
    }
    
    public int solution(int[] a) {
        int answer = 0;
        int n = a.length;

        // Integer.compare 없이 - 연산자로 Comparator를 작성하면 오버플로우로 인해서 제대로 정렬이 이루어지지 않는 것 같다
        PriorityQueue<Node> pq = new PriorityQueue<>((first, second) -> Integer.compare(first.value, second.value));
        for(int i = 0; i < n; i++){
            pq.offer(new Node(a[i], i));
        }
        pq.offer(new Node(Integer.MAX_VALUE, n));
        
        // System.out.println(pq);
        
        int left = Integer.MAX_VALUE;
        Node right = pq.poll();
        
        for(int i = 0; i < n; i++){
            while(right.index <= i){
                right = pq.poll();
            }
            
            if(a[i] < left && a[i] < right.value || a[i] < left && a[i] > right.value || a[i] > left && a[i] < right.value){
                // System.out.printf("left %d, right %d answer %d\n", left, right.value, answer);
                answer++;
            }
            
            if(a[i] < left) {
                left = a[i];
            }
        }
        
        return answer;
 