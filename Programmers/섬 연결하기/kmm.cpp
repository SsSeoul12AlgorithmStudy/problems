#include <string>
#include <vector>
#include <algorithm>
#include <iostream>
#define INF 1000000

using namespace std;

//n개의 섬 사이에 다리 건설하는 비용(cost)
//모든 섬이 서로 통행 가능하도록 만들 때 필요한 최소 비용
//다리를 여러번 건너더라도, 도달할 수만 있으면 통행 가능하다고 봄
//cost의 길이는 ((n-1)*n)/2
//플로이드 와샬:
    //모든 정점을 2차원 배열에 저장
    //이때 한 번에 가지 못하는 그래프는 INF로 기록
    //k, i, j로 loop를 돌고, 여기서 k는 해당 과정중 반드시 지나가는 노드
    //min(기존 노드, 거쳐가는 노드)를 통해 최소 거리를 판단, 배열에 저장

//일단 이거 돌리면 특정 노드까지의 최솟값은 나옴
//근데 이걸 어떻게 전체순회했을때 최소로 바꿀까

//흠 플로이드 워셜이 아닌가
//그럼 뭐지?
//
//와 크루스칼이였음
//MST를 구할때는 크루스칼로 구할 수 있음
//크루스칼 알고리즘:
    //그래프를 오름차순으로 정렬
    //간선들 하나씩 확인
    //다른 노드를 연결하고 있으면 연결 (union-find 자료구조 사용)
    //이미 같은 그룹이면 건너뛰기
    //간선을 N-1개 선택했으면 종료

//이거는 나중에 다시 풀어보기

vector<int> parent;

bool cmp(vector<int>& a, vector<int>& b) {
    return a[2] < b[2];
}

int find(int x) {
    if(parent[x] == x) return x;
    return parent[x] = find(parent[x]);
}

bool unite(int a, int b) {
    a = find(a);
    b = find(b);
    
    if(a == b) return false;
    
    parent[b] = a;
    return true;
}

int solution(int n, vector<vector<int>> costs) {
    int answer = 0;
    int cnt = 0;
    
    parent.resize(n);
    for(int i = 0; i < n; i++)
        parent[i] = i;
    
    sort(costs.begin(), costs.end(), cmp);

    for(vector<int>& v : costs) {
        if(cnt == n - 1) break;
        
        int a = v[0];
        int b = v[1];
        int cost = v[2];
        
        if(unite(a,b)) {
            answer += cost;
            cnt++;
        }
    }
    
    return answer;
}


// int solution(int n, vector<vector<int>> costs) {
//     int answer = 0;
//     //플로이드 워셜 기록할 보드
//     vector<vector<int>> flyd(n, vector<int>(n, INF));
    
//     //flyd 보드 채워넣기
//     for(vector<int> v : costs) {
//         flyd[v[0]][v[1]] = v[2];
//         flyd[v[1]][v[0]] = v[2];
//     }
    
//     //k노드를 반드시 지나갈때
//     for(int k = 0; k < n; k++) {
//         for(int i = 0; i < n; i++) {
//             for(int j = 0; j < n; j++) {
//                 flyd[i][j] = min(flyd[i][j], flyd[i][k] + flyd[k][j]);
//             }
//         }
//     }
    
//     for(vector<int> v : flyd) {
//         for(int t : v) {
//             cout << t << " ";
//         }
//         cout << "\n";
//     }
    
//     return answer;
// }
