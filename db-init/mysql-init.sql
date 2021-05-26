--
-- База данных: `mysqldb`
--

-- --------------------------------------------------------

--
-- Структура таблицы `coauthors`
--

CREATE DATABASE IF NOT EXISTS mysqldb;

USE mysqldb;

CREATE TABLE `coauthors_student`
(
    `publication_id` int(32) NOT NULL,
    `student_id`      int(32) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO `coauthors_student` (`publication_id`, `student_id`)
VALUES (1, 1);

CREATE TABLE `coauthors_teacher`
(
    `publication_id` int(32) NOT NULL,
    `teacher_id`     int(32) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO `coauthors_teacher` (`publication_id`, `teacher_id`)
VALUES (1, 1);

-- --------------------------------------------------------

--
-- Структура таблицы `conferences`
--

CREATE TABLE `conferences`
(
    `id`              int(32)                                NOT NULL,
    `name`            varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `place`           varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `conference_date` date                                   NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO `conferences` (id, name, place, conference_date)
VALUES (1, 'Конференция по литературе', 'Санкт-Петербург', '2021-05-30');

-- --------------------------------------------------------

--
-- Структура таблицы `conferences_student`
--

CREATE TABLE `conferences_student`
(
    `student_id`     int(32) NOT NULL,
    `conference_id` int(32) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO `conferences_student` (`student_id`, `conference_id`)
VALUES ('1', '1');

-- --------------------------------------------------------

--
-- Структура таблицы `conferences_teacher`
--

CREATE TABLE `conferences_teacher`
(
    `teacher_id`     int(32) NOT NULL,
    `conference_id` int(32) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO `conferences_teacher` (`teacher_id`, `conference_id`)
VALUES ('1', '1');

-- --------------------------------------------------------

--
-- Структура таблицы `library_card`
--

CREATE TABLE `library_card`
(
    `id`           int(32)                                NOT NULL,
    `book_name`    varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
    `receive_date` date                                   NOT NULL,
    `return_date`  date                                   NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO `library_card` (`id`, `book_name`, `receive_date`, `return_date`)
VALUES (1, 'Война и мир', '2021-04-01', '2021-04-30');


-- --------------------------------------------------------

--
-- Структура таблицы `students`
--

CREATE TABLE `students`
(
    `student_id`       int(32)                                NOT NULL,
    `name`             varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
    `student_position` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO `students` (`student_id`, `name`, `student_position`)
VALUES (1, 'Иванов Иван Иванович', 'Студент');


-- --------------------------------------------------------

--
-- Структура таблицы `teachers`
--

CREATE TABLE `teachers`
(
    `teacher_id`       int(32)                                NOT NULL,
    `name`             varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
    `teacher_position` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO `teachers` (`teacher_id`, `name`, `teacher_position`)
VALUES (1, 'Иванов Иван Иванович', 'Доцент');


-- --------------------------------------------------------

--
-- Структура таблицы `projects`
--

CREATE TABLE `projects`
(
    id         int(32)                                 NOT NULL,
    name       varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
    start_date date                                    NOT NULL,
    end_date   date                                    NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO `projects` (id, name, start_date, end_date)
VALUES (1, 'Исследование книги \"Война и мир\"', '2021-04-01', '2021-05-31');



-- --------------------------------------------------------

--
-- Структура таблицы `project_student`
--

CREATE TABLE `project_student`
(
    `student_id`  int(32) NOT NULL,
    `project_id` int(32) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO `project_student` (`student_id`, `project_id`)
VALUES (1, 1);



-- --------------------------------------------------------

--
-- Структура таблицы `project_teacher`
--

CREATE TABLE `project_teacher`
(
    `teacher_id`  int(32) NOT NULL,
    `project_id` int(32) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO `project_teacher` (`teacher_id`, `project_id`)
VALUES (1, 1);



-- --------------------------------------------------------

--
-- Структура таблицы `publications`
--

CREATE TABLE `publications`
(
    `id`               int(32)                                 NOT NULL,
    `name`             varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
    `edition_lang`     varchar(20) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `edition_vol`      int(10)                                 NOT NULL,
    `edition_place`    varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
    `edition_type`     varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
    `citation_index`   int(10)                                 NOT NULL,
    `publication_date` date                                    NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO `publications` (id, name, `edition_lang`, `edition_vol`, `edition_place`,
                            `edition_type`, `citation_index`, `publication_date`)
VALUES (1, 'Статья об исследовании книги \"Война и мир\"', 'Русский', 1, 'Санкт-Петербург', 'Научное', 2, '2021-05-31');



--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `coauthors_student`
--
ALTER TABLE `coauthors_student`
    ADD KEY `publication_id` (`publication_id`),
    ADD KEY `student_id` (`student_id`);

--
-- Индексы таблицы `coauthors_teacher`
--
ALTER TABLE `coauthors_teacher`
    ADD KEY `publication_id` (`publication_id`),
    ADD KEY `teacher_id` (`teacher_id`);

--
-- Индексы таблицы `conferences`
--
ALTER TABLE `conferences`
    ADD PRIMARY KEY (id);

--
-- Индексы таблицы `conferences_student`
--
ALTER TABLE `conferences_student`
    ADD KEY `student_id` (`student_id`),
    ADD KEY `conference_id` (`conference_id`);

--
-- Индексы таблицы `conferences_teacher`
--
ALTER TABLE `conferences_teacher`
    ADD KEY `teacher_id` (`teacher_id`),
    ADD KEY `conference_id` (`conference_id`);

--
-- Индексы таблицы `library_card`
--
ALTER TABLE `library_card`
    ADD KEY `student_id` (id);

--
-- Индексы таблицы `students`
--
ALTER TABLE `students`
    ADD PRIMARY KEY (`student_id`);

--
-- Индексы таблицы `teachers`
--
ALTER TABLE `teachers`
    ADD PRIMARY KEY (`teacher_id`);

--
-- Индексы таблицы `projects`
--
ALTER TABLE `projects`
    ADD PRIMARY KEY (id);

--
-- Индексы таблицы `project_student`
--
ALTER TABLE `project_student`
    ADD KEY `student_id` (`student_id`),
    ADD KEY `project_id` (`project_id`) USING BTREE;

--
-- Индексы таблицы `project_teacher`
--
ALTER TABLE `project_teacher`
    ADD KEY `teacher_id` (`teacher_id`),
    ADD KEY `project_id` (`project_id`) USING BTREE;

--
-- Индексы таблицы `publications`
--
ALTER TABLE `publications`
    ADD PRIMARY KEY (id);

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `coauthors_student`
--
ALTER TABLE `coauthors_student`
    ADD CONSTRAINT `coauthors_student_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`),
    ADD CONSTRAINT `coauthors_student_ibfk_2` FOREIGN KEY (`publication_id`) REFERENCES `publications` (id);

--
-- Ограничения внешнего ключа таблицы `coauthors_teacher`
--
ALTER TABLE `coauthors_teacher`
    ADD CONSTRAINT `coauthors_teacher_ibfk_1` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`teacher_id`),
    ADD CONSTRAINT `coauthors_teacher_ibfk_2` FOREIGN KEY (`publication_id`) REFERENCES `publications` (id);

--
-- Ограничения внешнего ключа таблицы `conferences_student`
--
ALTER TABLE `conferences_student`
    ADD CONSTRAINT `conferences_student_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`),
    ADD CONSTRAINT `conferences_student_ibfk_2` FOREIGN KEY (`conference_id`) REFERENCES `conferences` (id);

--
-- Ограничения внешнего ключа таблицы `conferences_student`
--
ALTER TABLE `conferences_teacher`
    ADD CONSTRAINT `conferences_teacher_ibfk_1` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`teacher_id`),
    ADD CONSTRAINT `conferences_teacher_ibfk_2` FOREIGN KEY (`conference_id`) REFERENCES `conferences` (id);

--
-- Ограничения внешнего ключа таблицы `library_card`
--
ALTER TABLE `library_card`
    ADD CONSTRAINT `library_card_ibfk_1` FOREIGN KEY (id) REFERENCES `students` (`student_id`);

--
-- Ограничения внешнего ключа таблицы `project_student`
--
ALTER TABLE `project_student`
    ADD CONSTRAINT `project_student_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`),
    ADD CONSTRAINT `project_student_ibfk_2` FOREIGN KEY (`project_id`) REFERENCES `projects` (id);

--
-- Ограничения внешнего ключа таблицы `project_teacher`
--
ALTER TABLE `project_teacher`
    ADD CONSTRAINT `project_teacher_ibfk_1` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`teacher_id`),
    ADD CONSTRAINT `project_teacher_ibfk_2` FOREIGN KEY (`project_id`) REFERENCES `projects` (id);
COMMIT;
