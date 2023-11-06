![image](https://github.com/pre-onboarding/yamyam/assets/80959635/1933009a-1077-4cbd-8c46-6f3f672fb490)
# 지리기반 맛집 추천 웹 서비스
<p>
<img src="https://img.shields.io/github/issues-pr-closed/pre-onboarding/yamyam?color=blueviolet"/>
<img src="https://img.shields.io/github/issues/pre-onboarding/yamyam?color=inactive"/>
<img src="https://img.shields.io/github/issues-closed/pre-onboarding/yamyam"/> 
</p>

## 소개
안녕하세여 기업형 과제를 맡게된 원티드 프리온 보딩 L팀입니다.    
저희는 사용자가 원하는 요구사항을 분석하여 프로젝트를 구현하였고, 
더 필요한 내용은 회의를 통해 추가 했습니다.    
자세한 내용이나 회의록을 보고 싶으면 [깃허브 위키](https://github.com/pre-onboarding/yamyam/wiki)를 확인해주세요!!

## 개요
경기도 일반 음식점 현황(중국식, 일식, 탕류)를 활용해서 음식점 목록을 자동으로 업데이트합니다. 유저의 위치에 맞게 맛집 및 메뉴를 추천하여 더 나은 다양한 음식 경험을 제공하고, 음식을 좋아하는 사람들 간의 소통과 공유를 촉진하려 합니다. 
## 요구사항
<details>
<summary>A.유저</summary>
<div align="left">
  
### 사용자 회원가입(API)

- 본 서비스에서는 유저 고유 정보가 크게 사용되지 않아 간단히 구현합니다.
- `계정명` , `패스워드` 입력하여 회원가입

### 사용자 로그인(API)

- `계정`, `비밀번호` 로 로그시 `JWT` 가 발급됩니다.
- 이후 모든 API 요청 Header 에 `JWT` 가 항시 포함되며, `JWT` 유효성을 검증합니다.

### 사용자 설정 업데이트(API)

- 사용자의 위치 인 `위도`, `경도` 를 업데이트 합니다.(구현 영역)
    - 사용자 브라우저(웹 또는 앱)에서 사용자 `위도` , `경도` 정보를 가지고 호출한다는 전제(미구현 영역).
- `점심 추천 기능 사용 여부` 를 업데이트 합니다.
    - 하위 점심추천 기능을 받을지 설정합니다.

### 사용자 정보 (API)

- `패스워드` 를 제외한 모든 사용자 정보를 반환합니다.
- 클라이언트에서 사용자 위, 경도 / 점심추천 기능 사용여부 를 사용하기 위해서 입니다.
</div>
</details>

<details>
<summary>B.데이터 파이르파라인</summary>
<div align="left">
  
- API호출로 동작되는 기능이 아닌 스케쥴러를 통해 매 시간 실행되는 기능들입니다. 클래스, 함수 등 자유롭게 구조하세요.

### 데이터 수집

- [공공데이터포털](https://www.data.go.kr/tcs/dss/selectDataSetList.do?dType=API&keyword=%EA%B2%BD%EA%B8%B0%EB%8F%84+%EC%9D%BC%EB%B0%98%EC%9D%8C%EC%8B%9D%EC%A0%90&operator=AND&detailKeyword=&publicDataPk=&recmSe=&detailText=&relatedKeyword=&commaNotInData=&commaAndData=&commaOrData=&must_not=&tabId=&dataSetCoreTf=&coreDataNm=&sort=_score&relRadio=&orgFullName=&orgFilter=&org=&orgSearch=&currentPage=1&perPage=10&brm=&instt=&svcType=&kwrdArray=&extsn=&coreDataNmArray=&pblonsipScopeCode=) 로 접속하여 연동할 Open API 규격을 확인합니다.
- **개발자 키 발급 절차**
- **경기도_일반음식점(xx) 에 해당하는 OpenAPI 중 3가지 이상 수집에 사용합니다.(한식, 중식, 일식 등)**
- `Open API` 에 포함된 모든 필드 포함.(데이터 형태에 따라 필드 형식 고려) >
    
    ```markdown
    <SIGUN_NM>안양시</SIGUN_NM>
        <SIGUN_CD/>
        <BIZPLC_NM>시시마루</BIZPLC_NM>
        <LICENSG_DE>19990201</LICENSG_DE>
        <BSN_STATE_NM>영업</BSN_STATE_NM>
        <CLSBIZ_DE/>
        <LOCPLC_AR/>
        <GRAD_FACLT_DIV_NM/>
        <MALE_ENFLPSN_CNT>0</MALE_ENFLPSN_CNT>
        <YY/>
        <MULTI_USE_BIZESTBL_YN/>
        <GRAD_DIV_NM/>
        <TOT_FACLT_SCALE/>
        <FEMALE_ENFLPSN_CNT>0</FEMALE_ENFLPSN_CNT>
        <BSNSITE_CIRCUMFR_DIV_NM>기타</BSNSITE_CIRCUMFR_DIV_NM>
        <SANITTN_INDUTYPE_NM/>
        <SANITTN_BIZCOND_NM>정종/대포집/소주방</SANITTN_BIZCOND_NM>
        <TOT_EMPLY_CNT/>
        <REFINE_LOTNO_ADDR>경기도 안양시 동안구 관양동 1602-6번지</REFINE_LOTNO_ADDR>
        <REFINE_ROADNM_ADDR>경기도 안양시 동안구 관평로182번길 23 (관양동, 무지개상가103호)</REFINE_ROADNM_ADDR>
        <REFINE_ZIP_CD>14066</REFINE_ZIP_CD>
        <REFINE_WGS84_LOGT>126.9615971924</REFINE_WGS84_LOGT>
        <REFINE_WGS84_LAT>37.3937521667</REFINE_WGS84_LAT>
    **평점**
    ```
    
    - 내부 모델명 및 필드명 자율적으로 설정 가능.
    - 참고, 하위 기능 개발 중에서 추가 필드도 생성 예정입니다.(평점 등)

### 데이터 전처리

- 데이터를 내부에서 사용될 형태로 변경합니다.
    - 변경이 불필요한 경우 그대로 사용하셔도 됩니다.
- 누락 되거나 이상값을 가질 경우 처리 방침을 정하고 구현합니다.
    - ex) 누락, null 등 이오면 어떤 값으로 채울지 등

### 데이터 저장

- 식당 마다 하나의 데이터가(레코드) 존재해야하며, 정보들은 업데이트 되어야 합니다.
- **유일키인** 사업자 코드가 없기에, 현재 사업자는 사업장마다 내야하는 규칙에 따라 **“** `**가게명` + `주소`**(일반주소, 도로명 주소 중 택1)” 로 유일하게 유지합니다.
    - (과제 구현을 위한 제약 조건으로 실제 데이터에는 중복될수도 있겠습니다. 이는 예외처리하여 무시 바랍니다.)
    - **`어떻게던 하나의 상호가 중복 생성되지 않는다`**

### 자동화
- `스케쥴러`를 설정하여 위 로직을 지정한 시간마다 실행시킵니다.
- 자유롭게 시간과 횟수 등을 설정하세요.

### 기타 - csv 업로드

- **시군구 데이터 업로드**
    
    [sgg_lat_lon.csv](https://prod-files-secure.s3.us-west-2.amazonaws.com/571a24a3-05f9-4ea5-b01f-cba1a3ac070d/1acdea50-8ee7-42af-9477-d1e50192e50c/sgg_lat_lon.csv)
    
    - 이처럼 자주 변하지 않는 데이터 들은 파일업로드를 통해 구현합니다.
    - 필드 (명칭 변경 가능)
        - `do-si` : 도, 시(특별시 등)
        - `sgg` : 시군구
        - `lat`: 위도
        - `lon`: 경도
    - 서비스 시작시 로드 하여도 되고, 직접 함수 실행하여 업로드 해도 좋습니다.
  
</div>
</details>

<details>
<summary>C.REST API - 맛집조회</summary>
<div align="left">
  
### 시군구 목록 (API)

- 위 업로드한 모든 목록을 반환합니다.
- 추후 첨부 된 예시(야놀자) 처럼 `시도` , `시군구` 로 지역 조회 기능에 사용됩니다.

### 맛집 (추가 필드 관리)

- `평점`
    - `double` 타입입니다.
    - 초기 값은 0.0 이며, 맛집이 받은 모든 평가의 평균입니다.
    - 하위 `맛집 평가 API` 에서 업데이트 됩니다.

### 맛집 목록(API)

맛집 목록 API

- GIS
    - GIS(Geographic Information System) 란 **인간생활에 필요한 지리정보를 컴퓨터 데이터로 변환하여 효율적으로 활용하기 위한 정보시스템**이다
- 아래 **`쿼리 파라미터`**를 사용 가능합니다.

| query | 속성 | default(미입력 시 값) | 설명 |
| --- | --- | --- | --- |
| lat | string | 필수값 | 지구 y축 원점 기준 거리 |
| lon | string | 필수값 | 주기 x축 원점 기준 거리 |
| range | double | 필수값 | km 를 소숫점으로 나타냅니다. 0.5 = 500m / 1.0 = 1000km |
| 정렬기능 | string | 거리순 | 정렬기능 파라메터 구조는 자유롭게 구현하되, 위에서 계산된 요청 좌표와 식당 사이의 거리인 거리순 과 평점순을 지원해야합니다. |
| 기타 |  |  |  |
- `lat`, `lon` : 각각 위, 경도를 나타내며 필수값 입니다.(없을 시 400)
    - **`내 주변보기`** 또는 **`특정 지역 보기`**  기능을 하지만 이는 클라이언트에서 구현합니다.
        - 내 주변보기: 클라이언트에서 유저 lat, lon 을 파라메터로 넣어줌.
        - 시군구에서 선택한 항목의 위경도로 요청, 범위는 사용자 필수 입력.
- `range` Km 를 의미하며, 사용자 요청 주소(`lat`, `lon` 과의 거리를 의미합니다.)
    - **1.0 지정시 요청 `lat`, `lon` 에서 1km 이내의 가게만 노출 됩니다.**
    - 본 기능은 난이도로 인해 쿼리 등으로만 수행하기 어렵습니다. 우선 불러온 데이터 들을 코드 loop 를 통해 필터링 하도록 구조하세요.(선택사항)
    - **두 좌표 간의 거리 구하기 Code**
        - js
            
            ```jsx
            function latLonToKm(point1, point2) {
                const lat1 = point1[1];
                const lon1 = point1[0];
                const lat2 = point2[1];
                const lon2 = point2[0];
            
                const R = 6371; // km
                const dLat = toRadians(lat2 - lat1);
                const dLon = toRadians(lon2 - lon1);
            
                const radLat1 = toRadians(lat1);
                const radLat2 = toRadians(lat2);
            
                const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(radLat1) * Math.cos(radLat2);
                const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            
                return R * c;
            }
            
            function toRadians(degrees) {
                return degrees * (Math.PI / 180);
            }
            
            const point1 = [127.07596008849987, 37.2040];
            const point2 = [127.08726833239848, 37.19497222488765];
            
            const distance = latLonToKm(point1, point2);
            console.log("거리 (km): " + distance);
            ```
            
        
        [distance.java](https://prod-files-secure.s3.us-west-2.amazonaws.com/571a24a3-05f9-4ea5-b01f-cba1a3ac070d/427e5e1f-5400-49d2-bf2b-24b2fc1be373/distance.java)
        
        [distance.py](https://prod-files-secure.s3.us-west-2.amazonaws.com/571a24a3-05f9-4ea5-b01f-cba1a3ac070d/99e0ef67-e94a-4b8e-ac21-6089dadc467c/distance.py)
        
- 기타 `page`, `search` , `filter` 등은 **선택사항**입니다.
- 이해를 돕기위한 **Note!**
    
    > **Note!**
    
    내 주변보기, 특정 지역 보기 등은 유저가 사용하는 기능 명칭입니다. 
    이는 사용자가 클라이언트에서 클릭하는 값들에 따라 결정되기에,
    
    **API 에서는 API 요청된 `lat`, `lon`, `range` 를 토대로 조회된 내용만 반환하면 됩니다.**
    
    ex)
    내 주변 보기 - 도보 > 클라이언트가 `유저 정보` lat, lon 참조 및 range = 1.0(km) 파라메터를 던집니다.
    내 주변 보기 - 교통수단 > 클라이언트가 `선택된 시군구` lat, lon 참조 및 range = 5.0(km) 파라메터를 던집니다.
    특정 지역 보기 > 클라이언트가 `선택된 시군구` lat, lon 참조 및 range = 10.0(km) 파라메터를 던집니다.
    > 
    - 이는 결국 같은 동작을 하는 API 를 분리하여 관리하지 않고, 하나의 API 로 지원하여 자유도와 유지보수를 돕게 됩니다.
- **lat = ! lon ! range ! 경기도맛집_한식 에서 아래 목록이 나와야 합니다**
- 특정 지리 좌표 보는 방법
    - 용인시 중심 >
    - 성남시 중심 >
    - 김포시 중심 >

### 맛집 상세정보(API)

- `맛집 모든필드` 를 포함합니다.
- `평가` 상세 리스트도 포함됩니다.
    - (`평가` 는 아래 참조.)
    - 모든 내역을 생성시간 역순(최신순) 으로 반환합니다.
    - 추가 요구사항 없습니다.
 </div>
</details>

<details>
<summary>D.REST API - 평가</summary>
<div align="left">
  
### 평가 (모델링)

| 필드 | 속성 | 설명 | 예시 값 |
| --- | --- | --- | --- |
| 유저 | fk | 평가를 생성한 유저 FK |  |
| 맛집 | fk | 유저가 평가한 대상 맛집 FK |  |
| 점수 | integer | 0 ~ 5 에 해당하는 점수 |  |
| 내용 | string | 평가 내용이며 0~255자 이내 작성 |  |

### 맛집 평가 생성 (API)

- `유저` 가 특정 `맛집` 에 평가를 한다.
- `평가` 가 생성되면, 해당 맛집의 `평점` 을 업데이트 한다.
    - 해당 맛집 모든 평가 기록 조회 및 평균 계산하여 업데이트

</div>
</details>

<details>
<summary>E.대규모 트래픽 대비 캐싱</summary>
<div align="left">
  
- **Redis 를 연동합니다.**
- 단계별 적용 가능한 만큼 구현합니다. (1단계 까지만도 Ok)

### 시군구 목록 고도화(API)

- 모든 유저가 사용하지만, 긴시간 변동이 없는 성격을 지닌 데이터 이기에, 캐싱을 진행한다.
    - 데이터 특성상 만료 기간은 없거나 일반 API 보다 길어도 됩니다.

### 맛집 상세정보 고도화(API)

- **1단계**
    
    ```markdown
    # API 가 호출될때 
    
    1. 캐시에 저장되었는지 확인.
    	1. 저장되어 있으면, 캐싱 데이터 반환한다.
    	2. 저장되어 있지 않으면, DB를 통해 데이터 불러온다.
    		2-1. 캐시에 저장 하고(600초 등 자율적으로 삭제 deadline 설정)
        2-3. 연산된 데이터를 반환한다.
    ```
    
- **2단계**
    
    ```markdown
    2-1. 단계에서 모든 데이터를 캐싱하는것이 아닌
     2-1-1. N개 이상의 평가가 존재하는(=인기있는)맛집만 캐싱
     2-1-2. "조회 수" 필드를 신설하여 관리하고, 조회수 N 이상만 캐싱
     2-1-3. 기타 맛집의 인기 또는 추천율이 높음을 증명하는 지표.(자유롭게 구상)
    
    위 1~3 중 적용
    ```
    

</div>
</details>

<details>
<summary>F.Notification</summary>
<div align="left">

### Discord Webhook 을 활용한 점심 추천 서비스

- **Web hook URL**
    
    ```jsx
    채널로 공유드립니다.
    ```
    
- **body**
    
    ```jsx
    simple
    {
      "username": "Webhook",
      "avatar_url": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRN9lF93jsUSQ2J5jX4f4OcOvJf4I37mCdrfg&usqp=CAU",
      "content": "Text message. Up to 2000 characters."
    
    }
    ```
    
    ```jsx
    detail
    
    {
      "username": "Webhook",
      "avatar_url": "https://i.imgur.com/4M34hi2.png",
      "content": "Text message. Up to 2000 characters.",
      "embeds": [
        {
          "author": {
            "name": "Birdie♫",
            "url": "https://www.reddit.com/r/cats/",
            "icon_url": "https://i.imgur.com/R66g1Pe.jpg"
          },
          "title": "Title",
          "url": "https://google.com/",
          "description": "Text message. You can use Markdown here. *Italic* **bold** __underline__ ~~strikeout~~ [hyperlink](https://google.com) `code`",
          "color": 15258703,
          "fields": [
            {
              "name": "Text",
              "value": "More text",
              "inline": true
            },
            {
              "name": "Even more text",
              "value": "Yup",
              "inline": true
            },
            {
              "name": "Use `\"inline\": true` parameter, if you want to display fields in the same line.",
              "value": "okay..."
            },
            {
              "name": "Thanks!",
              "value": "You're welcome :wink:"
            }
          ],
          "thumbnail": {
            "url": "https://upload.wikimedia.org/wikipedia/commons/3/38/4-Nature-Wallpapers-2014-1_ukaavUI.jpg"
          },
          "image": {
            "url": "https://upload.wikimedia.org/wikipedia/commons/5/5a/A_picture_from_China_every_day_108.jpg"
          },
          "footer": {
            "text": "Woah! So cool! :smirk:",
            "icon_url": "https://i.imgur.com/fKL31aD.jpg"
          }
        }
      ]
    }
    ```
    
- `유저` 중 `점심 추천 서비스` 사용여부를 체크한 유저에 한해, 점심시간 30분전(ex) 주변 맛집 리스트를 제공한다.
- 모든 기준은 직접 설정.
- ex)
    - 점심시간을 12시로 정하고 30분 전인 11:30 분에 `유저` 모델의 `lat`, `lon` 기반으로
    - 500 미터 이내의 맛집을 카테고리별로 5개씩 제공한다.
    - (한식 평점높음 5개, 중식 평점높은 5개 …)
- (또는, `메뉴` 모델을 새로 설계하신 뒤, 해당 `맛집` 별 메뉴 목록도 리턴)

</div>
</details>
  
## 기술스택
<p>
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring&logoColor=white"/>
<img src="https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge&logo=JPA&logoColor=white">
<img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=orange">
<img src="https://img.shields.io/badge/QueryDSL-0285c9?style=for-the-badge&logo=qeurydsl&logoColor=white">
</p>


## ERD
![image](https://github.com/pre-onboarding/yamyam/assets/80959635/722c6f64-ca33-4920-9c5f-d5ceab475ccb)

