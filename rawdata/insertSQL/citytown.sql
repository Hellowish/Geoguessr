-- 使用 geoguessr 資料庫
USE geoguessr;

-- Changhua.csv
CREATE TABLE Changhua (
    id INT AUTO_INCREMENT PRIMARY KEY,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL
);

LOAD DATA INFILE 'C:/Users/Administrator/Desktop/rawdata/split_data_city/Changhua.csv'
INTO TABLE Changhua
FIELDS TERMINATED BY ',' 
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(latitude, longitude);

-- Chiayi_City.csv
CREATE TABLE Chiayi_City (
    id INT AUTO_INCREMENT PRIMARY KEY,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL
);

LOAD DATA INFILE 'C:/Users/Administrator/Desktop/rawdata/split_data_city/Chiayi_City.csv'
INTO TABLE Chiayi_City
FIELDS TERMINATED BY ',' 
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(latitude, longitude);

-- Chiayi_County.csv
CREATE TABLE Chiayi_County (
    id INT AUTO_INCREMENT PRIMARY KEY,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL
);

LOAD DATA INFILE 'C:/Users/Administrator/Desktop/rawdata/split_data_city/Chiayi_County.csv'
INTO TABLE Chiayi_County
FIELDS TERMINATED BY ',' 
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(latitude, longitude);

-- Hsinchu_City.csv
CREATE TABLE Hsinchu_City (
    id INT AUTO_INCREMENT PRIMARY KEY,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL
);

LOAD DATA INFILE 'C:/Users/Administrator/Desktop/rawdata/split_data_city/Hsinchu_City.csv'
INTO TABLE Hsinchu_City
FIELDS TERMINATED BY ',' 
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(latitude, longitude);

-- Hsinchu_County.csv
CREATE TABLE Hsinchu_County (
    id INT AUTO_INCREMENT PRIMARY KEY,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL
);

LOAD DATA INFILE 'C:/Users/Administrator/Desktop/rawdata/split_data_city/Hsinchu_County.csv'
INTO TABLE Hsinchu_County
FIELDS TERMINATED BY ',' 
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(latitude, longitude);

-- Hualien.csv
CREATE TABLE Hualien (
    id INT AUTO_INCREMENT PRIMARY KEY,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL
);

LOAD DATA INFILE 'C:/Users/Administrator/Desktop/rawdata/split_data_city/Hualien.csv'
INTO TABLE Hualien
FIELDS TERMINATED BY ',' 
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(latitude, longitude);

-- Kaohsiung.csv
CREATE TABLE Kaohsiung (
    id INT AUTO_INCREMENT PRIMARY KEY,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL
);

LOAD DATA INFILE 'C:/Users/Administrator/Desktop/rawdata/split_data_city/Kaohsiung.csv'
INTO TABLE Kaohsiung
FIELDS TERMINATED BY ',' 
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(latitude, longitude);

-- Keelung.csv
CREATE TABLE Keelung (
    id INT AUTO_INCREMENT PRIMARY KEY,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL
);

LOAD DATA INFILE 'C:/Users/Administrator/Desktop/rawdata/split_data_city/Keelung.csv'
INTO TABLE Keelung
FIELDS TERMINATED BY ',' 
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(latitude, longitude);

-- Kinmen.csv
CREATE TABLE Kinmen (
    id INT AUTO_INCREMENT PRIMARY KEY,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL
);

LOAD DATA INFILE 'C:/Users/Administrator/Desktop/rawdata/split_data_city/Kinmen.csv'
INTO TABLE Kinmen
FIELDS TERMINATED BY ',' 
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(latitude, longitude);

-- Lienchiang.csv
CREATE TABLE Lienchiang (
    id INT AUTO_INCREMENT PRIMARY KEY,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL
);

LOAD DATA INFILE 'C:/Users/Administrator/Desktop/rawdata/split_data_city/Lienchiang.csv'
INTO TABLE Lienchiang
FIELDS TERMINATED BY ',' 
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(latitude, longitude);

-- Miaoli.csv
CREATE TABLE Miaoli (
    id INT AUTO_INCREMENT PRIMARY KEY,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL
);

LOAD DATA INFILE 'C:/Users/Administrator/Desktop/rawdata/split_data_city/Miaoli.csv'
INTO TABLE Miaoli
FIELDS TERMINATED BY ',' 
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(latitude, longitude);

-- Nantou.csv
CREATE TABLE Nantou (
    id INT AUTO_INCREMENT PRIMARY KEY,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL
);

LOAD DATA INFILE 'C:/Users/Administrator/Desktop/rawdata/split_data_city/Nantou.csv'
INTO TABLE Nantou
FIELDS TERMINATED BY ',' 
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(latitude, longitude);

-- NewTaipei.csv
CREATE TABLE NewTaipei (
    id INT AUTO_INCREMENT PRIMARY KEY,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL
);

LOAD DATA INFILE 'C:/Users/Administrator/Desktop/rawdata/split_data_city/NewTaipei.csv'
INTO TABLE NewTaipei
FIELDS TERMINATED BY ',' 
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(latitude, longitude);

-- Penghu.csv
CREATE TABLE Penghu (
    id INT AUTO_INCREMENT PRIMARY KEY,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL
);

LOAD DATA INFILE 'C:/Users/Administrator/Desktop/rawdata/split_data_city/Penghu.csv'
INTO TABLE Penghu
FIELDS TERMINATED BY ',' 
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(latitude, longitude);

-- Pingtung.csv
CREATE TABLE Pingtung (
    id INT AUTO_INCREMENT PRIMARY KEY,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL
);

LOAD DATA INFILE 'C:/Users/Administrator/Desktop/rawdata/split_data_city/Pingtung.csv'
INTO TABLE Pingtung
FIELDS TERMINATED BY ',' 
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(latitude, longitude);
