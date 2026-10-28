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

[문제 복기]
- 문제 이해에 약 1시간 30분 소요
- 포화 이진트리 개념을 몰랐고, 중위순회를 활용하는 방식에서 어려움을 겪음

[초기 접근]
1. numbers[i]를 이진수로 변환 (역순 저장)
2. 포화 이진트리의 노드 개수(1, 3, 7, 15 ...)에 맞춰 더미 노드 0 추가
3. 중위순회 결과를 이용해 트리 구조 파악
    - 중앙 인덱스가 루트 노드
    - 루트 기준 좌우 2^n 간격으로 자식 노드 탐색
    - 탐색할 때마다 간격을 절반으로 줄임
4. BFS로 부모가 0인데 자식이 1인 경우 검증

[놓친 부분]
1. 포화 이진트리 개념 부족
    - 노드 개수 = 2^k - 1 (k는 레벨 수)

2. 중위순회 활용 미숙
    - 중위순회 자체는 알고 있었지만 배열의 인덱스를 트리 구조와 연결하지 못함.

3. 입력 범위 분석 오류
    - numbers[i] <= 10^15 조건을 보고 최대 15비트라고 잘못 판단함.
    - 실제 10^15은 이진수로 최대 50비트 -> 포화 이진트리 63개 노드 필요
    - 10진수의 지수와 2진수의 비트 수를 혼동함.

4. 트리 크기를 확장하면 결과가 달라질 수 있다고 판단
    - 최소 포화 이진트리에서 실패해도 더미 노드를 추가하면 가능할 수 있다고 생각함.
    - 따라서 최대 15개 노드까지 확장하며 반복 검증하는 while문을 구상함.
    - 하지만 기존 트리는 확장된 트리의 오른쪽 서브트리로 그대로 유지됨.
    - 기존의 부모 0 -> 자식 1 관계가 사라지지 않으므로 확장해도 불가능함.

[불필요했던 접근]
1. 포화 이진트리 크기를 반복해서 확장하며 검증
    - 최소 포화 이진트리에서 한 번만 검증하면 충분함.

2. BFS에서 visited 배열 사용
    - 각 노드를 한 번씩 방문하므로 방문 처리가 필요 없음.
    - 부모 노드의 값도 이진수 문자열에서 직접 확인 가능함.

[개선할 점]
1. 새로운 자료구조가 등장하면 정의와 구조적 특징부터 이해하기
2. 입력 범위를 실제 구현 단위(비트 수, 배열 크기 등)로 변환해서 분석하기
3. 반복 탐색을 설계하기 전에 기존 상태에서 실패한 조건이 해소될 수 있는지 확인하기
4. 알고리즘 구현 전에 불필요한 탐색이나 상태 관리가 없는지 검토하기

[결론]
- BFS를 이용한 부모·자식 관계 검증 방향은 올바르게 접근함.
- 문제의 핵심은 알고리즘 자체보다 포화 이진트리의 구조와 중위순회 결과의 관계를 이해하는 것이었음.
- 특히 입력 범위 분석 오류와 트리 확장 시 기존 구조가 유지된다는 성질을 놓쳐 불필요한 로직을 설계함.
*/


import java.util.Queue;
import java.util.ArrayDeque;

class Solution {

    public int[] solution(long[] numbers) {
        int[] result = new int[numbers.length];

        for (int i = 0; i < numbers.length; i++) {
            result[i] = canBuildBTree(numbers[i]);
        }

        return result;
    }

    private int canBuildBTree(long number) {

        // 1. 이진수 변환 (역순 저장)
        StringBuilder binaryNum = new StringBuilder();

        while (number > 1) {
            binaryNum.append(number % 2);
            number /= 2;
        }

        if (number == 1) binaryNum.append(1);

        // 2. 포화 이진트리 만들기
        binaryNum = checkFullBinaryTree(binaryNum);

        // 3. BFS로 부모-자식 관계 검증

        // [판단 오류 1 - 입력 범위]
        // numbers[i] <= 10^15 이므로 최대 15비트라고 오해
        // 실제로는 최대 50비트 -> 포화 이진트리 63개 노드 필요
        
        
        // [판단 오류 2 - 반복 검증]
        // 최소 포화 이진트리에서 실패하더라도 더미 노드를 추가해
        // 트리 크기를 늘리면 성공할 수 있다고 판단하여 while문 작성
        
        // 하지만 기존 트리는 확장된 트리의 오른쪽 서브트리로 유지됨
        // 따라서 기존 부모-자식 관계가 바뀌지 않아 재검증이 불필요
        
        // -> 즉, 최소 포화 이진트리에서 BFS 한 번만 수행하면 됨

        int depth = getDepth(binaryNum.length());

        return bfs(depth, binaryNum.reverse().toString()) ? 1 : 0;
    }

    private int getDepth(int len) {
        int depth = 0;

        while (len > 0) {
            depth++;
            len /= 2;
        }

        return depth;
    }

    private boolean bfs(int depth, String number) {

        Queue<int[]> q = new ArrayDeque<>();

        // 중위순회 결과에서 중앙 인덱스가 루트
        // 1-based 인덱스 사용
        int root = 1 << (depth - 1);

        // {현재 노드 인덱스, 남은 깊이}
        q.offer(new int[]{root, depth - 1});

        
        // [불필요한 접근 - visited]
        // 처음에는 BFS로 중위 순회 순서로 루트 노드 부터 visited 배열을 사용
        // 하지만 트리는 사이클이 없고 각 노드를 한 번씩 탐색하므로 불필요

        while (!q.isEmpty()) {
            int[] now = q.poll();

            int node = now[0];
            int diff = now[1];

            // 리프 노드라면 자식이 없음
            if (diff == 0) continue;

            // 자식 노드까지의 인덱스 간격
            // 15개 노드 기준: 4 -> 2 -> 1
            int d2 = 1 << (diff - 1);

            int left = node - d2;
            int right = node + d2;

            char parent = number.charAt(node - 1);
            char leftChild = number.charAt(left - 1);
            char rightChild = number.charAt(right - 1);

            // 부모가 더미 노드(0)인데 자식이 실제 노드(1)이면 불가능
            if (parent == '0' &&
                (leftChild == '1' || rightChild == '1')) {
                return false;
            }

            q.offer(new int[]{left, diff - 1});
            q.offer(new int[]{right, diff - 1});
        }

        return true;
    }

    private StringBuilder checkFullBinaryTree(StringBuilder number) {

        // [판단 오류 3 - 포화 이진트리 최대 크기]
        // 15개 노드(비트) 까지만 확인하도록 작성
        // 실제 조건 - 최대 63개 노드(비트) 필요
         
        // 노드 개수 = 2^k - 1

        int size = 1;

        while (size < number.length()) {
            size = size * 2 + 1;
        }

        // 이진수를 역순 저장했으므로 뒤쪽에 0 추가
        while (number.length() < size) {
            number.append('0');
        }

        return number;
    }
}

