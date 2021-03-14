CREATE TABLE `globalurls` (
  `hostedURL` varchar(40) NOT NULL,
  `redirectURL` varchar(150) NOT NULL,
  PRIMARY KEY (`hostedURL`),
  UNIQUE KEY `hostedURL_UNIQUE` (`hostedURL`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

