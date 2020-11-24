CREATE TABLE `arxiv_paper_info` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(1024) DEFAULT NULL,
  `authors` varchar(1024) DEFAULT NULL,
  `month` int DEFAULT NULL,
  `year` int DEFAULT NULL,
  `subjects` varchar(1024) DEFAULT NULL,
  `abstract` text,
  `citation` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='simple paper info from arxiv';