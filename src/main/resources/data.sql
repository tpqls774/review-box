INSERT INTO movies (id, title, year, poster_url, summary, director, rating, runtime)
VALUES (1116465,
        '성룡의 전설',
        2024,
        'https://image.tmdb.org/t/p/w500/ju8tnsveoxWymYx3jQRXNX93pUY.jpg',
        '고고학자 첸은 학생들과 발굴 작업 중 ... 운명의 소용돌이에 휩싸이는데…',
        '당계례',
        6.745,
        129);

INSERT INTO movie_genres (movie_id, genre)
VALUES (1116465, 'ACTION'),
       (1116465, 'FANTASY'),
       (1116465, 'ADVENTURE');

-- 비밀번호: password123
INSERT INTO users (id, email, username, password, created_at, updated_at)
VALUES (1,
        'tester@example.com',
        'tester',
        '$2a$10$DIldThbVDQ41clPHKfc8JupgOJrSyTy2Q5d32lh8GsCwhy4sXqwp2',
        NOW(),
        NOW());

INSERT INTO reviews (id, title, content, movie_id, user_id, created_at, updated_at)
VALUES (1, '재밌고 액션 좋음', '성룡 액션은 역시 믿고 보는 느낌.', 1116465, 1, NOW(), NOW()),
       (2, '스토리는 평범함', '스토리는 뻔했지만 볼만했음.', 1116465, 1, NOW(), NOW()),
       (3, 'CG 멋졌다', '생각보다 CG 퀄리티가 좋았다.', 1116465, 1, NOW(), NOW()),
       (4, 'OST 좋음', '음악이 생각보다 좋아서 집중됨.', 1116465, 1, NOW(), NOW()),
       (5, '아이랑 보기 좋음', '가볍게 보기 좋은 가족영화 느낌.', 1116465, 1, NOW(), NOW());
