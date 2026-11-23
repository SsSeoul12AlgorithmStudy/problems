#include <string>
#include <vector>

using namespace std;

//n개의 풍선 일렬로 나열
//모든 풍선에는 다른 숫자
//임의의 인접한 두 풍선 선택 두 풍선 중 하나를 터트림
//터진 풍선들 사이 빈 공간 중앙으로 밀착
//두 풍선 중 번호가 더 작은 풍선을 터트리는 행위는 최대 1번

//인접한 2개를 잡고 터트려야함
//그럼 경우의 수 부분집합이면 될려나?
//시간초과 날 것 같음

//각 구간은 큰 풍선부터 터트리면 최솟값 하나를 남길 수 있음
//최종 상태는 [왼쪽 최소, a[i], 오른쪽 최소]
//양쪽 다 a[i]보다 작으면 불가능한 경우, 하나만 작으면 가능한 경우

int solution(vector<int> a) {
    int n = a.size();
    
    vector<int> right(n);
    
    right[n - 1] = a[n - 1];
    //맨 끝 인덱스에서부터 왼쪽으로 내려오기
    //a[i] 위치에서 오른쪽의 값의 최소가 right[i]라는 뜻
    for(int i = n - 2; i >= 0; --i) {
        right[i] = min(a[i], right[i + 1]);
    }
    
    //왼쪽은 for문으로 이제 탐색하면서 시작.
    int answer = 1;
    int minVal = a[0];
    for(int i = 1; i < n; i++) {
        //양쪽 다 a[i]보다 작으면 불가능한 경우이기 때문에 건너뛰고 나머지만 추가
        if(!(minVal < a[i] && a[i] > right[i])) answer++;
        minVal = min(minVal, a[i]);
    }
    
    return answer;
}
