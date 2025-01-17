-- MySQL 사용자 계정 생성
-- root 계정으로 접속 후 script에서 아래 코드 복사 붙여넣기 실행
-- mysql -u root -h localhost -p mysql
create database eightcolors2025;
create user 'eightcolors2025'@'%' identified by 'eightcolors2025';
grant all privileges on eightcolors2025.* to 'eightcolors2025'@'%';
flush privileges;
-- exit


CREATE DATABASE IF NOT EXISTS eightcolors2025; -- 데이터베이스 생성

use eightcolors2025; -- 데이터베이스 접속


-- 1. 관리자 계정 테이블
CREATE TABLE IF NOT EXISTS admin_users (
                                           admin_user_no BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           admin_id VARCHAR(50) UNIQUE,
                                           admin_passwd VARCHAR(255),
                                           admin_name VARCHAR(100),
                                           role VARCHAR(50) DEFAULT 'ROLE_ADMIN'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 2. 숙박업소 회원가입 테이블
CREATE TABLE IF NOT EXISTS host_users (
                                          host_user_no BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          id VARCHAR(100) UNIQUE,
                                          passwd VARCHAR(255),
                                          email VARCHAR(255),
                                          phone VARCHAR(100),
                                          phone_verify INT DEFAULT 0,
                                          name VARCHAR(100),
                                          zipcode VARCHAR(50),
                                          address1 VARCHAR(255),
                                          address2 VARCHAR(255),
                                          business_license_no VARCHAR(100),
                                          regdate DATETIME DEFAULT CURRENT_TIMESTAMP,
                                          role VARCHAR(50) DEFAULT 'ROLE_HOST'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- 3. 회원가입 테이블
CREATE TABLE IF NOT EXISTS users (
     user_no BIGINT AUTO_INCREMENT PRIMARY KEY,
     id VARCHAR(100),
     password VARCHAR(255),
     email VARCHAR(255),
     phone VARCHAR(100),
     name VARCHAR(100),
     zipcode VARCHAR(50),
     address1 VARCHAR(255),
     address2 VARCHAR(255),
     login_type VARCHAR(50),
     provider_id VARCHAR(255),
     reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     role VARCHAR(50) DEFAULT 'ROLE_USER'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 4. 숙소 테이블
CREATE TABLE IF NOT EXISTS residence (
       resid_no BIGINT AUTO_INCREMENT PRIMARY KEY,
       resid_name VARCHAR(255),
       host_user_no BIGINT,
       resid_description TEXT,
       resid_address VARCHAR(255),
       resid_type ENUM('resort', 'hotel', 'pension', 'motel'),
       resid_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       UNIQUE (host_user_no),
       FOREIGN KEY (host_user_no) REFERENCES host_users(host_user_no) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
-- 숙소 매진시 sold-out 상태 추가
ALTER TABLE residence
    ADD COLUMN sold_out BOOLEAN DEFAULT FALSE AFTER resid_type;
-- nx와 ny는 정수 타입이고, 기본값은 0으로 설정
ALTER TABLE residence
    ADD COLUMN nx INT DEFAULT 0 AFTER resid_type;
ALTER TABLE residence
    ADD COLUMN ny INT DEFAULT 0 AFTER nx;
-- regId와 regIdTemp는 문자열을 저장하므로 VARCHAR 타입으로 설정
ALTER TABLE residence
    ADD COLUMN regId VARCHAR(255) DEFAULT '' AFTER ny;
ALTER TABLE residence
    ADD COLUMN regIdTemp VARCHAR(255) DEFAULT '' AFTER regId;

select * from residence;

-- 5. 숙소 방 정보 테이블
CREATE TABLE IF NOT EXISTS residence_rooms (
                                               room_no BIGINT AUTO_INCREMENT PRIMARY KEY,
                                               resid_no BIGINT,
                                               room_name VARCHAR(255),
                                               price_per_night INT,
                                               FOREIGN KEY (resid_no) REFERENCES residence(resid_no) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 6. 예약 페이지 테이블
CREATE TABLE IF NOT EXISTS reservations (

                                            reservation_no BIGINT AUTO_INCREMENT PRIMARY KEY,
                                            user_no BIGINT,
                                            room_no BIGINT,
                                            checkin_date DATE,
                                            checkout_date DATE,
                                            total_price INT,
                                            discount_rate INT DEFAULT 0,
                                            discounted_price INT,
                                            transaction_id VARCHAR(255) UNIQUE,
                                            payment_status ENUM('대기중', '완료', '실패', '취소') DEFAULT '대기중',
                                            reservation_status ENUM('예약 완료', '체크인 완료', '체크아웃 완료', '예약 취소') DEFAULT '예약 완료',
                                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                            FOREIGN KEY (user_no) REFERENCES users(user_no) ON DELETE CASCADE,
                                            FOREIGN KEY (room_no) REFERENCES residence_rooms(room_no) ON DELETE CASCADE

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 7. 댓글 테이블
CREATE TABLE IF NOT EXISTS reviews (
                                       review_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       resid_no BIGINT,
                                       user_id BIGINT,
                                       comment TEXT,
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       FOREIGN KEY (resid_no) REFERENCES residence(resid_no),
                                       FOREIGN KEY (user_id) REFERENCES users(user_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 8. 고객센터 테이블
CREATE TABLE IF NOT EXISTS inquiries (
                                         inquiry_no BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         user_no BIGINT,
                                         title VARCHAR(255),
                                         content TEXT,
                                         inquiry_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                                         status ENUM('대기중', '답변완료') DEFAULT '대기중',
                                         FOREIGN KEY (user_no) REFERENCES users(user_no) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 9. 답변 테이블
CREATE TABLE IF NOT EXISTS answers (

  answer_no BIGINT AUTO_INCREMENT PRIMARY KEY,
  inquiry_no BIGINT,
  admin_user_no BIGINT,
  content TEXT,
  answer_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (inquiry_no) REFERENCES inquiries(inquiry_no) ON DELETE CASCADE,
  FOREIGN KEY (admin_user_no) REFERENCES admin_users(admin_user_no) ON DELETE CASCADE

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 10. 숙소 사진 테이블
CREATE TABLE IF NOT EXISTS property_photos (
                                               photo_no BIGINT AUTO_INCREMENT PRIMARY KEY,
                                               resid_no BIGINT,
                                               thumbnailUrls VARCHAR(255),
                                               photo_url01 VARCHAR(255),
                                               photo_url02 VARCHAR(255),
                                               photo_url03 VARCHAR(255),
                                               photo_url04 VARCHAR(255),
                                               photo_url05 VARCHAR(255),
                                               photo_url06 VARCHAR(255),
                                               photo_url07 VARCHAR(255),
                                               photo_url08 VARCHAR(255),
                                               photo_url09 VARCHAR(255),
                                               photo_url10 VARCHAR(255),
                                               FOREIGN KEY (resid_no) REFERENCES residence(resid_no) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 11. 공지사항 테이블
CREATE TABLE IF NOT EXISTS notices (
                                       notice_no BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       admin_user_no BIGINT,
                                       title VARCHAR(255),
                                       content TEXT,
                                       notice_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                                       is_active TINYINT(1) DEFAULT 1,
                                       FOREIGN KEY (admin_user_no) REFERENCES admin_users(admin_user_no) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
select * from notices;

-- 12. 양도 테이블
CREATE TABLE IF NOT EXISTS transfers (
                                         transfer_no BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         seller_user_no BIGINT,
                                         buyer_user_no BIGINT,
                                         reservation_no BIGINT,
                                         transfer_price INT,
                                         status ENUM('양도가능', '양도완료') DEFAULT '양도가능',
                                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                         FOREIGN KEY (seller_user_no) REFERENCES users(user_no) ON DELETE CASCADE,
                                         FOREIGN KEY (buyer_user_no) REFERENCES users(user_no) ON DELETE SET NULL,
                                         FOREIGN KEY (reservation_no) REFERENCES reservations(reservation_no) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


ALTER TABLE transfers
    ADD COLUMN transfer_title VARCHAR(255);

ALTER TABLE reservations
    MODIFY COLUMN discounted_price INT;

ALTER TABLE transfers
    MODIFY COLUMN transfer_price INT;

ALTER TABLE transfers
    ADD COLUMN transfer_content VARCHAR(1000);

select * from transfers;



-- 1. 관리자 계정 데이터 삽입
INSERT INTO admin_users (admin_id, admin_passwd, admin_name, role)
VALUES
    ('admin01', 'adminpass123', '관리자1', 'ROLE_ADMIN'),
    ('admin02', 'adminpass456', '관리자2', 'ROLE_ADMIN');


-- 2. 숙박업소 회원가입 데이터 삽입
INSERT INTO host_users (id, passwd, email, phone, name, zipcode, address1, address2, business_license_no, role)
VALUES
    ('hostuser01', 'hostpass123', 'hostuser01@email.com', '010-1234-5678', '호스트1', '12345', '서울시 강남구', '역삼동 123', '1234567890', 'ROLE_HOST'),
    ('hostuser02', 'hostpass456', 'hostuser02@email.com', '010-2345-6789', '호스트2', '54321', '서울시 서초구', '반포동 456', '0987654321', 'ROLE_HOST');

-- 3. 일반 회원가입 데이터 삽입
INSERT INTO users (id, password, email, phone, name, zipcode, address1, address2, login_type, provider_id, role)
VALUES

    ('user01', 'userpass123', 'user01@email.com', '010-5678-1234', '회원1', '11111', '서울시 마포구', '상암동 789', 'LOCAL', '', 'ROLE_USER'),
    ('user02', 'userpass456', 'user02@email.com', '010-6789-2345', '회원2', '22222', '서울시 동작구', '신대방동 101', 'GOOGLE', 'google123', 'ROLE_USER');


-- 4. 숙소 데이터 삽입
INSERT INTO residence (resid_name, host_user_no, resid_description, resid_address, resid_type)
VALUES
    ('호스텔 서울', 1, '서울에서 편리한 위치의 호스텔입니다.', '서울시 강남구 역삼동', 'resort'),
    ('호텔 강남', 2, '편안하고 고급스러운 호텔입니다.', '서울시 서초구 반포동', 'hotel');

-- 5. 숙소 방 정보 데이터 삽입
INSERT INTO residence_rooms (resid_no, room_name, price_per_night)
VALUES
    (1, '101호', 50000),
    (1, '102호', 60000),
    (2, '201호', 150000),
    (2, '202호', 180000);

-- 6. 예약 데이터 삽입 - 안됨
INSERT INTO reservations (user_no, room_no, checkin_date, checkout_date, total_price, discounted_price, transaction_id, payment_status, reservation_status)
VALUES
    (1, 1, '2025-02-01', '2025-02-03', 100000, 95000, 'txn12345', '완료', '예약 완료'),
    (2, 3, '2025-02-05', '2025-02-07', 300000, 290000, 'txn67890', '완료', '예약 완료');

-- 7. 리뷰 데이터 삽입
INSERT INTO reviews (resid_no, user_id, comment)
VALUES
    (1, 1, '편안하고 좋았어요! 다시 올게요.'),
    (2, 2, '서비스가 최고였습니다. 다시 방문하고 싶어요.');

-- 8. 고객센터 문의 데이터 삽입
INSERT INTO inquiries (user_no, title, content)
VALUES
    (1, '예약 변경 요청', '예약 날짜를 변경하고 싶습니다.'),
    (2, '문의사항', '결제 오류가 발생했습니다.');

-- 9. 답변 데이터 삽입
INSERT INTO answers (inquiry_no, admin_user_no, content)
VALUES
    (1, 1, '예약 날짜 변경은 가능합니다. 고객센터로 문의해주세요.'),
    (2, 2, '결제 오류는 기술팀에서 확인하고 있습니다. 잠시만 기다려주세요.');

-- 10. 숙소 사진 데이터 삽입
INSERT INTO property_photos (resid_no, thumbnailUrls, photo_url01, photo_url02)
VALUES
    (1, 'thumbnail01.jpg', 'photo01.jpg', 'photo02.jpg'),
    (2, 'thumbnail02.jpg', 'photo03.jpg', 'photo04.jpg');

-- 11. 공지사항 데이터 삽입 - 안됨
INSERT INTO notices (admin_user_no, title, content)
VALUES
    (1, '시스템 점검 안내', '정기적인 시스템 점검이 예정되어 있습니다. 서비스 이용에 불편을 드려 죄송합니다.');

-- 12. 양도 데이터 삽입
INSERT INTO transfers (seller_user_no, buyer_user_no, reservation_no, transfer_price, status, transfer_title)
VALUES
    (1, 2, 1, 100000, '양도완료', '양도합니다.');

INSERT INTO transfers (seller_user_no, buyer_user_no, reservation_no, transfer_price, status, transfer_title)
VALUES
    (2, 2, 1, 100000, '양도가능', '양도합니다.');

-- -----------------------------------------------------------------------------------------------------------------------------

-- 트리거: 답변 작성 시 문의 상태 변경

/*DELIMITER $$
CREATE TRIGGER update_inquiry_status_after_answer
    AFTER INSERT ON answers
    FOR EACH ROW
BEGIN
    UPDATE inquiries
    SET status = 'answered'
    WHERE inquiry_no = NEW.inquiry_no;
END $$
DELIMITER ;*/

-- -----------------------------------------------------------------------------------------------------------------------------

-- 수정된 주의사항, 테이블 생성 순서, ERD 관계 요약은 아래와 같습니다.

-- 주의사항
-- 외래 키 참조 순서
-- 외래 키(Foreign Key)가 있는 테이블은 반드시 참조되는 테이블이 먼저 생성되어야 합니다.
-- 예: users 테이블이 먼저 생성된 후, 이를 참조하는 reservations, reviews 등이 생성되어야 합니다.

-- 외래 키 제약 조건

-- ON DELETE CASCADE: 참조된 데이터가 삭제될 경우, 연관된 데이터도 자동으로 삭제됩니다.
-- ON UPDATE CASCADE: 참조된 데이터가 수정될 경우, 연관된 데이터도 자동으로 업데이트됩니다.
-- 이 설정은 데이터의 무결성을 유지하고 수동으로 삭제나 수정 작업을 줄이기 위해 포함되었습니다.
-- 인코딩
-- 모든 테이블은 UTF-8(MB4) 설정으로 생성되었으며, 다국어 데이터를 저장할 수 있습니다.

-- 트리거
-- answers 테이블에 답변이 작성되면 자동으로 inquiries 테이블의 상태(status)가 'answered'로 업데이트됩니다.
-- 트리거를 사용할 때는 정확한 동작을 확인하기 위해 테스트 환경에서 검증하세요.

-- -----------------------------------------------------------------------------------------------------------------------------

-- 테이블 생성 순서

-- 1. admin_users: 관리자 계정 테이블, users: 회원가입 테이블


-- 2. residence: 숙소 상세 정보 테이블


-- 3. reservations: 예약 정보 테이블, reviews: 후기/댓글 테이블
-- attendance_logs: 출석 로그 테이블, property_photos: 숙소 사진 테이블

-- 4. inquiries: 고객센터 문의 테이블, answers: 답변 테이블

-- -----------------------------------------------------------------------------------------------------------------------------

-- ERD 관계 요약

-- admin_users
-- admin_user_no → answers.admin_user_no (1:N 관계)
-- 관리자 계정은 여러 답변을 작성할 수 있음.

-- users
-- user_no → reservations.user_no (1:N 관계)
-- 한 회원은 여러 예약을 가질 수 있음.
-- user_no → reviews.user_no (1:N 관계)
-- 한 회원은 여러 리뷰를 작성할 수 있음.
-- user_no → attendance_logs.user_no (1:N 관계)
-- 한 회원은 여러 출석 로그를 가질 수 있음.
-- user_no → inquiries.user_no (1:N 관계)
-- 한 회원은 여러 문의를 작성할 수 있음.

-- residence
-- resid_no → reservations.resid_no (1:N 관계)
-- 한 숙소는 여러 예약 정보를 가질 수 있음.
-- resid_no → reviews.resid_no (1:N 관계)
-- 한 숙소는 여러 리뷰를 받을 수 있음.
-- resid_no → property_photos.resid_no (1:N 관계)
-- 한 숙소는 여러 사진을 가질 수 있음.

-- inquiries
-- inquiry_no → answers.inquiry_no (1:N 관계)
-- 한 문의는 여러 답변을 가질 수 있음.


-- 기상청 nx,ny 좌표 테이틀
CREATE TABLE weather_coordinate (
    kor_code VARCHAR(20) PRIMARY KEY,       -- 행정구역 코드 (예: kor1111000000)
    area_name VARCHAR(100) NOT NULL,        -- 지역 이름 (예: 서울특별시 종로구)
    grid_x INT NOT NULL,                    -- 격자 X 좌표 (nx)
    grid_y INT NOT NULL,                    -- 격자 Y 좌표 (ny)
    longitude DECIMAL(9,6) NOT NULL,        -- 경도 (소수점 6자리까지)
    latitude DECIMAL(9,6) NOT NULL,         -- 위도 (소수점 6자리까지)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 데이터 생성 시각
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 데이터 수정 시각
);

-- 서울
-- 서울특별시 종로구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor1111000000', '서울특별시 종로구', 60, 127, 126.0, 37.5727);
-- 서울특별시 중구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor1114000000', '서울특별시 중구', 60, 127, 126.0, 37.5636);
-- 서울특별시 용산구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor1117000000', '서울특별시 용산구', 60, 126, 126.0, 37.5326);
-- 서울특별시 성동구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor1120000000', '서울특별시 성동구', 61, 127, 127.0, 37.5637);
-- 서울특별시 광진구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor1121500000', '서울특별시 광진구', 62, 126, 127.0, 37.5383);
-- 서울특별시 동대문구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor1123000000', '서울특별시 동대문구', 61, 127, 127.0, 37.5743);
-- 서울특별시 중랑구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor1126000000', '서울특별시 중랑구', 62, 128, 127.0, 37.6061);
-- 서울특별시 성북구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor1129000000', '서울특별시 성북구', 61, 127, 127.0, 37.5873);
-- 서울특별시 강북구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor1130500000', '서울특별시 강북구', 61, 128, 127.0, 37.6351);
-- 서울특별시 도봉구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor1132000000', '서울특별시 도봉구', 61, 129, 127.0, 37.6548);
-- 서울특별시 노원구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor1135000000', '서울특별시 노원구', 61, 129, 127.0, 37.6557);
-- 서울특별시 은평구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor1138000000', '서울특별시 은평구', 59, 127, 126.0, 37.6040);
-- 서울특별시 서대문구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor1141000000', '서울특별시 서대문구', 59, 127, 126.0, 37.5791);
-- 서울특별시 마포구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor1144000000', '서울특별시 마포구', 59, 127, 126.0, 37.5663);
-- 서울특별시 양천구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor1147000000', '서울특별시 양천구', 58, 126, 126.0, 37.5270);
-- 서울특별시 강서구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor1150000000', '서울특별시 강서구', 58, 126, 126.0, 37.5586);
-- 서울특별시 구로구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor1153000000', '서울특별시 구로구', 58, 125, 126.0, 37.4952);

-- 서울특별시 금천구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor1154500000', '서울특별시 금천구', 59, 124, 126.0, 37.4592);
-- 서울특별시 영등포구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor1156000000', '서울특별시 영등포구', 58, 126, 126.0, 37.5162);
-- 서울특별시 동작구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor1159000000', '서울특별시 동작구', 59, 125, 126.0, 37.5110);
-- 서울특별시 관악구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor1162000000', '서울특별시 관악구', 59, 125, 126.0, 37.4787);
-- 서울특별시 서초구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor1165000000', '서울특별시 서초구', 61, 125, 127.0, 37.4836);
-- 서울특별시 강남구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor1168000000', '서울특별시 강남구', 61, 126, 127.0, 37.5174);
-- 서울특별시 송파구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor1171000000', '서울특별시 송파구', 62, 126, 127.0, 37.5144);
-- 서울특별시 강동구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor1174000000', '서울특별시 강동구', 62, 126, 127.0, 37.5303);

-- 부산
-- 부산광역시 중구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2611000000', '부산광역시 중구', 97, 74, 129.0, 35.1138);
-- 부산광역시 서구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2614000000', '부산광역시 서구', 97, 74, 129.0, 35.1031);
-- 부산광역시 동구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2617000000', '부산광역시 동구', 98, 75, 129.0, 35.0807);
-- 부산광역시 영도구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2620000000', '부산광역시 영도구', 98, 74, 129.0, 35.0917);
-- 부산광역시 부산진구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2623000000', '부산광역시 부산진구', 97, 75, 129.0, 35.1595);
-- 부산광역시 동래구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2626000000', '부산광역시 동래구', 98, 76, 129.0, 35.2470);
-- 부산광역시 남구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2629000000', '부산광역시 남구', 98, 75, 129.0, 35.1473);
-- 부산광역시 북구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2632000000', '부산광역시 북구', 96, 76, 128.0, 35.2092);
-- 부산광역시 해운대구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2635000000', '부산광역시 해운대구', 99, 75, 129.0, 35.1588);
-- 부산광역시 사하구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2638000000', '부산광역시 사하구', 96, 74, 128.0, 35.0995);
-- 부산광역시 금정구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2641000000', '부산광역시 금정구', 98, 77, 129.0, 35.2399);
-- 부산광역시 강서구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2644000000', '부산광역시 강서구', 96, 76, 128.0, 35.1693);
-- 부산광역시 연제구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2647000000', '부산광역시 연제구', 98, 76, 129.0, 35.1709);
-- 부산광역시 수영구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2650000000', '부산광역시 수영구', 99, 75, 129.0, 35.1582);
-- 부산광역시 사상구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2653000000', '부산광역시 사상구', 96, 75, 128.0, 35.1399);
-- 부산광역시 기장군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2671000000', '부산광역시 기장군', 100, 77, 129.0, 35.2391);

-- 대구
-- 대구광역시 중구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2711000000', '대구광역시 중구', 89, 90, 128.0, 35.8725);
-- 대구광역시 동구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2714000000', '대구광역시 동구', 90, 91, 128.0, 35.8806);
-- 대구광역시 서구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2717000000', '대구광역시 서구', 88, 90, 128.0, 35.8564);
-- 대구광역시 남구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2720000000', '대구광역시 남구', 89, 90, 128.0, 35.8340);
-- 대구광역시 북구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2723000000', '대구광역시 북구', 89, 91, 128.0, 35.8934);
-- 대구광역시 수성구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2726000000', '대구광역시 수성구', 89, 90, 128.0, 35.8560);
-- 대구광역시 달서구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2729000000', '대구광역시 달서구', 88, 90, 128.0, 35.8537);
-- 대구광역시 달성군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2771000000', '대구광역시 달성군', 86, 88, 128.0, 35.7441);
-- 대구광역시 군위군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2772000000', '대구광역시 군위군', 88, 99, 128.0, 35.8481);

-- 인천
-- 인천광역시 중구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2811000000', '인천광역시 중구', 54, 125, 126.0, 37.4639);
-- 인천광역시 동구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2814000000', '인천광역시 동구', 54, 125, 126.0, 37.4600);
-- 인천광역시 미추홀구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2817700000', '인천광역시 미추홀구', 54, 124, 126.0, 37.4440);
-- 인천광역시 연수구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2818500000', '인천광역시 연수구', 55, 123, 126.0, 37.4423);
-- 인천광역시 남동구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2820000000', '인천광역시 남동구', 56, 124, 126.0, 37.4505);
-- 인천광역시 부평구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2823700000', '인천광역시 부평구', 55, 125, 126.0, 37.4926);
-- 인천광역시 계양구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2824500000', '인천광역시 계양구', 56, 126, 126.0, 37.5387);
-- 인천광역시 서구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2826000000', '인천광역시 서구', 55, 126, 126.0, 37.5903);
-- 인천광역시 강화군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2871000000', '인천광역시 강화군', 51, 130, 126.0, 37.7383);
-- 인천광역시 옹진군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2872000000', '인천광역시 옹진군', 54, 124, 126.0, 37.7171);

-- 광주
-- 광주광역시 동구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2911000000', '광주광역시 동구', 60, 74, 126.0, 35.1530);
-- 광주광역시 서구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2914000000', '광주광역시 서구', 59, 74, 126.0, 35.1495);
-- 광주광역시 남구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2915500000', '광주광역시 남구', 59, 73, 126.0, 35.1460);
-- 광주광역시 북구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2917000000', '광주광역시 북구', 59, 75, 126.0, 35.1760);
-- 광주광역시 광산구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor2920000000', '광주광역시 광산구', 57, 74, 126.0, 35.2210);

-- 대전
-- 대전광역시 동구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor3011000000', '대전광역시 동구', 68, 100, 127.0, 36.3502);
-- 대전광역시 중구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor3014000000', '대전광역시 중구', 68, 100, 127.0, 36.3246);
-- 대전광역시 서구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor3017000000', '대전광역시 서구', 67, 100, 127.0, 36.3002);
-- 대전광역시 유성구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor3020000000', '대전광역시 유성구', 67, 101, 127.0, 36.3735);
-- 대전광역시 대덕구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor3023000000', '대전광역시 대덕구', 68, 100, 127.0, 36.3826);

-- 울산
-- 울산광역시 중구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor3111000000', '울산광역시 중구', 102, 84, 129.0, 35.5391);
-- 울산광역시 남구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor3114000000', '울산광역시 남구', 102, 84, 129.0, 35.5413);
-- 울산광역시 동구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor3117000000', '울산광역시 동구', 104, 83, 129.0, 35.4442);
-- 울산광역시 북구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor3120000000', '울산광역시 북구', 103, 85, 129.0, 35.5651);
-- 울산광역시 울주군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor3171000000', '울산광역시 울주군', 101, 84, 129.0, 35.5011);

-- 세종
-- 세종특별자치시 조치원읍
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor3611025000', '세종특별자치시 조치원읍', 66, 106, 127.0, 36.5232);
-- 세종특별자치시 연기면
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor3611031000', '세종특별자치시 연기면', 65, 105, 127.0, 36.5315);
-- 세종특별자치시 소정면
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor3611039000', '세종특별자치시 소정면', 63, 108, 127.0, 36.4756);
-- 세종특별자치시 세종시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor3611000000', '세종특별자치시 세종시', 66, 103, 127.0, 36.4823);


-- 경기
-- 경기도 수원시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4111100000', '경기도 수원시', 60, 121, 127.0, 37.2748);
-- 경기도 성남시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4113100000', '경기도 성남시', 63, 124, 127.0, 37.4484);
-- 경기도 의정부시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4115000000', '경기도 의정부시', 61, 130, 127.0, 37.7381);
-- 경기도 안양시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4117100000', '경기도 안양시', 59, 123, 126.0, 37.3925);
-- 경기도 부천시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4119200000', '경기도 부천시', 57, 125, 126.0, 37.4835);
-- 경기도 광명시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4121000000', '경기도 광명시', 58, 125, 126.0, 37.4762);
-- 경기도 평택시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4122000000', '경기도 평택시', 62, 114, 127.0, 37.0093);
-- 경기도 동두천시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4125000000', '경기도 동두천시', 61, 134, 127.0, 37.9012);
-- 경기도 안산시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4127100000', '경기도 안산시', 58, 121, 126.0, 37.3286);
-- 경기도 고양시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4128100000', '경기도 고양시', 57, 128, 126.0, 37.6763);
-- 경기도 과천시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4129000000', '경기도 과천시', 60, 124, 126.0, 37.4428);
-- 경기도 구리시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4131000000', '경기도 구리시', 62, 127, 127.0, 37.6135);
-- 경기도 남양주시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4136000000', '경기도 남양주시', 64, 128, 127.0, 37.6435);
-- 경기도 오산시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4137000000', '경기도 오산시', 62, 118, 127.0, 37.1427);
-- 경기도 시흥시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4139000000', '경기도 시흥시', 57, 123, 126.0, 37.4089);
-- 경기도 군포시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4141000000', '경기도 군포시', 59, 122, 126.0, 37.3582);
-- 경기도 의왕시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4143000000', '경기도 의왕시', 60, 122, 126.0, 37.3373);
-- 경기도 하남시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4145000000', '경기도 하남시', 64, 126, 127.0, 37.5418);
-- 경기도 용인시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4146100000', '경기도 용인시', 64, 119, 127.0, 37.2417);
-- 경기도 파주시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4148000000', '경기도 파주시', 56, 131, 126.0, 37.7481);
-- 경기도 이천시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4150000000', '경기도 이천시', 68, 121, 127.0, 37.2743);
-- 경기도 안성시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4155000000', '경기도 안성시', 65, 115, 127.0, 37.0089);
-- 경기도 김포시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4157000000', '경기도 김포시', 55, 128, 126.0, 37.6289);
-- 경기도 화성시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4159000000', '경기도 화성시', 57, 119, 126.0, 37.2573);
-- 경기도 광주시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4161000000', '경기도 광주시', 65, 123, 127.0, 37.4135);
-- 경기도 양주시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4163000000', '경기도 양주시', 61, 131, 127.0, 37.7485);
-- 경기도 포천시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4165000000', '경기도 포천시', 64, 134, 127.0, 37.8912);
-- 경기도 여주시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4167000000', '경기도 여주시', 71, 121, 127.0, 37.2963);
-- 경기도 연천군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4180000000', '경기도 연천군', 61, 138, 127.0, 37.9028);
-- 경기도 가평군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4182000000', '경기도 가평군', 69, 133, 127.0, 37.8342);
-- 경기도 양평군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4183000000', '경기도 양평군', 69, 125, 127.0, 37.4718);

-- 충청북도
-- 충청북도 청주시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4311100000', '충청북도 청주시', 69, 106, 127.0, 36.6423);
-- 충청북도 충주시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4313000000', '충청북도 충주시', 76, 114, 127.0, 36.9783);
-- 충청북도 제천시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4315000000', '충청북도 제천시', 81, 118, 128.0, 37.1294);
-- 충청북도 보은군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4372000000', '충청북도 보은군', 73, 103, 127.0, 36.4842);
-- 충청북도 옥천군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4373000000', '충청북도 옥천군', 71, 99, 127.0, 36.3012);
-- 충청북도 영동군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4374000000', '충청북도 영동군', 74, 97, 127.0, 36.2292);
-- 충청북도 증평군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4374500000', '충청북도 증평군', 71, 110, 127.0, 36.6879);
-- 충청북도 진천군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4375000000', '충청북도 진천군', 68, 111, 127.0, 36.7553);
-- 충청북도 괴산군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4376000000', '충청북도 괴산군', 74, 111, 127.0, 36.7578);
-- 충청북도 음성군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4377000000', '충청북도 음성군', 72, 113, 127.0, 37.0178);
-- 충청북도 단양군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4380000000', '충청북도 단양군', 84, 115, 128.0, 37.1192);

-- 충청남도
-- 충청남도 천안시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4413100000', '충청남도 천안시', 63, 110, 127.0, 36.8036);
-- 충청남도 공주시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4415000000', '충청남도 공주시', 63, 102, 127.0, 36.4879);
-- 충청남도 보령시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4418000000', '충청남도 보령시', 54, 100, 126.0, 36.3416);
-- 충청남도 아산시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4420000000', '충청남도 아산시', 60, 110, 127.0, 36.7882);
-- 충청남도 서산시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4421000000', '충청남도 서산시', 51, 110, 126.0, 36.7867);
-- 충청남도 논산시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4423000000', '충청남도 논산시', 62, 97, 127.0, 36.1775);
-- 충청남도 계룡시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4425000000', '충청남도 계룡시', 65, 99, 127.0, 36.2922);
-- 충청남도 당진시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4427000000', '충청남도 당진시', 54, 112, 126.0, 36.9703);
-- 충청남도 금산군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4471000000', '충청남도 금산군', 69, 95, 127.0, 36.1319);
-- 충청남도 부여군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4476000000', '충청남도 부여군', 59, 99, 126.0, 36.2938);
-- 충청남도 서천군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4477000000', '충청남도 서천군', 55, 94, 126.0, 36.0917);
-- 충청남도 청양군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4479000000', '충청남도 청양군', 57, 103, 126.0, 36.4482);
-- 충청남도 홍성군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4480000000', '충청남도 홍성군', 55, 106, 126.0, 36.6754);
-- 충청남도 예산군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4481000000', '충청남도 예산군', 58, 107, 126.0, 36.7406);
-- 충청남도 태안군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4482500000', '충청남도 태안군', 48, 109, 126.0, 36.7527);


-- 전라남도
-- 전라남도 목포시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4611000000', '전라남도 목포시', 50, 67, 126.0, 34.8111);
-- 전라남도 여수시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4613000000', '전라남도 여수시', 73, 66, 127.0, 34.7603);
-- 전라남도 순천시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4615000000', '전라남도 순천시', 70, 70, 127.0, 34.9487);
-- 전라남도 나주시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4617000000', '전라남도 나주시', 56, 71, 126.0, 35.0202);
-- 전라남도 광양시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4623000000', '전라남도 광양시', 73, 70, 127.0, 34.9307);
-- 전라남도 담양군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4671000000', '전라남도 담양군', 61, 78, 126.0, 35.3451);
-- 전라남도 곡성군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4672000000', '전라남도 곡성군', 66, 77, 127.0, 35.2053);
-- 전라남도 구례군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4673000000', '전라남도 구례군', 69, 75, 127.0, 35.1227);
-- 전라남도 고흥군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4677000000', '전라남도 고흥군', 66, 62, 127.0, 34.5883);
-- 전라남도 보성군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4678000000', '전라남도 보성군', 62, 66, 127.0, 34.7527);
-- 전라남도 화순군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4679000000', '전라남도 화순군', 61, 72, 126.0, 35.0355);
-- 전라남도 장흥군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4680000000', '전라남도 장흥군', 59, 64, 126.0, 34.6425);
-- 전라남도 강진군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4681000000', '전라남도 강진군', 57, 63, 126.0, 34.6501);
-- 전라남도 해남군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4682000000', '전라남도 해남군', 54, 61, 126.0, 34.5796);
-- 전라남도 영암군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4683000000', '전라남도 영암군', 56, 66, 126.0, 34.7228);
-- 전라남도 무안군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4684000000', '전라남도 무안군', 52, 71, 126.0, 34.9713);
-- 전라남도 함평군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4686000000', '전라남도 함평군', 52, 72, 126.0, 34.8252);
-- 전라남도 영광군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4687000000', '전라남도 영광군', 52, 77, 126.0, 35.2707);
-- 전라남도 장성군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4688000000', '전라남도 장성군', 57, 77, 126.0, 35.2328);
-- 전라남도 완도군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4689000000', '전라남도 완도군', 57, 56, 126.0, 34.3457);
-- 전라남도 진도군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4690000000', '전라남도 진도군', 48, 59, 126.0, 34.4874);
-- 전라남도 신안군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4691000000', '전라남도 신안군', 50, 66, 126.0, 34.8540);


-- 전라북도
-- 전북특별자치도 전주시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5211100000', '전북특별자치도 전주시', 63, 89, 127.0, 35.8189);
-- 전북특별자치도 군산시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5213000000', '전북특별자치도 군산시', 56, 92, 126.0, 35.7151);
-- 전북특별자치도 익산시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5214000000', '전북특별자치도 익산시', 60, 91, 126.0, 35.9467);
-- 전북특별자치도 정읍시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5218000000', '전북특별자치도 정읍시', 58, 83, 126.0, 35.5994);
-- 전북특별자치도 남원시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5219000000', '전북특별자치도 남원시', 68, 80, 127.0, 35.3803);
-- 전북특별자치도 김제시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5221000000', '전북특별자치도 김제시', 59, 88, 126.0, 35.7795);
-- 전북특별자치도 완주군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5271000000', '전북특별자치도 완주군', 63, 89, 127.0, 35.3908);
-- 전북특별자치도 진안군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5272000000', '전북특별자치도 진안군', 68, 88, 127.0, 35.6700);
-- 전북특별자치도 무주군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5273000000', '전북특별자치도 무주군', 72, 93, 127.0, 35.8780);
-- 전북특별자치도 장수군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5274000000', '전북특별자치도 장수군', 70, 85, 127.0, 35.5540);
-- 전북특별자치도 임실군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5275000000', '전북특별자치도 임실군', 66, 84, 127.0, 35.5807);
-- 전북특별자치도 순창군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5277000000', '전북특별자치도 순창군', 63, 79, 127.0, 35.4422);
-- 전북특별자치도 고창군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5279000000', '전북특별자치도 고창군', 56, 80, 126.0, 35.3597);
-- 전북특별자치도 부안군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5280000000', '전북특별자치도 부안군', 56, 87, 126.0, 35.7233);


-- 경상북도
-- 경상북도 포항시 남구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4711100000', '경상북도 포항시', 102, 94, 129.0, 36.0227);
-- 경상북도 경주시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4713000000', '경상북도 경주시', 100, 91, 129.0, 35.8505);
-- 경상북도 김천시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4715000000', '경상북도 김천시', 80, 96, 128.0, 36.1427);
-- 경상북도 안동시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4717000000', '경상북도 안동시', 91, 106, 128.0, 36.5664);
-- 경상북도 구미시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4719000000', '경상북도 구미시', 84, 96, 128.0, 36.1163);
-- 경상북도 영주시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4721000000', '경상북도 영주시', 89, 111, 128.0, 36.8554);
-- 경상북도 영천시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4723000000', '경상북도 영천시', 95, 93, 128.0, 35.9543);
-- 경상북도 상주시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4725000000', '경상북도 상주시', 81, 102, 128.0, 36.4344);
-- 경상북도 문경시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4728000000', '경상북도 문경시', 81, 106, 128.0, 36.5977);
-- 경상북도 경산시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4729000000', '경상북도 경산시', 91, 90, 128.0, 35.7997);
-- 경상북도 의성군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4773000000', '경상북도 의성군', 90, 101, 128.0, 36.2001);
-- 경상북도 청송군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4775000000', '경상북도 청송군', 96, 103, 129.0, 36.3639);
-- 경상북도 영양군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4776000000', '경상북도 영양군', 97, 108, 129.0, 36.3197);
-- 경상북도 영덕군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4777000000', '경상북도 영덕군', 102, 103, 129.0, 36.4442);
-- 경상북도 청도군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4782000000', '경상북도 청도군', 91, 86, 128.0, 35.6901);
-- 경상북도 고령군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4783000000', '경상북도 고령군', 83, 87, 128.0, 35.5732);
-- 경상북도 성주군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4784000000', '경상북도 성주군', 83, 91, 128.0, 35.7969);
-- 경상북도 칠곡군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4785000000', '경상북도 칠곡군', 85, 93, 128.0, 35.9883);
-- 경상북도 예천군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4790000000', '경상북도 예천군', 86, 107, 128.0, 36.6138);
-- 경상북도 봉화군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4792000000', '경상북도 봉화군', 90, 113, 128.0, 36.9023);
-- 경상북도 울진군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4793000000', '경상북도 울진군', 102, 115, 129.0, 36.9913);
-- 경상북도 울릉군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4794000000', '경상북도 울릉군', 127, 127, 130.0, 37.4667);

-- 경상남도
-- 경상남도 마산시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4812500000', '경상남도 마산시', 89, 76, 128.0, 35.2100);
-- 경상남도 창원시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4813000000', '경상남도 창원시', 88, 78, 128.0, 35.2280);
-- 경상남도 진주시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4815000000', '경상남도 진주시', 90, 83, 128.0, 35.1810);
-- 경상남도 김해시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4817000000', '경상남도 김해시', 89, 80, 128.0, 35.2323);
-- 경상남도 밀양시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4819000000', '경상남도 밀양시', 91, 82, 128.0, 35.4855);
-- 경상남도 통영시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4820000000', '경상남도 통영시', 91, 70, 128.0, 34.8491);
-- 경상남도 사천시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4821000000', '경상남도 사천시', 93, 75, 128.0, 34.9701);
-- 경상남도 진해시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4823000000', '경상남도 진해시', 87, 77, 128.0, 35.1075);
-- 경상남도 거제시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4825000000', '경상남도 거제시', 94, 70, 128.0, 34.8813);
-- 경상남도 양산시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4826000000', '경상남도 양산시', 90, 79, 128.0, 35.3384);
-- 경상남도 하동군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4871000000', '경상남도 하동군', 95, 79, 128.0, 35.0997);
-- 경상남도 거창군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4872000000', '경상남도 거창군', 87, 84, 128.0, 35.5874);
-- 경상남도 합천군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4873000000', '경상남도 합천군', 88, 89, 128.0, 35.5637);
-- 경상남도 창녕군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4874000000', '경상남도 창녕군', 92, 88, 128.0, 35.2902);
-- 경상남도 함안군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4875000000', '경상남도 함안군', 90, 85, 128.0, 35.1983);
-- 경상남도 산청군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4876000000', '경상남도 산청군', 94, 85, 128.0, 35.3289);
-- 경상남도 의령군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4877000000', '경상남도 의령군', 90, 91, 128.0, 35.3450);
-- 경상남도 남해군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4878000000', '경상남도 남해군', 93, 68, 128.0, 34.8445);
-- 경상남도 창원시 의창구
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor4812000000', '경상남도 창원시 의창구', 88, 77, 128.0, 35.2364);


-- 제주도
-- 제주특별자치도 제주시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5011000000', '제주특별자치도 제주시', 53, 38, 126.0, 33.4996);
-- 제주특별자치도 서귀포시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5013000000', '제주특별자치도 서귀포시', 52, 33, 126.0, 33.2556);

-- 강원도
-- 강원특별자치도 춘천시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5111000000', '강원특별자치도 춘천시', 73, 134, 127.0, 37.8836);
-- 강원특별자치도 원주시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5113000000', '강원특별자치도 원주시', 76, 122, 127.0, 37.3439);
-- 강원특별자치도 강릉시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5115000000', '강원특별자치도 강릉시', 92, 131, 128.0, 37.7519);
-- 강원특별자치도 동해시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5117000000', '강원특별자치도 동해시', 97, 127, 129.0, 37.5207);
-- 강원특별자치도 태백시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5119000000', '강원특별자치도 태백시', 95, 119, 128.0, 37.1547);
-- 강원특별자치도 속초시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5121000000', '강원특별자치도 속초시', 87, 141, 128.0, 38.2073);
-- 강원특별자치도 삼척시
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5123000000', '강원특별자치도 삼척시', 98, 125, 129.0, 38.4285);
-- 강원특별자치도 홍천군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5172000000', '강원특별자치도 홍천군', 75, 130, 127.0, 37.6556);
-- 강원특별자치도 횡성군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5173000000', '강원특별자치도 횡성군', 77, 125, 127.0, 37.4533);
-- 강원특별자치도 영월군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5175000000', '강원특별자치도 영월군', 86, 119, 128.0, 37.1925);
-- 강원특별자치도 평창군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5176000000', '강원특별자치도 평창군', 84, 123, 128.0, 37.4375);
-- 강원특별자치도 정선군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5177000000', '강원특별자치도 정선군', 89, 123, 128.0, 37.4608);
-- 강원특별자치도 철원군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5178000000', '강원특별자치도 철원군', 65, 139, 127.0, 38.2000);
-- 강원특별자치도 화천군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5179000000', '강원특별자치도 화천군', 72, 139, 127.0, 38.1180);
-- 강원특별자치도 양구군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5180000000', '강원특별자치도 양구군', 77, 139, 127.0, 38.1150);
-- 강원특별자치도 인제군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5181000000', '강원특별자치도 인제군', 80, 138, 128.0, 38.1333);
-- 강원특별자치도 고성군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5182000000', '강원특별자치도 고성군', 85, 145, 128.0, 38.3925);
-- 강원특별자치도 양양군
INSERT INTO weather_coordinate (kor_code, area_name, grid_x, grid_y, longitude, latitude)
VALUES ('kor5183000000', '강원특별자치도 양양군', 88, 138, 128.0, 38.0722);



commit;

select * from weather_coordinate;


