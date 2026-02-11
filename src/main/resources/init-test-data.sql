-- ============================================================
-- init-test-data-non-recommend.sql
-- 실행 순서:
--   1) init-tags.sql
--   2) this file
-- MySQL 8.0+
-- ============================================================

START TRANSACTION;

-- ============================================================
-- 0) helper sequence tables (reopen-safe)
-- ============================================================
DROP TEMPORARY TABLE IF EXISTS tmp_seq;
DROP TEMPORARY TABLE IF EXISTS tmp_seq_b;
DROP TEMPORARY TABLE IF EXISTS tmp_seq_c;

CREATE TEMPORARY TABLE tmp_seq (
                                   n INT PRIMARY KEY
);

INSERT INTO tmp_seq (n)
WITH RECURSIVE seq AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1
    FROM seq
    WHERE n < 300
)
SELECT n
FROM seq;

CREATE TEMPORARY TABLE tmp_seq_b AS
SELECT n FROM tmp_seq;
ALTER TABLE tmp_seq_b ADD PRIMARY KEY (n);

CREATE TEMPORARY TABLE tmp_seq_c AS
SELECT n FROM tmp_seq;
ALTER TABLE tmp_seq_c ADD PRIMARY KEY (n);

-- ============================================================
-- 1) cleanup (NON-RECOMMEND only)
-- ============================================================
DELETE FROM `enrollment` WHERE `id` BETWEEN 150001 AND 151000;

DELETE FROM `lecture` WHERE `id` BETWEEN 140001 AND 143500;
DELETE FROM `section` WHERE `id` BETWEEN 130001 AND 130600;
DELETE FROM `course_tags` WHERE `course_id` BETWEEN 120001 AND 120100;
DELETE FROM `course` WHERE `id` BETWEEN 120001 AND 120100;

DELETE FROM `user_roles` WHERE `user_id` BETWEEN 110001 AND 110100;
DELETE FROM `user_tags` WHERE `user_id` BETWEEN 110001 AND 110100;
DELETE FROM `users` WHERE `id` BETWEEN 110001 AND 110100;

-- ============================================================
-- 2) users (70 rows)
-- - learner 56 / instructor 10 / admin 2 / cs 2
-- ============================================================
INSERT INTO `users`
(`id`, `email`, `password`, `name`, `level`, `created_at`, `updated_at`)
SELECT
    110000 + n AS id,
    CASE
        WHEN n <= 56 THEN CONCAT('learner', LPAD(n, 2, '0'), '@lxp.test')
        WHEN n <= 66 THEN CONCAT('instructor', LPAD(n - 56, 2, '0'), '@lxp.test')
        WHEN n <= 68 THEN CONCAT('admin', LPAD(n - 66, 2, '0'), '@lxp.test')
        ELSE CONCAT('cs', LPAD(n - 68, 2, '0'), '@lxp.test')
        END AS email,
    '$2a$10$N9qo8uLOickgx2ZMRZo5e.ulB9JH7wW8fQ6n3w0xP2Lh7d1jQ9R6G' AS password,
    CASE
        WHEN n <= 56 THEN CONCAT('Learner ', LPAD(n, 2, '0'))
        WHEN n <= 66 THEN CONCAT('Instructor ', LPAD(n - 56, 2, '0'))
        WHEN n <= 68 THEN CONCAT('Admin ', LPAD(n - 66, 2, '0'))
        ELSE CONCAT('CS ', LPAD(n - 68, 2, '0'))
        END AS name,
    CASE
        WHEN n <= 18 THEN 'JUNIOR'
        WHEN n <= 38 THEN 'MIDDLE'
        WHEN n <= 63 THEN 'SENIOR'
        ELSE 'EXPERT'
        END AS level,
    TIMESTAMP('2025-09-01 09:00:00') + INTERVAL n DAY AS created_at,
    TIMESTAMP('2025-09-01 09:00:00') + INTERVAL n DAY + INTERVAL MOD(n, 15) DAY AS updated_at
FROM tmp_seq
WHERE n <= 70;

-- user_roles: 75 rows
INSERT INTO `user_roles` (`user_id`, `role`)
SELECT 110000 + n, 'LEARNER'
FROM tmp_seq
WHERE n <= 56;

INSERT INTO `user_roles` (`user_id`, `role`)
SELECT 110000 + n, 'INSTRUCTOR'
FROM tmp_seq
WHERE n BETWEEN 57 AND 66;

INSERT INTO `user_roles` (`user_id`, `role`)
SELECT 110000 + n, 'LEARNER'
FROM tmp_seq
WHERE n BETWEEN 57 AND 66
  AND MOD(n, 3) = 0;

INSERT INTO `user_roles` (`user_id`, `role`)
SELECT 110000 + n, 'ADMIN'
FROM tmp_seq
WHERE n BETWEEN 67 AND 68;

INSERT INTO `user_roles` (`user_id`, `role`)
VALUES (110067, 'CS_MANAGER');

INSERT INTO `user_roles` (`user_id`, `role`)
SELECT 110000 + n, 'CS_MANAGER'
FROM tmp_seq
WHERE n BETWEEN 69 AND 70;

-- user_tags: 248 rows (3/4/5개 편향)
-- tag_id는 init-tags.sql 생성 결과(1~104)를 전제
INSERT INTO `user_tags` (`user_id`, `tag_id`)
SELECT 110000 + n, 1 + MOD(n * 3, 20)
FROM tmp_seq
WHERE n <= 70;

INSERT INTO `user_tags` (`user_id`, `tag_id`)
SELECT 110000 + n, 21 + MOD(n * 5, 25)
FROM tmp_seq
WHERE n <= 70;

INSERT INTO `user_tags` (`user_id`, `tag_id`)
SELECT 110000 + n, 46 + MOD(n * 7, 25)
FROM tmp_seq
WHERE n <= 70;

INSERT INTO `user_tags` (`user_id`, `tag_id`)
SELECT 110000 + n, 71 + MOD(n * 11, 34)
FROM tmp_seq
WHERE n <= 28;

INSERT INTO `user_tags` (`user_id`, `tag_id`)
SELECT 110000 + n, 21 + MOD(n * 5 + 7, 25)
FROM tmp_seq
WHERE n <= 10;

-- ============================================================
-- 3) course / section / lecture
-- course 50 / course_tags 176 / section 72 / lecture 183
-- ============================================================
INSERT INTO `course`
(`id`, `instructor_id`, `created_at`, `updated_at`, `title`, `description`, `thumbnail_url`, `difficulty`)
SELECT
    120000 + n AS id,
    CASE
        WHEN n <= 18 THEN 110057
        WHEN n <= 30 THEN 110058
        WHEN n <= 38 THEN 110059
        WHEN n <= 44 THEN 110060
        WHEN n <= 47 THEN 110061
        WHEN n <= 49 THEN 110062
        ELSE 110063
        END AS instructor_id,
    TIMESTAMP('2025-10-01 10:00:00') + INTERVAL n DAY AS created_at,
    TIMESTAMP('2025-10-01 10:00:00') + INTERVAL n DAY + INTERVAL MOD(n, 20) DAY AS updated_at,
    CONCAT(
            CASE
                WHEN n <= 8 THEN 'Hot Track '
                WHEN n <= 25 THEN 'Core Track '
                ELSE 'Long-tail Track '
                END,
            LPAD(n, 2, '0')
    ) AS title,
    CONCAT('Synthetic course ', n, ' for skewed test data') AS description,
    CONCAT('https://cdn.lxp.test/course/', LPAD(n, 2, '0'), '.jpg') AS thumbnail_url,
    CASE
        WHEN MOD(n, 17) = 0 THEN NULL
        WHEN n <= 16 THEN 'JUNIOR'
        WHEN n <= 33 THEN 'MIDDLE'
        WHEN n <= 45 THEN 'SENIOR'
        ELSE 'EXPERT'
        END AS difficulty
FROM tmp_seq
WHERE n <= 50;

INSERT INTO `course_tags` (`course_id`, `tag_order`, `tag_id`)
SELECT 120000 + n, 0, 1 + MOD(n * 2, 20)
FROM tmp_seq
WHERE n <= 50;

INSERT INTO `course_tags` (`course_id`, `tag_order`, `tag_id`)
SELECT 120000 + n, 1, 21 + MOD(n * 3, 25)
FROM tmp_seq
WHERE n <= 50;

INSERT INTO `course_tags` (`course_id`, `tag_order`, `tag_id`)
SELECT 120000 + n, 2, 46 + MOD(n * 5, 25)
FROM tmp_seq
WHERE n <= 50;

INSERT INTO `course_tags` (`course_id`, `tag_order`, `tag_id`)
SELECT 120000 + n, 3, 71 + MOD(n * 7, 34)
FROM tmp_seq
WHERE n <= 20;

INSERT INTO `course_tags` (`course_id`, `tag_order`, `tag_id`)
SELECT 120000 + n, 4, 21 + MOD(n * 3 + 9, 25)
FROM tmp_seq
WHERE n <= 6;

-- sections: heavy / medium / light / zero 분포
INSERT INTO `section` (`id`, `title`, `sort_order`, `course_id`)
SELECT
    130000 + (c.n - 1) * 10 + s.n AS id,
    CONCAT('Section ', s.n, ' / C', LPAD(c.n, 2, '0')) AS title,
    s.n AS sort_order,
    120000 + c.n AS course_id
FROM tmp_seq c
         JOIN tmp_seq_b s ON s.n <= 6
WHERE c.n <= 50
  AND s.n <= CASE
                 WHEN c.n <= 4 THEN 6
                 WHEN c.n <= 15 THEN 3
                 WHEN c.n <= 30 THEN 1
                 ELSE 0
    END;

-- lectures: 일부 null/0 duration, 일부 null video
INSERT INTO `lecture` (`id`, `title`, `duration_seconds`, `sort_order`, `video_url`, `section_id`)
SELECT
    z.lecture_id AS id,
    CONCAT('Lecture ', z.lecture_order, ' / C', LPAD(z.course_n, 2, '0'), '-S', z.section_order) AS title,
    CASE
        WHEN MOD(z.lecture_id, 37) = 0 THEN NULL
        WHEN MOD(z.lecture_id, 53) = 0 THEN 0
        ELSE 600 + MOD(z.lecture_id, 9) * 300
        END AS duration_seconds,
    z.lecture_order AS sort_order,
    CASE
        WHEN MOD(z.lecture_id, 8) = 0 THEN NULL
        ELSE CONCAT('https://video.lxp.test/v/', z.lecture_id)
        END AS video_url,
    z.section_id AS section_id
FROM (
         SELECT
             c.n AS course_n,
             s.n AS section_order,
             l.n AS lecture_order,
             130000 + (c.n - 1) * 10 + s.n AS section_id,
             140000 + (c.n - 1) * 100 + s.n * 10 + l.n AS lecture_id
         FROM tmp_seq c
                  JOIN tmp_seq_b s ON s.n <= 6
                  JOIN tmp_seq_c l ON l.n <= 6
         WHERE c.n <= 30
           AND s.n <= CASE
                          WHEN c.n <= 4 THEN 6
                          WHEN c.n <= 15 THEN 3
                          ELSE 1
             END
           AND l.n <= CASE
                          WHEN c.n <= 4 THEN
                              CASE
                                  WHEN s.n = 1 THEN 5
                                  WHEN s.n IN (2, 3) THEN 4
                                  WHEN s.n = 4 THEN 3
                                  WHEN s.n = 5 THEN 2
                                  ELSE 1
                                  END
                          WHEN c.n <= 15 THEN
                              CASE WHEN s.n = 1 THEN 3 ELSE 2 END
                          ELSE 2
             END
     ) z;

-- ============================================================
-- 4) enrollment (108 rows) with skewed activity
-- ============================================================
DROP TEMPORARY TABLE IF EXISTS tmp_enrollment_seed;
CREATE TEMPORARY TABLE tmp_enrollment_seed (
                                               user_n INT NOT NULL,
                                               slot_n INT NOT NULL,
                                               course_n INT NOT NULL,
                                               PRIMARY KEY (user_n, slot_n)
);

-- high activity: 8 users * 5
INSERT INTO tmp_enrollment_seed (`user_n`, `slot_n`, `course_n`)
SELECT
    u.n,
    s.n,
    CASE s.n
        WHEN 1 THEN 1 + MOD(u.n, 5)
        WHEN 2 THEN 6 + MOD(u.n, 10)
        WHEN 3 THEN 16 + MOD(u.n, 10)
        WHEN 4 THEN 26 + MOD(u.n, 5)
        ELSE 31 + MOD(u.n, 5)
        END AS course_n
FROM tmp_seq u
         JOIN tmp_seq_b s ON s.n <= 5
WHERE u.n BETWEEN 1 AND 8;

-- medium activity: 20 users * 2
INSERT INTO tmp_enrollment_seed (`user_n`, `slot_n`, `course_n`)
SELECT
    u.n,
    s.n,
    CASE s.n
        WHEN 1 THEN 1 + MOD(u.n, 8)
        ELSE 11 + MOD(u.n, 15)
        END AS course_n
FROM tmp_seq u
         JOIN tmp_seq_b s ON s.n <= 2
WHERE u.n BETWEEN 9 AND 28;

-- low activity: 28 users * 1
INSERT INTO tmp_enrollment_seed (`user_n`, `slot_n`, `course_n`)
SELECT
    u.n,
    1 AS slot_n,
    1 + MOD(u.n, 12) AS course_n
FROM tmp_seq u
WHERE u.n BETWEEN 29 AND 56;

INSERT INTO `enrollment`
(`id`, `user_id`, `course_id`, `enrollment_status`, `enrolled_at`, `learning_started_at`,
 `completed_at`, `deleted_at`, `cancelled_at`, `cancel_type`, `cancel_reason_type`,
 `cancel_reason_comment`, `version`)
SELECT
    150000 + e.rn AS id,
    110000 + e.user_n AS user_id,
    120000 + e.course_n AS course_id,
    CASE
        WHEN MOD(e.rn, 10) IN (0, 1, 2) THEN 'ENROLLED'
        WHEN MOD(e.rn, 10) IN (3, 4, 5, 6) THEN 'IN_PROGRESS'
        WHEN MOD(e.rn, 10) IN (7, 8) THEN 'COMPLETED'
        ELSE 'CANCELLED'
        END AS enrollment_status,
    e.enrolled_at AS enrolled_at,
    CASE
        WHEN MOD(e.rn, 10) IN (0, 1, 2) THEN NULL
        WHEN MOD(e.rn, 10) = 9 AND MOD(e.rn, 4) = 0 THEN NULL
        ELSE e.enrolled_at + INTERVAL 1 DAY
        END AS learning_started_at,
    CASE
        WHEN MOD(e.rn, 10) IN (7, 8) THEN e.enrolled_at + INTERVAL (7 + MOD(e.rn, 15)) DAY
        ELSE NULL
        END AS completed_at,
    CASE
        WHEN MOD(e.rn, 23) = 0 AND MOD(e.rn, 10) <> 9 THEN e.enrolled_at + INTERVAL 3 DAY
        ELSE NULL
        END AS deleted_at,
    CASE
        WHEN MOD(e.rn, 10) = 9 THEN
            CASE
                WHEN MOD(e.rn, 4) = 0 THEN e.enrolled_at + INTERVAL 2 DAY
                ELSE e.enrolled_at + INTERVAL 4 DAY
                END
        ELSE NULL
        END AS cancelled_at,
    CASE
        WHEN MOD(e.rn, 10) = 9 THEN
            CASE MOD(e.rn, 5)
                WHEN 0 THEN 'SELF_SERVICE'
                WHEN 1 THEN 'CS_SERVICE'
                WHEN 2 THEN 'ADMIN_PENALTY'
                WHEN 3 THEN 'COURSE_UNAVAILABLE'
                ELSE 'PAYMENT_REVERSAL'
                END
        ELSE NULL
        END AS cancel_type,
    CASE
        WHEN MOD(e.rn, 10) = 9 THEN
            CASE MOD(e.rn, 6)
                WHEN 0 THEN 'BETTER_ALTERNATIVE_EXISTS'
                WHEN 1 THEN 'TECHNICAL_ISSUE'
                WHEN 2 THEN 'POLICY_VIOLATION'
                WHEN 3 THEN 'NO_LONGER_NEEDED'
                WHEN 4 THEN 'FRAUD_SUSPECTED'
                ELSE 'OTHER'
                END
        ELSE NULL
        END AS cancel_reason_type,
    CASE
        WHEN MOD(e.rn, 10) = 9 AND MOD(e.rn, 6) = 5 THEN CONCAT('custom cancel note #', e.rn)
        WHEN MOD(e.rn, 10) = 9 AND MOD(e.rn, 6) = 2 THEN 'policy violation confirmed'
        ELSE NULL
        END AS cancel_reason_comment,
    0 AS version
FROM (
         SELECT
             user_n,
             slot_n,
             course_n,
             ROW_NUMBER() OVER (ORDER BY user_n, slot_n) AS rn,
             TIMESTAMP('2026-01-01 09:00:00')
                 + INTERVAL MOD(user_n * 3 + slot_n, 35) DAY
                 + INTERVAL MOD(user_n + slot_n, 9) HOUR AS enrolled_at
         FROM tmp_enrollment_seed
     ) e;

COMMIT;

-- temp cleanup
DROP TEMPORARY TABLE IF EXISTS tmp_enrollment_seed;
DROP TEMPORARY TABLE IF EXISTS tmp_seq_c;
DROP TEMPORARY TABLE IF EXISTS tmp_seq_b;
DROP TEMPORARY TABLE IF EXISTS tmp_seq;

-- ============================================================
-- 5) validation SELECTs (NON-RECOMMEND)
-- ============================================================

-- A) expected counts
SELECT metric, actual, expected, (actual = expected) AS ok
FROM (
         SELECT 'users' AS metric, COUNT(*) AS actual, 70 AS expected
         FROM `users` WHERE `id` BETWEEN 110001 AND 110070

         UNION ALL
         SELECT 'user_roles', COUNT(*), 75
         FROM `user_roles` WHERE `user_id` BETWEEN 110001 AND 110070

         UNION ALL
         SELECT 'user_tags', COUNT(*), 248
         FROM `user_tags` WHERE `user_id` BETWEEN 110001 AND 110070

         UNION ALL
         SELECT 'course', COUNT(*), 50
         FROM `course` WHERE `id` BETWEEN 120001 AND 120050

         UNION ALL
         SELECT 'course_tags', COUNT(*), 176
         FROM `course_tags` WHERE `course_id` BETWEEN 120001 AND 120050

         UNION ALL
         SELECT 'section', COUNT(*), 72
         FROM `section` WHERE `id` BETWEEN 130001 AND 130600

         UNION ALL
         SELECT 'lecture', COUNT(*), 183
         FROM `lecture` WHERE `id` BETWEEN 140001 AND 143500

         UNION ALL
         SELECT 'enrollment', COUNT(*), 108
         FROM `enrollment` WHERE `id` BETWEEN 150001 AND 151000
     ) t
ORDER BY metric;

-- B) role distribution
SELECT `role`, COUNT(*) AS cnt
FROM `user_roles`
WHERE `user_id` BETWEEN 110001 AND 110070
GROUP BY `role`
ORDER BY cnt DESC, `role`;

-- C) tags per user distribution
SELECT tag_cnt, COUNT(*) AS user_cnt
FROM (
         SELECT `user_id`, COUNT(*) AS tag_cnt
         FROM `user_tags`
         WHERE `user_id` BETWEEN 110001 AND 110070
         GROUP BY `user_id`
     ) x
GROUP BY tag_cnt
ORDER BY tag_cnt DESC;

-- D) section density by course
SELECT
    CASE
        WHEN section_cnt = 0 THEN '0'
        WHEN section_cnt = 1 THEN '1'
        WHEN section_cnt BETWEEN 2 AND 3 THEN '2-3'
        ELSE '4+'
        END AS section_bucket,
    COUNT(*) AS course_cnt
FROM (
         SELECT c.`id`, COUNT(s.`id`) AS section_cnt
         FROM `course` c
                  LEFT JOIN `section` s ON s.`course_id` = c.`id`
         WHERE c.`id` BETWEEN 120001 AND 120050
         GROUP BY c.`id`
     ) t
GROUP BY section_bucket
ORDER BY section_bucket;

-- E) top / bottom courses by enrollment
SELECT e.`course_id`, COUNT(*) AS enroll_cnt
FROM `enrollment` e
WHERE e.`id` BETWEEN 150001 AND 151000
GROUP BY e.`course_id`
ORDER BY enroll_cnt DESC, e.`course_id`
LIMIT 10;

SELECT e.`course_id`, COUNT(*) AS enroll_cnt
FROM `enrollment` e
WHERE e.`id` BETWEEN 150001 AND 151000
GROUP BY e.`course_id`
ORDER BY enroll_cnt ASC, e.`course_id`
LIMIT 10;

-- F) enrollment status distribution
SELECT `enrollment_status`, COUNT(*) AS cnt
FROM `enrollment`
WHERE `id` BETWEEN 150001 AND 151000
GROUP BY `enrollment_status`
ORDER BY cnt DESC, `enrollment_status`;

-- G) learner activity bucket
SELECT
    CASE
        WHEN enroll_cnt >= 5 THEN '5+'
        WHEN enroll_cnt >= 3 THEN '3-4'
        WHEN enroll_cnt = 2 THEN '2'
        ELSE '1'
        END AS learner_load_bucket,
    COUNT(*) AS learner_count
FROM (
         SELECT `user_id`, COUNT(*) AS enroll_cnt
         FROM `enrollment`
         WHERE `id` BETWEEN 150001 AND 151000
         GROUP BY `user_id`
     ) t
GROUP BY learner_load_bucket
ORDER BY FIELD(learner_load_bucket, '1', '2', '3-4', '5+');

-- H) integrity checks (모두 0이어야 정상)
SELECT 'orphan_user_roles' AS check_name, COUNT(*) AS fail_count
FROM `user_roles` ur
         LEFT JOIN `users` u ON u.`id` = ur.`user_id`
WHERE ur.`user_id` BETWEEN 110001 AND 110070
  AND u.`id` IS NULL

UNION ALL
SELECT 'orphan_user_tags', COUNT(*)
FROM `user_tags` ut
         LEFT JOIN `users` u ON u.`id` = ut.`user_id`
WHERE ut.`user_id` BETWEEN 110001 AND 110070
  AND u.`id` IS NULL

UNION ALL
SELECT 'orphan_course_tags', COUNT(*)
FROM `course_tags` ct
         LEFT JOIN `course` c ON c.`id` = ct.`course_id`
WHERE ct.`course_id` BETWEEN 120001 AND 120050
  AND c.`id` IS NULL

UNION ALL
SELECT 'orphan_sections', COUNT(*)
FROM `section` s
         LEFT JOIN `course` c ON c.`id` = s.`course_id`
WHERE s.`id` BETWEEN 130001 AND 130600
  AND c.`id` IS NULL

UNION ALL
SELECT 'orphan_lectures', COUNT(*)
FROM `lecture` l
         LEFT JOIN `section` s ON s.`id` = l.`section_id`
WHERE l.`id` BETWEEN 140001 AND 143500
  AND s.`id` IS NULL

UNION ALL
SELECT 'enrollment_missing_user', COUNT(*)
FROM `enrollment` e
         LEFT JOIN `users` u ON u.`id` = e.`user_id`
WHERE e.`id` BETWEEN 150001 AND 151000
  AND u.`id` IS NULL

UNION ALL
SELECT 'enrollment_missing_course', COUNT(*)
FROM `enrollment` e
         LEFT JOIN `course` c ON c.`id` = e.`course_id`
WHERE e.`id` BETWEEN 150001 AND 151000
  AND c.`id` IS NULL;

-- I) cancellation consistency
SELECT
    SUM(`enrollment_status` = 'CANCELLED' AND (`cancelled_at` IS NULL OR `cancel_type` IS NULL OR `cancel_reason_type` IS NULL)) AS invalid_cancelled_rows,
    SUM(`enrollment_status` <> 'CANCELLED' AND (`cancelled_at` IS NOT NULL OR `cancel_type` IS NOT NULL OR `cancel_reason_type` IS NOT NULL)) AS invalid_non_cancelled_rows,
    SUM(`cancel_reason_type` = 'OTHER' AND (`cancel_reason_comment` IS NULL OR TRIM(`cancel_reason_comment`) = '')) AS invalid_other_reason_comment_rows
FROM `enrollment`
WHERE `id` BETWEEN 150001 AND 151000;

-- J) date consistency
SELECT
    SUM(`learning_started_at` IS NOT NULL AND `learning_started_at` < `enrolled_at`) AS invalid_learning_started_before_enrolled,
    SUM(`completed_at` IS NOT NULL AND `completed_at` < `enrolled_at`) AS invalid_completed_before_enrolled,
    SUM(`cancelled_at` IS NOT NULL AND `cancelled_at` < `enrolled_at`) AS invalid_cancelled_before_enrolled,
    SUM(`deleted_at` IS NOT NULL AND `deleted_at` < `enrolled_at`) AS invalid_deleted_before_enrolled,
    SUM(`completed_at` IS NOT NULL AND `cancelled_at` IS NOT NULL) AS invalid_completed_and_cancelled_both_set
FROM `enrollment`
WHERE `id` BETWEEN 150001 AND 151000;
