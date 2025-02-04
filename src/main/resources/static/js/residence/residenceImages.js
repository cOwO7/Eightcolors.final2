function previewImages() {
    const files = document.getElementById('photoFiles').files;

    // 각 필드에 이미지를 삽입하기
    for (let i = 0; i < files.length; i++) {
        const file = files[i];
        const reader = new FileReader();

        reader.onload = function (e) {
            const imgElement = document.createElement('img');
            imgElement.src = e.target.result;
            imgElement.style.width = '150px';  // 컨테이너 크기에 맞게 조정
            imgElement.style.height = '150px'; // 컨테이너 크기에 맞게 조정
            imgElement.style.objectFit = 'contain'; // 이미지 비율 유지하며 맞추기
            imgElement.style.marginBottom = '2px'; // 하단 여백 추가

            const cancelBtn = document.createElement('button');
            cancelBtn.innerText = 'X';
            cancelBtn.style.position = 'absolute';
            cancelBtn.style.top = '0';
            cancelBtn.style.right = '0';
            cancelBtn.style.backgroundColor = 'rgba(255, 0, 0, 0.5)';
            cancelBtn.style.color = 'white';
            cancelBtn.style.border = 'none';
            cancelBtn.style.borderRadius = '50%';
            cancelBtn.style.width = '20px';
            cancelBtn.style.height = '20px';
            cancelBtn.style.cursor = 'pointer';

            const imgContainer = document.createElement('div');
            imgContainer.style.position = 'relative';
            imgContainer.style.display = 'flex';
            imgContainer.style.justifyContent = 'center'; // 이미지 중앙 정렬
            imgContainer.style.alignItems = 'center';     // 이미지 수직 중앙 정렬
            imgContainer.style.marginBottom = '2px'; // 하단 여백 추가
            imgContainer.style.width = 'calc(20% - 10px)'; // 한 줄에 5개씩 보여주기 위한 크기 설정
            imgContainer.style.height = '150px'; // 고정 높이로 설정

            cancelBtn.onclick = function () {
                imgContainer.remove();
                removeFileFromInput(i); // 파일 목록에서 해당 이미지 삭제
            };

            imgContainer.appendChild(imgElement);
            imgContainer.appendChild(cancelBtn);

            // field${i+1} 요소가 존재하는지 확인
            const field = document.getElementById(`field${i + 1}`);
            if (field) {  // 요소가 존재하면
                field.innerHTML = ''; // 기존 내용 지우기
                field.appendChild(imgContainer); // 이미지와 취소 버튼 추가
            }

            // 파일 입력창이 여러 개일 경우, 나머지 필드는 텍스트 그대로 유지되도록 설정
            if (i >= 10) return; // 10개까지만 삽입
        };

        reader.readAsDataURL(file);
    }
}
// input에서 파일을 삭제하는 함수
function removeFileFromInput(index) {
    const input = document.getElementById('photoFiles');
    const dt = new DataTransfer();
    const files = input.files;

    for (let i = 0; i < files.length; i++) {
        if (i !== index) {
            dt.items.add(files[i]);
        }
    }

    input.files = dt.files;
}